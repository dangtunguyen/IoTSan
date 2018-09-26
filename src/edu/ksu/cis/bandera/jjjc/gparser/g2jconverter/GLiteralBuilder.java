package edu.ksu.cis.bandera.jjjc.gparser.g2jconverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.ElvisOperatorExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.GStringExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.transform.sc.StaticCompilationMetadataKeys;
import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;
import edu.ksu.cis.bandera.jjjc.node.*;

public class GLiteralBuilder {
	/********************************************/
	/* We need two separated maps: one for global variables and the other
	 * for local variables 
	 * */
	public static Map<String, GLiteralContainer> LiteralVarMap = new HashMap<String, GLiteralContainer>();
	
	/* Enum variable: input "mode", "enum", title: "Heating or cooling?", options: ["heat","cool"] 
	 * We need to handle this case dynamically. The range of enum variables is from
	 * 1500 to 2099.
	 * List<String> inputEnumList: this list contains all enum variables defined in smart apps.
	 * Config info from user:
	 * <name>mode<name>
	 * <value>heat<value>
	 * => value of "mode" = index of "heat" in inputEnumList + 1500;
	 * Note: if the enum string is already defined in function getIntValueFromStr,
	 * we will use this defined value. E.g., in case of "heat", we will use the value 22.
	 * */
	/*
	case "No": intVal = 1500; break;
	case "Yes": intVal = 1501; break;
	case "heat": intVal = 1502; break;
	case "cool": intVal = 1503; break;
	...
	*/
	public static List<String> inputEnumList = new ArrayList<String>();
	/********************************************/
	
	public static void initializeLiteralVarMap()
	{
		LiteralVarMap = new HashMap<String, GLiteralContainer>();
	}
	
