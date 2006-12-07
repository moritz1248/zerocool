package com.zerocool.items;

import org.keplerproject.luajava.LuaState;

import com.zerocool.app.ZeroCoolApp;
import com.zerocool.scene.DynamicGameObject;

//import sun.security.krb5.internal.ktab.l;

public class ItemObject extends DynamicGameObject {
	
	public ItemObject(int id, LuaState L) {
		super(id, null, L);
		// TODO Auto-generated constructor stub
		script_file_create = "Data/scripts/items/item_one_create.lua";
		script_file_think = "Data/scripts/items/item_one_think.lua";
		script_file_destroy = "Data/scripts/items/item_one_destroy.lua";
		
		onCreate();
	}

	@Override
	protected void onCreate() {
		if(script_file_create != null)
		{
			L.doFile(script_file_create);
		}
		
	}

	@Override
	protected void onDestroy() {
		if(script_file_think != null)
		{
			L.doFile(script_file_destroy);
			//L.call(arg0, arg1)
		}
		
	}

	@Override
	protected void onThink(float elapsed_time) {
		if(script_file_destroy != null)
		{
			L.doFile(script_file_think);
		}
		
	}

}
