package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.BooleanExpression;
import org.codehaus.groovy.ast.expr.CastExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.GStringExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.NotExpression;
import org.codehaus.groovy.ast.expr.PostfixExpression;
import org.codehaus.groovy.ast.expr.PrefixExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.DoWhileStatement;
import org.codehaus.groovy.ast.stmt.IfStatement;
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.ast.stmt.WhileStatement;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.ast.ClassNode;


/* Before:
 * state.modeStartTime = 0
 * def switchArr = (settings.switches ?: [])
 * After: assume "ClassName = GoodNight"
 * GoodNight_modeStartTime = 0
 * def switchArr = (switches ?: [])
 * */
public class GSystemPropertyHeaderRemoval extends ClassCodeVisitorSupport {
	/********************************************/
	private List<String> SystemPropertyHeaderList;
	private Map<String, ClassNode> StateVarMap;
	private String ClassName;
	/********************************************/

	public GSystemPropertyHeaderRemoval(String ClassName) {
		SystemPropertyHeaderList = Arrays.asList("state", "atomicState", "settings");
		StateVarMap = new HashMap<String, ClassNode>();
		this.ClassName = ClassName;
	}

	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}

	private void processBinaryExpression(BinaryExpression bex)
	{
		Expression leftExpr = bex.getLeftExpression();
		Expression rightExpr = bex.getRightExpression();

		/* Handle leftExpr */
		if(leftExpr instanceof NotExpression)
		{
			Expression subExpr = ((NotExpression) leftExpr).getExpression();

			if(subExpr instanceof PropertyExpression)
			{
				String newVarName = null;
				PropertyExpression prevPropExpr, curPropExpr;

				prevPropExpr = null;
				curPropExpr = (PropertyExpression) subExpr;
				while(true)
				{
					Expression objExpr = curPropExpr.getObjectExpression();

					if(objExpr instanceof PropertyExpression)
					{
						prevPropExpr = curPropExpr;
						curPropExpr = (PropertyExpression) objExpr;
					}
					else
					{
						if(objExpr instanceof VariableExpression)
						{
							String varName = ((VariableExpression) objExpr).getName();

							/* Check if varName is "state" or "settings" */
							if(SystemPropertyHeaderList.contains(varName))
							{
								Expression propExpr = curPropExpr.getProperty();

								if(propExpr instanceof ConstantExpression)
								{
									if(varName.equals("state") || varName.equals("atomicState"))
									{
										ClassNode gType = ClassHelper.OBJECT_TYPE;

										newVarName = this.ClassName + "_" + ((ConstantExpression) propExpr).getText();
										if(!StateVarMap.containsKey(newVarName))
										{
											StateVarMap.put(newVarName, gType);
										}
										else
										{
											if(StateVarMap.get(newVarName) == ClassHelper.OBJECT_TYPE)
											{
												StateVarMap.put(newVarName, gType);
											}
										}
									}
									else
									{
										newVarName = ((ConstantExpression) propExpr).getText();
									}
								}
							}
						}
						break;
					}
				}

				if(newVarName != null)
				{
					/* Remove the system property header: "state" or "settings" */
					VariableExpression newVarExpr = new VariableExpression(newVarName);

					if(prevPropExpr != null)
					{
						prevPropExpr.setObjectExpression(newVarExpr);
					}
					else
					{
						NotExpression newNotExpr = new NotExpression(newVarExpr);
						bex.setLeftExpression(newNotExpr);
					}
				}
			}
		}
		else if(leftExpr instanceof CastExpression)
		{
			Expression subExpr = ((CastExpression) leftExpr).getExpression();

			if(subExpr instanceof PropertyExpression)
			{
				String newVarName = null;
				PropertyExpression prevPropExpr, curPropExpr;

				prevPropExpr = null;
				curPropExpr = (PropertyExpression) subExpr;
				while(true)
				{
					Expression objExpr = curPropExpr.getObjectExpression();

					if(objExpr instanceof PropertyExpression)
					{
						prevPropExpr = curPropExpr;
						curPropExpr = (PropertyExpression) objExpr;
					}
					else
					{
						if(objExpr instanceof VariableExpression)
						{
							String varName = ((VariableExpression) objExpr).getName();

							/* Check if varName is "state" or "settings" */
							if(SystemPropertyHeaderList.contains(varName))
							{
								Expression propExpr = curPropExpr.getProperty();

								if(propExpr instanceof ConstantExpression)
								{
									if(varName.equals("state") || varName.equals("atomicState"))
									{
										ClassNode gType = ClassHelper.OBJECT_TYPE;

										newVarName = this.ClassName + "_" + ((ConstantExpression) propExpr).getText();
										if(!StateVarMap.containsKey(newVarName))
										{
											StateVarMap.put(newVarName, gType);
										}
										else
										{
											if(StateVarMap.get(newVarName) == ClassHelper.OBJECT_TYPE)
											{
												StateVarMap.put(newVarName, gType);
											}
										}
									}
									else
									{
										newVarName = ((ConstantExpression) propExpr).getText();
									}
								}
							}
						}
						break;
					}
				}

				if(newVarName != null)
				{
					/* Remove the system property header: "state" or "settings" */
					VariableExpression newVarExpr = new VariableExpression(newVarName);

					if(prevPropExpr != null)
					{
						prevPropExpr.setObjectExpression(newVarExpr);
					}
					else
					{
						CastExpression newCastExpr = new CastExpression(((CastExpression)leftExpr).getType(),newVarExpr);
						bex.setLeftExpression(newCastExpr);
					}
				}
			}
		}
		else if(leftExpr instanceof PropertyExpression)
		{
			String newVarName = null;
			PropertyExpression prevPropExpr, curPropExpr;

			prevPropExpr = null;
			curPropExpr = (PropertyExpression) leftExpr;
			while(true)
			{
				Expression objExpr = curPropExpr.getObjectExpression();

				if(objExpr instanceof PropertyExpression)
				{
					prevPropExpr = curPropExpr;
					curPropExpr = (PropertyExpression) objExpr;
				}
				else
				{
					if(objExpr instanceof VariableExpression)
					{
						String varName = ((VariableExpression) objExpr).getName();

						/* Check if varName is "state" or "settings" */
						if(SystemPropertyHeaderList.contains(varName))
						{
							Expression propExpr = curPropExpr.getProperty();

							if(propExpr instanceof ConstantExpression)
							{
								if(varName.equals("state") || varName.equals("atomicState"))
								{
									ClassNode gType = rightExpr.getType();

									newVarName = this.ClassName + "_" + ((ConstantExpression) propExpr).getText();
									if(!StateVarMap.containsKey(newVarName))
									{
										StateVarMap.put(newVarName, gType);
									}
									else
									{
										if(StateVarMap.get(newVarName) == ClassHelper.OBJECT_TYPE)
										{
											StateVarMap.put(newVarName, gType);
										}
									}
								}
								else
								{
									newVarName = ((ConstantExpression) propExpr).getText();
								}
							}
						}
					}
					break;
				}
			}

			if(newVarName != null)
			{
				/* Remove the system property header: "state" or "settings" */
				VariableExpression newVarExpr = new VariableExpression(newVarName);

				if(prevPropExpr != null)
				{
					prevPropExpr.setObjectExpression(newVarExpr);
				}
				else
				{
					bex.setLeftExpression(newVarExpr);
				}
			}
		}

		/* Handle rightExpr */
		if(rightExpr instanceof NotExpression)
		{
			Expression subExpr = ((NotExpression) rightExpr).getExpression();

			if(subExpr instanceof PropertyExpression)
			{
				String newVarName = null;
				PropertyExpression prevPropExpr, curPropExpr;

				prevPropExpr = null;
				curPropExpr = (PropertyExpression) subExpr;
				while(true)
				{
					Expression objExpr = curPropExpr.getObjectExpression();

					if(objExpr instanceof PropertyExpression)
					{
						prevPropExpr = curPropExpr;
						curPropExpr = (PropertyExpression) objExpr;
					}
					else
					{
						if(objExpr instanceof VariableExpression)
						{
							String varName = ((VariableExpression) objExpr).getName();

							/* Check if varName is "state" or "settings" */
							if(SystemPropertyHeaderList.contains(varName))
							{
								Expression propExpr = curPropExpr.getProperty();

								if(propExpr instanceof ConstantExpression)
								{
									if(varName.equals("state") || varName.equals("atomicState"))
									{
										ClassNode gType = ClassHelper.OBJECT_TYPE;

										newVarName = this.ClassName + "_" + ((ConstantExpression) propExpr).getText();
										if(!StateVarMap.containsKey(newVarName))
										{
											StateVarMap.put(newVarName, gType);
										}
										else
										{
											if(StateVarMap.get(newVarName) == ClassHelper.OBJECT_TYPE)
											{
												StateVarMap.put(newVarName, gType);
											}
										}
									}
									else
									{
										newVarName = ((ConstantExpression) propExpr).getText();
									}
								}
							}
						}
						break;
					}
				}

				if(newVarName != null)
				{
					/* Remove the system property header: "state" or "settings" */
					VariableExpression newVarExpr = new VariableExpression(newVarName);

					if(prevPropExpr != null)
					{
						prevPropExpr.setObjectExpression(newVarExpr);
					}
					else
					{
						NotExpression newNotExpr = new NotExpression(newVarExpr);
						bex.setRightExpression(newNotExpr);
					}
				}
			}
		}
		else if(rightExpr instanceof CastExpression)
		{
			Expression subExpr = ((CastExpression) rightExpr).getExpression();

			if(subExpr instanceof PropertyExpression)
			{
				String newVarName = null;
				PropertyExpression prevPropExpr, curPropExpr;

				prevPropExpr = null;
				curPropExpr = (PropertyExpression) subExpr;
				while(true)
				{
					Expression objExpr = curPropExpr.getObjectExpression();

					if(objExpr instanceof PropertyExpression)
					{
						prevPropExpr = curPropExpr;
						curPropExpr = (PropertyExpression) objExpr;
					}
					else
					{
						if(objExpr instanceof VariableExpression)
						{
							String varName = ((VariableExpression) objExpr).getName();

							/* Check if varName is "state" or "settings" */
							if(SystemPropertyHeaderList.contains(varName))
							{
								Expression propExpr = curPropExpr.getProperty();

								if(propExpr instanceof ConstantExpression)
								{
									if(varName.equals("state") || varName.equals("atomicState"))
									{
										ClassNode gType = ClassHelper.OBJECT_TYPE;

										newVarName = this.ClassName + "_" + ((ConstantExpression) propExpr).getText();
										if(!StateVarMap.containsKey(newVarName))
										{
											StateVarMap.put(newVarName, gType);
										}
										else
										{
											if(StateVarMap.get(newVarName) == ClassHelper.OBJECT_TYPE)
											{
												StateVarMap.put(newVarName, gType);
											}
										}
									}
									else
									{
										newVarName = ((ConstantExpression) propExpr).getText();
									}
								}
							}
						}
						break;
					}
				}

				if(newVarName != null)
				{
					/* Remove the system property header: "state" or "settings" */
					VariableExpression newVarExpr = new VariableExpression(newVarName);

					if(prevPropExpr != null)
					{
						prevPropExpr.setObjectExpression(newVarExpr);
					}
					else
					{
						CastExpression newNotExpr = new CastExpression(((CastExpression)rightExpr).getType(), newVarExpr);
						bex.setRightExpression(newNotExpr);
					}
				}
			}
		}
		else if(rightExpr instanceof PropertyExpression)
		{
			String newVarName = null;
			PropertyExpression prevPropExpr, curPropExpr;

			prevPropExpr = null;
			curPropExpr = (PropertyExpression) rightExpr;
			while(true)
			{
				Expression objExpr = curPropExpr.getObjectExpression();

				if(objExpr instanceof PropertyExpression)
				{
					prevPropExpr = curPropExpr;
					curPropExpr = (PropertyExpression) objExpr;
				}
				else
				{
					if(objExpr instanceof VariableExpression)
					{
						String varName = ((VariableExpression) objExpr).getName();

						/* Check if varName is "state" or "settings" */
						if(SystemPropertyHeaderList.contains(varName))
						{
							Expression propExpr = curPropExpr.getProperty();

							if(propExpr instanceof ConstantExpression)
							{
								if(varName.equals("state") || varName.equals("atomicState"))
								{
									ClassNode gType = leftExpr.getType();

									newVarName = this.ClassName + "_" + ((ConstantExpression) propExpr).getText();
									if(!StateVarMap.containsKey(newVarName))
									{
										StateVarMap.put(newVarName, gType);
									}
									else
									{
										if(StateVarMap.get(newVarName) == ClassHelper.OBJECT_TYPE)
										{
											StateVarMap.put(newVarName, gType);
										}
									}
								}
								else
								{
									newVarName = ((ConstantExpression) propExpr).getText();
								}
							}
						}
					}
					break;
				}
			}

			if(newVarName != null)
			{
				/* Remove the system property header: "state" or "settings" */
				VariableExpression newVarExpr = new VariableExpression(newVarName);

				if(prevPropExpr != null)
				{
					prevPropExpr.setObjectExpression(newVarExpr);
				}
				else
				{
					bex.setRightExpression(newVarExpr);
				}
			}
		}
	}

	private BooleanExpression createNewBooleanExpression(BooleanExpression boolExpr)
	{
		BooleanExpression newBoolExpr = null;
		Expression expr = boolExpr.getExpression();

		if(expr instanceof NotExpression)
		{
			Expression subExpr = ((NotExpression) expr).getExpression();

			if(subExpr instanceof PropertyExpression)
			{
				String newVarName = null;
				PropertyExpression prevPropExpr, curPropExpr;

				prevPropExpr = null;
				curPropExpr = (PropertyExpression) subExpr;
				while(true)
				{
					Expression objExpr = curPropExpr.getObjectExpression();

					if(objExpr instanceof PropertyExpression)
					{
						prevPropExpr = curPropExpr;
						curPropExpr = (PropertyExpression) objExpr;
					}
					else
					{
						if(objExpr instanceof VariableExpression)
						{
							String varName = ((VariableExpression) objExpr).getName();

							/* Check if varName is "state" or "settings" */
							if(SystemPropertyHeaderList.contains(varName))
							{
								Expression propExpr = curPropExpr.getProperty();

								if(propExpr instanceof ConstantExpression)
								{
									if(varName.equals("state") || varName.equals("atomicState"))
									{
										ClassNode gType = ClassHelper.OBJECT_TYPE;

										newVarName = this.ClassName + "_" + ((ConstantExpression) propExpr).getText();
										if(!StateVarMap.containsKey(newVarName))
										{
											StateVarMap.put(newVarName, gType);
										}
										else
										{
											if(StateVarMap.get(newVarName) == ClassHelper.OBJECT_TYPE)
											{
												StateVarMap.put(newVarName, gType);
											}
										}
									}
									else
									{
										newVarName = ((ConstantExpression) propExpr).getText();
									}
								}
							}
						}
						break;
					}
				}

				if(newVarName != null)
				{
					/* Remove the system property header: "state" or "settings" */
					VariableExpression newVarExpr = new VariableExpression(newVarName);

					if(prevPropExpr != null)
					{
						prevPropExpr.setObjectExpression(newVarExpr);
						newBoolExpr = new BooleanExpression(expr);
					}
					else
					{
						NotExpression newNotExpr = new NotExpression(newVarExpr);
						newBoolExpr = new BooleanExpression(newNotExpr);
					}
				}
			}
		}
		else if(expr instanceof PropertyExpression)
		{
			String newVarName = null;
			PropertyExpression prevPropExpr, curPropExpr;

			prevPropExpr = null;
			curPropExpr = (PropertyExpression) expr;
			while(true)
			{
				Expression objExpr = curPropExpr.getObjectExpression();

				if(objExpr instanceof PropertyExpression)
				{
					prevPropExpr = curPropExpr;
					curPropExpr = (PropertyExpression) objExpr;
				}
				else
				{
					if(objExpr instanceof VariableExpression)
					{
						String varName = ((VariableExpression) objExpr).getName();

						/* Check if varName is "state" or "settings" */
						if(SystemPropertyHeaderList.contains(varName))
						{
							Expression propExpr = curPropExpr.getProperty();

							if(propExpr instanceof ConstantExpression)
							{
								if(varName.equals("state") || varName.equals("atomicState"))
								{
									ClassNode gType = ClassHelper.OBJECT_TYPE;

									newVarName = this.ClassName + "_" + ((ConstantExpression) propExpr).getText();
									if(!StateVarMap.containsKey(newVarName))
									{
										StateVarMap.put(newVarName, gType);
									}
									else
									{
										if(StateVarMap.get(newVarName) == ClassHelper.OBJECT_TYPE)
										{
											StateVarMap.put(newVarName, gType);
										}
									}
								}
								else
								{
									newVarName = ((ConstantExpression) propExpr).getText();
								}
							}
						}
					}
					break;
				}
			}

			if(newVarName != null)
			{
				/* Remove the system property header: "state" or "settings" */
				VariableExpression newVarExpr = new VariableExpression(newVarName);

				if(prevPropExpr != null)
				{
					prevPropExpr.setObjectExpression(newVarExpr);
					newBoolExpr = new BooleanExpression(expr);
				}
				else
				{
					newBoolExpr = new BooleanExpression(newVarExpr);
				}
			}

		}

		return newBoolExpr;
	}

	@Override
	public void visitArgumentlistExpression(ArgumentListExpression ale) {
		List<Expression> exprList = ale.getExpressions();

		for(int i = 0; i < exprList.size(); i++)
		{
			Expression curExpr = exprList.get(i);

			if(curExpr instanceof PropertyExpression)
			{
				String newVarName = null;
				PropertyExpression prevPropExpr, curPropExpr;

				prevPropExpr = null;
				curPropExpr = (PropertyExpression) curExpr;
				while(true)
				{
					Expression objExpr = curPropExpr.getObjectExpression();

					if(objExpr instanceof PropertyExpression)
					{
						prevPropExpr = curPropExpr;
						curPropExpr = (PropertyExpression) objExpr;
					}
					else
					{
						if(objExpr instanceof VariableExpression)
						{
							String varName = ((VariableExpression) objExpr).getName();

							/* Check if varName is "state" or "settings" */
							if(SystemPropertyHeaderList.contains(varName))
							{
								Expression propExpr = curPropExpr.getProperty();

								if(propExpr instanceof ConstantExpression)
								{
									if(varName.equals("state") || varName.equals("atomicState"))
									{
										newVarName = this.ClassName + "_" + ((ConstantExpression) propExpr).getText();
									}
									else
									{
										newVarName = ((ConstantExpression) propExpr).getText();
									}
								}
							}
						}
						break;
					}
				}

				if(newVarName != null)
				{
					/* Remove the system property header: "state" or "settings" */
					VariableExpression newVarExpr = new VariableExpression(newVarName);

					if(prevPropExpr != null)
					{
						prevPropExpr.setObjectExpression(newVarExpr);
					}
					else
					{
						exprList.set(i, newVarExpr);
					}
				}
			}
		}

		super.visitArgumentlistExpression(ale);
	}

	@Override
	public void visitBinaryExpression(BinaryExpression bex)
	{
		this.processBinaryExpression(bex);
		super.visitBinaryExpression(bex);
	}

	@Override
	public void visitPostfixExpression(PostfixExpression postExpr) {
		Expression expr = postExpr.getExpression();

		if(expr instanceof PropertyExpression)
		{
			String newVarName = null;
			PropertyExpression prevPropExpr, curPropExpr;

			prevPropExpr = null;
			curPropExpr = (PropertyExpression) expr;
			while(true)
			{
				Expression objExpr = curPropExpr.getObjectExpression();

				if(objExpr instanceof PropertyExpression)
				{
					prevPropExpr = curPropExpr;
					curPropExpr = (PropertyExpression) objExpr;
				}
				else
				{
					if(objExpr instanceof VariableExpression)
					{
						String varName = ((VariableExpression) objExpr).getName();

						/* Check if varName is "state" or "settings" */
						if(SystemPropertyHeaderList.contains(varName))
						{
							Expression propExpr = curPropExpr.getProperty();

							if(propExpr instanceof ConstantExpression)
							{
								if(varName.equals("state") || varName.equals("atomicState"))
								{
									ClassNode gType = expr.getType();

									newVarName = this.ClassName + "_" + ((ConstantExpression) propExpr).getText();
									if(!StateVarMap.containsKey(newVarName))
									{
										StateVarMap.put(newVarName, gType);
									}
									else
									{
										if(StateVarMap.get(newVarName) == ClassHelper.OBJECT_TYPE)
										{
											StateVarMap.put(newVarName, gType);
										}
									}
								}
								else
								{
									newVarName = ((ConstantExpression) propExpr).getText();
								}
							}
						}
					}
					break;
				}
			}

			if(newVarName != null)
			{
				/* Remove the system property header: "state" or "settings" */
				VariableExpression newVarExpr = new VariableExpression(newVarName);

				if(prevPropExpr != null)
				{
					prevPropExpr.setObjectExpression(newVarExpr);
				}
				else
				{
					postExpr.setExpression(newVarExpr);
				}
			}
		}

		super.visitPostfixExpression(postExpr);
	}

	@Override
	public void visitPrefixExpression(PrefixExpression preExpr) {
		Expression expr = preExpr.getExpression();

		if(expr instanceof PropertyExpression)
		{
			String newVarName = null;
			PropertyExpression prevPropExpr, curPropExpr;

			prevPropExpr = null;
			curPropExpr = (PropertyExpression) expr;
			while(true)
			{
				Expression objExpr = curPropExpr.getObjectExpression();

				if(objExpr instanceof PropertyExpression)
				{
					prevPropExpr = curPropExpr;
					curPropExpr = (PropertyExpression) objExpr;
				}
				else
				{
					if(objExpr instanceof VariableExpression)
					{
						String varName = ((VariableExpression) objExpr).getName();

						/* Check if varName is "state" or "settings" */
						if(SystemPropertyHeaderList.contains(varName))
						{
							Expression propExpr = curPropExpr.getProperty();

							if(propExpr instanceof ConstantExpression)
							{
								if(varName.equals("state") || varName.equals("atomicState"))
								{
									ClassNode gType = expr.getType();

									newVarName = this.ClassName + "_" + ((ConstantExpression) propExpr).getText();
									if(!StateVarMap.containsKey(newVarName))
									{
										StateVarMap.put(newVarName, gType);
									}
									else
									{
										if(StateVarMap.get(newVarName) == ClassHelper.OBJECT_TYPE)
										{
											StateVarMap.put(newVarName, gType);
										}
									}
								}
								else
								{
									newVarName = ((ConstantExpression) propExpr).getText();
								}
							}
						}
					}
					break;
				}
			}

			if(newVarName != null)
			{
				/* Remove the system property header: "state" or "settings" */
				VariableExpression newVarExpr = new VariableExpression(newVarName);

				if(prevPropExpr != null)
				{
					prevPropExpr.setObjectExpression(newVarExpr);
				}
				else
				{
					preExpr.setExpression(newVarExpr);
				}
			}

		}
		super.visitPrefixExpression(preExpr);
	}

	public Map<String, ClassNode> getStateVarList()
	{
		return this.StateVarMap;
	}

	@Override
	public void visitIfElse(IfStatement ifElse) {
		BooleanExpression boolExpr = ifElse.getBooleanExpression();
		BooleanExpression newBoolExpr = this.createNewBooleanExpression(boolExpr);

		if(newBoolExpr != null)
		{
			ifElse.setBooleanExpression(newBoolExpr);
		}

		super.visitIfElse(ifElse);
	}

	@Override
	public void visitDoWhileLoop(DoWhileStatement loop) {
		BooleanExpression boolExpr = loop.getBooleanExpression();
		BooleanExpression newBoolExpr = this.createNewBooleanExpression(boolExpr);

		if(newBoolExpr != null)
		{
			loop.setBooleanExpression(newBoolExpr);
		}

		super.visitDoWhileLoop(loop);
	}

	@Override
	public void visitWhileLoop(WhileStatement loop) {
		BooleanExpression boolExpr = loop.getBooleanExpression();
		BooleanExpression newBoolExpr = this.createNewBooleanExpression(boolExpr);

		if(newBoolExpr != null)
		{
			loop.setBooleanExpression(newBoolExpr);
		}

		super.visitWhileLoop(loop);
	}

	@Override
	public void visitMethodCallExpression(MethodCallExpression mce) {
		Expression expr = mce.getObjectExpression();

		if(expr instanceof PropertyExpression)
		{
			String newVarName = null;
			PropertyExpression prevPropExpr, curPropExpr;

			prevPropExpr = null;
			curPropExpr = (PropertyExpression) expr;
			while(true)
			{
				Expression objExpr = curPropExpr.getObjectExpression();

				if(objExpr instanceof PropertyExpression)
				{
					prevPropExpr = curPropExpr;
					curPropExpr = (PropertyExpression) objExpr;
				}
				else
				{
					if(objExpr instanceof VariableExpression)
					{
						String varName = ((VariableExpression) objExpr).getName();

						/* Check if varName is "state" or "settings" */
						if(SystemPropertyHeaderList.contains(varName))
						{
							Expression propExpr = curPropExpr.getProperty();

							if(propExpr instanceof ConstantExpression)
							{
								if(varName.equals("state") || varName.equals("atomicState"))
								{
									ClassNode gType = expr.getType();

									newVarName = this.ClassName + "_" + ((ConstantExpression) propExpr).getText();
									if(!StateVarMap.containsKey(newVarName))
									{
										StateVarMap.put(newVarName, gType);
									}
									else
									{
										if(StateVarMap.get(newVarName) == ClassHelper.OBJECT_TYPE)
										{
											StateVarMap.put(newVarName, gType);
										}
									}
								}
								else
								{
									newVarName = ((ConstantExpression) propExpr).getText();
								}
							}
						}
					}
					break;
				}
			}

			if(newVarName != null)
			{
				/* Remove the system property header: "state" or "settings" */
				VariableExpression newVarExpr = new VariableExpression(newVarName);

				if(prevPropExpr != null)
				{
					prevPropExpr.setObjectExpression(newVarExpr);
				}
				else
				{
					mce.setObjectExpression(newVarExpr);
				}
			}
		}
		
		Expression meth = mce.getMethod();
		if((mce.getMethodAsString() == null) && (meth instanceof GStringExpression))
		{
			List<Expression> methList = ((GStringExpression)meth).getValues();
			
			if(methList.size() == 1)
			{
				/* Before:
				 * "$state.method"()
				 * After:
				 * "$ClassName_method"()
				 * */
				Expression methExpr = methList.get(0);
				
				if(methExpr instanceof PropertyExpression)
				{
					String newVarName = null;
					PropertyExpression prevPropExpr, curPropExpr;

					prevPropExpr = null;
					curPropExpr = (PropertyExpression) methExpr;
					while(true)
					{
						Expression objExpr = curPropExpr.getObjectExpression();

						if(objExpr instanceof PropertyExpression)
						{
							prevPropExpr = curPropExpr;
							curPropExpr = (PropertyExpression) objExpr;
						}
						else
						{
							if(objExpr instanceof VariableExpression)
							{
								String varName = ((VariableExpression) objExpr).getName();

								/* Check if varName is "state" or "settings" */
								if(SystemPropertyHeaderList.contains(varName))
								{
									Expression propExpr = curPropExpr.getProperty();

									if(propExpr instanceof ConstantExpression)
									{
										if(varName.equals("state") || varName.equals("atomicState"))
										{
											ClassNode gType = methExpr.getType();

											newVarName = this.ClassName + "_" + ((ConstantExpression) propExpr).getText();
											if(!StateVarMap.containsKey(newVarName))
											{
												StateVarMap.put(newVarName, gType);
											}
											else
											{
												if(StateVarMap.get(newVarName) == ClassHelper.OBJECT_TYPE)
												{
													StateVarMap.put(newVarName, gType);
												}
											}
										}
										else
										{
											newVarName = ((ConstantExpression) propExpr).getText();
										}
									}
								}
							}
							break;
						}
					}

					if(newVarName != null)
					{
						/* Remove the system property header: "state" or "settings" */
						VariableExpression newVarExpr = new VariableExpression(newVarName);

						if(prevPropExpr != null)
						{
							prevPropExpr.setObjectExpression(newVarExpr);
						}
						else
						{
							List<ConstantExpression> gStringStrings = new ArrayList<ConstantExpression>();
					        gStringStrings.add(new ConstantExpression(""));
					        gStringStrings.add(new ConstantExpression(""));
					        List<Expression> gStringValues = new ArrayList<Expression>();
					        gStringValues.add(newVarExpr);
					        GStringExpression newGStringExpr = new GStringExpression("$name", gStringStrings, gStringValues);
							mce.setMethod(newGStringExpr);
						}
					}
				}
			}
		}
		super.visitMethodCallExpression(mce);
	}
}
