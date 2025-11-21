package com.dth.geneticcar.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.dth.geneticcar.datatype.CarChromosome;
import com.dth.geneticcar.datatype.CarIndividual;
import com.dth.geneticcar.datatype.PolarVector;

public class RandomAverageCrossoverOperator implements CrossoverOperator {
    private float crossoverProbability;

    public RandomAverageCrossoverOperator(float crossoverProbability) {
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

	PolarVector[] firstVector = first.getCarChromosome().getChassisVector();
	PolarVector[] secondVector = second.getCarChromosome().getChassisVector();

	PolarVector[] newVector1 = new PolarVector[firstVector.length];
	PolarVector[] newVector2 = new PolarVector[firstVector.length];


	float point;
	for (int i = 0; i < firstVector.length; i++) {
	    point = r.nextFloat();
	    newVector1[i] = new PolarVector(Math.round((float) ((1 + point) * firstVector[i].getAngle() + (1 - point) * secondVector[i].getAngle()) / 2),
		(firstVector[i].getMagnitude() + firstVector[i].getMagnitude()) / 2
	    );
	    newVector2[i] = new PolarVector(Math.round((float) ((1 - point) * firstVector[i].getAngle() + (1 + point) * secondVector[i].getAngle()) / 2),
		(firstVector[i].getMagnitude() + firstVector[i].getMagnitude()) / 2
	    );
	}

	Arrays.sort(newVector1);
	Arrays.sort(newVector2);

	vectorFix(newVector1);
	vectorFix(newVector2);

	int[] firstWheels = first.getCarChromosome().getWheelVertex();
	int[] secondWheels = second.getCarChromosome().getWheelVertex();

	//wheel positions - not average (makes no sense)
	int[] newWheels1 = new int[firstWheels.length];
	int[] newWheels2 = new int[firstWheels.length];

	for (int i = 0; i < firstWheels.length; i++) {
	    if (r.nextBoolean() == true) {
		newWheels1[i] = firstWheels[i];
		newWheels2[i] = secondWheels[i];
	    } else {
		newWheels1[i] = secondWheels[i];
		newWheels2[i] = firstWheels[i];
	    }
	}

	float[] firstWheelRadius = first.getCarChromosome().getWheelRadius();
	float[] secondWheelRadius = second.getCarChromosome().getWheelRadius();

	float[] newWheelRadius1 = new float[firstWheelRadius.length];
	float[] newWheelRadius2 = new float[firstWheelRadius.length];

	for (int i = 0; i < firstWheelRadius.length; i++) {
	    point = r.nextFloat();
	    newWheelRadius1[i] = ((1 + point) * firstWheelRadius[i] + (1 - point) * secondWheelRadius[i]) / 2;
	    newWheelRadius2[i] = ((1 - point) * firstWheelRadius[i] + (1 + point) * secondWheelRadius[i]) / 2;
	}

	float chassisDensity1, chassisFriction1, chassisRestitution1,
	    wheelDensity1, wheelFriction1, wheelRestitution1;

	float chassisDensity2, chassisFriction2, chassisRestitution2,
	    wheelDensity2, wheelFriction2, wheelRestitution2;

	point = r.nextFloat();
	chassisDensity1 = ((1 + point) * first.getCarChromosome().getChassisDensity() + (1 - point) * second.getCarChromosome().getChassisDensity()) / 2;
	chassisDensity2 = ((1 - point) * first.getCarChromosome().getChassisDensity() + (1 + point) * second.getCarChromosome().getChassisDensity()) / 2;

	point = r.nextFloat();
	chassisFriction1 = ((1 + point) * first.getCarChromosome().getChassisFriction() + (1 - point) * second.getCarChromosome().getChassisFriction()) / 2;
	chassisFriction2 = ((1 - point) * first.getCarChromosome().getChassisFriction() + (1 + point) * second.getCarChromosome().getChassisFriction()) / 2;

	point = r.nextFloat();
	chassisRestitution1 = ((1 + point) * first.getCarChromosome().getChassisRestitution() + (1 - point) * second.getCarChromosome().getChassisRestitution()) / 2;
	chassisRestitution2 = ((1 - point) * first.getCarChromosome().getChassisRestitution() + (1 + point) * second.getCarChromosome().getChassisRestitution()) / 2;

	point = r.nextFloat();
	wheelDensity1 = ((1 + point) * first.getCarChromosome().getWheelDensity() + (1 - point) * second.getCarChromosome().getWheelDensity()) / 2;
	wheelDensity2 = ((1 - point) * first.getCarChromosome().getWheelDensity() + (1 + point) * second.getCarChromosome().getWheelDensity()) / 2;

	point = r.nextFloat();
	wheelFriction1 = ((1 + point) * first.getCarChromosome().getWheelFriction() + (1 - point) * second.getCarChromosome().getWheelFriction()) / 2;
	wheelFriction2 = ((1 - point) * first.getCarChromosome().getWheelFriction() + (1 + point) * second.getCarChromosome().getWheelFriction()) / 2;

	point = r.nextFloat();
	wheelRestitution1 = ((1 + point) * first.getCarChromosome().getWheelRestitution() + (1 - point) * second.getCarChromosome().getWheelRestitution()) / 2;
	wheelRestitution2 = ((1 - point) * first.getCarChromosome().getWheelRestitution() + (1 + point) * second.getCarChromosome().getWheelRestitution()) / 2;

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
