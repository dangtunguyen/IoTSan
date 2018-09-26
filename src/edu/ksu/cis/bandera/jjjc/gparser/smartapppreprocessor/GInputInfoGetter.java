package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.ListExpression;
import org.codehaus.groovy.ast.expr.MapEntryExpression;
import org.codehaus.groovy.ast.expr.MapExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.NamedArgumentListExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.control.SourceUnit;

import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;

/* This class is used to get all of the input info in a block
 * of source code: monitor device, control device, number, date,
 * time, text, ... 
 * */
public class GInputInfoGetter extends ClassCodeVisitorSupport
{
	/********************************************/
	ArrayList<BinaryExpression> allbexprs;
	boolean isSTModeEnforced;
	List<GInputInfo> inputInfoList;
	List<GSubscriptionInfo> subscriptionInfoList;
	List<String> enumList;
	/********************************************/
	
	public GInputInfoGetter(ArrayList<BinaryExpression> allbex)
	{
		allbexprs = allbex;
		isSTModeEnforced = false;
		inputInfoList = new ArrayList<GInputInfo>();
		subscriptionInfoList = new ArrayList<GSubscriptionInfo>();
		enumList = new ArrayList<String>();
	}
	
	/************************** Start of utility methods *******************/
	private boolean containInputInfo(String inputName)
	{
		for(GInputInfo inputInfo : inputInfoList)
		{
			if(inputInfo.inputName.equals(inputName))
			{
				return true;
			}
		}
		return false;
	}
	
	private boolean containSubscriptionInfo(String deviceName, String attribute, String evtType, String evtHandler)
	{
		for(GSubscriptionInfo subscriptionInfo : subscriptionInfoList)
		{
			if(subscriptionInfo.inputName.equals(deviceName) && subscriptionInfo.subscribedAttribute.equals(attribute)
					&& subscriptionInfo.subscribedEvtType.equals(evtType) && subscriptionInfo.evtHandler.equals(evtHandler))
			{
				return true;
			}
		}
		return false;
	}
	
