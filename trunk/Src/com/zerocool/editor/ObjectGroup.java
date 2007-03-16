package com.zerocool.editor;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.io.File;
import com.zerocool.scene.*;
import com.zerocool.scene.level.TileObject;

public class ObjectGroup
{
	public static final String DEFAULT_TEXTURE_FILE = "man.bmp";
	public static final ObjectGroup DEFAULT_OBJECT_GROUP = new ObjectGroup("Default Group", Color.lightGray, 1, null, null, false, null);
	private String name;
	private Color color;
	private String file;
	private File dgoFile = null;
	private int type;
	private ArrayList<GameObject> objects;
	private boolean update;
	
	public ObjectGroup(String groupName)
	{
		name = groupName;
		Random rand = new Random();
		color = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
		file = DEFAULT_TEXTURE_FILE;
		type = 1;
		update = false;
		objects = new ArrayList<GameObject>();
	}
	
	public ObjectGroup(String groupName, GameObject object)
	{
		name = groupName;
		Random rand = new Random();
		color = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
		file = object.getTextureID();
		type = object.getType();
		if(object instanceof DynamicGameObject)
		{
			dgoFile = ((DynamicGameObject)object).getFile();
		}
		update = false;
		objects = new ArrayList<GameObject>();
		objects.add(object);
	}
	
	public ObjectGroup(String groupName, ArrayList<GameObject> allObjects)
	{
		name = groupName;
		Random rand = new Random();
		color = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
		update = false;
		if(allObjects != null)
		{
			objects = new ArrayList<GameObject>(allObjects.size());
			for(GameObject go : allObjects)
			{
				objects.add(go);
			}
			GameObject object = allObjects.get(0);
			file = object.getTextureID();
			type = object.getType();
			if(object instanceof DynamicGameObject)
			{
				dgoFile = ((DynamicGameObject)object).getFile();
			}
		}
		else
		{
			objects = new ArrayList<GameObject>();
			file = DEFAULT_TEXTURE_FILE;
			type = 1;
		}
	}
	
	public ObjectGroup(String groupName, Color c, int objectType, String textureFile, File itemFile, boolean isUpdater, ArrayList<GameObject> gameObjects)
	{
		name = groupName;
		type = objectType;
		if(c != null)
		{
			color = c;
		}
		else
		{
			Random rand = new Random();
			color = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
		}
		if(textureFile != null)
		{
			file = textureFile;
		}
		else
		{
			file = DEFAULT_TEXTURE_FILE;
		}
		dgoFile = itemFile;
		if(gameObjects != null)
		{
			objects = new ArrayList<GameObject>(gameObjects.size());
			for(GameObject go : gameObjects)
			{
				objects.add(go);
			}
		}
		else
		{
			objects = new ArrayList<GameObject>();
		}
	}
	
	public ObjectGroup getClone()
	{
		return new ObjectGroup(name,color,type,file,dgoFile,update,getObjects());
	}
	
	public void matchTo(ObjectGroup match)
	{
		name = match.name;
		color = match.color;
		type = match.type;
		file = match.file;
		dgoFile = match.dgoFile;
		update = match.update;
		objects = match.getObjects();
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String groupName)
	{
		name = groupName;
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public void setColor(Color newColor)
	{
		color = newColor;
	}
	
	public int getType()
	{
		return type;
	}
	
	public String getTextureFile()
	{
		return file;
	}
	
	public void setTextureFile(String textureFile)
	{
		file = textureFile;
	}
	
	public File getItemFile()
	{
		return dgoFile;
	}
	
	public void setItemFile(File itemFile)
	{
		dgoFile = itemFile;
	}
	
	public boolean isUpdater()
	{
		return update;
	}
	
	public void setUpdate(boolean isUpdater)
	{
		update = isUpdater;
	}
	
	public void updateProfile(GameObject go)
	{
		file = go.getTextureID();
		type = go.getType();
		if(type == -1)
		{
			dgoFile = ((DynamicGameObject)go).getFile();
		}
	}
	
	public void updateAll()
	{
		if(update)
		{
			for(int c = 0; c < objects.size(); c++)
			{
				GameObject go = objects.get(c);
				if(type > 0 && go.getType() == -1)
				{
					TileObject to = new TileObject(TLEditor.nextId(), type, go.getX(), go.getY(), go.getZ(), go.getOrientation());
					to.setTextureID(file);
					to.setGroup(go.getGroup());
					objects.set(c, to);
				}
				else if(type == -1 && go.getType() > 0)
				{
					DynamicGameObject dgo = new DynamicGameObject(TLEditor.nextId(), go.getX(), go.getY(), go.getZ(), go.getOrientation(), dgoFile);
					dgo.setTextureID(file);
					dgo.setGroup(go.getGroup());
					objects.set(c, dgo);
				}
				else if(go.getType() == -1)
				{
					((DynamicGameObject)go).setFile(dgoFile);
					go.setTextureID(file);
				}
				else
				{
					((TileObject)go).setType(type);
					go.setTextureID(file);
				}
			}
		}
	}
	
	public ArrayList<GameObject> getObjects()
	{
		ArrayList<GameObject> allObjects = new ArrayList<GameObject>(objects.size());
		for(GameObject go : objects)
		{
			allObjects.add(go);
		}
		return allObjects;
	}
	
	public void addObjects(ArrayList<GameObject> newObjects)
	{
		for(GameObject go : newObjects)
		{
			if(go != null)
			{
				objects.add(go);
			}
		}
	}
	
	public void addObject(GameObject go)
	{
		if(go != null)
		{
			objects.add(go);
		}
	}
	
	public void removeObject(GameObject go)
	{
		System.out.println("an object was removed");
		if(go != null && objects.contains(go))
		{
			objects.remove(go);
		}
	}
	
	public void removeObjects(ArrayList<GameObject> toRemove)
	{
		for(GameObject go : toRemove)
		{
			removeObject(go);
		}
	}
	
	public JLabel getLabel()
	{
		BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();
		g.setColor(color);
		g.fillRect(0, 0, 10, 10);
		return new JLabel(name, new ImageIcon(img), JLabel.LEFT);
	}
	
	public String toString()
	{
		return name;
	}
}
