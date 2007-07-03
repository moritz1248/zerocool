package com.jfysics.physics.bodies;

import com.jfysics.math.vector.Vector2d;

public abstract class RigidBodyObject {
	protected BoundingBox bounds;
	protected Vector2d mCenterOfMass;
	protected double mRotVelocity;
	protected double mRotation;
	protected Vector2d mVelocity;
	protected Vector2d mPosition;
	
	protected double mDensity;
	public RigidBodyObject(Vector2d pos){
		this.mPosition = pos;
		mVelocity = new Vector2d();
		mCenterOfMass = new Vector2d();
		mRotVelocity = 0.0d;
		mRotation = 0.0d;
	}
	public Vector2d getCenterOfMass() {
		return mCenterOfMass;
	}
	public void setCenterOfMass(Vector2d centerOfMass) {
		this.mCenterOfMass = centerOfMass;
	}
	public double getDensity() {
		return mDensity;
	}
	public void setDensity(double density) {
		this.mDensity = density;
	}
	public Vector2d getPosition() {
		return mPosition;
	}
	public void setPosition(Vector2d position) {
		this.mPosition = position;
	}
	public Vector2d getVelocity() {
		return mVelocity;
	}
	public void setVelocity(Vector2d velocity) {
		this.mVelocity = velocity;
	}
	public void setRotationalVelocity(double rotVelocity)
	{
		mRotVelocity = rotVelocity;
	}
	public double getRotationalVelocity()
	{
		return mRotVelocity;
	}
	public void setRotation(double rotation)
	{
		mRotation = rotation;
	}
	public double getRotation()
	{
		return mRotation;
	}
	public abstract BoundingBox getBounds();
	public abstract void updateGeometry();
}
