package com.dth.geneticcar.manager;

import java.util.Arrays;

import com.dth.geneticcar.entities.CarEntity;

public class CarEntityManager {
    private CarEntity[] car;
    private int number;

    private float[] velocity;
    private float[] distance;
    private float[] lastDistance;

    private boolean[] running;
    private int[] stopFlag;
    private float finishDistance;

    private float[] distanceChange;

    public CarEntityManager(CarEntity[] car, float finishDistance) {
	this.car = car;
	number = car.length;

	velocity = new float[number];
	Arrays.fill(velocity, 0);

	distance = new float[number];
	Arrays.fill(distance, 0);
	lastDistance = new float[number];

	running = new boolean[number];
	Arrays.fill(running, true);

	stopFlag = new int[number];
	Arrays.fill(stopFlag, 0);

	distanceChange = new float[number];
	Arrays.fill(distanceChange, 0);

	this.finishDistance = finishDistance;
    }

    public void evalDistances() {

	for (int i = 0; i < number; i++) {
	    if (running[i]) {
		lastDistance[i] = distance[i];
		distance[i] = car[i].getPosition().x;
		velocity[i] = Math.abs(distance[i] - lastDistance[i]);

		stopFlag[i]++;
		if (stopFlag[i] == 60) {
		    distanceChange[i] = distance[i];
		} else if (stopFlag[i] == 300) {
		    distanceChange[i] = distance[i] - distanceChange[i];
		    if (distanceChange[i] < 0.6f) running[i] = false;
		    stopFlag[i] = 0;
		}

		if (distance[i] > finishDistance) running[i] = false;
	    }
	}
    }

    public boolean anyRunning() {
	for (int i = 0; i < number; i++) {
	    if (running[i] == true) return true;
	}

	return false;
    }

    public void removeAll() {
	for (int i = 0; i < number; i++) {
	    car[i].destroy();
	}
    }

    public CarEntity getFurthestCar() {
	float bestDistance = -1;
	int furthest = 0;

	for (int i = 0; i < number; i++) {
	    if (distance[i] > bestDistance && running[i]) {
		bestDistance = distance[i];
		furthest = i;
	    }
	}

	return car[furthest];
    }

    public boolean[] getRunning() {
	return running;
    }

    public float[] getDistances() {
	return distance;
    }

    public CarEntity[] getCars() {
	return car;
    }


}
