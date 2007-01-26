package com.jfysics.math.vector;

public class Vector2d {
	double x, y;
	public Vector2d(double x, double y){
		this.x = x; this.y = y;
	}
	public Vector2d(){
		this.x = 0; this.y = 0;
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
	public double[] getList(){
		return new double[]{x, y};
	}
	public double getDistance(){
		return Math.sqrt((x * x) + (y * y));
	}
	public void normalize(){
		double dist = getDistance();
		x /= dist;
		y /= dist;
	}
	public Vector2d add(Vector2d add){
		Vector2d result = new Vector2d(x + add.getX(), y + add.getY());
		return result;
	}
	public String toString(){
		return "(" + x + ", " + y + ")";
	}
}
