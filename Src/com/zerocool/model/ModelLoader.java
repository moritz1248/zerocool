package com.zerocool.model;
import java.net.URL;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

import com.jme.math.Matrix3f;
import com.jme.math.Quaternion;
import com.jme.scene.Node;
import com.jme.util.TextureKey;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.model.XMLparser.Converters.MaxToJme;
public class ModelLoader {
	MaxToJme maxtojme = new MaxToJme();
	ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
	Node node = new Node();
	
	public Node loadModel(String filename){
		
	InputStream is = null;
	try {
		is = new FileInputStream(new File(filename));
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}


	try {
	 
	    // Converts the file into a jme usable file
	    maxtojme.convert(is, bytearrayoutputstream);
	    
	    // Used to convert the jme usable file to a TriMesh
	    BinaryImporter binaryImporter = new BinaryImporter(); 
	    ByteArrayInputStream in=new ByteArrayInputStream(bytearrayoutputstream.toByteArray());
	    TextureKey.setOverridingLocation(new URL("file:/C:\\zerocool\\Zero Cool\\Data\\models\\H1_TEX.BMP"));
	    
	    //importer returns a Loadable, cast to Node
	    node = (Node)binaryImporter.load(in); 
	    
	} catch(Exception err)  {
	    System.out.println("Error loading 3DS model: "+err);
	}
	node.setLocalScale(.05f);
	node.setLocalRotation(new Quaternion(new float[]{(float)-Math.PI / 2f,0,0}));
	return node;
}
}
