package edu.ksu.cis.bandera.jjjc.gparser.potentialriskscreener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.control.SourceUnit;

import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;


public class GInOutputEvtGetter extends ClassCodeVisitorSupport {
	/********************************************/
	private Set<String> localMethodSet;
	private Map<String, List<GEventInfo>> outputEvtMap;
	private Map<String, List<GEventInfo>> inputEvtMap;
	/********************************************/
	
	public GInOutputEvtGetter(Set<String> localMethodSet)
	{
		this.localMethodSet = localMethodSet;
		this.outputEvtMap = new HashMap<String, List<GEventInfo>>();
		this.inputEvtMap = new HashMap<String, List<GEventInfo>>();
	}
	
	/* Getters */
	public Map<String, List<GEventInfo>> getOutputEvtMap()
	{
		return this.outputEvtMap;
	}
	public Map<String, List<GEventInfo>> getInputEvtMap()
	{
		return this.inputEvtMap;
	}
	
	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}
	
	@Override
	public void visitMethod(MethodNode meth)
	{
		String localMethName = meth.getName();
		
		if(this.localMethodSet.contains(localMethName))
		{
			GMethInOutputEvtGetter evtGetter = new GMethInOutputEvtGetter();
			List<GEventInfo> outputEvtList;
			List<GEventInfo> inputEvtList;
			Set<String> typeUnknownVarNameSet = new HashSet<String>();
			Map<String, String> outputVarNameEvtTypeMap;
			Set<String> inputTypeUnknownVarSet;
			
			meth.getCode().visit(evtGetter);
			outputEvtList = evtGetter.getOutputEvtList();
			inputEvtList = evtGetter.getInputEvtList();
			outputVarNameEvtTypeMap = evtGetter.getOutputVarNameEvtTypeMap();
			inputTypeUnknownVarSet = evtGetter.getInputTypeUnknownVarSet();
			
			/* Check if we have some type-unknown variables */
			if(evtGetter.getOutputVarNameEvtTypeMap().size() > 0)
			{
				typeUnknownVarNameSet.addAll(outputVarNameEvtTypeMap.keySet());
			}
			if(inputTypeUnknownVarSet.size() > 0)
			{
				typeUnknownVarNameSet.addAll(inputTypeUnknownVarSet);
			}
			
			if(typeUnknownVarNameSet.size() > 0)
			{
				GVarTypeInference typeInfer = new GVarTypeInference(typeUnknownVarNameSet);
				Map<String, String> outputVarNameTypeMap;
				
				/* We need to do type inference for those variables */
				meth.getCode().visit(typeInfer);
				outputVarNameTypeMap = typeInfer.getVarNameTypeMap();
				
				/* Output events */
				if(evtGetter.getOutputVarNameEvtTypeMap().size() > 0)
				{
					for(Map.Entry<String, String> entry : outputVarNameEvtTypeMap.entrySet())
					{
						/* Check if we can infer the type */
						if(outputVarNameTypeMap.containsKey(entry.getKey()))
						{
							String attribute = GUtil.getAttributeFromDeviceType(outputVarNameTypeMap.get(entry.getKey()));
							
							if(attribute != null)
							{
								outputEvtList.add(new GEventInfo(attribute, entry.getValue()));
							}
						}
					}
				}
				
				/* Input events */
				if(inputTypeUnknownVarSet.size() > 0)
				{
					for(String varName : inputTypeUnknownVarSet)
					{
						/* Check if we can infer the type */
						if(outputVarNameTypeMap.containsKey(varName))
						{
							String attribute = GUtil.getAttributeFromDeviceType(outputVarNameTypeMap.get(varName));
							
							if(attribute != null)
							{
								inputEvtList.add(new GEventInfo(attribute, ""));
							}
						}
					}
				}
			}
			this.outputEvtMap.put(localMethName, outputEvtList);
			this.inputEvtMap.put(localMethName, inputEvtList);
		}
		super.visitMethod(meth);
	}
}
