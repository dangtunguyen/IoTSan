package edu.ksu.cis.bandera.jjjc.gparser.g2jconverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
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

/* Before:
 * { 
 * 		aMap.put("test", "hello world")
 * }
 * After:
 * aMap.put("test", "hello world")
 * */
public class GBlockUnroller {
	/********************************************/
	private Set<String> localMethSet;
	/********************************************/

	public GBlockUnroller(Set<String> localMethSet)
	{
		this.localMethSet = localMethSet;
	}

	public void processAClassNode(ClassNode classNode)
	{
		/* Remove null expressions in all methods */
		for(MethodNode meth : classNode.getMethods())
		{
			if(this.localMethSet.contains(meth.getName()))
			{
				this.handleABlock(meth.getCode());
			}
		}
	}

	/* Before:
	 * { 
	 * 		aMap.put("test", "hello world")
	 * }
	 * After:
	 * aMap.put("test", "hello world")
	 * */
	private void handleABlock(Statement gStmt)
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
					this.handleABlock(((IfStatement) gSubStmt).getIfBlock()); 
					this.handleABlock(((IfStatement) gSubStmt).getElseBlock());
				}
				else if(gSubStmt instanceof WhileStatement)
				{
					/* Handle loopBlock of WhileStatement */
					this.handleABlock(((WhileStatement) gSubStmt).getLoopBlock());
				}
				else if(gSubStmt instanceof DoWhileStatement)
				{
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
					int insertPos;
					/* Remove current block statement */
					gStmtList.remove(i);
					
					/* Add sub-statements of gSubStmt to gStmtList*/
					insertPos = 0;
					for(Statement gSubSubStmt : ((BlockStatement)gSubStmt).getStatements())
					{
						gStmtList.add(i+insertPos, gSubSubStmt);
						insertPos++;
					}
				}
				else
				{
					System.out.println("[GBlockUnroller.handleABlock] unexpected statement type: " + gSubStmt);
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
			System.out.println("[GBlockUnroller.handleABlock] a wrong call!!!" + gStmt);
		}
	}
	
	public static void main(String[] args)
	{
		List<Integer> list = new ArrayList<Integer>();
		list.addAll(Arrays.asList(1,2,3));

		for(int i = 0; i < list.size(); i++)
		{
			if(i == 1)
			{
				list.remove(i);
				list.add(i, 4);
				list.add(i, 5);
			}
		}
		for(int i = 0; i < list.size(); i++)
		{
			System.out.println(list.get(i));
		}
	}
}
