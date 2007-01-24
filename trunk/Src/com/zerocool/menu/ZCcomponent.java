package com.zerocool.menu;

import java.awt.Graphics2D;
import java.io.Serializable;

//anything that can be drawn and/or used.
public interface ZCcomponent extends Serializable
{
	boolean mousify(int a, int b, int type);
	boolean keyify(int code, char key, int type);
	boolean isVisible();
	Value getState();
	PolyShape getShape();
	void draw(Graphics2D g, boolean drawVertices);
	void setPage(ZCpage page);
	void notify(ZCsubButton button, boolean clickOn);
	void translate(int x, int y);
}