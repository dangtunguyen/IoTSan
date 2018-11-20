package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import java.util.List;
import java.util.Set;

import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.expr.ClosureExpression;
import org.codehaus.groovy.ast.expr.DeclarationExpression;
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
import org.codehaus.groovy.syntax.Token;
import org.codehaus.groovy.syntax.Types;

import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;

/* Transform ".each" closure into "for loop" expression
 * */
public class GEachClosureTransformer {
	/********************************************/
	private Set<String> localMethSet;
	/********************************************/
	
	public GEachClosureTransformer(Set<String> localMethSet)
	{
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
	
	/* Before: 
	 * switches.each {
	 * 		map[it.id] = [switch: it.currentSwitch, level: it.currentLevel]
	 * }
	 * After:
	 * for(it in switches) {
	 * 		map[it.id] = [switch: it.currentSwitch, level: it.currentLevel]
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
						
						/* Check if the method is "each" */
						if((methName != null) && methName.equals("each"))
						{
							Expression objExpr = mce.getObjectExpression();
							
							if(objExpr instanceof VariableExpression)
							{
								java.util.List<Expression> gExprList = GUtil.buildExprList(mce.getArguments());
								Expression firstArg = gExprList.get(0);
								
								if(firstArg instanceof ClosureExpression)
								{
									/* Replace the "each" closure with a for loop statement */
									Parameter[] parms = ((ClosureExpression) firstArg).getParameters();
									Expression collExpr = new VariableExpression(((VariableExpression) objExpr).getName());
									BlockStatement loopBlock = new BlockStatement();
									String varName = null;
									
									if((parms.length == 0) || (parms.length == 1))
									{
										/* Set name of the variable */
										if(parms.length == 0)
										{
											varName = "it";
										}
										else
										{
											varName = parms[0].getName();
										}
										
										/* Set code of the loopBlock */
//										loopBlock.addStatement(((ClosureExpression) firstArg).getCode());
										BlockStatement eachBlock = (BlockStatement)((ClosureExpression) firstArg).getCode();
										loopBlock.addStatements(eachBlock.getStatements());
									}
									else if(parms.length == 2)
									{
										/* getObjectExpression of mce must be a Map variable */
										/* Set name of the variable */
										varName = "it";
										
										/* Add two declaration statements into the loopBlock */
										{
											VariableExpression keyVar = new VariableExpression(parms[0].getName());
											PropertyExpression keyRightExpr = new PropertyExpression(
													new VariableExpression("it"), "key");
											VariableExpression valueVar = new VariableExpression(parms[1].getName());
											PropertyExpression valueRightExpr = new PropertyExpression(
													new VariableExpression("it"), "value");
											DeclarationExpression keyVarDecl = new DeclarationExpression(
													keyVar, Token.newSymbol(Types.EQUAL, -1, -1), keyRightExpr);
											DeclarationExpression valueVarDecl = new DeclarationExpression(
													valueVar, Token.newSymbol(Types.EQUAL, -1, -1), valueRightExpr);
											
											loopBlock.addStatement(new ExpressionStatement(keyVarDecl));
											loopBlock.addStatement(new ExpressionStatement(valueVarDecl));
										}
										/* Set code of the loopBlock */
//										loopBlock.addStatement(((ClosureExpression) firstArg).getCode());
										BlockStatement eachBlock = (BlockStatement)((ClosureExpression) firstArg).getCode();
										loopBlock.addStatements(eachBlock.getStatements());
									}
									else
									{
										System.out.println("[GEachClosureTransformer.handleABlock] unexpected number of parameters: " + parms.length);
									}
									
									if(varName != null)
									{
										Parameter var = new Parameter(ClassHelper.OBJECT_TYPE, varName);
										ForStatement newStmt = new ForStatement(var, collExpr, loopBlock);
										
										/* Replace the "each" closure with a for loop statement */
										gStmtList.set(i, newStmt);
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
				{}
				else if(gSubStmt instanceof BreakStatement)
				{}
				else if(gSubStmt instanceof BlockStatement)
				{
					this.handleABlock(gSubStmt);
				}
				else
				{
					System.out.println("[GEachClosureTransformer.handleABlock] unexpected statement type: " + gSubStmt);
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
			System.out.println("[GEachClosureTransformer.handleABlock] a wrong call!!!");
		}
	}
}
