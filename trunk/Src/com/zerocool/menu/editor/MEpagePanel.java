package com.zerocool.menu.editor;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import com.zerocool.menu.*;

public class MEpagePanel extends JPanel implements MouseInputListener
{
	private ZCpage page;
	private BufferedImage screen;
	private Graphics2D g2;
	private boolean firstTime;
	private int height, width;
	private ZCcomponent current;
	private int vertex;
	private int lastX, lastY;
	
	public MEpagePanel()
	{
		page = new ZCpage(this);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void paint(Graphics g)
	{
		if(firstTime || height != getHeight() || width != getWidth())
		{
			width = getWidth();
			height = getHeight();
			screen = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
			g2 = (Graphics2D)screen.getGraphics();
			firstTime = false;
		}
		if(page != null)
			page.draw(g2, true);
		g.drawImage(screen, 0, 0, null);
	}

	public void mouseClicked(MouseEvent m)
	{
		boolean update = false;
		for(ZCcomponent zcc : page.getComponents())
		{
			if(zcc.getShape().contains(m.getX(), m.getY()))
			{
				current = zcc;
				update = true;
			}
			if(zcc.getShape().isCenterHit(m.getX(), m.getY()))
			{
				return;
			}
		}
		if(!update)
		{
			current = null;
		}
		lastX = m.getX();
		lastY = m.getY();
		repaint();
	}
	public void mouseDragged(MouseEvent m)
	{
		if(current != null)
		{
			current.translate(m.getX() - lastX, m.getY() - lastY);
		}
		lastX = m.getX();
		lastY = m.getY();
		repaint();
	}
	public void mousePressed(MouseEvent m)
	{
		boolean update = false;
		for(ZCcomponent zcc : page.getComponents())
		{
			if(zcc.getShape().contains(m.getX(), m.getY()))
			{
				current = zcc;
				update = true;
			}
			if(zcc.getShape().isCenterHit(m.getX(), m.getY()))
			{
				return;
			}
		}
		if(!update)
		{
			current = null;
		}
		lastX = m.getX();
		lastY = m.getY();
		repaint();
	}
	public void mouseMoved(MouseEvent m)
	{
	}
	public void mouseReleased(MouseEvent m)
	{
	}
	public void mouseExited(MouseEvent m)
	{
	}
	public void mouseEntered(MouseEvent m)
	{
	}
}