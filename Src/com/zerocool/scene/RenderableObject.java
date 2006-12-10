package com.zerocool.scene;

import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;
import java.net.URL;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.model.XMLparser.Converters.MaxToJme;

public class RenderableObject extends UpdatableNode {
	private Spatial object;
	MaxToJme maxtojme = new MaxToJme();
	ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
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
	
	public void loadModel(String filename){
		URL url = RenderableObject.class.getClassLoader().getResource(filename); 
		
		InputStream is=null;
		try {
		    is=url.openStream();
		} catch(Exception err)  {
		    System.out.println("Could not open Model file: "+err);
		}
		try {
		 
		    // Converts the file into a jme usable file
		    maxtojme.convert(is, bytearrayoutputstream);
		 
		    // Used to convert the jme usable file to a TriMesh
		    BinaryImporter binaryImporter = new BinaryImporter(); 
		    ByteArrayInputStream in=new ByteArrayInputStream(bytearrayoutputstream.toByteArray());
		 
		    //importer returns a Loadable, cast to Node
		    node = (Node)binaryImporter.load(in); 
		    this.attachChild(node);
		} catch(Exception err)  {
		    System.out.println("Error loading md3 model:"+err);
		}
	}
	
	public Spatial getRenderObject(){
		return object;
	}

}
