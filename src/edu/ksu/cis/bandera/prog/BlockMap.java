package edu.ksu.cis.bandera.prog;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999   Shawn Laubach (laubach@acm.org)        *
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
//BlockMap.java
//import java.io.*;
import ca.mcgill.sable.soot.*;
import java.util.*;
import ca.mcgill.sable.soot.jimple.*;

/**
 * A blockmap structure.  It holds the information for a method that
 * makes a control flow graph.  This holds the data to lookup a block
 * using the index.  It also keeps the local variables and other
 * information needed to create a new method from a block map.
 *
 * @author <a href="mailto:laubach@cis.ksu.edu">Shawn Laubach</a>
 *
 * @version 0.1
 */
public class BlockMap extends HashMap implements Cloneable {
	/**
	   * Option to set block level structure in the basic blocks.
	   */
	public final static int BLOCKLEVEL = 0;

	/**
	   * Option to set statement level structures in the basic blocks.
	   */
	public final static int STMTLEVEL = 1;

	/**
	   * Option to set a hammock CFG when there is only one entrance and
	   * one exit.
	   */
	public final static int HAMMOCK = 2;

	/**
	   * String name for the return variable used in hammock mode.
	   */
	public static String returnVariable = "$RESULT$";
	protected Index initBlock; // Start block
	protected List finalBlocks; // List of final blocks
	protected Map stmtToIndex; // maps statements to their block indexes
	protected JimpleBody listBody; // The jimple body
	protected ca.mcgill.sable.util.List locals; // The list of local variables
	protected StmtList stmtList; // List of statements
	protected SootMethod method; // method that the BlockMap is created from
	protected Map nameToLocal; // Maps string names to locals.
// Current only keeps new locals added 
// elsewhere

/**
   * Constructs a new empty blockmap.
   */
public BlockMap() {
	super();
	stmtToIndex = new HashMap();
	locals = new ca.mcgill.sable.util.VectorList();
	nameToLocal = new HashMap();
	finalBlocks = new ArrayList();
}
  /**
   * Constructs a new blockmap from a method with the flags.
   *
   * @param m the method to create the block map from.
   * @param flags these are the flags that specify statement level,
   * block level, and hammock configuration.
   */
  public BlockMap(SootMethod m, int flags)
  {
	this();

	boolean hammock = (flags & HAMMOCK) > 0; // Check if hammock is set
	boolean stmtLevel = (flags & STMTLEVEL) > 0; //Check if statement level

	method = m;

	if (stmtLevel)
	  setupStmtLevel(hammock);
	else
	  setupBlockLevel(hammock);
  }  
  /**
   * Adds a local to the local list.
   *
   * @param l local to add.
   */
  public void addLocal(Local l)
  {
	locals.add(l);
  }  
/**
   * Collapses all the block into a list of statements.  It merges
   * blocks that end with a goto to another block that only has one
   * entry point.  It then puts goto's and other structures as
   * necessesary to ensure correct list construction.
   *
   * @return A list of statements made form the blockmap.
   */
public ca.mcgill.sable.util.List collapse() {
	Vector pending = new Vector();
	ca.mcgill.sable.util.List stmtList = new ca.mcgill.sable.util.ArrayList();
	List visited = new ArrayList();
	Iterator it, jt;
	Index index;
	BasicBlock bb = null, previous = null;
	Map indexToStmt = new HashMap();
	int count = 0;
	setJumps();
	it = keySet().iterator();
	while (it.hasNext()) {
		bb = (BasicBlock) get(it.next());
		indexToStmt.put(bb.getIndex(), bb.get(0));
	}
	pending.addElement(initBlock);
	while (pending.size() > 0) {
		index = (Index) pending.firstElement();
		pending.removeElementAt(0);
		if (!visited.contains(index)) {
			visited.add(index);
			bb = (BasicBlock) get(index);
			if (bb == null)
				System.out.println("Null basic block:  why?");
			else {
				stmtList.addAll(bb.get());
				switch (bb.getSuccs().size()) {
					case 1 :
						if (visited.contains(bb.getSuccs(0)))
							stmtList.add(Jimple.v().newGotoStmt((Stmt) indexToStmt.get(bb.getSuccs(0))));
						else
							pending.insertElementAt(bb.getSuccs(0), 0);
						break;
					case 2 :
						if (!visited.contains(bb.getSuccs(0)))
							pending.addElement(bb.getSuccs(0));
						if (visited.contains(bb.getSuccs(1)))
							stmtList.add(Jimple.v().newGotoStmt((Stmt) indexToStmt.get(bb.getSuccs(1))));
						else
							pending.insertElementAt(bb.getSuccs(1), 0);
						break;
				}
				previous = bb;
			}
		}
	}
	return stmtList;
}
protected void compress() {
	Iterator it;

	// Set the predecessors to all the blocks
	{
		BasicBlock bb1, bb2;
		Index index;
		it = keySet().iterator();
		Iterator jt;
		while (it.hasNext()) {
			bb1 = (BasicBlock) get(it.next());
			jt = bb1.getSuccs().iterator();
			while (jt.hasNext()) {
				bb2 = (BasicBlock) get(jt.next());
				bb2.addPreds(bb1.getIndex());
			}
		}
	}
	Set s = new HashSet();
	s.addAll(keySet());
	it = s.iterator();
	while (it.hasNext()) {
		BasicBlock bb = (BasicBlock) get(it.next());
		if (bb.get().size() == 0) {
			if (bb.getIndex().equals(initBlock))
				initBlock = bb.getSuccs(0);
			BasicBlock tmp;
			Iterator jt;
			jt = bb.getPreds().iterator();
			while (jt.hasNext()) {
				tmp = (BasicBlock) get(jt.next());
				tmp.changeSuccs(bb.getIndex(), bb.getSuccs(0));
			}
			tmp = (BasicBlock) get(bb.getSuccs(0));
			tmp.changePreds(bb.getIndex(), bb.getPreds());
			remove(bb.getIndex());
		}
	}
}
/**
   * Creates a method from the blockmap.  It takes the name,
   * parameters, and the return type and creates the methods.
   *
   * @param name the name of the method.
   * @param params a list of parameters like what soot would take.
   * @param ret the return type of the method.
   *
   * @return The method that can be created from the block map.
   */
public SootMethod createMethod(String name, ca.mcgill.sable.util.List params, Type ret) {
	SootMethod method = new SootMethod(name, params, ret);
	JimpleBody body = (JimpleBody) Jimple.v().newBody(method);
	ca.mcgill.sable.util.Iterator it = locals.iterator();
	while (it.hasNext())
		body.addLocal((Local) it.next());
	ca.mcgill.sable.util.Iterator it2 = collapse().iterator();
	StmtList stmtList = body.getStmtList();
	while (it2.hasNext())
		stmtList.add(it2.next());
	method.storeBody(Jimple.v(), body);
	//Transformations.cleanupCode(body);
	//Transformations.removeUnusedLocals(body);

	return method;
}
  /**
   * Return the list of final blocks.
   *
   * @return List of final blocks.
   */
  public List getFinalBlocks()
  {
	return finalBlocks;
  }  
  /**
   * Get the initial index.
   */
  public Index getInit()
  {
	return initBlock;
  }  
  /**
   * Return the list body made from the method.
   */
  public JimpleBody getListBody()
  {
	return listBody;
  }  
  /**
   * Get a reference to a local.  If it is not there, it creates and
   * adds it.
   *
   * @param name the name of the local.
   */
  public Local getLocal(String name, Type t)
  {
	Map types;

	if ((types = (Map)nameToLocal.get(name)) == null)
	  {
		Map tmp = new HashMap();
		Local l = Jimple.v().newLocal(name, t);
		tmp.put(t, l);
		nameToLocal.put(name, tmp);
		locals.add(l);
	  }
	else
	  if (types.get(t) == null)
	  {
		Local l = Jimple.v().newLocal(name, t);
		types.put(t, l);
		locals.add(l);
	  }
	
	return (Local)((Map)nameToLocal.get(name)).get(t);
  }  
  /**
   * Get the list of locals declared for the block map.
   */
  public ca.mcgill.sable.util.List getLocals()
  {
	return locals;
  }  
  /**
   * Returns the statement to index map.
   */
  public Map getStmtToIndex()
  {
	return stmtToIndex;
  }  
  /**
   * Set the initial index of a block map.
   */
  public void setInit(Index i)
  {
	initBlock = i;
  }  
/**
   * Set all the jumps so that they point to the correct statement.
   * This method is used before any outside analysis because the
   * statements jumps point to can be null because they can be unknown
   * during the creation of a blockmap.  This sets all the statements
   * to the correct points.
   */
protected void setJumps() {
	Iterator it;
	int s;
	Stmt last;
	final Map indexToStmt = new HashMap();
	compress();
	it = keySet().iterator();
	while (it.hasNext()) {
		BasicBlock bb = (BasicBlock) get(it.next());
		indexToStmt.put(bb.getIndex(), bb.get(0));
		stmtToIndex.put(bb.get(0), bb.getIndex());
	}
	it = keySet().iterator();
	while (it.hasNext()) {
		final BasicBlock bb = (BasicBlock) get(it.next());
		AbstractStmtSwitch asw = new AbstractStmtSwitch() {
			public void caseGotoStmt(GotoStmt s) {
				s.setTarget((Stmt) indexToStmt.get(bb.getSuccs(0)));
			}
			public void caseIfStmt(IfStmt s) {
				s.setTarget((Stmt) indexToStmt.get(bb.getSuccs(0)));
			}
		};
		if (bb.getGoOn() != null) {
			Iterator jt = bb.getGoOn().iterator();
			while (jt.hasNext()) {
				last = (Stmt) jt.next();
				last.apply(asw);
			}
		} else {
			last = bb.get(bb.get().size() - 1);
			last.apply(asw);
		}
	}

	// Set the predecessors to all the blocks
	{
		BasicBlock bb1, bb2;
		Index index;
		it = keySet().iterator();
		Iterator jt;
		while (it.hasNext()) {
			bb1 = (BasicBlock) get(it.next());
			jt = bb1.getSuccs().iterator();
			while (jt.hasNext()) {
				bb2 = (BasicBlock) get(jt.next());
				bb2.addPreds(bb1.getIndex());
			}
		}
	}
}
/**
   * Set the list of locals.
   */
public void setLocals(ca.mcgill.sable.util.List l) {
	locals = l;
}
protected void setupBlockLevel(boolean hammock) {
	JimpleBody body = listBody = (JimpleBody) method.getBody(Jimple.v());
	stmtList = body.getStmtList(); // The statements in the body

	Map stmtToName = new HashMap(stmtList.size() * 2 + 1, 0.7f);
	StmtGraph stmtGraph = new BriefStmtGraph(stmtList);
	BasicBlock bb, finalBlock; // current and final basic blocks
	int blockCount = 0;
	Local returnValue = null;
	ca.mcgill.sable.util.Iterator it = body.getLocals().iterator();
	// Add the locals to the locals list
	while (it.hasNext())
		locals.add(it.next());

	// Set up for hammock form
	if (hammock) {
		Stmt returnStmt;

		// Adds to the block index
		finalBlock = new BasicBlock(new Index(blockCount++));

		// Set up the return statement with the correct type
		if (method.getReturnType() instanceof VoidType)
			finalBlock.addStmt(Jimple.v().newReturnVoidStmt());
		else {
			if (method.getReturnType() instanceof BooleanType)
				returnValue = Jimple.v().newLocal(method.getReturnType().toString() + returnVariable, IntType.v());
			else
				returnValue = Jimple.v().newLocal(method.getReturnType().toString() + returnVariable, method.getReturnType());
		finalBlock.addStmt(Jimple.v().newReturnStmt(returnValue));
		locals.add(returnValue);
	}

	// Put statement in the final block
	stmtToIndex.put(finalBlock.get(0), finalBlock.getIndex());
	put(finalBlock.getIndex(), finalBlock);
} else
	finalBlock = null;


// Create statement name table
{
	ca.mcgill.sable.util.Iterator boxIt = body.getUnitBoxes().iterator();
	Set labelStmts = new HashSet();

	// Build labelStmts
	{
		while (boxIt.hasNext()) {
			UnitBox box = (UnitBox) boxIt.next();
			Stmt stmt = (Stmt) box.getUnit();
			labelStmts.add(stmt);
		}
	}

	// Traverse the stmts and assign a label if necessary
	{
		int labelCount = 0;
		ca.mcgill.sable.util.Iterator stmtIt = stmtList.iterator();
		while (stmtIt.hasNext()) {
			Stmt s = (Stmt) stmtIt.next();
			if (labelStmts.contains(s))
				stmtToName.put(s, "label" + (labelCount++));
		}
	}
}
bb = new BasicBlock(new Index(blockCount++));
put(bb.getIndex(), bb);
initBlock = bb.getIndex();
stmtToIndex.put(stmtList.get(0), bb.getIndex());
for (int j = 0; j < stmtList.size(); j++) {
	Stmt s = ((Stmt) stmtList.get(j));

	// Put an empty line if the previous node was a branch node,
	// the current node is a join node or the previous statement
	// does not have this statement as a successor, or if this
	// statement has a label on it
	{
		if (j != 0) {
			Stmt previousStmt = (Stmt) stmtList.get(j - 1);
			if (stmtGraph.getSuccsOf(previousStmt).size() != 1 || stmtGraph.getPredsOf(s).size() != 1 || stmtToName.containsKey(s)) {
				ca.mcgill.sable.util.Iterator succsIterator = stmtGraph.getSuccsOf(previousStmt).iterator();
				while (succsIterator.hasNext()) {
					Stmt stmt = (Stmt) succsIterator.next();
					if (!stmtToIndex.containsKey(stmt))
						stmtToIndex.put(stmt, new Index(blockCount++));
					bb.addSuccs((Index) stmtToIndex.get(stmt));
				}

				// Create the new block and place in the map
				if (!stmtToIndex.containsKey(s))
					stmtToIndex.put(s, new Index(blockCount++));
				bb = new BasicBlock((Index) stmtToIndex.get(s));
				put(bb.getIndex(), bb);
				if (!hammock && stmtGraph.getSuccsOf(s).size() == 0)
					finalBlocks.add(bb.getIndex());
			} else {
				// Or if the previous node does not have this
				// statement as a successor.

				ca.mcgill.sable.util.List succs = stmtGraph.getSuccsOf(previousStmt);
				if (succs.get(0) != s) {
					ca.mcgill.sable.util.Iterator succsIterator = stmtGraph.getSuccsOf(previousStmt).iterator();
					while (succsIterator.hasNext()) {
						Stmt stmt = (Stmt) succsIterator.next();
						if (!stmtToIndex.containsKey(stmt))
							stmtToIndex.put(stmt, new Index(blockCount++));
						bb.addSuccs((Index) stmtToIndex.get(stmt));
					}

					// Create the new block and place in the map
					if (!stmtToIndex.containsKey(s))
						stmtToIndex.put(s, new Index(blockCount++));
					bb = new BasicBlock((Index) stmtToIndex.get(s));
					put(bb.getIndex(), bb);
				}
			}
		}
	}
	{
		if (s instanceof GotoStmt)
			bb.addStmt(s);
		else
			if (s instanceof ReturnStmt && hammock) {
				if (returnValue != null) {
					Stmt stmt = Jimple.v().newAssignStmt(returnValue, ((ReturnStmt) s).getReturnValue());
					bb.addStmt(stmt);
				}
				bb.addSuccs(finalBlock.getIndex());
			} else
				bb.addStmt(s);
	}
}
ca.mcgill.sable.util.Iterator succsIterator = stmtGraph.getSuccsOf((Stmt) stmtList.get(stmtList.size() - 1)).iterator();
while (succsIterator.hasNext()) {
	Stmt stmt = (Stmt) succsIterator.next();
	if (!stmtToIndex.containsKey(stmt))
		stmtToIndex.put(stmt, new Index(blockCount++));
	bb.addSuccs((Index) stmtToIndex.get(stmt));
}

// Set the predecessors to all the blocks
{
	BasicBlock bb1, bb2;
	Index index;
	Iterator it2 = keySet().iterator();
	Iterator jt;
	while (it2.hasNext()) {
		bb1 = (BasicBlock) get(it2.next());
		jt = bb1.getSuccs().iterator();
		while (jt.hasNext()) {
			bb2 = (BasicBlock) get(jt.next());
			bb2.addPreds(bb1.getIndex());
		}
	}
}
}
protected void setupStmtLevel(boolean hammock) {
	JimpleBody body = listBody = (JimpleBody) method.getBody(Jimple.v());
	stmtList = body.getStmtList(); // The statements in the body

	Map stmtToName = new HashMap(stmtList.size() * 2 + 1, 0.7f);
	StmtGraph stmtGraph = new BriefStmtGraph(stmtList);
	BasicBlock bb = null, finalBlock; // current and final basic blocks
	int blockCount = 0;
	Local returnValue = null;
	ca.mcgill.sable.util.Iterator it = body.getLocals().iterator();
	while (it.hasNext())

		
		// Add the locals to the locals list
		locals.add(it.next());

	// Create statement name table
	{
		ca.mcgill.sable.util.Iterator boxIt = body.getUnitBoxes().iterator();
		Set labelStmts = new HashSet();

		// Build labelStmts
		{
			while (boxIt.hasNext()) {
				UnitBox box = (UnitBox) boxIt.next();
				Stmt stmt = (Stmt) box.getUnit();
				labelStmts.add(stmt);
			}
		}

		// Traverse the stmts and assign a label if necessary
		{
			int labelCount = 0;
			ca.mcgill.sable.util.Iterator stmtIt = stmtList.iterator();
			while (stmtIt.hasNext()) {
				Stmt s = (Stmt) stmtIt.next();
				if (labelStmts.contains(s))
					stmtToName.put(s, "label" + (labelCount++));
			}
		}
	}
	if (hammock)

		
		// Set up for hammock form
		{
		Stmt returnStmt;

		// Adds to the block index
		finalBlock = new BasicBlock(new Index(blockCount++));

		// Set up the return statement with the correct type
		if (method.getReturnType() instanceof VoidType)
			finalBlock.addStmt(Jimple.v().newReturnVoidStmt());
		else {
			if (method.getReturnType() instanceof BooleanType)
				returnValue = Jimple.v().newLocal(method.getReturnType().toString() + returnVariable, IntType.v());
			else
				returnValue = Jimple.v().newLocal(method.getReturnType().toString() + returnVariable, method.getReturnType());
		finalBlock.addStmt(Jimple.v().newReturnStmt(returnValue));
		locals.add(returnValue);
	}

	// Put statement in the final block
	stmtToIndex.put(finalBlock.get(0), finalBlock.getIndex());
	put(finalBlock.getIndex(), finalBlock);
} else
	finalBlock = null;
for (int j = 0; j < stmtList.size(); j++) {
	boolean noGoto = false;
	Stmt s = ((Stmt) stmtList.get(j));
	if (!noGoto) {
		if (!stmtToIndex.containsKey(s))
			stmtToIndex.put(s, new Index(blockCount++));
		bb = new BasicBlock((Index) stmtToIndex.get(s));
		ca.mcgill.sable.util.Iterator succsIterator = stmtGraph.getSuccsOf(s).iterator();
		while (succsIterator.hasNext()) {
			Stmt stmt = (Stmt) succsIterator.next();
			if (!stmtToIndex.containsKey(stmt))
				stmtToIndex.put(stmt, new Index(blockCount++));
			bb.addSuccs((Index) stmtToIndex.get(stmt));
		}
	} else {
		noGoto = false;
		stmtToIndex.put(s, bb.getIndex());
		ca.mcgill.sable.util.Iterator succsIterator = stmtGraph.getSuccsOf(s).iterator();
		while (succsIterator.hasNext()) {
			Stmt stmt = (Stmt) succsIterator.next();
			if (!stmtToIndex.containsKey(stmt))
				stmtToIndex.put(stmt, new Index(blockCount++));
			bb.addSuccs((Index) stmtToIndex.get(stmt));
		}
	}
	put(bb.getIndex(), bb);
	if (j == 0)
		initBlock = bb.getIndex();
	stmtToIndex.put(stmtList.get(0), bb.getIndex());
	if (s instanceof GotoStmt)
		noGoto = true;
	else
		if (s instanceof ReturnStmt)
			if (hammock) {
				if (returnValue != null) {
					Stmt stmt = Jimple.v().newAssignStmt(returnValue, ((ReturnStmt) s).getReturnValue());
					bb.addStmt(stmt);
				}
				bb.addSuccs(finalBlock.getIndex());
			} else
				finalBlocks.add(bb);
		else
			bb.addStmt(s);
}

// Set the predecessors to all the blocks
{
	BasicBlock bb1, bb2;
	Index index;
	Iterator it2 = keySet().iterator();
	Iterator jt;
	while (it2.hasNext()) {
		bb1 = (BasicBlock) get(it2.next());
		jt = bb1.getSuccs().iterator();
		while (jt.hasNext()) {
			bb2 = (BasicBlock) get(jt.next());
			bb2.addPreds(bb1.getIndex());
		}
	}
}
}
  public String toString()
  {
	Index index;
	String s = "";
	Iterator i;

	//      setJumps();

	i = keySet().iterator();

	//      s += get(initBlock).toString();
	while (i.hasNext())
	  {
		index = (Index)i.next();
		//	  if (!index.equals(initBlock))
		s += get(index);
	  }

	return s;
  }  
}
