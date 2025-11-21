package com.dth.geneticcar.entities;

import com.dth.geneticcar.datatype.CarChromosome;
import com.dth.geneticcar.datatype.PolarVector;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WheelJoint;
import com.badlogic.gdx.physics.box2d.joints.WheelJointDef;

public class CarEntity {
    private World world;

    private CarChromosome carChromosome;

    private Body chassis;
    private Body[] wheel;
    private WheelJoint[] joint;

    /*
     * x and y are coordinates relative to the world, they're necessary to make sure that the car
     *  doesn't get created inside the surface or below it.
     */

    public CarEntity(World world, float x, float y, CarChromosome carChromosome) {
	this.world = world;
	this.carChromosome = carChromosome;

	BodyDef bodyDef = new BodyDef();
	bodyDef.type = BodyType.DynamicBody;
	bodyDef.position.set(x, y);

	FixtureDef segmentFixtureDef = new FixtureDef();
	segmentFixtureDef.density = carChromosome.getChassisDensity();
	segmentFixtureDef.friction = carChromosome.getChassisFriction();
	segmentFixtureDef.restitution = carChromosome.getChassisRestitution();
	segmentFixtureDef.filter.groupIndex = -8;

	FixtureDef wheelFixtureDef = new FixtureDef();
	wheelFixtureDef.density = carChromosome.getWheelDensity();
	wheelFixtureDef.friction = carChromosome.getWheelFriction();
	wheelFixtureDef.restitution = carChromosome.getWheelRestitution();
	wheelFixtureDef.filter.groupIndex = -8;

	PolarVector[] chassisVector = carChromosome.getChassisVector();
	int[] wheelVertex = carChromosome.getWheelVertex();
	float[] wheelRadius = carChromosome.getWheelRadius();

	// Creating a chassis body.
	chassis = world.createBody(bodyDef);

	for (int i = 0; i < chassisVector.length; i++) {
	    // Defining a shape.
	    Vector2[] chassisVector2 = new Vector2[3];
	    boolean validShape = false;

	    if (i != chassisVector.length - 1) {
		if (chassisVector[i + 1].getAngle() - chassisVector[i].getAngle() < 180) {
		    validShape = true;
		    chassisVector2[0] = new Vector2(0, 0);
		    chassisVector2[1] = new Vector2(chassisVector[i].toVector2());
		    chassisVector2[2] = new Vector2(chassisVector[i + 1].toVector2());
		}
	    } else {
		if (chassisVector[i].getAngle() - chassisVector[0].getAngle() > 180) {
		    validShape = true;
		    chassisVector2[0] = new Vector2(0, 0);
		    chassisVector2[1] = new Vector2(chassisVector[i].toVector2());
		    chassisVector2[2] = new Vector2(chassisVector[0].toVector2());
		}
	    }

	    if (validShape) {
		// Adding shape to a body.
		PolygonShape chassisShape = new PolygonShape();
		chassisShape.set(chassisVector2);
		segmentFixtureDef.shape = chassisShape;

		chassis.createFixture(segmentFixtureDef);
	    }
	}

	// Creating wheel bodies and shapes (one per body)
	wheel = new Body[wheelVertex.length];

	for (int i = 0; i < wheelVertex.length; i++) {
	    bodyDef.position.set(x + chassisVector[wheelVertex[i]].getX(), y + chassisVector[wheelVertex[i]].getY());

	    wheel[i] = world.createBody(bodyDef);

	    CircleShape wheelShape = new CircleShape();
	    wheelShape.setRadius(wheelRadius[i]);
	    wheelFixtureDef.shape = wheelShape;

	    wheel[i].createFixture(wheelFixtureDef);
	}

	//creating wheel joints
	joint = new WheelJoint[wheelVertex.length];
	WheelJointDef axisDef = new WheelJointDef();
	axisDef.motorSpeed = -25.0f;
	axisDef.maxMotorTorque = 10.0f;
	axisDef.enableMotor = true;
	;

	for (int i = 0; i < wheelVertex.length; i++) {

	    axisDef.bodyA = chassis;
	    axisDef.bodyB = wheel[i];

	    axisDef.localAnchorA.set(chassisVector[wheelVertex[i]].toVector2());
	    axisDef.localAnchorB.set(0, 0);
	    axisDef.localAxisA.set(0, 1f);
	    axisDef.frequencyHz = 4f;
	    axisDef.dampingRatio = 0.4f;

	    joint[i] = (WheelJoint) world.createJoint(axisDef);
	}
    }

