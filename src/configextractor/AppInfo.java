package configextractor;

import java.util.ArrayList;

public class AppInfo {
	public String appName;
	public ArrayList<SettingInfo> settingInfoList;
	
	public AppInfo(String name, ArrayList<SettingInfo> settings)
	{
		this.appName = name;
		this.settingInfoList = new ArrayList<SettingInfo>();
		this.settingInfoList.addAll(settings);
	}
}
