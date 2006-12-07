package com.zerocool.console;

import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Text;

/**
 *  <code>ConsoleManager</code> handles the consol information on the main screen.  This handles ten
 *  lines of code which scroll through depending on the input
 *
 * @author Chris Gibson
 * @version ConsoleManager.java,v 0.1 2006/10/03
 */
public class ConsoleManager 
{
	
	//Ten lines of console output in all
	Text line[] = new Text[10];
	
	//The node to attach to the screen
	Node node;
	
	/**
	 * public <code>ConsoleManager</code> initalizes the ten text fields and sets up the node
	 *
	 */
	public ConsoleManager()
	{
		
		//Creates the node
		node = new Node();
		
		for(int a = 0; a < 10; a++){
			line[a] = new Text("line" + a, "");
			line[a].setLocalTranslation(new Vector3f(0,20 * (a+1), 0));
			node.attachChild(line[a]);
		}
		
	}
	
	/**
	 * <code>addText</code> adds text to the lowest line and moves all the others up.
	 * @param message
	 */
	public void addText(String message){
		for(int a = 9; a > 0; a--){
			line[a].print(line[a-1].getText());
		}
		line[0].print(message);
	}
	
	/**
	 * <code>returnNode</code> returns the node of the entire console manager.
	 * @return node
	 */
	public Node returnNode(){return node;}
	

}
