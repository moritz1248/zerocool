//Code by Jonathan Sanford, Revention Software(tm)...this code is the intellectual property of Jonathan Sanford and should only be used by the members of Project Zero Cool for the design of said project
package com.zerocool.editor;

import com.zerocool.scene.DynamicGameObject;
import com.zerocool.scene.GameObject;
import com.zerocool.scene.level.TileObject;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.Scanner;

//this edits the levels which consists of tiles...therein the name tileleveleditor.  cause its a level of tiles, a tilelevel...ohhhh
public class TLEditor extends JPanel implements ActionListener, WindowListener, ListSelectionListener
{
	//the menuBar actually is added to the frame not the panel, so even though the panel implements the menuBar, it doesn't get to keep it.
	private JMenuBar menuBar;
	private JMenu fileMenu, layerMenu, tileMenu, groupMenu, toolMenu, helpMenu;
	//MI stands for menu item...or maybe my intelligence
	private JMenuItem newMI, openMI, saveMI, saveAsMI, exportMI, importMI, exitMI, layerUpMI, layerDownMI, addLayerMI, insertLayerMI, clearLayerMI, deleteLayerMI, syncronizeMI, addTileMI, editTileMI, deleteTileMI, addBorderMI, addWallMI, addBlockMI, helpMI, aboutMI, addToGroupPUMI, editPUMI, deletePUMI;
	//attempting to add a popup menu
	private JPopupMenu popMenu;
	//these are the two views of the current level:
	//layerView is only of the current layer
	//compositeView is of the whole level, a composite of all the layers
	private TLEviewer layerView, compositeView;
	//the controlPanel holds all the controls. the navPanel just holds the up and down buttons
	private JPanel controlPanel, navPanel, layerButt, groupButtPanel, groupPanel;
	//addTile adds a tile, adjustView adjusts the view, layer up moves up a layer, layer down does the tango and addLayer owns a deep fat fryer
	private JButton addTile, layerUp, layerDown, addLayer, insertLayer;
	private JButton addGroup, editGroup, deleteGroup;
	//this controls whether the two views are synchronized
	private JRadioButton syncronize, useGroupFormat;
	//layerLabel just displays the number of the current layer
	private JLabel layerLabel, xPosnLabel, zPosnLabel, groupTexture;
	private JList groupList;
	//the number of the current layer
	private int layer;
	//the current selected Objects in this layer
	private ArrayList<GameObject> selected;
	//the size of the selection
	private Dimension selectionBox;
	//this is the current level
	private ArrayList<ArrayList<GameObject>> level;
	//these are the user defined groups
	private ArrayList<ObjectGroup> groups;
	//this var determines whether everything will be printed or not
	private boolean reportOn;
	//this determines the next id to be used
	private static int nextID;
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
	//remember the relative path of this file
	private String levelFile;
	//later add support so that it knows whether you have edited something since the last save
	//private boolean edited = false;
	//to differentiate between selecting and moving
	private boolean isSelecting;
	//for selection
	private int cX, cZ;
	
