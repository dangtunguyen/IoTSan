package edu.ksu.cis.bandera.jjjc.gparser.g2jconverter;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.stc.StaticTypesMarker;

public class GVariableTypeInferrence extends ClassCodeVisitorSupport {
	/********************************************/
	private String varName;
	private ClassNode inferredType;
	/********************************************/
	
	public GVariableTypeInferrence(String varName)
	{
		this.varName = varName;
		this.inferredType = ClassHelper.int_TYPE;
	}
	
	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}
	
	@Override
	public void visitVariableExpression(VariableExpression expr) {
		if(expr.getName().equals(this.varName) && (expr.getNodeMetaData(StaticTypesMarker.INFERRED_TYPE) != null)
				&& (this.inferredType == ClassHelper.int_TYPE))
		{
			this.inferredType = expr.getNodeMetaData(StaticTypesMarker.INFERRED_TYPE);
		}	
		super.visitVariableExpression(expr);
    }
	
	public ClassNode getInferredType()
	{
		return this.inferredType;
	}
}
