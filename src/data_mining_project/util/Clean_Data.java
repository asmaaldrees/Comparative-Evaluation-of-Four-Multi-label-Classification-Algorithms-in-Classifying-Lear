//Removing all the duplicates,and merge all the labels into one entry

package data_mining_project.util;

import java.util.*;
import data_mining_project.core.LO_Metadata;

public class Clean_Data {
	
	public Clean_Data(LO_Metadata collection){
		
		List<LO_Metadata> lo_list = collection.get_lo_list();
		List<LO_Metadata> all_dups = new LinkedList<LO_Metadata>();
		List<LO_Metadata> dup_list = null;
		
		for(LO_Metadata lo: lo_list){
			
			dup_list = collection.get_duplicates(lo);
			
			if(!all_dups.contains(lo))
				for(LO_Metadata dup: dup_list){
				
					lo.add_labels(dup.get_labels());
					all_dups.add(dup);
				}
		}
		
		System.out.println("The number of Duplicates:  "+all_dups.size());
		
		collection.remove_all(all_dups);	
	}
}