package edu.ksu.cis.bandera.jjjc.gparser.g2jconverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.ClosureExpression;
import org.codehaus.groovy.ast.expr.ClosureListExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.DeclarationExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.PostfixExpression;
import org.codehaus.groovy.ast.expr.PrefixExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.BreakStatement;
import org.codehaus.groovy.ast.stmt.EmptyStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.ForStatement;
import org.codehaus.groovy.ast.stmt.IfStatement;
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.ast.stmt.WhileStatement;

import ca.mcgill.sable.util.LinkedList;
import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;
import edu.ksu.cis.bandera.jjjc.node.*;

public class GStmtBuilder {
	/********************************************/
	/* Groovy code:
	 * boolean result = send("GString: ${location.name}, $temperatureSensor1, $evt" + s2, i1)
	 * The above statement is translated into following Java code: 
	 * int[] sendArray1 = {800, 700, 400};
	 * boolean result = send(sendArray1, i1);
	 * (1) createdVarNameMap is used to track the created variable name.
	 * This list must be initialized before translating a method.
	 * (2) additionalStmtList is used to store the additional created statements (sendArray1).
	 * This list must be initialized before translating a statement.
	 * */
	public static Map<String, Integer> createdVarNameMap = new HashMap<String, Integer>();
	public static java.util.List<PBlockedStmt> additionalStmtList = new ArrayList<PBlockedStmt>();
	public static boolean isReturnStmtNeeded = false; /* This list must be initialized before translating a method */
	public static Map<String, GVarTypeContainer> varTypeList = new HashMap<String, GVarTypeContainer>(); /* This list must be initialized before translating a method */
	public static int currentIndex = 0; /* This variable is used to create index<currentIndex> variable */
	/********************************************/
	
	/*************************************************/
	public static void initializeCommonManagemetVars(ClassNode gType)
	{
		createdVarNameMap.clear();
		varTypeList.clear();
		additionalStmtList.clear();
		
		if((gType == ClassHelper.VOID_TYPE) || (gType == ClassHelper.OBJECT_TYPE))
		{
			isReturnStmtNeeded = false;
		}
		else
		{
			isReturnStmtNeeded = true;
		}
	}
	public static void initializeCreatedStmtList()
	{
		additionalStmtList.clear();
	}
	public static String getANewVarName(String methName, boolean isAnArray, boolean isAClosure)
	{
		String varName = null;
		int currentIndex = 0;
		
		if(createdVarNameMap.containsKey(methName))
		{
			currentIndex = createdVarNameMap.get(methName);
		}
		currentIndex++;
		if(isAnArray)
		{
			varName = methName + "Array" + currentIndex;
		}
		else if(isAClosure)
		{
			varName = methName + "Result" + currentIndex;
		}
		else
		{
			varName = methName + currentIndex;
		}
		createdVarNameMap.put(methName, currentIndex);
		
		return varName;
	}
	public static String getANewIndexName()
	{
		String indexName = "index" + currentIndex;
		currentIndex++;
		return indexName;
	}
	/*************************************************/
	
	public static PBlockedStmt buildALocalVariableDeclarationInBlockedStmt(Expression gLeftExpr, Expression gRightExpr)
	{
		PBlockedStmt jABlockedStmt = null;
		
		if(gLeftExpr != null)
		{
			/* Update LiteralVarMap */
			GLiteralBuilder.getStrLiteralsFromExprs(gLeftExpr, gRightExpr);
			
			if(gLeftExpr instanceof VariableExpression)
			{
				PLocalVariableDeclaration jALocalVariableDeclaration = GVarDeclaratorBuilder.buildALocalVariableDeclaration(gLeftExpr, gRightExpr);
				
				if(jALocalVariableDeclaration != null)
				{
					jABlockedStmt = new ALocalVariableDeclarationInBlockedStmt(jALocalVariableDeclaration, new TSemicolon());
				}
			}
			else
			{
				System.out.println("[GStmtBuilder.buildALocalVariableDeclarationInBlockedStmt] unexpected expression type: " + gLeftExpr);
			}
		}
		else
		{
			System.out.println("[GStmtBuilder.buildALocalVariableDeclarationInBlockedStmt] fatal error!!! gLeftExpr is null");
		}
		
		return jABlockedStmt;
	}
	
	/* This method is used to build an integer array local variable declaration statement from a 
	 * String/GString-typed expression.
	 * */
	public static String buildALocalVariableDeclarationInBlockedStmt(String methName, Expression gExpr)
	{
		String varName = null;
		
		if(GUtil.isExprConvertibleToIntArray(gExpr))
		{
			PVariableInitializer jVarInitializer = null;
			jVarInitializer = GVarInitializerBuilder.buildAExpVariableInitializerForStr(gExpr);
			
			if(jVarInitializer != null)
			{
				ClassNode gType = GUtil.getExprType(gExpr);
				
				if(gType != null)
				{
					LinkedList jVarDeclaratorList = new LinkedList();
					PType jType = (PType)GTypeConverter.convert(gType);
					PVariableDeclarator jAVariableDeclarator;
					PBlockedStmt jABlockedStmt;
					PLocalVariableDeclaration jALocalVariableDeclaration;
					PVariableDeclaratorId jVarId;
					
					/* Get a new variable name */
					varName = GStmtBuilder.getANewVarName(methName, true, false);
					jVarId = GVarDeclaratorBuilder.buildAVariableDeclaratorId(varName);
					
					jAVariableDeclarator = new AAssignedVariableDeclarator(jVarId,new TAssign(),jVarInitializer);
					jVarDeclaratorList.add(jAVariableDeclarator);
					
					jALocalVariableDeclaration = new ALocalVariableDeclaration(new LinkedList(), jType, jVarDeclaratorList);
					jABlockedStmt = new ALocalVariableDeclarationInBlockedStmt(jALocalVariableDeclaration, new TSemicolon());
					
					/* Update additional created statement list */
					additionalStmtList.add(jABlockedStmt);
					
					/* Update variable type list */
					GStmtBuilder.updateVarTypeList(varName, gType, jType);
				}	
			}	
		}
		
		return varName;
	}
	
