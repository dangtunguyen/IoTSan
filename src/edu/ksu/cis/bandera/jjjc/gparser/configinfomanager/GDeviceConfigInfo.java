package edu.ksu.cis.bandera.jjjc.gparser.configinfomanager;

import java.util.List;

public class GDeviceConfigInfo {
	public String inputName;
	public boolean isMultiple;
	public List<String> configDevices;
	public String deviceType;
	
	public GDeviceConfigInfo(String inputName, boolean isMultiple, List<String> configDevices, String deviceType)
	{
		this.inputName = inputName;
		this.isMultiple = isMultiple;
		this.configDevices = configDevices;
		this.deviceType = deviceType;
	}
}
