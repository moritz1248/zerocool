package com.zerocool.menu;

import java.awt.*;
import java.awt.font.*;

//will allow the user to enter input
public class ZCtextbox implements ZCcomponent
{
	private ZCpage parent;
	private Rectangle shape, selection;
	private String name, text;
	private Font font;
	private Color highlight;
	private ZCvisual border;
	private Value propChange;
	private int carrotX, carrotY;
	
	public ZCtextbox()
	{
		shape = new Rectangle(100, 100, 200, 30);
		selection = null;
		name = "none";
		text = "";
		font = Font.getFont("Helvetica-bold-italic");
		carrotX = 0;
		carrotY = 0;
		border = null;
		parent = null;
	}
	public boolean mousify(int a, int b, int type)
	{
		return false;
	}
	public boolean isVisible()
	{
		return true;
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
	public void draw(Graphics2D g)
	{
		TextLayout layout = new TextLayout(text, font, g.getFontRenderContext());
		//draw it
	}
}