package edu.ksu.cis.bandera.jjjc.decompiler;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2000 Roby Joehanes (robbyjo@cis.ksu.edu)            *
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
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.util.*;

import edu.ksu.cis.bandera.abstraction.*;
import edu.ksu.cis.bandera.abstraction.util.*;
import edu.ksu.cis.bandera.annotation.*;
import edu.ksu.cis.bandera.jjjc.node.*;

import java.io.*;
import java.util.*;

/**
 * DecompilerSwitch is a recursive analyzer for annotations.
 * The methods are basically each case possible for annotations.
 * Inside annotations, we have statements, which is recursively
 * handled by DecompilerStmtSwitch. Inside each statement, we can have
 * several value chunks, which is recursively handled by
 * DecompilerValueSwitch.
 * @author <a href="mailto:robbyjo@cis.ksu.edu">Roby Joehanes</a>
 * @version 0.4.21
**/
public class DecompilerSwitch extends AnnotationSwitch {
	private static DecompilerSwitch walker = new DecompilerSwitch();
	private Vector result = new Vector();
	private Hashtable lineToAnnotation = new Hashtable();
	private static int tempCounter = 0;

public DecompilerSwitch()
{
	result = new Vector();
}
private void caseBlock(BlockStmtAnnotation a)
{
	for (Enumeration i=a.getContainedAnnotations().elements(); i.hasMoreElements(); )
	{
		Annotation annot = (Annotation) i.nextElement();
		evaluate(annot);
	}
}
/**
 * caseBlockStmtAnnotation method comment.
 */
public void caseBlockStmtAnnotation(BlockStmtAnnotation a)
{
	lineToAnnotation.put(new DecompilerPair(result.size()),a);
	caseBlock(a);
}
/**
 * caseBreakStmtAnnotation method comment.
 */
public void caseBreakStmtAnnotation(BreakStmtAnnotation a) {
	lineToAnnotation.put(new DecompilerPair(result.size()),a);
	result.add("break;");
}
/**
 * caseCatchAnnotation method comment.
 */
public void caseCatchAnnotation(CatchAnnotation a) {
	StringBuffer s = new StringBuffer();
	Stmt[] stmts = a.getStatements();
	int max = stmts.length;

	lineToAnnotation.put(new DecompilerPair(result.size()),a);
	s.append("catch (");
	for (int i=0; i<max; i++)
	{
		if (stmts[i] instanceof IdentityStmt)
		{
			IdentityStmt ids = (IdentityStmt) stmts[i];
			Value lhs  = ids.getLeftOp();
			String left = DecompilerUtil.flattenToString(DecompilerValueSwitch.evaluate(lhs));
			DecompilerValueSwitch.reset();
			String right= DecompilerUtil.flattenToString(DecompilerValueSwitch.evaluate(ids.getRightOp()));
			DecompilerValueSwitch.reset();
			if (right.equals("@caughtexception"))
			{
				s.append(lhs.getType().toString()+" "+left);
			}
		}
	}
	s.append(") {");
	result.add(s.toString());
	caseBlock(a);
	result.add("}");
}
/**
 * caseClassDeclarationAnnotation method comment.
 */
public void caseClassDeclarationAnnotation(ClassDeclarationAnnotation a) {
	lineToAnnotation.put(new DecompilerPair(result.size()),a);
	// Neglected
}
/**
 * caseConstructorDeclarationAnnotation method comment.
 */
public void caseConstructorDeclarationAnnotation(ConstructorDeclarationAnnotation a) {
	Enumeration i=a.getContainedAnnotations().elements();

	Annotation an;

	lineToAnnotation.put(new DecompilerPair(result.size()),a);
	if ((!DecompilerInfo.isStaticMethod()) && (i.hasMoreElements()))
	{
		an = (Annotation) i.nextElement();
		evaluate(an);
	}

	if ((DecompilerInfo.getNoOfParam()>0) && (i.hasMoreElements()))
	{
		an = (Annotation) i.nextElement();
		DecompilerInfo.decipherParam(an.getStatements());
	}

	if (!DecompilerInfo.hasField()) DecompilerUtil.setAllowField(false);
	// Is there init helper?
	SootMethod sm = null;
	try {
		sm = a.getSootMethod().getDeclaringClass().getMethod("<initHelper>");
	} catch (Exception e)
	{
		sm = null;
	}
	if ((sm==null) && (i.hasMoreElements()) && (DecompilerInfo.hasField()))
	{
		// If there is no init helper, the next annotation has to be field-initialization.
		//DecompilerUtil.setAllowField(true);
		an = (Annotation) i.nextElement();
		evaluate(an);
		DecompilerUtil.setAllowField(false);
	}

	// Process the rest.
	while (i.hasMoreElements())
	{
		an = (Annotation) i.nextElement();
		evaluate(an);
	}

	if (result.size()==0)
	{
		Hashtable assgVar = DecompilerUtil.getAssignedVarTable();
	}
}
/**
 * caseContinueStmtAnnotation method comment.
 */
public void caseContinueStmtAnnotation(ContinueStmtAnnotation a)
{
	// Still murky... dunno about this one
	// result.add(a.toString().trim()+";");
	lineToAnnotation.put(new DecompilerPair(result.size()),a);
	result.add("continue;");
}
/**
 * caseDoWhileStmtAnnotation method comment.
 */
public void caseDoWhileStmtAnnotation(DoWhileStmtAnnotation a)
{
	StringBuffer s = new StringBuffer();
	Stmt[] testStmts = a.getTestStatements();

	result.addAll(DecompilerUtil.dumpStmt(testStmts));
	
	lineToAnnotation.put(new DecompilerPair(result.size()),a);
	result.add("do {");
	evaluate(a.getBlockAnnotation());
	s.append("} while ");
	s.append(DecompilerUtil.analyzeCondition(testStmts,2));
	s.append(";");
	result.add(s.toString());
}
/**
 * caseEmptyStmtAnnotation method comment.
 */
public void caseEmptyStmtAnnotation(EmptyStmtAnnotation a) {
	// An empty statement, do nothing
	lineToAnnotation.put(new DecompilerPair(result.size()),a);
}
/**
 * caseExpStmtAnnotation method comment.
 */
public void caseExpStmtAnnotation(ExpStmtAnnotation a) {
	lineToAnnotation.put(new DecompilerPair(result.size()),a);
	caseSequential(a);
}
/**
 * caseFieldDeclarationAnnotation method comment.
 */
public void caseFieldDeclarationAnnotation(FieldDeclarationAnnotation a)
{
	// Already processed in decompiling classes,
	// no need to further process this one
	lineToAnnotation.put(new DecompilerPair(result.size()),a);
}
/**
 * caseForStmtAnnotation method comment.
 */
public void caseForStmtAnnotation(ForStmtAnnotation a)
{
	Annotation initAn = a.getInitAnnotation();
	Stmt[] initStmts = null;
	if (initAn != null)
		initStmts = initAn.getStatements();
	Stmt[] testStmts = a.getTestStatements();
	Stmt[] updateStmts = a.getUpdateAnnotation().getStatements();

	if (DecompilerUtil.isDebugInfo())
	{
		result.add("// Init statements");
		result.addAll(DecompilerUtil.dumpStmt(initStmts));
		result.add("// Test statements");
		result.addAll(DecompilerUtil.dumpStmt(testStmts));
		result.add("// Update statements");
		result.addAll(DecompilerUtil.dumpStmt(updateStmts));
	}

	// Annotate this
	lineToAnnotation.put(new DecompilerPair(result.size()),a);
	DecompilerUtil.resetAssignedVar();
	Vector temp = DecompilerUtil.flattenStmts(initStmts);
	Hashtable assgVar = DecompilerUtil.getAssignedVarTable();
	Hashtable localDecl = a.getDeclaredLocals();
	StringBuffer initBuf = new StringBuffer();
	initBuf.append("for (");
	// If no declared locals, then we simply iterate through the init statements
	if (initStmts != null)
	{
		if (localDecl.size() == 0)
		{
			for (Enumeration e = assgVar.keys(); e.hasMoreElements();)
			{
				String loc = (String) e.nextElement();
				String val = (String) assgVar.get(loc);
				initBuf.append(loc + " = " + val);
				if (e.hasMoreElements())
					initBuf.append(", ");
			}
		} else
		{
			boolean first = true;
			for (Enumeration e = localDecl.keys(); e.hasMoreElements();)
			{
				String locName = (String) e.nextElement();
                    Local theLoc = (Local) localDecl.get(locName);
				String locType = theLoc.getType().toString();
                    locName = (String) theLoc.getName();
				String locVal = (String) assgVar.get(locName);
				if ((locVal == null) || (locVal.length() == 0))
				{
					System.out.println("Error in For: " + locType + " " + locName + " doesn't have value");
				}
				if (first)
				{
					initBuf.append(locType + " ");
					first = false;
				}
				initBuf.append(locName + " = " + locVal);
				if (e.hasMoreElements())
					initBuf.append(", ");
			}
		}
	}
	initBuf.append("; ");

	// Test statement
	String testLine = DecompilerUtil.analyzeCondition(testStmts,1);
	initBuf.append(testLine.substring(1,testLine.length()-1));
	initBuf.append("; ");
	Vector update = DecompilerUtil.flattenStmts(updateStmts);
	for (Enumeration e = update.elements(); e.hasMoreElements();)
	{
		String st = ((String) e.nextElement()).trim();
		initBuf.append(st.substring(0,st.lastIndexOf(";")));
		if (e.hasMoreElements())
			initBuf.append(", ");
	}
	initBuf.append(")");
	result.add(initBuf.toString());
	result.add("{");
	evaluate(a.getBlockAnnotation());
	result.add("}");
}
/**
 * caseIfStmtAnnotation method comment.
 */
public void caseIfStmtAnnotation(IfStmtAnnotation a)
{
	Stmt[] testStmt = a.getTestStatements();
	result.addAll(DecompilerUtil.dumpStmt(testStmt));

	// Not sure yet for the outcome
	lineToAnnotation.put(new DecompilerPair(result.size()),a);

	// Slicer may degenerate the if statement into a bunch of assignments...
	boolean hasIfStmt = false;
	for (int i = 0; i < testStmt.length; i++)
	{
		if (testStmt[i] instanceof IfStmt)
		{
			hasIfStmt = true; break;
		}
	}
	if (hasIfStmt)
	{
		String cond = DecompilerUtil.analyzeCondition(testStmt,1);
		String orig = a.toString();

		result.add("if " + cond);
		result.add("{");
		evaluate(a.getThenAnnotation());

		// Has an else?
		Annotation an = a.getElseAnnotation();
		if (!DecompilerUtil.isEmptyAnnotation(an))
		{
			result.add("} else");
			result.add("{");
			evaluate(an);
		}
		result.add("}");
	} else
	{
		result.addAll(DecompilerUtil.flattenStmts(testStmt));
	}
}
/**
 * caseInstanceInitializerAnnotation method comment.
 */
public void caseInstanceInitializerAnnotation(InstanceInitializerAnnotation a)
{
	lineToAnnotation.put(new DecompilerPair(result.size()),a);
	caseBlock(a);
}
/**
 * caseLabeledStmtAnnotation method comment.
 */
public void caseLabeledStmtAnnotation(LabeledStmtAnnotation a)
{
	lineToAnnotation.put(new DecompilerPair(result.size()),a);
	caseSequential(a);
}
/**
 * caseLocalDeclarationStmtAnnotation method comment.
 */
public void caseLocalDeclarationStmtAnnotation(LocalDeclarationStmtAnnotation a)
{
	StringBuffer s = new StringBuffer();
	String modi = Modifier.toString(a.getModifiers());
	String varType = a.getType().toString().trim();
	Hashtable locDecl = a.getDeclaredLocals();
	if (locDecl.isEmpty()) return; // Probably some bugs in slicing, try to cover up here.
	Hashtable abstractedVars = new Hashtable();

	// Something's get abstracted
	if (Decompiler.typeTable != null)
	{
		for (Enumeration locVar = locDecl.elements(); locVar.hasMoreElements(); )
		{
			Object key = locVar.nextElement();
			Abstraction lt = Decompiler.typeTable.get(key);
			if (((lt instanceof IntegralAbstraction) && !(lt instanceof ConcreteIntegralAbstraction)) ||
				((lt instanceof RealAbstraction) && !(lt instanceof ConcreteRealAbstraction)))
			{
				abstractedVars.put(key, locDecl.get(key));
				locDecl.remove(key);
			}
		}
	}

	lineToAnnotation.put(new DecompilerPair(result.size()),a);
	DecompilerUtil.resetAssignedVar();
	Vector temp = DecompilerUtil.flattenStmts(a.getStatements());
	Hashtable assgVar = DecompilerUtil.getAssignedVarTable();

	while (abstractedVars != null)
	{
		if (modi.length()>0) s.append(modi+" ");
	//   if (varType.equals("boolean")) varType = "int";
		s.append(varType + " ");

		for (Enumeration e = locDecl.keys(); e.hasMoreElements();)
		{
			String locName = (String) e.nextElement();
			String locVal  = (String) assgVar.get(locName);

			s.append(locName);

			// We have a default value here
			if ((locVal!=null) && (locVal.length()>0))
			{
				s.append(" = ");
				locVal = locVal.trim();
				if (varType.equals("boolean"))
				{
					if (locVal.equals("0")) locVal="false";
						else if (locVal.equals("1")) locVal="true";
							else if (locVal.endsWith("? 1 : 0"))
								{
									locVal = locVal.substring(0,locVal.length()-8).trim();
								}
				}
				s.append(locVal);
			}

			if (varType.equals("boolean"))
			{
				// Tell the decompiler that we have a boolean variable here
				DecompilerInfo.putVarInfo(locName);
			}

			if (e.hasMoreElements())
				s.append(", ");
		}
		s.append(";");
		result.add(s.toString());
		if (abstractedVars.isEmpty()) abstractedVars = null;
		else
		{
			locDecl = abstractedVars;
			varType = "int";
			abstractedVars = null;
		}
	}
	for (Enumeration ei = temp.elements(); ei.hasMoreElements(); )
	{
		String st = (String) ei.nextElement();
		if (st.indexOf("quantification") != -1 || st.indexOf("Bandera.choose") != -1)
		{
			result.add(st);
		}
	}
}
/**
 * caseMethodDeclarationAnnotation method comment.
 */
public void caseMethodDeclarationAnnotation(MethodDeclarationAnnotation a)
{
	lineToAnnotation.put(new DecompilerPair(result.size()),a);
	Enumeration i = a.getContainedAnnotations().elements();
	Annotation annot;

	// If it is not a static method, then JJJCTEMP$0 = this;
	if (!DecompilerInfo.isStaticMethod())
	{
		annot = (Annotation) i.nextElement();
		result.addAll(DecompilerUtil.flattenStmts(annot.getStatements()));
	}

	// If there are parameters, the next method would be parameters
	if (DecompilerInfo.getNoOfParam()>0)
	{
		annot = (Annotation) i.nextElement();
		DecompilerInfo.decipherParam(annot.getStatements());
	}

	// The rest are method bodies
	while (i.hasMoreElements())
	{
		annot = (Annotation) i.nextElement();
		evaluate(annot);
	}
}
/**
 * caseReturnStmtAnnotation method comment.
 */
public void caseReturnStmtAnnotation(ReturnStmtAnnotation a) {
	//String result = "return";

	// Get statements
	lineToAnnotation.put(new DecompilerPair(result.size()),a);

	caseSequential(a);
}
private void caseSequential(SequentialAnnotation a)
{
	result.addAll(DecompilerUtil.flattenStmts(a.getStatements()));
}
/**
 * caseSequentialAnnotation method comment.
 */
public void caseSequentialAnnotation(SequentialAnnotation a)
{
	lineToAnnotation.put(new DecompilerPair(result.size()),a);
	caseSequential(a);
}
/**
 * caseStaticInitializerAnnotation method comment.
 */
public void caseStaticInitializerAnnotation(StaticInitializerAnnotation a)
{
	lineToAnnotation.put(new DecompilerPair(result.size()),a);
	caseBlock(a);
}
/**
 * caseSuperConstructorInvocationStmtAnnotation method comment.
 */
public void caseSuperConstructorInvocationStmtAnnotation(SuperConstructorInvocationStmtAnnotation a)
{
	lineToAnnotation.put(new DecompilerPair(result.size()),a);
	caseSequential(a);
}
/**
 * caseSwitchStmtAnnotation method comment.
 */
public void caseSwitchStmtAnnotation(SwitchStmtAnnotation a)
{
	Stmt[] testStmt = a.getTestStatements();
	Hashtable switchTbl = a.getSwitchCases();
	LookupSwitchStmt ls = null;
	Abstraction lt = null;
	boolean found = false;
	boolean isAbstracted = false;

	lineToAnnotation.put(new DecompilerPair(result.size()), a);
    if (Decompiler.typeTable != null)
    {
    	for (int i = 0; i < testStmt.length; i++)
        {
            if (testStmt[i] instanceof LookupSwitchStmt)
            {
                ls = (LookupSwitchStmt) testStmt[i];
                Value v = ls.getKey();
                if (v != null)
                {
                    lt = Decompiler.typeTable.get(v);
                    isAbstracted = DecompilerUtil.isAbstracted(lt);
                    if (isAbstracted)
                    {
                        tempCounter++;
                        result.add("int abs_temp$"+tempCounter+";");
                    }
                }
                break;
            }
        }
    }

	Vector res = DecompilerUtil.flattenStmts(testStmt);
	Hashtable definedVars = DecompilerUtil.getAssignedVarTable();
	for (Enumeration e = res.elements(); e.hasMoreElements();)
	{
		String st = (String) e.nextElement();
		if (st.startsWith("switch "))
		{
            if (st.endsWith(";")) st = st.substring(0, st.length()-1).trim();
			result.add(st); found = true; break;
		}
	}
	if (!found)
	{
		result.add("// Error: Switch statement not found!");
		result.add("switch ()");
	}
	result.add("{");

	if (isAbstracted)
	{
		// Make a silly exception on abstracted code.... please don't complain....
		Hashtable annToResult = new Hashtable();
		Hashtable annToValue = new Hashtable();
		Hashtable valueToAnn = new Hashtable();
		Vector tempRes = result;
		Vector caseValues = new Vector();
		// Put the each case annotation with its result in a table
		for (Enumeration e = a.getValues().elements(); e.hasMoreElements();)
		{
			result = new Vector();
			Value v = (Value) e.nextElement();
			Annotation caseAnn = (Annotation) switchTbl.get(v);
			evaluate(caseAnn);
			annToResult.put(caseAnn,result);
			annToValue.put(caseAnn,v.toString());
			valueToAnn.put(v.toString(),caseAnn);
			if (v instanceof IntConstant) caseValues.add(new Integer(((IntConstant) v).value));
		}
		result = tempRes;
		// Determine the constant for default
		int defaultInt = 0;
		while (true)
		{
			if (caseValues.contains(new Integer(defaultInt)))
			{
				defaultInt++; // If the constant clashes, increment one by one
			} else
			{
				// We found one!!
				tempRes = result;
				result = new Vector();
				Annotation defAnn = (Annotation) a.getDefaultAnnotation();
				evaluate(defAnn);
				annToResult.put(defAnn,result);
				annToValue.put(defAnn,String.valueOf(defaultInt));
				valueToAnn.put(String.valueOf(defaultInt),defAnn);
				result = tempRes;
				break;
			}
		}

		// Look into the abstracted cases
		int max = ls.getLookupValues().size();
		for(int i = 0; i < max; i++)
		{
			String tokName = AbstractionClassLoader.getTokenName(lt,ls.getLookupValue(i));
			String fullName = lt.getClass().getName();
			int lastDot = fullName.lastIndexOf(".");
			String pkgName = fullName.substring(0,lastDot);
			Decompiler.addImports(pkgName+".*");
			result.add("case "+tokName+":");

			// Find the position of target in the test statements
			int fromPos = DecompilerUtil.stmtIndex((Stmt) ls.getTarget(i),testStmt);
			int toPos = i == (max-1) ? testStmt.length : DecompilerUtil.stmtIndex((Stmt) ls.getTarget(i+1),testStmt);

			if (fromPos >= 0 && toPos >= 0)
			{
				switchPartialDecompilation(fromPos,toPos,testStmt,annToValue,definedVars);
			}
		}
		// Default target is neglected by default

		result.add("}");
		// Add the second switch
		result.add("switch (abs_temp$"+tempCounter+")");
		result.add("{");
		for (Enumeration e = valueToAnn.keys(); e.hasMoreElements(); )
		{
			String theVal = (String) e.nextElement();
			Annotation theAnn = (Annotation) valueToAnn.get(theVal);
			Vector currentResult = (Vector) annToResult.get(theAnn);
			result.add("case "+theVal+":");
			result.addAll(currentResult);
		}
	} else
	{
		for (Enumeration e = a.getValues().elements(); e.hasMoreElements();)
		{
			Object v = e.nextElement();
			result.add("case " + v + ":");
			evaluate((Annotation) switchTbl.get(v));
		}
		Annotation def = a.getDefaultAnnotation();
		if (def != null)
		{
			result.add("default:");
			evaluate(def);
		}
	}
	result.add("}");
}
/**
 * caseSynchronizedStmtAnnotation method comment.
 */
public void caseSynchronizedStmtAnnotation(SynchronizedStmtAnnotation a)
{
	Stmt[] lockStmt = a.getLockStatements();
	boolean found = false;

	lineToAnnotation.put(new DecompilerPair(result.size()),a);
	Vector res = DecompilerUtil.flattenStmts(lockStmt);
	for (Enumeration e = res.elements(); e.hasMoreElements(); )
	{
		String st = ((String) e.nextElement()).trim();
		if (st.startsWith("synchronized "))
		{
			result.add(st.substring(0,st.lastIndexOf(";")).trim()); found = true;
			break;
		}
	}
	if (!found)
	{
		result.add("// Error: Synchronized statement not found!");
		result.add("synchronized (this)");
	}
	result.add("{");
	evaluate(a.getBlockAnnotation());
	result.add("}");
}
/**
 * caseThisConstructorInvocationStmtAnnotation method comment.
 */
public void caseThisConstructorInvocationStmtAnnotation(ThisConstructorInvocationStmtAnnotation a)
{
	lineToAnnotation.put(new DecompilerPair(result.size()),a);
	caseSequential(a);
}
/**
 * caseThrowStmtAnnotation method comment.
 */
public void caseThrowStmtAnnotation(ThrowStmtAnnotation a)
{
	lineToAnnotation.put(new DecompilerPair(result.size()),a);
	caseSequential(a);
}
/**
 * caseTryFinallyStmtAnnotation method comment.
 */
public void caseTryFinallyStmtAnnotation(TryFinallyStmtAnnotation a) {

	//  Dunno about getFinallyExceptionAnnotation
	lineToAnnotation.put(new DecompilerPair(result.size()),a);
	caseTryStmtAnnotation(a);
	result.add("finally {");
	evaluate(a.getFinallyAnnotation());
	result.add("}");
}
/**
 * caseTryStmtAnnotation method comment.
 */
public void caseTryStmtAnnotation(TryStmtAnnotation a)
{
	Stmt[] cstmts = ((Annotation) a.getCatchClauses().firstElement()).getStatements();
	// Are they got sliced away?
	if (cstmts.length == 0) return;
	lineToAnnotation.put(new DecompilerPair(result.size()),a);
	if ("JJJCTEMP$E".equals(((IdentityStmt) cstmts[0]).getLeftOp().toString().trim())) {
		evaluate(a.getBlockAnnotation());
		// add synchronized to method modifier
	}
	else
	{
		result.add("try {");
		evaluate(a.getBlockAnnotation());
		result.add("}");
		for (Enumeration e = a.getCatchClauses().elements(); e.hasMoreElements();)
		{
			Annotation clause = (Annotation) e.nextElement();
			evaluate(clause);
		}
	}
}
public void caseUnimplemented(Annotation a)
{
	result.add(a.toString() + "; // Unimplemented yet!");
}
/**
 * caseWhileStmtAnnotation method comment.
 */
public void caseWhileStmtAnnotation(WhileStmtAnnotation a) {
	StringBuffer s = new StringBuffer();
	Stmt[] testStmts = a.getTestStatements();

	result.addAll(DecompilerUtil.dumpStmt(testStmts));
	lineToAnnotation.put(new DecompilerPair(result.size()),a);
	s.append("while ");
	s.append(DecompilerUtil.analyzeCondition(testStmts,0));
	result.add(s.toString());
	result.add("{");
	evaluate(a.getBlockAnnotation());
	result.add("}");
}
public static Vector evaluate(Annotation a){
	if (a==null) return new Vector();
	a.apply(walker);
	return walker.getResult();
}
public static Hashtable getLineToAnnotation() { return walker.getTable(); }
private Vector getResult()
{
	return result;
}
private Hashtable getTable() { return lineToAnnotation; }
public static void reset()
{
	walker.result = new Vector();
	walker.lineToAnnotation = new Hashtable();
}

private void switchPartialDecompilation(int fromPos, int toPos, Stmt[] testStmt, Hashtable annToValue, Hashtable definedVars)
{
	boolean prevIf = false;
	for (int j = fromPos; j < toPos; j++)
	{
		if (testStmt[j] instanceof GotoStmt)
		{
			Stmt target = (Stmt) ((GotoStmt) testStmt[j]).getTarget();
			try
			{
				Annotation an = Decompiler.annot.getContainingAnnotation(target);
				String annval = (String) annToValue.get(an);
				String tempstr = "abs_temp$" + tempCounter + " = " + annval + ";";
				if (prevIf)
					result.add("else " + tempstr);
				else
					result.add(tempstr);
				result.add("break;");
				prevIf = false;
			} catch (Exception e)
			{
				System.out.println("Warning: Cannot find containing annotation in abstracted switch!");
			}
		} else
		{
			if (testStmt[j] instanceof IfStmt)
			{
				IfStmt ifs = (IfStmt) testStmt[j];
				Value cond = ifs.getCondition();
				if (cond instanceof EqExpr)
				{
					System.out.println("Warning: Slabs choose condition is not equality expression!");
				} else cond = ((EqExpr) cond).getOp1();
				String condstr = (String) definedVars.get(cond);
				try
				{
					if (condstr == null) throw new Exception();
					Stmt target = (Stmt) ifs.getTarget();
					Annotation an = Decompiler.annot.getContainingAnnotation(target);
					String annval = (String) annToValue.get(an);
					condstr = "if (" + condstr + ") abs_temp$" + tempCounter + " = " + annval + ";";
					if (prevIf)
						result.add("else " + condstr);
					else
						result.add(condstr);
					prevIf = true;
				} catch (Exception e)
				{
					System.out.println("Warning: Abstracted if in lookup switch has bugs!");
				}
			}
		}
	}
}
}
