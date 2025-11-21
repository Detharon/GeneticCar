package com.dth.geneticcar.manager;

import com.dth.geneticcar.datatype.CarIndividual;

public class SqrtFitnessMethod implements FitnessMethod {
    public void modifyFitness(CarIndividual[] carIndividual) {
	for (int i = 0; i < carIndividual.length; i++) {
	    carIndividual[i].setFitness((float) Math.sqrt(carIndividual[i].getFitness()));
	}
    }
}
