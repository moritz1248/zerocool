//Code by Jonathan Sanford, Revention Software(tm)...this code is the intellectual property of Jonathan Sanford and should only be used by the members of Project Zero Cool for the design of said project
package com.zerocool.editor;

import com.zerocool.scene.DynamicGameObject;
import com.zerocool.scene.GameObject;
import com.zerocool.scene.level.TileObject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.io.*;

//this edits the levels which consists of tiles...therein the name tileleveleditor.  cause its a level of tiles, a tilelevel...ohhhh
public class TLEditor extends JPanel implements ActionListener, WindowListener
{
	//the menuBar actually is added to the frame not the panel, so even though the panel implements the menuBar, it doesn't get to keep it.
	private JMenuBar menuBar;
	private JMenu fileMenu, layerMenu, tileMenu, toolMenu, helpMenu;
	//MI stands for menu item...or maybe my intelligence
	private JMenuItem newMI, openMI, saveMI, exitMI, layerUpMI, layerDownMI, addLayerMI, clearLayerMI, syncronizeMI, addTileMI, editTileMI, deleteTileMI, addBorderMI, addWallMI, addBlockMI, helpMI, aboutMI, editPUMI, deletePUMI;
	//attempting to add a popup menu
	private JPopupMenu popMenu;
	//these are the two views of the current level:
	//layerView is only of the current layer
	//compositeView is of the whole level, a composite of all the layers
	private TLEviewer layerView, compositeView;
	//the controlPanel holds all the controls. the navPanel just holds the up and down buttons
	private JPanel controlPanel, navPanel;
	//addTile adds a tile, adjustView adjusts the view, layer up moves up a layer, layer down does the tango and addLayer owns a deep fat fryer
	private JButton addTile, layerUp, layerDown, addLayer;
	//this controls whether the two views are synchronized
	private JRadioButton syncronize;
	//layerLabel just displays the number of the current layer
	private JLabel layerLabel, xPosnLabel, zPosnLabel;
	//the number of the current layer
	private int layer;
	//the current selected Object in this layer
	private GameObject selected;
	//this is the current level
	private ArrayList<ArrayList<GameObject>> level;
	//this var determines whether everything will be printed or not
	private boolean reportOn;
	//this determines the next id to be used
	private int nextID;
	//determines state for mouse functions (basically just used for create objects)
	private int mouseState;
	//for use whenever needed
	private ObjectEditor objEdit;
	//for the drag function
	private boolean isDragging;
	//so that the program remembers where you store the data files
	private File lastAccessed;
	//this remembers the starting location of the wall/block
	private int xStart, zStart;
	
