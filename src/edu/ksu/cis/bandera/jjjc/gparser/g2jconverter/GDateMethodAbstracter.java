package edu.ksu.cis.bandera.jjjc.gparser.g2jconverter;

import java.util.Arrays;
import java.util.List;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.BooleanExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.stmt.DoWhileStatement;
import org.codehaus.groovy.ast.stmt.IfStatement;
import org.codehaus.groovy.ast.stmt.WhileStatement;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.syntax.Token;
import org.codehaus.groovy.syntax.Types;

import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;

/* Before: if (start.before(now) && stop.after(now)) {
 * After if (start < now && stop > now) {
 * */
public class GDateMethodAbstracter extends ClassCodeVisitorSupport {
	/********************************************/
	private List<String> dateMethList;
	/********************************************/
	
	public GDateMethodAbstracter()
	{
		dateMethList = Arrays.asList("before", "after", "next");
	}
	
	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}
	
	private Expression buildExpression(MethodCallExpression mce)
	{
		Expression bex = null;
		String methName = mce.getMethodAsString();
		
		if(this.dateMethList.contains(methName))
		{
			if(methName.equals("next"))
			{
				bex = mce.getObjectExpression();
			}
			else
			{
				Token operation = null;
				
				switch(methName)
				{
				case "before": operation = Token.newSymbol(Types.COMPARE_LESS_THAN, -1, -1); break;
				case "after": operation = Token.newSymbol(Types.COMPARE_GREATER_THAN, -1, -1); break;
				}
				
				if(operation != null)
				{
					List<Expression> exprList = GUtil.buildExprList(mce.getArguments());
					
					if(exprList.size() == 1)
					{
						Expression objExpr = mce.getObjectExpression();
						bex = new BinaryExpression(objExpr, operation, exprList.get(0));
					}
					else
					{
						System.out.println("[GDateMethodAbstracter] unexpected number of arguments: " + exprList.size());
					}
				}
			}
		}
		return bex;
	}
	
	private BinaryExpression processBinaryExpression(BinaryExpression bex)
	{
		Expression leftExpr = bex.getLeftExpression();
		Expression rightExpr = bex.getRightExpression();
		
		/* Handle left expression */
		if(leftExpr instanceof BinaryExpression)
		{
			leftExpr = processBinaryExpression((BinaryExpression) leftExpr);
		}
		else if(leftExpr instanceof MethodCallExpression)
		{
			Expression subBex = buildExpression((MethodCallExpression) leftExpr);
			
			if(subBex != null)
			{
				leftExpr = subBex;
			}
		}
		bex.setLeftExpression(leftExpr);
		
		/* Handle right expression */
		if(rightExpr instanceof BinaryExpression)
		{
			rightExpr = processBinaryExpression((BinaryExpression) rightExpr);
		}
		else if(rightExpr instanceof MethodCallExpression)
		{
			Expression subBex = buildExpression((MethodCallExpression) rightExpr);
			
			if(subBex != null)
			{
				rightExpr = subBex;
			}
		}
		bex.setRightExpression(rightExpr);
		
		return bex;
	}
	
	private BooleanExpression createNewBooleanExpression(BooleanExpression boolExpr)
	{
		BooleanExpression newBoolExpr = null;
		Expression expr = boolExpr.getExpression();
		
		if(expr instanceof MethodCallExpression)
		{
			Expression bex = this.buildExpression((MethodCallExpression)expr);
			
			if(bex != null)
			{
				newBoolExpr = new BooleanExpression(bex);
			}
		}
		else if(expr instanceof BinaryExpression)
		{
			BinaryExpression bex = this.processBinaryExpression((BinaryExpression)expr);
			
			newBoolExpr = new BooleanExpression(bex);
		}
		
		return newBoolExpr;
	}
	
	@Override
	public void visitIfElse(IfStatement ifElse) {
		BooleanExpression boolExpr = ifElse.getBooleanExpression();
		BooleanExpression newBoolExpr = this.createNewBooleanExpression(boolExpr);
		
		if(newBoolExpr != null)
		{
			ifElse.setBooleanExpression(newBoolExpr);
		}
		
        super.visitIfElse(ifElse);
    }
	
	@Override
	public void visitDoWhileLoop(DoWhileStatement loop) {
		BooleanExpression boolExpr = loop.getBooleanExpression();
		BooleanExpression newBoolExpr = this.createNewBooleanExpression(boolExpr);
		
		if(newBoolExpr != null)
		{
			loop.setBooleanExpression(newBoolExpr);
		}
		
        super.visitDoWhileLoop(loop);
    }
	
	@Override
	public void visitWhileLoop(WhileStatement loop) {
		BooleanExpression boolExpr = loop.getBooleanExpression();
		BooleanExpression newBoolExpr = this.createNewBooleanExpression(boolExpr);
		
		if(newBoolExpr != null)
		{
			loop.setBooleanExpression(newBoolExpr);
		}
		
        super.visitWhileLoop(loop);
    }
	
	@Override
	public void visitBinaryExpression(BinaryExpression bex)
	{
		this.processBinaryExpression(bex);
		super.visitBinaryExpression(bex);
	}
}
