package com.zerocool.scene.camera;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.CameraNode;
import com.jme.scene.Node;

/*
 * User Camera
 * Generates a top down view for the user.
 */
public class UserCamera
{
	//Location of the camera's center
	private Vector3f location;
	
	//horizontal distance of the camera in relation to the center
	private float distance;
	
	//vertical distance in relation to the center
	private float height;
	
	//angle of the camera's rotation around the center point
	private float angle;
	
	//angle down of camera in relation to the center point
	private float angle_down;
	
	//Nodes for camera attachment in scene.
	private Node camNode;
	private Node node1;
	
	/*
	 * publc UserCamera
	 * creates and initializes the camera object
	 */
	public UserCamera(Vector3f location, float distance, float height, CameraNode cam)
	{
		//First set the angle to zero
		angle=0;
		//Creat the nodes
		camNode = new Node();
		node1 = new Node();
		//Attach the angled node to the translated node
		camNode.attachChild(node1);
		//Add the camera to the node
		node1.attachChild(cam);
		//Set location
		this.location = location;
		//Set height
		this.height = height;
		//Set distance
		this.distance = distance;
		//Initial update to compute the angle of the camera
		update();		
	}
	
	/*
	 * Update
	 * Double Checks all math within the camera class and regenerates the node
	 */
	public void update()
	{
		Vector3f temp = new Vector3f(-distance * (float)Math.sin(angle),height,-distance * (float)Math.cos(angle));
		float theta = (float) Math.atan(height / distance);
		node1.setLocalRotation(new Quaternion(new float[]{theta,0,0}));
		camNode.setLocalRotation(new Quaternion(new float[]{0,angle,0}));
		temp = temp.add(location);
		camNode.setLocalTranslation(temp);
		angle_down = theta;
	}
	
	/*
	 * Get/Set methods
	 */
	public Node		getNode(){return camNode;}
	public float	getAngle(){return angle;}
	public void		setAngle(float angle){this.angle = angle;}
	public void		setDistance(float distance){this.distance = distance;}
	public float	getDistance(){return distance;}
	public void		setHeight(float height){this.height = height;}
	public float	getHeight(){return height;}
	public float	getAngleDown(){return angle_down;}

	public void setLocation(Vector3f location) {
		this.location = location;
	}

	public Vector3f getLocation() {
		return location;
		
	}
	

}
