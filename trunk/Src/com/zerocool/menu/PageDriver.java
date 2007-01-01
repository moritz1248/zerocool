package com.zerocool.menu;
 
import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputListener;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;
import java.awt.*;
import java.awt.image.BufferedImage;;

//so far this is just the outline of what it's going to look like
//i'm going to create a menu editor which will make it so we can
//easily fine tune and/or redo the menu system at a moments notice
public class PageDriver extends JPanel implements MouseInputListener 
{
	//all that crap here
	private ArrayList<ZCpage> pages;
	private ZCpage current;
	
	public PageDriver() 
	{
		//window specifications
		setPreferredSize(new Dimension(800, 600));
		addMouseListener(this);
		addMouseMotionListener(this);
		
		//load from file
		pages = loadPages();
		if(pages != null && pages.size() > 0)
			current = pages.get(0);
		else
			System.out.println("Error: no input read");
		
		//test code
		current = new ZCpage();
		ZCselector s1 = new ZCselector();
		ZCslider s2 = new ZCslider();
		current.add(s1);
		current.add(s2);
		pages.add(current);
	}

	public void paint(Graphics g)
	{
		//can i get the current size of the window easily?
		//i don't want to have to initialize screen and g2 every time
		//create global variables...later
		BufferedImage screen = new BufferedImage(800, 600, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g2 = (Graphics2D)screen.getGraphics();
		g2.setColor(Color.gray);
		g2.fill(new Rectangle(0, 0, 800, 600));
		if(current != null)
			current.draw(g2);
		g.drawImage(screen, 0, 0, null);
	}
	
	public ArrayList<ZCpage> loadPages()
	{
		return new ArrayList<ZCpage>();
	}
	
	public void mouseClicked(MouseEvent m) 
	{
		current.mousifyAll(m.getX(), m.getY(), 0);
		System.out.println("mouse clicked at: " + m.getX() + "-" + m.getY());
		repaint();
	}
	public void mouseMoved(MouseEvent m) 
	{
		current.mousifyAll(m.getX(), m.getY(), 1);
		repaint();
	}
	public void mouseDragged(MouseEvent m)
	{
		current.mousifyAll(m.getX(), m.getY(), 2);
		repaint();
	}
	public void mousePressed(MouseEvent m) 
	{
		//current.mousifyAll(m.getX(), m.getY(), 3);
	}
	public void mouseReleased(MouseEvent m)
	{
		//current.mousifyAll(m.getX(), m.getY(), 4);
	}
	public void mouseEntered(MouseEvent m) 
	{
	}
	public void mouseExited(MouseEvent m) 
	{
	}
}