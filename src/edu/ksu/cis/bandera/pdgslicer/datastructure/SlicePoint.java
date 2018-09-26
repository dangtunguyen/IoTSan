package edu.ksu.cis.bandera.pdgslicer.datastructure;

import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import java.util.*;

public class SlicePoint extends SliceInterest {
	protected SootMethod sm;
	protected Stmt stmt;
	protected int index;
/**
 * 
 * @param sootClass ca.mcgill.sable.soot.SootClass
 * @param sootMethod ca.mcgill.sable.soot.SootMethod
 * @param stmt ca.mcgill.sable.soot.jimple.Stmt
 */
public SlicePoint(SootClass sootClass, SootMethod sootMethod, Stmt stmt, int in) {
	super(sootClass);
	this.sm = sootMethod;
	this.stmt = stmt;
	this.index = in;
}
/**
 * 
 * @return boolean
 * @param object java.lang.Object
 */
public boolean equals(Object object) {
	if (object instanceof SlicePoint) {
		SlicePoint sp = (SlicePoint) object;
		return stmt.equals(sp.getStmt()) && sm==sp.getSootMethod() && sc==sp.getSootClass();
	} else {
		return false;
	}
}
/**
 * Insert the method's description here.
 * Creation date: (00-7-2 22:27:28)
 * @return int
 */
public int getIndex() {
	return index;
}
/**
 * 
 * @return ca.mcgill.sable.soot.SootMethod
 */
public SootMethod getSootMethod() {
	return sm;
}
/**
 * 
 * @return ca.mcgill.sable.soot.jimple.Stmt
 */
public Stmt getStmt() {
	return stmt;
}
/**
 * Insert the method's description here.
 * Creation date: (00-7-4 12:32:04)
 * @return int
 * @param sootMethod ca.mcgill.sable.soot.SootMethod
 * @param st ca.mcgill.sable.soot.jimple.Stmt
 */
public static int getStmtIndex(SootMethod sootMethod, Stmt st) {
	JimpleBody jimpleBody = (JimpleBody) sootMethod.getBody(Jimple.v());
	StmtList stmtList = jimpleBody.getStmtList();
	int ind = stmtList.indexOf(st);
	return ind;
}
/**
 * 
 * @return int
 */
public int hashCode() {
	return stmt.hashCode();
}
/**
 * Insert the method's description here.
 * Creation date: (00-7-2 22:44:34)
 * @param in int
 */
public void setIndex(int in) {
	index = in;
}
/**
 * Insert the method's description here.
 * Creation date: (00-7-2 22:45:39)
 * @param smm ca.mcgill.sable.soot.SootMethod
 */
public void setSootMethod(SootMethod smm) {
	sm = smm;
}
/**
 * Insert the method's description here.
 * Creation date: (00-7-2 22:45:04)
 * @param s ca.mcgill.sable.soot.jimple.Stmt
 */
public void setStmt(Stmt s) {
	stmt = s;
}
/**
 * 
 * @return java.lang.String
 */
public String toString() {
	return stmt.toString()+" :: " + getSootMethod();
}
}
