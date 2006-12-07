package com.zerocool.scene;
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
 
public class GameObject extends UpdatableNode implements Serializable
{
	final int OBJECT_ID;
	float x,y,z;
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
		x = pX;
		y = pY;
		z = pZ;
		orientation = orient;
	}
	
	public GameObject getClone(int id)
	{
		System.out.println("Cloning GameObject type: " + getType());
		if(getType() == -1)
			return new DynamicGameObject(id, x, y, z, orientation, null, null);
		else if(getType() > 0)
			return new TileObject(id, getType(), x, y, z, orientation);
		else
			return new GameObject(id, x, y, z, orientation);
	}
	
	/**
	 * <code>getType</code> returns the type.
	 * @return type
	 */
	public int getType(){return 0;}
	
	/**
	 * <code>getX</code> returns the x coordinate.
	 * @return x
	 */
	public float getX(){return x;}
	
	
	/**
	 * <code>getY</code> returns the y coordinate.
	 * @return y
	 */
	public float getY(){return y;}
	
	
	/**
	 * <code>getZ</code> returns the z coordinate.
	 * @return z
	 */
	public float getZ(){return z;}
	
	
	/**
	 * <code>setX</code> sets the x coordinate
	 * @param x
	 */
	public void setX(float x){this.x=x;}
	
	
	/**
	 * <code>setY</code> sets the y coordinate
	 * @param y
	 */
	public void setY(float y){this.y=y;}
	
	
	/**
	 * <code>setZ</code> sets the z coordinate
	 * @param z
	 */
	public void setZ(float z){this.z=z;}


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
