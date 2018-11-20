package edu.ksu.cis.bandera.bir;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2001,2002    Radu Iosif (iosif@cis.ksu.edu)         *
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

/**
 * An expression value representing a nondeterministic choice among
 * all allocated instances of a record or array type. This choice
 * is by default external. 
 */

public class ForallExpr implements Expr {
  Type baseType;
  Type refType;
  TransSystem system;

  public ForallExpr(Type baseType, TransSystem system) { 
    this(baseType, new Ref(baseType, system), system);
  }

  public ForallExpr(Type baseType, Type refType, TransSystem system) {
    this.refType = refType;
    this.baseType = baseType;
    this.system = system;
  }

  public Type getBaseType() { return baseType; }
  public Type getType() { return refType; }
  
  public void apply(Switch sw)
  {
    ((ExprSwitch) sw).caseForallExpr(this);
  }

  public Object clone() { return new ForallExpr(baseType, refType, system); }
}

