package com.dth.geneticcar.entities;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

import com.dth.geneticcar.datatype.CarChromosome;
import com.dth.geneticcar.datatype.FixtureSpecs;
import com.dth.geneticcar.datatype.IndividualConstraints;
import com.dth.geneticcar.datatype.PolarVector;

public class CarChromosomeGenerator {
    private Random r;

    private int chassisVertex;
    private float minChassisLength, maxChassisLength;

    private int wheels;
    private float minWheelRadius, maxWheelRadius;

    FixtureSpecs minChassisSpecs, maxChassisSpecs;
    FixtureSpecs minWheelSpecs, maxWheelSpecs;

    public CarChromosomeGenerator(long seed) {
	r = new Random(seed);
    }

    public CarChromosomeGenerator(int chassisVertex, float minChassisLength, float maxChassisLength,
				  int wheels, float minWheelRadius, float maxWheelRadius,
				  FixtureSpecs minChassisSpecs, FixtureSpecs maxChassisSpecs,
				  FixtureSpecs minWheelSpecs, FixtureSpecs maxWheelSpecs) {

	this.chassisVertex = chassisVertex;
	this.minChassisLength = minChassisLength;
	this.maxChassisLength = maxChassisLength;

	this.wheels = wheels;
	this.minWheelRadius = minWheelRadius;
	this.maxWheelRadius = maxWheelRadius;

	this.minChassisSpecs = minChassisSpecs;
	this.maxChassisSpecs = maxChassisSpecs;

	this.minWheelSpecs = minWheelSpecs;
	this.maxWheelSpecs = maxWheelSpecs;

	r = new Random();
    }

    public CarChromosomeGenerator(IndividualConstraints specs) {
	this.chassisVertex = specs.getChassisVertex();
	this.minChassisLength = specs.getMinChassisLength();
	this.maxChassisLength = specs.getMaxChassisLength();

	this.wheels = specs.getWheelVertex();
	this.minWheelRadius = specs.getMinWheelRadius();
	this.maxWheelRadius = specs.getMaxWheelRadius();

	this.minChassisSpecs = specs.getMinFixtureChassisSpecs();
	this.maxChassisSpecs = specs.getMaxFixtureChassisSpecs();

	this.minWheelSpecs = specs.getMinFixtureWheelSpecs();
	this.maxWheelSpecs = specs.getMaxFixtureWheelSpecs();

	r = new Random();
    }

    //TODO create car generator class

    public CarChromosome generateCarChromosome() {
	int angle;
	float magnitude;
	PolarVector[] chassisVector = new PolarVector[chassisVertex];
	LinkedList<Integer> chosen = new LinkedList<>();

	for (int i = 0; i < chassisVertex; i++) {
	    //generating angle value, 0 inclusive to 360 exclusive, the loop ensures having unique angles
	    do {
		angle = r.nextInt(360);
	    } while (chosen.contains(angle));
	    chosen.add(angle);
	    //and magnitude
	    magnitude = r.nextFloat() * (maxChassisLength - minChassisLength) + minChassisLength;
	    chassisVector[i] = new PolarVector(angle, magnitude);
	}

	//vectors have to be sorted in ascending order, as Box2D expects polygon vercices to be given in counterclockwise order
	Arrays.sort(chassisVector);

	int[] wheelVertex = new int[wheels];
	float[] wheelRadius = new float[wheels];
	chosen = new LinkedList<>();
	int vertex;
	float radius;

	for (int i = 0; i < wheels; i++) {
	    do {
		vertex = r.nextInt(chassisVertex);
	    } while (chosen.contains(vertex));
	    chosen.add(vertex);
	    wheelVertex[i] = vertex;

	    radius = r.nextFloat() * (maxWheelRadius - minWheelRadius) + minWheelRadius;
	    wheelRadius[i] = radius;
	}

	//generating chassis density, friction, restitution;
	float chassisDensity = r.nextFloat() * (maxChassisSpecs.getDensity() - minChassisSpecs.getDensity()) + minChassisSpecs.getDensity();
	float chassisFriction = r.nextFloat() * (maxChassisSpecs.getFriction() - minChassisSpecs.getFriction()) + minChassisSpecs.getFriction();
	float chassisRestitution = r.nextFloat() * (maxChassisSpecs.getRestitution() - minChassisSpecs.getRestitution()) + minChassisSpecs.getRestitution();

	float wheelDensity = r.nextFloat() * (maxWheelSpecs.getDensity() - minWheelSpecs.getDensity()) + minWheelSpecs.getDensity();
	float wheelFriction = r.nextFloat() * (maxWheelSpecs.getFriction() - minWheelSpecs.getFriction()) + minWheelSpecs.getFriction();
	float wheelRestitution = r.nextFloat() * (maxWheelSpecs.getRestitution() - minWheelSpecs.getRestitution()) + minWheelSpecs.getRestitution();

	return new CarChromosome(chassisVector, wheelVertex, wheelRadius,
	    chassisDensity, chassisFriction, chassisRestitution,
	    wheelDensity, wheelFriction, wheelRestitution);
    }

    /**
     * Helper method used to sort vectors by their angle, in ascending order
     * @param vector
     */
}
