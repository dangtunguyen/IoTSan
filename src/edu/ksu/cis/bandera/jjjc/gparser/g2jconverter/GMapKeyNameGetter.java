package edu.ksu.cis.bandera.jjjc.gparser.g2jconverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.DeclarationExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.control.SourceUnit;

import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;

/* E.g.: Map aMap
 * aMap["test"] = "hello world"
 * => "test" will be added to the result list
 * */
public class GMapKeyNameGetter extends ClassCodeVisitorSupport {
	/********************************************/
	private List<String> mapKeyNameList;
	private List<String> globalStrVarNameList;
	/********************************************/
	
	public GMapKeyNameGetter()
	{
		this.mapKeyNameList = new ArrayList<String>();
		this.globalStrVarNameList = Arrays.asList("currentMode", "mode");
	}
	
	/* Getters */
	public List<String> getMapKeyNameList()
	{
		return this.mapKeyNameList;
	}
	
	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}
	
	@Override
	public void visitMethodCallExpression(MethodCallExpression mce) {
		String methName = mce.getMethodAsString();
		
		if(methName.equals("get") || methName.equals("put"))
		{
			Expression objExpr = mce.getObjectExpression();
			
			if(objExpr instanceof VariableExpression)
			{
				if(GUtil.isExprAMapType((VariableExpression) objExpr))
				{
					List<Expression> argList = GUtil.buildExprList(mce.getArguments());
					
					if(argList.size() > 0)
					{
						Expression firstArg = argList.get(0);
						
						if(firstArg instanceof ConstantExpression)
						{
							String keyName = firstArg.getText();
							
							if(!this.mapKeyNameList.contains(keyName))
							{
								this.mapKeyNameList.add(keyName);
							}
						}
					}
				}
			}
		}
		
		super.visitMethodCallExpression(mce);
	}
	
	@Override
    public void visitDeclarationExpression(DeclarationExpression expr) {
        Expression leftExpr = expr.getLeftExpression();
        
        if(GUtil.isExprAStr(leftExpr) && (leftExpr instanceof VariableExpression))
        {
        	Expression rightExpr = expr.getRightExpression();
        	String varName = ((VariableExpression) leftExpr).getName();
        	
        	if(rightExpr instanceof VariableExpression)
        	{
        		if(this.globalStrVarNameList.contains(((VariableExpression) rightExpr).getName()))
        		{
        			expr.setLeftExpression(new VariableExpression(varName, ClassHelper.int_TYPE.getPlainNodeReference()));
        		}
        	}
        	else if(rightExpr instanceof PropertyExpression)
        	{
        		Expression objExpr = ((PropertyExpression) rightExpr).getObjectExpression();
        		
        		if(objExpr instanceof VariableExpression)
            	{
        			if(((VariableExpression) objExpr).getName().equals("location"))
        			{
        				Expression propExpr = ((PropertyExpression) rightExpr).getProperty();
        				
        				if(this.globalStrVarNameList.contains(propExpr.getText()))
        				{
        					expr.setLeftExpression(new VariableExpression(varName, ClassHelper.int_TYPE.getPlainNodeReference()));
        				}
        			}
            	}
        	}
        }
		
        super.visitDeclarationExpression(expr);
    }
}
