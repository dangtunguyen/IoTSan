package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.control.SourceUnit;

import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;

/* This class is used to get all the callees, which are local methods,
 * in a block of source code
 * */
public class GCalleeInfoGetter extends ClassCodeVisitorSupport{
	/********************************************/
	private List<String> callees;
	private Set<String> localMethNames;
	/********************************************/

	public GCalleeInfoGetter(Set<String> localMethNames)
	{
		this.callees = new ArrayList<String>();
		this.localMethNames = localMethNames;
	}
	
	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}
	
	@Override
	public void visitMethodCallExpression(MethodCallExpression mce)
	{
		String methText;
		
		if(mce.getMethodAsString() == null)
		{
			//dynamic methods
			methText = mce.getText();
		}
		else
		{
			methText = mce.getMethodAsString();
		}
		
		if(localMethNames.contains(methText))
		{
			this.callees.add(methText);
		}
		else if(methText.equals("runIn"))
		{
			Expression args = mce.getArguments();
			List<Expression> exprList = GUtil.buildExprList(args);
			
			if(exprList.size() >= 2)
			{
				Expression refMeth = exprList.get(1);
				String refMethName = null;
				
				if(refMeth instanceof VariableExpression)
				{
					refMethName = ((VariableExpression) refMeth).getName();
				}
				else if(refMeth instanceof ConstantExpression)
				{
					refMethName = ((ConstantExpression) refMeth).getText();
				}
				
				if(refMethName != null)
				{
					this.callees.add(refMethName);
				}
			}
			else
			{
				System.out.println("GUnhandledMethodRemoval: size of exprList = " + exprList.size());
			}
		}
		
		super.visitMethodCallExpression(mce);
	}
	
	public List<String> getCallees()
	{
		return this.callees;
	}
}
