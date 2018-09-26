package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MapEntryExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.NamedArgumentListExpression;
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

/* Before: momentarySwitch.off(delay: 4000)
 * After: 
 * increaseSTSystemTime(4000)
 * momentarySwitch.off(delay: 4000)
 * */
public class GSTCommandDelayTransformer {
	/********************************************/
	private Set<String> localMethSet;
	/********************************************/

	public GSTCommandDelayTransformer(Set<String> localMethSet)
	{
		this.localMethSet = localMethSet;
	}

	public void processAClassNode(ClassNode classNode)
	{
		for(MethodNode meth : classNode.getMethods())
		{
			if(this.localMethSet.contains(meth.getName()))
			{
				this.handleABlock(meth.getCode());
			}
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
					Expression expr = ((ExpressionStatement) gSubStmt).getExpression();

					if(expr instanceof MethodCallExpression)
					{
						MethodCallExpression mce = (MethodCallExpression)expr;
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
						
						if(GUtil.isASTCommand(methText))
						{
							Expression args = mce.getArguments();
							List<Expression> exprList = GUtil.buildExprList(args);
							Expression delayAmountExpr = null;
							
							if(exprList.size() > 0)
							{
								for(Expression arg : exprList)
								{
									if(arg instanceof NamedArgumentListExpression)
									{
										for(MapEntryExpression entry : ((NamedArgumentListExpression)arg).getMapEntryExpressions())
										{
											Expression key = entry.getKeyExpression();
											
											if(key instanceof ConstantExpression)
											{
												String keyStr = key.getText().toLowerCase();
												
												if(keyStr.equals("delay"))
												{
													delayAmountExpr = entry.getValueExpression();
													break;
												}
											}
										}
									}
								}
							}
							if(delayAmountExpr != null)
							{
								/* increaseSTSystemTime(4000); */
								List<Expression> newExprList = new ArrayList<Expression>();
								ArgumentListExpression argListExpr;
								MethodCallExpression newMCE;
								ExpressionStatement newExprStmt;

								/* Add the delayAmountExpr to the newExprList */
								newExprList.add(delayAmountExpr);
								argListExpr = new ArgumentListExpression(newExprList);

								newMCE = new MethodCallExpression(
										new VariableExpression("this"),
										"increaseSTSystemTime",
										argListExpr);
								newExprStmt = new ExpressionStatement(newMCE);
								gStmtList.add(i, newExprStmt);
								i++;
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
					System.out.println("[GSTCommandDelayTransformer.handleABlock] unexpected statement type: " + gSubStmt);
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
			System.out.println("[GSTCommandDelayTransformer.handleABlock] a wrong call!!!" + gStmt);
		}
	}
}
