//This Class represents the List of LO MetaData entity scrapped from ARIADNE repository

package data_mining_project.core;

import java.util.*;

public class LO_Metadata {
	
	private List<LO_Metadata> collection= new LinkedList<LO_Metadata>();
	private String title;
	private List<String> keywords;
	private String description;
	private String rights;
	private Set<String> labels;
	private Set<String> keyword_labels;
	
	
	
	// Class Constructors

	public LO_Metadata(){}
	
	public LO_Metadata (String title, List<String> keywords, String description, String rights){
		this.title = title;
		this.keywords = keywords;
		this.description = description;
		this.rights = rights;
		this.labels = new TreeSet<String>();
		this.keyword_labels = new TreeSet<String>(); 
		make_keyword_labels(keywords);	
	}

	
	
	//Read The LOs and merge all of them to the list, Then Print them (Read_Data ClasS)
	
	public void add_LOM_instance(LO_Metadata item){
		
		collection.add(item);
	}

	public void merge_collection(LO_Metadata lo){
		
		List<LO_Metadata> lo_list  = lo.get_lo_list();
		
		for (LO_Metadata item: lo_list)
			collection.add(item);
	}
	
	public void print(){
		
		int i=1;
		for(LO_Metadata item : collection){
			
			System.out.println(i++);
			System.out.println(item.get_title());
			System.out.println(item.get_description());
			System.out.println(item.get_keywords());
			System.out.println(item.get_rights());
			System.out.println("--------------");
		}
	}
	
	
	//get Methods
	
	public String get_title(){
		return title;
	}
	
	public List<String> get_keywords(){
		return keywords;
	}
	
	public String get_description(){
		return description;
	}
	
	public String get_rights(){
		return rights;
	}
	
	public Set<String> get_labels(){
		return labels;
	}
	
	public Set<String> get_keyword_labels(){
		
		return keyword_labels;
	}
	
	
	
	//Clean_Data functionality: Equals and Duplicate and RemoveAll Methods (Clean_Data Class)

	public boolean equals(LO_Metadata lo){
		
		return  this.title.equals(lo.get_title());
	}
	
	public List<LO_Metadata> get_duplicates(LO_Metadata lom){
		
		List<LO_Metadata> dup_list = new LinkedList<LO_Metadata>();
		
		for(LO_Metadata lo: collection){
			
			if (lo.equals(lom) && lo != lom)
				dup_list.add(lo);	
		}
				
		return dup_list;
	}
	
	public void remove_all(List<LO_Metadata> lom_lst){
		
		collection.removeAll(lom_lst);	
	}
	
	
	
	// Add labels Methods
	
	public void add_label(String label){
		this.labels.add(label);
	}
	
	public void add_labels(Set<String> la){
		this.labels.addAll(la);
	}
	
	public void add_label_to_list(String label){
		
		for (LO_Metadata item : collection)
			item.add_label(label);		
	}
	
	
	// Print all the LOS (Tabulate_Data Class)
	
	public String toString(){
		
		return "-------------------"+"\n"+	
				"labels \t\t:"+labels+"\n"+
			    title+ "\n" +
			    description+ "\n" +
			    keywords+	"\n"+
			    rights +"\n";
	}
	
	
	// Return the list of all LOs
	public List<LO_Metadata> get_lo_list(){
		
		return collection;
	}
	
	
	
	//Formatting the Keywords
	
    private void make_keyword_labels(List<String> lst){
    	
    	String formated_label =null;
		
		for(String e: lst){
			
			formated_label = format_label(e);
			keyword_labels.add(formated_label);
		}	
	}
	
    private String format_label(String str){
		
		str = str.trim().toUpperCase();
		
		str = str.replace("\"", "");
		str = str.replace(".", "");
		str = str.replace(":", "");
		str = str.replace(";", "");
		str = str.replace("!", "");
		str = str.replace("&", "");
		str = str.replace("\'", "");
		str = str.replace("#", "");
		str = str.replace("(", "");
		str = str.replace(")", "");
		str = str.replace("{", "");
		str = str.replace("}", "");
		str = str.replace("?", "");
		str = str.replace("\\", "");
		str = str.replace("  ", " ");
		str = str.replace("--", " ");
		str.trim();
		str = str.replace(" ", "_");
		
		return str;
	}

}
