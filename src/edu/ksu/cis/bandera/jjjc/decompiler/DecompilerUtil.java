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
import java.util.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import edu.ksu.cis.bandera.annotation.*;
import edu.ksu.cis.bandera.abstraction.*;
import edu.ksu.cis.bandera.abstraction.util.*;

/**
 * DecompilerUtil is an auxilliary class to help the
 * decompilation, especially in flattenning a bunch
 * of related statements. The ones that is visible
 * from outside is flattenStmts and analyzeCondition.
 * Everything else is internal.
 * @author <a href="mailto:robbyjo@cis.ksu.edu">Roby Joehanes</a>
 * @version 0.4.21
**/
public class DecompilerUtil {
	private static Hashtable assignedVar = new Hashtable();
	public static final String gotoStr = "$.goto";
	private static Hashtable tempTable = new Hashtable();
	private static boolean debugInfo = false;
	private static boolean allowField = false;
/**
 * This method basically flattenning condition statements
 * in <TT>if</TT>, <TT>for</tt> (the second argument), <TT>while</tt>,
 * and <TT>do...while</tt>. The problem is that Jimple cannot have
 * complicated condition statements. So, conditions such as
 * <TT>((A && B) || C)</TT> is then divided into several <TT>if</tt>
 * statements. Moreover, <tt>if</tt> statements usually followed by
 * the nasty bad ugly <tt>goto</tt> which mess everythings up.
 * Which at this very moment, I'm quite perplexed.
**/
public static String analyzeCondition(Stmt[] stmts, int code)
{
	Vector result = new Vector();
	if (stmts==null) return "";
	String thenID, elseID;
	//Hashtable tempTable = new Hashtable();
	Hashtable condTable = new Hashtable();
	int max = stmts.length;
	Hashtable asgInIf   = new Hashtable();
/*	String lastAssignment = null;
	Type lastType = null;
*/
	for (int i = 0; i < max; i++)
	{
		switch (code)
		{
//			case 0: if (i==0) DecompilerValueSwitch.isAnIfStmt(true); break;
			case 1: DecompilerValueSwitch.isAnIfStmt(true); break;
		}
		// First decompile it
		Vector res = DecompilerStmtSwitch.evaluate(stmts[i]);
		DecompilerStmtSwitch.reset();

		// Then we'd like to know whether it is an if statement
		if (((String) res.get(0)).equals("if"))
		{
			String ref = ((String) res.get(res.size() - 1)).trim();
			Stmt s = DecompilerGotoMapper.getStmt(ref);

			// remove if and gotos
			replaceOccurence(res, tempTable);

			// Is there any assignments in the condition? Such as:
			// if ((x = foo()) >0)
			if (!asgInIf.isEmpty())
			{
				int j = res.size();
				for (int it = 0; it < j; it++)
				{
					String id = (String) res.get(it);
					if (asgInIf.containsKey(id))
					{
						// replace the token with the entry on the table
						String tempStr = (String) asgInIf.get(id);
						res.set(it,"("+id+" = "+tempStr+")");
					}
				}
			}

			String cond = fixScrewedUpThis(extractCondition(res));
			result.add(cond);
		} else
		{
			// Check the rest
			if (isAssignment(res))
			{
				Vector lhs = extractLHS(res);
				Vector rhs = extractRHS(res);
				replaceOccurence(rhs, tempTable);
				String right = fixScrewedUpThis(flattenToString(rhs));
				String left = flattenToString(lhs);
				right = stripImports(right);
				
				if (!isAllJimpleTemp(lhs))
				{
					replaceOccurence(lhs, tempTable);
					left = fixScrewedUpThis(flattenToString(lhs));
					asgInIf.put(left, right);
					assignedVar.put(left, right);  // Probably we need this... in case JJJC is naughty
/*					if (stmts[i] instanceof AssignStmt)
					{
						AssignStmt astmt = (AssignStmt) stmts[i];
						Type ltype = astmt.getLeftOp().getType();
						Type rtype = astmt.getRightOp().getType();
						if (ltype.equals(rtype)) {
							lastAssignment = left + " = " + right;
							lastType = ltype;
						} else {
							if (ltype instanceof BooleanType &&
								(rtype instanceof IntType || rtype instanceof LongType ||
								 rtype instanceof ShortType || rtype instanceof ByteType))
							{
								lastAssignment = left + " = (" + right + " != 0)";
								lastType = ltype;
							} else if (rtype instanceof BooleanType &&
								(ltype instanceof IntType || ltype instanceof LongType ||
								 ltype instanceof ShortType || ltype instanceof ByteType))
							{
							}
						}
					}
*/
					//result.add(left + " = " + right + ";");

				} else
				{
					tempTable.put(left, right);
				}
			} else
			{
				if (!(stmts[i] instanceof GotoStmt))
				System.out.println("Error in processing condition in method "+DecompilerInfo.getMethodName());
			}
		}
	}
	if (result.size()==0)
	{
		// Slicer might have messed around us. Beware!
/*		if (!asgInIf.isEmpty() && lastAssignment != null)
		{
			return "("+lastAssignment+")";
		}
*/
		return "(true)";
	}
	if (result.size()>1)
	{
		StringBuffer buf = new StringBuffer();
		buf.append("(");
		max = result.size();

		for (int i=0; i<max; i++)
		{
			buf.append((String) result.get(i));
			if (i<(max-1)) buf.append(" && ");
		}
		buf.append(")");
		return (buf.toString());
	}
	return flattenToString(result);
}
public static Vector dumpStmt(Stmt[] s)
{
	if ((s == null) || !isDebugInfo()) return new Vector();
	int max = s.length;
	Vector result = new Vector();

	result.add("");
	result.add("/*");
	result.add("   Jimple trace:");

	for (int i=0; i<max; i++)
	{
		if (s[i] instanceof JIfStmt)
		{
			String x = DecompilerGotoMapper.mapStmt(((JIfStmt) s[i]).getTarget());
			result.add("   "+s[i].toString()+x);
		} else
		result.add("   "+s[i].toString());
	}
	result.add("*/");
	return result;
}
private static String extractCondition(Vector x)
{	Vector temp = (Vector) x.clone();

	temp.removeElementAt(0);
	temp.removeElementAt(temp.size() - 1);
	temp.removeElementAt(temp.size() - 1);
	temp.removeElementAt(temp.size() - 1);

	return flattenToString(temp).trim();
}
private static Vector extractLHS(Vector x)
{
	int eqpos = getEqPos(x);
	Vector res = new Vector();

	for (int i=0; i<eqpos; i++)
	{
		res.add(x.get(i));
	}
	return res;
}
private static Vector extractRHS(Vector x)
{
	int eqpos = getEqPos(x);
	int max = x.size();
	Vector res = new Vector();

	for (int i=eqpos+1; i<max; i++)
	{
		res.add(x.get(i));
	}
	return res;
}
private static String fixScrewedUpThis(String s)
{
	int screwedUpThis = s.indexOf("this()");
	if (screwedUpThis>-1)
	{
		String prefix = s.substring(0,screwedUpThis);
		String postfix = s.substring(screwedUpThis+6);
		s = prefix+"this"+postfix;
	}
	return s;
}
/**
 * This method is basically flatten out all the related
 * statements by replacing all occurence of Jimple's temporary
 * variable with the real ones. This method also detects whether
 * we find parameter bindings and field default values.
**/
public static Vector flattenStmts(Stmt[] stmts)
{
	Vector result = new Vector();
	if (stmts == null || stmts.length == 0) return result;
	String cond = "";
	int max = stmts.length;

	// Decipher the statements
	for (int i = 0; i < max; i++)
	{
		// We ignore Goto Statement for a while to avoid confusion.
		if (stmts[i] instanceof GotoStmt) continue;
		Vector res = DecompilerStmtSwitch.evaluate(stmts[i]);
		DecompilerStmtSwitch.reset();

		// Normal statements contains ifs?
		// Suspect this
		if ((res.size()>0) && (((String) res.get(0)).equals("if")))
		{
			replaceOccurence(res, tempTable);
			cond = fixScrewedUpThis(extractCondition(res));

			if (cond.indexOf("quantification") != -1 || cond.indexOf("choose()") != -1)
			{
				int eq2null = cond.lastIndexOf("!= null");

				if (eq2null != -1)
				{
					result.add("if "+cond.substring(0,eq2null)+" == null)");
				} else if (cond.indexOf("choose()") != -1)
				{
					// Avoid garbages
					result.add("if (Bandera.choose())");
				} else
				{
					System.out.println("Warning: Can't handle condition '"+cond+"'");
				}
				cond = "";
			}
		} else
		if (isAssignment(res))
		{
			Vector lhs = extractLHS(res);
			Vector rhs = extractRHS(res);
			replaceOccurence(rhs, tempTable);
			String right = flattenToString(rhs);
			String left = flattenToString(lhs);

			if (right.equals("@this")) right="this";
			// Detect whether it is the result of SLABS or not

			right = stripImports(right);

			// Or not an exception.
			if (!right.equals("@caughtexception"))
			{
				if (!isAllJimpleTemp(lhs) && left.indexOf("qtvar") == -1)
				{
					replaceOccurence(lhs, tempTable);
					left = flattenToString(lhs);

					// Is the current method is an initializer and left hand side is a field?
					if ((DecompilerInfo.isInitializer()) && (DecompilerInfo.isField(left)) &&
						(DecompilerInfo.getNoOfParam()==0) && (isAllowField()))
					{
						DecompilerInfo.setFieldDefaultValue(left, right);
					} else
					{
						left = fixScrewedUpThis(left);
						right = fixScrewedUpThis(right);

						// Nasty trick hehehehe..... ;-P
						String isDef = (String) assignedVar.get(left);
						if (isDef != null)
						{
							if (cond.length()>0)
							{
								right = cond + " ? " + right + " : " + isDef;
								cond = "";
							}
						}
						
						assignedVar.put(left,right);
						result.add(left + " = " + right + ";");
					}
				} else
				{
					// Nasty trick hehehehe..... ;-P again....
					String isDef = (String) tempTable.get(left);
					if (isDef != null)
					{
						if (cond.length()>0)
						{
							right = cond + " ? " + right + " : " + isDef;
							cond = "";
						}
					}
					tempTable.put(left, right);
				}
			}
		} else
		{
			String firstToken = "", secondToken = "";

			if (res.size()>2)
			{
				firstToken = (String) res.get(0);
				secondToken = (String) res.get(1);
			}

			// To detect new MyClass();
			if (secondToken.equals("("))
			{
				if (isJimpleID(firstToken))
				{
					res.removeElementAt(0);
					replaceOccurence(res,tempTable);
					secondToken = ((String) tempTable.get(firstToken))+flattenToString(res);
                         if (secondToken.startsWith("this(") && !secondToken.equals("this()") &&
                             firstToken.equals("JJJCTEMP$0") && DecompilerInfo.getMethodName().equals("<init>") &&
                             DecompilerInfo.getNoOfParam() == 0)
                             result.add(secondToken + ";"); // Quick n dirty hack for: this() call;
					else tempTable.put(firstToken,secondToken);
				} else
				{
					String workAround = (String) assignedVar.get(firstToken);
					if (workAround!=null)
					{
						res.removeElementAt(0);
						replaceOccurence(res,tempTable);
						secondToken = workAround+flattenToString(res);
						assignedVar.put(firstToken,secondToken);
					}
				}
			} else
			{
				replaceOccurence(res, tempTable);
				String s = stripImports(flattenToString(res));
				s = fixScrewedUpThis(s);

				// Quick and dirty patch. Heh heh... ;-P
				if ((!s.equals("this()")) && (s.length()>0))
				{
					if (DecompilerInfo.getMethodRetType().equals("boolean"))
					{
						if (s.equals("return 1")) s = "return true"; else
						if (s.equals("return 0")) s = "return false"; else
						if (s.startsWith("return") && s.endsWith("? 1 : 0"))
						{
							s = s.substring(0,s.length()-8).trim();
						}
					}
					int bizzareBugPos = s.indexOf("? 1 : 0");
					if (bizzareBugPos != -1 && s.indexOf("quantification") == -1)
					{
						s = s.substring(0,bizzareBugPos)+s.substring(bizzareBugPos+7);
					}
					result.add(s + ";");
				}
			}
		}
	}
	return result;
}
public static String flattenToString(Vector x)
{
	if (x == null) return "";
	StringBuffer buf = new StringBuffer();
	int max = x.size();

	for (int i=0; i<max; i++)
	{
		buf.append((String) x.get(i));
	}
	return buf.toString().trim();
}
public static Hashtable getAssignedVarTable()
{
//	Hashtable temp = assignedVar;
//	assignedVar = new Hashtable();
	return assignedVar;
}
private static int getEqPos(Vector x)
{
	if (x.contains("=")) return x.indexOf("=");
	if (x.contains(":=")) return x.indexOf(":=");
	return -1;
}
private static int getJimpleVarID(String str)
{
	return Integer.parseInt(str.substring(9));
}
private static int getParamID(String x)
{
	return Integer.parseInt(x.substring(10));
}
public static Hashtable getTempTable() { return tempTable; }
private static int howManyIfs(Stmt[] stmts)
{
	int result = 0;
	int max = stmts.length;

	for (int i = 0; i<max; i++)
	{
		if (stmts[i] instanceof IfStmt) result++;
	}

	return result;
}
/**
 * 
 * @return boolean
 */
public static boolean isAbstracted(Abstraction lt) {
	return (((lt instanceof IntegralAbstraction) && !(lt instanceof ConcreteIntegralAbstraction)) ||
		((lt instanceof RealAbstraction) && !(lt instanceof ConcreteRealAbstraction)));
}
private static boolean isAllJimpleTemp(Vector x)
{
	int max = x.size();

	for (int i = 0; i<max; i++)
	{
		String s = ((String) x.get(i)).trim();
		if ((s.length()>0) && (!isJimpleID(s))) return false;
	}
	return true;
}
/**
 * 
 * @return boolean
 */
public static boolean isAllowField() {
	return allowField;
}
private static boolean isAssignment(Vector x)
{
	return x.contains("=") || x.contains(":=");
}
/**
 * 
 * @return boolean
 */
public static boolean isDebugInfo() {
	return debugInfo;
}
public static boolean isEmptyAnnotation(Annotation a)
{
	if (a==null) return true;
	Stmt[] stmts = a.getStatements();

	if (stmts.length>1) return false;
	if (stmts.length==0) return true;
	if (stmts[0] instanceof NopStmt) return true;
	
	return false;
}
public static boolean isJimpleID(String id)
{
	return ((id.startsWith("JJJCTEMP$")) || (id.startsWith("$ret")) || (id.startsWith("SLABS$")));
}
private static void replaceOccurence(Vector x, Hashtable table)
{
	if (table==null) return;
	int max = x.size();

	for(int i=0; i<max; i++)
	{
		String id = (String) x.get(i);
		if (isJimpleID(id) || id.indexOf("qtvar") != -1)
		{
			if (table.containsKey(id))
			{
				// replace the token with the entry on the table
				String tok = (String) table.get(id);

				// Do another nasty trick ;=)
				// to prevent new Blah.foo() being wrongly interpreted
				if ((tok.startsWith("new ")) && (i<(max-1)))
				{
					String nextTok = (String) x.get(i+1);
					if (nextTok.equals(".")) tok = "(" + tok + ")";
				}
				x.set(i,tok);
			} else
			{
				// Nasty hack ;-P
				if (id.equals("JJJCTEMP$0"))
				{
					x.set(i, "this");
				} else
				System.out.println("Slabs error: Can't find temp variable "+id+" in method "+DecompilerInfo.getMethodName());
			}
		}
	}
}
public static void resetAssignedVar()
{
	assignedVar = new Hashtable();
}
public static void resetTempTable()
{
	tempTable = new Hashtable();
}
/**
 * 
 * @param newAllowField boolean
 */
public static void setAllowField(boolean newAllowField) {
	allowField = newAllowField;
}
/**
 * 
 * @param newDebugInfo boolean
 */
public static void setDebugInfo(boolean newDebugInfo) {
	debugInfo = newDebugInfo;
}
public static int stmtIndex(Stmt s, Stmt[] stmts)
{
	int max = stmts.length;
	
	for (int i=0; i<max; i++)
	{
		if (stmts[i]==s) return i;
	}
	return -1;
}
public static String stripImports(String s)
{
	if (s.startsWith("edu.ksu.cis.bandera.slabs.lib."))
	{
		s = s.substring("edu.ksu.cis.bandera.slabs.lib.".length());
		Decompiler.addImports("edu.ksu.cis.bandera.slabs.lib.*");
	} else
	{
		// Later... 
	}
	return s;
}
}
