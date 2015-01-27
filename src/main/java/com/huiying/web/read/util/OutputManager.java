package com.huiying.web.read.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

/**
 * write a string into a file
 * 
 * @author dhuiying
 *
 */

public class OutputManager {

	/**
	 * if the file exists appends the content to the end of the file, otherwise
	 * create a new file
	 * 
	 * @param path
	 * @param content
	 * @param fname
	 */
	public void save2File(String path, List<String> content, String fname) {
		try {
			String fullPath = path + "/" + fname;
			File f = new File(fullPath);
			FileWriter fstream;
			if (f.exists()) {
				fstream = new FileWriter(path + "/" + fname, true);
			} else {
				fstream = new FileWriter(path + "/" + fname, false);
			}
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(stringList2String(content));
			// Close the output stream
			out.close();

		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	private String stringList2String(List<String> contentList) {
		StringBuilder sb = new StringBuilder();
		for (String line : contentList) {
			sb.append(line);
			sb.append("\n");
		}
		return sb.toString();
	}
}
