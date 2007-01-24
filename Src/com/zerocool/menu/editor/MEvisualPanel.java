package com.zerocool.menu.editor;

import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputListener;
import com.zerocool.menu.*;

public class MEvisualPanel extends JPanel implements MouseInputListener
{
	private ZCvisual visual;
	private BufferedImage screen;
	private Graphics2D g2;
	private boolean firstTime;
	private int height, width;
	private int crntVertex;
	private ZCvisual.Step current;
	
	public MEvisualPanel()
	{
		visual = new ZCvisual();
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
		g2.setColor(Color.white);
		g2.fill(new Rectangle(0, 0, width, height));
		if(visual != null)
			visual.draw(g2, true);
		g.drawImage(screen, 0, 0, null);
	}

	public void mouseClicked(MouseEvent m)
	{
		for(ZCvisual.Step s : visual.getSteps())
		{
			int hit = s.getShape().vertexHit(m.getX(), m.getY());
			if(hit >= 0)
			{
				crntVertex = hit;
				current = s;
				break;
			}
		}
		repaint();
	}
	public void mouseDragged(MouseEvent m)
	{
		if(crntVertex >= 0 && current != null)
		{
			if(current.getShape().setVertex(crntVertex, m.getX(), m.getY()))
			{
				crntVertex = -1;
				current = null;
			}
		}
		repaint();
	}
	public void mousePressed(MouseEvent m)
	{
		boolean set = false;
		for(ZCvisual.Step s : visual.getSteps())
		{
			int hit = s.getShape().vertexHit(m.getX(), m.getY());
			if(hit >= 0)
			{
				crntVertex = hit;
				current = s;
				set = true;
				break;
			}
		}
		if(set == false)
		{
			crntVertex = -1;
			current = null;
		}
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
