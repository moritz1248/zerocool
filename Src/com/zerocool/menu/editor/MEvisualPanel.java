package com.zerocool.menu.editor;

import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.event.MouseInputListener;

import com.zerocool.editor.ZCfileFilter;
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
	private File lastAccessed;
	
	public MEvisualPanel(File lastAccessed)
	{
		visual = new ZCvisual();
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
		g2.setColor(Color.white);
		g2.fill(new Rectangle(0, 0, width, height));
		if(visual != null)
			visual.draw(g2, true);
		g.drawImage(screen, 0, 0, null);
	}

	//use .zcv extension
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
		chooser.setFileFilter(new ZCfileFilter(".zcv"));
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
				ZCvisual newVisual = (ZCvisual)inStream.readObject();
				if(newVisual != null)
					visual = newVisual;
			}
			catch(IOException e)
			{
				System.out.println("IOException thrown while trying to open visual " + file + ";  Exception caught");
				System.out.println(e.toString());
			}
			catch(ClassNotFoundException e)
			{
				System.out.println("ClassNotFoundException thrown while trying to open visual " + file + ";  Exception caught");
			}
	    }
	    repaint();
	}
	public boolean save()
	{
		JFileChooser chooser = new JFileChooser(lastAccessed);
		chooser.setFileFilter(new ZCfileFilter(".zcv"));
		int choice = chooser.showSaveDialog(this);
	    if(choice == JFileChooser.APPROVE_OPTION && chooser.getSelectedFile() != null)
	    {
			File file = chooser.getSelectedFile();
			//make sure the file is in the right format
			String path = file.getAbsolutePath();
			if(!path.endsWith(".zcv"))
			{
				if(path.lastIndexOf('.') >= path.length() - 5)
				{
					return false;
				}
				path += ".zcv";
			}
			file = new File(path);
			System.out.println("Attempting to save as " + file);
			lastAccessed = file;
			//this code saves the file
			try
			{
				FileOutputStream fileOS = new FileOutputStream(file);
				ObjectOutputStream outStream = new ObjectOutputStream (fileOS);
				outStream.writeObject(visual);
			}
			catch(FileNotFoundException e)
			{
				System.out.println("FileNotFoundException thrown while trying to save visual as " + file + "; Exception caught");
				return false;
			}
			catch(IOException e)
			{
				System.out.println("IOException thrown while trying to save visual as " + file + "; Exception caught");
				System.out.println(e.toString());
				return false;
			}
			return true;
		}
		return false;
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
