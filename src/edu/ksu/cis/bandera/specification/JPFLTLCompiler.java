package edu.ksu.cis.bandera.specification;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 2001   Roby Joehanes (robbyjo@cis.ksu.edu)          *
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
/**
 * Visitor class to compile pattern for JPF.
 * Creation date: (8/10/01 6:12:38 PM)
 * @author: Roby Joehanes
 */
import java.util.*;
import java.lang.reflect.*;
import java.io.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.Type;
import ca.mcgill.sable.soot.jimple.*;
import edu.ksu.cis.bandera.specification.analysis.*;
import edu.ksu.cis.bandera.specification.datastructure.*;
import edu.ksu.cis.bandera.specification.node.*;
import edu.ksu.cis.bandera.specification.pattern.*;
import edu.ksu.cis.bandera.specification.pattern.datastructure.*;
import edu.ksu.cis.bandera.specification.predicate.node.PPropositionDefinition;
import edu.ksu.cis.bandera.specification.predicate.datastructure.*;
import edu.ksu.cis.bandera.annotation.*;
import edu.ksu.cis.bandera.jext.*;
import edu.ksu.cis.bandera.jjjc.*;
import edu.ksu.cis.bandera.util.*;
//import gov.nasa.arc.ase.jpf.tools.*;
//import gov.nasa.arc.ase.jpf.jvm.*;

