package edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor;

import org.codehaus.groovy.ast.ClassNode;

public class GParameter {
	public String name;
	public ClassNode type;
    
    public GParameter(String name, ClassNode type)
    {
    		this.type = type;
    		this.name = name;
    }
}
