package com.zerocool.menu;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Polygon;

//TODO: Advanced text positioning

//will allow a user to use a button
public class ZCbutton implements ZCcomponent
{
	public final int NOTHING = 0;
	public final int HIGHLIGHTED = 1;
	public final int SELECTED = 2;
	protected PolyShape shape;
	protected String name;
	//state: str=name, num=state
	protected Value state, propChange;
	protected ZCvisual image, selection, highlight;
	protected boolean visible;
	//if toIncrement == true then value += state.num else value = state.num or 0
	protected boolean toIncrement;
	protected ZCpage parent;
	
	public ZCbutton()
	{
		shape = new PolyShape(300, 300, 50, 50, PolyShape.SHAPE.OVAL);
		name = "empty";
		visible = true;
		state = new Value(name, 0, false);
		image = selection = highlight = null;
		propChange = new Value(name, 1, false);
		toIncrement = false;
	}
	public ZCbutton(PolyShape form,String nam,boolean selected,Value propC,boolean inc, ZCvisual img, ZCvisual select, ZCvisual high)
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
		if(isMousified && type == 0 && state.getNum() < SELECTED)
		{
			if(toIncrement)
			{
				parent.adjustProp(propChange.getStr(), new Value(null, propChange.getNum() + parent.getProp(propChange.getStr()).getNum(), false));
				state.setNum(HIGHLIGHTED);
			}
			else
			{
				parent.adjustProp(propChange.getStr(), new Value(null, propChange.getNum(), false));
				state.setNum(SELECTED);
			}
		}
		else if(isMousified && type == 0 && state.getNum() == SELECTED)
		{
			state.setNum(HIGHLIGHTED);
			if(!toIncrement)
				parent.adjustProp(propChange.getStr(), new Value(null, 0, false));
		}
		else if(isMousified && type == 1 && state.getNum() == NOTHING)
			state.setNum(HIGHLIGHTED);
		else if(!isMousified && type == 1 && state.getNum() == HIGHLIGHTED)
			state.setNum(NOTHING);
		else if(!isMousified && type == 0 && state.getNum() > NOTHING && toIncrement)
			state.setNum(NOTHING);
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
	public PolyShape getShape()
	{
		return shape;
	}
	public void setPage(ZCpage page)
	{
		parent = page;
	}
	public void notify(ZCsubButton button, boolean clickOn)
	{
	}
	public void translate(int x, int y)
	{
		shape.translate(x, y);
	}
	public void draw(Graphics2D g, boolean drawVertices)
	{
		if(image != null)
			image.draw(g, drawVertices);
		else
		{
			g.setColor(Color.black);
			shape.fill(g, drawVertices);
			g.setColor(Color.white);
			Rectangle bounds = shape.getBounds();
			g.drawString(name, bounds.x + 5, bounds.y + (bounds.height / 2) + 6);
		}
		if(state.getNum() == HIGHLIGHTED && highlight != null)
			highlight.draw(g, drawVertices);
		else if(state.getNum() == HIGHLIGHTED && highlight == null)
		{
			g.setColor(Color.yellow);
			shape.draw(g, false);
		}
		else if(state.getNum() == SELECTED && selection != null)
			selection.draw(g, drawVertices);
		else if(state.getNum() == SELECTED && selection == null)
		{
			g.setColor(Color.red);
			shape.draw(g, false);
		}
	}
}