	/* A String/GString variable will be translated into an integer array, which
	 * consist of both static value and dynamic value (a name variable)
	 * E.g., "${location.zipCode}, ${evt.value}" => int[] arr = [403, evt.value];
	 * */
	public static int getIntValueFromStr(String str)
	{
		int intVal = 0;
		
		switch(str)
		{
		/******* Start of specific names ********/
		/* Attribute's names and values */
		/* capability.motionSensor */
		case "motion": intVal = 11; break; /* name */
		case "active": intVal = 12; break;
		case "inactive": intVal = 13; break;
		
		/* capability.switch */
		case "switch": intVal = 14; break;
		case "on": intVal = 15; break;
		case "off": intVal = 16; break;
		
		/* capability.presenceSensor */
		case "presence": intVal = 17; break; /* name */
		case "not present": intVal = 18; break;
		case "present": intVal = 19; break;
		
		/* capability.temperatureMeasurement */
		case "temperature": intVal = 20; break; /* name, value: NUMBER */
		
		/* capability.thermostatCoolingSetpoint */
		case "coolingSetpoint": intVal = 21; break; /* name, value: NUMBER */
		case "coolingSetpointMin": intVal = 22; break; /* name, value: NUMBER */
		case "coolingSetpointMax": intVal = 23; break; /* name, value: NUMBER */
		
		/* capability.thermostatFanMode */
		case "thermostatFanMode": intVal = 24; break; /* name */
		case "auto": intVal = 25; break;
		case "circulate": intVal = 26; break;
		/* case "on": intVal = 15; break; => capability.switch */
		
		/* capability.thermostatHeatingSetpoint */
		case "heatingSetpoint": intVal = 27; break; /* name, value: NUMBER */
		case "heatingSetpointMin": intVal = 28; break; /* name, value: NUMBER */
		case "heatingSetpointMax": intVal = 29; break; /* name, value: NUMBER */
		
		/* capability.thermostatMode */
		case "thermostatMode": intVal = 30; break; /* name */
		/* case "auto": intVal = 25; break; => capability.thermostatFanMode */
		case "cool": intVal = 31; break;
		case "emergency heat": intVal = 32; break;
		case "heat": intVal = 33; break;
		/* case "off": intVal = 16; break; */
		
		/* capability.thermostatOperatingState */
		case "thermostatOperatingState": intVal =34; break; /* name */
		case "cooling": intVal = 35; break;
		case "fan only": intVal = 36; break;
		case "heating": intVal = 37; break;
		case "idle": intVal = 38; break;
		case "pending cool": intVal = 39; break;
		case "pending heat": intVal = 40; break;
		case "vent economizer": intVal = 41; break;
		
		/* capability.thermostatSetpoint */
		case "thermostatSetpoint": intVal = 42; break; /* name, value: NUMBER */
		case "thermostatSetpointMin": intVal = 43; break; /* name, value: NUMBER */
		case "thermostatSetpointMax": intVal = 44; break; /* name, value: NUMBER */
		
		/* capability.lock */
		case "lock": intVal = 45; break; /* name */
		case "locked": intVal = 46; break;
		case "unknown": intVal = 47; break;
		case "unlocked": intVal = 48; break;
		case "unlocked with timeout": intVal = 49; break;
		
		/* capability.smokeDetector */
		case "smoke": intVal = 50; break; /* name */
		case "clear": intVal = 51; break;
		case "detected": intVal = 52; break;
		case "tested": intVal = 53; break;
		
		/* capability.doorControl */
		case "door": intVal = 54; break; /* name */
		case "closed": intVal = 55; break;
		case "closing": intVal = 56; break;
		case "open": intVal = 57; break;
		case "opening": intVal = 58; break;
		/* case "unknown": intVal = 47; break; */
		
		/* capability.contactSensor */
		case "contact": intVal = 59; break; /* name */
		
		/* capability.powerMeter */
		case "power": intVal = 60; break; /* name */
		
		/* capability.illuminanceMeasurement */
		case "illuminance": intVal = 61; break; /* name */
		
		/* capability.waterSensor */
		case "water": intVal = 62; break; /* name */
		case "wet": intVal = 63; break;
		case "dry": intVal = 64; break;
		
		/* capability.valve */
		case "valve": intVal = 65; break; /* name */
		
		/* capability.accelerationSensor */
		case "acceleration": intVal = 66; break; /* name */
		
		/* getWeatherFeature */
		case "forecast": intVal = 67; break;
		case "rain": intVal = 68; break;
		case "snow": intVal = 69; break;
		case "showers": intVal = 70; break;
		case "sprinkles": intVal = 71; break;
		case "precipitation": intVal = 72; break;
		
		/* capability.battery */
		case "battery": intVal = 73; break;
		
		/* capability.switchLevel */
		case "level": intVal = 74; break;
		
		/* capability.carbonMonoxideDetector */
		case "carbonMonoxide": intVal = 75; break;
		
		/* capability.alarm */
		case "alarm": intVal = 76; break;
		case "both": intVal = 77; break;
		case "siren": intVal = 78; break;
		case "strobe": intVal = 79; break;
		
		/* device.AeonKeyFob */
		case "button": intVal = 80; break;
		case "pushed": intVal = 81; break;
		case "held": intVal = 82; break;
		
		/* capability.colorControl */
		case "color": intVal = 83; break;
		case "hue": intVal = 84; break;
		case "saturation": intVal = 85; break;
		
		/* capability.relativeHumidityMeasurement */
		case "humidity": intVal = 86; break;
		
		/* capability.powerSource */
		case "powered": intVal = 87; break;
		case "mains": intVal = 88; break;
		case "dc": intVal = 89; break;
		
		/* Other variables */
		/* Location modes */
		case "Home": intVal = 1400; break;
		case "Away": intVal = 1401; break;
		case "Night": intVal = 1402; break;
		case "mode": intVal = 1403; break;
		case "position": intVal = 1404; break;
		case "sunriseTime": intVal = 1405; break;
		case "sunsetTime": intVal = 1406; break;
		
		/* Enum variable: input "mode", "enum", title: "Heating or cooling?", options: ["heat","cool"] 
		 * We need to handle this case dynamically. The range of enum variables is from
		 * 1500 to 2099.
		 * List<String> inputEnumList: this list contains all enum variables defined in smart apps.
		 * Config info from user:
		 * <name>mode<name>
		 * <value>heat<value>
		 * => value of "mode" = index of "heat" in inputEnumList + 1500;
		 * Note: if the enum string is already defined in function getIntValueFromStr,
		 * we will use this defined value. E.g., in case of "heat", we will use the value 22.
		 * */
		/*
		case "No": intVal = 1500; break;
		case "Yes": intVal = 1501; break;
		case "heat": intVal = 1502; break;
		case "cool": intVal = 1503; break;
		...
		*/
		/******* End of specific names *********/
		
		/******* Start of class and its properties' names ********/
		case "STMode":
		case "STMode.getName":
		case "STMode.name": 
		case "STMode.getId":
		case "STMode.id": intVal = 2100; break;
		
		case "STState": intVal = 2200; break;
		case "STState.doubleValue": 
		case "STState.floatValue":
		case "STState.value":
		case "STState.integerValue":
		case "STState.stringValue":
		case "STState.numberValue":
		case "STState.numericValue":
		case "STState.getDoubleValue":
		case "STState.getFloatValue":
		case "STState.getIntegerValue":
		case "STState.getNumberValue":
		case "STState.getNumericValue":
		case "STState.getStringValue":
		case "STState.getValue": intVal = 2201; break;
		case "STState.name":
		case "STState.getName":
		case "STState.id":
		case "STState.getId":
		case "STState.hubId":
		case "STState.getHubId": intVal = 2202; break;
		
		case "STHub": intVal = 2300; break;
		case "STHub.name":
		case "STHub.getName":
		case "STHub.id":
		case "STHub.getId": 
		case "STHub.zigbeeId":
		case "STHub.getZigbeeId": 
		case "STHub.zigbeeEui":
		case "STHub.getZigbeeEui": intVal = 2301; break;
		case "STHub.localIP":
		case "STHub.getLocalIP":
		case "STHub.localSrvPortTCP":
		case "STHub.getLocalSrvPortTCP": intVal = 2302; break;
		
		case "STLocation": intVal = 2400; break;
		case "STLocation.currentMode":
		case "STLocation.getCurrentMode":
		case "STLocation.getModes":
		case "STLocation.getMode":
		case "STLocation.modes": intVal = 2401; break;
		case "STLocation.getHubs":
		case "STLocation.hubs": intVal = 2402; break;
		case "STLocation.latitude":
		case "STLocation.getLatitude":
		case "STLocation.longitude":
		case "STLocation.getLongitude":
		case "STLocation.zipCode":
		case "STLocation.getZipCode":
		case "STLocation.timeZone":
		case "STLocation.getTimeZone":intVal = 2403; break;
		
		case "STAttribute":
		case "STAttribute.values":
		case "STAttribute.getValues":intVal = 2500; break;
		
		case "STDevice": intVal = 2600; break;
		case "STDevice.status":
		case "STDevice.getStatus":
		case "STDevice.currentState":
		case "STDevice.currentValue":intVal = 2601; break;
		case "STDevice.lastActivity":
		case "STDevice.getLastActivity":
		case "STDevice.latestState":
		case "STDevice.latestValue":
		case "STDevice.events":
		case "STDevice.eventsBetween":
		case "STDevice.eventsSince":
		case "STDevice.statesBetween":
		case "STDevice.statesSince":intVal = 2602; break;
		case "STDevice.hub":
		case "STDevice.getHub":intVal = 2603; break;
		
		case "STEvent":
		case "STEvent.getData":
		case "STEvent.doubleValue":
		case "STEvent.getDoubleValue":
		case "STEvent.getFloatValue":
		case "STEvent.floatValue":
		case "STEvent.value":
		case "STEvent.getValue":
		case "STEvent.integerValue":
		case "STEvent.getIntegerValue":
		case "STEvent.stringValue":
		case "STEvent.getStringValue":
		case "STEvent.numberValue":
		case "STEvent.getNumberValue":
		case "STEvent.numericValue":
		case "STEvent.getNumericValue": intVal = 2700; break;
		
		case "STSwitch":
		case "STSwitch.switchState":
		case "STSwitch.currentSwitch":intVal = 2800; break;
		
		case "STDoorControl":
		case "STDoorControl.doorState":
		case "STDoorControl.currentDoor":intVal = 2900; break;
		
		case "STLock":
		case "STLock.lockState":
		case "STLock.currentLock":intVal = 3000; break;
		
		case "STMotionSensor":
		case "STMotionSensor.motionState":
		case "STMotionSensor.currentMotion":intVal = 3100; break;
		}
		
		if((intVal == 0) && inputEnumList.contains(str))
		{
			intVal = inputEnumList.indexOf(str) + 1500;
		}
		
		return intVal;
	}
	
