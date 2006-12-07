package com.zerocool.scene.level;
import java.util.ArrayList;

import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;

/**
 *  <code>TileLevel</code> -- CURRENTLY NOT SUPPORTED
 *
 * @author Chris Gibson
 * @version TileLevel.java,v 0.1 2006/10/03
 */
public class TileLevel {
	String level= "1,2|1,1|1,4";
	ArrayList tiles = new ArrayList();
	public TileLevel(){
		for(int a = -10; a < 10; a++){
			for(int b = -10; b < 10; b++){
				if(a == -10 || a == 9 || b == -10 || b == 9){
					tiles.add(new Tile(a,1,b));
				}else{
					tiles.add(new Tile(a,0,b));
				}
			}
		}
	}
	class Tile{
		int x,y,z;
		public Tile(int x, int y, int z){
			this.x=x;
			this.y=y;
			this.z=z;
		}
		public Vector3f getLocation(){
			return new Vector3f((float)x * 10,(float)y * 10,(float)z * 10);
		}
	}

	
	public Node getNode(){
		Node node = new Node();
		for(int a = 0; a < tiles.size(); a++){
			Vector3f location = ((Tile)tiles.get(a)).getLocation();
			node.attachChild(new Box("box" + a, location.add(new Vector3f(-5,-5,-5)),location.add(new Vector3f(5,5,5))));
		}
		return node;
	}
}
