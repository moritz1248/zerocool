package com.zerocool.level;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import com.zerocool.scene.GameObject;

public class LevelCompiler {
	protected ArrayList<ArrayList<GameObject>> mLevelData;
	protected int[][][] mLevelArray;
	
	ArrayList <Vertex>mVertexList = new ArrayList<Vertex>();
	ArrayList <Polygon>mPolygonList = new ArrayList<Polygon>();
	ArrayList <Texture>mTextureList = new ArrayList<Texture>();
	ArrayList <Vertex>mNormalList = new ArrayList<Vertex>();
	
	protected final int mScale = 20;
	protected final int mHalfScale = mScale / 2;
	protected double mMinX, mMinY, mMinZ;
	protected double mMaxX, mMaxY, mMaxZ;
	protected int mWidthX, mHeightY, mLengthZ;
	protected int mOffsetX, mOffsetY, mOffsetZ;
	protected boolean mBoundsSet = false;
	public void load(String filename) {

		if (filename != null) {
			if (mLevelData == null) {
				mLevelData = new ArrayList<ArrayList<GameObject>>();
			}
			File file = new File(filename);
			// this code opens the file
			try {
				FileInputStream fileIS = new FileInputStream(file);
				ObjectInputStream inStream = new ObjectInputStream(fileIS);
				ArrayList<ArrayList<GameObject>> newLevel = (ArrayList) inStream
						.readObject();
				if (newLevel != null)
					mLevelData = newLevel;
			} catch (IOException e) {
				System.out
						.println("IOException thrown while trying to open level "
								+ file + ";  Exception caught");
				System.out.println(e.toString());
			} catch (ClassNotFoundException e) {
				System.out
						.println("ClassNotFoundException thrown while trying to open level "
								+ file + ";  Exception caught");
			}

			for (ArrayList<GameObject> layer : mLevelData)
				for (GameObject go : layer) {
					if(!mBoundsSet)
					{
						mMinX = go.getX();
						mMaxX = go.getX();
						mMinY = go.getY();
						mMaxY = go.getY();
						mMinZ = go.getZ();
						mMaxZ = go.getZ();
						mBoundsSet = true;
					}else{
						if(mMinX > go.getX())
							mMinX = go.getX();
						else if(mMaxX < go.getX())
							mMaxX = go.getX();
						if(mMinY > go.getY())
							mMinY = go.getY();
						else if(mMaxY < go.getY())
							mMaxY = go.getY();
						if(mMinZ > go.getZ())
							mMinZ = go.getZ();
						else if(mMaxZ < go.getZ())
							mMaxZ = go.getZ();
					}
						
				}


			mOffsetX = (int)mMinX;
			mOffsetY = (int)mMinY;
			mOffsetZ = (int)mMinZ;
			mWidthX = (int)(mMaxX - mMinX + 1);
			mHeightY = (int)(mMaxY - mMinY + 1);
			mLengthZ = (int)(mMaxZ - mMinZ + 1);
			
			System.out.println("Min x: " + mMinX);
			System.out.println("Min y: " + mMinY);
			System.out.println("Min z: " + mMinZ);
			System.out.println("Max x: " + mMaxX);
			System.out.println("Max y: " + mMaxY);
			System.out.println("Max z: " + mMaxZ);

			System.out.println("width x: " + mWidthX);
			System.out.println("height y: " + mHeightY);
			System.out.println("length z: " + mLengthZ);
			System.out.println("-------------------");	
			mLevelArray = new int[mWidthX][mHeightY][mLengthZ];
			for(int i = 0; i < mWidthX; i++)
			{
				for(int j = 0; j < mHeightY; j++)
				{
					for(int k = 0; k < mLengthZ; k++)
					{
						mLevelArray[i][j][k] = 0;
					}
				}
			}
			for (ArrayList<GameObject> layer : mLevelData)
				for (GameObject go : layer) {
					System.out.println("adding " + go.toString());
					mLevelArray[(int)go.getX() - mOffsetX][(int)go.getY() - mOffsetY][(int)go.getZ() - mOffsetZ] = go.getType();
				}
			
			for(int j = 0; j < mHeightY; j++)
			{
				for(int i = 0; i < mWidthX; i++)
				{
					for(int k = 0; k < mLengthZ; k++)
					{
						int v1, v2, v3, v4, n1, n2, n3, n4, t1, t2, t3, t4;
						
						//try top (+1 y)
						if(mLevelArray[i][j][k] != 0)
						{
							if(j == mHeightY-1)
							{
								v1 = createVertex((i + mOffsetX)*mScale-mHalfScale, (j + mOffsetY)*mScale+mHalfScale, (k + mOffsetZ)*mScale-mHalfScale);
								v2 = createVertex((i + mOffsetX)*mScale-mHalfScale, (j + mOffsetY)*mScale+mHalfScale, (k + mOffsetZ)*mScale+mHalfScale);
								v3 = createVertex((i + mOffsetX)*mScale+mHalfScale, (j + mOffsetY)*mScale+mHalfScale, (k + mOffsetZ)*mScale+mHalfScale);
								v4 = createVertex((i + mOffsetX)*mScale+mHalfScale, (j + mOffsetY)*mScale+mHalfScale, (k + mOffsetZ)*mScale-mHalfScale);
								
								
								
								n1 = n2 = n3 = n4 = createNormal(0, 1, 0);
								
								t1 = createTexture(0, 0);
								t2 = createTexture(0, 1);
								t3 = createTexture(1, 1);
								t4 = createTexture(1, 0);
								
								createPolygon(v1, v2, v3, n1, n2, n3, t1, t2, t3);
								createPolygon(v3, v4, v1, n3, n4, n1, t3, t4, t1);
							}else{
								if(mLevelArray[i][j+1][k] == 0)
								{
									v1 = createVertex((i + mOffsetX)*mScale-mHalfScale, (j + mOffsetY)*mScale+mHalfScale, (k + mOffsetZ)*mScale-mHalfScale);
									v2 = createVertex((i + mOffsetX)*mScale-mHalfScale, (j + mOffsetY)*mScale+mHalfScale, (k + mOffsetZ)*mScale+mHalfScale);
									v3 = createVertex((i + mOffsetX)*mScale+mHalfScale, (j + mOffsetY)*mScale+mHalfScale, (k + mOffsetZ)*mScale+mHalfScale);
									v4 = createVertex((i + mOffsetX)*mScale+mHalfScale, (j + mOffsetY)*mScale+mHalfScale, (k + mOffsetZ)*mScale-mHalfScale);
									
									n1 = n2 = n3 = n4 = createNormal(0, 1, 0);
									
									t1 = createTexture(0, 0);
									t2 = createTexture(0, 1);
									t3 = createTexture(1, 1);
									t4 = createTexture(1, 0);
									
									createPolygon(v1, v2, v3, n1, n2, n3, t1, t2, t3);
									createPolygon(v3, v4, v1, n3, n4, n1, t3, t4, t1);
								}
							}
							
							//try left (-1 x)
							if(i == 0)
							{
								v1 = createVertex((i + mOffsetX)*mScale-mHalfScale, (j + mOffsetY)*mScale-mHalfScale, (k + mOffsetZ)*mScale-mHalfScale);
								v2 = createVertex((i + mOffsetX)*mScale-mHalfScale, (j + mOffsetY)*mScale-mHalfScale, (k + mOffsetZ)*mScale+mHalfScale);
								v3 = createVertex((i + mOffsetX)*mScale-mHalfScale, (j + mOffsetY)*mScale+mHalfScale, (k + mOffsetZ)*mScale+mHalfScale);
								v4 = createVertex((i + mOffsetX)*mScale-mHalfScale, (j + mOffsetY)*mScale+mHalfScale, (k + mOffsetZ)*mScale-mHalfScale);
								
								
	
								n1 = n2 = n3 = n4 = createNormal(-1, 0, 0);
								
								t1 = createTexture(0, 0);
								t2 = createTexture(0, 1);
								t3 = createTexture(1, 1);
								t4 = createTexture(1, 0);
								
								createPolygon(v1, v2, v3, n1, n2, n3, t1, t2, t3);
								createPolygon(v3, v4, v1, n3, n4, n1, t3, t4, t1);
							}else{
								if(mLevelArray[i-1][j][k] == 0)
								{
									v1 = createVertex((i + mOffsetX)*mScale-mHalfScale, (j + mOffsetY)*mScale-mHalfScale, (k + mOffsetZ)*mScale-mHalfScale);
									v2 = createVertex((i + mOffsetX)*mScale-mHalfScale, (j + mOffsetY)*mScale-mHalfScale, (k + mOffsetZ)*mScale+mHalfScale);
									v3 = createVertex((i + mOffsetX)*mScale-mHalfScale, (j + mOffsetY)*mScale+mHalfScale, (k + mOffsetZ)*mScale+mHalfScale);
									v4 = createVertex((i + mOffsetX)*mScale-mHalfScale, (j + mOffsetY)*mScale+mHalfScale, (k + mOffsetZ)*mScale-mHalfScale);
									
									
		
									n1 = n2 = n3 = n4 = createNormal(-1, 0, 0);
									
									t1 = createTexture(0, 0);
									t2 = createTexture(0, 1);
									t3 = createTexture(1, 1);
									t4 = createTexture(1, 0);
									
									createPolygon(v1, v2, v3, n1, n2, n3, t1, t2, t3);
									createPolygon(v3, v4, v1, n3, n4, n1, t3, t4, t1);
								}
							}
							
							//try right (+1 x)
							if(i == mWidthX - 1)
							{
								v1 = createVertex((i + mOffsetX)*mScale+mHalfScale, (j + mOffsetY)*mScale-mHalfScale, (k + mOffsetZ)*mScale-mHalfScale);
								v2 = createVertex((i + mOffsetX)*mScale+mHalfScale, (j + mOffsetY)*mScale+mHalfScale, (k + mOffsetZ)*mScale-mHalfScale);
								v3 = createVertex((i + mOffsetX)*mScale+mHalfScale, (j + mOffsetY)*mScale+mHalfScale, (k + mOffsetZ)*mScale+mHalfScale);
								v4 = createVertex((i + mOffsetX)*mScale+mHalfScale, (j + mOffsetY)*mScale-mHalfScale, (k + mOffsetZ)*mScale+mHalfScale);
	
								n1 = n2 = n3 = n4 = createNormal(1, 0, 0);
								
								t1 = createTexture(0, 0);
								t2 = createTexture(0, 1);
								t3 = createTexture(1, 1);
								t4 = createTexture(1, 0);
								
								createPolygon(v1, v2, v3, n1, n2, n3, t1, t2, t3);
								createPolygon(v3, v4, v1, n3, n4, n1, t3, t4, t1);
							}else{
								if(mLevelArray[i+1][j][k] == 0)
								{
									v1 = createVertex((i + mOffsetX)*mScale+mHalfScale, (j + mOffsetY)*mScale-mHalfScale, (k + mOffsetZ)*mScale-mHalfScale);
									v2 = createVertex((i + mOffsetX)*mScale+mHalfScale, (j + mOffsetY)*mScale+mHalfScale, (k + mOffsetZ)*mScale-mHalfScale);
									v3 = createVertex((i + mOffsetX)*mScale+mHalfScale, (j + mOffsetY)*mScale+mHalfScale, (k + mOffsetZ)*mScale+mHalfScale);
									v4 = createVertex((i + mOffsetX)*mScale+mHalfScale, (j + mOffsetY)*mScale-mHalfScale, (k + mOffsetZ)*mScale+mHalfScale);
	
									n1 = n2 = n3 = n4 = createNormal(1, 0, 0);
									
									t1 = createTexture(0, 0);
									t2 = createTexture(0, 1);
									t3 = createTexture(1, 1);
									t4 = createTexture(1, 0);
									
									createPolygon(v1, v2, v3, n1, n2, n3, t1, t2, t3);
									createPolygon(v3, v4, v1, n3, n4, n1, t3, t4, t1);
								}
							}
							
							//try front (-1 z)
							if(k == 0)
							{
								v1 = createVertex((i + mOffsetX)*mScale-mHalfScale, (j + mOffsetY)*mScale-mHalfScale, (k + mOffsetZ)*mScale-mHalfScale);
								v2 = createVertex((i + mOffsetX)*mScale-mHalfScale, (j + mOffsetY)*mScale+mHalfScale, (k + mOffsetZ)*mScale-mHalfScale);
								v4 = createVertex((i + mOffsetX)*mScale+mHalfScale, (j + mOffsetY)*mScale-mHalfScale, (k + mOffsetZ)*mScale-mHalfScale);
								v3 = createVertex((i + mOffsetX)*mScale+mHalfScale, (j + mOffsetY)*mScale+mHalfScale, (k + mOffsetZ)*mScale-mHalfScale);
								
	
								n1 = n2 = n3 = n4 = createNormal(0, 0, -1);
								
								t1 = createTexture(0, 0);
								t2 = createTexture(0, 1);
								t3 = createTexture(1, 1);
								t4 = createTexture(1, 0);
								
								createPolygon(v1, v2, v3, n1, n2, n3, t1, t2, t3);
								createPolygon(v3, v4, v1, n3, n4, n1, t3, t4, t1);
							}else{
								if(mLevelArray[i][j][k-1] == 0)
								{
									v1 = createVertex((i + mOffsetX)*mScale-mHalfScale, (j + mOffsetY)*mScale-mHalfScale, (k + mOffsetZ)*mScale-mHalfScale);
									v2 = createVertex((i + mOffsetX)*mScale-mHalfScale, (j + mOffsetY)*mScale+mHalfScale, (k + mOffsetZ)*mScale-mHalfScale);
									v4 = createVertex((i + mOffsetX)*mScale+mHalfScale, (j + mOffsetY)*mScale-mHalfScale, (k + mOffsetZ)*mScale-mHalfScale);
									v3 = createVertex((i + mOffsetX)*mScale+mHalfScale, (j + mOffsetY)*mScale+mHalfScale, (k + mOffsetZ)*mScale-mHalfScale);
									
		
									n1 = n2 = n3 = n4 = createNormal(0, 0, -1);
									
									t1 = createTexture(0, 0);
									t2 = createTexture(0, 1);
									t3 = createTexture(1, 1);
									t4 = createTexture(1, 0);
									
									createPolygon(v1, v2, v3, n1, n2, n3, t1, t2, t3);
									createPolygon(v3, v4, v1, n3, n4, n1, t3, t4, t1);
								}
							}
							
							//try back (+1 z)
							if(k == mLengthZ - 1)
							{
								v1 = createVertex((i + mOffsetX)*mScale-mHalfScale, (j + mOffsetY)*mScale-mHalfScale, (k + mOffsetZ)*mScale+mHalfScale);
								
								v2 = createVertex((i + mOffsetX)*mScale+mHalfScale, (j + mOffsetY)*mScale-mHalfScale, (k + mOffsetZ)*mScale+mHalfScale);
								v3 = createVertex((i + mOffsetX)*mScale+mHalfScale, (j + mOffsetY)*mScale+mHalfScale, (k + mOffsetZ)*mScale+mHalfScale);
								v4 = createVertex((i + mOffsetX)*mScale-mHalfScale, (j + mOffsetY)*mScale+mHalfScale, (k + mOffsetZ)*mScale+mHalfScale);
	
								n1 = n2 = n3 = n4 = createNormal(0, 0, 1);
								
								t1 = createTexture(0, 0);
								t2 = createTexture(0, 1);
								t3 = createTexture(1, 1);
								t4 = createTexture(1, 0);
								
								createPolygon(v1, v2, v3, n1, n2, n3, t1, t2, t3);
								createPolygon(v3, v4, v1, n3, n4, n1, t3, t4, t1);
							}else{
								if(mLevelArray[i][j][k+1] == 0)
								{
									v1 = createVertex((i + mOffsetX)*mScale-mHalfScale, (j + mOffsetY)*mScale-mHalfScale, (k + mOffsetZ)*mScale+mHalfScale);
									v2 = createVertex((i + mOffsetX)*mScale-mHalfScale, (j + mOffsetY)*mScale+mHalfScale, (k + mOffsetZ)*mScale+mHalfScale);
									v3 = createVertex((i + mOffsetX)*mScale+mHalfScale, (j + mOffsetY)*mScale+mHalfScale, (k + mOffsetZ)*mScale+mHalfScale);
									v4 = createVertex((i + mOffsetX)*mScale+mHalfScale, (j + mOffsetY)*mScale-mHalfScale, (k + mOffsetZ)*mScale+mHalfScale);
	
									n1 = n2 = n3 = n4 = createNormal(0, 0, 1);
									
									t1 = createTexture(0, 0);
									t2 = createTexture(0, 1);
									t3 = createTexture(1, 1);
									t4 = createTexture(1, 0);
									
									createPolygon(v1, v2, v3, n1, n2, n3, t1, t2, t3);
									createPolygon(v3, v4, v1, n3, n4, n1, t3, t4, t1);
								}
							}
						}
						System.out.print("[" + mLevelArray[i][j][k] + "]");
					}
					System.out.println();
				}
				System.out.println();
				System.out.println();
			}
			
		}
		generateObj("Data\\levels\\testCompile.obj");
	}
	
