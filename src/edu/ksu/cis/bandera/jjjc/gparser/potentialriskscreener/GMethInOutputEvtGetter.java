package edu.ksu.cis.bandera.jjjc.gparser.potentialriskscreener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.GenericsType;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MapExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.stc.StaticTypesMarker;

import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;

/* This class is used to discover all output events in a given
 * source code
 * */
public class GMethInOutputEvtGetter extends ClassCodeVisitorSupport {
	/********************************************/
	/* Output event */
	private List<GEventInfo> outputEvtList;
	/* outputVarNameEvtTypeMap contains the map from type-unknown variable name
	 * to event type.
	 * E.g.:
	 * code: mySwitch?.on()
	 * => one record <mySwitch, on>
	 * */
	private Map<String, String> outputVarNameEvtTypeMap;
	private String curVarName;
	
	/* Input event */
	private List<GEventInfo> inputEvtList;
	private Set<String> inputTypeUnknownVarSet;
	/********************************************/
	
	public GMethInOutputEvtGetter()
	{
		this.outputEvtList = new ArrayList<GEventInfo>();
		this.outputVarNameEvtTypeMap = new HashMap<String, String>();
		this.inputEvtList = new ArrayList<GEventInfo>();
		this.inputTypeUnknownVarSet = new HashSet<String>();
	}
	
