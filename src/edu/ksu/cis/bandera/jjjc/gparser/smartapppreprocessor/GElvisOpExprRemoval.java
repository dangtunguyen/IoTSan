package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import java.util.List;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.GStringExpression;
import org.codehaus.groovy.ast.expr.TernaryExpression;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.ForStatement;
import org.codehaus.groovy.control.SourceUnit;

/* This class is used to replace every ElvisOperatorExpression with its true expression
 * E.g.: def switchArr = (switches ?: [])
 * => result: def switchArr = switches
 * */
public class GElvisOpExprRemoval extends ClassCodeVisitorSupport{

	public GElvisOpExprRemoval(){}
	
	private Expression processTernaryExpression(TernaryExpression terExpr)
	{
		Expression result = null;
		Expression trueExpr = terExpr.getTrueExpression();
		Expression falseExpr = terExpr.getFalseExpression();
		
		while(trueExpr instanceof TernaryExpression)
		{
			falseExpr = ((TernaryExpression) trueExpr).getFalseExpression();
			trueExpr = ((TernaryExpression) trueExpr).getTrueExpression();
		}
		
		if(trueExpr instanceof GStringExpression)
		{
			result = falseExpr;
		}
		else
		{
			result = trueExpr;
		}
		
		return result;
	}
	
	private void processBinaryExpression(BinaryExpression bex)
	{
		Expression leftExpr = bex.getLeftExpression();
		Expression rightExpr = bex.getRightExpression();
		
		/* Handle left expression */
		if(leftExpr instanceof TernaryExpression)
		{
			Expression trueExpr = processTernaryExpression((TernaryExpression) leftExpr);
			bex.setLeftExpression(trueExpr);
		}
		else if(leftExpr instanceof BinaryExpression)
		{
			this.processBinaryExpression((BinaryExpression) leftExpr);
		}
		
		/* Handle left expression */
		if(rightExpr instanceof TernaryExpression)
		{
			Expression trueExpr = processTernaryExpression((TernaryExpression) rightExpr);
			bex.setRightExpression(trueExpr);
		}
		else if(rightExpr instanceof BinaryExpression)
		{
			this.processBinaryExpression((BinaryExpression) rightExpr);
		}
	}
	
	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}
	
	@Override
	 public void visitForLoop(ForStatement forLoop) {
		Expression collectionExpr = forLoop.getCollectionExpression();
		
		if(collectionExpr instanceof TernaryExpression)
		{
			Expression trueExpr = processTernaryExpression((TernaryExpression) collectionExpr);
			forLoop.setCollectionExpression(trueExpr);
		}
		
        super.visitForLoop(forLoop);
    }
	
	@Override
	public void visitBinaryExpression(BinaryExpression bex)
	{
		this.processBinaryExpression(bex);
		super.visitBinaryExpression(bex);
	}
	
	@Override
	public void visitExpressionStatement(ExpressionStatement exprStmt) {
        Expression expr = exprStmt.getExpression();
		
        if(expr instanceof TernaryExpression)
        {
			Expression trueExpr = processTernaryExpression((TernaryExpression) expr);
			exprStmt.setExpression(trueExpr);
		}
		
        super.visitExpressionStatement(exprStmt);
    }
	
	@Override
	public void visitArgumentlistExpression(ArgumentListExpression ale) {
		List<Expression> exprList = ale.getExpressions();
		
		for(int i = 0; i < exprList.size(); i++)
		{
			Expression curExpr = exprList.get(i);
			
			if(curExpr instanceof TernaryExpression)
			{
				Expression trueExpr = processTernaryExpression((TernaryExpression) curExpr);
				exprList.set(i, trueExpr);
			}
		}
		super.visitArgumentlistExpression(ale);
	}
}
