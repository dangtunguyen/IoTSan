import java.math.BigDecimal
import java.util.Date
import java.util.List
import java.util.Map
import java.util.TimeZone

public class STHub
{
	public String firmwareVersionString
	public String id
	public String localIP
	public String localSrvPortTCP
	public String name
	public String type
	public String zigbeeEui
	public String zigbeeId
	
	public String getFirmwareVersionString(){}
	public String getId(){}
	public String getLocalIP(){}
	public String getLocalSrvPortTCP(){}
	public String getName(){}
	public String getType(){}
	public String getZigbeeEui(){}
	public String getZigbeeId(){}
}
public class STMode
{
	public String name
	public String id
	
	public String getId(){}
	public String getName(){}
}
public class STState
{
	public double doubleValue
	public double floatValue
	public String name
//	public String value
	public double value
	public String id
	public Integer integerValue
	public Object jsonValue
	public String stringValue
	public double numberValue
	public double numericValue
	public String hubId
	public Date rawDateCreated
	public Date dateCreated
	public Date dateValue
	public Date date
//	public STxyz xyzValue
	
	public Date getDate(){}
	public Date getDateValue(){}
	public String getId(){}
	public double getDoubleValue(){}
	public double getFloatValue(){}
	public String getHubId(){}
	public String getInstalledSmartAppId(){}
	public Integer getIntegerValue(){}
	public String getIsoDate(){}
	public Object getJsonValue(){}
	public String getName(){}
	public double getNumberValue(){}
	public double getNumericValue(){}
	public String getStringValue(){}
	public String getUnit(){}
	public double getValue(){}
//	public Map<String, BigDecimal> getXyzValue(){}
//	public STxyz getXyzValue(){}
}
public class STEvent
{
	public double doubleValue
	public double floatValue
	public String name
	public String value
	public Boolean isStateChange
	public STDevice device
	public String deviceId
	public String hubId
	public Integer integerValue
	public Object jsonValue
	public STLocation location
	public String locationId
	public String source
	public String stringValue
	public double numberValue
	public double numericValue
	public String data
	public Date date
	public Date dateValue
	public String description
	public Boolean physical
	public Boolean type
	
	public String getData(){}
	public Date getDate(){}
	public Date getDateValue(){}
	public String getDescription(){}
	public String getDescriptionText(){}
	public STDevice getDevice(){}
	public String getDisplayName(){}
	public String getDeviceId(){}
	public String getId(){}
	public double getDoubleValue(){}
	public double getFloatValue(){}
	public String getHubId(){}
	public String getInstalledSmartAppId(){}
	public Integer getIntegerValue(){}
	public String getIsoDate(){}
	public Object getJsonValue(){}
	public STLocation getLocation(){}
	public String getLocationId(){}
	public String getName(){}
	public double getNumberValue(){}
	public double getNumericValue(){}
	public String getSource(){}
	public String getStringValue(){}
	public String getUnit(){}
	public String getValue(){}
	public Map<String, BigDecimal> getXyzValue(){}
	public Boolean isDigital(){}
	public Boolean isPhysical(){}
	public Boolean stateChange(){}
}
public class STLocation
{
	public Boolean contactBookEnabled
	public STMode currentMode
	public String id
	public List<STHub> hubs
	public BigDecimal latitude
	public BigDecimal longitude
	public List<STMode> modes
	public String name
	public String temperatureScale
	public TimeZone timeZone
	public String zipCode
	public String mode
	public STSunriseSunset sunriseSunset
	public STWeatherFeature weatherFeature
	
	public Boolean getContactBookEnabled(){}
	public STMode getCurrentMode(){}
	public String getId(){}
	public List<STHub> getHubs(){}
	public BigDecimal getLatitude(){}
	public BigDecimal getLongitude(){}
	public String getMode(){}
	public List<STMode> getModes(){}
	public String getName(){}
	public void setName(String name){}
	public void setMode(String mode){}
	public void setMode(STMode mode){}
	public String getTemperatureScale(){}
	public TimeZone getTimeZone(){}
	public String getZipCode(){}
	public STSunriseSunset getSunriseAndSunset(){}
	public STWeatherFeature getWeatherFeature(String){}
	public STWeatherFeature getWeatherFeature(String feature, String zipcode){}
}
public class STAttribute
{
	public String dataType
	public String name
	public List<String> values
	
