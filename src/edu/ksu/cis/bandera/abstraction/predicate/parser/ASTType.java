package edu.ksu.cis.bandera.abstraction.predicate.parser;

/* Generated By:JJTree: Do not edit this line. ASTType.java */
public class ASTType extends ASTSingleExpr {
  // Added attributes begin
  private int dim;
  private String baseType;
  // Added code end

  public ASTType(int id) {
	super(id);
  }  
  public ASTType(PredicateParser p, int id) {
	super(p, id);
  }  
  public String getBaseType() { return baseType; }  
  public int  getDimension() { return dim; }  
  /** Accept the visitor. **/
  public Object jjtAccept(PredicateParserVisitor visitor, Object data) {
	return visitor.visit(this, data);
  }  
  public static Node jjtCreate(int id) {
	  return new ASTType(id);
  }  
  public static Node jjtCreate(PredicateParser p, int id) {
	  return new ASTType(p, id);
  }  
  public void setBaseType(String t) { baseType = t; }  
  // Added attributes end

  // Added code begin
  public void setDimension(int d) { dim = d; }  
}
