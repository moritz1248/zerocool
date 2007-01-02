package com.zerocool.menu;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

//will allow user to adjust a value with a slider
public class ZCslider implements ZCcomponent
{
	private Rectangle shape;
	private String name;
	//state: str = name / num = value
	private Value state, propChange;
	private ZCvisual image, slidepiece;
	private ZCbutton left, right;
	private boolean visible, showVal;
	private int min, max, increment;
	private ZCpage parent;
	
	public ZCslider()
	{
		shape = new Rectangle(15, 0, 55, 15);
		name = "blank";
		state = new Value(name, 0, false);
		propChange = new Value(name, 0, false);
		image = slidepiece = null;
		visible = true;
		min = 0;
		max = 10;
		increment = 1;
		showVal = true;
		left = new ZCbutton(new Rectangle(shape.x - 15, shape.y, 15, shape.height), "<", false, new Value(name, -1 * increment, false),true, null, null, null);
		right = new ZCbutton(new Rectangle(shape.x + shape.width, shape.y, 15, shape.height), ">", false, new Value(name, increment, false),true, null, null, null);
		parent = new ZCpage();
	}
	public ZCslider(Rectangle form, String nam, int minimum, int maximum, int startValue, int incrementer, boolean showValue, Value propC, ZCvisual img, ZCvisual slider, ZCbutton lButton, ZCbutton rButton)
	{
		shape = form;
		name = nam;
		min = minimum;
		max = maximum;
		increment = incrementer;
		showVal = showValue;
		state = new Value(name, startValue, false);
		propChange = propC;
		image = img;
		slidepiece = slider;
		visible = true;
		if(lButton == null || rButton == null)
		{
			left = new ZCbutton(new Rectangle(shape.x - 15, shape.y, 15, shape.height), "<", false, new Value(propC.getStr(), -1 * increment, false),true, null, null, null);
			right = new ZCbutton(new Rectangle(shape.x + shape.width, shape.y, 15, shape.height), ">", false, new Value(propC.getStr(), increment, false),true, null, null, null);
		}
		else
		{
			right = rButton;
			left = lButton;
		}
		parent = new ZCpage();
	}
	public boolean mousify(int a, int b, int type)
	{
		boolean isMousified = shape != null && shape.contains(a, b);
		if(isMousified && type == 0)
		{
			int width = (shape.width * increment) / (max - min + 1);
			int num = ((a - shape.x) / width) + min;
			parent.adjustProp(propChange.getStr(), new Value(null, num, false));
			return true;
		}
		else if(isMousified && type == 2)
		{
			int width = (shape.width * increment) / (max - min + 1);
			int num = ((a - shape.x) / width) + min;
			parent.adjustProp(propChange.getStr(), new Value(null, num, false));
			return true;
		}
		return isMousified;
	}
	public boolean keyify(int code, char key, int type)
	{
		if((code == KeyEvent.VK_LEFT || code == KeyEvent.VK_DOWN) && getState().getNum() > min && type == 1)
		{
			parent.adjustProp(propChange.getStr(), new Value(null, getState().getNum() - increment, false));
			return true;
		}
		if((code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_UP) && getState().getNum() < max && type == 1)
		{
			parent.adjustProp(propChange.getStr(), new Value(null, getState().getNum() + increment, false));
			return true;
		}
		return false;
	}
	public boolean isVisible()
	{
		return visible;
	}
	public Value getState()
	{
		return parent.getProp(propChange.getStr());
	}
	public void setPage(ZCpage page)
	{
		parent = page;
		if(parent != null)
		{
			parent.add(left);
			parent.add(right);
			parent.adjustProp(name, new Value(name, min, false));
			parent.addListener(name, this);
		}
	}
	public void shout(String name, Value value)
	{
		if(parent.getProp(propChange.getStr()).getNum() > max)
			parent.adjustProp(propChange.getStr(), new Value(null, max, false));
		else if(parent.getProp(propChange.getStr()).getNum() < min)
			parent.adjustProp(propChange.getStr(), new Value(null, min, false));
	}
	public void draw(Graphics2D g)
	{
		if(image != null)
			image.draw(g);
		else
		{
			g.setColor(Color.white);
			g.fill(shape);
			if(showVal)
			{
				g.setColor(Color.black);
				g.fill(new Rectangle(shape.x - 15, shape.y + shape.height, shape.width + 30, 18));
				g.setColor(Color.white);
				g.drawString(name + ": " + getState().getNum(), shape.x - 10, shape.y + shape.height + (shape.height / 2) + 6);
			}
		}
		if(slidepiece != null)
			slidepiece.draw(g);
		else
		{
			g.setColor(Color.blue);
			int width = ((shape.width) / (max - min + 1));
			int position = parent.getProp(propChange.getStr()).getNum() - min;
			g.draw(new Rectangle(shape.x + (position * width), shape.y, width - 1, shape.height));
		}
	}
}