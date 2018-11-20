package edu.ksu.cis.bandera.jjjc.gparser.g2jconverter;

import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.Parameter;

import ca.mcgill.sable.util.LinkedList;
import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;
import edu.ksu.cis.bandera.jjjc.node.*;

public class GMethodDeclaratorBuilder {
	public static LinkedList buildParameters(Parameter[] gParms)
	{
		LinkedList paramList = new LinkedList();
		
		for(Parameter gParam : gParms)
		{
			PFormalParameter jParam = GUtil.newAFormalParameter(gParam.getName(),(PType) GTypeConverter.convert(gParam.getType()));
			paramList.add(jParam);
		}
		return paramList;
	}
	
	public static AMethodDeclarator buildAMethodDeclarator(String methName, Parameter[] gParms)
	{
		TId tId = new TId(methName);

		return new AMethodDeclarator(tId, new TLPar(), GMethodDeclaratorBuilder.buildParameters(gParms), new TRPar(), new LinkedList());
	}
	
	public static PModifier newAPublicModifier()
	{
		APublicModifier node = new APublicModifier(new TPublic());
		return node;
	}
	public static PModifier newAPrivateModifier()
	{
		APrivateModifier node = new APrivateModifier(new TPrivate());
		return node;
	}
	public static PModifier newAProtectedModifier()
	{
		AProtectedModifier node = new AProtectedModifier(new TProtected());
		return node;
	}
	public static PModifier newAStaticModifier()
	{
		AStaticModifier node = new AStaticModifier(new TStatic());
		return node;
	}
	public static PModifier newAAbstractModifier()
	{
		AAbstractModifier node = new AAbstractModifier(new TAbstract());
		return node;
	}
	public static PModifier newAFinalModifier()
	{
		AFinalModifier node = new AFinalModifier(new TFinal());
		return node;
	}
	public static LinkedList buildModifierList(MethodNode meth)
	{
		LinkedList modifierList = new LinkedList();
		
		if(meth.isPublic())
		{
			modifierList.add(GMethodDeclaratorBuilder.newAPublicModifier());
		}
		else if(meth.isPrivate())
		{
			modifierList.add(GMethodDeclaratorBuilder.newAPrivateModifier());
		}
		else if(meth.isProtected())
		{
			modifierList.add(GMethodDeclaratorBuilder.newAProtectedModifier());
		}
		
		if(meth.isStatic())
		{
			modifierList.add(GMethodDeclaratorBuilder.newAStaticModifier());
		}
		
		if(meth.isAbstract())
		{
			modifierList.add(GMethodDeclaratorBuilder.newAAbstractModifier());
		}
		
		if(meth.isFinal())
		{
			modifierList.add(GMethodDeclaratorBuilder.newAFinalModifier());
		}
		
		return modifierList;
	}
	
	public static PMethodHeader buildPMethodHeader(MethodNode meth, ClassNode returnType)
	{
		PThrows node4 = null;
		PMethodDeclarator node3 = (PMethodDeclarator) GMethodDeclaratorBuilder.buildAMethodDeclarator(
				GUtil.getStandardEvtHandlerName(meth.getName()), meth.getParameters());	
		LinkedList node1 = GMethodDeclaratorBuilder.buildModifierList(meth);
		PMethodHeader node;
		
		if(returnType == ClassHelper.VOID_TYPE)
		{
			node = new AVoidMethodHeader(node1, new TVoid(), node3, node4);
		}
		else
		{
			PType node2 = (PType) GTypeConverter.convert(returnType);
			node = new ATypedMethodHeader(node1, node2, node3, node4);
		}
		
		return node;
	}
}
