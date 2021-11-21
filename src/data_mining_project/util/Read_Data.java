package data_mining_project.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

import data_mining_project.core.LO_Metadata;

public class Read_Data {
	
	private Path INPUT_DIR = FileSystems.getDefault().getPath("Raw_Data");
	
	/* main method here for testing read_data functionality */
	
	public static void main(String[] args){
		
		new Read_Data().get_LOM_collection().print();
	}
	
	
	public LO_Metadata get_LOM_collection(){
		
		List<Path> list = get_file_list();
		LO_Metadata super_set= new LO_Metadata();
		LO_Metadata cur_set=null;
		
		for(Path item : list){
			
			try {
				cur_set = process_file(item);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			
			
			cur_set.add_label_to_list(item.getFileName().toString().replace(".txt", ""));
			super_set.merge_collection(cur_set);
		}
		
		return super_set;
			
	}
	
	
	private List<Path> get_file_list(){
		
		List<Path> list = new LinkedList<Path>();
		
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(INPUT_DIR)) {
		    for (Path file: stream) 
		        list.add(file);
		    
		} catch (IOException | DirectoryIteratorException x) {
		    System.err.println(x);
		}
		
		return list;
		
	}
	
	private LO_Metadata process_file(Path file) throws IOException{
		
		LO_Metadata lo = new LO_Metadata();
		
		BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8);
		
		int var =1;		
		
		String line="",
			   title="",
			   description="",
			   rights="",
			   keywords="";
				
		
		while((line = reader.readLine())!= null){
			
			switch(var){
			
			case 1:
				if(line.contentEquals("")) break;
				else title = line;
				var = 2; break;
				
				
			case 2:
				description = line;
				var = 3; break;
			
				
			case 3:
				 if (line.startsWith("Keywords:")){
				keywords = line.replace("Keywords", "");
				var=4;
				break;
				}
				else{
					description+= line;
					break;
				}
				
				 
			case 4:
				if(line.contentEquals("")|| line.contentEquals(" ")){
					var= 1;
					rights ="";
					lo.add_LOM_instance(new LO_Metadata( title, parse_tags(keywords), description,rights));
					break;
				}
				else if (line.startsWith("Rights:")){
					rights = line.replace("Rights:", "");
					lo.add_LOM_instance(new LO_Metadata( title, parse_tags(keywords), description,rights));
					var = 1;
					break;
				}
				else{
					keywords+= line;
					var = 4;
					break;
				}
			
			}
			
		}
		
		if (var == 4)
			lo.add_LOM_instance(new LO_Metadata( title, parse_tags(keywords), description,""));
		
		return lo;
		
		
	}
	
	private List<String> parse_tags(String keywords){
		
		StringTokenizer st = new StringTokenizer(keywords, ",");
		List<String> list = new LinkedList<String>();
		
		while(st.hasMoreTokens())
			list.add(st.nextToken());
		
		return list;
		
	}
	

}
