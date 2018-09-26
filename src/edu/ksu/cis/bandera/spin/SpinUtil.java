package edu.ksu.cis.bandera.spin;

import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.util.*;
import edu.ksu.cis.bandera.bir.LocVector;
import edu.ksu.cis.bandera.bir.TransVector;

public class SpinUtil {
	public static String getAssignMethName(String typeName)
	{
		String result = null;
		
		if((typeName.endsWith("_rec") || typeName.endsWith("_arr"))
				&& !typeName.equals("java_lang_Object_rec")
				&& !typeName.equals("int_arr")
				&& !typeName.equals("CInt2IntMap_rec")
				&& !typeName.equals("CInt2IIMMap_rec")
				&& !typeName.equals("CInt2IIIMMap_rec")
				&& !typeName.equals("CInt2IntMap_arr")
				&& !typeName.equals("CInt2IIMMap_arr")
				&& !typeName.equals("CInt2IIIMMap_arr"))
		{
			result = "assign_" + typeName;
		}
		
		return result;
	}
	
	public static String getChanWriteCommand(String leftExpr)
	{
		String result = null;
		
		if(leftExpr.contains(".STCommand_"))
		{
			if(leftExpr.contains("_STNetworkManager"))
			{
				result = "HandleNetworkEvt(_ST_Command);";
			}
			else
			{
				int index = leftExpr.indexOf(".STCommand_"); /* leftExpr = TooHot_switch1.STCommand_STSwitch_on */
				String device = leftExpr.substring(0, index); /* device = TooHot_switch1 */
				String subStr = leftExpr.substring(index+11); /* subStr = STSwitch_on */
   				index = subStr.indexOf("_");
   				String deviceType = subStr.substring(0, index);
   				
   				if(deviceType.equals("Location"))
   				{
   					result = "HandleLocationEvt(_ST_Command);";
   				}
   				else if(isAMapDevice(deviceType))
   				{
   					result = "Handle" + deviceType + 
   							"Evt(" + device + ", _ST_Command);"; 
   					/* result = HandleCInt2IntMapEvt(saveState_put_CInt2IntMap_JJJCTEMP_0.keyToPut, 
   					 * 				saveState_put_CInt2IntMap_JJJCTEMP_0.valueToPut, _ST_Command); 
   					 * */
   				}
   				else
   				{
   					String commandType = getSTCommandType(leftExpr);
   					
   					result = "";
   					switch(commandType)
   					{
   					case "setHeatingSetpoint": result = "_ST_Command.value = " + device + ".currentHeatingSetpoint;\n\t\t"; break;
   					case "setCoolingSetpoint": result = "_ST_Command.value = " + device + ".currentCoolingSetpoint;\n\t\t"; break;
   					case "setLevel": result = "_ST_Command.value = " + device + ".currentLevel;\n\t\t"; break;
   					case "setHue": result = "_ST_Command.value = " + device + ".currentHue;\n\t\t"; break;
   					case "setSaturation": result = "_ST_Command.value = " + device + ".currentSaturation;\n\t\t"; break;
   					case "setColor": result = "_ST_Command.value = " + device + ".currentColor;\n\t\t"; break;
   					default: break;
   					}
   					
   					result += "Handle" + deviceType + 
   							"Evt(" + device + ".gArrIndex, _ST_Command);"; /* result = HandleSTSwitchEvt(TooHot_switch1.gArrIndex, _ST_Command); */
   				}
			}
		}
		
		return result;
	}
	
	public static String getSTCommandType(String leftExpr)
	{
		String result = null;
		
		if(leftExpr.contains(".STCommand_"))
		{
			int index = leftExpr.indexOf(".STCommand_");
			String subStr = leftExpr.substring(index+11);
			index = subStr.indexOf("_");
			result = subStr.substring(index+1);
		}
		
		return result;
	}
	
	public static java.util.List<SootMethod> getEvtHandlerMethods(SootClass sc)
	{
		java.util.List<SootMethod> result = new java.util.ArrayList<SootMethod>();
		List smList = sc.getMethods();
		
		for(int i = 0; i < smList.size(); i++)
		{
			SootMethod sm = (SootMethod) smList.get(i);
			
			if(sm.getName().endsWith("EvtHandler"))
			{
				result.add(sm);
			}
		}
		
		return result;
	}
	
	public static boolean containEvtHandlerMethod(SootClass sc)
	{
		if(SpinUtil.getEvtHandlerMethods(sc).size() > 0)
		{
			return true;
		}
			
		return false;
	}
	
	public static boolean isADevice(String recordName)
	{
		if(recordName.equals("STAttribute_rec") || recordName.equals("STCommand_rec") ||
			recordName.equals("STCapability_rec") || recordName.equals("STEvent_rec") ||
			recordName.equals("STMode_rec") || recordName.equals("STState_rec") || 
			recordName.equals("STHub_rec") || recordName.equals("CInt2IntMap_rec") ||
			recordName.equals("CInt2IIMMap_rec") || recordName.equals("CInt2IIIMMap_rec") ||
			recordName.equals("STSunriseSunset_rec"))
		{
			return false;
		}
		return true;
	}
	
