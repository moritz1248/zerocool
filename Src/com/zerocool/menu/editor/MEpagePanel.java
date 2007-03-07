package com.zerocool.menu.editor;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.zerocool.editor.ZCfileFilter;
import com.zerocool.menu.*;

public class MEpagePanel extends JPanel implements MouseInputListener, ActionListener, ChangeListener
{
	private MEpanel parent;
	private ZCpage page;
	private BufferedImage screen;
	private Graphics2D g2;
	private boolean firstTime;
	private int height, width;
	private ZCcomponent current;
	private int lastX, lastY;
	private File lastAccessed;
	private JPopupMenu popup;
	private JMenuItem editPMI, deletePMI;
	private JMenuItem addNew, adjustProp;
	
	public MEpagePanel(MEpanel parent)
	{
		this.parent = parent;
		page = new ZCpage(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		lastAccessed = new File(ZCfileFilter.read("MenuEditor", "PagePath"));
		
		//popup menu???
		popup = new JPopupMenu();
		editPMI = new JMenuItem("Edit");
		editPMI.addActionListener(this);
		deletePMI = new JMenuItem("Delete");
		deletePMI.addActionListener(this);
		popup.add(editPMI);
		popup.add(deletePMI);
		
		//add menu items to parent
		addNew = new JMenuItem("Add new component");
		addNew.addActionListener(this);
		adjustProp = new JMenuItem("Adjust Properties");
		adjustProp.addActionListener(this);
		parent.setEditMenu(new JMenuItem[]{addNew});
		parent.setPropMenu(new JMenuItem[]{adjustProp});
	}
	
	public void stateChanged(ChangeEvent e)
	{
		if(parent.getSelectedComponent() == this)
		{
			parent.setEditMenu(new JMenuItem[]{addNew});
			parent.setPropMenu(new JMenuItem[]{adjustProp});
		}
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
	
	public void openNewComponentBox()
	{
		new ComponentBox();
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == addNew)
		{
			openNewComponentBox();
		}
		if(e.getSource() == adjustProp)
		{
			
		}
		if(e.getSource() == editPMI)
		{
			
		}
		if(e.getSource() == deletePMI)
		{
			page.remove(current);
			current = null;
			repaint();
		}
	}
	
	public void mouseClicked(MouseEvent m)
	{
		lastX = m.getX();
		lastY = m.getY();
		if(m.getButton() == MouseEvent.BUTTON1)
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
		}
		else
		{
			for(ZCcomponent zcc : page.getComponents())
			{
				if(zcc.getShape().contains(m.getX(), m.getY()))
				{
					current = zcc;
					popup.show(this, m.getX(), m.getY());
				}
			}
		}
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
	
	private class ComponentBox implements ActionListener
	{
		JFrame frame = new JFrame("New Component");
		JPanel panel = new JPanel();
		JPanel rowType = new JPanel();
		JPanel rowValue = new JPanel();
		JPanel rowVisual = new JPanel();
		JPanel rowButton = new JPanel();
		JLabel typeL = new JLabel("Type:\t");
		JLabel valueL = new JLabel("Initial Value:\t");
		JLabel visualL = new JLabel("Main Visual:\t");
		String[] types = {"Button", "Selector", "Slider", "TextBox", "Visual"};
		JComboBox typeList = new JComboBox(types);
		JTextField valueBox = new JTextField();
		JComboBox visualList = new JComboBox(new String[]{"Select One"});
		JButton ok = new JButton("OK");
		JButton cancel = new JButton("Cancel");
	
		private ComponentBox()
		{
			ok.addActionListener(this);
			cancel.addActionListener(this);
			rowType.setLayout(new BoxLayout(rowType, BoxLayout.X_AXIS));
			rowType.add(typeL);
			rowType.add(typeList);
			rowValue.setLayout(new BoxLayout(rowValue, BoxLayout.X_AXIS));
			rowValue.add(valueL);
			rowValue.add(valueBox);
			rowVisual.setLayout(new BoxLayout(rowVisual, BoxLayout.X_AXIS));
			rowVisual.add(visualL);
			rowVisual.add(visualList);
			rowButton.setLayout(new BoxLayout(rowButton, BoxLayout.X_AXIS));
			rowButton.add(ok);
			rowButton.add(cancel);
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.add(rowType);
			panel.add(rowValue);
			panel.add(rowVisual);
			panel.add(rowButton);
			frame.getContentPane().add(panel);
			frame.pack();
			frame.setVisible(true);
		}
		
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == ok)
			{
				ZCcomponent zcc;
				String str = valueBox.getText();
				switch(typeList.getSelectedIndex())
				{
				case 0:
					zcc = new ZCbutton();
					break;
				case 1:
					zcc = new ZCselector();
					break;
				case 2:
					zcc = new ZCslider();
					break;
				case 3:
					zcc = new ZCtextbox();
					break;
				case 4:
					zcc = new ZCvisual();
					break;
				default:
					zcc = null;
					break;
				}
				page.add(zcc);
				frame.dispose();
				repaint();
			}
			else if(e.getSource() == cancel)
			{
				frame.dispose();
			}
		}
	}
}