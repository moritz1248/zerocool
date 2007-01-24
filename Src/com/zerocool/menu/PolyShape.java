package com.zerocool.menu;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.Color;

public class PolyShape 
{
	public enum SHAPE {RECTANGLE, POLYGON, OVAL, LINE};
	public static final Color color = Color.magenta;
	private SHAPE type;
	private Polygon poly;
	private int xPos, yPos;
	private int width, height;
	private int[] vertexX, vertexY;
	
	public PolyShape(Polygon polygon)
	{
		type = SHAPE.POLYGON;
		poly = polygon;
		vertexX = poly.xpoints;
		vertexY = poly.ypoints;
		Rectangle r = poly.getBounds();
		xPos = r.x;
		yPos = r.y;
		width = r.width;
		height = r.height;
	}
	public PolyShape(Rectangle rect)
	{
		type = SHAPE.RECTANGLE;
		poly = translateRect(rect);
		vertexX = poly.xpoints;
		vertexY = poly.ypoints;
		xPos = rect.x;
		yPos = rect.y;
		width = rect.width;
		height = rect.height;
	}
	public PolyShape(int x1, int y1, int x2, int y2, SHAPE type)
	{
		if(type == SHAPE.OVAL)
		{
			this.type = SHAPE.OVAL;
			this.xPos = x1;
			this.yPos = y1;
			this.width = x2;
			this.height = y2;
			int[] vX = {x1, x1 + x2, x1, x1 - x2};
			int[] vY = {y1 - y2, y1, y1 + y2, y1};
			vertexX = vX;
			vertexY = vY;
		}
		else if(type == SHAPE.LINE)
		{
			this.type = SHAPE.LINE;
			xPos = x1;
			yPos = y1;
			width = x2 - x1;
			height = y2 - y1;
			int[] vX = {x1, x2};
			int[] vY = {y1, y2};
			vertexX = vX;
			vertexY = vY;
		}
		else if(type == SHAPE.RECTANGLE)
		{
			this.type = SHAPE.RECTANGLE;
			poly = translateRect(new Rectangle(x1, y1, x2, y2));
			vertexX = poly.xpoints;
			vertexY = poly.ypoints;
			xPos = x1;
			yPos = y1;
			width = x2;
			height = y2;
		}
		else
		{
			throw new IllegalArgumentException();
		}
	}
	
	public boolean contains(int x, int y)
	{
		switch(type)
		{
			case POLYGON:
			case RECTANGLE:
				return poly.contains(x, y);
			case OVAL:
				double xRel = x - xPos;
				double yRel = y - yPos;
				double yDerive = (height * height) * (1 - (xRel*xRel)/(width * width));
				return yRel*yRel < yDerive;
			default:
				return false;
		}
	}
	
	public boolean isCenterHit(int x, int y)
	{
		if(type == SHAPE.OVAL && Math.abs(x - xPos) < 3 && Math.abs(y - yPos) < 3)
			return true;
		else if (Math.abs(x - xPos - (width / 2)) < 3 && Math.abs(y - yPos - (height / 2)) < 3)
			return true;
		return false;
	}
	
	public int vertexHit(int x, int y)
	{
		for(int c = 0; c < vertexX.length && c < vertexY.length; c++)
			if(Math.abs(x - vertexX[c]) < 3 && Math.abs(y - vertexY[c]) < 3)
				return c;
		if(isCenterHit(x, y))
			return vertexX.length;
		return -1;
	}
	
	public boolean translate(int x, int y)
	{
		if(type == SHAPE.OVAL)
		{
			return setVertex(vertexX.length, x + xPos, y + yPos);	
		}
		else
		{
			return setVertex(vertexX.length, x + (xPos + width/2), y + (yPos + height/2));
		}
	}
	
