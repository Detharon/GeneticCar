package com.dth.geneticcar.desktop.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HtmlReader {
    public HtmlReader() {
    }

    public static String readHtml(String file) {
	BufferedReader br = null;
	StringBuilder sb = new StringBuilder("");

	try {
	    br = new BufferedReader(new FileReader(file));
	    String line;

	    while ((line = br.readLine()) != null) {
		sb.append(line);
	    }

	    sb.toString();
	} catch (Exception ex) {

	} finally {
	    if (br != null)
		try {
		    br.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}

	return new String(sb.toString());
    }
}
