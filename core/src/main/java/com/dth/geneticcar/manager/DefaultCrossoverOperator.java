package com.dth.geneticcar.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.dth.geneticcar.datatype.CarChromosome;
import com.dth.geneticcar.datatype.CarIndividual;
import com.dth.geneticcar.datatype.PolarVector;

public class DefaultCrossoverOperator implements CrossoverOperator {
    private float crossoverProbability;

    public DefaultCrossoverOperator(float crossoverProbability) {
	this.crossoverProbability = crossoverProbability;
    }

    public CarIndividual[] getNewPopulation(CarIndividual[] oldPopulation) {
	int number = oldPopulation.length;
	if (number <= 1) return oldPopulation;

	Random r = new Random();

	ArrayList<CarIndividual> tempCarIndividual = new ArrayList<>(Arrays.asList(oldPopulation));
	ArrayList<CarIndividual> newCarIndividual = new ArrayList<>(tempCarIndividual.size());
	CarIndividual[] children;


	int a, b;
	int s = tempCarIndividual.size();

	for (int i = 0; i < (s / 2); i++) {
	    if (r.nextFloat() > crossoverProbability) continue;

	    a = 0;
	    b = 0;
	    while (a == b) {
		a = r.nextInt(tempCarIndividual.size());
		b = r.nextInt(tempCarIndividual.size());
		if (a != b) {
		    children = cross(tempCarIndividual.get(a), tempCarIndividual.get(b));
		    newCarIndividual.add(new CarIndividual(children[0].getCarChromosome()));
		    newCarIndividual.add(new CarIndividual(children[1].getCarChromosome()));

		    if (a > b) {
			tempCarIndividual.remove(a);
			tempCarIndividual.remove(b);
		    } else {
			tempCarIndividual.remove(b);
			tempCarIndividual.remove(a);
		    }
		}
	    }
	}

	newCarIndividual.addAll(tempCarIndividual);

	return newCarIndividual.toArray(new CarIndividual[0]);
    }

    // --------------------------------------------------
    // Helper methods
    // --------------------------------------------------

