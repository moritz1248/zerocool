package com.zerocool.menu.editor;

import javax.swing.JFrame;

public class MenuEditor 
{
	public static void main(String args[])
	{
		JFrame frame = new JFrame("Menu Editor by Revention Software");
		MEpanel me = new MEpanel(frame);
		frame.getContentPane().add(me);
		frame.pack();
		frame.setVisible(true);
	}
}