package com.zerocool.menu.editor;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

public class MEpanel extends JTabbedPane implements ActionListener, WindowListener
{
	private JFrame parent;
	private JPanel start;
	private JButton newPageButt, newVisualButt, helpButt;
	private JLabel welcomeL;
	private JMenuBar menubar;
	private JMenu file, edit, properties, help;
	private JMenuItem exitMI, closeTabMI, newPageMI, newVisualMI, saveCurMI, 
		saveAllMI, loadPageMI, loadVisualMI;//...
	
	public MEpanel(JFrame frame)
	{
		parent = frame;
		frame.addWindowListener(this);
		setPreferredSize(new Dimension(800, 600));
		//start panel
		start = new JPanel();
		start.setLayout(new GridLayout(12, 1));
		//it's buttons and labels
		welcomeL = new JLabel("Welcome to the Menu Editor");
		start.add(welcomeL);
		newPageButt = new JButton("New Page");
		newPageButt.addActionListener(this);
		start.add(newPageButt);
		newVisualButt = new JButton("New Visual");
		newVisualButt.addActionListener(this);
		start.add(newVisualButt);
		helpButt = new JButton("Help");
		helpButt.addActionListener(this);
		start.add(helpButt);
		//menubar
		menubar = new JMenuBar();
		//menus
		file = new JMenu("File");
		edit = new JMenu("Edit");
		properties = new JMenu("Properties");
		help = new JMenu("Help");
		//menu items
		exitMI = new JMenuItem("Exit");
		exitMI.addActionListener(this);
		closeTabMI = new JMenuItem("Close this tab");
		closeTabMI.addActionListener(this);
		newPageMI = new JMenuItem("New Page");
		newPageMI.addActionListener(this);
		newVisualMI = new JMenuItem("New Visual");
		newVisualMI.addActionListener(this);
		saveCurMI = new JMenuItem("Save Current Tab");
		saveCurMI.addActionListener(this);
		saveAllMI = new JMenuItem("Save All");
		saveAllMI.addActionListener(this);
		loadPageMI = new JMenuItem("Load a Page");
		loadPageMI.addActionListener(this);
		loadVisualMI = new JMenuItem("Load a Visual");
		loadVisualMI.addActionListener(this);
		//add menu items to menu
		file.add(newPageMI);
		file.add(newVisualMI);
		file.addSeparator();
		file.add(loadPageMI);
		file.add(loadVisualMI);
		file.addSeparator();
		file.add(saveCurMI);
		file.add(saveAllMI);
		file.addSeparator();
		file.add(closeTabMI);
		file.add(exitMI);
		//add menus to menubar
		menubar.add(file);
		menubar.add(edit);
		menubar.add(properties);
		menubar.add(help);
		//add menubar to frame
		parent.setJMenuBar(menubar);
		
		add("Welcome", start);
	}
	
	public void setEditMenu(JMenuItem[] items)
	{
		//first delete all old items
		edit.removeAll();
		//add these items
		for(int c = 0; c < items.length; c++)
		{
			edit.add(items[c]);
		}
	}
	
	public void setPropMenu(JMenuItem[] items)
	{
		//first delete all old items
		properties.removeAll();
		//add these items
		for(int c = 0; c < items.length; c++)
		{
			properties.add(items[c]);
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == exitMI)
		{
			parent.dispose();
			System.exit(0);
		}
		else if(e.getSource() == closeTabMI)
		{
			remove(getSelectedIndex());
		}
		else if(e.getSource() == newPageButt || e.getSource() == newPageMI)
		{
			MEpagePanel mepp = new MEpagePanel(this);
			setSelectedComponent(add("Page", mepp));
			addChangeListener(mepp);
		}
		else if(e.getSource() == newVisualButt || e.getSource() == newVisualMI)
		{
			MEvisualPanel mevp = new MEvisualPanel(this);
			setSelectedComponent(add("Visual", mevp));
			addChangeListener(mevp);
		}
		else if(e.getSource() == saveCurMI)
		{
			save(false);
		}
		else if(e.getSource() == saveAllMI)
		{
			save(true);
		}
		else if(e.getSource() == loadPageMI)
		{
			loadPage();
		}
		else if(e.getSource() == loadVisualMI)
		{
			loadVisual();
		}
	}
	
	private void save(boolean saveAll)
	{
		if(saveAll)
		{
			int max = getTabCount();
			for(int c = 0; c < max; c++)
			{
				Component comp = getComponentAt(c);
				if(comp instanceof MEpagePanel)
				{
					((MEpagePanel)comp).save();
				}
				else if(comp instanceof MEvisualPanel)
				{
					((MEvisualPanel)comp).save();
				}
			}
		}
		else
		{
			Component comp = getSelectedComponent();
			if(comp instanceof MEpagePanel)
			{
				((MEpagePanel)comp).save();
			}
			else if(comp instanceof MEvisualPanel)
			{
				((MEvisualPanel)comp).save();
			}
		}
	}
	
	private void loadPage()
	{
		MEpagePanel mepp = new MEpagePanel(this);
		mepp.load(false);
		setSelectedComponent(add("Page", mepp));
		addChangeListener(mepp);
	}
	private void loadVisual()
	{
		MEvisualPanel mevp = new MEvisualPanel(this);
		mevp.load(false);
		setSelectedComponent(add("Visual", mevp));
		addChangeListener(mevp);
	}
	
	public void windowClosing(WindowEvent w)
	{
		parent.dispose();
	}
	public void windowClosed(WindowEvent w)
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
	public void windowActivated(WindowEvent w)
	{
	}
	public void windowDeactivated(WindowEvent w)
	{
	}
}
