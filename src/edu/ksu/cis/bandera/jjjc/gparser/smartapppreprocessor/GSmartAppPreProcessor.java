package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.classgen.GeneratorContext;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.control.customizers.CompilationCustomizer;

import ca.mcgill.sable.soot.Modifier;
import edu.ksu.cis.bandera.jjjc.gparser.configinfomanager.GConfigInfoManager;
import edu.ksu.cis.bandera.jjjc.gparser.g2jconverter.GLiteralBuilder;
import edu.ksu.cis.bandera.jjjc.gparser.potentialriskscreener.GAppStateChangeDetector;
import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;
import groovy.transform.CompileStatic;

public class GSmartAppPreProcessor extends CompilationCustomizer {
	/********************************************/
	private Map<String, ClassNode> stateVarMap;
	private List<GInputInfo> inputInfoList;
	private List<GDeviceInputInfo> deviceInfoList;
	private List<GSubscriptionInfo> subscriptionInfoList;
	private List<MethodNode> STCommonAPIs;
	private List<String> smartAppDefaultMethods;
	private Map<String, String> evtVarNameMap;
	private Map<String, List<String>> calleeInfoMap;
	private Set<String> localMethSet;
	private boolean usedInGParser;
	private List<String> evtHandlerList;
	private String className;
	private Map<String, Boolean> localMethAppStateChangedMap;
	private Map<String, ClassNode> varName2TypeMap;
	private GStateMapEnum stateMapType;
	private boolean isInstalledMethNeeded;
	private Set<String> fieldNameSet;
	private Set<String> scheduleTimeVarSet;
	private Map<String, ClassNode> httpGet2VarTypeMap;
	private Map<String, List<String>> httpGet2ValueRangeMap;
	/********************************************/
	
	public GSmartAppPreProcessor(List<MethodNode> STCommonAPIs, boolean usedInGParser) {
		super(CompilePhase.CONVERSION);
		
		stateVarMap = new HashMap<String, ClassNode>();
		inputInfoList = new ArrayList<GInputInfo>();
		subscriptionInfoList = new ArrayList<GSubscriptionInfo>();
		this.STCommonAPIs = STCommonAPIs;
		smartAppDefaultMethods = new ArrayList<String>();
		smartAppDefaultMethods.addAll(Arrays.asList("installed", "updated"));
		evtVarNameMap = new HashMap<String, String>();
		this.usedInGParser = usedInGParser;
	}
	
	/* Getters */
	public Map<String, ClassNode> getStateVarMap()
	{
		return this.stateVarMap;
	}
	public List<GInputInfo> getInputInfoList()
	{
		return this.inputInfoList;
	}
	public List<GSubscriptionInfo> getSubscriptionInfoList()
	{
		return this.subscriptionInfoList;
	}
	public Map<String, String> getEvtVarNameMap()
	{
		return this.evtVarNameMap;
	}
	public List<GDeviceInputInfo> getDeviceInfoList()
	{
		return this.deviceInfoList;
	}
	public Map<String, List<String>> getCalleeInfoMap()
	{
		return this.calleeInfoMap;
	}
	public Set<String> getLocalMethSet()
	{
		return this.localMethSet;
	}
	public String getClassName()
	{
		return this.className;
	}
	public List<String> getEvtHandlerList()
	{
		return this.evtHandlerList;
	}
	public Map<String, Boolean> getLocalMethAppStateChangedMap()
	{
		return this.localMethAppStateChangedMap;
	}
	public boolean isInstalledMethNeeded()
	{
		return this.isInstalledMethNeeded;
	}
	public Set<String> getFieldNameSet()
	{
		return this.fieldNameSet;
	}

