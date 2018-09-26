package edu.ksu.cis.bandera.jjjc.gparser.g2jconverter;

import java.util.List;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.CastExpression;
import org.codehaus.groovy.ast.expr.ClosureExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.ConstructorCallExpression;
import org.codehaus.groovy.ast.expr.ElvisOperatorExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.GStringExpression;
import org.codehaus.groovy.ast.expr.MapExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.NotExpression;
import org.codehaus.groovy.ast.expr.PostfixExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.UnaryMinusExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;

import ca.mcgill.sable.util.LinkedList;
import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;
import edu.ksu.cis.bandera.jjjc.node.*;

public class GExprBuilder {
	public static PExp buildAExp(Expression gExpr)
	{
		PExp jExpr = null;
		
		if(GUtil.isExprALiteral(gExpr))
		{
			jExpr = GExprBuilder.buildALiteralExp(gExpr);
		}
		else if(gExpr instanceof PropertyExpression)
		{
			jExpr = GExprBuilder.buildAFieldAccessExp((PropertyExpression) gExpr);
		}
		else if(gExpr instanceof VariableExpression)
		{
			jExpr = GExprBuilder.buildANameExp(gExpr);
		}
		else if(GUtil.isExprAnArrayAccess(gExpr))
		{
			jExpr = GExprBuilder.buildAArrayAccessExp(gExpr);
		}
		else if(gExpr instanceof BinaryExpression)
		{
			org.codehaus.groovy.syntax.Token gToken = ((BinaryExpression) gExpr).getOperation();
			PBinaryOperator aBinaryOperator = GOperatorBuilder.buildABinaryOperator(gToken);
			
			if(aBinaryOperator != null)
			{
				Expression gLeftExpr = ((BinaryExpression) gExpr).getLeftExpression();
				PExp jFirstExpr = GExprBuilder.buildAExp(gLeftExpr);
				
				if(jFirstExpr != null)
				{
					Expression gRightExpr = ((BinaryExpression) gExpr).getRightExpression();
					PExp jSecondExpr = GExprBuilder.buildAExp(gRightExpr);
					
					if(jSecondExpr != null)
					{
//						if(!GUtil.isExprAnArrayAccess(gRightExpr) && (gRightExpr instanceof BinaryExpression))
//						{
//							/* We need to put jSecondExpr into a "(jSecondExpr)" */
//							jSecondExpr = new AParExp(new TLPar(), jSecondExpr, new TRPar());
//						}
						jExpr = new ABinaryExp(jFirstExpr, aBinaryOperator, jSecondExpr);
					}
					else
					{
						System.out.println("[GExprBuilder.buildAExp] unexpected right expression type: " + gRightExpr);
					}
				}
				else
				{
					System.out.println("[GExprBuilder.buildAExp] unexpected left expression type: " + gLeftExpr);
				}
			}
			else if(gToken.getText().equals("="))
			{
				Expression gLeftExpr = ((BinaryExpression) gExpr).getLeftExpression();
				PLeftHandSide jLeftHandSide = GLeftHandSideBuilder.build(gLeftExpr);
				
				if(jLeftHandSide != null)
				{
					Expression gRightExpr = ((BinaryExpression) gExpr).getRightExpression();
					PExp jSecondExpr = GExprBuilder.buildAExp(gRightExpr);
					
					if(jSecondExpr != null)
					{
						PAssignmentOperator assignOp = new AAssignAssignmentOperator(new TAssign());
						jExpr = new AAssignmentExp(jLeftHandSide, assignOp, jSecondExpr);
					}
					else
					{
						System.out.println("[GExprBuilder.buildAExp] unexpected right expression type: " + gRightExpr);
					}
				}
				else
				{
					System.out.println("[GExprBuilder.buildAExp] unexpected left handside expression type: " + gLeftExpr);
				}
			}
			else
			{
				System.out.println("[GExprBuilder.buildAExp] unexpected binary operator: " + gToken);
			}
		}
		else if(gExpr instanceof MethodCallExpression)
		{
			if(GUtil.isMethCallExprABinaryExpr((MethodCallExpression) gExpr))
			{
				MethodCallExpression gMCE = (MethodCallExpression)gExpr;
				java.util.List<Expression> gExprList = GUtil.buildExprList(gMCE.getArguments());
				
				if(gExprList.size() == 2)
				{
					Expression gLeftExpr = gExprList.get(0);
					PExp jFirstExpr = GExprBuilder.buildAExp(gLeftExpr);
					
					if(jFirstExpr != null)
					{
						Expression gRightExpr = gExprList.get(1);
						PExp jSecondExpr = GExprBuilder.buildAExp(gRightExpr);
						
						if(jSecondExpr != null)
						{
//							if(!GUtil.isExprAnArrayAccess(gRightExpr) && (gRightExpr instanceof BinaryExpression))
//							{
//								/* We need to put jSecondExpr into a "(jSecondExpr)" */
//								jSecondExpr = new AParExp(new TLPar(), jSecondExpr, new TRPar());
//							}
							MethodNode targetMeth = gMCE.getMethodTarget();
							
							jExpr = new ABinaryExp(jFirstExpr, 
									GOperatorBuilder.buildABinaryOperatorFromMethName(targetMeth.getName()), jSecondExpr);
						}
						else
						{
							System.out.println("[GExprBuilder.buildAExp] unexpected second expression type: " + gRightExpr);
						}
					}
					else
					{
						System.out.println("[GExprBuilder.buildAExp] unexpected first expression type: " + gLeftExpr);
					}
				}
				else if(gExprList.size() == 1)
				{
					/* def scheduleDayTime = timeToday(daytime, location.timeZone)
					 * scheduleDayTime = scheduleDayTime + 1
					 * */
					Expression gLeftExpr = gMCE.getObjectExpression();
					PExp jFirstExpr = GExprBuilder.buildAExp(gLeftExpr);
					
					if(jFirstExpr != null)
					{
						Expression gRightExpr = gExprList.get(0);
						PExp jSecondExpr = GExprBuilder.buildAExp(gRightExpr);
						
						if(jSecondExpr != null)
						{
							MethodNode targetMeth = gMCE.getMethodTarget();
							
							jExpr = new ABinaryExp(jFirstExpr, 
									GOperatorBuilder.buildABinaryOperatorFromMethName(targetMeth.getName()), jSecondExpr);
						}
						else
						{
							System.out.println("[GExprBuilder.buildAExp] unexpected argument expression type: " + gRightExpr);
						}
					}
					else
					{
						System.out.println("[GExprBuilder.buildAExp] unexpected object expression type: " + gLeftExpr);
					}
				}
			}
			else if(GUtil.isClosureUsedInMethCallExpr((MethodCallExpression) gExpr))
			{
				if(((MethodCallExpression) gExpr).getMethodAsString().equals("sort"))
				{
					jExpr = GExprBuilder.buildAExp(((MethodCallExpression) gExpr).getObjectExpression());
				}
				else
				{
					jExpr = GExprBuilder.buildANameExpFromMethCallExpWithClosure((MethodCallExpression) gExpr);
				}
			}
			else
			{
				Expression gObjectExpr = ((MethodCallExpression) gExpr).getObjectExpression();
				Expression gMethExpr = ((MethodCallExpression) gExpr).getMethod();
				String methName = "";
				
				if(gMethExpr instanceof ConstantExpression)
				{
					methName = ((ConstantExpression)gMethExpr).getText();
				}
				
				if(methName.equals("first") ||
						methName.equals("last"))
				{
					/* Groovy code:
					 * def sensor = states.first()
					 * Java code:
					 * STState sensor = states[0];
					 * */
					PExp jObjectExpr = GExprBuilder.buildAExp(gObjectExpr);
					
					if(jObjectExpr instanceof ANameExp)
					{
						PName jObjectName = ((ANameExp)jObjectExpr).getName();
						
						if(jObjectName != null)
						{
							PExp jLiteralExpr = GExprBuilder.buildALiteralExp(new ConstantExpression(0, true));
							
							if(jLiteralExpr != null)
							{
								jExpr = GExprBuilder.buildAArrayAccessExp(jObjectName, jLiteralExpr);
							}
							else
							{
								System.out.println("[GExprBuilder.buildAExp] jLiteralExpr is null");
							}
						}
						else
						{
							System.out.println("[GExprBuilder.buildAExp] jObjectName is null");
						}
					}
					else
					{
						System.out.println("[GExprBuilder.buildAExp] unexpected object expression type: " + jObjectExpr);
					}
				}
				else
				{
					/* if (t0 >= startTime.getTime())
					 * => getTime() will be ignored and the above statement is
					 * translated to:
					 * if (t0 >= startTime)
					 * */
					if(GUtil.isMethCallIgnorable(methName))
					{
						jExpr = GExprBuilder.buildAExp(gObjectExpr);
					}
					else if(GUtil.isInterestedOjbMethCall(methName))
					{
						/* Before: int illuminanceState = Integer.parseInt(evt.value);
						 * After: int illuminanceState = evt.value;
						 * */
						List<Expression> argList = GUtil.buildExprList(((MethodCallExpression) gExpr).getArguments());
						
						if(argList.size() == 1)
						{
							jExpr = GExprBuilder.buildAExp(argList.get(0));
						}
					}
					else
					{
						jExpr = GExprBuilder.buildANameMethodInvocationExp((MethodCallExpression) gExpr);
					}
				}
			}
		}
		else if(gExpr instanceof PostfixExpression)
		{
			jExpr = GExprBuilder.buildAPostfixExp((PostfixExpression) gExpr);
		}
		else if((gExpr instanceof GStringExpression) ||
				(gExpr.getType().getName().equals("java.lang.String")))
		{
			jExpr = GExprBuilder.buildAExpForStr(gExpr);
		}
		else if(gExpr instanceof ElvisOperatorExpression)
		{
			jExpr = GExprBuilder.buildAExp(((ElvisOperatorExpression)gExpr).getTrueExpression());
		}
		else if(gExpr instanceof ConstructorCallExpression)
		{
			if(GUtil.isATimeDataType(gExpr.getType()))
			{
				java.util.List<Expression> gExprList = GUtil.buildExprList(((ConstructorCallExpression)gExpr).getArguments());
				
				if(gExprList.size() > 0)
				{
					jExpr = GExprBuilder.buildAExp(gExprList.get(0));
				}
				else
				{
					MethodCallExpression mce = new MethodCallExpression(
							new VariableExpression("this"),
							"now",
							MethodCallExpression.NO_ARGUMENTS
							);
					jExpr = GExprBuilder.buildAExp(mce);
				}
			}
			else
			{
				System.out.println("[GExprBuilder.buildAExp] unexpected constructor types!!! " + gExpr);
			}
		}
		else if(gExpr instanceof MapExpression)
		{
			/* We will handle this case later */
		}
		else if(gExpr instanceof NotExpression)
		{
			/* Will be translated into an AUnaryExp */
			Expression gSubExpr = ((NotExpression)gExpr).getExpression();
			PExp jSubExpr = buildAExp(gSubExpr);
			
			if(jSubExpr != null)
			{
				PUnaryOperator jUnaryOperator = new AComplementUnaryOperator(new TComplement());
				jExpr = new AUnaryExp(jUnaryOperator, jSubExpr);
			}
			else
			{
				System.out.println("[GExprBuilder.buildAExp] jSubExpr of NotExpression is null!!! ");
			}
		}
		else if(gExpr instanceof UnaryMinusExpression)
		{
			PExp jUnaryExpr = buildAExp(((UnaryMinusExpression)gExpr).getExpression());
			
			if(jUnaryExpr != null)
			{
				PUnaryOperator jUnaryOperator = new AMinusUnaryOperator(new TMinus());
				jExpr = new AUnaryExp(jUnaryOperator, jUnaryExpr);
			}
			else
			{
				System.out.println("[GExprBuilder.buildAExp] jUnaryExpr of UnaryMinusExpression is null!!! ");
			}
		}
		else if(gExpr instanceof CastExpression)
		{
			jExpr = buildAExp(((CastExpression)gExpr).getExpression());
		}
		
		return jExpr;
	}
	
