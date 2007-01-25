package com.zerocool.menu;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.event.KeyEvent;

//will allow the user to enter input (one line only...simplify things)
public class ZCtextbox implements ZCcomponent
{
	private ZCpage parent;
	private PolyShape shape;
	private Rectangle2D selection;
	private String name, text;
	private Color highlight;
	private ZCvisual border;
	private Value propChange;
	//should the caret be drawn here or should it be represented by a dynamic ZCvisual (probably better)
	//i'm just going to make the caret a static image
	private int caret, start, offset;
	//transient data...mmm
	private transient TextLayout layout;
	private float x, y;
	private boolean shiftPressed;
	
	public ZCtextbox()
	{
		shape = new PolyShape(100, 100, 500, 20, PolyShape.SHAPE.RECTANGLE);
		selection = null;
		highlight = Color.yellow;
		name = "none";
		text = "Testing...";
		caret = start = -1;
		border = null;
		parent = null;
	}
	public boolean mousify(int a, int b, int type)
	{
		if(layout == null)
			return false;
		boolean moused = shape.contains(a, b);
		TextHitInfo hit = layout.hitTestChar(a - x + offset, b - y);
		int index = hit.getInsertionIndex();
		if(!moused && (type == 0 || type == 3))
		{
			start = -1;
			selection = null;
			return false;
		}
		if(type == 0)
		{
			caret = index;
			start = -1;
			selection = null;
		}
		//else if(type == 2 && moused && start < 0)
		//	start = index;
		else if(type == 2 && start != -1)
		{
			caret = index;
			Shape selected = layout.getLogicalHighlightShape(start, caret);
			selection = selected.getBounds();
			selection.setRect(selection.getX() + x - offset, shape.getY(), selection.getWidth(), shape.getHeight());
		}
		else if(type == 3)
		{
			start = index;
			caret = index;
			selection = null;
		}
		return moused;
	}
	public boolean keyify(int code, char key, int type)
	{
		if(code == KeyEvent.VK_RIGHT && caret < text.length() && type == 1)
		{
			caret++;
			if(shiftPressed)
			{
				if(start < 0)
					start = caret - 1;
				Shape selected = layout.getLogicalHighlightShape(start, caret);
				selection = selected.getBounds();
				selection.setRect(selection.getX() + x - offset, shape.getY(), selection.getWidth(), shape.getHeight());
			}
			else
			{
				start = -1;
				selection = null;
			}
		}
		else if(code == KeyEvent.VK_LEFT && caret > 0 && type == 1)
		{
			caret--;
			if(shiftPressed)
			{
				if(start < 0)
					start = caret + 1;
				Shape selected = layout.getLogicalHighlightShape(start, caret);
				selection = selected.getBounds();
				selection.setRect(selection.getX() + x - offset, shape.getY(), selection.getWidth(), shape.getHeight());
			}
			else
			{
				start = -1;
				selection = null;
			}
		}
		else if(code == KeyEvent.VK_SHIFT && type == 1)
			shiftPressed = true;
		else if(code == KeyEvent.VK_SHIFT && type == 2)
			shiftPressed = false;
		else if(code == KeyEvent.VK_BACK_SPACE && type == 1)
		{
			if(selection != null && start != caret)
			{
				if(caret > start)
				{
					text = text.substring(0, start) + text.substring(caret, text.length());
					caret = start;
				}
				else
					text = text.substring(0, caret) + text.substring(start, text.length());
				selection = null;
				start = -1;
			}
			else if(caret != 0)
			{
				text = text.substring(0, caret - 1) + text.substring(caret, text.length());
				caret--;
			}
		}
		else if(code == KeyEvent.VK_DELETE && type == 1)
		{
			if(selection != null && start != caret)
			{
				if(caret > start)
				{
					text = text.substring(0, start) + text.substring(caret, text.length());
					caret = start;
				}
				else
					text = text.substring(0, caret) + text.substring(start, text.length());
				selection = null;
				start = -1;
			}
			else if(caret != text.length())
				text = text.substring(0, caret) + text.substring(caret + 1, text.length());
		}
		else if(type == 0 && (key >= ' ' && key <= '~'))
		{
			if(selection == null)
			{
				text = text.substring(0, caret) + key + text.substring(caret, text.length());
			}
			else
			{
				if(caret > start)
				{
					text = text.substring(0, start) + key + text.substring(caret, text.length());
					caret = start;
				}
				else
					text = text.substring(0, caret) + key + text.substring(start, text.length());
				selection = null;
				start = -1;
			}
			caret++;
		}
		return true;
	}
	public boolean isVisible()
	{
		return true;
	}
	public Value getState()
	{
		return new Value(text, 0, false);
	}
	public PolyShape getShape()
	{
		return shape;
	}
	public void translate(int x, int y)
	{
		shape.translate(x, y);
	}
	public void setPage(ZCpage page)
	{
		parent = page;
	}
	public void notify(ZCsubButton button, boolean clickOn)
	{
	}
	public void draw(Graphics2D g, boolean drawVertices)
	{
		//so that nothing "overflows"
		g.setClip(shape.getBounds());
		//draw background (white)
		g.setColor(Color.white);
		shape.fill(g, drawVertices);
		//draw highlight
		if(selection != null)
		{
			g.setColor(highlight);
			g.fill(selection);
		}
		//calculate layout
		if(text.equals("") || text == null)
			text = " ";
		Font font = g.getFont().deriveFont((float)(shape.getHeight() * .75));
		layout = new TextLayout(text, font, g.getFontRenderContext());
		//calculate caret
		Rectangle2D caretbound = null;
		if(caret >= 0 && caret <= text.length())
		{
			Shape caretspace = layout.getLogicalHighlightShape(caret, caret);
			caretbound = caretspace.getBounds();
			if(caretbound.getX() + x - offset < shape.getX() + shape.getHeight())
			{
				if(offset > shape.getHeight())
					offset -= shape.getHeight() / 2;
				else
					offset = 0;
			}
			else if(caretbound.getX() + x - offset > shape.getX() + shape.getWidth() - (shape.getHeight() / 2))
			{
				offset += shape.getHeight() / 2;
			}
		}
		//calculate x,y
		//this is to fix a glitch were the text will raise up if there isn't any hanging letters
		TextLayout l2 = new TextLayout("Lj,'", font, g.getFontRenderContext());
		Rectangle2D bounds = l2.getBounds();
		x = (float)(shape.getX() + 3);
		y = (float)(shape.getY() + bounds.getHeight() + 2);
		//draw text
		g.setColor(Color.black);
		layout.draw(g, x - offset, y);
		//draw caret (non-dynamic)
		if(caretbound != null)
		{
			caretbound.setRect(caretbound.getX() + x - offset, shape.getY(), 1, shape.getHeight());
			g.draw(caretbound);
		}
		//reset clip to allow border to 'spill out'
		parent.resetClip(g);
		//draw border
		if(border != null)
			border.draw(g, drawVertices);
		else
			shape.draw(g, drawVertices);
	}
}