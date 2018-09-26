import java.util.Date;
import java.util.Map;

import groovy.transform.CompileStatic;

/***************************************************************************************************/
public class STSunriseSunset
{
	public int sunrise;
	public int sunset;
}
public class STWeatherFeature
{
	public int forecast;
}
public class STMode
{
	public int name;
	public int id;

	public int getId(){
//		CInt2IntMap map = new CInt2IntMap();
		
		return id;
	}
	public int getName(){
		return name;
	}
}

public class STxyz
{
	public int x;
	public int y;
	public int z;
}

public class STState
{
	//	public int doubleValue;
	//	public int floatValue;
	public int name;
	public int value;
	//	public int id;
	//	public int integerValue;
	//	public int jsonValue;
	//	public int stringValue;
	//	public int numberValue;
	//	public int numericValue;
	//	public int hubId;
	//	public int rawDateCreated;
	//	public int dateCreated;
	public int date;
	//	public int dateValue;
	//	public int installedSmartAppId;
	//	public int isoDate;
	//	public int unit;
	public STxyz xyzValue;

	public int getDate(){
		return date;
	}
	public int getDateValue(){
		return date;
	}
	//	public int getId(){
	//		return id;
	//	}
	public int getDoubleValue(){
		return value;
	}
	public int getFloatValue(){
		return value;
	}
	//	public int getHubId(){
	//		return hubId;
	//	}
	//	public int getInstalledSmartAppId(){
	//		return installedSmartAppId;
	//	}
	public int getIntegerValue(){
		return value;
	}
	//	public int getIsoDate(){
	//		return isoDate;
	//	}
	//	public int getJsonValue(){
	//		return jsonValue;
	//	}
	public int getName(){
		return name;
	}
	public int getNumberValue(){
		return value;
	}
	public int getNumericValue(){
		return value;
	}
	public int getStringValue(){
		return value;
	}
	//	public int getUnit(){
	//		return unit;
	//	}
	public int getValue(){
		return value;
	}
	public STxyz getXyzValue(){
		return xyzValue;
	}
}

public class STEvent
{
	//	public int doubleValue;
	//	public int floatValue;
	public int name;
	public int value;
	//	public boolean isStateChange;
	//	public boolean isDigital;
	public boolean physical;
	//	public STDevice device;
	public int deviceId;
	//	public int hubId;
	//	public int integerValue;
	//	public int jsonValue;
	//	public STLocation location;
	//	public int locationId;
	//	public int source;
	//	public int stringValue;
	//	public int numberValue;
	//	public int numericValue;
	//	public int data;
	public int date;
	//	public int dateValue;
	//	public int description;
	//	public int descriptionText;
	//	public int displayName;
	public int id;
	//	public int installedSmartAppId;
	//	public int isoDate;
	public int unit;
	public boolean type;

	public int getData(){
		return value;
	}
	public int getDate(){
		return date;
	}
	public int getDateValue(){
		return date;
	}
	//	public int getDescription(){
	//		return description;
	//	}
	//	public int getDescriptionText(){
	//		return descriptionText;
	//	}
	//	public STDevice getDevice(){
	//		return device;
	//	}
	//	public int getDisplayName(){
	//		return displayName;
	//	}
	public int getDeviceId(){
		return deviceId;
	}
	public int getId(){
		return id;
	}
	public int getDoubleValue(){
		return value;
	}
	public int getFloatValue(){
		return value;
	}
	//	public int getHubId(){
	//		return hubId;
	//	}
	//	public int getInstalledSmartAppId(){
	//		return installedSmartAppId;
	//	}
	public int getIntegerValue(){
		return value;
	}
	//	public int getIsoDate(){
	//		return isoDate;
	//	}
	//	public int getJsonValue(){
	//		return jsonValue;
	//	}
	//	public STLocation getLocation(){
	//		return location;
	//	}
	//	public int getLocationId(){
	//		return locationId;
	//	}
	public int getName(){
		return name;
	}
	public int getNumberValue(){
		return value;
	}
	public int getNumericValue(){
		return value;
	}
	//	public int getSource(){
	//		return source;
	//	}
	public int getStringValue(){
		return value;
	}
	public int getUnit(){
		return unit;
	}
	public int getValue(){
		return value;
	}
	//	public Map<int, int> getXyzValue(){}
	//	public boolean isDigital(){
	//		return isDigital;
	//	}
	public boolean isPhysical(){
		return physical;
	}
	//	public boolean stateChange(){
	//		return isStateChange;
	//	}
}

public class STHub
{
	//	public int firmwareVersionString;
	//	public int id;
	public int localIP;
	//	public int localSrvPortTCP;
	public int name;
	//	public int type;
	//	public int zigbeeEui;
	//	public int zigbeeId;

	//	public int getFirmwareVersionString(){
	//		return firmwareVersionString;
	//	}
	//	public int getId(){
	//		return id;
	//	}
	public int getLocalIP(){
		return localIP;
	}
	//	public int getLocalSrvPortTCP(){
	//		return localSrvPortTCP;
	//	}
	public int getName(){
		return name;
	}
	//	public int getType(){
	//		return type;
	//	}
	//	public int getZigbeeEui(){
	//		return zigbeeEui;
	//	}
	//	public int getZigbeeId(){
	//		return zigbeeId;
	//	}
}

public class STLocation
{
	public boolean contactBookEnabled;
	//	public STMode currentMode;
	//	public int id;
	//	public STHub[] hubs;
	//	public int latitude;
	//	public int longitude;
	public STMode[] modes;
	//	public int name;
	//	public int temperatureScale;
	public int timeZone;
	//	public int zipCode;
	public int mode;
	public STSunriseSunset sunriseSunset;
	public STWeatherFeature weatherFeature;
	public STEvent latestEvt;
	public boolean STCommand_Location_Home;
	public boolean STCommand_Location_Away;
	public boolean STCommand_Location_Night;

	public boolean getContactBookEnabled(){
		return contactBookEnabled;
	}
	//	public STMode getCurrentMode(){
	//		return currentMode;
	//	}
	//	public int getId(){
	//		return id;
	//	}
	//	public STHub[] getHubs(){
	//		return hubs;
	//	}
	//	public int getLatitude(){
	//		return latitude;
	//	}
	//	public int getLongitude(){
	//		return longitude;
	//	}
	public int getMode(){
		return mode;
	}
	public STMode[] getModes(){
		return modes;
	}
	//	public int getName(){
	//		return name;
	//	}
	//	public void setName(int name){
	//		this.name = name;
	//	}
	public void setMode(int mode){
		if(mode == 1400) /* HOME */
		{
			STCommand_Location_Home = true;
		}
		else if(mode == 1401) /* AWAY */
		{
			STCommand_Location_Away = true;
		}
		else if(mode == 1401) /* NIGHT */
		{
			STCommand_Location_Night = true;
		}
	}
	public void setMode(STMode mode){
		if(mode.name == 1400) /* HOME */
		{
			STCommand_Location_Home = true;
		}
		else if(mode.name == 1401) /* AWAY */
		{
			STCommand_Location_Away = true;
		}
		else if(mode.name == 1401) /* NIGHT */
		{
			STCommand_Location_Night = true;
		}
	}
	//	public int getTemperatureScale(){
	//		return temperatureScale;
	//	}
	public int getTimeZone(){
		return timeZone;
	}
	//	public int getZipCode(){
	//		return zipCode;
	//	}
	STSunriseSunset getSunriseAndSunset(){
		return sunriseSunset;
	}
	public STWeatherFeature getWeatherFeature(int feature){
		return weatherFeature;
	}
	public STWeatherFeature getWeatherFeature(int feature, int zipcode){
		return weatherFeature;
	}
}

public class STAttribute
{
	//	public int dataType;
	public int name;
	//	public int[] values;

	//	public int getDataType(){
	//		return dataType;
	//	}
	public int getName(){
		return name;
	}
	//	public int[] getValues(){
	//		return values;
	//	}
}

public class STCommand
{
	//	public int[] arguments;
	public int name;

	//	public int[] getArguments(){
	//		return arguments;
	//	}
	public int getName(){
		return name;
	}
}

public class STCapability
{
	public STAttribute[] attributes;
	public STCommand[] commands;
	public int name;

	public STAttribute[] getAttributes(){
		return attributes;
	}
	public STCommand[] getCommands(){
		return commands;
	}
	public int getName(){
		return name;
	}
}

