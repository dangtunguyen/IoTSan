package edu.ksu.cis.bandera.bir;

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

/**
 * TypeSwitch to print a variable of a BIR type
 */

public class BirTypePrint extends AbstractTypeSwitch {
  BirState state;

  public BirTypePrint(BirState state) {
    this.state = state;
  }

  public void caseArray(Array type, Object o) {
    Literal[] store = varStore(o);
    int offset = varOffset(o);
    int size = ((ConstExpr) store[offset]).getValue();
    System.out.print("[");
    for (int i = 0; i < size; i++) {
      if (i > 0) System.out.print(",");
      type.getBaseType().apply(this, new Address(store, offset + i + 1));
    }
    System.out.print("]");
  }

  public void caseBool(Bool type, Object o) {
    simplePrint(type, o);
  }

  public void caseTid(Tid type, Object o) {
    simplePrint(type, o);
  }

  public void caseCollection(Collection type, Object o) {
    Literal[] store = varStore(o);
    int offset = varOffset(o);
    int size = ((StateVar) o).getActualSize();
    int extent = ((StateVar) o).getActualBaseTypeExtent();
    for (int i = 0; i < size; i++) {
      if (store[offset+i*extent] != null) {
	System.out.println();
	System.out.print("      #" + i + " = ");
	type.getBaseType().apply(this, new Address(store, offset+i*extent));
      }
    }
  }

  public void caseEnumerated(Enumerated type, Object o) {
    simplePrint(type, o);
  }

  public void caseField(Field type, Object o) {
    int offset = varOffset(o) + type.getOffset();
    System.out.print(type.getName() + "=");
    type.getType().apply(this, new Address(varStore(o), offset));
  }

  public void caseLock(Lock type, Object o) {	
    simplePrint(type, o);
  }

  public void caseRange(Range type, Object o) {
    simplePrint(type, o);
  }

  public void caseRecord(Record type, Object o) {
    Vector fields = type.getFields();
    System.out.print("{");
    for (int i = 0; i < fields.size(); i++) {
      if (i > 0) System.out.print(",");
      ((Field) fields.elementAt(i)).apply(this, o);
    }
    System.out.print("}");
  }

  public void caseRef(Ref type, Object o) {
    simplePrint(type, o);
  }
  
  public void defaultCase(Object obj) {
    throw new RuntimeException("Construct not handled: " + obj);
  }
  
  void simplePrint(Type type, Object o) {
    Literal[] store = varStore(o);
    int offset = varOffset(o);
    System.out.print(store[offset].toString());
  }

  Literal[] varStore(Object context) {
    if (context instanceof Address)
      return ((Address) context).getStore();
    else if (context instanceof StateVar) {
      StateVar var = (StateVar) context;
      if (var.getThread() != null)
	return ((LocalVar) var).getActiveThread().getStore();
      else
	return state.getStore();
    }

    throw new RuntimeException("wrong type context:" + 
			       context.getClass().getName());
  }

  int varOffset(Object context) {
    if (context instanceof Address)
      return ((Address) context).getOffset();
    else if (context instanceof StateVar)
      return ((StateVar) context).getOffset();

    throw new RuntimeException("wrong type context:" + 
			       context.getClass().getName());
  }
}


