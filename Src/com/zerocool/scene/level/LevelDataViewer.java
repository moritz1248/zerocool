package com.zerocool.scene.level;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import scene.GameObject;


@SuppressWarnings("serial")
public class LevelDataViewer extends JPanel{
	
	class Entry{
		String name;
		public String toString(){
			return name;
		}
		public Entry(String name){
			this.name = name;
		}
	}
	static Vector<String> rowData = new Vector<String>();
	static Vector<String> columnNamesVector = new Vector<String>();

	Object[] columnNames = new Object[]{"1", "2", "3", "4", "5", "6","7"};
	
	JTextField filename = new JTextField(20);
	JButton levelDown = new JButton("Down");
	JButton levelUp = new JButton("Up");
	DefaultTableModel model = new DefaultTableModel();
    JTable output;
	JButton load = new JButton("Load");
	Level level;
	int height = 0;
	public static void main(String ... args){
		LevelDataViewer app = new LevelDataViewer();
	}
	public LevelDataViewer(){
		output = new JTable(model);
		level = new Level();
		JScrollPane scrollPane = initTable();
		JFrame frame = new JFrame();
		frame.getContentPane().add(this);
		setLayout(new BorderLayout());
		JPanel topBar = new JPanel(new FlowLayout());
		topBar.add(load);
		load.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				loadLevel();
			}
		});
		topBar.add(filename);
		topBar.add(levelDown);
		topBar.add(levelUp);
		add(topBar, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	void loadLevel(){
		String file = filename.getText();
		level.load(file);
		//getVector();
		updateTable();
		output.updateUI();
	}
	
	void updateTable(){
		//getVector();
		model.setDataVector(getVector(), columnNames);
		output.updateUI();
		output.repaint();
		System.out.println("Reading in row");
	}
	
	@SuppressWarnings("unchecked")
	Object[][] getVector(){
		ArrayList <ArrayList<TileObject>>listLevel = level.getArrayList();
		ArrayList <TileObject>list = listLevel.get(height);
		Object[][] data = new Object[list.size()][7];
		for(int i = 0; i < list.size(); i++){
			System.out.println("[" + (Object)list.get(i).getX() + ", " + (Object)list.get(i).getY() + ", " + (Object)list.get(i).getZ() + "]");
			data[i] = new Object[]{(Object)list.get(i).getX(), (Object)list.get(i).getY(), (Object)list.get(i).getZ(), "3", "4", "5", "6"};
		}
		return data;
	}
	
	JScrollPane initTable(){
		columnNamesVector.add("1");
		columnNamesVector.add("2");
		columnNamesVector.add("3");
		columnNamesVector.add("4");
		columnNamesVector.add("5");
		Object[][] data = {
			{new Integer(5), new Integer(5),
				new Integer(5), new Integer(5), new Integer(5)}
		};
		
		output = new JTable(data, new String[]{"1","2","3","4","5"});
		JScrollPane scrollPane = new JScrollPane(output);
		output.setPreferredScrollableViewportSize(new Dimension(450, 350));
		return scrollPane;
	}
}
