package com.zerocool.scripting;

import org.keplerproject.luajava.*;

public class LuaTest {
	static LuaState L;
	public static void main(String ... args){
		L = LuaStateFactory.newLuaState();
	    L.openBasicLibraries();
	    
	    L.doFile("Data/scripts/hello.lua");
	    
	    System.out.println("Hello World from Java!");
	    L.doString("blah()");
	    try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    new LuaTest().init();
	}
	
	public void init(){
	    
		//L.pushString("printStuff");
	    L.pushJavaObject(this);
	    L.setGlobal("JavaObject");
	    L.doFile("scripts/function.lua");
	    
	}
	
	public void printStuff(int var){
		System.out.println("Hey, it worked!  The value of the variable passed is: " + var);
	}
	
}
