package com.zerocool.app;
/*
 * Copyright (c) 2003-2006 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


import jmetest.renderer.TestBoxColor;

import org.lwjgl.input.Keyboard;

import com.jme.app.SimpleGame;
import com.jme.image.Texture;
import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.SceneElement;
import com.jme.scene.Text;
import com.jme.scene.shape.*;
import com.jme.scene.state.TextureState;
import com.jme.util.LoggingSystem;
import com.jme.util.TextureManager;

/**
 * <code>TestPQTorus</code> demonstrates the construction and animation of
 * a parameterized torus, also known as a pq torus.
 * @author Eric Woroshow
 * @version $Id: TestPQTorus.java,v 1.21 2006/05/12 21:29:21 nca Exp $
 */
public class Text2dBox extends SimpleGame {

    private Quaternion rotQuat = new Quaternion();
    private float angle = 0;
    private Vector3f axis = new Vector3f(1, 1, 0);
    private Quad q;
    private Text pqText;
    private Vector2f qLocation = new Vector2f(100,100);
    private Vector2f qVelocity = new Vector2f(1,3);
    private int speed = 100;


  /**
     * Entry point for the test.
     * @param args arguments passed to the program; ignored
     */
    public static void main(String[] args) {
        LoggingSystem.getLogger().setLevel(java.util.logging.Level.WARNING);
        Text2dBox app = new Text2dBox();
        app.setDialogBehaviour(ALWAYS_SHOW_PROPS_DIALOG);
        app.start();
    }

    /**
     * Animates the PQ torus.
     */
    protected void simpleUpdate() {
    	pqText.print("locX: " + qLocation.x + " ,LocY: " + qLocation.y);
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
        	qVelocity.y += .1;
        }
        
      	qLocation = qLocation.add(qVelocity);
      	qVelocity.y -= .01;
      	if(qLocation.x < 0 || qLocation.x > display.getWidth() - 150){
      		qLocation = qLocation.subtract(qVelocity);
      		qVelocity.x *=-1;
      	}
      	if(qLocation.y < 0 || qLocation.y > display.getHeight() - 100){
      		qLocation = qLocation.subtract(qVelocity);
      		qVelocity.y *=-1;
      	}
      	q.setLocalTranslation(new Vector3f(75 + qLocation.x,50+qLocation.y,0));
        //rotQuat.fromAngleAxis(angle * FastMath.DEG_TO_RAD, axis);
        //rootNode.setLocalRotation(rotQuat);
        //rootNode.updateRenderState();
    }

    /**
     * builds the trimesh.
     *
     * @see com.jme.app.SimpleGame#initGame()
     */
    protected void simpleInitGame() {
      display.setTitle("PQ Torus Test");
      pqText = new Text("PQ label", "");
      pqText.setLocalTranslation(new Vector3f(0,20,0));
      pqText.setCullMode(SceneElement.CULL_NEVER);
      fpsNode.attachChild(pqText);

        //Generate the geometry
      q = new Quad("quad", 150, 100);
      q.setLocalTranslation(new Vector3f(75 + qLocation.x,50+qLocation.y,0));
      rootNode.attachChild(q);
      
      q.setRenderQueueMode(Renderer.QUEUE_ORTHO);
        TextureState ts = display.getRenderer().createTextureState();
        ts.setEnabled(true);
        ts.setTexture(
            TextureManager.loadTexture(
            TestBoxColor.class.getClassLoader().getResource(
            "jmetest/data/images/Monkey.jpg"),
            Texture.MM_LINEAR_LINEAR,
            Texture.FM_LINEAR));

        rootNode.setRenderState(ts);

    }

}