	/* This method is used to check if the input str should be
	 * considered as a dynamic value. We will handle this case later
	 * */
	public static boolean isStrADynamicValue(String str)
	{
		switch(str)
		{
//		case "STMode":
//		case "STMode.getName":
//		case "STMode.name": 
//		case "STMode.getId":
//		case "STMode.id":
//		
//		case "STState":
//		case "STState.doubleValue": 
//		case "STState.floatValue":
//		case "STState.value":
//		case "STState.integerValue":
//		case "STState.stringValue":
//		case "STState.numberValue":
//		case "STState.numericValue":
//		case "STState.getDoubleValue":
//		case "STState.getFloatValue":
//		case "STState.getIntegerValue":
//		case "STState.getNumberValue":
//		case "STState.getNumericValue":
//		case "STState.getStringValue":
//		case "STState.getValue":
//		case "STState.name":
//		case "STState.getName":
//		case "STState.id":
//		case "STState.getId":
//		case "STState.hubId":
//		case "STState.getHubId":
//		
//		case "STHub":
//		case "STHub.name":
//		case "STHub.getName":
//		case "STHub.id":
//		case "STHub.getId": 
//		case "STHub.zigbeeId":
//		case "STHub.getZigbeeId": 
//		case "STHub.zigbeeEui":
//		case "STHub.getZigbeeEui":
//		case "STHub.localIP":
//		case "STHub.getLocalIP":
//		case "STHub.localSrvPortTCP":
//		case "STHub.getLocalSrvPortTCP":
//		
//		case "STLocation":
//		case "STLocation.currentMode":
//		case "STLocation.getCurrentMode":
//		case "STLocation.getModes":
//		case "STLocation.getMode":
//		case "STLocation.modes":
//		case "STLocation.getHubs":
//		case "STLocation.hubs":
//		case "STLocation.latitude":
//		case "STLocation.getLatitude":
//		case "STLocation.longitude":
//		case "STLocation.getLongitude":
//		case "STLocation.zipCode":
//		case "STLocation.getZipCode":
//		case "STLocation.timeZone":
//		case "STLocation.getTimeZone":
//		
//		case "STAttribute":
//		case "STAttribute.values":
//		case "STAttribute.getValues":
		
//		case "STDevice":
//		case "STDevice.status":
//		case "STDevice.getStatus":
//		case "STDevice.currentState":
//		case "STDevice.currentValue":
//		case "STDevice.lastActivity":
//		case "STDevice.getLastActivity":
//		case "STDevice.latestState":
//		case "STDevice.latestValue":
//		case "STDevice.events":
//		case "STDevice.eventsBetween":
//		case "STDevice.eventsSince":
//		case "STDevice.statesBetween":
//		case "STDevice.statesSince":
//		case "STDevice.hub":
//		case "STDevice.getHub":
		
//		case "STEvent":
//		case "STEvent.getData":
//		case "STEvent.doubleValue":
//		case "STEvent.getDoubleValue":
//		case "STEvent.getFloatValue":
//		case "STEvent.floatValue":
//		case "STEvent.value":
//		case "STEvent.getValue":
//		case "STEvent.integerValue":
//		case "STEvent.getIntegerValue":
//		case "STEvent.stringValue":
//		case "STEvent.getStringValue": return true;
//		case "STEvent.numberValue":
//		case "STEvent.getNumberValue":
//		case "STEvent.numericValue":
//		case "STEvent.getNumericValue":
		
//		case "STSwitch":
//		case "STSwitch.switchState":
//		case "STSwitch.currentSwitch":
//		
//		case "STDoorControl":
//		case "STDoorControl.doorState":
//		case "STDoorControl.currentDoor":
//		
//		case "STLock":
//		case "STLock.lockState":
//		case "STLock.currentLock":
//		
//		case "STMotionSensor":
//		case "STMotionSensor.motionState":
//		case "STMotionSensor.currentMotion":
		}
		return false;
	}
	
