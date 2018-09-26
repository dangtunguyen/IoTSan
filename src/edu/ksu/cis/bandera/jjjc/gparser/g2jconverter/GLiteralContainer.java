package edu.ksu.cis.bandera.jjjc.gparser.g2jconverter;

import java.util.ArrayList;
import java.util.List;

import edu.ksu.cis.bandera.jjjc.node.PExp;

public class GLiteralContainer {
	public List<Integer> staticValList;
	public List<PExp> dynamicValList;
	public List<Integer> digitList;
	
	public GLiteralContainer()
	{
		this.staticValList = new ArrayList<Integer>();
		this.dynamicValList = new ArrayList<PExp>();
		this.digitList = new ArrayList<Integer>();
	}
	
	public void addAll(GLiteralContainer aLiteralContainer)
	{
		if(this.staticValList.size() > 0)
		{
			this.digitList.clear();
		}
		else
		{
			this.digitList = aLiteralContainer.digitList;
		}
		
		for(Integer it : aLiteralContainer.staticValList)
		{
			if(!this.staticValList.contains(it))
			{
				this.staticValList.add(it);
			}
		}
		
		this.dynamicValList.addAll(aLiteralContainer.dynamicValList);
	}
	
	public boolean isEmpty()
	{
		if((this.staticValList.size() > 0) || (this.dynamicValList.size() > 0) || (this.digitList.size() > 0))
		{
			return false;
		}
		
		return true;
	}
}
