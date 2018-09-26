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
import java.io.*;
import java.util.Vector;
import edu.ksu.cis.bandera.bir.*;


class SubtypeRelation {
  private int size;
  private int[][] rawData;

  public SubtypeRelation(int size) { this.size = size; }
  
  int[][] initMatrix() {
    int[][] matrix = new int[size][];
    for (int i = 0; i < size;  i ++)
      matrix[i] = new int[size];

    return matrix;
  }
  
  void copyMatrix(int[][] src, int[][] dest) {
    for (int i = 0; i < size; i ++)
      for (int j = 0; j < size; j ++)
	dest[i][j] = src[i][j];
  }

  int[][] copyData() {
    int[][] matrix = initMatrix();
    copyMatrix(rawData, matrix);
    return matrix;
  }

  void printData() {
    for (int i = 0; i < size; i ++) {
      for (int j = 0; j < size; j ++)
	System.out.print(rawData[i][j] + "\t");
      System.out.println("");
    }
  }

  boolean multiplyBooleanMatrix(int[][] op1,
				int[][] op2,
				int[][] result) 
  {
    boolean change = false;
    for (int i = 0; i < size; i ++)
      for (int j = 0; j < size; j ++)
	{
	  int sum = 0;
	  for (int k = 0; k < size; k ++)
	    sum |= op1[i][k] & op2[k][j];

	  change |= (result[i][j] != sum);
	  result[i][j] = sum;
	}

    return change;
  }

  void reflexiveClosure() {
    for (int i = 0; i < size; i ++)
      rawData[i][i] = 1;
  }

  void transitiveClosure() {
    int[][] temp = copyData();
    int[][] result = copyData();
    boolean change = true;

    while (change) {
      change = multiplyBooleanMatrix(rawData, temp, result);
      copyMatrix(result, rawData);
      copyMatrix(result, temp);
    }
  }

  public BitMatrix build(Vector relation) {
    rawData = initMatrix();
    for (int i = 0; i < relation.size(); i ++) {
      Subtype clause = (Subtype) relation.elementAt(i);
      Record subclass = clause.getSubclass();
      Record superclass = clause.getSuperclass();
      rawData[subclass.getUnique()][superclass.getUnique()] = 1;
    }

    reflexiveClosure();
    transitiveClosure();

    // printData();

    return new BitMatrix(rawData, size).pack();
  }
}
