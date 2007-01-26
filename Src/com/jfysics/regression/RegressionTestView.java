package com.jfysics.regression;

import javax.swing.*;

public class RegressionTestView extends JFrame{
	protected JTextArea output = new JTextArea(20,20);
	protected int test_count=0, error_count=0;
	public RegressionTestView(){
		super("Regression Test");
		this.getContentPane().add(output);
		this.pack();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	public void assertTest(boolean test_output, String test){
		test_count++;
		if(test_output){
			output.append("[TEST] " + test + "    [PASSED]\n");
		}else{
			output.append("[TEST] " + test + "    [FAILED]\n");
			error_count++;
		}
	}
}
