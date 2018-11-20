package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.CastExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.control.SourceUnit;

import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;

/* Before: def thresholdValue = threshold as int
 * After: def thresholdValue = threshold
 * */
public class GCastExprRemoval extends ClassCodeVisitorSupport {
	public GCastExprRemoval() {}
	
	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}
	
	@Override
	public void visitBinaryExpression(BinaryExpression bex)
	{
		Expression rightExpr = bex.getRightExpression();
		
		if(rightExpr instanceof CastExpression)
		{
			Expression exprExpr = ((CastExpression) rightExpr).getExpression();
			
			if(exprExpr instanceof PropertyExpression)
			{
				String prop = ((PropertyExpression) exprExpr).getPropertyAsString();
				
				if(GUtil.isPropStrType(prop))
				{
					String typeName = ((CastExpression) rightExpr).getType().getName();
					String newPropName = null;
					
					switch(typeName)
					{
//					case "double": newPropName = "doubleValue"; break;
//					case "float": newPropName = "floatValue"; break;
					case "double": 
					case "float": 
					case "int": newPropName = "integerValue"; break;
					}
					
					if(newPropName != null)
					{
						Expression objExpr = ((PropertyExpression) exprExpr).getObjectExpression();
						Expression newPropExpr = new PropertyExpression(objExpr, newPropName);
						
						bex.setRightExpression(newPropExpr);
					}
					else
					{
						bex.setRightExpression(exprExpr);
					}
				}
				else
				{
					bex.setRightExpression(exprExpr);
				}
			}
			else
			{
				bex.setRightExpression(exprExpr);
			}
		}
		super.visitBinaryExpression(bex);
	}
}
