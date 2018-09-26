package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.DeclarationExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.control.SourceUnit;

import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;

/* Explicitly add type of Map variables to fix compile error
 * in @CompileStatic
 * */
public class GMapVarCompileErrorFixer extends ClassCodeVisitorSupport {
	/********************************************/
	private GStateMapEnum stateMapType;
	/********************************************/
	
	public GMapVarCompileErrorFixer(GStateMapEnum stateMapType)
	{
		this.stateMapType = stateMapType;
	}
	
	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}
	
	@Override
    public void visitDeclarationExpression(DeclarationExpression expr) {
		Expression rightExpr = expr.getRightExpression();
		
		if(rightExpr instanceof BinaryExpression)
		{
			Expression leftRightExpr = ((BinaryExpression) rightExpr).getLeftExpression();
			
			if(leftRightExpr instanceof VariableExpression)
			{
				if(((VariableExpression) leftRightExpr).getName().equals("state"))
				{
					if(((BinaryExpression) rightExpr).getOperation().getText().contains("["))
					{
						/* Set type of left variable to Map<java.lang.Object, Map> */
						{
							Expression leftExpr = expr.getLeftExpression();
							
							if(leftExpr instanceof VariableExpression)
							{
								ClassNode fieldType = null;
								
								if(this.stateMapType == GStateMapEnum.Int2IntMap)
								{
									fieldType = ClassHelper.int_TYPE.getPlainNodeReference();
								}
								else if(this.stateMapType == GStateMapEnum.Int2IIMMap)
								{
									fieldType = ClassHelper.MAP_TYPE.getPlainNodeReference();
								}
								else if(this.stateMapType == GStateMapEnum.Int2IIIMMap)
								{
									fieldType = GUtil.createGroovyO2MMapType();
								}
								
								if(fieldType != null)
								{
									VariableExpression newLeftExpr = new VariableExpression(
											((VariableExpression) leftExpr).getName(), fieldType);
									expr.setLeftExpression(newLeftExpr);
								}
							}
						}
					}
				}
			}
		}
		
        super.visitDeclarationExpression(expr);
    }
}
