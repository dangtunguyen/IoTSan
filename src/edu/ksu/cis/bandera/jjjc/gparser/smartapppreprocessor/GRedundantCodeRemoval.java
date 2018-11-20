package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.expr.DeclarationExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
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

public class GRedundantCodeRemoval {
	/********************************************/
	private Set<String> localMethSet;
	private List<String> emptyMethList;
	private Map<String, Integer> localVarRefCountMap;
	/********************************************/
	
	public GRedundantCodeRemoval(Set<String> localMethSet) 
	{
		this.emptyMethList = new ArrayList<String>();
		this.localMethSet = localMethSet;
	}
	
	public void processAClassNode(ClassNode classNode)
	{
		/* Remove null expressions in all methods */
		for(MethodNode meth : classNode.getMethods())
		{
			if(this.localMethSet.contains(meth.getName()))
			{
				if(this.removeNullExpressions(meth.getCode()))
				{
					this.emptyMethList.add(meth.getName());
				}
			}
		}
		
		/* Remove null method calls in all methods */
		for(MethodNode meth : classNode.getMethods())
		{
			if(meth.getName().equals("strobeHandler"))
			{
				System.out.println();
			}
			if(this.localMethSet.contains(meth.getName()))
			{
				this.removeNullMethCalls(meth.getCode());
			}
		}
		
		/* Remove unused variables in all methods */
		for(MethodNode meth : classNode.getMethods())
		{
			if(this.localMethSet.contains(meth.getName()))
			{
				this.removeUnusedVariables(meth.getCode());
			}
		}
	}
	
	/* This function removes all null expressions.
	 * It returns true if the input block is empty, otherwise false.
	 * */
	private boolean removeNullExpressions(Statement gStmt)
	{
		boolean result = true;
		
		if(gStmt instanceof BlockStatement)
		{
			List<Statement> gStmtList = ((BlockStatement)gStmt).getStatements();
			
			for(int i = 0; i < gStmtList.size(); i++)
			{
				Statement gSubStmt = gStmtList.get(i);
				
				if(gSubStmt instanceof ExpressionStatement)
				{
					Expression expr = ((ExpressionStatement) gSubStmt).getExpression();
					
					if(expr instanceof MethodCallExpression)
					{
						String methName = ((MethodCallExpression) expr).getMethodAsString();
						
						if(methName != null)
						{
							if(!methName.equals("default_null_method"))
							{
								result = false;
							}
						}
						else
						{
							result = false;
						}
					}
					else
					{
						result = false;
					}
				}
				else if(gSubStmt instanceof IfStatement)
				{
					/* Remove IfStatement if both ifBlock and elseBlock are empty */
					if(this.removeNullExpressions(((IfStatement) gSubStmt).getIfBlock()) && 
							this.removeNullExpressions(((IfStatement) gSubStmt).getElseBlock()))
					{
						gStmtList.remove(i);
						i--;
						if(i < 0)
						{
							i = 0;
						}
					}
					else
					{
						result = false;
					}
				}
				else if(gSubStmt instanceof WhileStatement)
				{
					/* Remove WhileStatement if loopBlock is empty */
					if(this.removeNullExpressions(((WhileStatement) gSubStmt).getLoopBlock()))
					{
						gStmtList.remove(i);
						i--;
						if(i < 0)
						{
							i = 0;
						}
					}
					else
					{
						result = false;
					}
				}
				else if(gSubStmt instanceof DoWhileStatement)
				{
					/* Remove DoWhileStatement if loopBlock is empty */
					if(this.removeNullExpressions(((DoWhileStatement) gSubStmt).getLoopBlock()))
					{
						gStmtList.remove(i);
						i--;
						if(i < 0)
						{
							i = 0;
						}
					}
					else
					{
						result = false;
					}
				}
				else if(gSubStmt instanceof ForStatement)
				{
					/* Remove ForStatement if loopBlock is empty */
					if(this.removeNullExpressions(((ForStatement) gSubStmt).getLoopBlock()))
					{
						gStmtList.remove(i);
						i--;
						if(i < 0)
						{
							i = 0;
						}
					}
					else
					{
						result = false;
					}
				}
				else if(gSubStmt instanceof ReturnStatement)
				{
					result = false;
				}
				else if(gSubStmt instanceof BreakStatement)
				{}
				else
				{
					System.out.println("[GRedundantCodeRemoval.handleABlock] unexpected statement type: " + gSubStmt);
				}
			}
		}
		else if(!(gStmt instanceof EmptyStatement))
		{
			System.out.println("[GRedundantCodeRemoval.handleABlock] a wrong call!!!");
			result = false;
		}
		return result;
	}
	
