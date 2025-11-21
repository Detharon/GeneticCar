package com.dth.geneticcar.datatype;

import java.io.Serializable;

import com.badlogic.gdx.math.Vector2;

/**
 * Encapsulates a 2D Vector of a (angle, magnitude) form.
 */
public class PolarVector implements Serializable, Comparable<PolarVector> {
    private static final long serialVersionUID = -7595588169313022283L;

    /**
     * An angle in Euclidean space, given in degrees.
     */
    private int angle;

    /**
     * A magnitude of a vector
     */
    private float magnitude;

    /**
     * Constructs a vector.
     *
     * @param angle The vector angle
     * @param angle The vector magnitude
     */
    public PolarVector(int angle, float magnitude) {
	this.angle = angle;
	this.magnitude = magnitude;
    }

    public PolarVector(PolarVector polarVector) {
	this.angle = polarVector.getAngle();
	this.magnitude = polarVector.getMagnitude();
    }

    /**
     * @return A Vector2, which is a component form of PolarVector.
     */
    public Vector2 toVector2() {
	float x = getX();
	float y = getY();

	return new Vector2(x, y);
    }

    /**
     * @return X value of corresponding Vector2.
     */
    public float getX() {
	return (float) Math.cos(Math.toRadians(angle)) * magnitude;
    }

    /**
     * @return Y value of corresponding Vector2.
     */
    public float getY() {
	return (float) Math.sin(Math.toRadians(angle)) * magnitude;
    }

    /**
     * @return The vector angle.
     */
    public int getAngle() {
	return angle;
    }

    public void setAngle(int angle) {
	this.angle = angle;
    }

    /**
     * @return The vector magnitude.
     */
    public float getMagnitude() {
	return magnitude;
    }

    public void setMagnitude(float magnitude) {
	this.magnitude = magnitude;
    }

    @Override
    public int compareTo(PolarVector other) {
	if (angle > other.getAngle()) return 1;
	if (angle == other.getAngle()) return 0;
	else return -1;
    }


}
