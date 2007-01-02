package com.zerocool.menu;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

//will allow a user to use a button
public class ZCbutton implements ZCcomponent
{
	private Shape shape;
	private String name;
	//state: str=name, num=state
	//0 = nothing
	//1 = highlighted
	//2 = selected
	//3???selected and hightlighted
	private Value state, propChange;
	private ZCvisual image, selection, highlight;
	private boolean visible;
	//if toIncrement == true then value += state.num else value = state.num or 0
	private boolean toIncrement;
	private ZCpage parent;
	
	public ZCbutton()
	{
		shape = null;
		name = "blank";
		visible = false;
		state = new Value(name, 0, false);
		image = selection = highlight = null;
		propChange = new Value(name, 1, false);
		toIncrement = false;
	}
	public ZCbutton(Shape form,String nam,boolean selected,Value propC,boolean inc, ZCvisual img, ZCvisual select, ZCvisual high)
	{
		shape = form;
		name = nam;
		visible = true;
		if(selected)
			state = new Value(name, 2, false);
		else
			state = new Value(name, 0, false);
		propChange = propC;
		toIncrement = inc;
		image = img;
		selection = select;
		highlight = high;
	}
	public boolean mousify(int a, int b, int type)
	{
		boolean isMousified = shape != null && shape.contains(a, b);
		if(isMousified && type == 0 && state.getNum() < 2)
		{
			if(toIncrement)
			{
				System.out.print("value: " + propChange.getStr() + " incremented by " + propChange.getNum() + " - changed to: ");
				System.out.println(parent.getProp(propChange.getStr()));
				parent.adjustProp(propChange.getStr(), new Value(null, propChange.getNum() + parent.getProp(propChange.getStr()).getNum(), false));
				state.setNum(1);
			}
			else
			{
				parent.adjustProp(propChange.getStr(), new Value(null, propChange.getNum(), false));
				state.setNum(2);
			}
		}
		else if(isMousified && type == 0 && state.getNum() == 2)
		{
			state.setNum(1);
			if(!toIncrement)
				parent.adjustProp(propChange.getStr(), new Value(null, 0, false));
		}
		else if(isMousified && type == 1 && state.getNum() == 0)
			state.setNum(1);
		else if(!isMousified && type == 1 && state.getNum() == 1)
			state.setNum(0);
		else if(!isMousified && type == 0 && state.getNum() > 0 && toIncrement)
			state.setNum(0);
		return isMousified;
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
		return state;
	}
	public void setPage(ZCpage page)
	{
		parent = page;
	}
	public void shout(String name, Value value)
	{
	}
	public void draw(Graphics2D g)
	{
		if(image != null)
			image.draw(g);
		else
		{
			g.setColor(Color.black);
			g.fill(shape);
			g.setColor(Color.white);
			Rectangle bounds = shape.getBounds();
			g.drawString(name, bounds.x + 5, bounds.y + (bounds.height / 2) + 6);
		}
		if(state.getNum() == 1 && highlight != null)
			highlight.draw(g);
		else if(state.getNum() == 1 && highlight == null)
		{
			g.setColor(Color.yellow);
			g.draw(shape);
		}
		else if(state.getNum() == 2 && selection != null)
			selection.draw(g);
		else if(state.getNum() == 2 && selection == null)
		{
			g.setColor(Color.red);
			g.draw(shape);
		}
	}
}