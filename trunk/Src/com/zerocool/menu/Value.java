package com.zerocool.menu;

import java.io.Serializable;

//wrapper class to hold a string and an integer...whichever is needed
public class Value implements Serializable
{
	private String str;
	private int num;
	//um, not sure if I'm actually going to use the lock functionality, might just get rid of it
	private boolean locked;
	//thought it would work but something errors out when i try to use this as a null value...
	//see below for faulty code
	public final static int NULL = Integer.MIN_VALUE;
	public Value(String string, int number, boolean islocked)
	{
		str = string;
		num = number;
		locked = islocked;
	}
	public String getStr(){return str;}
	public int getNum(){return num;}
	public boolean setStr(String string)
	{
		if(locked)
			return false;
		if(string != null)
			str = string;
		return true;
	}
	public boolean setNum(int number)
	{
		if(locked)
			return false;
		if(num != NULL)
			num = number;
		return true;
	}
	public String toString()
	{
		return "Value(" + str + ", " + num + ")";
	}
}