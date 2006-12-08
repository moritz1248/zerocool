package com.zerocool.scene;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.zerocool.scene.level.TileObject;

/**
 * <code>GameObject</code> Is the overhead for all objects within the application, static or dynamic. 
 * <code>GameObject</code> includes the location and object ID, allowing any object in any situation
 * to be questioned and changed.
 *
 * @author Chris Gibson
 * @version GameObject.java,v 0.1 2006/10/03
 */
import java.io.Serializable;
 
public class GameObject extends RenderableObject implements Serializable
{
	final int OBJECT_ID;
	private int orientation;
	

	/**
	 * public <code>GameObject</code> extends <code>Node</code> and is the upper level modifier
	 * for all objects inside the game.
	 * @param id
	 */
	public GameObject(int id)
	{
		OBJECT_ID = id;
		orientation = 1;
	}
	
	public GameObject(int id, float pX, float pY, float pZ, int orient)
	{
		OBJECT_ID = id;
		setX(pX);
		setY(pY);
		setZ(pZ);
		orientation = orient;
	}
	
	public GameObject getClone(int id)
	{
		System.out.println("Cloning GameObject type: " + getType());
		if(getType() == -1)
			return new DynamicGameObject(id, getX(), getY(), getZ(), orientation, null, null);
		else if(getType() > 0)
			return new TileObject(id, getType(), getX(), getY(), getZ(), orientation);
		else
			return new GameObject(id, getX(), getY(), getZ(), orientation);
	}
	
	/**
	 * <code>getType</code> returns the type.
	 * @return type
	 */
	public int getType(){return 0;}
	

	


	@Override
	void Update(int elapsed) {
		// TODO Auto-generated method stub
		
	}
		/**
	 * <code>getOrientation</code> returns the orientation of tile.
	 * @return orientation;
	 */
	public int getOrientation(){return orientation;}
	
	/**
	 * <code>setOrientation</code> sets the tile's orientation
	 * @param orientation
	 */
	public void setOrientation(int orientation){this.orientation = orientation;}
}
