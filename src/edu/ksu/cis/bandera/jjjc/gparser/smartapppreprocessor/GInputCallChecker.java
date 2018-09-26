package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.control.SourceUnit;

public class GInputCallChecker extends ClassCodeVisitorSupport {
	/********************************************/
	/* This variable is set to true when either method "input"
	 * or "ifSet" is called */
	private boolean isInputCalled;
	/********************************************/
	
	public GInputCallChecker()
	{
		this.isInputCalled = false;
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
		
		if(methText.equals("input") || methText.equals("ifSet"))
		{
			this.isInputCalled = true;
		}
		
		super.visitMethodCallExpression(mce);
	}
	
	public boolean isInputMethCalled()
	{
		return this.isInputCalled;
	}
}