	/* Getters */
	public Map<String, String> getOutputVarNameEvtTypeMap()
	{
		return this.outputVarNameEvtTypeMap;
	}
	public List<GEventInfo> getOutputEvtList()
	{
		return this.outputEvtList;
	}
	public List<GEventInfo> getInputEvtList()
	{
		return this.inputEvtList;
	}
	public Set<String> getInputTypeUnknownVarSet()
	{
		return this.inputTypeUnknownVarSet;
	}
	
	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}
	
	private String getTypeName(VariableExpression var)
	{
		String typeName = null;
		
		ClassNode gType = var.getNodeMetaData(StaticTypesMarker.INFERRED_TYPE);
		
		if(gType != null)
		{
			if(GUtil.toArrayConvertible(gType))
			{
				GenericsType[] genericsTypes = gType.getGenericsTypes();
				
				if(genericsTypes != null)
				{
					typeName = genericsTypes[0].getType().getName();
				}
			}
			else
			{
				typeName = gType.getName();
			}
		}
		this.curVarName = var.getName();
		
		return typeName;
	}
	
	private String getVarTypeName(MethodCallExpression mce)
	{
		String varTypeName = null;
		Expression objExpr = mce.getObjectExpression();
		
		if(objExpr instanceof VariableExpression)
		{
			varTypeName = this.getTypeName((VariableExpression) objExpr);
		}
		else if(objExpr instanceof BinaryExpression) /* switches[0].on() */
		{
			Expression leftExpr = ((BinaryExpression) objExpr).getLeftExpression();
			
			if(leftExpr instanceof VariableExpression)
			{
				varTypeName = this.getTypeName((VariableExpression) leftExpr);
			}
		}
		else if(objExpr instanceof MethodCallExpression) /* switches.get(0).off() */
		{
			varTypeName = this.getVarTypeName((MethodCallExpression) objExpr);
		}
		
		return varTypeName;
	}
	
	private String getVarTypeName(PropertyExpression propExpr)
	{
		String varTypeName = null;
		Expression objExpr = propExpr.getObjectExpression();
		
		if(objExpr instanceof VariableExpression)
		{
			varTypeName = this.getTypeName((VariableExpression) objExpr);
		}
		else if(objExpr instanceof BinaryExpression) /* switches[0].status */
		{
			Expression leftExpr = ((BinaryExpression) objExpr).getLeftExpression();
			
			if(leftExpr instanceof VariableExpression)
			{
				varTypeName = this.getTypeName((VariableExpression) leftExpr);
			}
		}
		else if(objExpr instanceof MethodCallExpression) /* switches.get(0).lastActivity */
		{
			varTypeName = this.getVarTypeName((MethodCallExpression) objExpr);
		}
		else if(objExpr instanceof PropertyExpression)
		{
			varTypeName = this.getVarTypeName((PropertyExpression) objExpr);
		}
		
		return varTypeName;
	}
	
	private void handleSTCommand(String deviceType, String methName)
	{
		if(deviceType != null)
		{
			String attribute = GUtil.getAttributeFromDeviceTypeAndCommand(deviceType, methName);
			
			if(attribute != null)
			{
				outputEvtList.add(new GEventInfo(attribute, methName));
			}
		}
		else
		{
			List<String> attrList = GUtil.getAttributeListFromSTCommand(methName);
			
			for(String attr : attrList)
			{
				outputEvtList.add(new GEventInfo(attr, methName));
			}
		}
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
		
		if(GUtil.isALocationSetMeth(methText)) /* Output event */
		{
			this.outputEvtList.add(new GEventInfo("location", ""));
		}
		else if(GUtil.isALocationGetMeth(methText)) /* Input event */
		{
			this.inputEvtList.add(new GEventInfo("location", ""));
		}
		else if(GUtil.isASTCommand(methText)) /* Output event */
		{
			Expression objExpr = mce.getObjectExpression();
			boolean skip = false;
			
			if(objExpr instanceof VariableExpression)
			{
				if(((VariableExpression) objExpr).getName().equals("this"))
				{
					skip = true;
				}
			}
			
			if(!skip)
			{
				String deviceType = this.getVarTypeName(mce);
				this.handleSTCommand(deviceType, methText);
			}
			else if(GUtil.isASecuritySensitiveNetworkAccess(methText))
			{
				outputEvtList.add(new GEventInfo("network", methText));
			}
		}
		else if(GUtil.isADeviceGetMethod(methText)) /* Input event */
		{
			String deviceType = this.getVarTypeName(mce);
			
			if(deviceType != null)
			{
				String attribute = GUtil.getAttributeFromDeviceType(deviceType);
				
				if(attribute != null)
				{
					inputEvtList.add(new GEventInfo(attribute, ""));
				}
			}
			else
			{
				this.inputTypeUnknownVarSet.add(this.curVarName);
			}
		}
		else if(GUtil.isADeviceSpecialGetMethod(methText)) /* Input event */
		{
			Expression args = mce.getArguments();
			List<Expression> exprList = GUtil.buildExprList(args);
			Expression attrExpr = null;
			String attribute = null;
			
			/* Get first non-MapExpression argument */
			for(Expression arg : exprList)
			{
				if(!(arg instanceof MapExpression))
				{
					attrExpr = arg;
					break;
				}
			}
			if(attrExpr != null)
			{
				if(attrExpr instanceof ConstantExpression)
				{
					attribute = attrExpr.getText();
				}
			}
			if(attribute != null)
			{
				inputEvtList.add(new GEventInfo(attribute, ""));
			}
			else
			{
				String deviceType = this.getVarTypeName(mce);
				
				if(deviceType != null)
				{
					attribute = GUtil.getAttributeFromDeviceType(deviceType);
					if(attribute != null)
					{
						inputEvtList.add(new GEventInfo(attribute, ""));
					}
				}
				else
				{
					this.inputTypeUnknownVarSet.add(this.curVarName);
				}
			}
		}
		
		super.visitMethodCallExpression(mce);
	}
	
	/* Input event */
	@Override
	public void visitPropertyExpression(PropertyExpression propExpr) {
		String prop = propExpr.getPropertyAsString();
		
		if(GUtil.isADeviceProperty(prop)) /* switches.get(0).lastActivity */
		{
			String deviceType = this.getVarTypeName(propExpr);
			
			if(deviceType != null)
			{
				String attribute = GUtil.getAttributeFromDeviceType(deviceType);
				
				if(attribute != null)
				{
					inputEvtList.add(new GEventInfo(attribute, ""));
				}
			}
			else
			{
				this.inputTypeUnknownVarSet.add(this.curVarName);
			}
		}
		else if(GUtil.isASTProperty(prop))
		{
			String attribute = GUtil.getAttributeFromSTProperty(prop);
			
			if(attribute != null)
			{
				inputEvtList.add(new GEventInfo(attribute, ""));
			}
		}
		
		super.visitPropertyExpression(propExpr);
	}
}
