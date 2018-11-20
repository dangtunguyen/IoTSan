package edu.ksu.cis.bandera.jjjc.gparser.potentialriskscreener;

import java.util.List;
import java.util.Map;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.classgen.GeneratorContext;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.control.customizers.CompilationCustomizer;
import org.codehaus.groovy.transform.GroovyASTTransformation;

import edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor.GSmartAppPreProcessor;

@GroovyASTTransformation(phase = CompilePhase.INSTRUCTION_SELECTION)
public class GInputInfoExtractor extends CompilationCustomizer {
	/********************************************/
	private Map<String, List<GEventInfo>> outputEvtMap;
	private Map<String, List<GEventInfo>> inputEvtMap;
	/********************************************/

	public GInputInfoExtractor()
	{
		super(CompilePhase.INSTRUCTION_SELECTION);
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
	public void call(SourceUnit source, GeneratorContext context, ClassNode classNode) 
	{
		GInOutputEvtGetter evtGetter;
		List<CompilationCustomizer> ccList = source.getConfiguration().getCompilationCustomizers();
		
		if(ccList.get(0) instanceof GSmartAppPreProcessor)
		{
			GSmartAppPreProcessor preProcessor = (GSmartAppPreProcessor) ccList.get(0);
			
			evtGetter = new GInOutputEvtGetter(preProcessor.getLocalMethSet());
			classNode.visitContents(evtGetter);
			this.outputEvtMap = evtGetter.getOutputEvtMap();
			this.inputEvtMap = evtGetter.getInputEvtMap();
			
			System.out.println("******************************************************");
			System.out.println("Output event map of " + classNode.getName());
			for(Map.Entry<String, List<GEventInfo>> entry : outputEvtMap.entrySet())
			{
				System.out.print(entry.getKey() + ":");
				for(GEventInfo evt : entry.getValue())
				{
					System.out.print(evt.attribute + "." + evt.evtType + ", ");
				}
				System.out.println();
			}
			System.out.println("******************************************************");
			System.out.println("Input event map of " + classNode.getName());
			for(Map.Entry<String, List<GEventInfo>> entry : inputEvtMap.entrySet())
			{
				System.out.print(entry.getKey() + ":");
				for(GEventInfo evt : entry.getValue())
				{
					System.out.print(evt.attribute + "." + evt.evtType + ", ");
				}
				System.out.println();
			}
		}
	}
}