	public static PExp buildAPostfixExp(PostfixExpression gPostFixExpr)
	{
		PExp aPostIncrementExp = null;
		PExp jExp = GExprBuilder.buildANameExp(gPostFixExpr.getExpression());
		
		if(jExp != null)
		{
			String opStr = gPostFixExpr.getOperation().getText();
			
			if(opStr.equals("++"))
			{
				aPostIncrementExp = new APostIncrementExp(jExp, new TPlusPlus());
			}
			else if(opStr.equals("--"))
			{
				aPostIncrementExp = new APostDecrementExp(jExp, new TMinusMinus());
			}
		}
		
		return aPostIncrementExp;
	}
	
	public static PExp buildAArrayAccessExp(Expression gExpr)
	{
		PExp jExpr = null;
		
		if(GUtil.isExprAnArrayAccess(gExpr))
		{
			PName jName = GExprBuilder.buildAName(((BinaryExpression)gExpr).getLeftExpression());
			
			if(jName != null)
			{
				PExp jArrayExpr = GExprBuilder.buildAExp(((BinaryExpression)gExpr).getRightExpression());
				
				if(jArrayExpr != null)
				{
					PArrayAccess jArrayAccess = new ANameArrayAccess(jName, new TLBracket(), jArrayExpr, new TRBracket());
					jExpr = new AArrayAccessExp(jArrayAccess);
				}
			}
		}
		
		return jExpr;
	}
	
