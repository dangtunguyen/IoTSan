package edu.ksu.cis.bandera.jjjc.gparser.g2jconverter;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.expr.ArrayExpression;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.ConstructorCallExpression;
import org.codehaus.groovy.ast.expr.ElvisOperatorExpression;
import org.codehaus.groovy.ast.expr.EmptyExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.ListExpression;
import org.codehaus.groovy.ast.expr.MapExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;

import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;
import edu.ksu.cis.bandera.jjjc.node.*;

import ca.mcgill.sable.util.*;

public class GVarDeclaratorBuilder {
	public static PVariableDeclaratorId buildAVariableDeclaratorId(String varName)
	{
		XPDim node2 = null;
		TId node1 = new TId(varName);
		AVariableDeclaratorId jVarId = new AVariableDeclaratorId(node1, node2);
		
		return jVarId;
	}
	
	public static PVariableDeclarator buildAVariableDeclarator(String varName)
	{
		PVariableDeclaratorId jVarId = GVarDeclaratorBuilder.buildAVariableDeclaratorId(varName);
		
		return (new AIdVariableDeclarator(jVarId));
	}
	
	public static PVariableDeclarator buildAVariableDeclarator(Expression gLeftExpr, Expression gRightExpr)
	{
		PVariableDeclarator jAVariableDeclarator = null;
		
		if(gLeftExpr instanceof VariableExpression)
		{
			if(!(gRightExpr instanceof EmptyExpression))
			{
				PVariableInitializer jVarInitializer = GVarInitializerBuilder.build(gLeftExpr, gRightExpr);
				
				if(jVarInitializer != null)
				{
					PVariableDeclaratorId jVarId = GVarDeclaratorBuilder.buildAVariableDeclaratorId(((VariableExpression) gLeftExpr).getName());
					
					jAVariableDeclarator = new AAssignedVariableDeclarator(jVarId,new TAssign(),jVarInitializer);
				}
				else
				{
					System.out.println("[GVarDeclaratorBuilder.buildAAssignedVariableDeclarator] unexpected expression types!!! " + gRightExpr);
				}
			}
			else
			{
				PVariableDeclaratorId jVarId = GVarDeclaratorBuilder.buildAVariableDeclaratorId(((VariableExpression) gLeftExpr).getName());
				jAVariableDeclarator = new AIdVariableDeclarator(jVarId);
			}
		}
		else
		{
			System.out.println("[GVarDeclaratorBuilder.buildAAssignedVariableDeclarator] error!!!! " + gLeftExpr);
		}
		
		return jAVariableDeclarator;
	}
	
	public static PLocalVariableDeclaration buildALocalVariableDeclaration(Expression gLeftExpr, Expression gRightExpr)
	{
		ALocalVariableDeclaration jLocalVarDelaration = null;
		
		if(gLeftExpr instanceof VariableExpression)
		{
			ClassNode gType = GUtil.getExprType(gLeftExpr);
			
			if(gType != null)
			{
				LinkedList jVarDeclaratorList = new LinkedList();
				PVariableDeclarator jVarDeclarator = GVarDeclaratorBuilder.buildAVariableDeclarator(gLeftExpr, gRightExpr);
				PType jType;
				
				jType = (PType)GTypeConverter.createAMapType(gLeftExpr, gRightExpr);
				if(jType == null)
				{
					jType = (PType)GTypeConverter.convert(gType);
				}
				
				if(jVarDeclarator != null)
				{
					jVarDeclaratorList.add(jVarDeclarator);
				}
				
				if(jVarDeclaratorList.size() > 0)
				{
					jLocalVarDelaration = new ALocalVariableDeclaration(new LinkedList(), jType, jVarDeclaratorList);
					
					/* Update variable type list */
					GStmtBuilder.updateVarTypeList((VariableExpression) gLeftExpr, gType, jType);
				}
			}
			else
			{
				System.out.println("[GVarDeclaratorBuilder.buildALocalVariableDeclaration] unexpected data type " + gLeftExpr);
			}
		}
		
		return jLocalVarDelaration;
	}
}
