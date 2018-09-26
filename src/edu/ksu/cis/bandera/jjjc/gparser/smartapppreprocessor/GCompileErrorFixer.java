package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.Parameter;
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
import org.codehaus.groovy.syntax.Token;
import org.codehaus.groovy.syntax.Types;

import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;

/* Before: lock1.unlock()
 * Note: lock1 is a list type
 * After:
 * for(it in lock1)
 * {
 * 		it.unlock();
 * }
 * */
public class GCompileErrorFixer {
	/********************************************/
	private Set<String> localMethSet;
	private List<String> multipleDeviceNameList;
	private Map<String, Integer> methIndexMap = new HashMap<String, Integer>();
	/********************************************/
	
	public GCompileErrorFixer(List<String> multipleDeviceNames, Set<String> localMethSet)
	{
		this.multipleDeviceNameList = multipleDeviceNames;
		this.localMethSet = localMethSet;
	}
	
	public void processAClassNode(ClassNode classNode)
	{
		/* Remove null expressions in all methods */
		for(MethodNode meth : classNode.getMethods())
		{
			if(this.localMethSet.contains(meth.getName()))
			{
				this.handleABlock(meth.getCode());
			}
		}
	}
	
	/* Before: lock1.unlock()
	 * Note: lock1 is a list type
	 * After:
	 * for(it in lock1)
	 * {
	 * 		it.unlock();
	 * }
	 * */
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
					Expression expr = ((ExpressionStatement) gSubStmt).getExpression();
					
