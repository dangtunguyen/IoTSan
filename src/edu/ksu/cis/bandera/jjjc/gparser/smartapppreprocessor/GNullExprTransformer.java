package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import java.util.Map;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.control.SourceUnit;

/* Before: state.inactiveAt = null
 * After: state.inactiveAt = 0
 * */
public class GNullExprTransformer extends ClassCodeVisitorSupport {
	/********************************************/
	private Map<String, ClassNode> name2TypeMap;
	/********************************************/
	
	public GNullExprTransformer(Map<String, ClassNode> name2TypeMap)
	{
		this.name2TypeMap = name2TypeMap;
	}
	
	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}
	
	@Override
	public void visitBinaryExpression(BinaryExpression bex)
	{
		Expression leftExpr = bex.getLeftExpression();
		Expression rightExpr = bex.getRightExpression();
		
		/* Before: state.inactiveAt = null
		 * After: state.inactiveAt = 0
		 * */
		if(leftExpr instanceof VariableExpression)
		{
			String varName = ((VariableExpression) leftExpr).getName();
			
			if(this.name2TypeMap.containsKey(varName))
			{
				if(this.name2TypeMap.get(varName) == ClassHelper.double_TYPE)
				{
					if(rightExpr instanceof ConstantExpression)
					{
						if(((ConstantExpression)rightExpr).getText().equals("null"))
						{
							bex.setRightExpression(new ConstantExpression("0"));
						}
					}
				}
			}
		}
		
		super.visitBinaryExpression(bex);
	}
}