	@Override
	public void call(SourceUnit source, GeneratorContext context, ClassNode classNode) 
	{
		GElvisOpExprRemoval elvisRemoval = new GElvisOpExprRemoval();
//		GDateAttrAbstracter dateAttrAbstracter = new GDateAttrAbstracter();
		GUnhandledMethodRemoval unhandledMethRemoval = new GUnhandledMethodRemoval();
		GSystemPropertyHeaderRemoval sysPropHedRemoval = new GSystemPropertyHeaderRemoval(classNode.getName());
		GMethodInfoGetter gLMN;
		GInputInfoGetter gIIH;
		GInputNamesFormatter gInputNameFormatter;
		GMethodCalleeInfoGetter methCalleeInfoHandler;
		GArgTypeInferenceFromMethCall firstArgTypeInference;
		GArgTypeInferenceFromMethCallVarExpr secondArgTypeInference;
		Map<String, List<String>> callerInfoMap;
		Map<String, List<String>> varNames;
		Map<String, ArrayList<GParameter>> localMethods;
		GAppStateChangeDetector appStateChangeDetector = new GAppStateChangeDetector();
		GRedundantCodeRemoval redundantCodeRemoval;
//		GCastExprRemoval castExprRemoval = new GCastExprRemoval();
		GCompileErrorFixer compileErrorFixer;
		GDateExprAbstracter dateExprAbstracter = new GDateExprAbstracter();
		GWeatherFeatureAbstracter weatherFeatureAbstracter = new GWeatherFeatureAbstracter();
		List<String> originalInputNameList;
		GTryCatchStmtTransformer tryCatchStmtTransformer = new GTryCatchStmtTransformer();
		GHttpExprTransformer httpTransformer = new GHttpExprTransformer();
		GTimePropertyTransformer timePropTransformer = new GTimePropertyTransformer();
		List<String> httpGetVarList;
		
		/* Set current class name in GUtil */
		GUtil.currentClassName = classNode.getName();
		GUtil.setSourceUnit(source);
		tryCatchStmtTransformer.processAClassNode(classNode);
		httpTransformer.processAClassNode(classNode);
		httpGetVarList = httpTransformer.getHttpGetVarList();
		
		this.className = classNode.getName();
		classNode.visitContents(elvisRemoval);
		/* Abstract out date expression */
		classNode.visitContents(dateExprAbstracter);
		classNode.visitContents(timePropTransformer);
//		classNode.visitContents(dateAttrAbstracter);
		classNode.visitContents(weatherFeatureAbstracter);
//		classNode.visitContents(castExprRemoval);
		classNode.visitContents(appStateChangeDetector);
		this.localMethAppStateChangedMap = appStateChangeDetector.getLocalMethAppStateChangedMap();
		
		classNode.visitContents(sysPropHedRemoval);
		this.stateVarMap = sysPropHedRemoval.getStateVarList();
		this.fieldNameSet = new HashSet<String>();
		this.fieldNameSet.addAll(this.stateVarMap.keySet());
		this.fieldNameSet.addAll(httpGetVarList);
		
		/* Infer types of fields */
		{
			GFieldTypeInferrence fieldTypeInfer = new GFieldTypeInferrence(this.stateVarMap);
			fieldTypeInfer.processAClassNode(classNode);
			this.stateVarMap = fieldTypeInfer.getName2TypeMap();
		}
		
		/* Transform "null" to "0" */
		{
			GNullExprTransformer nullExprTransformer = new GNullExprTransformer(this.stateVarMap);
			classNode.visitContents(nullExprTransformer);
		}
		
		if(httpGetVarList.size() > 0)
		{
			/* Infer the type of httpGetVar */
			GHttpVarTypeInference httpVarTypeInfer = new GHttpVarTypeInference(this.stateVarMap, httpGetVarList);
			
			classNode.visitContents(httpVarTypeInfer);
			this.httpGet2VarTypeMap = httpVarTypeInfer.getHttpGet2VarTypeMap();
			this.httpGet2ValueRangeMap = httpVarTypeInfer.getHttpGet2ValueRangeMap();
		}
		else
		{
			this.httpGet2VarTypeMap = new HashMap<String, ClassNode>();
			this.httpGet2ValueRangeMap = new HashMap<String, List<String>>();
		}
		
		/* Fix compile errors of map variables */
		{
			GStateMapTreeBuilder stateMapTreeBuilder = new GStateMapTreeBuilder();
			classNode.visitContents(stateMapTreeBuilder);
			int mapDepth = stateMapTreeBuilder.getStateMapTree().size();
			
			if(mapDepth > 0)
			{
				switch(mapDepth)
				{
				case 1: this.stateMapType = GStateMapEnum.Int2IntMap; break;
				case 2: this.stateMapType = GStateMapEnum.Int2IIMMap; break;
				case 3: this.stateMapType = GStateMapEnum.Int2IIIMMap; break;
				default: this.stateMapType = GStateMapEnum.Int2IIIMMap;
				}
				GMapVarCompileErrorFixer mapVarErrorFixer = new GMapVarCompileErrorFixer(this.stateMapType);
				classNode.visitContents(mapVarErrorFixer);
			}
			else
			{
				this.stateMapType = GStateMapEnum.unknown;
			}
		}
		
		/* Get all input info */
		{
			GExpressionInfoGetter gMCV = new GExpressionInfoGetter();
			
			classNode.visitContents(gMCV);
			gIIH = new GInputInfoGetter(gMCV.getBexpressions());
			classNode.visitContents(gIIH);
			this.inputInfoList = gIIH.getInputInfoList();
			/* Process input info list */
			this.processInputInfoList();
			
			System.out.println("******************************************************");
			System.out.println("Input info list:");
			deviceInfoList = new ArrayList<GDeviceInputInfo>();
			for(GInputInfo inputInfo : this.inputInfoList)
			{
				if(inputInfo instanceof GDeviceInputInfo)
				{
					deviceInfoList.add((GDeviceInputInfo)inputInfo);
				}
				this.fieldNameSet.add(inputInfo.inputName);
				GUtil.printInputInfo(inputInfo);
			}
			
			System.out.println("******************************************************");
			System.out.println("Subscription info:");
			evtHandlerList = new ArrayList<String>();
			for(GSubscriptionInfo subscriptionInfo : gIIH.getSubscriptionInfoList())
			{
				System.out.println(subscriptionInfo.inputName + ", " + subscriptionInfo.subscribedAttribute
						+ ", " + subscriptionInfo.subscribedEvtType + ", " + subscriptionInfo.evtHandler);
				evtHandlerList.add(subscriptionInfo.evtHandler);
			}
			this.subscriptionInfoList = gIIH.getSubscriptionInfoList();
			GUtil.currentSmartAppEvtHandlerList = evtHandlerList;
			
			if(this.usedInGParser)
			{
				GLiteralBuilder.inputEnumList.addAll(gIIH.getEnumList());
				GConfigInfoManager.processSubscriptionInfo(classNode.getName(), this.subscriptionInfoList);
				GConfigInfoManager.processInputInfo(classNode.getName(), this.inputInfoList);
				GConfigInfoManager.setHttpGet2ValueRangeMap(this.className, this.httpGet2ValueRangeMap);
			}
		}
		
		/* Remove unhandled SmartThings' methods such as: 
		 * definition, preferences, subscribe, ... 
		 * */
		{
			GInputMethDiscovery inputMethDiscovery = new GInputMethDiscovery();
			classNode.visitContents(inputMethDiscovery);
			removeUnhandledSTMethods(classNode, inputMethDiscovery.getInputMethList());
		}
		classNode.visitContents(unhandledMethRemoval);
		
		
		/* Check if "installed" method is needed */
		{
			GMethCallInfoGetter methCallInfo = new GMethCallInfoGetter();
			
			this.isInstalledMethNeeded = false;
			for(MethodNode meth : classNode.getMethods())
			{
				if(meth.getName().equals("installed"))
				{
					meth.getCode().visit(methCallInfo);
					this.isInstalledMethNeeded = methCallInfo.isConcernedMethCalled();
					break;
				}
			}
		}
		
		/* Remove default methods of a SmartApp: installed and updated */
		if(this.isInstalledMethNeeded)
		{
			this.smartAppDefaultMethods.remove("installed");
			GConfigInfoManager.setInstalledMethNeeded(classNode.getName());
		}
		if(this.stateMapType != GStateMapEnum.unknown)
		{
			GConfigInfoManager.setStateMapUsed(classNode.getName(), this.stateMapType);
		}
		removeSmartAppDefaultMethods(classNode);
		
		/* Get all local methods' info */
		{
			gLMN = new GMethodInfoGetter(this.isInstalledMethNeeded);
			classNode.visitContents(gLMN);
			localMethods = gLMN.getLocalMethods();
			GUtil.currentSmartAppLocalMethods = localMethods.keySet();
			localMethSet = localMethods.keySet();
			
			System.out.println("******************************************************");
			System.out.println("Local methods:");
			for(Map.Entry<String, ArrayList<GParameter>> entry : localMethods.entrySet())
			{
				System.out.print(entry.getKey() + ": ");
				for(GParameter parm : entry.getValue())
				{
					System.out.print(parm.name + ": " + parm.type + ", ");
				}
				System.out.println();
			}
			
			/* Get callee info for each local method */
			methCalleeInfoHandler = new GMethodCalleeInfoGetter(localMethods.keySet());
			classNode.visitContents(methCalleeInfoHandler);
			calleeInfoMap = methCalleeInfoHandler.getCalleeInfo();
			System.out.println("******************************************************");
			System.out.println("Callee info:");
			for(Map.Entry<String, List<String>> entry : calleeInfoMap.entrySet())
			{
				System.out.print(entry.getKey() + ": ");
				for(String callee : entry.getValue())
				{
					System.out.print(callee + ", ");
				}
				System.out.println();
			}
			/* Get caller info */
			callerInfoMap = GUtil.getCallerInfo(calleeInfoMap);
			System.out.println("******************************************************");
			System.out.println("Caller info:");
			for(Map.Entry<String, List<String>> entry : callerInfoMap.entrySet())
			{
				System.out.print(entry.getKey() + ": ");
				for(String caller : entry.getValue())
				{
					System.out.print(caller + ", ");
				}
				System.out.println();
			}
			System.out.println("******************************************************");
		}
		
		/* Handle schedule method call expression such as runIn */
		{
			GScheduleExprTransformer scheduleExprTransformer = new GScheduleExprTransformer(this.localMethSet, this.fieldNameSet);
			scheduleExprTransformer.processAClassNode(classNode);
			this.scheduleTimeVarSet = scheduleExprTransformer.getScheduleTimeVarSet();
		}
		
		/* Transform delay commands */
		{
			GSTCommandDelayTransformer delayTransformer = new GSTCommandDelayTransformer(this.localMethSet);
			delayTransformer.processAClassNode(classNode);
		}
		
		/* Abstract math expression */
		{
			GMathExprAbstracter mathExprAbstracter = new GMathExprAbstracter();
			classNode.visitContents(mathExprAbstracter);
		}
		
		/* Add SmartThings common APIs to the classNode */
		this.addSTCommonAPIs(classNode);
		
		/* Change name format of input info */
		originalInputNameList = this.getInputNames();
		this.formatInputNames(classNode.getName());
		
		/* Add fields */
		addFields(source, classNode);
		
		/* To differentiate global variables between SmartApps, we need to
		 * to format the global variables' name as following:
		 * new variable name = class name + "_" + variable name 
		 * */
		{
			if(this.stateMapType != GStateMapEnum.unknown)
			{
				originalInputNameList.add("state");
			}
			gInputNameFormatter = new GInputNamesFormatter(originalInputNameList, 
					classNode.getName(), this.varName2TypeMap);
			classNode.visitContents(gInputNameFormatter);
		}
		
		/* Do data type inference for arguments and return type of local methods */
		{
			/* First trial to infer arg type of local methods */
			firstArgTypeInference = new GArgTypeInferenceFromMethCall(evtHandlerList, localMethods);
			classNode.visitContents(firstArgTypeInference);
			localMethods = firstArgTypeInference.getLocalMethods();
			
			System.out.println("******************************************************");
			System.out.println("Local methods after first trial of type inferrence:");
			for(Map.Entry<String, ArrayList<GParameter>> entry : localMethods.entrySet())
			{
				System.out.print(entry.getKey() + ": ");
				for(GParameter parm : entry.getValue())
				{
					System.out.print(parm.name + ": " + parm.type + ", ");
				}
				System.out.println();
			}
			System.out.println("******************************************************");
			
			/* Second trial to infer arg type of local methods */
			varNames = firstArgTypeInference.getVarNames();
			secondArgTypeInference = new GArgTypeInferenceFromMethCallVarExpr(localMethods, callerInfoMap, varNames);
			classNode.visitContents(secondArgTypeInference);
			localMethods = secondArgTypeInference.getLocalMethods();
			
			System.out.println("******************************************************");
			System.out.println("Local methods after second trial of type inferrence:");
			for(Map.Entry<String, ArrayList<GParameter>> entry : localMethods.entrySet())
			{
				System.out.print(entry.getKey() + ": ");
				for(GParameter parm : entry.getValue())
				{
					System.out.print(parm.name + ": " + parm.type + ", ");
				}
				System.out.println();
			}
			System.out.println("******************************************************");
			
			/* Final handler for type inference */
			handleLocalMethodParmsType(source, classNode, localMethods);
			if(this.usedInGParser)
			{
				GConfigInfoManager.processEvtVarNames(classNode.getName(), this.evtVarNameMap);
			}
			
			System.out.println("******************************************************");
			System.out.println("Local methods after final trial of type inferrence:");
			for(Map.Entry<String, ArrayList<GParameter>> entry : localMethods.entrySet())
			{
				System.out.print(entry.getKey() + ": ");
				for(GParameter parm : entry.getValue())
				{
					System.out.print(parm.name + ": " + parm.type + ", ");
				}
				System.out.println();
			}
			System.out.println("******************************************************");
		}
		
		/* Remove unhandled SmartThings' methods such as: 
		 * definition, preferences, subscribe, ... 
		 * */
//		removeUnhandledSTMethods(classNode);
//		classNode.visitContents(unhandledMethRemoval);
		
		/* Remove redundant code */
		redundantCodeRemoval = new GRedundantCodeRemoval(this.localMethSet);
		redundantCodeRemoval.processAClassNode(classNode);
		
		/* Transform "each" closure into for loop */
		{
			GEachClosureTransformer eachClosureTransformer = new GEachClosureTransformer(this.localMethSet);
			eachClosureTransformer.processAClassNode(classNode);
		}
		
		/* Add explicit type for method's parameters so that we can use @CompileStatic */
		{
			GMethParmTypeAssigner parmTypeAssigner = new GMethParmTypeAssigner(localMethods);
			classNode.visitContents(parmTypeAssigner);
		}
		
		/* Handler return types */
		{
			GReturnTypeHandler returnTypeHandler = new GReturnTypeHandler();
			GUtil.methNameTypeMap = new HashMap<String, ClassNode>();
			classNode.visitContents(returnTypeHandler);
		}
		
		/* Fix compile error due to the use of ST commands with list variable */
		compileErrorFixer = new GCompileErrorFixer(this.getMultipleDeviceNames(), this.localMethSet);
		compileErrorFixer.processAClassNode(classNode);
		
		/* Transform dynamic method calls */
		{
			GDynamicMethCallTransformer dynamicMethTransformer = new GDynamicMethCallTransformer(this.localMethSet);
			dynamicMethTransformer.processAClassNode(classNode);
			GConfigInfoManager.setAdditionalIntHttpGetVarRangeList(classNode.getName(), dynamicMethTransformer.getHttpResponseValueRangeList());
		}
		
		/* Add @CompileStatic annotation */
		addCompileStaticAnnotation(classNode);
	}
	
