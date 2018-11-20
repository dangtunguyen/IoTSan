package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.control.SourceUnit;

/* This class is used to discover methods that are used to get input info
 * from user
 * */
public class GInputMethDiscovery extends ClassCodeVisitorSupport {
	/********************************************/
	private List<String> inputMethList;
	/* stLocalNames contains all methods' names of SmartThings and Groovy script shell */
	private List<String> stLocalNames;
	/********************************************/
	
	public GInputMethDiscovery(){
		this.inputMethList = new ArrayList<String>();
		stLocalNames = new ArrayList<String>();
		stLocalNames.addAll(Arrays.asList("definition", "preferences", "installed", "updated", "run", "main", "runScript",
				"methodMissing", "propertyMissing"));
	}
	
	/* Getters */
	public List<String> getInputMethList()
	{
		return this.inputMethList;
	}
	
	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}
	
	@Override
	public void visitMethod(MethodNode meth) {
		if(!this.stLocalNames.contains(meth.getName()))
		{
			GMethInputMethDiscovery inputMethDiscovery = new GMethInputMethDiscovery();
			
			meth.getCode().visit(inputMethDiscovery);
			if(inputMethDiscovery.isInputMethUsed())
			{
				this.inputMethList.add(meth.getName());
			}
		}
		
		super.visitMethod(meth);
    }
}
