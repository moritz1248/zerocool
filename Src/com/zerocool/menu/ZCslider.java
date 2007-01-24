package com.zerocool.menu;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

//will allow user to adjust a value with a slider
public class ZCslider implements ZCcomponent
{
	private PolyShape shape;
	private String name;
	//state: str = name / num = value
	private Value state, propChange;
	private ZCvisual image, slidepiece;
	private ZCsubButton left, right;
	private boolean visible, showVal;
	private int min, max, increment;
	private ZCpage parent;
	
	public ZCslider()
	{
		shape = new PolyShape(15, 0, 55, 15, PolyShape.SHAPE.RECTANGLE);
		name = "blank";
		state = new Value(name, 0, false);
		propChange = new Value(name, 0, false);
		image = slidepiece = null;
		visible = true;
		min = 0;
		max = 10;
		increment = 1;
		showVal = true;
		left = new ZCsubButton(this, new PolyShape(shape.getX() - 15, shape.getY(), 15, shape.getHeight(), PolyShape.SHAPE.RECTANGLE), "<", false, new Value(name, -1 * increment, false),true, null, null, null);
		right = new ZCsubButton(this, new PolyShape(shape.getX() + shape.getWidth(), shape.getY(), 15, shape.getHeight(), PolyShape.SHAPE.RECTANGLE), ">", false, new Value(name, increment, false),true, null, null, null);
		parent = null;
	}
	public ZCslider(PolyShape form, String nam, int minimum, int maximum, int startValue, int incrementer, boolean showValue, Value propC, ZCvisual img, ZCvisual slider, ZCsubButton lButton, ZCsubButton rButton)
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
			left = new ZCsubButton(this, new PolyShape(shape.getX() - 15, shape.getY(), 15, shape.getY(), PolyShape.SHAPE.RECTANGLE), "<", false, new Value(propC.getStr(), -1 * increment, false),true, null, null, null);
			right = new ZCsubButton(this, new PolyShape(shape.getX() + shape.getWidth(), shape.getY(), 15, shape.getHeight(), PolyShape.SHAPE.RECTANGLE), ">", false, new Value(propC.getStr(), increment, false),true, null, null, null);
		}
		else
		{
			right.setParent(this);
			left.setParent(this);
			right = rButton;
			left = lButton;
		}
		parent = null;
	}
	public boolean mousify(int a, int b, int type)
	{
		boolean isMousified = shape != null && shape.contains(a, b);
		if(isMousified && type == 0)
		{
			int width = (shape.getWidth() * increment) / (max - min + 1);
			int num = ((a - shape.getX()) / width) + min;
			parent.adjustProp(propChange.getStr(), new Value(null, num, false));
			return true;
		}
		else if(isMousified && type == 2)
		{
			int width = (shape.getWidth() * increment) / (max - min + 1);
			int num = ((a - shape.getX()) / width) + min;
			parent.adjustProp(propChange.getStr(), new Value(null, num, false));
			return true;
		}
		isMousified |= left.mousify(a, b, type);
		isMousified |= right.mousify(a, b, type);
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
	public PolyShape getShape()
	{
		return shape;
	}
	public void translate(int x, int y)
	{
		shape.translate(x, y);
		left.translate(x, y);
		right.translate(x, y);
	}
	public void setPage(ZCpage page)
	{
		parent = page;
		if(parent != null)
		{
			parent.adjustProp(name, new Value(name, min, false));
		}
	}
	public void notify(ZCsubButton button, boolean clickOn)
	{
		int num = getState().getNum();
		if(button == left && clickOn)
		{
			if(num < min + increment)
				parent.adjustProp(name, new Value(null, min, false));
			else
				parent.adjustProp(name, new Value(null, num - increment, false));
		}
		else if(button == right && clickOn)
		{
			if(num > max - increment)
				parent.adjustProp(name, new Value(null, max, false));
			else
				parent.adjustProp(name, new Value(null, num + increment, false));
		}
	}
	public void draw(Graphics2D g, boolean drawVertices)
	{
		if(image != null)
			image.draw(g, drawVertices);
		else
		{
			g.setColor(Color.white);
			shape.fill(g, drawVertices);
			if(showVal)
			{
				g.setColor(Color.black);
				g.fill(new Rectangle(shape.getX() - 15, shape.getY() + shape.getHeight(), shape.getWidth() + 30, 18));
				g.setColor(Color.white);
				g.drawString(name + ": " + getState().getNum(), shape.getX() - 10, shape.getY() + shape.getHeight() + (shape.getHeight() / 2) + 6);
			}
		}
		if(slidepiece != null)
			slidepiece.draw(g, false);
		else
		{
			g.setColor(Color.blue);
			int width = ((shape.getWidth()) / (max - min + 1));
			int position = parent.getProp(propChange.getStr()).getNum() - min;
			g.draw(new Rectangle(shape.getX() + (position * width), shape.getY(), width - 1, shape.getHeight()));
		}
		left.draw(g, drawVertices);
		right.draw(g, drawVertices);
	}
}