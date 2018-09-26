package edu.ksu.cis.bandera.jjjc.gparser.g2jconverter;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.BooleanExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.DoWhileStatement;
import org.codehaus.groovy.ast.stmt.IfStatement;
import org.codehaus.groovy.ast.stmt.WhileStatement;
import org.codehaus.groovy.control.SourceUnit;

import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;

/* Before:
 * def map = state[mode]
 * After:
 * def map = state.get(mode)
 * */
public class GMapGetExprTransformer extends ClassCodeVisitorSupport {
	public GMapGetExprTransformer()
	{}

	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}

	private MethodCallExpression createMethCallExpr(PropertyExpression propExpr)
	{
		MethodCallExpression mce = null;
		Expression objExpr = propExpr.getObjectExpression();

		if(objExpr instanceof VariableExpression)
		{
			if(GUtil.isExprAMapType((VariableExpression) objExpr))
			{
				List<Expression> exprList = new ArrayList<Expression>();
				ArgumentListExpression argListExpr;

				/* Prepare argument list */
				exprList.add(propExpr.getProperty());
				argListExpr = new ArgumentListExpression(exprList);

				mce = new MethodCallExpression(
						(VariableExpression) objExpr,
						"get",
						argListExpr);
			}
		}

		return mce;
	}

	private MethodCallExpression createMethCallExpr(BooleanExpression boolExpr)
	{
		MethodCallExpression mce = null;
		Expression curExpr = boolExpr.getExpression();

		if(curExpr instanceof PropertyExpression)
		{
			mce = this.createMethCallExpr((PropertyExpression) curExpr);
		}
		else if(curExpr instanceof BinaryExpression)
		{
			mce = this.createMethCallExpr((BinaryExpression) curExpr);
		}

		return mce;
	}

	private MethodCallExpression createMethCallExpr(BinaryExpression binExpr)
	{
		MethodCallExpression mce = null;

		if(GUtil.isExprAMapAccess(binExpr))
		{
			Expression varExpr = binExpr.getLeftExpression();
			Expression keyExpr = binExpr.getRightExpression();

			List<Expression> exprList = new ArrayList<Expression>();
			ArgumentListExpression argListExpr;

			/* Prepare argument list */
			exprList.add(keyExpr);
			argListExpr = new ArgumentListExpression(exprList);

			mce = new MethodCallExpression(
					varExpr,
					"get",
					argListExpr);
		}

		return mce;
	}

	@Override
	public void visitArgumentlistExpression(ArgumentListExpression ale) {
		List<Expression> exprList = ale.getExpressions();

		for(int i = 0; i < exprList.size(); i++)
		{
			Expression curExpr = exprList.get(i);

			if(curExpr instanceof PropertyExpression)
			{
				MethodCallExpression mce = this.createMethCallExpr((PropertyExpression) curExpr);

				/* Replace the current property expression argument with the new mce */
				if(mce != null)
				{
					exprList.set(i, mce);
				}
			}
			else if(curExpr instanceof BinaryExpression)
			{
				MethodCallExpression mce = this.createMethCallExpr((BinaryExpression) curExpr);

				/* Replace the current property expression argument with the new mce */
				if(mce != null)
				{
					exprList.set(i, mce);
				}
			}
		}

		super.visitArgumentlistExpression(ale);
	}

	@Override
	public void visitBinaryExpression(BinaryExpression bex)
	{
		Expression leftExpr = bex.getLeftExpression();
		Expression rightExpr = bex.getRightExpression();

		/* Handle left expression */
		if(leftExpr instanceof PropertyExpression)
		{
			MethodCallExpression mce = this.createMethCallExpr((PropertyExpression) leftExpr);

			/* Replace the current property expression argument with the new mce */
			if(mce != null)
			{
				bex.setLeftExpression(mce);
			}
		}
		else if(leftExpr instanceof BinaryExpression)
		{
			MethodCallExpression mce = this.createMethCallExpr((BinaryExpression) leftExpr);

			/* Replace the current property expression argument with the new mce */
			if(mce != null)
			{
				bex.setLeftExpression(mce);
			}
		}

		/* Handle right expression */
		if(rightExpr instanceof PropertyExpression)
		{
			MethodCallExpression mce = this.createMethCallExpr((PropertyExpression) rightExpr);

			/* Replace the current property expression argument with the new mce */
			if(mce != null)
			{
				bex.setRightExpression(mce);
			}
		}
		else if(rightExpr instanceof BinaryExpression)
		{
			MethodCallExpression mce = this.createMethCallExpr((BinaryExpression) rightExpr);

			/* Replace the current property expression argument with the new mce */
			if(mce != null)
			{
				bex.setRightExpression(mce);
			}
		}

		super.visitBinaryExpression(bex);
	}

	@Override
	public void visitDoWhileLoop(DoWhileStatement loop) {
		BooleanExpression boolExpr = loop.getBooleanExpression();
		MethodCallExpression mce = this.createMethCallExpr(boolExpr);

		if(mce != null)
		{
			loop.setBooleanExpression(new BooleanExpression(mce));
		}

		super.visitDoWhileLoop(loop);
	}

	@Override
	public void visitIfElse(IfStatement ifElse) {
		BooleanExpression boolExpr = ifElse.getBooleanExpression();
		MethodCallExpression mce = this.createMethCallExpr(boolExpr);

		if(mce != null)
		{
			ifElse.setBooleanExpression(new BooleanExpression(mce));
		}

		super.visitIfElse(ifElse);
	}
	
	@Override
	public void visitWhileLoop(WhileStatement loop) {
		BooleanExpression boolExpr = loop.getBooleanExpression();
		MethodCallExpression mce = this.createMethCallExpr(boolExpr);

		if(mce != null)
		{
			loop.setBooleanExpression(new BooleanExpression(mce));
		}
		
        super.visitWhileLoop(loop);
    }
}
