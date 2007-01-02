package com.zerocool.menu;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

//holds all the components for a page as well as a properties object
public class ZCpage implements Serializable//deciding whether or not to make it a ZCcomponent \/
{
	private ArrayList<ZCcomponent> components;
	private ZCproperties properties;
	private ZCcomponent focus;
	private Color backColor;
	private ZCvisual background;
	//another variable(s) for the background maybe
	
	public ZCpage()
	{
		components = new ArrayList<ZCcomponent>();
		properties = new ZCproperties();
		focus = null;
	}
	
	public void add(ZCcomponent newComp)
	{
		components.add(newComp);
		newComp.setPage(this);
	}
	
	public void mousifyAll(int x, int y, int type)
	{
		for(ZCcomponent c : components)
			if(c.mousify(x, y, type) && !c.getClass().equals(ZCbutton.class))
				focus = c;
	}
	public void keyifyAll(int code, char key, int type)
	{
		//only keyify the focus so that text doesn't appear in multiple focuses
		if(focus != null)
			focus.keyify(code, key, type);
	}
	
	public Value getProp(String name)
	{
		return properties.getProp(name);
	}
	
	public void addListener(String name, ZCcomponent zcc)
	{
		properties.addListener(name, zcc);
	}
	
	//return true if the selected property was properly adjusted
	public boolean adjustProp(String name, Value value)
	{
		return properties.adjust(name, value);
	}

	public void resetClip(Graphics g)
	{
		//change for variable size
		g.setClip(new Rectangle(0, 0, 800, 600));
	}
	
	public void draw(Graphics2D g)
	{
		//background first
		if(backColor == null)
			backColor = Color.lightGray;
		g.setColor(backColor);
		g.fillRect(0, 0, 800, 600);
		if(background != null)
			background.draw(g);
		//is it really that simple?
		for(ZCcomponent zcc : components)
			if(zcc.isVisible())
				zcc.draw(g);
	}
}