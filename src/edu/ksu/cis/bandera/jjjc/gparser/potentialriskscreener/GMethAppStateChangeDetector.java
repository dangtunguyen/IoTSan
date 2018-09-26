package edu.ksu.cis.bandera.jjjc.gparser.potentialriskscreener;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.PostfixExpression;
import org.codehaus.groovy.ast.expr.PrefixExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.control.SourceUnit;

/* This class is used to check if any app's state is changed in the code
 * E.g.: state.modeStartTime = 0
 * => appStateChanged = true
 * */
public class GMethAppStateChangeDetector extends ClassCodeVisitorSupport {
	/********************************************/
	private boolean appStateChanged;
	/********************************************/
	
	public GMethAppStateChangeDetector() {
		appStateChanged = false;
	}
	
	/* Getters */
	public boolean getAppStateChanged()
	{
		return this.appStateChanged;
	}
	
	private void checkAppStateChanged(PropertyExpression propExpr)
	{
		Expression objExpr = propExpr.getObjectExpression();
		
		while(objExpr instanceof PropertyExpression)
		{
			objExpr = propExpr.getObjectExpression();
		}
		
		if(objExpr instanceof VariableExpression)
		{
			if(((VariableExpression) objExpr).getName().equals("state"))
			{
				this.appStateChanged = true;
			}
		}
	}
	
	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}
	
	@Override
	public void visitPostfixExpression(PostfixExpression postExpr) {
		Expression expr = postExpr.getExpression();
		
		if(expr instanceof PropertyExpression)
		{
			this.checkAppStateChanged((PropertyExpression) expr);
		}
		
		super.visitPostfixExpression(postExpr);
	}
	
	@Override
	public void visitPrefixExpression(PrefixExpression preExpr) {
		Expression expr = preExpr.getExpression();
		
		if(expr instanceof PropertyExpression)
		{
			this.checkAppStateChanged((PropertyExpression) expr);
		}
		super.visitPrefixExpression(preExpr);
	}
	
	@Override
	public void visitBinaryExpression(BinaryExpression bex) {
		if(bex.getOperation().getText().equals("="))
		{
			Expression leftExpr = bex.getLeftExpression();
			
			if(leftExpr instanceof PropertyExpression)
			{
				this.checkAppStateChanged((PropertyExpression) leftExpr);
			}
		}
		super.visitBinaryExpression(bex);
	}
}