	public static boolean isAMapArr(String recordName)
	{
		if(recordName.equals("CInt2IntMap_arr") ||
			recordName.equals("CInt2IIMMap_arr") || 
			recordName.equals("CInt2IIIMMap_arr"))
		{
			return true;
		}
		return false;
	}
	
	public static String getMapClassNameFromRecName(String recordName)
	{
		switch(recordName)
		{
		case "CInt2IntMap_rec": return "CInt2IntMap";
		case "CInt2IIMMap_rec": return "CInt2IIMMap";
		case "CInt2IIIMMap_rec": return "CInt2IIIMMap";
		}
		return null;
	}
	
	public static String getArrayNameFromRecordName(String recordName)
	{
		String result = null;
		
		if(recordName.contains("_rec"))
		{
			int index = recordName.indexOf("_rec");
			result =  recordName.substring(0, index) + "_arr";
		}
		
		return result;
	}
	
	public static String getArrayNameFromArrayAccessExpr(String arrayAccessExpr)
	{
		String result = null;
		
		if(arrayAccessExpr.contains(".element["))
		{
			int index = arrayAccessExpr.indexOf(".element[");
			result =  arrayAccessExpr.substring(0, index);
		}
		
		return result;
	}
	
	/* deviceName: frontDoorSensor
	 * deviceType: STMotionSensor
	 * => result = _g_MotionSensorArr.element[frontDoorSensor_STMotionSensor]
	 * */
	public static String getGlobalArrRef(String deviceName, String deviceType)
	{
		String result = "_g_" + deviceType + "Arr.element[" + deviceName + "_" + deviceType + "]";

		return result;
	}
	
	/* deviceName: frontDoorSensor
	 * deviceType: STMotionSensor
	 * => result = _g_MotionSensorArr.element[frontDoorSensor_STMotionSensor].NumSubscribers
	 * */
	public static String getGlobalArrRefNumSubscribers(String deviceName, String deviceType)
	{
		String result = "_g_" + deviceType + "Arr.element[" + deviceName + "_" + deviceType + "].NumSubscribers";
		
		return result;
	}
	
	/* deviceName: frontDoorSensor
	 * deviceType: STMotionSensor
	 * => result = _g_MotionSensorArr.element[frontDoorSensor_STMotionSensor].BroadcastChans
	 * [_g_MotionSensorArr.element[frontDoorSensor_STMotionSensor].NumSubscribers]
	 * */
	public static String getGlobalArrRefBroadcastChans(String deviceName, String deviceType)
	{
		String result = "_g_" + deviceType + "Arr.element[" + deviceName + "_" + deviceType + "].BroadcastChans["
				+ "_g_" + deviceType + "Arr.element[" + deviceName + "_" + deviceType + "].NumSubscribers]";
	
		return result;
	}
	
	/* deviceName: frontDoorSensor_STMotionSensor
	 * deviceType: STMotionSensor
	 * => result = _g_MotionSensorArr.element[frontDoorSensor_STMotionSensor].NumReceivedCommands
	 * */
	public static String getGlobalArrRefNumReceivedCommands(String deviceName, String deviceType)
	{
		String result = "_g_" + deviceType + "Arr.element[" + deviceName + "].NumReceivedCommands";

		return result;
	}
	
	/* staticVarName = _static__STNetworkManager.receivedPhoneNumber
	 * => result = _STNetworkManager.receivedPhoneNumber
	 * */
	public static String getOriginalStaticVarName(String staticVarName)
	{
		String result = null;
		
		if(staticVarName.startsWith("_static_"))
		{
			result =  staticVarName.substring(8);
		}
		
		return result;
	}
	
