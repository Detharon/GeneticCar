package com.dth.geneticcar.manager;

import com.dth.geneticcar.datatype.CarIndividual;

public interface CrossoverOperator {
    public CarIndividual[] getNewPopulation(CarIndividual[] oldPopulation);
}
