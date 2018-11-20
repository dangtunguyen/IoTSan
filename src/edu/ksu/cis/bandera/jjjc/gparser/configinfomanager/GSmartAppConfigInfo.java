package edu.ksu.cis.bandera.jjjc.gparser.configinfomanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor.GStateMapEnum;

public class GSmartAppConfigInfo {
	public List<GProcessedSubscriptionInfo> subscriptionInfoList;
	public List<GDeviceConfigInfo> deviceConfigInfoList;
	public List<GOtherConfigInfo> otherConfigInfoList;
	public Map<String, String> evtVarNameMap;
	public GPhoneConfigInfo configPhoneInfo;
	public boolean isInstalledMethNeeded;
	public GStateMapEnum stateMapType;
	public Map<String, List<String>> httpGet2ValueRangeMap;
	public List<String> additionalIntHttpGetVarRangeList;
	
	public GSmartAppConfigInfo(List<GProcessedSubscriptionInfo> subscriptionInfoList, List<GDeviceConfigInfo> deviceConfigInfoList,
			List<GOtherConfigInfo> otherConfigInfoList, Map<String, String> evtVarNameMap, GPhoneConfigInfo configPhoneInfo)
	{
		this.subscriptionInfoList = subscriptionInfoList;
		this.deviceConfigInfoList = deviceConfigInfoList;
		this.otherConfigInfoList = otherConfigInfoList;
		this.evtVarNameMap = evtVarNameMap;
		this.configPhoneInfo = configPhoneInfo;
		this.isInstalledMethNeeded = false;
		this.stateMapType = GStateMapEnum.unknown;
		this.httpGet2ValueRangeMap = new HashMap<String, List<String>>();
		this.additionalIntHttpGetVarRangeList = new ArrayList<String>();
	}
}