	private void addSTCommonAPIs(ClassNode classNode)
	{
		for(MethodNode meth : this.STCommonAPIs)
		{
			classNode.addMethod(meth);
		}
	}
	
	/* Remove default methods of a SmartApp: installed and updated 
	 * */
	private void removeSmartAppDefaultMethods(ClassNode classNode)
	{
		Iterator<MethodNode> iter = classNode.getMethods().iterator();
		
		while(iter.hasNext())
		{
			MethodNode meth = iter.next();
			
			if(this.smartAppDefaultMethods.contains(meth.getName()))
			{
				iter.remove();
			}
		}
	}
	
	/* Remove unhandled SmartThings' methods: definition and preferences
	 * */
	private void removeUnhandledSTMethods(ClassNode classNode, List<String> inputMethList)
	{
		for(Statement stmt : classNode.getModule().getStatementBlock().getStatements())
		{
			if(stmt instanceof ExpressionStatement)
			{
				Expression expr = ((ExpressionStatement) stmt).getExpression();
				
				if(expr instanceof MethodCallExpression)
				{
					/* Replace the current method call with a default null method call */
					ConstantExpression newMethod = new ConstantExpression("default_null_method");
					
					((MethodCallExpression) expr).setMethod(newMethod);
					((MethodCallExpression) expr).setArguments(MethodCallExpression.NO_ARGUMENTS);
				}
			}
		}
		
		for(String inputMeth : inputMethList)
		{
			List<MethodNode> methList = classNode.getMethods(inputMeth);
			
			if(methList != null)
			{
				for(MethodNode meth : methList)
				{
					classNode.removeMethod(meth);
				}
			}
		}
	}
	
