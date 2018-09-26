package edu.ksu.cis.bandera.jjjc.gparser.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.GenericsType;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.ClosureExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.GStringExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.TupleExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.sc.StaticCompilationMetadataKeys;
import org.codehaus.groovy.transform.stc.StaticTypesMarker;

import edu.ksu.cis.bandera.jjjc.gparser.g2jconverter.GStmtBuilder;
import edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor.GDeviceInputInfo;
import edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor.GInputInfo;
import edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor.GOtherInputInfo;
import edu.ksu.cis.bandera.jjjc.node.*;

public final class GUtil {
	/********************************************/
	public static String currentClassName;
	public static Map<String, ClassNode> varName2TypeMap = new HashMap<String, ClassNode>();
	public static Map<String, ClassNode> methNameTypeMap = new HashMap<String, ClassNode>();
	public static List<String> currentSmartAppEvtHandlerList = new ArrayList<String>();
	public static Set<String> currentSmartAppLocalMethods = new HashSet<String>();
	public static String classifiedSmartAppName = "NewSmartApp";
	/* SmartThings command list */
	public static List<String> stCommandList = Arrays.asList("on", "off", "both", "siren", "strobe",
			"close", "open", "lock", "unlock", "auto", "cool", "emergencyHeat", "heat", "setThermostatMode", "fanAuto",
			"fanCirculate", "fanOn", "setLevel", "COSmoke", "usercodechange", "setHue", "setSaturation", "setColor",
			"setCoolingSetpoint", "setHeatingSetpoint", "setThermostatFanMode", "beep", "take", "alarmOn", "alarmOff",
			"ledOn", "ledOff", "ledAuto", "push", "httpPost");
	/* SmartThings device get method (common for all types of devices) list */
	private static List<String> stDeviceGetMethodList = Arrays.asList("getStatus", "getLastActivity",
			"events", "eventsBetween", "eventsSince");
	/* SmartThings device special get method (common for all types of devices) list */
	private static List<String> stDeviceSpecialGetMethodList = Arrays.asList("currentState", "currentValue", "latestState", "latestValue",
			"statesBetween", "statesSince");
	/* SmartThings location set method list */
	private static List<String> stLocationSetMethodList = Arrays.asList("setLocationMode", "setMode", "execute");
	/* SmartThings location get method list */
	private static List<String> stLocationGetMethodList = Arrays.asList("getCurrentMode", "getMode", "getModes");
	/* SmartThings device property (common for all types of devices) list */
	private static List<String> stDevicePropertyList = 
			Arrays.asList("status", "lastActivity");
	/* SmartThings property list */
	private static List<String> stPropertyList = 
			Arrays.asList("alarmState", "currentAlarm",
			"switchState", "currentSwitch",
			"temperatureState", "currentTemperature",
			"carbonDioxideState", "currentCarbonDioxide",
			"doorState", "currentDoor",
			"lockState", "currentLock",
			"motionState", "currentMotion",
			"presenceState", "currentPresence",
			"smokeState", "currentSmoke",
			"thermostatOperatingStateState", "currentThermostatOperatingState",
			"thermostatSetpointState", "currentThermostatSetpoint",
			"thermostatModeState", "currentThermostatMode",
			"heatingSetpointState", "currentHeatingSetpoint",
			"thermostatFanModeState", "currentThermostatFanMode",
			"coolingSetpointState", "currentCoolingSetpoint",
			"currentMode", "modes", "mode");
	private static List<String> securitySensitiveActionList = Arrays.asList("open", "close", "unlock", "lock", "on", "off", "setLevel", 
			"COSmoke", "usercodechange", "setLocationMode", "setMode", "siren", "strobe", "both", "httpPost", "execute");
	private static List<String> securitySensitiveNetworkMethList = Arrays.asList("httpPost");
	private static List<String> securitySensitiveAttrList = Arrays.asList("location", "lock", "switch", "level", 
			"valve", "carbonMonoxide", "door", "color", "hue", "saturation", "alarm", "network", "thermostatMode",
			"thermostatFanMode");
	private static List<String> appStrVarNameList = Arrays.asList("currentMode");
	private static List<String> stStrPropList = Arrays.asList("value", "id", "name", "stringValue");
	private static List<String> stDoublePropList = Arrays.asList("time", "doubleValue", "floatValue","integerValue",
			"numberValue", "numericValue");
	private static List<String> stSystemFieldNameList = Arrays.asList("STCurrentSystemTime"); /* public class STCommonAPIs in SmartThings.java */
	private static List<String> STEventPropNameList = Arrays.asList("doubleValue", "floatValue",
			"name", "value", "isStateChange", "device", "deviceId", "hubId", "integerValue", "jsonValue",
			"location", "locationId", "source", "stringValue", "numberValue", "numericValue",
			"data", "date", "dateValue");
	private static SourceUnit sourceUnit;
	/* SmartThings void-type method list */
	private static List<String> stVoidMethList = 
			Arrays.asList("default_null_method", "sendSms", "sendPush", "increaseSTSystemTime", "sendSmsMessage",
					"sendNotificationToContacts", "sendPushMessage", "sendNotification", "setLocationMode",
					"setName", "setMode", "sendNotificationEvent", "sendHubCommand");
	/* SmartThings double-type method list */
	private static List<String> stDoubleMethList = Arrays.asList("now", "getLatitude", "getLongitude");
	/* SmartThings date-type method list */
	private static List<String> stDateMethList = Arrays.asList("timeTodayAfter", "timeToday");
	/* SmartThings string-type method list */
	private static List<String> stStringMethList = Arrays.asList("format", "toSystemFormat", "toString",
			"toLowerCase", "toUpperCase", "getMode", "getName", "getTemperatureScale", "getZipCode", "getId");
	/* System class method list */
	private static List<String> classMethList = Arrays.asList("compareEquals", "compareNotEqual", "compareGreaterThanEqual",
			"compareGreaterThan", "compareLessThanEqual", "compareLessThan");
	/* System basic method list */
	private static List<String> basicMethList = Arrays.asList("plus", "minus");
	private static List<String> ignoreableMethList = Arrays.asList("toInteger", "toLong", "toDouble", "toFloat", "clearTime", "toString",
			"toLowerCase", "format", "toSystemFormat", "toUpperCase", "getTime");
	private static List<String> interestedObjMethList = Arrays.asList("parseInt", "parseDouble");
	
