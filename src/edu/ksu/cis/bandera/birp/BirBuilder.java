package edu.ksu.cis.bandera.birp;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999   James Corbett (corbett@hawaii.edu)     *
 *               2001, 2002   Radu Iosif (iosif@cis.ksu.edu)         *
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
import edu.ksu.cis.bandera.bir.*;
import edu.ksu.cis.bandera.bir.Collection;
import edu.ksu.cis.bandera.birp.analysis.*;
import edu.ksu.cis.bandera.birp.node.*;
import edu.ksu.cis.bandera.birp.parser.*;
import edu.ksu.cis.bandera.birp.lexer.*;

import java.io.*;
import java.util.*;

public class BirBuilder extends DepthFirstAdapter implements BirConstants
{
	TransSystem system;
	Hashtable globalVars;
	Hashtable localVars;
	Hashtable threadLocs;
	Hashtable threadLocTables;
	Hashtable threadLocalVarTables;
	Hashtable threads;
	Vector refTypes = new Vector();
	BirThread currentThread;
	Location currentLoc;
	ActionVector currentActions;
	Enumerated currentEnumerated;
	Stack recordStack = new Stack();
	Record currentRecord;
	PrintAction currentPrintAction;
	ThreadAction currentThreadAction;
	boolean error;
	boolean inPredicate = false;
	String fileName;

	public BirBuilder(String fileName) {
		this.fileName = fileName;
	}


	LengthExpr arrayLength(Expr array, Token len) {
		Type arrayType = array.getType();

		if (arrayType.isKind(REF))
			arrayType = ((Ref)arrayType).getTargetType();

		if (! arrayType.isKind(ARRAY))
			error(len, "prefix of .length not array");

		if (array.getType().isKind(ARRAY))
			return new LengthExpr(array);
		else
			return new LengthExpr(new DerefExpr(array));
	}


	ArrayExpr arraySelect(Expr array, Expr index, Token bracket) {
		Type arrayType = array.getType();

		if (arrayType.isKind(REF))
			arrayType = ((Ref)arrayType).getTargetType();

		if (! arrayType.isKind(ARRAY))
			error(bracket, "prefix of [] not array");

		if (! index.getType().isKind(RANGE))
			error(bracket, "index of [] not range type");

		if (array.getType().isKind(ARRAY))
			return new ArrayExpr(array, index);
		else
			return new ArrayExpr(new DerefExpr(array), index);
	}


	void checkDecl(Token ident) {
		String name = ident.getText();

		Object existingDecl = (currentThread != null) ? 
				localVars.get(name) : globalVars.get(name);

				if (existingDecl != null) 
					error(ident, "redefinition of name: " + name);
	}


	StateVar declareVar(Token ident, Type type, Expr initVal) {
		String name = ident.getText();

		if ((initVal != null) && ! type.containsValue(initVal))
			error(ident, "bad initial value: " + initVal);

		checkDecl(ident);

		String key = (currentThread == null) ? name : 
			currentThread.getName() + "." + name;

		Expr initValue = (initVal == null) ? type.defaultVal() : initVal;
		StateVar var = system.declareVar(key, name, currentThread,
				type, initValue);
		/* [Thomas, May 23, 2017]
		 * Add null check
		 * */
		if(var != null)
		{
			if (currentThread != null) {
				currentThread.addLocal(var);
				localVars.put(name, var);
			} else 
				globalVars.put(name, var);
		}

		return var;
	}

	StateVar declareParam(Token ident, Type type) {
		String name = ident.getText();
		checkDecl(ident);

		String key = currentThread.getName() + "." + name;
		StateVar var = system.getVarOfKey(key);
		if (var != null)
			throw new RuntimeException("Variable already declared: " + name);

		name = system.createName(key, name);
		var = new StateVar(name, currentThread, type, null, system);

		system.putVarOfKey(key, var);
		system.setSource(var, key);
		// A parameter is also a local
		currentThread.addLocal(var);
		localVars.put(name, var);

		return var;
	}

	void error(Token tok, String msg) {
		throw new RuntimeException(fileName + ":" + tok.getLine() + ": " + msg);

		//  System.out.println(fileName + ":" + tok.getLine() + ": " + msg);
		//  error = true;
		//  return null;    
	}


	RecordExpr fieldSelect(Expr rec, Token id) {
		String name = id.getText();
		Type recType = rec.getType();
		if (recType.isKind(REF))
			recType = ((Ref)recType).getTargetType();

		if (! recType.isKind(RECORD))
			error(id, "prefix of " + name + " is not record type");

		Field field = ((Record)recType).getField(name);
		if (field == null)
			error(id, "field " + name + " not found");

		if (rec.getType().isKind(RECORD)) 
			return new RecordExpr(rec, field);
		else 
			return new RecordExpr(new DerefExpr(rec), field);
	}


	Expr getDecl(Token ident) {
		String name = ident.getText();
		if (currentThread != null) {
			Object var = localVars.get(name);
			if (var != null)
				return (StateVar) var;
		}

		Object var = globalVars.get(name);
		if (var != null)
			if ((var instanceof StateVar) || (var instanceof Constant))
				return (Expr) var;
			else
				error(ident, "not a value: " + name);

		error(ident, "undefined name: " + name);
		return null;
	}