	private void addCompileStaticAnnotation(ClassNode classNode)
	{
		classNode.addAnnotation(new AnnotationNode(ClassHelper.make(CompileStatic.class)));
		classNode.getModule().addStarImport("groovy.transform.CompileStatic");
	}
	
	private boolean toDoubleConvertible(ClassNode gType)
	{
		if((gType == ClassHelper.OBJECT_TYPE) || (gType == ClassHelper.int_TYPE) ||
				(gType == ClassHelper.Integer_TYPE) || (gType == ClassHelper.Long_TYPE))
		{
			return true;
		}
		
		return false;
	}
	
	private void addFields(SourceUnit source, ClassNode classNode)
	{
		ClassNode fieldType = null;
		
		this.varName2TypeMap = new HashMap<String, ClassNode>();
		
		/* Add SmartThings' default variable: location */
		try
		{
			fieldType = new ClassNode(source.getClassLoader().loadClass("STLocation"));
		}
		catch(ClassNotFoundException exc){
			System.out.println(exc.getMessage());
		}
		classNode.addField("location", Modifier.PRIVATE, fieldType, null);
		
		/* Add SmartThings' default variable: smartAppName_app */
		try
		{
			fieldType = new ClassNode(source.getClassLoader().loadClass("STApp"));
		}
		catch(ClassNotFoundException exc){
			System.out.println(exc.getMessage());
		}
		classNode.addField(classNode.getName() + "_app", Modifier.PRIVATE, fieldType, null);
		this.varName2TypeMap.put(classNode.getName() + "_app", fieldType);
		
		/* Add SmartThings' default variable: state */
		if(this.stateMapType != GStateMapEnum.unknown)
		{
			if(this.stateMapType == GStateMapEnum.Int2IntMap)
			{
				fieldType = ClassHelper.MAP_TYPE.getPlainNodeReference();
			}
			else
			{
				fieldType = GUtil.createGroovyO2MMapType();
			}
			classNode.addField(classNode.getName() + "_state", Modifier.PRIVATE, fieldType, null);
			this.varName2TypeMap.put(classNode.getName() + "_state", fieldType);
		}
		
		/* Add SmartThings' default variable: currentMode */
//		fieldType = GUtil.createGroovyType(source, "TEXT", false); /* String type */
//		classNode.addField("currentMode", Modifier.PRIVATE, fieldType, null);
//		this.varNameTypeMap.put("currentMode", fieldType);
		
		/* Add SmartApp's global variables */
		for(GInputInfo inputInfo : this.inputInfoList)
		{
			String typeName = null;
			
			if (inputInfo instanceof GDeviceInputInfo)
			{
				typeName = ((GDeviceInputInfo)inputInfo).deviceType;
			} else if (inputInfo instanceof GOtherInputInfo)
			{
				typeName = ((GOtherInputInfo)inputInfo).infoType;
			}
			
			if(typeName != null)
			{
				fieldType = GUtil.createGroovyType(source, typeName, inputInfo.isMultiple);
				classNode.addField(inputInfo.inputName, Modifier.PRIVATE, fieldType, null);
				
				this.varName2TypeMap.put(inputInfo.inputName, fieldType);
			}
			else
			{
				System.out.println("[GSmartAppPreProcessor][addFields] unexpected input info type " + inputInfo);
			}
			GUtil.varName2TypeMap = this.varName2TypeMap;
		}
		
		/* Add SmartApp's state variables */
		for(Map.Entry<String, ClassNode> entry : this.stateVarMap.entrySet())
		{
			String varName = entry.getKey();
			
			fieldType = entry.getValue();
			if(this.toDoubleConvertible(fieldType))
			{
				fieldType = ClassHelper.double_TYPE;
			}
			classNode.addField(varName, Modifier.PRIVATE, fieldType, null);
			
			this.varName2TypeMap.put(varName, fieldType);
		}
		
		/* Add created schedule time variables */
		for(String varName : this.scheduleTimeVarSet)
		{
			fieldType = GUtil.createGroovyType(source, "TIME", false); /* java.util.Date */
			classNode.addField(varName, Modifier.PRIVATE, fieldType, null);
			
			this.varName2TypeMap.put(varName, fieldType);
		}
		
		/* Add SmartApp's httpGet variables */
		for(Map.Entry<String, ClassNode> entry : this.httpGet2VarTypeMap.entrySet())
		{
			String varName = entry.getKey();
			
			fieldType = entry.getValue();
			if(this.toDoubleConvertible(fieldType))
			{
				fieldType = ClassHelper.double_TYPE;
			}
			classNode.addField(varName, Modifier.PRIVATE, fieldType, null);
			
			this.varName2TypeMap.put(varName, fieldType);
		}
	}
	
