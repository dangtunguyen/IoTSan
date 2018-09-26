package edu.ksu.cis.bandera.abstraction.predicate.parser;

/**
 * Insert the type's description here.
 * Creation date: (4/23/01 9:42:38 PM)
 * @author: Administrator
 */
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
public interface Predicate {
	public SootClass  getClassContext();
	public Value getExpr();
	public SootMethod getMethodContext();
	public String getName();
}
