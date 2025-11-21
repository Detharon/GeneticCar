package com.dth.geneticcar.manager;

import com.dth.geneticcar.datatype.CarChromosome;
import com.dth.geneticcar.datatype.CarIndividual;
import com.dth.geneticcar.datatype.IndividualConstraints;
import com.dth.geneticcar.entities.CarEntity;

public class CarIndividualManager {
    private CarIndividual[] carIndividual;
    private int number;
    private SelectionOperator selectionOperator;
    private CrossoverOperator crossoverOperator;
    CarIndividual elite;

    public CarIndividualManager(CarEntity[] car, float[] distance, SelectionOperator selectionOperator, CrossoverOperator crossoverOperator) {
	number = car.length;
	setCarIndividual(new CarIndividual[number]);
	removeNegatives(distance);

	for (int i = 0; i < number; i++) {
	    getCarIndividual()[i] = new CarIndividual(new CarChromosome(car[i].getCarChromosome()), distance[i]);
	}

	this.selectionOperator = selectionOperator;
	this.crossoverOperator = crossoverOperator;
    }

    public void selection() {
	carIndividual = selectionOperator.select(carIndividual);
    }

    public void crossover() {
	carIndividual = crossoverOperator.getNewPopulation(carIndividual);
    }

    public void mutation(float chance, IndividualConstraints specs) {
	for (int i = 0; i < carIndividual.length; i++) {
	    carIndividual[i].mutate(chance, specs);
	}
    }

    public void rememberBest() {
	elite = new CarIndividual(chooseBest(carIndividual));
    }

    public void reviveBest() {
	carIndividual[0] = elite;
    }

    // --------------------------------------------------
    // General helpers
    // --------------------------------------------------

    private float[] removeNegatives(float[] distance) {
	for (int i = 0; i < distance.length; i++) {
	    if (distance[i] < 0) distance[i] = 0;
	}
	return distance;
    }

    private CarIndividual chooseBest(CarIndividual[] carIndividual) {
	CarIndividual best = carIndividual[0];
	for (int i = 1; i < carIndividual.length; i++) {
	    if (carIndividual[i].getFitness() > best.getFitness()) best = carIndividual[i];
	}
	return best;
    }

    // --------------------------------------------------
    // Public methods
    // --------------------------------------------------

    public CarIndividual[] getCarIndividual() {
	return carIndividual;
    }

    public void setCarIndividual(CarIndividual[] carIndividual) {
	this.carIndividual = carIndividual;
    }

    public int getNumber() {
	return number;
    }
}
