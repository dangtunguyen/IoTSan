package edu.ksu.cis.bandera.abstraction.predicate.parser;

import java.util.*;

public abstract class ASTBinOpExpr extends SimpleNode {
  // Added attribute begin
  private LinkedList exprList;
  private String token;
  // Added code end

  public ASTBinOpExpr(int id) {
	super(id);
  }  
  public ASTBinOpExpr(PredicateParser p, int id) {
	super(p, id);
  }  
  public List getExprList() { return exprList; }  
  public String getToken() { return token; }  
  // Added attribute end

  // Added code begin
  public void setExprList(LinkedList l) { exprList = l; }  
  public void setToken(String t) { token = t; }  
}
