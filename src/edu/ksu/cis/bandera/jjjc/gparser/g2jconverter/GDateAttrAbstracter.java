package edu.ksu.cis.bandera.jjjc.gparser.g2jconverter;

import java.util.Arrays;
import java.util.List;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.CastExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.control.SourceUnit;

public class GDateAttrAbstracter extends ClassCodeVisitorSupport {
	/********************************************/
	/* These attributes' names will be replaced with "date"
	 * */
	private List<String> dateAttrNameList = Arrays.asList("dateValue", "rawDateCreated", "dateCreated");
	/********************************************/
	
	public GDateAttrAbstracter()
	{}
	
	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}
	
	private String getNewAttrName(PropertyExpression expr)
	{
		String newAttrName = null;
		String attrName = expr.getPropertyAsString();
		
		if(dateAttrNameList.contains(attrName))
		{
			newAttrName = "date";
		}
		
		return newAttrName;
	}
	
	/* E.g.: sensor.rawDateCreated.time
	 * => sensor.date.time
	 * */
	private void handleNestedPropExpr(PropertyExpression expr)
	{
		Expression objExpr = expr.getObjectExpression();
		
		if(objExpr instanceof PropertyExpression)
		{
			String newAttrName = getNewAttrName((PropertyExpression) objExpr);

			if(newAttrName != null)
			{
				/* Replace current attribute's name with a new value */
				PropertyExpression newObjExpr = new PropertyExpression(
						((PropertyExpression) objExpr).getObjectExpression(), newAttrName);
				expr.setObjectExpression(newObjExpr);
				handleNestedPropExpr(newObjExpr);
			}
			else
			{
				handleNestedPropExpr((PropertyExpression) objExpr);
			}
		}
	}
	
	@Override
	public void visitArgumentlistExpression(ArgumentListExpression ale) {
		List<Expression> exprList = ale.getExpressions();
		
		for(int i = 0; i < exprList.size(); i++)
		{
			Expression curExpr = exprList.get(i);
			
			if(curExpr instanceof PropertyExpression)
			{
				String newAttrName = getNewAttrName((PropertyExpression) curExpr);

				if(newAttrName != null)
				{
					/* Replace current attribute's name with a new value */
					exprList.set(i, new PropertyExpression(
									((PropertyExpression) curExpr).getObjectExpression(), newAttrName));
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

		/* Handle leftExpr */
		if(leftExpr instanceof PropertyExpression)
		{
			String newAttrName = getNewAttrName((PropertyExpression) leftExpr);

			if(newAttrName != null)
			{
				/* Replace current attribute's name with a new value */
				bex.setLeftExpression( new PropertyExpression(
								((PropertyExpression) leftExpr).getObjectExpression(), newAttrName));
			}
		}
		else if(leftExpr instanceof CastExpression)
		{
			Expression castExpr = ((CastExpression)leftExpr).getExpression();
			
			if(castExpr instanceof PropertyExpression)
			{
				String newAttrName = getNewAttrName((PropertyExpression) castExpr);

				if(newAttrName != null)
				{
					/* Replace current attribute's name with a new value */
					Expression newCastExpr = new PropertyExpression(
									((PropertyExpression) castExpr).getObjectExpression(), newAttrName);
					bex.setLeftExpression(new CastExpression(((CastExpression)leftExpr).getType(), newCastExpr));
				}
			}
		}

		/* Handle rightExpr */
		if(rightExpr instanceof PropertyExpression)
		{
			String newAttrName = getNewAttrName((PropertyExpression) rightExpr);

			if(newAttrName != null)
			{
				/* Replace current attribute's name with a new value */
				bex.setRightExpression( new PropertyExpression(
								((PropertyExpression) rightExpr).getObjectExpression(), newAttrName));
			}
		}
		else if(rightExpr instanceof CastExpression)
		{
			Expression castExpr = ((CastExpression)rightExpr).getExpression();
			
			if(castExpr instanceof PropertyExpression)
			{
				String newAttrName = getNewAttrName((PropertyExpression) castExpr);

				if(newAttrName != null)
				{
					/* Replace current attribute's name with a new value */
					Expression newCastExpr = new PropertyExpression(
									((PropertyExpression) castExpr).getObjectExpression(), newAttrName);
					bex.setRightExpression(new CastExpression(((CastExpression)rightExpr).getType(), newCastExpr));
				}
			}
		}

		super.visitBinaryExpression(bex);
	}
	
	@Override
	public void visitPropertyExpression(PropertyExpression expr) {
		handleNestedPropExpr(expr);
		super.visitPropertyExpression(expr);
	}
}
