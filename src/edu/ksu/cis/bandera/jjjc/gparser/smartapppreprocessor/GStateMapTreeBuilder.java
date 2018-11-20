package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.DeclarationExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.control.SourceUnit;

public class GStateMapTreeBuilder extends ClassCodeVisitorSupport {
	/********************************************/
	private List<String> mapVarList;
	private Map<String, String> stateMapTree;
	/********************************************/
	
	public GStateMapTreeBuilder()
	{
		this.mapVarList = new ArrayList<String>();
		this.mapVarList.add("state");
		this.stateMapTree = new HashMap<String, String>();
	}
	
	/* Getters */
	public Map<String, String> getStateMapTree()
	{
		return this.stateMapTree;
	}
	
	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}
	
	@Override
    public void visitDeclarationExpression(DeclarationExpression expr) {
		Expression leftExrp = expr.getLeftExpression();
		
		if(leftExrp instanceof VariableExpression)
		{
			String rightVarName = null;
			
			Expression rightExpr = expr.getRightExpression();
			
			if(rightExpr instanceof BinaryExpression)
			{
				Expression leftRightExpr = ((BinaryExpression) rightExpr).getLeftExpression();
				
				if(leftRightExpr instanceof VariableExpression)
				{
					rightVarName = ((VariableExpression) leftRightExpr).getName();
				}
			}
			else if(rightExpr instanceof PropertyExpression)
			{
				Expression objectExpr = ((PropertyExpression) rightExpr).getObjectExpression();
				
				if(objectExpr instanceof VariableExpression)
				{
					rightVarName = ((VariableExpression) objectExpr).getName();
				}
			}
			
			if(rightVarName != null)
			{
				if(this.mapVarList.contains(rightVarName))
				{
					if(!this.stateMapTree.containsKey(rightVarName))
					{
						String leftVarName = ((VariableExpression) leftExrp).getName();
						
						this.stateMapTree.put(rightVarName, leftVarName); /* parent - child relationship */
						
						if(!this.mapVarList.contains(leftVarName))
						{
							this.mapVarList.add(leftVarName);
						}
					}
				}
			}
		}
		
        super.visitDeclarationExpression(expr);
    }
}
