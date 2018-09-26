package edu.ksu.cis.bandera.jext;

/**
 * Insert the type's description here.
 * Creation date: (4/11/01 2:36:48 AM)
 * @author: Roby Joehanes
 */
public interface PAExprSwitch extends BanderaExprSwitch {
	public void caseHookExpr(HookExpr v);
	public void caseLogicalNotExpr(LogicalNotExpr v);
}
