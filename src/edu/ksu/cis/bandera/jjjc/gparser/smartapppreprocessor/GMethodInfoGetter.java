package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.control.SourceUnit;
import groovy.transform.CompileStatic;


/* This class is used to get names and parameters of all local methods in
 * a block of source code.
 * */
@CompileStatic
public class GMethodInfoGetter extends ClassCodeVisitorSupport {
	/********************************************/
	private Map<String, ArrayList<GParameter>> localMethods;
	/* stLocalNames contains all methods' names of SmartThings and Groovy script shell */
	private List<String> stLocalNames;
	/********************************************/
	
	public GMethodInfoGetter(boolean isInstalledNeeded)
	{
		stLocalNames = new ArrayList<String>();
		stLocalNames.addAll(Arrays.asList("definition", "preferences", "installed", "updated", "run", "main", "runScript",
				"methodMissing", "propertyMissing"));
		if(isInstalledNeeded)
		{
			stLocalNames.remove("installed");
		}
		localMethods = new HashMap<String, ArrayList<GParameter>>();
	}

	@Override
	public void visitMethod(MethodNode meth)
	{
		String methName = meth.getName();
		
		if (!stLocalNames.contains(methName) && !methName.startsWith("this$") && !methName.startsWith("$static"))
		{
			GInputCallChecker inputCallChecker = new GInputCallChecker();
			
			meth.getCode().visit(inputCallChecker);
			if(!inputCallChecker.isInputMethCalled())
			{
				if(localMethods.containsKey(methName))
				{
					System.out.println("Duplicate method: " + methName);
				}
				else
				{
					Parameter[] parms = meth.getParameters();
					ArrayList<GParameter> gParms = new ArrayList<GParameter>();
					
					for(Parameter parm : parms)
					{
						GParameter gParm = new GParameter(parm.getName(), parm.getType());
						gParms.add(gParm);
					}
					localMethods.put(methName, gParms);
				}
			}
		}
					
		super.visitMethod(meth);
	}
	
	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}

	public Map<String, ArrayList<GParameter>> getLocalMethods()
	{
		return this.localMethods;
	}
}