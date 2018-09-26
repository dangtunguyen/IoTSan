package edu.ksu.cis.bandera.jjjc.gparser.g2jconverter;

import java.util.Set;

import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.control.SourceUnit;

/* Before: 
 * def doorOpenTooLong() {
 * ...
 * doorOpenTooLong()
 * ...
 * }
 * After:
 * def doorOpenTooLong() {
 * ...
 * default_null_method()
 * ...
 * }
 * */
public class GRecursiveCallRemoval extends ClassCodeVisitorSupport {
	/********************************************/
	private Set<String> localMethSet;
	/********************************************/
	
	public GRecursiveCallRemoval(Set<String> localMethSet)
	{
		this.localMethSet = localMethSet;
	}
	
	@Override
	protected SourceUnit getSourceUnit() {
		return null;
	}
	
	@Override
	public void visitMethod(MethodNode meth)
	{
		String methName = meth.getName();
		
		if(this.localMethSet.contains(methName))
		{
			GMethRecursiveCallRemoval removal = new GMethRecursiveCallRemoval(methName);
			
			meth.getCode().visit(removal);
		}
		
		super.visitMethod(meth);
	}
}
