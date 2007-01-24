package com.zerocool.menu;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

//will allow the user to select from a menu of items
public class ZCselector implements ZCcomponent
{
	private PolyShape shape;
	private String name;
	private Value state, propChange;
	private ZCvisual image;
	private ZCsubButton left, right;
	private boolean visible;
	private ZCpage parent;
	private ArrayList<String> values;
	private int selection;
	
	public ZCselector()
	{
		shape = new PolyShape(50, 50, 100, 15, PolyShape.SHAPE.RECTANGLE);
		name = "Standard";
		values = new ArrayList<String>();
		values.add("standard");
		values.add("nonStandard");
		state = new Value(values.get(0), 0, false);
		propChange = new Value(name, 0, false);
		image = null;
		visible = true;
		parent = null;
		left = new ZCsubButton(this, new PolyShape(shape.getX() - 15, shape.getY(), 15, shape.getHeight(), PolyShape.SHAPE.RECTANGLE), "<", false, new Value(propChange.getStr(), -1, false),true, null, null, null);
		right = new ZCsubButton(this, new PolyShape(shape.getX() + shape.getWidth(), shape.getY(), 15, shape.getHeight(), PolyShape.SHAPE.RECTANGLE), ">", false, new Value(propChange.getStr(), 1, false),true, null, null, null);
	}
	public ZCselector(PolyShape form, String nam, ArrayList<String> selections, int firstSelect, Value propC, ZCvisual img, ZCsubButton lButton, ZCsubButton rButton)
	{
		shape = form;
		name = nam;
		values = selections;
		if(values == null || values.size() == 0)
		{
			values = new ArrayList<String>();
			values.add("standard");
			values.add("nonStandard");
		}
		state = new Value(values.get(0), firstSelect, false);
		propChange = propC;
		image = img;
		visible = true;
		if(lButton == null || rButton == null)
		{
			left = new ZCsubButton(this, new PolyShape(shape.getX() - 15, shape.getY(), 15, shape.getHeight(), PolyShape.SHAPE.RECTANGLE), "<", false, new Value(propChange.getStr(), -1, false),true, null, null, null);
			right = new ZCsubButton(this, new PolyShape(shape.getX() + shape.getWidth(), shape.getY(), 15, shape.getHeight(), PolyShape.SHAPE.RECTANGLE), ">", false, new Value(propChange.getStr(), 1, false),true, null, null, null);
		}
		else
		{
			right = rButton;
			left = lButton;
		}
		parent = null;
	}
	public boolean mousify(int a, int b, int type)
	{
		boolean moused = shape.contains(a, b);
		moused |= left.mousify(a, b, type);
		moused |= right.mousify(a, b, type);
		return moused;
	}
	public boolean keyify(int code, char key, int type)
	{
		int num = getState().getNum();
		if((code == KeyEvent.VK_LEFT || code == KeyEvent.VK_UP) && num > 0 && type == 1)
		{
			parent.adjustProp(propChange.getStr(), new Value(values.get(num - 1), num - 1, false));
			return true;
		}
		if((code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_DOWN) && num < values.size() - 1 && type == 1)
		{
			parent.adjustProp(propChange.getStr(), new Value(values.get(num + 1), num + 1, false));
			return true;
		}
		return false;
	}
	public boolean isVisible()
	{
		return true;
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
			if(selection < values.size() && selection >= 0)
				parent.adjustProp(name, new Value(values.get(selection), selection, false));
			else
			{
				selection = 0;
				parent.adjustProp(name, new Value(values.get(selection), selection, false));
			}
		}
	}
	public void notify(ZCsubButton button, boolean clickOn)
	{
		int num = getState().getNum();
		if(button == left && num > 0 && clickOn)
		{
			parent.adjustProp(propChange.getStr(), new Value(values.get(num - 1), num - 1, false));
		}
		else if(button == right && num < values.size() - 1 && clickOn)
		{
			parent.adjustProp(propChange.getStr(), new Value(values.get(num + 1), num + 1, false));
		}
	}
	public void draw(Graphics2D g, boolean drawVertices)
	{
		g.setColor(Color.white);
		shape.fill(g, drawVertices);
		g.setColor(Color.black);
		g.drawString(getState().getStr(), shape.getX() + 5, shape.getY() + (shape.getHeight() / 2) + 6);
		left.draw(g, drawVertices);
		right.draw(g, drawVertices);
	}
}