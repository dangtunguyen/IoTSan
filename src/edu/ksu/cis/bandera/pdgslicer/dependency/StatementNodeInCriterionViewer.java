package edu.ksu.cis.bandera.pdgslicer.dependency;

/**
 * Insert the type's description here.
 * Creation date: (00-11-20 15:37:28)
 * @author: 
 */
import edu.ksu.cis.bandera.pdgslicer.datastructure.*;
public class StatementNodeInCriterionViewer extends LocationNodeInCriterionViewer {
	SlicePoint[] locationArray;
/**
 * StatementNode constructor comment.
 */
public StatementNodeInCriterionViewer() {
	super();
}
/**
 * StatementNode constructor comment.
 * @param sm ca.mcgill.sable.soot.SootMethod
 * @param sa edu.ksu.cis.bandera.annotation.Annotation
 * @param sp edu.ksu.cis.bandera.pdgslicer.datastructure.SlicePoint
 */
public StatementNodeInCriterionViewer(ca.mcgill.sable.soot.SootMethod sm, edu.ksu.cis.bandera.annotation.Annotation sa, edu.ksu.cis.bandera.pdgslicer.datastructure.SlicePoint sp, SlicePoint[] la) {
	super(sm, sa, sp);
	locationArray = la;
}
}
