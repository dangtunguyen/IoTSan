package edu.ksu.cis.bandera.jjjc.gparser.configinfomanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.ksu.cis.bandera.jjjc.gparser.g2jconverter.GLiteralBuilder;
import edu.ksu.cis.bandera.jjjc.gparser.potentialriskscreener.GPotentialRiskScreener;
import edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor.GDeviceInputInfo;
import edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor.GInputInfo;
import edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor.GOtherInputInfo;
import edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor.GStateMapEnum;
import edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor.GSubscriptionInfo;
import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;

public class GConfigInfoManager {
	/***********************************************************/
	/* dbReader is responsible for reading configuration info 
	 * of SmartApps from database
	 * */
	private static GDbReader dbReader;
	
	/* smartAppConfigInfoMap contains configuration info of all installed smart applications.
	 * Format of a record: <SmartApp's name, SmartApp's configuration info>
	 * */
	private static Map<String, GSmartAppConfigInfo> smartAppConfigInfoMap = new HashMap<String, GSmartAppConfigInfo>();
	/* deviceList contains all devices of a SmartThings system.
	 * Format of a record: <device's name, device's type>
	 * E.g.:
	 * <frontDoorSensor_STMotionSensor, STMotionSensor> 
	 * */
	private static Map<String, String> deviceList = new HashMap<String, String>();
	
	private static java.util.List<String> deviceTypeList = new ArrayList<String>();
	/* Format of a record: <device's name, number of subscribers>
	 * E.g.:
	 * <frontDoorSensor_STMotionSensor, 2> 
	 * */
	private static Map<String, Integer> subscriberCounterMap = new HashMap<String, Integer>();
	/* configInfoGenerator is used to generate all possible config info for
	 * a new smart app
	 * */
	private static GConfigInfoGenerator configInfoGenerator;
	private static List<GInputInfo> classifiedAppInputInfoList;
	/***********************************************************/
	
	public static void init(String project_root, boolean classificationMode)
	{
		dbReader = new GDbReader(project_root + "/input/smartapps/ConfigInfo.xlsx",
				project_root + "/input/smartapps/SpecialConfigInfo.xlsx");
		dbReader.load();
		if(classificationMode)
		{
			configInfoGenerator = new GConfigInfoGenerator(dbReader.getNewSmartAppConfigInfo());
			configInfoGenerator.run();
		}
	}
	
	public static Map<String, GSmartAppConfigInfo> getSmartAppConfigInfoMap()
	{
		return smartAppConfigInfoMap;
	}
	
	public static Set<String> getSmartApps()
	{
		return smartAppConfigInfoMap.keySet();
	}

	public static java.util.List<GProcessedSubscriptionInfo> getSubscriptionInfo(String smartAppName)
	{
		if(smartAppConfigInfoMap.containsKey(smartAppName))
		{
			return smartAppConfigInfoMap.get(smartAppName).subscriptionInfoList;
		}
		return null;
	}
	
	public static java.util.List<GDeviceConfigInfo> getDeviceConfigInfo(String smartAppName)
	{
		if(smartAppConfigInfoMap.containsKey(smartAppName))
		{
			return smartAppConfigInfoMap.get(smartAppName).deviceConfigInfoList;
		}
		return null;
	}

	public static java.util.List<String> getConfigDevices(String smartAppName, String deviceInputName)
	{
		java.util.List<String> result = null;
		
		if(smartAppConfigInfoMap.containsKey(smartAppName))
		{
			java.util.List<GDeviceConfigInfo> deviceConfigInfoList = smartAppConfigInfoMap.get(smartAppName).deviceConfigInfoList;
	
			if(deviceConfigInfoList != null)
			{
				for(GDeviceConfigInfo configInfo : deviceConfigInfoList)
				{
					if(configInfo.inputName.equals(deviceInputName))
					{
						result = configInfo.configDevices;
					}
				}
			}
		}

		return result;
	}
	
