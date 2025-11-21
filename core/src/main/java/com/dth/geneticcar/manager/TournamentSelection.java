package com.dth.geneticcar.manager;

import java.util.Random;

import com.dth.geneticcar.datatype.CarIndividual;

public class TournamentSelection implements SelectionOperator {
    private int tournamentSize;

    public TournamentSelection(int tournamentSize) {
	this.tournamentSize = tournamentSize;
    }

    @Override
    public CarIndividual[] select(CarIndividual[] carIndividual) {
	Random r = new Random();
	int number = carIndividual.length;
	CarIndividual[] newCars = new CarIndividual[number];
	int[] chosen;

	for (int i = 0; i < number; i++) {
	    chosen = new int[tournamentSize];
	    for (int j = 0; j < tournamentSize; j++) {
		chosen[j] = r.nextInt(number);
	    }
	    int winner = makeTournament(carIndividual, chosen);
	    newCars[i] = new CarIndividual(carIndividual[winner].getCarChromosome());
	}

	return newCars;
    }

    // --------------------------------------------------
    // Helpers
    // --------------------------------------------------

    private int makeTournament(CarIndividual[] carIndividual, int[] chosen) {
	int best = 0;
	for (int i = 1; i < chosen.length; i++) {
	    if (carIndividual[chosen[i]].getFitness() >= carIndividual[best].getFitness()) best = chosen[i];
	}
	return best;
    }
}
