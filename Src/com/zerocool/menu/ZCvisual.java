package com.zerocool.menu;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

//will cover images and text, static and dynamic
public class ZCvisual implements ZCcomponent
{
	private ZCpage parent;
	private Shape shape;
	private ArrayList<Step> steps;
	private boolean visible, useTimer;
	
	public ZCvisual()
	{
		visible = true;
		parent = null;
		shape = new Rectangle(300, 300, 200, 200);
		steps = new ArrayList<Step>();
		steps.add(new Step(0, null, Color.black, false, null));
		useTimer = false;
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
	public void setPage(ZCpage page)
	{
		parent = page;
		if(useTimer)
			parent.setTimer();
	}
	public void shout(String name, Value value)
	{
		//add functionality to change graphics due to value changes?
	}
	//draw without any offset
	public void draw(Graphics2D g)
	{
		for(Step s : steps)
			s.draw(g, 0, 0);
	}
	//draw with offset a,b or centered at a,b
	public void draw(Graphics2D g, int a, int b, boolean isCentered)
	{
		if(isCentered)
			for(Step s : steps)
				s.draw(g, (new Double(a - (shape.getBounds().getWidth() / 2))).intValue(), (new Double(b - (shape.getBounds().getHeight()))).intValue());
		else
			for(Step s : steps)
				s.draw(g, a, b);
	}
	
	//PRIVATE CLASS
	
	//here is where the power lies
	private class Step
	{
		//x# and y# can represent location and/or width/height and/or something else
		private int type;
		private int[][] props;
		private Color color;
		private volatile BufferedImage image;
		private boolean dynamic;
		
		//Each step is one piece of visual code
		private Step()
		{
			type = 0;
			color = Color.black;
			props = new int[2][2];
			props[0][1] = 10;
			props[1][1] = 10;
			image = null;
			dynamic = false;
		}
		private Step(int stepType, int[][] properties, Color c, boolean changing, BufferedImage bufferedImage)
		{
			type = stepType;
			color = c;
			if(props != null)
				props = properties;
			else
			{
				props = new int[2][2];
				Rectangle2D bounds = shape.getBounds();
				props[0][0] = (new Double(bounds.getX())).intValue();
				props[1][0] = (new Double(bounds.getY())).intValue();
				props[0][1] = (new Double(bounds.getWidth())).intValue();
				props[1][1] = (new Double(bounds.getHeight())).intValue();
				
			}
			dynamic = changing;
			image = bufferedImage;
		}
		
		//draw this step with offset (a,b)
		public void draw(Graphics2D g, int a, int b)
		{
			System.out.println("Drawing step type: " + type);
			g.setColor(color);
			switch(type)
			{
				//yer basic circle
				case 0:
					if(props.length >= 2 && props[0].length >= 2)
						g.fillOval(props[0][0] + a, props[1][0] + b, props[0][1], props[1][1]);
					break;
				//yer basic line
				case 1:
					if(props.length >= 2 && props[0].length >= 2)
						g.drawLine(props[0][0] + a, props[1][0] + b, props[0][1] + a, props[1][1] + b);
				default:
					g.drawImage(image, null, null);
			}
		}
	}
}