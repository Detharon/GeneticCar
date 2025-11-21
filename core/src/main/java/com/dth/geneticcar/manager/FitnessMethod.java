package com.dth.geneticcar.manager;

import com.dth.geneticcar.datatype.CarIndividual;

public interface FitnessMethod {
    void modifyFitness(CarIndividual[] carIndividual);
}
