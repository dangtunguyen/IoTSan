package edu.ksu.cis.bandera.jjjc.gparser.g2jconverter;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.FieldNode;

import ca.mcgill.sable.util.LinkedList;
import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;
import edu.ksu.cis.bandera.jjjc.node.*;

public class GFieldDeclarationBuilder {
	
	public static AFieldClassBodyDeclaration build(FieldNode gField)
	{
		AFieldClassBodyDeclaration jFieldClassBodyDeclaration = null;
		Node jType;
		
		if(GUtil.isAMapType(gField.getType()))
		{
			jType = GTypeConverter.createAStateMapType();
		}
		else
		{
			jType = GTypeConverter.convert(gField.getType());
		}
		
		if(jType != null)
		{
			AFieldDeclaration jFieldDeclaration;
			PVariableDeclarator jVariableDeclarator = GVarDeclaratorBuilder.buildAVariableDeclarator(gField.getName());
			LinkedList VarDeclList = new LinkedList();
			LinkedList modifierList = new LinkedList();
			
			VarDeclList.add(jVariableDeclarator);
			modifierList.add(new AStaticModifier(new TStatic()));
			jFieldDeclaration = new AFieldDeclaration(modifierList, 
					(PType) jType, VarDeclList, (new TSemicolon()));
			jFieldClassBodyDeclaration = new AFieldClassBodyDeclaration(jFieldDeclaration);
		}
		else
		{
			System.out.println("[GFieldDeclarationBuilder][build] unexpected field type " + gField.getType());
		}
		
		return jFieldClassBodyDeclaration;
	}
	
	public static List<AFieldClassBodyDeclaration> build(ClassNode gClass)
	{
		List<AFieldClassBodyDeclaration> result = new ArrayList<AFieldClassBodyDeclaration>();
		
		for(FieldNode gField : gClass.getFields())
		{
			AFieldClassBodyDeclaration jFieldClassBodyDeclaration = build(gField);
			
			if(jFieldClassBodyDeclaration != null)
			{
				result.add(jFieldClassBodyDeclaration);
			}
		}
		
		return result;
	}
}