	/* This method is used in closure handling. E.g.1:
	 * def recentEvents = temperatureSensor1.eventsSince(timeAgo)?.findAll { it.name == "temperature" }
	 * => We will do the following handling in this method:
	 * STEvent[] eventsSinceArray1 = temperatureSensor1.eventsSince(timeAgo);
	 * E.g.2:
	 * Groovy code: for (it in switches){}
	 * Java code:
	 * STSwitch[] tempArray1 = switches;
	 * int index1 = 0;
	 * while(index1 < tempArray1.length) {}
	 * */
	public static PName buildALocalVariableDeclarationInBlockedStmt(Expression gRightExpr)
	{
		PName jVarName = null;
		
		if((gRightExpr instanceof VariableExpression) ||
				(gRightExpr instanceof PropertyExpression))
		{
			jVarName = GExprBuilder.buildAName(gRightExpr);
		}
		else
		{
			PVariableInitializer jVarInitializer = GVarInitializerBuilder.buildAExpVariableInitializer(gRightExpr);
			
			if(jVarInitializer != null)
			{
				ClassNode gType = GUtil.getExprType(gRightExpr);
				
				if(gType != null)
				{
					String methName = "tempArray";
					LinkedList jVarDeclaratorList = new LinkedList();
					PType jType = (PType)GTypeConverter.convert(gType);
					PVariableDeclarator jAVariableDeclarator;
					PBlockedStmt jABlockedStmt;
					PLocalVariableDeclaration jALocalVariableDeclaration;
					PVariableDeclaratorId jVarId;
					boolean isArray = false;
					String varName = null;
					
					if(gRightExpr instanceof MethodCallExpression)
					{
						methName = ((MethodCallExpression)gRightExpr).getMethodAsString();
						isArray = GUtil.isArrayNameNeeded(methName);
					}
					
					/* Get a new variable name */
					varName = GStmtBuilder.getANewVarName(methName, isArray, false);
					jVarId = GVarDeclaratorBuilder.buildAVariableDeclaratorId(varName);
					jVarName = GExprBuilder.buildAName(new VariableExpression(varName));
					
					jAVariableDeclarator = new AAssignedVariableDeclarator(jVarId,new TAssign(),jVarInitializer);
					jVarDeclaratorList.add(jAVariableDeclarator);
					
					jALocalVariableDeclaration = new ALocalVariableDeclaration(new LinkedList(), jType, jVarDeclaratorList);
					jABlockedStmt = new ALocalVariableDeclarationInBlockedStmt(jALocalVariableDeclaration, new TSemicolon());
					
					/* Update additional created statement list */
					additionalStmtList.add(jABlockedStmt);
					
					/* Update variable type list */
					GStmtBuilder.updateVarTypeList(varName, gType, jType);
				}	
			}
		}
		
		return jVarName;
	}
	/* Groovy code:
	 * def recentEvents = temperatureSensor1.eventsSince(timeAgo)?.findAll { it.name == "temperature" && it.doubleValue == 10}
	 * def alreadySentSms = recentEvents.count { it.doubleValue <= tooCold } > 1
	 * => Java code:
	 * STEvent[] eventsSinceArray1 = temperatureSensor1.eventsSince(timeAgo);
	 * STEvent[] findAllResult1; => this is the result of this method
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
	 * int countResult1 = 0; => this is the result of this method
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
	public static String buildALocalVariableDeclarationInBlockedStmt(String methName, ClassNode gType)
	{
		String varName = null;
		PType jType = (PType) GTypeConverter.convert(gType);
		
		if(jType != null)
		{
			PVariableInitializer jVarInitializer = null;
			LinkedList jVarDeclaratorList = new LinkedList();
			PVariableDeclarator jAVariableDeclarator;
			PBlockedStmt jABlockedStmt;
			PLocalVariableDeclaration jALocalVariableDeclaration;
			PVariableDeclaratorId jVarId;
			
			/* Get a new variable name */
			varName = GStmtBuilder.getANewVarName(methName, false, true);
			jVarId = GVarDeclaratorBuilder.buildAVariableDeclaratorId(varName);
			
			
			if(GUtil.toIntConvertible(gType))
			{
				jVarInitializer = GVarInitializerBuilder.buildAExpVariableInitializer(new ConstantExpression(0, true));
			}
			
			if(jVarInitializer != null)
			{
				jAVariableDeclarator = new AAssignedVariableDeclarator(jVarId,new TAssign(),jVarInitializer);
			}
			else
			{
				jAVariableDeclarator = new AIdVariableDeclarator(jVarId);
			}
			jVarDeclaratorList.add(jAVariableDeclarator);
			
			jALocalVariableDeclaration = new ALocalVariableDeclaration(new LinkedList(), jType, jVarDeclaratorList);
			jABlockedStmt = new ALocalVariableDeclarationInBlockedStmt(jALocalVariableDeclaration, new TSemicolon());
			
			/* Update additional created statement list */
			additionalStmtList.add(jABlockedStmt);
			