public class STDevice
{
	//	public STCapability[] capabilities;
	//	public int displayName;
	//	public STHub hub;
	//	public int name;
	public int id;
	public int gArrIndex;
	//	public int label;
	//	public int lastActivity;
	//	public int manufacturerName;
	//	public int modelName;
	//	public int status;
	//	public STAttribute[] supportedAttributes;
	//	public STCommand[] supportedCommands;
	public STEvent[] events;
	public STState[] states;
	public STState batteryState; /* <attribute name>State */
	public long currentBattery; /* current<Uppercase attribute name> */

	public STEvent[] events(int N){
		return events;
	}
	public STEvent[] events(){
		return events;
	}
	public STEvent[] eventsBetween(int startDate, int endDate){
		return events;
	}
	public STEvent[] eventsSince(int startDate){
		return events;
	}
	//	public STCapability[] getCapabilities(){
	//		return capabilities;
	//	}
	//	public int getDisplayName(){
	//		return displayName;
	//	}
	//	public STHub getHub(){
	//		return hub;
	//	}
	//	public int getName(){
	//		return name;
	//	}
	public int getId(){
		return id;
	}
	//	public int getLabel(){
	//		return label;
	//	}
	//	public int getLastActivity(){
	//		return lastActivity;
	//	}
	//	public int getManufacturerName(){
	//		return manufacturerName;
	//	}
	//	public int getModelName(){
	//		return modelName;
	//	}
	//	public int getStatus(){
	//		return status;
	//	}
	//	public STAttribute[] getSupportedAttributes(){
	//		return supportedAttributes;
	//	}
	//	public STCommand getSupportedCommands(){
	//		return supportedCommands;
	//	}
	//	public boolean hasAttribute(int attributeName){
	//		for(int i = 0; i < supportedAttributes.length; i++)
	//		{
	//			if(supportedAttributes[i].name == attributeName)
	//			{
	//				return true;
	//			}
	//		}
	//		return false;
	//	}
	//	public boolean hasCapability(int capabilityName){
	//		for(int i = 0; i < capabilities.length; i++)
	//		{
	//			if(capabilities[i].name == capabilityName)
	//			{
	//				return true;
	//			}
	//		}
	//		return false;
	//	}
	//	public boolean hasCommand(int commandName){
	//		for(int i = 0; i < supportedCommands.length; i++)
	//		{
	//			if(supportedCommands[i].name == commandName)
	//			{
	//				return true;
	//			}
	//		}
	//		return false;
	//	}
	public STState[] statesBetween(int attributeName, int startDate, int endDate){
		return states;
	}
	public STState[] statesSince(int attributeName, int startDate){
		return states;
	}

	public STState currentState(int attributeName);
	public int currentValue(int attributeName);
	public STState latestState(int attributeName);
	public int latestValue(int attributeName);
}


/*************/

public class STAlarm extends STDevice /* capability.alarm */
{
	public STState alarmState; /* <attribute name>State */
	public int currentAlarm; /* current<Uppercase attribute name> */
	public boolean STCommand_STAlarm_both, STCommand_STAlarm_off, STCommand_STAlarm_siren, STCommand_STAlarm_strobe;


	public STState currentState(int attributeName){
		return alarmState;
	}

	public int currentValue(int attributeName){
		return currentAlarm;
	}

	public STState latestState(int attributeName){
		return alarmState;
	}

	public int latestValue(int attributeName){
		return currentAlarm;
	}

	public void both(){
		STCommand_STAlarm_both = true;
	}
	public void both(int delay){
		STCommand_STAlarm_both = true;
	}
	public void off(){
		STCommand_STAlarm_off = true;
	}
	public void off(int delay){
		STCommand_STAlarm_off = true;
	}
	public void siren(){
		STCommand_STAlarm_siren = true;
	}
	public void siren(int delay){
		STCommand_STAlarm_siren = true;
	}
	public void strobe(){
		STCommand_STAlarm_strobe = true;
	}
	public void strobe(int delay){
		STCommand_STAlarm_strobe = true;
	}
}

public class STSwitch extends STDevice /* capability.switch */
{
	public STState switchState; /* <attribute name>State */
	public int currentSwitch; /* current<Uppercase attribute name> */
	public STState levelState;
	public int currentLevel;
	public boolean STCommand_STSwitch_on, STCommand_STSwitch_off, STCommand_STSwitch_setLevel;

	public STState currentState(int attributeName){
		switch(attributeName)
		{
		case 14: return switchState; /* switch */
		case 74: return levelState; /* level */
		default: return switchState;
		}
	}

	public int currentValue(int attributeName){
		switch(attributeName)
		{
		case 14: return currentSwitch; /* switch */
		case 74: return currentLevel; /* level */
		default: return currentSwitch;
		}
	}

	public STState latestState(int attributeName){
		switch(attributeName)
		{
		case 14: return switchState; /* switch */
		case 74: return levelState; /* level */
		default: return switchState;
		}
	}

	public int latestValue(int attributeName){
		switch(attributeName)
		{
		case 14: return currentSwitch; /* switch */
		case 74: return currentLevel; /* level */
		default: return currentSwitch;
		}
	}

	public void on(){
		STCommand_STSwitch_on = true;
	}
	public void on(int delay){
		STCommand_STSwitch_on = true;
	}
	public void off(){
		STCommand_STSwitch_off = true;
	}
	public void off(int delay){
		STCommand_STSwitch_off = true;
	}
	public void setLevel(int level){
		currentLevel = level;
		STCommand_STSwitch_setLevel = true;
	}
	public void setLevel(int level, int delay){
		currentLevel = level;
		STCommand_STSwitch_setLevel = true;
	}
}

public class STBulb extends STSwitch {} /* capability.bulb */

public class STTempMeas extends STDevice /* capability.temperatureMeasurement */
{
	public STState temperatureState; /* <attribute name>State */
	public int currentTemperature; /* current<Uppercase attribute name> */


	public STState currentState(int attributeName){
		return temperatureState;
	}
	public int currentValue(int attributeName){
		return currentTemperature;
	}

	public STState latestState(int attributeName){
		return temperatureState;
	}
	public int latestValue(int attributeName){
		return currentTemperature;
	}
}

public class STCarDioMeas extends STDevice /* capability.carbonDioxideMeasurement */
{
	public STState carbonDioxideState; /* <attribute name>State */
	public int currentCarbonDioxide; /* current<Uppercase attribute name> */


	public STState currentState(int attributeName){
		return carbonDioxideState;
	}
	public int currentValue(int attributeName){
		return currentCarbonDioxide;
	}

	public STState latestState(int attributeName){
		return carbonDioxideState;
	}
	public int latestValue(int attributeName){
		return currentCarbonDioxide;
	}
}

public class STCarMoDetector extends STDevice /* capability.carbonMonoxideDetector */
{
	public STState carbonMonoxideState; /* <attribute name>State */
	public int currentCarbonMonoxide; /* current<Uppercase attribute name> */
	public boolean STCommand_STCarMoDetector_COSmoke;
	
	public STState currentState(int attributeName){
		return carbonMonoxideState;
	}
	public int currentValue(int attributeName){
		return currentCarbonMonoxide;
	}

	public STState latestState(int attributeName){
		return carbonMonoxideState;
	}
	public int latestValue(int attributeName){
		return currentCarbonMonoxide;
	}
	
	public void COSmoke(){
		STCommand_STCarMoDetector_COSmoke = true;
	}
	public void COSmoke(int delay){
		STCommand_STCarMoDetector_COSmoke = true;
	}
}

public class STDoorControl extends STDevice /* capability.doorControl */
{
	public STState doorState; /* <attribute name>State */
	public int currentDoor; /* current<Uppercase attribute name> */
	public boolean STCommand_STDoorControl_open, STCommand_STDoorControl_close;


	public STState currentState(int attributeName){
		return doorState;
	}

	public int currentValue(int attributeName){
		return currentDoor;
	}

	public STState latestState(int attributeName){
		return doorState;
	}

	public int latestValue(int attributeName){
		return currentDoor;
	}

	public void close(){
		STCommand_STDoorControl_close = true;
	}
	public void close(int delay){
		STCommand_STDoorControl_close = true;
	}
	public void open(){
		STCommand_STDoorControl_open = true;
	}
	public void open(int delay){
		STCommand_STDoorControl_open = true;
	}
}

public class STLight extends STSwitch {} /* capability.light */