	public boolean setVertex(int vertex, int x, int y)
	{
		if(type == SHAPE.OVAL)
		{
			switch(vertex)
			{
				case 0:
					int yDif = y - (yPos - height);
					height -= yDif;
					break;
				case 1:
					int xDif = x - (xPos + width);
					width += xDif;
					break;
				case 2:
					int yDif2 = y - (yPos + height);
					height += yDif2;
					break;
				case 3:
					int xDif2 = x - (xPos - width);
					width -= xDif2;
					break;
				case 4:
					xPos = x;
					yPos = y;
					break;
				default:
					System.out.println("WTF mate?");
					break;
			}
			boolean release = false;
			if(width < 0)
			{
				width = -1 * width;
				xPos -= width;
				release = true;
			}
			if(height < 0)
			{
				height = -1 * height;
				yPos -= height;
				release = true;
			}
			int[] vX = {xPos, xPos + width, xPos, xPos - width};
			int[] vY = {yPos - height, yPos, yPos + height, yPos};
			vertexX = vX;
			vertexY = vY;
			return release;
		}
		else if(type == SHAPE.LINE)
		{
			System.out.println(vertex);
			switch(vertex)
			{
				case 0:
					width += xPos - x;
					height += yPos - y;
					xPos = x;
					yPos = y;
					int[] vX = {xPos, xPos + width};
					int[] vY = {yPos, yPos + height};
					vertexX = vX;
					vertexY = vY;
					break;
				case 1:
					width = x - xPos;
					height = y - yPos;
					vertexX[1] = xPos + width;
					vertexY[1] = yPos + height;
					break;
				case 2:
					xPos += x - (xPos + (width/2));
					yPos += y - (yPos + (height/2));
					int[] vX2 = {xPos, xPos + width};
					int[] vY2 = {yPos, yPos + height};
					vertexX = vX2;
					vertexY = vY2;
					break;
			}
			return false;
		}
		else
		{
			if(vertex == vertexX.length)
			{
				int xDif = x - (xPos + (width / 2));
				int yDif = y - (yPos + (height / 2));
				for(int c = 0; c < vertexX.length; c++)
				{
					vertexX[c] += xDif;
					vertexY[c] += yDif;
				}
				poly = new Polygon(vertexX, vertexY, vertexX.length);
				Rectangle rect = poly.getBounds();
				xPos = rect.x;
				yPos = rect.y;
				width = rect.width;
				height = rect.height;
			}
			else
			{
				vertexX[vertex] = x;
				vertexY[vertex] = y;
				poly = new Polygon(vertexX, vertexY, vertexX.length);
				Rectangle rect = poly.getBounds();
				xPos = rect.x;
				yPos = rect.y;
				width = rect.width;
				height = rect.height;
			}
			return false;
		}
	}
	
	public Rectangle getBounds()
	{
		switch(type)
		{
			case POLYGON:
			case LINE:
				return poly.getBounds();
			case RECTANGLE:
				return new Rectangle(xPos, yPos, width, height);
			case OVAL:
				return new Rectangle(xPos - width, yPos - height, width * 2, height * 2);
			default:
				return null;
		}
	}
	
	public void draw(Graphics2D g, boolean drawVertices)
	{
		switch(type)
		{
			case POLYGON:
			case RECTANGLE:
				g.draw(poly);
				break;
			case LINE:
				g.drawLine(xPos, yPos, xPos + width, yPos + height);
				break;
			case OVAL:
				g.drawOval(xPos - width, yPos - height, width * 2, height * 2);
				break;
			default:
				System.out.println("Error in determining shape type.");
				break;
		}
		if(drawVertices)
			drawVertices(g);
	}
	
	public void fill(Graphics2D g, boolean drawVertices)
	{
		switch(type)
		{
			case POLYGON:
			case RECTANGLE:
				g.fill(poly);
				break;
			case LINE:
				g.drawLine(xPos, yPos, xPos + width, yPos + height);
				break;
			case OVAL:
				g.fillOval(xPos - width, yPos - height, width * 2, height * 2);
				break;
			default:
				System.out.println("Error in determining shape type.");
				break;
		}
		if(drawVertices)
			drawVertices(g);
	}
	
	private void drawVertices(Graphics2D g)
	{
		g.setColor(color);
		for(int c = 0; c < vertexX.length && c < vertexY.length; c++)
			g.drawRect(vertexX[c] - 3, vertexY[c] - 3, 6, 6);
		//draw center node
		if(type == SHAPE.POLYGON || type == SHAPE.RECTANGLE)
		{
			Rectangle r = poly.getBounds();
			g.drawRect(r.x + (r.width / 2) - 3, r.y + (r.height / 2) - 3, 6, 6);
		}
		else if(type == SHAPE.LINE)
		{
			g.drawRect(xPos + (width / 2) - 3, yPos + (height / 2) - 3, 6, 6);
		}
		else if(type == SHAPE.OVAL)
		{
			g.drawRect(xPos - 3, yPos - 3, 6, 6);
		}
	}
	
	//static function to translate a Rectangle into a Polygon
	public static Polygon translateRect(Rectangle r)
	{
		int[] x = {r.x, r.x + r.width, r.x + r.width, r.x};
		int[] y = {r.y, r.y, r.y + r.height, r.y + r.height};
		return new Polygon(x, y, 4);
	}
	
	public int getX()
	{
		return xPos;
	}
	public int getY()
	{
		return yPos;
	}
	public int getWidth()
	{
		return width;
	}
	public int getHeight()
	{
		return height;
	}
	public int getCenterX()
	{
		if(type == SHAPE.OVAL)
			return xPos;
		return xPos + (width / 2);
	}
	public int getCenterY()
	{
		if(type == SHAPE.OVAL)
			return yPos;
		return yPos + (height / 2);
	}
	public int[] getXVertices()
	{
		return vertexX;
	}
	public int[] getYVertices()
	{
		return vertexY;
	}
	
	
}

