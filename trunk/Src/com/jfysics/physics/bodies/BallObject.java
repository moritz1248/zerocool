package com.jfysics.physics.bodies;

import com.jfysics.math.vector.Vector2d;

public class BallObject extends RigidBodyObject{
	protected double radius;
	public BallObject(Vector2d pos, double rad){
		super(pos);
		radius = rad;
	}
	@Override
	public BoundingBox getBounds() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void updateGeometry() {
		// TODO Auto-generated method stub
		
	}
}