	public static PExp buildAArrayAccessExp(PName jArrayVarName, PName jLoopIndexVarName)
	{
		PExp jArrayExpr = new ANameExp((PName)jLoopIndexVarName.clone());
		PArrayAccess jArrayAccess = new ANameArrayAccess(
				(PName)jArrayVarName.clone(), new TLBracket(), jArrayExpr, new TRBracket());
		PExp jExpr = new AArrayAccessExp(jArrayAccess);
		
		return jExpr;
	}
	
	public static PExp buildAArrayAccessExp(PName jArrayVarName, PExp jArrayExpr)
	{
		PArrayAccess jArrayAccess = new ANameArrayAccess(
				(PName)jArrayVarName.clone(), new TLBracket(), jArrayExpr, new TRBracket());
		PExp jExpr = new AArrayAccessExp(jArrayAccess);
		
		return jExpr;
	}
	
	public static PExp buildALiteralExp(Expression gExpr)
	{
		PExp jExpr = null;
		
		if(GUtil.isExprALiteral(gExpr))
		{
			jExpr = new ALiteralExp(GLiteralBuilder.build(gExpr));
		}
		
		return jExpr;
	}
	
	public static PExp buildANameExp(Expression gExpr)
	{
		PExp jExpr = null;
		PName jAName = GExprBuilder.buildAName(gExpr);
		
		if(jAName != null)
		{
			jExpr = new ANameExp(jAName);
		}
		
		return jExpr;
	}
	public static PExp buildANameExp(String varName, String propertyName)
	{
		TId tId = new TId(varName);
		ASimpleName aSimpleName = new ASimpleName(tId);
		TId tPropertyId = new TId(propertyName);
		AQualifiedName aQualifiedName = new AQualifiedName(aSimpleName, new TDot(), tPropertyId);
		return (new ANameExp(aQualifiedName));
	}
	public static PExp buildANameExp(PName varName, String propertyName)
	{
		TId tPropertyId = new TId(propertyName);
		AQualifiedName aQualifiedName = new AQualifiedName((PName)varName.clone(), new TDot(), tPropertyId);
		return (new ANameExp(aQualifiedName));
	}
	public static PExp buildANameExp(String varName)
	{
		TId tId = new TId(varName);
		ASimpleName aSimpleName = new ASimpleName(tId);
		return (new ANameExp(aSimpleName));
	}
	
