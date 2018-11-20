package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import java.util.List;
import java.util.Map;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.BooleanExpression;
import org.codehaus.groovy.ast.expr.CastExpression;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.DeclarationExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.GStringExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.NotExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.DoWhileStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.ForStatement;
import org.codehaus.groovy.ast.stmt.IfStatement;
import org.codehaus.groovy.ast.stmt.WhileStatement;
import org.codehaus.groovy.control.SourceUnit;

/* To differentiate global variables between SmartApps, we need to
 * to format the global variables' name as following:
 * new variable name = class name + "_" + variable name 
 * */
public class GInputNamesFormatter extends ClassCodeVisitorSupport {
	/********************************************/
	private List<String> inputNameList;
	private String className;
	private Map<String, ClassNode> varNameTypeMap;
	/********************************************/

	public GInputNamesFormatter(List<String> inputNames, String className, Map<String, ClassNode> varNameTypeMap)
	{
		this.inputNameList = inputNames;
		this.inputNameList.add("app");
		this.className = className;
		this.varNameTypeMap = varNameTypeMap;
	}

	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}

	private void handlePropExpr(PropertyExpression propExpr)
	{
		String newVarName = null;
		PropertyExpression curPropExpr;

		curPropExpr = propExpr;
		while(true)
		{
			Expression objExpr = curPropExpr.getObjectExpression();

			if(objExpr instanceof PropertyExpression)
			{
				curPropExpr = (PropertyExpression) objExpr;
			}
			else
			{
				if(objExpr instanceof VariableExpression)
				{
					String varName = ((VariableExpression) objExpr).getName();

					/* Check if variable is a global variable */
					if(inputNameList.contains(varName))
					{
						newVarName = this.className + "_" + varName;
					}
				}
				break;
			}
		}

		if(newVarName != null)
		{
			ClassNode varType = ClassHelper.OBJECT_TYPE;
			if(this.varNameTypeMap.containsKey(newVarName))
			{
				varType = this.varNameTypeMap.get(newVarName);
			}
			
			VariableExpression newVarExpr = new VariableExpression(newVarName, varType);
			curPropExpr.setObjectExpression(newVarExpr);
		}
	}

	private void handleMethCallExpr(MethodCallExpression mce)
	{
		Expression objExpr = mce.getObjectExpression();

		if(objExpr instanceof MethodCallExpression)
		{
			handleMethCallExpr((MethodCallExpression) objExpr);
		}
		else if(objExpr instanceof PropertyExpression)
		{
			handlePropExpr((PropertyExpression) objExpr);
		}
		else if(objExpr instanceof BinaryExpression)
		{
			Expression leftExpr = ((BinaryExpression) objExpr).getLeftExpression();
			
			if(leftExpr instanceof VariableExpression)
			{
				String varName = ((VariableExpression) leftExpr).getName();

				if(this.inputNameList.contains(varName))
				{
					String newVarName = this.className + "_" + varName;
					ClassNode varType = ClassHelper.OBJECT_TYPE;
					if(this.varNameTypeMap.containsKey(newVarName))
					{
						varType = this.varNameTypeMap.get(newVarName);
					}
					
					VariableExpression newVarExpr = new VariableExpression(newVarName, varType);
					((BinaryExpression) objExpr).setLeftExpression(newVarExpr);
				}
			}
			else
			{
				System.out.println("[GInputNamesFormatter][handleMethCallExpr] need to handle, leftExpr = "
						+ leftExpr);
			}
		}
		else if(objExpr instanceof VariableExpression)
		{
			String varName = ((VariableExpression) objExpr).getName();

			if(this.inputNameList.contains(varName))
			{
				String newVarName = this.className + "_" + varName;
				ClassNode varType = ClassHelper.OBJECT_TYPE;
				if(this.varNameTypeMap.containsKey(newVarName))
				{
					varType = this.varNameTypeMap.get(newVarName);
				}
				
				VariableExpression newVarExpr = new VariableExpression(newVarName, varType);
				mce.setObjectExpression(newVarExpr);
			}
		}
		else if(!(objExpr instanceof ClassExpression))
		{
			System.out.println("[GInputNamesFormatter][handleMethCallExpr] need to handle "
					+ objExpr);
		}
	}
	
	private void handleExprList(List<Expression> exprList)
	{
		for(int i = 0; i < exprList.size(); i++)
		{
			Expression curExpr = exprList.get(i);

			if(curExpr instanceof PropertyExpression)
			{
				this.handlePropExpr((PropertyExpression) curExpr);
			}
			else if(curExpr instanceof MethodCallExpression)
			{
				this.handleMethCallExpr((MethodCallExpression) curExpr);
			}
			else if(curExpr instanceof VariableExpression)
			{
				String varName = ((VariableExpression) curExpr).getName();

				if(this.inputNameList.contains(varName))
				{
					String newVarName = this.className + "_" + varName;
					ClassNode varType = ClassHelper.OBJECT_TYPE;
					if(this.varNameTypeMap.containsKey(newVarName))
					{
						varType = this.varNameTypeMap.get(newVarName);
					}
					
					VariableExpression newVarExpr = new VariableExpression(newVarName, varType);
					exprList.set(i, newVarExpr);
				}
				else if(varName.equals("currentMode"))
				{
					/* Replace with location.mode */
					PropertyExpression newPropExpr = this.createLocationModePropExpr();
					exprList.set(i, newPropExpr);
				}
			}
		}
	}
	
	private PropertyExpression createLocationModePropExpr()
	{
		PropertyExpression result = new PropertyExpression(
				new VariableExpression("location"),
				new ConstantExpression("mode"));
		
		return result;
	}

	@Override
	public void visitBinaryExpression(BinaryExpression bex)
	{
		Expression leftExpr = bex.getLeftExpression();
		Expression rightExpr = bex.getRightExpression();

		/* Handle leftExpr */
		if(leftExpr instanceof VariableExpression)
		{
			String varName = ((VariableExpression) leftExpr).getName();

			if(this.inputNameList.contains(varName))
			{
				String newVarName = this.className + "_" + varName;
				ClassNode varType = ClassHelper.OBJECT_TYPE;
				if(this.varNameTypeMap.containsKey(newVarName))
				{
					varType = this.varNameTypeMap.get(newVarName);
				}
				
				VariableExpression newVarExpr = new VariableExpression(newVarName, varType);
				bex.setLeftExpression(newVarExpr);
			}
			else if(varName.equals("currentMode"))
			{
				/* Replace with location.mode */
				PropertyExpression newPropExpr = this.createLocationModePropExpr();
				bex.setLeftExpression(newPropExpr);
			}
		}
		else if(leftExpr instanceof NotExpression)
		{
			Expression subExpr = ((NotExpression)leftExpr).getExpression();
			
			if(subExpr instanceof VariableExpression)
			{
				String varName = ((VariableExpression) subExpr).getName();
	
				if(this.inputNameList.contains(varName))
				{
					String newVarName = this.className + "_" + varName;
					ClassNode varType = ClassHelper.OBJECT_TYPE;
					if(this.varNameTypeMap.containsKey(newVarName))
					{
						varType = this.varNameTypeMap.get(newVarName);
					}
					
					VariableExpression newVarExpr = new VariableExpression(newVarName, varType);
					NotExpression newNotExpr = new NotExpression(newVarExpr);
					bex.setLeftExpression(newNotExpr);
				}
			}
		}
		else if(leftExpr instanceof CastExpression)
		{
			Expression subExpr = ((CastExpression)leftExpr).getExpression();
			
			if(subExpr instanceof VariableExpression)
			{
				String varName = ((VariableExpression) subExpr).getName();
	
				if(this.inputNameList.contains(varName))
				{
					String newVarName = this.className + "_" + varName;
					ClassNode varType = ClassHelper.OBJECT_TYPE;
					if(this.varNameTypeMap.containsKey(newVarName))
					{
						varType = this.varNameTypeMap.get(newVarName);
					}
					
					VariableExpression newVarExpr = new VariableExpression(newVarName, varType);
					CastExpression newNotExpr = new CastExpression(((CastExpression)leftExpr).getType(), newVarExpr);
					bex.setLeftExpression(newNotExpr);
				}
			}
		}
		else if(leftExpr instanceof PropertyExpression)
		{
			handlePropExpr((PropertyExpression) leftExpr);
		}

		/* Handle rightExpr */
		if(rightExpr instanceof VariableExpression)
		{
			String varName = ((VariableExpression) rightExpr).getName();

			if(this.inputNameList.contains(varName))
			{
				String newVarName = this.className + "_" + varName;
				ClassNode varType = ClassHelper.OBJECT_TYPE;
				if(this.varNameTypeMap.containsKey(newVarName))
				{
					varType = this.varNameTypeMap.get(newVarName);
				}
				
				VariableExpression newVarExpr = new VariableExpression(newVarName, varType);
				bex.setRightExpression(newVarExpr);
			}
			else if(varName.equals("currentMode"))
			{
				/* Replace with location.mode */
				PropertyExpression newPropExpr = this.createLocationModePropExpr();
				bex.setRightExpression(newPropExpr);
			}
		}
		else if(rightExpr instanceof NotExpression)
		{
			Expression subExpr = ((NotExpression)rightExpr).getExpression();
			
			if(subExpr instanceof VariableExpression)
			{
				String varName = ((VariableExpression) subExpr).getName();
	
				if(this.inputNameList.contains(varName))
				{
					String newVarName = this.className + "_" + varName;
					ClassNode varType = ClassHelper.OBJECT_TYPE;
					if(this.varNameTypeMap.containsKey(newVarName))
					{
						varType = this.varNameTypeMap.get(newVarName);
					}
					
					VariableExpression newVarExpr = new VariableExpression(newVarName, varType);
					NotExpression newNotExpr = new NotExpression(newVarExpr);
					bex.setRightExpression(newNotExpr);
				}
			}
		}
		else if(rightExpr instanceof CastExpression)
		{
			Expression subExpr = ((CastExpression)rightExpr).getExpression();
			
			if(subExpr instanceof VariableExpression)
			{
				String varName = ((VariableExpression) subExpr).getName();
	
				if(this.inputNameList.contains(varName))
				{
					String newVarName = this.className + "_" + varName;
					ClassNode varType = ClassHelper.OBJECT_TYPE;
					if(this.varNameTypeMap.containsKey(newVarName))
					{
						varType = this.varNameTypeMap.get(newVarName);
					}
					
					VariableExpression newVarExpr = new VariableExpression(newVarName, varType);
					CastExpression newNotExpr = new CastExpression(((CastExpression)rightExpr).getType(), newVarExpr);
					bex.setRightExpression(newNotExpr);
				}
			}
		}
		else if(rightExpr instanceof PropertyExpression)
		{
			handlePropExpr((PropertyExpression) rightExpr);
		}
		else if(rightExpr instanceof MethodCallExpression)
		{
			handleMethCallExpr((MethodCallExpression) rightExpr);
		}

		super.visitBinaryExpression(bex);
	}

	public void visitMethodCallExpression(MethodCallExpression mce) {
		this.handleMethCallExpr(mce);
		super.visitMethodCallExpression(mce);
	}

	@Override
	public void visitArgumentlistExpression(ArgumentListExpression ale) {
		this.handleExprList(ale.getExpressions());
		super.visitArgumentlistExpression(ale);
	}

	@Override
	public void visitForLoop(ForStatement forLoop) {
		Expression colectionExpr = forLoop.getCollectionExpression();

		if(colectionExpr instanceof PropertyExpression)
		{
			this.handlePropExpr((PropertyExpression) colectionExpr);
		}
		else if(colectionExpr instanceof MethodCallExpression)
		{
			this.handleMethCallExpr((MethodCallExpression) colectionExpr);
		}
		else if(colectionExpr instanceof VariableExpression)
		{
			String varName = ((VariableExpression) colectionExpr).getName();

			if(this.inputNameList.contains(varName))
			{
				String newVarName = this.className + "_" + varName;
				ClassNode varType = ClassHelper.OBJECT_TYPE;
				if(this.varNameTypeMap.containsKey(newVarName))
				{
					varType = this.varNameTypeMap.get(newVarName);
				}
				
				VariableExpression newVarExpr = new VariableExpression(newVarName, varType);
				forLoop.setCollectionExpression(newVarExpr);
			}
		}

		super.visitForLoop(forLoop);
	}

	@Override
	public void visitDeclarationExpression(DeclarationExpression expr) {
		Expression rightExpr = expr.getRightExpression();
		
		if(rightExpr instanceof VariableExpression)
		{
			String varName = ((VariableExpression) rightExpr).getName();

			if(this.inputNameList.contains(varName))
			{
				String newVarName = this.className + "_" + varName;
				ClassNode varType = ClassHelper.OBJECT_TYPE;
				if(this.varNameTypeMap.containsKey(newVarName))
				{
					varType = this.varNameTypeMap.get(newVarName);
				}
				
				VariableExpression newVarExpr = new VariableExpression(newVarName, varType);
				expr.setRightExpression(newVarExpr);
			}
		}
		else if(rightExpr instanceof PropertyExpression)
		{
			handlePropExpr((PropertyExpression) rightExpr);
		}
		else if(rightExpr instanceof MethodCallExpression)
		{
			handleMethCallExpr((MethodCallExpression) rightExpr);
		}
		
		super.visitDeclarationExpression(expr);
	}

	@Override
	public void visitBooleanExpression(BooleanExpression expr) {
		Expression rightExpr = expr.getExpression();
		
		if(rightExpr instanceof PropertyExpression)
		{
			handlePropExpr((PropertyExpression) rightExpr);
		}
		else if(rightExpr instanceof MethodCallExpression)
		{
			handleMethCallExpr((MethodCallExpression) rightExpr);
		}
		super.visitBooleanExpression(expr);
	}

	@Override
	public void visitGStringExpression(GStringExpression expr) {
		this.handleExprList(expr.getValues());
		super.visitGStringExpression(expr);
	}
	
	@Override
	public void visitWhileLoop(WhileStatement loop) {
		Expression expr = loop.getBooleanExpression().getExpression();
		
		if(expr instanceof VariableExpression)
		{
			String varName = ((VariableExpression) expr).getName();

			if(this.inputNameList.contains(varName))
			{
				String newVarName = this.className + "_" + varName;
				ClassNode varType = ClassHelper.OBJECT_TYPE;
				if(this.varNameTypeMap.containsKey(newVarName))
				{
					varType = this.varNameTypeMap.get(newVarName);
				}
				
				VariableExpression newVarExpr = new VariableExpression(newVarName, varType);
				loop.setBooleanExpression(new BooleanExpression(newVarExpr));
			}
		}
		else if(expr instanceof NotExpression)
		{
			Expression subExpr = ((NotExpression)expr).getExpression();
			
			if(subExpr instanceof VariableExpression)
			{
				String varName = ((VariableExpression) subExpr).getName();
	
				if(this.inputNameList.contains(varName))
				{
					String newVarName = this.className + "_" + varName;
					ClassNode varType = ClassHelper.OBJECT_TYPE;
					if(this.varNameTypeMap.containsKey(newVarName))
					{
						varType = this.varNameTypeMap.get(newVarName);
					}
					
					VariableExpression newVarExpr = new VariableExpression(newVarName, varType);
					NotExpression newNotExpr = new NotExpression(newVarExpr);
					loop.setBooleanExpression(new BooleanExpression(newNotExpr));
				}
			}
		}
		else if(expr instanceof CastExpression)
		{
			Expression subExpr = ((CastExpression)expr).getExpression();
			
			if(subExpr instanceof VariableExpression)
			{
				String varName = ((VariableExpression) subExpr).getName();
	
				if(this.inputNameList.contains(varName))
				{
					String newVarName = this.className + "_" + varName;
					ClassNode varType = ClassHelper.OBJECT_TYPE;
					if(this.varNameTypeMap.containsKey(newVarName))
					{
						varType = this.varNameTypeMap.get(newVarName);
					}
					
					VariableExpression newVarExpr = new VariableExpression(newVarName, varType);
					CastExpression newNotExpr = new CastExpression(((CastExpression)expr).getType(), newVarExpr);
					loop.setBooleanExpression(new BooleanExpression(newNotExpr));
				}
			}
		}
		
        super.visitWhileLoop(loop);
    }

	@Override
    public void visitDoWhileLoop(DoWhileStatement loop) {
		Expression expr = loop.getBooleanExpression().getExpression();
		
		if(expr instanceof VariableExpression)
		{
			String varName = ((VariableExpression) expr).getName();

			if(this.inputNameList.contains(varName))
			{
				String newVarName = this.className + "_" + varName;
				ClassNode varType = ClassHelper.OBJECT_TYPE;
				if(this.varNameTypeMap.containsKey(newVarName))
				{
					varType = this.varNameTypeMap.get(newVarName);
				}
				
				VariableExpression newVarExpr = new VariableExpression(newVarName, varType);
				loop.setBooleanExpression(new BooleanExpression(newVarExpr));
			}
		}
		else if(expr instanceof NotExpression)
		{
			Expression subExpr = ((NotExpression)expr).getExpression();
			
			if(subExpr instanceof VariableExpression)
			{
				String varName = ((VariableExpression) subExpr).getName();
	
				if(this.inputNameList.contains(varName))
				{
					String newVarName = this.className + "_" + varName;
					ClassNode varType = ClassHelper.OBJECT_TYPE;
					if(this.varNameTypeMap.containsKey(newVarName))
					{
						varType = this.varNameTypeMap.get(newVarName);
					}
					
					VariableExpression newVarExpr = new VariableExpression(newVarName, varType);
					NotExpression newNotExpr = new NotExpression(newVarExpr);
					loop.setBooleanExpression(new BooleanExpression(newNotExpr));
				}
			}
		}
		else if(expr instanceof CastExpression)
		{
			Expression subExpr = ((CastExpression)expr).getExpression();
			
			if(subExpr instanceof VariableExpression)
			{
				String varName = ((VariableExpression) subExpr).getName();
	
				if(this.inputNameList.contains(varName))
				{
					String newVarName = this.className + "_" + varName;
					ClassNode varType = ClassHelper.OBJECT_TYPE;
					if(this.varNameTypeMap.containsKey(newVarName))
					{
						varType = this.varNameTypeMap.get(newVarName);
					}
					
					VariableExpression newVarExpr = new VariableExpression(newVarName, varType);
					CastExpression newNotExpr = new CastExpression(((CastExpression)expr).getType(), newVarExpr);
					loop.setBooleanExpression(new BooleanExpression(newNotExpr));
				}
			}
		}
        super.visitDoWhileLoop(loop);
    }

	@Override
    public void visitIfElse(IfStatement ifElse) {
		Expression expr = ifElse.getBooleanExpression().getExpression();
		
		if(expr instanceof VariableExpression)
		{
			String varName = ((VariableExpression) expr).getName();

			if(this.inputNameList.contains(varName))
			{
				String newVarName = this.className + "_" + varName;
				ClassNode varType = ClassHelper.OBJECT_TYPE;
				if(this.varNameTypeMap.containsKey(newVarName))
				{
					varType = this.varNameTypeMap.get(newVarName);
				}
				
				VariableExpression newVarExpr = new VariableExpression(newVarName, varType);
				ifElse.setBooleanExpression(new BooleanExpression(newVarExpr));
			}
		}
		else if(expr instanceof NotExpression)
		{
			Expression subExpr = ((NotExpression)expr).getExpression();
			
			if(subExpr instanceof VariableExpression)
			{
				String varName = ((VariableExpression) subExpr).getName();
	
				if(this.inputNameList.contains(varName))
				{
					String newVarName = this.className + "_" + varName;
					ClassNode varType = ClassHelper.OBJECT_TYPE;
					if(this.varNameTypeMap.containsKey(newVarName))
					{
						varType = this.varNameTypeMap.get(newVarName);
					}
					
					VariableExpression newVarExpr = new VariableExpression(newVarName, varType);
					NotExpression newNotExpr = new NotExpression(newVarExpr);
					ifElse.setBooleanExpression(new BooleanExpression(newNotExpr));
				}
			}
		}
		else if(expr instanceof CastExpression)
		{
			Expression subExpr = ((CastExpression)expr).getExpression();
			
			if(subExpr instanceof VariableExpression)
			{
				String varName = ((VariableExpression) subExpr).getName();
	
				if(this.inputNameList.contains(varName))
				{
					String newVarName = this.className + "_" + varName;
					ClassNode varType = ClassHelper.OBJECT_TYPE;
					if(this.varNameTypeMap.containsKey(newVarName))
					{
						varType = this.varNameTypeMap.get(newVarName);
					}
					
					VariableExpression newVarExpr = new VariableExpression(newVarName, varType);
					CastExpression newNotExpr = new CastExpression(((CastExpression)expr).getType(), newVarExpr);
					ifElse.setBooleanExpression(new BooleanExpression(newNotExpr));
				}
			}
		}
        super.visitIfElse(ifElse);
    }
	
	@Override
	public void visitExpressionStatement(ExpressionStatement exprStmt) {
        Expression expr = exprStmt.getExpression();
		
        if(expr instanceof VariableExpression)
        {
        		String varName = ((VariableExpression) expr).getName();

			if(this.inputNameList.contains(varName))
			{
				String newVarName = this.className + "_" + varName;
				ClassNode varType = ClassHelper.OBJECT_TYPE;
				if(this.varNameTypeMap.containsKey(newVarName))
				{
					varType = this.varNameTypeMap.get(newVarName);
				}
				
				VariableExpression newVarExpr = new VariableExpression(newVarName, varType);
				exprStmt.setExpression(newVarExpr);
			}
		}
		
        super.visitExpressionStatement(exprStmt);
    }
	
	@Override
	public void visitPropertyExpression(PropertyExpression propExpr)
	{
		this.handlePropExpr(propExpr);
		super.visitPropertyExpression(propExpr);
	}
}