	// sablecc visitor actions
	public void inAEmptyLiveset(AEmptyLiveset node)
	{
		currentLoc.setLiveVars(new StateVarVector(1));
	}


	public void inAEnumeratedTypespec(AEnumeratedTypespec node)
	{
		currentEnumerated = system.enumeratedType();
	}


	public void inALocation(ALocation node)
	{
		String label = node.getId().getText();
		currentLoc = (Location) threadLocs.get(label);
		currentLoc.setLabel(label);
	}


	public void inANonemptyLiveset(ANonemptyLiveset node)
	{
		currentLoc.setLiveVars(new StateVarVector());
		StateVar var = (StateVar) getDecl(node.getFirst());

		if (var.getThread() == null)
			error(node.getFirst(), "variables in live clause must be local");
		else
			currentLoc.getLiveVars().addElement(var);
	}


	public void inAPredicates(APredicates node)
	{
		inPredicate = true;
	}


	public void inAPrintaction(APrintaction node)
	{
		currentPrintAction = new PrintAction();
	}

	public void inAProcess(AProcess node)
	{
		String name = node.getStartname().getText();

		system = new TransSystem(name);
		globalVars = new Hashtable();
		threads = new Hashtable();
		threadLocTables = new Hashtable();
		threadLocalVarTables = new Hashtable();

		// Create all threads so they can reference each other
		Object threadNodes[] = node.getThreads().toArray();

		for(int i = 0; i < threadNodes.length; i++) {
			AThread threadNode = (AThread) threadNodes[i];
			String threadName = threadNode.getStartname().getText();
			/* [Thomas, May 21, 2017]
			 * Add one more parameter to the function call createThread
			 * */
			threads.put(threadName, 
					system.createThread(threadName, threadName, threadName)); 
		}
	}


	public void inARecordTypespec(ARecordTypespec node)
	{
		recordStack.push(currentRecord);
		currentRecord = system.recordType();    
	}


	public void inAThread(AThread node)
	{
		// When we first encounter a thread declaration,
		// go back and resolve the targets of the ref types
		if (refTypes != null) {
			for (int i = 0; i < refTypes.size(); i++) {
				Ref refType = (Ref) refTypes.elementAt(i);
				refType.resolveTargets();
			}

			refTypes = null;
		}

		// Set up the context for building the thread
		localVars = new Hashtable();
		threadLocs = new Hashtable();
		String name = node.getStartname().getText();
		ALocation startLocation = (ALocation) node.getStartloc();
		String startLabel = startLocation.getId().getText();
		currentThread = system.threadOfKey(name); 
		Location startLoc = 
				system.locationOfKey(startLabel + "#" + name, currentThread);
		currentThread.setStartLoc(startLoc);
		threadLocs.put(startLabel, startLoc);
		if (node.getMain() != null)
			currentThread.setMain(true);

		// Create all the locations 
		Object locs[] = node.getLocations().toArray();
		for(int i = 0; i < locs.length; i++) {
			ALocation aloc = (ALocation) locs[i];
			String locLabel = aloc.getId().getText();
			if (threadLocs.get(locLabel) != null)
				error(aloc.getId(), "loc " + locLabel 
						+ " defined more than once");
			Location loc = 
					system.locationOfKey(locLabel + "#" + name, currentThread);
			threadLocs.put(locLabel,loc);
		}

		threadLocTables.put(currentThread,threadLocs);
		threadLocalVarTables.put(currentThread,localVars);
	}


	public void inATransformation(ATransformation node)
	{
		currentActions = new ActionVector();
	}


	public void inStart(Start node)
	{
		error = false;
	}


	public void outAAllocation(AAllocation node)
	{
		Expr arg = getDecl(node.getId());
		if (! (arg instanceof StateVar))
			error(node.getId(), "'new' can be invoked only on collections");

		StateVar collection = (StateVar) arg;
		if (! collection.getType().isKind(COLLECTION))
			error(node.getId(), "'new' can be invoked only on collections");

		Expr var = (Expr) getOut(node.getLhs());
		if (! var.getType().isKind(REF))
			error(node.getId(), "lhs of 'new' assignment must be reference");
		else if (! ((Ref)var.getType()).getTargets().contains(collection))
			error(node.getId(), "ref cannot point to collection allocated");

		Expr rhs;
		Collection colType = (Collection) collection.getType();
		if (node.getArraylength() != null) {
			if (! colType.getBaseType().isKind(ARRAY))
				error(node.getId(),"not an array collection");
			Expr arrayLen = (Expr) getOut(node.getArraylength());
			rhs = new NewArrayExpr(collection,arrayLen);
		} else {
			if (! colType.getBaseType().isKind(RECORD))
				error(node.getId(),"not an record collection");
			rhs = new NewExpr(collection);
		}

		setOut(node, new AssignAction(var, rhs));
	}