public class STLock extends STDevice /* capability.lock */
{
	public STState lockState; /* <attribute name>State */
	public int currentLock; /* current<Uppercase attribute name> */
	public boolean STCommand_STLock_lock, STCommand_STLock_unlock, STCommand_STLock_usercodechange;


	public STState currentState(int attributeName){
		return lockState;
	}

	public int currentValue(int attributeName){
		return currentLock;
	}

	public STState latestState(int attributeName){
		return lockState;
	}

	public int latestValue(int attributeName){
		return currentLock;
	}

	public void lock(){
		STCommand_STLock_lock = true;
	}
	public void lock(int delay){
		STCommand_STLock_lock = true;
	}
	public void unlock(){
		STCommand_STLock_unlock = true;
	}
	public void unlock(int delay){
		STCommand_STLock_unlock = true;
	}
	public void usercodechange(double user, double code, double status){
		STCommand_STLock_usercodechange = true;
	}
}

public class STMotionSensor extends STDevice /* capability.motionSensor */
{
	public STState motionState; /* <attribute name>State */
	public int currentMotion; /* current<Uppercase attribute name> */

	public STState currentState(int attributeName){
		return motionState;
	}

	public int currentValue(int attributeName){
		return currentMotion;
	}

	public STState latestState(int attributeName){
		return motionState;
	}

	public int latestValue(int attributeName){
		return currentMotion;
	}
}

public class STContactSensor extends STDevice /* capability.contactSensor */
{
	public STState contactState; /* <attribute name>State */
	public int currentContact; /* current<Uppercase attribute name> */

	public STState currentState(int attributeName){
		return contactState;
	}

	public int currentValue(int attributeName){
		return currentContact;
	}

	public STState latestState(int attributeName){
		return contactState;
	}

	public int latestValue(int attributeName){
		return currentContact;
	}
}

public class STPresSensor extends STDevice /* capability.presenceSensor */
{
	public STState presenceState; /* <attribute name>State */
	public int currentPresence; /* current<Uppercase attribute name> */


	public STState currentState(int attributeName){
		return presenceState;
	}

	public int currentValue(int attributeName){
		return currentPresence;
	}

	public STState latestState(int attributeName){
		return presenceState;
	}

	public int latestValue(int attributeName){
		return currentPresence;
	}
}

public class STOutlet extends STSwitch {} /* capability.outlet */

public class STSmokeDetector extends STCarMoDetector /* capability.smokeDetector */
{
	public STState smokeState; /* <attribute name>State */
	public int currentSmoke; /* current<Uppercase attribute name> */


	public STState currentState(int attributeName){
		if(attributeName == 50) /* smoke */
		{
			return smokeState;
		}
		else if(attributeName == 75) /* carbonMonoxide */
		{
			return carbonMonoxideState;
		}
		return batteryState; /* 73 */
	}

	public int currentValue(int attributeName){
		if(attributeName == 50) /* smoke */
		{
			return currentSmoke;
		}
		else if(attributeName == 75) /* carbonMonoxide */
		{
			return currentCarbonMonoxide;
		}
		return currentBattery; /* 73 */
	}

	public STState latestState(int attributeName){
		if(attributeName == 50) /* smoke */
		{
			return smokeState;
		}
		else if(attributeName == 75) /* carbonMonoxide */
		{
			return carbonMonoxideState;
		}
		return batteryState; /* 73 */
	}

	public int latestValue(int attributeName){
		if(attributeName == 50) /* smoke */
		{
			return currentSmoke;
		}
		else if(attributeName == 75) /* carbonMonoxide */
		{
			return currentCarbonMonoxide;
		}
		return currentBattery; /* 73 */
	}
}

public class STTherOpState extends STDevice /* capability.thermostatOperatingState */
{
	public STState thermostatOperatingStateState; /* <attribute name>State */
	public int currentThermostatOperatingState; /* current<Uppercase attribute name> */


	public STState currentState(int attributeName){
		return thermostatOperatingStateState;
	}

	public int currentValue(int attributeName){
		return currentThermostatOperatingState;
	}

	public STState latestState(int attributeName){
		return thermostatOperatingStateState;
	}

	public int latestValue(int attributeName){
		return currentThermostatOperatingState;
	}
}

public class STTherSetpoint extends STDevice /* capability.thermostatSetpoint */
{
	public STState thermostatSetpointState; /* <attribute name>State */
	public int currentThermostatSetpoint; /* current<Uppercase attribute name> */


	public STState currentState(int attributeName){
		return thermostatSetpointState;
	}
	public int currentValue(int attributeName){
		return currentThermostatSetpoint;
	}

	public STState latestState(int attributeName){
		return thermostatSetpointState;
	}
	public int latestValue(int attributeName){
		return currentThermostatSetpoint;
	}
}

public class STTherMode extends STDevice /* capability.thermostatMode */
{
	public STState thermostatModeState; /* <attribute name>State */
	public int currentThermostatMode; /* current<Uppercase attribute name> */
	public boolean STCommand_STTherMode_auto, STCommand_STTherMode_cool, STCommand_STTherMode_emergencyHeat, STCommand_STTherMode_heat, STCommand_STTherMode_off;


	public STState currentState(int attributeName){
		return thermostatModeState;
	}

	public int currentValue(int attributeName){
		return currentThermostatMode;
	}

	public STState latestState(int attributeName){
		return thermostatModeState;
	}

	public int latestValue(int attributeName){
		return currentThermostatMode;
	}

	public void auto(){
		STCommand_STTherMode_auto = true;
	}
	public void auto(int delay){
		STCommand_STTherMode_auto = true;
	}
	public void cool(){
		STCommand_STTherMode_cool = true;
	}
	public void cool(int delay){
		STCommand_STTherMode_cool = true;
	}
	public void emergencyHeat(){
		STCommand_STTherMode_emergencyHeat = true;
	}
	public void emergencyHeat(int delay){
		STCommand_STTherMode_emergencyHeat = true;
	}
	public void heat(){
		STCommand_STTherMode_heat = true;
	}
	public void heat(int delay){
		STCommand_STTherMode_heat = true;
	}
	public void off(){
		STCommand_STTherMode_off = true;
	}
	public void off(int delay){
		STCommand_STTherMode_off = true;
	}
	public void setThermostatMode(int mode){
		if(mode == 25)
		{
			STCommand_STTherMode_auto = true; /* AUTO 25 */
		}
		else if(mode == 31)
		{
			STCommand_STTherMode_cool = true; /* COOL 31 */
		}
		else if(mode == 32)
		{
			STCommand_STTherMode_emergencyHeat = true; /* EMERGENCY_HEAT 32 */
		}
		else if(mode == 33)
		{
			STCommand_STTherMode_heat = true; /* HEAT 33 */
		}
		else if(mode == 16)
		{
			STCommand_STTherMode_off = true; /*  OFF 16 */
		}
	}
}

public class STTherHeatSetpoint extends STDevice /* capability.thermostatHeatingSetpoint */
{
	public STState heatingSetpointState; /* <attribute name>State */
	public int currentHeatingSetpoint; /* current<Uppercase attribute name> */
	public boolean STCommand_STTherHeatSetpoint_setHeatingSetpoint;

	public STState currentState(int attributeName){
		return heatingSetpointState;
	}
	public int currentValue(int attributeName){
		return currentHeatingSetpoint;
	}

	public STState latestState(int attributeName){
		return heatingSetpointState;
	}
	public int latestValue(int attributeName){
		return currentHeatingSetpoint;
	}

	public void setHeatingSetpoint(int setpoint){
		currentHeatingSetpoint = setpoint;
		STCommand_STTherHeatSetpoint_setHeatingSetpoint = true;
	}
	public void setHeatingSetpoint(int setpoint, int delay){
		currentHeatingSetpoint = setpoint;
		STCommand_STTherHeatSetpoint_setHeatingSetpoint = true;
	}
}

public class STTherFanMode extends STDevice /* capability.thermostatFanMode */
{
	public STState thermostatFanModeState; /* <attribute name>State */
	public int currentThermostatFanMode; /* current<Uppercase attribute name> */
	public boolean STCommand_STTherFanMode_fanAuto, STCommand_STTherFanMode_fanCirculate, STCommand_STTherFanMode_fanOn;


	public STState currentState(int attributeName){
		return thermostatFanModeState;
	}

	public int currentValue(int attributeName){
		return currentThermostatFanMode;
	}

	public STState latestState(int attributeName){
		return thermostatFanModeState;
	}