					if(expr instanceof MethodCallExpression)
					{
						MethodCallExpression mce = (MethodCallExpression)expr;
						String methName = mce.getMethodAsString();
						
						/* Check if the method is a SmartThings command type */
						if(GUtil.isASTCommand(methName))
						{
							Expression objExpr = mce.getObjectExpression();
							
							if(objExpr instanceof VariableExpression)
							{
								/* Check if variable is a multiple device (list of devices) */
								if(this.multipleDeviceNameList.contains(((VariableExpression) objExpr).getName()))
								{
									/* Replace the mce with a for statement */
									Parameter var = new Parameter(ClassHelper.OBJECT_TYPE, "it");
									Expression collExpr = new VariableExpression(((VariableExpression) objExpr).getName());
									MethodCallExpression newMCE = new MethodCallExpression(
											new VariableExpression("it"),
											methName,
											mce.getArguments()
											);
									ExpressionStatement newExprStmt = new ExpressionStatement(newMCE);
									BlockStatement loopBlock = new BlockStatement();
									loopBlock.addStatement(newExprStmt);
									ForStatement newStmt = new ForStatement(var, collExpr, loopBlock);
									gStmtList.set(i, newStmt);
								}
							}
							else if(objExpr instanceof MethodCallExpression)
							{
								String objMethName = ((MethodCallExpression) objExpr).getMethodAsString();
								
								if(GUtil.methNameTypeMap.containsKey(objMethName))
								{
									ClassNode gType = GUtil.methNameTypeMap.get(objMethName);
									
									if(gType.getName().contains("List"))
									{
										String newVarName;
										int index = 0;
										{
											if(this.methIndexMap.containsKey(objMethName))
											{
												index = this.methIndexMap.get(objMethName);
											}
										}
										newVarName = objMethName + "Result" + index;
										Expression collExpr = new VariableExpression(newVarName);
										
										/* Add a declaration expression statement */
										{
											Token operation = Token.newSymbol(Types.ASSIGN, -1, -1); 
											DeclarationExpression declExpr = new DeclarationExpression(
													collExpr,
													operation,
													objExpr);
											ExpressionStatement newExprStmt = new ExpressionStatement(declExpr);
											gStmtList.add(i, newExprStmt);
										}
										
										/* Replace the mce with a for statement */
										{
											Parameter var = new Parameter(ClassHelper.OBJECT_TYPE, "it");
											MethodCallExpression newMCE = new MethodCallExpression(
													new VariableExpression("it"),
													methName,
													mce.getArguments()
													);
											ExpressionStatement newExprStmt = new ExpressionStatement(newMCE);
											BlockStatement loopBlock = new BlockStatement();
											loopBlock.addStatement(newExprStmt);
											ForStatement newStmt = new ForStatement(var, collExpr, loopBlock);
											gStmtList.set(i+1, newStmt);
										}
										
										index++;
										this.methIndexMap.put(objMethName, index);
									}
								}
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
				{
					Expression expr = ((ReturnStatement) gSubStmt).getExpression();
					
					if(expr instanceof MethodCallExpression)
					{
						MethodCallExpression mce = (MethodCallExpression)expr;
						String methName = mce.getMethodAsString();
						
						/* Check if the method is a SmartThings command type */
						if(GUtil.isASTCommand(methName))
						{
							Expression objExpr = mce.getObjectExpression();
							
							if(objExpr instanceof VariableExpression)
							{
								/* Check if variable is a multiple device (list of devices) */
								if(this.multipleDeviceNameList.contains(((VariableExpression) objExpr).getName()))
								{
									/* Replace the mce with a for statement */
									Parameter var = new Parameter(ClassHelper.OBJECT_TYPE, "it");
									Expression collExpr = new VariableExpression(((VariableExpression) objExpr).getName());
									MethodCallExpression newMCE = new MethodCallExpression(
											new VariableExpression("it"),
											methName,
											mce.getArguments()
											);
									ExpressionStatement newExprStmt = new ExpressionStatement(newMCE);
									BlockStatement loopBlock = new BlockStatement();
									loopBlock.addStatement(newExprStmt);
									ForStatement newStmt = new ForStatement(var, collExpr, loopBlock);
									gStmtList.set(i, newStmt);
								}
							}
							else if(objExpr instanceof MethodCallExpression)
							{
								String objMethName = ((MethodCallExpression) objExpr).getMethodAsString();
								
								if(GUtil.methNameTypeMap.containsKey(objMethName))
								{
									ClassNode gType = GUtil.methNameTypeMap.get(objMethName);
									
									if(gType.getName().contains("List"))
									{
										String newVarName;
										int index = 0;
										{
											if(this.methIndexMap.containsKey(objMethName))
											{
												index = this.methIndexMap.get(objMethName);
											}
										}
										newVarName = objMethName + "Result" + index;
										Expression collExpr = new VariableExpression(newVarName);
										
										/* Add a declaration expression statement */
										{
											Token operation = Token.newSymbol(Types.ASSIGN, -1, -1); 
											DeclarationExpression declExpr = new DeclarationExpression(
													collExpr,
													operation,
													objExpr);
											ExpressionStatement newExprStmt = new ExpressionStatement(declExpr);
											gStmtList.add(i, newExprStmt);
										}
										
										/* Replace the mce with a for statement */
										{
											Parameter var = new Parameter(ClassHelper.OBJECT_TYPE, "it");
											MethodCallExpression newMCE = new MethodCallExpression(
													new VariableExpression("it"),
													methName,
													mce.getArguments()
													);
											ExpressionStatement newExprStmt = new ExpressionStatement(newMCE);
											BlockStatement loopBlock = new BlockStatement();
											loopBlock.addStatement(newExprStmt);
											ForStatement newStmt = new ForStatement(var, collExpr, loopBlock);
											gStmtList.set(i+1, newStmt);
										}
										
										index++;
										this.methIndexMap.put(objMethName, index);
									}
								}
							}
						}
					}
				}
				else if(gSubStmt instanceof BreakStatement)
				{}
				else if(gSubStmt instanceof BlockStatement)
				{
					this.handleABlock((BlockStatement) gSubStmt);
				}
				else
				{
					System.out.println("[GCompileErrorFixer.handleABlock] unexpected statement type: " + gSubStmt);
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
			System.out.println("[GCompileErrorFixer.handleABlock] a wrong call!!!");
		}
	}
}
