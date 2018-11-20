package edu.ksu.cis.bandera.pdgslicer;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999   Hongjun Zheng (zheng@cis.ksu.edu)      *
 * All rights reserved.                                              *
 *                                                                   *
 * This work was done as a project in the SAnToS Laboratory,         *
 * Department of Computing and Information Sciences, Kansas State    *
 * University, USA (http://www.cis.ksu.edu/santos).                  *
 * It is understood that any modification not identified as such is  *
 * not covered by the preceding statement.                           *
 *                                                                   *
 * This work is free software; you can redistribute it and/or        *
 * modify it under the terms of the GNU Library General Public       *
 * License as published by the Free Software Foundation; either      *
 * version 2 of the License, or (at your option) any later version.  *
 *                                                                   *
 * This work is distributed in the hope that it will be useful,      *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of    *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU *
 * Library General Public License for more details.                  *
 *                                                                   *
 * You should have received a copy of the GNU Library General Public *
 * License along with this toolkit; if not, write to the             *
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,      *
 * Boston, MA  02111-1307, USA.                                      *
 *                                                                   *
 * Java is a trademark of Sun Microsystems, Inc.                     *
 *                                                                   *
 * To submit a bug report, send a comment, or get the latest news on *
 * this project and other SAnToS projects, please visit the web-site *
 *                http://www.cis.ksu.edu/santos                      *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import ca.mcgill.sable.soot.jimple.Stmt;
import ca.mcgill.sable.util.*;
/**
 * This class is for storing information about statement on which
 * other statements are data and interference dependent.
 */
public class DataBox {
  /**
   * The statement of interference dependent.
   */
	private Stmt interStmt; //this is for interference statement 
  /**
   * The statement of data dependent.
   */
	private Stmt onstmt;
  /**
   * A set of {@link Value Value} on which other statements are
   * dependent.
   */
	private Set var;
	private boolean isInvokeInit;
	private boolean isNewExprStmt;
  /**
   * Constructor of {@link DataBox DataBox}.
   * Initializing both <code>isInvokeInit</code> and <code>isNewExprStmt</code>
   * to <code>false</code>.
   * <p>
   * @param stmt the statement on which other statements are dependent.
   * @param data a set of value on which other statements are dependent.
   */
public DataBox(Stmt stmt, Set data) {
	onstmt = stmt;
	interStmt = stmt;
	var = data;
	isInvokeInit = false;
	isNewExprStmt = false;
}
public boolean equals(Object o) {
	DataBox ad = (DataBox) o;
	boolean stmtEq = false;
	/*
	if (this.interStmt != null)
	stmtEq = this.interStmt.equals(ad.getInterferStmt());
	*/
	if (this.onstmt != null)
		stmtEq = this.onstmt.equals(ad.getStmt());

		
	if (stmtEq){
		this.var.addAll(ad.getDependVar());
		//ad.var = new ArraySet();
		//ad.var.addAll(this.var);
	}
		
	return stmtEq;
}
  /**
   * Get data dependent variables' value.
   * <p>
   * @return a set of {@link Value Value}.
   */
public Set getDependVar() {
	Set returnVar = new ArraySet();
	returnVar.addAll(var);
	return returnVar;
}
  /**
   * Get interference dependent statement.
   * <p>
   * @return {@link #interStmt interStmt}.
   */
  public Stmt getInterferStmt()
	{
	  return interStmt;
	}
  /**
   * Get interference variables' value.
   * <p>
   * @return a set of {@link Value Value}.
   */
public Set getInterferVars() {
	Set returnVar = new ArraySet();
	returnVar.addAll(var);
	return returnVar;
}
  /**
   * Get data dependent statement.
   * <p>
   * @return {@link #onstmt onstmt}.
   */
public Stmt getStmt() {
	return onstmt;
}
  /**
   * See if the statatement involed in this <code>DataBox</code> is 
   * new expression statement.
   * <p>
   * @return {@link #isNewExprStmt isNewExprStmt}.
   */
  public boolean isNewExprStmt()
	{
	  return isNewExprStmt;
	}
  /**
   * See if the statatement involed in this <code>DataBox</code> is 
   * special invoke init method statement.
   * <p>
   * @return {@link #isInvokeInit isInvokeInit}.
   */
  public boolean isSpecialInvokeInit()
	{
	  return isInvokeInit;
	}
  /**
   * Set <code>isInvokeInit</code> to <code>true</code>.
   */
  public void setToInvokeInit()
	{
	  isInvokeInit = true;
	}
  /**
   * Set <code>isNewExprStmt</code> to <code>true</code>.
   */
  public void setToNewExprStmt()
	{
	  isNewExprStmt = true;
	}
  public String toString()
	{
	  if (onstmt != null)
	return "dataBox {" + onstmt + "," + var + "}";
	  else 
	return "dataBox {" + interStmt + "," + var + "}";
	}
}
