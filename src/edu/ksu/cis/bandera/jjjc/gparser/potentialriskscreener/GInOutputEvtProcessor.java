package edu.ksu.cis.bandera.jjjc.gparser.potentialriskscreener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor.GSubscriptionInfo;
import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;

public class GInOutputEvtProcessor {
	/********************************************/
	private String smartAppName;
	private List<String> evtHandlerList;
	private List<GSubscriptionInfo> subscriptionInfoList;
	private Map<String, List<String>> calleeInfoMap;
	private Map<String, List<GEventInfo>> outputEvtMap;
	private Map<String, List<GEventInfo>> inputEvtMap;
	private Map<String, Boolean> localMethAppStateChangedMap;
	
	/* <methName, list of called methNames>
	 * */
	private Map<String, List<String>> fullCalleeInfoMap;
	private List<GEvtHandlerInfo> evtHandlerInfoList;
	private Map<String, List<GEventInfo>> evtHandlerInputEvtMap;
	private Map<String, List<GEventInfo>> evtHandlerOutputEvtMap;
	private Map<String, Boolean> evtHandlerAppStateChangedMap;
	/********************************************/
	
	public static void main(String args[]) {
		Map<String, List<String>> calleeInfoMap = new HashMap<String, List<String>>();
		
		List<String> lst1 = Arrays.asList("correctMode", "correctTime", "allQuiet", "switchesOk", "takeActions");
		calleeInfoMap.put("scheduleCheck", lst1);
		
		List<String> lst2 = Arrays.asList("send");
		calleeInfoMap.put("takeActions", lst2);
		
		List<String> lst3 = Arrays.asList("correctMode", "correctTime", "allQuiet", "switchesOk", "takeActions");
		calleeInfoMap.put("switchOffHandler", lst3);
		
		List<String> lst4 = Arrays.asList("correctMode", "correctTime", "scheduleCheck");
		calleeInfoMap.put("motionInactiveHandler", lst4);
		
		GInOutputEvtProcessor checker = new GInOutputEvtProcessor(null, null, null, calleeInfoMap, null, null, null);
	}
	
	public GInOutputEvtProcessor(String smartAppName,
			List<String> evtHandlerList, 
			List<GSubscriptionInfo> subscriptionInfoList, 
			Map<String, List<String>> calleeInfo,
			Map<String, List<GEventInfo>> outputEvtMap,
			Map<String, List<GEventInfo>> inputEvtMap,
			Map<String, Boolean> localMethAppStateChangedMap)
	{
		this.smartAppName = smartAppName;
		this.evtHandlerList = evtHandlerList;
		this.subscriptionInfoList = subscriptionInfoList;
		this.calleeInfoMap = calleeInfo;
		this.outputEvtMap = outputEvtMap;
		this.inputEvtMap = inputEvtMap;
		this.localMethAppStateChangedMap = localMethAppStateChangedMap;
		
		fullCalleeInfoMap = new HashMap<String, List<String>>();
		evtHandlerInfoList = new ArrayList<GEvtHandlerInfo>();
		evtHandlerInputEvtMap = new HashMap<String, List<GEventInfo>>();
		evtHandlerOutputEvtMap = new HashMap<String, List<GEventInfo>>();
		evtHandlerAppStateChangedMap = new HashMap<String, Boolean>();
	}
	
	private List<String> getFullCalleeInfoList(String caller)
	{
		if(fullCalleeInfoMap.containsKey(caller))
		{
			return fullCalleeInfoMap.get(caller);
		}
		else
		{
			List<String> calleeInfoList = new ArrayList<String>();
			
			if(this.calleeInfoMap.containsKey(caller))
			{
				for(String callee : this.calleeInfoMap.get(caller))
				{
					if(!callee.equals(caller))
					{
						if(!calleeInfoList.contains(callee))
						{
							calleeInfoList.add(callee);
						}
						for(String subCalleed : getFullCalleeInfoList(callee))
						{
							if(!calleeInfoList.contains(subCalleed))
							{
								calleeInfoList.add(subCalleed);
							}
						}
					}
				}
				fullCalleeInfoMap.put(caller, calleeInfoList);
			}
			return calleeInfoList;
		}
	}
	
