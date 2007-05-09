package com.zerocool.tools;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.io.File;

public class ModelViewerCanvas extends Canvas {
	public enum Status{Paused, Spinning, Blank, Loading, Error};
	private Status mCurStatus = Status.Blank;
	private Color mBgColor = Color.WHITE;
	private Color mFgColor = Color.BLACK;
	private boolean mIsSpinning = true;
	private double mSpinSpeed = 0.5;
	private double mSpin = 0.0;
	private File file;
	
	public ModelViewerCanvas()
	{
		super();
	}
	
	public void update(Graphics g) {
		g.setColor(mBgColor);
        g.clearRect(0, 0, getWidth(), getHeight());
        g.setColor(mFgColor);
        paint(g);
    }
	
	public void paint(Graphics g) {
		switch(mCurStatus){
		case Paused:
			g.drawString("Paused", 10, 10);
			mBgColor = Color.green;
			break;
		case Spinning:
			g.drawString("Spinning", 10, 10);
			mBgColor = Color.yellow;
			break;
		case Blank:
			g.drawString("Blank", 10, 10);
			mBgColor = Color.gray;
			break;
		case Loading:
			g.drawString("Loading", 10, 10);
			mBgColor = Color.orange;
			break;
		case Error:
			g.drawString("Error", 10, 10);
			mBgColor = Color.red;
			break;
		}
	}
	
	public void loadModel(String filename)
	{
		loadModel(new File(filename));
	}
	
	public void loadModel(File file)
	{
		this.file = file;
		mCurStatus = Status.Spinning;
		repaint();
	}
	
	public void setSpinState(boolean spinning)
	{
		mIsSpinning = spinning;
	}
	
	public void setSpinSpeed(double spinSpeed)
	{
		mSpinSpeed = spinSpeed;
	}
	
	public void turnLeft()
	{
		repaint();
	}
	
	public void turnRight()
	{
		repaint();
	}
	
	public void setToCenter()
	{
		repaint();
	}
	
}
