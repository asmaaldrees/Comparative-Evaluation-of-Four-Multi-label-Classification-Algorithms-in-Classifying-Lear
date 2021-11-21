package data_mining_project.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

import data_mining_project.core.*;

public class Print_Keywords {
	
	public void print(LO_Metadata lo){
	
		Path path = FileSystems.getDefault().getPath("Label_Categories", "keywordlist.txt");
		
		
	     BufferedWriter writer = null;
		try {
			writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	     PrintWriter print_writer = new PrintWriter(writer);
	     List<LO_Metadata> lom_list = lo.get_lo_list();
	     Set<String> keyword_set = new TreeSet<String>();
	     
	     
	     for(LO_Metadata lom: lom_list){
	    	 
	    	keyword_set.addAll(lom.get_keyword_labels()); 
	     }
	     
	     for(String s: keyword_set){
	    	 print_writer.println(s);
	     }
	     print_writer.flush();
	     print_writer.close();
	 
		
	}
	
	public static void main(String[] args){
		
		LO_Metadata loc = new Read_Data().get_LOM_collection();
		new Print_Keywords().print(loc);
	}

}
