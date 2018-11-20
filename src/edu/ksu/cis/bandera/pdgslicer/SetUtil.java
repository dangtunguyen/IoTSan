package edu.ksu.cis.bandera.pdgslicer;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999   Hongjun Zheng (zheng@cis.ksu.edu)      *
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

import java.util.BitSet;
import ca.mcgill.sable.util.*;
import ca.mcgill.sable.soot.jimple.*;
/**
 * This class contains some utilities for operations of 
 * {@link BitSet BitSet} and {@link Set Set}.
 * <br> Currently, all methods in this class are 
 * <code>static</code>.
 */
public class SetUtil {

/**
 * Clear elements of a bit set in terms of a ruler bit set.
 * <p>
 * @return result bit set.
 * @param orignalSet a bit set need to be cleared by a ruler.
 * @param notSet ruler bit set.
 */
static BitSet bitSetAndNot(BitSet originalSet, BitSet notSet) {
	BitSet cloneOrig = (BitSet) originalSet.clone();
	for (int i = 0; i < notSet.size(); i++)
		if (notSet.get(i))
			cloneOrig.clear(i);
	return cloneOrig;
}
/**
 * Transform a bit set of statements into a set of {@link Stmt Stmt}.
 * <p>
 * @return a set of {@link Stmt Stmt} which is corresponding to 
 * the <code>bitSet</code>.
 * @param bitSet a bit set of statements indexed with <code>stmtList</code>.
 * @param stmtList statement list.
 */
public static Set bitSetToStmtSet(BitSet bitSet, StmtList stmtList) {
	Set stmtSet = new ArraySet();
	for (int i = 0; i < bitSet.size(); i++) {
		if (!bitSet.get(i))
			continue;
		stmtSet.add((Stmt) stmtList.get(i));
	}
	return stmtSet;
}
/**
 * See if a bit set is empty
 * <p>
 * @return <code>true</code> if it is empty; <code>false</code> otherwise.
 * @param bs query bit set.
 */
public static boolean emptyBitSet(BitSet bs) {
	for (int i = 0; i < bs.size(); i++)
		if (bs.get(i))
			return false;
	return true;
}
/**
 * See if a bit set is empty
 * <p>
 * @return <code>true</code> if it is empty; <code>false</code> otherwise.
 * @param bs query bit set.
 */
public static boolean emptyBitSetWithLength(BitSet bs, int length) {
	for (int i = 0; i < length; i++)
		if (bs.get(i))
			return false;
	return true;
}
/**
 * Insert the method's description here.
 * Creation date: (00-12-6 11:01:22)
 * @param bs java.util.BitSet
 */
public static void initializeBitSetToAllFalse(BitSet bs) {
	for (int i = 0; i < bs.size(); i++)
		bs.clear(i);
}
/**
 * Insert the method's description here.
 * Creation date: (00-12-6 11:01:22)
 * @param bs java.util.BitSet
 */
public static void initializeBitSetToAllTrue(BitSet bs) {
	for (int i = 0; i < bs.size(); i++)
		bs.set(i);
}
/**
 * Get logical size of a bit set. Logical size means
 * how many element in a bit set is set to <code>true</code>.
 * <p>
 * @return logical size of the bit set.
 * @param bs query bit set.
 */
static int logicalSizeOfBitSet(BitSet bs) {
	int size = 0;
	for (int i=0; i<bs.size(); i++)
	 if (bs.get(i)) size++;
	return size;
}
/**
 * Get intersection of two {@link Set Set}.
 * <p>
 * @return intersection of two Set.
 * @param set1 first set.
 * @param set2 second set.
 */
public static Set setIntersection(Set set1, Set set2) {
	Set returnSet = new ArraySet();
	for (Iterator i = set1.iterator(); i.hasNext();)
	{
		Object obj = (Object) i.next();
		if (set2.contains(obj)) returnSet.add(obj);
	}
	return returnSet;
}
/**
 * Transform an array of {@link Stmt Stmt} to a bit set indexed
 * with given statement list.
 * <p>
 * @return a bit set of statement indexed with <code>localStmtList</code>.
 * @param stmtSet a statement set for transforming.
 * @param localStmtList a statement list for indexing.
 */
static BitSet stmtArrayToBitSet(Stmt[] stmtSet, StmtList localStmtList) {
	BitSet bitSetOfStmt = new BitSet(localStmtList.size());
	for (int stmtIt = 0; stmtIt < stmtSet.length; stmtIt++) {
		Stmt stmt = stmtSet[stmtIt];
		bitSetOfStmt.set(localStmtList.indexOf(stmt));
	}
	return bitSetOfStmt;
}
/**
 * Transform a {@link Set Set} of {@link Stmt Stmt} to
 * a bit set indexed with a given stmatement list.
 * <p>
 * @return a bit set of statement indexed with <code>localStmtList</code>.
 * @param stmtSet a set of {@link Stmt Stmt}.
 * @param localStmtList statement list for indexing.
 */
static BitSet stmtSetToBitSet(Set stmtSet, StmtList localStmtList) {
	BitSet bitSetOfStmt = new BitSet(localStmtList.size());
	for (Iterator stmtIt = stmtSet.iterator(); stmtIt.hasNext();) {
		Stmt stmt = (Stmt) stmtIt.next();
		bitSetOfStmt.set(localStmtList.indexOf(stmt));
	}
	return bitSetOfStmt;
}
}
