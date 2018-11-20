package edu.ksu.cis.bandera.spin;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2001   Radu Iosif (iosif@cis.ksu.edu)               *
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
import java.util.Vector;


class BitMatrix {
  private int size;
  private int[][] rawData;
  private Vector packedData = new Vector();

  public BitMatrix(int[][] rawData, int size) { 
    this.rawData = rawData;
    this.size = size; 
  }

  public int getSize() { return size; }
  public int getLength() { return packedData.size(); }
  public Object elementAt(int i) { return packedData.elementAt(i); }

  // little endian encoding of bit matrix
  public BitMatrix pack() {
    int b = 0, cnt = 0;
    for (int i = 0; i < size; i ++)
      for (int j = 0; j < size; j ++, cnt = (cnt + 1) % 8)
	{
	  b |= rawData[i][j] << (7 - cnt);
	  if (cnt == 7) {
	    packedData.addElement(new Integer(b));
	    b = 0;
	  }
	}

    if (cnt < 7)
      packedData.addElement(new Integer(b));

    return this;
  }
}



