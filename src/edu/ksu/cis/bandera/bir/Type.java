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
import edu.ksu.cis.bandera.util.DefaultValues;

public abstract class Type implements Definition {

  String name;
  String typeId;
  int extent = 1;

  public static Type booleanType = new Bool();
  public static Type tidType = new Tid(DefaultValues.birMaxThreads);
  public static Type defaultRangeType = 
    new Range(DefaultValues.birMinIntRange,DefaultValues.birMaxIntRange);
  public static final Type intType = new Range();
  public static final Type WR_lockType = new Lock(true,true);
  public abstract void apply(TypeSwitch sw, Object o);
  public boolean compatibleWith(Type type) {
    return this.getClass() == type.getClass();
  }
  public boolean containsValue(Object value) { return false; }
  public Expr defaultVal() { return null; }
  public Object getDef() { return this.toString(); }
  public int getExtent() { return extent; }
  public String getName() { return name; }
  public String getTypeId() { return typeId; }
  public abstract boolean isKind(int kind);
  public boolean isSubtypeOf(Type type) { return false; }
  public void setName(String name) { this.name = name; }
  public void setTypeId(String typeId) { this.typeId = typeId; }
  public String typeName() {
    return (name == null) ? typeId : name;
  }
  public String typeSpec() {
    return (name == null) ? this.toString() : name;
  }
  /* [Thomas, July 11, 2017]
   * */
  public static void init()
  {
	  booleanType = new Bool();
	  tidType = new Tid(DefaultValues.birMaxThreads);
	  defaultRangeType = 
			    new Range(DefaultValues.birMinIntRange,DefaultValues.birMaxIntRange);
  }
}