	public int latestValue(int attributeName){
		return currentThermostatFanMode;
	}

	public void fanAuto(){
		STCommand_STTherFanMode_fanAuto = true;
	}
	public void fanAuto(int delay){
		STCommand_STTherFanMode_fanAuto = true;
	}
	public void fanCirculate(){
		STCommand_STTherFanMode_fanCirculate = true;
	}
	public void fanCirculate(int delay){
		STCommand_STTherFanMode_fanCirculate = true;
	}
	public void fanOn(){
		STCommand_STTherFanMode_fanOn = true;
	}
	public void fanOn(int delay){
		STCommand_STTherFanMode_fanOn = true;
	}
	public void setThermostatFanMode(int mode){
		if(mode == 25)
		{
			STCommand_STTherFanMode_fanAuto = true; /* AUTO 25 */
		}
		else if(mode == 26)
		{
			STCommand_STTherFanMode_fanCirculate = true; /* CIRCULATE 26 */
		}
		else if(mode == 15)
		{
			STCommand_STTherFanMode_fanOn = true; /* ON 15 */
		}
	}
}

public class STTherCoSetpoint extends STDevice /* capability.thermostatCoolingSetpoint */
{
	public STState coolingSetpointState; /* <attribute name>State */
	public int currentCoolingSetpoint; /* current<Uppercase attribute name> */
	public boolean STCommand_STTherCoSetpoint_setCoolingSetpoint;


	public STState currentState(int attributeName){
		return coolingSetpointState;
	}
	public int currentValue(int attributeName){
		return currentCoolingSetpoint;
	}

	public STState latestState(int attributeName){
		return coolingSetpointState;
	}
	public int latestValue(int attributeName){
		return currentCoolingSetpoint;
	}

	public void setCoolingSetpoint(int setpoint){
		currentCoolingSetpoint = setpoint;
		STCommand_STTherCoSetpoint_setCoolingSetpoint = true;
	}
	public void setCoolingSetpoint(int setpoint, int delay){
		currentCoolingSetpoint = setpoint;
		STCommand_STTherCoSetpoint_setCoolingSetpoint = true;
	}
}

public class STThermostat extends STDevice /* capability.thermostat */
{
	public STState thermostatModeState; /* <attribute name>State */
	public int currentThermostatMode; /* current<Uppercase attribute name> */
	public STState temperatureState; /* <attribute name>State */
	public int currentTemperature; /* current<Uppercase attribute name> */
	public STState coolingSetpointState; /* <attribute name>State */
	public int currentCoolingSetpoint; /* current<Uppercase attribute name> */
	public STState heatingSetpointState; /* <attribute name>State */
	public int currentHeatingSetpoint; /* current<Uppercase attribute name> */
	public STState thermostatSetpointState; /* <attribute name>State */
	public int currentThermostatSetpoint; /* current<Uppercase attribute name> */
	public STState thermostatFanModeState; /* <attribute name>State */
	public int currentThermostatFanMode; /* current<Uppercase attribute name> */
	public STState thermostatOperatingStateState; /* <attribute name>State */
	public int currentThermostatOperatingState; /* current<Uppercase attribute name> */
	public boolean STCommand_STThermostat_fanAuto, STCommand_STThermostat_fanCirculate, STCommand_STThermostat_fanOn,
	STCommand_STThermostat_setHeatingSetpoint, STCommand_STThermostat_setCoolingSetpoint;
	public boolean STCommand_STThermostat_auto, STCommand_STThermostat_cool, STCommand_STThermostat_emergencyHeat, STCommand_STThermostat_heat, STCommand_STThermostat_off;

	public STState currentState(int attributeName){
		if(attributeName == 20) /* TEMPERATURE */
		{
			return temperatureState;
		}
		else if(attributeName == 30) /* THERMOSTATMODE */
		{
			return thermostatModeState;
		}
		else if(attributeName == 21) /* COOLINGSETPOINT */
		{
			return coolingSetpointState;
		}
		else if(attributeName == 27) /* HEATINGSETPOINT */
		{
			return heatingSetpointState;
		}
		else if(attributeName == 42) /* THERMOSTATSETPOINT */
		{
			return thermostatSetpointState;
		}
		else if(attributeName == 24) /* THERMOSTATFANMODE */
		{
			return thermostatFanModeState;
		}
		else if(attributeName == 34) /* THERMOSTATOPERATINGSTATE */
		{
			return thermostatOperatingStateState;
		}
		return coolingSetpointState;
	}

	public int currentValue(int attributeName){
		if(attributeName == 20) /* TEMPERATURE */
		{
			return currentTemperature;
		}
		else if(attributeName == 30) /* THERMOSTATMODE */
		{
			return currentThermostatMode;
		}
		else if(attributeName == 21) /* COOLINGSETPOINT */
		{
			return currentCoolingSetpoint;
		}
		else if(attributeName == 27) /* HEATINGSETPOINT */
		{
			return currentHeatingSetpoint;
		}
		else if(attributeName == 42) /* THERMOSTATSETPOINT */
		{
			return currentThermostatSetpoint;
		}
		else if(attributeName == 24) /* THERMOSTATFANMODE */
		{
			return currentThermostatFanMode;
		}
		else if(attributeName == 34) /* THERMOSTATOPERATINGSTATE */
		{
			return currentThermostatOperatingState;
		}
		return currentThermostatMode;
	}

	public STState latestState(int attributeName){
		if(attributeName == 20) /* TEMPERATURE */
		{
			return temperatureState;
		}
		else if(attributeName == 30) /* THERMOSTATMODE */
		{
			return thermostatModeState;
		}
		else if(attributeName == 21) /* COOLINGSETPOINT */
		{
			return coolingSetpointState;
		}
		else if(attributeName == 27) /* HEATINGSETPOINT */
		{
			return heatingSetpointState;
		}
		else if(attributeName == 42) /* THERMOSTATSETPOINT */
		{
			return thermostatSetpointState;
		}
		else if(attributeName == 24) /* THERMOSTATFANMODE */
		{
			return thermostatFanModeState;
		}
		else if(attributeName == 34) /* THERMOSTATOPERATINGSTATE */
		{
			return thermostatOperatingStateState;
		}
		return coolingSetpointState;
	}

	public int latestValue(int attributeName){
		if(attributeName == 20) /* TEMPERATURE */
		{
			return currentTemperature;
		}
		else if(attributeName == 30) /* THERMOSTATMODE */
		{
			return currentThermostatMode;
		}
		else if(attributeName == 21) /* COOLINGSETPOINT */
		{
			return currentCoolingSetpoint;
		}
		else if(attributeName == 27) /* HEATINGSETPOINT */
		{
			return currentHeatingSetpoint;
		}
		else if(attributeName == 42) /* THERMOSTATSETPOINT */
		{
			return currentThermostatSetpoint;
		}
		else if(attributeName == 24) /* THERMOSTATFANMODE */
		{
			return currentThermostatFanMode;
		}
		else if(attributeName == 34) /* THERMOSTATOPERATINGSTATE */
		{
			return currentThermostatOperatingState;
		}
		return currentThermostatMode;
	}
	
	public void auto(){
		STCommand_STThermostat_auto = true;
	}
	public void auto(int delay){
		STCommand_STThermostat_auto = true;
	}
	public void cool(){
		STCommand_STThermostat_cool = true;
	}
	public void cool(int delay){
		STCommand_STThermostat_cool = true;
	}
	public void emergencyHeat(){
		STCommand_STThermostat_emergencyHeat = true;
	}
	public void emergencyHeat(int delay){
		STCommand_STThermostat_emergencyHeat = true;
	}
	public void heat(){
		STCommand_STThermostat_heat = true;
	}
	public void heat(int delay){
		STCommand_STThermostat_heat = true;
	}
	public void off(){
		STCommand_STThermostat_off = true;
	}
	public void off(int delay){
		STCommand_STThermostat_off = true;
	}
	public void setThermostatMode(int mode){
		if(mode == 25)
		{
			STCommand_STThermostat_auto = true; /* AUTO 25 */
		}
		else if(mode == 31)
		{
			STCommand_STThermostat_cool = true; /* COOL 31 */
		}
		else if(mode == 32)
		{
			STCommand_STThermostat_emergencyHeat = true; /* EMERGENCY_HEAT 32 */
		}
		else if(mode == 33)
		{
			STCommand_STThermostat_heat = true; /* HEAT 33 */
		}
		else if(mode == 16)
		{
			STCommand_STThermostat_off = true; /*  OFF 16 */
		}
	}

