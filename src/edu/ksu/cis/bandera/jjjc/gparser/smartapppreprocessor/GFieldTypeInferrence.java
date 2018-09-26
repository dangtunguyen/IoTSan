package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.DeclarationExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
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

import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;

public class GFieldTypeInferrence {
	/********************************************/
	private Map<String, ClassNode> name2TypeMap;
	private Map<String, ClassNode> localVarName2TypeMap;
	/********************************************/
	
	public GFieldTypeInferrence(Map<String, ClassNode> name2TypeMap)
	{
		this.name2TypeMap = name2TypeMap;
	}
	
	/* Getters */
	public Map<String, ClassNode> getName2TypeMap()
	{
		return this.name2TypeMap;
	}
	
	public void processAClassNode(ClassNode classNode)
	{
		for(MethodNode meth : classNode.getMethods())
		{
			this.localVarName2TypeMap = new HashMap<String, ClassNode>();
			this.handleABlock(meth.getCode());
		}
	}
	
	private ClassNode getInferredType(Expression expr)
	{
		ClassNode gType = expr.getType();
		
		if(gType == ClassHelper.OBJECT_TYPE)
		{
			if(expr instanceof VariableExpression)
			{
				String varName = ((VariableExpression) expr).getName();
				
				if(this.localVarName2TypeMap.containsKey(varName))
				{
					gType = this.localVarName2TypeMap.get(varName);
				}
			}
			else if(expr instanceof MethodCallExpression)
			{
				String methName = ((MethodCallExpression) expr).getMethodAsString();
				gType = GUtil.getTypeFromSmartThingApi(methName, true);
			}
			else if(expr instanceof PropertyExpression)
			{
				String propName = ((PropertyExpression) expr).getPropertyAsString();
				gType = GUtil.getTypeFromPropName(propName);
			}
		}
		
		return gType;
	}
	
	private void handleABinaryExpression(BinaryExpression bex)
	{
		Expression leftExpr = bex.getLeftExpression();
		Expression rightExpr = bex.getRightExpression();
		
		if(leftExpr instanceof VariableExpression)
		{
			String varName = ((VariableExpression) leftExpr).getName();
			
			if(this.name2TypeMap.containsKey(varName))
			{
				if(this.name2TypeMap.get(varName) == ClassHelper.OBJECT_TYPE)
				{
					ClassNode gType = this.getInferredType(rightExpr);
					this.name2TypeMap.put(varName, gType);
				}
			}
		}
		else if(leftExpr instanceof BinaryExpression)
		{
			this.handleABinaryExpression((BinaryExpression) leftExpr);
		}
		
		if(rightExpr instanceof VariableExpression)
		{
			String varName = ((VariableExpression) rightExpr).getName();
			
			if(this.name2TypeMap.containsKey(varName))
			{
				if(this.name2TypeMap.get(varName) == ClassHelper.OBJECT_TYPE)
				{
					ClassNode gType = this.getInferredType(leftExpr);
					this.name2TypeMap.put(varName, gType);
				}
			}
		}
		else if(rightExpr instanceof BinaryExpression)
		{
			this.handleABinaryExpression((BinaryExpression) rightExpr);
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
					Expression expr = ((ExpressionStatement)gSubStmt).getExpression();
					
					if(expr instanceof DeclarationExpression)
					{
						/* Store local variable type */
						Expression leftExpr = ((DeclarationExpression)expr).getLeftExpression();
						
						if(leftExpr instanceof VariableExpression)
						{
							Expression rightExpr = ((DeclarationExpression)expr).getRightExpression();
							ClassNode gType = this.getInferredType(rightExpr);
							
							if(gType != ClassHelper.OBJECT_TYPE)
							{
								String varName = ((VariableExpression) leftExpr).getName();
								this.localVarName2TypeMap.put(varName, gType);
							}
						}
					}
					else if(expr instanceof BinaryExpression)
					{
						this.handleABinaryExpression((BinaryExpression) expr);
					}
				}
				else if(gSubStmt instanceof IfStatement)
				{
					Expression expr = ((IfStatement)gSubStmt).getBooleanExpression().getExpression();
					if(expr instanceof BinaryExpression)
					{
						this.handleABinaryExpression((BinaryExpression) expr);
					}
					
					/* Handle ifBlock and elseBlock of an IfStatement */
					this.handleABlock(((IfStatement) gSubStmt).getIfBlock()); 
					this.handleABlock(((IfStatement) gSubStmt).getElseBlock());
				}
				else if(gSubStmt instanceof WhileStatement)
				{
					Expression expr = ((WhileStatement)gSubStmt).getBooleanExpression().getExpression();
					if(expr instanceof BinaryExpression)
					{
						this.handleABinaryExpression((BinaryExpression) expr);
					}
					
					/* Handle loopBlock of WhileStatement */
					this.handleABlock(((WhileStatement) gSubStmt).getLoopBlock());
				}
				else if(gSubStmt instanceof DoWhileStatement)
				{
					Expression expr = ((DoWhileStatement)gSubStmt).getBooleanExpression().getExpression();
					if(expr instanceof BinaryExpression)
					{
						this.handleABinaryExpression((BinaryExpression) expr);
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
				{}
				else if(gSubStmt instanceof BreakStatement)
				{}
				else if(gSubStmt instanceof BlockStatement)
				{
					this.handleABlock((BlockStatement) gSubStmt);
				}
				else
				{
					System.out.println("[GFieldTypeInferrence.handleABlock] unexpected statement type: " + gSubStmt);
				}
			}
		}
		else if(gStmt instanceof IfStatement)
		{
			Expression expr = ((IfStatement)gStmt).getBooleanExpression().getExpression();
			if(expr instanceof BinaryExpression)
			{
				this.handleABinaryExpression((BinaryExpression) expr);
			}
			
			/* Handle ifBlock and elseBlock of an IfStatement */
			this.handleABlock(((IfStatement) gStmt).getIfBlock()); 
			this.handleABlock(((IfStatement) gStmt).getElseBlock());
		}
		else if(!(gStmt instanceof EmptyStatement))
		{
			System.out.println("[GFieldTypeInferrence.handleABlock] a wrong call!!!");
		}
	}
}