	public void outAAllocationAction(AAllocationAction node)
	{
		Action action = (Action) getOut(node.getAllocation());
		currentActions.addElement(action);
	}


	public void outAAndExpr6(AAndExpr6 node)
	{
		Expr arg1 = (Expr) getOut(node.getExpr6());
		Expr arg2 = (Expr) getOut(node.getExpr5());

		setOut(node, new AndExpr(arg1, arg2));

		if (! (arg1.getType().isKind(BOOL) && arg2.getType().isKind(BOOL)))
			error(node.getAnd(), "arguments to && must be boolean");
	}


	public void outAArraylength(AArraylength node)
	{
		setOut(node, getOut(node.getExpr()));
	}


	public void outAArraylengthExpr0(AArraylengthExpr0 node)
	{
		Expr array = (Expr) getOut(node.getExpr0());
		setOut(node, arrayLength(array,node.getLength()));        
	}


	public void outAArraylengthLhs(AArraylengthLhs node)
	{
		Expr array = (Expr) getOut(node.getLhs());
		setOut(node, arrayLength(array,node.getLength()));        
	}


	public void outAArrayselectExpr0(AArrayselectExpr0 node)
	{
		Expr array = (Expr) getOut(node.getExpr0());
		Expr index = (Expr) getOut(node.getExpr());
		setOut(node, arraySelect(array,index,node.getLbrack()));
	}


	public void outAArrayselectLhs(AArrayselectLhs node)
	{
		Expr array = (Expr) getOut(node.getLhs());
		Expr index = (Expr) getOut(node.getExpr());
		setOut(node, arraySelect(array,index,node.getLbrack()));
	}


	public void outAArrayTypespec(AArrayTypespec node)
	{
		Type baseType = (Type) getOut(node.getType());
		ConstExpr size = (ConstExpr) getOut(node.getConst());
		setOut(node, system.arrayType(baseType, size));
	}


	public void outAAssertaction(AAssertaction node)
	{
		Expr cond = (Expr) getOut(node.getExpr());
		if (! cond.getType().isKind(BOOL))
			error(node.getAssert(),"assert expression must be boolean");

		setOut(node, new AssertAction(cond));
	}


	public void outAAssertactionAction(AAssertactionAction node)
	{
		Action action = (Action) getOut(node.getAssertaction());
		currentActions.addElement(action);
	}


	public void outAAssignment(AAssignment node)
	{
		Expr var = (Expr) getOut(node.getLhs());
		Expr expr = (Expr) getOut(node.getExpr());

		setOut(node, new AssignAction(var, expr));

		if (! var.getType().compatibleWith(expr.getType()))
			error(node.getAssign(), "incompatible type in assignment");

		if (! var.getType().isKind(RANGE|BOOL|ENUMERATED|REF))
			error(node.getAssign(), "only range, boolean, enumerated, and ref types can be assigned");
	}


	public void outAAssignmentAction(AAssignmentAction node)
	{
		Action action = (Action) getOut(node.getAssignment());
		currentActions.addElement(action);
	}


	public void outAAtlocationThreadtest(AAtlocationThreadtest node)
	{
		if (! inPredicate)
			error(node.getThreadname(),
					"Thread location tests can only be used in predicates");

		String threadName = node.getThreadname().getText();
		BirThread thread = (BirThread)threads.get(threadName);
		if (thread == null)
			error(node.getThreadname(), "thread not yet declared: " + 
					threadName);

		String locLabel = node.getLocname().getText();
		Hashtable threadLocTable = (Hashtable) threadLocTables.get(thread);
		Location loc = (Location) threadLocTable.get(locLabel);

		if (loc == null)
			error(node.getLocname(), "no location " + locLabel + " in thread " 
					+ threadName);

		Expr lhs = (Expr) getOut(node.getLhs());
		setOut(node, new ThreadLocTest(loc, lhs));
	}


	public void outABooleanTypespec(ABooleanTypespec node)
	{
		setOut(node, system.booleanType());
	}


	public void outATidTypespec(ATidTypespec node)
	{
		setOut(node, system.tidType());
	}


	public void outABoolValue(ABoolValue node)
	{
		setOut(node, getOut(node.getBool()));
	}

	private void setValues(Token tok, ChooseExpr choose, Object[] temp)
	{
		for(int i = 0; i < temp.length; i++) {
			Expr choice = (Expr) getOut(((AChoicetail) temp[i]).getValue());
			if (choice.getType() != choose.getType()) 
				error(tok, "incompatible types in choose: ");
			choose.addChoice(choice);
		}
	}

	public void outAInternChoice(AInternChoice node)
	{
		Expr lhs = (Expr) getOut(node.getLhs());
		Expr firstChoice = (Expr) getOut(node.getValue());
		InternChooseExpr choose = new InternChooseExpr(firstChoice);
		Object temp[] = node.getRest().toArray();
		setValues(node.getInternchoose(), choose, temp);
		setOut(node, new AssignAction(lhs, choose));
	}


