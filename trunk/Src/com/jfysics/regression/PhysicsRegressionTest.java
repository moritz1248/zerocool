package com.jfysics.regression;

import com.jfysics.math.line.Line2d;
import com.jfysics.math.vector.Vector2d;
import com.jfysics.physics.bodies.BoundingBox;


public class PhysicsRegressionTest {
	RegressionTestView view = new RegressionTestView();
	public static void main(String ... args){
		new PhysicsRegressionTest();
	}
	public PhysicsRegressionTest(){
		int test = 1;
		Line2d line1 = new Line2d(-4, 0, -2, 0);
		Line2d line2 = new Line2d(4, 4, -2, -2);
		System.out.println("Line one and two intersect at: " + line1.getIntersection(line2));
		view.assertTest(line1.getIntersection(line2).getX() == 0, "Line Intersection (x)");
		view.assertTest(line1.getIntersection(line2).getY() == 0, "Line Intersection (y)");
		
		BoundingBox b1 = new BoundingBox(new Vector2d(0,0), new Vector2d(5,5));
		BoundingBox b2 = new BoundingBox(new Vector2d(2,2), new Vector2d(7,10));
		
		view.assertTest(b1.collides(b2), "BoundingBox collide 1");
		view.assertTest(b2.collides(b1), "BoundingBox collide 2");
		
		b2 = new BoundingBox(new Vector2d(6,10), new Vector2d(11,15));
		
		view.assertTest(!b1.collides(b2), "BoundingBox no-collide 1");
		view.assertTest(!b2.collides(b1), "BoundingBox no-collide 2");
	}
}
