package com.jfysics.regression;

import java.awt.Font;

import javax.swing.*;

public class RegressionTestView extends JFrame{
	protected JTextArea output = new JTextArea(20,60);
	protected int test_count=0, error_count=0;
	Font font = new Font("Courier New", 0, 14);
	Font font2 = new Font("Courier New", Font.BOLD, 14);
	public RegressionTestView(){
		
		super("Regression Test");

		JScrollPane pane = new JScrollPane(output);
		output.setFont(font);
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.getContentPane().add(pane);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	public void assertTest(boolean test_output, String test){
		test_count++;
		for(int i = test.length(); i < 40; i++)
		{
			test = test + " ";
		}
		if(test_output){
			output.append("[TEST] " + test + "    [PASSED]\n");
		}else{
			output.append("[TEST] " + test + "    [FAILED]\n");
			error_count++;
		}
	}
}
