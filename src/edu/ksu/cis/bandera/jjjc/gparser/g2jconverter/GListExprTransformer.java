package edu.ksu.cis.bandera.jjjc.gparser.g2jconverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.GenericsType;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.BooleanExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.DeclarationExpression;
import org.codehaus.groovy.ast.expr.EmptyExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.ListExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.PostfixExpression;
import org.codehaus.groovy.ast.expr.TernaryExpression;
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
import org.codehaus.groovy.transform.stc.StaticTypesMarker;

import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;

/* Before: (switches + onSwitches).findAll{it}
 * After:
 * STSwitch[] STSwitch_arr0;
 * int index0 = 0;
 * for(it in switches)
 * {
 * 		STSwitch_arr0[index0] = it;
 * 		index0++;
 * }
 * for(it in onSwitches)
 * {
 * 		STSwitch_arr0[index0] = it;
 * 		index0++;
 * }
 * STSwitch_arr0.findAll{it}
 * */
public class GListExprTransformer{
	/********************************************/
	private List<String> listMethList;
	private Map<String, Integer> deviceTypeIndexMap;
	private Set<String> localMethSet;
	private int intIndex;
	private List<Statement> currentStmtList;
	private int insertPosition;
	/********************************************/
	
	public GListExprTransformer(Set<String> localMethSet) {
		this.listMethList = Arrays.asList("findAll", "find");
		this.deviceTypeIndexMap = new HashMap<String, Integer>();
		this.localMethSet = localMethSet;
		this.intIndex = 0;
	}
	
	private List<String> getListVarNames(MethodCallExpression mce)
	{
		List<String> result = new ArrayList<String>();
		String methName = mce.getMethodAsString();
		
		if(methName.equals("plus"))
		{
			List<Expression> argList = GUtil.buildExprList(mce.getArguments());
			
			if(argList.size() == 1)
			{
				Expression objExpr = mce.getObjectExpression();
				Expression firstArg = argList.get(0);
				
				/* Handle objExpr */
				if(objExpr instanceof MethodCallExpression)
				{
					result.addAll(this.getListVarNames((MethodCallExpression) objExpr));
				}
				else if(objExpr instanceof VariableExpression)
				{
					ClassNode gType = objExpr.getNodeMetaData(StaticTypesMarker.INFERRED_TYPE);
					
					if(gType != null)
					{
						if(gType.getName().contains("List"))
						{
							String varName = ((VariableExpression) objExpr).getName();
							result.add(varName);
						}
					}
				}
				
				if(firstArg instanceof VariableExpression)
				{
					ClassNode gType = firstArg.getNodeMetaData(StaticTypesMarker.INFERRED_TYPE);
					
					if(gType != null)
					{
						if(gType.getName().contains("List"))
						{
							String varName = ((VariableExpression) firstArg).getName();
							result.add(varName);
						}
					}
				}
			}
		}
		
		return result;
	}
	
	private ClassNode getBaseType(MethodCallExpression mce)
	{
		ClassNode baseType = null;
		String methName = mce.getMethodAsString();
		
		if(methName.equals("plus"))
		{
			List<Expression> argList = GUtil.buildExprList(mce.getArguments());
			
			if(argList.size() == 1)
			{
				Expression firstArg = argList.get(0);
				
				if(firstArg instanceof VariableExpression)
				{
					ClassNode gType = firstArg.getNodeMetaData(StaticTypesMarker.INFERRED_TYPE);
					
					if(gType != null)
					{
						if(gType.getName().contains("List"))
						{
							GenericsType[] genericsTypes = gType.getGenericsTypes();
							
							if(genericsTypes.length > 0)
							{
								baseType = genericsTypes[0].getType();
							}
						}
					}
				}
			}
		}
		
		return baseType;
	}
	
