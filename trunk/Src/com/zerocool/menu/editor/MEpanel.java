package com.zerocool.menu.editor;

import com.zerocool.menu.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
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
	private JMenuItem exitMI, closeTabMI;//...
	
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
		file.add(exitMI);
		closeTabMI = new JMenuItem("Close this tab");
		closeTabMI.addActionListener(this);
		file.add(closeTabMI);
		//add menus to menubar
		menubar.add(file);
		menubar.add(edit);
		menubar.add(properties);
		menubar.add(help);
		//add menubar to frame
		parent.setJMenuBar(menubar);
		
		add("Welcome", start);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == exitMI)
		{
			parent.dispose();
			System.exit(0);
		}
		else if(e.getSource() == closeTabMI)
			remove(getSelectedIndex());
		else if(e.getSource() == newPageButt)
			setSelectedComponent(add("Page", new MEpagePanel()));
		else if(e.getSource() == newVisualButt)
			setSelectedComponent(add("Visual", new MEvisualPanel()));
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
