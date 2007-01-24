package com.zerocool.menu;
 
import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputListener;
import java.awt.event.*;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.util.ArrayList;
import java.awt.*;
import java.awt.image.BufferedImage;

//i'm going to create a menu editor which will make it so we can
//easily fine tune and/or redo the menu system at a moments notice
public class PageDriver extends JPanel implements MouseInputListener, KeyListener, WindowListener
{
	//all that crap here
	private ArrayList<ZCpage> pages;
	private ZCpage current;
	private JFrame parent;
	private BufferedImage screen;
	private Graphics2D g2;
	private boolean firstTime;
	private int height, width;
	
	public PageDriver(JFrame frame) 
	{
		firstTime = true;
		
		parent = frame;
		parent.addWindowListener(this);
		parent.addKeyListener(this);
		
		//window specifications
		setPreferredSize(new Dimension(800, 600));
		addMouseListener(this);
		addMouseMotionListener(this);
		//keylistener has to be added to the frame not the panel
		
		//load from file
		pages = loadPages();
		if(pages != null && pages.size() > 0)
			current = pages.get(0);
		else
			System.out.println("Error: no input read");
		
		current = new ZCpage(this);
		
		pages.add(current);
	}

	public void paint(Graphics g)
	{
		if(firstTime || height != parent.getHeight() || width != parent.getWidth())
		{
			width = parent.getWidth();
			height = parent.getHeight();
			screen = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
			g2 = (Graphics2D)screen.getGraphics();
			firstTime = false;
		}
		if(current != null)
			current.draw(g2, false);
		g.drawImage(screen, 0, 0, null);
	}
	
	public ArrayList<ZCpage> loadPages()
	{
		return new ArrayList<ZCpage>();
	}
	
	//mouse stuff
	public void mouseClicked(MouseEvent m) 
	{
		current.mousifyAll(m.getX(), m.getY(), 0);
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
		current.mousifyAll(m.getX(), m.getY(), 3);
		repaint();
	}
	public void mouseReleased(MouseEvent m)
	{
		//current.mousifyAll(m.getX(), m.getY(), 4);
		//repaint();
	}
	public void mouseEntered(MouseEvent m) 
	{
	}
	public void mouseExited(MouseEvent m) 
	{
	}
	//keyboard stuff
	public void keyTyped(KeyEvent k)
	{
		current.keyifyAll(k.getKeyCode(), k.getKeyChar(), 0);
		repaint();
	}
	public void keyPressed(KeyEvent k)
	{
		current.keyifyAll(k.getKeyCode(), k.getKeyChar(), 1);
		repaint();
	}
	public void keyReleased(KeyEvent k)
	{
		current.keyifyAll(k.getKeyCode(), k.getKeyChar(), 2);
		repaint();
	}
	//window stuff
	public void windowClosing(WindowEvent w)
	{
		parent.dispose();
	}
	public void windowClosed(WindowEvent w)
	{
	}
	public void windowDeactivated(WindowEvent w)
	{
	}
	public void windowActivated(WindowEvent w)
	{
	}
	public void windowOpened(WindowEvent w)
	{
	}
	public void windowIconified(WindowEvent w)
	{
	}
	public void windowDeiconified(WindowEvent w)
	{
	}
}