	/* for(it in switches)
	 * {
	 * 		STSwitch_arr0[index0] = it;
	 * 		index0++;
	 * }
	 * */
	private ForStatement buildForStatement(String collVar, String resultArrVar, String index, ClassNode baseType)
	{
		Expression collExpr = new VariableExpression(collVar);
		Parameter var = new Parameter(baseType, "it");
		BlockStatement loopBlock = new BlockStatement();
		
		/* Create loopBlock */
		{
			/* STSwitch_arr0[index0] */
			Expression indexVarExpr = new VariableExpression(index, ClassHelper.int_TYPE.getPlainNodeReference());
			Expression arrayVarExpr = new VariableExpression(resultArrVar, baseType.makeArray());
			Token arrayAccOp = Token.newSymbol(Types.LEFT_SQUARE_BRACKET, -1, -1);
			Expression arrAccessExpr = new BinaryExpression(arrayVarExpr, arrayAccOp, indexVarExpr);
			
			/* STSwitch_arr0[index0] = it; */
			Expression itVarExpr = new VariableExpression("it", baseType);
			Token assignOp = Token.newSymbol(Types.ASSIGN, -1, -1);
			Expression assignExpr = new BinaryExpression(arrAccessExpr, assignOp, itVarExpr);
			ExpressionStatement assignExprStmt = new ExpressionStatement(assignExpr);
			loopBlock.addStatement(assignExprStmt);
			
			/* index0++; */
			Token incrOp = Token.newSymbol(Types.PLUS_PLUS, -1, -1);
			Expression incrExpr = new PostfixExpression(indexVarExpr, incrOp);
			ExpressionStatement incrExprStmt = new ExpressionStatement(incrExpr);
			loopBlock.addStatement(incrExprStmt);
		}
		
		return (new ForStatement(var, collExpr, loopBlock));
	}
	
	/* STSwitch[] STSwitch_arr0; */
	private ExpressionStatement buildArrVarDeclarationExprStmt(String arrVarName, ClassNode baseType)
	{
		Expression arrayVarExpr = new VariableExpression(arrVarName, baseType.makeArray());
		Token assignOp = Token.newSymbol(Types.ASSIGN, -1, -1);
		Expression declExpr = new DeclarationExpression(arrayVarExpr, assignOp, new EmptyExpression());
		return (new ExpressionStatement(declExpr));
	}
	
	/* int index0 = 0; */
	private ExpressionStatement buildIntegerVarDeclarationExprStmt(String intVarName)
	{
		Expression arrayVarExpr = new VariableExpression(intVarName, ClassHelper.int_TYPE.getPlainNodeReference());
		Token assignOp = Token.newSymbol(Types.ASSIGN, -1, -1);
		Expression declExpr = new DeclarationExpression(arrayVarExpr, assignOp, new ConstantExpression("0"));
		return (new ExpressionStatement(declExpr));
	}
	
	private String getVarName(Expression expr)
	{
		String result = "tempSearchVar";
		
		if(expr instanceof VariableExpression)
		{
			result = ((VariableExpression) expr).getName();
		}
		
		return result;
	}
	
	private ClassNode getBaseType(ListExpression expr)
	{
		ClassNode baseType = ClassHelper.int_TYPE.getPlainNodeReference();
		List<Expression> exprList = expr.getExpressions();
		
		if(exprList.size() > 0)
		{
			ClassNode inferredType = exprList.get(0).getNodeMetaData(StaticTypesMarker.INFERRED_TYPE);
			
			if(inferredType != null)
			{
				baseType = inferredType;
			}
		}
		
		return baseType;
	}
	
	/* tmSearchArr0[tmIndex0++] = "cool" */
	private ExpressionStatement buildArrAccessAssignment(Expression arrVar, Expression indexVar, Expression rightExpr)
	{
		/* tmSearchArr0[tmIndex0++] */
		Token arrayAccOp = Token.newSymbol(Types.LEFT_SQUARE_BRACKET, -1, -1);
		Expression arrAccessExpr = new BinaryExpression(arrVar, arrayAccOp, indexVar);
		Token assignOp = Token.newSymbol(Types.ASSIGN, -1, -1);
		Expression assignmentlExpr = new BinaryExpression(arrAccessExpr, assignOp, rightExpr);
		return (new ExpressionStatement(assignmentlExpr));
	}
	
