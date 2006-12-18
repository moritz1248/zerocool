package com.zerocool.menu;

import java.awt.Graphics;
//import java.awt.Graphics2D;  
//good class to have, if you want to do advanced visuals
import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputListener;
import javax.swing.JPanel;
import java.util.ArrayList;

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
		//and more to add
	}

	public void paint(Graphics g)
	{
		//whoa, actual code...shunness
		if(current != null)
			current.draw();
	}
	
	public void mouseClicked(MouseEvent arg0) 
	{
	}
	public void mouseEntered(MouseEvent arg0) 
	{
	}
	public void mouseExited(MouseEvent arg0) 
	{
	}
	public void mousePressed(MouseEvent arg0) 
	{
	}
	public void mouseReleased(MouseEvent arg0)
	{
	}
	public void mouseDragged(MouseEvent arg0)
	{
	}
	public void mouseMoved(MouseEvent arg0) 
	{
	}
	
	//***INTERNAL CLASSES BEYOND THIS POINT***//
	
	//anything that can be drawn and/or used.
	public interface ZCcomponent
	{
		boolean isMousified(int x, int y);
		boolean isVisible();
		int getState();
		void draw(Graphics g);
	}
	//holds all the components for a page as well as a properties object
	private class ZCpage //deciding whether or not to make it a ZCcomponent
	{
		private ZCpage()
		{
		}
		
		public void draw(Graphics g)
		{
			//draw it
		}
	}
	//holds all the data for a page
	private class ZCproperties //probably not though
	{
		private ZCproperties()
		{
		}
		//mro...i don't have a draw method
	}
	
	//why use an action class???
	
	//will allow a user to use a button
	private class ZCbutton implements ZCcomponent
	{
		private ZCbutton()
		{
		}
		public boolean isMousified(int x, int y)
		{
			return false;
		}
		public boolean isVisible()
		{
			return false;
		}
		public int getState()
		{
			return 0;
		}
		public void draw(Graphics g)
		{
			//draw it
		}
	}
	//will allow user to adjust a value with a slider
	private class ZCslider implements ZCcomponent
	{
		private ZCslider()
		{
		}
		public boolean isMousified(int x, int y)
		{
			return false;
		}
		public boolean isVisible()
		{
			return false;
		}
		public int getState()
		{
			return 0;
		}
		public void draw(Graphics g)
		{
			//draw it
		}
	}
	//will allow the user to select from a menu of items
	private class ZCselector implements ZCcomponent
	{
		private ZCselector()
		{
		}
		public boolean isMousified(int x, int y)
		{
			return false;
		}
		public boolean isVisible()
		{
			return false;
		}
		public int getState()
		{
			return 0;
		}
		public void draw(Graphics g)
		{
			//draw it
		}
	}
	//will allow the user to enter input
	private class ZCtextbox implements ZCcomponent
	{
		private ZCtextbox()
		{
		}
		public boolean isMousified(int x, int y)
		{
			return false;
		}
		public boolean isVisible()
		{
			return false;
		}
		public int getState()
		{
			return 0;
		}
		public void draw(Graphics g)
		{
			//draw it
		}
	}
	//will cover images and text, static and dynamic
	private class ZCvisual implements ZCcomponent
	{
		private ZCvisual()
		{
		}
		public boolean isMousified(int x, int y)
		{
			return false;
		}
		public boolean isVisible()
		{
			return false;
		}
		public int getState()
		{
			return 0;
		}
		public void draw(Graphics g)
		{
			//draw it
		}
	}
}
