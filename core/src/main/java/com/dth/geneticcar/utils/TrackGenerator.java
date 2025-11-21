package com.dth.geneticcar.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class TrackGenerator {
    public static void generateTrack(String filename, float length, int segments, float maxdiff, float delta) {
	BufferedWriter bw = null;
	Random r = new Random();
	float currentHeight = 0, newHeight;
	boolean up;

	try {
	    bw = new BufferedWriter(new FileWriter(filename));
	    bw.write(String.valueOf(length));
	    bw.write("\r\n");

	    for (int i = 0; i < segments; i++) {
		newHeight = r.nextFloat() * maxdiff;
		up = r.nextBoolean();

		if (up) {
		    currentHeight += newHeight;
		} else if (currentHeight - newHeight > 0) {
		    currentHeight -= newHeight;
		} else {
		    currentHeight += newHeight;
		}
		bw.write(String.valueOf(currentHeight));
		bw.write("\r\n");
		maxdiff += delta;
	    }
	} catch (Exception ex) {

	} finally {
	    if (bw != null)
		try {
		    bw.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
    }
}
