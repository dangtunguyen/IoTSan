package edu.ksu.cis.bandera.jjjc.gparser.g2jconverter;

import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;

import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;
import edu.ksu.cis.bandera.jjjc.node.*;

public class GLeftHandSideBuilder {
	public static PLeftHandSide build(Expression gExpr)
	{
		PLeftHandSide jLeftHandSide = null;
		
		if((gExpr instanceof VariableExpression) || 
				(gExpr instanceof PropertyExpression))
		{
			PName jName = GExprBuilder.buildAName(gExpr);
			jLeftHandSide = new ANameLeftHandSide(jName);
		}
		else if(GUtil.isExprAnArrayAccess(gExpr))
		{
			PName jName = GExprBuilder.buildAName(((BinaryExpression)gExpr).getLeftExpression());
			
			if(jName != null)
			{
				PExp jArrayExpr = GExprBuilder.buildAExp(((BinaryExpression)gExpr).getRightExpression());
				
				if(jArrayExpr != null)
				{
					PArrayAccess jArrayAccess = new ANameArrayAccess(jName, new TLBracket(), jArrayExpr, new TRBracket());
					jLeftHandSide = new AArrayAccessLeftHandSide(jArrayAccess);
				}
			}
		}
		
		return jLeftHandSide;
	}
}
