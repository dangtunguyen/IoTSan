package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.DeclarationExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.TupleExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.control.SourceUnit;

public class GExpressionInfoGetter extends ClassCodeVisitorSupport
{
	/********************************************/
	private ArrayList<String> globals;
	private ArrayList<DeclarationExpression> dexpressions;
	private ArrayList<BinaryExpression> bexpressions;
	/********************************************/
	
	public GExpressionInfoGetter()
	{
		globals = new ArrayList<String>();
		dexpressions = new ArrayList<DeclarationExpression>();
		bexpressions = new ArrayList<BinaryExpression>();
	}
	
	@Override
	public void visitBinaryExpression(BinaryExpression bex)
	{
		bexpressions.add(bex);
	}
	
	@Override
	public void visitDeclarationExpression(DeclarationExpression dex)
	{
		dexpressions.add(dex);
		
		if(!dex.isMultipleAssignmentDeclaration())
		{
			VariableExpression left = dex.getVariableExpression();
			globals.add(left.getName().toLowerCase());
		}
		else
		{
			TupleExpression tex = dex.getTupleExpression();
			List<Expression> lefts = tex.getExpressions();
			
			for(Expression it : lefts)
			{
				globals.add(((VariableExpression) it).getName().toLowerCase());
			}
		}
		
		super.visitDeclarationExpression(dex);
	}
	
	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}
	
	public ArrayList<BinaryExpression> getBexpressions()
	{
		return this.bexpressions;
	}
}