package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.GenericsType;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;
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
import org.codehaus.groovy.control.SourceUnit;

import edu.ksu.cis.bandera.jjjc.gparser.g2jconverter.GReturnAdder;
import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;

public class GReturnTypeHandler  extends ClassCodeVisitorSupport {
	/********************************************/
	/* stLocalNames contains all methods' names of SmartThings and Groovy script shell */
	private List<String> stLocalNames;
	private ClassNode returnType;
	/********************************************/
	
	public GReturnTypeHandler()
	{
		stLocalNames = Arrays.asList("definition", "preferences", "installed", "updated", "run", "main", "runScript",
				"methodMissing", "propertyMissing", "setMetaClass", "getProperty", "$getStaticMetaClass");
	}
	
	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}
	
	private List<String> getVarNameList(BinaryExpression bex)
	{
		List<String> result = new ArrayList<String>();
		Expression leftExpr = bex.getLeftExpression();
		Expression rightExpr = bex.getRightExpression();
		
		/* Handle leftExpr */
		if(leftExpr instanceof BinaryExpression)
		{
			result.addAll(this.getVarNameList((BinaryExpression) leftExpr));
		}
		else if(leftExpr instanceof VariableExpression)
		{
			String varName = ((VariableExpression) leftExpr).getName();
			result.add(varName);
		}
		
		/* Handle rightExpr */
		if(rightExpr instanceof BinaryExpression)
		{
			result.addAll(this.getVarNameList((BinaryExpression) rightExpr));
		}
		else if(rightExpr instanceof VariableExpression)
		{
			String varName = ((VariableExpression) rightExpr).getName();
			result.add(varName);
		}
		
		return result;
	}
	
	private void inferReturnType(Expression expr)
	{
		if (expr instanceof VariableExpression)
		{
			/* We need to get the varName so that we can traverse the code to
			 * get the data type of the variable, which will become the return
			 * type of the method */
			String varName = ((VariableExpression) expr).getName();
			
			if(GUtil.varName2TypeMap.containsKey(varName))
			{
				this.returnType = GUtil.varName2TypeMap.get(varName);
			}
		}
		else if (expr instanceof MethodCallExpression) /* (switches + onSwitches).findAll{it} */
		{
			/* We need handle other types of returnExpr */
			String methName = ((MethodCallExpression)expr).getMethodAsString();
			
			this.returnType = GUtil.getTypeFromSmartThingApi(methName, true);
			if(this.returnType == ClassHelper.OBJECT_TYPE)
			{
				List<String> varNameList = new ArrayList<String>();
				Expression objExpr = ((MethodCallExpression) expr).getObjectExpression();
				ClassNode gType = null;
				
				if(objExpr instanceof VariableExpression)
				{
					String varName = ((VariableExpression) objExpr).getName();
					varNameList.add(varName);
				}
				else if(objExpr instanceof BinaryExpression)
				{
					varNameList.addAll(this.getVarNameList((BinaryExpression) objExpr));
				}
				
				for(String varName : varNameList)
				{
					if(GUtil.varName2TypeMap.containsKey(varName))
					{
						gType = GUtil.varName2TypeMap.get(varName);
						break;
					}
				}
				if(gType != null)
				{
					if(methName.equals("findAll"))
					{
						this.returnType = gType;
					}
					else if(methName.equals("find"))
					{
						GenericsType[] genericsTypes = gType.getGenericsTypes();
						
						if(genericsTypes.length > 0)
						{
							this.returnType = genericsTypes[0].getType();
						}
					}
				}
			}
		}
		else if (expr instanceof BinaryExpression)
		{
			String operation = ((BinaryExpression)expr).getOperation().getText();
			
			if(operation.equals("=")) /* state[evt.deviceId] = now() */
			{
				if(GUtil.isExprAMapAccess(((BinaryExpression)expr).getLeftExpression()))
				{
					this.returnType = ClassHelper.VOID_TYPE;
				}
				else
				{
					inferReturnType(((BinaryExpression)expr).getRightExpression());
				}
			}
			else if(operation.equals("+")) /* switches + onSwitches */
			{
				List<String> varNameList = new ArrayList<String>();
				varNameList.addAll(this.getVarNameList((BinaryExpression) expr));
				
				for(String varName : varNameList)
				{
					if(GUtil.varName2TypeMap.containsKey(varName))
					{
						this.returnType = GUtil.varName2TypeMap.get(varName);
					}
				}
			}
		}
	}
	
	private void getReturnTypeOfABlock(Statement gStmt)
	{
		if(gStmt instanceof BlockStatement)
		{
			List<Statement> gStmtList = ((BlockStatement)gStmt).getStatements();
			
			for(int i = 0; i < gStmtList.size(); i++)
			{
				Statement gSubStmt = gStmtList.get(i);
				
				if(gSubStmt instanceof ExpressionStatement)
				{}
				else if(gSubStmt instanceof IfStatement)
				{
					/* Handle ifBlock and elseBlock of an IfStatement */
					this.getReturnTypeOfABlock(((IfStatement) gSubStmt).getIfBlock()); 
					this.getReturnTypeOfABlock(((IfStatement) gSubStmt).getElseBlock());
				}
				else if(gSubStmt instanceof WhileStatement)
				{
					/* Handle loopBlock of WhileStatement */
					this.getReturnTypeOfABlock(((WhileStatement) gSubStmt).getLoopBlock());
				}
				else if(gSubStmt instanceof DoWhileStatement)
				{
					/* Handle loopBlock of DoWhileStatement */
					this.getReturnTypeOfABlock(((DoWhileStatement) gSubStmt).getLoopBlock());
				}
				else if(gSubStmt instanceof ForStatement)
				{
					/* Handle loopBlock of ForStatement */
					this.getReturnTypeOfABlock(((ForStatement) gSubStmt).getLoopBlock());
				}
				else if(gSubStmt instanceof ReturnStatement)
				{
					Expression expr = ((ReturnStatement) gSubStmt).getExpression();
					ClassNode retType = expr.getType();
					
					if(this.returnType == null)
					{
						this.returnType = retType;
					}
					
					if((this.returnType == ClassHelper.OBJECT_TYPE)
							|| GUtil.isArrayGenericsTypesNull(this.returnType))
					{
						this.inferReturnType(expr);
					}
				}
				else if(gSubStmt instanceof BreakStatement)
				{}
				else if(gSubStmt instanceof BlockStatement)
				{
					this.getReturnTypeOfABlock((BlockStatement) gSubStmt);
				}
				else
				{
					System.out.println("[GReturnTypeHandler.getReturnTypeOfABlock] unexpected statement type: " + gSubStmt);
				}
			}
		}
		else if(gStmt instanceof IfStatement)
		{
			/* Handle ifBlock and elseBlock of an IfStatement */
			this.getReturnTypeOfABlock(((IfStatement) gStmt).getIfBlock()); 
			this.getReturnTypeOfABlock(((IfStatement) gStmt).getElseBlock());
		}
		else if(gStmt instanceof ReturnStatement)
		{
			Expression expr = ((ReturnStatement) gStmt).getExpression();
			ClassNode retType = expr.getType();
			
			if(this.returnType == null)
			{
				this.returnType = retType;
			}
			
			if((this.returnType == ClassHelper.OBJECT_TYPE)
					|| GUtil.isArrayGenericsTypesNull(this.returnType))
			{
				this.inferReturnType(expr);
			}
		}
		else if(!(gStmt instanceof EmptyStatement))
		{
			System.out.println("[GReturnTypeHandler.getReturnTypeOfABlock] a wrong call!!!" + gStmt);
		}
	}
	
	/* If return type of a method is void, and if the source code
	 * of this method contain any return statement, this return 
	 * statement will be transformed into an ExpressionStatement
	 * */
	private void transformReturnStmtOfABlock(Statement gStmt)
	{
		if(gStmt instanceof BlockStatement)
		{
			List<Statement> gStmtList = ((BlockStatement)gStmt).getStatements();
			
			for(int i = 0; i < gStmtList.size(); i++)
			{
				Statement gSubStmt = gStmtList.get(i);
				
				if(gSubStmt instanceof ExpressionStatement)
				{}
				else if(gSubStmt instanceof IfStatement)
				{
					/* Handle ifBlock and elseBlock of an IfStatement */
					Statement ifBlock = ((IfStatement) gSubStmt).getIfBlock();
					Statement elseBlock = ((IfStatement) gSubStmt).getElseBlock();
					
					if(ifBlock instanceof ReturnStatement)
					{
						Expression expr = ((ReturnStatement) ifBlock).getExpression();
						
						if(expr instanceof ConstantExpression)
						{
							if(expr.getText().equals("null")) /* return; */
							{
								/* Replace the return statement with an empty block statement */
								((IfStatement)gSubStmt).setIfBlock(new BlockStatement());
							}
							else
							{
								gStmtList.set(i, new ExpressionStatement(expr));
							}
						}
						else
						{
							gStmtList.set(i, new ExpressionStatement(expr));
						}
					}
					else
					{
						this.transformReturnStmtOfABlock(ifBlock); 
					}
					
					if(elseBlock instanceof ReturnStatement)
					{
						Expression expr = ((ReturnStatement) elseBlock).getExpression();
						
						if(expr instanceof ConstantExpression)
						{
							if(expr.getText().equals("null")) /* return; */
							{
								/* Replace the return statement with an empty block statement */
								((IfStatement)gSubStmt).setElseBlock(new BlockStatement());
							}
							else
							{
								gStmtList.set(i, new ExpressionStatement(expr));
							}
						}
						else
						{
							gStmtList.set(i, new ExpressionStatement(expr));
						}
					}
					else
					{
						this.transformReturnStmtOfABlock(elseBlock); 
					}
				}
				else if(gSubStmt instanceof WhileStatement)
				{
					/* Handle loopBlock of WhileStatement */
					Statement loopBlock = ((WhileStatement) gSubStmt).getLoopBlock();
					
					if(loopBlock instanceof ReturnStatement)
					{
						Expression expr = ((ReturnStatement) loopBlock).getExpression();
						
						if(expr instanceof ConstantExpression)
						{
							if(expr.getText().equals("null")) /* return; */
							{
								/* Remove this void WhileStatement */
								gStmtList.remove(i);
							}
							else
							{
								gStmtList.set(i, new ExpressionStatement(expr));
							}
						}
						else
						{
							gStmtList.set(i, new ExpressionStatement(expr));
						}
					}
					else
					{
						this.transformReturnStmtOfABlock(loopBlock); 
					}
				}
				else if(gSubStmt instanceof DoWhileStatement)
				{
					/* Handle loopBlock of DoWhileStatement */
					Statement loopBlock = ((DoWhileStatement) gSubStmt).getLoopBlock();
					
					if(loopBlock instanceof ReturnStatement)
					{
						Expression expr = ((ReturnStatement) loopBlock).getExpression();
						
						if(expr instanceof ConstantExpression)
						{
							if(expr.getText().equals("null")) /* return; */
							{
								/* Remove this void DoWhileStatement */
								gStmtList.remove(i);
							}
							else
							{
								gStmtList.set(i, new ExpressionStatement(expr));
							}
						}
						else
						{
							gStmtList.set(i, new ExpressionStatement(expr));
						}
					}
					else
					{
						this.transformReturnStmtOfABlock(loopBlock); 
					}
				}
				else if(gSubStmt instanceof ForStatement)
				{
					/* Handle loopBlock of ForStatement */
					Statement loopBlock = ((ForStatement) gSubStmt).getLoopBlock();
					
					if(loopBlock instanceof ReturnStatement)
					{
						Expression expr = ((ReturnStatement) loopBlock).getExpression();
						
						if(expr instanceof ConstantExpression)
						{
							if(expr.getText().equals("null")) /* return; */
							{
								/* Remove this void ForStatement */
								gStmtList.remove(i);
							}
							else
							{
								gStmtList.set(i, new ExpressionStatement(expr));
							}
						}
						else
						{
							gStmtList.set(i, new ExpressionStatement(expr));
						}
					}
					else
					{
						this.transformReturnStmtOfABlock(loopBlock); 
					}
					this.transformReturnStmtOfABlock(((ForStatement) gSubStmt).getLoopBlock());
				}
				else if(gSubStmt instanceof ReturnStatement)
				{
					Expression expr = ((ReturnStatement) gSubStmt).getExpression();
					
					if(expr instanceof ConstantExpression)
					{
						if(expr.getText().equals("null")) /* return; */
						{
							/* Remove this void return statement */
							gStmtList.remove(i);
						}
						else
						{
							gStmtList.set(i, new ExpressionStatement(expr));
						}
					}
					else
					{
						gStmtList.set(i, new ExpressionStatement(expr));
					}
				}
				else if(gSubStmt instanceof BreakStatement)
				{}
				else
				{
					System.out.println("[GReturnTypeHandler.transformReturnStmtOfABlock] unexpected statement type: " + gSubStmt);
				}
			}
		}
		else if(gStmt instanceof IfStatement)
		{
			/* Handle ifBlock and elseBlock of an IfStatement */
			this.transformReturnStmtOfABlock(((IfStatement) gStmt).getIfBlock()); 
			this.transformReturnStmtOfABlock(((IfStatement) gStmt).getElseBlock());
		}
		else if(!(gStmt instanceof EmptyStatement))
		{
			System.out.println("[GReturnTypeHandler.transformReturnStmtOfABlock] a wrong call!!!");
		}
	}
	
	@Override
	public void visitMethod(MethodNode meth)
	{
		String methName = meth.getName();
		
		if (!stLocalNames.contains(methName) && !methName.startsWith("this$") && !methName.startsWith("$static"))
		{
			GReturnAdder returnAdder = new GReturnAdder();
			this.returnType = meth.getReturnType();
			
			/* Check if method's content is empty */
			{
				Statement gStmt = meth.getCode();
				
				if(gStmt instanceof BlockStatement)
				{
					if(((BlockStatement)gStmt).getStatements().size() == 0)
					{
						this.returnType = ClassHelper.VOID_TYPE;
					}
				}
			}
			
			/* Add an explicit return statement */
			if(returnType != ClassHelper.VOID_TYPE)
			{
				returnAdder.visitMethod(meth);
			}
			
			/* First trial to infer the return type of method */
			if((returnType == ClassHelper.OBJECT_TYPE) || GUtil.isArrayGenericsTypesNull(returnType))
			{
				this.returnType = null;
				this.getReturnTypeOfABlock(meth.getCode());
				
				if(this.returnType == null)
				{
					this.returnType = ClassHelper.VOID_TYPE;
				}
				if(this.returnType == ClassHelper.VOID_TYPE)
				{
					this.transformReturnStmtOfABlock(meth.getCode());
				}
			}
			meth.setReturnType(this.returnType);
			GUtil.methNameTypeMap.put(methName, this.returnType);
		}
		super.visitMethod(meth);
	}
}
