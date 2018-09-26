package edu.ksu.cis.bandera.abstraction.predicate.parser;

/**
 * Main class
 * Creation date: (4/8/01 10:08:26 PM)
 * @author: Roby Joehanes
 */
import java.util.*;
import java.io.*;
import ca.mcgill.sable.soot.grimp.*;
import ca.mcgill.sable.soot.jimple.*;
import ca.mcgill.sable.soot.*;
import edu.ksu.cis.bandera.jext.*;
import edu.ksu.cis.bandera.jjjc.*;

public class PredicateProcessor implements PredicateParserVisitor {
	private static PredicateProcessor visitor = new PredicateProcessor();
	private static Grimp grimp = Grimp.v();
	private Value baseValue = null;
	private static SootClassManager scm = null;
/**
 * GrimpPredicate constructor comment.
 */
public PredicateProcessor() {
	super();
}
public Value binOpProcess(List el, String tok, Object data)
{
	if (el.size() == 0) throw new RuntimeException("Token or expression list is zero!");
	Value v1 = null, v2 = null;

	for (Iterator ei = el.iterator(); ei.hasNext(); )
	{
		v2 = (Value) ((SimpleNode) ei.next()).jjtAccept(this, data);
		if (v1 != null)
		{
			if (tok.equals("|"))
			{
				v1 = grimp.newOrExpr(v1, v2);
			} else if (tok.equals("&"))
			{
				v1 = grimp.newAndExpr(v1, v2);
			} else if (tok.equals("^"))
			{
				v1 = grimp.newXorExpr(v1, v2);
			} else if (tok.equals("||"))
			{
				v1 = new LogicalOrExpr(v1, v2);
			} else if (tok.equals("&&"))
			{
				v1 = new LogicalAndExpr(v1, v2);
			} else throw new RuntimeException("Error: Undefined token '"+tok+"'");
		} else v1 = v2;
	}

	return v1;
}
public List convert(ASTCompilationUnit top)
{
	return (List) top.jjtAccept(visitor,null);
}
private Value fieldAccess(String s)
{
	String context = getContext(s);
	if (context == null)  return null;  // Simple names, quit
	
	// probably qualified name or in format of ClassName.blah
	String field;
	if (s.length() > context.length())
		field = s.substring(context.length() + 1);
	else field = s; /* throw new RuntimeException("Illegal field specified by '"+s+"'"); */
	return fieldAccess(context,field);
}
private Value fieldAccess(String clsName, String field)
{
	return fieldAccess(clsName, field, null);
}
private Value fieldAccess(String clsName, String field, Value base)
{
	// Field is in the form of xxxx.xxxx.xxx.xxx etc... (so it's raw)
	SootClass sc;
	SootField sf;
	String temp = field;
	int idx;
	String s;
	if (base == null) base = grimp.newLocal("this", RefType.v(clsName));
	if (field.equals("this")) return base;
	if (field == null || field.equals("")) throw new RuntimeException("Field access violation!");


	do {

		sc = getSootClass(base.getType().toString());
		if (sc == null) throw new RuntimeException("SootClass '"+base.getType().toString()+"' is not found!");

		idx = temp.indexOf('.');
		if (idx > -1)
		{
			s = temp.substring(0, idx);
			temp = temp.substring(idx + 1);
		} else
		{
			s = temp; temp = null;
		}

		if (!sc.declaresField(s)) return null; // We don't have such field;

		try
		{
			sf = sc.getField(s);
		} catch (Exception e)
		{
			/*try
			{
				sf = sc.getField(s, RefType.v(base.getType().toString()));
			} catch (Exception e2)*/
			{
				throw new RuntimeException("Unable to reference field '" + s + "': " + e.getMessage());
			}
		}

		if (Modifier.isStatic(sf.getModifiers()))
		{
			base = grimp.newStaticFieldRef(sf);
		} else
		{
			base = grimp.newInstanceFieldRef(base, sf);
		}
	} while (temp != null);

	return base;
}
private String getContext(String s)
{
	if (getSootClass(s) != null) return s;  // Short-circuit trivial cases

	String context = s;
	int idx;
	do {
		idx = context.lastIndexOf('.');
		if (idx > -1)
		{
			context = context.substring(0,idx);
			if (getSootClass(context) != null) return context;
		}
	} while (idx > -1);
	return null;
}
private Value getLocal(PredicateImpl pred, String loc)
{
	SootClass sc = pred.getClassContext();
	if (sc == null) throw new RuntimeException("Error: Class '"+pred.getClassName()+"' is not found!");
	SootMethod sm = pred.getMethodContext();

	JimpleBody jb = (JimpleBody) sm.getBody(Jimple.v());

	Value v = null;
	int idx = loc.indexOf('.');
	if (idx > -1)
	{
		// Dotted access here
		String first = loc.substring(0,idx);
		String rest = loc.substring(idx+1);
		if (!jb.declaresLocal(first)) return null;
		v = jb.getLocal(first);
		v = fieldAccess(sm.getName(), rest, v);
	} else
	{
		if (!jb.declaresLocal(loc)) return null;
		v = jb.getLocal(loc);
	}

	return v;
}
public static SootClass getSootClass(String name)
{
	SootClass sc = scm.managesClass(name) ? scm.getClass(name) : null;
	return sc;
}
public static SootMethod getSootMethod(String cls, String method, List type)
{
	SootClass sc = getSootClass(cls);
	if (sc == null) throw new RuntimeException("Cannot find class name '"+cls+"'");

	// Incompatible type conversion.
	ca.mcgill.sable.util.LinkedList ll = new ca.mcgill.sable.util.LinkedList();
	for (Iterator i = type.iterator(); i.hasNext(); )
	{
		ll.addLast(i.next());
	}

	SootMethod sm = sc.getMethod(method, ll);
	if (sm == null) System.out.println("Warning: The method '"+method+"' on class '"+cls+"' with "+type+" is not found!");
	return sm;
}
public static void main(String[] args)
{
	if (args.length == 3)
	{
		try {
			CompilationManager.setDoBSL(false);
			CompilationManager.reset();
			CompilationManager.setFilename(args[2]+File.separator+args[0]);
			CompilationManager.setClasspath(args[2]);
			CompilationManager.setIncludedPackagesOrTypes(new String[0]);

			CompilationManager.compile();
			if (CompilationManager.getExceptions().size() > 0)
			{
				throw new RuntimeException("Compilation failed!\n");
			}
			System.out.println("Compiled successfully!\n");

			System.out.println("Parsing: " + args[2]+File.separator+args[1]);
			FileInputStream in = new FileInputStream(args[2]+File.separator+args[1]);
			List pred = process(in, CompilationManager.getSootClassManager());
			if (pred == null)
				System.out.println("Error!");
		} catch (Exception e)
		{
			System.out.println("Waaaah! " + e.getMessage());
		}
	}
}
public Value multiBinOpProcess(List el, List tl, Object data)
{
	if (tl.size() == 0 || el.size() == 0) throw new RuntimeException("Token or expression list is zero!");
	if (tl.size() != el.size()-1) throw new RuntimeException("Unbalanced operator and operands!");
	Iterator ti = tl.iterator();
	Value v1 = null, v2 = null;

	for (Iterator ei = el.iterator(); ei.hasNext(); )
	{
		v2 = (Value) ((SimpleNode) ei.next()).jjtAccept(this, data);
		if (v1 != null)
		{
			String tok = (String) ti.next();
			if (tok.equals("<<"))
			{
				v1 = grimp.newShlExpr(v1, v2);
			} else if (tok.equals(">>"))
			{
				v1 = grimp.newShrExpr(v1, v2);
			} else if (tok.equals(">>>"))
			{
				v1 = grimp.newUshrExpr(v1, v2);
			} else if (tok.equals("+"))
			{
				v1 = grimp.newAddExpr(v1, v2);
			} else if (tok.equals("-"))
			{
				v1 = grimp.newSubExpr(v1, v2);
			} else if (tok.equals("*"))
		     {
			     v1 = grimp.newMulExpr(v1, v2);
		     } else if (tok.equals("/"))
		     {
			     v1 = grimp.newDivExpr(v1, v2);
		     } else if (tok.equals("%"))
		     {
			     v1 = grimp.newRemExpr(v1, v2);
		     } else if (tok.equals("=="))
		     {
			     v1 = grimp.newEqExpr(v1, v2);
		     } else if (tok.equals("!="))
		     {
			     v1 = grimp.newNeExpr(v1, v2);
			} else throw new RuntimeException("Error: Undefined token '"+tok+"'");
		} else v1 = v2;
	}

	return v1;
}
public static List process(InputStream in, SootClassManager cm) throws Exception
{
	ASTCompilationUnit node = PredicateParser.parse(in);
	scm = cm;
	List pred = visitor.convert(node);
	return pred;
}
/**
 * visit method comment.
 */
public Object visit(ASTAddSubExpr node, Object data) {
	return multiBinOpProcess(node.getExprList(), node.getTokenList(), data);
}
/**
 * visit method comment.
 */
public Object visit(ASTAndExpr node, Object data) {
	return binOpProcess(node.getExprList(), node.getToken(), data);
}
/**
 * visit method comment.
 */
public Object visit(ASTArguments node, Object data) {
	List args = node.getArguments();
	LinkedList l = new LinkedList();

	for (Iterator i = args.iterator(); i.hasNext(); )
	{
		l.addLast(((SimpleNode) i.next()).jjtAccept(this, data));
	}
	return l;
}
/**
 * visit method comment.
 */
public Object visit(ASTArrayExpr node, Object data) {
	Value arr = (Value) node.getExpr().jjtAccept(this, data);
	String s = node.getTag();
	if (s != null) // The first
	{
		arr = grimp.newArrayRef(fieldAccess(s),arr);
	} else   // The trailing []
	{
		if (baseValue == null) throw new RuntimeException("We've gotta have a base value!");
		arr = grimp.newArrayRef(baseValue,arr);
	}

	return arr;
}
/**
 * visit method comment.
 */
public Object visit(ASTBitAndExpr node, Object data) {
	return binOpProcess(node.getExprList(), node.getToken(), data);
}
/**
 * visit method comment.
 */
public Object visit(ASTBitOrExpr node, Object data) {
	return binOpProcess(node.getExprList(), node.getToken(), data);
}
/**
 * visit method comment.
 */
public Object visit(ASTBitXorExpr node, Object data) {
	return binOpProcess(node.getExprList(), node.getToken(), data);
}
/**
 * visit method comment.
 */
public Object visit(ASTCastExpr node, Object data) {
	Value v = (Value) node.getExpr().jjtAccept(this,data);
	Type  t = (Type) node.getType().jjtAccept(this,data);
	return grimp.newCastExpr(v,t);
}
/**
 * visit method comment.
 */
public Object visit(ASTCastLookahead node, Object data) {
	throw new RuntimeException("CastLookahead is unused!");
}
/**
 * visit method comment.
 */
public Object visit(ASTCompilationUnit node, Object data) {
	List ll = node.getPredicate();

	if (ll == null || ll.isEmpty()) throw new RuntimeException("Predicate Processor: No predicates are mentioned!");
	for (Iterator i = ll.iterator(); i.hasNext(); )
	{
		PredicateImpl p = (PredicateImpl) i.next();
		p.resolveArgs(this);
		p.setExpr((Value) p.getExprAST().jjtAccept(this,p));
	}
	return ll;
}
/**
 * visit method comment.
 */
public Object visit(ASTCondExpr node, Object data) {
	SimpleNode ts = node.getTestExpr(), th = node.getThenExpr(), el = node.getElseExpr(), result;

	Value v1 = (Value) ts.jjtAccept(this, data);
	Value v2 = (Value) th.jjtAccept(this, data);
	Value v3 = (Value) el.jjtAccept(this, data);

	return new HookExpr(v1, v2, v3);
}
/**
 * visit method comment.
 */
public Object visit(ASTDotClassExpr node, Object data) {
	if (baseValue != null) throw new RuntimeException("dot class expression: Invalid combination!");

	Type t = (Type) node.getExpr().jjtAccept(this,data);
	Vector param = new Vector();
	ca.mcgill.sable.util.LinkedList l = new ca.mcgill.sable.util.LinkedList();

	l.addLast(node.getTag());
	param.add("java.lang.String");

	return grimp.newStaticInvokeExpr(getSootMethod("java.lang.Class","forName",param),l);
}
/**
 * visit method comment.
 */
public Object visit(ASTDottedExpr node, Object data) {
	if (baseValue == null) throw new RuntimeException("dotted expression: Invalid combination!");
	return fieldAccess(((PredicateImpl) data).getClassName(), node.getTag(), baseValue);
}
/**
 * visit method comment.
 */
public Object visit(ASTEqExpr node, Object data) {
	return multiBinOpProcess(node.getExprList(), node.getTokenList(), data);
}
/**
 * visit method comment.
 */
public Object visit(ASTInstanceOfExpr node, Object data) {
	Value v = (Value) node.getExpr().jjtAccept(this,data);
	Type t = (Type) node.getType().jjtAccept(this,data);
	return grimp.newInstanceOfExpr(v,t);
}
/**
 * visit method comment.
 */
public Object visit(ASTLiteral node, Object data) {
	Object l = node.getLiteral();

	if (l == null) return NullConstant.v();
	if (l instanceof Integer) return IntConstant.v(((Integer) l).intValue());
	if (l instanceof Long) return LongConstant.v(((Long) l).longValue());
	if (l instanceof Float) return FloatConstant.v(((Float) l).floatValue());
	if (l instanceof Double) return DoubleConstant.v(((Double) l).doubleValue());
	if (l instanceof Boolean) return null;
	if (l instanceof Character) return null;
	if (l instanceof String) return StringConstant.v((String) l);

	throw new RuntimeException("Undefined Literal!");
}
/**
 * visit method comment.
 */
public Object visit(ASTMulDivExpr node, Object data) {
	return multiBinOpProcess(node.getExprList(), node.getTokenList(), data);
}
/**
 * visit method comment.
 */
public Object visit(ASTName node, Object data) {
	return node.getTag();
}
/**
 * visit method comment.
 */
public Object visit(ASTOrExpr node, Object data) {
	return binOpProcess(node.getExprList(), node.getToken(), data);
}
/**
 * visit method comment.
 */
public Object visit(ASTPrimaryExpr node, Object data) {

	baseValue = null;
	for (Iterator i = node.getNodes().iterator(); i.hasNext(); )
	{
		SimpleNode n = (SimpleNode) i.next();
	
		if (n instanceof ASTName)
		{
			if (baseValue != null) throw new RuntimeException("Name expression: Invalid combination!");
			String s = (String) n.jjtAccept(this,data);
			// No trailling ends, then we're accessing field.
			if (!i.hasNext())
			{
				if (getContext(s) == null)
				{
					// Deal with locals or field.
					PredicateImpl pred = (PredicateImpl) data;

					Value v = getLocal(pred,s); // Is it a local?
					if (v != null)
					{
						return v; // Yup...
					}

					v = fieldAccess(pred.getClassName(),s); // Is it a field?
					if (v != null)
					{
						return v; // Yup...
					}

					// Nope? Uh oh... an error happens...
					throw new RuntimeException("Invalid reference on '"+s+"'");

				} else
				{
					// So, we have a fully qualified name
					Value v = (Value) fieldAccess(s);
					if (v != null) return v; // Success? Good for you...

					// Nope? Uh oh... an error happens...
					throw new RuntimeException("Invalid reference on '"+s+"'");
				}
				
			} else throw new RuntimeException("Unhandled case!");
		} else if (n instanceof ASTLiteral)
		{
			if (baseValue != null) throw new RuntimeException("Literal expression: Invalid combination!");

			Value v = (Value) n.jjtAccept(this,data);
			if (!i.hasNext()) return v;
			if (i.hasNext() && !(v instanceof StringConstant))
			{
			     throw new RuntimeException("Illegal literal expression: It can't be trailled by any other things!");
			}
			// We're sure at this point v is a string constant and has a trailling nodes.
			// But, we won't handle that anyway, so bail out!
			throw new RuntimeException("String literal with method call: Unhandled case!");
		} else if (n instanceof ASTThisExpr)
		{
			baseValue = (Value) n.jjtAccept(this,data);
		} else if (n instanceof ASTSuperExpr)
		{
			baseValue = (Value) n.jjtAccept(this,data);
		} else if (n instanceof ASTDottedExpr)
		{
			baseValue = (Value) n.jjtAccept(this,data);
		} else if (n instanceof ASTDotClassExpr)
		{
			baseValue = (Value) n.jjtAccept(this,data);
		} else if (n instanceof ASTArrayExpr)
		{
			baseValue = (Value) n.jjtAccept(this,data);
		} else throw new RuntimeException("Unexpected nodes!");
	}
	if (baseValue == null) throw new RuntimeException("Unexpected end of nodes!");
	return baseValue;
}
/**
 * visit method comment.
 */
public Object visit(ASTPrimitiveType node, Object data) {
	throw new RuntimeException("PrimitiveType is unused! It's already bypassed");
}
/**
 * visit method comment.
 */
public Object visit(ASTRelationalExpr node, Object data) {
	Value v1 = (Value) node.getOp1().jjtAccept(this, data);
	Value v2 = (Value) node.getOp2().jjtAccept(this, data);
	String tok = node.getTag();

	if (tok.equals("<"))
	{
		return grimp.newLtExpr(v1, v2);
	} else if (tok.equals(">"))
	{
		return grimp.newGtExpr(v1, v2);
	} else if (tok.equals("<="))
	{
		return grimp.newLeExpr(v1, v2);
	} else if (tok.equals(">="))
	{
		return grimp.newGeExpr(v1, v2);
	} else throw new RuntimeException("Error: Undefined token '"+tok+"'");
}
/**
 * visit method comment.
 */
public Object visit(ASTShiftExpr node, Object data) {
	return multiBinOpProcess(node.getExprList(), node.getTokenList(), data);
}
/**
 * visit method comment.
 */
public Object visit(ASTSuperExpr node, Object data) {
	if (baseValue == null)
		return grimp.newThisRef(((PredicateImpl) data).getClassContext().getSuperClass());
	else
	{
		if (baseValue instanceof ThisRef)
			return grimp.newThisRef(getSootClass(baseValue.getType().toString()).getSuperClass());
		else throw new RuntimeException("Invalid combination on 'super' exception");
	}
}
/**
 * visit method comment.
 */
public Object visit(ASTThisExpr node, Object data) {
	if (baseValue != null) throw new RuntimeException("'this' expression: Invalid combination!");
	return grimp.newThisRef(((PredicateImpl) data).getClassContext());
}
/**
 * visit method comment.
 */
public Object visit(ASTType node, Object data) {
	String type = node.getBaseType();
	int dim = node.getDimension();
	Type t;

	if (type.equals("int")) t = IntType.v();
	else if (type.equals("long")) t = LongType.v();
	else if (type.equals("boolean")) t = BooleanType.v();
	else if (type.equals("char")) t = CharType.v();
	else if (type.equals("short")) t = ShortType.v();
	else if (type.equals("byte")) t = ByteType.v();
	else if (type.equals("float")) t = FloatType.v();
	else if (type.equals("double")) t = DoubleType.v();
	else t = RefType.v(type);

	if (dim > 0) t = ArrayType.v((BaseType) t, dim);

	return t;
}
/**
 * visit method comment.
 */
public Object visit(ASTUnaryExpr node, Object data) {
	return node.getExpr().jjtAccept(this,data);
}
/**
 * visit method comment.
 */
public Object visit(ASTUnaryMathExpr node, Object data) {
	Value v = (Value) (node.getExpr().jjtAccept(this,data));
	String tok = node.getTag();
	if (tok.equals("+"))
	{
		// do nothing
	} else if (tok.equals("-"))
	{
		v = grimp.newNegExpr(v);
	} else if (tok.equals("~"))
	{
		// ~x = -1 - x;
		Type t = v.getType();
		if (t instanceof ArrayType) t = ((ArrayType) t).baseType;
		if (t.equals(IntType.v()) || t.equals(ShortType.v()) || t.equals(ByteType.v()))
		{
			v = grimp.newSubExpr(IntConstant.v(-1), v);
		} else if (t.equals(LongType.v()))
		{
			v = grimp.newSubExpr(LongConstant.v(-1), v);
		} else throw new RuntimeException("Error: Cannot apply ~ operator to "+t.toString()+" on "+v.toString());
	} else if (tok.equals("!"))
	{
		v = new LogicalNotExpr(v);
	} else throw new RuntimeException("Error: Unexpected token '"+tok+"'");
	 return v;
}
/**
 * visit method comment.
 */
public Object visit(SimpleNode node, Object data) {
	throw new RuntimeException("SimpleNode is unused!");
}
}
