package com.zerocool.managers;

public class GameAction {
	

	final int ACTION_PLAYER_ACCEL_FORWARD = 0;
	final int ACTION_PLAYER_ACCEL_BACKWARD = 1;
	final int ACTION_PLAYER_ACCEL_LEFT = 2;
	final int ACTION_PLAYER_ACCEL_RIGHT = 3;
	final int ACTION_PLAYER_TURN_LEFT = 4;
	final int ACTION_PLAYER_TURN_RIGHT = 5;
	final int ACTION_PLAYER_FIRE_PRIMARY = 6;
	final int ACTION_PLAYER_FIRE_SECONDARY = 7;
	
	int action;
	
	public GameAction(int arg){action = arg;}
	
	public int getAction(){
		return action;
	}

}

