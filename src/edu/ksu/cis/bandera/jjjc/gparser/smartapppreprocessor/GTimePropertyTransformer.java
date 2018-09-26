package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.CastExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.control.SourceUnit;

/* We have to do this transformation because of Groovy Compiler error
 * Before: def elapsed = now() - presenceState.rawDateCreated.time
 * After: def elapsed = now() - (presenceState.rawDateCreated as long)
 * */
public class GTimePropertyTransformer extends ClassCodeVisitorSupport {
	public GTimePropertyTransformer() {}
	
	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}
	
	@Override
	public void visitBinaryExpression(BinaryExpression bex)
	{
		Expression leftExpr = bex.getLeftExpression();
		Expression rightExpr = bex.getRightExpression();

		/* Handle leftExpr */
		if(leftExpr instanceof PropertyExpression)
		{
			if(((PropertyExpression)leftExpr).getPropertyAsString().equals("time"))
			{
				Expression castExpr = new CastExpression(ClassHelper.long_TYPE, ((PropertyExpression)leftExpr).getObjectExpression());
				bex.setLeftExpression(castExpr);
			}
		}
		
		/* Handle rightExpr */
		if(rightExpr instanceof PropertyExpression)
		{
			if(((PropertyExpression)rightExpr).getPropertyAsString().equals("time"))
			{
				Expression castExpr = new CastExpression(ClassHelper.long_TYPE, ((PropertyExpression)rightExpr).getObjectExpression());
				bex.setRightExpression(castExpr);
			}
		}
		
		super.visitBinaryExpression(bex);
	}
}
