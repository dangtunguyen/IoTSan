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
 * TypeSwitch to initialize a variable of a BIR type
 */

public class BirTypeInit extends AbstractTypeSwitch {
  BirState state;
  //  Literal [] store;
  TransSystem system;

  public BirTypeInit(BirState state) {
    this.state = state;
    //  this.store = state.getStore();
    this.system = state.getSystem();
  }

  public void caseArray(Array type, Object o)
  {
    Literal[] store; 
    int offset;
    int size; 
    if (o instanceof Address) {
      store = ((Address) o).getStore();
      offset = ((Address) o).getOffset();
      size = ((IntLit) store[offset]).getValue();
    }
    else {
      store = varStore(o);
      offset = varOffset(o);
      size = type.getSize().getValue();
      // must init length (static array)
      store[offset] = new IntLit(size);
    }
    for (int i = 0; i < size; i++)
      type.getBaseType().apply(this, new Address(store, offset + i + 1));
  }
  
  public void caseBool(Bool type, Object o) {
    simpleInit(type, o);
  }

  public void caseTid(Tid type, Object o) {
    simpleInit(type, o);
  }

  public void caseCollection(Collection type, Object o) {}

  public void caseEnumerated(Enumerated type, Object o) {
    simpleInit(type, o);
  }

  public void caseField(Field type, Object o) {
    Literal[] store = varStore(o);
    int offset = varOffset(o) + type.getOffset();
    type.getType().apply(this, new Address(store, offset));
  }

  public void caseLock(Lock type, Object o) {	
    Literal[] store = varStore(o);
    int offset = varOffset(o);
    //store[offset] = new LockLit(system);
    store[offset] = new LockLit(system, state);
  }

  public void caseRange(Range type, Object o) {
    simpleInit(type, o);
  }

  public void caseRecord(Record type, Object o) {
    Vector fields = type.getFields();
    for (int i = 0; i < fields.size(); i++) 
      ((Field)fields.elementAt(i)).apply(this, o);
  }

  public void caseRef(Ref type, Object o) {
    Literal[] store = varStore(o);
    int offset = varOffset(o);
    store[offset] = new NullExpr(system);
  }

  public void defaultCase(Object obj) {
    throw new RuntimeException("Construct not handled: " + obj);
  }

  Expr initValue(Type type, Object context) {
    return (context instanceof Address) ?
      type.defaultVal() : ((StateVar) context).getInitVal();
  }

  void simpleInit(Type type, Object o) {
    Literal[] store = varStore(o);
    initValue(type, o).apply(state);
    Literal val = (Literal) state.getResult();
    store[varOffset(o)] = val;
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
