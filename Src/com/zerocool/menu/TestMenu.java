package com.zerocool.menu;

import javax.swing.JFrame;

//this class will die
//this code will be implemented within the program
public class TestMenu 
{
	public static void main(String args[])
	{
		JFrame frame = new JFrame("Testing ze menu");
		
		PageDriver pd = new PageDriver();
		frame.addKeyListener(pd);
		frame.getContentPane().add(pd);
		frame.pack();
		frame.setVisible(true);
	}
}