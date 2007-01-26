package com.jfysics.examples;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.jfysics.math.line.Line2d;
import com.jfysics.math.vector.Vector2d;
import com.jfysics.physics.bodies.BallObject;

public class BallAndLineTest extends JPanel
{
	int numLines = 2;
	int numBalls = 90;
	Vector2d gravity = new Vector2d(0, 0.05);
	BallObject[] balls = new BallObject[numBalls];
	Line2d[] lines = new Line2d[numLines];
	Line2d[] lineNormals = new Line2d[numLines];
    int width = 500;
	int height = 500;
	public static void main(String ... args){
		new BallAndLineTest();
	}
	public BallAndLineTest()
	{
		super();
		for(int i = 0; i < balls.length; i++){
			balls[i] = new BallObject(new Vector2d(Math.random() * (width-100) + 50, Math.random() * 100), 10);
			System.out.println(balls[i].getPosition());
		}
		/*for(int i = 0; i < lines.length; i++){
			lines[i] = new Line2d((int)(Math.random() * (width-100) + 50), (int)(Math.random() * (height - 200) + 150),(int)(Math.random() * (width - 100) + 50),(int)(Math.random() * (height - 200) + 150));
		}*/
		lines[0] = new Line2d(250, 300, 10, 150);
		lines[1] = new Line2d(250, 400, 450, 300);
		for(int i = 0; i < lines.length; i++){
			Vector2d normal = lines[i].getNormal();
			//Vector2d midpoint = lines[i].getMidPoint();
			Vector2d point1 = new Vector2d((normal.getX() * 10), (normal.getY() * 10));
			Vector2d point2 = new Vector2d(-(normal.getX() * 10), -(normal.getY() * 10));
			lineNormals[i] = new Line2d(point1, point2);
		}
		
		JFrame frame = new JFrame("Line Intersection Example");
		setPreferredSize(new Dimension(500,500));
		frame.getContentPane().add(this);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		Timer t = new Timer(10, new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				repaint();
				update();
			}
			
		});
		t.start();
	}
	public void paintComponent(Graphics g)
	{
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		for(Line2d line : lineNormals){
			g.setColor(Color.RED);
			g.drawLine((int)line.getP1().getX(),(int)line.getP1().getY(),(int)line.getP2().getX(),(int)line.getP2().getY());
		}
		for(Line2d line : lines){
			g.setColor(Color.BLACK);
			g.drawLine((int)line.getP1().getX(),(int)line.getP1().getY(),(int)line.getP2().getX(),(int)line.getP2().getY());
		}
		for(BallObject ball : balls){
			boolean isHit = false;
			for(int i = 0; i < lineNormals.length; i++){
				Line2d test = new Line2d(new Vector2d(), new Vector2d());
				test.setP1(lineNormals[i].getP1().add(ball.getPosition().add(new Vector2d(gravity.getX() * 4, gravity.getY() * 4))));
				test.setP2(lineNormals[i].getP2().add(ball.getPosition().add(new Vector2d(gravity.getX() * 4, gravity.getY() * 4))));
				g.setColor(Color.RED);
				g.drawLine((int)test.getP1().getX(),(int)test.getP1().getY(),(int)test.getP2().getX(),(int)test.getP2().getY());
				
				Vector2d intersect = lines[i].getIntersection(test);
				if(lines[i].isOnSegment(intersect, test)){
					isHit = true;
					Line2d normal = lineNormals[i];
					double lineAngle = Math.atan(normal.getSlope());
					double ballAngle = Math.atan(ball.getVelocity().getY() / ball.getVelocity().getX());
					double ballSpeed = ball.getVelocity().getDistance();
					double newAngle;
					System.out.println(ballAngle);
					if(lineAngle > 0 && lineAngle < Math.PI / 2){
						newAngle = ballAngle + (lineAngle + ballAngle);
					}else{
						newAngle = -ballAngle + (lineAngle - ballAngle);
					}
					Vector2d newVelocity = new Vector2d();
					newVelocity.setX(5);
					newVelocity.setY(5 * Math.tan(newAngle));
					newVelocity.normalize();
					newVelocity.setX(newVelocity.getX() * ballSpeed * .8d);
					newVelocity.setY(newVelocity.getY() * ballSpeed * .8d);
					ball.setVelocity(newVelocity);
					break;
				}
			}
			if(isHit){
				g.setColor(Color.BLACK);
				g.fillOval((int)ball.getPosition().getX() - 10, (int)ball.getPosition().getY() - 10, 20, 20);
				g.setColor(Color.GREEN);
				g.fillOval((int)ball.getPosition().getX() - 9, (int)ball.getPosition().getY() - 9, 18, 18);
				
			}else{
				g.setColor(Color.BLACK);
				g.fillOval((int)ball.getPosition().getX() - 10, (int)ball.getPosition().getY() - 10, 20, 20);
				g.setColor(Color.GRAY);
				g.fillOval((int)ball.getPosition().getX() - 9, (int)ball.getPosition().getY() - 9, 18, 18);
			}
		}
	}
	public void update(){
		for(BallObject bo : balls){
			bo.setVelocity(bo.getVelocity().add(gravity));
			bo.setPosition(bo.getPosition().add(bo.getVelocity()));
			if(bo.getPosition().getY() > height){
				bo.setPosition(new Vector2d(Math.random() * (width-100), -100));
				bo.setVelocity(new Vector2d(0, 0.01));
			}
		}
	}
}