	private void generateObj(String filename)
	{
		File file = new File(filename);
		PrintStream writer = null;
		try {
			writer = new PrintStream(file);
			writer.println("# OBJ generated by ZeroCool Level Compiler");
			writer.println();
			writer.println("mtllib level.mtl");
			writer.println("o LEVEL");
			for(Vertex v : mVertexList)
				writer.println("v " + v.x + " " + v.y + " " + v.z);
			writer.println("# " + mVertexList.size() + " vertices");
			writer.println();
			
			for(Texture t : mTextureList)
				writer.println("vt " + t.tx + " " + t.ty);
			writer.println("# " + mTextureList.size() + " texture points");
			writer.println();
			
			for(Vertex n : mNormalList)
				writer.println("vn " + n.x + " " + n.y + " " + n.z);
			writer.println("# " + mNormalList.size() + " normals");
			writer.println();
			
			writer.println();
			writer.println("g LEVEL_default_0");
			writer.println("usemtl LEVEL_default_0");

			for(Polygon p : mPolygonList)
				writer.println("f " +p.v1 + "/" + p.t1 + "/" + p.n1 +
						" "+ p.v2 + "/" + p.t2 + "/" + p.n2 + 
						" "+ p.v3 + "/" + p.t3 + "/" + p.n3);
			writer.println("# " + mPolygonList.size() + " triangles");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			writer.close();
		}
		
	}
	
