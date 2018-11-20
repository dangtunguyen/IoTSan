package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.GenericsType;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.ForStatement;
import org.codehaus.groovy.control.SourceUnit;

import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;

/* This class is used to infer the type of each parameter of a method
 * based on the input BinaryExpression argument. This class is used
 * by class GArgTypeInferenceFromMethCallVarExpr
 * */
public class GTypeInferenceFromVarExpr extends ClassCodeVisitorSupport{
	/********************************************/
	private Map<String, ArrayList<GParameter>> localMethods;
	private Map<String, List<String>> varNames;
	/********************************************/

	public GTypeInferenceFromVarExpr(Map<String, ArrayList<GParameter>> localMethods, Map<String, List<String>> varNames)
	{
		this.localMethods = localMethods;
		this.varNames = varNames;
	}

	/* Getters */
	public Map<String, ArrayList<GParameter>> getLocalMethods()
	{
		return this.localMethods;
	}

	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}

	@Override
	public void visitBinaryExpression(BinaryExpression bex)
	{
		Expression leftExpr;

		leftExpr = bex.getLeftExpression();
		if(leftExpr instanceof VariableExpression)
		{
			String varName = ((VariableExpression) leftExpr).getName();

			for(Map.Entry<String, List<String>> entry : this.varNames.entrySet())
			{
				if(entry.getValue().contains(varName))
				{
					int parmIndex = entry.getValue().indexOf(varName);

					if(this.localMethods.get(entry.getKey()).get(parmIndex).type == ClassHelper.OBJECT_TYPE)
					{
						Expression rightExpr;
						ClassNode gType;

						rightExpr = bex.getRightExpression();
						gType = rightExpr.getType();

						if(gType == ClassHelper.OBJECT_TYPE)
						{
							if(rightExpr instanceof MethodCallExpression)
							{
								gType = GUtil.getTypeFromMethCall((MethodCallExpression)rightExpr, true);
							}
							else if(rightExpr instanceof VariableExpression)
							{
								String rightVarName = ((VariableExpression) rightExpr).getName();
								
								if(GUtil.varName2TypeMap.containsKey(rightVarName))
								{
									gType = GUtil.varName2TypeMap.get(rightVarName);
								}
							}
						}

						if(gType == ClassHelper.GSTRING_TYPE)
						{
							gType = ClassHelper.STRING_TYPE;
						}

						this.localMethods.get(entry.getKey()).get(parmIndex).type = gType;
					}
				}
			}
		}

		super.visitBinaryExpression(bex);
	}

	/* evt.deviceId => type = STEvent
	 * */
	@Override
	public void visitPropertyExpression(PropertyExpression propExpr)
	{
		Expression objExpr = propExpr.getObjectExpression();

		if(objExpr instanceof VariableExpression)
		{
			String varName = ((VariableExpression) objExpr).getName();

			for(Map.Entry<String, List<String>> entry : this.varNames.entrySet())
			{
				if(entry.getValue().contains(varName))
				{
					int parmIndex = entry.getValue().indexOf(varName);

					if(this.localMethods.get(entry.getKey()).get(parmIndex).type == ClassHelper.OBJECT_TYPE)
					{
						String propName = propExpr.getPropertyAsString();
						ClassNode gType = null;

						if(GUtil.isASTEventProp(propName))
						{
							gType = GUtil.getClassType("STEvent");
						}

						if(gType != null)
						{
							this.localMethods.get(entry.getKey()).get(parmIndex).type = gType;
						}
					}
				}
			}
		}

		super.visitPropertyExpression(propExpr);
	}

	/* for (sensor in sensors) {
	 * */
	@Override
	public void visitForLoop(ForStatement forLoop) {
		String varName = forLoop.getVariable().getName();

		for(Map.Entry<String, List<String>> entry : this.varNames.entrySet())
		{
			if(entry.getValue().contains(varName))
			{
				int parmIndex = entry.getValue().indexOf(varName);

				if(this.localMethods.get(entry.getKey()).get(parmIndex).type == ClassHelper.OBJECT_TYPE)
				{
					Expression colExpr = forLoop.getCollectionExpression();
					ClassNode gType = null;

					if(colExpr instanceof VariableExpression)
					{
						GenericsType[] genericTypes = ((VariableExpression)colExpr).getOriginType().getGenericsTypes();

						if(genericTypes.length > 0)
						{
							gType = genericTypes[0].getType();
						}
					}

					if(gType != null)
					{
						this.localMethods.get(entry.getKey()).get(parmIndex).type = gType;
					}
				}
			}
		}


		super.visitForLoop(forLoop);
	}
}
