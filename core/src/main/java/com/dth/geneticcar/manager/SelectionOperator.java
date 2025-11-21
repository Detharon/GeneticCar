package com.dth.geneticcar.manager;

import com.dth.geneticcar.datatype.CarIndividual;

public interface SelectionOperator {
    public CarIndividual[] select(CarIndividual[] carIndividual);
}
