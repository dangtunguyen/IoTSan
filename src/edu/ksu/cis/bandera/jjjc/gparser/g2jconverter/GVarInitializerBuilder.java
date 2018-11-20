package edu.ksu.cis.bandera.jjjc.gparser.g2jconverter;

import org.codehaus.groovy.ast.expr.ArrayExpression;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.CastExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.ConstructorCallExpression;
import org.codehaus.groovy.ast.expr.ElvisOperatorExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.GStringExpression;
import org.codehaus.groovy.ast.expr.ListExpression;
import org.codehaus.groovy.ast.expr.MapExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;

import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;
import edu.ksu.cis.bandera.jjjc.node.*;

import ca.mcgill.sable.util.*;

public class GVarInitializerBuilder {
	public static PVariableInitializer build(Expression gLeftExpr, Expression gRightExpr)
	{
		PVariableInitializer jVarInitializer = null;
		
		if(gRightExpr instanceof MapExpression)
		{
			jVarInitializer = GVarInitializerBuilder.buildASimpleClassVariableInitializer(GTypeConverter.getMapTypeName(gLeftExpr, gRightExpr));
		}
		else if(gRightExpr instanceof ArrayExpression)
		{
			jVarInitializer = GVarInitializerBuilder.buildAArrayVariableInitializer((ArrayExpression) gRightExpr);
		}
		else if(gRightExpr instanceof ListExpression)
		{
			jVarInitializer = GVarInitializerBuilder.buildAArrayVariableInitializer((ListExpression) gRightExpr);
		}
		else if(GUtil.isExprAStr(gRightExpr) && (GUtil.isExprAStr(gLeftExpr)))
		{
			jVarInitializer = GVarInitializerBuilder.buildAExpVariableInitializerForStr(gRightExpr);
		}
		else if(GUtil.isExprALiteral(gRightExpr) ||
				(gRightExpr instanceof VariableExpression) || 
				(gRightExpr instanceof PropertyExpression) ||
				(gRightExpr instanceof MethodCallExpression) ||
				GUtil.isExprAnArrayAccess(gRightExpr) ||
				(gRightExpr instanceof BinaryExpression) ||
				(gRightExpr instanceof ConstantExpression))
		{
			jVarInitializer = GVarInitializerBuilder.buildAExpVariableInitializer(gRightExpr);
		}
		else if(gRightExpr instanceof ElvisOperatorExpression)
		{
			jVarInitializer = GVarInitializerBuilder.buildAExpVariableInitializer(((ElvisOperatorExpression)gRightExpr).getTrueExpression());
		}
		else if(gRightExpr instanceof ConstructorCallExpression)
		{
			if(GUtil.isATimeDataType(gRightExpr.getType()))
			{
				java.util.List<Expression> gExprList = GUtil.buildExprList(((ConstructorCallExpression)gRightExpr).getArguments());
				
				if(gExprList.size() > 0)
				{
					jVarInitializer = GVarInitializerBuilder.buildAExpVariableInitializer(gExprList.get(0));
				}
				else
				{
					MethodCallExpression mce = new MethodCallExpression(
							new VariableExpression("this"),
							"now",
							MethodCallExpression.NO_ARGUMENTS
							);
					jVarInitializer = GVarInitializerBuilder.buildAExpVariableInitializer(mce);
				}
			}
			else
			{
				System.out.println("[GVarDeclaratorBuilder.buildAAssignedVariableDeclarator] unexpected constructor types!!! " + gRightExpr);
			}
		}
		else if(gRightExpr instanceof CastExpression)
		{
			jVarInitializer = build(gLeftExpr, ((CastExpression)gRightExpr).getExpression());
		}
		
		return jVarInitializer;
	}
	
	public static PVariableInitializer buildAExpVariableInitializer(Expression gExpr)
	{
		PVariableInitializer jVarInitialier = null;
		PExp jExpr = GExprBuilder.buildAExp(gExpr);
		
		if(jExpr != null)
		{
			jVarInitialier = new AExpVariableInitializer(jExpr);
		}
		
		return jVarInitialier;
	}
	
	public static PVariableInitializer buildAArrayAccessExpVariableInitializer(PName jArrayVarName, PName jLoopIndexVarName)
	{
		PVariableInitializer jVarInitialier = null;
		PExp jExpr = GExprBuilder.buildAArrayAccessExp(jArrayVarName, jLoopIndexVarName);
		
		if(jExpr != null)
		{
			jVarInitialier = new AExpVariableInitializer(jExpr);
		}
		
		return jVarInitialier;
	}
	
