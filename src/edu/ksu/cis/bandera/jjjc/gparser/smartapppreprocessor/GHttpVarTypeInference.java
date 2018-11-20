package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.control.SourceUnit;

import edu.ksu.cis.bandera.jjjc.gparser.g2jconverter.GLiteralBuilder;
import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;

public class GHttpVarTypeInference extends ClassCodeVisitorSupport {
	/********************************************/
	private List<String> httpGetVarList;
	private Map<String, ClassNode> stateVarMap;
	private Map<String, ClassNode> httpGet2VarTypeMap;
	private Map<String, String> httpGet2VarNameMap;
	private Map<String, List<String>> httpGet2ValueRangeMap;
	/********************************************/
	
	public GHttpVarTypeInference(Map<String, ClassNode> stateVarMap, List<String> httpGetVarList)
	{
		this.stateVarMap = stateVarMap;
		this.httpGetVarList = httpGetVarList;
		
		this.httpGet2VarTypeMap = new HashMap<String, ClassNode>();
		for(String var : this.httpGetVarList)
		{
			this.httpGet2VarTypeMap.put(var, ClassHelper.OBJECT_TYPE);
		}
		this.httpGet2VarNameMap = new HashMap<String, String>();
		this.httpGet2ValueRangeMap = new HashMap<String, List<String>>();
	}
	
	/* Getters */
	public Map<String, ClassNode> getHttpGet2VarTypeMap()
	{
		return this.httpGet2VarTypeMap;
	}
	public Map<String, String> getHttpGet2VarNameMap()
	{
		return this.httpGet2VarNameMap;
	}
	public Map<String, List<String>> getHttpGet2ValueRangeMap()
	{
		return this.httpGet2ValueRangeMap;
	}
	
	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}
	
	private boolean isAHttpGetVar(Expression expr)
	{
		if(expr instanceof VariableExpression)
		{
			if(this.httpGetVarList.contains(((VariableExpression) expr).getName()))
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void visitBinaryExpression(BinaryExpression bex)
	{
		Expression leftExpr = bex.getLeftExpression();
		Expression rightExpr = bex.getRightExpression();
		
		if(this.isAHttpGetVar(leftExpr))
		{
			String httpGetVarName = ((VariableExpression) leftExpr).getName();
			List<String> valueRange = new ArrayList<String>();
			
			if(rightExpr instanceof VariableExpression)
			{
				String rightVarName = ((VariableExpression) rightExpr).getName();
				
				if(this.stateVarMap.containsKey(rightVarName))
				{
					ClassNode varType = this.stateVarMap.get(rightVarName);
					
					this.httpGet2VarTypeMap.put(httpGetVarName, varType);
					
					if((varType == ClassHelper.Boolean_TYPE) ||
							(varType == ClassHelper.boolean_TYPE))
					{
						valueRange.add("0");
						valueRange.add("1");
					}
				}
				else
				{
					this.httpGet2VarNameMap.put(httpGetVarName, rightVarName);
				}
			}
			else if(rightExpr instanceof ConstantExpression)
			{
				String rightExprStr = rightExpr.getText();
				
				if((rightExpr.getType() == ClassHelper.Boolean_TYPE) ||
						(rightExpr.getType() == ClassHelper.boolean_TYPE))
				{
					this.httpGet2VarTypeMap.put(httpGetVarName, ClassHelper.Boolean_TYPE);
					valueRange.add("0");
					valueRange.add("1");
				}
				else if(GUtil.isDigitList(rightExprStr))
				{
					this.httpGet2VarTypeMap.put(httpGetVarName, ClassHelper.long_TYPE);
					valueRange.add(rightExprStr);
				}
				else
				{
					int strValue = GLiteralBuilder.getIntValueFromStr(rightExprStr);
					
					if(strValue == 0)
					{
						GLiteralBuilder.inputEnumList.add(rightExprStr);
						strValue = GLiteralBuilder.getIntValueFromStr(rightExprStr);
					}
					rightExprStr = "" + strValue;
					
					this.httpGet2VarTypeMap.put(httpGetVarName, ClassHelper.STRING_TYPE);
					valueRange.add(rightExprStr);
				}
			}
			this.httpGet2ValueRangeMap.put(httpGetVarName, valueRange);
		}
		
		if(this.isAHttpGetVar(rightExpr))
		{
			String httpGetVarName = ((VariableExpression) rightExpr).getName();
			List<String> valueRange = new ArrayList<String>();
			
			if(leftExpr instanceof VariableExpression)
			{
				String leftVarName = ((VariableExpression) leftExpr).getName();
				
				if(this.stateVarMap.containsKey(leftVarName))
				{
					ClassNode varType = this.stateVarMap.get(leftVarName);
					
					this.httpGet2VarTypeMap.put(httpGetVarName, varType);
					
					if((varType == ClassHelper.Boolean_TYPE) ||
							(varType == ClassHelper.boolean_TYPE))
					{
						valueRange.add("0");
						valueRange.add("1");
					}
				}
				else
				{
					this.httpGet2VarNameMap.put(httpGetVarName, leftVarName);
				}
			}
			else if(leftExpr instanceof ConstantExpression)
			{
				String leftExprStr = leftExpr.getText();
				
				if((leftExpr.getType() == ClassHelper.Boolean_TYPE) ||
						(leftExpr.getType() == ClassHelper.boolean_TYPE))
				{
					this.httpGet2VarTypeMap.put(httpGetVarName, ClassHelper.Boolean_TYPE);
					valueRange.add("0");
					valueRange.add("1");
				}
				else if(GUtil.isDigitList(leftExprStr))
				{
					this.httpGet2VarTypeMap.put(httpGetVarName, ClassHelper.long_TYPE);
					valueRange.add(leftExprStr);
				}
				else
				{
					int strValue = GLiteralBuilder.getIntValueFromStr(leftExprStr);
					
					if(strValue == 0)
					{
						GLiteralBuilder.inputEnumList.add(leftExprStr);
						strValue = GLiteralBuilder.getIntValueFromStr(leftExprStr);
					}
					leftExprStr = "" + strValue;
					
					this.httpGet2VarTypeMap.put(httpGetVarName, ClassHelper.STRING_TYPE);
					valueRange.add(leftExprStr);
				}
			}
			this.httpGet2ValueRangeMap.put(httpGetVarName, valueRange);
		}
		
		super.visitBinaryExpression(bex);
	}
}
