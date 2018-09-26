package edu.ksu.cis.bandera.jjjc.gparser.g2jconverter;

import org.codehaus.groovy.ast.ClassNode;

import edu.ksu.cis.bandera.jjjc.node.PType;

public class GVarTypeContainer {
	public ClassNode gType;
	public PType jType;
	
	public GVarTypeContainer(ClassNode gType, PType jType)
	{
		this.gType = gType;
		this.jType = jType;
	}
}
