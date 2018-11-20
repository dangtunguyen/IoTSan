package edu.ksu.cis.bandera.abstraction.pvs;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1999, 2000   Corina Pasareanu (pcorina@cis.ksu.edu) *
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

public class Node {
 int token;
 Integer value;
 String id_value;
 Node left=null;
 Node right=null;

 static int n=0;

 public Node(int t, Node n) {
	token=t;
		left=n;
 } 
 public Node(int t, Node l,Node r) {
	token=t;
	left=l;
		right=r;
 } 
 public Node(int t, Integer v) {
	token=t;
	value=v;
 } 
 public Node(int t, String s) {
	token=t;
		id_value=s;
 } 
 public Node(int t, String s, Node n) {
	token=t;
		left=n;
		id_value=s;
 } 
 /* print AST for generated BASL spec */
 public void print_BASL(PrintWriter f_spec) {

	   switch (token)
	   {
		case sym.SEMI: if (right==null) {
		         f_spec.print("    ");
						 left.print_BASL(f_spec);
						 f_spec.print(" -> " + id_value + " ;\n");
					   } else {
								left.print_BASL(f_spec);
								right.print_BASL(f_spec);
							  }
					   break;
		case sym.OR: left.print_BASL(f_spec);f_spec.print("||");
			right.print_BASL(f_spec);break;
		case sym.AND:left.print_BASL(f_spec);f_spec.print("&&");
			right.print_BASL(f_spec);break;
		case sym.EQ: left.print_BASL(f_spec);f_spec.print("==");
			right.print_BASL(f_spec);break;
		case sym.NE: left.print_BASL(f_spec);f_spec.print("!=");
			right.print_BASL(f_spec);break;
		case sym.LE: left.print_BASL(f_spec);f_spec.print("<=");
			right.print_BASL(f_spec);break;
		case sym.LT: left.print_BASL(f_spec);f_spec.print("<");
			right.print_BASL(f_spec);break;
		case sym.GE: left.print_BASL(f_spec);f_spec.print(">=");
			right.print_BASL(f_spec);break;
		case sym.GT: left.print_BASL(f_spec);f_spec.print(">");
			right.print_BASL(f_spec);break;
		case sym.PLUS:left.print_BASL(f_spec);f_spec.print("+");
			right.print_BASL(f_spec);break;
		case sym.MINUS:left.print_BASL(f_spec);f_spec.print("-");
			right.print_BASL(f_spec);break;
		case sym.TIMES:left.print_BASL(f_spec);f_spec.print("*");
			right.print_BASL(f_spec);break;
		case sym.DIV:left.print_BASL(f_spec);f_spec.print("/");
			right.print_BASL(f_spec);break;
		case sym.MOD:left.print_BASL(f_spec);f_spec.print("%");
			right.print_BASL(f_spec);break;
		case sym.NUMBER: f_spec.print(" "+value+" ");break;
		case sym.ID: f_spec.print(" "+id_value+" ");break;
		case sym.UMINUS: f_spec.print("-");left.print_BASL(f_spec);break;
		case sym.NOT: f_spec.print("!");left.print_BASL(f_spec);break;
		case sym.LPAREN:f_spec.print("(");left.print_BASL(f_spec);
						f_spec.print(")");break;
		default: break;
	   }
		return;
 } 
 /* print AST for pvs theory */
 public void print_PVS(PrintWriter f_pvs) {

	   switch (token)
	   {
		case sym.SEMI: if (right==null) {
						 f_pvs.print("P"+n+"(e): boolean = (");n++;
						 left.print_PVS(f_pvs);
						 f_pvs.println(")");
					   } else {
								left.print_PVS(f_pvs);
								right.print_PVS(f_pvs);
							  }
					   break;
		case sym.OR: left.print_PVS(f_pvs);f_pvs.print("OR");
			right.print_PVS(f_pvs);break;
		case sym.AND:left.print_PVS(f_pvs);f_pvs.print("AND");
			right.print_PVS(f_pvs);break;
		case sym.EQ: left.print_PVS(f_pvs);f_pvs.print("=");
			right.print_PVS(f_pvs);break; 
		case sym.NE: left.print_PVS(f_pvs);f_pvs.print("/=");
			right.print_PVS(f_pvs);break;
		case sym.LE: left.print_PVS(f_pvs);f_pvs.print("<=");
			right.print_PVS(f_pvs);break;
		case sym.LT: left.print_PVS(f_pvs);f_pvs.print("<");
			right.print_PVS(f_pvs);break;
		case sym.GE: left.print_PVS(f_pvs);f_pvs.print(">=");
			right.print_PVS(f_pvs);break;
		case sym.GT: left.print_PVS(f_pvs);f_pvs.print(">");
			right.print_PVS(f_pvs);break;
		case sym.PLUS:left.print_PVS(f_pvs);f_pvs.print("+");
			right.print_PVS(f_pvs);break;
		case sym.MINUS:left.print_PVS(f_pvs);f_pvs.print("-");
			right.print_PVS(f_pvs);break;
		case sym.TIMES:left.print_PVS(f_pvs);f_pvs.print("*");
			right.print_PVS(f_pvs);break;
		case sym.DIV:left.print_PVS(f_pvs);f_pvs.print("/");
			right.print_PVS(f_pvs);break;
		case sym.MOD:left.print_PVS(f_pvs);f_pvs.print("//");
			right.print_PVS(f_pvs);break;
		case sym.NUMBER: f_pvs.print(" "+value+" ");break;
		case sym.ID: f_pvs.print(" e ");break;
		case sym.UMINUS: f_pvs.print("-");left.print_PVS(f_pvs);break;
		case sym.NOT: f_pvs.print("NOT");left.print_PVS(f_pvs);break;
		case sym.LPAREN:f_pvs.print("(");left.print_PVS(f_pvs);
						f_pvs.print(")");break;
		default: break; 
	   }
		return;
 } 
}
