package edu.ksu.cis.bandera.abstraction.pvs;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998-2001 SAnToS Laboratories (santos@cis.ksu.edu)  *

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
//----------------------------------------------------
// The following code was generated by CUP v0.10k
// Fri Sep 29 14:44:07 CDT 2000
//----------------------------------------------------

import java_cup.runtime.*;
import java.io.*;
import java.util.*;

/** Cup generated class to encapsulate user supplied action code.*/
class CUP$parser$actions {
  private final parser parser;

  /** Constructor */
  CUP$parser$actions(parser parser) {
	this.parser = parser;
  }  
  /** Method with the actual generated action code. */
  public final java_cup.runtime.Symbol CUP$parser$do_action(
	int                        CUP$parser$act_num,
	java_cup.runtime.lr_parser CUP$parser$parser,
	java.util.Stack            CUP$parser$stack,
	int                        CUP$parser$top)
	throws java.lang.Exception
	{
	  /* Symbol object for return from actions */
	  java_cup.runtime.Symbol CUP$parser$result;

	  /* select the action based on the action number */
	  switch (CUP$parser$act_num)
		{
		  /*. . . . . . . . . . . . . . . . . . . .*/
		  case 29: // expr ::= ID 
			{
			  Node RESULT = null;
		int idleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int idright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		String id = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 RESULT= new Node(sym.ID,id); 
		 if(id.compareTo(parser.variable)!=0)
			System.err.println("Line "+ ((Yylex) parser.getScanner()).line() +": id "+id+
			" not matched by declared "+parser.variable+" ! "); 
	      
			  CUP$parser$result = new java_cup.runtime.Symbol(8/*expr*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
			}
		  return CUP$parser$result;

		  /*. . . . . . . . . . . . . . . . . . . .*/
		  case 28: // expr ::= NUMBER 
			{
			  Node RESULT = null;
		int nleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int nright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		Integer n = (Integer)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 RESULT=new Node(sym.NUMBER,n); 
			  CUP$parser$result = new java_cup.runtime.Symbol(8/*expr*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
			}
		  return CUP$parser$result;

		  /*. . . . . . . . . . . . . . . . . . . .*/
		  case 27: // expr ::= LPAREN expr RPAREN 
			{
			  Node RESULT = null;
		int eleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
		int eright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
		Node e = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		 RESULT=new Node(sym.LPAREN,e); 
			  CUP$parser$result = new java_cup.runtime.Symbol(8/*expr*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
			}
		  return CUP$parser$result;

		  /*. . . . . . . . . . . . . . . . . . . .*/
		  case 26: // expr ::= NOT expr 
			{
			  Node RESULT = null;
		int eleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int eright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		Node e = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 RESULT=new Node(sym.NOT,e); 
			  CUP$parser$result = new java_cup.runtime.Symbol(8/*expr*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
			}
		  return CUP$parser$result;

		  /*. . . . . . . . . . . . . . . . . . . .*/
		  case 25: // expr ::= MINUS expr 
			{
			  Node RESULT = null;
		int eleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int eright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		Node e = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 RESULT=new Node(sym.UMINUS,e); 
			  CUP$parser$result = new java_cup.runtime.Symbol(8/*expr*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
			}
		  return CUP$parser$result;

		  /*. . . . . . . . . . . . . . . . . . . .*/
		  case 24: // expr ::= expr MOD expr 
			{
			  Node RESULT = null;
		int lleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left;
		int lright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).right;
		Node l = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		int rleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int rright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		Node r = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 RESULT=new Node(sym.MOD,l,r); 
			  CUP$parser$result = new java_cup.runtime.Symbol(8/*expr*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
			}
		  return CUP$parser$result;

		  /*. . . . . . . . . . . . . . . . . . . .*/
		  case 23: // expr ::= expr DIV expr 
			{
			  Node RESULT = null;
		int lleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left;
		int lright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).right;
		Node l = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		int rleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int rright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		Node r = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 RESULT=new Node(sym.DIV,l,r); 
			  CUP$parser$result = new java_cup.runtime.Symbol(8/*expr*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
			}
		  return CUP$parser$result;

		  /*. . . . . . . . . . . . . . . . . . . .*/
		  case 22: // expr ::= expr TIMES expr 
			{
			  Node RESULT = null;
		int lleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left;
		int lright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).right;
		Node l = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		int rleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int rright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		Node r = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 RESULT=new Node(sym.TIMES,l, r); 
			  CUP$parser$result = new java_cup.runtime.Symbol(8/*expr*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
			}
		  return CUP$parser$result;

		  /*. . . . . . . . . . . . . . . . . . . .*/
		  case 21: // expr ::= expr MINUS expr 
			{
			  Node RESULT = null;
		int lleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left;
		int lright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).right;
		Node l = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		int rleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int rright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		Node r = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 RESULT=new Node(sym.MINUS,l,r); 
			  CUP$parser$result = new java_cup.runtime.Symbol(8/*expr*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
			}
		  return CUP$parser$result;

		  /*. . . . . . . . . . . . . . . . . . . .*/
		  case 20: // expr ::= expr PLUS expr 
			{
			  Node RESULT = null;
		int lleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left;
		int lright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).right;
		Node l = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		int rleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int rright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		Node r = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 RESULT=new Node(sym.PLUS,l,r); 
			  CUP$parser$result = new java_cup.runtime.Symbol(8/*expr*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
			}
		  return CUP$parser$result;

		  /*. . . . . . . . . . . . . . . . . . . .*/
		  case 19: // expr ::= expr GT expr 
			{
			  Node RESULT = null;
		int lleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left;
		int lright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).right;
		Node l = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		int rleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int rright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		Node r = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 RESULT=new Node(sym.GT,l,r); 
			  CUP$parser$result = new java_cup.runtime.Symbol(8/*expr*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
			}
		  return CUP$parser$result;

		  /*. . . . . . . . . . . . . . . . . . . .*/
		  case 18: // expr ::= expr GE expr 
			{
			  Node RESULT = null;
		int lleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left;
		int lright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).right;
		Node l = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		int rleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int rright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		Node r = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 RESULT=new Node(sym.GE,l,r); 
			  CUP$parser$result = new java_cup.runtime.Symbol(8/*expr*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
			}
		  return CUP$parser$result;

		  /*. . . . . . . . . . . . . . . . . . . .*/
		  case 17: // expr ::= expr LT expr 
			{
			  Node RESULT = null;
		int lleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left;
		int lright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).right;
		Node l = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		int rleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int rright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		Node r = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 RESULT=new Node(sym.LT,l,r); 
			  CUP$parser$result = new java_cup.runtime.Symbol(8/*expr*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
			}
		  return CUP$parser$result;

		  /*. . . . . . . . . . . . . . . . . . . .*/
		  case 16: // expr ::= expr LE expr 
			{
			  Node RESULT = null;
		int lleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left;
		int lright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).right;
		Node l = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		int rleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int rright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		Node r = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 RESULT=new Node(sym.LE,l,r); 
			  CUP$parser$result = new java_cup.runtime.Symbol(8/*expr*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
			}
		  return CUP$parser$result;

		  /*. . . . . . . . . . . . . . . . . . . .*/
		  case 15: // expr ::= expr NE expr 
			{
			  Node RESULT = null;
		int lleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left;
		int lright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).right;
		Node l = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		int rleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int rright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		Node r = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 RESULT=new Node(sym.NE,l,r); 
			  CUP$parser$result = new java_cup.runtime.Symbol(8/*expr*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
			}
		  return CUP$parser$result;

		  /*. . . . . . . . . . . . . . . . . . . .*/
		  case 14: // expr ::= expr EQ expr 
			{
			  Node RESULT = null;
		int lleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left;
		int lright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).right;
		Node l = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		int rleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int rright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		Node r = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 RESULT=new Node(sym.EQ,l,r); 
			  CUP$parser$result = new java_cup.runtime.Symbol(8/*expr*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
			}
		  return CUP$parser$result;

		  /*. . . . . . . . . . . . . . . . . . . .*/
		  case 13: // expr ::= expr AND expr 
			{
			  Node RESULT = null;
		int lleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left;
		int lright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).right;
		Node l = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		int rleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int rright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		Node r = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 RESULT=new Node(sym.AND,l,r); 
			  CUP$parser$result = new java_cup.runtime.Symbol(8/*expr*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
			}
		  return CUP$parser$result;

		  /*. . . . . . . . . . . . . . . . . . . .*/
		  case 12: // expr ::= expr OR expr 
			{
			  Node RESULT = null;
		int lleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left;
		int lright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).right;
		Node l = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-2)).value;
		int rleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int rright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		Node r = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 RESULT=new Node(sym.OR,l,r); 
			  CUP$parser$result = new java_cup.runtime.Symbol(8/*expr*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
			}
		  return CUP$parser$result;

		  /*. . . . . . . . . . . . . . . . . . . .*/
		  case 11: // expr_part ::= expr ARROW ID SEMI 
			{
			  Node RESULT = null;
		int eleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-3)).left;
		int eright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-3)).right;
		Node e = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-3)).value;
		int idleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
		int idright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
		String id = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		 parser.npredicates=parser.npredicates+1;
		 parser.tokens.add(id); 
	         RESULT=new Node(sym.SEMI,id,e); 
			  CUP$parser$result = new java_cup.runtime.Symbol(7/*expr_part*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-3)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
			}
		  return CUP$parser$result;

		  /*. . . . . . . . . . . . . . . . . . . .*/
		  case 10: // expr_list ::= expr_part 
			{
			  Node RESULT = null;
		int eleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int eright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		Node e = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 RESULT=e; 
			  CUP$parser$result = new java_cup.runtime.Symbol(6/*expr_list*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
			}
		  return CUP$parser$result;

		  /*. . . . . . . . . . . . . . . . . . . .*/
		  case 9: // expr_list ::= expr_list expr_part 
			{
			  Node RESULT = null;
		int lleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
		int lright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
		Node l = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		int rleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int rright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		Node r = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
		 RESULT=new Node(sym.SEMI,l,r); 
			  CUP$parser$result = new java_cup.runtime.Symbol(6/*expr_list*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
			}
		  return CUP$parser$result;

		  /*. . . . . . . . . . . . . . . . . . . .*/
		  case 8: // abstract_fun ::= ABSTRACT LPAREN ID NT$0 RPAREN BEGIN expr_list END 
			{
			  Object RESULT = null;
			  // propagate RESULT from NT$0
			  if ( ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-4)).value != null )
				RESULT = (Object) ((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-4)).value;
		int sleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-5)).left;
		int sright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-5)).right;
		String s = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-5)).value;
		int eleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
		int eright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
		Node e = (Node)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		 parser.tree=e; 
			  CUP$parser$result = new java_cup.runtime.Symbol(4/*abstract_fun*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-7)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
			}
		  return CUP$parser$result;

		  /*. . . . . . . . . . . . . . . . . . . .*/
		  case 7: // NT$0 ::= 
			{
			  Object RESULT = null;
		int sleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left;
		int sright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right;
		String s = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-0)).value;
 parser.variable = s; 
			  CUP$parser$result = new java_cup.runtime.Symbol(9/*NT$0*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
			}
		  return CUP$parser$result;

		  /*. . . . . . . . . . . . . . . . . . . .*/
		  case 6: // id_list ::= id_list COMMA ID 
			{
			  Object RESULT = null;

			  CUP$parser$result = new java_cup.runtime.Symbol(5/*id_list*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-2)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
			}
		  return CUP$parser$result;

		  /*. . . . . . . . . . . . . . . . . . . .*/
		  case 5: // id_list ::= ID 
			{
			  Object RESULT = null;

			  CUP$parser$result = new java_cup.runtime.Symbol(5/*id_list*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
			}
		  return CUP$parser$result;

		  /*. . . . . . . . . . . . . . . . . . . .*/
		  case 4: // token_def ::= TOKENS EQUAL LBRACE id_list RBRACE SEMI 
			{
			  Object RESULT = null;

			  CUP$parser$result = new java_cup.runtime.Symbol(3/*token_def*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-5)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
			}
		  return CUP$parser$result;

		  /*. . . . . . . . . . . . . . . . . . . .*/
		  case 3: // type ::= FLOAT 
			{
			  Object RESULT = null;
		 parser.basic_type = sym.FLOAT; 
			  CUP$parser$result = new java_cup.runtime.Symbol(2/*type*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
			}
		  return CUP$parser$result;

		  /*. . . . . . . . . . . . . . . . . . . .*/
		  case 2: // type ::= INT 
			{
			  Object RESULT = null;
		 parser.basic_type = sym.INT; 
			  CUP$parser$result = new java_cup.runtime.Symbol(2/*type*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
			}
		  return CUP$parser$result;

		  /*. . . . . . . . . . . . . . . . . . . .*/
		  case 1: // $START ::= spec EOF 
			{
			  Object RESULT = null;
		int start_valleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left;
		int start_valright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).right;
		Object start_val = (Object)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-1)).value;
		RESULT = start_val;
			  CUP$parser$result = new java_cup.runtime.Symbol(0/*$START*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-1)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
			}
		  /* ACCEPT */
		  CUP$parser$parser.done_parsing();
		  return CUP$parser$result;

		  /*. . . . . . . . . . . . . . . . . . . .*/
		  case 0: // spec ::= ABSTRACTION ID EXTENDS type BEGIN token_def abstract_fun END 
			{
			  Object RESULT = null;
		int nameleft = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-6)).left;
		int nameright = ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-6)).right;
		String name = (String)((java_cup.runtime.Symbol) CUP$parser$stack.elementAt(CUP$parser$top-6)).value;
		 parser.name=name; 
			  CUP$parser$result = new java_cup.runtime.Symbol(1/*spec*/, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-7)).left, ((java_cup.runtime.Symbol)CUP$parser$stack.elementAt(CUP$parser$top-0)).right, RESULT);
			}
		  return CUP$parser$result;

		  /* . . . . . .*/
		  default:
			throw new Exception(
			   "Invalid action number found in internal parse table");

		}
	}
}
