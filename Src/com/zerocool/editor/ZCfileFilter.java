package com.zerocool.editor;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class ZCfileFilter extends FileFilter 
{
	private String ext;
	public ZCfileFilter(String extension)
	{
		ext = extension;
	}
	public boolean accept(File f)
	{
		if (f.isDirectory()) 
		{
			return true;
		}
	    if(f.getPath().endsWith(ext))
	    {
	    	return true;
	    }
		return false;
	}
	public String getDescription()
	{
		if(ext.equals(".zcl"))
		{
			return "ZeroCool level files";
		}
		else if(ext.equals(".zcm"))
		{
			return "ZeroCool menu page files";
		}
		else if(ext.equals(".zcv"))
		{
			return "ZeroCool visual files";
		}
		return ext + " files";
	}
}