	public static GLiteralContainer getLiteralsFromPropertyExpression(PropertyExpression expr)
	{
		GLiteralContainer literalContainer = new GLiteralContainer();
		String exprText = expr.getText();
		
		if(LiteralVarMap.containsKey(exprText))
		{
			literalContainer = LiteralVarMap.get(exprText);
		}
		else
		{
			String str = null;
			
			if(exprText.startsWith("settings."))
			{
				ClassNode gType = GUtil.getExprType(expr);
				
				if(gType != null)
				{
					str = gType.getName();
				}
			}
			else
			{
				Expression objectExpr = expr.getObjectExpression();
				
				str = objectExpr.getNodeMetaData(StaticCompilationMetadataKeys.PROPERTY_OWNER) + 
						"." + expr.getPropertyAsString();
			}
			
			if(str != null)
			{
				if(GLiteralBuilder.isStrADynamicValue(str))
				{
					PExp jExpr = GExprBuilder.buildANameExp(expr);
					
					if(jExpr != null)
					{
						/* Update dynamic values */
						literalContainer.dynamicValList.add(jExpr);
					}
				}
				else
				{
					/* Update static values */
					literalContainer.staticValList.add(GLiteralBuilder.getIntValueFromStr(str));
				}
			}
		}
		
		return literalContainer;
	}
	public static GLiteralContainer getLiteralsFromVariableExpression(VariableExpression expr)
	{
		GLiteralContainer literalContainer = new GLiteralContainer();
		String str = expr.getName();
		
		if(LiteralVarMap.containsKey(str))
		{
			literalContainer = LiteralVarMap.get(str);
		}
		else
		{
			ClassNode gType = GUtil.getExprType(expr);
			
			if(gType != null)
			{
				String type = gType.getName();
				
				if(GLiteralBuilder.isStrADynamicValue(type))
				{
					PExp jExpr = GExprBuilder.buildANameExp(expr);
					
					if(jExpr != null)
					{
						/* Update dynamic values */
						literalContainer.dynamicValList.add(jExpr);
					}
				}
				else
				{
					int intVal = 0;
					intVal = GLiteralBuilder.getIntValueFromStr(type);
					
					/* Update static values */
					literalContainer.staticValList.add(intVal);	
				}
			}
		}
		
		return literalContainer;
	}
	public static GLiteralContainer getLiteralsFromAStr(String str)
	{
		GLiteralContainer literalContainer = new GLiteralContainer();
		int intVal = 0;
		intVal = GLiteralBuilder.getIntValueFromStr(str);
		
		/* Update static values */
		literalContainer.staticValList.add(intVal);
		
		/* Update digit list */
		literalContainer.digitList = GUtil.getDigitList(str);
		
		return literalContainer;
	}
	public static GLiteralContainer getLiteralsFromGStringExpression(GStringExpression gStringExpr)
	{
		GLiteralContainer literalContainer = new GLiteralContainer();
		List<Expression> exprList = gStringExpr.getValues();
		
		for(Expression expr : exprList)
		{
			GLiteralContainer exprLiteralContainer = GLiteralBuilder.getStrLiteralsFromExprs(null, expr);
			literalContainer.addAll(exprLiteralContainer);
		}
		
		return literalContainer;
	}
	public static GLiteralContainer getLiteralsFromMethCallExpr(MethodCallExpression mce)
	{
		GLiteralContainer literalContainer = new GLiteralContainer();
		Expression objectExpr = mce.getObjectExpression();
		List<Expression> exprList = GUtil.buildExprList(mce.getArguments());
		
		/* Process ObjectExpression */
		if(objectExpr instanceof VariableExpression)
		{
			GLiteralContainer aLiteralContainer = GLiteralBuilder.getLiteralsFromVariableExpression((VariableExpression)objectExpr);
			literalContainer.addAll(aLiteralContainer);
		}
		else if(objectExpr instanceof PropertyExpression)
		{
			GLiteralContainer aLiteralContainer = GLiteralBuilder.getLiteralsFromPropertyExpression((PropertyExpression) objectExpr);
			literalContainer.addAll(aLiteralContainer);
		}
		else if(objectExpr instanceof GStringExpression)
		{
			GLiteralContainer aLiteralContainer = GLiteralBuilder.getLiteralsFromGStringExpression((GStringExpression) objectExpr);
			literalContainer.addAll(aLiteralContainer);
		}
		else if(objectExpr instanceof MethodCallExpression)
		{
			GLiteralContainer aLiteralContainer = GLiteralBuilder.getLiteralsFromMethCallExpr((MethodCallExpression)objectExpr);
			literalContainer.addAll(aLiteralContainer);
		}
		
		/* Process arguments */
		for(Expression expr : exprList)
		{
			GLiteralContainer exprLiteralContainer = GLiteralBuilder.getStrLiteralsFromExprs(null, expr);
			literalContainer.addAll(exprLiteralContainer);
		}
		
		return literalContainer;
	}
	public static GLiteralContainer getStrLiteralsFromExprs(Expression leftExpr, Expression rightExpr)
	{
		GLiteralContainer literalContainer = new GLiteralContainer();
		
		if(rightExpr != null)
		{
			if(rightExpr instanceof PropertyExpression)
			{
				GLiteralContainer aLiteralContainer = GLiteralBuilder.getLiteralsFromPropertyExpression((PropertyExpression) rightExpr);
				literalContainer.addAll(aLiteralContainer);
			}
			else if(rightExpr instanceof VariableExpression)
			{
				GLiteralContainer aLiteralContainer = GLiteralBuilder.getLiteralsFromVariableExpression((VariableExpression) rightExpr);
				literalContainer.addAll(aLiteralContainer);
			}
			else if(rightExpr instanceof GStringExpression)
			{
				GLiteralContainer aLiteralContainer = GLiteralBuilder.getLiteralsFromGStringExpression((GStringExpression) rightExpr);
				literalContainer.addAll(aLiteralContainer);
			}
			else if(rightExpr instanceof ConstantExpression &&
					rightExpr.getType().getName().equals("java.lang.String"))
			{
				String str = ((ConstantExpression)rightExpr).getText();
				
				GLiteralContainer aLiteralContainer = GLiteralBuilder.getLiteralsFromAStr(str);
				literalContainer.addAll(aLiteralContainer);
			}
			else if(rightExpr instanceof MethodCallExpression)
			{
				/* We need to handle SmartThing's API method calls later */
				GLiteralContainer aLiteralContainer = GLiteralBuilder.getLiteralsFromMethCallExpr((MethodCallExpression)rightExpr);
				literalContainer.addAll(aLiteralContainer);
			}
			else if(rightExpr instanceof ElvisOperatorExpression)
			{
				GLiteralContainer aLiteralContainer = GLiteralBuilder.getStrLiteralsFromExprs(null, ((ElvisOperatorExpression)rightExpr).getTrueExpression());
				literalContainer.addAll(aLiteralContainer);
				
				aLiteralContainer = GLiteralBuilder.getStrLiteralsFromExprs(null, ((ElvisOperatorExpression)rightExpr).getFalseExpression());
				literalContainer.addAll(aLiteralContainer);
			}
		}
		else
		{
			System.out.println("[getStrIntValuesFromExprs] rightExpr is null!!!");
		}
		
		/* Update LiteralVarMap */
		if((leftExpr != null) && !literalContainer.isEmpty())
		{
			if(leftExpr instanceof VariableExpression)
			{
				String varName = ((VariableExpression) leftExpr).getName();
				
				if(LiteralVarMap.containsKey(varName))
				{
					LiteralVarMap.get(varName).addAll(literalContainer);
				}
				else
				{
					LiteralVarMap.put(varName, literalContainer);
				}
			}
			else if(leftExpr instanceof PropertyExpression)
			{
				String exprText = leftExpr.getText();
				
				exprText = GUtil.getSTStandardName(exprText);
				if(LiteralVarMap.containsKey(exprText))
				{
					LiteralVarMap.get(exprText).addAll(literalContainer);
				}
				else
				{
					LiteralVarMap.put(exprText, literalContainer);
				}
			}
		}
		
		return literalContainer;
	}
	
	public static PLiteral build(Expression gExpr)
	{
		PLiteral node = null;
		
		if(GUtil.isExprANum(gExpr))
		{
			String intStr = GUtil.getIntValueStr(gExpr);
			TDecimalIntegerLiteral node1 = new TDecimalIntegerLiteral(intStr);
			ADecimalIntegerLiteral node2 = new ADecimalIntegerLiteral(node1);
			node = new AIntegerLiteralLiteral(node2);
		}
		else if(GUtil.isExprABoolean(gExpr))
		{
			String booleanValueStr = GUtil.getBooleanValueStr(gExpr);
			PBooleanLiteral node1;
			
			if(booleanValueStr.equals("true"))
			{
				node1 = new ATrueBooleanLiteral(new TTrue());
			}
			else
			{
				node1 = new AFalseBooleanLiteral(new TFalse());
			}
			node = new ABooleanLiteralLiteral(node1);
		}
		else if(GUtil.isExprANull(gExpr))
		{
			TDecimalIntegerLiteral node1 = new TDecimalIntegerLiteral("0");
			ADecimalIntegerLiteral node2 = new ADecimalIntegerLiteral(node1);
			node = new AIntegerLiteralLiteral(node2);
		}
		
		return node;
	}
}
