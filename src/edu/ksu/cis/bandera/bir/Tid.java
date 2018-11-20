package edu.ksu.cis.bandera.bir;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2001, 2002   Radu Iosif (iosif@cis.ksu.edu)         *
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
 * The thread identifier type.
 */

public class Tid extends Type implements BirConstants {

  private int max;

  public Tid(int max) { this.max = max; }

  public void apply(TypeSwitch sw, Object o)
  {
    sw.caseTid(this, o);
  }

  public boolean containsValue(Object value) {
    int val;
    if (value instanceof Integer)
      val = ((Integer) value).intValue();
    else
      return false;

    return (val >= 0 && val < max);
  }

  public int maxTid() { 
    return max;
  }

  public Expr defaultVal() { 
    return new IntLit(0);
  }

  public boolean equals(Object o) {
    return (o instanceof Tid);
  }

  public boolean isKind(int kind) {
    return (kind & TID) != 0;
  }

  public String toString() {
    return "tid";
  }
}