	/* This function removes all MethodCallExpression, whose name is
	 * in the list emptyMethList.
	 * */
	private void removeNullMethCalls(Statement gStmt)
	{
		if(gStmt instanceof BlockStatement)
		{
			List<Statement> gStmtList = ((BlockStatement)gStmt).getStatements();
			
			for(int i = 0; i < gStmtList.size(); i++)
			{
				Statement gSubStmt = gStmtList.get(i);
				
				if(gSubStmt instanceof ExpressionStatement)
				{
					Expression expr = ((ExpressionStatement) gSubStmt).getExpression();
					
					if(expr instanceof MethodCallExpression)
					{
						if(this.emptyMethList.contains(((MethodCallExpression) expr).getMethodAsString()))
						{
							gStmtList.remove(i);
							i--;
							if(i < 0)
							{
								i = 0;
							}
						}
					}
				}
				else if(gSubStmt instanceof IfStatement)
				{
					this.removeNullMethCalls(((IfStatement) gSubStmt).getIfBlock()); 
					this.removeNullMethCalls(((IfStatement) gSubStmt).getElseBlock());
				}
				else if(gSubStmt instanceof WhileStatement)
				{
					this.removeNullMethCalls(((WhileStatement) gSubStmt).getLoopBlock());
				}
				else if(gSubStmt instanceof DoWhileStatement)
				{
					this.removeNullMethCalls(((DoWhileStatement) gSubStmt).getLoopBlock());
				}
				else if(gSubStmt instanceof ForStatement)
				{
					this.removeNullMethCalls(((ForStatement) gSubStmt).getLoopBlock());
				}
			}
		}
	}
	
	/* This function return all local variables defined in the
	 * given code.
	 * */
	private Set<String> getLocalVariables(Statement gStmt)
	{
		Set<String> result = new HashSet<String>();
		
		if(gStmt instanceof BlockStatement)
		{
			List<Statement> gStmtList = ((BlockStatement)gStmt).getStatements();
			
			for(int i = 0; i < gStmtList.size(); i++)
			{
				Statement gSubStmt = gStmtList.get(i);
				
				if(gSubStmt instanceof ExpressionStatement)
				{
					Expression expr = ((ExpressionStatement) gSubStmt).getExpression();
					
					if(expr instanceof DeclarationExpression)
					{
						Expression lefExpr = ((DeclarationExpression) expr).getLeftExpression();
						
						if(lefExpr instanceof VariableExpression)
						{
							String varName = ((VariableExpression) lefExpr).getName();
							
							if(!result.contains(varName))
							{
								result.add(varName);
							}
						}
					}
				}
				else if(gSubStmt instanceof IfStatement)
				{
					result.addAll(this.getLocalVariables(((IfStatement) gSubStmt).getIfBlock())); 
					result.addAll(this.getLocalVariables(((IfStatement) gSubStmt).getElseBlock()));
				}
				else if(gSubStmt instanceof WhileStatement)
				{
					result.addAll(this.getLocalVariables(((WhileStatement) gSubStmt).getLoopBlock()));
				}
				else if(gSubStmt instanceof DoWhileStatement)
				{
					result.addAll(this.getLocalVariables(((DoWhileStatement) gSubStmt).getLoopBlock()));
				}
				else if(gSubStmt instanceof ForStatement)
				{
					result.addAll(this.getLocalVariables(((ForStatement) gSubStmt).getLoopBlock()));
				}
			}
		}
		return result;
	}
	
	/* This function check if local variable is used in the given code.
	 * */
	private void performVarRemoval(Statement gStmt)
	{
		if(gStmt instanceof BlockStatement)
		{
			List<Statement> gStmtList = ((BlockStatement)gStmt).getStatements();
			
			for(int i = 0; i < gStmtList.size(); i++)
			{
				Statement gSubStmt = gStmtList.get(i);
				
				if(gSubStmt instanceof ExpressionStatement)
				{
					Expression expr = ((ExpressionStatement) gSubStmt).getExpression();
					
					if(expr instanceof DeclarationExpression)
					{
						Expression lefExpr = ((DeclarationExpression) expr).getLeftExpression();
						
						if(lefExpr instanceof VariableExpression)
						{
							if(this.localVarRefCountMap.get(((VariableExpression) lefExpr).getName()) <= 1)
							{
								gStmtList.remove(i);
								i--;
								if(i < 0)
								{
									i = 0;
								}
							}
						}
					}
				}
				else if(gSubStmt instanceof IfStatement)
				{
					this.performVarRemoval(((IfStatement) gSubStmt).getIfBlock()); 
					this.performVarRemoval(((IfStatement) gSubStmt).getElseBlock());
				}
				else if(gSubStmt instanceof WhileStatement)
				{
					this.performVarRemoval(((WhileStatement) gSubStmt).getLoopBlock());
				}
				else if(gSubStmt instanceof DoWhileStatement)
				{
					this.performVarRemoval(((DoWhileStatement) gSubStmt).getLoopBlock());
				}
				else if(gSubStmt instanceof ForStatement)
				{
					this.performVarRemoval(((ForStatement) gSubStmt).getLoopBlock());
				}
			}
		}
	}
	
	/* This function removes all all unused variables in the given code.
	 * */
	private void removeUnusedVariables(Statement gStmt)
	{
		Set<String> localVarList = this.getLocalVariables(gStmt);
		
		if(localVarList.size() > 0)
		{
			GVariableRefCountGetter refCount = new GVariableRefCountGetter(localVarList);
			
			/* Get local variable reference count */
			gStmt.visit(refCount);
			this.localVarRefCountMap = refCount.getVarUsageCountMap();
			
			/* Remove unused variables */
			this.performVarRemoval(gStmt);
		}
	}
	
	public static void main(String[] args)
	{
		List<Integer> l = new ArrayList<Integer>();
		l.addAll(Arrays.asList(1,2,3,4));
		
		for(int i = 0; i < l.size(); i++)
		{
			if(l.get(i) == 2)
			{
				l.remove(i);
			}
		}
		System.out.println(l);
	}
}
