package com.zerocool.model;

import java.net.URL;
import java.sql.Savepoint;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

import jmetest.renderer.loader.TestObjJmeWrite;

import com.jme.math.Quaternion;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.util.TextureKey;
import com.jme.util.export.Savable;
import com.jme.util.export.binary.BinaryImporter;
import com.jmex.model.XMLparser.Converters.ObjToJme;

public class ObjModelLoader {
	

	ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();

	Node node = new Node();

	public Spatial loadM(String filename)
	{
		InputStream is = null;
		try {
			is = new FileInputStream(new File(filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Spatial r = null;
		ObjToJme converter=new ObjToJme();
        try {
            URL objFile=ObjModelLoader.class.getClassLoader().getResource(filename);
            converter.setProperty("mtllib",objFile);
            ByteArrayOutputStream BO=new ByteArrayOutputStream();
            System.out.println("Starting to convert .obj to .jme");
            converter.convert(is,BO);
            
            //jbr.setProperty("texurl",new File(".").toURL());
            System.out.println("Done converting, now watch how fast it loads!");
            long time=System.currentTimeMillis();
            r=(Spatial)BinaryImporter.getInstance().load(new ByteArrayInputStream(BO.toByteArray()));
            
            
        }catch (Exception e)
        {
        	e.printStackTrace();
        }
        return r;
	}
        
	public Node loadModel(String filename) {
		ObjToJme objtojme = new ObjToJme();
		InputStream is = null;
		try {
			is = new FileInputStream(new File(filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {

			// Converts the file into a jme usable file
			objtojme.convert(is, bytearrayoutputstream);

			// Used to convert the jme usable file to a TriMesh
			BinaryImporter binaryImporter = new BinaryImporter();
			ByteArrayInputStream in = new ByteArrayInputStream(
					bytearrayoutputstream.toByteArray());
			TextureKey.setOverridingLocation(new URL("file:/"
					+ System.getProperty("user.dir") + "\\Data\\textures\\"));
			// importer returns a Loadable, cast to Node
			Savable s =  binaryImporter.load(in);
			node.attachChild((Node)s);

		} catch (Exception err) {
			System.out.println("Error loading OBJ model: " + err);
			err.printStackTrace();
		}
		node.setLocalScale(.05f);
		node.setLocalRotation(new Quaternion(new float[] {
				(float) -Math.PI / 2f, 0, 0 }));
		return node;
	}
}