	/* Before: if (tm in ["cool","auto"]) {
	 * After:
	 * boolean tmFound = false;
	 * String[] tmSearchArr0;
	 * int tmIndex0 = 0;
	 * tmSearchArr0[tmIndex0++] = "cool"
	 * tmSearchArr0[tmIndex0++] = "auto"
	 * for(it in tmSearchArr0)
	 * {
	 * 		if(it == tm)
	 * 		{
	 * 			tmFound = true;
	 * 			break;
	 * 		}
	 * }
	 * if (tmFound) {
	 * */
	private BooleanExpression processTernaryExpression(TernaryExpression terExpr)
	{
		BooleanExpression boolExpr = null;
		Expression falseExpr = terExpr.getFalseExpression();
		
		if(falseExpr instanceof MethodCallExpression)
		{
			List<Expression> argList = GUtil.buildExprList(((MethodCallExpression) falseExpr).getArguments());
			
			if(argList.size() == 1)
			{
				Expression objExpr = ((MethodCallExpression) falseExpr).getObjectExpression();
				
				if(objExpr instanceof ListExpression)
				{
					ClassNode baseType;
					Expression searchVarExpr = argList.get(0);
					Expression searchArrVar;
					String varName = this.getVarName(searchVarExpr);
					Expression indexVar;
					Expression indexPostfixVar;
					Expression boolFoundVar;
					
					/* boolean tmFound = false; */
					{
						String boolFoundVarName = varName + "Found";
						int index = 0;
						
						if(this.deviceTypeIndexMap.containsKey(boolFoundVarName))
						{
							index = this.deviceTypeIndexMap.get(boolFoundVarName);
						}
						index++;
						this.deviceTypeIndexMap.put(boolFoundVarName, index);
						boolFoundVarName = boolFoundVarName + (index-1);
						
						boolFoundVar = new VariableExpression(boolFoundVarName, ClassHelper.boolean_TYPE.getPlainNodeReference());
						Token assignOp = Token.newSymbol(Types.ASSIGN, -1, -1);
						Expression foundResultDecl = new DeclarationExpression(
								boolFoundVar,
								assignOp,
								new ConstantExpression(Boolean.FALSE, true));
						this.currentStmtList.add(this.insertPosition, new ExpressionStatement(foundResultDecl));
						this.insertPosition++;
					}
					
					/* String[] tmSearchArr0; */
					{
						String searchArrVarName = varName + "SearchArr";
						int index = 0;
						
						if(this.deviceTypeIndexMap.containsKey(searchArrVarName))
						{
							index = this.deviceTypeIndexMap.get(searchArrVarName);
						}
						index++;
						this.deviceTypeIndexMap.put(searchArrVarName, index);
						searchArrVarName = searchArrVarName + (index-1);
						
						baseType = this.getBaseType((ListExpression) objExpr);
						searchArrVar = new VariableExpression(searchArrVarName, baseType.makeArray());
						Token assignOp = Token.newSymbol(Types.ASSIGN, -1, -1);
						Expression searchArrVarDecl = new DeclarationExpression(
								searchArrVar,
								assignOp,
								new EmptyExpression());
						this.currentStmtList.add(this.insertPosition, new ExpressionStatement(searchArrVarDecl));
						this.insertPosition++;
					}
					
					/* int tmIndex0 = 0; */
					{
						String indexVarName = varName + "Index";
						int index = 0;
						
						if(this.deviceTypeIndexMap.containsKey(indexVarName))
						{
							index = this.deviceTypeIndexMap.get(indexVarName);
						}
						index++;
						this.deviceTypeIndexMap.put(indexVarName, index);
						indexVarName = indexVarName + (index-1);
						
						indexVar = new VariableExpression(indexVarName, ClassHelper.int_TYPE.getPlainNodeReference());
						Token assignOp = Token.newSymbol(Types.ASSIGN, -1, -1);
						Expression indexVarDecl = new DeclarationExpression(
								indexVar,
								assignOp,
								new ConstantExpression(0, true));
						this.currentStmtList.add(this.insertPosition, new ExpressionStatement(indexVarDecl));
						this.insertPosition++;
					}
					
					/* tmIndex0++ */
					{
						Token incrOp = Token.newSymbol(Types.PLUS_PLUS, -1, -1);
						indexPostfixVar = new PostfixExpression(indexVar, incrOp);
					}
					
					/* tmSearchArr0[tmIndex0++] = "cool" */
					for(Expression rightExpr : ((ListExpression)objExpr).getExpressions())
					{
						ExpressionStatement newExpr = this.buildArrAccessAssignment(
								searchArrVar, indexPostfixVar, rightExpr);
						this.currentStmtList.add(this.insertPosition, newExpr);
						this.insertPosition++;
					}
					
					 /* for(it in tmSearchArr0)
					 * {
					 * 		if(it == tm)
					 * 		{
					 * 			tmFound = true;
					 * 			break;
					 * 		}
					 * }
					 * */
					{
						Parameter var = new Parameter(baseType, "it");
						BlockStatement forLoopBlock = new BlockStatement();
						
						/* Build forLoopBlock */
						{
							/* it == tm */
							Token compEqOp = Token.newSymbol(Types.COMPARE_EQUAL, -1, -1);
							Expression itVar = new VariableExpression("it", baseType);
							Expression ifExpr = new BinaryExpression(
									itVar,
									compEqOp,
									searchVarExpr);
							BooleanExpression ifBoolExpr = new BooleanExpression(ifExpr);
							BlockStatement ifBlock = new BlockStatement();
							/* Build ifBlock */
							{
								/* tmFound = true; */
								Token assignOp = Token.newSymbol(Types.ASSIGN, -1, -1);
								Expression foundSetExpr = new BinaryExpression(
										boolFoundVar,
										assignOp,
										new ConstantExpression(Boolean.TRUE, true));
								ifBlock.addStatement(new ExpressionStatement(foundSetExpr));
								/* break; */
								ifBlock.addStatement(new BreakStatement());
							}
							forLoopBlock.addStatement(new IfStatement(ifBoolExpr, ifBlock, EmptyStatement.INSTANCE));
						}
						
						ForStatement forStmt = new ForStatement(var, searchArrVar, forLoopBlock);
						this.currentStmtList.add(this.insertPosition, forStmt);
						this.insertPosition++;
					}
					
					/* if (tmFound) { */
					{
						boolExpr = new BooleanExpression(boolFoundVar);
					}
				}
			}
		}
		
		return boolExpr;
	}
	