	public void fanAuto(){
		STCommand_STThermostat_fanAuto = true;
	}
	public void fanAuto(int delay){
		STCommand_STThermostat_fanAuto = true;
	}
	public void fanCirculate(){
		STCommand_STThermostat_fanCirculate = true;
	}
	public void fanCirculate(int delay){
		STCommand_STThermostat_fanCirculate = true;
	}
	public void fanOn(){
		STCommand_STThermostat_fanOn = true;
	}
	public void fanOn(int delay){
		STCommand_STThermostat_fanOn = true;
	}
	public void setHeatingSetpoint(int setpoint){
		currentHeatingSetpoint = setpoint;
		STCommand_STThermostat_setHeatingSetpoint = true;
	}
	public void setHeatingSetpoint(int setpoint, int delay){
		currentHeatingSetpoint = setpoint;
		STCommand_STThermostat_setHeatingSetpoint = true;
	}
	public void setCoolingSetpoint(int setpoint){
		currentCoolingSetpoint = setpoint;
		STCommand_STThermostat_setCoolingSetpoint = true;
	}
	public void setCoolingSetpoint(int setpoint, int delay){
		currentCoolingSetpoint = setpoint;
		STCommand_STThermostat_setCoolingSetpoint = true;
	}
	public void setThermostatFanMode(int mode){
		if(mode == 25)
		{
			STCommand_STThermostat_fanAuto = true; /* AUTO 25 */
		}
		else if(mode == 26)
		{
			STCommand_STThermostat_fanCirculate = true; /* CIRCULATE 26 */
		}
		else if(mode == 15)
		{
			STCommand_STThermostat_fanOn = true; /* ON 15 */
		}
	}
}

public class STPowerMeter extends STDevice /* capability.powerMeter */
{
	public STState powerState; /* <attribute name>State */
	public int currentPower; /* current<Uppercase attribute name> */
	
	public STState currentState(int attributeName){
		return powerState;
	}

	public int currentValue(int attributeName){
		return currentPower;
	}

	public STState latestState(int attributeName){
		return powerState;
	}

	public int latestValue(int attributeName){
		return currentPower;
	}
}

public class STIlMeas extends STDevice /* capability.illuminanceMeasurement */
{
	public STState illuminanceState; /* <attribute name>State */
	public int currentIlluminance; /* current<Uppercase attribute name> */
	
	public STState currentState(int attributeName){
		return illuminanceState;
	}

	public int currentValue(int attributeName){
		return currentIlluminance;
	}

	public STState latestState(int attributeName){
		return illuminanceState;
	}

	public int latestValue(int attributeName){
		return currentIlluminance;
	}
}

public class STWaterSensor extends STDevice /* capability.waterSensor */
{
	public STState waterState; /* <attribute name>State */
	public int currentWater; /* current<Uppercase attribute name> */
	
	public STState currentState(int attributeName){
		return waterState;
	}

	public int currentValue(int attributeName){
		return currentWater;
	}

	public STState latestState(int attributeName){
		return waterState;
	}

	public int latestValue(int attributeName){
		return currentWater;
	}
}

public class STValve extends STDevice /* capability.valve */
{
	public STState valveState; /* <attribute name>State */
	public int currentValve; /* current<Uppercase attribute name> */
	public boolean STCommand_STValve_open, STCommand_STValve_close;

	public STState currentState(int attributeName){
		return valveState;
	}

	public int currentValue(int attributeName){
		return currentValve;
	}

	public STState latestState(int attributeName){
		return valveState;
	}

	public int latestValue(int attributeName){
		return currentValve;
	}

	public void close(){
		STCommand_STValve_close = true;
	}
	public void close(int delay){
		STCommand_STValve_close = true;
	}
	public void open(){
		STCommand_STValve_open = true;
	}
	public void open(int delay){
		STCommand_STValve_open = true;
	}
}

public class STPetFeederShield extends STDevice /* capability.PetFeederShield */
{
	public STState petFeederShieldState; /* <attribute name>State */
	public int currentPetFeederShield; /* current<Uppercase attribute name> */
	
	public STState currentState(int attributeName){
		return petFeederShieldState;
	}

	public int currentValue(int attributeName){
		return currentPetFeederShield;
	}

	public STState latestState(int attributeName){
		return petFeederShieldState;
	}

	public int latestValue(int attributeName){
		return currentPetFeederShield;
	}
	public void feed(){}
	public void feed(Map map){}
}

public class STThreeAxis extends STDevice /* capability.threeAxis */
{
	public STState threeAxisState; /* <attribute name>State */
	public STxyz currentThreeAxis; /* current<Uppercase attribute name> */
	
	public STState currentState(int attributeName){
		return threeAxisState;
	}

	public int currentValue(int attributeName){
		return currentThreeAxis;
	}

	public STState latestState(int attributeName){
		return threeAxisState;
	}

	public int latestValue(int attributeName){
		return currentThreeAxis;
	}
}

public class STAccSensor extends STDevice /* capability.accelerationSensor */
{
	public STState accelerationState; /* <attribute name>State */
	public int currentAcceleration; /* current<Uppercase attribute name> */

	public STState currentState(int attributeName){
		return accelerationState;
	}

	public int currentValue(int attributeName){
		return currentAcceleration;
	}

	public STState latestState(int attributeName){
		return accelerationState;
	}

	public int latestValue(int attributeName){
		return currentAcceleration;
	}
}

public class STSmartWethStationTile extends STDevice /* capability.smartweatherStationTile */
{
	public STState smartweatherStationTileState; /* <attribute name>State */
	public int currentSmartweatherStationTile; /* current<Uppercase attribute name> */
	
	public STState currentState(int attributeName){
		return smartweatherStationTileState;
	}

	public int currentValue(int attributeName){
		return currentSmartweatherStationTile;
	}

	public STState latestState(int attributeName){
		return smartweatherStationTileState;
	}

	public int latestValue(int attributeName){
		return currentSmartweatherStationTile;
	}
}

public class STSwitchLevel extends STDevice /* capability.switchLevel */
{
	public STState switchState; /* <attribute name>State */
	public int currentSwitch; /* current<Uppercase attribute name> */
	public STState levelState;
	public int currentLevel;
	public boolean STCommand_STSwitchLevel_on, STCommand_STSwitchLevel_off, STCommand_STSwitchLevel_setLevel;

	public STState currentState(int attributeName){
		switch(attributeName)
		{
		case 14: return switchState; /* switch */
		case 74: return levelState; /* level */
		default: return switchState;
		}
	}

	public int currentValue(int attributeName){
		switch(attributeName)
		{
		case 14: return currentSwitch; /* switch */
		case 74: return currentLevel; /* level */
		default: return currentSwitch;
		}
	}

	public STState latestState(int attributeName){
		switch(attributeName)
		{
		case 14: return switchState; /* switch */
		case 74: return levelState; /* level */
		default: return switchState;
		}
	}

	public int latestValue(int attributeName){
		switch(attributeName)
		{
		case 14: return currentSwitch; /* switch */
		case 74: return currentLevel; /* level */
		default: return currentSwitch;
		}
	}

	public void on(){
		STCommand_STSwitchLevel_on = true;
	}
	public void on(int delay){
		STCommand_STSwitchLevel_on = true;
	}
	public void off(){
		STCommand_STSwitchLevel_off = true;
	}
	public void off(int delay){
		STCommand_STSwitchLevel_off = true;
	}
	public void setLevel(int level){
		currentLevel = level;
		STCommand_STSwitchLevel_setLevel = true;
	}
	public void setLevel(int level, int delay){
		currentLevel = level;
		STCommand_STSwitchLevel_setLevel = true;
	}
}

public class STAeonKeyFob extends STDevice /* device.AeonKeyFob */
{
	public STState buttonState; /* <attribute name>State */
	public int currentButton; /* current<Uppercase attribute name> */
	
	public STState currentState(int attributeName){
		return buttonState;
	}

	public int currentValue(int attributeName){
		return currentButton;
	}

	public STState latestState(int attributeName){
		return buttonState;
	}

	public int latestValue(int attributeName){
		return currentButton;
	}
}

