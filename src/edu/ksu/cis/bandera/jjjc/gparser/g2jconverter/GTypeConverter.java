package edu.ksu.cis.bandera.jjjc.gparser.g2jconverter;

import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.GenericsType;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MapExpression;

import edu.ksu.cis.bandera.jjjc.gparser.configinfomanager.GConfigInfoManager;
import edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor.GStateMapEnum;
import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;
import edu.ksu.cis.bandera.jjjc.node.*;

/* This class is used to convert from a Groovy Type into 
 * its corresponding Java Type
 * */
public class GTypeConverter {
	public static Node convert(ClassNode gType)
	{
		if((gType == ClassHelper.boolean_TYPE) || (gType == ClassHelper.Boolean_TYPE)
				|| gType.getName().equals("java.lang.Boolean"))
		{
			TBoolean tBoolean = new TBoolean();
			ABooleanPrimitiveType aBooleanPrimitiveType = new ABooleanPrimitiveType(tBoolean);
			APrimitiveType node = new APrimitiveType((PPrimitiveType)aBooleanPrimitiveType);
			return node;
		}
		else if(GUtil.toIntConvertible(gType))
		{
			TInt tInt = new TInt();
			AIntPrimitiveType aIntPrimitiveType = new AIntPrimitiveType(tInt);
			APrimitiveType node = new APrimitiveType((PPrimitiveType)aIntPrimitiveType);
			return node;
		}
		else if(GUtil.toArrayConvertible(gType))
		{
			/* List is not supported by Bandera. Thus, we have to convert List to Array */
			GenericsType[] genericsTypes = gType.getGenericsTypes();
			ClassNode genericType;
			
			if(genericsTypes != null)
			{
				genericType = genericsTypes[0].getType();
			}
			else
			{
				/* Set default type to integer */
				genericType = new ClassNode(int.class);
				System.out.println("[GTypeConverter.convert] Set default genric type to int !!!");
			}
			
			{
				ADim aDim = new ADim(new TLBracket(), new TRBracket()); /* [] */
				X2PDim x2PDim = new X2PDim(aDim);
				PArrayType arrayType;
				AArrayReferenceType aArrayReferenceType;
				
				if(GUtil.isPrimitiveType(genericType))
				{
					/* Convert a list of primitive type */
					APrimitiveType aPrimitiveType = (APrimitiveType)GTypeConverter.convert(genericType);
					arrayType = new APrimitiveArrayType(aPrimitiveType.getPrimitiveType(), x2PDim);
				}
				else
				{
					/* Convert a list of name type */
					TId tId = new TId(genericType.getName());
					ASimpleName aSimpleName = new ASimpleName(tId);
					arrayType = new ANameArrayType(aSimpleName, x2PDim);
				}
				aArrayReferenceType = new AArrayReferenceType(arrayType);
				return (new AReferenceType(aArrayReferenceType));
			}
		}
		else if(GUtil.isArrayType(gType))
		{
			ClassNode arrayDataType = GUtil.getArrayComponentType(gType);
			ADim aDim = new ADim(new TLBracket(), new TRBracket()); /* [] */
			X2PDim x2PDim = new X2PDim(aDim);
			PArrayType arrayType;
			AArrayReferenceType aArrayReferenceType;
			
			if(GUtil.isPrimitiveType(arrayDataType))
			{
				/* Convert an array of primitive type */
				APrimitiveType aPrimitiveType = (APrimitiveType)GTypeConverter.convert(arrayDataType);
				arrayType = new APrimitiveArrayType(aPrimitiveType.getPrimitiveType(), x2PDim);
			}
			else
			{
				/* Convert an array of name type */
				TId tId = new TId(arrayDataType.getName());
				ASimpleName aSimpleName = new ASimpleName(tId);
				arrayType = new ANameArrayType(aSimpleName, x2PDim);
			}
			aArrayReferenceType = new AArrayReferenceType(arrayType);
			
			if(GUtil.getArrayDim(gType) > 1)
			{
				System.out.println("[GTypeConverter.convert] Bandera does not support this array's dimension: " + GUtil.getArrayDim(gType));
			}
			
			return (new AReferenceType(aArrayReferenceType));
		}
		else
		{
			/* The default type is ANameReferenceType */
			TId tId = new TId(gType.getName());
			ASimpleName aSimpleName = new ASimpleName(tId);
			ANameReferenceType aNameRefType = new ANameReferenceType(aSimpleName);
			
//			System.out.println("[GTypeConverter.convert] Set default type for " + gType);

			return (new AReferenceType(aNameRefType));
		}
	}
	
