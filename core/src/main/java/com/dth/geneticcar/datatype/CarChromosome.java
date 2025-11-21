package com.dth.geneticcar.datatype;

import java.util.Arrays;

public class CarChromosome {
    private PolarVector[] chassisVector;
    private float chassisDensity, chassisFriction, chassisRestitution;
    private float wheelDensity, wheelFriction, wheelRestitution;
    private int[] wheelVertex;
    private float[] wheelRadius;

    public CarChromosome(CarChromosome copy) {
	int chassisLength = copy.chassisVector.length;
	PolarVector[] pv = new PolarVector[chassisLength];
	for (int i = 0; i < chassisLength; i++) {
	    pv[i] = new PolarVector(copy.getChassisVector()[i]);
	}

	this.chassisVector = pv;

	this.chassisDensity = copy.chassisDensity;
	this.chassisFriction = copy.chassisFriction;
	this.chassisRestitution = copy.chassisRestitution;

	this.wheelDensity = copy.wheelDensity;
	this.wheelFriction = copy.wheelFriction;
	this.wheelRestitution = copy.wheelRestitution;

	this.wheelVertex = Arrays.copyOf(copy.wheelVertex, copy.wheelVertex.length);
	this.wheelRadius = Arrays.copyOf(copy.wheelRadius, copy.wheelRadius.length);
    }

    public CarChromosome(PolarVector[] chassisVector, int[] wheelVertex, float[] wheelRadius,
			 float chassisDensity, float chassisFriction, float chassisRestitution,
			 float wheelDensity, float wheelFriction, float wheelRestitution) {
	this.setChassisVector(chassisVector);
	this.setWheelVertex(wheelVertex);
	this.setWheelRadius(wheelRadius);
	this.setChassisDensity(chassisDensity);
	this.setChassisFriction(chassisFriction);
	this.setChassisRestitution(chassisRestitution);
	this.setWheelDensity(wheelDensity);
	this.setWheelFriction(wheelFriction);
	this.setWheelRestitution(wheelRestitution);
    }

    public CarChromosome(String s) {
	String[] part = s.split("\\|");

	String[] tempPart = part[0].split(","); //chassis vectors
	setChassisVector(new PolarVector[tempPart.length / 2]);
	for (int i = 0, j = 0; i < tempPart.length; i += 2, j++) {
	    getChassisVector()[j] = new PolarVector(
		Integer.parseInt(tempPart[i]),
		Float.parseFloat(tempPart[i + 1])
	    );
	}

	tempPart = part[1].split(","); //wheel vertices
	setWheelVertex(new int[tempPart.length]);
	for (int i = 0; i < tempPart.length; i++) {
	    getWheelVertex()[i] = Integer.parseInt(tempPart[i]);
	}

	tempPart = part[2].split(",");
	setWheelRadius(new float[tempPart.length]);
	for (int i = 0; i < tempPart.length; i++) {
	    getWheelRadius()[i] = Float.parseFloat(tempPart[i]);
	}

	setChassisDensity(Float.parseFloat(part[3]));
	setChassisFriction(Float.parseFloat(part[4]));
	setChassisRestitution(Float.parseFloat(part[5]));

	setWheelDensity(Float.parseFloat(part[6]));
	setWheelFriction(Float.parseFloat(part[7]));
	setWheelRestitution(Float.parseFloat(part[8]));
    }

    @Override
    public String toString() {
	StringBuilder sb = new StringBuilder("");

	for (int i = 0; i < getChassisVector().length; i++) {
	    sb.append(getChassisVector()[i].getAngle()).append(",").append(getChassisVector()[i].getMagnitude());
	    if (i != getChassisVector().length - 1) sb.append(",");
	}

	sb.append("|");

	for (int i = 0; i < getWheelVertex().length; i++) {
	    sb.append(getWheelVertex()[i]);
	    if (i != getWheelVertex().length - 1) sb.append(",");
	}

	sb.append("|");

	for (int i = 0; i < getWheelRadius().length; i++) {
	    sb.append(getWheelRadius()[i]);
	    if (i != getWheelRadius().length - 1) sb.append(",");
	}

	sb.append("|");

	sb.append(getChassisDensity());
	sb.append("|");
	sb.append(getChassisFriction());
	sb.append("|");
	sb.append(getChassisRestitution());
	sb.append("|");

	sb.append(getWheelDensity());
	sb.append("|");
	sb.append(getWheelFriction());
	sb.append("|");
	sb.append(getWheelRestitution());

	return sb.toString();
    }

    public PolarVector[] getChassisVector() {
	return chassisVector;
    }

    public void setChassisVector(PolarVector[] chassisVector) {
	this.chassisVector = chassisVector;
    }

    public float getChassisDensity() {
	return chassisDensity;
    }

    public void setChassisDensity(float chassisDensity) {
	this.chassisDensity = chassisDensity;
    }

    public float getChassisFriction() {
	return chassisFriction;
    }

    public void setChassisFriction(float chassisFriction) {
	this.chassisFriction = chassisFriction;
    }

    public float getChassisRestitution() {
	return chassisRestitution;
    }

    public void setChassisRestitution(float chassisRestitution) {
	this.chassisRestitution = chassisRestitution;
    }

    public float getWheelDensity() {
	return wheelDensity;
    }

    public void setWheelDensity(float wheelDensity) {
	this.wheelDensity = wheelDensity;
    }

    public float getWheelFriction() {
	return wheelFriction;
    }

    public void setWheelFriction(float wheelFriction) {
	this.wheelFriction = wheelFriction;
    }

    public float getWheelRestitution() {
	return wheelRestitution;
    }

    public void setWheelRestitution(float wheelRestitution) {
	this.wheelRestitution = wheelRestitution;
    }

    public int[] getWheelVertex() {
	return wheelVertex;
    }

    public void setWheelVertex(int[] wheelVertex) {
	this.wheelVertex = wheelVertex;
    }

    public float[] getWheelRadius() {
	return wheelRadius;
    }

    public void setWheelRadius(float[] wheelRadius) {
	this.wheelRadius = wheelRadius;
    }
}