	private void buildFullCalleeInfoMap()
	{
		if(fullCalleeInfoMap.size() == 0)
		{
			for(String caller : this.calleeInfoMap.keySet())
			{
				List<String> fullCalleeInfoList = this.getFullCalleeInfoList(caller);
				fullCalleeInfoMap.put(caller, fullCalleeInfoList);
			}
		}
	}
	
	private void addEvtInfo(List<GEventInfo> evtInfoList, GEventInfo evtInfo)
	{
		boolean present = false;
		
		for(GEventInfo curEvt : evtInfoList)
		{
			if(curEvt.attribute.equals(evtInfo.attribute) && curEvt.evtType.equals(evtInfo.evtType))
			{
				present = true;
				break;
			}
		}
		if(!present)
		{
			evtInfoList.add(evtInfo);
		}
	}
	
	private void buildEvtHandlerInputEvtMap()
	{
		if(this.evtHandlerInputEvtMap.size() == 0)
		{
			/* Get input events from subscription info */
			for(GSubscriptionInfo subInfo : this.subscriptionInfoList)
			{
				String evtHandler = GUtil.getStandardEvtHandlerName(subInfo.evtHandler);
				String attribute = subInfo.subscribedAttribute;
				
				if(subInfo.inputName.equals("location"))
				{
					attribute = "location";
				}
				
				if(this.evtHandlerInputEvtMap.containsKey(evtHandler))
				{
					this.addEvtInfo(this.evtHandlerInputEvtMap.get(evtHandler),
							new GEventInfo(attribute, subInfo.subscribedEvtType));
				}
				else
				{
					List<GEventInfo> evtInfoList = new ArrayList<GEventInfo>();
					
					evtInfoList.add(new GEventInfo(attribute, subInfo.subscribedEvtType));
					this.evtHandlerInputEvtMap.put(evtHandler, evtInfoList);
				}
			}
			
			/* Get input events from app's activities */
			this.buildFullCalleeInfoMap();
			
			for(String evt : this.evtHandlerList)
			{
				String evtHandler = GUtil.getStandardEvtHandlerName(evt);
				
				if(this.fullCalleeInfoMap.containsKey(evt))
				{
					List<GEventInfo> evtInfoList;
					
					if(this.inputEvtMap.containsKey(evt))
					{
						evtInfoList = this.inputEvtMap.get(evt);
					}
					else
					{
						evtInfoList = new ArrayList<GEventInfo>();
					}
					
					/* Add input events of each called methods to evtInfoList */
					for(String callee : this.fullCalleeInfoMap.get(evt))
					{
						if(this.inputEvtMap.containsKey(callee))
						{
							for(GEventInfo evtInfo : this.inputEvtMap.get(callee))
							{
								this.addEvtInfo(evtInfoList, evtInfo);
							}
						}
					}
					if(this.evtHandlerInputEvtMap.containsKey(evtHandler))
					{
						List<GEventInfo> curEvtInfoList = this.evtHandlerInputEvtMap.get(evtHandler);
						
						for(GEventInfo evtInfo : evtInfoList)
						{
							this.addEvtInfo(curEvtInfoList, evtInfo);
						}
					}
					else
					{
						this.evtHandlerInputEvtMap.put(evtHandler, evtInfoList);
					}
				}
			}
		}
	}
	
	private void buildEvtHandlerOutputEvtMap()
	{
		if(this.evtHandlerOutputEvtMap.size() == 0)
		{
			this.buildFullCalleeInfoMap();
			
			for(String evt : this.evtHandlerList)
			{
				String evtHandler = GUtil.getStandardEvtHandlerName(evt);
				
				if(this.fullCalleeInfoMap.containsKey(evt))
				{
					List<GEventInfo> evtInfoList;
					
					if(this.outputEvtMap.containsKey(evt))
					{
						evtInfoList = this.outputEvtMap.get(evt);
					}
					else
					{
						evtInfoList = new ArrayList<GEventInfo>();
					}
					
					/* Add output events of each called methods to evtInfoList */
					for(String callee : this.fullCalleeInfoMap.get(evt))
					{
						if(this.outputEvtMap.containsKey(callee))
						{
							for(GEventInfo evtInfo : this.outputEvtMap.get(callee))
							{
								this.addEvtInfo(evtInfoList, evtInfo);
							}
						}
					}
					this.evtHandlerOutputEvtMap.put(evtHandler, evtInfoList);
				}
				else
				{
					/* This evt handler does not have any output event */
					this.evtHandlerOutputEvtMap.put(evtHandler, new ArrayList<GEventInfo>());
				}
			}
		}
	}
	
