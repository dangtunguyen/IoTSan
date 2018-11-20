package edu.ksu.cis.bandera.pdgslicer.datastructure;

import ca.mcgill.sable.soot.*;

public abstract class SliceVariable extends SliceInterest {
/**
 * Insert the method's description here.
 * Creation date: (00-11-22 16:50:01)
 */
public SliceVariable() {}
/**
 * 
 * @param sootClass ca.mcgill.sable.soot.SootClass
 */
public SliceVariable(SootClass sootClass) {
	super(sootClass);
}
}
