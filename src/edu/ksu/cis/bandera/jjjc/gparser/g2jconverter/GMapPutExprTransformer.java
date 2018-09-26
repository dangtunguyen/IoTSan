package edu.ksu.cis.bandera.jjjc.gparser.g2jconverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.DeclarationExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MapEntryExpression;
import org.codehaus.groovy.ast.expr.MapExpression;
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
 * aMap["test"] = "hello world"
 * After:
 * aMap.put("test", "hello world")
 * */
public class GMapPutExprTransformer {
	/********************************************/
	private String methName;
	private int mapVarIndex;
	private Set<String> localMethSet;
	/********************************************/

	public GMapPutExprTransformer(Set<String> localMethSet)
	{
		this.localMethSet = localMethSet;
	}

	public void processAClassNode(ClassNode classNode)
	{
		for(MethodNode meth : classNode.getMethods())
		{
			if(this.localMethSet.contains(meth.getName()))
			{
				mapVarIndex = 0;
				this.methName = meth.getName();
				this.handleABlock(meth.getCode());
			}
		}
	}

	/* Before: 
	 * aMap["test"] = "hello world"
	 * After:
	 * aMap.put("test", "hello world")
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
					
					if(expr instanceof BinaryExpression)
					{
						BinaryExpression binExpr = (BinaryExpression)expr;
						Expression leftExpr = binExpr.getLeftExpression();

						if(GUtil.isExprAMapAccess(leftExpr))
						{
							Expression rightExpr = binExpr.getRightExpression();
							String varName = ((VariableExpression)(((BinaryExpression)leftExpr).getLeftExpression())).getName();
							Expression keyExpr = ((BinaryExpression)leftExpr).getRightExpression(); /* First parameter */
							Expression objExpr = new VariableExpression(varName, ClassHelper.MAP_TYPE.getPlainNodeReference());
							List<Expression> exprList = new ArrayList<Expression>();
							ArgumentListExpression argListExpr;
							MethodCallExpression newMCE;
							ExpressionStatement newExprStmt;

							/* Add first parameter to the exprList */
							exprList.add(keyExpr);

