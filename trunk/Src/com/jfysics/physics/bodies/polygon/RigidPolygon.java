package com.jfysics.physics.bodies.polygon;

import java.awt.Rectangle;
import java.util.ArrayList;

import com.jfysics.math.line.Line2d;
import com.jfysics.math.vector.Vector2d;
import com.jfysics.physics.bodies.BoundingBox;
import com.jfysics.physics.bodies.RigidBodyObject;

public class RigidPolygon extends RigidBodyObject{
	private ArrayList<Vector2d> vectors;
	//private ArrayList<Line2d> lines;
	public RigidPolygon(ArrayList<Vector2d> vecList){
		super(new Vector2d());
		vectors = vecList;
	}
	
	
	public BoundingBox getBounds()
	{
		if(bounds != null)
		{
			return BoundingBox.translateBounds(bounds, mPosition);
		}else{
			generateBoundingBox();
			return BoundingBox.translateBounds(bounds, mPosition);
		}
	}
	
	private void generateBoundingBox()
	{
		Vector2d min = vectors.get(0).clone(), max = vectors.get(0).clone();
		for(Vector2d v : vectors)
		{
			if(v.getX() < min.getX())
			{
				min.setX(v.getX());
			}
			if(v.getY() < min.getY())
			{
				min.setY(v.getY());
			}
			if(v.getX() > max.getX())
			{
				max.setX(v.getX());
			}
			if(v.getY() > max.getY())
			{
				max.setY(v.getY());
			}
		}
		bounds = new BoundingBox(min, max);
	}


	public int getPointCount() {
		return vectors.size();
	}


	public ArrayList<Vector2d> getVectors() {
		return vectors;
	}


	@Override
	public void updateGeometry() {
		// TODO Auto-generated method stub
		
	}
	
	public Vector2d getVector(int i)
	{
		if(i < vectors.size())
			return vectors.get(i).add(mPosition);
		else
			return vectors.get(i-vectors.size()).add(mPosition);
	}


	public Vector2d checkCollide(RigidPolygon polygon) {
		int poly1count = getPointCount();
		int poly2count = polygon.getPointCount();
		Line2d line1, line2;
		for(int i = 0; i < poly1count; i++)
		{
			line1 = new Line2d(getVector(i), getVector(i+1));
			for(int j = 0; j < poly2count; j++)
			{
				line2 = new Line2d(polygon.getVector(j), polygon.getVector(j+1));
				Vector2d intersect = line2.getIntersection(line1);
				if(line2.isOnSegment(intersect,line1))
				{
					return intersect;
				}

			}
		}
		return null;
	}
}
