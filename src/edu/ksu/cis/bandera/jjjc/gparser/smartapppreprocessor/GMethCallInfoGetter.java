package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import java.util.Arrays;
import java.util.List;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.control.SourceUnit;

/* This class is used to check if any method other than default
 * methods (e.g., "subscribe", "unsubscribe") is called in the code
 * */
public class GMethCallInfoGetter extends ClassCodeVisitorSupport {
	/********************************************/
	private boolean isConcernedMethCalled;
	private List<String> defaultMethList;
	/********************************************/
	
	public GMethCallInfoGetter()
	{
		this.isConcernedMethCalled = false;
		this.defaultMethList = Arrays.asList("subscribe", "unsubscribe", "default_null_method");
	}
	
	/* Getters */
	public boolean isConcernedMethCalled()
	{
		return this.isConcernedMethCalled;
	}
	
	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}
	
	@Override
	public void visitMethodCallExpression(MethodCallExpression mce)
	{
		String methName = mce.getMethodAsString();
		
		if(!this.defaultMethList.contains(methName))
		{
			this.isConcernedMethCalled = true;
		}
		
		super.visitMethodCallExpression(mce);
	}
}