	/* Before: if (!days || days.contains(today))
	 * After:
	 * boolean containsResult0 = false;
	 * for(it in days)
	 * {
	 * 		if(it == today)
	 * 		{
	 * 			containsResult0 = true;
	 * 			break;
	 * 		}
	 * }
	 * if (!days || containsResult0) {
	 * */
	private Expression processContainsMethCallExpression(MethodCallExpression mce)
	{
		Expression resultExpr = null;
		String methName = mce.getMethodAsString();
		
		if(methName.equals("contains"))
		{
			List<Expression> argList = GUtil.buildExprList(mce.getArguments());
			
			if(argList.size() == 1)
			{
				Expression objExpr = mce.getObjectExpression();
				ClassNode objExprType = objExpr.getNodeMetaData(StaticTypesMarker.INFERRED_TYPE);
				
				if(objExprType != null)
				{
					if(objExprType.getName().contains("List"))
					{
						ClassNode baseType = ClassHelper.OBJECT_TYPE;
						Expression searchVarExpr = argList.get(0);
						Expression boolFoundVar;
						
						/* Infer baseType */
						{
							GenericsType[] genericsType = objExprType.getGenericsTypes();
							
							if(genericsType.length > 0)
							{
								baseType = genericsType[0].getType();
							}
						}
						
						/* boolean containsResult0 = false; */
						{
							String boolFoundVarName ="containsResult";
							int index = 0;
							
							if(this.deviceTypeIndexMap.containsKey(boolFoundVarName))
							{
								index = this.deviceTypeIndexMap.get(boolFoundVarName);
							}
							index++;
							this.deviceTypeIndexMap.put(boolFoundVarName, index);
							boolFoundVarName = boolFoundVarName + (index-1);
							
							boolFoundVar = new VariableExpression(boolFoundVarName, ClassHelper.boolean_TYPE.getPlainNodeReference());
							Token assignOp = Token.newSymbol(Types.ASSIGN, -1, -1);
							Expression foundResultDecl = new DeclarationExpression(
									boolFoundVar,
									assignOp,
									new ConstantExpression(Boolean.FALSE, true));
							this.currentStmtList.add(this.insertPosition, new ExpressionStatement(foundResultDecl));
							this.insertPosition++;
						}
						
						 /* for(it in days)
						 * {
						 * 		if(it == today)
						 * 		{
						 * 			containsResult0 = true;
						 * 			break;
						 * 		}
						 * }
						 * */
						{
							Parameter var = new Parameter(baseType, "it");
							BlockStatement forLoopBlock = new BlockStatement();
							
							/* Build forLoopBlock */
							{
								/* it == containsResult0 */
								Token compEqOp = Token.newSymbol(Types.COMPARE_EQUAL, -1, -1);
								Expression itVar = new VariableExpression("it", baseType);
								Expression ifExpr = new BinaryExpression(
										itVar,
										compEqOp,
										searchVarExpr);
								BooleanExpression ifBoolExpr = new BooleanExpression(ifExpr);
								BlockStatement ifBlock = new BlockStatement();
								/* Build ifBlock */
								{
									/* containsResult0 = true; */
									Token assignOp = Token.newSymbol(Types.ASSIGN, -1, -1);
									Expression foundSetExpr = new BinaryExpression(
											boolFoundVar,
											assignOp,
											new ConstantExpression(Boolean.TRUE, true));
									ifBlock.addStatement(new ExpressionStatement(foundSetExpr));
									/* break; */
									ifBlock.addStatement(new BreakStatement());
								}
								forLoopBlock.addStatement(new IfStatement(ifBoolExpr, ifBlock, EmptyStatement.INSTANCE));
							}
							
							ForStatement forStmt = new ForStatement(var, objExpr, forLoopBlock);
							this.currentStmtList.add(this.insertPosition, forStmt);
							this.insertPosition++;
						}
						
						/* if (!days || containsResult0) { */
						{
							resultExpr = boolFoundVar;
						}
					}
					else
					{
						/* Before: result = text.contains(STORMY[i])
						 * After: result = text == STORMY[i]
						 * */
						Token operation = Token.newSymbol(Types.COMPARE_EQUAL, -1, -1);
						resultExpr = new BinaryExpression(objExpr, operation, argList.get(0));
					}
				}
				else
				{
					/* Before: result = text.contains(STORMY[i])
					 * After: result = text == STORMY[i]
					 * */
					Token operation = Token.newSymbol(Types.COMPARE_EQUAL, -1, -1);
					resultExpr = new BinaryExpression(objExpr, operation, argList.get(0));
				}
			}
		}
		
		return resultExpr;
	}
	private void processBinaryExpression(BinaryExpression bex)
	{
		Expression leftExpr = bex.getLeftExpression();
		Expression rightExpr = bex.getRightExpression();
		
		/* Handle leftExpr */
		if(leftExpr instanceof MethodCallExpression)
		{
			Expression newExpr = this.processContainsMethCallExpression((MethodCallExpression) leftExpr);
			
			if(newExpr != null)
			{
				bex.setLeftExpression(newExpr);
			}
		}
		else if(leftExpr instanceof BinaryExpression)
		{
			this.processBinaryExpression((BinaryExpression) leftExpr);
		}
		
		/* Handle rightExpr */
		if(rightExpr instanceof MethodCallExpression)
		{
			Expression newExpr = this.processContainsMethCallExpression((MethodCallExpression) rightExpr);
			
			if(newExpr != null)
			{
				bex.setRightExpression(newExpr);
			}
		}
		else if(rightExpr instanceof BinaryExpression)
		{
			this.processBinaryExpression((BinaryExpression) rightExpr);
		}
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
					
					if(expr instanceof BinaryExpression)
					{
						Expression rightExpr = ((BinaryExpression) expr).getRightExpression();
						
						if(rightExpr instanceof MethodCallExpression)
						{
							String methName = ((MethodCallExpression) rightExpr).getMethodAsString();
							
							/* STSwitch[] result = (switches + onSwitches).findAll{it} */
							if(this.listMethList.contains(methName))
							{
								Expression objExpr = ((MethodCallExpression) rightExpr).getObjectExpression();
								
								if(objExpr instanceof MethodCallExpression)
								{
									List<String> listVarNames = this.getListVarNames((MethodCallExpression) objExpr);
									
									if(listVarNames.size() > 0)
									{
										ClassNode baseType = this.getBaseType((MethodCallExpression) objExpr);
										
										if(baseType != null)
										{
											String typeName = baseType.getName();
											int position, index = 0;
											String arrVarName;
											String indexVarName = "arrIndex" + this.intIndex++;
											
											if(this.deviceTypeIndexMap.containsKey(typeName))
											{
												index  = this.deviceTypeIndexMap.get(typeName);
											}
											arrVarName = typeName + "Arr" + index;
											index++;
											this.deviceTypeIndexMap.put(typeName, index);
											
											position = 0;
											/* STSwitch[] STSwitch_arr0; */
											{
												ExpressionStatement arrDeclStmt = this.buildArrVarDeclarationExprStmt(arrVarName, baseType);
												gStmtList.add(i+position, arrDeclStmt);
												position++;
											}
											
											/* int index0 = 0; */
											{
												ExpressionStatement intDeclStmt = this.buildIntegerVarDeclarationExprStmt(indexVarName);
												gStmtList.add(i+position, intDeclStmt);
												position++;
											}
											
											/* for(it in switches)
											 * {
											 * 		STSwitch_arr0[index0] = it;
											 * 		index0++;
											 * }
											 * */
											{
												for(String collVar : listVarNames)
												{
													ForStatement forStmt = this.buildForStatement(collVar, arrVarName, indexVarName, baseType);
													gStmtList.add(i+position, forStmt);
													position++;
												}
											}
											
											/* STSwitch_arr0.findAll{it} */
											{
												Expression newOjbExpr = new VariableExpression(arrVarName, baseType.makeArray());
												((MethodCallExpression) rightExpr).setObjectExpression(newOjbExpr);
											}
										}
									}
								}
							}
							else if(methName.equals("plus"))
							{
								/* STSwitch[] result = switches + onSwitches */
								Expression objExpr = (MethodCallExpression) rightExpr;
								
								if(objExpr instanceof MethodCallExpression)
								{
									List<String> listVarNames = this.getListVarNames((MethodCallExpression) objExpr);
									
									if(listVarNames.size() > 0)
									{
										ClassNode baseType = this.getBaseType((MethodCallExpression) objExpr);
										
										if(baseType != null)
										{
											String typeName = baseType.getName();
											int position, index = 0;
											String arrVarName;
											String indexVarName = "arrIndex" + this.intIndex++;
											
											if(this.deviceTypeIndexMap.containsKey(typeName))
											{
												index  = this.deviceTypeIndexMap.get(typeName);
											}
											arrVarName = typeName + "Arr" + index;
											index++;
											this.deviceTypeIndexMap.put(typeName, index);
											
											position = 0;
											/* STSwitch[] STSwitch_arr0; */
											{
												ExpressionStatement arrDeclStmt = this.buildArrVarDeclarationExprStmt(arrVarName, baseType);
												gStmtList.add(i+position, arrDeclStmt);
												position++;
											}
											
											/* int index0 = 0; */
											{
												ExpressionStatement intDeclStmt = this.buildIntegerVarDeclarationExprStmt(indexVarName);
												gStmtList.add(i+position, intDeclStmt);
												position++;
											}
											
											/* for(it in switches)
											 * {
											 * 		STSwitch_arr0[index0] = it;
											 * 		index0++;
											 * }
											 * */
											{
												for(String collVar : listVarNames)
												{
													ForStatement forStmt = this.buildForStatement(collVar, arrVarName, indexVarName, baseType);
													gStmtList.add(i+position, forStmt);
													position++;
												}
											}
											
											/* STSwitch[] result = STSwitch_arr0 */
											{
												Expression newRightExpr = new VariableExpression(arrVarName, baseType.makeArray());
												((BinaryExpression)expr).setRightExpression(newRightExpr);
											}
										}
									}
								}
							}
						}
						
						/* Process "contains" method call expressions */
						{
							this.insertPosition = i;
							this.currentStmtList = gStmtList;
							this.processBinaryExpression((BinaryExpression) expr);
						}
					}
				}
				else if(gSubStmt instanceof IfStatement)
				{
					Expression booleanExpr = ((IfStatement) gSubStmt).getBooleanExpression().getExpression();
					
					if(booleanExpr instanceof TernaryExpression)
					{
						this.insertPosition = i;
						this.currentStmtList = gStmtList;
						BooleanExpression newBoolExpr = this.processTernaryExpression((TernaryExpression) booleanExpr);
						
						if(newBoolExpr != null)
						{
							((IfStatement) gSubStmt).setBooleanExpression(newBoolExpr);
						}
					}
					else if(booleanExpr instanceof BinaryExpression)
					{
						this.insertPosition = i;
						this.currentStmtList = gStmtList;
						this.processBinaryExpression((BinaryExpression) booleanExpr);
					}
					else if(booleanExpr instanceof MethodCallExpression)
					{
						this.insertPosition = i;
						this.currentStmtList = gStmtList;
						Expression newExpr = this.processContainsMethCallExpression((MethodCallExpression) booleanExpr);
						
						if(newExpr != null)
						{
							((IfStatement) gSubStmt).setBooleanExpression(new BooleanExpression(newExpr));
						}
					}
					/* Handle ifBlock and elseBlock of an IfStatement */
					this.handleABlock(((IfStatement) gSubStmt).getIfBlock()); 
					this.handleABlock(((IfStatement) gSubStmt).getElseBlock());
				}
				else if(gSubStmt instanceof WhileStatement)
				{
					Expression booleanExpr = ((WhileStatement) gSubStmt).getBooleanExpression().getExpression();
					if(booleanExpr instanceof TernaryExpression)
					{
						this.insertPosition = i;
						this.currentStmtList = gStmtList;
						BooleanExpression newBoolExpr = this.processTernaryExpression((TernaryExpression) booleanExpr);
						
						if(newBoolExpr != null)
						{
							((WhileStatement) gSubStmt).setBooleanExpression(newBoolExpr);
						}
					}
					else if(booleanExpr instanceof BinaryExpression)
					{
						this.insertPosition = i;
						this.currentStmtList = gStmtList;
						this.processBinaryExpression((BinaryExpression) booleanExpr);
					}
					else if(booleanExpr instanceof MethodCallExpression)
					{
						this.insertPosition = i;
						this.currentStmtList = gStmtList;
						Expression newExpr = this.processContainsMethCallExpression((MethodCallExpression) booleanExpr);
						
						if(newExpr != null)
						{
							((WhileStatement) gSubStmt).setBooleanExpression(new BooleanExpression(newExpr));
						}
					}
					
					/* Handle loopBlock of WhileStatement */
					this.handleABlock(((WhileStatement) gSubStmt).getLoopBlock());
				}
				else if(gSubStmt instanceof DoWhileStatement)
				{
					Expression booleanExpr = ((DoWhileStatement) gSubStmt).getBooleanExpression().getExpression();
					if(booleanExpr instanceof TernaryExpression)
					{
						this.insertPosition = i;
						this.currentStmtList = gStmtList;
						BooleanExpression newBoolExpr = this.processTernaryExpression((TernaryExpression) booleanExpr);
						
						if(newBoolExpr != null)
						{
							((DoWhileStatement) gSubStmt).setBooleanExpression(newBoolExpr);
						}
					}
					else if(booleanExpr instanceof BinaryExpression)
					{
						this.insertPosition = i;
						this.currentStmtList = gStmtList;
						this.processBinaryExpression((BinaryExpression) booleanExpr);
					}
					else if(booleanExpr instanceof MethodCallExpression)
					{
						this.insertPosition = i;
						this.currentStmtList = gStmtList;
						Expression newExpr = this.processContainsMethCallExpression((MethodCallExpression) booleanExpr);
						
						if(newExpr != null)
						{
							((DoWhileStatement) gSubStmt).setBooleanExpression(new BooleanExpression(newExpr));
						}
					}
					
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
						String methName = ((MethodCallExpression) expr).getMethodAsString();
						
						if(this.listMethList.contains(methName))
						{
							Expression objExpr = ((MethodCallExpression) expr).getObjectExpression();
							
							if(objExpr instanceof MethodCallExpression)
							{
								List<String> listVarNames = this.getListVarNames((MethodCallExpression) objExpr);
								
								if(listVarNames.size() > 0)
								{
									ClassNode baseType = this.getBaseType((MethodCallExpression) objExpr);
									
									if(baseType != null)
									{
										String typeName = baseType.getName();
										int position, index = 0;
										String arrVarName;
										String indexVarName = "arrIndex" + this.intIndex++;
										
										if(this.deviceTypeIndexMap.containsKey(typeName))
										{
											index  = this.deviceTypeIndexMap.get(typeName);
										}
										arrVarName = typeName + "Arr" + index;
										index++;
										this.deviceTypeIndexMap.put(typeName, index);
										
										position = 0;
										/* STSwitch[] STSwitch_arr0; */
										{
											ExpressionStatement arrDeclStmt = this.buildArrVarDeclarationExprStmt(arrVarName, baseType);
											gStmtList.add(i+position, arrDeclStmt);
											position++;
										}
										
										/* int index0 = 0; */
										{
											ExpressionStatement intDeclStmt = this.buildIntegerVarDeclarationExprStmt(indexVarName);
											gStmtList.add(i+position, intDeclStmt);
											position++;
										}
										
										/* for(it in switches)
										 * {
										 * 		STSwitch_arr0[index0] = it;
										 * 		index0++;
										 * }
										 * */
										{
											for(String collVar : listVarNames)
											{
												ForStatement forStmt = this.buildForStatement(collVar, arrVarName, indexVarName, baseType);
												gStmtList.add(i+position, forStmt);
												position++;
											}
										}
										
										/* STSwitch_arr0.findAll{it} */
										{
											Expression newOjbExpr = new VariableExpression(arrVarName, baseType.makeArray());
											((MethodCallExpression) expr).setObjectExpression(newOjbExpr);
										}
									}
								}
							}
						}
						else if(methName.equals("plus"))
						{
							/* return (switches + onSwitches) */
							Expression objExpr = (MethodCallExpression) expr;
							
							if(objExpr instanceof MethodCallExpression)
							{
								List<String> listVarNames = this.getListVarNames((MethodCallExpression) objExpr);
								
								if(listVarNames.size() > 0)
								{
									ClassNode baseType = this.getBaseType((MethodCallExpression) objExpr);
									
									if(baseType != null)
									{
										String typeName = baseType.getName();
										int position, index = 0;
										String arrVarName;
										String indexVarName = "arrIndex" + this.intIndex++;
										
										if(this.deviceTypeIndexMap.containsKey(typeName))
										{
											index  = this.deviceTypeIndexMap.get(typeName);
										}
										arrVarName = typeName + "Arr" + index;
										index++;
										this.deviceTypeIndexMap.put(typeName, index);
										
										position = 0;
										/* STSwitch[] STSwitch_arr0; */
										{
											ExpressionStatement arrDeclStmt = this.buildArrVarDeclarationExprStmt(arrVarName, baseType);
											gStmtList.add(i+position, arrDeclStmt);
											position++;
										}
										
										/* int index0 = 0; */
										{
											ExpressionStatement intDeclStmt = this.buildIntegerVarDeclarationExprStmt(indexVarName);
											gStmtList.add(i+position, intDeclStmt);
											position++;
										}
										
										/* for(it in switches)
										 * {
										 * 		STSwitch_arr0[index0] = it;
										 * 		index0++;
										 * }
										 * */
										{
											for(String collVar : listVarNames)
											{
												ForStatement forStmt = this.buildForStatement(collVar, arrVarName, indexVarName, baseType);
												gStmtList.add(i+position, forStmt);
												position++;
											}
										}
										
										/* return STSwitch_arr0 */
										{
											Expression newReturnExpr = new VariableExpression(arrVarName, baseType.makeArray());
											((ReturnStatement) gSubStmt).setExpression(newReturnExpr);
										}
									}
								}
							}
						}
						else
						{
							this.insertPosition = i;
							this.currentStmtList = gStmtList;
							Expression newExpr = this.processContainsMethCallExpression((MethodCallExpression) expr);
							
							if(newExpr != null)
							{
								((ReturnStatement) gSubStmt).setExpression(newExpr);
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
					System.out.println("[GListExprTransformer.handleABlock] unexpected statement type: " + gSubStmt);
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
			System.out.println("[GListExprTransformer.handleABlock] a wrong call!!!" + gStmt);
		}
	}
}