							if(rightExpr instanceof MapExpression)
							{
								/* map[it.id] = [switch: it.currentSwitch, level: it.currentLevel] */
								/* Create a new map variable and add all items of rightExpr to it EmptyExpression*/
								int insertPos = 0;
								String newMapVarName = this.methName + "MapVar" + this.mapVarIndex;
								this.mapVarIndex++;
								VariableExpression newMapVar = new VariableExpression(newMapVarName, 
										ClassHelper.MAP_TYPE.getPlainNodeReference());
								DeclarationExpression newMapVarDecl = new DeclarationExpression(
										newMapVar, Token.newSymbol(Types.EQUAL, -1, -1), 
										new MapExpression());

								/* Add the created map variable declaration statement to gStmtList */
								gStmtList.add(i+insertPos, new ExpressionStatement(newMapVarDecl));
								insertPos++;

								/* Create a "put" MethodCallExpression for each item of the Map expression */
								for(MapEntryExpression entry : ((MapExpression) rightExpr).getMapEntryExpressions())
								{
									List<Expression> exprList1 = new ArrayList<Expression>();
									ArgumentListExpression argListExpr1;
									MethodCallExpression newMCE1;

									/* Prepare argument list */
									exprList1.add(entry.getKeyExpression());
									exprList1.add(entry.getValueExpression());
									argListExpr1 = new ArgumentListExpression(exprList1);

									newMCE1 = new MethodCallExpression(
											newMapVar,
											"put",
											argListExpr1);
									gStmtList.add(i+insertPos, new ExpressionStatement(newMCE1));
									insertPos++;
								}
								/* Add second parameter to the exprList */
								exprList.add(newMapVar);
								argListExpr = new ArgumentListExpression(exprList);

								newMCE = new MethodCallExpression(
										objExpr,
										"put",
										argListExpr);
								newExprStmt = new ExpressionStatement(newMCE);
								gStmtList.set(i+insertPos, newExprStmt);
								i += insertPos+1;
							}
							else
							{
								/* aMap["test"] = "hello world" */
								/* Add second parameter to the exprList */
								exprList.add(rightExpr);
								argListExpr = new ArgumentListExpression(exprList);

								newMCE = new MethodCallExpression(
										objExpr,
										"put",
										argListExpr);
								newExprStmt = new ExpressionStatement(newMCE);
								gStmtList.set(i, newExprStmt);
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
					
					if(expr instanceof BinaryExpression)
					{
						BinaryExpression binExpr = (BinaryExpression)expr;
						Expression leftExpr = binExpr.getLeftExpression();

						if(GUtil.isExprAMapAccess(leftExpr))
						{
							Expression rightExpr = binExpr.getRightExpression();
							String varName = ((VariableExpression)(((BinaryExpression)leftExpr).getLeftExpression())).getName();
							Expression keyExpr = ((BinaryExpression)leftExpr).getRightExpression(); /* First parameter */
							Expression objExpr = new VariableExpression(varName, ClassHelper.MAP_TYPE.getPlainNodeReference());
							List<Expression> exprList = new ArrayList<Expression>();
							ArgumentListExpression argListExpr;
							MethodCallExpression newMCE;
							ReturnStatement newExprStmt;

							/* Add first parameter to the exprList */
							exprList.add(keyExpr);

							if(rightExpr instanceof MapExpression)
							{
								/* map[it.id] = [switch: it.currentSwitch, level: it.currentLevel] */
								/* Create a new map variable and add all items of rightExpr to it EmptyExpression*/
								int insertPos = 0;
								String newMapVarName = this.methName + "MapVar" + this.mapVarIndex;
								this.mapVarIndex++;
								VariableExpression newMapVar = new VariableExpression(newMapVarName, 
										ClassHelper.MAP_TYPE.getPlainNodeReference());
								DeclarationExpression newMapVarDecl = new DeclarationExpression(
										newMapVar, Token.newSymbol(Types.EQUAL, -1, -1), 
										new MapExpression());

								/* Add the created map variable declaration statement to gStmtList */
								gStmtList.add(i+insertPos, new ExpressionStatement(newMapVarDecl));
								insertPos++;

								/* Create a "put" MethodCallExpression for each item of the Map expression */
								for(MapEntryExpression entry : ((MapExpression) rightExpr).getMapEntryExpressions())
								{
									List<Expression> exprList1 = new ArrayList<Expression>();
									ArgumentListExpression argListExpr1;
									MethodCallExpression newMCE1;

									/* Prepare argument list */
									exprList1.add(entry.getKeyExpression());
									exprList1.add(entry.getValueExpression());
									argListExpr1 = new ArgumentListExpression(exprList1);

									newMCE1 = new MethodCallExpression(
											newMapVar,
											"put",
											argListExpr1);
									gStmtList.add(i+insertPos, new ExpressionStatement(newMCE1));
									insertPos++;
								}
								/* Add second parameter to the exprList */
								exprList.add(newMapVar);
								argListExpr = new ArgumentListExpression(exprList);

								newMCE = new MethodCallExpression(
										objExpr,
										"put",
										argListExpr);
								newExprStmt = new ReturnStatement(newMCE);
								gStmtList.set(i+insertPos, newExprStmt);
								i += insertPos+1;
							}
							else
							{
								/* aMap["test"] = "hello world" */
								/* Add second parameter to the exprList */
								exprList.add(rightExpr);
								argListExpr = new ArgumentListExpression(exprList);

								newMCE = new MethodCallExpression(
										objExpr,
										"put",
										argListExpr);
								newExprStmt = new ReturnStatement(newMCE);
								gStmtList.set(i, newExprStmt);
							}
						}
					}
				}
				else if(gSubStmt instanceof BreakStatement)
				{}
				else if(gSubStmt instanceof BlockStatement)
				{
					this.handleABlock(gSubStmt);
				}
				else
				{
					System.out.println("[GMapPutExprTransformer.handleABlock] unexpected statement type: " + gSubStmt);
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
			System.out.println("[GMapPutExprTransformer.handleABlock] a wrong call!!!" + gStmt);
		}
	}

	public static void main(String[] args)
	{
		List<Integer> list = new ArrayList<Integer>();
		list.addAll(Arrays.asList(1,2,3));

		for(int i = 0; i < list.size(); i++)
		{
			if(i == 1)
			{
				list.add(i, 4);
				list.add(i, 5);
			}
		}
		for(int i = 0; i < list.size(); i++)
		{
			System.out.println(list.get(i));
		}
	}
}
