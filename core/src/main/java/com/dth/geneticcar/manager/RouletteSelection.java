package com.dth.geneticcar.manager;

import java.util.Random;

import com.dth.geneticcar.datatype.CarChromosome;
import com.dth.geneticcar.datatype.CarIndividual;

public class RouletteSelection implements SelectionOperator {
    private FitnessMethod fitnessMethod;

    public RouletteSelection(FitnessMethod fitnessMethod) {
	this.fitnessMethod = fitnessMethod;
    }

    @Override
    public CarIndividual[] select(CarIndividual[] carIndividual) {
	fitnessMethod.modifyFitness(carIndividual);

	float sum = calculateFitnessSum(carIndividual);
	int number = carIndividual.length;
	float[] percentage = new float[number];
	calculatePercentages(carIndividual, percentage, sum);

	float[] minRange = new float[number];
	float[] maxRange = new float[number];
	calculateMinMaxRage(carIndividual, percentage, minRange, maxRange);

	Random r = new Random();
	float randomPercentage;
	int chosen = 0;
	CarIndividual[] newCars = new CarIndividual[number];

	for (int i = 0; i < number; i++) {
	    randomPercentage = r.nextFloat();
	    for (int j = 0; j < number; j++) {
		if (randomPercentage >= minRange[j] && randomPercentage < maxRange[j]) {
		    chosen = j;
		    break;
		}
	    }
	    newCars[i] = new CarIndividual(new CarChromosome(carIndividual[chosen].getCarChromosome()));
	}
	return newCars;
    }

    // --------------------------------------------------
    // Helpers
    // --------------------------------------------------

    private float calculateFitnessSum(CarIndividual[] cars) {
	float sum = 0;
	for (int i = 0; i < cars.length; i++) {
	    sum += cars[i].getFitness();
	}
	return sum;
    }

    private void calculatePercentages(CarIndividual[] cars, float[] percentage, float sum) {
	for (int i = 0; i < cars.length; i++) {
	    percentage[i] = cars[i].getFitness() / sum;
	}
    }

    private void calculateMinMaxRage(CarIndividual[] cars, float[] percentage, float[] minRange, float[] maxRange) {
	minRange[0] = 0;
	for (int i = 0; i < cars.length; i++) {
	    maxRange[i] = minRange[i] + percentage[i];
	    if (i < cars.length - 1) {
		minRange[i + 1] = maxRange[i];
	    } else {
		maxRange[i] += 0.1;
	    }
	}
    }
}