	private int createVertex(double x, double y, double z)
	{
		boolean found = false;
		int index = mVertexList.size();
		Vertex v = new Vertex(x,y,z);
		for(int i= 0; i < mVertexList.size(); i++)
		{
			if(v.equals(mVertexList.get(i)))
			{
				index = i;
				found = true;
				break;
			}
		}
		if(!found)
			mVertexList.add(v);
		return index;
	}
	
	private int createNormal(double x, double y, double z)
	{
		boolean found = false;
		int index = mNormalList.size();
		Vertex v = new Vertex(x,y,z);
		for(int i= 0; i < mNormalList.size(); i++)
		{
			if(v.equals(mNormalList.get(i)))
			{
				index = i;
				found = true;
				break;
			}
		}
		if(!found)
			mNormalList.add(v);
		return index;
	}
	
	private int createTexture(double tx, double ty)
	{
		boolean found = false;
		int index = mTextureList.size();
		Texture v = new Texture(tx,ty);
		for(int i= 0; i < mTextureList.size(); i++)
		{
			if(v.equals(mTextureList.get(i)))
			{
				index = i;
				found = true;
				break;
			}
		}
		if(!found)
			mTextureList.add(v);
		return index;
	}
	
	private void createPolygon(int v1, int v2, int v3, int n1, int n2, int n3, int t1, int t2, int t3)
	{
		mPolygonList.add(new Polygon(v1,v2,v3, n1, n2, n3, t1, t2, t3));
	}

