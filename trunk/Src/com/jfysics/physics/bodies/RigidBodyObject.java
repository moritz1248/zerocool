package com.jfysics.physics.bodies;

import com.jfysics.math.vector.Vector2d;

public abstract class RigidBodyObject {
	protected BoundingBox bounds;
	protected Vector2d centerOfMass;
	protected Vector2d velocity;
	protected Vector2d position;
	protected double density;
	public RigidBodyObject(Vector2d pos){
		this.position = pos;
		velocity = new Vector2d();
		centerOfMass = new Vector2d();
	}
	public Vector2d getCenterOfMass() {
		return centerOfMass;
	}
	public void setCenterOfMass(Vector2d centerOfMass) {
		this.centerOfMass = centerOfMass;
	}
	public double getDensity() {
		return density;
	}
	public void setDensity(double density) {
		this.density = density;
	}
	public Vector2d getPosition() {
		return position;
	}
	public void setPosition(Vector2d position) {
		this.position = position;
	}
	public Vector2d getVelocity() {
		return velocity;
	}
	public void setVelocity(Vector2d velocity) {
		this.velocity = velocity;
	}
	public abstract BoundingBox getBounds();
	public abstract void updateGeometry();
}
