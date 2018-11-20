package edu.ksu.cis.bandera.jjjc.gparser.g2jconverter;


import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.control.SourceUnit;

public class GMethReturnTypeHandler extends ClassCodeVisitorSupport {
	/********************************************/
	private String varName; /* name of the VariableExpression */
	/********************************************/
	
	public GMethReturnTypeHandler()
	{
		varName = null;
	}
	
	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}
	
	@Override
	public void visitReturnStatement(ReturnStatement statement)
	{
		Expression returnExpr = statement.getExpression();
		
		if (returnExpr instanceof VariableExpression)
		{
			/* We need to get the varName so that we can traverse the code to
			 * get the data type of the variable, which will become the return
			 * type of the method */
			this.varName = ((VariableExpression) returnExpr).getName();
		}
		super.visitReturnStatement(statement);
	}
	
	public String getVarName()
	{
		return this.varName;
	}
}
