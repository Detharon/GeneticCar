package com.dth.geneticcar.utils;

import java.util.EventObject;

import com.dth.geneticcar.datatype.CarIndividual;

public class CarEvent extends EventObject {
    private static final long serialVersionUID = -8604383771546252077L;
    CarIndividual[] cars;
    float trackLength;

    public CarEvent(Object source, CarIndividual[] cars, float trackLength) {
	super(source);
	this.cars = cars;
	this.trackLength = trackLength;
    }

    public CarIndividual[] getCars() {
	return cars;
    }

    public float getTrackLength() {
	return trackLength;
    }
}
