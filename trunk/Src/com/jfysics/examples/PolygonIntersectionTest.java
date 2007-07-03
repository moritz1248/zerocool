package com.jfysics.examples;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.*;

import com.jfysics.math.line.Line2d;
import com.jfysics.math.vector.Vector2d;
import com.jfysics.physics.bodies.BoundingBox;
import com.jfysics.physics.bodies.polygon.RigidPolygon;

public class PolygonIntersectionTest extends JPanel{

	int poly_count = 20;
	boolean[] collide = new boolean[poly_count];
	boolean[] box_collide = new boolean[poly_count];
	RigidPolygon[] polygons = new RigidPolygon[poly_count];
	RigidPolygon[] polygonBuffer = new RigidPolygon[poly_count];
	BoundingBox b_left, b_right, b_top, b_bottom;
	Color[] colors = new Color[poly_count];
	
	Vector2d[] velocities = new Vector2d[poly_count];
	
    int width = 500;
	int height = 500;
	public static void main(String ... args){
		new PolygonIntersectionTest();
	}
	public PolygonIntersectionTest(){
		super();
		
		b_left = new BoundingBox(new Vector2d(-10, -10), new Vector2d(0, 510));
		b_right = new BoundingBox(new Vector2d(500, -10), new Vector2d(510, 510));
		b_top = new BoundingBox(new Vector2d(-10, -10), new Vector2d(510, 0));
		b_bottom = new BoundingBox(new Vector2d(-10, 500), new Vector2d(510, 510));
		
		ArrayList<Vector2d> vectorList;
		for(int j = 0; j < poly_count; j++)
		{
			box_collide[j] = false;
			collide[j] = false;
			vectorList = new ArrayList<Vector2d>();
			for(int i = 0; i < 10; i++)
			{
				vectorList.add(new Vector2d(Math.cos(Math.toRadians(36 * i)) * (Math.random() * 30 + 5), Math.sin(Math.toRadians(36 * i)) * (Math.random() * 30 + 5)));
			}
			colors[j] = new Color((int)(Math.random() * 255), (int)(Math.random() * 255), (int)(Math.random() * 255));
			velocities[j] = new Vector2d(.5d * Math.cos(Math.random() * 2 * Math.PI),.5d *  Math.sin(Math.random() * 2 * Math.PI));
			polygons[j] = new RigidPolygon(vectorList);
			polygons[j].setPosition(new Vector2d(Math.random() * 350 + 50,Math.random() * 350 + 50));
			polygons[j].setRotationalVelocity(-0.02);
		}
		
		JFrame frame = new JFrame("Line Intersection Example");
		setPreferredSize(new Dimension(width,height));
		frame.getContentPane().add(this);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		Timer t = new Timer(10, new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				repaint();
			}
			
		});
		frame.addKeyListener(new KeyListener(){

			public void keyPressed(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {}
			
		});
		t.start();
	}
	public void createPolygonBuffer()
	{
		if(polygonBuffer == null)
		{
			polygonBuffer = new RigidPolygon[poly_count];
		}
		RigidPolygon p;
		for(int j = 0; j < polygons.length; j++)
		{
			p = polygons[j];
			double x1, y1, x2, y2;
			Vector2d loc = p.getPosition();
			ArrayList<Vector2d> vectors = p.getVectors();
			double f = p.getRotation();
			ArrayList<Vector2d> polyVec = new ArrayList<Vector2d>();
			for(int i = 0; i < p.getPointCount(); i++)
			{
				Vector2d temp = vectors.get(i);
				
				x1 = temp.getX();
				y1 = temp.getY();
				x2 = (int)(x1 * Math.cos(f) - y1 * Math.sin(f));
				y2 = (int)(y1 * Math.cos(f) + x1 * Math.sin(f));
				polyVec.add(new Vector2d(x2, y2));
			}
			polygonBuffer[j] = new RigidPolygon(polyVec);
			polygonBuffer[j].setPosition(loc);
		}
	}
	
	public void calculateSpin(int polygon1, Vector2d collision, int polygon2) {

		double c = 1000000;
		
		double i1 = 10;
		
		double i2 = 10;
		
		double m1 = 10;
		
		double w1 = polygons[polygon1].getRotationalVelocity();
		
		Vector2d vel1 = velocities[polygon1];
		
		Vector2d loc1 = polygons[polygon1].getPosition();
		
		double m2 = 10;
		
		double w2 = polygons[polygon2].getRotationalVelocity();

		Vector2d vel2 = velocities[polygon2];
		
		Vector2d loc2 = polygons[polygon2].getPosition();
		
		double dPx = m1 * vel1.getX() + m2 * vel2.getX();

		double dPy = m1 * vel1.getY() + m2 * vel2.getY();

		// constants to minimize repeat calculations

		double wdx1 = collision.getX() - loc1.getX();

		double wdy1 = collision.getY() - loc1.getY();

		double wdx2 = collision.getX() - loc2.getX();

		double wdy2 = collision.getY() - loc2.getY();

		double wH1 = Math.sqrt(wdx1 * wdx1 + wdy1 * wdy1);

		double wH2 = Math.sqrt(wdx2 * wdx2 + wdy2 * wdy2);

		// the change in momentum

		dPx += (wdx1 * i1 * w1 / wH1) + (wdx2 * i2 * w2 / wH2);

		dPy += (wdy1 * i1 * w1 / wH1) + (wdy2 * i2 * w2 / wH2);

		// more constants to minimize repeat calculations

		double k1 = wdx1 / wH1;

		double q1 = Math.sqrt((dPx * dPx + dPy * dPy) / (2 * k1 * k1));

		double tan1 = wdy1 / wdx1;

		double dVx1 = dPy - q1;

		double dVy1 = dVx1 * tan1;

		double dW1 = q1;

		double k2 = wdx2 / wH2;

		double q2 = Math.sqrt((dPx * dPx + dPy * dPy) / (2 * k2 * k2));

		double tan2 = wdy2 / wdx2;

		double dVx2 = dPy - q2;

		double dVy2 = dVx2 * tan2;

		double dW2 = q2;

		// now you can set the variables
		
		vel1 = vel1.add(new Vector2d(dVx1/c, dVy1/c));
		
		velocities[polygon1] = vel1;

		w1 += dW1/c;
		
		polygons[polygon1].setRotationalVelocity(w1);

		vel2 = vel2.add(new Vector2d(dVx2/c, dVy2/c));
		
		velocities[polygon2] = vel2;
		

		w2 += dW2/c;
		polygons[polygon2].setRotationalVelocity(w2);

	}
	
	
	public void paintComponent(Graphics g){
		createPolygonBuffer();
		g.setColor(Color.WHITE);
		g.fillRect(0,0,width,height);
		for(int i = 0; i < polygonBuffer.length; i++)
		{
			if(polygons[i].getRotationalVelocity() != -0.02)
				System.out.println("WTF " + polygons[i].getRotationalVelocity());
			
			polygons[i].setRotation(polygons[i].getRotation() + polygons[i].getRotationalVelocity());
			polygons[i].setPosition(polygons[i].getPosition().add(velocities[i]));
			if(polygons[i].getBounds().collides(b_top))
				velocities[i].multY(-1);
			if(polygons[i].getBounds().collides(b_bottom))
				velocities[i].multY(-1);
			if(polygons[i].getBounds().collides(b_left))
				velocities[i].multX(-1);
			if(polygons[i].getBounds().collides(b_right))
				velocities[i].multX(-1);
			boolean boxIsTouching = false, polyIsTouching = false;
			for(int j = i; j < polygonBuffer.length; j++)
				if((i != j) && (polygonBuffer[i].getBounds().collides(polygonBuffer[j].getBounds())))
				{
					boxIsTouching = true;
					box_collide[i] = true;
					box_collide[j] = true;
					break;
				}
			if(box_collide[i])
			{
				
				for(int j = i; j < polygonBuffer.length; j++)
				{
					Vector2d intersect = polygonBuffer[i].checkCollide(polygonBuffer[j]);
					if((i != j) && (intersect != null))
					{
						if(!collide[i])
							collide[i] = true;
						if(!collide[j])
							collide[j] = true;
						g.setColor(Color.black);
						g.drawOval((int)intersect.getX()-5, (int)intersect.getY() - 5, 10, 10);
						calculateSpin(i, intersect, j);
						polyIsTouching = true;
						break;
					}
				}
			}
			if(box_collide[i])
				drawBoundingBox(g, polygonBuffer[i], Color.red);
			if(collide[i])
				drawPolygon(g, polygonBuffer[i], false, Color.red);
			else
				drawPolygon(g, polygonBuffer[i], false, Color.lightGray);
			drawPolygon(g, polygonBuffer[i], true, Color.black);
			collide[i] = false;
			box_collide[i] = false;
		}
		g.setColor(Color.BLACK);

		g.drawString("The " + poly_count + " randomly generated polygonBuffer will turn red when colliding.", 10, 20);
		
	}
	
	public void drawBoundingBox(Graphics g, RigidPolygon poly, Color color)
	{
		BoundingBox box = poly.getBounds();
		g.setColor(color);
		g.drawRect((int)(box.getMinimum().getX()), (int)(box.getMinimum().getY()), (int)box.getWidth(), (int)box.getHeight());
	}
	
	public void drawPolygon(Graphics g, RigidPolygon polygon, boolean isOutline, Color color)
	{
		int[] x = new int[polygon.getPointCount()];
		int[] y = new int[polygon.getPointCount()];
		double x1, y1;
		Vector2d loc = polygon.getPosition();
		ArrayList<Vector2d> vectors = polygon.getVectors();
		for(int i = 0; i < polygon.getPointCount(); i++)
		{
			Vector2d temp = vectors.get(i);
			double f = polygon.getRotation();
			x1 = temp.getX();
			y1 = temp.getY();
			x[i] =(int)(x1 + loc.getX());
			y[i] =(int)(y1 + loc.getY());
			//x[i] = (int)(x1 * Math.cos(f) - y1 * Math.sin(f) + loc.getX());
			//y[i] = (int)(y1 * Math.cos(f) + x1 * Math.sin(f) + loc.getY());
		}
		g.setColor(color);
		if(isOutline)
			g.drawPolygon(x, y, polygon.getPointCount());
		else
			g.fillPolygon(x, y, polygon.getPointCount());
	}
}