	private void buildEvtHandlerAppStateChangedMap()
	{
		if(this.evtHandlerAppStateChangedMap.size() == 0)
		{
			for(String evt : this.evtHandlerList)
			{
				String evtHandler = GUtil.getStandardEvtHandlerName(evt);
				boolean isAppStateChanged = false;
				
				if(this.localMethAppStateChangedMap.containsKey(evt))
				{
					isAppStateChanged = this.localMethAppStateChangedMap.get(evt);
				}
				if(!isAppStateChanged)
				{
					if(this.fullCalleeInfoMap.containsKey(evt))
					{
						for (String callee : this.fullCalleeInfoMap.get(evt))
						{
							if(this.localMethAppStateChangedMap.containsKey(callee))
							{
								isAppStateChanged = this.localMethAppStateChangedMap.get(callee);
								if(isAppStateChanged)
								{
									break;
								}
							}
						}
					}
				}
				this.evtHandlerAppStateChangedMap.put(evtHandler, isAppStateChanged);
			}
		}
	}
	
	public List<GEvtHandlerInfo> getEvtHandlerInfoList()
	{
		if(this.evtHandlerInfoList.size() == 0)
		{
			List<String> evtHandlerNameList = new ArrayList<String>();
			this.buildEvtHandlerInputEvtMap();
			this.buildEvtHandlerOutputEvtMap();
			this.buildEvtHandlerAppStateChangedMap();
			
			for(String evt : this.evtHandlerList)
			{
				String evtHandler = GUtil.getStandardEvtHandlerName(evt);
				
				if(!evtHandlerNameList.contains(evtHandler))
				{
					List<GEventInfo> inputEvtList;
					List<GEventInfo> outputEvtList;
					boolean isAppStateChanged = false;
					
					evtHandlerNameList.add(evtHandler);
					
					/* Handle input evt list */
					if(this.evtHandlerInputEvtMap.containsKey(evtHandler))
					{
						inputEvtList = this.evtHandlerInputEvtMap.get(evtHandler);
					}
					else
					{
						inputEvtList = new ArrayList<GEventInfo>();
					}
					/* Handle output evt list */
					if(this.evtHandlerOutputEvtMap.containsKey(evtHandler))
					{
						outputEvtList = this.evtHandlerOutputEvtMap.get(evtHandler);
					}
					else
					{
						outputEvtList = new ArrayList<GEventInfo>();
					}
					/* Handle app state changed */
					if(this.evtHandlerAppStateChangedMap.containsKey(evtHandler))
					{
						isAppStateChanged = this.evtHandlerAppStateChangedMap.get(evtHandler);
					}
					/* Create a new record and add it to the evtHandlerInfoList */
					this.evtHandlerInfoList.add(new GEvtHandlerInfo(this.smartAppName, evtHandler, inputEvtList, outputEvtList, isAppStateChanged));
				}
			}
		}
		
		return this.evtHandlerInfoList;
	}
	
	/* Print utilities */
	private void printEvtInfoList(List<GEventInfo> evtInfoList)
	{
		for(GEventInfo evtInfo : evtInfoList)
		{
			System.out.print("(" + evtInfo.attribute + ", " + evtInfo.evtType + ") ");
		}
		System.out.println();
	}
	public void printEvtHandlerInfoList()
	{
		this.getEvtHandlerInfoList();
		
		if(this.evtHandlerInfoList != null)
		{
			System.out.println("******************************************************");
			for(GEvtHandlerInfo evtHandlerInfo : this.evtHandlerInfoList)
			{
				System.out.println(evtHandlerInfo.smartAppName + "." + evtHandlerInfo.evtHandler + ":");
				System.out.print("Input event list: ");
				this.printEvtInfoList(evtHandlerInfo.inputEvtList);
				System.out.print("Output event list: ");
				this.printEvtInfoList(evtHandlerInfo.outputEvtList);
				System.out.println("isAppStateChanged: " + evtHandlerInfo.isAppStateChanged);
			}
		}
	}
}
