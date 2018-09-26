package edu.ksu.cis.bandera.jjjc.gparser.g2jconverter;

import java.util.List;
import java.util.Map;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.classgen.GeneratorContext;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.control.customizers.CompilationCustomizer;
import org.codehaus.groovy.transform.GroovyASTTransformation;

import edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor.GSmartAppPreProcessor;
import edu.ksu.cis.bandera.jjjc.node.AFieldClassBodyDeclaration;
import edu.ksu.cis.bandera.jjjc.node.AMethodClassBodyDeclaration;

@GroovyASTTransformation(phase = CompilePhase.INSTRUCTION_SELECTION)
public class G2JConverter extends CompilationCustomizer {
	private List<AFieldClassBodyDeclaration> jFieldList;
	private Map<String, AMethodClassBodyDeclaration> jMethodMap;
	private String className;
	
	public G2JConverter() {
		super(CompilePhase.INSTRUCTION_SELECTION);
	}

	@Override
	public void call(SourceUnit source, GeneratorContext context, ClassNode classNode) 
	{
		List<CompilationCustomizer> ccList = context.getCompileUnit().getConfig().getCompilationCustomizers();
		
		if(ccList.get(0) instanceof GSmartAppPreProcessor)
		{
			GMethodConverter methConverter;
			GDateAttrAbstracter dateAttrAbstracter = new GDateAttrAbstracter();
			GMapGetExprTransformer mapGetExprTransformer = new GMapGetExprTransformer();
			GBlockUnroller blockUnroller = new GBlockUnroller(
					((GSmartAppPreProcessor)ccList.get(0)).getLocalMethSet());
			GMapPutExprTransformer mapPutExprTransformer = new GMapPutExprTransformer(
					((GSmartAppPreProcessor)ccList.get(0)).getLocalMethSet());
			GPropertyNameAbstracter attrAsbtracter = new GPropertyNameAbstracter();
			GDateMethodAbstracter dateMethAbstracter = new GDateMethodAbstracter();
			GRecursiveCallRemoval recursiveCallRemoval = new GRecursiveCallRemoval(
					((GSmartAppPreProcessor)ccList.get(0)).getLocalMethSet());
			GListExprTransformer listExprTransformer = new GListExprTransformer(
					((GSmartAppPreProcessor)ccList.get(0)).getLocalMethSet());
			GPropExprTransformer propExprTransformer = new GPropExprTransformer(
					((GSmartAppPreProcessor)ccList.get(0)).getLocalMethSet());
			GRedundantPropertyRemoval redundantPropRemoval = new GRedundantPropertyRemoval();
			
			/* Abstract out date attributes */
			classNode.visitContents(dateAttrAbstracter);
			
			/* Remove redundant property such as "time" */
			classNode.visitContents(redundantPropRemoval);
			
			/* Abstract out the property/attribute names */
			classNode.visitContents(attrAsbtracter);
			
			/* Unroll nested blocks */
			blockUnroller.processAClassNode(classNode);
			
			/* Transform map "put" expressions */
			mapPutExprTransformer.processAClassNode(classNode);
			
			/* Transform map "get" expressions */
			classNode.visitContents(mapGetExprTransformer);
			
			/* Abstract out date methods */
			classNode.visitContents(dateMethAbstracter);
			
			/* Remove recursive calls */
			classNode.visitContents(recursiveCallRemoval);
			
			/* Transform list expressions */
			listExprTransformer.processAClassNode(classNode);
			
			/* Transform property expressions */
			propExprTransformer.processAClassNode(classNode);
			
			/* Get list of map variable's key names */
			{
				GMapKeyNameGetter mapKeyNameGetter = new GMapKeyNameGetter();
				classNode.visitContents(mapKeyNameGetter);
				
				/* Add the result list to inputEnumList */
				for(String keyName : mapKeyNameGetter.getMapKeyNameList())
				{
					if(!GLiteralBuilder.inputEnumList.contains(keyName))
					{
						GLiteralBuilder.inputEnumList.add(keyName);
					}
				}
			}
			
			methConverter = new GMethodConverter(((GSmartAppPreProcessor)ccList.get(0)).isInstalledMethNeeded());
			classNode.visitContents(methConverter);
			this.jMethodMap = methConverter.getJMethodMap();
			this.jFieldList = GFieldDeclarationBuilder.build(classNode);
			this.className = classNode.getName();
		}
	}
	
	public List<AFieldClassBodyDeclaration> getJFieldList()
	{
		return this.jFieldList;
	}
	public Map<String, AMethodClassBodyDeclaration> getJMethodMap()
	{
		return this.jMethodMap;
	}
	public String getClassName()
	{
		return this.className;
	}
}
