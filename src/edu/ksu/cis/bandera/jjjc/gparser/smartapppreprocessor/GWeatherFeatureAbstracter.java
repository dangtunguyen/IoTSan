package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import java.util.Arrays;
import java.util.List;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.control.SourceUnit;

/* Before: def forecast = json?.forecast?.txt_forecast?.forecastday?.first()
 * After: def forecast = json?.forecast
 * */
public class GWeatherFeatureAbstracter extends ClassCodeVisitorSupport {
	/********************************************/
	private List<String> weatherFeatureAttrNameList = Arrays.asList("forecast", "fcttext");
	/********************************************/
	
	public GWeatherFeatureAbstracter(){}
	
	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}
	
	private Expression processPropertyExpression(PropertyExpression propExpr)
	{
		Expression newExpr = null;
		String propName = propExpr.getPropertyAsString();
		Expression objExpr = propExpr.getObjectExpression();
		
		while(objExpr instanceof PropertyExpression)
		{
			propName = ((PropertyExpression)objExpr).getPropertyAsString();
			objExpr = ((PropertyExpression)objExpr).getObjectExpression();
		}
		
		if(this.weatherFeatureAttrNameList.contains(propName))
		{
			if(objExpr instanceof VariableExpression)
			{
				if(propName.equals("forecast"))
				{
					/* Before: json?.forecast?.txt_forecast?.forecastday
					 * After: json?.forecast
					 * */
					newExpr = new PropertyExpression(objExpr, propName);
				}
				else if(propName.equals("fcttext"))
				{
					/* Before: forecast?.fcttext
					 * After: forecast
					 * */
					newExpr = objExpr;
				}
			}
		}
		
		return newExpr;
	}
	private Expression processExpression(Expression expr)
	{
		Expression newExpr = null;
		
		if(expr instanceof PropertyExpression)
		{
			newExpr = this.processPropertyExpression((PropertyExpression) expr);
		}
		else if(expr instanceof MethodCallExpression)
		{
			newExpr = this.processExpression(((MethodCallExpression)expr).getObjectExpression());
		}
		
		return newExpr;
	}
	private void processBinaryExpression(BinaryExpression bex)
	{
		Expression leftExpr = bex.getLeftExpression();
		Expression rightExpr = bex.getRightExpression();
		
		/* Handle leftExpr */
		if(leftExpr instanceof BinaryExpression)
		{
			this.processBinaryExpression((BinaryExpression)leftExpr);
		}
		else
		{
			Expression newExpr = this.processExpression(leftExpr);
			
			if(newExpr != null)
			{
				bex.setLeftExpression(newExpr);
			}
		}
		
		/* Handle rightExpr */
		if(rightExpr instanceof BinaryExpression)
		{
			this.processBinaryExpression((BinaryExpression)rightExpr);
		}
		else
		{
			Expression newExpr = this.processExpression(rightExpr);
			
			if(newExpr != null)
			{
				bex.setRightExpression(newExpr);
			}
		}
	}
	
	@Override
	public void visitBinaryExpression(BinaryExpression bex)
	{
		this.processBinaryExpression(bex);
		super.visitBinaryExpression(bex);
	}
	
	@Override
	public void visitMethodCallExpression(MethodCallExpression mce) {
		String methName = mce.getMethodAsString();
		
		if(methName != null)
		{
			if(methName.equals("getSunriseAndSunset"))
			{
				/* Before: def s = getSunriseAndSunset(zipCode: zipCode, sunriseOffset: sunriseOffset, sunsetOffset: sunsetOffset)
				 * After: def s = getSunriseAndSunset()
				 * */
				mce.setArguments(MethodCallExpression.NO_ARGUMENTS);
			}
		}
		
        super.visitMethodCallExpression(mce);
    }
}