	/* If local method is an event handler, parameter type must be STEvent.
	 * Otherwise, if parameter type is OBJECT_TYPE, it will be assigned to Long_Type
	 * by default.
	 * */
	private void handleLocalMethodParmsType(SourceUnit source, ClassNode classNode, Map<String, ArrayList<GParameter>> localMethods)
	{
		List<String> evtVarNameList = new ArrayList<String>();
		
		for(Map.Entry<String, ArrayList<GParameter>> entry : localMethods.entrySet())
		{
			String methName = entry.getKey();
			ArrayList<GParameter> parmList = entry.getValue();
			
			if(GUtil.currentSmartAppEvtHandlerList.contains(methName))
			{
				if(parmList.size() == 1)
				{
					parmList.get(0).type = GUtil.createGroovyType(source, "STEvent", false);
					
					if(!this.evtVarNameMap.containsKey(methName))
					{
						this.evtVarNameMap.put(methName, parmList.get(0).name);
					}
					if(!evtVarNameList.contains(parmList.get(0).name))
					{
						/* Instead of using parameter, we declare a local field for STEvent */
						evtVarNameList.add(parmList.get(0).name);
						classNode.addField(parmList.get(0).name, Modifier.PRIVATE, parmList.get(0).type, null);
					}
				}
				else
				{
					System.out.println("[GSmartAppPreProcessor][handleLocalMethodParmsType] unexpected number of parameters for event handler, method: " + 
											methName + ", number of parameters: " + parmList.size());
				}
			}
			else
			{
				for(GParameter parm : parmList)
				{
					if(this.toDoubleConvertible(parm.type))
					{
						parm.type =  ClassHelper.double_TYPE;
					}
				}
			}
		}
	}
	
