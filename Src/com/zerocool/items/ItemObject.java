package com.zerocool.items;

import org.keplerproject.luajava.LuaState;

import com.zerocool.app.ZeroCoolApp;
import com.zerocool.scene.DynamicGameObject;

//import sun.security.krb5.internal.ktab.l;

public class ItemObject extends DynamicGameObject {
	
	public ItemObject(int id) {
		super(id, null);
		// TODO Auto-generated constructor stub
		script_file_create = "Data/scripts/items/item_one_create.lua";
		script_file_think = "Data/scripts/items/item_one_think.lua";
		script_file_destroy = "Data/scripts/items/item_one_destroy.lua";
		
		onCreate();
	}

	@Override
	protected void onCreate() {

		
	}

	@Override
	protected void onDestroy() {

		
	}

	@Override
	protected void onThink(float elapsed_time) {

		
	}

}