    public CarEntity(World world, float x, float y, FixtureDef segmentFixtureDef, FixtureDef wheelFixtureDef, PolarVector[] chassisVector, int[] wheelVertex, float[] wheelRadius) {
	// Defining body.
	BodyDef bodyDef = new BodyDef();
	bodyDef.type = BodyType.DynamicBody;
	bodyDef.position.set(x, y);

	// Creating a chassis body.
	chassis = world.createBody(bodyDef);

	for (int i = 0; i < chassisVector.length; i++) {
	    // Defining a shape.
	    Vector2[] chassisVector2 = new Vector2[3];
	    boolean validShape = false;

	    if (i != chassisVector.length - 1) {
		if (chassisVector[i + 1].getAngle() - chassisVector[i].getAngle() < 180) {
		    validShape = true;
		    chassisVector2[0] = new Vector2(0, 0);
		    chassisVector2[1] = new Vector2(chassisVector[i].toVector2());
		    chassisVector2[2] = new Vector2(chassisVector[i + 1].toVector2());
		}
	    } else {
		if (chassisVector[i].getAngle() - chassisVector[0].getAngle() > 180) {
		    validShape = true;
		    chassisVector2[0] = new Vector2(0, 0);
		    chassisVector2[1] = new Vector2(chassisVector[i].toVector2());
		    chassisVector2[2] = new Vector2(chassisVector[0].toVector2());
		}
	    }

	    if (validShape) {
		// Adding shape to a body.
		PolygonShape chassisShape = new PolygonShape();
		chassisShape.set(chassisVector2);
		segmentFixtureDef.shape = chassisShape;

		chassis.createFixture(segmentFixtureDef);
	    }
	}

	// Creating wheel bodies and shapes (one per body)
	wheel = new Body[wheelVertex.length];

	for (int i = 0; i < wheelVertex.length; i++) {
	    bodyDef.position.set(x + chassisVector[wheelVertex[i]].getX(), y + chassisVector[wheelVertex[i]].getY());

	    wheel[i] = world.createBody(bodyDef);

	    CircleShape wheelShape = new CircleShape();
	    wheelShape.setRadius(wheelRadius[i]);
	    wheelFixtureDef.shape = wheelShape;

	    wheel[i].createFixture(wheelFixtureDef);
	}

	//creating wheel joints
	joint = new WheelJoint[wheelVertex.length];
	WheelJointDef axisDef = new WheelJointDef();
	axisDef.motorSpeed = -25.0f;
	axisDef.maxMotorTorque = 10.0f;
	axisDef.enableMotor = true;
	;

	for (int i = 0; i < wheelVertex.length; i++) {

	    axisDef.bodyA = chassis;
	    axisDef.bodyB = wheel[i];

	    axisDef.localAnchorA.set(chassisVector[wheelVertex[i]].toVector2());
	    axisDef.localAnchorB.set(0, 0);
	    axisDef.localAxisA.set(0, 1f);
	    axisDef.frequencyHz = 4f;
	    axisDef.dampingRatio = 0.4f;

	    joint[i] = (WheelJoint) world.createJoint(axisDef);
	}
    }

    public Vector2 getPosition() {
	return chassis.getPosition();
    }

    public void destroy() {
	for (int i = 0; i < wheel.length; i++) {
	    world.destroyJoint(joint[i]);
	    world.destroyBody(wheel[i]);
	}
	world.destroyBody(chassis);
    }

    public CarChromosome getCarChromosome() {
	return carChromosome;
    }
}
