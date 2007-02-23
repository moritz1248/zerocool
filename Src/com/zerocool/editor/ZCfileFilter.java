package com.zerocool.editor;

import javax.swing.filechooser.FileFilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
	
	public static String read(String program, String var)
	{
		if(!var.startsWith(">"))
		{
			var = '>' + var;
		}
		if(!var.endsWith("="))
		{
			var += '=';
		}
		try
		{
			System.out.println("Directory: " + System.getProperty("user.dir"));
			FileReader fr = new FileReader(System.getProperty("user.dir") + "/config.txt");
			BufferedReader reader = new BufferedReader(fr);
			System.out.println("Attempting to read first line.");
			String nextLine;
			boolean inside = false;
			for(nextLine = reader.readLine(); nextLine != null; nextLine = reader.readLine())
				if(nextLine.startsWith(program + "::"))
				{
					inside = true;
				}
				else if(nextLine.startsWith("<<"))
				{
					inside = false;
				}
				else if(inside && nextLine.startsWith(var))
				{
					if(nextLine.charAt(var.length()) == '*')
						return System.getProperty("user.dir") + nextLine.substring(var.length() + 1, nextLine.length());
					else
						return nextLine.substring(var.length(), nextLine.length());
				}
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Exception caught: " + e.toString());
		}
		catch(IOException e)
		{
			System.out.println("Exception caught: " + e.toString());
		}
		return null;
		
	}
}