	public static Node createAnIntType()
	{
		TInt tInt = new TInt();
		AIntPrimitiveType aIntPrimitiveType = new AIntPrimitiveType(tInt);
		APrimitiveType node = new APrimitiveType((PPrimitiveType)aIntPrimitiveType);
		
		return node;
	}
	
	public static Node createAMapType(Expression gLeftExpr, Expression gRightExpr)
	{
		AReferenceType result = null;
		
		if(GUtil.isExprAMapType(gLeftExpr))
		{
			String mapClassName = null;
			
			if(GUtil.isAStateMapGetExpr(gRightExpr))
			{
				GStateMapEnum stateMapType = GConfigInfoManager.getStateMapUsed(GUtil.currentClassName);
				
				if(stateMapType == GStateMapEnum.Int2IIIMMap)
				{
					mapClassName = "CInt2IIMMap"; /* level 2 */
				}
				else if(stateMapType == GStateMapEnum.Int2IIMMap)
				{
					mapClassName = "CInt2IntMap"; /* level 1 */
				}
			}
			else if((gRightExpr instanceof MapExpression) || GUtil.isExprAMapType(gLeftExpr))
			{
				mapClassName = "CInt2IntMap"; /* level 1 */
			}
			
			if(mapClassName != null)
			{
				TId tId = new TId(mapClassName);
				ASimpleName aSimpleName = new ASimpleName(tId);
				ANameReferenceType aNameRefType = new ANameReferenceType(aSimpleName);
				result = new AReferenceType(aNameRefType);
			}
		}
		
		return result;
	}
	
	public static Node createAStateMapType()
	{
		String mapClassName;
		GStateMapEnum stateMapType = GConfigInfoManager.getStateMapUsed(GUtil.currentClassName);
		
		if(stateMapType == GStateMapEnum.Int2IIIMMap)
		{
			mapClassName = "CInt2IIIMMap"; /* level 3 */
		}
		else if(stateMapType == GStateMapEnum.Int2IIMMap)
		{
			mapClassName = "CInt2IIMMap"; /* level 2 */
		}
		else
		{
			mapClassName = "CInt2IntMap"; /* level 1 */
		}
		TId tId = new TId(mapClassName);
		ASimpleName aSimpleName = new ASimpleName(tId);
		ANameReferenceType aNameRefType = new ANameReferenceType(aSimpleName);
		
		return (new AReferenceType(aNameRefType));
	}
	
	public static String getMapTypeName(Expression gLeftExpr, Expression gRightExpr)
	{
		String mapClassName = null;
		
		if(GUtil.isExprAMapType(gLeftExpr))
		{
			if(GUtil.isAStateMapGetExpr(gRightExpr))
			{
				GStateMapEnum stateMapType = GConfigInfoManager.getStateMapUsed(GUtil.currentClassName);
				
				if(stateMapType == GStateMapEnum.Int2IIIMMap)
				{
					mapClassName = "CInt2IIMMap"; /* level 2 */
				}
				else if(stateMapType == GStateMapEnum.Int2IIMMap)
				{
					mapClassName = "CInt2IntMap"; /* level 1 */
				}
			}
			else if((gRightExpr instanceof MapExpression) || GUtil.isExprAMapType(gLeftExpr))
			{
				mapClassName = "CInt2IntMap"; /* level 1 */
			}
		}
		
		return mapClassName;
	}
}
