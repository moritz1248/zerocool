package com.zerocool.scene;

import java.io.File;

import org.keplerproject.luajava.LuaState;
import org.lwjgl.input.Keyboard;


import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.MaterialState;
import com.jme.system.DisplaySystem;
import com.jmex.physics.*;
import com.zerocool.model.ModelLoader;
import com.zerocool.scene.camera.UserCamera;

/**
 *  <code>PlayerObject</code> extends <code>DynamicGameObject</code>. <code>PlayerObject</code>
 *  manages the objects controlled by the players.
 *
 * @author Chris Gibson
 * @version PlayerObject.java,v 0.1 2006/10/03
 */
public class PlayerObject extends DynamicGameObject{

	private UserCamera camera;
	private float angle = 0;
	private Vector3f velocity = new Vector3f(0,0,0);
	ModelLoader loader = new ModelLoader();
	
	/**
	 * public <code>PlayerObject</code> initializes the player object and sets the player's id.
	 * The ID numbers 0 to 32 are reserved for players.
	 * @param player_ID
	 */
	public PlayerObject(int player_ID, MaterialState ms) {
		super(player_ID, null);
		Node n = loader.loadModel("Data\\models\\hover1.3DS", "H1_TEX.BMP");
		
		setRenderObject(n);
		this.setLocalScale(.5f);
	}
	
	
	/**
	 * <code>addChildCamera</code> adds a camera to be pulled along with the player's controlled object.
	 * @param camera
	 */
	public void addChildCamera(UserCamera camera){
		this.camera = camera;
		//attachChild(camera.getNode());
	}


	@Override
	/**
	 * <code>onCreate</code> is called when the object is created.
	 */
	public void onCreate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	/**
	 * <code>onDestroy</code> is called when the object is destroyed.
	 */
	public void onDestroy() {
		// TODO Auto-generated method stub
		
	}
	
	
	/**
	 * <code>setAngle</code> sets the angle.
	 * @param angle
	 */
	public void setAngle(float angle){this.angle = angle;}
	
	
	/**
	 * <code>getAngle</code> gets the angle.
	 * @return angle
	 */
	public float getAngle(){return angle;}


	@Override
	public void onThink(float elapsed_time) {
		camera.setLocation(getLocalTranslation());
		float vel = .05f / elapsed_time;
		//System.out.println(vel);
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
			angle+=.1f/elapsed_time;	
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
			angle-=.1f/elapsed_time;	
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
			velocity = velocity.add(new Vector3f((float)Math.sin(angle) * vel, 0, (float)Math.cos(angle) * vel));
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
			velocity = velocity.subtract(new Vector3f((float)Math.sin(angle) * vel, 0, (float)Math.cos(angle) * vel));
		}
		setLocalRotation(new Quaternion(new float[]{0,angle,0}));
		setLocalTranslation(getLocalTranslation().add(velocity));
		velocity = velocity.mult(.995f);
		if(new Vector3f(0,0,0).distance(velocity) > .6){
			velocity =velocity.normalize().mult(.6f);
		}
		/*if(getLocalTranslation().x < -9*10 || getLocalTranslation().x > 8*10){
			velocity.x *=-1;
		}
		if(getLocalTranslation().z < -9*10 || getLocalTranslation().z > 8*10){
			velocity.z *=-1;
		}*/
		
	}
	@Override
	public void Update(float elapsed){
		onThink(elapsed);
	}


}
