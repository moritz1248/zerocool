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

import org.lwjgl.Sys;

import com.jfysics.math.line.Line2d;
import com.jfysics.math.vector.Vector2d;
import com.jfysics.physics.bodies.BoundingBox;
import com.jfysics.physics.bodies.polygon.RigidPolygon;

public class PolygonIntersectionStressTest extends JPanel{

	int frames = 0;
	int lastFPS = 0;
	int bestFPS = 0;
	int worstFPS = 1000;
	long lastTime = 0;
	int poly_count = 100;
	int display_count = 50;
	boolean[] collide = new boolean[poly_count];
	boolean[] box_collide = new boolean[poly_count];
	RigidPolygon[] polygons = new RigidPolygon[poly_count];
	BoundingBox b_left, b_right, b_top, b_bottom;
	Color[] colors = new Color[poly_count];
	Vector2d[] velocities = new Vector2d[poly_count];
    int width = 500;
	int height = 500;
	public static void main(String ... args){
		new PolygonIntersectionStressTest();
	}
	public PolygonIntersectionStressTest(){
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
		}
		
		JFrame frame = new JFrame("Line Intersection Example");
		setPreferredSize(new Dimension(width,height));
		frame.getContentPane().add(this);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		lastTime = System.currentTimeMillis();
		Timer t = new Timer(10, new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				
				repaint();
			}
			
		});
		
		Timer t2 = new Timer(2000, new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				display_count+=3;
			}
			
		});
		
		frame.addKeyListener(new KeyListener(){

			public void keyPressed(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {}
			
		});
		t.start();
		t2.start();
	}
	
	
	public void paintComponent(Graphics g){
		g.setColor(Color.WHITE);
		g.fillRect(0,0,width,height);
		for(int i = 0; i < display_count; i++)
		{
			polygons[i].setPosition(polygons[i].getPosition().add(velocities[i]));
			if(polygons[i].getBounds().collides(b_top))
				velocities[i].multY(-1);
			if(polygons[i].getBounds().collides(b_bottom))
				velocities[i].multY(-1);
			if(polygons[i].getBounds().collides(b_left))
				velocities[i].multX(-1);
			if(polygons[i].getBounds().collides(b_right))
				velocities[i].multX(-1);
			for(int j = i; j < display_count; j++)
				if((i != j) && (polygons[i].getBounds().collides(polygons[j].getBounds())))
				{
					box_collide[i] = true;
					box_collide[j] = true;
					break;
				}
			if(box_collide[i])
			{
				
				for(int j = i; j < display_count; j++)
				{
					Vector2d intersect = polygons[i].checkCollide(polygons[j]);
					if((i != j) && (intersect != null))
					{
						if(!collide[i])
							collide[i] = true;
						if(!collide[j])
							collide[j] = true;
						break;
					}
				}
			}
			if(box_collide[i])
				drawBoundingBox(g, polygons[i], Color.red);
			if(collide[i])
				drawPolygon(g, polygons[i], false, Color.red);
			else
				drawPolygon(g, polygons[i], false, Color.lightGray);
			drawPolygon(g, polygons[i], true, Color.black);
			collide[i] = false;
			box_collide[i] = false;
		}
		g.setColor(Color.BLACK);
		frames++;
		if(System.currentTimeMillis() - lastTime > 1000)
		{
			lastTime = System.currentTimeMillis();
			lastFPS = frames;
			frames = 0;
			if(lastFPS > bestFPS)
				bestFPS = lastFPS;
			if(lastFPS < worstFPS)
				worstFPS = lastFPS;
		}
		g.drawString("The " + poly_count + " randomly generated polygons will turn red when colliding.", 10, 20);
		g.drawString("FPS: " + lastFPS + "   best: " + bestFPS + "   worst: " + worstFPS + "   display count: " + display_count, 10, 30);

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
		Vector2d loc = polygon.getPosition();
		ArrayList<Vector2d> vectors = polygon.getVectors();
		for(int i = 0; i < polygon.getPointCount(); i++)
		{
			Vector2d temp = vectors.get(i);
			x[i] = (int)(temp.getX() + loc.getX());
			y[i] = (int)(temp.getY() + loc.getY());
		}
		g.setColor(color);
		if(isOutline)
			g.drawPolygon(x, y, polygon.getPointCount());
		else
			g.fillPolygon(x, y, polygon.getPointCount());
	}
}
