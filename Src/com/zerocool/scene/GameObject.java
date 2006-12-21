package com.zerocool.scene;

import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;
import com.zerocool.model.ModelLoader;
import com.zerocool.scene.level.TileObject;
import java.io.Serializable;

/**
 * <code>GameObject</code> Is the overhead for all objects within the application, static or dynamic. 
 * <code>GameObject</code> includes the location and object ID, allowing any object in any situation
 * to be questioned and changed.
 *
 * @author Chris Gibson
 * @version GameObject.java,v 0.1 2006/10/03
 */
 
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
	
	protected int getID() {
		// TODO Auto-generated method stub
		return OBJECT_ID;
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
			return new DynamicGameObject(id, getX(), getY(), getZ(), orientation, null);
		else if(getType() > 0)
			return new TileObject(id, getType(), getX(), getY(), getZ(), orientation);
		else
			return new GameObject(id, getX(), getY(), getZ(), orientation);
	}
	
	public void convertTileInformation() {
		setX(getX()* 10);
		setY(getY() * 10);
		setZ(getZ() * 10);
		//ModelLoader loader = new ModelLoader();
		if(this instanceof TileObject)
			//setRenderObject(loader.loadModel("Data\\models\\hover1.3DS", "H1_TEX.BMP"));
			setRenderObject(new Box("tile", new Vector3f(-5, -5, -5), new Vector3f(5, 5, 5)));
		else if(this instanceof DynamicGameObject)
			setRenderObject(new Box("tile", new Vector3f(-2, -2, -2), new Vector3f(2, 2, 2)));
	}
	
	/**
	 * <code>getType</code> returns the type.
	 * @return type
	 */
	public int getType(){return 0;}



	@Override
	public void Update(int elapsed) {
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
