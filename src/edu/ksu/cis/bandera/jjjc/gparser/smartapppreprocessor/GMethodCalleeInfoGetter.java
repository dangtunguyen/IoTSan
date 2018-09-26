package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.control.SourceUnit;

public class GMethodCalleeInfoGetter extends ClassCodeVisitorSupport {
	/********************************************/
	private Map<String, List<String>> calleeInfo;
	private Set<String> localMethNames;
	/********************************************/

	public GMethodCalleeInfoGetter(Set<String> localMethNames)
	{
		this.localMethNames = localMethNames;
		this.calleeInfo = new HashMap<String, List<String>>();
	}
	
	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}
	
	@Override
	public void visitMethod(MethodNode meth)
	{
		String methName = meth.getName();
		
		if(this.localMethNames.contains(methName))
		{
			if(this.calleeInfo.containsKey(methName))
			{
				System.out.println("Duplicate method: " + methName);
			}
			else
			{
				GCalleeInfoGetter calleeInfoHandler = new GCalleeInfoGetter(this.localMethNames);
				
				meth.getCode().visit(calleeInfoHandler);
				this.calleeInfo.put(methName, calleeInfoHandler.getCallees());
			}
		}
		
		super.visitMethod(meth);
	}
	
	public Map<String, List<String>> getCalleeInfo()
	{
		return this.calleeInfo;
	}
}