	public static Map<String, Integer> getSubscriberCounterMap()
	{
		if(subscriberCounterMap.size() == 0)
		{
			for(GSmartAppConfigInfo configInfo : smartAppConfigInfoMap.values())
			{
				for(GDeviceConfigInfo dci : configInfo.deviceConfigInfoList)
				{
					for(String deviceName : dci.configDevices)
					{
						/* frontDoorSensor_STMotionSensor */
						String name = deviceName + "_" + dci.deviceType;
						
						if(!subscriberCounterMap.containsKey(name))
						{
							subscriberCounterMap.put(name, 1);
						}
						else
						{
							subscriberCounterMap.put(name, subscriberCounterMap.get(name)+1);
						}
					}
				}
			}
		}
		return subscriberCounterMap;
	}
	
	public static boolean isInputDeviceMultiple(String smartAppName, String deviceInputName)
	{
		if(smartAppConfigInfoMap.containsKey(smartAppName))
		{
			java.util.List<GDeviceConfigInfo> deviceConfigInfoList = smartAppConfigInfoMap.get(smartAppName).deviceConfigInfoList;
	
			if(deviceConfigInfoList != null)
			{
				for(GDeviceConfigInfo configInfo : deviceConfigInfoList)
				{
					if(configInfo.inputName.equals(deviceInputName))
					{
						return configInfo.isMultiple;
					}
				}
			}
		}

		return false;
	}

	public static int getNumConfigDevices(String smartAppName, String deviceInputName)
	{
		int result = 1;
		java.util.List<String> configDevices = getConfigDevices(smartAppName, deviceInputName);

		if(configDevices != null)
		{
			result = configDevices.size();
		}
		else if(!deviceInputName.equals("location") && !deviceInputName.equals("app"))
		{
			System.out.println("[GConfigInfoManager.getNumConfigDevices] configDevices = null for "
					+ deviceInputName);
		}

		return result;
	}

	public static String getEvtVarName(String smartAppName, String evtHandler)
	{
		String result = "evt";

		if(smartAppConfigInfoMap.containsKey(smartAppName))
		{
			if(smartAppConfigInfoMap.get(smartAppName).evtVarNameMap.containsKey(evtHandler))
			{
				result = smartAppConfigInfoMap.get(smartAppName).evtVarNameMap.get(evtHandler);
			}
		}

		return result;
	}

	public static Map<String, String> getDeviceListFromConfigInfo()
	{
		Set<String> smartAppNames = GPotentialRiskScreener.getCurrentSmartAppNames();
		
		deviceList = new HashMap<String, String>();
		for(Map.Entry<String, GSmartAppConfigInfo> entry : smartAppConfigInfoMap.entrySet())
		{
			if(smartAppNames.contains(entry.getKey()))
			{
				GSmartAppConfigInfo configInfo = entry.getValue();
				
				if(configInfo.deviceConfigInfoList != null)
				{
					for(GDeviceConfigInfo dci : configInfo.deviceConfigInfoList)
					{
						for(String deviceName : dci.configDevices)
						{
							/* frontDoorSensor_STMotionSensor */
							String name = deviceName + "_" + dci.deviceType;
							
							if(!deviceList.containsKey(name))
							{
								deviceList.put(name, dci.deviceType);
							}
						}
					}
				}
			}
		}
			
		return deviceList;
	}
	
	public static java.util.List<String> getDeviceTypeListFromConfigInfo()
	{
		Set<String> smartAppNames = GPotentialRiskScreener.getCurrentSmartAppNames();
		
		deviceTypeList = new ArrayList<String>();
		for(Map.Entry<String, GSmartAppConfigInfo> entry : smartAppConfigInfoMap.entrySet())
		{
			if(smartAppNames.contains(entry.getKey()))
			{
				GSmartAppConfigInfo configInfo = entry.getValue();
				
				if(configInfo.deviceConfigInfoList != null)
				{
					for(GDeviceConfigInfo dci : configInfo.deviceConfigInfoList)
					{
						if(!deviceTypeList.contains(dci.deviceType))
						{
							deviceTypeList.add(dci.deviceType);
						}
					}
				}
			}
		}
			
		return deviceTypeList;
	}
	
