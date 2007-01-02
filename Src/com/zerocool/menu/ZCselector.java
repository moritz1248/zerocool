package com.zerocool.menu;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;
import java.util.ArrayList;

//will allow the user to select from a menu of items
public class ZCselector implements ZCcomponent
{
	private Rectangle shape;
	private String name;
	private Value state, propChange;
	private ZCvisual image;
	private ZCbutton left, right;
	private boolean visible;
	private ZCpage parent;
	private ArrayList<String> values;
	private int selection;
	
	public ZCselector()
	{
		shape = new Rectangle(50, 50, 100, 15);
		name = "Standard";
		values = new ArrayList<String>();
		values.add("standard");
		state = new Value(values.get(0), 0, false);
		propChange = new Value(name, 0, false);
		image = null;
		visible = true;
		parent = null;
		left = new ZCbutton(new Rectangle(shape.x - 15, shape.y, 15, shape.height), "<", false, new Value(propChange.getStr(), -1, false),true, null, null, null);
		right = new ZCbutton(new Rectangle(shape.x + shape.width, shape.y, 15, shape.height), ">", false, new Value(propChange.getStr(), 1, false),true, null, null, null);
	}
	public ZCselector(Rectangle form, String nam, ArrayList<String> selections, int firstSelect, Value propC, ZCvisual img, ZCbutton lButton, ZCbutton rButton)
	{
		shape = form;
		name = nam;
		values = selections;
		if(values == null || values.size() == 0)
		{
			values = new ArrayList<String>();
			values.add("Standard");
		}
		state = new Value(values.get(0), firstSelect, false);
		propChange = propC;
		image = img;
		visible = true;
		if(lButton == null || rButton == null)
		{
			left = new ZCbutton(new Rectangle(shape.x - 15, shape.y, 15, shape.height), "<", false, new Value(propChange.getStr(), -1, false),true, null, null, null);
			right = new ZCbutton(new Rectangle(shape.x + shape.width, shape.y, 15, shape.height), ">", false, new Value(propChange.getStr(), 1, false),true, null, null, null);
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
		return false;
	}
	public boolean keyify(int code, char key, int type)
	{
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
	public void setPage(ZCpage page)
	{
		parent = page;
		if(parent != null)
		{
			parent.add(left);
			parent.add(right);
			if(selection < values.size() && selection >= 0)
				parent.adjustProp(name, new Value(values.get(selection), selection, false));
			else
			{
				selection = 0;
				parent.adjustProp(name, new Value(values.get(selection), selection, false));
			}
			parent.addListener(name, this);
		}
	}
	public void shout(String name, Value value)
	{
		if(name.equals(propChange.getStr()))
		{
			if(value.getNum() >= values.size())
				value.setNum(values.size() - 1);
			else if(value.getNum() < 0)
				value.setNum(0);
			if(value.getNum() != selection)
			{
				selection = value.getNum();
				parent.adjustProp(name, new Value(values.get(selection), value.getNum(), false));
			}
		}
	}
	public void draw(Graphics2D g)
	{
		g.setColor(Color.white);
		g.fill(shape);
		g.setColor(Color.black);
		g.drawString(getState().getStr(), shape.x + 5, shape.y + (shape.height / 2) + 6);
	}
}