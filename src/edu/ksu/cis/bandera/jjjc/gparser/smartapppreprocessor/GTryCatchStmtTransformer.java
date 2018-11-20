package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import java.util.ArrayList;
import java.util.List;

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
import org.codehaus.groovy.ast.stmt.TryCatchStatement;
import org.codehaus.groovy.ast.stmt.WhileStatement;

/* Before:
 * try {
 * 		runIn(60*minutes,changeIntensity,[overwrite: false])
 * }
 * catch(e)
 * {
 * 		log.debug e
 * }
 * After:
 * runIn(60*minutes,changeIntensity,[overwrite: false])
 * */
public class GTryCatchStmtTransformer {
	public GTryCatchStmtTransformer()
	{}
	
	public void processAClassNode(ClassNode classNode)
	{
		for(MethodNode meth : classNode.getMethods())
		{
			this.handleABlock(meth.getCode());
		}
	}
	
	/* Before:
	 * try {
	 * 		runIn(60*minutes,changeIntensity,[overwrite: false])
	 * }
	 * catch(e)
	 * {
	 * 		log.debug e
	 * }
	 * After:
	 * runIn(60*minutes,changeIntensity,[overwrite: false])
	 * */
	private void handleABlock(Statement gStmt)
	{
		if(gStmt instanceof BlockStatement)
		{
			List<Statement> gStmtList = ((BlockStatement)gStmt).getStatements();
			
			for(int i = 0; i < gStmtList.size(); i++)
			{
				Statement gSubStmt = gStmtList.get(i);
				
				if(gSubStmt instanceof TryCatchStatement)
				{
					List<Statement> tryStmtList = new ArrayList<Statement>();
					Statement tryStmt = ((TryCatchStatement)gSubStmt).getTryStatement();
					
					if(tryStmt instanceof BlockStatement)
					{
						tryStmtList.addAll(((BlockStatement)tryStmt).getStatements());
					}
					else
					{
						tryStmtList.add(tryStmt);
					}
					/* Add tryStmtList to gStmtList */
					gStmtList.addAll(i, tryStmtList);
					
					/* Remove the TryCatchStatement */
					gStmtList.remove(gSubStmt);
				}
				else if(gSubStmt instanceof ExpressionStatement)
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
					this.handleABlock((BlockStatement) gSubStmt);
				}
				else
				{
					System.out.println("[GTryCatchStmtTransformer.handleABlock] unexpected statement type: " + gSubStmt);
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
			System.out.println("[GTryCatchStmtTransformer.handleABlock] a wrong call!!!");
		}
	}
}