	private static int MAX_INT_ARRAY_SIZE = 5;
	private static int MAX_SYSTEM_TIME = 50;
	private static int MAX_TEMPERATURE = 3;
	private static int MAX_ARRAY_SIZE = 5;
	private static int MAX_SUBSCRIBERS = 10;
	private static int MAX_STORED_EVENTS = 3;
	private static int MAX_NUM_EVENTS = 8;
	private static int MAX_SWITCH_DEVICES = 10;
	private static int MAX_COMMAND_REPITIONS = 2;
	private static int MAX_POWER_METER = 3;
	private static int MAX_TRAN_FAILURES = 1;
	private static String NULL_STR = "NULL";
	/********************************************/
	
	public static void main(String args[]) {
		List<Integer> dl = getDigitList("12345");
		System.out.println(dl);
	}
	
	public static String getNullStr()
	{
		return NULL_STR;
	}
	
	public static int getMaxIntArraySize()
	{
		return MAX_INT_ARRAY_SIZE;
	}
	
	public static int getMaxArraySize()
	{
		return MAX_ARRAY_SIZE;
	}
	
	public static int getMaxSubscribers()
	{
		return MAX_SUBSCRIBERS;
	}
	
	public static int getMaxNumEvents()
	{
		return MAX_NUM_EVENTS;
	}
	
	public static int getMaxStoredEvents()
	{
		return MAX_STORED_EVENTS;
	}
	
	public static int getMaxSwtichDevices()
	{
		return MAX_SWITCH_DEVICES;
	}
	
	public static int getMaxSystemTime()
	{
		return MAX_SYSTEM_TIME;
	}
	
	public static int getMaxTemperature()
	{
		return MAX_TEMPERATURE;
	}
	
	public static int getMaxCommandRepititions()
	{
		return MAX_COMMAND_REPITIONS;
	}
	
	public static void setMaxIntArraySize(int value)
	{
		MAX_INT_ARRAY_SIZE = value;
	}
	
	public static int getMaxPowerMeter()
	{
		return MAX_POWER_METER;
	}
	
	public static int getMaxTranFailures()
	{
		return MAX_TRAN_FAILURES;
	}
	
	/* Return the content of file at "path" as a String
	 * */
	public static String readFile(String path, Charset encoding) throws IOException 
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
	
	public static void printInputInfo(GInputInfo inputInfo)
	{
		if (inputInfo instanceof GDeviceInputInfo)
		{
			System.out.println(((GDeviceInputInfo)inputInfo).inputName + ", " + ((GDeviceInputInfo)inputInfo).deviceType + 
					", " + ((GDeviceInputInfo)inputInfo).isMultiple);
		} else if (inputInfo instanceof GOtherInputInfo)
		{
			System.out.println(((GOtherInputInfo)inputInfo).inputName + ", " + ((GOtherInputInfo)inputInfo).infoType + 
					", " + ((GOtherInputInfo)inputInfo).isMultiple);
		}
	}
	
	public static List<Expression> buildExprList(Expression args)
	{
		List<Expression> exprList = new ArrayList<Expression>();
		
		/* Build the list of argument expressions */
		if(args instanceof TupleExpression)
		{
			exprList = ((TupleExpression) args).getExpressions();
		}
		else
		{
			exprList.add(args);
		}
		
		return exprList;
	}
	
	public static Map<String, List<String>> getCallerInfo(Map<String, List<String>> calleeInfo)
	{
		Map<String, List<String>> callerInfo;
		
		callerInfo = new HashMap<String, List<String>>();
		for(Map.Entry<String, List<String>> entry : calleeInfo.entrySet())
		{
			for(String callee : entry.getValue())
			{
				if(callerInfo.containsKey(callee))
				{
					if(!callerInfo.get(callee).contains(entry.getKey()))
					{
						callerInfo.get(callee).add(entry.getKey());
					}
				}
				else
				{
					List<String> callers = new ArrayList<String>();
					
					callers.add(entry.getKey());
					callerInfo.put(callee, callers);
				}
			}
		}
		
		return callerInfo;
	}
	
	public static boolean containsCaller(Map<String, List<String>> callerInfo, String caller)
	{
		for(List<String> callers : callerInfo.values())
		{
			if(callers.contains(caller))
			{
				return true;
			}
		}
		return false;
	}
	