	public void outAExternChoice(AExternChoice node)
	{
		Expr lhs = (Expr) getOut(node.getLhs());
		Expr firstChoice = (Expr) getOut(node.getValue());
		ExternChooseExpr choose = new ExternChooseExpr(firstChoice);
		Object temp[] = node.getRest().toArray();
		setValues(node.getExternchoose(), choose, temp);
		setOut(node, new AssignAction(lhs, choose));
	}


	public void outAForallChoice(AForallChoice node)
	{
		Expr lhs = (Expr) getOut(node.getLhs());
		Object decl = globalVars.get(node.getId().getText());

		if (! lhs.getType().isKind(REF))
			error(node.getForall(),
					"lhs of assignment must be of a reference type");

		if ((decl == null) ||
				!((decl instanceof Record) ||
						(decl instanceof Array)))
			error(node.getForall(), 
					"undefined aggregate type " + node.getId().getText());
		else 
			setOut(node, new AssignAction(lhs, new ForallExpr((Type) decl, system)));
	}


	public void outAChoiceAction(AChoiceAction node)
	{
		Action action = (Action) getOut(node.getChoice());
		currentActions.addElement(action);
	}


	public void outACollection(ACollection node)
	{
		Type baseType = (Type) getOut(node.getType());
		ConstExpr size = (ConstExpr) getOut(node.getConst());
		Collection collection = system.collectionType(baseType,size);
		declareVar(node.getId(), collection, null);
	}


	public void outAConstantDefinition(AConstantDefinition node)
	{
		String proposedName = node.getId().getText();
		checkDecl(node.getId());
		int val = parseInt(node.getInt().getText());
		String name = system.createName(null, proposedName);
		Constant constant = new Constant(name, val, Type.intType);
		globalVars.put(name, constant);
		system.define(name, constant);
	}


	public void outADefinedType(ADefinedType node)
	{
		Object def = globalVars.get(node.getId().getText());
		if (def == null) 
			error(node.getId(), "type undefined: " + node.getId().getText());
		else if (!( def instanceof Type))
			error(node.getId(), "not a type: " + node.getId().getText());
		else
			setOut(node, def);
	}


	public void outADivExpr2(ADivExpr2 node)
	{
		Expr arg1 = (Expr) getOut(node.getExpr2());
		Expr arg2 = (Expr) getOut(node.getExpr1());
		setOut(node, new DivExpr(arg1, arg2));

		if (! (arg1.getType().isKind(RANGE) && arg2.getType().isKind(RANGE)))
			error(node.getDiv(), "arguments to '/' must be integer");
	}


	public void outAEnumeratedTypespec(AEnumeratedTypespec node)
	{
		setOut(node, currentEnumerated);
		currentEnumerated = null;
	}


	public void outAEqExpr5(AEqExpr5 node)
	{
		Expr arg1 = (Expr) getOut(node.getExpr5());
		Expr arg2 = (Expr) getOut(node.getExpr4());

		if (! (arg1.getType().isKind(BOOL|RANGE|ENUMERATED|REF) 
				&& arg2.getType().isKind(BOOL|RANGE|ENUMERATED|REF)))
			error(node.getEq(), "arguments to == must be ref, boolean, range, or enumerated");

		setOut(node, new EqExpr(arg1, arg2));
	}


	public void outAExpr(AExpr node)
	{
		setOut(node, getOut(node.getExpr7()));
	}


	public void outAExpr0Expr1(AExpr0Expr1 node)
	{
		setOut(node, getOut(node.getExpr0()));	
	}


	public void outAExpr1Expr2(AExpr1Expr2 node)
	{
		setOut(node, getOut(node.getExpr1()));	
	}


	public void outAExpr2Expr3(AExpr2Expr3 node)
	{
		setOut(node, getOut(node.getExpr2()));	
	}


	public void outAExpr3Expr4(AExpr3Expr4 node)
	{
		setOut(node, getOut(node.getExpr3()));	
	}


	public void outAExpr4Expr5(AExpr4Expr5 node)
	{
		setOut(node, getOut(node.getExpr4()));	
	}


	public void outAExpr5Expr6(AExpr5Expr6 node)
	{
		setOut(node, getOut(node.getExpr5()));	
	}


	public void outAExpr6Expr7(AExpr6Expr7 node)
	{
		setOut(node, getOut(node.getExpr6()));	
	}


	public void outAFalseBool(AFalseBool node)
	{
		setOut(node, new BoolLit(false));
	}


	public void outAField(AField node)
	{
		String name = node.getId().getText();
		Type type = (Type) getOut(node.getType());
		currentRecord.addField(name,type);
	}


	public void outAFieldselectExpr0(AFieldselectExpr0 node)
	{
		setOut(node, fieldSelect((Expr) getOut(node.getExpr0()),
				node.getId()));
	}


	public void outAFieldselectLhs(AFieldselectLhs node)
	{
		setOut(node, fieldSelect((Expr) getOut(node.getLhs()),
				node.getId()));
	}