/*public class STColorInfo
{
	public int _switch;
	public int level;
	public int hue;
	public int saturation;
}*/
public class STColorCtrl extends STDevice /* capability.colorControl */
{
	public STState colorState; /* <attribute name>State */
	public int currentColor; /* current<Uppercase attribute name> */
	public STState hueState; /* <attribute name>State */
	public int currentHue; /* current<Uppercase attribute name> */
	public STState saturationState; /* <attribute name>State */
	public int currentSaturation; /* current<Uppercase attribute name> */
	public STState switchState; /* <attribute name>State */
	public int currentSwitch; /* current<Uppercase attribute name> */
	public STState levelState;
	public int currentLevel;
	public boolean STCommand_STColorCtrl_on, STCommand_STColorCtrl_off, STCommand_STColorCtrl_setLevel,
		STCommand_STColorCtrl_setHue, STCommand_STColorCtrl_setSaturation, STCommand_STColorCtrl_setColor;
	
	public int currentValue(int attributeName){
		switch(attributeName)
		{
		case 14: return currentSwitch; /* switch */
		case 74: return currentLevel; /* level */
		case 83: return currentColor; /* color */
		case 84: return currentHue; /* hue */
		case 85: return currentSaturation; /* saturation */
		default: return currentSwitch;
		}
	}
	public int latestValue(int attributeName){
		switch(attributeName)
		{
		case 14: return currentSwitch; /* switch */
		case 74: return currentLevel; /* level */
		case 83: return currentColor; /* color */
		case 84: return currentHue; /* hue */
		case 85: return currentSaturation; /* saturation */
		default: return currentSwitch;
		}
	}
	public STState currentState(int attributeName){
		switch(attributeName)
		{
		case 14: return switchState; /* switch */
		case 74: return levelState; /* level */
		case 83: return colorState; /* color */
		case 84: return hueState; /* hue */
		case 85: return saturationState; /* saturation */
		default: return switchState;
		}
	}
	public STState latestState(int attributeName){
		switch(attributeName)
		{
		case 14: return switchState; /* switch */
		case 74: return levelState; /* level */
		case 83: return colorState; /* color */
		case 84: return hueState; /* hue */
		case 85: return saturationState; /* saturation */
		default: return switchState;
		}
	}
	
	public void on(){
		STCommand_STColorCtrl_on = true;
	}
	public void on(int delay){
		STCommand_STColorCtrl_on = true;
	}
	public void off(){
		STCommand_STColorCtrl_off = true;
	}
	public void off(int delay){
		STCommand_STColorCtrl_off = true;
	}
	public void setLevel(int level){
		currentLevel = level;
		STCommand_STColorCtrl_setLevel = true;
	}
	public void setLevel(int level, int delay){
		currentLevel = level;
		STCommand_STColorCtrl_setLevel = true;
	}
	public void setHue(int hue){
		currentHue = hue;
		STCommand_STColorCtrl_setHue = true;
	}
	public void setSaturation(int saturation){
		currentSaturation = saturation;
		STCommand_STColorCtrl_setSaturation = true;
	}
	public void setColor(int color){
		/* color = currentSwitch (1 byte) |  currentLevel | currentHue | currentSaturation
		 * */
		currentSwitch = color>>24;
		currentLevel = color>>16;
		currentHue = color>>8;
		currentSaturation = color & 0x000F;
		currentColor = color;
		STCommand_STColorCtrl_setColor = true;
	}
}

public class STTone extends STDevice /* capability.tone */
{
	public void beep(){}
	public void beep(double delay){}
}

public class STImageCapture extends STDevice /* capability.imageCapture */
{
	public STState imageState; /* <attribute name>State */
	public int currentImage; /* current<Uppercase attribute name> */
	
	public int currentValue(int attributeName){
		return currentImage;
	}
	public int latestValue(int attributeName){
		return currentImage;
	}
	public STState currentState(int attributeName){
		return imageState;
	}
	public STState latestState(int attributeName){
		return imageState;
	}
	
	public void take(){}
	public void take(int delay){}
	public void alarmOn(){}
	public void alarmOn(int delay){}
	public void alarmOff(){}
	public void alarmOff(int delay){}
	public void ledOn(){}
	public void ledOn(int delay){}
	public void ledOff(){}
	public void ledOff(int delay){}
	public void ledAuto(){}
	public void ledAuto(int delay){}
}

public class STRelHumMeas extends STDevice /* capability.relativeHumidityMeasurement */
{
	public STState humidityState; /* <attribute name>State */
	public int currentHumidity; /* current<Uppercase attribute name> */
	
	public int currentValue(String attributeName){
		return currentHumidity;
	}
	public int latestValue(String attributeName){
		return currentHumidity;
	}
	public STState currentState(int attributeName){
		return humidityState;
	}
	public STState latestState(int attributeName){
		return humidityState;
	}
}

public class STApp
{
	//	public int doubleValue;
	//	public int floatValue;
	public int name;
	public int value;
	//	public boolean isStateChange;
	//	public boolean isDigital;
	//	public boolean isPhysical;
	//	public STDevice device;
	//	public int deviceId;
	//	public int hubId;
	//	public int integerValue;
	//	public int jsonValue;
	//	public STLocation location;
	//	public int locationId;
	//	public int source;
	//	public int stringValue;
	//	public int numberValue;
	//	public int numericValue;
	//	public int data;
	public int date;
	//	public int dateValue;
	//	public int description;
	//	public int descriptionText;
	//	public int displayName;
	public int id;
	//	public int installedSmartAppId;
	//	public int isoDate;
	//	public int unit;
	public int label;

	public int getData(){
		return value;
	}
	public int getDate(){
		return date;
	}
	public int getDateValue(){
		return date;
	}
	//	public int getDescription(){
	//		return description;
	//	}
	//	public int getDescriptionText(){
	//		return descriptionText;
	//	}
	//	public STDevice getDevice(){
	//		return device;
	//	}
	//	public int getDisplayName(){
	//		return displayName;
	//	}
	//	public int getDeviceId(){
	//		return deviceId;
	//	}
	public int getId(){
		return id;
	}
	public int getDoubleValue(){
		return value;
	}
	public int getFloatValue(){
		return value;
	}
	//	public int getHubId(){
	//		return hubId;
	//	}
	//	public int getInstalledSmartAppId(){
	//		return installedSmartAppId;
	//	}
	public int getIntegerValue(){
		return value;
	}
	//	public int getIsoDate(){
	//		return isoDate;
	//	}
	//	public int getJsonValue(){
	//		return jsonValue;
	//	}
	//	public STLocation getLocation(){
	//		return location;
	//	}
	//	public int getLocationId(){
	//		return locationId;
	//	}
	public int getName(){
		return name;
	}
	public int getNumberValue(){
		return value;
	}
	public int getNumericValue(){
		return value;
	}
	//	public int getSource(){
	//		return source;
	//	}
	public int getStringValue(){
		return value;
	}
	//	public int getUnit(){
	//		return unit;
	//	}
	public int getValue(){
		return value;
	}
	//	public Map<int, int> getXyzValue(){}
	//	public boolean isDigital(){
	//		return isDigital;
	//	}
	//	public boolean physical(){
	//		return isPhysical;
	//	}
	//	public boolean stateChange(){
	//		return isStateChange;
	//	}
}

public class STMomentary extends STDevice /* capability.momentary */
{
	public STState momentaryState; /* <attribute name>State */
	public int currentMomentary; /* current<Uppercase attribute name> */
	
	public int currentValue(String attributeName){}
	public int latestValue(String attributeName){}
	
	public void push(){}
	public void push(int delay){}
}

public class STNetworkManager
{
	public int configuredPhoneNumber;
	public int receivedPhoneNumber;
	public boolean STCommand_sendSms;
	public boolean STCommand_httpPost;
	public boolean STCommand_unsubscribe;
}

public class STCommonAPIs
{
	/* System variable */
	static STLocation location;
	static int STCurrentSystemTime;
	static STNetworkManager _STNetworkManager;

