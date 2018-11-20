package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.control.SourceUnit;

import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;

public class GArgTypeInferenceFromMethCallVarExpr extends ClassCodeVisitorSupport{
	/********************************************/
	private Map<String, ArrayList<GParameter>> localMethods;
	private Map<String, List<String>> callerInfo;
	private Map<String, List<String>> varNames;
	/********************************************/
	
	public GArgTypeInferenceFromMethCallVarExpr(Map<String, ArrayList<GParameter>> localMethods, 
			Map<String, List<String>> callerInfo, Map<String, List<String>> varNames)
	{
		this.localMethods = localMethods;
		this.callerInfo = callerInfo;
		this.varNames = varNames;
	}
	
	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}
	
	@Override
	public void visitMethod(MethodNode meth)
	{
		String methName = meth.getName();
		
		if(GUtil.containsCaller(this.callerInfo, methName))
		{
			GTypeInferenceFromVarExpr typeInfer = new GTypeInferenceFromVarExpr(this.localMethods, this.varNames);
			
			meth.getCode().visit(typeInfer);
			this.localMethods = typeInfer.getLocalMethods();
		}
		
		super.visitMethod(meth);
	}
	
	public Map<String, ArrayList<GParameter>> getLocalMethods()
	{
		return this.localMethods;
	}
}
