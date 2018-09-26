package edu.ksu.cis.bandera.jjjc.gparser.potentialriskscreener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.control.SourceUnit;

public class GAppStateChangeDetector extends ClassCodeVisitorSupport {
	/********************************************/
	private List<String> stLocalNames;
	private Map<String, Boolean> localMethAppStateChangedMap;
	/********************************************/
	
	public GAppStateChangeDetector()
	{
		this.stLocalNames = Arrays.asList("definition", "preferences", "installed", "updated", "run", "main", "runScript",
				"methodMissing", "propertyMissing");
		this.localMethAppStateChangedMap = new HashMap<String, Boolean>();
	}
	
	/* Getters */
	public Map<String, Boolean> getLocalMethAppStateChangedMap()
	{
		return this.localMethAppStateChangedMap;
	}
	
	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}
	
	@Override
	public void visitMethod(MethodNode meth) {
		String localMethName = meth.getName();
		
		if(!this.stLocalNames.contains(localMethName))
		{
			GMethAppStateChangeDetector appStateChangeDetector = new GMethAppStateChangeDetector();
			
			meth.getCode().visit(appStateChangeDetector);
			this.localMethAppStateChangedMap.put(localMethName, appStateChangeDetector.getAppStateChanged());
		}
	}
}