	/* System methods */
	static int now()
	{
		return STCurrentSystemTime*3600000; /* 60*60*1000 */
	}
	static void increaseSTSystemTime(int amount) /* amount in seconds */
	{
		int h = amount/3600 + 1;
		STCurrentSystemTime += h;
	}
	static int timeTodayAfter(int[] startTimeString, int timeString, int timeZone)
	{
		return timeString*3600000;
	}
	static int timeTodayAfter(int[] startTimeString, int[] timeString)
	{
		return timeString*3600000;
	}
	static int timeTodayAfter(int startTime, int timeVal, int timeZone)
	{
		return timeVal*3600000;
	}
	static int timeTodayAfter(int startTime, int timeVal)
	{
		return timeVal*3600000;
	}
	static int timeToday(int startTime, int timeZone)
	{
		return startTime;
	}
	static int timeToday(int startTime)
	{
		return startTime;
	}
	static void setLocationMode(int mode){
		if(mode == 1400) /* HOME */
		{
			location.STCommand_Location_Home = true;
		}
		else if(mode == 1401) /* AWAY */
		{
			location.STCommand_Location_Away = true;
		}
		else if(mode == 1402) /* NIGHT */
		{
			location.STCommand_Location_Night = true;
		}
	}
	static void setLocationMode(STMode mode){
		if(mode.name == 1400) /* HOME */
		{
			location.STCommand_Location_Home = true;
		}
		else if(mode.name == 1401) /* AWAY */
		{
			location.STCommand_Location_Away = true;
		}
		else if(mode.name == 1402) /* NIGHT */
		{
			location.STCommand_Location_Night = true;
		}
	}
	STSunriseSunset getSunriseAndSunset(){
		return location.sunriseSunset;
	}
	public STWeatherFeature getWeatherFeature(int feature){
		return location.weatherFeature;
	}
	public STWeatherFeature getWeatherFeature(int feature, int zipcode){
		return location.weatherFeature;
	}
	static void sendNotificationToContacts(int msg, int[] recipients){}
	static void sendPush(int msg){}
	static void sendSms(int phoneNumb, int msg)
	{
		_STNetworkManager.receivedPhoneNumber = phoneNumb;
		_STNetworkManager.STCommand_sendSms = true;
	}
	static void httpPost()
	{
		_STNetworkManager.STCommand_httpPost = true;
	}
	static void unsubscribe()
	{
		_STNetworkManager.STCommand_unsubscribe = true;
	}
}

/* Map's depth = 1 */
public class CInt2IntMap
{
	private int size;
	private int[] keyArr;
	private int[] valueArr;
	private int gArrIndex;
	public int keyToPut;
	public int valueToPut;
	public boolean STCommand_CInt2IntMap_put;

//	public CInt2IntMap()
//	{
//		size = 0;
//		gArrIndex = 10;
//		keyArr = new int[10];
//		valueArr = new int[10];
//	}

	public int get(int key)
	{
		int index = 10;

		for(int i = 0; i < size; i++)
		{
			if(keyArr[i] == key)
			{
				index = i;
				break;
			}
		}
		if(index == 10)
		{
			return 0;
		}
		return valueArr[index];
	}

	public void put(int key, int value)
	{
//		int index = 0;
//
//		for(int i = 0; i < size; i++)
//		{
//			if(keyArr[i] == key)
//			{
//				index = i;
//				break;
//			}
//		}
//
//		if(index == 0)
//		{
//			index = size;
//			size++;
//
//			if(size == 10)
//			{
//				size--;
//			}
//		}
//
//		keyArr[index] = key;
//		valueArr[index] = value;
		keyToPut = key;
		valueToPut = value;
		STCommand_CInt2IntMap_put = true;
	}

	public void put(int key, boolean value)
	{
//		int index = 0;
//
//		for(int i = 0; i < size; i++)
//		{
//			if(keyArr[i] == key)
//			{
//				index = i;
//				break;
//			}
//		}
//
//		if(index == 0)
//		{
//			index = size;
//			size++;
//
//			if(size == 10)
//			{
//				size--;
//			}
//		}
//
//		keyArr[index] = key;
//		valueArr[index] = value ? 1:0;
		keyToPut = key;
		valueToPut = value;
		STCommand_CInt2IntMap_put = true;
	}
}

/* Map's depth = 2 */
public class CInt2IIMMap
{
	private int size;
	private int[] keyArr;
	private CInt2IntMap[] valueArr;
	private int gArrIndex;
	public int keyToPut;
	public CInt2IntMap valueToPut;
	public boolean STCommand_CInt2IIMMap_put;

//	public CInt2IIMMap()
//	{
//		size = 0;
//		keyArr = new int[10];
//		valueArr = new CInt2IntMap[10];
//		gArrIndex = 10;
//		valueArr[0] = new CInt2IntMap();
//	}

	public CInt2IntMap get(int key)
	{
		int index = 10;

		for(int i = 1; i <= size; i++)
		{
			if(keyArr[i] == key)
			{
				index = i;
				break;
			}
		}
		if(index == 10)
		{
//			valueArr[0].size = 0;
//			valueArr[0].gArrIndex = 100;
			index = 0;
		}
		return valueArr[index];
	}

	public void put(int key, CInt2IntMap value)
	{
//		int index = 0;
//
//		for(int i = 0; i < size; i++)
//		{
//			if(keyArr[i] == key)
//			{
//				index = i;
//				break;
//			}
//		}
//
//		if(index == 0)
//		{
//			index = size;
//			size++;
//
//			if(size == 10)
//			{
//				size--;
//			}
//		}
//
//		keyArr[index] = key;
//		valueArr[index] = value;
		keyToPut = key;
		valueToPut = value;
		STCommand_CInt2IIMMap_put = true;
	}
}

/* Map's depth = 3 */
public class CInt2IIIMMap
{
	private int size;
	private int[] keyArr;
	private CInt2IIMMap[] valueArr;
	private int gArrIndex;
	public int keyToPut;
	public CInt2IIMMap valueToPut;
	public boolean STCommand_CInt2IIIMMap_put;

//	public CInt2IIIMMap()
//	{
//		size = 0;
//		gArrIndex = 10;
//		keyArr = new int[10];
//		valueArr = new CInt2IIMMap[10];
//	}

	public CInt2IIMMap get(int key)
	{
		int index = 10;

		for(int i = 1; i <= size; i++)
		{
			if(keyArr[i] == key)
			{
				index = i;
				break;
			}
		}
		if(index == 10)
		{
			index = 0;
		}
		return valueArr[index];
	}

	public void put(int key, CInt2IIMMap value)
	{
//		int index = 0;
//
//		for(int i = 0; i < size; i++)
//		{
//			if(keyArr[i] == key)
//			{
//				index = i;
//				break;
//			}
//		}
//
//		if(index == 0)
//		{
//			index = size;
//			size++;
//
//			if(size == 10)
//			{
//				size--;
//			}
//		}
//
//		keyArr[index] = key;
//		valueArr[index] = value;
		keyToPut = key;
		valueToPut = value;
		STCommand_CInt2IIIMMap_put = true;
	}
}

public class STInitializer
{
	/* Initialized record variables */
	static STMode STInitializer_record_1;
	static STState STInitializer_record_2;
	static STHub STInitializer_record_3;
	static STEvent STInitializer_record_4;
	static STAttribute STInitializer_record_5;
	static STCommand STInitializer_record_6;
	static STCapability STInitializer_record_7;
	static STDevice STInitializer_record_8;
	static STLocation STInitializer_record_9;
	static STAlarm STInitializer_record_10;
	static STSwitch STInitializer_record_11;
	static STBulb STInitializer_record_12;
	static STTempMeas STInitializer_record_13;
	static STCarDioMeas STInitializer_record_14;
	static STCarMoDetector STInitializer_record_15;
	static STDoorControl STInitializer_record_16;
	static STLight STInitializer_record_17;
	static STLock STInitializer_record_18;
	static STMotionSensor STInitializer_record_19;
	static STPresSensor STInitializer_record_20;
	static STOutlet STInitializer_record_21;
	static STSmokeDetector STInitializer_record_22;
	static STTherOpState STInitializer_record_23;
	static STTherSetpoint STInitializer_record_24;
	static STTherMode STInitializer_record_25;
	static STTherHeatSetpoint STInitializer_record_26;
	static STTherFanMode STInitializer_record_27;
	static STTherCoSetpoint STInitializer_record_28;
	static STThermostat STInitializer_record_29;
	static STNetworkManager STInitializer_record_30;
	static STContactSensor STInitializer_record_31;
	static CInt2IntMap STInitializer_record_32;
	static CInt2IIMMap STInitializer_record_33;
	static CInt2IIIMMap STInitializer_record_34;
	static STPowerMeter STInitializer_record_35;
	static STApp STInitializer_record_36;
	static STIlMeas STInitializer_record_37;
	static STWaterSensor STInitializer_record_38;
	static STAccSensor STInitializer_record_39;
	static STSunriseSunset STInitializer_record_40;
	static STWeatherFeature STInitializer_record_41;
	static STSwitchLevel STInitializer_record_42;
	static STValve STInitializer_record_43;
	static STAeonKeyFob STInitializer_record_44;
	static STThreeAxis STInitializer_record_45;
	static STRelHumMeas STInitializer_record_46;
	
