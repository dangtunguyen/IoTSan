package edu.ksu.cis.bandera.pdgslicer.datastructure;

/**
 * Insert the type's description here.
 * Creation date: (00-11-22 14:58:57)
 * @author: 
 */
public class SliceStatement extends SlicePoint {
/**
 * SliceStatement constructor comment.
 * @param sootClass ca.mcgill.sable.soot.SootClass
 * @param sootMethod ca.mcgill.sable.soot.SootMethod
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 * @param in int
 */
public SliceStatement(ca.mcgill.sable.soot.SootClass sootClass, ca.mcgill.sable.soot.SootMethod sootMethod, ca.mcgill.sable.soot.jimple.Stmt stmt, int in) {
	super(sootClass, sootMethod, stmt, in);
}
/**
 * Insert the method's description here.
 * Creation date: (00-11-22 17:12:56)
 * @return java.lang.String
 */
public String toString() {
	return super.toString();
}
}
