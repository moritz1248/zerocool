package com.zerocool.menu;

import java.io.Serializable;
import java.util.HashMap;

//holds all the data for a page
public class ZCproperties implements Serializable//probably not though /\
{
	private HashMap<String, Value> propMap;
	private HashMap<String, ZCcomponent> listeners;
	//later maybe make it possible for more objects to 'listen'
	public ZCproperties()
	{
		propMap = new HashMap<String, Value>();
		listeners = new HashMap<String, ZCcomponent>();
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
		if(listener != null)
			listeners.put(name, listener);
	}
	public void addListener(String name, ZCcomponent listener)
	{
		listeners.put(name, listener);
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
		boolean out = val.setStr(value.getStr()) && val.setNum(value.getNum());
		if(listeners.containsKey(name))
			listeners.get(name).shout(name, value);
		return out;
	}
	
	public String toString()
	{
		String out = "";
		for(String str : propMap.keySet())
			out += "Prop: " + str + " = " + propMap.get(str).getStr() + " | " + propMap.get(str).getNum() + "\n";
		return out;
	}
}