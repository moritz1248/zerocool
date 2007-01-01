package com.zerocool.menu;

import java.awt.Graphics2D;

//will cover images and text, static and dynamic
public class ZCvisual implements ZCcomponent
{
	private ZCpage parent;
	
	public ZCvisual()
	{
	}
	public boolean mousify(int a, int b, int type)
	{
		return false;
	}
	public boolean isVisible()
	{
		return false;
	}
	public Value getState()
	{
		return null;
	}
	public void setPage(ZCpage page)
	{
		parent = page;
	}
	public void shout(String name, Value value)
	{
	}
	//allow an extra draw method for offset and/or orientation (for example center visual at 36-94)
	public void draw(Graphics2D g)
	{
		//draw it
	}
}