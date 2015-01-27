package com.huiying.web.read.util;

import java.io.File;
import java.io.FilenameFilter;

public class keywordMatch implements FilenameFilter {

	String kw;
	
	public keywordMatch(String kw) {
	this.kw = kw;
	}
	
	public boolean accept(File dir, String name) {

		if(name.indexOf(kw) != -1)
			return true;
		else
			return false;
	}

}