	/* Initialized array variables */
	static int[] STInitializer_arr_0;
	static STMode[] STInitializer_arr_1;
	static STState[] STInitializer_arr_2;
	static STHub[] STInitializer_arr_3;
	static STLocation[] STInitializer_arr_4;
	static STAttribute[] STInitializer_arr_5;
	static STCommand[] STInitializer_arr_6;
	static STCapability[] STInitializer_arr_7;
	static STDevice[] STInitializer_arr_8;
	static STEvent[] STInitializer_arr_9;
	static STAlarm[] STInitializer_arr_10;
	static STSwitch[] STInitializer_arr_11;
	static STBulb[] STInitializer_arr_12;
	static STTempMeas[] STInitializer_arr_13;
	static STCarDioMeas[] STInitializer_arr_14;
	static STCarMoDetector[] STInitializer_arr_15;
	static STDoorControl[] STInitializer_arr_16;
	static STLight[] STInitializer_arr_17;
	static STLock[] STInitializer_arr_18;
	static STMotionSensor[] STInitializer_arr_19;
	static STPresSensor[] STInitializer_arr_20;
	static STOutlet[] STInitializer_arr_21;
	static STSmokeDetector[] STInitializer_arr_22;
	static STTherOpState[] STInitializer_arr_23;
	static STTherSetpoint[] STInitializer_arr_24;
	static STTherMode[] STInitializer_arr_25;
	static STTherHeatSetpoint[] STInitializer_arr_26;
	static STTherFanMode[] STInitializer_arr_27;
	static STTherCoSetpoint[] STInitializer_arr_28;
	static STThermostat[] STInitializer_arr_29;
	static STNetworkManager[] STInitializer_arr_30;
	static STContactSensor[] STInitializer_arr_31;
	static CInt2IntMap[] STInitializer_arr_32;
	static CInt2IIMMap[] STInitializer_arr_33;
	static CInt2IIIMMap[] STInitializer_arr_34;
	static STPowerMeter[] STInitializer_arr_35;
	static STApp[] STInitializer_arr_36;
	static STIlMeas[] STInitializer_arr_37;
	static STWaterSensor[] STInitializer_arr_38;
	static STAccSensor[] STInitializer_arr_39;
	static STSunriseSunset[] STInitializer_arr_40;
	static STWeatherFeature[] STInitializer_arr_41;
	static STSwitchLevel[] STInitializer_arr_42;
	static STValve[] STInitializer_arr_43;
	static STAeonKeyFob[] STInitializer_arr_44;
	static STThreeAxis[] STInitializer_arr_45;
	static STRelHumMeas[] STInitializer_arr_46;

	public STInitializer()
	{
		/* Initialized record variables */
		STInitializer_record_40 = new STSunriseSunset();
		STInitializer_record_41 = new STWeatherFeature();
		STInitializer_record_1 = new STMode();
		STInitializer_record_2 = new STState();
		STInitializer_record_3 = new STHub();
		STInitializer_record_4 = new STEvent();
		STInitializer_record_5 = new STAttribute();
		STInitializer_record_6 = new STCommand();
		STInitializer_record_7 = new STCapability();
		STInitializer_record_8 = new STDevice();
		STInitializer_record_9 = new STLocation();
		STInitializer_record_10 = new STAlarm();
		STInitializer_record_11 = new STSwitch();
		STInitializer_record_12 = new STBulb();
		STInitializer_record_13 = new STTempMeas();
		STInitializer_record_14 = new STCarDioMeas();
		STInitializer_record_15 = new STCarMoDetector();
		STInitializer_record_16 = new STDoorControl();
		STInitializer_record_17 = new STLight();
		STInitializer_record_18 = new STLock();
		STInitializer_record_19 = new STMotionSensor();
		STInitializer_record_20 = new STPresSensor();
		STInitializer_record_21 = new STOutlet();
		STInitializer_record_22 = new STSmokeDetector();
		STInitializer_record_23 = new STTherOpState();
		STInitializer_record_24 = new STTherSetpoint();
		STInitializer_record_25 = new STTherMode();
		STInitializer_record_26 = new STTherHeatSetpoint();
		STInitializer_record_27 = new STTherFanMode();
		STInitializer_record_28 = new STTherCoSetpoint();
		STInitializer_record_29 = new STThermostat();
		STInitializer_record_30 = new STNetworkManager();
		STInitializer_record_31 = new STContactSensor();
		STInitializer_record_32 = new CInt2IntMap();
		STInitializer_record_33 = new CInt2IIMMap();
		STInitializer_record_34 = new CInt2IIIMMap();
		STInitializer_record_35 = new STPowerMeter();
		STInitializer_record_36 = new STApp();
		STInitializer_record_37 = new STIlMeas();
		STInitializer_record_38 = new STWaterSensor();
		STInitializer_record_39 = new STAccSensor();
		STInitializer_record_42 = new STSwitchLevel();
		STInitializer_record_43 = new STValve();
		STInitializer_record_44 = new STAeonKeyFob();
		STInitializer_record_45 = new STThreeAxis();
		STInitializer_record_46 = new STRelHumMeas();

		/* Initialized array variables */
		STInitializer_arr_40 = new STSunriseSunset[10];
		STInitializer_arr_41 = new STWeatherFeature[10];
		STInitializer_arr_0 = new int[10];
		STInitializer_arr_1 = new STMode[10];
		STInitializer_arr_2 = new STState[10];
		STInitializer_arr_3 = new STHub[10];
		STInitializer_arr_4 = new STLocation[10];
		STInitializer_arr_5 = new STAttribute[10];
		STInitializer_arr_6 = new STCommand[10];
		STInitializer_arr_7 = new STCapability[10];
		STInitializer_arr_8 = new STDevice[10];
		STInitializer_arr_9 = new STEvent[10];
		STInitializer_arr_10 = new STAlarm[10];
		STInitializer_arr_11 = new STSwitch[10];
		STInitializer_arr_12 = new STBulb[10];
		STInitializer_arr_13 = new STTempMeas[10];
		STInitializer_arr_14 = new STCarDioMeas[10];
		STInitializer_arr_15 = new STCarMoDetector[10];
		STInitializer_arr_16 = new STDoorControl[10];
		STInitializer_arr_17 = new STLight[10];
		STInitializer_arr_18 = new STLock[10];
		STInitializer_arr_19 = new STMotionSensor[10];
		STInitializer_arr_20 = new STPresSensor[10];
		STInitializer_arr_21 = new STOutlet[10];
		STInitializer_arr_22 = new STSmokeDetector[10];
		STInitializer_arr_23 = new STTherOpState[10];
		STInitializer_arr_24 = new STTherSetpoint[10];
		STInitializer_arr_25 = new STTherMode[10];
		STInitializer_arr_26 = new STTherHeatSetpoint[10];
		STInitializer_arr_27 = new STTherFanMode[10];
		STInitializer_arr_28 = new STTherCoSetpoint[10];
		STInitializer_arr_29 = new STThermostat[10];
		STInitializer_arr_30 = new STNetworkManager[10];
		STInitializer_arr_31 = new STContactSensor[10];
		STInitializer_arr_32 = new CInt2IntMap[10];
		STInitializer_arr_33 = new CInt2IIMMap[10];
		STInitializer_arr_34 = new CInt2IIIMMap[10];
		STInitializer_arr_35 = new STPowerMeter[10];
		STInitializer_arr_36 = new STApp[10];
		STInitializer_arr_37 = new STIlMeas[10];
		STInitializer_arr_38 = new STWaterSensor[10];
		STInitializer_arr_39 = new STAccSensor[10];
		STInitializer_arr_42 = new STSwitchLevel[10];
		STInitializer_arr_43 = new STValve[10];
		STInitializer_arr_44 = new STAeonKeyFob[10];
		STInitializer_arr_45 = new STThreeAxis[10];
		STInitializer_arr_46 = new STRelHumMeas[10];
	}

	public void STInitializerEvtHandler() {
//		CInt2IntMap map = new CInt2IntMap();
//		map.put(1, 10);
	}
}
/***************************************************************************************************/