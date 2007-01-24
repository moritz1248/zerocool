package com.zerocool.menu;

import java.io.Serializable;
import java.util.HashMap;

//holds all the data for a page
public class ZCproperties implements Serializable
{
	private HashMap<String, Value> propMap;
	//later maybe make it possible for more objects to 'listen'
	public ZCproperties()
	{
		propMap = new HashMap<String, Value>();
	}
	//mro...i don't have a draw method
	public Value getProp(String name)
	{
		if(propMap.containsKey(name))
			return propMap.get(name);
		else
		{
			propMap.put(name, new Value(name, 0, false));
			return getProp(name);
		}
	}
	public void addProp(String name, Value value, ZCcomponent listener)
	{
		propMap.put(name, value);
	}
	//returns whether or not the requested change was accepted
	public boolean adjust(String name, Value value)
	{
		if(!propMap.containsKey(name) || value == null)
		{
			propMap.put(name, value);
			return false;
		}
		Value val = propMap.get(name);
		return val.setStr(value.getStr()) && val.setNum(value.getNum());
	}
	
	public String toString()
	{
		String out = "";
		for(String str : propMap.keySet())
			out += "Prop: " + str + " = " + propMap.get(str).getStr() + " | " + propMap.get(str).getNum() + "\n";
		return out;
	}
}