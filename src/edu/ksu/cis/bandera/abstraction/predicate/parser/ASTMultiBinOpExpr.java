package edu.ksu.cis.bandera.abstraction.predicate.parser;

import java.util.*;

public abstract class ASTMultiBinOpExpr extends SimpleNode {
  // Added attribute begin
  private LinkedList tokenList, exprList;
  // Added code end

  public ASTMultiBinOpExpr(int id) {
	super(id);
  }  
  public ASTMultiBinOpExpr(PredicateParser p, int id) {
	super(p, id);
  }  
  public List getExprList() { return exprList; }  
  public List getTokenList() { return tokenList; }  
  // Added attribute end

  // Added code begin
  public void setExprList(LinkedList l) { exprList = l; }  
  public void setTokenList(LinkedList l) { tokenList = l; }  
}
