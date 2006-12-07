package com.zerocool.managers;

import org.lwjgl.input.Keyboard;

/**
 *  <code>InputManager</code> -- Manages all keyboard and mouse input
 *
 * @author Chris Gibson
 * @version TileLevel.java,v 0.1 2006/10/03
 */
public class InputManager {

	/**
	 * public <code>checkInput</code> returns keyboard and mouse input to the application
	 * @param state
	 * @return game_action
	 */
	public GameAction checkInput(State state){
		
		//If the keyboard has been initialized
		if(Keyboard.isCreated()){
			if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
				
			}else if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
				
			}else if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
				
			}else if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
				
			}
			
		}
		return null;
	}
}
