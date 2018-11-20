package edu.ksu.cis.bandera.pdgslicer.datastructure;

import ca.mcgill.sable.soot.*;

public class SliceField extends SliceVariable {
	private SootField sf;
/**
 * 
 * @param sootClass ca.mcgill.sable.soot.SootClass
 * @param sootField ca.mcgill.sable.soot.SootField
 */
public SliceField(SootClass sootClass, SootField sootField) {
	super(sootClass);
	this.sf = sootField;
}
/**
 * 
 * @return boolean
 * @param object java.lang.Object
 */
public boolean equals(Object object) {
	if (object instanceof SliceField) {
		SliceField sfd = (SliceField) object;
		return sf.equals(sfd.getSootField()) && sc==sfd.getSootClass();
	} else {
		return false;
	}
}
/**
 * 
 * @return ca.mcgill.sable.soot.SootField
 */
public SootField getSootField() {
	return sf;
}
/**
 * 
 * @return int
 */
public int hashCode() {
	return sf.hashCode();
}
/**
 * Insert the method's description here.
 * Creation date: (00-7-2 22:42:20)
 * @param sfd ca.mcgill.sable.soot.SootField
 */
public void setSootField(SootField sfd) {
	sf = sfd;
}
/**
 * 
 * @return java.lang.String
 */
public String toString() {
	return sf.toString() + " :: " + getSootClass();
}
}