			/* Update variable type list */
			GStmtBuilder.updateVarTypeList(varName, gType, jType);
		}
		
		return varName;
	}
	/* This method is used to create an index declaration statement:
	 * int index1 = 0;
	 * */
	public static String buildALocalVariableDeclarationInBlockedStmt()
	{
		String varName = null;
		PVariableInitializer jVarInitializer = null;
		LinkedList jVarDeclaratorList = new LinkedList();
		PVariableDeclarator jAVariableDeclarator;
		PBlockedStmt jABlockedStmt;
		PLocalVariableDeclaration jALocalVariableDeclaration;
		PVariableDeclaratorId jVarId;
		PType jType = (PType)GTypeConverter.createAnIntType();
		
		/* Get a new variable name */
		varName = GStmtBuilder.getANewIndexName();
		jVarId = GVarDeclaratorBuilder.buildAVariableDeclaratorId(varName);
		
		/* Create a local variable declaration */
		jVarInitializer = GVarInitializerBuilder.buildAExpVariableInitializer(new ConstantExpression(0, true));
		jAVariableDeclarator = new AAssignedVariableDeclarator(jVarId,new TAssign(),jVarInitializer);
		jVarDeclaratorList.add(jAVariableDeclarator);
		
		jALocalVariableDeclaration = new ALocalVariableDeclaration(new LinkedList(), jType, jVarDeclaratorList);
		jABlockedStmt = new ALocalVariableDeclarationInBlockedStmt(jALocalVariableDeclaration, new TSemicolon());
		
		/* Update additional created statement list */
		additionalStmtList.add(jABlockedStmt);
		
		return varName;
	}
	
	/* STEvent it = eventsSinceArray1[index1];
	 * */
	public static PBlockedStmt buildALocalVariableDeclarationInBlockedStmt(String itName, 
			ClassNode gItType, PName jArrayVarName, PName jLoopIndexVarName)
	{
		PBlockedStmt jABlockedStmt = null;
		PVariableInitializer jVarInitializer = null;
		jVarInitializer = GVarInitializerBuilder.buildAArrayAccessExpVariableInitializer((PName)jArrayVarName.clone(), 
				(PName)jLoopIndexVarName.clone());
		
		if(jVarInitializer != null)
		{
			LinkedList jVarDeclaratorList = new LinkedList();
			PType jType = (PType)GTypeConverter.convert(gItType);
			PVariableDeclarator jAVariableDeclarator;
			
			PLocalVariableDeclaration jALocalVariableDeclaration;
			PVariableDeclaratorId jVarId;
			jVarId = GVarDeclaratorBuilder.buildAVariableDeclaratorId(itName);
			
			jAVariableDeclarator = new AAssignedVariableDeclarator(jVarId,new TAssign(),jVarInitializer);
			jVarDeclaratorList.add(jAVariableDeclarator);
			
			jALocalVariableDeclaration = new ALocalVariableDeclaration(new LinkedList(), jType, jVarDeclaratorList);
			jABlockedStmt = new ALocalVariableDeclarationInBlockedStmt(jALocalVariableDeclaration, new TSemicolon());
			
			/* Update variable type list */
			GStmtBuilder.updateVarTypeList(itName, gItType, jType);
		}	
		
		return jABlockedStmt;
	}
	
	/* 	if(it.name == additionalTempArray1 && it.doubleValue == 10)
	 * 	{
	 * 		findAllResult1[index2] = it;
	 * 		index2++;
	 * 	}
	 *****/
	public static PBlockedStmt buildAIfStmtForClosure(Expression gExpr, PName jResultVarName, 
			PName jArrayIndexVarName, String itName, boolean isResultAIntType)
	{
		PBlockedStmt jABlockedStmt = null;
		PExp jIfExpr = GExprBuilder.buildAExp(gExpr);
		LinkedList jIfBlockedStmtList = new LinkedList();
		
		if(jIfExpr != null)
		{
			if(isResultAIntType)
			{
				/* Build a postfix expression statement: 
				 * countResult1++; 
				 * */
				PBlockedStmt jABlockedStmt1 = GStmtBuilder.buildAPostFixExpStmt((PName)jResultVarName.clone(), "++");
				jIfBlockedStmtList.add(jABlockedStmt1);
			}
			else
			{
				PName jItName = GExprBuilder.buildAName(new VariableExpression(itName));
				
				if(jItName != null)
				{
					if(jArrayIndexVarName != null)
					{
						/* Build an array update statement and an index increment statement
						 *  {
						 * 		findAllResult1[index2] = it;
						 * 		index2++;
						 * 	}
						 * */
						/* Build an array update statement */
						PBlockedStmt jABlockedStmt1 = GStmtBuilder.buildAAssignmentExpStmt(
								(PName)jResultVarName.clone(), (PName)jArrayIndexVarName.clone(), (PName)jItName.clone());
						if(jABlockedStmt1 != null)
						{
							jIfBlockedStmtList.add(jABlockedStmt1);
						}
						
						/* Build an index increment statement */
						jABlockedStmt1 = GStmtBuilder.buildAPostFixExpStmt((PName)jArrayIndexVarName.clone(), "++");
						if(jABlockedStmt1 != null)
						{
							jIfBlockedStmtList.add(jABlockedStmt1);
						}
					}
					else
					{
						/* Build an assignment 
						 * {
						 * 		findResult = it;
						 * }
						 * */
						PBlockedStmt jABlockedStmt1 = GStmtBuilder.buildAAssignmentExpStmt(
								(PName)jResultVarName.clone(), (PName)jItName.clone());
						if(jABlockedStmt1 != null)
						{
							jIfBlockedStmtList.add(jABlockedStmt1);
						}
					}
				}
			}
			/* Build a if block */
			{
				PBlock jIfBlock = new ABlock(new TLBrace(), jIfBlockedStmtList, new TRBrace());
				PStmt jIfStmt = new AIfStmt(new TIf(), new TLPar(), jIfExpr, new TRPar(), jIfBlock,
						new TElse(), new ABlock(new TLBrace(), new LinkedList(), new TRBrace()));
				jABlockedStmt = new AStmtBlockedStmt(jIfStmt);
			}
		}
		
		return jABlockedStmt;
	}
	
	/* Create a while loop to replace the closure code 
	 * Input info:
	 * (1) input array name: jArrayVarName
	 * (2) result variable name: jResultVarName
	 * (3) loop index name: jLoopIndexVarName
	 * (4) increment index name: jArrayIndexVarName
	 * (5) iterator data type: gIteratorType
	 * (6) ClosureExpression
	 * (7) boolean isClosureResultAIntType
	 * Output info: None
	 * */
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
	public static void buildAWhileStmtForClosure(PName jArrayVarName, PName jResultVarName, PName jLoopIndexVarName, 
			PName jArrayIndexVarName, ClassNode gIteratorType, ClosureExpression gClosureExpr, boolean isResultAIntType, 
			boolean isATransformClosure)
	{
		String itName = "it"; /* Default name */
		Parameter[] gClosureParams = gClosureExpr.getParameters();
		PBlockedStmt jItLocalVarDeclarationStmt = null;
		
		/* Handle iterator */
		if(gClosureParams.length > 0)
		{
			itName = gClosureParams[0].getName();
		}
		jItLocalVarDeclarationStmt = GStmtBuilder.buildALocalVariableDeclarationInBlockedStmt(
				itName, gIteratorType, (PName)jArrayVarName.clone(), (PName)jLoopIndexVarName.clone());
		
		if(jItLocalVarDeclarationStmt != null)
		{
			Statement gClosureCode = gClosureExpr.getCode();
			
			if(gClosureCode instanceof BlockStatement)
			{
				java.util.List<Statement> gStmtList = ((BlockStatement)gClosureCode).getStatements();
				
				if(gStmtList.size() > 0)
				{
					LinkedList closureStmtList = new LinkedList();
					/* The last statement should be a boolean expression */
					Statement gLastClosureStmt = gStmtList.get(gStmtList.size()-1);
					int firstAdditionalStmtListSize, secondAdditionalStmtListSize;
					
					/* Handle closure statement list */
					{
						/* Save current size of additionalStmtList */
						firstAdditionalStmtListSize = additionalStmtList.size();
						
						LinkedList resultStmtList = GStmtBuilder.buildABlockStmtList(gClosureCode, true);
						
						/* Get current size of additionalStmtList */
						secondAdditionalStmtListSize = additionalStmtList.size();
						if(firstAdditionalStmtListSize < secondAdditionalStmtListSize)
						{
							/* We need to add the extra statements to created closure 
							 * statement list, and remove those statements from additionalStmtList 
							 * */
							for(int i = firstAdditionalStmtListSize; i < secondAdditionalStmtListSize; i++)
							{
								closureStmtList.add(additionalStmtList.remove(firstAdditionalStmtListSize));
							}
						}
						closureStmtList.addAll(resultStmtList);
					}
					
					if(gLastClosureStmt instanceof ExpressionStatement)
					{
						Expression gLastClosureExpr = ((ExpressionStatement) gLastClosureStmt).getExpression();
						PBlockedStmt jBlockedStmtForLastClosureStmt = null;
						
						/* Save current size of additionalStmtList */
						firstAdditionalStmtListSize = additionalStmtList.size();
						
						if(isATransformClosure)
						{
							/* Groovy code:
							 * def states = motionSensors.collect { it.currentState("motion")}
							 * Java code:
							 * STState[] collectResult1; => base type of array is return type of closure
							 * int loopIndex1 = 0;
							 * while(loopIndex1 < motionSensors.length)
							 * {
							 * 		STMotionSensor it = motionSensors[loopIndex1];
							 * 		int[] tempArray1 = {5};
							 * 		collectResult1[loopIndex1] = it.currentState(tempArray1);
							 * 		loopIndex1++;
							 * }
							 * */
							/* Handler for: collectResult1[loopIndex1] = it.currentState(tempArray1);
							 * */
							jBlockedStmtForLastClosureStmt = GStmtBuilder.buildAAssignmentExpStmt((PName)jResultVarName.clone(), 
									(PName)jLoopIndexVarName.clone(), gLastClosureExpr);
						}
						else
						{
							/* Build a IfStmt */
							if(jArrayIndexVarName != null)
							{
								jBlockedStmtForLastClosureStmt = GStmtBuilder.buildAIfStmtForClosure(gLastClosureExpr,(PName)jResultVarName.clone(), 
										(PName)jArrayIndexVarName.clone(), itName, isResultAIntType);
							}
							else
							{
								jBlockedStmtForLastClosureStmt = GStmtBuilder.buildAIfStmtForClosure(gLastClosureExpr,(PName)jResultVarName.clone(), 
										null, itName, isResultAIntType);
							}
						}
						
						if(jBlockedStmtForLastClosureStmt != null)
						{
							LinkedList jWhileStmtList = new LinkedList();
							PExp jBooleanExp = null;
							
							/* Add iterator variable declaration statement into while loop's statement list */
							jWhileStmtList.add(jItLocalVarDeclarationStmt);
							
							/* Add closure statement list into while loop's statement list */
							jWhileStmtList.addAll(closureStmtList);
							
							/* Get current size of additionalStmtList */
							secondAdditionalStmtListSize = additionalStmtList.size();
							if(firstAdditionalStmtListSize < secondAdditionalStmtListSize)
							{
								/* We need to add the extra statements to created while block's 
								 * statement list, and remove those statements from additionalStmtList 
								 * */
								for(int i = firstAdditionalStmtListSize; i < secondAdditionalStmtListSize; i++)
								{
									jWhileStmtList.add(additionalStmtList.remove(firstAdditionalStmtListSize));
								}
							}
							
							/* Add IfStmt into while loop's statement list */
							jWhileStmtList.add(jBlockedStmtForLastClosureStmt);
							
							/* Create a loop increment index statement */
							{
								PBlockedStmt jWhileLoopIndexIncStmt = GStmtBuilder.buildAPostFixExpStmt((PName)jLoopIndexVarName.clone(), "++");
								
								if(jWhileLoopIndexIncStmt != null)
								{
									jWhileStmtList.add(jWhileLoopIndexIncStmt);
								}
							}
							
							/* Create boolean expression for while loop */
							{
								PExp jFirstExpr = new ANameExp((PName)jLoopIndexVarName.clone());
								PExp jSecondExpr = GExprBuilder.buildANameExp((PName)jArrayVarName.clone(), "length");
								jBooleanExp = GExprBuilder.buildABinaryExpr(jFirstExpr, "<", jSecondExpr);
							}
							
							/* Create a while statement */
							if(jBooleanExp != null)
							{
								PBlock jWhileBlock = new ABlock(new TLBrace(), jWhileStmtList, new TRBrace());
								PStmt jWhileStmt = new AWhileStmt(new TWhile(), new TLPar(), jBooleanExp, new TRPar(), jWhileBlock);
								PBlockedStmt jABlockedStmt = new AStmtBlockedStmt(jWhileStmt);
								
								/* Add the created statement into the additionalStmtList */
								additionalStmtList.add(jABlockedStmt);
							}
						}
					}
				}
			}
		}
	}
	
	/* We need to handle dynamic invocation later
	 * */
	public static PBlockedStmt buildANameMethodInvocationExpStmt(MethodCallExpression gMCE)
	{
		PBlockedStmt jABlockedStmt = null;
		PExp aNameMethodInvocationExp = GExprBuilder.buildANameMethodInvocationExp(gMCE);
		
		if(aNameMethodInvocationExp != null)
		{
			AExpStmt aExpStmt = new AExpStmt(aNameMethodInvocationExp, new TSemicolon());
			jABlockedStmt = new AStmtBlockedStmt(aExpStmt);
		}
		
		return jABlockedStmt;
	}
	
	public static PBlockedStmt buildAAssignmentExpStmt(BinaryExpression gExpr)
	{
		PBlockedStmt jABlockedStmt = null;
		Expression gRightExpr = gExpr.getRightExpression();
		PExp jExpr = GExprBuilder.buildAExp(gRightExpr);
		
		if(jExpr != null)
		{
			Expression gLeftExpr = gExpr.getLeftExpression();
			
			/* Update LiteralVarMap */
			if((gLeftExpr instanceof VariableExpression) ||
					(gLeftExpr instanceof PropertyExpression))	
			{
				GLiteralBuilder.getStrLiteralsFromExprs(gLeftExpr, gRightExpr);
			}

			/* Build the assignment statement */
			{
				PLeftHandSide jLeftHandSide = GLeftHandSideBuilder.build(gLeftExpr);
				AAssignAssignmentOperator aAssignmentOperator = new AAssignAssignmentOperator(new TAssign());
				PExp jWholeExpr = new AAssignmentExp(jLeftHandSide,aAssignmentOperator,jExpr);
				AExpStmt aExpStmt = new AExpStmt(jWholeExpr, new TSemicolon());
				jABlockedStmt = new AStmtBlockedStmt(aExpStmt);
			}
		}
		else
		{
			System.out.println("[GStmtBuilder.buildAAssignmentExpStmt] unexpected right expression type " + gRightExpr);
		}
		
		return jABlockedStmt;
	}
	public static PBlockedStmt buildAAssignmentExpStmt(PName jArrayVarName, PName jIArrayIndexVarName, PName jRightVarName)
	{
		PBlockedStmt jABlockedStmt = null;
		PExp jArrayExpr = new ANameExp((PName)jIArrayIndexVarName.clone());
		PArrayAccess jArrayAccess = new ANameArrayAccess(
				(PName)jArrayVarName.clone(), new TLBracket(), jArrayExpr, new TRBracket());
		
		if(jArrayAccess != null)
		{
			PExp jExpr = new ANameExp(jRightVarName);
			PLeftHandSide jLeftHandSide = new AArrayAccessLeftHandSide(jArrayAccess);
			AAssignAssignmentOperator aAssignmentOperator = new AAssignAssignmentOperator(new TAssign());
			PExp jWholeExpr = new AAssignmentExp(jLeftHandSide,aAssignmentOperator,jExpr);
			AExpStmt aExpStmt = new AExpStmt(jWholeExpr, new TSemicolon());
			jABlockedStmt = new AStmtBlockedStmt(aExpStmt);
		}
		
		return jABlockedStmt;
	}
	public static PBlockedStmt buildAAssignmentExpStmt(PName jArrayVarName, PName jIArrayIndexVarName, Expression gRightExpr)
	{
		PBlockedStmt jABlockedStmt = null;
		PExp jArrayExpr = new ANameExp((PName)jIArrayIndexVarName.clone());
		PArrayAccess jArrayAccess = new ANameArrayAccess(
				(PName)jArrayVarName.clone(), new TLBracket(), jArrayExpr, new TRBracket());
		
		if(jArrayAccess != null)
		{
			PExp jExpr = GExprBuilder.buildAExp(gRightExpr);
			PLeftHandSide jLeftHandSide = new AArrayAccessLeftHandSide(jArrayAccess);
			AAssignAssignmentOperator aAssignmentOperator = new AAssignAssignmentOperator(new TAssign());
			PExp jWholeExpr = new AAssignmentExp(jLeftHandSide,aAssignmentOperator,jExpr);
			AExpStmt aExpStmt = new AExpStmt(jWholeExpr, new TSemicolon());
			jABlockedStmt = new AStmtBlockedStmt(aExpStmt);
		}
		
		return jABlockedStmt;
	}
	public static PBlockedStmt buildAAssignmentExpStmt(PName jLeftVarName, PName jRightVarName)
	{
		PBlockedStmt jABlockedStmt = null;
		
		PExp jExpr = new ANameExp((PName)jRightVarName.clone());
		PLeftHandSide jLeftHandSide = new ANameLeftHandSide((PName)jLeftVarName.clone());
		AAssignAssignmentOperator aAssignmentOperator = new AAssignAssignmentOperator(new TAssign());
		PExp jWholeExpr = new AAssignmentExp(jLeftHandSide,aAssignmentOperator,jExpr);
		AExpStmt aExpStmt = new AExpStmt(jWholeExpr, new TSemicolon());
		jABlockedStmt = new AStmtBlockedStmt(aExpStmt);
		
		return jABlockedStmt;
	}
	
	public static PBlockedStmt buildAPostFixExpStmt(PostfixExpression gPostFixExpr)
	{
		PBlockedStmt jABlockedStmt = null;
		PExp jExp = GExprBuilder.buildAExp(gPostFixExpr);
		
		if(jExp != null)
		{
			AExpStmt aExpStmt = new AExpStmt(jExp, new TSemicolon());
			jABlockedStmt = new AStmtBlockedStmt(aExpStmt);
		}
	
		return jABlockedStmt;
	}
	
	public static PBlockedStmt buildAPostFixExpStmt(PName jVarName, String opStr)
	{
		PBlockedStmt jABlockedStmt = null;
		PExp jExp = new ANameExp((PName)jVarName.clone());
		
		if(opStr.equals("++"))
		{
			PExp aPostIncrementExp = new APostIncrementExp(jExp, new TPlusPlus());
			AExpStmt aExpStmt = new AExpStmt(aPostIncrementExp, new TSemicolon());
			jABlockedStmt = new AStmtBlockedStmt(aExpStmt);
		}
		else if(opStr.equals("--"))
		{
			PExp aPostIncrementExp = new APostDecrementExp(jExp, new TMinusMinus());
			AExpStmt aExpStmt = new AExpStmt(aPostIncrementExp, new TSemicolon());
			jABlockedStmt = new AStmtBlockedStmt(aExpStmt);
		}
	
		return jABlockedStmt;
	}
	
	public static PBlockedStmt buildAExprBlockedStmt(ExpressionStatement gExprStmt)
	{
		PBlockedStmt jABlockedStmt = null;
		
		if(!GStmtBuilder.isExprStmtSkippable(gExprStmt))
		{
			Expression gExpr = gExprStmt.getExpression();
	
			if(gExpr != null)
			{
				/* The method GLiteralBuilder.getStrLiteralsFromExprs(leftExpr, rightExpr) has to be
				 * called for each BinaryExpression to update the LiteralVarMap.
				 * */
				if(gExpr instanceof DeclarationExpression)
				{
					jABlockedStmt = GStmtBuilder.buildALocalVariableDeclarationInBlockedStmt(
							((DeclarationExpression) gExpr).getLeftExpression(), ((DeclarationExpression) gExpr).getRightExpression());
				}
				else if(gExpr instanceof BinaryExpression)
				{
					jABlockedStmt = GStmtBuilder.buildAAssignmentExpStmt((BinaryExpression) gExpr);
				}
				else if(gExpr instanceof MethodCallExpression)
				{
					jABlockedStmt = GStmtBuilder.buildANameMethodInvocationExpStmt((MethodCallExpression) gExpr);
				}
				else if(gExpr instanceof PostfixExpression)
				{
					jABlockedStmt = GStmtBuilder.buildAPostFixExpStmt((PostfixExpression) gExpr);
				}
				else if(gExpr instanceof PrefixExpression)
				{
					/* We will handle later */
					System.out.println("[GStmtBuilder.buildABlockedStmt] we need to handle a PrefixExpression");
				}
			}
			else
			{
				System.out.println("[GStmtBuilder.buildABlockedStmt] fatal error!!! gExpr is null");
			}
		}
		
		return jABlockedStmt;
	}
	
	public static PBlockedStmt buildAIfBlockedStmt(IfStatement gIfStmt)
	{
		PBlockedStmt jABlockedStmt = null;
		PBlock jIfBlock = GStmtBuilder.buildABlock(gIfStmt.getIfBlock());
		
		if(jIfBlock != null)
		{
			PBlock jElseBlock = null;
			Expression gBooleanExp = gIfStmt.getBooleanExpression().getExpression();
			PExp jExp = GExprBuilder.buildAExp(gBooleanExp);
			
			if(gIfStmt.getElseBlock() instanceof BlockStatement)
			{
				jElseBlock = GStmtBuilder.buildABlock(gIfStmt.getElseBlock());
			}
			else if(gIfStmt.getElseBlock() instanceof IfStatement)
			{
				PBlockedStmt jElseBlockStmt = GStmtBuilder.buildAIfBlockedStmt((IfStatement)gIfStmt.getElseBlock());
				LinkedList jBlockedStmtList = new LinkedList();
				
				if(jElseBlockStmt != null)
				{
					jBlockedStmtList.add(jElseBlockStmt);
				}
				/* Create a block */
				jElseBlock = new ABlock(new TLBrace(), jBlockedStmtList, new TRBrace());
			}
			
			if(jExp != null)
			{
				PStmt jIfStmt = null;
				
				if(jElseBlock != null)
				{
					jIfStmt = new AIfStmt(new TIf(), new TLPar(), jExp, new TRPar(), jIfBlock,
							new TElse(), jElseBlock);
				}
				else
				{
					jIfStmt = new AIfStmt(new TIf(), new TLPar(), jExp, new TRPar(), jIfBlock,
							new TElse(), new ABlock(new TLBrace(), new LinkedList(), new TRBrace()));
				}
				jABlockedStmt = new AStmtBlockedStmt(jIfStmt);
			}
		}
	
		return jABlockedStmt;
	}
	
	public static PBlockedStmt buildAWhileBlockedStmt(WhileStatement gWhileStmt)
	{
		PBlockedStmt jABlockedStmt = null;
		PBlock jBlock = GStmtBuilder.buildABlock(gWhileStmt.getLoopBlock());
		
		if(jBlock != null)
		{
			PExp jExp = GExprBuilder.buildAExp(gWhileStmt.getBooleanExpression().getExpression());
			if(jExp != null)
			{
				PStmt jWhileStmt = new AWhileStmt(new TWhile(), new TLPar(), jExp, new TRPar(), jBlock);
				jABlockedStmt = new AStmtBlockedStmt(jWhileStmt);
			}
		}
	
		return jABlockedStmt;
	}
	
	public static PBlockedStmt buildABreakBlockedStmt()
	{
		PStmt jBreakStmt = new ABreakStmt(new TBreak(), null, new TSemicolon());
		PBlockedStmt jABlockedStmt = new AStmtBlockedStmt(jBreakStmt);
	
		return jABlockedStmt;
	}
	
	/* Groovy code:
	 * for (it in switches) {
	 * 		if (it.currentSwitch == "on") {
	 * 			result = false
	 * 			break
	 * 		}
	 * }
	 * => Java code:
	 * STSwitch[] tempArray1 = switches;
	 * int index1 = 0;
	 * while(index1 < tempArray1.length)
	 * {
	 * 		STSwitch it = tempArray1[index1];
	 * 		int[] tempArray2 = {2};
	 * 		if(it.currentSwitch == tempArray2) {
	 * 			result = false;
	 * 			break;
	 * 		}
	 * 		index1++;
	 * */
	public static PBlockedStmt buildAWhileBlockedStmt(ForStatement gForStmt)
	{
		PBlockedStmt jABlockedStmt = null;
		Expression collExpr = gForStmt.getCollectionExpression();
		
		if(collExpr instanceof ClosureListExpression)
		{
			/* for (int i = 0; i < STORMY.size() && !result; i++) {
			 * 		result = text.contains(STORMY[i])
			 * }
			 * */
			List<Expression> collExprList =  ((ClosureListExpression)collExpr).getExpressions();
			
			if(collExprList.size() == 3)
			{
				/* int i = 0; */
				PBlockedStmt jDeclStmt = GStmtBuilder.buildAExprBlockedStmt(
						new ExpressionStatement(collExprList.get(0)));
				if(jDeclStmt != null)
				{
					/* i < STORMY.size() && !result */
					PExp jBooleanExp = GExprBuilder.buildAExp(collExprList.get(1));
					
					if(jBooleanExp != null)
					{
						/* i++ */
						PBlockedStmt jIncrlStmt = GStmtBuilder.buildAExprBlockedStmt(
								new ExpressionStatement(collExprList.get(2)));
						
						if(jIncrlStmt != null)
						{
							/* Update additional created statement list */
							additionalStmtList.add(jDeclStmt);
							
							/* Build a list of statements loop block of the for loop
							 * */
							LinkedList jForLoopStmtList = GStmtBuilder.buildABlockStmtList(gForStmt.getLoopBlock(), false);
							LinkedList jWhileLoopStmtList = new LinkedList();
							
							jWhileLoopStmtList.addAll(jForLoopStmtList);
							jWhileLoopStmtList.add(jIncrlStmt);
							
							PBlock jWhileBlock = new ABlock(new TLBrace(), jWhileLoopStmtList, new TRBrace());
							PStmt jWhileStmt = new AWhileStmt(new TWhile(), new TLPar(), jBooleanExp, new TRPar(), jWhileBlock);
							jABlockedStmt = new AStmtBlockedStmt(jWhileStmt);
						}
					}
				}
			}
		}
		else
		{
			PName jArrayVarName = null;
			
			/* STSwitch[] tempArray1 = switches;
			 * */
			jArrayVarName = GStmtBuilder.buildALocalVariableDeclarationInBlockedStmt(gForStmt.getCollectionExpression());
			if(jArrayVarName != null)
			{
				/* int index1 = 0;
				 * */
				String loopIndexName = GStmtBuilder.buildALocalVariableDeclarationInBlockedStmt();
				TId tId1 = new TId(loopIndexName);
				PName jLoopIndexVarName = new ASimpleName(tId1);
				PExp jBooleanExp = null;
				
				/* Create boolean expression for while loop:
				 * index1 < tempArray1.length 
				 * */
				{
					PExp jFirstExpr = new ANameExp((PName)jLoopIndexVarName.clone());
					PExp jSecondExpr = GExprBuilder.buildANameExp((PName)jArrayVarName.clone(), "length");
					jBooleanExp = GExprBuilder.buildABinaryExpr(jFirstExpr, "<", jSecondExpr);
				}
				
				if(jBooleanExp != null)
				{
					/* Create an iterator declaration statement
					 * STSwitch it = tempArray1[index1];
					 * */
					PBlockedStmt jItLocalVarDeclarationStmt = GStmtBuilder.buildALocalVariableDeclarationInBlockedStmt(
							gForStmt.getVariable().getName(), gForStmt.getVariable().getType(), (PName)jArrayVarName.clone(), 
							(PName)jLoopIndexVarName.clone());
					
					if(jItLocalVarDeclarationStmt != null)
					{
						/* Build a list of statements loop block of the for loop
						 * */
						LinkedList jForLoopStmtList = GStmtBuilder.buildABlockStmtList(gForStmt.getLoopBlock(), false);
						LinkedList jWhileLoopStmtList = new LinkedList();
						
						jWhileLoopStmtList.add(jItLocalVarDeclarationStmt);
						jWhileLoopStmtList.addAll(jForLoopStmtList);
						
						/* Create a loop increment index statement */
						{
							PBlockedStmt jWhileLoopIndexIncStmt = GStmtBuilder.buildAPostFixExpStmt((PName)jLoopIndexVarName.clone(), "++");
							
							if(jWhileLoopIndexIncStmt != null)
							{
								jWhileLoopStmtList.add(jWhileLoopIndexIncStmt);
							}
						}
						
						/* Create a while statement */
						if(jBooleanExp != null)
						{
							PBlock jWhileBlock = new ABlock(new TLBrace(), jWhileLoopStmtList, new TRBrace());
							PStmt jWhileStmt = new AWhileStmt(new TWhile(), new TLPar(), jBooleanExp, new TRPar(), jWhileBlock);
							jABlockedStmt = new AStmtBlockedStmt(jWhileStmt);
						}
					}
				}
			}
		}
	
		return jABlockedStmt;
	}
	
	public static PBlock buildABlock(Statement gStmt)
	{
		PBlock jBlock = null;
		
		if((gStmt instanceof BlockStatement) || (gStmt instanceof EmptyStatement))
		{
			LinkedList jBlockedStmtList = new LinkedList();
			
			if(gStmt instanceof BlockStatement)
			{
				java.util.List<Statement> gStmtList = ((BlockStatement)gStmt).getStatements();
				
				/* Build a list of PBlockedStmt.
				 * Groovy does not accept a block declaration without conditional 
				 * statement (if, while, for, ...), e.g.,
				 * {
				 * ...
				 * } 
				 **/
				for(Statement gSubStmt : gStmtList)
				{
					PBlockedStmt jBlockedStmt = null;
					int firstAdditionalStmtListSize, secondAdditionalStmtListSize;
					
					/* Save current size of additionalStmtList */
					firstAdditionalStmtListSize = additionalStmtList.size();
					
					if(gSubStmt instanceof ExpressionStatement)
					{
						jBlockedStmt = GStmtBuilder.buildAExprBlockedStmt((ExpressionStatement) gSubStmt);
					}
					else if(gSubStmt instanceof IfStatement)
					{
						jBlockedStmt = GStmtBuilder.buildAIfBlockedStmt((IfStatement) gSubStmt);
					}
					else if(gSubStmt instanceof WhileStatement)
					{
						jBlockedStmt = GStmtBuilder.buildAWhileBlockedStmt((WhileStatement) gSubStmt);
					}
					else if(gSubStmt instanceof ForStatement)
					{
						jBlockedStmt = GStmtBuilder.buildAWhileBlockedStmt((ForStatement) gSubStmt);
					}
					else if(gSubStmt instanceof ReturnStatement)
					{
						Expression gExpr = ((ReturnStatement)gSubStmt).getExpression();
						
						if(GStmtBuilder.isReturnStmtNeeded)
						{
							PExp jExpr = GExprBuilder.buildAExp(gExpr);
							
							if(jExpr != null)
							{
								AReturnStmt jReturnStmt = new AReturnStmt(new TReturn(), jExpr, new TSemicolon());
								jBlockedStmt = new AStmtBlockedStmt(jReturnStmt);
							}
						}
						else
						{
							jBlockedStmt = GStmtBuilder.buildAExprBlockedStmt(new ExpressionStatement(gExpr));
						}
					}
					else if(gSubStmt instanceof BreakStatement)
					{
						jBlockedStmt = GStmtBuilder.buildABreakBlockedStmt();
					}
					else
					{
						System.out.println("[GStmtBuilder.buildABlock] unexpected statement type: " + gSubStmt);
					}
					
					if(jBlockedStmt != null)
					{
						/* Get current size of additionalStmtList */
						secondAdditionalStmtListSize = additionalStmtList.size();
						if(firstAdditionalStmtListSize < secondAdditionalStmtListSize)
						{
							/* We need to add the extra statements to created while block's 
							 * statement list, and remove those statements from additionalStmtList 
							 * */
							for(int i = firstAdditionalStmtListSize; i < secondAdditionalStmtListSize; i++)
							{
								jBlockedStmtList.add(additionalStmtList.remove(firstAdditionalStmtListSize));
							}
						}
						
						/* Add the translated statement into the list */
						jBlockedStmtList.add(jBlockedStmt);
					}
				}
			}
			/* Create a block */
			jBlock = new ABlock(new TLBrace(), jBlockedStmtList, new TRBrace());
		}
		else if(gStmt instanceof ExpressionStatement)
		{
			PBlockedStmt jBlockedStmt = null;
			int firstAdditionalStmtListSize, secondAdditionalStmtListSize;
			LinkedList jBlockedStmtList = new LinkedList();
			
			/* Save current size of additionalStmtList */
			firstAdditionalStmtListSize = additionalStmtList.size();
			jBlockedStmt = GStmtBuilder.buildAExprBlockedStmt((ExpressionStatement) gStmt);
			
			if(jBlockedStmt != null)
			{
				/* Get current size of additionalStmtList */
				secondAdditionalStmtListSize = additionalStmtList.size();
				if(firstAdditionalStmtListSize < secondAdditionalStmtListSize)
				{
					/* We need to add the extra statements to created while block's 
					 * statement list, and remove those statements from additionalStmtList 
					 * */
					for(int i = firstAdditionalStmtListSize; i < secondAdditionalStmtListSize; i++)
					{
						jBlockedStmtList.add(additionalStmtList.remove(firstAdditionalStmtListSize));
					}
				}
				
				/* Add the translated statement into the list */
				jBlockedStmtList.add(jBlockedStmt);
			}
			/* Create a block */
			jBlock = new ABlock(new TLBrace(), jBlockedStmtList, new TRBrace());
		}
		else
		{
			System.out.println("[GStmtBuilder.buildABlock] a wrong call!!!");
		}
		
		return jBlock;
	}
	
	public static LinkedList buildABlockStmtList(Statement gStmt, boolean skipLastSubStmt)
	{
		LinkedList jBlockedStmtList = new LinkedList();
		
		if((gStmt instanceof BlockStatement) || (gStmt instanceof EmptyStatement))
		{
			java.util.List<Statement> gStmtList = ((BlockStatement)gStmt).getStatements();
			int numOfProcessedStmts = gStmtList.size();
			
			if(numOfProcessedStmts > 0)
			{
				if(skipLastSubStmt)
				{
					numOfProcessedStmts--;
				}
				
				/* Build a list of PBlockedStmt.
				 * Groovy does not accept a block declaration without conditional 
				 * statement (if, while, for, ...), e.g.,
				 * {
				 * ...
				 * } 
				 **/
				for(int i = 0; i < numOfProcessedStmts; i++)
				{
					Statement gSubStmt = gStmtList.get(i);
					PBlockedStmt jBlockedStmt = null;
					int firstAdditionalStmtListSize, secondAdditionalStmtListSize;
					
					/* Save current size of additionalStmtList */
					firstAdditionalStmtListSize = additionalStmtList.size();
					
					if(gSubStmt instanceof ExpressionStatement)
					{
						jBlockedStmt = GStmtBuilder.buildAExprBlockedStmt((ExpressionStatement) gSubStmt);
					}
					else if(gSubStmt instanceof IfStatement)
					{
						jBlockedStmt = GStmtBuilder.buildAIfBlockedStmt((IfStatement) gSubStmt);
					}
					else if(gSubStmt instanceof WhileStatement)
					{
						jBlockedStmt = GStmtBuilder.buildAWhileBlockedStmt((WhileStatement) gSubStmt);
					}
					else if(gSubStmt instanceof ForStatement)
					{
						jBlockedStmt = GStmtBuilder.buildAWhileBlockedStmt((ForStatement) gSubStmt);
					}
					else if(gSubStmt instanceof ReturnStatement)
					{
						Expression gExpr = ((ReturnStatement)gSubStmt).getExpression();
						
						if(GStmtBuilder.isReturnStmtNeeded)
						{
							PExp jExpr = GExprBuilder.buildAExp(gExpr);
							
							if(jExpr != null)
							{
								AReturnStmt jReturnStmt = new AReturnStmt(new TReturn(), jExpr, new TSemicolon());
								jBlockedStmt = new AStmtBlockedStmt(jReturnStmt);
							}
						}
						else
						{
							jBlockedStmt = GStmtBuilder.buildAExprBlockedStmt(new ExpressionStatement(gExpr));
						}
					}
					else if(gSubStmt instanceof BreakStatement)
					{
						jBlockedStmt = GStmtBuilder.buildABreakBlockedStmt();
					}
					else
					{
						System.out.println("[GStmtBuilder.buildABlockStmtList] unexpected statement type: " + gSubStmt);
					}
					
					if(jBlockedStmt != null)
					{
						/* Get current size of additionalStmtList */
						secondAdditionalStmtListSize = additionalStmtList.size();
						if(firstAdditionalStmtListSize < secondAdditionalStmtListSize)
						{
							/* We need to add the extra statements to created while block's 
							 * statement list, and remove those statements from additionalStmtList 
							 * */
							for(i = firstAdditionalStmtListSize; i < secondAdditionalStmtListSize; i++)
							{
								jBlockedStmtList.add(additionalStmtList.remove(firstAdditionalStmtListSize));
							}
						}
						
						/* Add the translated statement into the list */
						jBlockedStmtList.add(jBlockedStmt);
					}
				}
			}
		}
		else
		{
			System.out.println("[GStmtBuilder.buildABlockStmtList] a wrong call!!!");
		}
		
		return jBlockedStmtList;
	}
	
	/* Skippable statements:
	 * 1) subscribe(location, modeChangeHandler)
	 * 2) log.debug
	 * 3) 
	 * */
	public static boolean isExprStmtSkippable(ExpressionStatement gExprStmt)
	{
		Expression gExpr = gExprStmt.getExpression();
		
		if(gExpr instanceof MethodCallExpression)
		{
			Expression gObjectExpr = ((MethodCallExpression)gExpr).getObjectExpression();
			Expression methExpr = ((MethodCallExpression)gExpr).getMethod();
			
			if(gObjectExpr instanceof VariableExpression)
			{
				String varName = ((VariableExpression) gObjectExpr).getName();
				
				if(varName.equals("log"))
				{
					return true;
				}
			}
			
			if(methExpr instanceof ConstantExpression)
			{
				String methName = methExpr.getText();
				
				if(methName.equals("subscribe") || methName.equals("default_null_method"))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static void updateVarTypeList(VariableExpression var, ClassNode gType, PType jType)
	{
		String varName = var.getName();
		
		if(!GStmtBuilder.varTypeList.containsKey(varName))
		{
			GVarTypeContainer aTypeContainer = new GVarTypeContainer(gType, jType);
			GStmtBuilder.varTypeList.put(varName, aTypeContainer);
		}
	}
	public static void updateVarTypeList(String varName, ClassNode gType, PType jType)
	{
		if(!GStmtBuilder.varTypeList.containsKey(varName))
		{
			GVarTypeContainer aTypeContainer = new GVarTypeContainer(gType, jType);
			GStmtBuilder.varTypeList.put(varName, aTypeContainer);
		}
	}
	
	public static ClassNode getVarGType(VariableExpression var)
	{
		String varName = var.getName();
		
		if(GStmtBuilder.varTypeList.containsKey(varName))
		{
			return GStmtBuilder.varTypeList.get(varName).gType;
		}
		return null;
	}
	
	public static ClassNode getVarGType(String varName)
	{
		if(GStmtBuilder.varTypeList.containsKey(varName))
		{
			return GStmtBuilder.varTypeList.get(varName).gType;
		}
		return null;
	}
}
