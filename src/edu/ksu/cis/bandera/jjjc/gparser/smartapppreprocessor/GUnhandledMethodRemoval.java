package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import org.codehaus.groovy.control.SourceUnit;

import java.util.Arrays;
import java.util.List;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;

/* Before: runIn(minutes * 60, scheduleCheck, [overwrite: false])
 * After: scheduleCheck()
 * */
public class GUnhandledMethodRemoval extends ClassCodeVisitorSupport {
	/********************************************/
	private List<String> UnhandledMethodList;
	/********************************************/

	public GUnhandledMethodRemoval() {
		UnhandledMethodList = Arrays.asList("subscribe", "subscribeToCommand", "sendNotificationToContacts",
				"sendPush", "sendSms", "unschedule", "poll", "refresh", "sendNotification", "sendNotificationEvent",
				"sendHubCommand", "debug", "execute", "setTimeZone"); /* "unsubscribe", "schedule", */
	}

	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}

	@Override
	public void visitMethodCallExpression(MethodCallExpression mce) {
		String methText;
		Expression objExpr = mce.getObjectExpression();

		if(mce.getMethodAsString() == null)
		{
			//dynamic methods
			methText = mce.getText();
		}
		else
		{
			methText = mce.getMethodAsString();
		}

		if(methText.equals("unsubscribe"))
		{
			mce.setArguments(MethodCallExpression.NO_ARGUMENTS);
		}
		else if(UnhandledMethodList.contains(methText))
		{
			/* Replace the current method call with a default null method call */
			ConstantExpression newMethod = new ConstantExpression("default_null_method");
			VariableExpression newObjExpr = new VariableExpression("this");

			mce.setObjectExpression(newObjExpr);
			mce.setMethod(newMethod);
			mce.setArguments(MethodCallExpression.NO_ARGUMENTS);
		}
		else if(objExpr instanceof VariableExpression)
		{
			if(((VariableExpression) objExpr).getName().equals("log"))
			{
				/* Replace the current method call with a default null method call */
				ConstantExpression newMethod = new ConstantExpression("default_null_method");
				VariableExpression newObjExpr = new VariableExpression("this");

				mce.setObjectExpression(newObjExpr);
				mce.setMethod(newMethod);
				mce.setArguments(MethodCallExpression.NO_ARGUMENTS);
			}
		}

		super.visitMethodCallExpression(mce);
	}

	/* Before: log.debug 
	 * After: default_null_method()
	 * */
	@Override
	public void visitExpressionStatement(ExpressionStatement statement) {
		Expression expr = statement.getExpression();
		
		if(expr instanceof PropertyExpression)
		{
			Expression objExpr = ((PropertyExpression) expr).getObjectExpression();
			
			if(objExpr instanceof VariableExpression)
			{
				if(((VariableExpression) objExpr).getName().equals("log"))
				{
					/* Replace the current method call with a default null method call */
					MethodCallExpression mce = new MethodCallExpression(
							new VariableExpression("this"),
							"default_null_method",
							MethodCallExpression.NO_ARGUMENTS
							);
					statement.setExpression(mce);
				}
			}
		}
		
		super.visitExpressionStatement(statement);
	}
}
