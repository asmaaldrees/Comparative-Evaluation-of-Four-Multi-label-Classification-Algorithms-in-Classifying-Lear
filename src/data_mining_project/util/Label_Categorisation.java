package data_mining_project.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import data_mining_project.core.LO_Metadata;

public class Label_Categorisation {
	
	private  Path INPUT_FILE = FileSystems.getDefault().getPath("Label_Categories", "Categories.xml");
	private List<Category> categories;
	
	public Label_Categorisation(){
		categories = new LinkedList<Category>();
		try {
		read_categorisations();
		    }
		catch (ParserConfigurationException | SAXException | IOException e) {
		e.printStackTrace();
		}
	} 
	
	
	private void read_categorisations() throws ParserConfigurationException, SAXException, IOException{
		
		File xml_file = INPUT_FILE.toFile();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(xml_file);
		
		doc.getDocumentElement();
		
		NodeList node_list = doc.getElementsByTagName("categories"),
				 child_list = null;
		
		Node temp_node = null;
		Category current_category = null;
		
				
		for(int i=0; i < node_list.getLength(); i++){
			
			temp_node =  node_list.item(i);
			child_list = temp_node.getChildNodes();
			current_category = new Category(((Element)temp_node).getAttribute("name"));
			
			categories.add(current_category);
			
			for(int j=0; j <child_list.getLength(); j++){
				
				temp_node = child_list.item(j);
				if (temp_node.getNodeType() == Node.ELEMENT_NODE) {
					current_category.add_keyword(((Element)temp_node).getAttribute("name"));
				}
			}
		}			

	}
	
public void update_labels(LO_Metadata loc){
		
		List<LO_Metadata> lom_list = loc.get_lo_list();
		Set<String> temp_labels = null;
		
		for(LO_Metadata lom: lom_list){
			
			temp_labels = get_labels(lom);
			lom.add_labels(temp_labels);
		}
		
		
	}
	
	private Set<String> get_labels(LO_Metadata lom){
		
		Set<String> words = lom.get_keyword_labels();
		Set<String> labels = new TreeSet<String>();
		
		
		for(String w: words){
			
			for(Category cat: categories){
				
				if(cat.has_keyword(w))
					labels.add(cat.get_name());
			}
		}
		
		return labels;
	}
	
}


class Category {

	private String name = null;
	private Set<String> keywords;
	
	public Category(String name){
		this.name = name;
		keywords = new TreeSet<String>();
	}
	
	public void add_keyword(String keyword){
		
		keywords.add(keyword);
	}
	
	public String get_name(){
		return name;
	}
	
	public boolean has_keyword(String keyword){
		
		return keywords.contains(keyword);
	}
	

}
