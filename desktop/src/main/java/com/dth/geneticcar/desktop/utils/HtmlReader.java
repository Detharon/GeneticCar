package com.dth.geneticcar.desktop.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HtmlReader {

    public static String readHtml(String fileLocation) {
	// TODO: Use Files.readString() in newer Java
	String content = "";
	try (Stream<String> stream = Files.lines(Paths.get(fileLocation), StandardCharsets.UTF_8)) {
	    content = stream.collect(Collectors.joining(System.lineSeparator()));
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return content;
    }

}
