package edu.ksu.cis.bandera.pdgslicer.datastructure;

import ca.mcgill.sable.soot.*;
public abstract class SliceInterest implements java.io.Serializable {
	protected SootClass sc;
/**
 * Insert the method's description here.
 * Creation date: (00-11-22 16:50:46)
 */
public SliceInterest() {}
/**
 * 
 * @param sootClass ca.mcgill.sable.soot.SootClass
 */
public SliceInterest(SootClass sootClass) {
	sc = sootClass;
	}
/**
 * 
 * @return ca.mcgill.sable.soot.SootClass
 */
public SootClass getSootClass() {
	return sc;
}
/**
 * Insert the method's description here.
 * Creation date: (00-7-2 22:39:19)
 * @param scs ca.mcgill.sable.soot.SootClass
 */
public void setSootClass(SootClass scs) {
	sc = scs;
}
}