	//ALL HAIL THE EDITOR!!!
	public TLEditor()
	{
		reportOn = true;
		System.out.println(reportOn);
		
		objEdit = new ObjectEditor();
		
		isDragging = false;
		
		xStart = zStart = -1;
		
		nextID = 100;
		
		mouseState = 0;
		
		String file = read("FilePath=");
		if(file != null)
			lastAccessed = new File(file);
		else
			lastAccessed = new File(System.getProperty("user.dir"));
		
		level = new ArrayList<ArrayList<GameObject>>();
		//this initaties the first layer; layer 0
		level.add(new ArrayList<GameObject>());
		
		layer = 0;
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		//the views
		layerView = new TLEviewer(0, this);
		compositeView = new TLEviewer(-1, this);
		layerView.addBrother(compositeView);
		compositeView.addBrother(layerView);
		//the buttons
		addTile = new JButton("Add Tile");
		addTile.addActionListener(this);
		layerUp = new JButton("UP");
		layerUp.addActionListener(this);
		layerDown = new JButton("DOWN");
		layerDown.addActionListener(this);
		addLayer = new JButton("Add Layer");
		addLayer.addActionListener(this);
		syncronize = new JRadioButton("Syncronize Views");
		syncronize.addActionListener(this);
		//the labels
		layerLabel = new JLabel("Layer: 0");
		xPosnLabel = new JLabel("X: - ");
		zPosnLabel = new JLabel("Z: - ");
		//the navPanel
		navPanel = new JPanel();
		navPanel.setLayout(new GridLayout(1,2));
		navPanel.add(layerUp);
		navPanel.add(layerDown);
		//the controlPanel
		controlPanel = new JPanel();
		controlPanel.setPreferredSize(new Dimension(200, 500));
		controlPanel.setLayout(new GridLayout(10,1));
		controlPanel.add(addTile);
		controlPanel.add(layerLabel);
		controlPanel.add(navPanel);
		controlPanel.add(addLayer);
		controlPanel.add(syncronize);
		controlPanel.add(xPosnLabel);
		controlPanel.add(zPosnLabel);
		//mix it all up and what do you get...?
		add(layerView);
		add(controlPanel);
		add(compositeView);
		
		//popup menu???
		popMenu = new JPopupMenu();
		editPUMI = new JMenuItem("Edit");
		editPUMI.addActionListener(this);
		deletePUMI = new JMenuItem("Delete");
		deletePUMI.addActionListener(this);
		popMenu.add(editPUMI);
		popMenu.add(deletePUMI);
		
		//all that follows is menu crap
		menuBar = new JMenuBar();
		
		fileMenu = new JMenu("File");
		layerMenu = new JMenu("Layer");
		tileMenu = new JMenu("Tile");
		toolMenu = new JMenu("Tools");
		helpMenu = new JMenu("Help");
		
		newMI = new JMenuItem("New");
		newMI.addActionListener(this);
		openMI = new JMenuItem("Open");
		openMI.addActionListener(this);
		saveMI = new JMenuItem("Save");
		saveMI.addActionListener(this);
		exitMI = new JMenuItem("Exit");
		exitMI.addActionListener(this);
		layerUpMI = new JMenuItem("Up");
		layerUpMI.addActionListener(this);
		layerDownMI = new JMenuItem("Down");
		layerDownMI.addActionListener(this);
		addLayerMI = new JMenuItem("Add");
		addLayerMI.addActionListener(this);
		clearLayerMI = new JMenuItem("Clear");
		clearLayerMI.addActionListener(this);
		syncronizeMI = new JMenuItem("Syncronize Views");
		syncronizeMI.addActionListener(this);
		addTileMI = new JMenuItem("Add");
		addTileMI.addActionListener(this);
		editTileMI = new JMenuItem("Edit");
		editTileMI.addActionListener(this);
		deleteTileMI = new JMenuItem("Delete");
		deleteTileMI.addActionListener(this);
		addBorderMI = new JMenuItem("Add Border");
		addBorderMI.addActionListener(this);
		addWallMI = new JMenuItem("Add Wall");
		addWallMI.addActionListener(this);
		addBlockMI = new JMenuItem("Add Block");
		addBlockMI.addActionListener(this);
		helpMI = new JMenuItem("Help");
		helpMI.addActionListener(this);
		aboutMI = new JMenuItem("About");
		aboutMI.addActionListener(this);
		
		fileMenu.add(newMI);
		fileMenu.addSeparator();
		fileMenu.add(openMI);
		fileMenu.add(saveMI);
		fileMenu.addSeparator();
		fileMenu.add(exitMI);
		layerMenu.add(layerUpMI);
		layerMenu.add(layerDownMI);
		layerMenu.addSeparator();
		layerMenu.add(addLayerMI);
		layerMenu.add(clearLayerMI);
		layerMenu.addSeparator();
		layerMenu.add(syncronizeMI);
		tileMenu.add(addTileMI);
		tileMenu.add(editTileMI);
		tileMenu.add(deleteTileMI);
		toolMenu.add(addBorderMI);
		toolMenu.add(addWallMI);
		toolMenu.add(addBlockMI);
		helpMenu.add(helpMI);
		helpMenu.add(aboutMI);
		
		menuBar.add(fileMenu);
		menuBar.add(tileMenu);
		menuBar.add(layerMenu);
		menuBar.add(toolMenu);
		menuBar.add(helpMenu);
	}
	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		if(source == newMI)
		{
			report("User choose to create a new level");
			renew();
		}
		else if(source == openMI)
		{
			report("User choose to open a saved level");
			open();
		}
		else if(source == saveMI)
		{
			report("User choose to save the current level");
			saved();
		}
		else if(source == exitMI)
		{
			report("User choose to exit");
			exit();
		}
		else if(source == layerUp || source == layerUpMI)
		{
			report("User choose to view next layer up");
			changeLayer(true);
		}
		else if(source == layerDown || source == layerDownMI)
		{
			report("User choose to view next layer down");
			changeLayer(false);
		}
		else if(source == addLayer || source == addLayerMI)
		{
			report("User choose to add a layer");
			addLayer();
		}
		else if(source == syncronize || source == syncronizeMI)
		{
			if(source == syncronizeMI)
				syncronize.setSelected(!syncronize.isSelected());
			if(syncronize.isSelected())
				report("User choose to syncronize views");
			else
				report("User choose to allow free view");
			compositeView.syncronize();
		}
		else if(source == clearLayerMI)
		{
			report("User choose to add a layer");
			clearLayer();
		}
		else if(source == addTile || source == addTileMI)
		{
			report("User choose to add a tile");
			addTile();
		}
		else if(source == editTileMI || source == editPUMI)
		{
			report("User choose to edit the selected tile");
			editTile();
		}
		else if(source == deleteTileMI || source == deletePUMI)
		{
			report("User choose to delete the selected tile");
			deleteTile();
		}
		else if(source == addBorderMI)
		{
			report("User choose to add a border");
			setLayer();
		}
		else if(source == addWallMI)
		{
			report("User choose to add a wall");
			addWall();
		}
		else if(source == addBlockMI)
		{
			report("User choose to add a block of tiles");
			addBlock();
		}
		else if(source == helpMI)
		{
			report("User choose to ask for help");
			openHelp();
		}
		else if(source == aboutMI)
		{
			openAbout();
			report("User choose to view the info about this program");
		}
		else
			report("User choose to do nothing");
	}
	public void clearLayer()
	{
		level.set(layer, new ArrayList());
		repaint();
	}
	public void renew()
	{
		layer = 0;
		layerView.setLayer(layer);
		layerLabel.setText("Layer: " + layer);
		level = new ArrayList();
		level.add(new ArrayList());
		int choice = JOptionPane.showConfirmDialog(this, "Do you want to border the level area?", "Watch for illegal immigrants", JOptionPane.YES_NO_OPTION);
		if(choice == JOptionPane.YES_OPTION)
		{
			setLayer();
		}
	}
	public void setLayer()
	{
		String input = JOptionPane.showInputDialog(this, "Type in the desired dimensions of the level (width,height)", "20,20");
		report(input);
		if(input != null && input.indexOf(',') > 0)
		{
			int index = input.indexOf(',');
			int index2 = input.indexOf(' ');
			if(index2 < index && index2 > 0)
				index = index2;
			int x = Integer.parseInt(input.substring(0, index));
			while(input.charAt(index) < '0' || input.charAt(index) > '9') index++;
			int z = Integer.parseInt(input.substring(index, input.length()));
			level.set(layer, new ArrayList<GameObject>());
			selected = null;
			for(int a = 1; a < x - 1; a++)
			{
				TileObject t1 = new TileObject(nextId(), 1, a, layer, 0, 1);
				TileObject t2 = new TileObject(nextId(), 1, a, layer, z - 1, 1);
				level.get(layer).add(t1);
				level.get(layer).add(t2);
			}
			for(int a = 1; a < z - 1; a++)
			{
				TileObject t3 = new TileObject(nextId(), 1, 0, layer, a, 1);
				TileObject t4 = new TileObject(nextId(), 1, x - 1, layer, a, 1);
				level.get(layer).add(t3);
				level.get(layer).add(t4);
			}
			TileObject corner1 = new TileObject(nextId(), 1, 0, layer, 0, 1);
			TileObject corner2 = new TileObject(nextId(), 1, x - 1, layer, z - 1, 1);
			TileObject corner3 = new TileObject(nextId(), 1, 0, layer, z - 1, 1);
			TileObject corner4 = new TileObject(nextId(), 1, x - 1, layer, 0, 1);
			level.get(layer).add(corner1);
			level.get(layer).add(corner2);
			level.get(layer).add(corner3);
			level.get(layer).add(corner4);
			layerView.setCorner(0, 0);
			compositeView.setCorner(0, 0);
			report("Level reset");
		}
		repaint();
	}
	//allows the coder to turn on or off the output
	public void report(String out)
	{
		if(reportOn)
			System.out.println(out);
	}
	//returns the next id
	public int nextId()
	{
		return nextID++;
	}
	public void open()
	{
		int choice = JOptionPane.showConfirmDialog(this, "Do you want to save the current level first?", "Save?", JOptionPane.YES_NO_OPTION);
		if(choice == JOptionPane.YES_OPTION)
			saved();
		JFileChooser chooser = new JFileChooser(lastAccessed);
		choice = chooser.showOpenDialog(this);
	    if(choice == JFileChooser.APPROVE_OPTION && chooser.getSelectedFile() != null)
	    {
	    	File file = chooser.getSelectedFile();
	    	lastAccessed = file;
			//this code opens the file
			try
			{
				FileInputStream fileIS = new FileInputStream(file);
				ObjectInputStream inStream = new ObjectInputStream(fileIS);
				ArrayList newLevel = (ArrayList)inStream.readObject();
				if(newLevel != null)
					level = newLevel;
			}
			catch(IOException e)
			{
				report("IOException thrown while trying to open level " + file + ";  Exception caught");
				report(e.toString());
			}
			catch(ClassNotFoundException e)
			{
				report("ClassNotFoundException thrown while trying to open level " + file + ";  Exception caught");
			}
	    }
	    selected = null;
	    layer = 0;
		layerView.setLayer(layer);
		layerLabel.setText("Layer: " + layer);
		layerView.setCorner(0,0);
		compositeView.setCorner(0,0);
	    repaint();
	}
	public boolean saved()
	{
		JFileChooser chooser = new JFileChooser(lastAccessed);
		int choice = chooser.showSaveDialog(this);
	    if(choice == JFileChooser.APPROVE_OPTION && chooser.getSelectedFile() != null)
	    {
			File file = chooser.getSelectedFile();
			report("Attempting to save as " + file);
			lastAccessed = file;
			//this code saves the file
			try
			{
				FileOutputStream fileOS = new FileOutputStream(file);
				ObjectOutputStream outStream = new ObjectOutputStream (fileOS);
				outStream.writeObject(level);
			}
			catch(FileNotFoundException e)
			{
				report("FileNotFoundException thrown while trying to save level as " + file + ".dat;  Exception caught");
				return false;
			}
			catch(IOException e)
			{
				report("IOException thrown while trying to save level as " + file + ".dat;  Exception caught");
				report(e.toString());
				return false;
			}
			return true;
		}
		return false;
	}
	public String read(String var)
	{
		try
		{
			report("Directory: " + System.getProperty("user.dir"));
			FileReader fr = new FileReader(System.getProperty("user.dir") + "/Apps/com/zerocool/editor/config.txt");
			BufferedReader reader = new BufferedReader(fr);
			report("Attempting to read first line.");
			String nextLine;
			for(nextLine = reader.readLine(); nextLine != null; nextLine = reader.readLine())
				if(nextLine.startsWith(var))
				{
					if(nextLine.charAt(var.length()) == '*')
						return System.getProperty("user.dir") + nextLine.substring(var.length() + 1, nextLine.length());
					else
						return nextLine.substring(var.length(), nextLine.length());
				}
		}
		catch(FileNotFoundException e)
		{
			report("Exception caught: " + e.toString());
		}
		catch(IOException e)
		{
			report("Exception caught: " + e.toString());
		}
		return null;
		
	}
	public void changeLayer(boolean isUp)
	{
		if(isUp && layer + 1 < level.size())
		{
			layer++;
			selected = null;
			layerView.setLayer(layer);
			layerLabel.setText("Layer: " + layer);
			report("Layer increased. Current layer: " + layer);
		}
		else if(!isUp && layer > 0)
		{
			layer--;
			selected = null;
			layerView.setLayer(layer);
			layerLabel.setText("Layer: " + layer);
			report("Layer decreased. Current layer: " + layer);
		}
		repaint();
	}
	public void addLayer()
	{
		level.add(new ArrayList());
		selected = null;
		layer = level.size() - 1;
		layerView.setLayer(layer);
		layerLabel.setText("Layer: " + layer);
		report("Layer added. Current layer: " + layer);
		repaint();
	}
	public void addTile()
	{
		//sets the mouseState to allow the user to add a tile
		if(mouseState == 0 && !objEdit.isOpen())
			mouseState = 1;
	}
	public void addWall()
	{
		//sets the mouseState to allow the user to add a wall
		if(mouseState == 0 && !objEdit.isOpen())
			mouseState = 2;
	}
	public void addBlock()
	{
		//sets the mouseState to allow the user to add a block of tiles
		if(mouseState == 0 && !objEdit.isOpen())
			mouseState = 3;
	}
	public void createTiles(int x, int z, int type)
	{
		//adapt functioning from calls to draw function
		if(type == 2)
		{
			if(xStart != -1)
			{
				int xA, zA, dX, dZ, l, h;
				dX = Math.abs(x - xStart) + 1;
				dZ = Math.abs(z - zStart) + 1;
				if(dX >= dZ)
				{
					l = dX;
					h = 1;
				}
				else
				{
					l = 1;
					h = dZ;
				}
				if(x < xStart && h == 1)
					xA = x;
				else
					xA = xStart;
				if(z > zStart && l == 1)
					zA = z;
				else
					zA = zStart;
				createBlock(xA, zA, l, h);
			}
		}
		else if(type == 3)
		{
			if(xStart != -1)
			{
				int xA, zA, dX, dZ;
				dX = Math.abs(x - xStart) + 1;
				dZ = Math.abs(z - zStart) + 1;
				if(x < xStart)
					xA = x;
				else
					xA = xStart;
				if(z > zStart)
					zA = z;
				else
					zA = zStart;
				createBlock(xA, zA, dX, dZ);
			}
		}
	}
	public void createBlock(int x, int z, int dx, int dz)
	{
		ArrayList lyr = level.get(layer);
		for(int a = 0; a < dx; a++)
			for(int b = 0; b < dz; b++)
				if(objectAt(x + a,z - b) == null)
					lyr.add(new TileObject(nextId(),1,x+a,layer,z-b,1)); 
		repaint();
	}
	public void editTile()
	{
		//opens a editor box
		if(!objEdit.isOpen() && selected != null)
		{
			objEdit = new ObjectEditor(selected, this);
		}
	}
	public void deleteTile()
	{
		if(selected != null)
		{
			int num = level.size();
			if(level.get(layer).remove(selected) && level.size() + 1 == num)
				report("Tile deleted successfully");
			else
				report("Error in tile deletion");
			selected = null;
			repaint();
		}
		report("No tile selected for deletion");
	}
	public void openHelp()
	{
		//add code to open a help box
		JFrame frame = new JFrame("Help");
		frame.setPreferredSize(new Dimension(600, 600));
		JTextArea helpText = new JTextArea();
		helpText.setLineWrap(true);
		helpText.setWrapStyleWord(true);
		helpText.setEditable(false);
		String helpPath = read("HelpPath=");
		try
		{
			FileReader fr = new FileReader(helpPath);
			BufferedReader reader = new BufferedReader(fr);
			helpText.read(reader, null);
			JScrollPane scroll = new JScrollPane(helpText, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			frame.getContentPane().add(scroll);
			frame.pack();
			frame.setVisible(true);
			report("Help menu opened.  Lines in document: " + helpText.getLineCount());
		}
		catch(FileNotFoundException e)
		{
			report("Exception caught: " + e.toString());
		}
		catch(IOException e)
		{
			report("Exception caught: " + e.toString());
		}
	}
	public void openAbout()
	{
		//add code to display info about the editor
		JFrame frame = new JFrame("About");
		frame.setPreferredSize(new Dimension(200, 110));
		String text = "Tile Level Editor";
		text += "\nVersion: 1.0";
		text += "\nAuthor: Jonathan Sanford";
		text += "\nCompany: Revention Software(tm)";
		JTextArea aboutText = new JTextArea(text);
		aboutText.setEditable(false);
		frame.getContentPane().add(aboutText);
		frame.pack();
		frame.setVisible(true);
	}
	public boolean canDragObject(int x, int z, int button)
	{
		if(!objEdit.isOpen())
			if(mouseState == 2 || mouseState == 3)
			{
				if(xStart == -1)
				{
					xStart = x;
					zStart = z;
				}
				return true;
			}
			else if(isDragging)
			{
				//move object
				if(objectAt(x,z) == null && selected != null)
				{
					selected.setX(x);
					selected.setZ(z);
					repaint();
				}
				return true;
			}
			else if(objectAt(x,z) != null && selected == objectAt(x,z))
			{
				isDragging = true;
				return true;
			}
			else if(objectAt(x,z) != null)
			{
				selected = objectAt(x,z);
				isDragging = true;
				return true;
			}
			else
				return false;
		else
			return true;
	}
	public void mouseReleased(int x, int z)
	{
		isDragging = false;
		if(mouseState == 2 || mouseState == 3)
		{
			createTiles(x, z, mouseState);
			mouseState = 0;
		}
		xStart = zStart = -1;
	}
	public void mouseClicked(int x, int z, int button)
	{
		if(objEdit.isOpen())
			mouseState = -1;
		if(mouseState == 1 && objectAt(x,z) == null)
		{
			//add tile
			TileObject tile = new TileObject(nextId(), 1, x, layer, z, 1);
			level.get(layer).add(tile);
			selected = tile;
			editTile();
		}
		else if(mouseState == 0)
		{
			if(objectAt(x,z) != selected)
				selected = objectAt(x,z);
			else if(button == 1 && selected != null)
			{
				int orient = selected.getOrientation() + 1;
				if(orient > 4)
					orient = 1;
				selected.setOrientation(orient);
			}
			else if(button == 3 && selected != null)
			{
				popMenu.show(this, x * 25 + 25, 500 - (z * 25));
			}
		}
		repaint();
		mouseState = 0;
	}
	public void updateMouse(int x, int z)
	{
		xPosnLabel.setText("X = " + x + " ");
		zPosnLabel.setText("Z = " + z + " ");
		if(mouseState == 1)
			layerView.draw(x, z, 1, 1, true);
		else if(mouseState == 2)
		{
			if(xStart != -1)
			{
				int xA, zA, dX, dZ, l, h;
				dX = Math.abs(x - xStart) + 1;
				dZ = Math.abs(z - zStart) + 1;
				if(dX >= dZ)
				{
					l = dX;
					h = 1;
				}
				else
				{
					l = 1;
					h = dZ;
				}
				if(x < xStart && h == 1)
					xA = x;
				else
					xA = xStart;
				if(z > zStart && l == 1)
					zA = z;
				else
					zA = zStart;
				layerView.draw(xA, zA, l, h, true);
			}
			else
				layerView.draw(x, z, 1, 1, true);
		}
		else if(mouseState == 3)
		{
			if(xStart != -1)
			{
				int xA, zA, dX, dZ;
				dX = Math.abs(x - xStart) + 1;
				dZ = Math.abs(z - zStart) + 1;
				if(x < xStart)
					xA = x;
				else
					xA = xStart;
				if(z > zStart)
					zA = z;
				else
					zA = zStart;
				layerView.draw(xA, zA, dX, dZ, true);
			}
			else
				layerView.draw(x, z, 1, 1, true);
		}
	}
	private GameObject objectAt(int x, int z)
	{
		ArrayList<GameObject> lyr = level.get(layer);
		for(GameObject go : lyr)
			if((int)go.getX() == x && (int)go.getZ() == z)
				return go;
		return null;
	}
	public void edited(GameObject newGO)
	{
		ArrayList lyr = level.get(layer);
		lyr.set(lyr.indexOf(selected), newGO);
		selected = newGO;
		report("Changes have been recorded");
		repaint();
	}
	//this lets the frame add the menuBar.
	public JMenuBar getMenuBar()
	{
		return menuBar;
	}
	private void exit()
	{
		int choice = JOptionPane.showConfirmDialog(this, "Do you want to save before closing?", "10 seconds until self destruct, 9, 8...", JOptionPane.YES_NO_OPTION); 
		if(choice == JOptionPane.YES_OPTION)
		{
			if(saved())
				System.exit(0);
		}
		else
			System.exit(0);
	}
	public void windowClosing(WindowEvent w)
	{
		exit();
	}
	public void windowOpened(WindowEvent w){}
	public void windowClosed(WindowEvent w){}
	public void windowIconified(WindowEvent w){}
	public void windowDeiconified(WindowEvent w){}
	public void windowActivated(WindowEvent w){}
	public void windowDeactivated(WindowEvent w){}
	
	
	//******ALL CODE BELOW THIS POINT IS FOR PRIVATE CLASSES******//
	
	//a private class to handle the graphics for the different views
	private class TLEviewer extends JPanel
	{
		//determines which layer will be drawn...if -1 then all layers will be drawn
		private int layer, xCorner, zCorner, xStandard, zStandard, xA, zA, dX, dZ;
		private TLEditor parent;
		private TLEviewer brother;
		
		private TLEviewer(int lyr, TLEditor tle)
		{
			layer = lyr;
			xCorner = zCorner = 0;
			xStandard = zStandard = -1;
			xA = zA = dX = dZ = -1;
			//i prefer that size
			setPreferredSize(new Dimension(500, 500));
			Listener listening = new Listener(this);
			addMouseListener(listening);
			addMouseMotionListener(listening);
			parent = tle;
		}
		
		public void addBrother(TLEviewer sibling)
		{
			brother = sibling;
		}
		
		public void syncronize()
		{
			if(brother != null && syncronize.isSelected())
				brother.setCorner(xCorner, zCorner);
			parent.repaint();
		}
		
		//functions for use with syncronize
		public int getXCorner()
		{
			return xCorner;
		}
		public int getZCorner()
		{
			return zCorner;
		}
		public void setCorner(int x, int z)
		{
			xCorner = x;
			zCorner = z;
		}
		public void reset()
		{
			xA = zA = dX = dZ = -1;
		}
		public void setLayer(int lyr)
		{
			layer = lyr;
			repaint();
		}
		public void mouseDragged(MouseEvent m)
		{
			int x = (m.getX() / 25);
			int z = ((500 - m.getY()) / 25);
			if(!parent.canDragObject(x + xCorner, z + zCorner, m.getButton()))
			{
				if(xStandard == -1)
				{
					xStandard = x;
					zStandard = z;
				}
				else
				{
					xCorner -= x - xStandard;
					zCorner -= z - zStandard;
					xStandard = x;
					zStandard = z;
					syncronize();
				}
			}
			parent.updateMouse(x + xCorner, z + zCorner);
			repaint();
		}
		public void mouseClicked(MouseEvent m)
		{
			int x = (m.getX() / 25) + xCorner;
			int z = ((500 - m.getY()) / 25) + zCorner;
			parent.mouseClicked(x, z, m.getButton());
		}
		public void mouseReleased(MouseEvent m)
		{
			xStandard = zStandard = -1;
			draw(-1, -1, -1, -1, true);
			int x = (m.getX() / 25) + xCorner;
			int z = ((500 - m.getY()) / 25) + zCorner;
			parent.mouseReleased(x, z);
		}
		public void mouseMoved(MouseEvent m)
		{
			int x = (m.getX() / 25) + xCorner;
			int z = ((500 - m.getY()) / 25) + zCorner;
			parent.updateMouse(x, z);
		}
		public void draw(int x, int z, int l, int h, boolean drawBrother)
		{
			xA = x;
			zA = z;
			dX = l;
			dZ = h;
			if(drawBrother)
				brother.draw(x, z, l, h, false);
			repaint();
		}
		
		//graphics!!!!
		public void paint(Graphics g)
		{
			//background
			g.setColor(Color.black);
			g.fillRect(0,0,500,500);
			
			//selected square
			if(selected != null)
			{
				g.setColor(Color.orange);
				g.fillRect(((int)selected.getX() - xCorner) * 25, 500 - (((int)selected.getZ() - zCorner + 1) * 25), 25, 25);
			}
			
			//highlighted squares
			if(xA != -1)
			{
				g.setColor(Color.yellow);
				g.fillRect((xA - xCorner) * 25, 500 - ((zA - zCorner + 1) * 25), dX * 25, dZ * 25);
			}
			
			//grid
			g.setColor(Color.blue);
			for(int a = 1; a < 20; a++)
			{
				g.drawLine(0, a*25, 500, a*25);
				g.drawLine(a*25, 0, a*25, 500);
			}
			
			//objects
			if(layer == -1)
				for(int lyr = 0; lyr < level.size(); lyr++)
					drawLayer(lyr, false, g);
			else
				drawLayer(layer, true, g);
		}
		
		public void drawLayer(int layerNum, boolean isSingle, Graphics g)
		{
			//draw all objects which reside in the current layer
			ArrayList<GameObject> layer = level.get(layerNum);
			//shade is the highest shade used for this layer
			int shade;
			if(isSingle)
				shade = 255;
			else
				shade = ((206 * (layerNum + 1)) / level.size()) + 49;
			//interval is the range of shades that can be used for this layer
			int interval;
			if(isSingle)
				interval = 206;
			else
				interval = 206 / level.size();
			for(GameObject tile : layer)
			{
				int type = tile.getType();
				int orientation = tile.getOrientation();
				int x = ((int)tile.getX() - xCorner) * 25;
				int z = 500 - (((int)tile.getZ() - zCorner + 1) * 25);
				//all shading according to layer
				if(type == -1)
				{
					//red triangle for any Dynamic Game Object
					g.setColor(new Color(shade, 0, 0));
					if(orientation == 1)
					{
						int[] polyX = {x + 3, x + 12, x + 21};
						int[] polyZ = {z + 21, z + 3, z + 21};
						g.fillPolygon(polyX, polyZ, 3);
					}
					else if(orientation == 2)
					{
						int[] polyX = {x + 3, x + 21, x + 3};
						int[] polyZ = {z + 21, z + 12, z + 3};
						g.fillPolygon(polyX, polyZ, 3);
					}
					else if(orientation == 3)
					{
						int[] polyX = {x + 3, x + 12, x + 21};
						int[] polyZ = {z + 3, z + 21, z + 2};
						g.fillPolygon(polyX, polyZ, 3);
					}
					else if(orientation == 4)
					{
						int[] polyX = {x + 21, x + 3, x + 21};
						int[] polyZ = {z + 21, z + 12, z + 3};
						g.fillPolygon(polyX, polyZ, 3);
					}
				}
				else if(type == 1)
				{
					//green full square for a solid block Tile Object
					g.setColor(new Color(0, shade, 0));
					g.fillRect(x + 3, z + 3, 18, 18);
				}
				else if(type == 2)
				{
					//green triangle for a sloped wall Tile Object
					g.setColor(new Color(0, shade, 0));
					if(orientation == 1)
					{
						int[] polyX = {x + 3, x + 21, x + 3};
						int[] polyZ = {z + 3, z + 3, z + 21};
						g.fillPolygon(polyX, polyZ, 3);
					}
					else if(orientation == 2)
					{
						int[] polyX = {x + 3, x + 21, x + 21};
						int[] polyZ = {z + 3, z + 3, z + 21};
						g.fillPolygon(polyX, polyZ, 3);
					}
					else if(orientation == 3)
					{
						int[] polyX = {x + 21, x + 21, x + 3};
						int[] polyZ = {z + 3, z + 21, z + 21};
						g.fillPolygon(polyX, polyZ, 3);
					}
					else if(orientation == 4)
					{
						int[] polyX = {x + 3, x + 3, x + 21};
						int[] polyZ = {z + 3, z + 21, z + 21};
						g.fillPolygon(polyX, polyZ, 3);
					}
				}
				else if(type == 3)
				{
					//green fading square for a sloped ramp Tile Object
					for(int step = 0; step < 18; step++)
					{
						if(isSingle)
							g.setColor(new Color(0,(206 * step / 18) + 50, 0));
						else
							g.setColor(new Color(0,(interval*layerNum + (interval*step/18)) + 50,0));
						if(orientation == 1)
							g.drawLine(x + 3, z + 21 - step,x + 21, z + 21 - step);
						else if(orientation == 2)
							g.drawLine(x + 3 + step, z + 3, x + 3 + step, z + 21);
						else if(orientation == 3)
							g.drawLine(x + 3, z + 3 + step,x + 21, z + 3 + step);
						else if(orientation == 4)
							g.drawLine(x + 21 - step, z + 3, x + 21 - step, z + 21);
					}
				}
				else if(type == 4)
				{
					//green empty square for a spawn point Tile Object
					g.setColor(new Color(0, shade, 0));
					g.drawRect(x + 3, z + 3, 18, 18);
				}
				else if(type == 5)
				{
					//green x for a special tile Tile Object
					g.setColor(new Color(0, shade, 0));
					g.drawLine(x + 3, z + 3, x + 21, z + 21);
					g.drawLine(x + 3, z + 21, x + 21, z +3);
				}
			}
		}
	}
	
	//this class allows the parent class (in this case TLEditor) the ability to only define certain functions without throwing a compiling error
	private class Listener implements MouseMotionListener, MouseListener, KeyListener
	{
		//MouseInputListener...just found it...may be very useful...but i'll stick with the current implementation
		private TLEviewer parent;
		private Listener(TLEviewer tlv)
		{
			parent = tlv;
		}
		
		public void mouseMoved(MouseEvent m)
		{
			parent.mouseMoved(m);
		}
		public void mouseEntered(MouseEvent m)
		{
		}
		public void mouseExited(MouseEvent m)
		{
			xPosnLabel.setText("X = - ");
			zPosnLabel.setText("Z = - ");
		}
		public void mouseDragged(MouseEvent m)
		{
			parent.mouseDragged(m);
		}
		public void mouseClicked(MouseEvent m)
		{
			parent.mouseClicked(m);
		}
		public void mouseReleased(MouseEvent m)
		{
			parent.mouseReleased(m);
		}
		public void mousePressed(MouseEvent m)
		{
		}
		public void keyTyped(KeyEvent k)
		{
		}
		public void keyPressed(KeyEvent k)
		{
		}
		public void keyReleased(KeyEvent k)
		{
		}
	}
	
	private class CustomFNF implements FilenameFilter
	{
		String extension;
		private CustomFNF(String ext)
		{
			extension = ext;
		}
		public boolean accept(File dir, String name)
		{
			return (name.endsWith(extension));
		}
	}
	
	//this is used to edit tiles and dynamicGameObjects
	private class ObjectEditor extends WindowAdapter implements ActionListener
	{
		private GameObject before, after;
		private TLEditor parent;
		private JFrame frame;
		private JPanel panel, rotate, options, left, right;
		private JLabel orientL, imageL, rotateL, posnL, xL, yL, zL, typeL, fileL;
		private JButton rLeft, rRight, reset, keep;
		private JComboBox typeCB, fileCB;
		private boolean isTile;
		private File path;
		private File[] allFiles;
		
		//this is just to create a dummy class
		private ObjectEditor()
		{
			frame = null;
		}
		
		//currently this is configured mainly for a TileObject...more functionality will be added later for the DynamicGameObject
		private ObjectEditor(GameObject go, TLEditor tle)
		{
			//functionality allows the user to reset the object to the before state
			before = go;
			after = go.getClone(nextId());
			
			parent = tle;
			
			//by changing this you can change where the program looks to find the dynamic game object item types
			String itemPath = parent.read("ItemPath=");
			if(itemPath != null)
				path = new File(itemPath);
			else
				path = new File("");
			
			//frame and panels
			frame = new JFrame("GameObject Editor");
			frame.addWindowListener(this);
			panel = new JPanel();
			rotate = new JPanel();
			rotate.setLayout(new BoxLayout(rotate, BoxLayout.X_AXIS));
			left = new JPanel();
			left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
			options = new JPanel();
			options.setLayout(new BoxLayout(options, BoxLayout.X_AXIS));
			right = new JPanel();
			right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
			
			//labels
			orientL = new JLabel("Orientation: " + go.getOrientation());
			imageL = new JLabel(new ImageIcon(drawObject()));
			rotateL = new JLabel("Rotate: ");
			posnL = new JLabel("Position: ");
			xL = new JLabel("X: " + go.getX());
			yL = new JLabel("Y: " + go.getY());
			zL = new JLabel("Z: " + go.getZ());
			typeL = new JLabel("Type: ");
			fileL = new JLabel("Item Type: ");
			
			//buttons
			rLeft = new JButton("Left");
			rLeft.addActionListener(this);
			rRight = new JButton("Right");
			rRight.addActionListener(this);
			reset = new JButton("Reset");
			reset.addActionListener(this);
			keep = new JButton("Keep");
			keep.addActionListener(this);
			
			//combo boxes
			String[] allTypes = {"solid block", "sloped wall", "sloped ramp", "spawn point node", "special tile", "dynamic object"};
			typeCB = new JComboBox(allTypes);
			int index = go.getType() - 1;
			if(index < 0)
				index = 5;
			typeCB.setSelectedIndex(index);
			typeCB.addActionListener(this);
			
			allFiles = getFiles();
			String[] allNames;
			if(allFiles != null)
			{
				allNames = new String[allFiles.length];
				for(int c = 0; c < allFiles.length; c++)
					allNames[c] = getName(allFiles[c]);
				fileCB = new JComboBox(allNames);
				if(go.getType() == -1)
				{
					File f = ((DynamicGameObject)go).getFile();
					String name = getName(f);
					for(int c = 0; c < allNames.length; c++)
						if(name != null && name.equals(allNames[c]))
						{
							fileCB.setSelectedIndex(c);
							break;
						}
				}
				else
					fileCB.setVisible(false);
				fileCB.addActionListener(this);
			}
			else
			{
				allNames = null;
				fileCB = new JComboBox();
				if(go.getType() != -1)
					fileCB.setVisible(false);
			}
			
			rotate.add(rotateL);
			rotate.add(rLeft);
			rotate.add(rRight);
			
			left.add(orientL);
			left.add(imageL);
			left.add(rotate);
			left.add(xL);
			left.add(yL);
			left.add(zL);
			
			options.add(reset);
			options.add(keep);
			
			right.add(typeL);
			right.add(typeCB);
			right.add(fileL);
			right.add(fileCB);
			right.add(options);
			
			panel.add(left);
			panel.add(right);
			
			frame.getContentPane().add(panel);
			frame.pack();
			frame.setVisible(true);
		}
		
		private File[] getFiles()
		{
			//add code to look in the specified folder (path) for any and all DynamicGameObject .zci files
			File[] all = path.listFiles();
			System.out.println("all files of parent dir " + path.toString() + " :: ");
			for(File each : all)
				System.out.println(each.toString());
			return path.listFiles(new CustomFNF(".zci"));
		}
		
		private String getName(File f)
		{
			//add code to look in the txt file f and extract the value of the parameter [Name=...]
			if(f == null)
			{
				System.out.println("I've got no where else to look!");
				return null;
			}
			try
			{
				FileReader fileReader = new FileReader(f);
				BufferedReader reader = new BufferedReader(fileReader);
				String input = reader.readLine();
				int beginIndex = input.indexOf("[Name=") + 6;
				int endIndex = input.indexOf(']');
				return input.substring(beginIndex, endIndex);
			}
			catch(FileNotFoundException fnfe)
			{
				System.out.println("no file for you");
				return null;
			}
			catch(IOException ioe)
			{
				System.out.println("now where were we?");
				return null;
			}
		}
		
		public void windowClosing(WindowEvent w)
		{
			report("Window closed by user; disposing frame content; frame nullified");
			parent.edited(after);
			frame.dispose();
			frame = null;
		}
		
		//this function fails when the window is manually closed
		public boolean isOpen()
		{
			return frame != null;	
		}
		
		private BufferedImage drawObject()
		{
			BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
			Graphics g = image.getGraphics();
			g.setColor(Color.black);
			g.fillRect(0, 0, 50, 50);
			int type;
			type = after.getType();
			int orientation = after.getOrientation();
			if(type == -1)
			{
				//red triangle for any Dynamic Game Object
				g.setColor(new Color(255, 0, 0));
				if(orientation == 1)
				{
					int[] polyX = {6, 24, 42};
					int[] polyZ = {42, 6, 42};
					g.fillPolygon(polyX, polyZ, 3);
				}
				if(orientation == 2)
				{
					int[] polyX = {6, 42, 6};
					int[] polyZ = {42, 24, 6};
					g.fillPolygon(polyX, polyZ, 3);
				}
				if(orientation == 3)
				{
					int[] polyX = {6, 24, 42};
					int[] polyZ = {6, 42, 6};
					g.fillPolygon(polyX, polyZ, 3);
				}
				if(orientation == 4)
				{
					int[] polyX = {42, 6, 42};
					int[] polyZ = {42, 24, 6};
					g.fillPolygon(polyX, polyZ, 3);
				}
			}
			else if(type == 1)
			{
				//green full square for a solid block Tile Object
				g.setColor(new Color(0, 255, 0));
				g.fillRect(6, 6, 36, 36);
			}
			else if(type == 2)
			{
				//green triangle for a sloped wall Tile Object
				g.setColor(new Color(0, 255, 0));
				if(orientation == 1)
				{
					int[] polyX = {6, 42, 6};
					int[] polyZ = {6, 6, 42};
					g.fillPolygon(polyX, polyZ, 3);
				}
				else if(orientation == 2)
				{
					int[] polyX = {6, 42, 42};
					int[] polyZ = {6, 6, 42};
					g.fillPolygon(polyX, polyZ, 3);
				}
				else if(orientation == 3)
				{
					int[] polyX = {42, 42, 6};
					int[] polyZ = {6, 42, 42};
					g.fillPolygon(polyX, polyZ, 3);
				}
				else if(orientation == 4)
				{
					int[] polyX = {6, 6, 42};
					int[] polyZ = {6, 42, 42};
					g.fillPolygon(polyX, polyZ, 3);
				}
			}
			else if(type == 3)
			{
				//green fading square for a sloped ramp Tile Object
				for(int step = 0; step < 36; step++)
				{
					g.setColor(new Color(0,step * (255/36),0));
					if(orientation == 1)
						g.drawLine(6, 42 - step,42, 42 - step);
					else if(orientation == 2)
						g.drawLine(6 + step, 6, 6 + step, 42);
					else if(orientation == 3)
						g.drawLine(6, 6 + step,42, 6 + step);
					else if(orientation == 4)
						g.drawLine(42 - step, 6, 42 - step, 42);
				}
			}
			else if(type == 4)
			{
				//green empty square for a spawn point Tile Object
				g.setColor(new Color(0, 255, 0));
				g.drawRect(6, 6, 36, 36);
			}
			else if(type == 5)
			{
				//green x for a special tile Tile Object
				g.setColor(new Color(0, 255, 0));
				g.drawLine(6, 6, 42, 42);
				g.drawLine(6, 42, 42, 6);
			}
			return image;
		}
		
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == rLeft)
			{
				int alpha = after.getOrientation() - 1;
				if(alpha <= 0)
					alpha = 4;
				orientL.setText("Orientation: " + alpha);
				after.setOrientation(alpha);
			}
			else if(e.getSource() == rRight)
			{
				int alpha = after.getOrientation() + 1;
				if(alpha > 4)
					alpha = 1;
				orientL.setText("Orientation: " + alpha);
				after.setOrientation(alpha);
			}
			else if(e.getSource() == typeCB)
			{
				int type = typeCB.getSelectedIndex() + 1;
				if(type == 6)
				{
					type = -1;
					after = new DynamicGameObject(nextId(), after.getX(), after.getY(), after.getZ(), after.getOrientation(), null);
					fileL.setVisible(true);
					fileCB.setVisible(true);
				}
				else
				{
					if(after.getType() > 0)
						((TileObject)after).setType(type);
					else
					{
						after = new TileObject(nextId(), type, after.getX(), after.getY(), after.getZ(), after.getOrientation());
					}
					fileL.setVisible(false);
					fileCB.setVisible(false);
				}
				report("New type: " + after.getType());
			}
			else if(e.getSource() == fileCB)
			{
				((DynamicGameObject)after).setFile(allFiles[fileCB.getSelectedIndex()]);
			}
			else if(e.getSource() == reset)
			{
				after = before;
				int index = before.getType() - 1;
				if(index < 0)
				{
					index = 5;
					fileL.setVisible(true);
					fileCB.setVisible(true);
				}
				else
				{
					fileL.setVisible(false);
					fileCB.setVisible(false);
				}
				typeCB.setSelectedIndex(index);
			}
			else if(e.getSource() == keep)
			{
				parent.edited(after);
				frame.dispose();
				frame = null;
			}
			imageL.setIcon(new ImageIcon(drawObject()));
			repaint();
		}
	}
}
