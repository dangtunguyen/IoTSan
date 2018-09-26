package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import java.util.List;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.control.SourceUnit;

import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;

/* Before: def isOpen = Math.abs(latestThreeAxisState.xyzValue.z) > 250
 * After: def isOpen = latestThreeAxisState.xyzValue.z > 250
 * */
public class GMathExprAbstracter extends ClassCodeVisitorSupport{
	public GMathExprAbstracter() {}
	
	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}
	
	private void processBinaryExpression(BinaryExpression bex)
	{
		Expression leftExpr = bex.getLeftExpression();
		Expression rightExpr = bex.getRightExpression();
		
		/* Handle left expression */
		if(leftExpr instanceof MethodCallExpression)
		{
			Expression objExpr = ((MethodCallExpression) leftExpr).getObjectExpression();
			
			if(objExpr instanceof VariableExpression)
			{
				if(((VariableExpression) objExpr).getName().equals("Math"))
				{
					List<Expression> argList = GUtil.buildExprList(((MethodCallExpression) leftExpr).getArguments());
					
					if(argList.size() > 0)
					{
						bex.setLeftExpression(argList.get(0));
					}
					else
					{
						System.out.println("[GMathExprAbstracter.processBinaryExpression] unexpected number of arguments");
					}
				}
			}
		}
		else if(leftExpr instanceof BinaryExpression)
		{
			this.processBinaryExpression((BinaryExpression) leftExpr);
		}
		
		/* Handle right expression */
		if(rightExpr instanceof MethodCallExpression)
		{
			Expression objExpr = ((MethodCallExpression) rightExpr).getObjectExpression();
			
			if(objExpr instanceof VariableExpression)
			{
				if(((VariableExpression) objExpr).getName().equals("Math"))
				{
					List<Expression> argList = GUtil.buildExprList(((MethodCallExpression) rightExpr).getArguments());
					
					if(argList.size() > 0)
					{
						bex.setRightExpression(argList.get(0));
					}
					else
					{
						System.out.println("[GMathExprAbstracter.processBinaryExpression] unexpected number of arguments");
					}
				}
			}
		}
		else if(rightExpr instanceof BinaryExpression)
		{
			this.processBinaryExpression((BinaryExpression) rightExpr);
		}
	}
	
	@Override
	public void visitBinaryExpression(BinaryExpression bex)
	{
		this.processBinaryExpression(bex);
		super.visitBinaryExpression(bex);
	}
}