	public static boolean isSTTempMeasPresent()
	{
		Set<String> smartAppNames = GPotentialRiskScreener.getCurrentSmartAppNames();
		
		for(Map.Entry<String, GSmartAppConfigInfo> entry : smartAppConfigInfoMap.entrySet())
		{
			if(smartAppNames.contains(entry.getKey()))
			{
				GSmartAppConfigInfo configInfo = entry.getValue();
				
				if(configInfo.deviceConfigInfoList != null)
				{
					for(GDeviceConfigInfo dci : configInfo.deviceConfigInfoList)
					{
						if(dci.deviceType.equals("STTempMeas"))
						{
							return true;
						}
					}
				}
			}
		}
			
		return false;
	}
	
	public static java.util.List<String> getSubscribedDevices(String smartAppName)
	{
		java.util.List<String> result = new ArrayList<String>();
		
		if(smartAppConfigInfoMap.containsKey(smartAppName))
		{
			for(GProcessedSubscriptionInfo psi : smartAppConfigInfoMap.get(smartAppName).subscriptionInfoList)
			{
				if(!result.contains(psi.inputName))
				{
					result.add(psi.inputName);
				}
			}
		}
		return result;
	}
	
	public static boolean isSubscribedToLocation(String smartAppName)
	{
		if(smartAppConfigInfoMap.containsKey(smartAppName))
		{
			if(smartAppConfigInfoMap.get(smartAppName).subscriptionInfoList != null)
			{
				for(GProcessedSubscriptionInfo psi : smartAppConfigInfoMap.get(smartAppName).subscriptionInfoList)
				{
					if(psi.inputName.equals("location"))
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static boolean isSubscribedToApp(String smartAppName)
	{
		if(smartAppConfigInfoMap.containsKey(smartAppName))
		{
			if(smartAppConfigInfoMap.get(smartAppName).subscriptionInfoList != null)
			{
				for(GProcessedSubscriptionInfo psi : smartAppConfigInfoMap.get(smartAppName).subscriptionInfoList)
				{
					if(psi.inputName.equals("app"))
					{
						return true;
					}
				}
			}	
		}
		return false;
	}
	
	public static String getConfiguredPhoneNumber(String smartAppName)
	{
		if(smartAppConfigInfoMap.containsKey(smartAppName))
		{
			if(smartAppConfigInfoMap.get(smartAppName).configPhoneInfo != null)
			{
				return smartAppConfigInfoMap.get(smartAppName).configPhoneInfo.configuredPhoneNumber;
			}
		}
		return null;
	}
	
	public static GPhoneConfigInfo getPhoneConfigInfo(String smartAppName)
	{
		if(smartAppConfigInfoMap.containsKey(smartAppName))
		{
			return smartAppConfigInfoMap.get(smartAppName).configPhoneInfo;
		}
		return null;
	}
	
	public static List<GOtherConfigInfo> getOtherConfigInfoList(String smartAppName)
	{
		if(smartAppConfigInfoMap.containsKey(smartAppName))
		{
			return smartAppConfigInfoMap.get(smartAppName).otherConfigInfoList;
		}
		return null;
	}
	
	public static boolean isTempMeasDeviceUsed()
	{
		getDeviceTypeListFromConfigInfo();
		if(deviceTypeList.contains("STTempMeas"))
		{
			return true;
		}
		return false;
	}
	
	public static void processSubscriptionInfo(String smartAppName, List<GSubscriptionInfo> subscriptionInfoList)
	{
		List<GProcessedSubscriptionInfo> processedSubscriptionInfoList = new ArrayList<GProcessedSubscriptionInfo>();
		Map<String, List<GSubscriptionInfo>> infoMap = new HashMap<String, List<GSubscriptionInfo>>();
		
		for(GSubscriptionInfo info : subscriptionInfoList)
		{
			String inputName;
			GSubscriptionInfo newInfo = info.clone();
			
			if(info.inputName.equals("location") || info.inputName.equals("app"))
			{
				inputName = info.inputName;
			}
			else
			{
				inputName = smartAppName + "_" + info.inputName;
			}
			
			if(newInfo.subscribedAttribute.toLowerCase().equals("sunset"))
			{
				newInfo.subscribedAttribute = "sunsetTime";
			}
			else if(newInfo.subscribedAttribute.toLowerCase().equals("sunrise"))
			{
				newInfo.subscribedAttribute = "sunriseTime";
			}
			
			if(newInfo.subscribedEvtType.equals("active"))
			{
				newInfo.subscribedEvtType = "_active";
			}
			else
			{
				newInfo.subscribedEvtType = GUtil.getCommandFromEvtType(newInfo.subscribedEvtType);
			}
			
			newInfo.evtHandler = GUtil.getStandardEvtHandlerName(newInfo.evtHandler);
			newInfo.inputName = inputName;
			if(!infoMap.containsKey(inputName))
			{
				List<GSubscriptionInfo> infoList = new ArrayList<GSubscriptionInfo>();
				infoList.add(newInfo);
				infoMap.put(inputName, infoList);
			}
			else
			{
				infoMap.get(inputName).add(newInfo);
			}
		}
		for(Map.Entry<String, List<GSubscriptionInfo>> entry : infoMap.entrySet())
		{
			processedSubscriptionInfoList.add(new GProcessedSubscriptionInfo(entry.getKey(), entry.getValue()));
		}
		
		if(smartAppConfigInfoMap.containsKey(smartAppName))
		{
			smartAppConfigInfoMap.get(smartAppName).subscriptionInfoList = processedSubscriptionInfoList;
		}
		else
		{
			GSmartAppConfigInfo smartAppConfigInfo = new GSmartAppConfigInfo(processedSubscriptionInfoList, null,
																			null, null, null);
			smartAppConfigInfoMap.put(smartAppName, smartAppConfigInfo);
		}
	}
	
	public static void processEvtVarNames(String smartAppName, Map<String, String> evtVarNameMap)
	{
		Map<String, String> processedEvtVarNameMap = new HashMap<String, String>();
		
		for(Map.Entry<String, String> entry : evtVarNameMap.entrySet())
		{
			String evtHandler = GUtil.getStandardEvtHandlerName(entry.getKey());
			
			if(evtHandler != null)
			{
				evtHandler = smartAppName + "_" + evtHandler;
				processedEvtVarNameMap.put(evtHandler, entry.getValue());
			}
			else
			{
				System.out.println("[GConfigInfoManager][processEvtVarNames] " + entry.getKey());
			}
		}
		
		if(smartAppConfigInfoMap.containsKey(smartAppName))
		{
			smartAppConfigInfoMap.get(smartAppName).evtVarNameMap = processedEvtVarNameMap;
		}
		else
		{
			GSmartAppConfigInfo smartAppConfigInfo = new GSmartAppConfigInfo(null, null, null,
																			processedEvtVarNameMap, null);
			smartAppConfigInfoMap.put(smartAppName, smartAppConfigInfo);
		}
	}
	
	public static void processInputInfo(String smartAppName, List<GInputInfo> inputInfoList)
	{
		if(GUtil.classifiedSmartAppName.equals(smartAppName))
		{
			/* We will save the info of the new smart app now and process it later */
			classifiedAppInputInfoList = new ArrayList<GInputInfo>();
			for(GInputInfo inputInfo : inputInfoList)
			{
				classifiedAppInputInfoList.add(inputInfo.clone());
			}
		}
		else
		{
			Map<String, List<String>> configInfoMap = dbReader.getConfigInfo(smartAppName);
			
			if(configInfoMap != null)
			{
				if(smartAppConfigInfoMap.containsKey(smartAppName))
				{
					java.util.List<GDeviceConfigInfo> dciList = new ArrayList<GDeviceConfigInfo>();
					List<GOtherConfigInfo> otherConfigInfoList = new ArrayList<GOtherConfigInfo>();
					
					for(GInputInfo inputInfo : inputInfoList)
					{
						if(inputInfo instanceof GDeviceInputInfo)
						{
							GDeviceInputInfo device = (GDeviceInputInfo)inputInfo;
							
							if(configInfoMap.containsKey(device.inputName))
							{
								GDeviceConfigInfo dci = new GDeviceConfigInfo(smartAppName + "_" + device.inputName, 
															device.isMultiple, configInfoMap.get(device.inputName), device.deviceType);
								dciList.add(dci);
							}
							else
							{
								System.out.println("[GConfigInfoManager][processInputInfo] missing config info of "
										+ device.inputName);
							}
						}
						else if(inputInfo instanceof GOtherInputInfo)
						{
							GOtherInputInfo otherInfo = (GOtherInputInfo)inputInfo;
							
							if(configInfoMap.containsKey(otherInfo.inputName))
							{
								if(otherInfo.infoType.equals("PHONE"))
								{
									if(GUtil.isDigitList(configInfoMap.get(otherInfo.inputName).get(0)))
									{
										GPhoneConfigInfo phoneInfo = new GPhoneConfigInfo(smartAppName + "_" +
																		otherInfo.inputName, configInfoMap.get(otherInfo.inputName).get(0));
										smartAppConfigInfoMap.get(smartAppName).configPhoneInfo = phoneInfo;
									}
									else
									{
										System.out.println("[GConfigInfoManager][processInputInfo] wrong phone number setting " +
												configInfoMap.get(otherInfo.inputName).get(0));
									}
								}
								else if(otherInfo.infoType.equals("DECIMAL") || otherInfo.infoType.equals("NUMBER")
										|| otherInfo.infoType.equals("TIME"))
								{
									/* Convert to int */
									GOtherConfigInfo info = new GOtherConfigInfo(smartAppName + "_" + otherInfo.inputName, 
											configInfoMap.get(otherInfo.inputName), otherInfo.isMultiple);
									otherConfigInfoList.add(info);
								}
								else
								{
									/* Convert to int */
									int value = GLiteralBuilder.getIntValueFromStr(configInfoMap.get(otherInfo.inputName).get(0));
									GOtherConfigInfo info = new GOtherConfigInfo(smartAppName + "_" + otherInfo.inputName, 
																	Arrays.asList(Integer.toString(value)), otherInfo.isMultiple);
									otherConfigInfoList.add(info);
								}
							}
							else
							{
								System.out.println("[GConfigInfoManager][processInputInfo] missing config info of "
										+ otherInfo.inputName);
							}
						}
					}
					
					smartAppConfigInfoMap.get(smartAppName).deviceConfigInfoList = dciList;
					smartAppConfigInfoMap.get(smartAppName).otherConfigInfoList = otherConfigInfoList;
				}
				else
				{
					System.out.println("[GConfigInfoManager][processInputInfo] wrong call order!!!");
				}
			}
		}
	}
	private static void processClassifiedAppInputInfo()
	{
		Map<String, List<String>> configInfoMap = dbReader.getConfigInfo(GUtil.classifiedSmartAppName);
		
		if(configInfoMap != null)
		{
			if(smartAppConfigInfoMap.containsKey(GUtil.classifiedSmartAppName))
			{
				java.util.List<GDeviceConfigInfo> dciList = new ArrayList<GDeviceConfigInfo>();
				List<GOtherConfigInfo> otherConfigInfoList = new ArrayList<GOtherConfigInfo>();
				
				for(GInputInfo inputInfo : classifiedAppInputInfoList)
				{
					if(inputInfo instanceof GDeviceInputInfo)
					{
						GDeviceInputInfo device = (GDeviceInputInfo)inputInfo;
						
						if(configInfoMap.containsKey(device.inputName))
						{
							GDeviceConfigInfo dci = new GDeviceConfigInfo(GUtil.classifiedSmartAppName + "_" + device.inputName, 
														device.isMultiple, configInfoMap.get(device.inputName), device.deviceType);
							dciList.add(dci);
						}
						else
						{
							System.out.println("[GConfigInfoManager][processClassifiedAppInputInfo] missing config info of "
									+ device.inputName);
						}
					}
					else if(inputInfo instanceof GOtherInputInfo)
					{
						GOtherInputInfo otherInfo = (GOtherInputInfo)inputInfo;
						
						if(configInfoMap.containsKey(otherInfo.inputName))
						{
							if(otherInfo.infoType.equals("PHONE"))
							{
								if(GUtil.isDigitList(configInfoMap.get(otherInfo.inputName).get(0)))
								{
									GPhoneConfigInfo phoneInfo = new GPhoneConfigInfo(GUtil.classifiedSmartAppName + "_" +
																	otherInfo.inputName, configInfoMap.get(otherInfo.inputName).get(0));
									smartAppConfigInfoMap.get(GUtil.classifiedSmartAppName).configPhoneInfo = phoneInfo;
								}
								else
								{
									System.out.println("[GConfigInfoManager][processClassifiedAppInputInfo] wrong phone number setting " +
											configInfoMap.get(otherInfo.inputName).get(0));
								}
							}
							else if(otherInfo.infoType.equals("DECIMAL") || otherInfo.infoType.equals("NUMBER")
									|| otherInfo.infoType.equals("TIME"))
							{
								/* Convert to int */
								GOtherConfigInfo info = new GOtherConfigInfo(GUtil.classifiedSmartAppName + "_" + otherInfo.inputName, 
										configInfoMap.get(otherInfo.inputName), otherInfo.isMultiple);
								otherConfigInfoList.add(info);
							}
							else
							{
								/* Convert to int */
								int value = GLiteralBuilder.getIntValueFromStr(configInfoMap.get(otherInfo.inputName).get(0));
								GOtherConfigInfo info = new GOtherConfigInfo(GUtil.classifiedSmartAppName + "_" + otherInfo.inputName, 
																Arrays.asList(Integer.toString(value)), otherInfo.isMultiple);
								otherConfigInfoList.add(info);
							}
						}
						else
						{
							System.out.println("[GConfigInfoManager][processClassifiedAppInputInfo] missing config info of "
									+ otherInfo.inputName);
						}
					}
				}
				
				smartAppConfigInfoMap.get(GUtil.classifiedSmartAppName).deviceConfigInfoList = dciList;
				smartAppConfigInfoMap.get(GUtil.classifiedSmartAppName).otherConfigInfoList = otherConfigInfoList;
			}
			else
			{
				System.out.println("[GConfigInfoManager][processClassifiedAppInputInfo] wrong call order!!!");
			}
		}
	}
	
	public static Map<String, List<String>> getConfigInfo(String smartAppName)
	{
		return dbReader.getConfigInfo(smartAppName);
	}
	
	public static String getConfigDeviceType(String smartAppName, String inputName)
	{
		Map<String, String> deviceTypeMap = dbReader.getDeviceTypeMap(smartAppName);
		
		if(deviceTypeMap != null)
		{
			if(deviceTypeMap.containsKey(inputName))
			{
				return deviceTypeMap.get(inputName);
			}
		}
		
		return null;
	}
	
	public static List<GSpecialConfiInfo> getSpecialConfiInfoList()
	{
		Set<String> deviceNameList = getDeviceListFromConfigInfo().keySet();
		List<GSpecialConfiInfo> result = new ArrayList<GSpecialConfiInfo>();
		
		for(GSpecialConfiInfo info : dbReader.getSpecialConfiInfoList())
		{
			if(deviceNameList.contains(info.deviceName))
			{
				result.add(info);
			}
		}
		
		return result;
	}
	
	public static String getDeviceType(String smartAppName, String inputName)
	{
		if(smartAppConfigInfoMap.containsKey(smartAppName))
		{
			if(smartAppConfigInfoMap.get(smartAppName).deviceConfigInfoList != null)
			{
				for(GDeviceConfigInfo device : smartAppConfigInfoMap.get(smartAppName).deviceConfigInfoList)
				{
					if(device.inputName.equals(inputName))
					{
						return device.deviceType;
					}
				}
			}
		}
		
		return null;
	}
	
	public static void setInstalledMethNeeded(String smartAppName)
	{
		if(smartAppConfigInfoMap.containsKey(smartAppName))
		{
			smartAppConfigInfoMap.get(smartAppName).isInstalledMethNeeded = true;
		}
	}
	public static boolean getInstalledMethNeeded(String smartAppName)
	{
		if(smartAppConfigInfoMap.containsKey(smartAppName))
		{
			return smartAppConfigInfoMap.get(smartAppName).isInstalledMethNeeded;
		}
		return false;
	}
	public static void setStateMapUsed(String smartAppName, GStateMapEnum stateMapType)
	{
		if(smartAppConfigInfoMap.containsKey(smartAppName))
		{
			smartAppConfigInfoMap.get(smartAppName).stateMapType = stateMapType;
		}
	}
	public static GStateMapEnum getStateMapUsed(String smartAppName)
	{
		if(smartAppConfigInfoMap.containsKey(smartAppName))
		{
			return smartAppConfigInfoMap.get(smartAppName).stateMapType;
		}
		return GStateMapEnum.unknown;
	}
	public static boolean isStateMapUsed()
	{
		Set<String> smartApps = GPotentialRiskScreener.getCurrentSmartAppNames();
		
		for(String smartAppName : smartApps)
		{
			if(getStateMapUsed(smartAppName) != GStateMapEnum.unknown)
			{
				return true;
			}
		}
		
		return false;
	}
	public static void setHttpGet2ValueRangeMap(String smartAppName, Map<String, List<String>> httpGet2ValueRangeMap)
	{
		if(smartAppConfigInfoMap.containsKey(smartAppName))
		{
			smartAppConfigInfoMap.get(smartAppName).httpGet2ValueRangeMap = httpGet2ValueRangeMap;
		}
	}
	public static Map<String, List<String>> getHttpGet2ValueRangeMap(String smartAppName)
	{
		if(smartAppConfigInfoMap.containsKey(smartAppName))
		{
			return smartAppConfigInfoMap.get(smartAppName).httpGet2ValueRangeMap;
		}
		return null;
	}
	public static void setAdditionalIntHttpGetVarRangeList(String smartAppName, List<String> additionalIntHttpGetVarRangeList)
	{
		if(smartAppConfigInfoMap.containsKey(smartAppName))
		{
			smartAppConfigInfoMap.get(smartAppName).additionalIntHttpGetVarRangeList = additionalIntHttpGetVarRangeList;
		}
	}
	public static List<String> getAdditionalIntHttpGetVarRangeList(String smartAppName)
	{
		if(smartAppConfigInfoMap.containsKey(smartAppName))
		{
			return smartAppConfigInfoMap.get(smartAppName).additionalIntHttpGetVarRangeList;
		}
		return null;
	}
	
	private static List<String> getInputNames(String deviceType)
	{
		List<String> result = new ArrayList<String>();
		
		for(String smartAppName : GPotentialRiskScreener.getCurrentSmartAppNames())
		{
			if(smartAppConfigInfoMap.containsKey(smartAppName))
			{
				if(smartAppConfigInfoMap.get(smartAppName).deviceConfigInfoList != null)
				{
					for(GDeviceConfigInfo deviceInfo : smartAppConfigInfoMap.get(smartAppName).deviceConfigInfoList)
					{
						if(deviceInfo.deviceType.equals(deviceType))
						{
							result.add(deviceInfo.inputName);
						}
					}
				}
			}
		}
		
		return result;
	}
	public static boolean isEvtTypeSubsribed(String deviceType, String evtType)
	{
		List<String> inputNameList = getInputNames(deviceType);
		
		for(String smartAppName : GPotentialRiskScreener.getCurrentSmartAppNames())
		{
			if(smartAppConfigInfoMap.containsKey(smartAppName))
			{
				if(smartAppConfigInfoMap.get(smartAppName).subscriptionInfoList != null)
				{
					for(GProcessedSubscriptionInfo processedSubInfo : smartAppConfigInfoMap.get(smartAppName).subscriptionInfoList)
					{
						if(inputNameList.contains(processedSubInfo.inputName))
						{
							if(processedSubInfo.subscriptionInfoList != null)
							{
								for(GSubscriptionInfo subInfo : processedSubInfo.subscriptionInfoList)
								{
									if(subInfo.subscribedEvtType.equals(evtType))
									{
										return true;
									}
								}
							}
						}
					}
				}
			}
		}
		
		return false;
	}
	
	public static List<Map<String, List<String>>> getGeneratedConfigInfoList()
	{
		return configInfoGenerator.getGeneratedConfigInfoList();
	}
	public static void putNewSmartAppConfigInfo(Map<String, List<String>> inputInfoMap)
	{
		dbReader.putNewSmartAppConfigInfo(inputInfoMap);
		processClassifiedAppInputInfo();
	}
	
	public static void createTestData()
	{
		/* Test data for GoodNight.groovy */
		java.util.List<GProcessedSubscriptionInfo> sciList = new ArrayList<GProcessedSubscriptionInfo>();
		
		java.util.List<GSubscriptionInfo> seiList1 = new ArrayList<GSubscriptionInfo>();
		GSubscriptionInfo sei1 = new GSubscriptionInfo(null, "motion", "_active", "motionActiveEvtHandler");
		seiList1.add(sei1);
		GSubscriptionInfo sei2 = new GSubscriptionInfo(null, "motion", "inactive", "motionInactiveEvtHandler");
		seiList1.add(sei2);
		GProcessedSubscriptionInfo sci1 = new GProcessedSubscriptionInfo("GoodNight_motionSensors", seiList1);
		sciList.add(sci1);
		
		java.util.List<GSubscriptionInfo> seiList2 = new ArrayList<GSubscriptionInfo>();
		GSubscriptionInfo sei4 = new GSubscriptionInfo(null, "switch", "off", "switchOffEvtHandler");
		seiList2.add(sei4);
		GProcessedSubscriptionInfo sci2 = new GProcessedSubscriptionInfo("GoodNight_switches", seiList2);
		sciList.add(sci2);
		
		java.util.List<GSubscriptionInfo> seiList3 = new ArrayList<GSubscriptionInfo>();
		GSubscriptionInfo sei5 = new GSubscriptionInfo(null, null, null, "modeChangeEvtHandler");
		seiList3.add(sei5);
		GProcessedSubscriptionInfo sci3 = new GProcessedSubscriptionInfo("location", seiList3);
		sciList.add(sci3);

		java.util.List<GDeviceConfigInfo> dciList = new ArrayList<GDeviceConfigInfo>();
		java.util.List<String> deviceList1 = new ArrayList<String>(Arrays.asList("frontDoorSensor", 
				"gateSensor", "backDoorSensor"));
		GDeviceConfigInfo dci1 = new GDeviceConfigInfo("GoodNight_motionSensors", true, deviceList1, "STMotionSensor");
		dciList.add(dci1);
		java.util.List<String> deviceList2 = new ArrayList<String>(Arrays.asList("livingRoomBulb", "bedRoomBulb"));
		GDeviceConfigInfo dci2 = new GDeviceConfigInfo("GoodNight_switches", true, deviceList2, "STSwitch");
		dciList.add(dci2);
		
		Map<String, String> evtVarNameMap = new HashMap<String, String>();
		evtVarNameMap.put("GoodNight_switchOffEvtHandler", "evt");
		evtVarNameMap.put("GoodNight_motionActiveEvtHandler", "evt");
		evtVarNameMap.put("GoodNight_motionInactiveEvtHandler", "evt");
		evtVarNameMap.put("GoodNight_modeChangeEvtHandler", "evt");
		
		String configuredPhoneNumb = "123";
		GPhoneConfigInfo phoneInfo = new GPhoneConfigInfo("GoodNight_phoneNumber", configuredPhoneNumb);
		
		List<GOtherConfigInfo> otherConfigInfoList = new ArrayList<GOtherConfigInfo>();
		GOtherConfigInfo info1 = new GOtherConfigInfo("GoodNight_minutes", Arrays.asList("1"), false);
		otherConfigInfoList.add(info1);
		GOtherConfigInfo info2 = new GOtherConfigInfo("GoodNight_timeOfDay", Arrays.asList("5"), false);
		otherConfigInfoList.add(info2);
		GOtherConfigInfo info3 = new GOtherConfigInfo("GoodNight_newMode", Arrays.asList("1401"), true);
		otherConfigInfoList.add(info3);
		GOtherConfigInfo info4 = new GOtherConfigInfo("GoodNight_sendPushMessage", Arrays.asList("1501"), true);
		otherConfigInfoList.add(info4);
		
		GSmartAppConfigInfo configInfo = new GSmartAppConfigInfo(sciList, dciList, otherConfigInfoList, evtVarNameMap, phoneInfo);
		smartAppConfigInfoMap.put("GoodNight", configInfo);
	}
	
	public static void main(String[] args)
	{
		GConfigInfoManager.init("/Users/tunguyen/Desktop/Workspace/Java/IoTSan", false);
		System.out.println(GConfigInfoManager.getGeneratedConfigInfoList());
	}
}
