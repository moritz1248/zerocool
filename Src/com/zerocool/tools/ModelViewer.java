package com.zerocool.tools;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.io.File;

public class ModelViewer extends JFrame implements ActionListener
{
	private JTextField fileName, modelName;
	private JButton browse, left, center, right;
	private JTextArea comments;
	private ModelViewerCanvas viewer;
	private JLabel name, size, created;
	
	public ModelViewer(String name)
	{
		super(name);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JTabbedPane jtp = new JTabbedPane();
		JPanel mvPanel = new JPanel();
		mvPanel.setLayout(new BorderLayout());
		mvPanel.add(createEastPanel(),BorderLayout.EAST);
		mvPanel.add(createWestPanel(),BorderLayout.WEST);
		jtp.add("Model Viewer", mvPanel);
		jtp.add("Effect Viewer", new JPanel());
		getContentPane().add(jtp);
		pack();
		setVisible(true);
	}
	
	public static void main(String[] args) 
	{
		ModelViewer mv = new ModelViewer("Model Viewer");
	}
	
	private JPanel createWestPanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JPanel sub1 = new JPanel();
		sub1.setLayout(new BoxLayout(sub1, BoxLayout.X_AXIS));
		sub1.add(new JLabel("File "));
		fileName = new JTextField();
		fileName.addActionListener(this);
		sub1.add(fileName);
		browse = new JButton("Browse");
		browse.addActionListener(this);
		sub1.add(browse);
		panel.add(sub1);
		
		JPanel sub2 = new JPanel();
		sub2.setLayout(new BoxLayout(sub2, BoxLayout.X_AXIS));
		sub2.add(new JLabel("Model Name "));
		modelName = new JTextField();
		sub2.add(modelName);
		panel.add(sub2);
		
		//TODO: adjust the position of the comment label
		panel.add(new JLabel("Comments"));
		comments = new JTextArea(5, 40);
		comments.setLineWrap(true);
		panel.add(comments);
		
		JPanel sub3 = new JPanel();
		left = new JButton("<<<");
		left.addActionListener(this);
		sub3.add(left);
		center = new JButton("Center");
		center.addActionListener(this);
		sub3.add(center);
		right = new JButton(">>>");
		right.addActionListener(this);
		sub3.add(right);
		panel.add(sub3);
		
		return panel;
	}
	
	private JPanel createEastPanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		viewer = new ModelViewerCanvas();
		panel.add(viewer);
		name = new JLabel("File Name: ");
		panel.add(name);
		size = new JLabel("File Size: ");
		panel.add(size);
		created = new JLabel("Date Created: ");
		panel.add(created);
		
		return panel;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == browse)
		{
			JFileChooser chooser = new JFileChooser();
			int returnVal = chooser.showOpenDialog(this);
		    if(returnVal == JFileChooser.APPROVE_OPTION)
		    {
		    	File file = chooser.getSelectedFile();
		    	fileName.setText(file.getPath());
		    	viewer.loadModel(file);
		    }
		}
		else if(e.getSource() == left)
		{
			viewer.turnLeft();
		}
		else if(e.getSource() == center)
		{
			viewer.setToCenter();
		}
		else if(e.getSource() == right)
		{
			viewer.turnRight();
		}
		else if(e.getSource() == fileName)
		{
			viewer.loadModel(fileName.getText());
		}
	}
}
