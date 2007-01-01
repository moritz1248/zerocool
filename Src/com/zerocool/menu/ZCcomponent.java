package com.zerocool.menu;

import java.awt.Graphics2D;
import java.io.Serializable;

//anything that can be drawn and/or used.
public interface ZCcomponent extends Serializable
{
	boolean mousify(int a, int b, int type);
	boolean isVisible();
	Value getState();
	void draw(Graphics2D g);
	void setPage(ZCpage page);
	void shout(String name, Value value);
}