public class JPFLTLCompiler extends DepthFirstAdapter {
	private String tempDir = ".";
    private String classpath = ".";
	private TemporalLogicProperty tlp;
	private Hashtable ptab;
	private List qList;
	private Pattern pattern;
	private Iterator patternParamIterator;
	private String currentValue = null;
	private Hashtable patternNodeTable = new Hashtable();
	private Hashtable patternNameTable = new Hashtable();
    private Hashtable qv2Type = new Hashtable();
    private Hashtable qv2Expr = new Hashtable();
    private Hashtable mangledQvTable = new Hashtable();
	private boolean useConstraint;
/**
 * 
 * @param dir java.lang.String To denote output directory where JPF java predicate should be placed
 * @param tl edu.ksu.cis.bandera.specification.datastructure.TemporalLogicProperty The currently active temporal logic property
 * @param q java.util.List The list of quantified variables available.
 */
public JPFLTLCompiler(String dir, TemporalLogicProperty tl, List q)
{
	if (dir != null) tempDir = dir;
	if (tl == null) throw new RuntimeException("Temporal Logic Property may NOT be null!!");
	tlp = tl; qList = q;
}
public void caseABinaryExp(ABinaryExp node)
{
	node.getLeft().apply(this);
	String leftValue = currentValue;
	node.getRight().apply(this);
	String rightValue = currentValue;

	PBinOp binOp = node.getBinOp();
	if (binOp instanceof AAndBinOp) {
		currentValue = "(" + leftValue + " /\\ " + rightValue + ")";
	} else if (binOp instanceof AOrBinOp) {
		currentValue = "(" + leftValue + " \\/ " + rightValue + ")";
	} else {
		currentValue = "(" + leftValue + " -> " + rightValue + ")";
	}
}
public void caseAComplementExp(AComplementExp node)
{
	node.getExp().apply(this);
	if (currentValue == null) currentValue = "(0 != 0)";
	currentValue = "!"+currentValue;
}
public void caseAExpWord(AExpWord node) {
	node.getExp().apply(this);

	try {
		String id = (String) patternParamIterator.next();
		if (currentValue == null) currentValue = "(0 == 0)";
		patternNameTable.put(id, currentValue);
	} catch (Exception e)
	{
		System.out.println("WARNING: Unexpected pattern end!");
	}
}
public void caseAPredicateExp(APredicateExp node) {
	String temp = (String) patternNodeTable.get(node);

	if (temp == null)
	{
		System.out.println("WARNING: Pattern table of "+node+" yields null!");
		temp = "(0 == 0)"; // Injects bogus value
	}
	currentValue = temp;
}
public void compile()
{
	try {
	classpath = System.getProperty("java.class.path");
	ptab = tlp.getPredicatesTable();
	pattern = PatternSaverLoader.getPattern(tlp.getPatternName(), tlp.getPatternScope());
	patternParamIterator = pattern.getParametersOccurenceOrder().iterator();
	LinkedList processedPreds = new LinkedList();

	// Iterate through the predicate table
	for (Enumeration e = ptab.keys(); e.hasMoreElements(); )
	{
		APredicateExp n = (APredicateExp) e.nextElement();

		// We get the expression and its string representation
		Predicate pred = (Predicate) ptab.get(n);

		// Get the declaring class
		String className = pred.getType().getFullyQualifiedName();
		Class theClass = null;
		try {
			theClass = FileClassLoader.load(className);
		} catch (Exception exx)
		{
			throw new RuntimeException("Can't find class " + className);
		}
		java.lang.reflect.Method theMethod = null;
		Annotation ann = pred.getAnnotation();

		// Does this predicate defined above a method?
		SootMethod sootMethod = null;
		if (pred.getMethods() != null)
			sootMethod = (SootMethod) pred.getMethods().get(0);
		else
		{
			if (ann instanceof MethodDeclarationAnnotation)
			sootMethod = ((MethodDeclarationAnnotation) ann).getSootMethod();
		}

		if (sootMethod != null)
		{
			//theMethod = getMatchedMethod(theClass, ((MethodDeclarationAnnotation) ann).getSootMethod());
			theMethod = getMatchedMethod(theClass, sootMethod);
               if (theMethod == null)
               {
                   throw new RuntimeException("Method not found: "+sootMethod);
               }
		}
		// So, at this point we get the class and the corresponding method.

		// Resolve Java predicate naming
		PPropositionDefinition node = (PPropositionDefinition) pred.getNode();
		String predFileName = className;
		if (theMethod != null) predFileName += ("$"+theMethod.getName());
		predFileName += ("$" + node.getId().getText());

		// Is this predicate have been processed? (to avoid rewriting files)
		if (!processedPreds.contains(predFileName))
		{
               // Pre-process variable types used in predicate
               Hashtable predVarTable = new Hashtable();
               Hashtable varUsed = pred.getVariablesUsed();
               for (Enumeration etab = varUsed.keys(); etab.hasMoreElements();)
               {
                   Object o = etab.nextElement();
                   String varName = o.toString().trim();
                   o = varUsed.get(o);
                   if (!(o instanceof Value)) continue; // non-locals, no need to process
                   Value jimpleVal = (Value) o;
                   Type jimpleType = jimpleVal.getType();
                   String jimpleTypeString = jimpleType.toString();
                   // We don't know yet about the arrays, so this may err...
                   Class typeClass = null;
                   try {
                        typeClass = FileClassLoader.load(jimpleTypeString);
                   } catch (Exception exc)
                   {
                       try {
                           typeClass = FileClassLoader.load("java.lang."+jimpleTypeString);
                       } catch(Exception exce)
                       {
                       }
                   }
                   if (typeClass != null) predVarTable.put(varName, typeClass);
               }
			try {
				// Call JPF's BSL translator
				//node.apply(new Translation(theClass, theMethod, tempDir, predVarTable));

				// Compile it
				BanderaUtil.runJavac(classpath, tempDir + File.separator + predFileName + ".java");

				// Mark this as already processed
				processedPreds.add(predFileName);
			} catch (Exception exx)
			{
				throw new RuntimeException("Error: " + exx.getMessage());
			}
		}

		// We then have to inject the correct parameter. (if any)
		if (n.getArgs() != null)
		{
			String params = "";

			PArgs predArg = n.getArgs();
			do {

				String argId;
				if (predArg instanceof AArgsArgs)
				{
					AArgsArgs multiArgs = (AArgsArgs) predArg;
					predArg = multiArgs.getArgs();
					argId = getQuantifiedVariable(multiArgs.getId().toString());
				} else if (predArg instanceof AIdArgs)
				{
					AIdArgs singleArg = (AIdArgs) predArg;
					argId = getQuantifiedVariable(singleArg.getId().toString());
					predArg = null;
				} else {
					throw new RuntimeException("Unknown predicate parameter: "+predArg);
				}

				if (!"".equals(params)) params = argId + ", "+ params; else params = argId;
			} while (predArg != null);
		
			// Put the name subtitution inside the table
			System.out.println(predFileName + "(" + params + ") added");
			patternNodeTable.put(n, "\"" + predFileName + "(" + params + ")\"");
		} else
		{
			// If there are no parameters, then we simply put the predicate file name.
			patternNodeTable.put(n, "\"" + predFileName + "\"");
		}
	}

	// Up to this point, all .java files needed for JPF predicate
	// evaluation is setup and compiled. We have already had the pattern node symbols table.
	// Then we need to setup pattern name subtitution table: i.e. S->foo, P->baz
	// This can be achieved by visiting the predicate AST node (no other way)
	tlp.getNode().apply(this);

	// Then we need to check whether we have quantified variables
	if (qList != null && qList.size() > 0) // Yes, so we need to build "Select.java" appropriately
	{
		String lineSep = System.getProperty("line.separator");

		// Stamp out the preamble
        String prologue =
            "import gov.nasa.arc.ase.jpf.jvm.State;" + lineSep +
            "import gov.nasa.arc.ase.jpf.jvm.Reference;" + lineSep +
            "import gov.nasa.arc.ase.jpf.jvm.Thread;" + lineSep + lineSep +
            "public class Selected";
        String midpart = " {" + lineSep +
            "     public static boolean evaluate(State state, Reference _this) {" + lineSep +
            "          return (_this != null)";

        String epilogue = ";" + lineSep + "     }" + lineSep + "}" + lineSep;

        if (qList.size() > qv2Type.size())
        {
            // Flows here means that some quantifiers don't have complex type
            // restriction (i.e. simple quantifier like forall[x: A].)

    		// Prepare the file to dump the output
            String completeName = tempDir + File.separator + "Selected.java";
            System.out.println(completeName);
            saveAndCompile(completeName, prologue+  midpart + epilogue);
        }

        // Flows here means that some quantifiers do have complex type
        // restriction (i.e. forall[x: A+B].)
        for (Enumeration e = qv2Type.keys(); e.hasMoreElements(); )
        {
            String keyString = ((String) e.nextElement());
            String mangledKey = keyString.replace('.','$');
            StringBuffer selectbuf = new StringBuffer();
            String completeName = tempDir + File.separator + "Selected$"+mangledKey+".java";
            System.out.println(completeName);
            Value val = (Value) qv2Expr.get(keyString);
            String result = QuantifierValueTranslator.translate(val);
            saveAndCompile(completeName, prologue + "$" + mangledKey + midpart + " && " + result + epilogue);
        }
	}
	} catch (Exception suckers)
	{
		suckers.printStackTrace();
	}
}

private void saveAndCompile(String filename, String content)
{
    try {
        PrintStream javaPrint = new PrintStream(new FileOutputStream(new File(filename)));
        javaPrint.print(content);
        javaPrint.close();

        // Then compile that file
        BanderaUtil.runJavac(classpath, filename);
    } catch (IOException ex)
    {
        throw new RuntimeException("Cannot create file '" + filename + "'");
    }
}

public String getFormula(String mapping)
{
	if ("LTL".equalsIgnoreCase(mapping))
		return getLTLFormula();


	throw new RuntimeException("Mapping to " + mapping + " is currently not supported");
}
private String getLTLFormula()
{
	// Build our LTL formula
	String ltlFormula;
	
	try {
		ltlFormula = pattern.expandMapping("ltl", patternNameTable);
	} catch (Exception e)
	{
		throw new RuntimeException("Pattern mapping not supported!\nTable:\n"+patternNameTable);
	}

	// If we have quantified variables, we need to modify the
	// LTL Formula into the form:
	// (!Selected U (Selected && Predicate)) || [] (!Selected)
	if (qList != null && qList.size() > 0)
	{
		StringBuffer sel = new StringBuffer();
		for (Iterator qi = qList.iterator(); qi.hasNext(); )
		{
			QuantifierClassPair qcp = (QuantifierClassPair) qi.next();
            String qualifiedName = qcp.getClassName()+"."+qcp.getFieldName();
            String mangledName = qualifiedName.replace('.','$');
            if (qv2Expr.get(qualifiedName) == null)  // No such name, then don't mangle
    			sel.append("\"Selected("+qualifiedName+")\"");
            else
               sel.append("\"Selected$"+mangledName+"("+qualifiedName+")\"");

			if (qi.hasNext()) sel.append(" /\\ ");
		}
		String selected = "(" + sel.toString() + ")";
		ltlFormula = "(!"+selected+" U ("+selected+" /\\ (" + ltlFormula + "))) \\/ []!"+selected;
	}

	// Notice that JPF's LTL format is incompatible to ours. So convert it first
	int lastpos = 0;

	// convert the && -> /\
	do {
		lastpos = ltlFormula.indexOf("&&", lastpos);
		if (lastpos != -1)
			ltlFormula = ltlFormula.substring(0,lastpos) + "/\\" + ltlFormula.substring(lastpos+2);
	} while (lastpos != -1);

	lastpos = 0;

	// convert the || -> \/
	do {
		lastpos = ltlFormula.indexOf("||", lastpos);
		if (lastpos != -1)
			ltlFormula = ltlFormula.substring(0,lastpos) + "\\/" + ltlFormula.substring(lastpos+2);
	} while (lastpos != -1);

	// At this point, our LTL formula is ready to go!
	return ltlFormula;
}
private Method getMatchedMethod(Class theClass, SootMethod sm)
{
	Method[] methods = theClass.getDeclaredMethods();

	int max = methods.length;
	String name = sm.getName();
	if (name == null) throw new RuntimeException("Error! Bogus Soot Method!");

	for (int i = 0; i < max; i++)
	{
		Method m = methods[i];
		if (!name.equals(m.getName())) continue; // Not the same name
		Class[] params = m.getParameterTypes();
		int maxParams = params.length;
		if (sm.getParameterCount() != maxParams) continue; // Not the same number of params
		boolean found = true;

		for (int j = 0; j < maxParams; j++)
		{
			Class p1 = params[j];
			String sig = getTypeString(p1);
			Type t1 = sm.getParameterType(j);
			if (!sig.equals(t1.toString().trim()))
			{
				found = false; break;
			}
		}
		if (found) return m;
	}

	return null;
}

private String getQuantifiedVariable(String argId)
{
	Hashtable qtab = tlp.getQuantifiersTable();
	try {
		QuantifiedVariable qv = (QuantifiedVariable) qtab.get(argId.trim());

        String qName = getQualifiedNameForQv(qv.getName());  // Because the quantified variable may be placed in different class

        // If the constraint is not null (like having forall[b:A+B]. or something like that
        if (qv.getConstraint() != null)
        {
            // Store its type information too
            String typeString = qv.getType().toString().trim().replace('.','$');
            qv2Type.put(qName, typeString);
            qv2Expr.put(qName, qv.getConstraint());
            mangledQvTable.put(qv, qName.replace('.','$'));
        }

		return qName;
		// Warning: This translation assumes qv.getName() always returns simple name, not fully qualified one.
	} catch (Exception exx)
	{
		exx.printStackTrace();
		throw new RuntimeException("Parameter "+argId+" is not in the table:\n"+exx.getMessage());
	}
}

private String getQualifiedNameForQv(String v)
{
	for (Iterator i = qList.iterator(); i.hasNext(); )
	{
		QuantifierClassPair qcp = (QuantifierClassPair) i.next();
		if (qcp.getFieldName().equals("quantification$"+v))
			return qcp.toString();
	}
	return null;
}

private String getTypeString(Class c)
{
	if (c.isArray()) return getTypeString(c.getComponentType())+"[]";
	return c.getName();
}
}
