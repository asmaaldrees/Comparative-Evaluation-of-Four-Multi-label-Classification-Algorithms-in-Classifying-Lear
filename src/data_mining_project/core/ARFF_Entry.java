//This file represents and ARFF entry, each LO_Metadata object is transformed into an ARFF entry.

package data_mining_project.core;

import java.util.*;

public class ARFF_Entry {
	
	private Set<String> attributes;
	private List<Integer>   arff_attribute_no_list; // attribute number list 
	
	
	public ARFF_Entry(LO_Metadata lom){
		
		attributes = new HashSet<String>();
		arff_attribute_no_list = new LinkedList<Integer>();
		
		add_title(lom.get_title());
		add_description(lom.get_description());
		add_keywords(lom.get_keywords());
		add_rights(lom.get_rights());
		add_labels(lom.get_labels());
	}
	
	private void add_labels(Set<String> labels){
		
		for(String l:labels)
			attributes.add(l);
	}
	
	public boolean contains_attribute(String attr){
		
		return attributes.contains(attr);
	}
	
	public List<Integer> get_attribute_numers(){
		return arff_attribute_no_list;
	}
	
	public Set<String> get_attributes(){
		
		return attributes;
	}
	
	
	public void add_attribute_no(int n){
		arff_attribute_no_list.add(new Integer(n));
	}
	
	private void add_title(String title){
		
		List<String> lst = get_word_list(title);
		for(String i : lst)
			attributes.add(i); 
		
		
	}
	
	private void add_description(String desc){
		
		List<String> lst = get_word_list(desc);
		for(String i : lst)
			attributes.add(i); 
		
	}
	
	private void add_keywords(List<String> keywords){
		
		List<String> lst;
		
		for(String k: keywords){			
			lst = get_word_list(k);
			for(String i : lst)
				attributes.add(i);
		}
			
		
	}
	

	
	private void add_rights(String rights){
		
		if (rights == null || rights.equals("")) return;
		
		List<String> lst = get_word_list(rights);
		for(String i : lst)
			attributes.add(i);
		
	}
	
	private List<String> get_word_list(String str){
		
		List<String> lst = new LinkedList<String>();
		str = str.trim().toLowerCase();
		
		str = str.replace(" - ", " ");
		str = str.replace("- ", " ");		
		str = str.replace("\"", "");
		str = str.replace(",", "");
		str = str.replace(".", "");
		str = str.replace(":", "");
		str = str.replace(";", "");
		str = str.replace("!", "");
		str = str.replace("\'", "");
		str = str.replace("#", "");
		str = str.replace("(", "");
		str = str.replace(")", "");
		str = str.replace("{", "");
		str = str.replace("}", "");
		str = str.replace("?", "");
		str = str.replace("\\", "");
        str = str.replace("&", "");
        str = str.replace("*", "");
        str = str.replace("--", "");
        str = str.replace("/ ", "");
		str = str.replace("  ", " ");
		str = str.trim();
		
		StringTokenizer st = new StringTokenizer(str, " ");
		
		while(st.hasMoreTokens())
			lst.add(st.nextToken());
		
		return lst;
	}	
}
