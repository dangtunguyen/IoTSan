package edu.ksu.cis.bandera.abstraction.predicate.parser;

import java.util.*;

public abstract class ASTSingleExpr extends SimpleNode {
  // Added attribute begin
  private SimpleNode expr;
  // Added code end

  public ASTSingleExpr(int id) {
	super(id);
  }  
  public ASTSingleExpr(PredicateParser p, int id) {
	super(p, id);
  }  
  public SimpleNode getExpr() { return expr; }  
  // Added attribute end

  // Added code begin
  public void setExpr(SimpleNode n) { expr = n; }  
}
