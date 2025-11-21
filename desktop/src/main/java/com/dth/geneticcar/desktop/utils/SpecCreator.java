package com.dth.geneticcar.desktop.utils;

import com.dth.geneticcar.datatype.CarChromosome;
import com.dth.geneticcar.datatype.FixtureSpecs;

public class SpecCreator {
    FixtureSpecs minCar, maxCar, minWheel, maxWheel;

    float minSegmentLength, maxSegmentLength;
    float minWheelRadius, maxWheelRadius;

    int segments, wheels;

    public SpecCreator() {
	FixtureSpecs minCar = new FixtureSpecs(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
	FixtureSpecs maxCar = new FixtureSpecs(-1, -1, -1);
	FixtureSpecs minWheel = new FixtureSpecs(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
	FixtureSpecs maxWheel = new FixtureSpecs(-1, -1, -1);

	this.minCar = minCar;
	this.maxCar = maxCar;
	this.minWheel = minWheel;
	this.maxWheel = maxWheel;

	minSegmentLength = Float.MAX_VALUE;
	maxSegmentLength = 0;

	minWheelRadius = Float.MAX_VALUE;
	maxWheelRadius = 0;
    }

    public void getData(CarChromosome chromosome) {
	if (chromosome.getChassisDensity() < minCar.getDensity()) minCar.setDensity(chromosome.getChassisDensity());
	if (chromosome.getChassisFriction() < minCar.getFriction()) minCar.setFriction(chromosome.getChassisFriction());
	if (chromosome.getChassisRestitution() < minCar.getRestitution())
	    minCar.setRestitution(chromosome.getChassisRestitution());

	if (chromosome.getWheelDensity() < minWheel.getDensity()) minWheel.setDensity(chromosome.getWheelDensity());
	if (chromosome.getWheelFriction() < minWheel.getFriction()) minWheel.setFriction(chromosome.getWheelFriction());
	if (chromosome.getWheelRestitution() < minWheel.getRestitution())
	    minWheel.setRestitution(chromosome.getWheelRestitution());

	if (chromosome.getChassisDensity() > maxCar.getDensity()) maxCar.setDensity(chromosome.getChassisDensity());
	if (chromosome.getChassisFriction() > maxCar.getFriction()) maxCar.setFriction(chromosome.getChassisFriction());
	if (chromosome.getChassisRestitution() > maxCar.getRestitution())
	    maxCar.setRestitution(chromosome.getChassisRestitution());

	if (chromosome.getWheelDensity() > maxWheel.getDensity()) maxWheel.setDensity(chromosome.getWheelDensity());
	if (chromosome.getWheelFriction() > maxWheel.getFriction()) maxWheel.setFriction(chromosome.getWheelFriction());
	if (chromosome.getWheelRestitution() > maxWheel.getRestitution())
	    maxWheel.setRestitution(chromosome.getWheelRestitution());

	segments = chromosome.getChassisVector().length;
	for (int i = 0; i < segments; i++) {
	    if (chromosome.getChassisVector()[i].getMagnitude() > maxSegmentLength)
		maxSegmentLength = chromosome.getChassisVector()[i].getMagnitude();
	    if (chromosome.getChassisVector()[i].getMagnitude() < minSegmentLength)
		minSegmentLength = chromosome.getChassisVector()[i].getMagnitude();
	}

	wheels = chromosome.getWheelVertex().length;
	for (int i = 0; i < wheels; i++) {
	    if (chromosome.getWheelRadius()[i] > maxWheelRadius) maxWheelRadius = chromosome.getWheelRadius()[i];
	    if (chromosome.getWheelRadius()[i] < minWheelRadius) minWheelRadius = chromosome.getWheelRadius()[i];
	}
    }

    public FixtureSpecs getMinCar() {
	return minCar;
    }

    public void setMinCar(FixtureSpecs minCar) {
	this.minCar = minCar;
    }

    public FixtureSpecs getMaxCar() {
	return maxCar;
    }

    public void setMaxCar(FixtureSpecs maxCar) {
	this.maxCar = maxCar;
    }

    public FixtureSpecs getMinWheel() {
	return minWheel;
    }

    public void setMinWheel(FixtureSpecs minWheel) {
	this.minWheel = minWheel;
    }

    public FixtureSpecs getMaxWheel() {
	return maxWheel;
    }

    public void setMaxWheel(FixtureSpecs maxWheel) {
	this.maxWheel = maxWheel;
    }

    public float getMinSegmentLength() {
	return minSegmentLength;
    }

    public void setMinSegmentLength(float minSegmentLength) {
	this.minSegmentLength = minSegmentLength;
    }

    public float getMaxSegmentLength() {
	return maxSegmentLength;
    }

    public void setMaxSegmentLength(float maxSegmentLength) {
	this.maxSegmentLength = maxSegmentLength;
    }

    public float getMinWheelRadius() {
	return minWheelRadius;
    }

    public void setMinWheelRadius(float minWheelRadius) {
	this.minWheelRadius = minWheelRadius;
    }

    public float getMaxWheelRadius() {
	return maxWheelRadius;
    }

    public void setMaxWheelRadius(float maxWheelRadius) {
	this.maxWheelRadius = maxWheelRadius;
    }

    public int getSegments() {
	return segments;
    }

    public void setSegments(int segments) {
	this.segments = segments;
    }

    public int getWheels() {
	return wheels;
    }

    public void setWheels(int wheels) {
	this.wheels = wheels;
    }


}
