package com.zerocool.app;


import org.lwjgl.input.Keyboard;



import com.jme.app.SimpleGame;
import com.jme.bounding.*;
import com.jme.light.LightNode;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.CameraNode;
import com.jme.scene.Node;
import com.jme.scene.SceneElement;
import com.jme.scene.Text;
import com.jme.scene.shape.Box;
import com.jme.scene.shape.Cylinder;
import com.jme.scene.shape.Sphere;
import com.jme.scene.state.CullState;
import com.jme.scene.state.MaterialState;
import com.jme.util.LoggingSystem;
import com.zerocool.console.ConsoleManager;
import com.zerocool.items.ItemObject;
import com.zerocool.managers.GameAction;
import com.zerocool.managers.GameKeyAction;
import com.zerocool.managers.InputManager;
import com.zerocool.managers.State;
import com.zerocool.scene.GameObject;
import com.zerocool.scene.PlayerObject;
import com.zerocool.scene.camera.UserCamera;
import com.zerocool.scene.level.Level;




/**
 *  <code>ZeroCoolApp</code> initializes the application and sets up all the managers as well
 *  as the engine.
 *
 * @author Chris Gibson
 * @version ZeroCoolApp.java,v 0.1 2006/10/03
 */
public class ZeroCoolApp extends SimpleZCGame{
	
	/*
	 * MANAGERS
	 */
	InputManager input = new InputManager();
	State applicationState = new State(0);
	
	
	/*
	 * LEVEL VARIABLES
	 */
	Level level = new Level();
	//ItemObject item;
	String filename;
	/*
	 * PLAYER VARIABLES
	 */
	PlayerObject player;
	Box box_1;
	/*
	 * SERVER VARIABLES
	 */
	boolean isServer;
	int port;
	String ip;
	
	/*
	 * CAMERA
	 */
	UserCamera userCamera;
	
	/*
	 * CONSOLE
	 */
	ConsoleManager console = new ConsoleManager();
	
	/*
	 * APPLICATION INFORMATION VARIABLES
	 */
	public final String GAME_TITLE = "Simple Game";
	public final String GAME_VERSION = "0.01a : build 20";

	/**
	 * <code>main</code> function begins the application
	 * @param args
	 */
	public static void main(String[] args) {
		if(args.length == 0)
			new ZeroCoolApp(null);
		else
			new ZeroCoolApp(args[0]);
	}
	
