package edu.ksu.cis.bandera.jjjc.gparser.g2jconverter;

import java.util.Arrays;
import java.util.List;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.control.SourceUnit;

/* This class is use to minimize number attributes of a
 * SmartThing's object.
 * E.g.:
 * Before: evt.doubleValue
 * after: evt.integerValue/value
 * */
public class GPropertyNameAbstracter extends ClassCodeVisitorSupport {
	/********************************************/
	/* These attributes' names will be replaced with "integerValue"/"value"
	 * */
	private List<String> numbAttrNameList = Arrays.asList("doubleValue", "floatValue",
			"numberValue", "numericValue", "integerValue");
	/* These attributes' names will be replaced with "value"
	 * */
	private List<String> strAttrNameList = Arrays.asList("stringValue", "data");
	/* These attributes' names will be replaced with "date"
	 * */
	private List<String> dateAttrNameList = Arrays.asList("dateValue", "rawDateCreated",
			"dateCreated");
	/********************************************/

	public GPropertyNameAbstracter() {}
	
	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}
	
	private String getNewAttrName(PropertyExpression expr)
	{
		String newAttrName = null;
		String attrName = expr.getPropertyAsString();
		
		if(numbAttrNameList.contains(attrName))
		{
			newAttrName = "value";
		}
		else if(strAttrNameList.contains(attrName))
		{
			newAttrName = "value";
		}
		else if(dateAttrNameList.contains(attrName))
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
				/* Process property name */
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

		super.visitBinaryExpression(bex);
	}
	
	@Override
	public void visitPropertyExpression(PropertyExpression expr) {
		handleNestedPropExpr(expr);
		super.visitPropertyExpression(expr);
	}
}
