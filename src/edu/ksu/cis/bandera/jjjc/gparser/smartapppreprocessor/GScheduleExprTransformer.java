package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
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

/* Before: 
 * runIn(findFalseAlarmThreshold() * 60, "takeAction", [overwrite: false])
 * After:
 * increaseSTSystemTime(findFalseAlarmThreshold() * 60);
 * takeAction();
 * */
public class GScheduleExprTransformer {
	/********************************************/
	private List<String> scheduleMethodList;
	private Set<String> localMethSet;
	private Set<String> fieldNameSet;
	/* schedule(riseTime, sunriseHandler)
	 * ClassName: SunriseSunset
	 * => record: SunriseSunset_riseTime
	 * */
	private Set<String> scheduleTimeVarSet;
	private String className;
	/********************************************/

	public GScheduleExprTransformer(Set<String> localMethSet, Set<String> fieldNameSet)
	{
		this.localMethSet = localMethSet;
		scheduleMethodList = Arrays.asList("runIn", "schedule", "runOnce");
		this.fieldNameSet = fieldNameSet;
		this.scheduleTimeVarSet = new HashSet<String>();
	}
	
	public Set<String> getScheduleTimeVarSet()
	{
		return this.scheduleTimeVarSet;
	}

	public void processAClassNode(ClassNode classNode)
	{
		this.className = classNode.getName();
		
		for(MethodNode meth : classNode.getMethods())
		{
			if(this.localMethSet.contains(meth.getName()))
			{
				this.handleABlock(meth.getCode());
			}
		}
	}

	/* Before: 
	 * runIn(findFalseAlarmThreshold() * 60, "takeAction", [overwrite: false])
	 * After:
	 * increaseSTSystemTime(findFalseAlarmThreshold() * 60);
	 * takeAction();
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
						
						if(this.scheduleMethodList.contains(methText))
						{
							Expression args = mce.getArguments();
							List<Expression> exprList = GUtil.buildExprList(args);
							
							if(methText.equals("runIn"))
							{
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
										/* increaseSTSystemTime(findFalseAlarmThreshold() * 60); */
										{
											List<Expression> newExprList = new ArrayList<Expression>();
											ArgumentListExpression argListExpr;
											MethodCallExpression newMCE;
											ExpressionStatement newExprStmt;

											/* Add the first parameter of exprList to the newExprList */
											newExprList.add(exprList.get(0));
											argListExpr = new ArgumentListExpression(newExprList);

											newMCE = new MethodCallExpression(
													new VariableExpression("this"),
													"increaseSTSystemTime",
													argListExpr);
											newExprStmt = new ExpressionStatement(newMCE);
											gStmtList.add(i, newExprStmt);
										}
										
										/* takeAction(); */
										{
											ConstantExpression newMethod = new ConstantExpression(refMethName);
											
											mce.setMethod(newMethod);
											mce.setArguments(MethodCallExpression.NO_ARGUMENTS);
										}
									}
								}
								else
								{
									System.out.println("[GScheduleExprTransformer.handleABlock] size of exprList = " + exprList.size());
								}
							}
							else if(methText.equals("schedule") || methText.equals("runOnce"))
							{
								/* Before: schedule(riseTime, sunriseHandler)
								 * After: SunriseSunset_riseTime = riseTime
								 * */
								boolean removeNeeded = true;
								
								if(exprList.size() >= 2)
								{
									if(exprList.get(0) instanceof VariableExpression)
									{
										String varName = ((VariableExpression)exprList.get(0)).getName();
										
										if(!this.fieldNameSet.contains(varName))
										{
											/* We need to add a new field */
											String newField = this.className + "_" + varName;
											
											if(!this.scheduleTimeVarSet.contains(newField))
											{
												this.scheduleTimeVarSet.add(newField);
											}
											
											Token operation = Token.newSymbol(Types.ASSIGN, -1, -1);
											BinaryExpression bex = new BinaryExpression(
													new VariableExpression(newField),
													operation,
													new VariableExpression(varName));
											ExpressionStatement newExprStmt = new ExpressionStatement(bex);
											gStmtList.set(i, newExprStmt);
											/* We don't need to remove the current statement since 
											 * we already replace it with the newExprStmt
											 * */
											removeNeeded = false;
										}
									}
								}
								if(removeNeeded)
								{
									/* Remove the schedule method call */
									gStmtList.remove(i);
									i--;
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
					System.out.println("[GScheduleExprTransformer.handleABlock] unexpected statement type: " + gSubStmt);
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
			System.out.println("[GScheduleExprTransformer.handleABlock] a wrong call!!!" + gStmt);
		}
	}
}