	public ZeroCoolApp(String file)
	{
		filename = "Data\\levels\\test.zcl";
		if(file != null)
			filename = "Data\\levels\\" + file;
		
		//JMonkeyEngine stuff
        LoggingSystem.getLogger().setLevel(java.util.logging.Level.WARNING);
        //Create a new instance of the application
        this.setDialogBehaviour(ALWAYS_SHOW_PROPS_DIALOG);
        //Begin the application
        this.start();
	}

	
	/**
	 * <code>simpleInitGame</code> initializes all the jMonkeyEngine stuff, along with all cameras,
	 * levels, models etc...
	 */
	@Override
	protected void simpleInitGame() {
		//Set the title of the game
		display.setTitle(GAME_TITLE);
		
		//Initial Console messages
		console.addText("Engine Initiated");
		console.addText(GAME_TITLE + " v" + GAME_VERSION);
		
		//Add the consol to the screen
		fpsNode.attachChild(console.returnNode());
	

	    //Add the box to the root node

	    //s.setLocalTranslation(new Vector3f(0,10,0));
	    MaterialState ms = display.getRenderer().createMaterialState();
	    ms.setAmbient(new ColorRGBA(0.0f,0.0f,0.5f,0.5f));
	    ms.setEmissive(new ColorRGBA(0.3f,0.1f,0,0.5f));
	    ms.setDiffuse(new ColorRGBA(0,1,0,0.5f));
	    ms.setSpecular(new ColorRGBA(1,1,0,0.5f));
	    ms.setShininess(100);
	    

	    //box_1 = new Box("box",new Vector3f(100,0,130), new Vector3f(80,40,140));
	    //rootNode.attachChild(box_1);
	    //Create a new player object, controlled by the current user
	    player = new PlayerObject(0, ms);
	    
//	  *******COLLISION WORK ***********//
//	    player.setModelBound(new BoundingBox());
//	    box_1.setModelBound(new BoundingBox());
//	  *******COLLISION WORK ***********//
	    
	    player.setLocalTranslation(new Vector3f(100,10,100));
	    player.addChildCamera(userCamera);
	    rootNode.attachChild(player);
	    
	    //Get the level and add it to the root node
	    level.load(filename);
	    rootNode.attachChild(level);
	    
	    //item = new ItemObject(02);
	    
	    //Initialize the camera
	    createCamera();
	    rootNode.attachChild(userCamera.getNode());
	}
	
	
	/**
	 * <code>createCamera</code> initializes the player's camera.
	 *
	 */
	protected void createCamera(){
		//Generates the camera Object, this is never to be edited directly, only used to add to nodes.
	    Camera cam = display.getRenderer().createCamera(display.getWidth(),
	            display.getHeight());
	    	    cam.setFrustum(1.0f, 1000.0f, -0.55f, 0.55f, 0.4125f, -0.4125f);
	    	    cam.update();
	    
	    //Creates the UserCamera
	    userCamera = new UserCamera(new Vector3f(0,10,0),1, 120, new CameraNode("Camera Node", cam));
	    
	    //Adds to the root node [[[[ADD TO PLAYER NODE]]]]
	    player.addChildCamera(userCamera);
	}
	
	
	/**
	 * <code>simpleUpdate</code> is called every render refresh
	 */
	protected void simpleUpdate() {
		//Find the time elapsed, everything in the game will be moving at 30 units/second
		float elapsed = (timer.getFrameRate() / 30f);

		//item.Update(elapsed);
		checkKeyboard(elapsed);
		
//		*******COLLISION WORK ***********//
		/*player.updateModelBound();
		box_1.updateModelBound();
		rootNode.updateWorldBound();
		if(box_1.getWorldBound().intersects(player.getWorldBound())){
			System.out.println("Touching!");
		}*/
//		*******COLLISION WORK ***********//
		if(isServer)
		{
			updateObjects(elapsed);
			updatePlayerObjects(elapsed);
			broadcastObjects();
		}else{
			fetchObjects();
		}
		

		player.Update(elapsed);
		userCamera.update();
	}
	
	public void checkKeyboard(float elapsed)
	{
		if(Keyboard.isCreated()){
			if(Keyboard.isKeyDown(Keyboard.KEY_Q)){
			    // Get current size of heap in bytes
			    long heapSize = Runtime.getRuntime().totalMemory();
			    
			    // Get maximum size of heap in bytes. The heap cannot grow beyond this size.
			    // Any attempt will result in an OutOfMemoryException.
			    long heapMaxSize = Runtime.getRuntime().maxMemory();
			    
			    // Get amount of free memory within the heap in bytes. This size will increase
			    // after garbage collection and decrease as new objects are created.
			    long heapFreeSize = Runtime.getRuntime().freeMemory();
			    System.out.println("--HEAP SIZE: " + heapSize);
			    System.out.println("--MAX HEAP: " + heapMaxSize);
			    System.out.println("--FREE MEMORY: " + heapFreeSize);
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_W))
				userCamera.setHeight(userCamera.getHeight() + (1f / elapsed));
			if(Keyboard.isKeyDown(Keyboard.KEY_S))
				userCamera.setHeight(userCamera.getHeight() - (1f / elapsed));
		
			if(Keyboard.isKeyDown(Keyboard.KEY_A))
				userCamera.setAngle(userCamera.getAngle() + (.1f / elapsed));
			if(Keyboard.isKeyDown(Keyboard.KEY_D))
				userCamera.setAngle(userCamera.getAngle() - (.1f / elapsed));
			
			if(Keyboard.isKeyDown(Keyboard.KEY_R))
				userCamera.setDistance(userCamera.getDistance() + (1f / elapsed));
			if(Keyboard.isKeyDown(Keyboard.KEY_F))
				userCamera.setDistance(userCamera.getDistance() - (1f / elapsed));
			}
	}
	
	public void updateObjects(float elapsed)
	{
		
	}
	
	public void updatePlayerObjects(float elapsed)
	{
		player.onThink(elapsed);
	}
	
	public void broadcastObjects()
	{
		
	}
	
	public void fetchObjects()
	{
		
	}

}
