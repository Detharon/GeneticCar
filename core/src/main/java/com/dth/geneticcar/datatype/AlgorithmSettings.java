package com.dth.geneticcar.datatype;

import com.dth.geneticcar.manager.AverageCrossoverOperator;
import com.dth.geneticcar.manager.CrossoverOperator;
import com.dth.geneticcar.manager.DefaultCrossoverOperator;
import com.dth.geneticcar.manager.DefaultFitnessMethod;
import com.dth.geneticcar.manager.FitnessMethod;
import com.dth.geneticcar.manager.ModFitnessMethod;
import com.dth.geneticcar.manager.RandomAverageCrossoverOperator;
import com.dth.geneticcar.manager.RouletteSelection;
import com.dth.geneticcar.manager.SelectionOperator;
import com.dth.geneticcar.manager.SqrtFitnessMethod;
import com.dth.geneticcar.manager.TournamentSelection;

public class AlgorithmSettings {
    float crossoverProbability, mutationProbability, gravity;
    private int crossoverMethod, selectionMethod, fitnessMethod;
    private int tournamentSize;
    private boolean elite;

    public static final String[] CROSSOVER_METHODS = {"Wymieniaj�ca", "U�redniaj�ca", "U�redniaj�ce losowa"};
    public static final String[] SELECTION_METHODS = {"Metoda ko�a ruletki", "Turniejowa"};
    public static final String[] FITNESS_METHODS = {"Standardowa", "Min = 0", "Pierwiastek"};

    public AlgorithmSettings() {
	crossoverProbability = 0.75f;
	mutationProbability = 0.05f;
	gravity = 9.81f;

	crossoverMethod = 0;
	selectionMethod = 0;
	fitnessMethod = 0;

	elite = true;

	setTournamentSize(3);
    }

    public float getCrossoverProbability() {
	return crossoverProbability;
    }

    public void setCrossoverProbability(float crossoverProbability) {
	this.crossoverProbability = crossoverProbability;
    }

    public float getMutationProbability() {
	return mutationProbability;
    }

    public void setMutationProbability(float mutationProbability) {
	this.mutationProbability = mutationProbability;
    }

    public float getGravity() {
	return gravity;
    }

    public void setGravity(float gravity) {
	this.gravity = gravity;
    }

    public int getCrossoverMethod() {
	return crossoverMethod;
    }

    public void setCrossoverMethod(int crossoverMethod) {
	this.crossoverMethod = crossoverMethod;
    }

    public int getSelectionMethod() {
	return selectionMethod;
    }

    public void setSelectionMethod(int selectionMethod) {
	this.selectionMethod = selectionMethod;
    }

    public FitnessMethod getFitnessMethod() {
	switch (fitnessMethod) {
	    case 1:
		return new ModFitnessMethod();
	    case 2:
		return new SqrtFitnessMethod();
	    default:
		return new DefaultFitnessMethod();
	}
    }

    public SelectionOperator getSelectionOperator() {
	switch (selectionMethod) {
	    case 1:
		return new TournamentSelection(getTournamentSize());
	    default:
		return new RouletteSelection(getFitnessMethod());
	}
    }

    public CrossoverOperator getCrossoverOperator() {
	switch (crossoverMethod) {
	    case 1:
		return new AverageCrossoverOperator(crossoverProbability);
	    case 2:
		return new RandomAverageCrossoverOperator(crossoverProbability);
	    default:
		return new DefaultCrossoverOperator(crossoverProbability);
	}
    }

    public void setFitnessMethod(int fitnessMethod) {
	this.fitnessMethod = fitnessMethod;
    }

    public boolean isElite() {
	return elite;
    }

    public void setElite(boolean elite) {
	this.elite = elite;
    }

    public int getTournamentSize() {
	return tournamentSize;
    }

    public void setTournamentSize(int tournamentSize) {
	this.tournamentSize = tournamentSize;
    }

}
