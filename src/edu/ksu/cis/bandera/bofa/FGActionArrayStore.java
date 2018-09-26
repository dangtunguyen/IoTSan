/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999                                          *
 * John Hatcliff (hatcliff@cis.ksu.edu)
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

package edu.ksu.cis.bandera.bofa;

import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.util.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/*
 * FGActionArrayStore.java
 * $Id: FGActionArrayStore.java,v 1.3 2002/04/01 00:52:46 rvprasad Exp $
 */

/**
 * This class describes the action to be taken when new values arrive at the base position of an
 * array reference that appears on the LHS of an assignment statement.
 *
 * @author <A HREF="http://www.cis.ksu.edu/~hatcliff">John Hatcliff</A>
 * @author <a href="http://www.cis.ksu.edu/~rvprasad">Venkatesh Prasad Ranganath</a>
 *
 * @version $Name:  $($Revision: 1.3 $)
 */
public class FGActionArrayStore implements FGAction
{
	private static final Logger logger = LogManager.getLogger(FGActionArrayStore.class);

	/* This FGNode will have an incoming arc from the node for the expression on the RHS of the
	 * assignment statement. All values from this node should flow into the contents of any array
	 * variant associated with the array reference base node tagged by this action. */

	/**
	 * Node for the array store AST.
	 */
	private FGNode arrayStoreNode;

	/**
	 * ClassToken corresponding to the array.
	 */
	private ClassTokenArray refClassTokenArray;

	/**
	 * Set of array variants that have been connected into the flow graph for the base node tagged by
	 * this action.
	 */
	private Set installedArrayVariants;

	/**
	 * For every value variant that flows into the AST Node annotated by this action, we want to find
	 * the corresponding array variant node, and hook it into the flowgraph as a receiver node so
	 * that all values flowing into the AST Node annotated by this action will flow into the set of
	 * memory cells abstracted by the array variant.
	 *
     * @param refClassTokenArray the class token associated with the array type of the reference
     * node.
	 * @param arrayStoreNode the array reference AST node.
	 */
	public FGActionArrayStore(ClassTokenArray refClassTokenArray,
							  FGNode arrayStoreNode)
    {
		this.refClassTokenArray     = refClassTokenArray;
		this.arrayStoreNode         = arrayStoreNode;
		this.installedArrayVariants = new HashSet();
    }

	/**
	 * Hooks in the array reference node into the flow graph as a consumer node.
	 *
	 * @param values triggered the action.
	 * @see FGActionArrayRef#doAction FGActionArrayRef.doAction
	 */
	public void doAction(FASet values)
    {
		ClassToken      classToken;
		ArrayVariant    arrayVariant;

		/* Iterate through the given set of value variants */
		for(Iterator i = values.iterator(); i.hasNext();) {
			ValueVariant valueVariant = (ValueVariant) i.next();

			/*
			 * First, extract the class token from the value variant.  If the class token is not an
			 * array then we have a type-checking error.  Such errors may actually be due to
			 * imprecision in the analysis.  I have not observed this, but the possibility exists and
			 * is mentioned in Grove's thesis.  For now, we will raise an exception if such a thing
			 * happens.
			 *
			 * If the class token is an array class we could also type check with the array class
			 * given by refClassTokenArray.  Ignore this for now.
			 *
			 * If the class token is an array class, then call the array variant manager to select
			 * the appropriate array variant.  For now, let's lookup the array variant using the
			 * ClassToken in the value variant.
			 *
			 * If the array variant is not already installed here, - hook the node abstracting the
			 * contents of the array into the flowgraph.  - generate work to move ALL values at the
			 * array ref AST node to the array variant node.  If the array variant is already
			 * installed, then values flowing into the array ref node will automatically be pushed
			 * into the variant by the normal processing of flowgraph node successors.
			 */

			classToken = valueVariant.getClassToken();
			if (!(classToken instanceof ClassTokenArray)) {
				// Null values may flow into arrays, but they should not be used to get array components.
				if (classToken == ClassTokenSimple.nullClassToken) {
					continue;
				} // end of if (classToken.equals(ClassTokenManaged.nullToken))

				RuntimeException e = new RuntimeException("BOFA: non-array object flowing into  array ref site" + classToken);
				logger.debug("This happens at assignments or at argument pass sites.", e);
				throw e;
			}

			// Select the appropriate array variant
			arrayVariant = ArrayVariantManager.select(classToken, valueVariant);

			if (!installedArrayVariants.contains(arrayVariant)) {

				// hook array variant into the flowgraph
				FGNode arrayNode = arrayVariant.getNode();
				// make value flow arc from the array reference AST node to the array cell
				// summary(node).
				FGNode.makeArc(arrayStoreNode, arrayNode);
				FA.workList.insert(new FGWorkSendVals(arrayStoreNode.getValues(), arrayNode));

				// Add the array variant to the list of variants installed at this node.
				installedArrayVariants.add(arrayVariant);
			} /* end: if */
		} /* end: for each valueVariant */
    }
}