	public String getDataType(){}
	public String getName(){}
	public List<String> getValues(){}
}
public class STCommand
{
	public List<String> arguments
	public String name
	
	public List<String> getArguments(){}
	public String getName(){}
}
public class STCapability
{
	public List<STAttribute> attributes
	public List<STCommand> commands
	public String name
	
	public List<STAttribute> getAttributes(){}
	public List<STCommand> getCommands(){}
	public String getName(){}
}
public class STDevice
{
	public List<STCapability> capabilities
	public String displayName
	public STHub hub
	public String name
	public String id
	public String label
	public String lastActivity
	public String manufacturerName
	public String modelName
	public String status
	public List<STAttribute> supportedAttributes
	public List<STCommand> supportedCommands
	public STState batteryState /* <attribute name>State */
	public double currentBattery /* current<Uppercase attribute name> */
	
	public STState currentState(String attributeName){}
//	public Object currentValue(String attributeName){}
	public List<STEvent> events(double N){}
	public List<STEvent> events(){}
	public List<STEvent> eventsBetween(Date startDate, Date endDate, Map map = [:]){}
	public List<STEvent> eventsBetween(Object startDate, Object endDate, Map map = [:]){}
	public List<STEvent> eventsSince(Date startDate, Map map = [:]){}
	public List<STEvent> eventsSince(Object date){}
	public List<STCapability> getCapabilities(){}
	public String getDisplayName(){}
	public STHub getHub(){}
	public String getName(){}
	public String getId(){}
	public String getLabel(){}
	public String getLastActivity(){}
	public String getManufacturerName(){}
	public String getModelName(){}
	public String getStatus(){}
	public List<STAttribute> getSupportedAttributes(){}
	public List<STCommand> getSupportedCommands(){}
	public Boolean hasAttribute(String attributeName){}
	public Boolean hasCapability(String capabilityName){}
	public Boolean hasCommand(String commandName){}
	public STState latestState(String attributeName){}
//	public Object latestValue(String attributeName){}
	public List<STState> statesBetween(String attributeName, Date startDate, Date endDate, Map map = [:]){}
	public List<STState> statesSince(String attributeName, Date startDate, Map map = [:]){}
}
public class STSunriseSunset
{
	public Date sunrise
	public Date sunset
}
public class STWeatherFeature
{
	public String forecast
}

public class STCommonAPIs
{
	double now() {}
	void increaseSTSystemTime(double amount){} /* amount in seconds */
	Date timeTodayAfter(Object startTimeString, Object timeString, Object timeZone){}
	Date timeToday(Object startTime, Object timeZone){}
	Date timeToday(Object startTime){}
	void sendSms(String phoneNumber, String message){}
	void sendSmsMessage(String phoneNumber, String message){}
	void sendNotificationToContacts(String message, String contact){}
	void sendNotificationToContacts(String message, String contact, Map map){}
	void sendNotificationToContacts(String message, Collection contacts){}
	void sendNotificationToContacts(String message, Collection contacts, Map map){}
	void sendPush(String message){}
	void sendPushMessage(String message){}
	void sendNotification(String message){}
	void sendNotificationEvent(String message){}
	void setLocationMode(String mode){}
	void sendHubCommand(Object command){}
	void sendHubCommand(Object command, Object delay){}
	void default_null_method(){}
	STSunriseSunset getSunriseAndSunset(){}
	STWeatherFeature getWeatherFeature(String){}
	STWeatherFeature getWeatherFeature(String feature, String zipcode){}
	void httpPost(){}
	void unsubscribe(){}
	List<STDevice> getChildDevices(){}
	STDevice getChildDevice(Object deviceId){}
}