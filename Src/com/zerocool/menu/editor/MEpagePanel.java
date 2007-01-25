package com.zerocool.menu.editor;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import com.zerocool.editor.ZCfileFilter;
import com.zerocool.menu.*;

public class MEpagePanel extends JPanel implements MouseInputListener
{
	private ZCpage page;
	private BufferedImage screen;
	private Graphics2D g2;
	private boolean firstTime;
	private int height, width;
	private ZCcomponent current;
	private int lastX, lastY;
	private File lastAccessed;
	
	public MEpagePanel(File lastAccessed)
	{
		page = new ZCpage(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		this.lastAccessed = lastAccessed;
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
	//use .zcm extension
	public void load(boolean saveRequired)
	{
		int choice;
		if(saveRequired)
		{
			choice = JOptionPane.showConfirmDialog(this, "Do you want to save the current page first?", "Save?", JOptionPane.YES_NO_OPTION);
			if(choice == JOptionPane.YES_OPTION)
				save();
		}
		JFileChooser chooser = new JFileChooser(lastAccessed);
		chooser.setFileFilter(new ZCfileFilter(".zcm"));
		choice = chooser.showOpenDialog(this);
	    if(choice == JFileChooser.APPROVE_OPTION && chooser.getSelectedFile() != null)
	    {
	    	File file = chooser.getSelectedFile();
	    	lastAccessed = file;
			//this code opens the file
			try
			{
				FileInputStream fileIS = new FileInputStream(file);
				ObjectInputStream inStream = new ObjectInputStream(fileIS);
				ZCpage newPage = (ZCpage)inStream.readObject();
				if(newPage != null)
				{
					page = newPage;
					page.setParent(this);
				}
			}
			catch(IOException e)
			{
				System.out.println("IOException thrown while trying to open page " + file + ";  Exception caught");
				System.out.println(e.toString());
			}
			catch(ClassNotFoundException e)
			{
				System.out.println("ClassNotFoundException thrown while trying to open page " + file + ";  Exception caught");
			}
	    }
	    repaint();
	}
	public boolean save()
	{
		JFileChooser chooser = new JFileChooser(lastAccessed);
		chooser.setFileFilter(new ZCfileFilter(".zcm"));
		int choice = chooser.showSaveDialog(this);
	    if(choice == JFileChooser.APPROVE_OPTION && chooser.getSelectedFile() != null)
	    {
			File file = chooser.getSelectedFile();
			//make sure the file is in the right format
			String path = file.getAbsolutePath();
			if(!path.endsWith(".zcm"))
			{
				if(path.lastIndexOf('.') >= path.length() - 5)
				{
					return false;
				}
				path += ".zcm";
			}
			file = new File(path);
			System.out.println("Attempting to save as " + file);
			lastAccessed = file;
			//this code saves the file
			try
			{
				FileOutputStream fileOS = new FileOutputStream(file);
				ObjectOutputStream outStream = new ObjectOutputStream (fileOS);
				outStream.writeObject(page);
			}
			catch(FileNotFoundException e)
			{
				System.out.println("FileNotFoundException thrown while trying to save page as " + file + "; Exception caught");
				return false;
			}
			catch(IOException e)
			{
				System.out.println("IOException thrown while trying to save page as " + file + "; Exception caught");
				System.out.println(e.toString());
				return false;
			}
			return true;
		}
		return false;
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