	public void outAGeExpr4(AGeExpr4 node)
	{
		Expr arg1 = (Expr) getOut(node.getExpr4());
		Expr arg2 = (Expr) getOut(node.getExpr3());

		setOut(node, new LeExpr(arg2, arg1));

		if (! (arg1.getType().isKind(RANGE | ENUMERATED) 
				&& arg2.getType().isKind(RANGE | ENUMERATED)))
			error(node.getGe(), "arguments to '>=' must be integer");
	}


	public void outAGtExpr4(AGtExpr4 node)
	{
		Expr arg1 = (Expr) getOut(node.getExpr4());
		Expr arg2 = (Expr) getOut(node.getExpr3());

		setOut(node, new LtExpr(arg2, arg1));

		if (! (arg1.getType().isKind(RANGE | ENUMERATED) 
				&& arg2.getType().isKind(RANGE | ENUMERATED)))
			error(node.getGt(), "arguments to '>' must be integer");
	}


	public void outAHaslockLocktestop(AHaslockLocktestop node)
	{
		setOut(node, new Integer(HAS_LOCK));
	}


	public void outAIdConst(AIdConst node)
	{
		Object def = globalVars.get(node.getId().getText());

		if (def == null) 
			error(node.getId(), "constant undefined: " + node.getId().getText());
		else if (!( def instanceof Constant))
			error(node.getId(), "not a constant: " + node.getId().getText());
		else
			setOut(node, def);
	}


	public void outAIdValue(AIdValue node)
	{
		setOut(node, getDecl(node.getId()));
	}


	public void outAInitializer(AInitializer node)
	{
		if (node.getAssign() != null) 
			setOut(node, getOut(node.getValue()));
		else
			setOut(node, null);
	}


	public void outAInstanceofExpr0(AInstanceofExpr0 node)
	{
		Expr refExpr = (Expr) getOut(node.getExpr0());
		Object decl = globalVars.get(node.getId().getText());

		if (! refExpr.getType().isKind(REF))
			error(node.getInstanceof(), 
					"first argument of 'instanceof' must be a reference");

		if ((decl == null) || 
				!((decl instanceof Record) || 
						(decl instanceof Array)))
			error(node.getInstanceof(), 
					"second argument of 'instanceof' must be an aggregate type");
		else 
			setOut(node, new InstanceOfExpr(refExpr, (Type) decl));
	}


	public void outAIntConst(AIntConst node)
	{
		setOut(node, new IntLit(parseInt(node.getInt().getText())));
	}


	public void outAIntegerValue(AIntegerValue node)
	{
		setOut(node, new IntLit(parseInt(node.getInt().getText())));
	}


	//    public void outAJoinThreadop(AJoinThreadop node)
	//    {
	//      setOut(node, new Integer(JOIN));
	//    }


	public void outALeExpr4(ALeExpr4 node)
	{
		Expr arg1 = (Expr) getOut(node.getExpr4());
		Expr arg2 = (Expr) getOut(node.getExpr3());

		setOut(node, new LeExpr(arg1, arg2));

		if (! (arg1.getType().isKind(RANGE | ENUMERATED) 
				&& arg2.getType().isKind(RANGE | ENUMERATED)))
			error(node.getLe(), "arguments to '<=' must be integer");
	}


	public void outALivevar(ALivevar node)
	{
		StateVar var = (StateVar) getDecl(node.getId());
		if (var.getThread() == null)
			error(node.getId(), "variables in live clause must be local");
		else
			currentLoc.getLiveVars().addElement(var);
	}


	public void outALocation(ALocation node)
	{
		currentLoc = null;
	}


	public void outALockavailableLocktestop(ALockavailableLocktestop node)
	{
		setOut(node, new Integer(LOCK_AVAILABLE));
	}


	public void outALockLockOp(ALockLockOp node)
	{
		setOut(node, new Integer(LOCK));
	}


	public void outALocktest(ALocktest node)
	{
		Expr lock = (Expr) getOut(node.getLhs());
		int operation = ((Integer)getOut(node.getLocktestop())).intValue();
		setOut(node, new LockTest(lock, operation, currentThread));
	}


	public void outALocktestExpr0(ALocktestExpr0 node)
	{
		setOut(node, getOut(node.getLocktest()));
	}


	public void outALockTypespec(ALockTypespec node)
	{
		boolean wait = (node.getWait() != null);
		boolean reentrant = (node.getReentrant() != null);
		setOut(node, system.lockType(wait,reentrant));
	}


	public void outALockupdate(ALockupdate node)
	{
		Expr lock = (Expr) getOut(node.getLhs());
		int operation = ((Integer)getOut(node.getLockOp())).intValue();
		setOut(node, new LockAction(lock, operation, currentThread));
	}


	public void outALockupdateAction(ALockupdateAction node)
	{
		Action action = (Action) getOut(node.getLockupdate());
		currentActions.addElement(action);
	}


	public void outALtExpr4(ALtExpr4 node)
	{
		Expr arg1 = (Expr) getOut(node.getExpr4());
		Expr arg2 = (Expr) getOut(node.getExpr3());

		setOut(node, new LtExpr(arg1, arg2));

		if (! (arg1.getType().isKind(RANGE | ENUMERATED) 
				&& arg2.getType().isKind(RANGE | ENUMERATED)))
			error(node.getLt(), "arguments to '<' must be integer");
	}


