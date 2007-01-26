package com.jfysics.regression;

import com.jfysics.math.line.Line2d;
import com.jfysics.math.vector.Vector2d;


public class PhysicsRegressionTest {
	RegressionTestView view = new RegressionTestView();
	public static void main(String ... args){
		new PhysicsRegressionTest();
	}
	public PhysicsRegressionTest(){
		int test = 1;
		Line2d line1 = new Line2d(-4, 0, -2, 0);
		Line2d line2 = new Line2d(4, 4, -2, -2);
		System.out.println("Line one and two intersect at: " + line1.intersects(line2));
		view.assertTest(line1.intersects(line2).getX() == 0, "Line Intersection (x)");
		view.assertTest(line1.intersects(line2).getY() == 0, "Line Intersection (y)");
	}
}