	public static ClassNode getTypeFromSmartThingApi(String stApi, boolean isMultiple)
	{
		ClassNode inferredType = ClassHelper.OBJECT_TYPE;
		
		if(stApi.equals("getChildDevice"))
		{
			/* Inferred type: STDevice */
			inferredType = getClassType("STDevice");
		}
		else if(stApi.equals("getChildDevices"))
		{
			/* Inferred type: either STDevice or List<STDevice> */
			ClassNode baseType = getClassType("STDevice");
			if(isMultiple)
			{
				GenericsType genericsType = new GenericsType(baseType);
				GenericsType[] genericsTypes = {genericsType};
				ClassNode gType = ClassHelper.LIST_TYPE.getPlainNodeReference();
				gType.setGenericsTypes(genericsTypes);
				
				inferredType = gType;
			}
			else
			{
				inferredType = baseType;
			}
		}
		else if(stApi.equals("events") || stApi.equals("eventsBetween") || stApi.equals("eventsSince"))
		{
			/* Inferred type: either STEvent or STEvent[] */
			ClassNode baseType = getClassType("STEvent");
			if(isMultiple)
			{
				GenericsType genericsType = new GenericsType(baseType);
				GenericsType[] genericsTypes = {genericsType};
				ClassNode gType = ClassHelper.LIST_TYPE.getPlainNodeReference();
				gType.setGenericsTypes(genericsTypes);
				
				inferredType = gType;
			}
			else
			{
				inferredType = baseType;
			}
		}
		else if(GUtil.isAVoidMethod(stApi))
		{
			inferredType = ClassHelper.VOID_TYPE;
		}
		else if(GUtil.isADoubleMethod(stApi))
		{
			inferredType = ClassHelper.double_TYPE;
		}
		else if(GUtil.isADateMethod(stApi))
		{
			inferredType = new ClassNode(java.util.Date.class);
		}
		else if(GUtil.isAStringMethod(stApi))
		{
			inferredType = ClassHelper.STRING_TYPE;
		}
		else if(stApi.equals("getSunriseAndSunset"))
		{
			inferredType = getClassType("STSunriseSunset");
		}
		else if(stApi.equals("getWeatherFeature"))
		{
			inferredType = getClassType("STWeatherFeature");
		}
		else if(stApi.equals("getCurrentMode"))
		{
			inferredType = getClassType("STMode");
		}
		else if(stApi.equals("getTimeZone"))
		{
			inferredType = new ClassNode(java.util.TimeZone.class);
		}
		
		return inferredType;
	}
	
	public static ClassNode getTypeFromMethCall(MethodCallExpression mce, boolean isMultiple)
	{
		ClassNode inferredType = ClassHelper.OBJECT_TYPE;
		Expression objectExpr = mce.getObjectExpression();
		
		if(objectExpr instanceof VariableExpression)
		{
			String exprMethText; 
			
			if(mce.getMethodAsString() == null)
			{
				//dynamic methods
				exprMethText = mce.getText();
			}
			else
			{
				exprMethText = mce.getMethodAsString();
			}
			inferredType = GUtil.getTypeFromSmartThingApi(exprMethText, isMultiple);
		}
		else if(objectExpr instanceof MethodCallExpression)
		{
			Expression method = mce.getMethod();
			
			if(method instanceof ConstantExpression)
			{
				String methName = ((ConstantExpression)method).getText();
				boolean isMultipleLev1 = true;
				
				if(methName.equals("find"))
				{
					isMultipleLev1 = false;
				}
				else if(methName.equals("findAll"))
				{
					isMultipleLev1 = true;
				}
				else
				{
					System.out.println("Error in getTypeFromMethCall: " + methName);
				}
				inferredType = GUtil.getTypeFromMethCall((MethodCallExpression)objectExpr, isMultipleLev1);
			}
			else
			{
				System.out.println("Error in getTypeFromMethCall: " + method.getType());
			}
		}
		
		return inferredType;
	}
	
	public static boolean toIntConvertible(ClassNode gType)
	{
		if((gType == ClassHelper.byte_TYPE) || (gType == ClassHelper.Byte_TYPE) ||
			(gType == ClassHelper.long_TYPE) || (gType == ClassHelper.Long_TYPE) ||
			(gType == ClassHelper.double_TYPE) || (gType == ClassHelper.Double_TYPE)
			|| (gType == ClassHelper.BigDecimal_TYPE) ||
			(gType == ClassHelper.int_TYPE) || (gType == ClassHelper.Integer_TYPE) ||
			(gType == ClassHelper.char_TYPE) || (gType == ClassHelper.Character_TYPE) ||
			(gType == ClassHelper.short_TYPE) || (gType == ClassHelper.Short_TYPE) ||
			(gType == ClassHelper.float_TYPE) || (gType == ClassHelper.Float_TYPE) ||
			(gType == ClassHelper.OBJECT_TYPE) ||
			gType.getName().equals("java.math.BigDecimal") ||
			gType.getName().equals("java.lang.Byte") ||
			gType.getName().equals("java.lang.Character") ||
			gType.getName().equals("java.lang.Double") ||
			gType.getName().equals("java.lang.Float") ||
			gType.getName().equals("java.lang.Integer") ||
			gType.getName().equals("java.lang.Short") ||
			gType.getName().equals("java.lang.Long") ||
			gType.getName().equals("java.lang.Number") ||
			gType.getName().equals("java.util.Date") ||
			gType.getName().equals("java.util.TimeZone") || /* Need to add TimeZone */
			gType.getName().equals("java.lang.String") ||
			gType.getName().equals("java.lang.Object") ||
			gType.getName().equals("groovy.lang.GString")) 
		{
			return true;
		}
		return false;
	}
	
	public static boolean isPrimitiveType(ClassNode gType)
	{
		if((gType == ClassHelper.boolean_TYPE) || (gType == ClassHelper.Boolean_TYPE)
				|| gType.getName().equals("java.lang.Boolean")
				|| GUtil.toIntConvertible(gType))
		{
			return true;
		}
		return false;
	}
	
