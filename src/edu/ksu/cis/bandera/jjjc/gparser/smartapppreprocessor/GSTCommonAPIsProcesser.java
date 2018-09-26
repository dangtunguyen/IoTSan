package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.classgen.GeneratorContext;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.control.customizers.CompilationCustomizer;

public class GSTCommonAPIsProcesser extends CompilationCustomizer {
	private List<MethodNode> methodsList;
	
	public GSTCommonAPIsProcesser() {
		super(CompilePhase.SEMANTIC_ANALYSIS);
		methodsList = new ArrayList<MethodNode>();
	}

	@Override
	public void call(SourceUnit source, GeneratorContext context, ClassNode classNode) 
	{
		if(classNode.getName().equals("STCommonAPIs"))
		{
			List<String> defaultMeths = Arrays.asList("$getStaticMetaClass", "getMetaClass", "setMetaClass", "invokeMethod", "getProperty", "setProperty");
			
			for(MethodNode meth : classNode.getMethods())
			{
				if(!defaultMeths.contains(meth.getName()))
				{
					this.methodsList.add(meth);
				}
			}
		}
	}
	
	public List<MethodNode> getMethodsList()
	{
		return this.methodsList;
	}
}
