package edu.ksu.cis.bandera.pdgslicer.datastructure;

import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;

public class SliceLocal extends SliceVariable {
	private SootMethod sm;
	private Local local;
/**
 * 
 * @param sootClass ca.mcgill.sable.soot.SootClass
 * @param sootMethod ca.mcgill.sable.soot.SootMethod
 * @param local ca.mcgill.sable.soot.jimple.Local
 */
public SliceLocal(SootClass sootClass, SootMethod sootMethod, Local local) {
	super(sootClass);
	this.sm = sootMethod;
	this.local = local;
}
/**
 * 
 * @return boolean
 * @param object java.lang.Object
 */
public boolean equals(Object object) {
	if (object instanceof SliceLocal) {
		SliceLocal sloc = (SliceLocal) object;
		return local.equals(sloc.getLocal()) && sm==sloc.getSootMethod() && sc==sloc.getSootClass();
	} else {
		return false;
	}
}
/**
 * 
 * @return ca.mcgill.sable.soot.jimple.Local
 */
public Local getLocal() {
	return local;
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
 * @return int
 */
public int hashCode() {
	return local.hashCode();
}
/**
 * Insert the method's description here.
 * Creation date: (00-7-2 22:43:03)
 * @param loc ca.mcgill.sable.soot.jimple.Local
 */
public void setLocal(Local loc) {
	local = loc;
}
/**
 * Insert the method's description here.
 * Creation date: (00-7-2 22:43:45)
 * @param smd ca.mcgill.sable.soot.SootMethod
 */
public void setSootMethod(SootMethod smd) {
	sm = smd;
}
/**
 * 
 * @return java.lang.String
 */
public String toString() {
	return local.toString()+" :: " + getSootMethod();
}
}
