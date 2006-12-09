package com.zerocool.scene.level;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import com.zerocool.scene.GameObject;


import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;

@SuppressWarnings("serial")
public class Level extends Node{
	protected ArrayList<ArrayList<GameObject>> level;
	public void load(String filename){
		
		if(filename != null)
	    {
			if(level == null){
				level = new ArrayList<ArrayList<GameObject>>();
			}
	    	File file = new File(filename);
			//this code opens the file
			try
			{
				FileInputStream fileIS = new FileInputStream(file);
				ObjectInputStream inStream = new ObjectInputStream(fileIS);
				ArrayList<ArrayList<GameObject>> newLevel = (ArrayList)inStream.readObject();
				if(newLevel != null)
					level = newLevel;
			}
			catch(IOException e)
			{
				System.out.println("IOException thrown while trying to open level " + file + ";  Exception caught");
				System.out.println(e.toString());
			}
			catch(ClassNotFoundException e)
			{
				System.out.println("ClassNotFoundException thrown while trying to open level " + file + ";  Exception caught");
			}
			for(int i = 0; i < level.size(); i++){
				for(int j = 0; j < level.get(i).size(); j++){
					GameObject go = level.get(i).get(j);
					
					go.convertTileInformation();
					//Box b = new Box("tile-" + i + "-" +  j, new Vector3f(tile.getX()*10-5, tile.getY()*10-5, tile.getZ()*10-5), new Vector3f(tile.getX()*10+5, tile.getY()*10+5, tile.getZ()*10+5));
					attachChild(go);
				}
			}
		}		
	}
	ArrayList getArrayList(){
		return level;
	}
}