	public void outAMinusExpr1(AMinusExpr1 node)
	{
		Expr arg1 = new IntLit(0);
		Expr arg2 = (Expr) getOut(node.getExpr1());

		setOut(node, new SubExpr(arg1, arg2));	

		if (! arg1.getType().isKind(RANGE))
			error(node.getMinus(), "argument to '-' must be integer");
	}


	public void outAMinusExpr3(AMinusExpr3 node)
	{
		Expr arg1 = (Expr) getOut(node.getExpr3());
		Expr arg2 = (Expr) getOut(node.getExpr2());

		setOut(node, new SubExpr(arg1, arg2));

		if (! (arg1.getType().isKind(RANGE) && arg2.getType().isKind(RANGE)))
			error(node.getMinus(), "arguments to '-' must be integer");
	}


	public void outAModExpr2(AModExpr2 node)
	{
		Expr arg1 = (Expr) getOut(node.getExpr2());
		Expr arg2 = (Expr) getOut(node.getExpr1());

		setOut(node, new RemExpr(arg1, arg2));

		if (! (arg1.getType().isKind(RANGE) && arg2.getType().isKind(RANGE)))
			error(node.getMod(), "arguments to '%' must be integer");
	}


	public void outAMultExpr2(AMultExpr2 node)
	{
		Expr arg1 = (Expr) getOut(node.getExpr2());
		Expr arg2 = (Expr) getOut(node.getExpr1());

		setOut(node, new MulExpr(arg1, arg2));

		if (! (arg1.getType().isKind(RANGE) && arg2.getType().isKind(RANGE)))
			error(node.getMult(), "arguments to '*' must be integer");
	}


	public void outANameEnumconst(ANameEnumconst node)
	{
		checkDecl(node.getId());
		String name = system.createName(null, node.getId().getText());
		Constant constant = currentEnumerated.add(name);
		globalVars.put(name, constant);
	}


	public void outANamevalueEnumconst(ANamevalueEnumconst node)
	{
		checkDecl(node.getId());
		String name = system.createName(null, node.getId().getText());
		int value = parseInt(node.getInt().getText());
		Constant constant = currentEnumerated.add(name, value);
		globalVars.put(name, constant);
	}


	public void outANoteqExpr5(ANoteqExpr5 node)
	{
		Expr arg1 = (Expr) getOut(node.getExpr5());
		Expr arg2 = (Expr) getOut(node.getExpr4());

		if (! (arg1.getType().isKind(BOOL|RANGE|ENUMERATED|REF) 
				&& arg2.getType().isKind(BOOL|RANGE|ENUMERATED|REF)))
			error(node.getNoteq(), "arguments to != must be ref, boolean, range, or enumerated");

		setOut(node, new NeExpr(arg1, arg2));
	}


	public void outANotExpr1(ANotExpr1 node)
	{
		Expr arg1 = (Expr)getOut(node.getExpr1());

		setOut(node, new NotExpr(arg1));

		if (! arg1.getType().isKind(BOOL))
			error(node.getNot(), "argument to ! must be boolean");
	}


	public void outANotifyallLockOp(ANotifyallLockOp node)
	{
		setOut(node, new Integer(NOTIFYALL));
	}


	public void outANotifyLockOp(ANotifyLockOp node)
	{
		setOut(node, new Integer(NOTIFY));
	}


	public void outANullValue(ANullValue node)
	{
		setOut(node, new NullExpr(system));
	}


	public void outAOrExpr7(AOrExpr7 node)
	{
		Expr arg1 = (Expr) getOut(node.getExpr7());
		Expr arg2 = (Expr) getOut(node.getExpr6());

		setOut(node, new OrExpr(arg1, arg2));

		if (! (arg1.getType().isKind(BOOL) && arg2.getType().isKind(BOOL)))
			error(node.getOr(), "arguments to || must be boolean");
	}


	public void outAParenexprExpr0(AParenexprExpr0 node)
	{
		setOut(node, getOut(node.getExpr()));
	}


	public void outAPlusExpr1(APlusExpr1 node)
	{
		setOut(node, getOut(node.getExpr1()));
	}


	public void outAPlusExpr3(APlusExpr3 node)
	{
		Expr arg1 = (Expr) getOut(node.getExpr3());
		Expr arg2 = (Expr) getOut(node.getExpr2());

		setOut(node, new AddExpr(arg1, arg2));

		if (! (arg1.getType().isKind(RANGE) && arg2.getType().isKind(RANGE)))
			error(node.getPlus(), "arguments to '+' must be integer");
	}


	public void outAPredicate(APredicate node)
	{
		String predName = node.getId().getText();
		Expr predExpr = (Expr) getOut(node.getExpr());
		system.declarePredicate(predName,predExpr);
	}


	public void outAPrintaction(APrintaction node)
	{
		setOut(node,currentPrintAction);
		currentPrintAction = null;
	}


