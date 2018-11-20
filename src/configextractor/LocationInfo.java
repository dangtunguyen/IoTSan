package configextractor;

import java.util.ArrayList;

public class LocationInfo {
	public String name;
	public ArrayList<AppInfo> appList;
	
	public LocationInfo(String name, ArrayList<AppInfo> appList)
	{
		this.name = name;
		this.appList = new ArrayList<AppInfo>();
		this.appList.addAll(appList);
	}
}
