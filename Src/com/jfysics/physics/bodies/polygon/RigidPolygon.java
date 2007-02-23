package com.jfysics.physics.bodies.polygon;

import java.awt.Rectangle;
import java.util.ArrayList;

import com.jfysics.math.vector.Vector2d;
import com.jfysics.physics.bodies.BoundingBox;

public class RigidPolygon {
	private ArrayList<Vector2d> vectors;
	private BoundingBox boundingBox;
	public RigidPolygon(ArrayList<Vector2d> vecList){
		vectors = vecList;
	}
	public BoundingBox getBounds()
	{
		if(boundingBox != null)
		{
			return boundingBox;
		}else{
			generateBoundingBox();
			return boundingBox;
		}
	}
	private void generateBoundingBox()
	{
		Vector2d min = vectors.get(0), max = vectors.get(0);
		for(Vector2d v : vectors)
		{
			if(v.getX() < min.getX())
				min.setX(v.getX());
			if(v.getY() < min.getY())
				min.setY(v.getY());
			if(v.getX() > max.getX())
				max.setX(v.getX());
			if(v.getY() > max.getY())
				max.setY(v.getY());
		}
		boundingBox = new BoundingBox(min, max);
	}
}
