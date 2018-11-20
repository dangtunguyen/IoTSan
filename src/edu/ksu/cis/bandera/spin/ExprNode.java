package edu.ksu.cis.bandera.spin;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999   James Corbett (corbett@hawaii.edu)     *
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
import java.io.*;
import java.util.*;

import edu.ksu.cis.bandera.bir.*;

/**
 * A base expression in a case tree.
 * <p>
 * Note: an ExprNode actually holds two expressions.  The first is
 * the main expression (the node's value).  The second is an auxiliary
 * expression that holds the value of an ExprNode that this node
 * has been composed with until the ExprNode is updated via the update()
 * method.
 */

public class ExprNode implements TreeNode {
	
  public String expr1;
  public String expr2;

  public ExprNode(String expr) {
    this.expr1 = expr;
  }
  public ExprNode(String expr1, String expr2) {
    this.expr1 = expr1;
    this.expr2 = expr2;
  }
  /**
   * When composing two case trees, when we reach a leaf of the
   * first tree (i.e., this method), then return the second tree
   * specialized in the context of the first tree.
   */

  public TreeNode compose(TreeNode tree, Case context) {
    return tree.specialize(this, context);
  }
  public Vector getLeafCases(Vector leafCases) {
    return null;
  }
  public Vector getLeaves(Vector leaves) {
    leaves.addElement(this);
    return leaves;
  }
  public void print(int level) {
    if (expr2 == null)
      System.out.println(expr1);
    else
      System.out.println("(" + expr1 + "," + expr2 + ")");
  }
  /**
   * When composing two case trees and we reach the bottom of
   * the second tree, make an ExprNode that holds the two
   * expressions from the original nodes.
   */

  public TreeNode specialize(ExprNode leaf, Case context) {
    return new ExprNode(leaf.expr1, this.expr1);
  }
  public void update(String expr) {
    this.expr1 = expr;
    this.expr2 = null;
  }
  public void append(String expr) {
    update(expr1 + expr);
  }
}
