package com.jfysics.math.line;

import com.jfysics.math.vector.Vector2d;

public class Line2d {
	protected Vector2d p1, p2;
	
	public Line2d(Vector2d point1, Vector2d point2){
		p1 = point1;
		p2 = point2;
	}
	public Line2d(double x1, double y1, double x2, double y2){
		p1 = new Vector2d(x1, y1);
		p2 = new Vector2d(x2, y2);
	}
	public Vector2d getP1(){
		return p1;
	}
	public Vector2d getP2(){
		return p2;
	}
	
	public Vector2d intersects(Line2d line){
		Vector2d p3 = line.getP1();
		Vector2d p4 = line.getP2();
		double m1, m2;
		if(p2.getX() - p1.getX() != 0){
			m1 = (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
		}else{
			m1 = 1e100;
		}
		if(p4.getX() - p3.getX() != 0){
			m2 = (p4.getY() - p3.getY()) / (p4.getX() - p3.getX());
		}else{
			m2 = 1e100;
		}
		double b1 = p1.getY() - (m1 * p1.getX());
		double b2 = p3.getY() - (m2 * p3.getX());

		double x_intersect = (b2 - b1) / (m1 - m2);
		double y_intersect = (m1 * x_intersect) + b1;
		if((x_intersect < Math.max(p1.getX(), p2.getX())) && (x_intersect > Math.min(p1.getX(), p2.getX())) && (y_intersect < Math.max(p1.getY(), p2.getY())) && (x_intersect > Math.min(p1.getY(), p2.getY())) && (x_intersect < Math.max(p3.getX(), p4.getX())) && (x_intersect > Math.min(p3.getX(), p4.getX())) && (y_intersect < Math.max(p3.getY(), p4.getY())) && (x_intersect > Math.min(p3.getY(), p4.getY()))){
		}else{
			return null;
		}
		return new Vector2d(x_intersect, y_intersect);
	}
	public void setP1(Vector2d p1) {
		this.p1 = p1;
	}
	public void setP2(Vector2d p2) {
		this.p2 = p2;
	}
}
