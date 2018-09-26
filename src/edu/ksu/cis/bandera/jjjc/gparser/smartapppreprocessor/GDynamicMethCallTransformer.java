package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.Variable;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.BooleanExpression;
import org.codehaus.groovy.ast.expr.ClosureExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.DeclarationExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.FieldExpression;
import org.codehaus.groovy.ast.expr.GStringExpression;
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

import edu.ksu.cis.bandera.jjjc.gparser.g2jconverter.GLiteralBuilder;
import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;

/* Before:
 * def meth = "both"
 * alarm."$meth"()
 * After:
 * if(meth == "both")
 * {
 *     alarm.both()
 * }
 * else if(meth == "off")
 * {
 *     alarm.off()
 * }
 * else if(meth == "siren")
 * {
 *     alarm.siren()
 * }
 * else if(meth == "strobe")
 * {
 *     alarm.strobe()
 * }
 * */
public class GDynamicMethCallTransformer {

	/********************************************/
	private Set<String> localMethSet;
	private List<String> httpResponseValueRangeList;
	/********************************************/
	
	public GDynamicMethCallTransformer(Set<String> localMethSet)
	{
		this.localMethSet = localMethSet;
		this.httpResponseValueRangeList = new ArrayList<String>();
	}
	
	/* Getters */
	public List<String> getHttpResponseValueRangeList()
	{
		return this.httpResponseValueRangeList;
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
	
	/* Before:
	 * def meth = "both"
	 * alarm."$meth"()
	 * After:
	 * if(meth == "both")
	 * {
	 *     alarm.both()
	 * }
	 * else if(meth == "off")
	 * {
	 *     alarm.off()
	 * }
	 * else if(meth == "siren")
	 * {
	 *     alarm.siren()
	 * }
	 * else if(meth == "strobe")
	 * {
	 *     alarm.strobe()
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
						Expression meth = mce.getMethod();
						
						/* Check if the method is dynamic */
						if((mce.getMethodAsString() == null) && (meth instanceof GStringExpression))
						{
							Expression objExpr = mce.getObjectExpression();
							
							if(objExpr instanceof VariableExpression)
							{
								String varName = ((VariableExpression) objExpr).getName();
								List<Expression> methList = ((GStringExpression)meth).getValues();
								
								if(methList.size() == 1)
								{
									List<Expression> argList = GUtil.buildExprList(mce.getArguments());
									
									if(varName.equals("this"))
									{
										if(argList.size() == 0)
										{
											/* Before:
											 * "$state.method"()
											 * After:
											 * if(state.method == "unsubscribe")
											 * {
											 * 		unsubscribe()
											 * }
											 * */
											BooleanExpression boolExpr =  new BooleanExpression(
																				new BinaryExpression(
																						methList.get(0),
																						Token.newSymbol(Types.COMPARE_EQUAL, -1, -1),
																						new ConstantExpression("unsubscribe")));
											MethodCallExpression staticMCE = new MethodCallExpression(
																				new VariableExpression("this"),
																				"unsubscribe",
																				MethodCallExpression.NO_ARGUMENTS);
											BlockStatement ifBlock = new BlockStatement();
											ifBlock.addStatement(new ExpressionStatement(staticMCE));
											Statement ifStmt = new IfStatement(boolExpr, ifBlock, EmptyStatement.INSTANCE);
											
											/* Replace the dynamic meth call with the ifStmt */
											gStmtList.set(i, ifStmt);
											
											/* Add "unsubscribe" to enum list */
											{
												int intVal = GLiteralBuilder.getIntValueFromStr("unsubscribe");
												
												if(intVal == 0)
												{
													GLiteralBuilder.inputEnumList.add("unsubscribe");
													intVal = GLiteralBuilder.getIntValueFromStr("unsubscribe");
												}
												this.httpResponseValueRangeList.add("" + intVal);
											}
										}
										else if(argList.size() == 1)
										{
											/* Before:
											 * "$state.method"(mode)
											 * After:
											 * if(state.method == "setLocationMode")
											 * {
											 * 		setLocationMode(mode)
											 * }
											 * */
											BooleanExpression boolExpr =  new BooleanExpression(
																				new BinaryExpression(
																						methList.get(0),
																						Token.newSymbol(Types.COMPARE_EQUAL, -1, -1),
																						new ConstantExpression("setLocationMode")));
											MethodCallExpression staticMCE = new MethodCallExpression(
																				new VariableExpression("this"),
																				"setLocationMode",
																				mce.getArguments());
											BlockStatement ifBlock = new BlockStatement();
											ifBlock.addStatement(new ExpressionStatement(staticMCE));
											Statement ifStmt = new IfStatement(boolExpr, ifBlock, EmptyStatement.INSTANCE);
											
											/* Replace the dynamic meth call with the ifStmt */
											gStmtList.set(i, ifStmt);
											
											/* Add "setLocationMode" to enum list */
											{
												int intVal = GLiteralBuilder.getIntValueFromStr("setLocationMode");
												
												if(intVal == 0)
												{
													GLiteralBuilder.inputEnumList.add("setLocationMode");
													intVal = GLiteralBuilder.getIntValueFromStr("setLocationMode");
												}
												this.httpResponseValueRangeList.add("" + intVal);
											}
										}
										else
										{
											System.out.println("[GDynamicMethCallTransformer.handleABlock] unexpected arguments!!! " + argList);
										}
									}
									else
									{
										/* def meth = "both"
										 * alarm."$meth"()
										 * */
										if(GUtil.varName2TypeMap.containsKey(varName))
										{
											String deviceType = GUtil.varName2TypeMap.get(varName).getName();
											
											if(deviceType.equals("STAlarm"))
											{
												/* Before:
												 * def meth = "both"
												 * alarm."$meth"()
												 * After:
												 * if(meth == "both")
												 * {
												 * 		alarm.both()
												 * }
												 * */
												/* "both" */
												{
													BooleanExpression boolExpr =  new BooleanExpression(
															new BinaryExpression(
																	methList.get(0),
																	Token.newSymbol(Types.COMPARE_EQUAL, -1, -1),
																	new ConstantExpression("both")));
													MethodCallExpression staticMCE = new MethodCallExpression(
																						mce.getObjectExpression(),
																						"both",
																						mce.getArguments());
													BlockStatement ifBlock = new BlockStatement();
													ifBlock.addStatement(new ExpressionStatement(staticMCE));
													Statement ifStmt = new IfStatement(boolExpr, ifBlock, EmptyStatement.INSTANCE);
													
													/* Replace the dynamic meth call with the ifStmt */
													gStmtList.set(i, ifStmt);
													
													/* Add "setLocationMode" to enum list */
													{
														int intVal = GLiteralBuilder.getIntValueFromStr("both");
														
														if(intVal == 0)
														{
															GLiteralBuilder.inputEnumList.add("both");
															intVal = GLiteralBuilder.getIntValueFromStr("both");
														}
														this.httpResponseValueRangeList.add("" + intVal);
													}
												}
												
												/* "off" */
												{
													BooleanExpression boolExpr =  new BooleanExpression(
															new BinaryExpression(
																	methList.get(0),
																	Token.newSymbol(Types.COMPARE_EQUAL, -1, -1),
																	new ConstantExpression("off")));
													MethodCallExpression staticMCE = new MethodCallExpression(
																						mce.getObjectExpression(),
																						"off",
																						mce.getArguments());
													BlockStatement ifBlock = new BlockStatement();
													ifBlock.addStatement(new ExpressionStatement(staticMCE));
													Statement ifStmt = new IfStatement(boolExpr, ifBlock, EmptyStatement.INSTANCE);
													
													/* Replace the dynamic meth call with the ifStmt */
													gStmtList.add(i, ifStmt);
													
													/* Add "setLocationMode" to enum list */
													{
														int intVal = GLiteralBuilder.getIntValueFromStr("off");
														
														if(intVal == 0)
														{
															GLiteralBuilder.inputEnumList.add("off");
															intVal = GLiteralBuilder.getIntValueFromStr("off");
														}
														this.httpResponseValueRangeList.add("" + intVal);
													}
												}
												
												/* "siren" */
												{
													BooleanExpression boolExpr =  new BooleanExpression(
															new BinaryExpression(
																	methList.get(0),
																	Token.newSymbol(Types.COMPARE_EQUAL, -1, -1),
																	new ConstantExpression("siren")));
													MethodCallExpression staticMCE = new MethodCallExpression(
																						mce.getObjectExpression(),
																						"siren",
																						mce.getArguments());
													BlockStatement ifBlock = new BlockStatement();
													ifBlock.addStatement(new ExpressionStatement(staticMCE));
													Statement ifStmt = new IfStatement(boolExpr, ifBlock, EmptyStatement.INSTANCE);
													
													/* Replace the dynamic meth call with the ifStmt */
													gStmtList.add(i, ifStmt);
													
													/* Add "setLocationMode" to enum list */
													{
														int intVal = GLiteralBuilder.getIntValueFromStr("siren");
														
														if(intVal == 0)
														{
															GLiteralBuilder.inputEnumList.add("siren");
															intVal = GLiteralBuilder.getIntValueFromStr("siren");
														}
														this.httpResponseValueRangeList.add("" + intVal);
													}
												}
												
												/* "strobe" */
												{
													BooleanExpression boolExpr =  new BooleanExpression(
															new BinaryExpression(
																	methList.get(0),
																	Token.newSymbol(Types.COMPARE_EQUAL, -1, -1),
																	new ConstantExpression("strobe")));
													MethodCallExpression staticMCE = new MethodCallExpression(
																						mce.getObjectExpression(),
																						"strobe",
																						mce.getArguments());
													BlockStatement ifBlock = new BlockStatement();
													ifBlock.addStatement(new ExpressionStatement(staticMCE));
													Statement ifStmt = new IfStatement(boolExpr, ifBlock, EmptyStatement.INSTANCE);
													
													/* Replace the dynamic meth call with the ifStmt */
													gStmtList.add(i, ifStmt);
													
													/* Add "setLocationMode" to enum list */
													{
														int intVal = GLiteralBuilder.getIntValueFromStr("strobe");
														
														if(intVal == 0)
														{
															GLiteralBuilder.inputEnumList.add("strobe");
															intVal = GLiteralBuilder.getIntValueFromStr("strobe");
														}
														this.httpResponseValueRangeList.add("" + intVal);
													}
												}
											}
										}
										else
										{
											System.out.println("[GDynamicMethCallTransformer.handleABlock] unknown type!!! " + varName);
										}
									}
								}
								else
								{
									System.out.println("[GDynamicMethCallTransformer.handleABlock] unexpected method!!! " + methList);
								}
							}
							else
							{
								System.out.println("[GDynamicMethCallTransformer.handleABlock] unexpected object expression!!! " + objExpr);
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
