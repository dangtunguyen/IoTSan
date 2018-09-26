package edu.ksu.cis.bandera.jjjc.gparser.potentialriskscreener;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.GenericsType;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.stc.StaticTypesMarker;

import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;

public class GVarTypeInference extends ClassCodeVisitorSupport {
	/********************************************/
	private Set<String> varNameSet;
	private Map<String, String> varNameTypeMap;
	/********************************************/
	
	public GVarTypeInference(Set<String> varNameSet)
	{
		this.varNameSet = varNameSet;
		this.varNameTypeMap = new HashMap<String, String>();
	}
	
	/* Getters */
	public Map<String, String> getVarNameTypeMap()
	{
		return this.varNameTypeMap;
	}
	
	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}
	
	@Override
	public void visitBinaryExpression(BinaryExpression bex)
	{
		Expression leftExpr;
		
		leftExpr = bex.getLeftExpression();
		if(leftExpr instanceof VariableExpression)
		{
			String varName = ((VariableExpression) leftExpr).getName();
			
			if(varNameSet.contains(varName) && !this.varNameTypeMap.containsKey(varName))
			{
				ClassNode gType = bex.getRightExpression().getNodeMetaData(StaticTypesMarker.INFERRED_TYPE);
				
				if(gType != null)
				{
					String typeName = null;
					
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
					
					if(typeName != null)
					{
						this.varNameTypeMap.put(varName, typeName);
					}
				}
			}
		}
	}
}
