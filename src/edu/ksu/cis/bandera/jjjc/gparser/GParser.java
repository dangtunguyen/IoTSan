package edu.ksu.cis.bandera.jjjc.gparser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.ksu.cis.bandera.jjjc.node.*;
import groovy.lang.GroovyShell;
import groovy.lang.MissingMethodException;
import edu.ksu.cis.bandera.jjjc.gparser.g2jconverter.G2JConverter;
import edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor.GSTCommonAPIsProcesser;
import edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor.GSmartAppPreProcessor;
import edu.ksu.cis.bandera.jjjc.gparser.util.GParserException;

import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.control.CompilerConfiguration;

import ca.mcgill.sable.util.LinkedList;


public class GParser {
	/********************************************/
	public static List<MethodNode> gSTCommonAPIs = null;
	public static String project_root;
	private static List<AMethodClassBodyDeclaration> jSTCommonAPIs = null;
	private static List<AFieldClassBodyDeclaration> jSTCommonFields = null;
	private List<AFieldClassBodyDeclaration> jFieldList;
	private Map<String, AMethodClassBodyDeclaration> jMethodMap;
	private String className;
	
	private String fileName;
	/********************************************/
	
	public GParser(String fileName)
	{
		this.fileName = fileName;
	}
	
	public static void init(String project_root)
	{
		GParser.project_root = project_root;
		
		/* Load SmartThings' common APIs from library */
		try {
			GParser.loadGSTCommonAPIs();
		}
		catch(IOException ioe)
		{
			System.out.println("Cannot find STCommonAPIs.groovy" + ioe.toString());
		}
	}
	
	public static void loadGSTCommonAPIs() throws IOException
	{
		CompilerConfiguration CC = new CompilerConfiguration(CompilerConfiguration.DEFAULT);
		GroovyShell gShell;
		File f;
		GSTCommonAPIsProcesser comAPIsProc = new GSTCommonAPIsProcesser();
		
		CC.addCompilationCustomizers(comAPIsProc);
		gShell = new GroovyShell(CC);
		try {
			f = new File(project_root + "/lib/groovy/STCommonAPIs.groovy");
			gShell.evaluate(f);
		} catch(MissingMethodException mme)
		{
			String missingMethod = mme.toString();
			
			if(!missingMethod.contains("definition()") && !missingMethod.contains("main()"))
			{
				System.out.println(missingMethod);
			}
		}
		gSTCommonAPIs = comAPIsProc.getMethodsList();
	}
	
	public static void loadJSTCommonAPIs(Start node)
	{
		if(jSTCommonAPIs == null)
		{
			jSTCommonAPIs = new ArrayList<AMethodClassBodyDeclaration>();
			jSTCommonFields = new ArrayList<AFieldClassBodyDeclaration>();
			
			ACompilationUnit jCompilationUnit = (ACompilationUnit)node.getPCompilationUnit();
			LinkedList classList = jCompilationUnit.getTypeDeclaration();
			
			for(int i = 0; i < classList.size(); i++)
			{
				if(classList.get(i) instanceof AClassTypeDeclaration)
				{
					AClassDeclaration jClass = (AClassDeclaration)((AClassTypeDeclaration) classList.get(i)).getClassDeclaration();
					
					if(jClass.getId().getText().equals("STCommonAPIs"))
					{
						AClassBody jClassBody = (AClassBody)jClass.getClassBody();
						LinkedList jDeclarationList = jClassBody.getClassBodyDeclaration();
						
						for(int j = 0; j < jDeclarationList.size(); j++)
						{
							if(jDeclarationList.get(j) instanceof AMethodClassBodyDeclaration)
							{
								jSTCommonAPIs.add((AMethodClassBodyDeclaration) jDeclarationList.get(j));
							}
							else if(jDeclarationList.get(j) instanceof AFieldClassBodyDeclaration)
							{
								jSTCommonFields.add((AFieldClassBodyDeclaration) jDeclarationList.get(j));
							}
						}
						break;
					}
				}
			}
		}
	}
	
