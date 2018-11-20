package ca.mcgill.sable.util;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * SableUtil, a clean room implementation of the Collection API.     *
 * Copyright (C) 1997, 1998 Raja Vallee-Rai (kor@sable.mcgill.ca).   *
 * All rights reserved.                                              *
 *                                                                   *
 * This work was done as a project of the Sable Research Group,      *
 * School of Computer Science, McGill University, Canada             *
 * (http://www.sable.mcgill.ca/).  It is understood that any         *
 * modification not identified as such is not covered by the         *
 * preceding statement.                                              *
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
 * License along with this library; if not, write to the             *
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,      *
 * Boston, MA  02111-1307, USA.                                      *
 *                                                                   *
 * To submit a bug report, send a comment, or get the latest news on *
 * this project and other Sable Research Group projects, please      *
 * visit the web site: http://www.sable.mcgill.ca/                   *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/*
 Reference Version
 -----------------
 This is the latest official version on which this file is based.
 The reference version is: $SableUtilVersion: 1.11 $

 Change History
 --------------
 A) Notes:

 Please use the following template.  Most recent changes should
 appear at the top of the list.

 - Modified on [date (March 1, 1900)] by [name]. [(*) if appropriate]
   [description of modification].

 Any Modification flagged with "(*)" was done as a project of the
 Sable Research Group, School of Computer Science,
 McGill University, Canada (http://www.sable.mcgill.ca/).

 You should add your copyright, using the following template, at
 the top of this file, along with other copyrights.

 *                                                                   *
 * Modifications by [name] are                                       *
 * Copyright (C) [year(s)] [your name (or company)].  All rights     *
 * reserved.                                                         *
 *                                                                   *

 B) Changes:

 - Modified on June 15, 1998 by Raja Vallee-Rai (kor@sable.mcgill.ca). (*)
   First release of this file.
*/

/**
 * <h3>Definition of a ValueObject</h3>
 *
 * <p>A ValueObject is a type of object which cannot be explicitly constructed by the user.
 * The class defines a set of different values.
 *
 * <p>
 * <ul>
 * <li>There are two ways to retrieve an object
 * with a specific value:
 *
 * <ol>
 * <li>Use the v() method.  This returns a ValueObject with representing the given value
 * (the v() method may have arguments).
 * <li>Use an operator method on one or multiple ValueObjects.
 * </ol>
 *
 * <li> In order to determine if two ValueObject objects have the same value, you must use the
 * equals() method, because two different instances may be equivalent.
 *
 * <li>You cannot use constructors to create a ValueObject.
 * <li>The fields of a ValueObject are final.  They cannot be modified.

 * <li>To assign an object x the same value as object y, you simply write x = y.
 * </ul>
 *
 * <h3>Example Usage</h3>
 *
 * <p>Here are some examples of value object uses:
 *
 * <pre>
 * IntSet a = IntSet.v();
 * IntSet b = IntSet.v(5);
 *
 * a = a.union(b);
 * a = a.union(IntSet.v(6));
 *
 * System.out.println(a); // should print out something like {5, 6}
 * </pre>
 *
 * <p>It's also possible to have multiple subclasses of the same ValueObject.
 *
 * <pre>
 * given:
 *    abstract class Type
 *
 *    class IntType extends Type    with IntType.v();
 *    class StringType extends Type with StringType.v();
 *    class ArrayType extends Type  with ArrayType.v(Type t, int dimension);
 * </pre>
 *
 * In this case, there are two type constants, namely IntType.v() and StringType.v(), and one
 * Type which takes two arguments, that is, ArrayType().
 *
 * <h3>Methods of Implementation</h3>
 *
 * <p>There's two different ways of implementing a ValueObject class:
 *
 * <ol>
 * <li>Unique Instance per Value: with this method, each value has a unique object associated
 * with it.  This means comparisons are extremely quick because the equals() method which simply
 * use the == operator on the two objects.  The downside is that you need to maintain a Hashtable
 * of sorts which is checked every time a value is retrieved with the v() method.  Also, memory
 * is not wasted on duplicate objects.
 *
 * <li>Multiple Instances per Value: with this method, the construction of values is much
 * faster because there is no hashtable to go through.  On the other hand, the equals() will
 * be performed more slowly because it will have to construct the structures of both objects, and
 * this method consumes more memory because of possible duplicates.  Memory for the value objects
 * will be de-allocated, however, when the objects are no longer in use, whereas the Unique Instance
 * per Value object cannot use the garbage collector to recuperate the unused ValueObjects because
 * there will always be a reference to the object in the Hashtable.
 * </ol>

 */

public interface ValueObject
{
}
