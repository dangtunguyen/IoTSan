package edu.ksu.cis.bandera.abstraction.predicate.parser;

/* Generated By:JJTree: Do not edit this line. ASTArrayExpr.java */
public class ASTArrayExpr extends ASTSingleExpr {
  public ASTArrayExpr(int id) {
	super(id);
  }  
  public ASTArrayExpr(PredicateParser p, int id) {
	super(p, id);
  }  
  /** Accept the visitor. **/
  public Object jjtAccept(PredicateParserVisitor visitor, Object data) {
	return visitor.visit(this, data);
  }  
  public static Node jjtCreate(int id) {
	  return new ASTArrayExpr(id);
  }  
  public static Node jjtCreate(PredicateParser p, int id) {
	  return new ASTArrayExpr(p, id);
  }  
}