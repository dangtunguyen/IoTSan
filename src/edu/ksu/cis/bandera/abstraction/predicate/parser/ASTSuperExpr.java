package edu.ksu.cis.bandera.abstraction.predicate.parser;

/* Generated By:JJTree: Do not edit this line. ASTSuperExpr.java */
public class ASTSuperExpr extends SimpleNode {
  public ASTSuperExpr(int id) {
	super(id);
  }  
  public ASTSuperExpr(PredicateParser p, int id) {
	super(p, id);
  }  
  /** Accept the visitor. **/
  public Object jjtAccept(PredicateParserVisitor visitor, Object data) {
	return visitor.visit(this, data);
  }  
  public static Node jjtCreate(int id) {
	  return new ASTSuperExpr(id);
  }  
  public static Node jjtCreate(PredicateParser p, int id) {
	  return new ASTSuperExpr(p, id);
  }  
}