package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import java.util.List;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.ConstructorCallExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.control.SourceUnit;

/* Before: def minusFive = new Date(minutes: now.minutes - 5)
 * After: def minusFive = new Date()
 * */
public class GDateExprAbstracter extends ClassCodeVisitorSupport {
	public GDateExprAbstracter()
	{}

	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}

	@Override
	public void visitBinaryExpression(BinaryExpression bex)
	{
		Expression rightExpr = bex.getRightExpression();

		if((rightExpr instanceof ConstructorCallExpression) && (rightExpr.getType().getName().equals("Date")))
		{
			ConstructorCallExpression newRightExpr = new ConstructorCallExpression(rightExpr.getType(), MethodCallExpression.NO_ARGUMENTS);
			bex.setRightExpression(newRightExpr);
		}
		else if(rightExpr instanceof MethodCallExpression)
		{
			String methName = ((MethodCallExpression)rightExpr).getMethodAsString();

			/* def dateString = new Date().format("yyyy-MM-dd")
			 * state.lastRun = new Date().toSystemFormat()
			 * result: 
			 * def dateString = new Date().format("yyyy-MM-dd")
			 * state.lastRun = new Date()
			 * */
			if(methName.equals("format"))
			{
				Expression objExpr = ((MethodCallExpression)rightExpr).getObjectExpression();

				if((objExpr instanceof ConstructorCallExpression) && (objExpr.getType().getName().equals("Date")))
				{
					ConstructorCallExpression newObjExpr = new ConstructorCallExpression(objExpr.getType(), MethodCallExpression.NO_ARGUMENTS);
					((MethodCallExpression)rightExpr).setObjectExpression(newObjExpr);
				}
			}
			else if(methName.equals("toSystemFormat"))
			{
				Expression objExpr = ((MethodCallExpression)rightExpr).getObjectExpression();

				if((objExpr instanceof ConstructorCallExpression) && (objExpr.getType().getName().equals("Date")))
				{
					ConstructorCallExpression newRigtExpr = new ConstructorCallExpression(objExpr.getType(), MethodCallExpression.NO_ARGUMENTS);
					bex.setRightExpression(newRigtExpr);
				}
			}
		}

		super.visitBinaryExpression(bex);
	}

	@Override
	public void visitArgumentlistExpression(ArgumentListExpression ale) {
		List<Expression> exprList = ale.getExpressions();

		for(int i = 0; i < exprList.size(); i++)
		{
			Expression curExpr = exprList.get(i);

			if((curExpr instanceof ConstructorCallExpression) && (curExpr.getType().getName().equals("Date")))
			{
				ConstructorCallExpression newArg = new ConstructorCallExpression(curExpr.getType(), MethodCallExpression.NO_ARGUMENTS);
				exprList.set(i, newArg);
			}
		}

		super.visitArgumentlistExpression(ale);
	}

	@Override
	public void visitReturnStatement(ReturnStatement statement) {
		Expression expr = statement.getExpression();
		
		if((expr instanceof ConstructorCallExpression) && (expr.getType().getName().equals("Date")))
		{
			ConstructorCallExpression newExpr = new ConstructorCallExpression(expr.getType(), MethodCallExpression.NO_ARGUMENTS);
			statement.setExpression(newExpr);
		}
		
		super.visitReturnStatement(statement);
	}
}
