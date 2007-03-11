/**
 *  <code>DynamicGameObject</code> extends <code>GameObject</code>.  <code>DynamicGameObject</code>
 *  Loads information on dynamic objects within the game level and runs the predefined scripts which
 *  dictate the actions and behaviors of each object.
 *
 * @author Chris Gibson
 * @version DynamicGameObject.java,v 0.1 2006/10/03
 *
 * modified by Jonathan Sanford for external use
 */
package com.zerocool.scene;

import java.io.File;
import java.util.Scanner;

import org.keplerproject.luajava.LuaState;

@SuppressWarnings("serial")
public class DynamicGameObject extends GameObject
{
	protected String script_file_create;
	protected String script_file_think;
	protected String script_file_destroy;
	public File file;
	
	/**
	 * public <code>DynamicGameObject</code> initializes the Game Object.
	 * @param id
	 */
	public DynamicGameObject(int id, File source)
	{
		super(id);
		file = source;
		onCreate();
	}
	
	public DynamicGameObject(int id, float x, float y, float z, int orient, File source)
	{
		super(id, x, y, z, orient);	
		file = source;
	}
	
	public int getType()
	{
		return -1;
	}
	
	public File getFile()
	{
		return file;
	}
	
	public void setFile(File f)
	{
		file = f;
	}
	
	public String toText(char delimiter)
	{
		return "DynamicGameObject" + delimiter + getID() + delimiter + file.getName() + delimiter + super.toText(delimiter);
	}
	
	public void toObject(Scanner scan)
	{
		file = new File(scan.next());
		super.toObject(scan);
	}
	
	/**
	 * This method calls the protected <code>onDestroy</code> method, which cannot be invoked
	 * outside the <code>DynamicGameObject</code> class.  This allows the application to do a little
	 * bit of cleaning up and double checking before actually calling the scripts that run upon 
	 * its removal.
	 */
	public void destroy()
	{
		onDestroy();
	}
	
	
	public void Update(float elapsed)
	{
		onThink(elapsed);
	}

	
	/**
	 * Called whenever the game updates, usually per frame.  This calls the LUA script and 
	 * parses the output.
	 * @param elapsed_time
	 */
	protected void onThink(float elapsed_time)
	{
	}
	
	
	/**
	 * <code>onCreate</code> runs the predefined script files when the object is created.
	 */
	protected void onCreate()
	{
	}

	
	/**
	 * <code>onDestroy</code> runs the predefined script files when the object is removed.
	 */
	protected void onDestroy()
	{
	}
	
}
