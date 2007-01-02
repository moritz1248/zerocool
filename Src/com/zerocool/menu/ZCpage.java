package com.zerocool.menu;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;

//holds all the components for a page as well as a properties object
public class ZCpage implements Serializable//deciding whether or not to make it a ZCcomponent \/
{
	private ArrayList<ZCcomponent> components;
	private ZCproperties properties;
	//another variable for the background maybe
	
	public ZCpage()
	{
		components = new ArrayList<ZCcomponent>();
		properties = new ZCproperties();
	}
	
	public void add(ZCcomponent newComp)
	{
		components.add(newComp);
		newComp.setPage(this);
	}
	
	public void mousifyAll(int x, int y, int type)
	{
		for(ZCcomponent c : components)
			c.mousify(x, y, type);
	}
	public void keyifyAll(int code, char key, int type)
	{
		for(ZCcomponent c : components)
			c.keyify(code, key, type);
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
		//is it really that simple?
		for(ZCcomponent zcc : components)
			if(zcc.isVisible())
				zcc.draw(g);
	}
}