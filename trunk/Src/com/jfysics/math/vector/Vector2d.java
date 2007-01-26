package com.jfysics.math.vector;

public class Vector2d {
	double x, y;
	public Vector2d(double x, double y){
		this.x = x; this.y = y;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public String toString(){
		return "[ " + x + ", " + y + " ]";
	}
	public double[] getList(){
		return new double[]{x, y};

	}
}
