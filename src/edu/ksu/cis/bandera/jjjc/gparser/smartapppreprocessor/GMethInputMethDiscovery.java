package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import java.util.Arrays;
import java.util.List;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.control.SourceUnit;

public class GMethInputMethDiscovery extends ClassCodeVisitorSupport {
	/********************************************/
	private List<String> inputMethNameList = Arrays.asList("input", "section");
	private boolean isInputMethUsed;
	/********************************************/
	
	public GMethInputMethDiscovery() {
		isInputMethUsed = false;
	}
	
	/* Getters */
	public boolean isInputMethUsed()
	{
		return this.isInputMethUsed;
	}
	
	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}
	
	@Override
	public void visitMethodCallExpression(MethodCallExpression mce)
	{
		String methName = mce.getMethodAsString();
		
		if((methName != null) && (this.inputMethNameList.contains(methName)))
		{
			this.isInputMethUsed = true;
		}
		
		super.visitMethodCallExpression(mce);
	}
}
