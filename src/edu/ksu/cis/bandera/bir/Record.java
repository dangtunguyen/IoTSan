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
import ca.mcgill.sable.util.*;

import java.io.*;
import java.util.*;

/**
 * A record type.
 */

public class Record extends Type implements BirConstants {

  Vector fields;
  boolean isMonitor = false;
  int unique;

  public Record() {
    this.fields = new Vector(10);
    this.extent = 0;
  }

  public Field addField(String name, Type fieldType) {

      // a little hack to see where this is being added from.
      /*
      if((name != null) && (name.equals("BIRLock"))) {
	  Exception e = new Exception("The name of the field being added is BIRLock.");
	  e.printStackTrace(System.err);
      }
      */

    Field field = new Field(name, fieldType, this);
    fields.addElement(field);
    return field;
  }

  public void apply(TypeSwitch sw, Object o)
  {
    sw.caseRecord(this, o);
  }

  public Expr defaultVal() { return null; }
  /**
   * Two record types are equal if they have the same fields
   * (name and type) in the same order.
   */

  public boolean equals(Object o) {
    if (o instanceof Record) {
      Vector o_fields = ((Record)o).fields;
      if (o_fields.size() != fields.size())
	return false;
      for (int i = 0; i < fields.size(); i++) 
	if (! fields.elementAt(i).equals(o_fields.elementAt(i)))
	  return false;
      return true;
    }
    return false;
  }

  public int getExtent() { 
    if (extent == 0)
      for (int i = 0; i < fields.size(); i++) {
	Field field = (Field)fields.elementAt(i);
	field.setOffset(extent);
	extent += field.getType().getExtent();
      }
    return extent;
  }

  public void setUnique(int unique) { this.unique = unique; }
  public int getUnique() { return unique; }

  public Field getField(String name) {
    for (int i = 0; i < fields.size(); i++) {
      Field field = (Field)fields.elementAt(i);
      if (field.getName().equals(name))
	return field;
    }
    return null;
  }

  public Vector getFields() { return fields; }

  public boolean isEmpty() { 
    return fields.isEmpty();
  }
  public boolean isKind(int kind) { 
    return (kind & RECORD) != 0;
  }

  // Printing
  public String toString() {
    String result = "";
    for (int i = 0; i < fields.size(); i++) 
      result += ((Field)fields.elementAt(i)).toString();
    return "record { " + result + "}";
  }
}
