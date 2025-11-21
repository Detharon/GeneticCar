package com.dth.geneticcar.datatype;

import java.io.Serializable;

public class IndividualConstraints implements Serializable {
    private static final long serialVersionUID = -1827763463117670745L;

    private int chassisVertex, wheelVertex;
    private float minChassisLength, maxChassisLength;
    private float minWheelRadius, maxWheelRadius;
    private FixtureSpecs minFixtureChassisSpecs, maxFixtureChassisSpecs;
    private FixtureSpecs minFixtureWheelSpecs, maxFixtureWheelSpecs;

    public IndividualConstraints(int chassisVertex, int wheelVertex, float minChassisLength, float maxChassisLength, float minWheelRadius, float maxWheelRadius,
				 FixtureSpecs minFixtureSpecs, FixtureSpecs maxFixtureSpecs,
				 FixtureSpecs minFixtureWheelSpecs, FixtureSpecs maxFixtureWheelSpecs) {
	this.setChassisVertex(chassisVertex);
	this.setWheelVertex(wheelVertex);
	this.setMinChassisLength(minChassisLength);
	this.setMaxChassisLength(maxChassisLength);
	this.setMinWheelRadius(minWheelRadius);
	this.setMaxWheelRadius(maxWheelRadius);
	this.setMinFixtureChassisSpecs(minFixtureSpecs);
	this.setMaxFixtureChassisSpecs(maxFixtureSpecs);
	this.setMinFixtureWheelSpecs(minFixtureSpecs);
	this.setMaxFixtureWheelSpecs(maxFixtureSpecs);
    }

    // --------------------------------------------------
    // Public methods
    // --------------------------------------------------

    public int getChassisVertex() {
	return chassisVertex;
    }

    public void setChassisVertex(int chassisVertex) {
	this.chassisVertex = chassisVertex;
    }

    public int getWheelVertex() {
	return wheelVertex;
    }

    public void setWheelVertex(int wheelVertex) {
	this.wheelVertex = wheelVertex;
    }

    public float getMinChassisLength() {
	return minChassisLength;
    }

    public void setMinChassisLength(float minChassisLength) {
	this.minChassisLength = minChassisLength;
    }

    public float getMaxChassisLength() {
	return maxChassisLength;
    }

    public void setMaxChassisLength(float maxChassisLength) {
	this.maxChassisLength = maxChassisLength;
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

    public FixtureSpecs getMinFixtureChassisSpecs() {
	return minFixtureChassisSpecs;
    }

    public void setMinFixtureChassisSpecs(FixtureSpecs minFixtureChassisSpecs) {
	this.minFixtureChassisSpecs = minFixtureChassisSpecs;
    }

    public FixtureSpecs getMaxFixtureChassisSpecs() {
	return maxFixtureChassisSpecs;
    }

    public void setMaxFixtureChassisSpecs(FixtureSpecs maxFixtureChassisSpecs) {
	this.maxFixtureChassisSpecs = maxFixtureChassisSpecs;
    }

    public FixtureSpecs getMinFixtureWheelSpecs() {
	return minFixtureWheelSpecs;
    }

    public void setMinFixtureWheelSpecs(FixtureSpecs minFixtureWheelSpecs) {
	this.minFixtureWheelSpecs = minFixtureWheelSpecs;
    }

    public FixtureSpecs getMaxFixtureWheelSpecs() {
	return maxFixtureWheelSpecs;
    }

    public void setMaxFixtureWheelSpecs(FixtureSpecs maxFixtureWheelSpecs) {
	this.maxFixtureWheelSpecs = maxFixtureWheelSpecs;
    }
}
