package com.dth.geneticcar.datatype;

import java.util.Arrays;
import java.util.Random;

public class CarIndividual {
    CarChromosome carChromosome;
    private float fitness = 0;

    public CarIndividual(CarChromosome carChromosome, float fitness) {
	this.carChromosome = carChromosome;
	this.fitness = fitness;
    }

    public CarIndividual(CarChromosome carChromosome) {
	this.carChromosome = carChromosome;
    }

    public CarIndividual(CarIndividual carIndividual) {
	this.carChromosome = new CarChromosome(carIndividual.getCarChromosome());
    }

    // --------------------------------------------------
    // Public methods
    // --------------------------------------------------

    public CarChromosome getCarChromosome() {
	return carChromosome;
    }

    public float getFitness() {
	return fitness;
    }

    public void setFitness(float fitness) {
	this.fitness = fitness;
    }

    public void mutate(float chance, IndividualConstraints specs) {
	mutateChassisVertex(chance, specs);
	mutateWheelVertex(chance, specs);
	mutateWheelRadius(chance, specs);
	mutateChassisSpecs(chance, specs);
	mutateWheelSpecs(chance, specs);
    }

    // --------------------------------------------------
    // Helper methods
    // --------------------------------------------------

    private boolean containsAngle(PolarVector[] vector, int angle) {
	for (int i = 0; i < vector.length; i++) {
	    if (vector[i].getAngle() == angle) return true;
	}
	return false;
    }

    // --------------------------------------------------
    // Mutation helpers
    // --------------------------------------------------

    private void mutateChassisVertex(float chance, IndividualConstraints specs) {
	Random r = new Random();

	for (int i = 0; i < carChromosome.getChassisVector().length * 2 - 1; i++) {
	    if (r.nextFloat() < chance) {
		if (i % 2 == 0) {
		    int newAngle = r.nextInt(360);
		    while (containsAngle(carChromosome.getChassisVector(), newAngle)) {
			newAngle = r.nextInt(360);
		    }
		    carChromosome.getChassisVector()[i / 2].setAngle(newAngle);
		    Arrays.sort(carChromosome.getChassisVector());
		} else {
		    float newMagnitude = r.nextFloat() * (specs.getMaxChassisLength() - specs.getMinChassisLength()) + specs.getMinChassisLength();
		    carChromosome.getChassisVector()[(i + 1) / 2].setMagnitude(newMagnitude);
		}
	    }
	}
    }

    private void mutateWheelVertex(float chance, IndividualConstraints specs) {
	Random r = new Random();

	for (int i = 0; i < specs.getWheelVertex(); i++) {
	    if (r.nextFloat() < chance) {
		int whichVertex = r.nextInt(specs.getWheelVertex());
		int newValue = r.nextInt(specs.getChassisVertex());
		carChromosome.getWheelVertex()[whichVertex] = newValue;
	    }
	}
    }

    private void mutateWheelRadius(float chance, IndividualConstraints specs) {
	Random r = new Random();

	for (int i = 0; i < specs.getWheelVertex(); i++) {
	    if (r.nextFloat() < chance) {
		int whichRadius = r.nextInt(specs.getWheelVertex());
		float newValue = r.nextFloat() * (specs.getMaxWheelRadius() - specs.getMinWheelRadius()) + specs.getMinWheelRadius();
		carChromosome.getWheelRadius()[whichRadius] = newValue;
	    }
	}
    }

    private void mutateChassisSpecs(float chance, IndividualConstraints specs) {
	Random r = new Random();

	if (r.nextFloat() < chance) carChromosome.setChassisDensity(r.nextFloat() *
	    (specs.getMaxFixtureChassisSpecs().getDensity() - specs.getMinFixtureChassisSpecs().getDensity()) + specs.getMinFixtureChassisSpecs().getDensity());
	if (r.nextFloat() < chance) carChromosome.setChassisFriction(r.nextFloat() *
	    (specs.getMaxFixtureChassisSpecs().getFriction() - specs.getMinFixtureChassisSpecs().getFriction()) + specs.getMinFixtureChassisSpecs().getFriction());
	if (r.nextFloat() < chance) carChromosome.setChassisRestitution(r.nextFloat() *
	    (specs.getMaxFixtureChassisSpecs().getRestitution() - specs.getMinFixtureChassisSpecs().getRestitution()) + specs.getMinFixtureChassisSpecs().getRestitution());
    }

    private void mutateWheelSpecs(float chance, IndividualConstraints specs) {
	Random r = new Random();

	if (r.nextFloat() < chance) carChromosome.setWheelDensity(r.nextFloat() *
	    (specs.getMaxFixtureWheelSpecs().getDensity() - specs.getMinFixtureWheelSpecs().getDensity()) + specs.getMinFixtureWheelSpecs().getDensity());
	if (r.nextFloat() < chance) carChromosome.setWheelFriction(r.nextFloat() *
	    (specs.getMaxFixtureWheelSpecs().getFriction() - specs.getMinFixtureWheelSpecs().getFriction()) + specs.getMinFixtureWheelSpecs().getFriction());
	if (r.nextFloat() < chance) carChromosome.setWheelRestitution(r.nextFloat() *
	    (specs.getMaxFixtureWheelSpecs().getRestitution() - specs.getMinFixtureWheelSpecs().getRestitution()) + specs.getMinFixtureWheelSpecs().getRestitution());
    }
}
