package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.control.SourceUnit;

import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;

public class GArgTypeInferenceFromMethCall extends ClassCodeVisitorSupport{
	/********************************************/
	private List<String> evtHandlers;
	private List<String> localMethNames;
	private Map<String, ArrayList<GParameter>> localMethods;
	private Map<String, List<String>> varNames;
	/********************************************/
	
	public GArgTypeInferenceFromMethCall(List<String> evtHandlers, Map<String, ArrayList<GParameter>> localMethods)
	{
		this.evtHandlers = evtHandlers;
		this.localMethods = localMethods;
		this.localMethNames = new ArrayList<String>();
		this.varNames = new HashMap<String, List<String>>();
		
		for(String methName : localMethods.keySet())
		{
			this.localMethNames.add(methName);
		}
	}
	
	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}
	
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
		
		/* The event handler always has one parameter: evt, thus we don't need to handle event handlers */
		if(!this.evtHandlers.contains(methText) && this.localMethNames.contains(methText))
		{
			Expression args = mce.getArguments();
			List<Expression> exprList = GUtil.buildExprList(args);
			
			if(exprList.size() != this.localMethods.get(methText).size())
			{
				System.out.println("Argument size mismatch:" + exprList.size() + " v.s. " + this.localMethods.get(methText).size());
			}
			else
			{
				int index = 0;
				List<String> params = new ArrayList<String>();
				
				for(Expression expr : exprList)
				{
					/* Get names of parameters */
					if(expr instanceof VariableExpression)
					{
						params.add(((VariableExpression)expr).getName());
					}
					else
					{
						params.add(GUtil.getNullStr());
					}
					
					/* Infer types of parameters */
					if (this.localMethods.get(methText).get(index).type == ClassHelper.OBJECT_TYPE)
					{
						boolean done = false;
						
						if(expr instanceof VariableExpression)
						{
							String varName = ((VariableExpression)expr).getName();
							
							if(GUtil.varName2TypeMap.containsKey(varName))
							{
								done = true;
								this.localMethods.get(methText).get(index).type = GUtil.varName2TypeMap.get(varName);
							}
							else if(GUtil.isVarAnAppStrType(varName))
							{
								done = true;
								this.localMethods.get(methText).get(index).type = ClassHelper.STRING_TYPE;
							}
						}
						else if(expr instanceof PropertyExpression)
						{
							Expression propExpr = ((PropertyExpression) expr).getProperty();
							
							if(propExpr instanceof ConstantExpression)
							{
								String propName = propExpr.getText();
								
								if(GUtil.isPropStrType(propName))
								{
									done = true;
									this.localMethods.get(methText).get(index).type = ClassHelper.STRING_TYPE;
								}
							}
						}
						
						if(!done)
						{
							ClassNode argType = expr.getType();
							
							if((argType == ClassHelper.DYNAMIC_TYPE) || (argType == ClassHelper.OBJECT_TYPE))
							{
								if(expr instanceof MethodCallExpression)
								{
									this.localMethods.get(methText).get(index).type = 
											GUtil.getTypeFromMethCall((MethodCallExpression)expr, true);
								}
								/* We need to handle "else" case using other inference methods */
							}
							else if ((argType == ClassHelper.GSTRING_TYPE) || (argType == ClassHelper.STRING_TYPE))
							{
								this.localMethods.get(methText).get(index).type = ClassHelper.STRING_TYPE;
							}
							else
							{
								this.localMethods.get(methText).get(index).type = argType;
							}
						}
					}
					index++;
				}
				if(!varNames.containsKey(methText))
				{
					varNames.put(methText, params);
				}
			}
		}
	}
	
	public Map<String, ArrayList<GParameter>> getLocalMethods()
	{
		return this.localMethods;
	}
	public Map<String, List<String>> getVarNames()
	{
		return this.varNames;
	}
}
