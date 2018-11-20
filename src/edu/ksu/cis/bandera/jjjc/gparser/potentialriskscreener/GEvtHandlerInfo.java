package edu.ksu.cis.bandera.jjjc.gparser.potentialriskscreener;

import java.util.List;

public class GEvtHandlerInfo {
	public String smartAppName;
	public String evtHandler;
	public List<GEventInfo> inputEvtList;
	public List<GEventInfo> outputEvtList;
	public boolean isAppStateChanged;
	
	public GEvtHandlerInfo(String smartAppName, String evtHandler, 
			List<GEventInfo> inputEvtList, List<GEventInfo> outputEvtList, boolean isAppStateChanged)
	{
		this.smartAppName = smartAppName;
		this.evtHandler = evtHandler;
		this.inputEvtList = inputEvtList;
		this.outputEvtList = outputEvtList;
		this.isAppStateChanged = isAppStateChanged;
	}
}