	private boolean isMultipleInput(Expression args)
	{
		List<Expression> exprList = GUtil.buildExprList(args);
		Iterator<Expression> argIt;
		
		argIt = exprList.iterator();
		while(argIt.hasNext()) 
		{
			Expression arg = argIt.next();
			
			if (arg instanceof MapExpression)
			{
				MapExpression mex = (MapExpression)arg;
				Iterator<MapEntryExpression> entryExprIt;
				
				entryExprIt = mex.getMapEntryExpressions().iterator();
				while(entryExprIt.hasNext())
				{
					MapEntryExpression entryExpr = entryExprIt.next();
					Expression keyExpr = entryExpr.getKeyExpression();
					Expression valExpr = entryExpr.getValueExpression();
					
					if(keyExpr instanceof ConstantExpression && valExpr instanceof ConstantExpression)
					{
						String keyExprTxt = keyExpr.getText().toLowerCase();
						String valExprTxt = valExpr.getText().toLowerCase();
						
						if (keyExprTxt.equals("multiple") && valExprTxt.equals("true"))
						{
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	private String getInputName(Expression args)
	{
		List<Expression> exprList = GUtil.buildExprList(args);
		Iterator<Expression> argIt;
		
		/* Search for a map with key equals "name" */
		argIt = exprList.iterator();
		while(argIt.hasNext()) 
		{
			Expression arg = argIt.next();
			
			if (arg instanceof MapExpression)
			{
				MapExpression mex = (MapExpression)arg;
				Iterator<MapEntryExpression> entryExprIt = mex.getMapEntryExpressions().iterator();
				
				while(entryExprIt.hasNext())
				{
					MapEntryExpression entryExpr = entryExprIt.next();
					Expression keyExpr = entryExpr.getKeyExpression();
					Expression valExpr = entryExpr.getValueExpression();
					
					if(keyExpr instanceof ConstantExpression && valExpr instanceof ConstantExpression)
					{
						String keyExprTxt = keyExpr.getText().toLowerCase();
						String valExprTxt = valExpr.getText();
						
						if (keyExprTxt.equals("name"))
						{
							return valExprTxt;
						}
					}
				}
			}
		}

		/* name should be the first ConstantExpression */
		argIt = exprList.iterator();
		while(argIt.hasNext()) 
		{
			Expression arg = argIt.next();
			
			if (arg instanceof ConstantExpression)
			{
				return arg.getText();
			}
		}
		
		return null;
	}
	
	private String getInputType(Expression args)
	{
		List<Expression> exprList = GUtil.buildExprList(args);
		Iterator<Expression> argIt;
		boolean isNamePresent = false;
		int index, typeIndex;
		
		/* Search for a map with key equals "name" */
		argIt = exprList.iterator();
		while(argIt.hasNext()) 
		{
			Expression arg = argIt.next();
			
			if (arg instanceof MapExpression)
			{
				MapExpression mex = (MapExpression)arg;
				Iterator<MapEntryExpression> entryExprIt = mex.getMapEntryExpressions().iterator();
				
				while(entryExprIt.hasNext())
				{
					MapEntryExpression entryExpr = entryExprIt.next();
					Expression keyExpr = entryExpr.getKeyExpression();
					Expression valExpr = entryExpr.getValueExpression();
					
					if(keyExpr instanceof ConstantExpression && valExpr instanceof ConstantExpression)
					{
						String keyExprTxt = keyExpr.getText().toLowerCase();
						String valExprTxt = valExpr.getText();
						
						if (keyExprTxt.equals("type"))
						{
							return valExprTxt;
						} else if(keyExprTxt.equals("name"))
						{
							isNamePresent = true;
						}
					}
				}
			}
		}

		/* type should be the second ConstantExpression */
		argIt = exprList.iterator();
		index = 1;
		typeIndex = isNamePresent? 1 : 2;
		while(argIt.hasNext()) 
		{
			Expression arg = argIt.next();
			
			if (arg instanceof ConstantExpression)
			{
				if (index == typeIndex)
				{
					return arg.getText();
				}
				index++;
			}
		}
		
		return null;
	}
	
	private void processEnumType(Expression args)
	{
		List<Expression> exprList = GUtil.buildExprList(args);
		Iterator<Expression> argIt;
		
		/* Search for a map with key equals "options" */
		argIt = exprList.iterator();
		while(argIt.hasNext()) 
		{
			Expression arg = argIt.next();
			
			if (arg instanceof MapExpression)
			{
				MapExpression mex = (MapExpression)arg;
				Iterator<MapEntryExpression> entryExprIt = mex.getMapEntryExpressions().iterator();
				
				while(entryExprIt.hasNext())
				{
					MapEntryExpression entryExpr = entryExprIt.next();
					Expression keyExpr = entryExpr.getKeyExpression();
					Expression valExpr = entryExpr.getValueExpression();
					
					if(keyExpr instanceof ConstantExpression && valExpr instanceof ListExpression)
					{
						String keyExprTxt = keyExpr.getText().toLowerCase();
						
						if (keyExprTxt.equals("options"))
						{
							for(Expression enumExpr : ((ListExpression) valExpr).getExpressions())
							{
								if(enumExpr instanceof ConstantExpression)
								{
									String enumVal = enumExpr.getText();
									
									if(!this.enumList.contains(enumVal))
									{
										this.enumList.add(enumVal);
									}
								}
							}
						}
					}
				}
			}
		}
	}
	/************************** End of utility methods *******************/
	
	/* 1. input "temperature1", "number", title: "Temperature"
	 * 2. input(name: "color", type: "enum", title: "Color", options: ["Red","Green","Blue","Yellow"])
	 * */
	@Override
	public void visitMethodCallExpression(MethodCallExpression mce)
	{
		String methText;
		
		if(mce.getMethodAsString() == null)
		{
			//dynamic methods
			methText = mce.getText();
		}
		else
		{
			methText = mce.getMethodAsString();
		}
		
		/* Two ways to request mode info:
		 * 1. input "modes", "mode", title: "only when mode is", multiple: true, required: false
		 * => SmartApp will decide how to use the mode info
		 * 2. mode(title: "set for specific mode(s)"): HUB will allow this smartApp to be
		 * executed in the selected mode(s)
		 * => We need to handle the second case also
		 * */
		
		if(methText.equals("mode"))
		{
			/* The SmartThings will allow the execution SmartApp in only these modes
			 * We need the selected modes from user
			 * Example: mode title: "Set for specific mode(s)", required: false 
			 * */
			isSTModeEnforced = true;
		}
		//if you wondering what this awful "ifSet" is,
		//then you have some app author to thank. In his
		//infinite wisdom, he decided to redefine input -> ifSet
		else if(methText.equals("input") ||
			methText.equals("ifSet"))
		{
			boolean isHandled = false;
			Expression args = mce.getArguments();
			List<Expression> exprList = GUtil.buildExprList(args);
			Iterator<Expression> argIt;
			
			/* Get list of enum options */
			processEnumType(args);
			
			argIt = exprList.iterator();
			while(argIt.hasNext()) 
			{
				Expression arg = argIt.next();
				
				//we are searching for capability.*
				if(arg instanceof ConstantExpression)
				{
					String txt = arg.getText();
					
					if(txt.contains("capability.") || txt.contains("device.")) /* A device */
					{
						String inputName = null;
						String deviceType; 
						int index = txt.indexOf('.');
						
						deviceType = txt.substring(index+1, txt.length());
						inputName = getInputName(args);
						
						if (inputName != null)
						{
							if (this.containInputInfo(inputName))
							{
								System.out.println("[GInputInfoGetter] Duplicated name:" + inputName);
							}
							else
							{
								boolean isMultiple = false;
								GDeviceInputInfo deviceInfo;
								
								isMultiple = isMultipleInput(args);
								deviceInfo = new GDeviceInputInfo(inputName, deviceType, isMultiple);
								this.inputInfoList.add(deviceInfo);
							}
						}
						isHandled = true;
					}
					else if(txt.equals("mode")) /* A security sensitive info */
					{
						String inputName = null;
						
						inputName = getInputName(args);
						if (inputName != null)
						{
							if (this.containInputInfo(inputName))
							{
								System.out.println("[GInputInfoGetter] Duplicated name:" + inputName);
							}
							else
							{
								boolean isMultiple = false;
								GOtherInputInfo otherInputInfo;
								
								isMultiple = isMultipleInput(args);
								otherInputInfo = new GOtherInputInfo(inputName, txt, isMultiple);
								this.inputInfoList.add(otherInputInfo);
							}
						}
						isHandled = true;
					}
				}
				else if(arg instanceof MapExpression ||
						arg instanceof VariableExpression ||
						arg instanceof PropertyExpression)
				{
					MapExpression mex = null;
					
					if(arg instanceof PropertyExpression) /* E.g.: foo.bar */
					{
						String property = ((PropertyExpression) arg).getText();
						Iterator<BinaryExpression> bexprIt;
						
						//search thru our list of binary expressions
						bexprIt = allbexprs.iterator();
						while(bexprIt.hasNext())
						{
							BinaryExpression bexpr = bexprIt.next();
							
							if(bexpr.getLeftExpression() instanceof PropertyExpression)
							{
								PropertyExpression leftProperty = (PropertyExpression) bexpr.getLeftExpression();
								if(leftProperty.getText().equals(property))
								{
									if(bexpr.getRightExpression() instanceof ConstantExpression)
									{
										ConstantExpression right = (ConstantExpression) bexpr.getRightExpression();
										String rightText = right.getText();
										System.out.println("[GInputInfoGetter] followed a Binary Property Constant expression");
										System.out.println(rightText + " is assigned to " + leftProperty.getText());
										
										if(rightText.contains("capability."))
										{
											System.out.println(rightText);
											isHandled = true;
										}
									}
								}
							}
						}
					}
					else if(arg instanceof VariableExpression)
					{
						//run thru the AST once to find the DeclarationExpression
						//for this VariableExpression
						//we expect the rightExpression() of the declaration
						//to be a MapExpression. We only try to do this
						//once as a matter of policy. Ideally, could write
						//a recursive function to traverse multiple levels.
						VariableExpression argvex = (VariableExpression) arg;
						String varName = argvex.getName();
						Iterator<BinaryExpression> bexpIt;
						
						bexpIt = allbexprs.iterator();
						while(bexpIt.hasNext())
						{
							BinaryExpression bexpr = bexpIt.next();
							
							//input mapVariable
							if(bexpr.getRightExpression() instanceof MapExpression)
							{
								if(bexpr.getLeftExpression() instanceof VariableExpression)
								{
									//the left is a Variable, right is a Map
									String testVarName = ((VariableExpression) bexpr.getLeftExpression()).getName();
									
									if(testVarName.equals(varName))
									{
										System.out.println("[GInputInfoGetter] followed 1 binary expr: " + varName);
										mex = (MapExpression) bexpr.getRightExpression();
										break;
									}
								}
							}
							//input "strVar", varWithSomeDeclEarlier
							else if(bexpr.getRightExpression() instanceof ConstantExpression)
							{
								if(bexpr.getLeftExpression() instanceof VariableExpression)
								{
									//the left is a Variable, right is a constant
									String testVarName = ((VariableExpression) bexpr.getLeftExpression()).getName();
									
									if(testVarName.equals(varName))
									{
										String txt = bexpr.getRightExpression().getText();
										
										if(txt.contains("capability.") || txt.contains("device."))
										{
											System.out.println(txt);
											isHandled = true;
										}
									}
								}
							}
						}
					}
					else
					{
						mex = (MapExpression) arg;
					}
					
					if (mex != null) {
						Iterator<MapEntryExpression> entryExprIt = mex.getMapEntryExpressions().iterator();
						
						while(entryExprIt.hasNext())
						{
							Expression valExpr = entryExprIt.next().getValueExpression();
							
							if(valExpr instanceof ConstantExpression)
							{
								ConstantExpression cap_exp = (ConstantExpression) valExpr;
								String txt = cap_exp.getText();
								
								if(txt.contains("capability.") || txt.contains("device.")) /* A device */
								{
									String inputName = null;
									String deviceType; 
									int index = txt.indexOf('.');
									
									deviceType = txt.substring(index+1, txt.length());
									inputName = getInputName(args);
									
									if (inputName != null)
									{
										if (this.containInputInfo(inputName))
										{
											System.out.println("[GInputInfoGetter] Duplicated name:" + inputName);
										}
										else
										{
											boolean isMultiple = false;
											GDeviceInputInfo deviceInfo;
											
											isMultiple = isMultipleInput(args);
											deviceInfo = new GDeviceInputInfo(inputName, deviceType, isMultiple);
											this.inputInfoList.add(deviceInfo);
										}
									}
									isHandled = true;
								}
								else if(txt.equals("mode")) /* A security sensitive info */
								{
									String inputName = null;
									
									inputName = getInputName(args);
									if (inputName != null)
									{
										if (this.containInputInfo(inputName))
										{
											System.out.println("[GInputInfoGetter] Duplicated name:" + inputName);
										}
										else
										{
											boolean isMultiple = false;
											GOtherInputInfo otherInputInfo;
											
											isMultiple = isMultipleInput(args);
											otherInputInfo = new GOtherInputInfo(inputName, txt, isMultiple);
											this.inputInfoList.add(otherInputInfo);
										}
									}
									isHandled = true;
								}
							}
						}
					}
				}
				else if(arg instanceof NamedArgumentListExpression)
				{
					NamedArgumentListExpression nnale = (NamedArgumentListExpression) arg;
					Iterator<MapEntryExpression> entryExprIt;
					
					entryExprIt = nnale.getMapEntryExpressions().iterator();
					while(entryExprIt.hasNext())
					{
						Expression valExpr = entryExprIt.next().getValueExpression();	
						
						if(valExpr instanceof ConstantExpression)
						{
							ConstantExpression cap_exp = (ConstantExpression) valExpr;
							String txt = cap_exp.getText();
							
							if(txt.contains("capability.") || txt.contains("device."))
							{
								System.out.println(txt);
								isHandled = true;
							}
						}
					}
				}
			}
			
			/* Process other input info type */
			if(!isHandled)
			{
				String inputName = null;
				String inputType = null;
				
				inputName = getInputName(args);
				inputType = getInputType(args);
				if((inputName != null) && (inputType != null))
				{
					if(this.containInputInfo(inputName))
					{
						System.out.println("[GInputInfoGetter] Duplicate input name:" + inputName);
					}
					else
					{
						boolean isMultiple = isMultipleInput(args);
						GOtherInputInfo otherInputInfo;
						
						otherInputInfo = new GOtherInputInfo(inputName, inputType, isMultiple);
						this.inputInfoList.add(otherInputInfo);
					}
				}
			}
		} else if(methText.equals("subscribeToCommand"))
		{
			/* void subscribeToCommand(device, commandName, handlerMethod) 
			 * 1. Each device needs to broadcast it received commands so that
			 * its subscribers execute their handlers
			 * 2. We need to handle this case
			 * */
			Expression args = mce.getArguments();
			List<Expression> exprList = GUtil.buildExprList(args);
			
			if(exprList.size() == 3) /* Device state subscriptions must have 3 parameters */
			{
				if(exprList.get(0) instanceof VariableExpression && exprList.get(1) instanceof ConstantExpression 
						&& exprList.get(2) instanceof VariableExpression)
				{
					ConstantExpression cexp = (ConstantExpression) exprList.get(1);
					VariableExpression vExpr0 = (VariableExpression) exprList.get(0);
					VariableExpression vExpr2 = (VariableExpression) exprList.get(2);
					String evtHandler = vExpr2.getName();
					String deviceName = vExpr0.getName();
					String cexptext = cexp.getText();
					String attrUse = cexptext;
					String evtType = cexptext;
					
					/* [March 31, 2017] Thomas: 
					 * Example: one record of subscriptionInfoList has the following format
					 * <themotion, motion, active, motionDetectedHandler>
					 * */
					if(!this.containSubscriptionInfo(deviceName, attrUse, evtType, evtHandler))
					{
						GSubscriptionInfo subscriptionInfo = new GSubscriptionInfo(deviceName, 
								attrUse, evtType, evtHandler);
						subscriptionInfoList.add(subscriptionInfo);
					}
				}
				else
				{
					System.out.println("[GInputInfoGetter] subscribeToCommand: not a ConstantExpression!");
				}
			}
		}
		else if(methText.equals("subscribe"))
		{
			/* void subscribe(deviceOrDevices, String attributeName, handlerMethod)
			 * void subscribe(deviceOrDevices, String attributeNameAndValue, handlerMethod)
			 * void subscribe(Location location, handlerMethod)
			 * void subscribe(Location location, String eventName, handlerMethod)
			 * void subscribe(app, handlerMethod)
			 * */
			
			boolean skipCheck = false;
			Expression args = mce.getArguments();
			List<Expression> exprList = GUtil.buildExprList(args);
			
			if(exprList.size() >= 3) /* Device state subscriptions must have 3 parameters */
			{
				//handle location and app subscriptions
				if(exprList.get(0) instanceof VariableExpression)
				{
					VariableExpression argvex0 = (VariableExpression) exprList.get(0);
					
					if(argvex0.getName().equals("app"))
					{
						/* We need to handle this case */
						skipCheck = true;
						System.out.println("[GInputInfoGetter] We need to handle this case!!!");
					}
				}
				
				if(!skipCheck)
				{
					//we have a variable, constant, variable generally
					//if we don't, flag this by spitting out text on the console
					//so we can analyze and see how to deal with it
					if(exprList.get(0) instanceof VariableExpression && exprList.get(1) instanceof ConstantExpression 
						&& exprList.get(2) instanceof VariableExpression)
					{
						ConstantExpression cexp = (ConstantExpression) exprList.get(1);
						VariableExpression vExpr0 = (VariableExpression) exprList.get(0);
						VariableExpression vExpr2 = (VariableExpression) exprList.get(2);
						String evtHandler = vExpr2.getName();
						String deviceName = vExpr0.getName();
						String cexptext = cexp.getText();
						String attrUse = cexptext;
						String evtType = "";
						
						if(cexptext.contains("."))
						{
							//strip out the dot and stuff after it
							int index = cexptext.indexOf('.');
							attrUse = cexptext.substring(0, index);
							
							evtType = cexptext.substring(index+1, cexptext.length());
						}
						
						/* [March 31, 2017] Thomas: 
						 * Example: one record of subscriptionInfoList has the following format
						 * <themotion, motion, active, motionDetectedHandler>
						 * */
						if(!this.containSubscriptionInfo(deviceName, attrUse, evtType, evtHandler))
						{
							GSubscriptionInfo subscriptionInfo = new GSubscriptionInfo(deviceName, 
									attrUse, evtType, evtHandler);
							subscriptionInfoList.add(subscriptionInfo);
						}
					}
					else
					{
						System.out.println("[GInputInfoGetter] subscribe: not a ConstantExpression!");
					}
				}
			}
			else if(exprList.size() == 2) /* Location mode change subscriptions may have 2 parameters */
			{
				/* subscribe(Location location, handlerMethod)
				 * subscribe(Location location, String eventName, handlerMethod)
				 * subscribe(app, appTouchMethod) //subscribe to touch Events for this app
				 * */
				if((exprList.get(0) instanceof VariableExpression) && (exprList.get(1) instanceof VariableExpression))
				{
					VariableExpression vExpr0 = (VariableExpression) exprList.get(0);
					VariableExpression vExpr1 = (VariableExpression) exprList.get(1);
					String evtHandler = vExpr1.getName();
					String deviceName = vExpr0.getName();
					String attr = "";
					
					if(deviceName.equals("location"))
					{
						attr = "mode";
					}
					
					if(!this.containSubscriptionInfo(deviceName, attr, "", evtHandler) && !deviceName.equals("app"))
					{
						GSubscriptionInfo subscriptionInfo = new GSubscriptionInfo(deviceName, 
								attr, "", evtHandler);
						subscriptionInfoList.add(subscriptionInfo);
					}
				}
				else
				{
					System.out.println("[GInputInfoGetter] subscribe: unexpected subscription command!!!");
				}
			}
		}
		else if(methText.equals("schedule") || methText.equals("runOnce"))
		{
			Expression args = mce.getArguments();
			List<Expression> exprList = GUtil.buildExprList(args);
			
			if(exprList.size() == 2)
			{
				if((exprList.get(0) instanceof VariableExpression) && (exprList.get(1) instanceof VariableExpression))
				{
					VariableExpression vExpr0 = (VariableExpression) exprList.get(0);
					VariableExpression vExpr1 = (VariableExpression) exprList.get(1);
					String evtHandler = vExpr1.getName();
					String deviceName = vExpr0.getName();
					
					if(!this.containSubscriptionInfo(deviceName, "time", "", evtHandler))
					{
						GSubscriptionInfo subscriptionInfo = new GSubscriptionInfo(deviceName, 
								"time", "", evtHandler);
						subscriptionInfoList.add(subscriptionInfo);
					}
				}
				else if((exprList.get(0) instanceof VariableExpression) && (exprList.get(1) instanceof ConstantExpression))
				{
					VariableExpression vExpr0 = (VariableExpression) exprList.get(0);
					ConstantExpression vExpr1 = (ConstantExpression) exprList.get(1);
					String evtHandler = vExpr1.getText();
					String deviceName = vExpr0.getName();
					
					if(!this.containSubscriptionInfo(deviceName, "time", "", evtHandler))
					{
						GSubscriptionInfo subscriptionInfo = new GSubscriptionInfo(deviceName, 
								"time", "", evtHandler);
						subscriptionInfoList.add(subscriptionInfo);
					}
				}
				else
				{
					System.out.println("[GInputInfoGetter] schedule: unexpected subscription command!!!");
				}
			}
		}
		/* We need to handle this case:
		 * runOnce(mytime, someHandlerMethod)
		 * schedule(myTime, someHandlerMethod)
		 * */
		
		super.visitMethodCallExpression(mce);
	}
	
	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}
	
	public List<GInputInfo> getInputInfoList()
	{
		return this.inputInfoList;
	}
	
	public List<GSubscriptionInfo> getSubscriptionInfoList()
	{
		return this.subscriptionInfoList;
	}
	
	public List<String> getEnumList()
	{
		return this.enumList;
	}
}