	public static PArrayInitializer buildAArrayInitializer(ArrayExpression gArrExpr)
	{
		PArrayInitializer jArrInitializer = null;
		LinkedList jVarInitializerList = new LinkedList();
		
		for(Expression gExpr : gArrExpr.getExpressions())
		{
			if(GUtil.isExprALiteral(gExpr) ||
					(gExpr instanceof VariableExpression) || (gExpr instanceof PropertyExpression))
			{
				PVariableInitializer jVarInitialier = GVarInitializerBuilder.buildAExpVariableInitializer(gExpr);
				
				if(jVarInitialier != null)
				{
					jVarInitializerList.add(jVarInitialier);
				}
			}
		}
		jArrInitializer = new AArrayInitializer(new TLBrace(), jVarInitializerList, new TComma(), new TRBrace());
		
		return jArrInitializer;
	}
	
	public static PVariableInitializer buildAArrayVariableInitializer(ArrayExpression gArrExpr)
	{
		PVariableInitializer jVarInitialier = null;
		PArrayInitializer jArrInitializer = GVarInitializerBuilder.buildAArrayInitializer(gArrExpr);
		
		if(jArrInitializer != null)
		{
			jVarInitialier = new AArrayVariableInitializer(jArrInitializer);
		}
		
		return jVarInitialier;
	}
	
	public static PArrayInitializer buildAArrayInitializer(ListExpression gListExpr)
	{
		PArrayInitializer jArrInitializer = null;
		LinkedList jVarInitializerList = new LinkedList();
		
		for(Expression gExpr : gListExpr.getExpressions())
		{
			PVariableInitializer jVarInitialier = GVarInitializerBuilder.buildAExpVariableInitializer(gExpr);
			
			if(jVarInitialier != null)
			{
				jVarInitializerList.add(jVarInitialier);
			}
		}
		jArrInitializer = new AArrayInitializer(new TLBrace(), jVarInitializerList, new TComma(), new TRBrace());
		
		return jArrInitializer;
	}
	
	public static PVariableInitializer buildAArrayVariableInitializer(ListExpression gListExpr)
	{
		PVariableInitializer jVarInitialier = null;
		PArrayInitializer jArrInitializer = GVarInitializerBuilder.buildAArrayInitializer(gListExpr);
		
		if(jArrInitializer != null)
		{
			jVarInitialier = new AArrayVariableInitializer(jArrInitializer);
		}
		
		return jVarInitialier;
	}
	
	/* This method is used to build an AArrayVariableInitializer from a String/GString expression,
	 * which is converted into an array of Integer
	 * */
	public static PVariableInitializer buildAExpVariableInitializerForStr(Expression gExpr)
	{
		PVariableInitializer jVarInitialier = null;
		
		if(GUtil.isExprAStr(gExpr))
		{
			if((gExpr instanceof ConstantExpression) || (gExpr instanceof GStringExpression))
			{
				/* E.g.: def forecast = "rain"
				 * We need to get the int value of the string
				 * */
				GLiteralContainer literalContainer = GLiteralBuilder.getStrLiteralsFromExprs(null, gExpr);
				
				if(!literalContainer.isEmpty())
				{
					/* Create a list of AExpVariableInitializer */
					if(literalContainer.digitList.size() > 0)
					{
						PLiteral jLiteral;
						PExp jExpr;
						
						/* Create a ALiteral */
						TDecimalIntegerLiteral node1 = new TDecimalIntegerLiteral("0");
						ADecimalIntegerLiteral node2 = new ADecimalIntegerLiteral(node1);
						jLiteral = new AIntegerLiteralLiteral(node2);
						
						/* Create an ALiteralExp */
						jExpr = new ALiteralExp(jLiteral);
						
						/* Create an AExpVariableInitializer */
						jVarInitialier = new AExpVariableInitializer(jExpr);
					}
					else
					{
						Integer firstNonZeroInt = 0;
						PLiteral jLiteral;
						PExp jExpr;
						
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
						
						/* Create an AExpVariableInitializer */
						jVarInitialier = new AExpVariableInitializer(jExpr);
					}
				}
			}
			else
			{
				PExp jExpr = GExprBuilder.buildAExp(gExpr);
				
				if(jExpr != null)
				{
					jVarInitialier = new AExpVariableInitializer(jExpr);
				}
			}
		}
		
		return jVarInitialier;
	}
	
	public static PVariableInitializer buildASimpleClassVariableInitializer(String className)
	{
		TId tId = new TId(className);
		ASimpleName aSimpleName = new ASimpleName(tId);
		ASimpleClassInstanceCreationExp jExpr = new ASimpleClassInstanceCreationExp(
				new TNew(),
				aSimpleName,
				new TLPar(),
				new LinkedList(),
				new TRPar(),
				null);
		
		return (new AExpVariableInitializer(jExpr));
	}
}