	/* Groovy code:
	 * def recentEvents = temperatureSensor1.eventsSince(timeAgo)?.findAll { it.name == "temperature" && it.doubleValue == 10}
	 * def alreadySentSms = recentEvents.count { it.doubleValue <= tooCold } > 1
	 * => Java code:
	 * STEvent[] eventsSinceArray1 = temperatureSensor1.eventsSince(timeAgo);
	 * STEvent[] findAllResult1;
	 * int index1 = 0;
	 * int index2 = 0;
	 * while(index1 < eventsSinceArray1.length)
	 * {
	 * 		STEvent it = eventsSinceArray1[index1];
	 * 		int[] additionalTempArray1 = {1}; => We need to handle this case
	 * 		if(it.name == additionalTempArray1 && it.doubleValue == 10)
	 * 		{
	 * 			findAllResult1[index2] = it;
	 * 			index2++;
	 * 		}
	 * 		index1++;
	 * }
	 * STEvent[] recentEvents = findAllResult1;
	 * 
	 * int countResult1 = 0;
	 * int index3 = 0;
	 * while(index3 < recentEvents.length)
	 * {
	 * 		STEvent it = recentEvents[index1];
	 * 		if(it.doubleValue <= tooCold)
	 * 		{
	 * 			countResult1++;
	 * 		}
	 * 		index3++;
	 * }
	 * boolean alreadySentSms = countResult1 > 1
	 * */
	public static PExp buildANameExpFromMethCallExpWithClosure(MethodCallExpression gMCE)
	{
		PExp jExpr = null;
		String methName = gMCE.getMethodAsString();
		
		if(GUtil.isClosureUsedInMethCallExpr(gMCE) && GUtil.isClosureMethodAbleToHandle(methName))
		{
			Expression gObjectExpr = gMCE.getObjectExpression();
			ClassNode gResutlVarType = GUtil.getExprType(gMCE);
			
			if(gResutlVarType != null)
			{
				ClassNode gIteratorType = GUtil.getBaseType(gObjectExpr);
				
				if(gIteratorType != null)
				{
					/* Handle input array name */
					/* STEvent[] eventsSinceArray1 = temperatureSensor1.eventsSince(timeAgo); => eventsSinceArray1 
					 * def alreadySentSms = recentEvents.count { it.doubleValue <= tooCold } > 1 => recentEvents
					 * */
					PName jArrayVarName = GStmtBuilder.buildALocalVariableDeclarationInBlockedStmt(gObjectExpr);
					
					if(jArrayVarName != null)
					{
						/* Handle closure result name */
						/* STEvent[] findAllResult1; => findAllResult1
						 * int countResult1 = 0; => countResult1
						 * */
						String resultVarName = GStmtBuilder.buildALocalVariableDeclarationInBlockedStmt(methName, gResutlVarType);
						
						if(resultVarName != null)
						{
							TId tId = new TId(resultVarName);
							PName jResultVarName = new ASimpleName(tId);
							
							{
								/* Handle the loop index of the created while statement 
								 * E.g.: int index1 = 0;
								 * */
								String loopIndexName = GStmtBuilder.buildALocalVariableDeclarationInBlockedStmt();
								TId tId1 = new TId(loopIndexName);
								PName jLoopIndexVarName = new ASimpleName(tId1);
								PName jArrayIndexVarName = null;
								
								/* Handle the increment index of the result when
								 * the return type is an array of objects 
								 * E.g.: int index2 = 0;
								 * */
								if(!GUtil.isATransformClosure(methName) && 
										(GUtil.toArrayConvertible(gResutlVarType) || GUtil.isArrayType(gResutlVarType)))
								{
									String incrementIndexName = GStmtBuilder.buildALocalVariableDeclarationInBlockedStmt();
									TId tId2 = new TId(incrementIndexName);
									jArrayIndexVarName = new ASimpleName(tId2);
								}
								
								/* Create a while loop to replace the closure code 
								 * Input info:
								 * (1) input array name: jArrayVarName
								 * (2) result variable name: jResultVarName
								 * (3) loop index name: jLoopIndexVarName
								 * (4) increment index name: jArrayIndexVarName
								 * (5) iterator data type: gIteratorType
								 * (6) ClosureExpression
								 * (7) boolean isResultAIntType
								 * Output info:
								 * (1) ANameExp: jExpr (findAllResult1 or countResult1)
								 * */
								{
									java.util.List<Expression> gExprList = GUtil.buildExprList(gMCE.getArguments());
									ClosureExpression gClosureExpr = null;
									
									for(Expression gExpr : gExprList)
									{
										if(gExpr instanceof ClosureExpression)
										{
											gClosureExpr = (ClosureExpression)gExpr;
											break;
										}
									}
									if(gClosureExpr != null)
									{
										boolean isResultAIntType = false;
										
										if(GUtil.toIntConvertible(gResutlVarType))
										{
											isResultAIntType = true;
										}
										GStmtBuilder.buildAWhileStmtForClosure(jArrayVarName, jResultVarName, 
												jLoopIndexVarName, jArrayIndexVarName, gIteratorType, gClosureExpr, 
												isResultAIntType, GUtil.isATransformClosure(methName));
										
										/* Build a name expression to replace the whole MCE with closure in old code */
										jExpr = GExprBuilder.buildANameExp(resultVarName);
									}
								}
							}
						}
					}
				}
			}
		}
		
		return jExpr;
	}
	
