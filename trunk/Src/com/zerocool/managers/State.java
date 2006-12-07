package com.zerocool.managers;

/**
 *  <code>State</code> manages the application state
 *
 * @author Chris Gibson
 * @version TileLevel.java,v 0.1 2006/10/03
 */
public class State {
	
	/*
	 * States
	 */
	final int STATE_LOADING = 0;
	final int STATE_PLAYING = 1;
	final int STATE_MENU = 2;
	
	//Current State
	private int curState = 0;
	
	
	/**
	 * public <code>State</code> sets the beginning state
	 * @param currentState
	 */
	public State(int currentState){curState = currentState;}
	
	
	/**
	 * <code>setState</code> sets the current state
	 * @param state
	 */
	public void setState(int state){curState = state;}
	
	
	/**
	 * <code>getState</code> returns the current state.
	 * @return current state
	 */
	public int getState(){return curState;}
}