	private AClassBody buildAClassBody()
	{
		AClassBody result;
		LinkedList jDeclarationList = new LinkedList();
		
		/* Add fields to jDeclarationList */
		for(int i = 1; i < jSTCommonFields.size(); i++)
		{
			AFieldClassBodyDeclaration STField = jSTCommonFields.get(i);
			jDeclarationList.add(STField.clone());
		}
		for(AFieldClassBodyDeclaration field : this.jFieldList)
		{
			jDeclarationList.add(field);
		}
		
		/* Add methods to jDeclarationList */
		for(AMethodClassBodyDeclaration STMeth : jSTCommonAPIs)
		{
			jDeclarationList.add(STMeth.clone());
		}
		for(AMethodClassBodyDeclaration meth : this.jMethodMap.values())
		{
			jDeclarationList.add(meth);
		}
		result = new AClassBody(new TLBrace(), jDeclarationList, new TRBrace());
		
		return result;
	}
	private AClassTypeDeclaration buildAClassTypeDeclaration()
	{
		AClassTypeDeclaration result;
		AClassDeclaration jClassDeclaration;
		AClassBody jBody = this.buildAClassBody();
		LinkedList modifierList = new LinkedList();
		
		modifierList.add(new APublicModifier(new TPublic()));
		jClassDeclaration = new AClassDeclaration(modifierList, new TClass(), new TId(this.className),
				null, null, jBody);
		result = new AClassTypeDeclaration(jClassDeclaration);
		
		return result;
	}
	private Start buildStartNode()
	{
		Start result;
		ACompilationUnit jCompilationUnit;
		LinkedList jTypeDeclarationList = new LinkedList();
		EOF jEof = new EOF();
		
		jTypeDeclarationList.add(this.buildAClassTypeDeclaration());
		jCompilationUnit = new ACompilationUnit(null, null, new TSemicolon(),
												new LinkedList(), jTypeDeclarationList);
		result = new Start(jCompilationUnit, jEof);
		
		return result;
	}
	
	/* We generate one class for each SmartApp:
	 * 1. class's name = fileName
	 * 2. methods: EvtHandlers and their used functions
	 * 3. fields: the SmartApp's input info
	 * */
	public Start parse() throws GParserException, IOException
	{
		if((gSTCommonAPIs == null) || (jSTCommonAPIs == null) || (jSTCommonFields == null))
		{
			throw new GParserException("We need to load Groovy and Java SmartThings' APIs before running parse()!!!");
		}
		
		List<String> gClassPath;
		CompilerConfiguration CC = new CompilerConfiguration(CompilerConfiguration.DEFAULT);
		G2JConverter g2jConverter = new G2JConverter();
		GSmartAppPreProcessor gSmartAppPreProcessor = new GSmartAppPreProcessor(gSTCommonAPIs, true);
		GroovyShell gShell;
		File f = new File(this.fileName);
		
		CC.addCompilationCustomizers(gSmartAppPreProcessor, g2jConverter);
		/* Set support libraries */
		gClassPath = CC.getClasspath();
		gClassPath.add(project_root + "/lib/groovy/SmartThings.jar");
		CC.setClasspathList(gClassPath);
				
		gShell = new GroovyShell(CC);
		try {
			gShell.evaluate(f);
		} catch(MissingMethodException mme)
		{
			//missing method on *.definition is fine since it contains
			//nothing related to computing over-privilege
			
			String missingMethod = mme.toString();
			
			if(!missingMethod.contains("definition()"))
			{
				System.out.println(missingMethod);
			}
		}
		this.jFieldList = g2jConverter.getJFieldList();
		this.jMethodMap = g2jConverter.getJMethodMap();
		this.className = g2jConverter.getClassName();

		return this.buildStartNode();
	}
}