	public static PName buildAName(Expression gExpr)
	{
		if(gExpr instanceof VariableExpression)
		{
			TId tId = new TId(((VariableExpression) gExpr).getName());
			return (new ASimpleName(tId));
		}
		else if(gExpr instanceof PropertyExpression)
		{
			Expression gObjectExpr = ((PropertyExpression) gExpr).getObjectExpression();
			Expression gProperty = ((PropertyExpression)gExpr).getProperty();
			PName jObjectName = GExprBuilder.buildAName(gObjectExpr);
			
			if(jObjectName != null)
			{
				String propertyName = gProperty.getText();
				
				if(GUtil.isPropertyIgnorable(propertyName))
				{
					return jObjectName;
				}
				else
				{
					TId tPropertyId = new TId(propertyName);
					return (new AQualifiedName(jObjectName, new TDot(), tPropertyId));
				}
			}
		}
		
		return null;
	}
	
	public static PExp buildANameMethodInvocationExp(MethodCallExpression gMCE)
	{
		PExp jExpr = null;
		String varName = null;
		
		/* ANameMethodInvocationExp is a call to a local method, not
		 * common methods such as plus, minus, ...
		 **/
		/* Get varName if exists */
		{
			Expression gObjectExpr = gMCE.getObjectExpression();
			
			if(gObjectExpr instanceof VariableExpression)
			{
				varName = ((VariableExpression) gObjectExpr).getName();
				
				if(varName.equals("this"))
				{
					varName = null;
				}
			}
		}
//		if(GUtil.isALocalMethCall(gMCE))
		{
			Expression methExpr = gMCE.getMethod();
			
			if(methExpr instanceof ConstantExpression)
			{
				String methName = ((ConstantExpression) methExpr).getText();
				TId methTId;
				ASimpleName methSimpleName;
				
				methName = GUtil.getStandardEvtHandlerName(methName);
				
				if(methName.equals("size"))
				{
					methTId = new TId(varName);
					methSimpleName = new ASimpleName(methTId);
					
					TId tPropertyId = new TId("length");
					AQualifiedName aQualifiedName = new AQualifiedName(methSimpleName, new TDot(), tPropertyId);
					jExpr = new ANameExp(aQualifiedName);
				}
				else
				{
					LinkedList jArgList = new LinkedList();
					java.util.List<Expression> gExprList = GUtil.buildExprList(gMCE.getArguments());
					PName methFullName;
					
					/* Create method name */
					if(varName != null)
					{
						methTId = new TId(varName);
						methSimpleName = new ASimpleName(methTId);
						
						TId tPropertyId = new TId(methName);
						AQualifiedName aQualifiedName = new AQualifiedName(methSimpleName, new TDot(), tPropertyId);
						methFullName = aQualifiedName;
					}
					else
					{
						methTId = new TId(methName);
						methSimpleName = new ASimpleName(methTId);
						methFullName = methSimpleName;
					}
					
					/* Create argument list */
					for(Expression gExpr : gExprList)
					{
						PExp jArgExpr = null;
						
						jArgExpr = GExprBuilder.buildAExp(gExpr);
						
						if(jArgExpr != null)
						{
							jArgList.add(jArgExpr);
						}
					}
					/* Create an expression */
					jExpr = new ANameMethodInvocationExp(methFullName, new TLPar(), jArgList, new TRPar());
				}
			}
		}
		
		return jExpr;
	}
	