	public void outAPrintactionAction(APrintactionAction node)
	{
		Action action = (Action) getOut(node.getPrintaction());
		currentActions.addElement(action);
	}


	public void outAProcess(AProcess node)
	{
		String name = node.getStartname().getText();
		String endName = node.getEndname().getText();
		if (! name.equals(endName))
			error(node.getEndname(),
					"process " + name + " ended with end " + endName);
		system.numberLocations();
		setOut(node, system);
	}


	public void outAProgram(AProgram node)
	{
		setOut(node, getOut(node.getProcess()));
	}


	public void outARangeTypespec(ARangeTypespec node) {
		ConstExpr lo = (ConstExpr) getOut(node.getLow());
		ConstExpr hi = (ConstExpr) getOut(node.getHi());
		setOut(node, system.rangeType(lo,hi));
	}


	public void outARecordTypespec(ARecordTypespec node)
	{
		setOut(node, currentRecord);
		system.addType(currentRecord);
		try { 
			currentRecord = (Record) recordStack.pop(); 
		} catch (EmptyStackException e) { 
			currentRecord = null; 
		}
	}


	public void outARefTypespec(ARefTypespec node)
	{
		Ref refType = system.refType();
		refType.addTarget(node.getFirst().getText());
		Object temp[] = node.getRest().toArray();

		for(int i = 0; i < temp.length; i++)
			refType.addTarget(((AReftail) temp[i]).getId().getText());

		refTypes.addElement(refType);
		setOut(node, refType);
	}


	public void outARefValue(ARefValue node)
	{
		Expr arg = getDecl(node.getId());

		if (! (arg instanceof StateVar))
			error(node.getId(), "'ref' can be invoked only on a variable");

		StateVar singleton = (StateVar) arg;

		if (! singleton.getType().isKind(ARRAY|RECORD))
			error(node.getId(), "'ref' can be invoked only on arrays/records");

		setOut(node, new RefExpr(singleton));
	}


	public void outARemoterefExpr0(ARemoterefExpr0 node)
	{
		if (! inPredicate)
			error(node.getThreadname(),
					"Remote references can only be used in predicates");

		String threadName = node.getThreadname().getText();
		BirThread thread = (BirThread)threads.get(threadName);
		if (thread == null)
			error(node.getThreadname(), "thread not declared: " + threadName);

		Expr lhs = (Expr) getOut(node.getLhs());    
		String localName = node.getLocalname().getText();
		Hashtable threadLocalVars =(Hashtable) threadLocalVarTables.get(thread);
		Object var = threadLocalVars.get(localName);
		if (var == null || !(var instanceof StateVar))
			error(node.getLocalname(),"invalid local: " + localName);

		setOut(node, new RemoteRef(lhs, (StateVar) var));
	}


	//    public void outAStartThreadop(AStartThreadop node)
	//    {
	//      setOut(node, new Integer(START));
	//    }


	public void outAStringPrintarg(AStringPrintarg node)
	{
		String s = node.getString().getText();
		currentPrintAction.addPrintItem(s.substring(1,s.length() - 1));
	}


	public void outASubtype(ASubtype node)
	{
		String subId = node.getSubclass().getText();
		String superId = node.getSuperclass().getText();
		Object subclass = globalVars.get(subId);
		Object superclass = globalVars.get(superId);

		if ((subclass == null) 
				|| !(subclass instanceof Record))
			error(node.getExtendz(), 
					"first argument of 'extends' must be a record type");

		if ((superclass == null) 
				|| !(superclass instanceof Record))
			error(node.getExtendz(),
					"second argument of 'extends' must be a record type");

		system.subtype((Record) subclass, (Record) superclass);
	}


	public void outATerminatedThreadtest(ATerminatedThreadtest node)
	{
		//    BirThread thread = (BirThread) threads.get(node.getId().getText());
		//    if (thread == null)
		//      error(node.getId(), "thread not yet declared: " + 
		//	    node.getId().getText());

		int operation = THREAD_TERMINATED;
		Expr lhs = (Expr) getOut(node.getLhs());
		setOut(node, new ThreadTest(lhs, operation, currentThread));
	}


	public void outAThread(AThread node)
	{
		String name = node.getStartname().getText();
		String endName = node.getEndname().getText();
		if (! name.equals(endName))
			error(node.getEndname(),
					"thread " + name + " ended with end " + endName);
		localVars = null;
		currentThread = null;	
		threadLocs = null;
	}


	public void outAThreadtestExpr0(AThreadtestExpr0 node)
	{
		setOut(node, getOut(node.getThreadtest()));
	}


	public void outAStartarg(AStartarg node)
	{
		Expr arg = (Expr) getOut(node.getExpr());
		currentThreadAction.addActual(arg);
	}


	public void outAThreadupdateAction(AThreadupdateAction node)
	{
		Action action = (Action) getOut(node.getThreadupdate());
		currentActions.addElement(action);
	}


	public void inAStartThreadupdate(AStartThreadupdate node)
	{
		currentThreadAction = new ThreadAction();
	}