	ArrayList getArrayList() {
		return mLevelData;
	}
	
	class Texture{
		double tx, ty;
		
		public Texture(double tx, double ty)
		{
			this.tx = tx;
			this.ty = ty;
		}
		
		public boolean equals(Object o)
		{
			if(o != null && o instanceof Texture)
			{
				Texture t = (Texture)o;
				if(t.tx == this.tx && t.ty == this.ty)
					return true;
				else
					return false;
			}else
				return false;
		}
	}
	
	class Vertex{
		double x, y, z;
		
		public Vertex(double x, double y, double z)
		{
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		public boolean equals(Object o)
		{
			if(o != null && o instanceof Vertex)
			{
				Vertex v = (Vertex)o;
				if(v.x == this.x && v.y == this.y && v.z == this.z)
					return true;
				else
					return false;
			}else
				return false;
		}
	}
	class Polygon{
		public Polygon(int v1, int v2, int v3, int n1, int n2, int n3, int t1, int t2, int t3) {
			this.v1 = v1 + 1;
			this.v2 = v2 + 1;
			this.v3 = v3 + 1;
			this.n1 = n1 + 1;
			this.n2 = n2 + 1;
			this.n3 = n3 + 1;
			this.t1 = t1 + 1;
			this.t2 = t2 + 1;
			this.t3 = t3 + 1;
		}

		int v1, v2, v3;
		int n1, n2, n3;
		int t1, t2, t3;
	}
}
