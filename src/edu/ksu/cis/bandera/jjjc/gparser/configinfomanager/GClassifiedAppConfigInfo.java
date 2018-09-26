package edu.ksu.cis.bandera.jjjc.gparser.configinfomanager;

import java.util.List;

public class GClassifiedAppConfigInfo {
	public String inputName;
	public List<String> valueList;
	public boolean isMultiple;
	public boolean isRanged;
	
	public GClassifiedAppConfigInfo(String inputName, List<String> valueList, boolean isMultiple, boolean isRanged)
	{
		this.inputName = inputName;
		this.valueList = valueList;
		this.isMultiple = isMultiple;
		this.isRanged = isRanged;
	}
}
