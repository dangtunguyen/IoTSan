package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import java.util.ArrayList;
import java.util.Map;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.control.SourceUnit;

import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;

public class GMethParmTypeAssigner extends ClassCodeVisitorSupport {
	/********************************************/
	Map<String, ArrayList<GParameter>> localMethodMap;
	/********************************************/
	
	public GMethParmTypeAssigner(Map<String, ArrayList<GParameter>> localMethods)
	{
		this.localMethodMap = localMethods;
	}
	
	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}
	
	@Override
	public void visitMethod(MethodNode meth) {
		String methName = meth.getName();
		
		if(this.localMethodMap.containsKey(methName))
		{
			Parameter[] parms = new Parameter[meth.getParameters().length];
			
			if(this.localMethodMap.get(methName).size() == parms.length)
			{
				int i = 0;
				
				for(Parameter parm : meth.getParameters())
				{
					ClassNode parmType = this.getParmType(methName, parm.getName());
					
					if(parmType != null)
					{
						Parameter newPar = new Parameter(parmType, parm.getName());
						parms[i++] = newPar;
					}
					else
					{
						System.out.println("[GMethParmTypeAssigner] error!!! " + methName + ":" + parm);
					}
				}
				meth.setParameters(parms);
			}
			
			if(GUtil.currentSmartAppEvtHandlerList.contains(methName))
			{
				/* Set return type of event handler methods to void */
				meth.setReturnType(ClassHelper.VOID_TYPE);
				/* Instead of using parameter, we declare a local field for STEvent */
				meth.setParameters(new Parameter[0]);
			}
		}
		
		super.visitMethod(meth);
    }
	
	private ClassNode getParmType(String methName, String parmName)
	{
		for(GParameter parm : this.localMethodMap.get(methName))
		{
			if(parm.name.equals(parmName))
			{
				return parm.type;
			}
		}
		
		return null;
	}
}
