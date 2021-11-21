package data_mining_project.util;

import data_mining_project.core.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class Generate_ARFF {
	
	private LO_Metadata collection;
	private SortedSet<String> attributes;
	private SortedSet<String> labels;
	private List<ARFF_Entry> entry_list;
	
	public Generate_ARFF(LO_Metadata lo){
		
		collection = lo;
		attributes = new TreeSet<String>();
		labels	   = new TreeSet<String>();
		entry_list = new LinkedList<ARFF_Entry>();
		
	}
	
	
	public int get_attributes_no(){
		int a= attributes.size()-labels.size();
		return a;
	}
	
	
	public void produce_label_file()throws IOException{
		
		make_label_list();
		
		Path path = FileSystems.getDefault().getPath(“Mulan_input_files", "Ariadne.xml");
		BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
		PrintWriter pw = new PrintWriter(writer);
		
		pw.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		pw.println("<labels xmlns=\"http://mulan.sourceforge.net/labels\">");
		
		Iterator<String> it = labels.iterator();
						
		while(it.hasNext())
			pw.println("<label name= \""+ it.next()+"\"></label>");
		
		pw.println("</labels>");
		
		pw.flush();
		pw.close();
				
	}
	
	
	private void make_label_list(){
		
		List <LO_Metadata> lom_lst = collection.get_lo_list();
		
		for(LO_Metadata lom: lom_lst)
			labels.addAll(lom.get_labels());
	}
	
	
	
	public void produce_arff_file() throws IOException{
		
		make_arff_list();
		merge_all_attributes();
		update_attribute_no_list();
		
		
		
		Path path = FileSystems.getDefault().getPath(“Mulan_input_files", "Ariadne.arff");
		BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
		PrintWriter pw = new PrintWriter(writer);
		
		pw.println("@relation  ARIADNE_Repository \n");
		
		Iterator<String> it = attributes.iterator();
		
		while (it.hasNext())
			pw.println("@attribute\t"+it.next()+"\t{0,1}");
		
		pw.println();
		pw.println("@data");
		
		List<Integer> attr_numbers = null;
		for(ARFF_Entry e: entry_list){
			pw.print("{");
			attr_numbers = e.get_attribute_numers();
			boolean first = true;
			for(Integer i: attr_numbers){
				if(first==false) pw.print(",");
				pw.print(i+" "+1);
				first = false;
			}
			pw.println("}");
			
		}
		
		pw.flush();
		pw.close();
		

	}
	private void make_arff_list(){
		
		List <LO_Metadata> lom_lst = collection.get_lo_list();
		
		for(LO_Metadata e: lom_lst )
			entry_list.add(new ARFF_Entry(e));
		
		
	}
	
	private void merge_all_attributes(){
		
		for(ARFF_Entry e: entry_list)
				attributes.addAll(e.get_attributes());
		
	}
	private void update_attribute_no_list(){
		
		for(ARFF_Entry e: entry_list)
		{
			Iterator<String> it = attributes.iterator();
		int no =0;
		
		while(it.hasNext()){
			
			if(e.contains_attribute(it.next()))
				e.add_attribute_no(no);
			
			no++;
		}
	}
		} 
}
