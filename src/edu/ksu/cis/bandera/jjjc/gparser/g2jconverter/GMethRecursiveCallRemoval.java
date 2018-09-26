package edu.ksu.cis.bandera.jjjc.gparser.g2jconverter;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.control.SourceUnit;

/* Before: 
 * def doorOpenTooLong() {
 * ...
 * doorOpenTooLong()
 * ...
 * }
 * After:
 * def doorOpenTooLong() {
 * ...
 * default_null_method()
 * ...
 * }
 * */
public class GMethRecursiveCallRemoval extends ClassCodeVisitorSupport {
	/********************************************/
	private String currentMehtName;
	/********************************************/
	
	public GMethRecursiveCallRemoval(String currentMehtName)
	{
		this.currentMehtName = currentMehtName;
	}
	
	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}
	
	@Override
	public void visitMethodCallExpression(MethodCallExpression mce)
	{
		String methName = mce.getMethodAsString();
		
		if(methName.equals(this.currentMehtName))
		{
			Expression objExpr = mce.getObjectExpression();
			
			if(objExpr instanceof VariableExpression)
			{
				if(((VariableExpression)objExpr).getName().equals("this"))
				{
					mce.setObjectExpression(new VariableExpression("this"));
					mce.setMethod(new ConstantExpression("default_null_method"));
					mce.setArguments(MethodCallExpression.NO_ARGUMENTS);
				}
			}
		}
	}
}