    private CarIndividual[] cross(CarIndividual first, CarIndividual second) {
	Random r = new Random();

	PolarVector[] currentVector = first.getCarChromosome().getChassisVector();
	PolarVector[] otherVector = second.getCarChromosome().getChassisVector();

	PolarVector[] newVector1 = new PolarVector[currentVector.length];
	PolarVector[] newVector2 = new PolarVector[currentVector.length];


	for (int i = 0; i < currentVector.length; i++) {
	    if (r.nextBoolean() == true) {
		newVector1[i] = new PolarVector(currentVector[i]);
		newVector2[i] = new PolarVector(otherVector[i]);
	    } else {
		newVector1[i] = new PolarVector(otherVector[i]);
		newVector2[i] = new PolarVector(currentVector[i]);
	    }
	}

	Arrays.sort(newVector1);
	Arrays.sort(newVector2);

	vectorFix(newVector1);
	vectorFix(newVector2);

	int[] currentWheels = first.getCarChromosome().getWheelVertex();
	int[] otherWheels = second.getCarChromosome().getWheelVertex();

	int[] newWheels1 = new int[currentWheels.length];
	int[] newWheels2 = new int[currentWheels.length];

	for (int i = 0; i < currentWheels.length; i++) {
	    if (r.nextBoolean() == true) {
		newWheels1[i] = currentWheels[i];
		newWheels2[i] = otherWheels[i];
	    } else {
		newWheels1[i] = otherWheels[i];
		newWheels2[i] = currentWheels[i];
	    }
	}

	float[] currentWheelRadius = first.getCarChromosome().getWheelRadius();
	float[] otherWheelRadius = second.getCarChromosome().getWheelRadius();

	float[] newWheelRadius1 = new float[currentWheelRadius.length];
	float[] newWheelRadius2 = new float[currentWheelRadius.length];

	for (int i = 0; i < currentWheelRadius.length; i++) {
	    if (r.nextBoolean() == true) {
		newWheelRadius1[i] = currentWheelRadius[i];
		newWheelRadius2[i] = otherWheelRadius[i];
	    } else {
		newWheelRadius1[i] = otherWheelRadius[i];
		newWheelRadius2[i] = currentWheelRadius[i];
	    }
	}

	float chassisDensity1, chassisFriction1, chassisRestitution1,
	    wheelDensity1, wheelFriction1, wheelRestitution1;

	float chassisDensity2, chassisFriction2, chassisRestitution2,
	    wheelDensity2, wheelFriction2, wheelRestitution2;

	if (r.nextBoolean() == true) {
	    chassisDensity1 = first.getCarChromosome().getChassisDensity();
	    chassisDensity2 = second.getCarChromosome().getChassisDensity();
	} else {
	    chassisDensity1 = second.getCarChromosome().getChassisDensity();
	    chassisDensity2 = first.getCarChromosome().getChassisDensity();
	}

	if (r.nextBoolean() == true) {
	    chassisFriction1 = first.getCarChromosome().getChassisFriction();
	    chassisFriction2 = second.getCarChromosome().getChassisFriction();
	} else {
	    chassisFriction1 = second.getCarChromosome().getChassisFriction();
	    chassisFriction2 = first.getCarChromosome().getChassisFriction();
	}

	if (r.nextBoolean() == true) {
	    chassisRestitution1 = first.getCarChromosome().getChassisRestitution();
	    chassisRestitution2 = second.getCarChromosome().getChassisRestitution();
	} else {
	    chassisRestitution1 = second.getCarChromosome().getChassisRestitution();
	    chassisRestitution2 = first.getCarChromosome().getChassisRestitution();
	}

	if (r.nextBoolean() == true) {
	    wheelDensity1 = first.getCarChromosome().getWheelDensity();
	    wheelDensity2 = second.getCarChromosome().getWheelDensity();
	} else {
	    wheelDensity1 = second.getCarChromosome().getWheelDensity();
	    wheelDensity2 = first.getCarChromosome().getWheelDensity();
	}

	if (r.nextBoolean() == true) {
	    wheelFriction1 = first.getCarChromosome().getWheelFriction();
	    wheelFriction2 = second.getCarChromosome().getWheelFriction();
	} else {
	    wheelFriction1 = second.getCarChromosome().getWheelFriction();
	    wheelFriction2 = first.getCarChromosome().getWheelFriction();
	}

	if (r.nextBoolean() == true) {
	    wheelRestitution1 = first.getCarChromosome().getWheelRestitution();
	    wheelRestitution2 = second.getCarChromosome().getWheelRestitution();
	} else {
	    wheelRestitution1 = second.getCarChromosome().getWheelRestitution();
	    wheelRestitution2 = first.getCarChromosome().getWheelRestitution();
	}

	CarChromosome newCarChromosome1 = new CarChromosome(
	    newVector1,
	    newWheels1,
	    newWheelRadius1,
	    chassisDensity1, chassisFriction1, chassisRestitution1,
	    wheelDensity1, wheelFriction1, wheelRestitution1
	);

	CarChromosome newCarChromosome2 = new CarChromosome(
	    newVector2,
	    newWheels2,
	    newWheelRadius2,
	    chassisDensity2, chassisFriction2, chassisRestitution2,
	    wheelDensity2, wheelFriction2, wheelRestitution2
	);

	return new CarIndividual[]{
	    new CarIndividual(newCarChromosome1, 0),
	    new CarIndividual(newCarChromosome2, 0)
	};
    }

    private void vectorFix(PolarVector[] vector) {
	for (int i = 0; i < vector.length - 1; i++) {
	    if (vector[i].getAngle() == vector[i + 1].getAngle()) {
		vector[i + 1].setAngle(vector[i].getAngle() + 1);
	    }
	}
    }
}
