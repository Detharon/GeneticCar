package com.dth.geneticcar.manager;

import com.dth.geneticcar.datatype.CarIndividual;

public class ModFitnessMethod implements FitnessMethod {

    @Override
    public void modifyFitness(CarIndividual[] carIndividual) {
	int number = carIndividual.length;
	float max = -1;
	float min = Float.MAX_VALUE;

	for (int i = 0; i < number; i++) {
	    if (carIndividual[i].getFitness() > max) max = carIndividual[i].getFitness();
	    if (carIndividual[i].getFitness() < min) min = carIndividual[i].getFitness();
	}

	for (int i = 0; i < carIndividual.length; i++) {
	    carIndividual[i].setFitness(carIndividual[i].getFitness() - min);
	}
    }

}
