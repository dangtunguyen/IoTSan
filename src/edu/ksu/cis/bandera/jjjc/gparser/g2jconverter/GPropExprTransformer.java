package edu.ksu.cis.bandera.jjjc.gparser.g2jconverter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.codehaus.groovy.syntax.Token;
import org.codehaus.groovy.syntax.Types;
import org.codehaus.groovy.transform.stc.StaticTypesMarker;

import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;

/* Before: result = evt.value == value && onOff[0].value == value
 * After: 
 * STEvent onOffElement0 = onOff[0]
 * result = evt.value == value && onOffElement0.value == value
 * */
public class GPropExprTransformer {
	/********************************************/
	private Map<String, Integer> arrNameIndexMap;
	private Set<String> localMethSet;
	private List<Statement> currentStmtList;
	private int insertPosition;
	/********************************************/
	
	public GPropExprTransformer(Set<String> localMethSet) {
		this.arrNameIndexMap = new HashMap<String, Integer>();
		this.localMethSet = localMethSet;
	}
	
	private String getVarName(BinaryExpression bex)
	{
		String result = "tempArr";
		Expression leftExpr = bex.getLeftExpression();
		
		if(leftExpr instanceof VariableExpression)
		{
			result = ((VariableExpression) leftExpr).getName();
		}
		
		return result;
	}
	
	/* Before: onOff[0].value
	 * After:
	 * STEvent onOffElement0 = onOff[0];
	 * onOffElement0.value 
	 * */
	private ExpressionStatement buildArrVarDeclarationExprStmt(PropertyExpression propExpr)
	{
		ExpressionStatement declExprStmt = null;
		PropertyExpression mostNestedPropExpr = propExpr;
		Expression objExpr = propExpr.getObjectExpression();
		
		while(objExpr instanceof PropertyExpression)
		{
			mostNestedPropExpr = (PropertyExpression) objExpr;
			objExpr = ((PropertyExpression) objExpr).getObjectExpression();
		}
		
		if(GUtil.isExprAnArrayAccess(objExpr))
		{
			ClassNode baseType = objExpr.getNodeMetaData(StaticTypesMarker.INFERRED_TYPE);
			
			if(baseType != null)
			{
				int index = 0;
				String varName = this.getVarName((BinaryExpression) objExpr);
				
				if(this.arrNameIndexMap.containsKey(varName))
				{
					index = this.arrNameIndexMap.get(varName);
				}
				String newVarName = varName + "Element" + index;
				index++;
				this.arrNameIndexMap.put(varName, index);
				
				/* STEvent onOffElement0 = onOff[0]; */
				Expression newVarExpr = new VariableExpression(newVarName, baseType);
				Token assignOp = Token.newSymbol(Types.ASSIGN, -1, -1);
				Expression declExpr = new DeclarationExpression(newVarExpr, assignOp, objExpr);
				declExprStmt = new ExpressionStatement(declExpr);
				
				/* onOffElement0.value */
				mostNestedPropExpr.setObjectExpression(newVarExpr);
			}
		}
		
		return declExprStmt;
	}
	
	private void processExpression(Expression expr)
	{
		if(expr instanceof BinaryExpression)
		{
			processExpression(((BinaryExpression) expr).getLeftExpression());
			processExpression(((BinaryExpression) expr).getRightExpression());
		}
		else if(expr instanceof PropertyExpression)
		{
			ExpressionStatement newExprStmt = this.buildArrVarDeclarationExprStmt((PropertyExpression) expr);
			
			if(newExprStmt != null)
			{
				this.currentStmtList.add(this.insertPosition, newExprStmt);
				this.insertPosition++;
			}
		}
		else if(expr instanceof MethodCallExpression)
		{
			/* Process object expression */
			processExpression(((MethodCallExpression) expr).getObjectExpression());
			
			/* Process arguments */
			{
				List<Expression> argList = GUtil.buildExprList(((MethodCallExpression) expr).getArguments());
				
				for(Expression arg : argList)
				{
					this.processExpression(arg);
				}
			}
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
					this.insertPosition = i;
					this.currentStmtList = gStmtList;
					this.processExpression(expr);
				}
				else if(gSubStmt instanceof IfStatement)
				{
					Expression expr = ((IfStatement) gSubStmt).getBooleanExpression().getExpression();
					this.insertPosition = i;
					this.currentStmtList = gStmtList;
					this.processExpression(expr);
					
					/* Handle ifBlock and elseBlock of an IfStatement */
					this.handleABlock(((IfStatement) gSubStmt).getIfBlock()); 
					this.handleABlock(((IfStatement) gSubStmt).getElseBlock());
				}
				else if(gSubStmt instanceof WhileStatement)
				{
					Expression expr = ((WhileStatement) gSubStmt).getBooleanExpression().getExpression();
					this.insertPosition = i;
					this.currentStmtList = gStmtList;
					this.processExpression(expr);
					
					/* Handle loopBlock of WhileStatement */
					this.handleABlock(((WhileStatement) gSubStmt).getLoopBlock());
				}
				else if(gSubStmt instanceof DoWhileStatement)
				{
					Expression expr = ((WhileStatement) gSubStmt).getBooleanExpression().getExpression();
					this.insertPosition = i;
					this.currentStmtList = gStmtList;
					this.processExpression(expr);
					
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
					this.insertPosition = i;
					this.currentStmtList = gStmtList;
					this.processExpression(expr);
				}
				else if(gSubStmt instanceof BreakStatement)
				{}
				else if(gSubStmt instanceof BlockStatement)
				{
					this.handleABlock(gSubStmt);
				}
				else
				{
					System.out.println("[GPropExprTransformer.handleABlock] unexpected statement type: " + gSubStmt);
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
			System.out.println("[GPropExprTransformer.handleABlock] a wrong call!!!" + gStmt);
		}
	}
}
