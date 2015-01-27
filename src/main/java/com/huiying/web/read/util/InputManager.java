package com.huiying.web.read.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InputManager {
	
	 public List<String> readFile(String fname)
	 {
		 List<String> results = new ArrayList<String>();
		 try {
			 BufferedReader br = new BufferedReader(new FileReader(fname)); 
			 String temp = "";
			 while((temp=br.readLine()) != null) {
				 results.add(temp);
				 }
			 br.close(); 
			 } catch (FileNotFoundException e1) 
			 {
			    // TODO Auto-generated catch block
			    e1.printStackTrace();
			 } catch (IOException e1) {
			    // TODO Auto-generated catch block
			    e1.printStackTrace();
			 }
		  return results;
	 }
	 
	 //list all the files which matches the key word
	 public String[] listFiles(String path, String kword)
	 {
		 File dir = new File(path);
		 FilenameFilter filter = new OnlyExt(kword);
		 String[] result = null;
		 
		 result = dir.list(filter);
		 return result;
	 }
	 
	 //list all the files which matches the key word
	 public String[] listFilesByKeyword(String path, String kword)
	 {
		 File dir = new File(path);
		 FilenameFilter filter = new keywordMatch(kword);
		 String[] result = null;
		 
		 result = dir.list(filter);
		 return result;
	 }

}
