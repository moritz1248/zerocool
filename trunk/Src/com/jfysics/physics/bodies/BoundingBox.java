package com.jfysics.physics.bodies;

import com.jfysics.math.vector.Vector2d;

public class BoundingBox {
	private Vector2d min, max;
	public BoundingBox(Vector2d min, Vector2d max)
	{
		this.min = min;
		this.max = max;
	}
	public Vector2d getMaximum() {
		return max;
	}
	public void setMaximum(Vector2d max) {
		this.max = max;
	}
	public Vector2d getMinimum() {
		return min;
	}
	public void setMinimum(Vector2d min) {
		this.min = min;
	}
	public boolean collides(BoundingBox testBox)
	{
		Vector2d min2 = testBox.getMinimum();
		Vector2d max2 = testBox.getMaximum();
		return !((min.getX() > max2.getX()) || (min.getY() > max2.getY()) ||
				(min2.getX() > max.getX()) || (min2.getY() > max.getY()) ||
				(max.getX() < min2.getX()) || (max.getY() < min2.getY()) ||
				(max2.getX() < min.getX()) || (max2.getY() < min.getY())); 
	}
	public double getWidth()
	{
		return max.getX() - min.getX();
	}
	
	public double getHeight()
	{
		return max.getY() - min.getY();
	}
	public static BoundingBox translateBounds(BoundingBox bounds, Vector2d position) {
		BoundingBox box = new BoundingBox(bounds.getMinimum().add(position), bounds.getMaximum().add(position));
		return box;
	}
}
