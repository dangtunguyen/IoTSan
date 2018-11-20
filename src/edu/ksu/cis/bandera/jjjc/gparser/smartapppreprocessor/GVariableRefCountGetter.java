package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.control.SourceUnit;

public class GVariableRefCountGetter extends ClassCodeVisitorSupport {
	/********************************************/
	private Map<String, Integer> varRefCountMap;
	/********************************************/
	
	public GVariableRefCountGetter(Set<String> varList)
	{
		this.varRefCountMap = new HashMap<String, Integer>();
		for(String var : varList)
		{
			this.varRefCountMap.put(var, 0);
		}
	}
	
	/* Getters */
	public Map<String, Integer> getVarUsageCountMap()
	{
		return this.varRefCountMap;
	}
	
	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}
	
	@Override
	public void visitVariableExpression(VariableExpression expr) {
		String var = expr.getName();
		
		/* Check if the visited variables is a local variable */
		if(this.varRefCountMap.containsKey(var))
		{
			/* Increase reference count */
			this.varRefCountMap.put(var, this.varRefCountMap.get(var)+1);
		}
		
		super.visitVariableExpression(expr);
    }
}
