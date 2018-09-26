package edu.ksu.cis.bandera.jjjc.gparser.g2jconverter;

import java.util.Arrays;
import java.util.List;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.control.SourceUnit;

/* Before: scheduleNightTime.time < timeNow
 * After: scheduleNightTime < timeNow
 * */
public class GRedundantPropertyRemoval extends ClassCodeVisitorSupport {
	/********************************************/
	private List<String> redundantPropNameList = Arrays.asList("time");
	/********************************************/
	
	public GRedundantPropertyRemoval() {}
	
	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}
	
	private Expression processPropertyExpression(PropertyExpression propExpr)
	{
		Expression newExpr = null;
		String propName = propExpr.getPropertyAsString();
		Expression objExpr = propExpr.getObjectExpression();
		
		while(objExpr instanceof PropertyExpression)
		{
			propName = ((PropertyExpression)objExpr).getPropertyAsString();
			objExpr = ((PropertyExpression)objExpr).getObjectExpression();
		}
		
		if(this.redundantPropNameList.contains(propName))
		{
			if(objExpr instanceof VariableExpression)
			{
				newExpr = objExpr;
			}
		}
		
		return newExpr;
	}
	private Expression processExpression(Expression expr)
	{
		Expression newExpr = null;
		
		if(expr instanceof PropertyExpression)
		{
			newExpr = this.processPropertyExpression((PropertyExpression) expr);
		}
		else if(expr instanceof MethodCallExpression)
		{
			newExpr = this.processExpression(((MethodCallExpression)expr).getObjectExpression());
		}
		
		return newExpr;
	}
	private void processBinaryExpression(BinaryExpression bex)
	{
		Expression leftExpr = bex.getLeftExpression();
		Expression rightExpr = bex.getRightExpression();
		
		/* Handle leftExpr */
		if(leftExpr instanceof BinaryExpression)
		{
			this.processBinaryExpression((BinaryExpression)leftExpr);
		}
		else
		{
			Expression newExpr = this.processExpression(leftExpr);
			
			if(newExpr != null)
			{
				bex.setLeftExpression(newExpr);
			}
		}
		
		/* Handle rightExpr */
		if(rightExpr instanceof BinaryExpression)
		{
			this.processBinaryExpression((BinaryExpression)rightExpr);
		}
		else
		{
			Expression newExpr = this.processExpression(rightExpr);
			
			if(newExpr != null)
			{
				bex.setRightExpression(newExpr);
			}
		}
	}
	
	@Override
	public void visitBinaryExpression(BinaryExpression bex)
	{
		this.processBinaryExpression(bex);
		super.visitBinaryExpression(bex);
	}
}
