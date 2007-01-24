package com.zerocool.menu;

public class ZCsubButton extends ZCbutton 
{
	private ZCcomponent parent;
	
	public ZCsubButton(ZCcomponent parent, PolyShape form,String nam,boolean selected,Value propC,boolean inc, ZCvisual img, ZCvisual select, ZCvisual high)
	{
		super(form, nam, selected, propC, inc, img, select, high);
		this.parent = parent;
	}
	
	public boolean mousify(int a, int b, int type)
	{
		boolean isMousified = shape != null && shape.contains(a, b);
		if(isMousified && type == 0 && state.getNum() < SELECTED)
		{
			if(toIncrement)
			{
				parent.notify(this, true);
				state.setNum(HIGHLIGHTED);
			}
			else
			{
				parent.notify(this, true);
				state.setNum(SELECTED);
			}
		}
		else if(isMousified && type == 0 && state.getNum() == SELECTED)
		{
			state.setNum(HIGHLIGHTED);
			if(!toIncrement)
			{
				parent.notify(this, false);
			}
		}
		else if(isMousified && type == 1 && state.getNum() == NOTHING)
			state.setNum(HIGHLIGHTED);
		else if(!isMousified && type == 1 && state.getNum() == HIGHLIGHTED)
			state.setNum(NOTHING);
		else if(!isMousified && type == 0 && state.getNum() > NOTHING && toIncrement)
			state.setNum(NOTHING);
		return isMousified;
	}
	
	public void setParent(ZCcomponent zcc)
	{
		parent = zcc;
	}
}