	public void outAStartThreadupdate(AStartThreadupdate node)
	{
		if (node.getLhsAssign() != null) {
			Expr lhs = (Expr) getOut(((ALhsAssign) node.getLhsAssign()).getLhs());
			currentThreadAction.setLhs(lhs);
		}

		String id = node.getId().getText();
		BirThread thread = (BirThread) threads.get(id);
		if (thread == null)
			error(node.getId(), "thread not yet declared: " + id);

		int formals = thread.getParameters().size();
		int actuals = currentThreadAction.getActuals().size();
		if (formals != actuals)
			error(node.getId(), "start of thread " + id + " with " 
					+ actuals + " parameters instead of " + formals);

		currentThreadAction.setOperation(START);
		currentThreadAction.setThread(currentThread);
		currentThreadAction.setThreadArg(thread);
		setOut(node, currentThreadAction);

		currentThreadAction = null;
	}


	public void outAExitThreadupdate(AExitThreadupdate node)
	{
		setOut(node, new ThreadAction(EXIT, currentThread));
	}


	public void outATransformation(ATransformation node)
	{
		Location toLoc = (Location) threadLocs.get(node.getId().getText());
		if (toLoc == null)
			error(node.getId(), "target of goto not found: " + 
					node.getId().getText());

		Expr guard = (Expr) getOut(node.getExpr());
		if (! guard.getType().isKind(BOOL)) 
			error(node.getWhen(), "guard expression must be boolean" +
					node.getExpr());

		Transformation trans = currentLoc.addTrans(toLoc,guard,currentActions);
		trans.setVisible(node.getInvisible() == null);
		currentActions = null;
	}


	public void outATrueBool(ATrueBool node)
	{
		setOut(node, new BoolLit(true));
	}


	public void outATypedefDefinition(ATypedefDefinition node)
	{
		String name = node.getId().getText();
		checkDecl(node.getId());

		Type type = (Type) getOut(node.getTypespec());
		type.setName(name);
		globalVars.put(name, type);
		system.define(name, type);
	}


	public void outATypespecType(ATypespecType node)
	{
		setOut(node, getOut(node.getTypespec()));
	}


	public void outAUnlockLockOp(AUnlockLockOp node)
	{
		setOut(node, new Integer(UNLOCK));
	}


	public void outAUnwaitLockOp(AUnwaitLockOp node)
	{
		setOut(node, new Integer(UNWAIT));
	}


	public void outAValueExpr0(AValueExpr0 node)
	{
		setOut(node, getOut(node.getValue()));        
	}


	public void outAValueLhs(AValueLhs node)
	{
		setOut(node, getDecl(node.getId()));
	}


	public void outAVariable(AVariable node)
	{
		Expr initVal = (node.getInitializer() == null) ? null : 
			(Expr) getOut(node.getInitializer());
		declareVar(node.getId(), (Type) getOut(node.getType()), initVal);
	}


	public void outAParameter(AParameter node)
	{
		StateVar var = declareParam(node.getId(), (Type) getOut(node.getType()));
		currentThread.addParameter(var);
	}


	public void outAVarPrintarg(AVarPrintarg node)
	{
		Expr arg = getDecl(node.getId());
		if (! (arg instanceof StateVar))
			error(node.getId(), "can only print variables");
		StateVar var = (StateVar) arg;
		if (! var.getType().isKind(BOOL|RANGE|ENUMERATED))
			error(node.getId(), "can only print boolean, range, or enum types");
		currentPrintAction.addPrintItem(var);	
	}


	public void outAWaitLockOp(AWaitLockOp node)
	{
		setOut(node, new Integer(WAIT));
	}


	public void outAWasnotifiedLocktestop(AWasnotifiedLocktestop node)
	{
		setOut(node, new Integer(WAS_NOTIFIED));
	}


	public void outStart(Start node)
	{
		setOut(node, getOut(node.getPProgram()));
	}


	public static TransSystem parse(String birSource) {
		TransSystem result = null;
		try {
			String fileName = birSource + ".bir";
			File birFile = new File(fileName);	  
			FileInputStream streamIn = new FileInputStream(birFile);
			InputStreamReader streamRead = new InputStreamReader(streamIn);
			PushbackReader pushRead = new PushbackReader(streamRead, 1024);
			Lexer lex = new Lexer(pushRead);
			Parser parser = new Parser(lex);
			Start tree = parser.parse();
			BirBuilder builder = new BirBuilder(fileName);
			tree.apply(builder);
			result = (TransSystem) builder.getOut(tree);
			streamIn.close();
			return result;
		} catch(IOException e) {
			throw new RuntimeException("Could not open BIR source file (" + e + ")");
		} catch(LexerException e) {
			System.out.println("Lexical error:\n" + e);
		} catch(ParserException e) {
			System.out.println("Syntax error:\n" + e);
		}
		return null;
	}  


	int parseInt(String s) {
		try {
			int result = Integer.parseInt(s);
			return result;
		} catch (NumberFormatException e) {
			throw new RuntimeException("Integer didn't parse: " + s);
		}
	}
}
