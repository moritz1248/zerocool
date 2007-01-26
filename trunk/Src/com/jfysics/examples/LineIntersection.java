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
	Line2d[] lines = new Line2d[10];
	public static void main(String ... args){
		new LineIntersection();
	}
	public LineIntersection(){
		super();
		for(int i = 0; i < lines.length; i++){
			lines[i] = new Line2d((int)(Math.random() * 400 + 50), (int)(Math.random() * 400 + 50),(int)(Math.random() * 400 + 50),(int)(Math.random() * 400 + 50));
		}
		JFrame frame = new JFrame("Line Intersection Example");
		setPreferredSize(new Dimension(500,500));
		frame.getContentPane().add(this);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		Timer t = new Timer(1, new ActionListener(){

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
		g.clearRect(0,0,500,500);
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

					if(lines[i].intersects(lineTest) != null){
						amount++;
						g.setColor(Color.BLACK);
						g.fillOval((int)lines[i].intersects(lineTest).getX() - 5, (int)lines[i].intersects(lineTest).getY() - 5, 10, 10);
						g.setColor(Color.GREEN);
						g.fillOval((int)lines[i].intersects(lineTest).getX() - 4, (int)lines[i].intersects(lineTest).getY() - 4, 8, 8);
					}else{
						g.setColor(Color.BLACK);
						g.fillOval((int)lines[i].intersects(lineTest).getX() - 5, (int)lines[i].intersects(lineTest).getY() - 5, 10, 10);
						g.setColor(Color.GRAY);
						g.fillOval((int)lines[i].intersects(lineTest).getX() - 4, (int)lines[i].intersects(lineTest).getY() - 4, 8, 8);

					}
			}
		g.setColor(Color.BLACK);
		g.drawString(amount + " intersections.", 10, 10);
	}
}