	//ALL HAIL THE EDITOR!!!
	public TLEditor()
	{
		reportOn = true;
		System.out.println(reportOn);
		
		objEdit = new ObjectEditor();
		
		isDragging = false;
		
		isSelecting = false;
		
		xStart = zStart = Integer.MAX_VALUE;
		
		cX = cZ = Integer.MAX_VALUE;
		
		nextID = 100;
		
		mouseState = 0;
		
		String file = ZCfileFilter.read("TLEditor", "FilePath");
		if(file != null)
			lastAccessed = new File(file);
		else
			lastAccessed = new File(System.getProperty("user.dir"));
		
		groups = new ArrayList<ObjectGroup>();
		groups.add(ObjectGroup.DEFAULT_OBJECT_GROUP);

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
		insertLayer = new JButton("Insert Layer");
		insertLayer.addActionListener(this);
		syncronize = new JRadioButton("Syncronize Views");
		syncronize.addActionListener(this);
		useGroupFormat = new JRadioButton("Use Group Format");
		useGroupFormat.addActionListener(this);
		addGroup = new JButton("Add");
		addGroup.addActionListener(this);
		editGroup = new JButton("Edit");
		editGroup.addActionListener(this);
		deleteGroup = new JButton("Delete");
		deleteGroup.addActionListener(this);
		//the labels
		layerLabel = new JLabel("Layer: 0");
		xPosnLabel = new JLabel("X: - ");
		zPosnLabel = new JLabel("Z: - ");
		groupTexture = new JLabel("Default Group", generateIcon(null, Color.black), JLabel.LEFT);
		groupTexture.setVerticalTextPosition(JLabel.TOP);
		groupTexture.setHorizontalTextPosition(JLabel.CENTER);
		//the list
		groupList = new JList(groups.toArray());
		groupList.setCellRenderer(
			new ListCellRenderer()
			{
				public Component getListCellRendererComponent(JList list,Object value,int index,boolean isSelected,boolean cellHasFocus)
				{
					JLabel label;
					if(value.getClass() == ObjectGroup.class)
					{
						label = ((ObjectGroup)value).getLabel();
					}
					else
					{
						label = new JLabel(value.toString());
					}
					label.setOpaque(true);
					if(isSelected)
					{
						label.setBackground(Color.orange);
					}
					else
					{
						label.setBackground(Color.white);
					}
					return label;
				}
			});
		groupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		groupList.addListSelectionListener(this);
		//groupList.setPreferredSize(new Dimension(180,-1));
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
		controlPanel.add(insertLayer);
		controlPanel.add(syncronize);
		controlPanel.add(useGroupFormat);
		controlPanel.add(xPosnLabel);
		controlPanel.add(zPosnLabel);
		//the group button panel
		groupButtPanel = new JPanel();
		groupButtPanel.setLayout(new BoxLayout(groupButtPanel, BoxLayout.X_AXIS));
		groupButtPanel.add(addGroup);
		groupButtPanel.add(editGroup);
		groupButtPanel.add(deleteGroup);
		//the groupPanel
		groupPanel = new JPanel();
		groupPanel.setPreferredSize(new Dimension(200, 500));
		groupPanel.add(groupTexture);
		JScrollPane jsp = new JScrollPane(groupList);
		jsp.setPreferredSize(new Dimension(180, 200));
		groupPanel.add(jsp);
		groupPanel.add(groupButtPanel);
		//mix it all up and what do you get...?
		add(layerView);
		add(controlPanel);
		add(compositeView);
		add(groupPanel);
		
		//popup menu???
		popMenu = new JPopupMenu();
		addToGroupPUMI = new JMenuItem("Add to Selected Group");
		addToGroupPUMI.addActionListener(this);
		editPUMI = new JMenuItem("Edit");
		editPUMI.addActionListener(this);
		deletePUMI = new JMenuItem("Delete");
		deletePUMI.addActionListener(this);
		popMenu.add(addToGroupPUMI);
		popMenu.add(editPUMI);
		popMenu.add(deletePUMI);
		
		//all that follows is menu crap
		menuBar = new JMenuBar();
		
		fileMenu = new JMenu("File");
		layerMenu = new JMenu("Layer");
		tileMenu = new JMenu("Tile");
		groupMenu = new JMenu("Group");
		toolMenu = new JMenu("Tools");
		helpMenu = new JMenu("Help");
		
		newMI = new JMenuItem("New");
		newMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		newMI.addActionListener(this);
		openMI = new JMenuItem("Open");
		openMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		openMI.addActionListener(this);
		saveMI = new JMenuItem("Save");
		saveMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		saveMI.addActionListener(this);
		saveAsMI = new JMenuItem("Save As...");
		saveAsMI.addActionListener(this);
		importMI = new JMenuItem("Import");
		importMI.addActionListener(this);
		exportMI = new JMenuItem("Export");
		exportMI.addActionListener(this);
		exitMI = new JMenuItem("Exit");
		exitMI.addActionListener(this);
		layerUpMI = new JMenuItem("Up");
		layerUpMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, ActionEvent.CTRL_MASK));
		layerUpMI.addActionListener(this);
		layerDownMI = new JMenuItem("Down");
		layerDownMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, ActionEvent.CTRL_MASK));
		layerDownMI.addActionListener(this);
		addLayerMI = new JMenuItem("Add");
		addLayerMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		addLayerMI.addActionListener(this);
		insertLayerMI = new JMenuItem("Insert");
		insertLayerMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
		insertLayerMI.addActionListener(this);
		clearLayerMI = new JMenuItem("Clear");
		clearLayerMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		clearLayerMI.addActionListener(this);
		deleteLayerMI = new JMenuItem("Delete");
		deleteLayerMI.addActionListener(this);;
		syncronizeMI = new JMenuItem("Syncronize Views");
		syncronizeMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, ActionEvent.CTRL_MASK));
		syncronizeMI.addActionListener(this);
		addTileMI = new JMenuItem("Add");
		addTileMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		addTileMI.addActionListener(this);
		editTileMI = new JMenuItem("Edit");
		editTileMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		editTileMI.addActionListener(this);
		deleteTileMI = new JMenuItem("Delete");
		deleteTileMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		deleteTileMI.addActionListener(this);
		addBorderMI = new JMenuItem("Add Border");
		addBorderMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
		addBorderMI.addActionListener(this);
		addWallMI = new JMenuItem("Add Wall");
		addWallMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
		addWallMI.addActionListener(this);
		addBlockMI = new JMenuItem("Add Block");
		addBlockMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.CTRL_MASK));
		addBlockMI.addActionListener(this);
		helpMI = new JMenuItem("Help");
		helpMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		helpMI.addActionListener(this);
		aboutMI = new JMenuItem("About");
		aboutMI.addActionListener(this);
		
		fileMenu.add(newMI);
		fileMenu.addSeparator();
		fileMenu.add(openMI);
		fileMenu.add(saveMI);
		fileMenu.add(saveAsMI);
		fileMenu.addSeparator();
		fileMenu.add(importMI);
		fileMenu.add(exportMI);
		fileMenu.addSeparator();
		fileMenu.add(exitMI);
		layerMenu.add(layerUpMI);
		layerMenu.add(layerDownMI);
		layerMenu.addSeparator();
		layerMenu.add(addLayerMI);
		layerMenu.add(insertLayerMI);
		layerMenu.add(clearLayerMI);
		layerMenu.add(deleteLayerMI);
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
		//menuBar.add(groupMenu);
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
		else if(source == saveAsMI)
		{
			report("User choose to save the current level");
			saveAs();
		}
		else if(source == saveMI)
		{
			report("User choose to save the current level");
			save();
		}
		else if(source == importMI)
		{
			report("User choose to import a raw level");
			importLevel();
		}
		else if(source == exportMI)
		{
			report("User choose to export to raw rext");
			exportLevel();
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
			addLayer(false);
		}
		else if(source == insertLayer || source == insertLayerMI)
		{
			report("User choose to insert a layer");
			addLayer(true);
		}
		else if(source == clearLayerMI)
		{
			report("User choose to clear this layer");
			clearLayer();
		}
		else if(source == deleteLayerMI)
		{
			report("User choose to delete this layer");
			deleteLayer();
		}
		else if(source == syncronize || source == syncronizeMI)
		{
			if(source == syncronizeMI)
				syncronize.setSelected(!syncronize.isSelected());
			if(syncronize.isSelected())
				report("User choose to enable syncronized view");
			else
				report("User choose to enable free view");
			compositeView.syncronize();
		}
		else if(source == addTile || source == addTileMI)
		{
			report("User choose to add a tile");
			addTile();
		}
		else if(source == addToGroupPUMI)
		{
			report("User choose to add the selected tiles to the selected group");
			addToGroup((ObjectGroup)groupList.getSelectedValue());
			selectGroup((ObjectGroup)groupList.getSelectedValue());
		}
		else if(source == editTileMI || source == editPUMI)
		{
			report("User choose to edit the selected tile");
			editSelection();
		}
		else if(source == deleteTileMI || source == deletePUMI)
		{
			report("User choose to delete the selected tile");
			deleteTile();
		}
		else if(source == addGroup)
		{
			report("User choose to add a group");
			addGroup();
		}
		else if(source == editGroup)
		{
			report("User choose to edit the selected group");
			editGroup();
		}
		else if(source == deleteGroup)
		{
			report("User choose to delete the selected group");
			deleteGroup();
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
	public void valueChanged(ListSelectionEvent e)
	{
		report("User selected a different group");
		if(!groupList.isSelectionEmpty())
		{
			ObjectGroup group = groups.get(groupList.getSelectedIndex());
			groupTexture.setText(group.getName());
			groupTexture.setIcon(generateIcon(group.getTextureFile(), group.getColor()));
			selectGroup(group);
		}
	}
	public void editSelection()
	{
		if(!objEdit.isOpen() && selected != null)
		{
			ObjectGroup og = new ObjectGroup("", selected);
			og.setUpdate(true);
			objEdit = new ObjectEditor(og, this, false);
		}
	}
	public void addGroup()
	{
		String groupName = JOptionPane.showInputDialog(this, "What would you like to name this group?", "New Group", JOptionPane.PLAIN_MESSAGE);
		if(groupName != null && groupName != "")
		{
			ObjectGroup og = new ObjectGroup(groupName);
			groups.add(og);
			groupList.setListData(groups.toArray());
		}
	}
	public void editGroup()
	{
		//opens a editor box
		if(!objEdit.isOpen() && groupList.getSelectedIndex() > 0)
		{
			ObjectGroup og =(ObjectGroup)groupList.getSelectedValue();
			selectGroup(og);
			report(og.toString());
			objEdit = new ObjectEditor(og, this, true);
		}
	}
	public void deleteGroup()
	{
		if(groupList.getSelectedIndex() > 0)
		{
			ObjectGroup og = groups.remove(groupList.getSelectedIndex());
			for(GameObject go : og.getObjects())
			{
				go.setGroup(null);
			}
			groupList.setListData(groups.toArray());
			groupList.setSelectedValue(groups.get(0), true);
		}
	}
	public void addToGroup(ObjectGroup group)
	{
		if(group != null)
		{
			for(GameObject go : selected)
			{
				group.addObject(go);
				go.setGroup(group);
			}
		}
	}
	public void selectGroup(ObjectGroup group)
	{
		if(group != null)
		{
			selected = new ArrayList<GameObject>();
			for(GameObject go : group.getObjects())
			{
				if(go.getY() == layer)
				{
					selected.add(go);
				}
			}
		}
		repaint();
	}
	public void clearLayer()
	{
		level.set(layer, new ArrayList<GameObject>());
		selected = null;
		repaint();
	}
	public void deleteLayer()
	{
		if(level.size() > 1)
		{
			for(GameObject go : level.get(layer))
			{
				go.setGroup(null);
			}
			level.remove(layer);
			layer--;
			layerView.setLayer(layer);
			layerLabel.setText("Layer: " + layer);
			for(int c = layer + 1; c < level.size(); c++)
			{
				for(GameObject go : level.get(c))
				{
					go.setY(go.getY() - 1);
				}
			}
			selected = null;
			repaint();
		}
	}
	public void renew()
	{
		level = new ArrayList<ArrayList<GameObject>>();
		level.add(new ArrayList<GameObject>());
		layer = 0;
		layerView.setLayer(layer);
		layerLabel.setText("Layer: " + layer);
		int choice = JOptionPane.showConfirmDialog(this, "Do you want to border the level area?", "Watch for illegal immigrants", JOptionPane.YES_NO_OPTION);
		if(choice == JOptionPane.YES_OPTION)
		{
			setLayer();
		}
		groups = new ArrayList<ObjectGroup>();
		groups.add(ObjectGroup.DEFAULT_OBJECT_GROUP);
		groupList.setListData(groups.toArray());
		levelFile = null;
		repaint();
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
	public static int nextId()
	{
		return nextID++;
	}
	private ImageIcon generateIcon(String imageFile, Color c)
	{
		ImageIcon ii;
		try
		{
			//
			File f = new File(ZCfileFilter.read("TLEditor","TexturePath") + '/' + imageFile);
			report(f.getPath());
			BufferedImage bi = ImageIO.read(f);
			Image img;
			if(bi.getWidth() / 300.0 > bi.getHeight() / 200.0)
			{
				img = bi.getScaledInstance(160, -1, Image.SCALE_FAST);
			}
			else
			{
				img = bi.getScaledInstance(-1, 160, Image.SCALE_FAST);
			}
			bi = new BufferedImage(180, 180, bi.getType());
			Graphics g = bi.getGraphics();
			g.setColor(c);
			g.fillRect(0,0,180,180);
			g.drawImage(img, 10, 10, null);
			ii = new ImageIcon(bi);
		}
		catch(Exception error)
		{
			System.out.println("Error reading file " + imageFile);
			BufferedImage image = new BufferedImage(180, 180, BufferedImage.TYPE_INT_RGB);
			Graphics g = image.getGraphics();
			g.setColor(c);
			g.fillRect(0,0,180,180);
			g.setColor(new Color(255 - c.getRed(), 255 - c.getBlue(), 255 - c.getGreen()));
			g.drawString("Could Not Display", 40, 96);
			ii = new ImageIcon(image);
		}
		return ii;
	}
	private void importLevel()
	{
		int choice = JOptionPane.showConfirmDialog(this, "Do you want to save the current level first?", "Save?", JOptionPane.YES_NO_OPTION);
		if(choice == JOptionPane.YES_OPTION)
			save();
		JFileChooser chooser = new JFileChooser(lastAccessed);
		chooser.setFileFilter(new ZCfileFilter(".txt"));
		choice = chooser.showOpenDialog(this);
	    if(choice == JFileChooser.APPROVE_OPTION && chooser.getSelectedFile() != null)
	    {
	    	Scanner mainScanner;
			try
			{
				mainScanner = new Scanner(chooser.getSelectedFile());
		    }
			catch(FileNotFoundException e)
		    {
				JOptionPane.showMessageDialog(this, "Import failed", "Import failed", JOptionPane.ERROR_MESSAGE);
				return;
		    }
			//the version line
			mainScanner.nextLine();
			//the "Layers: " token
			mainScanner.next();
			int layers = mainScanner.nextInt();
			level = new ArrayList<ArrayList<GameObject>>();
			for(int c = 0; c < layers; c++)
			{
				level.add(new ArrayList<GameObject>());
			}
			while(mainScanner.hasNextLine())
			{
				GameObject go = null;
				try
				{
					Scanner input = new Scanner(mainScanner.nextLine() + "\t").useDelimiter("\t");
					String objType = input.next();
					int id = input.nextInt();
					if(objType.equals("TileObject"))
					{
						go = new TileObject(id);
						go.toObject(input);
					}
					else if(objType.equals("DynamicGameObject"))
					{
						go = new DynamicGameObject(id, null);
						go.toObject(input);
					}
					if(go != null)
					{
						int goLayer = (new Float(go.getY())).intValue();
						level.get(goLayer).add(go);
					}
				}
				catch(Exception e)
				{
					System.out.println("Exception thrown during import " + e + " trying to create " + go);
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
	}
	public void exportLevel()
	{
		if(levelFile == null)
		{
			int choice = JOptionPane.showConfirmDialog(this, "Please save this file first...", "Save?", JOptionPane.OK_CANCEL_OPTION);
			if(!(choice == JOptionPane.OK_OPTION && saveAs()))
			{
				JOptionPane.showMessageDialog(this, "Export failed, please save first", "Export failed", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		else
		{
			save();
		}
		String fileName = levelFile.substring(0, levelFile.length() - 4) + ".txt";
		try
		{
			System.out.println(fileName);
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
			writer.write("Version: 1.0" + '\n');
			writer.write("Layers: " + level.size() + '\n');
			for(ArrayList<GameObject> layer : level)
				for(GameObject go : layer)
				{
					writer.write(go.toText('\t') + '\n');
					System.out.println(go.toText('\t'));
				}
			writer.close();
		}
		catch(Exception e)
		{
			System.out.println("Exception thrown in trying to write products to " + "file " + fileName);
			JOptionPane.showMessageDialog(this, "Export failed, an exception was thrown", "Export failed", JOptionPane.ERROR_MESSAGE);
			return;
		}
	}
	public void open()
	{
		int choice = JOptionPane.showConfirmDialog(this, "Do you want to save the current level first?", "Save?", JOptionPane.YES_NO_OPTION);
		if(choice == JOptionPane.YES_OPTION)
			save();
		JFileChooser chooser = new JFileChooser(lastAccessed);
		chooser.setFileFilter(new ZCfileFilter(".zcl"));
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
				ArrayList<ArrayList<GameObject>> newLevel = (ArrayList<ArrayList<GameObject>>)inStream.readObject();
				if(newLevel != null)
				{
					level = newLevel;
					levelFile = file.getPath();
					report("level open complete");
					groups = null;
					file = new File(file.getPath().substring(0, file.getPath().length() - 4) + ".zcg");
					fileIS = new FileInputStream(file);
					inStream = new ObjectInputStream(fileIS);
					ArrayList<ObjectGroup> newGroups = (ArrayList<ObjectGroup>)inStream.readObject();
					if(newGroups != null)
					{
						groups = newGroups;
						for(ObjectGroup og : groups)
						{
							for(GameObject go : og.getObjects())
							{
								GameObject obj = objectAt((int)go.getX(), (int)go.getY(), (int)go.getZ());
								go.setGroup(null);
								go = null;
								og.addObject(obj);
								obj.setGroup(og);
							}
						}
						groupList.setListData(groups.toArray());
						report("groups open complete");
					}
				}
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
	public boolean save()
	{
		if(levelFile == null)
		{
			return saveAs();
		}
		File file = new File(levelFile);
		String path = file.getPath();
		if(!path.endsWith(".zcl"))
		{
			path += ".zcl";
			file = new File(path);
		}
		report("Attempting to save as " + file);
		lastAccessed = file;
		//this code saves the file
		try
		{
			FileOutputStream fileOS = new FileOutputStream(file);
			ObjectOutputStream outStream = new ObjectOutputStream (fileOS);
			outStream.writeObject(level);
			report("level save complete");
			file = new File(file.getPath().substring(0, file.getPath().length() - 4) + ".zcg");
			fileOS = new FileOutputStream(file);
			outStream = new ObjectOutputStream(fileOS);
			outStream.writeObject(groups);
			report("group save complete");
		}
		catch(FileNotFoundException e)
		{
			report("FileNotFoundException thrown while trying to save level as " + file + ";  Exception caught");
			return saveAs();
		}
		catch(IOException e)
		{
			report("IOException thrown while trying to save level as " + file + ";  Exception caught");
			report(e.toString());
			return saveAs();
		}
		return true;
	}
	
	public boolean saveAs()
	{
		JFileChooser chooser = new JFileChooser(lastAccessed);
		chooser.setFileFilter(new ZCfileFilter(".zcl"));
		int choice = chooser.showSaveDialog(this);
	    if(choice == JFileChooser.APPROVE_OPTION && chooser.getSelectedFile() != null)
	    {
			File file = chooser.getSelectedFile();
			String path = file.getPath();
			if(!path.endsWith(".zcl"))
			{
				path += ".zcl";
				file = new File(path);
			}
			report("Attempting to save as " + file);
			lastAccessed = file;
			//this code saves the file
			try
			{
				FileOutputStream fileOS = new FileOutputStream(file);
				ObjectOutputStream outStream = new ObjectOutputStream (fileOS);
				outStream.writeObject(level);
				levelFile = file.getPath();
				report("level save complete");
				file = new File(file.getPath().substring(0, file.getPath().length() - 4) + ".zcg");
				fileOS = new FileOutputStream(file);
				outStream = new ObjectOutputStream(fileOS);
				outStream.writeObject(groups);
				report("group save complete"); 
			}
			catch(FileNotFoundException e)
			{
				report("FileNotFoundException thrown while trying to save level as " + file + ";  Exception caught");
				return false;
			}
			catch(IOException e)
			{
				report("IOException thrown while trying to save level as " + file + ";  Exception caught");
				report(e.toString());
				return false;
			}
			return true;
		}
		return false;
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
	public void addLayer(boolean insert)
	{
		if(insert)
		{
			level.add(layer, new ArrayList<GameObject>());
			selected = null;
			for(int c = layer + 1; c < level.size(); c++)
			{
				for(GameObject go : level.get(c))
				{
					go.setY(go.getY() + 1);
				}
			}
			//layer = level.size() - 1;
			//layerView.setLayer(layer);
			//layerLabel.setText("Layer: " + layer);
			report("Layer added. Current layer: " + layer);
			repaint();
		}
		else
		{
			level.add(new ArrayList<GameObject>());
			selected = null;
			layer = level.size() - 1;
			layerView.setLayer(layer);
			layerLabel.setText("Layer: " + layer);
			report("Layer added. Current layer: " + layer);
			repaint();
		}
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
		{
			mouseState = 2;
			selected = null;
			isSelecting = false;
			if(xStart != Integer.MAX_VALUE && selectionBox != null)
			{
				if(selectionBox.width > selectionBox.height)
				{
					layerView.draw(cX, zStart, selectionBox.width, 1, true);
				}
				else
				{
					layerView.draw(xStart, cZ, 1, selectionBox.height, true);
				}
			}
		}
	}
	public void addBlock()
	{
		//sets the mouseState to allow the user to add a block of tiles
		if(mouseState == 0 && !objEdit.isOpen())
		{
			mouseState = 3;
			selected = null;
			isSelecting = false;
			if(xStart != Integer.MAX_VALUE && selectionBox != null)
			{
				layerView.draw(cX, cZ, selectionBox.width, selectionBox.height, true);
			}
		}
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
		ArrayList<GameObject> lyr = level.get(layer);
		selected = new ArrayList<GameObject>();
		for(int a = 0; a < dx; a++)
			for(int b = 0; b < dz; b++)
				if(objectAt(x + a,z - b) == null)
				{
					if(groupList.getSelectedIndex() < 1 || !useGroupFormat.isSelected())
					{
						TileObject to = new TileObject(nextId(),1,x+a,layer,z-b,1);
						lyr.add(to);
						selected.add(to);
					}
					else
					{
						ObjectGroup og = (ObjectGroup)groupList.getSelectedValue();
						if(og.getType() == -1)
						{
							DynamicGameObject dgo = new DynamicGameObject(nextId(),x+a,layer,z-b,1,og.getItemFile());
							dgo.setTextureID(og.getTextureFile());
							dgo.setGroup(og);
							og.addObject(dgo);
							lyr.add(dgo);
							selected.add(dgo);
						}
						else
						{
							TileObject to = new TileObject(nextId(),og.getType(),x+a,layer,z-b,1);
							to.setTextureID(og.getTextureFile());
							to.setGroup(og);
							og.addObject(to);
							lyr.add(to);
							selected.add(to);
						}
					}
				}
		repaint();
	}
	public void editTile(int x, int z)
	{
		//opens a editor box
		if(!objEdit.isOpen() && selected != null)
		{
			objEdit = new ObjectEditor(objectAt(x,z), this);
		}
	}
	public void deleteTile()
	{
		if(selected != null)
		{
			ArrayList<GameObject> thisLayer = level.get(layer);
			int num = thisLayer.size();
			for(GameObject go : selected)
			{
				if(thisLayer.remove(go) && thisLayer.size() + 1 == num)
				{
					go.setGroup(null);
					report("Tile deleted successfully");
				}
				else
				{
					report("Error in tile deletion");
				}
				num = thisLayer.size();
			}
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
		String helpPath = ZCfileFilter.read("TLEditor", "HelpPath");
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
		frame.setSize(new Dimension(200, 110));
		frame.setResizable(false);
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
	
	public boolean canDragObject(int x, int z, int button, boolean mask)
	{
		if(!objEdit.isOpen())
		{
			if(mouseState == 2 || mouseState == 3 || isSelecting)
			{
				if(xStart == Integer.MAX_VALUE || (selected != null && !mask))
				{
					xStart = x;
					zStart = z;
					if(!mask)
					{
						selected = null;
					}
				}
				else
				{
					int dx = Math.abs(x - xStart) + 1;
					int dz = Math.abs(z - zStart) + 1;
					if(x < xStart)
						cX = x;
					else
						cX = xStart;
					if(z > zStart)
						cZ = z;
					else
						cZ = zStart;
					selectionBox = new Dimension(dx, dz);
				}
				return true;
			}
			else if(isDragging)
			{
				//first check to see if the spot is open
				boolean flag = true;
				for(GameObject go : selected)
				{
					GameObject other = objectAt((int)go.getX() - xStart + x, (int)go.getZ() - zStart + z);
					if(other != null && !selected.contains(other))
					{
						flag = false;
						break;
					}
				}
				if(flag)
				{
					for(GameObject go : selected)
					{
						go.setX(go.getX() - xStart + x);
						go.setZ(go.getZ() - zStart + z);
					}
					xStart = x;
					zStart = z;
				}
				return true;
			}
			else if(objectAt(x,z) != null)
			{
				if(selected == null || !selected.contains(objectAt(x,z)))
				{
					selected = new ArrayList<GameObject>();
					selected.add(objectAt(x,z));
					selectionBox = new Dimension(1,1);
				}
				isDragging = true;
				xStart = x;
				zStart = z;
				return true;
			}
			else
				return false;
		}
		else
			return true;
	}
	public void mouseReleased(int x, int z, boolean mask)
	{
		isDragging = false;
		if((mouseState == 2 || mouseState == 3) && xStart != Integer.MAX_VALUE)
		{
			createTiles(x, z, mouseState);
			xStart = Integer.MAX_VALUE;
			zStart = Integer.MAX_VALUE;
			mouseState = 0;
			isSelecting = false;
		}
		if(isSelecting && xStart != Integer.MAX_VALUE)
		{
			if(!mask || selected == null)
			{
				selected = new ArrayList<GameObject>();
			}
			for(int i = 0; i < selectionBox.width; i++)
				for(int j = 0; j < selectionBox.height; j++)
				{
					GameObject go = objectAt(cX + i, cZ - j);
					if(go != null)
					{
						if(!selected.contains(go))
						{
							selected.add(go);
						}
						else
						{
							selected.remove(go);
						}
					}
				}
			if(selected.size() == 0)
			{
				selected = null;
			}
			isSelecting = false;
		}
		xStart = zStart = Integer.MAX_VALUE;
		cX = cZ = -1;
	}
	public void mouseClicked(int x, int z, int button)
	{
		if(objEdit.isOpen())
			mouseState = -1;
		GameObject go = objectAt(x,z);
		if(mouseState == 1 && go == null)
		{
			//add tile
			GameObject tile;
			if(groupList.getSelectedIndex() < 1 || !useGroupFormat.isSelected())
			{
				tile = new TileObject(nextId(),1,x,layer,z,1);
			}
			else
			{
				ObjectGroup og = (ObjectGroup)groupList.getSelectedValue();
				if(og.getType() == -1)
				{
					tile = new DynamicGameObject(nextId(),x,layer,z,1,og.getItemFile());
				}
				else
				{
					tile = new TileObject(nextId(),og.getType(),x,layer,z,1);
				}
				tile.setTextureID(og.getTextureFile());
				tile.setGroup(og);
				og.addObject(tile);
			}
			level.get(layer).add(tile);
			selected = new ArrayList<GameObject>();
			selected.add(tile);
			xStart = Integer.MAX_VALUE;
			zStart = Integer.MAX_VALUE;
			selectionBox = null;
			if(!useGroupFormat.isSelected())
			{
				editTile(x, z);
			}
		}
		else if(mouseState == 0 && go != null)
		{
			if(selected == null || !selected.contains(go))
			{
				selected = new ArrayList<GameObject>();
				selected.add(go);
				xStart = x;
				zStart = z;
				selectionBox = new Dimension(1,1);
			}
			else if(button == 1 && selected != null)
			{
				int orient = go.getOrientation() + 1;
				if(orient > 4)
					orient = 1;
				go.setOrientation(orient);
			}
		}
		else
		{
			selected = null;
			xStart = Integer.MAX_VALUE;
			zStart = Integer.MAX_VALUE;
			selectionBox = null;
		}
		repaint();
		mouseState = 0;
	}
	public void updateMouse(int x, int z, boolean ctrlMask)
	{
		xPosnLabel.setText("X = " + x + " ");
		zPosnLabel.setText("Z = " + z + " ");
		if((mouseState == 2 || mouseState == 3) && (selected != null) && !ctrlMask)
		{
			xStart = Integer.MAX_VALUE;
			zStart = Integer.MAX_VALUE;
			selected = null;
		}
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
	private GameObject objectAt(int x, int y, int z)
	{
		ArrayList<GameObject> lyr = level.get(y);
		for(GameObject go : lyr)
			if((int)go.getX() == x && (int)go.getZ() == z)
				return go;
		return null;
	}
	public void edited(ObjectGroup oldG, ObjectGroup newG, boolean newGroup)
	{
		ArrayList<GameObject> oldObjects = oldG.getObjects();
		ArrayList<GameObject> newObjects = newG.getObjects();
		ArrayList<GameObject> lyr = level.get(layer);
		for(int c = 0; c < oldObjects.size(); c++)
		{
			GameObject old = oldObjects.get(c);
			if(lyr.contains(old))
			{
				lyr.set(lyr.indexOf(old), newObjects.get(c));
			}
		}
		if(newGroup)
		{
			for(GameObject go : newObjects)
			{
				go.setGroup(newG);
			}
			groups.add(newG);
			groupList.setListData(groups.toArray());
			groupList.setSelectedValue(newG, true);
			selectGroup(newG);
		}
		else
		{
			oldG.matchTo(newG);
			selectGroup(oldG);
		}
		if(oldG == groupList.getSelectedValue())
		{
			groupTexture.setText(oldG.getName());
			groupTexture.setIcon(generateIcon(oldG.getTextureFile(), oldG.getColor()));
		}
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
			if(save())
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
			xStandard = zStandard = Integer.MAX_VALUE;
			xA = zA = dX = dZ = Integer.MAX_VALUE;
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
		public void moveCorner(int dx, int dz)
		{
			xCorner += dx;
			zCorner += dz;
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
			boolean mask = (m.getModifiers() / 2) % 2 == 1;
			if(!parent.canDragObject(x + xCorner, z + zCorner, m.getButton(), mask))
			{
				if(xStandard == Integer.MAX_VALUE)
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
;			parent.updateMouse(x + xCorner, z + zCorner, mask);
			parent.repaint();
		}
		public void mouseClicked(MouseEvent m)
		{
			int x = (m.getX() / 25) + xCorner;
			int z = ((500 - m.getY()) / 25) + zCorner;
			if(m.getButton() == 3 && selected != null)
			{
				popMenu.show(this, m.getX(), m.getY());
			}
			parent.mouseClicked(x, z, m.getButton());
		}
		public void mouseReleased(MouseEvent m)
		{
			xStandard = zStandard = Integer.MAX_VALUE;
			draw(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, true);
			int x = (m.getX() / 25) + xCorner;
			int z = ((500 - m.getY()) / 25) + zCorner;
			boolean mask = (m.getModifiers() / 2) % 2 == 1;
			parent.mouseReleased(x, z, mask);
		}
		public void mouseMoved(MouseEvent m)
		{
			int x = (m.getX() / 25) + xCorner;
			int z = ((500 - m.getY()) / 25) + zCorner;
			boolean mask = (m.getModifiers() / 2) % 2 == 1;
			parent.updateMouse(x, z, mask);
		}
		public void mousePressed(MouseEvent m)
		{
			int x = (m.getX() / 25) + xCorner;
			int z = ((500 - m.getY()) / 25) + zCorner;
			if(mouseState == 0 && (selected == null || objectAt(x,z) == null) && (m.getButton() == MouseEvent.BUTTON2 || m.getButton() == MouseEvent.BUTTON3))
			{
				isSelecting = true;
				xStart = x;
				zStart = z;
				cX = x;
				cZ = z;
				selectionBox = new Dimension(1,1);
			}
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
				for(GameObject go : selected)
				{
					if(go != null)
					{
						g.fillRect(((int)go.getX() - xCorner) * 25, 500 - (((int)go.getZ() - zCorner + 1) * 25), 25, 25);
					}
				}
			}
			
			//highlighted squares (for multi-select)
			if(isSelecting && selectionBox != null)
			{
				g.setColor(Color.gray);
				g.fillRect((cX - xCorner) * 25, 500 - ((cZ - zCorner + 1) * 25), selectionBox.width * 25, selectionBox.height * 25);
			}
			//highlighted squares (for add functions)
			else if(zA != Integer.MAX_VALUE)
			{
				g.setColor(Color.yellow);
				System.out.println(xA + "-" + zA + "::" + dX + "-" + dZ);
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
				if(isSingle)
				{
					g.setColor(tile.getColor());
				}
				else
				{
					g.setColor(new Color(shade, shade, shade));
				}
				if(type == -1)
				{
					//triangle for any Dynamic Game Object
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
					//full square for a solid block Tile Object
					g.fillRect(x + 3, z + 3, 18, 18);
				}
				else if(type == 2)
				{
					//triangle for a sloped wall Tile Object
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
					//fading square for a sloped ramp Tile Object
					for(int step = 0; step < 18; step++)
					{
						Color color = tile.getColor();
						if(isSingle)
							g.setColor(new Color((206 * color.getRed() * step / (18 * 255)) + 50,(206 * color.getGreen() * step / (18 * 255)) + 50, (206 * color.getBlue() * step / (18 * 255)) + 50));
						else
						{
							int colorSlice = (interval*layerNum + (interval*step/18)) + 50;
							g.setColor(new Color(colorSlice, colorSlice, colorSlice));
						}
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
					//empty square for a spawn point Tile Object
					g.drawRect(x + 3, z + 3, 18, 18);
				}
				else if(type == 5)
				{
					//x for a special tile Tile Object
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
			parent.mousePressed(m);
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
		private ObjectGroup group;
		private GameObject before, after;
		private TLEditor parent;
		private JFrame frame;
		private JPanel panel, top, options, groupName, sample, gOptions, left, right, bottom;
		private JLabel typeL, fileL, textureL, groupL, colorL;
		private JButton reset, keep, pickColor, newGroup, saveGroup;
		private JRadioButton updater;
		private JTextField name;
		private JComboBox typeCB, fileCB, textureCB;
		private File filePath, texturePath;
		private File[] allFiles;
		private File[] allTextures;
		private boolean isExistingGroup;
		private Color color;
		
		//this is just to create a dummy class
		private ObjectEditor()
		{
			frame = null;
		}
		
		private ObjectEditor(ObjectGroup group, TLEditor tle, boolean existant)
		{
			this.group = group;
			if(group.getType() > 0)
			{
				before = new TileObject(nextId());
				((TileObject)before).setType(group.getType());
			}
			else
			{
				before = new DynamicGameObject(nextId(), null);
			}
			before.setTextureID(group.getTextureFile());
			after = before.getClone(nextId());
			color = group.getColor();
			
			parent = tle;
			
			isExistingGroup = existant;
			
			createFrame();
		}
		
		private ObjectEditor(GameObject go, TLEditor tle)
		{
			//functionality allows the user to reset the object to the before state
			if(go == null)
			{
				throw new IllegalArgumentException();
			}
			
			before = go;
			after = go.getClone(nextId());
			
			group = new ObjectGroup("", go);
			group.setUpdate(true);
			
			color = group.getColor();
			
			parent = tle;
			
			isExistingGroup = false;
			
			createFrame();
		}
		
		private void createFrame()
		{	
			//by changing this you can change where the program looks to find the dynamic game object item types
			String itemPath = ZCfileFilter.read("TLEditor", "ItemPath");
			if(itemPath != null)
				filePath = new File(itemPath);
			else
				filePath = new File("");
			
			String txtrPath = ZCfileFilter.read("TLEditor", "TexturePath");
			if(txtrPath != null)
				texturePath = new File(txtrPath);
			else
				texturePath = new File("");
			
			//frame and panels
			frame = new JFrame("GameObject Editor");
			frame.addWindowListener(this);
			top = new JPanel();
			panel = new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			groupName = new JPanel();
			groupName.setLayout(new BoxLayout(groupName, BoxLayout.X_AXIS));
			sample = new JPanel();
			sample.setLayout(new BoxLayout(sample, BoxLayout.X_AXIS));
			gOptions = new JPanel();
			gOptions.setLayout(new BoxLayout(gOptions, BoxLayout.X_AXIS));
			left = new JPanel();
			left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
			options = new JPanel();
			options.setLayout(new BoxLayout(options, BoxLayout.X_AXIS));
			right = new JPanel();
			right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
			bottom = new JPanel();
			
			//labels
			typeL = new JLabel("Type: ");
			fileL = new JLabel("Item Type: ");
			groupL = new JLabel("Group Name: ");
			colorL = new JLabel(color.getRed()+ " " + color.getGreen() + " " + color.getBlue() + " ", getSwab(), JLabel.LEFT);
			
			//buttons
			reset = new JButton("Reset");
			reset.addActionListener(this);
			keep = new JButton("Keep");
			keep.addActionListener(this);
			pickColor = new JButton("Pick Color");
			pickColor.addActionListener(this);
			newGroup = new JButton("New Group");
			newGroup.addActionListener(this);
			saveGroup = new JButton("Save Group");
			saveGroup.addActionListener(this);
			if(!isExistingGroup)
			{
				saveGroup.setEnabled(false);
			}
			
			//radio button
			updater = new JRadioButton("Use to update");
			updater.setSelected(group.isUpdater());
			
			//text field
			name = new JTextField(group.getName());
			
			//combo boxes
			String[] allTypes = {"solid block", "sloped wall", "sloped ramp", "spawn point node", "special tile", "dynamic object"};
			typeCB = new JComboBox(allTypes);
			int index = before.getType() - 1;
			if(index < 0)
				index = 5;
			typeCB.setSelectedIndex(index);
			typeCB.addActionListener(this);
			
			allFiles = filePath.listFiles(new CustomFNF(".zci"));
			if(allFiles != null)
			{
				String[] allNames;
				allNames = new String[allFiles.length];
				for(int c = 0; c < allFiles.length; c++)
					allNames[c] = getName(allFiles[c]);
				fileCB = new JComboBox(allNames);
				if(before.getType() == -1)
				{
					File f = ((DynamicGameObject)before).getFile();
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
				fileCB = new JComboBox();
			}
			if(before.getType() != -1)
			{
				fileCB.setVisible(false);
				fileL.setVisible(false);
			}
			
			allTextures = texturePath.listFiles();
			if(allTextures != null)
			{
				int selection = 0;
				String[] allNames = new String[allTextures.length];
				for(int c = 0; c < allTextures.length; c++)
				{
					allNames[c] = allTextures[c].getName();
					if(allNames[c].equals(before.getTextureID()))
					{
						selection = c;
					}
				}
				if(allNames.length == 0)
				{
					allNames = new String[]{""};
				}
				textureCB = new JComboBox(allNames);
				textureCB.setSelectedIndex(selection);
			}
			else
			{
				textureCB = new JComboBox(new String[]{""});
			}
			textureCB.addActionListener(this);
			textureL = new JLabel();
			textureL.setVerticalTextPosition(JLabel.TOP);
			textureL.setHorizontalTextPosition(JLabel.CENTER);
			formatTextureView();
			
			options.add(reset);
			options.add(keep);
			
			groupName.add(groupL);
			groupName.add(name);
			
			sample.add(colorL);
			sample.add(pickColor);
			
			gOptions.add(newGroup);
			gOptions.add(saveGroup);
			
			left.add(groupName);
			left.add(sample);
			left.add(updater);
			left.add(gOptions);
			
			right.add(typeL);
			right.add(typeCB);
			right.add(fileL);
			right.add(fileCB);
			right.add(options);
			
			bottom.add(textureL);
			bottom.add(textureCB);
			
			top.add(left);
			top.add(right);
			
			panel.add(top);
			panel.add(bottom);
			
			frame.getContentPane().add(panel);
			frame.pack();
			frame.setResizable(false);
			frame.setVisible(true);
		}
		
		private ImageIcon getSwab()
		{	
			BufferedImage bi = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
			Graphics g = bi.getGraphics();
			g.setColor(color);
			g.fillRect(0,0,10,10);
			return new ImageIcon(bi);
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
			//parent.edited(before, after);
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
				//green triangle for any Dynamic Game Object
				g.setColor(new Color(0, 255, 0));
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
		
		private void formatTextureView()
		{
			try
			{
				BufferedImage bi = ImageIO.read(allTextures[textureCB.getSelectedIndex()]);
				Image img;
				if(bi.getWidth() / 300.0 > bi.getHeight() / 200.0)
				{
					img = bi.getScaledInstance(300, -1, Image.SCALE_SMOOTH);
				}
				else
				{
					img = bi.getScaledInstance(-1, 200, Image.SCALE_SMOOTH);
				}
				textureL.setIcon(new ImageIcon(img));
				textureL.setText((String)textureCB.getSelectedItem());
				after.setTextureID((String)textureCB.getSelectedItem());
			}
			catch(Exception error)
			{
				textureL.setText("Could Not Display");
				textureL.setIcon(new ImageIcon(new BufferedImage(300, 200, BufferedImage.TYPE_BYTE_BINARY)));
			}
		}
		
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == typeCB)
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
				frame.pack();
				report("New type: " + after.getType());
			}
			else if(e.getSource() == fileCB)
			{
				((DynamicGameObject)after).setFile(allFiles[fileCB.getSelectedIndex()]);
			}
			else if(e.getSource() == textureCB)
			{
				formatTextureView();
				frame.pack();
			}
			else if(e.getSource() == reset)
			{
				after = before.getClone(nextId());
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
				int selection = 0;
				for(int c = 0; c < allTextures.length; c++)
				{
					if(allTextures[c].getName().equals(before.getTextureID()))
					{
						selection = c;
						break;
					}
				}
				textureCB.setSelectedIndex(selection);
				color = group.getColor();
				colorL.setIcon(getSwab());
				colorL.setText(color.getRed() + " " + color.getGreen() + " " + color.getBlue() + " ");
				name.setText(group.getName());
				updater.setSelected(group.isUpdater());
				formatTextureView();
			}
			else if(e.getSource() == keep || e.getSource() == saveGroup)
			{
				ObjectGroup newG = group.getClone();
				newG.updateProfile(after);
				newG.setName(name.getText());
				newG.setColor(color);
				newG.setUpdate(updater.isSelected());
				newG.updateAll();
				parent.edited(group, newG, false);
				frame.dispose();
				frame = null;
			}
			else if(e.getSource() == pickColor)
			{
				color = JColorChooser.showDialog(frame, "Pick a color", color);
				colorL.setText(color.getRed()+ " " + color.getGreen() + " " + color.getBlue() + " ");
				colorL.setIcon(getSwab());
			}
			else if(e.getSource() == newGroup)
			{
				ObjectGroup newG = group.getClone();
				newG.updateProfile(after);
				newG.setName(name.getText());
				newG.setColor(color);
				newG.setUpdate(updater.isSelected());
				newG.updateAll();
				parent.edited(group, newG, true);
				frame.dispose();
				frame = null;
			}
			repaint();
		}
	}
}
