package com.dth.geneticcar.datatype;

import java.io.Serializable;

/**
 * Encapsulates the most commonly used fixture specifications: density, friction, and restitution.
 */
public class FixtureSpecs implements Serializable {
    private static final long serialVersionUID = -5644876315862218977L;

    private float density = 2.0f;
    private float friction = .4f;
    private float restitution = .4f;

    /**
     * Default constructor, takes no parameters and uses default values:
     * density = 2.0f
     * friction = 0.4f
     * restitution = 2.0f
     */
    public FixtureSpecs() {
    }

    public FixtureSpecs(float density, float friction, float restitution) {
	this.setDensity(density);
	this.setFriction(friction);
	this.setRestitution(restitution);
    }

    public float getDensity() {
	return density;
    }

    public void setDensity(float density) {
	this.density = density;
    }

    public float getFriction() {
	return friction;
    }

    public void setFriction(float friction) {
	this.friction = friction;
    }

    public float getRestitution() {
	return restitution;
    }

    public void setRestitution(float restitution) {
	this.restitution = restitution;
    }
}
