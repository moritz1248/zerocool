package com.zerocool.scene;

import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;


public class RenderableObject extends UpdatableNode {
	private Spatial object;

	Node node = new Node();
	
	public RenderableObject(){
		super();
	}
	
	@Override
	public void Update(int elapsed) {
		// TODO Auto-generated method stub
		
	}
	
	public void setRenderObject(Spatial object){
		if(object != null)
			object.removeFromParent();
		this.object = object;
		attachChild(object);
	}
	

	
	public Spatial getRenderObject(){
		return object;
	}

}
