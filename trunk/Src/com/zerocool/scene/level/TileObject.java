package com.zerocool.scene.level;

import com.jme.math.Vector3f;
import com.jme.scene.shape.Box;
import com.zerocool.scene.GameObject;

/**
 * <code>TileObject</code> extends <code>GameObject</code>. It is the shell class for every Tile loaded
 *  into the application to create the arenas and levels that the players play on.  
 * to be questioned and changed.
 *
 * @author Chris Gibson
 * @version TileObject.java,v 0.1 2006/10/03
 *
 */
public class TileObject extends GameObject
{
	/**
	 * Tile types:
	 *   1. Solid block
	 *   2. sloped wall
	 *   3. sloped ramp
	 *   4. spawn point node
	 *   5. special tile (to be implemented later.  will use the attribute variable to change the type)
	 *   (more to be added later)
	 * 
	 * Orientation:
	 *   1. original direction
	 *   2-4. 90 degrees clockwise from previous
	 */
	private int type;
	private int attribute; /*currently not implemented.  will include dynamic blocks*/
	
	/**
	 * public <code>TileObject</code> generates the Tile Object and initializes its ID.
	 * @param id
	 */
	public TileObject(int id)
	{
		super(id);
	}
	
	public TileObject(int id, int type, float x, float y, float z, int orient)
	{
		super(id, x, y, z, orient);
		this.type = type;
		//setRenderObject(new Box("tile", new Vector3f(-5, -5, -5), new Vector3f(5, 5, 5)));
	}
	
	/**
	 * <code>getType</code> returns the type of tile.
	 * @return type;
	 */
	public int getType(){return type;}
	
	/**

	 * <code>setType</code> sets the tile's type.
	 * @param type
	 */
	public void setType(int type){this.type = type;}
	
}