	public static boolean isLocReferred(LocVector threadLocVector, int locId)
	{
		for(int i = 0; i < threadLocVector.size(); i++)
		{
			int currentLocId = threadLocVector.elementAt(i).getId();
			
			if((currentLocId+1) != locId)
			{
				TransVector outTrans = threadLocVector.elementAt(i).getOutTrans();
				
				for(int j = 0; j < outTrans.size(); j++)
				{
					if(outTrans.elementAt(j).getToLoc().getId() == locId)
					{
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public static DeviceNameInfo getDeviceNameInfo(String deviceType)
	{
		DeviceNameInfo device = null;
		
		switch(deviceType)
		{
		case "STSwitch":
		{
			device = new DeviceNameInfo();
			device.gArr = "_g_" + deviceType + "Arr";
			device.currentDevice = "currentSwitch";
			device.deviceState = "switchState";
			device.deviceStateName = "SWITCH";
		}
		break;
		case "STMotionSensor":
		{
			device = new DeviceNameInfo();
			device.gArr = "_g_" + deviceType + "Arr";
			device.currentDevice = "currentMotion";
			device.deviceState = "motionState";
			device.deviceStateName = "MOTION";
		}
		break;
		case "STTempMeas":
		{
			device = new DeviceNameInfo();
			device.gArr = "_g_" + deviceType + "Arr";
			device.currentDevice = "currentTemperature";
			device.deviceState = "temperatureState";
			device.deviceStateName = "TEMPERATURE";
		}
		break;
		case "STPresSensor":
		{
			device = new DeviceNameInfo();
			device.gArr = "_g_" + deviceType + "Arr";
			device.currentDevice = "currentPresence";
			device.deviceState = "presenceState";
			device.deviceStateName = "PRESENCE";
		}
		break;
		case "STLock":
		{
			device = new DeviceNameInfo();
			device.gArr = "_g_" + deviceType + "Arr";
			device.currentDevice = "currentLock";
			device.deviceState = "lockState";
			device.deviceStateName = "LOCK";
		}
		break;
		case "STContactSensor":
		{
			device = new DeviceNameInfo();
			device.gArr = "_g_" + deviceType + "Arr";
			device.currentDevice = "currentContact";
			device.deviceState = "contactState";
			device.deviceStateName = "CONTACT";
		}
		break;
		case "STPowerMeter":
		{
			device = new DeviceNameInfo();
			device.gArr = "_g_" + deviceType + "Arr";
			device.currentDevice = "currentPower";
			device.deviceState = "powerState";
			device.deviceStateName = "POWER";
		}
		break;
		case "STIlMeas":
		{
			device = new DeviceNameInfo();
			device.gArr = "_g_" + deviceType + "Arr";
			device.currentDevice = "currentIlluminance";
			device.deviceState = "illuminanceState";
			device.deviceStateName = "ILLUMINANCE";
		}
		break;
		case "STWaterSensor":
		{
			device = new DeviceNameInfo();
			device.gArr = "_g_" + deviceType + "Arr";
			device.currentDevice = "currentWater";
			device.deviceState = "waterState";
			device.deviceStateName = "WATER";
		}
		break;
		case "STValve":
		{
			device = new DeviceNameInfo();
			device.gArr = "_g_" + deviceType + "Arr";
			device.currentDevice = "currentValve";
			device.deviceState = "valveState";
			device.deviceStateName = "VALVE";
		}
		break;
		case "STAccSensor":
		{
			device = new DeviceNameInfo();
			device.gArr = "_g_" + deviceType + "Arr";
			device.currentDevice = "currentAcceleration";
			device.deviceState = "accelerationState";
			device.deviceStateName = "ACCELERATION";
		}
		break;
		case "STSwitchLevel":
		{
			device = new DeviceNameInfo();
			device.gArr = "_g_" + deviceType + "Arr";
			device.currentDevice = "currentLevel";
			device.deviceState = "levelState";
			device.deviceStateName = "LEVEL";
		}
		break;
		case "STCarMoDetector":
		{
			device = new DeviceNameInfo();
			device.gArr = "_g_" + deviceType + "Arr";
			device.currentDevice = "currentCarbonMonoxide";
			device.deviceState = "carbonMonoxideState";
			device.deviceStateName = "CARBONMONOXIDE";
		}
		break;
		case "STAlarm":
		{
			device = new DeviceNameInfo();
			device.gArr = "_g_" + deviceType + "Arr";
			device.currentDevice = "currentAlarm";
			device.deviceState = "alarmState";
			device.deviceStateName = "ALARM";
		}
		break;
		case "STSmokeDetector":
		{
			device = new DeviceNameInfo();
			device.gArr = "_g_" + deviceType + "Arr";
			device.currentDevice = "currentSmoke";
			device.deviceState = "smokeState";
			device.deviceStateName = "SMOKE";
		}
		break;
		case "STAeonKeyFob":
		{
			device = new DeviceNameInfo();
			device.gArr = "_g_" + deviceType + "Arr";
			device.currentDevice = "currentButton";
			device.deviceState = "buttonState";
			device.deviceStateName = "BUTTON";
		}
		break;
		case "STRelHumMeas":
		{
			device = new DeviceNameInfo();
			device.gArr = "_g_" + deviceType + "Arr";
			device.currentDevice = "currentHumidity";
			device.deviceState = "humidityState";
			device.deviceStateName = "HUMIDITY";
		}
		break;
		}
		
		return device;
	}
	
	public static boolean isANumberCommand(String deviceType)
	{
		switch(deviceType)
		{
		case "STSwitchLevel": return true;
		}
		return false;
	}
	
	public static boolean isAMapDevice(String deviceType)
	{
		switch(deviceType)
		{
		case "CInt2IntMap":
		case "CInt2IIMMap":
		case "CInt2IIIMMap": return true;
		}
		return false;
	}
	
	public static boolean isASensorDevice(String deviceType)
	{
		switch(deviceType)
		{
		case "STMotionSensor":
		case "STPresSensor":
		case "STContactSensor":
		case "STPowerMeter":
		case "STThermostat":
		case "STIlMeas":
		case "STWaterSensor":
		case "STAccSensor":
		case "STCarMoDetector":
		case "STSmokeDetector":
		case "STAeonKeyFob":
		case "STRelHumMeas":
		case "STTempMeas": return true;
		}
		return false;
	}
}