	private List<String> getInputNames()
	{
		List<String> result = new ArrayList<String>();
		
		for(GInputInfo inputInfo : this.inputInfoList)
		{
			result.add(inputInfo.inputName);
		}
		return result;
	}
	
	private void formatInputNames(String className)
	{
		for(GInputInfo inputInfo : this.inputInfoList)
		{
			inputInfo.inputName = className + "_" + inputInfo.inputName;
		}
	}
	
	private List<String> getMultipleDeviceNames()
	{
		List<String> result = new ArrayList<String>();
		
		for(GDeviceInputInfo device : this.deviceInfoList)
		{
			if(device.isMultiple)
			{
				result.add(device.inputName);
			}
		}
		return result;
	}
	
	private void processInputInfoList()
	{
		for(GInputInfo inputInfo : this.inputInfoList)
		{
			if(inputInfo instanceof GDeviceInputInfo)
			{
				GDeviceInputInfo device = (GDeviceInputInfo)inputInfo;
				
				if(GUtil.isAnUnknownDevice(device.deviceType))
				{
					String deviceType = GConfigInfoManager.getConfigDeviceType(GUtil.currentClassName, device.inputName);
					
					if(deviceType != null)
					{
						device.deviceType = deviceType;
					}
					else
					{
						System.out.println("[GSmartAppPreProcessor.processInputInfoList] missing specific configuration for " 
											+ device.inputName + ", device type = " + device.deviceType);
					}
				}
			}
		}
	}
}
