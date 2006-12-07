//Code by Jonathan Sanford, Revention Software(tm)...this code is the intellectual property of Jonathan Sanford and should only be used by the members of Project Zero Cool for the design of said project
package com.zerocool.editor;

import javax.swing.*;

//first off let me say that i hate commenting code and so if you find sarcastic comments or otherwise deemed 'unproffesional' commenting...well make this bs up yourself
//TileLevelEditor is a class you idiot
//and no i don't like those star thingies
//This is the main class which only serves to create a frame and a panel and fit the panel into the frame. aka this is the technician at an art gallery.
public class TileLevelEditor
{
	public static void main(String args[])
	{
		//don't like the title...too bad.  oh and it's trademarked
		JFrame frame = new JFrame("TileLevelEditor v1.0 by Revention Software(tm) for Project Zero Cool");
		
		//creates the panel then abandons it to its hopeless fate of frog shaped birth defects that children tease.  poor it.
		TLEditor panel = new TLEditor();
		frame.setJMenuBar(panel.getMenuBar());
		
		frame.addWindowListener(panel);
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
	}
}
