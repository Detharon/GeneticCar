package com.dth.geneticcar.desktop.utils;

public class ChromosomeValidator {
    int wheels = -1, segments = -1;

    String error;
    int errorLine;

    public ChromosomeValidator(int wheels, int segments) {
	this.wheels = wheels;
	this.segments = segments;
    }

    public ChromosomeValidator() {
    }

    public boolean isValid(String chromosome) {
	String[] part = chromosome.split("\\|");
	String[] tempPart = part[0].split(",");

	if (part.length != 9) {
	    error = "Chromosom posiada z�y format, brakuje jednego z gen�w, lub jest ich za du�o.";
	    return false;
	}

	int segments = part[0].split(",").length / 2;
	int wheels = part[1].split(",").length;

	if (this.segments == -1) setProperties(segments, wheels);
	if (!checkProperties(segments, wheels)) {
	    error = "Liczba k� lub segment�w karoserii nie jest jednakowa w ca�ej populacji.";
	    return false;
	}

	tempPart = part[1].split(",");

	for (int i = 0; i < tempPart.length; i++) {
	    try {
		if (i % 2 == 0) Integer.parseInt(tempPart[i]);
		else Float.parseFloat(tempPart[i]);
	    } catch (NumberFormatException ex) {
		error = "Jedna z liczb została podana w złym formacie: " + tempPart[i];
		return false;
	    }
	}

	tempPart = part[1].split(",");

	for (int i = 0; i < tempPart.length; i++) {
	    try {
		Integer.parseInt(tempPart[i]);
	    } catch (NumberFormatException ex) {
		error = "Jedna z liczb została podana w złym formacie: " + tempPart[i] + ". Wymagana jest liczba ca�kowita.";
		return false;
	    }
	}

	tempPart = part[2].split(",");

	for (int i = 0; i < tempPart.length; i++) {
	    try {
		Float.parseFloat(tempPart[i]);
	    } catch (NumberFormatException ex) {
		error = "Jedna z liczb została podana w złym formacie: " + tempPart[i];
		return false;
	    }
	}

	for (int i = 3; i < 9; i++) {
	    try {
		Float.parseFloat(part[i]);
	    } catch (NumberFormatException ex) {
		error = "Jedna z liczb została podana w złym formacie: " + tempPart[i];
		return false;
	    }
	}

	error = null;
	return true;
    }

    private void setProperties(int segments, int wheels) {
	this.segments = segments;
	this.wheels = wheels;
    }

    private boolean checkProperties(int segments, int wheels) {
	if (this.segments != segments || this.wheels != wheels) return false;
	return true;
    }

    public String getError() {
	return error;
    }

}
