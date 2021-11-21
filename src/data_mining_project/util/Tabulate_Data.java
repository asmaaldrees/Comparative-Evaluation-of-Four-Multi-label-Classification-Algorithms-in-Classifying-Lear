// Tabulate the Multilabeled LOs within a single text file for easy reference

package data_mining_project.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

import data_mining_project.core.*;

public class Tabulate_Data {
	
	public void tabulate(LO_Metadata lo) throws IOException{
	
		Path path = FileSystems.getDefault().getPath("Tabulated_Data", "output.txt");
		BufferedWriter writer =Files.newBufferedWriter(path, StandardCharsets.UTF_8);
	    PrintWriter print_writer = new PrintWriter(writer);
	     	    
	     List<LO_Metadata> lom_list = lo.get_lo_list();
	     
	     int i=1;
	     for(LO_Metadata lom: lom_list){
	    	 
	    	 print_writer.print(i++);
	    	 print_writer.println(lom);
	     }
	     print_writer.flush();
	     print_writer.close();
	}
}