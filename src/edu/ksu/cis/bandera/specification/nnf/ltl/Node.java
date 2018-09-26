package edu.ksu.cis.bandera.specification.nnf.ltl;

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
//package NNF;
import java.io.*;

public class Node {
 int token;
 String atom_value;
 Node left=null;
 Node right=null;
 


 public Node(int t, Node n) {
	token=t;
		left=n;
 } 
 public Node(int t, Node l,Node r) {
	token=t;
	left=l;
		right=r;
 } 
 public Node(int t, String s) {
	token=t;
		atom_value=s;
 } 
 /* print ltl tree */
 public void ltlprint(StringWriter sw) {
	   switch (token)
	   {
	case sym.ATOM: sw.write(atom_value);break;
		case sym.OR:   sw.write("(");left.ltlprint(sw);
					   sw.write(" || ");right.ltlprint(sw);
	               sw.write(")");break;
		case sym.AND:  sw.write("(");left.ltlprint(sw);
	               sw.write(" && ");right.ltlprint(sw);
	               sw.write(")");break;
		case sym.NOT:  sw.write("!");left.ltlprint(sw);
	               sw.write("");break;
		case sym.F:    sw.write("<>(");left.ltlprint(sw);
	               sw.write(")");break;
		case sym.X:    sw.write("X(");left.ltlprint(sw);
	               sw.write(")");break;
	case sym.G:    sw.write("[](");left.ltlprint(sw);
	               sw.write(")");break;
	case sym.UNTIL:sw.write("(");left.ltlprint(sw);
	               sw.write(" U");
	               right.ltlprint(sw);sw.write(")");break;
		default: sw.write("Error : bad node type");break; 
	   }
		return;
 } 
public void nnf()
{
	Node tmp_left, tmp_right;
	switch (token)
	{
		case sym.E :
		case sym.A :
		case sym.F :
		case sym.X :
		case sym.G :
			left.nnf();
			break;
		case sym.UNTIL :
		case sym.OR :
		case sym.AND :
			left.nnf();
			right.nnf();
			break;
		case sym.NOT :
			/* All of the real work happens here */
			switch (left.token)
			{
				case sym.E :
					/* !Ep => A!p */
					token = sym.A;
					left.token = sym.NOT;
					nnf();
					break;
				case sym.A :
					/* !Ap => E!p */
					token = sym.E;
					left.token = sym.NOT;
					nnf();
					break;
				case sym.F :
					/* !Fp => G!p */
					token = sym.G;
					left.token = sym.NOT;
					nnf();
					break;
				case sym.X :
					/* !Xp => X!p */
					token = sym.X;
					left.token = sym.NOT;
					nnf();
					break;
				case sym.G :
					/* !Gp => F!p */
					token = sym.F;
					left.token = sym.NOT;
					nnf();
					break;
				case sym.UNTIL :
					/* ![pUq] => [!q U (!p & !q)] | G!q */
					/* what about V? */
					/* ![pUq] => [!p V !q] */
					token = sym.OR;
					tmp_left = new Node(sym.UNTIL, new Node(sym.NOT, left.right), new Node(sym.AND, new Node(sym.NOT, left.left), new Node(sym.NOT, left.right)));
					tmp_right = new Node(sym.G, new Node(sym.NOT, left.right));
					left = tmp_left;
					right = tmp_right;
					nnf();
					break;
				case sym.OR :
					/* !(p | q) => (!p & !q) */
					token = sym.AND;
					tmp_left = new Node(sym.NOT, left.left);
					tmp_right = new Node(sym.NOT, left.right);
					left = tmp_left;
					right = tmp_right;
					nnf();
					break;
				case sym.AND :
					/* !(p & q) => (!p | !q) */
					token = sym.OR;
					tmp_left = new Node(sym.NOT, left.left);
					tmp_right = new Node(sym.NOT, left.right);
					left = tmp_left;
					right = tmp_right;
					nnf();
					break;
				case sym.NOT :
					/* !!p == p */
					token = left.left.token;
					tmp_left = left.left.left;
					tmp_right = left.left.right;
					atom_value = left.left.atom_value;
					left = tmp_left;
					right = tmp_right;
					nnf();
					break;
				case sym.ATOM :
					/* !a == !a */
					break;
			}
			break;
		case sym.ATOM :
			break;
		default :
			System.out.println("Error : bad node type encountered in nnf");
	}
}
 /* print tree */
 public void print() {
	   switch (token)
	   {
	case sym.ATOM: System.out.print(atom_value);break;
		case sym.OR:   System.out.print("(");left.print();
					   System.out.print(" | ");right.print();
	               System.out.print(")");break;
		case sym.AND:  System.out.print("(");left.print();
	               System.out.print(" & ");right.print();
	               System.out.print(")");break;
		case sym.NOT:  System.out.print("!(");left.print();
	               System.out.print(")");break;
		case sym.E:    System.out.print("E(");left.print();
	               System.out.print(")");break;
		case sym.A:    System.out.print("A(");left.print();
	               System.out.print(")");break;
		case sym.F:    System.out.print("F(");left.print();
	               System.out.print(")");break;
		case sym.X:    System.out.print("X(");left.print();
	               System.out.print(")");break;
	case sym.G:    System.out.print("G(");left.print();
	               System.out.print(")");break;
	case sym.UNTIL:System.out.print("[");left.print();
	               System.out.print(" U");
	               right.print();System.out.print("]");break;
		default: System.out.print("Error : bad node type");break; 
	   }
		return;
 } 
}
