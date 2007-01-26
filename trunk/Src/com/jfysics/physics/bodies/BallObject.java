package com.jfysics.physics.bodies;

import com.jfysics.math.vector.Vector2d;

public class BallObject extends RigidBodyObject{
	protected double radius;
	public BallObject(Vector2d pos, double rad){
		super(pos);
		radius = rad;
	}
}
