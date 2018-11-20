package edu.ksu.cis.bandera.jjjc.gparser.g2jconverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.control.SourceUnit;

import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;
import edu.ksu.cis.bandera.jjjc.node.*;

public class GMethodConverter extends ClassCodeVisitorSupport {
	/********************************************/
	/* stLocalNames contains all methods' names of SmartThings and Groovy script shell */
	private List<String> stLocalNames;
	private Map<String, AMethodClassBodyDeclaration> jMethodMap;
	/********************************************/
	
	public GMethodConverter(boolean isInstalledNeeded)
	{
		stLocalNames = new ArrayList<String>();
		stLocalNames.addAll(Arrays.asList("definition", "preferences", "installed", "updated", "run", "main", "runScript",
				"methodMissing", "propertyMissing", "setMetaClass", "getProperty", "$getStaticMetaClass"));
		if(isInstalledNeeded)
		{
			stLocalNames.remove("installed");
		}
		jMethodMap = new HashMap<String, AMethodClassBodyDeclaration>();
	}
	
	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}
	
	@Override
	public void visitMethod(MethodNode meth)
	{
		String methName = meth.getName();
		
//		System.out.println(methName);
		
		if (!stLocalNames.contains(methName) && !methName.startsWith("this$") && !methName.startsWith("$static"))
		{
			GMethReturnTypeHandler retTypeHandler = new GMethReturnTypeHandler();
			ClassNode returnType = meth.getReturnType();
			
			/* First trial to infer the return type of method */
			if((returnType == ClassHelper.OBJECT_TYPE) || GUtil.isArrayGenericsTypesNull(returnType))
			{
				meth.getCode().visit(retTypeHandler);
//				returnType = retTypeHandler.getReturnType();
			}
			
			/* Second trial to infer the return type of method */
			if((returnType == ClassHelper.OBJECT_TYPE) || GUtil.isArrayGenericsTypesNull(returnType))
			{
				String varName = retTypeHandler.getVarName();
				
				if(varName != null)
				{
					GVariableTypeInferrence varInfer = new GVariableTypeInferrence(varName);
					
					meth.getCode().visit(varInfer);
					returnType = varInfer.getInferredType();
				}
			}
			
			/* Set return type to void by default */
			if(returnType == ClassHelper.OBJECT_TYPE)
			{
				returnType = ClassHelper.VOID_TYPE;
			}
			
			/* Initialize management variables */
			GStmtBuilder.initializeCommonManagemetVars(returnType);
			
			// takeAction temperatureHandler correctTime switchesOk allQuiet
			if(GUtil.currentSmartAppLocalMethods.contains(methName))
			{
				/* Build a method header */
				PMethodHeader jAMethHeader = GMethodDeclaratorBuilder.buildPMethodHeader(meth, returnType);
				
				if(jAMethHeader != null)
				{
					/* Build a block for the method */
					Statement gStmt = meth.getCode();
					PBlock jABlock = GStmtBuilder.buildABlock(gStmt);
					
					if(jABlock != null)
					{
						/* Create AMethodClassBodyDeclaration */
						ABlockMethodBody jABlockMethodBody = new ABlockMethodBody(jABlock);
						AMethodDeclaration jAMethodDeclaration = new AMethodDeclaration(jAMethHeader, jABlockMethodBody);
						AMethodClassBodyDeclaration jAMethodClassBodyDeclaration = new AMethodClassBodyDeclaration(jAMethodDeclaration);
						
						/* Add the created AMethodClassBodyDeclaration into the map */
						jMethodMap.put(methName, jAMethodClassBodyDeclaration);
						
						System.out.println(jAMethodClassBodyDeclaration);
					}
					else
					{
						System.out.println("[GMethodCoverter.visitMethod] jABlock is null in method " + methName);
					}
				}
				else
				{
					System.out.println("[GMethodCoverter.visitMethod] jAMethHeader is null");
				}
			}
		}
					
		super.visitMethod(meth);
	}
	
	public Map<String, AMethodClassBodyDeclaration> getJMethodMap()
	{
		return this.jMethodMap;
	}
}
