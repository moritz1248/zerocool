package com.zerocool.menu;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.io.Serializable;

//will cover images and text, static and dynamic
public class ZCvisual implements ZCcomponent
{
	private ZCpage parent;
	private PolyShape shape;
	private ArrayList<Step> steps;
	private boolean visible, useTimer;
	
	public ZCvisual()
	{
		visible = true;
		parent = null;
		shape = new PolyShape(300, 300, 100, 100, PolyShape.SHAPE.LINE);
		steps = new ArrayList<Step>();
		steps.add(new Step(null, Color.black, false, null));
		useTimer = false;
	}
	public ArrayList<Step> getSteps()
	{
		return steps;
	}
	public boolean mousify(int a, int b, int type)
	{
		return false;
	}
	public boolean keyify(int code, char key, int type)
	{
		return false;
	}
	public boolean isVisible()
	{
		return visible;
	}
	public Value getState()
	{
		return null;
	}
	public PolyShape getShape()
	{
		return shape;
	}
	public void translate(int x, int y)
	{
		shape.translate(x, y);
	}
	public void setPage(ZCpage page)
	{
		parent = page;
		if(useTimer)
			parent.setTimer();
	}
	public void notify(ZCsubButton button, boolean clickOn)
	{
	}
	//draw without any offset
	public void draw(Graphics2D g, boolean drawVertices)
	{
		for(Step s : steps)
			s.draw(g, 0, 0);
		if(drawVertices)
		{
			g.setColor(PolyShape.color);
			shape.draw(g, drawVertices);
		}
	}
	//draw with offset a,b or centered at a,b
	public void draw(Graphics2D g, int a, int b, boolean isCentered, boolean drawVertices)
	{
		if(isCentered)
			for(Step s : steps)
				s.draw(g, shape.getCenterX(), shape.getCenterY());
		else
			for(Step s : steps)
				s.draw(g, a, b);
		if(drawVertices)
		{
			g.setColor(PolyShape.color);
			shape.draw(g, drawVertices);
		}
	}
	
	//PRIVATE CLASS
	
	//here is where the power lies
	public class Step implements Serializable
	{
		//x# and y# can represent location and/or width/height and/or something else
		private int[][] props;
		private PolyShape piece;
		private Color color;
		private volatile BufferedImage image;
		private boolean dynamic;
		
		//Each step is one piece of visual code
		private Step()
		{
			color = Color.black;
			image = null;
			dynamic = false;
		}
		private Step(PolyShape form, Color c, boolean changing, BufferedImage bufferedImage)
		{
			if(form == null)
				piece = shape;
			else
				piece = form;
			color = c;
			dynamic = changing;
			image = bufferedImage;
		}
		
		//draw this step with offset (a,b)
		public void draw(Graphics2D g, int a, int b)
		{
			g.setColor(color);
			if(image != null)
				g.drawImage(image, null, null);
			else
				piece.fill(g, false);
		}
		
		public PolyShape getShape()
		{
			return piece;
		}
	}
}