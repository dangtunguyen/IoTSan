package configextractor;

import java.util.ArrayList;

public class SettingInfo {
	public String varName;
	public String type;
	public ArrayList<String> configValues;
	
	public SettingInfo(String name, String type, ArrayList<String> values)
	{
		this.varName = name;
		this.type = type;
		this.configValues = new ArrayList<String>();
		this.configValues.addAll(values);
	}
}
