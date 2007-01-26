package com.jfysics.examples;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.*;

import com.jfysics.math.line.Line2d;
import com.jfysics.math.vector.Vector2d;

public class LineIntersection extends JPanel{
	Line2d lineTest = new Line2d(150, 50, 150, 100);
	int numLines = 30;
	Line2d[] lines = new Line2d[numLines];
	Line2d[] lineNormals = new Line2d[numLines];
    int width = 500;
	int height = 500;
	public static void main(String ... args){
		new LineIntersection();
	}
	public LineIntersection(){
		super();
		for(int i = 0; i < lines.length; i++){
			lines[i] = new Line2d((int)(Math.random() * (width-100) + 50), (int)(Math.random() * (height - 100) + 50),(int)(Math.random() * (width - 100) + 50),(int)(Math.random() * (height - 100) + 50));
		}
		for(int i = 0; i < lines.length; i++){
			Vector2d normal = lines[i].getNormal();
			Vector2d midpoint = lines[i].getMidPoint();
			Vector2d point1 = new Vector2d(midpoint.getX() + (normal.getX() * 5), midpoint.getY() + (normal.getY() * 5));
			Vector2d point2 = new Vector2d(midpoint.getX() - (normal.getX() * 5), midpoint.getY() - (normal.getY() * 5));
			lineNormals[i] = new Line2d(point1, point2);
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
		t.start();
		addMouseMotionListener(new MouseMotionListener(){

			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				lineTest.getP2().setX(e.getX());
				lineTest.getP2().setY(e.getY());
			}
			
		});
		addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent e) {
				lineTest.setP1(new Vector2d(e.getX(), e.getY()));
				
			}

			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	public void paintComponent(Graphics g){
		g.clearRect(0,0,width,height);
		/*for(Line2d line : lineNormals){
			g.setColor(Color.RED);
			g.drawLine((int)line.getP1().getX(),(int)line.getP1().getY(),(int)line.getP2().getX(),(int)line.getP2().getY());
		}*/
		for(Line2d line : lines){
			g.setColor(Color.BLACK);
			g.drawLine((int)line.getP1().getX(),(int)line.getP1().getY(),(int)line.getP2().getX(),(int)line.getP2().getY());
		}
		g.setColor(Color.BLUE);
		g.drawLine((int)lineTest.getP1().getX(),(int)lineTest.getP1().getY(),(int)lineTest.getP2().getX(),(int)lineTest.getP2().getY());
		int amount = 0;
		if(lineTest != null)
			
			for(int i = 0; i < lines.length; i++){

				if(lines[i] != null)
				{
					Vector2d intersect = lines[i].getIntersection(lineTest);
					if(lines[i].isOnSegment(intersect, lineTest)){
						amount++;
						g.setColor(Color.BLACK);
						g.fillOval((int)intersect.getX() - 5, (int)intersect.getY() - 5, 10, 10);
						g.setColor(Color.GREEN);
						g.fillOval((int)intersect.getX() - 4, (int)intersect.getY() - 4, 8, 8);
					}/*else{
						g.setColor(Color.BLACK);
						g.fillOval((int)intersect.getX() - 5, (int)intersect.getY() - 5, 10, 10);
						g.setColor(Color.RED);
						g.fillOval((int)intersect.getX() - 4, (int)intersect.getY() - 4, 8, 8);
					}*/
				}
			}
		g.setColor(Color.BLACK);
		g.drawString(amount + " intersections.", 10, 10);
		g.setColor(Color.BLUE);
		g.drawString("Click within the window to change the starting point for the line.", 10, 20);
		
	}
}
