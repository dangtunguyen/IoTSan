package edu.ksu.cis.bandera.abstraction.predicate.parser;

/**
 * Predicate data structure.
 * Creation date: (4/15/01 1:20:34 AM)
 * @author: Roby Joehanes
 */
import java.util.*;
import ca.mcgill.sable.soot.*;
import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.soot.grimp.*;

public class PredicateImpl implements Predicate {
	private String name;
	private Value v = null;
	private SootClass cls = null;
	private SootMethod method = null;
	private boolean global = false;
	private SimpleNode ast = null;
	private String methodName = null;
	private String className = null;
	private List args = null;
	public PredicateImpl() {}
/**
 * Predicate constructor comment.
 */
public PredicateImpl(String n) {
	name = n;
}
public SootClass getClassContext()
{
	if (cls == null) cls = PredicateProcessor.getSootClass(className);
	return cls;
}
public String getClassName() { return className; }
/**
 * 
 * @return ca.mcgill.sable.soot.jimple.Value
 */
public Value getExpr() {
	return v;
}
	public SimpleNode getExprAST() { return ast; }
public SootMethod getMethodContext()
{
	if (method == null)
	{
		method = PredicateProcessor.getSootMethod(className, methodName, args);
	}
	return method;
}
/**
 * 
 * @return java.lang.String
 */
public String getName() {
	return name;
}
public boolean isGlobal() { return global; }
public void resolveArgs(PredicateProcessor v)
{
	LinkedList ll = new LinkedList();
	for (Iterator i = args.iterator(); i.hasNext();)
	{
		ll.addLast(((ASTType) i.next()).jjtAccept(v, this));
	}
	args = ll;
}
public void setClassContext(String s)
{
	className = s;
}
public void setContext(SootClass sc, SootMethod sm)
{
	if (sc == null) throw new RuntimeException("Cannot set null context on predicates!");
	if (isGlobal())
	{
		if (sm == null)
		{
			// Another field access, so we ignore it
			return;
		}
		if (method != null)
			throw new RuntimeException("Local access can't be brought into global scope! It's a bug!!");

		// At this point, we're certain that the global scope can only happen
		// on access fields between two classes. Now, the newly introduced
		// scope has a local limit on it. Therefore, the whole context is no
		// longer global.
		global = false;

		// The next step is to introduce our newly limiting scope
		cls = sc;
		method = sm;
	} else if (cls == null)
	{
		cls = sc;    // Initialization
		method = sm;
	} else
	{
		if (cls.getName().equals(sc.getName()))
		{
			// Here, we are in the same class
			if (method == null)
			{
				method = sm;  // Previously, it's a field access, now convert it to local (if any)
			} else
			{
				// We make sure if we have local access, it must occur at the same method
				// and the same class
				if (sm != null && !sm.getName().equals(method))
					throw new RuntimeException("Cannot compare locals in different methods!");
			}
		} else
		{
			// Here we have two different classes
			// if we have sm == null && method == null  ==> field to field access
			// if we have sm != null && method == null  ==> field to local access
			// if we have sm == null && method != null  ==> local to field access
			// Otherwise: local to local, if the methods are not equal, then it's illegal
			if (sm != null && method != null)
			{
				if (!sm.getName().equals(method))
				{
					throw new RuntimeException("Cannot compare locals in different methods (and even worse: in different class)!");
				}
			} else if (method == null && sm != null)
			{
				// It was previously a field access, now we have a method enclosure,
				// So, the context must be this method and this class.
				method = sm;
				cls = sc;
			} else if (method != null && sm == null)
			{
				// It was previously a local access, now we're accessing fields
				// So, nothing is changed
			} else
			{
				// Here, we are accessing fields between classes
				global = true;
			}
		}
	}
}
/**
 * 
 * @param newV ca.mcgill.sable.soot.jimple.Value
 */
public void setExpr(Value newV) {
	v = newV;
}
	public void setExprAST(SimpleNode n) { ast = n; }
public void setMethodContext(String s, List a)
{
	methodName = s; args = a;
}
/**
 * 
 * @param newName java.lang.String
 */
public void setName(String newName) {
	name = newName;
}
public String toString() { return name; }
}