	public static boolean toArrayConvertible(ClassNode gType)
	{
		if(gType.getName().equals("java.util.List") || gType.getName().equals("java.util.ArrayList")
				|| gType.getName().equals("java.util.Set") || gType.getName().equals("java.util.HashSet")
				|| gType.getName().equals("java.util.Collection"))
		{
			return true;
		}
		return false;
	}
	
	public static boolean isArrayGenericsTypesNull(ClassNode gType)
	{
		if(GUtil.toArrayConvertible(gType))
		{
			GenericsType[] genericTypes = gType.getGenericsTypes();
			if (genericTypes == null)
			{
				return true;
			}
			else if(genericTypes.length == 1)
			{
				ClassNode baseType = genericTypes[0].getType();
				String tyepName = baseType.getName();
				
				if(tyepName.equals("java.lang.Object")) /* java.lang.Object */
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean isArrayType(ClassNode gType)
	{
		if(gType.getComponentType() != null)
		{
			return true;
		}
		return false;
	}
	
	public static int getArrayDim(ClassNode gType)
	{
		int dim = 0;
		ClassNode tempType = gType;
		
		while(tempType.getComponentType() != null)
		{
			dim++;
			tempType = tempType.getComponentType();
		}
		
		return dim;
	}
	
	public static ClassNode getArrayComponentType(ClassNode gType)
	{
		ClassNode tempType = gType;
		
		while(tempType.getComponentType() != null)
		{
			tempType = tempType.getComponentType();
		}
		
		return tempType;
	}
	
	public static boolean isExprANum(Expression gExpr)
	{
		if(gExpr instanceof ConstantExpression)
		{
			ClassNode type = ((ConstantExpression)gExpr).getType();
			
			if(type.getName().equals("java.math.BigDecimal") || (type == ClassHelper.int_TYPE))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static String getIntValueStr(Expression gExpr)
	{
		String intValueStr = null;
		
		if(GUtil.isExprANum(gExpr))
		{
			ClassNode type = ((ConstantExpression)gExpr).getType();
			String valueStr = ((ConstantExpression)gExpr).getValue().toString();
			
			if(type.getName().equals("java.math.BigDecimal"))
			{
				int index = valueStr.indexOf(".");
				intValueStr = valueStr.substring(0, index);
			}
			else
			{
				intValueStr = valueStr;
			}
		}
		
		return intValueStr;
	}
	
	public static boolean isExprABoolean(Expression gExpr)
	{
		if(gExpr instanceof ConstantExpression)
		{
			ClassNode type = ((ConstantExpression)gExpr).getType();
			
			if((type == ClassHelper.boolean_TYPE) || (type == ClassHelper.Boolean_TYPE)
			|| type.getName().equals("java.lang.Boolean"))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isExprANull(Expression gExpr)
	{
		if(gExpr instanceof ConstantExpression)
		{
			ClassNode type = ((ConstantExpression)gExpr).getType();
			String text = ((ConstantExpression)gExpr).getText();
			
			if((type == ClassHelper.OBJECT_TYPE) && (text.equals("null")))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isExprALiteral(Expression gExpr)
	{
		if(GUtil.isExprANum(gExpr) || GUtil.isExprABoolean(gExpr) || GUtil.isExprANull(gExpr))
		{
			return true;
		}
		return false;
	}
	
	public static boolean isExprAStr(Expression gExpr)
	{
		if(gExpr.getType().getName().equals("java.lang.String") || gExpr.getType().getName().equals("groovy.lang.GString"))
		{
			return true;
		}
		else
		{
			ClassNode inferredType = gExpr.getNodeMetaData(StaticTypesMarker.INFERRED_TYPE);
			
			if(inferredType != null)
			{
				if(inferredType.getName().equals("java.lang.String") || inferredType.getName().equals("groovy.lang.GString"))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean isExprConvertibleToIntArray(Expression gExpr)
	{
		if(GUtil.isExprAStr(gExpr))
		{
			if((gExpr instanceof GStringExpression) || (gExpr instanceof MethodCallExpression)
					|| (gExpr instanceof ConstantExpression))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static ClassNode getExprType(Expression gExpr)
	{
		ClassNode gType = null;
		
		gType = gExpr.getNodeMetaData(StaticTypesMarker.INFERRED_TYPE);
		
		if(gType == null)
		{
			gType = gExpr.getType();
		}
		if(((gType == null) || (gType == ClassHelper.OBJECT_TYPE)) && (gExpr instanceof VariableExpression))
		{
			gType = GStmtBuilder.getVarGType((VariableExpression) gExpr);
		}
		
		if(gType == null)
		{
			gType = ClassHelper.int_TYPE.getPlainNodeReference();
		}
		
		return gType;
	}
	
	public static String getBooleanValueStr(Expression expr)
	{
		String booleanValueStr = null;
		
		if(GUtil.isExprABoolean(expr))
		{
			booleanValueStr = ((ConstantExpression)expr).getValue().toString();
		}
		
		return booleanValueStr;
	}
	
	public static String getRootTypeOfPropertyExpression(PropertyExpression expr)
	{
		String str = null;
		String exprText = expr.getText();
		Expression objectExpr = expr.getObjectExpression();
		
		if(exprText.startsWith("this."))
		{
			/* We will handle this case later */
		}
		else
		{
			if(objectExpr instanceof VariableExpression)
			{
				str = objectExpr.getType().getName();
			}
			else if(objectExpr instanceof PropertyExpression)
			{
				str = GUtil.getRootTypeOfPropertyExpression((PropertyExpression) objectExpr);
			}
		}
		
		return str;
	}
	
	public static String getSTStandardName(String name)
	{
		String standardName = name;
		
		if(!(name.startsWith("settings.") || name.startsWith("state.")))
		{
			standardName = "settings." + name;
		}
		
		return standardName;
	}
	
	public static boolean isALocalMethCall(MethodCallExpression gMCE)
	{
		if(gMCE.isImplicitThis())
		{
			return true;
		}
		else
		{
			String methText = gMCE.getText();
			
			if(methText.startsWith("this."))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isExprAnArrayAccess(Expression gExpr)
	{
		if(gExpr instanceof BinaryExpression)
		{
			org.codehaus.groovy.syntax.Token gToken = ((BinaryExpression) gExpr).getOperation();
			
			if(gToken.getText().equals("["))
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean isClosureUsedInMethCallExpr(MethodCallExpression gMCE)
	{
		java.util.List<Expression> gExprList = GUtil.buildExprList(gMCE.getArguments());
		
		for(Expression gExpr : gExprList)
		{
			if(gExpr instanceof ClosureExpression)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isMethCallExprABinaryExpr(MethodCallExpression gMCE)
	{
		String methName = gMCE.getMethodAsString();
		
		if(basicMethList.contains(methName))
		{
			/* def scheduleDayTime = timeToday(daytime, location.timeZone)
			 * scheduleDayTime = scheduleDayTime + 1
			 * */
			java.util.List<Expression> gExprList = GUtil.buildExprList(gMCE.getArguments());
			
			if(gExprList.size() == 1)
			{
				return true;
			}
		}
		else if(classMethList.contains(methName) && (gMCE.getObjectExpression() instanceof ClassExpression))
		{
			java.util.List<Expression> gExprList = GUtil.buildExprList(gMCE.getArguments());
			
			if(gExprList.size() == 2)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isArrayNameNeeded(String methName)
	{
		switch(methName)
		{
		case "events":
		case "eventsBetween":
		case "getCapabilities":
		case "statesSince":
		case "statesBetween":
		case "getSupportedAttributes":
		case "getSupportedCommands":
		case "getHubs":
		case "getModes":
		case "getValues":
		case "getArguments":
		case "getAttributes":
		case "getCommands": return true;
		}
		return false;
	}
	
	public static ClassNode getBaseType(Expression gExpr)
	{
		ClassNode gBaseType = null;
		ClassNode gType = GUtil.getExprType(gExpr);
		
		if(gType != null)
		{
			if(GUtil.toArrayConvertible(gType)) /* List */
			{
				GenericsType[] genericsTypes = gType.getGenericsTypes();
				
				if(genericsTypes != null)
				{
					gBaseType = genericsTypes[0].getType();
				}
			}
			else if(GUtil.isArrayType(gType)) /* Array */
			{
				gBaseType = GUtil.getArrayComponentType(gType);
			}
		}
		
		return gBaseType;
	}
	
	public static boolean isClosureMethodAbleToHandle(String methName)
	{
		switch(methName)
		{
		case "findAll":
		case "find":
		case "collect":
		case "each": /* We will handle this case later */
		case "count": return true;
		}
		
		return false;
	}
	
	public static boolean isATransformClosure(String methName)
	{
		switch(methName)
		{
		case "collect": return true;
		}
		
		return false;
	}
	
	public static boolean isATimeDataType(ClassNode gType)
	{
		String typeName = gType.getName();
		
		if(typeName.equals("java.util.Date"))
		{
			return true;
		}
		
		return false;
	}
	
	public static boolean isPropertyIgnorable(String propertyName)
	{
		switch(propertyName)
		{
		case "time": return true;
		}
		
		return false;
	}
	
	public static boolean isMethCallIgnorable(String methName)
	{
		if(ignoreableMethList.contains(methName))
		{
			return true;
		}
		
		return false;
	}
	
	public static boolean isInterestedOjbMethCall(String methName)
	{
		if(interestedObjMethList.contains(methName))
		{
			return true;
		}
		return false;
	}
	
	public static boolean isDigitList(String str)
	{
		for(int i = 0; i < str.length(); i++)
		{
			if(!Character.isDigit(str.charAt(i)))
			{
				return false;
			}
		}
		return true;
	}
	
	public static List<Integer> getDigitList(String str)
	{
		List<Integer> result = new ArrayList<Integer>();
		
		if(GUtil.isDigitList(str))
		{
			for(int i = 0; i < str.length(); i++)
			{
				result.add(Character.getNumericValue(str.charAt(i)));
			}
		}
		
		return result;
	}
	
	/* All event handler method's name has to be ended 
	 * with "EvtHandler"
	 * E.g.:
	 * Before: motionActiveHandler
	 * After: motionActiveEvtHandler
	 * */
	public static String getStandardEvtHandlerName(String methName)
	{
		String result = methName;
		
		if(currentSmartAppEvtHandlerList.contains(methName))
		{
			if(methName.endsWith("Handler"))
			{
				int index = methName.indexOf("Handler");
				
				result = methName.substring(0, index) + "EvtHandler";
			}
			else
			{
				result = methName + "EvtHandler";
			}
		}
		else if(methName.equals("installed"))
		{
			result = "installedEvtHandler";
		}
		
		return result;
	}
	
	public static ClassNode createGroovyType(SourceUnit source, String typeName, boolean isMultiple)
	{
		ClassNode baseType = null;
		ClassNode gType = null;
		
		switch(typeName)
		{
		case "BOOL": baseType = ClassHelper.boolean_TYPE.getPlainNodeReference(); break;
		case "DECIMAL": baseType = ClassHelper.long_TYPE.getPlainNodeReference(); break;
		case "NUMBER": baseType = ClassHelper.long_TYPE.getPlainNodeReference(); break;
		case "CONTACT":
		case "EMAIL":
		case "ENUM":
		case "ICON":
		case "PASSWORD":
		case "PHONE":
		case "TEXT":	
		case "MODE":
		case "UNKNOWN":	baseType = ClassHelper.STRING_TYPE.getPlainNodeReference(); break;
		case "TIME": baseType = new ClassNode(java.util.Date.class); break;
		default:
			try
			{
				baseType = new ClassNode(source.getClassLoader().loadClass(typeName));
			}
			catch(ClassNotFoundException exc){
				System.out.println(typeName + " not found with error: " + exc.getMessage());
			}
		}
		
		if(isMultiple)
		{
			GenericsType genericsType = new GenericsType(baseType);
			GenericsType[] genericsTypes = {genericsType};
			
			gType = ClassHelper.LIST_TYPE.getPlainNodeReference();
			gType.setGenericsTypes(genericsTypes);
		}
		else
		{
			gType = baseType;
		}
		
		return gType;
	}
	
	/* Map<java.lang.Object, Map> */
	public static ClassNode createGroovyO2MMapType()
	{
		ClassNode baseType1 = ClassHelper.OBJECT_TYPE.getPlainNodeReference();
		GenericsType genericsType1 = new GenericsType(baseType1);
		
		ClassNode baseType2 = ClassHelper.MAP_TYPE.getPlainNodeReference();
		GenericsType genericsType2 = new GenericsType(baseType2);
		
		GenericsType[] genericsTypes = {genericsType1, genericsType2};
		ClassNode gType = ClassHelper.MAP_TYPE.getPlainNodeReference();
		
		gType.setGenericsTypes(genericsTypes);
		
		return gType;
	}
	
	public static boolean isASTCommand(String methName)
	{
		if(stCommandList.contains(methName))
		{
			return true;
		}
		return false;
	}
	public static boolean isALocationSetMeth(String methName)
	{
		if(stLocationSetMethodList.contains(methName))
		{
			return true;
		}
		return false;
	}
	public static boolean isALocationGetMeth(String methName)
	{
		if(stLocationGetMethodList.contains(methName))
		{
			return true;
		}
		return false;
	}
	public static boolean isADeviceProperty(String prop)
	{
		if(stDevicePropertyList.contains(prop))
		{
			return true;
		}
		return false;
	}
	public static boolean isASTProperty(String prop)
	{
		if(stPropertyList.contains(prop))
		{
			return true;
		}
		return false;
	}
	public static boolean isADeviceGetMethod(String meth)
	{
		if(stDeviceGetMethodList.contains(meth))
		{
			return true;
		}
		return false;
	}
	public static boolean isADeviceSpecialGetMethod(String meth)
	{
		if(stDeviceSpecialGetMethodList.contains(meth))
		{
			return true;
		}
		return false;
	}
	public static String getAttributeFromDeviceType(String deviceType)
	{
		String attribute = null;
		
		switch(deviceType)
		{
		case "STSwitch": attribute = "switch"; break;
		case "STAlarm": attribute = "alarm"; break;
		case "STBulb": attribute = "switch"; break;
		case "STDoorControl": attribute = "door"; break;
		case "STLight": attribute = "switch"; break;
		case "STLock": attribute = "lock"; break;
		case "STMotionSensor": attribute = "motion"; break;
		case "STPresSensor": attribute = "presence"; break;
		case "STSwitchLevel": attribute = "level"; break;
		case "STValve": attribute = "valve"; break;
		case "STPowerMeter": attribute = "power"; break;
		case "STIlMeas": attribute = "illuminance"; break;
		case "STWaterSensor": attribute = "water"; break;
		case "STThreeAxis": attribute = "threeAxis"; break;
		case "STAccSensor": attribute = "acceleration"; break;
		case "STCarMoDetector": attribute = "carbonMonoxide"; break;
		case "STSmokeDetector": attribute = "smoke"; break;
		case "STAeonKeyFob": attribute = "button"; break;
		case "STColorCtrl": attribute = "color"; break;
		}
		return attribute;
	}
	
	public static String getAttributeFromDeviceTypeAndCommand(String deviceType, String command)
	{
		String attribute = null;
		
		switch(command)
		{
		case "fanAuto":
		case "fanCirculate":
		case "fanOn":
		case "setThermostatFanMode": attribute = "thermostatFanMode"; break;
		case "setCoolingSetpoint": attribute = "coolingSetpoint"; break;
		case "setHeatingSetpoint": attribute = "heatingSetpoint"; break;
		case "auto":
		case "cool":
		case "emergencyHeat":
		case "heat":
		case "setThermostatMode": attribute = "thermostatMode"; break;
		case "on": attribute = "switch"; break;
		case "off": {
			switch(deviceType)
			{
			case "STAlarm": attribute = "alarm"; break;
			case "STBulb":
			case "STSwitch":
			case "STLight":
			case "STOutlet":
			case "STRelSwitch": attribute = "switch"; break;
			case "STTherMode":
			case "STThermostat": attribute = "thermostatMode"; break;
			default: attribute = "switch"; break;
			}
		}
		break;
		case "both":
		case "siren":
		case "strobe": attribute = "alarm"; break;
		case "open":
		case "close": {
			switch(deviceType)
			{
			case "STGarDoCtrl":
			case "STDoorControl": attribute = "door"; break;
			case "STValve": attribute = "valve"; break;
			case "STWindowShade": attribute = "windowShade"; break;
			default: attribute = "door"; break;
			}
		}
		break;
		case "usercodechange":
		case "unlock":
		case "lock": attribute = "lock"; break;
		case "setLevel": attribute = "level"; break;
		case "COSmoke": attribute = "carbonMonoxide"; break;
		case "setHue": attribute = "hue"; break;
		case "setSaturation": attribute = "saturation"; break;
		case "setColor": attribute = "color"; break;
		}
		return attribute;
	}
	
	public static List<String> getAttributeListFromSTCommand(String command)
	{
		List<String> attribute = new ArrayList<String>();
		
		switch(command)
		{
		case "fanAuto":
		case "fanCirculate":
		case "fanOn":
		case "setThermostatFanMode": attribute.add("thermostatFanMode"); break;
		case "setCoolingSetpoint": attribute.add("coolingSetpoint"); break;
		case "setHeatingSetpoint": attribute.add("heatingSetpoint"); break;
		case "auto":
		case "cool":
		case "emergencyHeat":
		case "heat":
		case "setThermostatMode": attribute.add("thermostatMode"); break;
		case "on": attribute.add("switch"); break;
		case "off": attribute.add("alarm"); attribute.add("switch"); attribute.add("thermostatMode"); break;
		case "both":
		case "siren":
		case "strobe": attribute.add("alarm"); break;
		case "open":
		case "close": attribute.add("door"); attribute.add("valve"); attribute.add("windowShade"); attribute.add("door"); break;
		case "usercodechange":
		case "unlock":
		case "lock": attribute.add("lock"); break;
		case "setLevel": attribute.add("level"); break;
		case "COSmoke": attribute.add("carbonMonoxide"); break;
		case "setHue": attribute.add("hue"); break;
		case "setSaturation": attribute.add("saturation"); break;
		case "setColor": attribute.add("color"); break;
		}
		return attribute;
	}
	
	public static String getAttributeFromSTProperty(String prop)
	{
		String attribute = null;
		
		switch(prop)
		{
		case "alarmState":
		case "currentAlarm": attribute = "alarm"; break;
			
		case "switchState":
		case "currentSwitch": attribute = "switch"; break;
			
		case "temperatureState":
		case "currentTemperature": attribute = "temperature"; break;
			
		case "carbonDioxideState": 
		case "currentCarbonDioxide": attribute = "carbonDioxide"; break;
			
		case "doorState":
		case "currentDoor": attribute = "door"; break;
			
		case "lockState":
		case "currentLock": attribute = "lock"; break;
			
		case "motionState": 
		case "currentMotion": attribute = "motion"; break;
			
		case "presenceState": 
		case "currentPresence": attribute = "presence"; break;
			
		case "smokeState": 
		case "currentSmoke": attribute = "smoke"; break;
		
		case "carbonMonoxideState": 
		case "currentCarbonMonoxide": attribute = "carbonMonoxide"; break;
			
		case "thermostatOperatingStateState": 
		case "currentThermostatOperatingState": attribute = "thermostatOperatingState"; break;
			
		case "thermostatSetpointState": 
		case "currentThermostatSetpoint": attribute = "thermostatSetpoint"; break;
			
		case "thermostatModeState": 
		case "currentThermostatMode": attribute = "thermostatMode"; break;
			
		case "heatingSetpointState": 
		case "currentHeatingSetpoint": attribute = "heatingSetpoint"; break;
			
		case "thermostatFanModeState": 
		case "currentThermostatFanMode": attribute = "thermostatFanMode"; break;
			
		case "coolingSetpointState": 
		case "currentCoolingSetpoint": attribute = "coolingSetpoint"; break;
			
		case "currentMode": 
		case "modes": 
		case "mode": attribute = "location"; break;
		
		case "contactState":
		case "currentContact": attribute = "contact"; break;
		
		case "powerState":
		case "currentPower": attribute = "power"; break;
		
		case "illuminanceState":
		case "currentIlluminance": attribute = "illuminance"; break;
		
		case "waterState":
		case "currentWater": attribute = "water"; break;
		
		case "valveState":
		case "currentValve": attribute = "valve"; break;
		
		case "accelerationState":
		case "currentAcceleration": attribute = "acceleration"; break;
		
		case "levelState":
		case "currentLevel": attribute = "level"; break;
		}
		
		return attribute;
	}
	public static String getCommandFromEvtType(String evtType)
	{
		String command = evtType;
		
		switch(evtType)
		{
		/* Door Control */
		case "closed": 
		case "closing": command = "close"; break;
		case "open": 
		case "opening": command = "open"; break;
		
		/* Lock */
		case "locked": command = "lock"; break;
		case "unlocked":
		case "unlocked with timeout": command = "unlock"; break;
		}
		
		return command;
	}
	
	public static boolean isASecuritySensitiveAction(String attr, String evtType)
	{
		if(securitySensitiveAttrList.contains(attr))
		{
			if(securitySensitiveActionList.contains(evtType) || evtType.equals(""))
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean isASecuritySensitiveNetworkAccess(String methName)
	{
		if(securitySensitiveNetworkMethList.contains(methName))
		{
			return true;
		}
		return false;
	}
	
	public static boolean isVarAnAppStrType(String varName)
	{
		if(appStrVarNameList.contains(varName))
		{
			return true;
		}
		return false;
	}
	
	public static boolean isPropStrType(String propName)
	{
		if(stStrPropList.contains(propName))
		{
			return true;
		}
		return false;
	}
	
	public static boolean isExprAMapType(Expression varExpr)
	{
		ClassNode type = varExpr.getType();
		
		if(type.getName().equals("java.util.Map") || 
				type.getName().equals("java.util.LinkedHashMap"))
		{
			return true;
		}
		else
		{
			ClassNode inferredType = varExpr.getNodeMetaData(StaticTypesMarker.INFERRED_TYPE);
			
			if(inferredType != null)
			{
				if(inferredType.getName().equals("java.util.Map") || 
						inferredType.getName().equals("java.util.LinkedHashMap"))
				{
					return true;
				}
			}
			else
			{
				inferredType = varExpr.getNodeMetaData(StaticCompilationMetadataKeys.PROPERTY_OWNER);
				
				if(inferredType != null)
				{
					if(inferredType.getName().equals("java.util.Map") || 
							inferredType.getName().equals("java.util.LinkedHashMap"))
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/* def aMap = [:]
	 * aMap["test"] = "hello world"
	 * */
	public static boolean isExprAMapAccess(Expression expr)
	{
		if(expr instanceof BinaryExpression)
		{
			if(((BinaryExpression) expr).getOperation().getText().contains("["))
			{
				Expression leftExpr = ((BinaryExpression) expr).getLeftExpression();
				
				if(leftExpr instanceof VariableExpression)
				{
					return isExprAMapType((VariableExpression) leftExpr);
				}
			}
		}
		return false;
	}
	
	public static boolean isAMapGetExpr(Expression expr)
	{
		if(expr instanceof MethodCallExpression)
		{
			if(((MethodCallExpression) expr).getMethodAsString().equals("get"))
			{
				Expression objExpr = ((MethodCallExpression) expr).getObjectExpression();
				
				if(objExpr instanceof VariableExpression)
				{
					return isExprAMapType((VariableExpression) objExpr);
				}
			}
		}
		return false;
	}
	
	public static boolean isAStateMapGetExpr(Expression expr)
	{
		if(isAMapGetExpr(expr))
		{
			Expression objExpr = ((MethodCallExpression) expr).getObjectExpression();
			String varName = ((VariableExpression) objExpr).getName();
			
			if(varName.equals(currentClassName + "_state"))
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean isAMapType(ClassNode gType)
	{
		if(gType.getName().equals("java.util.Map") || 
				gType.getName().equals("java.util.LinkedHashMap"))
		{
			return true;
		}
		return false;
	}
	
	public static boolean isASystemField(String fieldName)
	{
		if(stSystemFieldNameList.contains(fieldName))
		{
			return true;
		}
		return false;
	}
	
	public static boolean isASTEventProp(String propName)
	{
		if(STEventPropNameList.contains(propName))
		{
			return true;
		}
		return false;
	}
	
	public static void setSourceUnit(SourceUnit source)
	{
		sourceUnit = source;
	}
	
	public static ClassNode getClassType(String className)
	{
		ClassNode classType = ClassHelper.OBJECT_TYPE.getPlainNodeReference();
		
		try
		{
			classType = new ClassNode(sourceUnit.getClassLoader().loadClass(className));
		}
		catch(ClassNotFoundException exc){
			System.out.println(exc.getMessage());
		}
		
		return classType;
	}
	
	public static boolean isAnUnknownDevice(String deviceType)
	{
		switch(deviceType)
		{
		case "STBattery": return true;
		}
		return false;
	}
	
	public static boolean isAVoidMethod(String methName)
	{
		if(stVoidMethList.contains(methName))
		{
			return true;
		}
		return false;
	}
	public static boolean isADoubleMethod(String methName)
	{
		if(stDoubleMethList.contains(methName))
		{
			return true;
		}
		return false;
	}
	public static boolean isADateMethod(String methName)
	{
		if(stDateMethList.contains(methName))
		{
			return true;
		}
		return false;
	}
	public static boolean isAStringMethod(String methName)
	{
		if(stStringMethList.contains(methName))
		{
			return true;
		}
		return false;
	}
	
	public static ClassNode getTypeFromPropName(String propName)
	{
		if(stStrPropList.contains(propName))
		{
			return ClassHelper.STRING_TYPE;
		}
		else if(stDoublePropList.contains(propName))
		{
			return ClassHelper.double_TYPE;
		}
		return ClassHelper.OBJECT_TYPE;
	}
	
	/*********************** start of new methods **********************************/
	public static Node newAVariableDeclaratorId(String varName)
	{
		XPDim node2 = null;
		TId node1 = new TId(varName);
		AVariableDeclaratorId node = new AVariableDeclaratorId(node1, node2);
		
		return node;
	}
	public static PFormalParameter newAFormalParameter(String varName, PType node2)
	{
		PVariableDeclaratorId node3 = (PVariableDeclaratorId)GUtil.newAVariableDeclaratorId(varName);
		XPModifier node1 = null;
		AFormalParameter node = new AFormalParameter(node1, node2, node3);
		return node;
	}
//	public static PFormalParameterList newAFormalParameterFormalParameterList(PFormalParameter node1)
//	{
//		AFormalParameterFormalParameterList node = new AFormalParameterFormalParameterList(node1);
//		return node;
//	}
//	public static PFormalParameterList newAFormalParameterListFormalParameterList(PFormalParameter node3, PFormalParameterList node1)
//	{
//		TComma node2 = new TComma();
//		AFormalParameterListFormalParameterList node = new AFormalParameterListFormalParameterList(node1, node2, node3);
//		return node;
//	}
	/*********************** end of new methods ************************************/
}