	public static PExp buildABinaryExpr(PExp jFirstExpr, String opStr, PExp jSecondExpr)
	{
		PExp jExpr = null;
		PBinaryOperator aBinaryOperator = GOperatorBuilder.buildABinaryOperator(opStr);
		
		if(aBinaryOperator != null)
		{
			jExpr = new ABinaryExp(jFirstExpr, aBinaryOperator, jSecondExpr);
		}
		
		return jExpr;
	}
	
	public static PExp buildAExpForStr(Expression gExpr)
	{
		PExp jExpr = null;
		
		if(GUtil.isExprAStr(gExpr))
		{
			GLiteralContainer literalContainer = GLiteralBuilder.getStrLiteralsFromExprs(null, gExpr);
			
			if(!literalContainer.isEmpty())
			{
				/* Create a list of AExpVariableInitializer */
				if(literalContainer.digitList.size() > 0)
				{
					PLiteral jLiteral;
					
					
					/* Create a ALiteral */
					TDecimalIntegerLiteral node1 = new TDecimalIntegerLiteral("0");
					ADecimalIntegerLiteral node2 = new ADecimalIntegerLiteral(node1);
					jLiteral = new AIntegerLiteralLiteral(node2);
					
					/* Create an ALiteralExp */
					jExpr = new ALiteralExp(jLiteral);
				}
				else
				{
					Integer firstNonZeroInt = 0;
					PLiteral jLiteral;
					
					for(Integer intVal : literalContainer.staticValList)
					{
						if(intVal != 0)
						{
							firstNonZeroInt = intVal;
							break;
						}
					}
					
					/* Create a ALiteral */
					TDecimalIntegerLiteral node1 = new TDecimalIntegerLiteral(firstNonZeroInt.toString());
					ADecimalIntegerLiteral node2 = new ADecimalIntegerLiteral(node1);
					jLiteral = new AIntegerLiteralLiteral(node2);
					
					/* Create an ALiteralExp */
					jExpr = new ALiteralExp(jLiteral);
				}
			}
		}
		return jExpr;
	}
	
	public static PExp buildAFieldAccessExp(PropertyExpression gExpr)
	{
		PExp jExpr = null;
		PExp jObjExpr = buildAExp(gExpr.getObjectExpression());
		String propName = gExpr.getPropertyAsString();
		
		/* We will ignore "time" property
		 * E.g.: def elapsed = now() - sensor.rawDateCreated.time
		 * */
		if(propName.equals("time"))
		{
//			ClassNode inferredType = gExpr.getObjectExpression().getNodeMetaData(StaticCompilationMetadataKeys.PROPERTY_OWNER);
//			if((inferredType != null) && GUtil.isATimeDataType(inferredType))
//			{
//				jExpr = jObjExpr;
//			}
			jExpr = jObjExpr;
		}
		else
		{
			if(jObjExpr != null)
			{
				String propStr = gExpr.getPropertyAsString();
				APrimaryFieldAccess primField = new APrimaryFieldAccess(
						jObjExpr,
						new TDot(),
						new TId(propStr));
				jExpr = new AFieldAccessExp(primField);
			}
		}
		
		return jExpr;
	}
}
