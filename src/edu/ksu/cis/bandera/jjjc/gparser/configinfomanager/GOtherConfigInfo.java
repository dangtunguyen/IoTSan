package edu.ksu.cis.bandera.jjjc.gparser.configinfomanager;

import java.util.List;

public class GOtherConfigInfo {
	public String inputName; /* variable name: GoodNight_sendPushMessage */
	public List<String> values; /* Yes -> {"1502"} */
	public boolean isMultiple; /* false */
	
	public GOtherConfigInfo(String inputName, List<String> values, boolean isMultiple)
	{
		this.inputName = inputName;
		this.values = values;
		this.isMultiple = isMultiple;
	}
}
