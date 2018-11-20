package edu.ksu.cis.bandera.spin;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998, 1999   James Corbett (corbett@hawaii.edu)     *
 * Copyright (C) 2001, 2002   Radu Iosif (iosif@cis.ksu.edu)         *
 * Copyright (C) 2001, 2002   Matthew Dwyer (dwyer@cis.ksu.edu)      *
 * Copyright (C) 2001, 2002   Roby Joehanes (robbyjo@cis.ksu.edu)    *
 * Copyright (C) 2001-2003   Todd Wallentine (tcw@cis.ksu.edu)      *
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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Category;

import edu.ksu.cis.bandera.bir.AbstractExprSwitch;
import edu.ksu.cis.bandera.bir.ActionVector;
import edu.ksu.cis.bandera.bir.AddExpr;
import edu.ksu.cis.bandera.bir.AndExpr;
import edu.ksu.cis.bandera.bir.Array;
import edu.ksu.cis.bandera.bir.ArrayExpr;
import edu.ksu.cis.bandera.bir.AssertAction;
import edu.ksu.cis.bandera.bir.AssignAction;
import edu.ksu.cis.bandera.bir.BirConstants;
import edu.ksu.cis.bandera.bir.BirThread;
import edu.ksu.cis.bandera.bir.BirTrace;
import edu.ksu.cis.bandera.bir.BoolLit;
import edu.ksu.cis.bandera.bir.ChooseExpr;
import edu.ksu.cis.bandera.bir.Collection;
import edu.ksu.cis.bandera.bir.ConstExpr;
import edu.ksu.cis.bandera.bir.Constant;
import edu.ksu.cis.bandera.bir.DerefExpr;
import edu.ksu.cis.bandera.bir.DivExpr;
import edu.ksu.cis.bandera.bir.Enumerated;
import edu.ksu.cis.bandera.bir.EqExpr;
import edu.ksu.cis.bandera.bir.Expr;
import edu.ksu.cis.bandera.bir.ExternChooseExpr;
import edu.ksu.cis.bandera.bir.Field;
import edu.ksu.cis.bandera.bir.ForallExpr;
import edu.ksu.cis.bandera.bir.InstanceOfExpr;
import edu.ksu.cis.bandera.bir.IntLit;
import edu.ksu.cis.bandera.bir.InternChooseExpr;
import edu.ksu.cis.bandera.bir.LeExpr;
import edu.ksu.cis.bandera.bir.LengthExpr;
import edu.ksu.cis.bandera.bir.LocVector;
import edu.ksu.cis.bandera.bir.Location;
import edu.ksu.cis.bandera.bir.Lock;
import edu.ksu.cis.bandera.bir.LockAction;
import edu.ksu.cis.bandera.bir.LockTest;
import edu.ksu.cis.bandera.bir.LtExpr;
import edu.ksu.cis.bandera.bir.MulExpr;
import edu.ksu.cis.bandera.bir.NeExpr;
import edu.ksu.cis.bandera.bir.NewArrayExpr;
import edu.ksu.cis.bandera.bir.NewExpr;
import edu.ksu.cis.bandera.bir.NotExpr;
import edu.ksu.cis.bandera.bir.NullExpr;
import edu.ksu.cis.bandera.bir.OrExpr;
import edu.ksu.cis.bandera.bir.PrintAction;
import edu.ksu.cis.bandera.bir.Range;
import edu.ksu.cis.bandera.bir.Record;
import edu.ksu.cis.bandera.bir.RecordExpr;
import edu.ksu.cis.bandera.bir.Ref;
import edu.ksu.cis.bandera.bir.RefExpr;
import edu.ksu.cis.bandera.bir.RemExpr;
import edu.ksu.cis.bandera.bir.RemoteRef;
import edu.ksu.cis.bandera.bir.StateVar;
import edu.ksu.cis.bandera.bir.StateVarVector;
import edu.ksu.cis.bandera.bir.SubExpr;
import edu.ksu.cis.bandera.bir.ThreadAction;
import edu.ksu.cis.bandera.bir.ThreadLocTest;
import edu.ksu.cis.bandera.bir.ThreadTest;
import edu.ksu.cis.bandera.bir.ThreadVector;
import edu.ksu.cis.bandera.bir.Tid;
import edu.ksu.cis.bandera.bir.TransSystem;
import edu.ksu.cis.bandera.bir.TransVector;
import edu.ksu.cis.bandera.bir.Transformation;
import edu.ksu.cis.bandera.bir.Type;
import edu.ksu.cis.bandera.checker.Checker;
import edu.ksu.cis.bandera.checker.OptionsFactory;
import edu.ksu.cis.bandera.checker.spin.SpinOptions;
import edu.ksu.cis.bandera.jjjc.gparser.configinfomanager.GConfigInfoManager;
import edu.ksu.cis.bandera.jjjc.gparser.configinfomanager.GDeviceConfigInfo;
import edu.ksu.cis.bandera.jjjc.gparser.configinfomanager.GOtherConfigInfo;
import edu.ksu.cis.bandera.jjjc.gparser.configinfomanager.GProcessedSubscriptionInfo;
import edu.ksu.cis.bandera.jjjc.gparser.configinfomanager.GSmartAppConfigInfo;
import edu.ksu.cis.bandera.jjjc.gparser.configinfomanager.GSpecialConfiInfo;
import edu.ksu.cis.bandera.jjjc.gparser.potentialriskscreener.GPotentialRiskScreener;
import edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor.GStateMapEnum;
import edu.ksu.cis.bandera.jjjc.gparser.smartapppreprocessor.GSubscriptionInfo;
import edu.ksu.cis.bandera.jjjc.gparser.util.GUtil;
import edu.ksu.cis.bandera.sessions.BIROptions;
import edu.ksu.cis.bandera.sessions.ResourceBounds;
import edu.ksu.cis.bandera.sessions.Session;
import edu.ksu.cis.bandera.sessions.SessionManager;
import edu.ksu.cis.bandera.util.BanderaUtil;
import edu.ksu.cis.bandera.util.NeverClaimTranslator;
import edu.ksu.cis.bandera.util.NeverClaimTranslatorFactory;
import edu.ksu.cis.bandera.util.PlatformInformation;
import edu.ksu.cis.bandera.util.Preferences;

/**
 * SpinTrans is the main class of the SPIN translator, which generates
 * PROMELA source representing a transition system.
 * <p>
 * The SPIN translator is invoked on a transition system as follows:
 * <pre>
 * // Parameters
 * TransSystem system = ...;   // the transition system
 * SpinOptions options = new SpinOptions();   // SPIN options
 * options.setMemoryLimit(256);            // set various options
 * ...
 * SpinTrans spin = SpinTrans.translate(system,options);  // invoke translator
 * spin.runSpin();                           // Run SPIN on generated PROMELA
 * BirTrace trace = spin.parseOutput();      // Parse output as BIR trace
 * </pre>
 *
 * @author James Corbett (corbett@hawaii.edu)
 * @author Radu Iosif (iosif@cis.ksu.edu
 * @author Matthew Dwyer (dwyer@cis.ksu.edu)
 * @author Roby Joehanes (robbyjo@cis.ksu.edu)
 * @author Todd Wallentine (tcw@cis.ksu.edu)
 * @version $Revision: 1.11 $ - $Date: 2003/06/23 18:59:53 $
 */
public class SpinTrans extends AbstractExprSwitch implements BirConstants, Checker {

	private static Category log = Category.getInstance(SpinTrans.class);
	private static boolean isDeviceFailureEnabled = false;

	TransSystem system;
	PrintWriter out; // Dest for translator output
	SpinTypeName namer; // Helper class to name types

	// Print control flags
	int indentLevel = 0; // indentation level
	boolean inDefine = false; // inside #define (must end line with \)
	boolean newLine = true; // at beginning of line (must indent)

	boolean resetTemp = false; // temp used during trans---reset it at end
	Transformation currentTrans; // Current trans being translated
	int actionNum; // Action number of current trans

	BitMatrix packedSubtype;

	SpinOptions options;
	boolean ranVerifier = false;
	String panOutput;
	
	/* [Thomas, May 21, 2017]
	 * Add sootClassName
	 * */
	String sootClassName;
	boolean d_stepOpen; 
	int d_stepSize;
	int indexValue = 0;
	String intTypeName = "";
	String boolTypeName = "";

	//static String ccName = "gcc"; 
	// we are now using Preferences.getCCAlias so that the user can choose where and which c compiler they can use

	// This is a special case name that is present in all case trees
	public static final String normal = "NORMAL_CASE";

	private boolean isVerbose = true;
	private String outputDir = "."+File.separator;

	/**
	 * This flag determines if we are currently evaluating a predicate
	 * statement.  If so, some things are handled slightly different (for
	 * example, references to global locals).
	 */
	private boolean evaluatingPredicate;

	private List panOutputList;

	SpinTrans(TransSystem system, SpinOptions options, PrintWriter out) {
		this.system = system;
		this.out = out;
		this.namer = new SpinTypeName();
		if(options == null) {
			this.options = (SpinOptions)OptionsFactory.createOptionsInstance("Spin");
			this.options.init();
		}
		else {
			this.options = options;
		}
		evaluatingPredicate = false;
	}


	Type arrayBaseType(Type type) {
		if (!(type instanceof Array))
			return type;
		return arrayBaseType(((Array) type).getBaseType());
	}


	/**
	 * Get action ID of current action = loc_num trans_index action_index
	 */
	String actionId() {
		int locNum = currentTrans.getFromLoc().getId();
		int transNum = 
				currentTrans.getFromLoc().getOutTrans().indexOf(currentTrans);
		return locNum + " " + transNum + " " + actionNum;
	}  


	/**
	 * Get action ID of current action, but comma seperated.
	 */
	String actionIdWithCommas() {
		int locNum = currentTrans.getFromLoc().getId();
		int transNum = 
				currentTrans.getFromLoc().getOutTrans().indexOf(currentTrans);
		return locNum + "," + transNum + "," + actionNum;
	}  


	/**
	 * Apply translator to BIR Expr, return case tree.
	 */
	CaseNode applyTo(Expr expr) {
		expr.apply(this);
		return (CaseNode) getResult();
	}  


	public void caseAddExpr(AddExpr expr) {
		translateBinaryOp(expr.getOp1(), expr.getOp2(), " + ");
	}  


	public void caseAndExpr(AndExpr expr) {
		translateBinaryOp(expr.getOp1(), expr.getOp2(), " && ");
	}  


	/**
	 * Translate array expression.
	 * <p>
	 * Must insert traps for array out of bounds.
	 */
	public void caseArrayExpr(ArrayExpr expr) {
		TreeNode arrayTree = applyTo(expr.getArray());
		TreeNode indexTree = applyTo(expr.getIndex());
		TreeNode result = arrayTree.compose(indexTree, null);
		Vector leafCases = result.getLeafCases(new Vector());
		for (int i = 0; i < leafCases.size(); i++) {
			Case leafCase = (Case) leafCases.elementAt(i);
			ExprNode leaf = (ExprNode) leafCase.node;
			/* [Thomas, May 9, 2017]
			 * Remove bound exception check
			 * */
			// expr1 is array name, expr2 is index
			//	    leafCase.insertTrap(leaf.expr2 + " >= " + leaf.expr1 + ".length",
			//				"ArrayIndexOutOfBoundsException",true);
			//	    if (! nonNegative(expr.getIndex()))
			//		leafCase.insertTrap(leaf.expr2 + " < 0", 
			//				    "ArrayIndexOutOfBoundsException", true);
			leaf.update(leaf.expr1 + ".element[" + leaf.expr2 + "]");
		}
		setResult(result);
	}


	/**
	 * Translate assert action.
	 */
	public void caseAssertAction(AssertAction assertAction) {
		CaseNode condTree = (CaseNode) applyTo(assertAction.getCondition());
		Vector leaves = condTree.getLeaves(new Vector());
		// Assert each leaf is true
		for (int i = 0; i < leaves.size(); i++) {
			ExprNode leaf = (ExprNode) leaves.elementAt(i);
			leaf.update("assert(" + leaf.expr1 + ");");
		}
		printStatement(condTree.getCase(normal));
	}


	/**
	 * Translate a BIR assignment action.
	 */
	public void caseAssignAction(AssignAction assign) {
		/* [Thomas, July 26, 2017] */
		if(assign.getRhs() instanceof NewExpr)
		{
			Type rhsType = ((NewExpr)assign.getRhs()).getType();
			
			if(rhsType instanceof Ref)
			{
				String recordName = ((Ref)rhsType).getTargetType().getName();
				String typeName = SpinUtil.getMapClassNameFromRecName(recordName);
				
				if(typeName != null)
				{
					if(assign.getLhs() instanceof StateVar)
					{
						String varName = ((StateVar)assign.getLhs()).getName();
						
						println("allocate" + typeName + "ArrIndex(" + varName + ");");
					}
				}
			}
		}
		else
		{
			TreeNode rhsTree = applyTo(assign.getRhs());
			TreeNode lhsTree = applyTo(assign.getLhs());
			CaseNode result = (CaseNode) lhsTree.compose(rhsTree, null);
			Vector leafCases = result.getLeafCases(new Vector());
			boolean isPrintSkipped = false;
	
			for (int i = 0; i < leafCases.size(); i++) {
				Case leafCase = (Case) leafCases.elementAt(i);
				ExprNode leaf = (ExprNode) leafCase.node;
				Type lhsType = assign.getLhs().getType();
				Type rhsType = assign.getRhs().getType();
				
				if (lhsType.isKind(REF))
				{
					if(leaf.expr2.equals("_temp_") && ((Ref)rhsType).getTargetType().isKind(ARRAY))
					{
						/* Skip printing */
						isPrintSkipped = true;
					}
					else
					{
						String targetTypeName = ((Ref)lhsType).getTargetType().getName();
						
						if(targetTypeName.endsWith("_rec") && leaf.expr1.contains(".element["))
						{
							String arrayName = SpinUtil.getArrayNameFromArrayAccessExpr(leaf.expr1);
							
							if(arrayName != null)
							{
								/* Increase the length of the array */
								println(arrayName + ".length++;");
							}
						}
						
						leaf.update("assign_" + targetTypeName + "(" + 
								leaf.expr1 + ", " + leaf.expr2 + ");");
					}
				}
				else
				{
					String chanWriteCommand = SpinUtil.getChanWriteCommand(leaf.expr1);
	
					if(chanWriteCommand != null)
					{
						String STCommandType = SpinUtil.getSTCommandType(leaf.expr1);
	
						if(STCommandType != null)
						{
							if(STCommandType.equals("sendSms"))
							{
								printConfiguredPhoneNumberAssignment();
							}
							
							/* Print out the EvtType assignment */
							println("_ST_Command.EvtType = " + STCommandType + ";");
							
	//						if(d_stepOpen && !chanWriteCommand.equals("HandleNetworkEvt(_ST_Command);"))
	//						{
	//							d_stepOpen = false;
	//							indentLevel --;
	//							println("}");
	//							println("skip;");
	//						}
						}
	
						leaf.update(chanWriteCommand);
					}
					else
					{
	//					if(leaf.expr1.contains(".element["))
	//					{
	//						String arrayName = SpinUtil.getArrayNameFromArrayAccessExpr(leaf.expr1);
	//						
	//						if(arrayName != null)
	//						{
	//							/* Increase the length of the array */
	//							println(arrayName + ".length++;");
	//						}
	//					}
						
						leaf.update(leaf.expr1 + " = " + leaf.expr2 + ";");
					}
				}
			}
			if(!isPrintSkipped)
			{
				printStatement(result.getCase(normal));
			}
		}
	}  


	public void caseBoolLit(BoolLit expr) {
		setResult(specialize("" + expr.getValue()));
	}  
	/**
	 * Translate choose expression.
	 * <p>
	 * As in NexExpr, we spit out code (in this case a nondeterministic
	 * if statement) that sets the _temp_ variable of the process.
	 * We then return _temp_ as the value of the expression.
	 * Once again, this is safe because a choose() must appear
	 * alone on the RHS of an assign action.
	 */


	private void translateChoice(ChooseExpr expr) {
		Vector choices = expr.getChoices();
		println("if");
		for (int i = 0; i < choices.size(); i++) {
			Expr choice = (Expr) choices.elementAt(i);
			ExprNode leaf = (ExprNode) applyTo(choice).getCase(normal);
			println(":: _temp_ = " + leaf.expr1 + "; printf(\"BIR? " +
					actionNum + " " + i + "\\n\"); ");
		}
		println("fi;");
		resetTemp = true;
	}


	public void caseInternChooseExpr(InternChooseExpr expr) {
		if (!options.getChooseFreeSearch())
			translateChoice(expr);
		else      
			println("goto done;");
		setResult(specialize("_temp_"));
	}


	public void caseExternChooseExpr(ExternChooseExpr expr) {
		translateChoice(expr);
		setResult(specialize("_temp_"));
	}


	public void caseForallExpr(ForallExpr expr) {
		StateVarVector stateVarVector = system.getStateVars();
		println("if");
		for (int i = 0; i < stateVarVector.size(); i ++) {
			StateVar var = stateVarVector.elementAt(i);
			Type type = var.getType();
			if (type.isKind(COLLECTION) && 
					((Collection) type).getBaseType() == expr.getBaseType()) {
				println(":: do");
				print("   :: (_i_ < " + ((Collection) type).getSize().getValue());
				println(" && " + var.getName() + ".inuse[_i_]) ->");
				println("      _i_ = _i_ + 1;");
				println("      if");
				println("      :: _temp_ = _ref(" + refIndex(var) + ", _i_ - 1);");
				println("         printf(\"BIR? " + actionNum + " %d\\n\", _temp_);");
				println("         break;");
				println("      :: else;");
				println("      fi");
				print("   :: (_i_ == " + ((Collection) type).getSize().getValue());
				println(" || !" + var.getName() + ".inuse[_i_]) -> break;");
				println("   od;");
			}
		}
		println("fi;");
		println("_i_ = 0;");
		setResult(specialize("_temp_"));
	}


	public void caseConstant(Constant expr) {
		setResult(specialize(expr.getName()));
	}  


	/**
	 * Translate dereference.
	 * <p>
	 * This is the case that creates most of the CaseNodes in
	 * a case tree.  We need to branch based on the collection
	 * of the target.
	 */
	public void caseDerefExpr(DerefExpr expr) {
		TreeNode result = applyTo(expr.getTarget());
		StateVarVector targets =
				((Ref) expr.getTarget().getType()).getTargets();
		Vector leafCases = result.getLeafCases(new Vector());
		for (int i = 0; i < leafCases.size(); i++) {
			Case leafCase = (Case) leafCases.elementAt(i);
			ExprNode leaf = (ExprNode) leafCase.node;
			/* [Thomas, May 1st, 2017]: remove the reference
			 * */
			//		    CaseNode caseNode = new CaseNode();
			//		    // Loop through the possible targets, making a case for each
			//		    for (int j = 0; j < targets.size(); j++) {
			//				StateVar target = targets.elementAt(j);
			//				String prom = target.getName();
			//				// For collections, add the instance selection
			//				if (target.getType().isKind(COLLECTION))
			//				    prom += ".instance[_index(" + leaf.expr1 + ")]";
			//				String cond = "(_collect(" + leaf.expr1 + ") == " +
			//				    refIndex(target) + ")";
			//				caseNode.addCase(cond, prom);
			//		    }
			//		    // Collection 0 is the null pointer
			//		    String cond = "(_collect(" + leaf.expr1 + ") == 0)";
			//		    caseNode.addTrapCase(cond, "NullPointerException", true);
			leafCase.replace(leaf);
		}
		setResult(result);
	}


	/**
	 * Translate division.
	 * <p>
	 * DivExpr and RemExpr require special handling because they
	 * can cause a ArithmeticException if the second arg is zero.
	 */
	public void caseDivExpr(DivExpr expr) {
		TreeNode e1Tree = applyTo(expr.getOp1());
		TreeNode e2Tree = applyTo(expr.getOp2());
		TreeNode result = e1Tree.compose(e2Tree, null);
		Vector leafCases = result.getLeafCases(new Vector());
		for (int i = 0; i < leafCases.size(); i++) {
			Case leafCase = (Case) leafCases.elementAt(i);
			ExprNode leaf = (ExprNode) leafCase.node;
			leafCase.insertTrap(leaf.expr2 + " == 0", "ArithmeticException",
					true);
			leaf.update("(" + leaf.expr1 + " / " + leaf.expr2 + ")");
		}
		setResult(result);
	}


	public void caseEqExpr(EqExpr expr) {
		translateBinaryOp(expr.getOp1(), expr.getOp2(), " == ");
	}  


	/**
	 * Translate instanceof expression (basically a big disjunction to
	 * test the refIndex for all possible targets).
	 */
	public void caseInstanceOfExpr(InstanceOfExpr expr) {
		TreeNode result = applyTo(expr.getRefExpr());
		Type type = expr.getArgType();

		//    StateVarVector targets = expr.getRefType().getTargets();

		Vector leafCases = result.getLeafCases(new Vector());
		for (int i = 0; i < leafCases.size(); i++) {
			Case leafCase = (Case) leafCases.elementAt(i);
			ExprNode leaf = (ExprNode) leafCase.node;
			CaseNode caseNode = new CaseNode();

			if (type instanceof Array)
				type = arrayBaseType(type);

			if (type instanceof Record)
				caseNode.addCase("(_collect("+leaf.expr1+") > 0)",
						"_extends(_typeid[_collect("+leaf.expr1+")-1], "
								+ ((Record) type).getUnique() + ")");
			else
				caseNode.addCase("", "0");

			//      String elseCond = "! (";
			//        for (int j = 0; j < targets.size(); j++) {
			//  	StateVar target = targets.elementAt(j);
			//  	String cond = "(_collect(" + leaf.expr1 + ") == " +
			//  	  refIndex(target) + ")";
			//  	caseNode.addCase(cond, "1");
			//  	if (j > 0)
			//  	  elseCond += " || ";
			//  	elseCond += cond;
			//        }
			//      elseCond += ")";
			//      caseNode.addCase(elseCond, "0");

			leafCase.replace(caseNode);
		}
		setResult(result);
	}  


	public void caseIntLit(IntLit expr) {
		setResult(specialize("" + expr.getValue()));
	}  


	public void caseLeExpr(LeExpr expr) {
		translateBinaryOp(expr.getOp1(), expr.getOp2(), " <= ");
	}  


	/**
	 * Translate array length expression.
	 */
	public void caseLengthExpr(LengthExpr expr) {
		TreeNode result = applyTo(expr.getArray());
		Vector leaves = result.getLeaves(new Vector());
		for (int i = 0; i < leaves.size(); i++) {
			ExprNode leaf = (ExprNode) leaves.elementAt(i);
			leaf.update(leaf.expr1 + ".length");
		}
		setResult(result);
	}  


	/**
	 * Translate lock action.
	 */
	public void caseLockAction(LockAction lockAction) {
		CaseNode lockTree = (CaseNode) applyTo(lockAction.getLockExpr());
		String opName =
				"_" + LockAction.operationName(lockAction.getOperation());
		// Wait action requires specific thread id
		if (lockAction.isWait())
			opName += "_" + lockAction.getThread().getId();
		Lock lockType = (Lock) lockAction.getLockExpr().getType();
		// Reentrant locks require _R versions of operations (except notify)
		if (lockType.isReentrant() &&
				! lockAction.isLockAction(NOTIFY | NOTIFYALL))
			opName += "_R";
		Vector leaves = lockTree.getLeaves(new Vector());
		for (int i = 0; i < leaves.size(); i++) {
			ExprNode leaf = (ExprNode) leaves.elementAt(i);
			// Apply op to each leaf expr
			leaf.update(opName + "(" + leaf.expr1 + "," +
					actionIdWithCommas() + ");");
		}
		printStatement(lockTree.getCase(normal));
	}  


	/**
	 * Translate lock test.
	 */
	public void caseLockTest(LockTest lockTest) {
		TreeNode lockTree;
		Vector leaves;
		switch (lockTest.getOperation()) {
		case LOCK_AVAILABLE:
			String opName = "_lockAvailable";
			// Reentrant locks require special _R versions of operations
			if (((Lock) lockTest.getLockExpr().getType()).isReentrant())
				opName += "_R";
			lockTree = applyTo(lockTest.getLockExpr());
			leaves = lockTree.getLeaves(new Vector());
			for (int i = 0; i < leaves.size(); i++) {
				ExprNode leaf = (ExprNode) leaves.elementAt(i);
				leaf.update(opName + "(" + leaf.expr1 + ")");
			}
			setResult(lockTree);
			return;
		case HAS_LOCK:
			// We don't use this test (it's always true)
			setResult(specialize("true"));
			return;
		case WAS_NOTIFIED:
			lockTree = applyTo(lockTest.getLockExpr());
			leaves = lockTree.getLeaves(new Vector());
			for (int i = 0; i < leaves.size(); i++) {
				ExprNode leaf = (ExprNode) leaves.elementAt(i);
				leaf.update("_wasNotified_" +
						lockTest.getThread().getId() + "(" +
						leaf.expr1 + ")");
			}
			setResult(lockTree);
			return;
		}
	}  


	public void caseLtExpr(LtExpr expr) {
		translateBinaryOp(expr.getOp1(), expr.getOp2(), " < ");
	}  


	public void caseMulExpr(MulExpr expr) {
		translateBinaryOp(expr.getOp1(), expr.getOp2(), " * ");
	}  


	public void caseNeExpr(NeExpr expr) {
		translateBinaryOp(expr.getOp1(), expr.getOp2(), " != ");
	}  


	//    public void caseNeExpr(NeExpr expr) {
	//	translateBinaryOp(expr.getOp1(), expr.getOp2(), " != ");
	//	}


	/**
	 * Translate array allocator.
	 * <p>
	 * Similar to NewExpr (above), except that we need to set (and check)
	 * the supplied length.
	 */
	public void caseNewArrayExpr(NewArrayExpr expr) {
		StateVar target = expr.getCollection();
		int size = ((Collection) target.getType()).getSize().getValue();
		int locNum = currentTrans.getFromLoc().getId();
		int transNum = 
				currentTrans.getFromLoc().getOutTrans().indexOf(currentTrans);

		// Call macro _allocate(collection,refindex,size,locNum,transNum,actionNum)
		/* [Thomas, May 9, 2017]
		 * Remove this print
		 * */
		//	println("_allocate(" + target.getName() + "," +
		//		refIndex(target) + "," + size + "," + locNum + "," +
		//		transNum + "," + actionNum + ");");
		//	Array type = (Array)((Collection) target.getType()).getBaseType();
		//	int maxLength = type.getSize().getValue();
		// Translate the length expression, then update each leaf to
		// (a) check that the length is less than the max for this type
		// (b) if so, assign the supplied length to the .length field
		//     of the array instance just allocated (_temp_).
		CaseNode setLength = applyTo(expr.getLength());
		Vector leafCases = setLength.getLeafCases(new Vector());
		for (int i = 0; i < leafCases.size(); i++) {
			Case leafCase = (Case) leafCases.elementAt(i);
			ExprNode leaf = (ExprNode) leafCase.node;
			//	    leafCase.insertTrap(leaf.expr1 + " > " + maxLength, 
			//				"ArraySizeLimitException", false);
			//	    leaf.update(target.getName() +
			//			".instance[_index(_temp_)].length = " +
			//			leaf.expr1 + "; printf(\"BIR? " + actionNum +
			//			" %d\\n\"," + leaf.expr1 + ");");
			if(currentTrans.getActions().elementAt(0).isAssignAction())
			{
				if(((AssignAction)currentTrans.getActions().elementAt(0)).getLhs() instanceof StateVar)
				{
					StateVar leftVar = (StateVar) ((AssignAction)currentTrans.getActions().elementAt(0)).getLhs();
					println(leftVar.getName() + ".length = " + leaf.expr1 + ";");
				}
			}
		}
		// Generate the code that sets the array length
		/* [Thomas, May 9, 2017]
		 * Remove this print
		 * */
		//	printStatement(setLength.getCase(normal));
		resetTemp = false;
		setResult(specialize("_temp_"));
	}  


	/**
	 * Translate an allocator.
	 * <p>
	 * When we translate the allocator expression, we spit out a
	 * call to the _allocate() macro, which selects a free instance
	 * of the collection and assigns the instance index to _temp_.
	 * We then just return "_temp_" as the value of this expression
	 * (this is OK since allocators must appear alone on the RHS
	 * of an assignment action).
	 */
	public void caseNewExpr(NewExpr expr) {
		StateVar target = expr.getCollection();
		int size = ((Collection) target.getType()).getSize().getValue();
		int locNum = currentTrans.getFromLoc().getId();
		int transNum = 
				currentTrans.getFromLoc().getOutTrans().indexOf(currentTrans);
		// Call macro _allocate(collection,refindex,size,locNum,transNum,actionNum)
		println("_allocate(" + target.getName() + "," +
				refIndex(target) + "," + size + "," + locNum + 
				"," + transNum + "," + actionNum + ");");
		resetTemp = true;
		setResult(specialize("_temp_"));
	}  


	public void caseNotExpr(NotExpr expr) {
		translateUnaryOp(expr.getOp(), "! ");
	}  


	public void caseNullExpr(NullExpr expr) {
		setResult(specialize("0"));
	}  


	public void caseOrExpr(OrExpr expr) {
		translateBinaryOp(expr.getOp1(), expr.getOp2(), " || ");
	}  


	/**
	 * Translate BIR Print action.
	 * <p>
	 * Perhaps remove this---easier to just get output from BIR trace instead.
	 */
	public void casePrintAction(PrintAction printAction) {
		if (printAction.getPrintItems().size() > 0) {
			String format = "BIR|";
			String params = "";
			Vector printItems = printAction.getPrintItems();
			for (int i = 0; i < printItems.size(); i++) {
				Object item = printItems.elementAt(i);
				if (item instanceof String)
					format += item;
				else {
					format += "%d";
					CaseNode varTree = (CaseNode) applyTo((Expr) item);
					params +=
							"," + ((ExprNode) varTree.getCase(normal)).expr1;
				}
			}
			println("printf(\"" + format + "\\n\"" + params + "); ");
		}
	}  


	/**
	 * Translate field selector.
	 */
	public void caseRecordExpr(RecordExpr expr) {
		TreeNode result = applyTo(expr.getRecord());
		String field = expr.getField().getName();
		Vector leaves = result.getLeaves(new Vector());
		for (int i = 0; i < leaves.size(); i++) {
			ExprNode leaf = (ExprNode) leaves.elementAt(i);
			leaf.update(leaf.expr1 + "." + field);
		}
		setResult(result);
	}  


	public void caseRefExpr(RefExpr expr) {
		setResult(specialize("_ref(" + refIndex(expr.getTarget()) + ",0)"));
	}  


	public void caseRemExpr(RemExpr expr) {
		TreeNode e1Tree = applyTo(expr.getOp1());
		TreeNode e2Tree = applyTo(expr.getOp2());
		TreeNode result = e1Tree.compose(e2Tree, null);
		Vector leafCases = result.getLeafCases(new Vector());
		for (int i = 0; i < leafCases.size(); i++) {
			Case leafCase = (Case) leafCases.elementAt(i);
			ExprNode leaf = (ExprNode) leafCase.node;
			leafCase.insertTrap(leaf.expr2 + " == 0", "ArithmeticException",
					true);
			leaf.update("(" + leaf.expr1 + " % " + leaf.expr2 + ")");
		}
		setResult(result);
	}  


	/**
	 * Handle the StateVar in the tree walking.  The proper way to
	 * handle this type of node is to either return the name of the
	 * variable that it represents or, if it is a global local, return
	 * a reference to the correct position in the global local array.
	 *
	 * @apram StateVar expr
	 */
	public void caseStateVar(StateVar expr) {
		if(expr == null) {
			throw new RuntimeException("The StateVar given is null.  Cannot determine a proper result.");
		}
		else if(expr.isGlobalLocal()) {
			BirThread birThread = expr.getThread();
			if(birThread != null) {
				String threadName = birThread.getName();
				if(evaluatingPredicate) {
					log.debug("Found a global local StateVar while evaluating a predicate: " + expr.getName() +
							", pid = " + expr.getGlobalLocalPid() + ", thread name = " + threadName +
							", hashCode = " + expr.hashCode());
					setResult(specialize(threadName + "_" + expr.getName() + "[" + expr.getGlobalLocalPid() + "]"));
				}
				else {
					log.debug("Found a global local StateVar: " + expr.getName());
					setResult(specialize(threadName + "_" + expr.getName() + "[_pid]"));
				}
			}
			else {
				log.debug("Found a global local without a BirThread.");
				setResult(specialize(expr.getName()));
			}
		}
		else {
			setResult(specialize(expr.getName()));
		}
	}  


	public void caseSubExpr(SubExpr expr) {
		translateBinaryOp(expr.getOp1(), expr.getOp2(), " - ");
	}  


	/**
	 * Translate thread action.
	 */
	public void caseThreadAction(ThreadAction threadAction) {
		if(threadAction == null) {
			log.error("threadAction is null.");
			return;
		}

		log.debug("threadAction = " + threadAction);
		switch (threadAction.getOperation())
		{
		case START:
			BirThread birThread = threadAction.getThreadArg();
			log.debug("birThread = " + birThread);
			String name = birThread.getName();
			/*
		  String name = "";
		  if(birThread != null) {
		  name = birThread.getName();
		  }
		  else {
		  name = "BirThreadIsNull";
		  System.out.println("birThread is null.");
		  System.out.println("threadAction.getLhs() = " + threadAction.getLhs());
		  }
			 */

			ExprNode leaf = new ExprNode("_temp_ = run " + name + "(");
			Vector actuals = threadAction.getActuals();
			for (int i = 0; i < actuals.size(); i ++) {
				CaseNode arg = applyTo((Expr) actuals.elementAt(i));
				ExprNode argLeaf = (ExprNode) arg.getCase(normal);
				leaf.update(leaf.expr1 + argLeaf.expr1);
				if (i < actuals.size() - 1)
					leaf.update(leaf.expr1 + ", ");
			}

			leaf.append(") - 1;");
			printStatement(leaf);

			CaseNode caseNode = new CaseNode();
			caseNode.addCase("_temp_ < "+ ((Tid)Type.tidType).maxTid(),
					new ExprNode("skip;"));
			caseNode.addTrapCase("else ", "ThreadLimitException", false);
			printStatement(caseNode);

			// If there exists an lhs we create a new
			// assignment action and print it out

			if (threadAction.getLhs() != null) {
				Expr tempExpr = new StateVar("_temp_",null,Type.tidType,null,system);
				AssignAction asgn = new AssignAction(threadAction.getLhs(),tempExpr);
				asgn.apply(this);
			}	

			//  	  CaseNode lhs = applyTo(threadAction.getLhs());	  
			//  	  leaf = (ExprNode) lhs.getCase(normal);
			//  	  leaf.append(" = _temp_;");
			//  	  printStatement(leaf);

			break;

		case EXIT:
			BirThread thread = threadAction.getThread();
			Location last = thread.getLocations().top();
			println("goto " + locLabel(last) + " ;");
			break;
		}
	}  


	/**
	 * Translate thread location test.
	 */
	public void caseThreadLocTest(ThreadLocTest threadLocTest) {
		String threadName = threadLocTest.getThread().getName();
		String locLabel = locLabel(threadLocTest.getLocation());
		CaseNode lhs = applyTo(threadLocTest.getLhs());
		ExprNode leaf = (ExprNode) lhs.getCase(normal);
		// PROMELA syntax:  thread[pid]@label
		String test = threadName + "[" + leaf.expr1 + "]@" + locLabel;
		setResult(specialize(test));
	}  


	/**
	 * Translate remote reference.
	 */
	public void caseRemoteRef(RemoteRef remoteRef) {
		String threadName = remoteRef.getThread().getName();
		String varName = remoteRef.getVar().getName();
		CaseNode lhs = applyTo(remoteRef.getLhs());
		ExprNode leaf = (ExprNode) lhs.getCase(normal);
		// PROMELA syntax:  thread[pid]:var ?
		String test = threadName + "[" + leaf.expr1 + "]:" + varName;
		setResult(specialize(test));
	}


	/**
	 * Translate thread test.
	 */
	public void caseThreadTest(ThreadTest threadTest) {
		switch (threadTest.getOperation()) {
		case THREAD_TERMINATED:
			// String opName = ThreadTest.operationName(
			// 				       threadTest.getOperation());
			// int threadId = threadTest.getThreadArg().getId();
			// Just call the #define'd macro for this thread
			// String test = "_" + opName + "_" + threadId;
			CaseNode lhs = applyTo(threadTest.getLhs());
			ExprNode leaf = (ExprNode) lhs.getCase(normal);
			String test = "_threadActive[" + leaf.expr1 + "] == THREAD_EXITED ";
			setResult(specialize(test));
			return;
		}
	}  


	static String currentDir() {
		return System.getProperty("user.dir");
	}  


	public void defaultCase(Object obj) {
		throw new RuntimeException("Trans type not handled: " + obj);
	}  

	private Type getType(String typeName)
	{
		Vector types = system.getTypes();
		
		for (int i = 0; i < types.size(); i++) {
			Type type = (Type) types.elementAt(i);
			
			if (! type.isKind(BOOL | TID | RANGE | LOCK | ENUMERATED | REF)) {
				if(type.getName().equals(typeName))
				{
					return type;
				}
			}
		}
		return null;
	}

	/**
	 * Generate constant and type definitions (using type namer).
	 */
	void definitions() {
//		Vector definitions = system.getDefinitions();

		// For each Constant, generate a #define
		/* [Thomas, April 29, 2017]
		 * Remove all constant definitions
		 * */
		//		for (int i = 0; i < definitions.size(); i++) {
		//		    Definition def = (Definition) definitions.elementAt(i);
		//		    if (def instanceof Constant)
		//		    {
		//		    		println("#define " + def.getName() + " " + def.getDef());
		//		    }
		//		}

		Vector types = system.getTypes();
		for (int i = 0; i < types.size(); i++) {
			Type type = (Type) types.elementAt(i);
			// For non-composite types, generate a #define for the type name
			if (type.isKind(BOOL | TID | RANGE | LOCK | ENUMERATED | REF)) {
				/* [Thomas, April 29, 2017]
				 * Remove all type definitions except those types' names
				 * starting with "type_"
				 * */
				if(type.typeName().startsWith("type_"))
				{
					//					println("/* " + type.typeName() + " = " + type + " */");
					print("#define " + type.typeName() + " ");
					type.apply(namer, this);
					println((String) namer.getResult());
					
					if(((String) namer.getResult()).equals("int"))
					{
						intTypeName = type.typeName();
					}
					else if(((String) namer.getResult()).equals("bool"))
					{
						boolTypeName = type.typeName();
					}
				}

				// For enumerated types, also #define the named constants
				if (type.isKind(ENUMERATED)) {
					Enumerated enumVar = (Enumerated) type;
					for (int j = 0; j < enumVar.getEnumeratedSize(); j++) {
						String name = enumVar.getNameOf(
								enumVar.getFirstElement() + j);
						if (name != null)
							println("#define " + name + " " +
									(enumVar.getFirstElement() + j));
					}
				}
			}
		}

		// For composite types (record, array, collection), generate
		// a typedef (struct).
		/* [Thomas, May 16, 2017]
		 * */
		println();
		println("typedef int_arr { byte length; short element[MAX_INT_ARRAY_SIZE]; }");
		println();
		for (int i = 0; i < types.size(); i++) {
			Type type = (Type) types.elementAt(i);
			if (! type.isKind(BOOL | TID | RANGE | LOCK | ENUMERATED | REF)) {
				/* [Thomas, April 29, 2017]
				 * Remove all type definitions whose types' names
				 * starting with "type_"
				 * */
				if(!type.typeName().startsWith("type_") && 
						!type.typeName().equals("java_lang_Object_rec") &&
						!type.typeName().endsWith("_arr"))
				{
					//println("/* " + type.typeName() + " = " + type + " */");
					print("typedef " + type.typeName() + " ");
					type.apply(namer, this);
					println((String) namer.getResult());
					
					/* Print used array types
					 * */
					{
						String arrayName = SpinUtil.getArrayNameFromRecordName(type.getName());
						
						if(arrayName != null)
						{
							Type arrType = this.getType(arrayName);
							
							if(arrType != null)
							{
								print("typedef " + arrType.typeName() + " ");
								arrType.apply(namer, this);
								println((String) namer.getResult());
							}
						}
					}
					println();
				}
			}
		}
		println();
	}

	/* [Thomas, May 3rd, 2017]
	 * This method is used to defined macros such as
	 * #define MAX_CHAN_SIZE 2
	 * #define MAX_SUBSCRIBERS 10
	 * */
	private void declareMacros()
	{
		indentLevel = 0;
		println("/********* Start of macro definitions ******************/");
		println("#define MAX_SYSTEM_TIME " + GUtil.getMaxSystemTime());
//		println("#define MAX_TEMPERATURE " + GUtil.getMaxTemperature());
//		println("#define MAX_POWER_METER " + GUtil.getMaxPowerMeter());
		println("#define MAX_SUBSCRIBERS " + GUtil.getMaxSubscribers());
		println("#define MAX_ARRAY_SIZE " + GUtil.getMaxArraySize());
		println("#define MAX_INT_ARRAY_SIZE " + GUtil.getMaxIntArraySize());
		println("#define MAX_SWITCH_DEVICES " + GUtil.getMaxSwtichDevices());
		println("#define MAX_STORED_EVENTS " + GUtil.getMaxStoredEvents());
		println("#define MAX_COMMAND_REPITIONS " + GUtil.getMaxCommandRepititions());
		println("#define MAX_NUM_EVENTS " + GUtil.getMaxNumEvents());
//		println("#define MAX_TRAN_FAILURES " + GUtil.getMaxTranFailures());
		println("#define ST_SUNRISE_TIME 2");
		println("#define ST_SUNSET_TIME 4");
		println();
		println("/* Int value of string type */");
		/* CAPABILITY.MOTIONSENSOR */
		println("#define MOTION 11");
		println("#define ACTIVE 12");
		println("#define INACTIVE 13");

		/* CAPABILITY.SWITCH */
		println("#define SWITCH 14");
		println("#define ON 15");
		println("#define OFF 16");

		/* CAPABILITY.PRESENCESENSOR */
		println("#define PRESENCE 17");
		println("#define NOT_PRESENT 18");
		println("#define PRESENT 19");

		/* CAPABILITY.TEMPERATUREMEASUREMENT */
		println("#define TEMPERATURE 20"); /* NAME, VALUE: NUMBER */

		/* CAPABILITY.THERMOSTATCOOLINGSETPOINT */
		println("#define COOLINGSETPOINT 21"); /* NAME, VALUE: NUMBER */
		println("#define COOLINGSETPOINTMIN 22"); /* NAME, VALUE: NUMBER */
		println("#define COOLINGSETPOINTMAX 23"); /* NAME, VALUE: NUMBER */

		/* CAPABILITY.THERMOSTATFANMODE */
		println("#define THERMOSTATFANMODE 24");
		println("#define AUTO 25");
		println("#define CIRCULATE 26");

		/* CAPABILITY.THERMOSTATHEATINGSETPOINT */
		println("#define HEATINGSETPOINT 27"); /* NAME, VALUE: NUMBER */
		println("#define HEATINGSETPOINTMIN 28"); /* NAME, VALUE: NUMBER */
		println("#define HEATINGSETPOINTMAX 29"); /* NAME, VALUE: NUMBER */

		/* CAPABILITY.THERMOSTATMODE */
		println("#define THERMOSTATMODE 30");
		println("#define COOL 31");
		println("#define EMERGENCY_HEAT 32");
		println("#define HEAT 33");

		/* CAPABILITY.THERMOSTATOPERATINGSTATE */
		println("#define THERMOSTATOPERATINGSTATE 34");
		println("#define COOLING 35");
		println("#define FAN_ONLY 36");
		println("#define HEATING 37");
		println("#define IDLE 38");
		println("#define PENDING_COOL 39");
		println("#define PENDING_HEAT 40");
		println("#define VENT_ECONOMIZER 41");

		/* CAPABILITY.THERMOSTATSETPOINT */
		println("#define THERMOSTATSETPOINT 42"); /* NAME, VALUE: NUMBER */
		println("#define THERMOSTATSETPOINTMIN 43"); /* NAME, VALUE: NUMBER */
		println("#define THERMOSTATSETPOINTMAX 44"); /* NAME, VALUE: NUMBER */

		/* CAPABILITY.LOCK */
		println("#define LOCK 45");
		println("#define LOCKED 46");
		println("#define UNKNOWN 47");
		println("#define UNLOCKED 48");
		println("#define UNLOCKED_WITH_TIMEOUT 49");

		/* CAPABILITY.SMOKEDETECTOR */
		println("#define SMOKE 50");
		println("#define CLEAR 51");
		println("#define DETECTED 52");
		println("#define TESTED 53");

		/* CAPABILITY.DOORCONTROL */
		println("#define DOOR 54");
		println("#define CLOSED 55");
		println("#define CLOSING 56");
		println("#define OPEN 57");
		println("#define OPENING 58");
		
		/* capability.contactSensor */
		println("#define CONTACT 59");
		
		/* capability.powerMeter */
		println("#define POWER 60");
		
		/* capability.illuminanceMeasurement */
		println("#define ILLUMINANCE 61");
		
		/* capability.waterSensor */
		println("#define WATER 62");
		println("#define WET 63");
		println("#define DRY 64");
		
		/* capability.valve */
		println("#define VALVE 65");
		
		/* capability.accelerationSensor */
		println("#define ACCELERATION 66");
		
		/* capability.accelerationSensor */
		println("#define FORECAST 67");
		println("#define RAIN 68");
		println("#define SNOW 69");
		println("#define SHOWERS 70");
		println("#define SPRINKLES 71");
		println("#define PRECIPITATION 72");
		
		/* capability.battery */
		println("#define BATTERY 73");
		
		/* capability.switchLevel */
		println("#define LEVEL 74");
		
		/* capability.carbonMonoxideDetector */
		println("#define CARBONMONOXIDE 75");
		
		/* capability.alarm */
		println("#define ALARM 76");
		println("#define BOTH 77");
		println("#define SIREN 78");
		println("#define STROBE 79");
		
		/* device.AeonKeyFob */
		println("#define BUTTON 80");
		println("#define PUSHED 81");
		println("#define HELD 82");
		
		/* capability.colorControl */
		println("#define COLOR 83");
		println("#define HUE 84");
		println("#define SATURATION 85");
		
		/* capability.relativeHumidityMeasurement */
		println("#define HUMIDITY 86");
		
		/* capability.powerSource */
		println("#define POWERED 87");
		println("#define MAINS 88");
		println("#define DC 89");

		/* OTHER VARIABLES */
		/* LOCATION MODES */
		println("#define HOME 1400");
		println("#define AWAY 1401");
		println("#define NIGHT 1402");
		println("#define MODE 1403");
		println("#define POSITION 1404");
		println("#define SUNRISETIME 1405");
		println("#define SUNSETTIME 1406");
		println("/********* End of macro definitions *******************/");
		println();

		println("/********* Start of enum definition *******************/");
		println("/* mtype defines all attributes' string values (sensor) and commands' names (actuator) */");
		println("mtype = {exit, inactive, _active, on, off, notpresent, present, fanAuto, fanCirculate, fanOn,");
		indentLevel = 1;
		println("Home, Away, Night, _mode, position, _sunrise, _sunset, sunsetTime, sunriseTime, sendSms,");
		println("auto, cool, emergencyHeat, heat, lock, unlock, clear, detected, tested, close, open,");
		println("temperature, appTouch, setCoolingSetpoint, setHeatingSetpoint, put, power_meter,");
		println("illuminance, wet, dry, setLevel, COSmoke, alarm, both, siren, strobe, pushed, held,");
		println("usercodechange, setHue, setSaturation, setColor, tampered, humidity, httpPost, battery,");
		println("powered, mains, dc, unsubscribe};");
		indentLevel = 0;
		println("/********* End of enum definition *********************/");
		println();
	}
	private void declareMapUtilities()
	{
		/* CInt2IntMap */
		{
			println("inline allocateCInt2IntMapArrIndex(rec)");
			println("{");
			println("\trec.gArrIndex = _g_CInt2IntMapArr.length;");
			println("\tif");
			println("\t:: rec.gArrIndex >= MAX_INT_ARRAY_SIZE -> assert(0);");
			println("\t:: else -> skip;");
			println("\tfi");
			println("\t_g_CInt2IntMapArr.length++;");
			println("}");
			
			println("inline HandleCInt2IntMapEvt(rec, evt)");
			println("{");
			println("\tif");
			println("\t:: evt.EvtType == put -> {");
			println("\t\t_index = MAX_INT_ARRAY_SIZE;");
			println("\t\tfor(i : 0 .. _g_CInt2IntMapArr.element[rec.gArrIndex].size-1) {");
			println("\t\t\tif");
			println("\t\t\t:: _g_CInt2IntMapArr.element[rec.gArrIndex].keyArr.element[i] == rec.keyToPut -> _index = i; break;");
			println("\t\t\t:: else -> skip;");
			println("\t\t\tfi");
			println("\t\t}");
			
			println("\t\tif");
			println("\t\t:: _index == MAX_INT_ARRAY_SIZE -> {");
			println("\t\t\t_index = _g_CInt2IntMapArr.element[rec.gArrIndex].size;");
			println("\t\t\t_g_CInt2IntMapArr.element[rec.gArrIndex].size++;");
			println("\t\t}");
			println("\t\t:: else -> skip;");
			println("\t\tfi");
			
			println("\t\tif");
			println("\t\t:: _index >= MAX_INT_ARRAY_SIZE -> assert(0);");
			println("\t\t:: else -> skip;");
			println("\t\tfi");
			
			println("\t\t_g_CInt2IntMapArr.element[rec.gArrIndex].gArrIndex = rec.gArrIndex;");
			println("\t\t_g_CInt2IntMapArr.element[rec.gArrIndex].keyArr.element[_index] = rec.keyToPut;");
			println("\t\t_g_CInt2IntMapArr.element[rec.gArrIndex].valueArr.element[_index] = rec.valueToPut;");
			
			println("\t}");
			println("\t:: else -> skip;");
			println("\tfi");
			println("}");
		}
		
		/* CInt2IIMMap */
		{
			println("inline allocateCInt2IIMMapArrIndex(rec)");
			println("{");
			println("\trec.gArrIndex = _g_CInt2IIMMapArr.length;");
			println("\tif");
			println("\t:: rec.gArrIndex >= MAX_INT_ARRAY_SIZE -> assert(0);");
			println("\t:: else -> skip;");
			println("\tfi");
			println("\t_g_CInt2IIMMapArr.length++;");
			println("}");
			
			println("inline HandleCInt2IIMMapEvt(rec, evt)");
			println("{");
			println("\tif");
			println("\t:: evt.EvtType == put -> {");
			println("\t\t_index = MAX_INT_ARRAY_SIZE;");
			println("\t\tfor(i : 1 .. _g_CInt2IIMMapArr.element[rec.gArrIndex].size) {");
			println("\t\t\tif");
			println("\t\t\t:: _g_CInt2IIMMapArr.element[rec.gArrIndex].keyArr.element[i] == rec.keyToPut -> _index = i; break;");
			println("\t\t\t:: else -> skip;");
			println("\t\t\tfi");
			println("\t\t}");
			
			println("\t\tif");
			println("\t\t:: _index == MAX_INT_ARRAY_SIZE -> {");
			println("\t\t\t_index = _g_CInt2IIMMapArr.element[rec.gArrIndex].size+1;");
			println("\t\t\t_g_CInt2IIMMapArr.element[rec.gArrIndex].size++;");
			println("\t\t}");
			println("\t\t:: else -> skip;");
			println("\t\tfi");
			
			println("\t\tif");
			println("\t\t:: _index >= MAX_INT_ARRAY_SIZE -> assert(0);");
			println("\t\t:: else -> skip;");
			println("\t\tfi");
			
			println("\t\t_g_CInt2IIMMapArr.element[rec.gArrIndex].gArrIndex = rec.gArrIndex;");
			println("\t\t_g_CInt2IIMMapArr.element[rec.gArrIndex].keyArr.element[_index] = rec.keyToPut;");
			println("\t\tassign_CInt2IntMap_rec(_g_CInt2IIMMapArr.element[rec.gArrIndex].valueArr.element[_index], _g_CInt2IntMapArr.element[rec.valueToPut.gArrIndex]);");
//			println("\t\tif");
//			println("\t\t:: rec.valueToPut.gArrIndex == 0 -> {");
//			println("\t\t\tallocateCInt2IntMapArrIndex(rec.valueToPut);");
//			println("\t\t\t_g_CInt2IIMMapArr.element[rec.gArrIndex].valueArr.element[_index].gArrIndex = rec.valueToPut.gArrIndex;");
//			println("\t\t\t_g_CInt2IntMapArr.element[0].size = 0;");
//			println("\t\t}");
//			println("\t\t:: else -> skip;");
//			println("\t\tfi");
			
			println("\t}");
			println("\t:: else -> skip;");
			println("\tfi");
			println("}");
		}
		
		/* CInt2IIIMMap */
		{
			println("inline allocateCInt2IIIMMapArrIndex(rec)");
			println("{");
			println("\trec.gArrIndex = _g_CInt2IIIMMapArr.length;");
			println("\tif");
			println("\t:: rec.gArrIndex >= MAX_INT_ARRAY_SIZE -> assert(0);");
			println("\t:: else -> skip;");
			println("\tfi");
			println("\t_g_CInt2IIIMMapArr.length++;");
			println("}");
			
			println("inline HandleCInt2IIIMMapEvt(rec, evt)");
			println("{");
			println("\tif");
			println("\t:: evt.EvtType == put -> {");
			println("\t\t_index = MAX_INT_ARRAY_SIZE;");
			println("\t\tfor(i : 1 .. _g_CInt2IIIMMapArr.element[rec.gArrIndex].size) {");
			println("\t\t\tif");
			println("\t\t\t:: _g_CInt2IIIMMapArr.element[rec.gArrIndex].keyArr.element[i] == rec.keyToPut -> _index = i; break;");
			println("\t\t\t:: else -> skip;");
			println("\t\t\tfi");
			println("\t\t}");
			
			println("\t\tif");
			println("\t\t:: _index == MAX_INT_ARRAY_SIZE -> {");
			println("\t\t\t_index = _g_CInt2IIIMMapArr.element[rec.gArrIndex].size+1;");/* Reserve the first item */
			println("\t\t\t_g_CInt2IIIMMapArr.element[rec.gArrIndex].size++;");
			println("\t\t}");
			println("\t\t:: else -> skip;");
			println("\t\tfi");
			
			println("\t\tif");
			println("\t\t:: _index >= MAX_INT_ARRAY_SIZE -> assert(0);");
			println("\t\t:: else -> skip;");
			println("\t\tfi");
			
			println("\t\t_g_CInt2IIIMMapArr.element[rec.gArrIndex].gArrIndex = rec.gArrIndex;");
			println("\t\t_g_CInt2IIIMMapArr.element[rec.gArrIndex].keyArr.element[_index] = rec.keyToPut;");
			println("\t\tassign_CInt2IIMMap_rec(_g_CInt2IIIMMapArr.element[rec.gArrIndex].valueArr.element[_index], _g_CInt2IIMMapArr.element[rec.valueToPut.gArrIndex]);");
			println("\t\tif");
			println("\t\t:: rec.valueToPut.gArrIndex == 0 -> {");
			println("\t\t\tallocateCInt2IIMMapArrIndex(rec.valueToPut);");
			println("\t\t\t_g_CInt2IIIMMapArr.element[rec.gArrIndex].valueArr.element[_index].gArrIndex = rec.valueToPut.gArrIndex;");
			println("\t\t\tassign_CInt2IIMMap_rec(_g_CInt2IIMMapArr.element[rec.valueToPut.gArrIndex], _g_CInt2IIMMapArr.element[0]);");
			println("\t\t\t_g_CInt2IIMMapArr.element[rec.valueToPut.gArrIndex].gArrIndex = rec.valueToPut.gArrIndex;");
			println("\t\t\t_g_CInt2IIMMapArr.element[0].size = 0;");
			println("\t\t}");
			println("\t\t:: else -> skip;");
			println("\t\tfi");
			
			println("\t}");
			println("\t:: else -> skip;");
			println("\tfi");
			println("}");
		}
	}
	/* [Thomas, May 11, 2017]
	 * Definition of utility methods
	 * */
	private void declareSTUtilities()
	{
		println("/********* Start of utility definitions ****************/");

		{
			println("/* mtype m; int result */");
			println("inline getIntValueFromMtype(m, result)");
			println("{");
			println("\tif");

			println("\t:: m == inactive -> result = INACTIVE;");
			println("\t:: m == _active -> result = ACTIVE;");

			println("\t:: m == on -> result = ON;");
			println("\t:: m == off -> result = OFF;");

			println("\t:: m == notpresent -> result = NOT_PRESENT;");
			println("\t:: m == present -> result = PRESENT;");

			println("\t:: m == fanAuto -> result = AUTO;");
			println("\t:: m == fanCirculate -> result = CIRCULATE;");
			println("\t:: m == fanOn -> result = ON;");

			println("\t:: m == auto -> result = AUTO;");
			println("\t:: m == cool -> result = COOL;");
			println("\t:: m == emergencyHeat -> result = EMERGENCY_HEAT;");
			println("\t:: m == heat -> result = HEAT;");

			println("\t:: m == lock -> result = LOCKED;");
			println("\t:: m == unlock -> result = UNLOCKED;");

			println("\t:: m == clear -> result = CLEAR;");
			println("\t:: m == detected -> result = DETECTED;");
			println("\t:: m == tested -> result = TESTED;");

			println("\t:: m == close -> result = CLOSED;");
			println("\t:: m == open -> result = OPEN;");
			
			println("\t:: m == Home -> result = HOME;");
			println("\t:: m == Away -> result = AWAY;");
			println("\t:: m == Night -> result = NIGHT;");
			
			println("\t:: m == temperature -> result = TEMPERATURE;");
			
			println("\t:: m == alarm -> result = ALARM;");
			println("\t:: m == both -> result = BOTH;");
			println("\t:: m == siren -> result = SIREN;");
			println("\t:: m == strobe -> result = STROBE;");
			
			println("\t:: m == pushed -> result = PUSHED;");
			println("\t:: m == held -> result = HELD;");
			
			println("\t:: m == setLevel -> result = LEVEL;");

			println("\t:: else -> result = 0;");
			println("\tfi");
			println("}");
		}
		
		{
			println("/* byte currIndex; byte prevIndex */");
			println("inline getPrevStoredEvtIndex(currIndex, prevIndex)");
			println("{");
			println("\tif");

			println("\t:: currIndex == 0 -> prevIndex = (MAX_STORED_EVENTS-1);");
			println("\t:: !(currIndex == 0) -> prevIndex = (currIndex-1);");
			
			println("\tfi");
			println("}");
		}
		
		{
			Map<String, String> deviceList = GConfigInfoManager.getDeviceListFromConfigInfo();
			
			println("inline ResetGlobalVariables()");
			println("{");
			println("\t/* Reset system variables */");
			println("\tlocation.NumReceivedCommands = 0;");
//			println("\t_STNetworkManager.NumReceivedCommands = 0;");
			println();
			
			println("\t/* Reset device variables */");
			for(Map.Entry<String, String> entry : deviceList.entrySet())
			{
				String NumReceivedCommands = SpinUtil.getGlobalArrRefNumReceivedCommands(entry.getKey(), entry.getValue());
				println("\t" + NumReceivedCommands + " = 0;");
			}
			
			println("}");
		}
		
		{

			Map<String, String> deviceList = GConfigInfoManager.getDeviceListFromConfigInfo();
			
			println("inline ResetDeviceDefaultState()");
			println("{");
			boolean skipNeeded = true;
			{
				println("\t/* Reset default state of devices */");
				for(Map.Entry<String, String> entry : deviceList.entrySet())
				{
					String deviceType = entry.getValue();
					String deviceName = entry.getKey();
					
					if(entry.getValue().equals("STMotionSensor"))
					{
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].currentMotion = INACTIVE;");
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].motionState.value = INACTIVE;");
						skipNeeded = false;
					}
					else if(entry.getValue().equals("STSwitch"))
					{
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].currentSwitch = OFF;");
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].switchState.value = OFF;");
						skipNeeded = false;
					}
					else if(entry.getValue().equals("STTempMeas"))
					{
					}
					else if(entry.getValue().equals("STPresSensor"))
					{
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].currentPresence = PRESENT;");
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].presenceState.value = PRESENT;");
						skipNeeded = false;
					}
					else if(entry.getValue().equals("STLock"))
					{
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].currentLock = LOCKED;");
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].lockState.value = LOCKED;");
						skipNeeded = false;
					}
					else if(entry.getValue().equals("STContactSensor"))
					{
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].currentContact = CLOSED;");
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].contactState.value = CLOSED;");
						skipNeeded = false;
					}
					else if(entry.getValue().equals("STThermostat"))
					{
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].currentThermostatFanMode = ON;");
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].thermostatFanModeState.value = ON;");
						
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].currentThermostatMode = AUTO;");
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].thermostatModeState.value = AUTO;");
						skipNeeded = false;
					}
					else if(entry.getValue().equals("STPowerMeter"))
					{
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].currentPower = 1;");
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].powerState.value = 1;");
						skipNeeded = false;
					}
					else if(entry.getValue().equals("STWaterSensor"))
					{
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].currentWater = DRY;");
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].waterState.value = DRY;");
						skipNeeded = false;
					}
					else if(entry.getValue().equals("STValve"))
					{
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].currentValve = OPEN;");
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].valveState.value = OPEN;");
						skipNeeded = false;
					}
					else if(entry.getValue().equals("STAccSensor"))
					{
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].currentAcceleration = INACTIVE;");
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].accelerationState.value = INACTIVE;");
						skipNeeded = false;
					}
					else if(entry.getValue().equals("STAlarm"))
					{
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].currentAlarm = OFF;");
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].alarmState.value = OFF;");
						skipNeeded = false;
					}
					else if(entry.getValue().equals("STSmokeDetector"))
					{
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].currentSmoke = CLEAR;");
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].smokeState.value = CLEAR;");
						skipNeeded = false;
					}
					else if(entry.getValue().equals("STCarMoDetector"))
					{
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].currentCarbonMonoxide = CLEAR;");
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].carbonMonoxideState.value = CLEAR;");
						skipNeeded = false;
					}
					else if(entry.getValue().equals("STAeonKeyFob"))
					{
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].currentButton = CLEAR;");
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].buttonState.value = CLEAR;");
						skipNeeded = false;
					}
				}
			}
			if(skipNeeded)
			{
				println("\tskip;");
			}
			println("}");
		}
		if(GConfigInfoManager.isStateMapUsed())
		{
			declareMapUtilities();
		}

		println("/********* End of utility definitions ******************/");
		println();
	}
	private void declareMapRecAssignmentMethods()
	{
		/* CInt2IntMap */
		println("inline assign_CInt2IntMap_rec(rec1, rec2)\n" +
				"{\n" +
				   "\trec1.size = rec2.size;\n" +
				   
				   //assign_int_arr(rec1.keyArr, rec2.keyArr);
				   //rec1.keyArr.length = rec2.keyArr.length;
				   "\tfor(i : 0 .. MAX_INT_ARRAY_SIZE-1) {\n" +
					   "\t\trec1.keyArr.element[i] = rec2.keyArr.element[i];\n" +
				   "\t}\n" +
				   
				   //assign_int_arr(rec1.valueArr, rec2.valueArr);
				   //rec1.valueArr.length = rec2.valueArr.length;
				   "\tfor(i : 0 .. MAX_INT_ARRAY_SIZE-1) {\n" +
					   "\t\trec1.valueArr.element[i] = rec2.valueArr.element[i];\n" +
				   "\t}\n" +
				   
				   "\trec1.gArrIndex = rec2.gArrIndex;\n" +
				   "\trec1.keyToPut = rec2.keyToPut;\n" +
				   "\trec1.valueToPut = rec2.valueToPut;\n" +
				   "\trec1.isAlive = rec2.isAlive;\n" +
				"}");
		
		/* CInt2IIMMap */
		println("inline assign_CInt2IIMMap_rec(rec1, rec2)\n" +
				"{\n" +
				   "\trec1.size = rec2.size;\n" +
				   
				   //assign_int_arr(rec1.keyArr, rec2.keyArr);
				   //rec1.keyArr.length = rec2.keyArr.length;
				   "\tfor(i : 0 .. MAX_INT_ARRAY_SIZE-1) {\n" +
				   "\t\trec1.keyArr.element[i] = rec2.keyArr.element[i];\n" +
				   "\t}\n" +
				   
				   //assign_CInt2IntMap_arr(rec1.valueArr, rec2.valueArr);
				   //rec1.valueArr.length = rec2.valueArr.length;
				   "\tfor(i : 0 .. MAX_INT_ARRAY_SIZE-1) {\n" +
				   "\t\trec1.valueArr.element[i].size = rec2.valueArr.element[i].size;\n" +
					   
					   //assign_int_arr(rec1.valueArr.element[i].keyArr, rec2.valueArr.element[i].keyArr);
					   //rec1.valueArr.element[i].keyArr.length = rec2.valueArr.element[i].keyArr.length;
					   "\t\tfor(i1 : 0 .. MAX_INT_ARRAY_SIZE-1) {\n" +
					   "\t\t\trec1.valueArr.element[i].keyArr.element[i1] = rec2.valueArr.element[i].keyArr.element[i1];\n" +
					   "\t\t}\n" +
					   
					   //assign_int_arr(rec1.valueArr.element[i].valueArr, rec2.valueArr.element[i].valueArr);
					   //rec1.valueArr.element[i].valueArr.length = rec2.valueArr.element[i].valueArr.length;
					   "\t\tfor(i1 : 0 .. MAX_INT_ARRAY_SIZE-1) {\n" +
					   "\t\t\trec1.valueArr.element[i].valueArr.element[i1] = rec2.valueArr.element[i].valueArr.element[i1];\n" +
					   "\t\t}\n" +
					   
					   "\t\trec1.valueArr.element[i].gArrIndex = rec2.valueArr.element[i].gArrIndex;\n" +
					   "\t\trec1.valueArr.element[i].keyToPut = rec2.valueArr.element[i].keyToPut;\n" +
					   "\t\trec1.valueArr.element[i].valueToPut = rec2.valueArr.element[i].valueToPut;\n" +
					   "\t\trec1.valueArr.element[i].isAlive = rec2.valueArr.element[i].isAlive;\n" +
				   "\t}\n" +
				   
				   "\trec1.gArrIndex = rec2.gArrIndex;\n" +
				   "\trec1.keyToPut = rec2.keyToPut;\n" +
				   
				   //assign_CInt2IntMap_rec(rec1.valueToPut, rec2.valueToPut);
				   "\trec1.valueToPut.size = rec2.valueToPut.size;\n" +
				   
				   //assign_int_arr(rec1.valueToPut.keyArr, rec2.valueToPut.keyArr);
				   //rec1.valueToPut.keyArr.length = rec2.valueToPut.keyArr.length;
				   "\tfor(i : 0 .. MAX_INT_ARRAY_SIZE-1) {\n" +
				   "\t\trec1.valueToPut.keyArr.element[i] = rec2.valueToPut.keyArr.element[i];\n" +
				   "\t}\n" +
				   
				   //assign_int_arr(rec1.valueToPut.valueArr, rec2.valueToPut.valueArr);
				   //rec1.valueToPut.valueArr.length = rec2.valueToPut.valueArr.length;
				   "\tfor(i : 0 .. MAX_INT_ARRAY_SIZE-1) {\n" +
				   		"\t\trec1.valueToPut.valueArr.element[i] = rec2.valueToPut.valueArr.element[i];\n" +
				   	"\t}\n" +
				   
				   "\trec1.valueToPut.gArrIndex = rec2.valueToPut.gArrIndex;\n" +
				   "\trec1.valueToPut.keyToPut = rec2.valueToPut.keyToPut;\n" +
				   "\trec1.valueToPut.valueToPut = rec2.valueToPut.valueToPut;\n" +
				   "\trec1.valueToPut.isAlive = rec2.valueToPut.isAlive;\n" +
				   
					"\trec1.isAlive = rec2.isAlive;\n" +
				"}");
		
		/* CInt2IIIMMap */
		println("inline assign_CInt2IIIMMap_rec(rec1, rec2)\n" +
				"{\n" +
				   "\trec1.size = rec2.size;\n" +
				   
				   //assign_int_arr(rec1.keyArr, rec2.keyArr);
				   //rec1.keyArr.length = rec2.keyArr.length;
				   "\tfor(i : 0 .. MAX_INT_ARRAY_SIZE-1) {\n" +
				   		"\t\trec1.keyArr.element[i] = rec2.keyArr.element[i];\n" +
				   "\t}\n" +
				   
				   //assign_CInt2IIMMap_arr(rec1.valueArr, rec2.valueArr);
				   //rec1.valueArr.length = rec1.valueArr.length;
				   "\tfor(i : 0 .. MAX_INT_ARRAY_SIZE-1) {\n" +
				      //assign_CInt2IIMMap_rec(rec1.valueArr.element[i], rec1.valueArr.element[i]);
				      "\t\trec1.valueArr.element[i].size = rec2.valueArr.element[i].size;\n" +
				      //assign_int_arr(rec1.valueArr.element[i].keyArr, rec2.valueArr.element[i].keyArr);
				      //rec1.valueArr.element[i].keyArr.length = rec2.valueArr.element[i].keyArr.length;
				      "\t\tfor(i1 : 0 .. MAX_INT_ARRAY_SIZE-1) {\n" +
				      		"\t\t\trec1.valueArr.element[i].keyArr.element[i1] = rec2.valueArr.element[i].keyArr.element[i1];\n" +
				      "\t\t}\n" +
				      //assign_CInt2IntMap_arr(rec1.valueArr.element[i].valueArr, rec2.valueArr.element[i].valueArr);
				      //rec1.valueArr.element[i].valueArr.length = rec2.valueArr.element[i].valueArr.length;
				      "\t\tfor(i1 : 0 .. MAX_INT_ARRAY_SIZE-1) {\n" +
				         //assign_CInt2IntMap_rec(rec1.valueArr.element[i].valueArr.element[i1], rec2.valueArr.element[i].valueArr.element[i1]);
				         "\t\t\trec1.valueArr.element[i].valueArr.element[i1].size = rec2.valueArr.element[i].valueArr.element[i1].size;\n" +
					     //assign_int_arr(rec1.valueArr.element[i].valueArr.element[i1].keyArr, rec2.valueArr.element[i].valueArr.element[i1].keyArr);
					     //rec1.valueArr.element[i].valueArr.element[i1].keyArr.length = rec2.valueArr.element[i].valueArr.element[i1].keyArr.length;
					     "\t\t\tfor(i2 : 0 .. MAX_INT_ARRAY_SIZE-1) {\n" +
					     		"\t\t\t\trec1.valueArr.element[i].valueArr.element[i1].keyArr.element[i2] = rec2.valueArr.element[i].valueArr.element[i1].keyArr.element[i2];\n" +
					     "\t\t\t}\n" +
					     //assign_int_arr(rec1.valueArr.element[i].valueArr.element[i1].valueArr, rec2.valueArr.element[i].valueArr.element[i1].valueArr);
					     //rec1.valueArr.element[i].valueArr.element[i1].valueArr.length = rec2.valueArr.element[i].valueArr.element[i1].valueArr.length;
					     "\t\t\tfor(i2 : 0 .. MAX_INT_ARRAY_SIZE-1) {\n" +
					     	"\t\t\t\trec1.valueArr.element[i].valueArr.element[i1].valueArr.element[i2] = rec2.valueArr.element[i].valueArr.element[i1].valueArr.element[i2];\n" +
					     "\t\t\t}\n" +
					     "\t\t\trec1.valueArr.element[i].valueArr.element[i1].gArrIndex = rec2.valueArr.element[i].valueArr.element[i1].gArrIndex;\n" +
					     "\t\t\trec1.valueArr.element[i].valueArr.element[i1].keyToPut = rec2.valueArr.element[i].valueArr.element[i1].keyToPut\n" +
					     "\t\t\trec1.valueArr.element[i].valueArr.element[i1].valueToPut = rec2.valueArr.element[i].valueArr.element[i1].valueToPut\n" +
					     "\t\t\trec1.valueArr.element[i].valueArr.element[i1].isAlive = rec2.valueArr.element[i].valueArr.element[i1].isAlive\n" +
					  "\t\t}\n" +
					  "\t\trec1.valueArr.element[i].gArrIndex = rec2.valueArr.element[i].gArrIndex\n" +
					  "\t\trec1.valueArr.element[i].keyToPut = rec2.valueArr.element[i].keyToPut\n" +
				      //assign_CInt2IntMap_rec(rec1.valueArr.element[i].valueToPut, rec2.valueArr.element[i].valueToPut)\n" +
				      "\t\trec1.valueArr.element[i].valueToPut.size = rec2.valueArr.element[i].valueToPut.size\n" +
				      //assign_int_arr(rec1.valueArr.element[i].valueToPut.keyArr, rec2.valueArr.element[i].valueToPut.keyArr)\n" +
				      //rec1.valueArr.element[i].valueToPut.keyArr.length = rec2.valueArr.element[i].valueToPut.keyArr.length\n" +
				      "\t\tfor(i1 : 0 .. MAX_INT_ARRAY_SIZE-1) {\n" +
				      		"\t\t\trec1.valueArr.element[i].valueToPut.keyArr.element[i1] = rec2.valueArr.element[i].valueToPut.keyArr.element[i1]\n" +
				      "\t\t}\n" +
				      //assign_int_arr(rec1.valueArr.element[i].valueToPut.valueArr, rec2.valueArr.element[i].valueToPut.valueArr)\n" +
				      //rec1.valueArr.element[i].valueToPut.valueArr.length = rec2.valueArr.element[i].valueToPut.valueArr.length\n" +
				      "\t\tfor(i1 : 0 .. MAX_INT_ARRAY_SIZE-1) {\n" +
				      		"\t\t\trec1.valueArr.element[i].valueToPut.valueArr.element[i1] = rec2.valueArr.element[i].valueToPut.valueArr.element[i1]\n" +
				      "\t\t}\n" +
				      "\t\trec1.valueArr.element[i].valueToPut.gArrIndex = rec2.valueArr.element[i].valueToPut.gArrIndex\n" +
				      "\t\trec1.valueArr.element[i].valueToPut.keyToPut = rec2.valueArr.element[i].valueToPut.keyToPut\n" +
				      "\t\trec1.valueArr.element[i].valueToPut.valueToPut = rec2.valueArr.element[i].valueToPut.valueToPut\n" +
				      "\t\trec1.valueArr.element[i].valueToPut.isAlive = rec2.valueArr.element[i].valueToPut.isAlive\n" +
					  
					  "\t\trec1.valueArr.element[i].isAlive = rec2.valueArr.element[i].isAlive\n" +
				   "\t}\n" +
				   
				   "\trec1.gArrIndex = rec2.gArrIndex\n" +
				   "\trec1.keyToPut = rec2.keyToPut\n" +
				   
				   //assign_CInt2IIMMap_rec(rec1.valueToPut, rec2.valueToPut)\n" +
				   "\trec1.valueToPut.size = rec2.valueToPut.size\n" +
				   //assign_int_arr(rec1.valueToPut.keyArr, rec2.valueToPut.keyArr)\n" +
				   //rec1.valueToPut.keyArr.length = rec2.valueToPut.keyArr.length\n" +
				   "\tfor(i : 0 .. MAX_INT_ARRAY_SIZE-1) {\n" +
				   		"\t\trec1.valueToPut.keyArr.element[i] = rec2.valueToPut.keyArr.element[i]\n" +
				   	"\t}\n" +
				   //assign_CInt2IntMap_arr(rec1.valueToPut.valueArr, rec2.valueToPut.valueArr)\n" +
				   //rec1.valueToPut.valueArr.length = rec2.valueToPut.valueArr.length\n" +
				   "\tfor(i : 0 .. MAX_INT_ARRAY_SIZE-1) {\n" +
					   //assign_CInt2IntMap_rec(rec1.valueToPut.valueArr.element[i], rec2.valueToPut.valueArr.element[i])\n" +
					   "\t\trec1.valueToPut.valueArr.element[i].size = rec2.valueToPut.valueArr.element[i].size\n" +
					   //assign_int_arr(rec1.valueToPut.valueArr.element[i].keyArr, rec2.valueToPut.valueArr.element[i].keyArr)\n" +
					   //rec1.valueToPut.valueArr.element[i].keyArr.length = rec2.valueToPut.valueArr.element[i].keyArr.length\n" +
					   "\t\tfor(i1 : 0 .. MAX_INT_ARRAY_SIZE-1) {\n" +
					   		"\t\t\trec1.valueToPut.valueArr.element[i].keyArr.element[i1] = rec2.valueToPut.valueArr.element[i].keyArr.element[i1]\n" +
					   "\t\t}\n" +
					   //assign_int_arr(rec1.valueToPut.valueArr.element[i].valueArr, rec2.valueToPut.valueArr.element[i].valueArr)\n" +
					   //rec1.valueToPut.valueArr.element[i].valueArr.length = rec2.valueToPut.valueArr.element[i].valueArr.length\n" +
					   "\t\tfor(i1 : 0 .. MAX_INT_ARRAY_SIZE-1) {\n" +
					   		"\t\t\trec1.valueToPut.valueArr.element[i].valueArr.element[i1] = rec2.valueToPut.valueArr.element[i].valueArr.element[i1]\n" +
					   "\t\t}\n" +
					   "\t\trec1.valueToPut.valueArr.element[i].gArrIndex = rec2.valueToPut.valueArr.element[i].gArrIndex\n" +
					   "\t\trec1.valueToPut.valueArr.element[i].keyToPut = rec2.valueToPut.valueArr.element[i].keyToPut\n" +
					   "\t\trec1.valueToPut.valueArr.element[i].valueToPut = rec2.valueToPut.valueArr.element[i].valueToPut\n" +
					   "\t\trec1.valueToPut.valueArr.element[i].isAlive = rec2.valueToPut.valueArr.element[i].isAlive\n" +
				   "\t}\n" +
				   "\trec1.valueToPut.gArrIndex = rec2.valueToPut.gArrIndex\n" +
				   "\trec1.valueToPut.keyToPut = rec2.valueToPut.keyToPut\n" +
				   //assign_CInt2IntMap_rec(rec1.valueToPut.valueToPut, rec2.valueToPut.valueToPut)\n" +
				   "\trec1.valueToPut.valueToPut.size = rec2.valueToPut.valueToPut.size\n" +
				   //assign_int_arr(rec1.valueToPut.valueToPut.keyArr, rec2.valueToPut.valueToPut.keyArr)\n" +
				   //rec1.valueToPut.valueToPut.keyArr.length = rec1.valueToPut.valueToPut.keyArr.length\n" +
				   "\tfor(i : 0 .. MAX_INT_ARRAY_SIZE-1) {\n" +
				   		"\t\trec1.valueToPut.valueToPut.keyArr.element[i] = rec1.valueToPut.valueToPut.keyArr.element[i]\n" +
				   	"\t}\n" +
				   //assign_int_arr(rec1.valueToPut.valueToPut.valueArr, rec2.valueToPut.valueToPut.valueArr)\n" +
				   //rec1.valueToPut.valueToPut.valueArr.length = rec2.valueToPut.valueToPut.valueArr.length\n" +
				   "\tfor(i : 0 .. MAX_INT_ARRAY_SIZE-1) {\n" +
				   		"\t\trec1.valueToPut.valueToPut.valueArr.element[i] = rec2.valueToPut.valueToPut.valueArr.element[i]\n" +
				   "\t}\n" +
				   "\trec1.valueToPut.valueToPut.gArrIndex = rec2.valueToPut.valueToPut.gArrIndex\n" +
				   "\trec1.valueToPut.valueToPut.keyToPut = rec2.valueToPut.valueToPut.keyToPut\n" +
				   "\trec1.valueToPut.valueToPut.valueToPut = rec2.valueToPut.valueToPut.valueToPut\n" +
				   "\trec1.valueToPut.valueToPut.isAlive = rec2.valueToPut.valueToPut.isAlive\n" +
				   "\trec1.valueToPut.isAlive = rec2.valueToPut.isAlive\n" +
				   "\trec1.isAlive = rec2.isAlive\n" +
				"}");
	}
	
	private void declareMapArrAssignmentMethods()
	{
		/* CInt2IntMap */
		println("inline assign_CInt2IntMap_arr(arr1, arr2)\n" +
				"{\n" +
					"\tarr1.length = arr2.length;\n" +
					"\tfor(i0 : 0 .. MAX_INT_ARRAY_SIZE-1) {\n" +
						"\tassign_CInt2IntMap_rec(arr1.element[i0], arr2.element[i0]);\n" +
					"\t}\n" +
				"}");
		
		/* CInt2IIMMap */
		println("inline assign_CInt2IIMMap_arr(arr1, arr2)\n" +
				"{\n" +
					"\tarr1.length = arr2.length;\n" +
					"\tfor(i0 : 0 .. MAX_INT_ARRAY_SIZE-1) {\n" +
						"\t\tassign_CInt2IIMMap_rec(arr1.element[i0], arr2.element[i0]);\n" +
					"\t}\n" +
				"}");
		
		/* CInt2IIIMMap */
		println("inline assign_CInt2IIIMMap_arr(arr1, arr2)\n" +
				"{\n" +
					"\tarr1.length = arr2.length;\n" +
					"\tfor(i0 : 0 .. MAX_INT_ARRAY_SIZE-1) {\n" +
						"\t\tassign_CInt2IIIMMap_rec(arr1.element[i0], arr2.element[i0]);\n" +
					"\t}\n" +
				"}");
	}
	/* [Thomas, May 2nd, 2017]
	 * typedef STMotionSensor_rec { type_5 motionDetected; }
	 * =>
	 * inline assign_STMotionSensor_rec(rec1, rec2)
	 * {
	 * 		rec1.motionDetected = rec2.motionDetected;
	 * }
	 * 
	 * typedef STMotionSensor_arr { byte length; STMotionSensor_rec element[3] }
	 * =>
	 * inline assign_STMotionSensor_arr(arr1, arr2)
	 * {
	 * 		int i;
	 * 		arr1.length = arr2.length;
	 * 		for(i : 0 .. arr2.length-1) {
	 * 			assign_STMotionSensor_rec(arr1.element[i], arr2.element[i]);
	 * 		}
	 * }
	 * */
	private void declareAssignMethods()
	{
		Vector types = system.getTypes();
		int index = 0;
		
		indentLevel = 0;
		println("/*************** Start of assignment methods *****************/");
		
		/* Print out counter variables */
		{
			int maxCounter = getMaxIncrCounter();
			
			println("hidden short _index;");
			println("hidden short i;");
			for(int counter = 0; counter < maxCounter; counter++)
			{
				println("hidden short i" + counter + ";");
			}
			println();
		}

		for (int i = 0; i < types.size(); i++) {
			Type type = (Type) types.elementAt(i);
			if (type.isKind(ARRAY | RECORD)) {
				if(!type.typeName().startsWith("type_"))
				{
					String methName = SpinUtil.getAssignMethName(type.typeName());

					if(methName != null)
					{
						String methHeader = null;

						if(methName.endsWith("_rec"))
						{
							methHeader = "inline " + methName + "(rec1, rec2)";
						}
						else if(methName.endsWith("_arr"))
						{
							methHeader = "inline " + methName + "(arr1, arr2)";
						}

						if(methHeader != null)
						{
							indentLevel = 0;
							println(methHeader);
							println("{");

							indentLevel = 1;
							if (type.isKind(RECORD)) {
								Vector fields = ((Record)type).getFields();

								for (int j = 0; j < fields.size(); j++) {
									Field recField = (Field) fields.elementAt(j);
									String recFieldName = recField.getName();

									if(recField.getType().isKind(ARRAY | RECORD))
									{
										println("assign_" + recField.getType().getName() + "(rec1."
												+ recFieldName + ", rec2." + recFieldName + ");");
									}
									else if(recField.getType().isKind(REF))
									{
										Type targetType = ((Ref)recField.getType()).getTargetType();

										println("assign_" + targetType.getName() + "(rec1."
												+ recFieldName + ", rec2." + recFieldName + ");");
									}
									else if(!recFieldName.startsWith("STCommand_"))
									{
										println("rec1." + recFieldName + " = rec2." + 
												recFieldName + ";");
									}
								}
								
								/* Assign additional fields */
								println("rec1.isAlive = rec2.isAlive;");
								if(SpinUtil.isADevice(type.typeName()))
								{
									println("rec1.NumReceivedCommands = rec2.NumReceivedCommands;");
									//println("rec1.ToDeviceChannel = rec2.ToDeviceChannel;");
									//println("rec1.FromDeviceChannel = rec2.FromDeviceChannel;");
									if(type.typeName().equals("STLocation_rec"))
									{
										println("rec1.LatestCommandType = rec2.LatestCommandType;");
										println("rec1.LatestCommandID = rec2.LatestCommandID;");
									}
									else
									{
										println("rec1.currEvtIndex = rec2.currEvtIndex;");
									}
								}
								else if(type.typeName().equals("STEvent_rec"))
								{
									println("rec1.EvtType = rec2.EvtType;");
								}
							}
							else if (type.isKind(ARRAY)) {
								Type baseType = ((Array)type).getBaseType();

								if(baseType.isKind(REF))
								{
									baseType = ((Ref)baseType).getTargetType();
								}

//								println("int i" + index + ";");
//								println();
								
								println("arr1.length = arr2.length;");
								
//								println("for(i" + index + " : 0 .. arr1.length-1) {");
								println("for(i" + index + " : 0 .. MAX_ARRAY_SIZE-1) {");
								indentLevel = 2;
								if (baseType.isKind(ARRAY | RECORD)) {
									println("assign_" + baseType.getName() + 
											"(arr1.element[i"+ index + "], arr2.element[i" + index + "]);");
								}
								else
								{
									println("arr1.element[i" + index + "] = arr2.element[i" + index + "];");
								}
								indentLevel = 1;
								println("}");
								index++;
							}
							indentLevel = 0;
							println("}");
						}
					}
				}
			}
		}
		/* int array */
		{
			println("inline assign_int_arr(arr1, arr2)\n" +
					"{\n" +
					   "\tarr1.length = arr2.length;\n" +
						"\tfor(i0 : 0 .. MAX_INT_ARRAY_SIZE-1) {\n" +
							"\t\tarr1.element[i0] = arr2.element[i0];\n" +
						"\t}\n" +
					"}");
		}
		if(GConfigInfoManager.isStateMapUsed())
		{
			declareMapRecAssignmentMethods();
			declareMapArrAssignmentMethods();
		}

		println("/*************** End of assignment methods *******************/");
		println();
	}
	
	private int getMaxIncrCounter()
	{
		Vector types = system.getTypes();
		int index = 0;

		for (int i = 0; i < types.size(); i++) {
			Type type = (Type) types.elementAt(i);
			if (type.isKind(ARRAY | RECORD)) {
				if(!type.typeName().startsWith("type_"))
				{
					String methName = SpinUtil.getAssignMethName(type.typeName());

					if(methName != null)
					{
						if(methName.endsWith("_rec") || methName.endsWith("_arr"))
						{
							if (type.isKind(ARRAY)) {
								index++;
							}
						}
					}
				}
			}
		}
		return index;
	}

	private void declareSmartAppManager()
	{
		String latestEvtIndex = "latestEvtIndex" + indexValue;
		
		println("/*************** Start of smart manager *************************/");
		println("proctype SmartAppManager()");
		println("{");

		println("\tSTEvent_rec evt;");
		println("\tbyte " + latestEvtIndex + ";");
		println();
		println("end: do");
		{
			Set<String> smartApps = GPotentialRiskScreener.getCurrentSmartAppNames();
			Set<String> evtHandlers = GPotentialRiskScreener.getCurrentEvtHandlers();
			String evtHandler;
			
			for(String smartAppName : smartApps)
			{
				java.util.List<GProcessedSubscriptionInfo> subscriptionInfoList = GConfigInfoManager.getSubscriptionInfo(smartAppName);
				
				println("\t/* Handlers of " + smartAppName + " */");
				{
					for(GProcessedSubscriptionInfo subscriptionInfo : subscriptionInfoList)
					{
						if(subscriptionInfo.inputName.equals("location"))
						{
							evtHandler = smartAppName + "_" + subscriptionInfo.subscriptionInfoList.get(0).evtHandler;
							
							println("\tif");
							println("\t:: location.BroadcastChans[" + smartAppName + "_location] == true -> atomic {");
							println("\t\t/* Reset broadcast event indicator */");
							println("\t\tlocation.BroadcastChans[" + smartAppName + "_location] = false;");
							println();
							
							println("\t\t/* Fetch current data of global variables */");
							boolean skipNeeded = true;
							println("\t\td_step {");
							for(GDeviceConfigInfo dci : GConfigInfoManager.getDeviceConfigInfo(smartAppName))
							{
								if(dci.isMultiple)
								{
									int index = 0;
									
									for(String deviceName : dci.configDevices)
									{
										/* frontDoorSensor_STMotionSensor */
										String name = SpinUtil.getGlobalArrRef(deviceName, dci.deviceType);
										
										println("\t\t\tassign_" + dci.deviceType + 
												"_rec(" + dci.inputName + ".element[" + index + "], " + name + ");");
										index++;
										skipNeeded = false;
									}
								}
								else
								{
									String name = SpinUtil.getGlobalArrRef(dci.configDevices.get(0), dci.deviceType);
									
									println("\t\t\tassign_" + dci.deviceType + 
											"_rec(" + dci.inputName + ", " + name + ");");
									skipNeeded = false;
								}
							}
							if(skipNeeded)
							{
								println("\t\t\tskip;");
							}
							println("\t\t}");
							println();
							
							if(evtHandlers.contains(evtHandler))
							{
								println("\t\t" + evtHandler + "(location.latestEvt);");
							}
							else
							{
								println("\t\tskip;");
							}
							println("\t}");
							println("\t:: else -> skip;");
							println("\tfi");
						}
						else if(subscriptionInfo.inputName.equals("app"))
						{
//							evtHandler = smartAppName + "_" + subscriptionInfo.subscriptionInfoList.get(0).evtHandler;
//							
//							println("\t:: " + smartAppName + "_AppChan?evt -> atomic {");
//							println("\t\t/* Fetch current data of global variables */");
//							println("\t\td_step {");
//							for(GDeviceConfigInfo dci : GConfigInfoManager.getDeviceConfigInfo(smartAppName))
//							{
//								if(dci.isMultiple)
//								{
//									int index = 0;
//									
//									for(String deviceName : dci.configDevices)
//									{
//										/* frontDoorSensor_STMotionSensor */
//										String name = SpinUtil.getGlobalArrRef(deviceName, dci.deviceType);
//										
//										println("\t\t\tassign_" + dci.deviceType + 
//												"_rec(" + dci.inputName + ".element[" + index + "], " + name + ");");
//										index++;
//									}
//								}
//								else
//								{
//									String name = SpinUtil.getGlobalArrRef(dci.configDevices.get(0), dci.deviceType);
//									
//									println("\t\t\tassign_" + dci.deviceType + 
//											"_rec(" + dci.inputName + ", " + name + ");");
//								}
//							}
//							println("\t\t}");
//							println();
//							
//							if(evtHandlers.contains(evtHandler))
//							{
//								println("\t\t" + evtHandler + "(evt);");
//							}
//							else
//							{
//								println("\t\tskip;");
//							}
//							println("\t}");
						}
						else
						{
							int numConfigDevices = GConfigInfoManager.getNumConfigDevices(smartAppName, subscriptionInfo.inputName);
							String deviceType = GConfigInfoManager.getDeviceType(smartAppName, subscriptionInfo.inputName);
							
							if(!GConfigInfoManager.isInputDeviceMultiple(smartAppName, subscriptionInfo.inputName))
							{
								String arrIndex = subscriptionInfo.inputName + ".id";
								String broadcastRef = "_g_" + deviceType + "Arr.element[" + arrIndex
										+ "].BroadcastChans[" + subscriptionInfo.inputName + ".BroadcastChanIndex]";
								
								println("\tif");
								println("\t:: " + broadcastRef + " == true -> atomic {");
								{
									println("\t\t/* Reset broadcast event indicator */");
									println("\t\t " + broadcastRef + " = false");
									println();
									
									println("\t\t/* Get latest event */");
									{
										println("\t\tgetPrevStoredEvtIndex(_g_" + deviceType + "Arr.element[" + arrIndex 
												+ "].currEvtIndex, " + latestEvtIndex + ");");
										println("\t\tassign_STEvent_rec(evt, " + "_g_" + deviceType + "Arr.element[" + arrIndex 
										        + "].events.element[" + latestEvtIndex + "]);");
									}
									println();
									
									println("\t\t/* Fetch current data of global variables */");
									boolean skipNeeded = true;
									println("\t\td_step {");
									for(GDeviceConfigInfo dci : GConfigInfoManager.getDeviceConfigInfo(smartAppName))
									{
										if(dci.isMultiple)
										{
											int index = 0;
											
											for(String deviceName : dci.configDevices)
											{
												/* frontDoorSensor_STMotionSensor */
												String name = SpinUtil.getGlobalArrRef(deviceName, dci.deviceType);
												
												println("\t\t\tassign_" + dci.deviceType + 
														"_rec(" + dci.inputName + ".element[" + index + "], " + name + ");");
												index++;
												skipNeeded = false;
											}
										}
										else
										{
											String name = SpinUtil.getGlobalArrRef(dci.configDevices.get(0), dci.deviceType);
											
											println("\t\t\tassign_" + dci.deviceType + 
													"_rec(" + dci.inputName + ", " + name + ");");
											skipNeeded = false;
										}
									}
									if(skipNeeded)
									{
										println("\t\t\tskip;");
									}
									println("\t\t}");
									println();
									
									/*****************/
									println("\t\tif");
									for(GSubscriptionInfo subEvtInfo : subscriptionInfo.subscriptionInfoList)
									{
										String evtType = subEvtInfo.subscribedEvtType;
										String printStr;
										
										evtHandler = smartAppName + "_" + subEvtInfo.evtHandler;
	
										if(evtType.equals(""))
										{
											/* Print out event handler */
											if(evtHandlers.contains(evtHandler))
											{
												printStr = "\t\t:: true -> " + evtHandler + "(evt);";
											}
											else
											{
												printStr = "\t\t:: true -> skip;";
											}
											println(printStr);
										}
										else
										{
											/* Print out event type check and event handler */
											if(evtHandlers.contains(evtHandler))
											{
												printStr = "\t\t:: evt.EvtType == " + evtType + " -> " + evtHandler + "(evt);";
											}
											else
											{
												printStr = "\t\t:: evt.EvtType == " + evtType + " -> skip;";
											}
											println(printStr);
										}
									}
									println("\t\t:: else -> skip;");
									println("\t\tfi");
									/*****************/
								}
								println("\t}");
								println("\t:: else -> skip;");
								println("\tfi");
							}
							else
							{
								for(int i = 0; i < numConfigDevices; i++)
								{
									String arrIndex = subscriptionInfo.inputName + ".element[" + i + "]" + ".id";
									String broadcastRef = "_g_" + deviceType + "Arr.element[" + arrIndex
											+ "].BroadcastChans[" + subscriptionInfo.inputName + ".element[" + i + "]" 
											+ ".BroadcastChanIndex]";
									
									println("\tif");
									println("\t:: " + broadcastRef + " == true -> atomic {");
									{
										println("\t\t/* Reset broadcast event indicator */");
										println("\t\t " + broadcastRef + " = false");
										println();
										
										println("\t\t/* Get latest event */");
										{
											println("\t\tgetPrevStoredEvtIndex(_g_" + deviceType + "Arr.element[" + arrIndex 
													+ "].currEvtIndex, " + latestEvtIndex + ");");
											println("\t\tassign_STEvent_rec(evt, " + "_g_" + deviceType + "Arr.element[" + arrIndex 
											        + "].events.element[" + latestEvtIndex + "]);");
										}
										println();
										
										println("\t\t/* Fetch current data of global variables */");
										boolean skipNeeded = true;
										println("\t\td_step {");
										for(GDeviceConfigInfo dci : GConfigInfoManager.getDeviceConfigInfo(smartAppName))
										{
											if(dci.isMultiple)
											{
												int index = 0;
												
												for(String deviceName : dci.configDevices)
												{
													/* frontDoorSensor_STMotionSensor */
													String name = SpinUtil.getGlobalArrRef(deviceName, dci.deviceType);
													
													println("\t\t\tassign_" + dci.deviceType + 
															"_rec(" + dci.inputName + ".element[" + index + "], " + name + ");");
													index++;
													skipNeeded = false;
												}
											}
											else
											{
												String name = SpinUtil.getGlobalArrRef(dci.configDevices.get(0), dci.deviceType);
												
												println("\t\t\tassign_" + dci.deviceType + 
														"_rec(" + dci.inputName + ", " + name + ");");
												skipNeeded = false;
											}
										}
										if(skipNeeded)
										{
											println("\t\t\tskip;");
										}
										println("\t\t}");
										println();
										
										/*****************/
										println("\t\tif");
										for(GSubscriptionInfo subEvtInfo : subscriptionInfo.subscriptionInfoList)
										{
											String evtType = subEvtInfo.subscribedEvtType;
											String printStr;
											
											evtHandler = smartAppName + "_" + subEvtInfo.evtHandler;
		
											if(evtType.equals(""))
											{
												/* Print out event handler */
												if(evtHandlers.contains(evtHandler))
												{
													printStr = "\t\t:: true -> " + evtHandler + "(evt);";
												}
												else
												{
													printStr = "\t\t:: true -> skip;";
												}
												println(printStr);
											}
											else
											{
												/* Print out event type check and event handler */
												if(evtHandlers.contains(evtHandler))
												{
													printStr = "\t\t:: evt.EvtType == " + evtType + " -> " + evtHandler + "(evt);";
												}
												else
												{
													printStr = "\t\t:: evt.EvtType == " + evtType + " -> skip;";
												}
												println(printStr);
											}
										}
										println("\t\t:: else -> skip;");
										println("\t\tfi");
										/*****************/
									}
									println("\t}");
									println("\t:: else -> skip;");
									println("\tfi");
								}
							}
						}
					}
				}
				println();
			}
		}
		println("\tod");
		println("}");
		println("/*************** End of smart manager ***************************/");
		println();
		
		indexValue++;
	}
	
	private void declareAnActuatorEvtHandler(String deviceType)
	{
		DeviceNameInfo device = SpinUtil.getDeviceNameInfo(deviceType);
		
		if(device != null)
		{
			println("hidden byte _index" + indexValue + ";");
			println("inline Handle" + deviceType + "Evt(deviceIndex, Evt)");
			println("{");
			println("\tbyte latestEvtIndex" + indexValue + ";");
			if(!SpinUtil.isANumberCommand(deviceType))
			{
				println("\tshort Handle" + deviceType + "Evt_state;");
			}
			println();
			
//			println("\tatomic {");
			println("\td_step {");
			println("\t\t" + device.gArr + ".element[deviceIndex].isOnline = 1;");
			
			if(deviceType.equals("STLock"))
			{
				println("\t\t/* Smart app cannot change lock code */");
				println("\t\tif");
				println("\t\t:: Evt.EvtType == usercodechange -> assert(0);");
				println("\t\t:: else -> skip;");
				println("\t\tfi");
				println();
			}
			
			println("\t\t/* Validate the counter */");
			println("\t\tif");
			println("\t\t:: " + device.gArr + ".element[deviceIndex].currEvtIndex >= MAX_STORED_EVENTS -> " 
					+ device.gArr + ".element[deviceIndex].currEvtIndex = 0;");
			println("\t\t:: else -> skip;");
			println("\t\tfi");
			println();
			
			println("\t\t/* Check conflict commands */");
			println("\t\tgetPrevStoredEvtIndex(" + device.gArr 
					+ ".element[deviceIndex].currEvtIndex, latestEvtIndex" + indexValue + ");");
			println("\t\tif");
//			println("\t\t:: (" + device.gArr 
//					+ ".element[deviceIndex].events.element[latestEvtIndex" + indexValue + "].id == Evt.id) -> assert(Evt.EvtType == " 
//					+ device.gArr + ".element[deviceIndex].events.element[latestEvtIndex" + indexValue + "].EvtType);");
			println("\t\t:: (Evt.physical == 0) && (" + device.gArr 
					+ ".element[deviceIndex].events.element[latestEvtIndex" + indexValue + "].id == Evt.id) && " 
					+ "(" + device.gArr + ".element[deviceIndex].events.element[latestEvtIndex" 
					+ indexValue + "].date == STCurrentSystemTime*3600000) -> ");
			println("\t\t\tassert(Evt.EvtType == " + device.gArr + ".element[deviceIndex].events.element[latestEvtIndex" + indexValue + "].EvtType);");
			println("\t\t:: else -> skip;");
			println("\t\tfi");
			println();
			
			println("\t\t/* Increase number of received commands per event */");
			println("\t\tif");
			println("\t\t:: (Evt.physical == 0) && (" + device.gArr + ".element[deviceIndex].events.element[latestEvtIndex" + 
					indexValue + "].id == 0) || ((" + device.gArr + ".element[deviceIndex].events.element[latestEvtIndex" + 
					indexValue + "].id == Evt.id) && " 
					+ "(" + device.gArr + ".element[deviceIndex].events.element[latestEvtIndex" 
					+ indexValue + "].date == STCurrentSystemTime*3600000)) -> "
					+ device.gArr + ".element[deviceIndex].NumReceivedCommands++;");
			println("\t\t:: else -> skip;");
			println("\t\tfi");
			println("\t\t/* Check DOS attack or infinite loop of commands */");
			println("\t\tif");
			println("\t\t:: (" + device.gArr + ".element[deviceIndex].NumReceivedCommands >= MAX_COMMAND_REPITIONS) -> assert(0);");
			println("\t\t:: else -> skip;");
			println("\t\tfi");
			
//			println("\t}");//
			
			println();
	
//			println("\tif");//
//			println("\t:: ((Evt.EvtType != " + device.gArr 
//					+ ".element[deviceIndex].events.element[latestEvtIndex" + indexValue + "].EvtType) || (" 
//					+ device.gArr + ".element[deviceIndex].events.element[latestEvtIndex" + indexValue + "].id == 0)) -> {");//
//			println("\td_step {");//
				
			if(SpinUtil.isANumberCommand(deviceType))
			{
				println("\t\t/* Update current state */");
				println("\t\t" + device.gArr + ".element[deviceIndex]." + device.currentDevice + " = Evt.value;");
				println("\t\t" + device.gArr + ".element[deviceIndex]." + device.deviceState + ".value = Evt.value;");
				println("\t\t" + device.gArr + ".element[deviceIndex]." 
						+ device.deviceState + ".name = " + device.deviceStateName + ";");
				println("\t\t" + device.gArr + ".element[deviceIndex]." + device.deviceState + ".date = STCurrentSystemTime*3600000;");
				println("\t\t" + device.gArr + ".element[deviceIndex]." + device.deviceState + ".isAlive = 1;");
				println();
				
				println("\t\t/* Store new state */");
				println("\t\tassign_STState_rec(" + device.gArr + ".element[deviceIndex].states.element[" 
						+ device.gArr + ".element[deviceIndex].currEvtIndex], " + device.gArr + ".element[deviceIndex]." + device.deviceState + ");");
				println();
		
				println("\t\t/* Store the current event */");
				println("\t\tEvt.name = " + device.deviceStateName + ";");
				println("\t\tEvt.date = STCurrentSystemTime*3600000;");
				println("\t\tEvt.isAlive = 1;");
				println("\t\tassign_STEvent_rec(" + device.gArr + ".element[deviceIndex].events.element[" 
						+ device.gArr + ".element[deviceIndex].currEvtIndex], Evt);");
				println();
		
				println("\t\t/* Increment the counter */");
				println("\t\t" + device.gArr + ".element[deviceIndex].currEvtIndex++;");
				println();
					
//				println("\t\t_index" + indexValue + " = 0;");
//				println("\t}");
				
				println("\t\t/* Broadcast the state change event to subscribers */");
				println("\t\tfor(_index" + indexValue + " : 0 .. " + device.gArr + ".element[deviceIndex].NumSubscribers-1) {");
				println("\t\t\t" + device.gArr + ".element[deviceIndex].BroadcastChans[_index" + indexValue + "] = true;");
				println("\t\t}");
			}
			else
			{
				println("\t\t/* Update current state if it is different from latest state */");
				println("\t\tif");
				println("\t\t:: (Evt.EvtType != " + device.gArr + ".element[deviceIndex].events.element[latestEvtIndex" + indexValue + "].EvtType) || ("
						+ device.gArr + ".element[deviceIndex].events.element[latestEvtIndex" + indexValue + "].id == 0) -> {");
				println("\t\t\tgetIntValueFromMtype(Evt.EvtType, Handle" + deviceType + "Evt_state);");
				println("\t\t\t" + device.gArr + ".element[deviceIndex]." + device.currentDevice + " = Handle" + deviceType + "Evt_state;");
				println("\t\t\t" + device.gArr + ".element[deviceIndex]." + device.deviceState + ".value = Handle" + deviceType + "Evt_state;");
				println("\t\t\t" + device.gArr + ".element[deviceIndex]." 
						+ device.deviceState + ".name = " + device.deviceStateName + ";");
				println("\t\t\t" + device.gArr + ".element[deviceIndex]." + device.deviceState + ".date = STCurrentSystemTime*3600000;");
				println("\t\t\t" + device.gArr + ".element[deviceIndex]." + device.deviceState + ".isAlive = 1;");
				println();
				
				println("\t\t\t/* Store new state */");
				println("\t\t\tassign_STState_rec(" + device.gArr + ".element[deviceIndex].states.element[" 
						+ device.gArr + ".element[deviceIndex].currEvtIndex], " + device.gArr + ".element[deviceIndex]." + device.deviceState + ");");
				println();
		
				println("\t\t\t/* Store the current event */");
				println("\t\t\tEvt.name = " + device.deviceStateName + ";");
				println("\t\t\tEvt.value = Handle" + deviceType + "Evt_state;");
				println("\t\t\tEvt.date = STCurrentSystemTime*3600000;");
				println("\t\t\tEvt.isAlive = 1;");
				println("\t\t\tassign_STEvent_rec(" + device.gArr + ".element[deviceIndex].events.element[" 
						+ device.gArr + ".element[deviceIndex].currEvtIndex], Evt);");
				println();
		
				println("\t\t\t/* Increment the counter */");
				println("\t\t\t" + device.gArr + ".element[deviceIndex].currEvtIndex++;");
				println();
				
				println("\t\t\t/* Broadcast the state change event to subscribers */");
				println("\t\t\tfor(_index" + indexValue + " : 0 .. " + device.gArr + ".element[deviceIndex].NumSubscribers-1) {");
				println("\t\t\t\t" + device.gArr + ".element[deviceIndex].BroadcastChans[_index" + indexValue + "] = true;");
				println("\t\t\t}");
				
				println("\t\t}");
				println("\t\t:: else -> skip;");
				println("\t\tfi");
			}
			
			println("\t}");
//			println("\tfi");
			println("}");
			
			indexValue++;
		}
		else
		{
			System.out.println("[SpinTrans][declareAnActuatorEvtManager] unsupported device type: " + deviceType);
		}
	}
	
	private void declareAnActuatorEvtHandlerWithDeviceFailure(String deviceType)
  {
    DeviceNameInfo device = SpinUtil.getDeviceNameInfo(deviceType);
    
    if(device != null)
    {
      println("hidden byte _index" + indexValue + ";");
      println("inline Handle" + deviceType + "Evt(deviceIndex, Evt)");
      println("{");
      println("\tbyte latestEvtIndex" + indexValue + ";");
      if(!SpinUtil.isANumberCommand(deviceType))
      {
        println("\tshort Handle" + deviceType + "Evt_state;");
      }
      println();
      
//      println("\tatomic {");
      println("\tif");
      println("\t:: " + device.gArr + ".element[deviceIndex].isOnline = 0;");
      println("\t:: d_step {");
      println("\t\t" + device.gArr + ".element[deviceIndex].isOnline = 1;");
      
      if(deviceType.equals("STLock"))
      {
        println("\t\t/* Smart app cannot change lock code */");
        println("\t\tif");
        println("\t\t:: Evt.EvtType == usercodechange -> assert(0);");
        println("\t\t:: else -> skip;");
        println("\t\tfi");
        println();
      }
      
      println("\t\t/* Validate the counter */");
      println("\t\tif");
      println("\t\t:: " + device.gArr + ".element[deviceIndex].currEvtIndex >= MAX_STORED_EVENTS -> " 
          + device.gArr + ".element[deviceIndex].currEvtIndex = 0;");
      println("\t\t:: else -> skip;");
      println("\t\tfi");
      println();
      
      println("\t\t/* Check conflict commands */");
      println("\t\tgetPrevStoredEvtIndex(" + device.gArr 
          + ".element[deviceIndex].currEvtIndex, latestEvtIndex" + indexValue + ");");
      println("\t\tif");
//      println("\t\t:: (" + device.gArr 
//          + ".element[deviceIndex].events.element[latestEvtIndex" + indexValue + "].id == Evt.id) -> assert(Evt.EvtType == " 
//          + device.gArr + ".element[deviceIndex].events.element[latestEvtIndex" + indexValue + "].EvtType);");
      println("\t\t:: (Evt.physical == 0) && (" + device.gArr 
          + ".element[deviceIndex].events.element[latestEvtIndex" + indexValue + "].id == Evt.id) && " 
          + "(" + device.gArr + ".element[deviceIndex].events.element[latestEvtIndex" 
          + indexValue + "].date == STCurrentSystemTime*3600000) -> ");
      println("\t\t\tassert(Evt.EvtType == " + device.gArr + ".element[deviceIndex].events.element[latestEvtIndex" + indexValue + "].EvtType);");
      println("\t\t:: else -> skip;");
      println("\t\tfi");
      println();
      
      println("\t\t/* Increase number of received commands per event */");
      println("\t\tif");
      println("\t\t:: (Evt.physical == 0) && (" + device.gArr + ".element[deviceIndex].events.element[latestEvtIndex" + 
          indexValue + "].id == 0) || ((" + device.gArr + ".element[deviceIndex].events.element[latestEvtIndex" + 
          indexValue + "].id == Evt.id) && " 
          + "(" + device.gArr + ".element[deviceIndex].events.element[latestEvtIndex" 
          + indexValue + "].date == STCurrentSystemTime*3600000)) -> "
          + device.gArr + ".element[deviceIndex].NumReceivedCommands++;");
      println("\t\t:: else -> skip;");
      println("\t\tfi");
      println("\t\t/* Check DOS attack or infinite loop of commands */");
      println("\t\tif");
      println("\t\t:: (" + device.gArr + ".element[deviceIndex].NumReceivedCommands >= MAX_COMMAND_REPITIONS) -> assert(0);");
      println("\t\t:: else -> skip;");
      println("\t\tfi");
      
//      println("\t}");//
      
      println();
  
//      println("\tif");//
//      println("\t:: ((Evt.EvtType != " + device.gArr 
//          + ".element[deviceIndex].events.element[latestEvtIndex" + indexValue + "].EvtType) || (" 
//          + device.gArr + ".element[deviceIndex].events.element[latestEvtIndex" + indexValue + "].id == 0)) -> {");//
//      println("\td_step {");//
        
      if(SpinUtil.isANumberCommand(deviceType))
      {
        println("\t\t/* Update current state */");
        println("\t\t" + device.gArr + ".element[deviceIndex]." + device.currentDevice + " = Evt.value;");
        println("\t\t" + device.gArr + ".element[deviceIndex]." + device.deviceState + ".value = Evt.value;");
        println("\t\t" + device.gArr + ".element[deviceIndex]." 
            + device.deviceState + ".name = " + device.deviceStateName + ";");
        println("\t\t" + device.gArr + ".element[deviceIndex]." + device.deviceState + ".date = STCurrentSystemTime*3600000;");
        println("\t\t" + device.gArr + ".element[deviceIndex]." + device.deviceState + ".isAlive = 1;");
        println();
        
        println("\t\t/* Store new state */");
        println("\t\tassign_STState_rec(" + device.gArr + ".element[deviceIndex].states.element[" 
            + device.gArr + ".element[deviceIndex].currEvtIndex], " + device.gArr + ".element[deviceIndex]." + device.deviceState + ");");
        println();
    
        println("\t\t/* Store the current event */");
        println("\t\tEvt.name = " + device.deviceStateName + ";");
        println("\t\tEvt.date = STCurrentSystemTime*3600000;");
        println("\t\tEvt.isAlive = 1;");
        println("\t\tassign_STEvent_rec(" + device.gArr + ".element[deviceIndex].events.element[" 
            + device.gArr + ".element[deviceIndex].currEvtIndex], Evt);");
        println();
    
        println("\t\t/* Increment the counter */");
        println("\t\t" + device.gArr + ".element[deviceIndex].currEvtIndex++;");
        println();
          
//        println("\t\t_index" + indexValue + " = 0;");
//        println("\t}");
        
        println("\t\t/* Broadcast the state change event to subscribers */");
        println("\t\tfor(_index" + indexValue + " : 0 .. " + device.gArr + ".element[deviceIndex].NumSubscribers-1) {");
        println("\t\t\t" + device.gArr + ".element[deviceIndex].BroadcastChans[_index" + indexValue + "] = true;");
        println("\t\t}");
      }
      else
      {
        println("\t\t/* Update current state if it is different from latest state */");
        println("\t\tif");
        println("\t\t:: (Evt.EvtType != " + device.gArr + ".element[deviceIndex].events.element[latestEvtIndex" + indexValue + "].EvtType) || ("
            + device.gArr + ".element[deviceIndex].events.element[latestEvtIndex" + indexValue + "].id == 0) -> {");
        println("\t\t\tgetIntValueFromMtype(Evt.EvtType, Handle" + deviceType + "Evt_state);");
        println("\t\t\t" + device.gArr + ".element[deviceIndex]." + device.currentDevice + " = Handle" + deviceType + "Evt_state;");
        println("\t\t\t" + device.gArr + ".element[deviceIndex]." + device.deviceState + ".value = Handle" + deviceType + "Evt_state;");
        println("\t\t\t" + device.gArr + ".element[deviceIndex]." 
            + device.deviceState + ".name = " + device.deviceStateName + ";");
        println("\t\t\t" + device.gArr + ".element[deviceIndex]." + device.deviceState + ".date = STCurrentSystemTime*3600000;");
        println("\t\t\t" + device.gArr + ".element[deviceIndex]." + device.deviceState + ".isAlive = 1;");
        println();
        
        println("\t\t\t/* Store new state */");
        println("\t\t\tassign_STState_rec(" + device.gArr + ".element[deviceIndex].states.element[" 
            + device.gArr + ".element[deviceIndex].currEvtIndex], " + device.gArr + ".element[deviceIndex]." + device.deviceState + ");");
        println();
    
        println("\t\t\t/* Store the current event */");
        println("\t\t\tEvt.name = " + device.deviceStateName + ";");
        println("\t\t\tEvt.value = Handle" + deviceType + "Evt_state;");
        println("\t\t\tEvt.date = STCurrentSystemTime*3600000;");
        println("\t\t\tEvt.isAlive = 1;");
        println("\t\t\tassign_STEvent_rec(" + device.gArr + ".element[deviceIndex].events.element[" 
            + device.gArr + ".element[deviceIndex].currEvtIndex], Evt);");
        println();
    
        println("\t\t\t/* Increment the counter */");
        println("\t\t\t" + device.gArr + ".element[deviceIndex].currEvtIndex++;");
        println();
          
//        println("\t\t\t_index" + indexValue + " = 0;");
//        println("\t}");
        
        println("\t\t\t/* Broadcast the state change event to subscribers */");
        println("\t\t\tfor(_index" + indexValue + " : 0 .. " + device.gArr + ".element[deviceIndex].NumSubscribers-1) {");
        println("\t\t\t\t" + device.gArr + ".element[deviceIndex].BroadcastChans[_index" + indexValue + "] = true;");
        println("\t\t\t}");
        
        println("\t\t}");
        println("\t\t:: else -> skip;");
        println("\t\tfi");
      }
      
//      println("\t\t}");//
//      println("\t\t:: else -> skip;");//
//      println("\t\tfi");//
      
      println("\t}");
      println("\tfi");
      println("}");
      
      indexValue++;
    }
    else
    {
      System.out.println("[SpinTrans][declareAnActuatorEvtManager] unsupported device type: " + deviceType);
    }
  }
	
	private void declareAThermoStatDevice()
	{
		println("hidden byte _index" + indexValue + ";");
		println("inline HandleSTThermostatEvt(deviceIndex, Evt)");
		println("{");
		println("\tbyte latestEvtIndex" + indexValue + ";");
		println("\tshort HandleSTThermostatEvt_state;");
		println();
		
		println("\td_step {");
		
		println("\t\t/* Validate the counter */");
		println("\t\tif");
		println("\t\t:: _g_STThermostatArr.element[deviceIndex].currEvtIndex >= MAX_STORED_EVENTS -> _g_STThermostatArr.element[deviceIndex].currEvtIndex = 0;");
		println("\t\t:: else -> skip;");
		println("\t\tfi");
		println();
		
//		println("\t\t/* Check conflict commands */");
//		println("\t\tgetPrevStoredEvtIndex(_g_STThermostatArr.element[deviceIndex].currEvtIndex, latestEvtIndex" + 
//				indexValue + ");");
//		println("\t\tif");
//		println("\t\t:: (_g_STThermostatArr.element[deviceIndex].events.element[latestEvtIndex" + indexValue + "].id == Evt.id) && "
//				+ "(_g_STThermostatArr.element[deviceIndex].events.element[latestEvtIndex" + indexValue + "].date == STCurrentSystemTime*3600000)"
//				+ "-> assert(Evt.EvtType == " 
//				+ "_g_STThermostatArr.element[deviceIndex].events.element[latestEvtIndex" + indexValue + "].EvtType);");
//		println("\t\t:: else -> skip;");
//		println("\t\tfi");
//		println();
		
		println("\t\t/* Increase number of received commands per event */");
		println("\t\tif");
		println("\t\t:: (Evt.physical == 0) && (_g_STThermostatArr.element[deviceIndex].events.element[latestEvtIndex" + 
				indexValue + "].id == 0) || ((_g_STThermostatArr.element[deviceIndex].events.element[latestEvtIndex" + 
				indexValue + "].id == Evt.id) &&"
				+ "(_g_STThermostatArr.element[deviceIndex].events.element[latestEvtIndex" + indexValue + "].date == STCurrentSystemTime*3600000)"
				+ ") -> _g_STThermostatArr.element[deviceIndex].NumReceivedCommands++;");
		println("\t\t:: else -> skip;");
		println("\t\tfi");
		println("\t\t/* Check DOS attack or infinite loop of commands */");
		println("\t\tif");
		println("\t\t:: (_g_STThermostatArr.element[deviceIndex].NumReceivedCommands >= MAX_COMMAND_REPITIONS) -> assert(0);");
		println("\t\t:: else -> skip;");
		println("\t\tfi");
		println();
		
		println("\t\t/* Update current state */");
		println("\t\tgetIntValueFromMtype(Evt.EvtType, HandleSTThermostatEvt_state);");
		println("\t\tif");
		/* ThermostatFanMode */
		println("\t\t:: (Evt.EvtType == fanAuto) || (Evt.EvtType == fanCirculate) || (Evt.EvtType == fanOn) -> {");
		println("\t\t\t_g_STThermostatArr.element[deviceIndex].currentThermostatFanMode = HandleSTThermostatEvt_state;");
		println("\t\t\t_g_STThermostatArr.element[deviceIndex].thermostatFanModeState.name = THERMOSTATFANMODE;");
		println("\t\t\t_g_STThermostatArr.element[deviceIndex].thermostatFanModeState.value = HandleSTThermostatEvt_state;");
		println("\t\t\t_g_STThermostatArr.element[deviceIndex].thermostatFanModeState.date = STCurrentSystemTime*3600000;");
		println("\t\t\t_g_STThermostatArr.element[deviceIndex].thermostatFanModeState.isAlive = 1;");
		println();
		println("\t\t\t/* Store new state */");
		println("\t\t\tassign_STState_rec(_g_STThermostatArr.element[deviceIndex].states.element[" 
				+ "_g_STThermostatArr.element[deviceIndex].currEvtIndex], _g_STThermostatArr.element[deviceIndex].thermostatFanModeState);");
		println();
		println("\t\t\tEvt.name = THERMOSTATFANMODE;");
		println("\t\t\tEvt.value = HandleSTThermostatEvt_state;");
		println("\t\t}");
		
		/* ThermostatMode */
		println("\t\t:: (Evt.EvtType == auto) || (Evt.EvtType == cool) || (Evt.EvtType == emergencyHeat) || (Evt.EvtType == heat) || (Evt.EvtType == off) -> {");
		println("\t\t\t_g_STThermostatArr.element[deviceIndex].currentThermostatMode = HandleSTThermostatEvt_state;");
		println("\t\t\t_g_STThermostatArr.element[deviceIndex].thermostatModeState.name = THERMOSTATMODE;");
		println("\t\t\t_g_STThermostatArr.element[deviceIndex].thermostatModeState.value = HandleSTThermostatEvt_state;");
		println("\t\t\t_g_STThermostatArr.element[deviceIndex].thermostatModeState.date = STCurrentSystemTime*3600000;");
		println("\t\t\t_g_STThermostatArr.element[deviceIndex].thermostatModeState.isAlive = 1;");
		println();
		println("\t\t\t/* Store new state */");
		println("\t\t\tassign_STState_rec(_g_STThermostatArr.element[deviceIndex].states.element[" 
				+ "_g_STThermostatArr.element[deviceIndex].currEvtIndex], _g_STThermostatArr.element[deviceIndex].thermostatModeState);");
		println();
		println("\t\t\tEvt.name = THERMOSTATMODE;");
		println("\t\t\tEvt.value = HandleSTThermostatEvt_state;");
		println("\t\t}");
		
		/* ThermostatHeatingSetpoint */
		println("\t\t:: (Evt.EvtType == setHeatingSetpoint) -> {");
		println("\t\t\t_g_STThermostatArr.element[deviceIndex].currentHeatingSetpoint = Evt.value;");
		println("\t\t\t_g_STThermostatArr.element[deviceIndex].heatingSetpointState.name = HEATINGSETPOINT;");
		println("\t\t\t_g_STThermostatArr.element[deviceIndex].heatingSetpointState.value = Evt.value;");
		println("\t\t\t_g_STThermostatArr.element[deviceIndex].heatingSetpointState.date = STCurrentSystemTime*3600000;");
		println("\t\t\t_g_STThermostatArr.element[deviceIndex].heatingSetpointState.isAlive = 1;");
		println();
		println("\t\t\t/* Store new state */");
		println("\t\t\tassign_STState_rec(_g_STThermostatArr.element[deviceIndex].states.element[" 
				+ "_g_STThermostatArr.element[deviceIndex].currEvtIndex], _g_STThermostatArr.element[deviceIndex].heatingSetpointState);");
		println();
		println("\t\t\tEvt.name = HEATINGSETPOINT;");
		println("\t\t}");
		
		/* ThermostatCoolingSetpoint */
		println("\t\t:: (Evt.EvtType == setCoolingSetpoint) -> {");
		println("\t\t\t_g_STThermostatArr.element[deviceIndex].currentCoolingSetpoint = Evt.value;");
		println("\t\t\t_g_STThermostatArr.element[deviceIndex].coolingSetpointState.name = COOLINGSETPOINT;");
		println("\t\t\t_g_STThermostatArr.element[deviceIndex].coolingSetpointState.value = Evt.value;");
		println("\t\t\t_g_STThermostatArr.element[deviceIndex].coolingSetpointState.date = STCurrentSystemTime*3600000;");
		println("\t\t\t_g_STThermostatArr.element[deviceIndex].coolingSetpointState.isAlive = 1;");
		println();
		println("\t\t\t/* Store new state */");
		println("\t\t\tassign_STState_rec(_g_STThermostatArr.element[deviceIndex].states.element[" 
				+ "_g_STThermostatArr.element[deviceIndex].currEvtIndex], _g_STThermostatArr.element[deviceIndex].coolingSetpointState);");
		println();
		println("\t\t\tEvt.name = COOLINGSETPOINT;");
		println("\t\t}");
		
		/* Temperature */
		println("\t\t:: (Evt.EvtType == temperature) -> {");
		println("\t\t\t_g_STThermostatArr.element[deviceIndex].currentTemperature = Evt.value;");
		println("\t\t\t_g_STThermostatArr.element[deviceIndex].temperatureState.name = TEMPERATURE;");
		println("\t\t\t_g_STThermostatArr.element[deviceIndex].temperatureState.value = Evt.value;");
		println("\t\t\t_g_STThermostatArr.element[deviceIndex].temperatureState.date = STCurrentSystemTime*3600000;");
		println("\t\t\t_g_STThermostatArr.element[deviceIndex].temperatureState.isAlive = 1;");
		println();
		println("\t\t\t/* Store new state */");
		println("\t\t\tassign_STState_rec(_g_STThermostatArr.element[deviceIndex].states.element[" 
				+ "_g_STThermostatArr.element[deviceIndex].currEvtIndex], _g_STThermostatArr.element[deviceIndex].temperatureState);");
		println();
		println("\t\t\tEvt.name = TEMPERATURE;");
		println("\t\t}");
		
		println("\t\t:: else -> skip;");
		println("\t\tfi");
		println();

		println("\t\t/* Store the current event */");
		println("\t\tEvt.date = STCurrentSystemTime*3600000;");
		println("\t\tEvt.isAlive = 1;");
		println("\t\tassign_STEvent_rec(_g_STThermostatArr.element[deviceIndex].events.element[" 
				+ "_g_STThermostatArr.element[deviceIndex].currEvtIndex], Evt);");
		println();

		println("\t\t/* Increment the counter */");
		println("\t\t_g_STThermostatArr.element[deviceIndex].currEvtIndex++;");
		println();
		
		println("\t\t/* Broadcast the state change event to subscribers */");
		println("\t\tif");
		println("\t\t:: (numTranFailure < MAX_TRAN_FAILURES) -> {");
		println("\t\t\tif");
		println("\t\t\t:: numTranFailure++;");
		println("\t\t\t:: {");
		println("\t\t\t\tfor(_index" + indexValue + " : 0 .. _g_STThermostatArr.element[deviceIndex].NumSubscribers-1) {");
		println("\t\t\t\t\t_g_STThermostatArr.element[deviceIndex].BroadcastChans[_index" + indexValue + "] = true;");
		println("\t\t\t\t}");
		println("\t\t\t}");
		println("\t\t\tfi");
		println("\t\t}");
		println("\t\t:: else -> {");
		println("\t\t\tfor(_index" + indexValue + " : 0 .. _g_STThermostatArr.element[deviceIndex].NumSubscribers-1) {");
		println("\t\t\t\t_g_STThermostatArr.element[deviceIndex].BroadcastChans[_index" + indexValue + "] = true;");
		println("\t\t\t}");
		println("\t\t}");
		println("\t\tfi");
		
		println("\t}");
		println("}");
		
		indexValue++;
	}
	
	private void declareSTColorCtrlDevice()
	{
		println("hidden byte _index" + indexValue + ";");
		println("inline HandleSTColorCtrlEvt(deviceIndex, Evt)");
		println("{");
		println("\tbyte latestEvtIndex" + indexValue + ";");
		println("\tshort HandleSTColorCtrlEvt_state;");
		println();
		
		println("\td_step {");
		
		println("\t\t/* Validate the counter */");
		println("\t\tif");
		println("\t\t:: _g_STColorCtrlArr.element[deviceIndex].currEvtIndex >= MAX_STORED_EVENTS -> _g_STColorCtrlArr.element[deviceIndex].currEvtIndex = 0;");
		println("\t\t:: else -> skip;");
		println("\t\tfi");
		println();
		
//		println("\t\t/* Check conflict commands */");
//		println("\t\tgetPrevStoredEvtIndex(_g_STColorCtrlArr.element[deviceIndex].currEvtIndex, latestEvtIndex" + 
//				indexValue + ");");
//		println("\t\tif");
//		println("\t\t:: (_g_STColorCtrlArr.element[deviceIndex].events.element[latestEvtIndex" + indexValue + "].id == Evt.id) && "
//				+ "(_g_STColorCtrlArr.element[deviceIndex].events.element[latestEvtIndex" + indexValue + "].date == STCurrentSystemTime*3600000)"
//				+ " -> assert(Evt.EvtType == " 
//				+ "_g_STColorCtrlArr.element[deviceIndex].events.element[latestEvtIndex" + indexValue + "].EvtType);");
//		println("\t\t:: else -> skip;");
//		println("\t\tfi");
//		println();
		
		println("\t\t/* Increase number of received commands per event */");
		println("\t\tif");
		println("\t\t:: (Evt.physical == 0) && (_g_STColorCtrlArr.element[deviceIndex].events.element[latestEvtIndex" + 
				indexValue + "].id == 0) || ((_g_STColorCtrlArr.element[deviceIndex].events.element[latestEvtIndex" + 
				indexValue + "].id == Evt.id) && " 
				+ "(_g_STColorCtrlArr.element[deviceIndex].events.element[latestEvtIndex" + indexValue + "].date == STCurrentSystemTime*3600000)" 
				+ ") -> _g_STColorCtrlArr.element[deviceIndex].NumReceivedCommands++;");
		println("\t\t:: else -> skip;");
		println("\t\tfi");
		println("\t\t/* Check DOS attack or infinite loop of commands */");
		println("\t\tif");
		println("\t\t:: (_g_STColorCtrlArr.element[deviceIndex].NumReceivedCommands >= MAX_COMMAND_REPITIONS) -> assert(0);");
		println("\t\t:: else -> skip;");
		println("\t\tfi");
		println();
		
		println("\t\t/* Update current state */");
		println("\t\tgetIntValueFromMtype(Evt.EvtType, HandleSTColorCtrlEvt_state);");
		println("\t\tif");
		/* Switch */
		println("\t\t:: (Evt.EvtType == on) || (Evt.EvtType == off) -> {");
		println("\t\t\t_g_STColorCtrlArr.element[deviceIndex].currentSwitch = HandleSTColorCtrlEvt_state;");
		println("\t\t\t_g_STColorCtrlArr.element[deviceIndex].switchState.name = SWITCH;");
		println("\t\t\t_g_STColorCtrlArr.element[deviceIndex].switchState.value = HandleSTColorCtrlEvt_state;");
		println("\t\t\t_g_STColorCtrlArr.element[deviceIndex].switchState.date = STCurrentSystemTime*3600000;");
		println("\t\t\t_g_STColorCtrlArr.element[deviceIndex].switchState.isAlive = 1;");
		println();
		println("\t\t\t/* Store new state */");
		println("\t\t\tassign_STState_rec(_g_STColorCtrlArr.element[deviceIndex].states.element[" 
				+ "_g_STColorCtrlArr.element[deviceIndex].currEvtIndex], _g_STColorCtrlArr.element[deviceIndex].switchState);");
		println();
		println("\t\t\tEvt.name = SWITCH;");
		println("\t\t\tEvt.value = HandleSTColorCtrlEvt_state;");
		println("\t\t}");
		
		/* Level */
		println("\t\t:: (Evt.EvtType == setLevel) -> {");
		println("\t\t\t_g_STColorCtrlArr.element[deviceIndex].currentLevel = Evt.value;");
		println("\t\t\t_g_STColorCtrlArr.element[deviceIndex].levelState.name = LEVEL;");
		println("\t\t\t_g_STColorCtrlArr.element[deviceIndex].levelState.value = Evt.value;");
		println("\t\t\t_g_STColorCtrlArr.element[deviceIndex].levelState.date = STCurrentSystemTime*3600000;");
		println("\t\t\t_g_STColorCtrlArr.element[deviceIndex].levelState.isAlive = 1;");
		println();
		println("\t\t\t/* Store new state */");
		println("\t\t\tassign_STState_rec(_g_STColorCtrlArr.element[deviceIndex].states.element[" 
				+ "_g_STColorCtrlArr.element[deviceIndex].currEvtIndex], _g_STColorCtrlArr.element[deviceIndex].levelState);");
		println();
		println("\t\t\tEvt.name = LEVEL;");
		println("\t\t}");
		
		/* Hue */
		println("\t\t:: (Evt.EvtType == setHue) -> {");
		println("\t\t\t_g_STColorCtrlArr.element[deviceIndex].currentHue = Evt.value;");
		println("\t\t\t_g_STColorCtrlArr.element[deviceIndex].hueState.name = HUE;");
		println("\t\t\t_g_STColorCtrlArr.element[deviceIndex].hueState.value = Evt.value;");
		println("\t\t\t_g_STColorCtrlArr.element[deviceIndex].hueState.date = STCurrentSystemTime*3600000;");
		println("\t\t\t_g_STColorCtrlArr.element[deviceIndex].hueState.isAlive = 1;");
		println();
		println("\t\t\t/* Store new state */");
		println("\t\t\tassign_STState_rec(_g_STColorCtrlArr.element[deviceIndex].states.element[" 
				+ "_g_STColorCtrlArr.element[deviceIndex].currEvtIndex], _g_STColorCtrlArr.element[deviceIndex].hueState);");
		println();
		println("\t\t\tEvt.name = HUE;");
		println("\t\t}");
		
		/* Saturation */
		println("\t\t:: (Evt.EvtType == setSaturation) -> {");
		println("\t\t\t_g_STColorCtrlArr.element[deviceIndex].currentSaturation = Evt.value;");
		println("\t\t\t_g_STColorCtrlArr.element[deviceIndex].saturationState.name = SATURATION;");
		println("\t\t\t_g_STColorCtrlArr.element[deviceIndex].saturationState.value = Evt.value;");
		println("\t\t\t_g_STColorCtrlArr.element[deviceIndex].saturationState.date = STCurrentSystemTime*3600000;");
		println("\t\t\t_g_STColorCtrlArr.element[deviceIndex].saturationState.isAlive = 1;");
		println();
		println("\t\t\t/* Store new state */");
		println("\t\t\tassign_STState_rec(_g_STColorCtrlArr.element[deviceIndex].states.element[" 
				+ "_g_STColorCtrlArr.element[deviceIndex].currEvtIndex], _g_STColorCtrlArr.element[deviceIndex].saturationState);");
		println();
		println("\t\t\tEvt.name = SATURATION;");
		println("\t\t}");
		
		/* Color */
		println("\t\t:: (Evt.EvtType == setColor) -> {");
		println("\t\t\t_g_STColorCtrlArr.element[deviceIndex].currentColor = Evt.value;");
		println("\t\t\t_g_STColorCtrlArr.element[deviceIndex].currentSwitch = Evt.value >> 24;");
		println("\t\t\t_g_STColorCtrlArr.element[deviceIndex].currentLevel = Evt.value >> 16;");
		println("\t\t\t_g_STColorCtrlArr.element[deviceIndex].currentHue = Evt.value >> 8;");
		println("\t\t\t_g_STColorCtrlArr.element[deviceIndex].currentSaturation = Evt.value & 0x000F;");
		println("\t\t\t_g_STColorCtrlArr.element[deviceIndex].colorState.name = COLOR;");
		println("\t\t\t_g_STColorCtrlArr.element[deviceIndex].colorState.value = Evt.value;");
		println("\t\t\t_g_STColorCtrlArr.element[deviceIndex].colorState.date = STCurrentSystemTime*3600000;");
		println("\t\t\t_g_STColorCtrlArr.element[deviceIndex].colorState.isAlive = 1;");
		println();
		println("\t\t\t/* Store new state */");
		println("\t\t\tassign_STState_rec(_g_STColorCtrlArr.element[deviceIndex].states.element[" 
				+ "_g_STColorCtrlArr.element[deviceIndex].currEvtIndex], _g_STColorCtrlArr.element[deviceIndex].colorState);");
		println();
		println("\t\t\tEvt.name = COLOR;");
		println("\t\t}");
		
		println("\t\t:: else -> skip;");
		println("\t\tfi");
		println();

		println("\t\t/* Store the current event */");
		println("\t\tEvt.date = STCurrentSystemTime*3600000;");
		println("\t\tEvt.isAlive = 1;");
		println("\t\tassign_STEvent_rec(_g_STColorCtrlArr.element[deviceIndex].events.element[" 
				+ "_g_STColorCtrlArr.element[deviceIndex].currEvtIndex], Evt);");
		println();

		println("\t\t/* Increment the counter */");
		println("\t\t_g_STColorCtrlArr.element[deviceIndex].currEvtIndex++;");
		println();
		
		println("\t\t/* Broadcast the state change event to subscribers */");
		println("\t\tif");
		println("\t\t:: (numTranFailure < MAX_TRAN_FAILURES) -> {");
		println("\t\t\tif");
		println("\t\t\t:: numTranFailure++;");
		println("\t\t\t:: {");
		println("\t\t\t\tfor(_index" + indexValue + " : 0 .. _g_STColorCtrlArr.element[deviceIndex].NumSubscribers-1) {");
		println("\t\t\t\t\t_g_STColorCtrlArr.element[deviceIndex].BroadcastChans[_index" + indexValue + "] = true;");
		println("\t\t\t\t}");
		println("\t\t\t}");
		println("\t\t\tfi");
		println("\t\t}");
		println("\t\t:: else -> {");
		println("\t\t\tfor(_index" + indexValue + " : 0 .. _g_STColorCtrlArr.element[deviceIndex].NumSubscribers-1) {");
		println("\t\t\t\t_g_STColorCtrlArr.element[deviceIndex].BroadcastChans[_index" + indexValue + "] = true;");
		println("\t\t\t}");
		println("\t\t}");
		println("\t\tfi");
		
		println("\t}");
		println("}");
		
		indexValue++;
	}
	
	private void declareActuatorDeviceEvtHandlers()
	{
		println("/*************** Start of actuator device event handler ******/");
		
		for(String deviceType : GConfigInfoManager.getDeviceTypeListFromConfigInfo())
		{
			if(deviceType.equals("STThermostat"))
			{
				declareAThermoStatDevice();
			}
			else if(deviceType.equals("STColorCtrl"))
			{
				declareSTColorCtrlDevice();
			}
			else if(!SpinUtil.isASensorDevice(deviceType))
			{
			  if (isDeviceFailureEnabled) {
			    declareAnActuatorEvtHandlerWithDeviceFailure(deviceType);
			  } else {
			    declareAnActuatorEvtHandler(deviceType);
			  }
			}
		}

		println("/*************** End of actuator device event handler ********/");
		println();
	}
	
	private void declareASensorDeviceEvtHandler(String deviceType)
	{
		DeviceNameInfo device = SpinUtil.getDeviceNameInfo(deviceType);
		
		if(device != null)
		{
			println("hidden byte _index" + indexValue + ";");
			println("inline Handle" + deviceType + "Evt(deviceIndex, Evt)");
			println("{");
			println("\td_step {");
			
			if(deviceType.equals("STCarMoDetector"))
			{
				println("\t\t/* Check if smart app trigger a sensed event */");
				println("\t\tif");
				println("\t\t:: Evt.EvtType == COSmoke -> assert(0);");
				println("\t\t:: else -> skip;");
				println("\t\tfi");
				println();
			}
			
			println("\t\t/* Validate the currEvtIndex */");
			println("\t\tif");
			println("\t\t:: " + device.gArr + ".element[deviceIndex].currEvtIndex >= MAX_STORED_EVENTS -> " 
					+ device.gArr + ".element[deviceIndex].currEvtIndex = 0;");
			println("\t\t:: else -> skip;");
			println("\t\tfi");
			println();
			println("\t\t/* Store the generated event */");
			println("\t\tassign_STEvent_rec(" + device.gArr
					+ ".element[deviceIndex].events.element[" + device.gArr 
					+ ".element[deviceIndex].currEvtIndex], Evt);");
			println();
			println("\t\t/* Update current state */");
			println("\t\t" + device.gArr + ".element[deviceIndex]." + device.currentDevice + " = Evt.value;");
			println("\t\t" + device.gArr + ".element[deviceIndex]." + device.deviceState + ".name = Evt.name;");
			println("\t\t" + device.gArr + ".element[deviceIndex]." + device.deviceState + ".value = Evt.value;");
			println("\t\t" + device.gArr + ".element[deviceIndex]." + device.deviceState + ".date = Evt.date;");
			println("\t\t" + device.gArr + ".element[deviceIndex]." + device.deviceState + ".isAlive = 1;");
			println();
			
			println("\t\t/* Store new state */");
			println("\t\tassign_STState_rec(" + device.gArr + ".element[deviceIndex].states.element[" 
					+ device.gArr + ".element[deviceIndex].currEvtIndex], " 
					+ device.gArr + ".element[deviceIndex]." + device.deviceState + ");");
			println();
			println("\t\t/* Increment the counter */");
			println("\t\t" + device.gArr + ".element[deviceIndex].currEvtIndex++;");
			println();
			
			println("\t\t/* Broadcast the generated event to subscribers */");
			if(deviceType.equals("STPresSensor") && !GPotentialRiskScreener.isLocationOutputEvtPresent())
      {
        println("\t\tChangeLocationMode(Evt);");
      }
			println("\t\tfor(_index" + indexValue + " : 0 .. " + device.gArr + ".element[deviceIndex].NumSubscribers-1) {"); 
      println("\t\t\t" + device.gArr + ".element[deviceIndex].BroadcastChans[_index" + indexValue + "] = true;");
      println("\t\t}");
			println("\t}");
			println("}");
			indexValue++;
		}
		else
		{
			System.out.println("[SpinTrans][declareASensorDeviceEvtHandler] unsupported device type: " + deviceType);
		}
	}
	
	private void declareASensorDeviceEvtHandlerWithDeviceFailure(String deviceType)
  {
    DeviceNameInfo device = SpinUtil.getDeviceNameInfo(deviceType);
    
    if(device != null)
    {
      println("hidden byte _index" + indexValue + ";");
      println("inline Handle" + deviceType + "Evt(deviceIndex, Evt)");
      println("{");
//      println("\tatomic {");
      println("\td_step {");
      
      if(deviceType.equals("STCarMoDetector"))
      {
        println("\t\t/* Check if smart app trigger a sensed event */");
        println("\t\tif");
        println("\t\t:: Evt.EvtType == COSmoke -> assert(0);");
        println("\t\t:: else -> skip;");
        println("\t\tfi");
        println();
      }
      
      println("\t\t/* Validate the currEvtIndex */");
      println("\t\tif");
      println("\t\t:: " + device.gArr + ".element[deviceIndex].currEvtIndex >= MAX_STORED_EVENTS -> " 
          + device.gArr + ".element[deviceIndex].currEvtIndex = 0;");
      println("\t\t:: else -> skip;");
      println("\t\tfi");
      println();
      println("\t\t/* Store the generated event */");
      println("\t\tassign_STEvent_rec(" + device.gArr
          + ".element[deviceIndex].events.element[" + device.gArr 
          + ".element[deviceIndex].currEvtIndex], Evt);");
      println();
      println("\t\t/* Update current state */");
      println("\t\t" + device.gArr + ".element[deviceIndex]." + device.currentDevice + " = Evt.value;");
      println("\t\t" + device.gArr + ".element[deviceIndex]." + device.deviceState + ".name = Evt.name;");
      println("\t\t" + device.gArr + ".element[deviceIndex]." + device.deviceState + ".value = Evt.value;");
      println("\t\t" + device.gArr + ".element[deviceIndex]." + device.deviceState + ".date = Evt.date;");
      println("\t\t" + device.gArr + ".element[deviceIndex]." + device.deviceState + ".isAlive = 1;");
      println();
      
      println("\t\t/* Store new state */");
      println("\t\tassign_STState_rec(" + device.gArr + ".element[deviceIndex].states.element[" 
          + device.gArr + ".element[deviceIndex].currEvtIndex], " 
          + device.gArr + ".element[deviceIndex]." + device.deviceState + ");");
      println();
      println("\t\t/* Increment the counter */");
      println("\t\t" + device.gArr + ".element[deviceIndex].currEvtIndex++;");
      println();
  
//      println("\t\t_index" + indexValue + " = 0;");
      println("\t}");
      
      println("\t/* Broadcast the generated event to subscribers */");
      if(deviceType.equals("STPresSensor") && !GPotentialRiskScreener.isLocationOutputEvtPresent())
      {
        println("\tChangeLocationMode(Evt);");
      }
      println("\tif");
      println("\t:: " + device.gArr + ".element[deviceIndex].isOnline = 0;");
      println("\t:: {");
      println("\t\t" + device.gArr + ".element[deviceIndex].isOnline = 1;");
      println("\t\tfor(_index" + indexValue + " : 0 .. " + device.gArr + ".element[deviceIndex].NumSubscribers-1) {"); 
      println("\t\t\t" + device.gArr + ".element[deviceIndex].BroadcastChans[_index" + indexValue + "] = true;");
      println("\t\t}");
      println("\t}");
      println("\tfi");
//      println("\t}");
      println("}");
      
      indexValue++;
    }
    else
    {
      System.out.println("[SpinTrans][declareASensorDeviceEvtHandler] unsupported device type: " + deviceType);
    }
  }

	private void declareSensorDeviceEvtHandlers()
	{
		println("/*************** Start of sensor device event handler ********/");
		
		for(String deviceType : GConfigInfoManager.getDeviceTypeListFromConfigInfo())
		{
			if(SpinUtil.isASensorDevice(deviceType) && !deviceType.equals("STThermostat"))
			{
			  if (isDeviceFailureEnabled) {
			    declareASensorDeviceEvtHandlerWithDeviceFailure(deviceType);
			  } else {
			    declareASensorDeviceEvtHandler(deviceType);
			  }
			}
		}
		
		println("/*************** End of sensor device event handler **********/");
		println();
	}
	/* [Thomas, May 16, 2017]
	 * */
	private void declareSmartThings()
	{
		boolean isEvtGenerated;
		String latestEvtIndex = "latestEvtIndex" + indexValue;
		Set<String> smartApps = GPotentialRiskScreener.getCurrentSmartAppNames();
		List<GSpecialConfiInfo> specialConfiInfoList = GConfigInfoManager.getSpecialConfiInfoList();
		
		println("/*************** Start of events generator *******************/");
//		println("hidden byte EventsGenerator_NumEvents;");
		
		
		/* Declare counter variable for each type of sensor device */
		for(String deviceType : GConfigInfoManager.getDeviceTypeListFromConfigInfo())
		{
			if(SpinUtil.isASensorDevice(deviceType))
			{
				println("hidden byte " + deviceType + "Index;");
			}
		}
		println("bool eventProcessed, sunriseDone, sunsetDone;");
		
		println("proctype SmartThings()");
		println("{");
		println("\tbyte powerMeter;");
		println("\tbyte evtId;");
		println("\tSTEvent_rec generatedEvent;");
		println("\tbyte " + latestEvtIndex + ";");
		println("\tbool allEvtsHandled, systemTimeIncrementNeeded;");
		/* Declare boolean variables for special config info */
		for(GSpecialConfiInfo info : specialConfiInfoList)
		{
			println("\tbool " + info.deviceName + "_Done;");
		}
		println();
		
		println("\td_step {");
		println("\t\tSTCurrentSystemTime = 1;");
		println("\t\tgeneratedEvent.id = evtId;");
		println("\t\tgeneratedEvent.date = STCurrentSystemTime*3600000;");
		println("\t\tgeneratedEvent.isAlive = 1;");
		println("\t\tgeneratedEvent.physical = 1;");
		println("\t\tsunriseDone = false;");
		println("\t\tsunsetDone = false;");
		/* Initialize boolean variables of special config info */
		for(GSpecialConfiInfo info : specialConfiInfoList)
		{
			println("\t\t" + info.deviceName + "_Done = false;");
		}
		println("\t}");
		
		/* Perform behaviors triggered at installed time by smart apps */
//		if(GConfigInfoManager.isStateMapUsed())
		{
			boolean anyInstalledMethNeeded = false;
			
			for(String smartAppName : smartApps)
			{
				if(GConfigInfoManager.getInstalledMethNeeded(smartAppName))
				{
					anyInstalledMethNeeded = true;
					println("\t" + smartAppName + "_installedEvtHandler(generatedEvent);");
				}
			}
			if(anyInstalledMethNeeded)
			{
				println("\tResetDeviceDefaultState();");
				println();
			}
		}
		
		println("\tfor(evtId : 2 .. MAX_NUM_EVENTS) {");
				
		println("\t\td_step {");
		println("\t\t\tResetGlobalVariables();");
//		println("\t\t\t/* Validate system time */");
//		println("\t\t\tif");
//		println("\t\t\t:: STCurrentSystemTime > MAX_SYSTEM_TIME -> STCurrentSystemTime = 1;");
//		println("\t\t\t:: else -> STCurrentSystemTime++;");
//		println("\t\t\tfi");
		println("\t\t\teventProcessed = false;");
		println("\t\t\tSTCurrentSystemTime = evtId;");
		println("\t\t\tgeneratedEvent.date = STCurrentSystemTime*3600000;");
		println("\t\t\tgeneratedEvent.id = evtId;");
		println("\t\t\tgeneratedEvent.isAlive = 1;");
		println("\t\t\tgeneratedEvent.physical = 1;");
//		println("\t\t\tevtId++;");
		println("\t\t}");
		println();
		
		if(!GConfigInfoManager.isSTTempMeasPresent())
		{
			println("\t\t/* Randomly select system temperature */");
			println("\t\tif");
			println("\t\t:: STCurrentTemperature = 1;");
			println("\t\t:: STCurrentTemperature = 2;");
			println("\t\t:: STCurrentTemperature = 3;");
			println("\t\tfi");
			println();
		}
		
		println("\t\t/* Generate location events */");
		println("\t\tif");
		println("\t\t:: (STCurrentSystemTime >= location.sunriseSunset.sunrise) && (sunriseDone == false) -> {");
		println("\t\t\tsunriseDone = true;");
		println("\t\t\tgeneratedEvent.EvtType = sunriseTime;");
		println("\t\t\tgeneratedEvent.value = STCurrentSystemTime;");
		println("\t\t\tgeneratedEvent.name = SUNRISETIME;");
		println("\t\t\tHandleLocationEvt(generatedEvent);");
		println("\t\t\tskip;");
		println("\t\t\tgoto loc_StartOfEvtHandlers;");
		println("\t\t}");
		println("\t\t:: (STCurrentSystemTime >= location.sunriseSunset.sunset) && (sunsetDone == false) -> {");
		println("\t\t\tsunsetDone = true;");
		println("\t\t\tgeneratedEvent.EvtType = sunsetTime;");
		println("\t\t\tgeneratedEvent.value = STCurrentSystemTime;");
		println("\t\t\tgeneratedEvent.name = SUNSETTIME;");
		println("\t\t\tHandleLocationEvt(generatedEvent);");
		println("\t\t\tskip;");
		println("\t\t\tgoto loc_StartOfEvtHandlers;");
		println("\t\t}");
		println("\t\t:: else -> skip;");
		println("\t\tfi");
		println();
		
		/* Generate one special event for each configured actuator device */
		for(GSpecialConfiInfo info : specialConfiInfoList)
		{
			if(info.deviceType.equals("STSwitch"))
			{
				println("\t\t/* Generate one special event for a switch */");
				println("\t\tif");
				println("\t\t:: " + info.deviceName + "_Done == false -> {");
				println("\t\t\td_step {");
				println("\t\t\t\t" + info.deviceName + "_Done = true;");
				if(info.command.equals("off"))
				{
					println("\t\t\t\tgeneratedEvent.EvtType = off;");
					println("\t\t\t\tgeneratedEvent.value = OFF;");
				}
				else
				{
					println("\t\t\t\tgeneratedEvent.EvtType = on;");
					println("\t\t\t\tgeneratedEvent.value = ON;");
				}
				println("\t\t\t\tgeneratedEvent.name = SWITCH;");
				println("\t\t\t\tgeneratedEvent.date = STCurrentSystemTime*3600000;");
				println("\t\t\t}");
				
				println("\t\t\tHandleSTSwitchEvt(" + info.deviceName + ", generatedEvent);");
				println("\t\t\tskip;");
				println("\t\t\tgoto loc_StartOfEvtHandlers;");
				println("\t\t}");
				println("\t\t:: !(" + info.deviceName + "_Done == false) -> skip;");
				println("\t\tfi");
			}
			else if(info.deviceType.equals("STLock"))
			{
				println("\t\t/* Generate one special event for a lock */");
				println("\t\tif");
				println("\t\t:: " + info.deviceName + "_Done == false -> {");
				println("\t\t\td_step {");
				println("\t\t\t\t" + info.deviceName + "_Done = true;");
				if(info.command.equals("lock"))
				{
					println("\t\t\t\tgeneratedEvent.EvtType = lock;");
					println("\t\t\t\tgeneratedEvent.value = LOCKED;");
				}
				else
				{
					println("\t\t\t\tgeneratedEvent.EvtType = unlock;");
					println("\t\t\t\tgeneratedEvent.value = UNLOCKED;");
				}
				println("\t\t\t\tgeneratedEvent.name = LOCK;");
				println("\t\t\t\tgeneratedEvent.date = STCurrentSystemTime*3600000;");
				println("\t\t\t}");
				
				println("\t\t\tHandleSTLockEvt(" + info.deviceName + ", generatedEvent);");
				println("\t\t\tskip;");
				println("\t\t\tgoto loc_StartOfEvtHandlers;");
				println("\t\t}");
				println("\t\t:: !(" + info.deviceName + "_Done == false) -> skip;");
				println("\t\tfi");
			}
		}
		
		println("\t\t/* Randomly generate an event */");
		println("\t\tif");
		isEvtGenerated = false;
		/* Generate location event */
//		{
//			println("\t\t/* Generate location events */");
//			println("\t\t:: {");
//			println("\t\t\tif");
//			println("\t\t\t:: generatedEvent.EvtType = Home;");
//			println("\t\t\t:: generatedEvent.EvtType = Away;");
//			println("\t\t\t:: generatedEvent.EvtType = Night;");
//			println("\t\t\tfi");
//			println("\t\t\tHandleLocationEvt(generatedEvent);");
//			println("\t\t}");
//			isEvtGenerated = true;
//		}
		
		for(String deviceType : GConfigInfoManager.getDeviceTypeListFromConfigInfo())
		{
			if(deviceType.equals("STMotionSensor"))
			{
				println("\t\t/* Generate events for motion sensors */");
				println("\t\t:: {");
				println("\t\t\td_step {");
				println("\t\t\t\t" + deviceType + "Index++;");
				println("\t\t\t\t/* Validate counter */");
				println("\t\t\t\tif");
				println("\t\t\t\t:: " + deviceType + "Index >= _g_" + deviceType + "Arr.length -> " + deviceType + "Index = 0;");
				println("\t\t\t\t:: else -> skip;");
				println("\t\t\t\tfi");
				println("\t\t\t}");
				println();
				
				println("\t\t\tif");
				println("\t\t\t:: generatedEvent.EvtType = inactive; generatedEvent.value = INACTIVE;");
				println("\t\t\t:: generatedEvent.EvtType = _active; generatedEvent.value = ACTIVE;");
				if(GConfigInfoManager.isEvtTypeSubsribed("STMotionSensor", "battery"))
				{
					println("\t\t\t:: generatedEvent.EvtType = battery; generatedEvent.value = BATTERY;");
				}
				if(GConfigInfoManager.isEvtTypeSubsribed("STMotionSensor", "powered"))
				{
					println("\t\t\t:: generatedEvent.EvtType = powered; generatedEvent.value = POWERED;");
				}
				println("\t\t\tfi");
				println("\t\t\tgeneratedEvent.name = MOTION;");
				println("\t\t\tHandle" + deviceType + "Evt(" + deviceType + "Index, generatedEvent);");
				println("\t\t}");
				isEvtGenerated = true;
			}
			else if(deviceType.equals("STTempMeas"))
			{
				println("\t\t/* Generate events for temperature measurement */");
				println("\t\t:: {");
				println("\t\t\t/* Randomly select system temperature */");
				println("\t\t\tif");
				println("\t\t\t:: STCurrentTemperature = 1;");
				println("\t\t\t:: STCurrentTemperature = 2;");
				println("\t\t\t:: STCurrentTemperature = 3;");
				println("\t\t\tfi");
				
				println("\t\t\td_step {");
				println("\t\t\t\t" + deviceType + "Index++;");
				println("\t\t\t\t/* Validate counter */");
				println("\t\t\t\tif");
				println("\t\t\t\t:: " + deviceType + "Index >= _g_" + deviceType + "Arr.length -> " + deviceType + "Index = 0;");
				println("\t\t\t\t:: else -> skip;");
				println("\t\t\t\tfi");
				println("\t\t\t\tgeneratedEvent.EvtType = temperature;");
				println("\t\t\t\tgeneratedEvent.value = STCurrentTemperature;");
				println("\t\t\t\tgeneratedEvent.name = TEMPERATURE;");
				println("\t\t\t}");
				
				println("\t\t\tHandle" + deviceType + "Evt(" + deviceType + "Index, generatedEvent);");
				println("\t\t}");
				isEvtGenerated = true;
			}
			else if(deviceType.equals("STPresSensor"))
			{
				println("\t\t/* Generate events for presence sensor */");
				println("\t\t:: {");
				println("\t\t\td_step {");
				println("\t\t\t\t" + deviceType + "Index++;");
				println("\t\t\t\t/* Validate counter */");
				println("\t\t\t\tif");
				println("\t\t\t\t:: " + deviceType + "Index >= _g_" + deviceType + "Arr.length -> " + deviceType + "Index = 0;");
				println("\t\t\t\t:: else -> skip;");
				println("\t\t\t\tfi");
				println("\t\t\t}");
				println();
				
				println("\t\t\tif");
				println("\t\t\t:: generatedEvent.EvtType = notpresent; generatedEvent.value = NOT_PRESENT;");
				println("\t\t\t:: generatedEvent.EvtType = present; generatedEvent.value = PRESENT;");
				println("\t\t\tfi");
				println("\t\t\tgeneratedEvent.name = PRESENCE;");
				println("\t\t\tHandle" + deviceType + "Evt(" + deviceType + "Index, generatedEvent);");
				println("\t\t}");
				isEvtGenerated = true;
			}
			else if(deviceType.equals("STContactSensor"))
			{
				println("\t\t/* Generate events for presence sensor */");
				println("\t\t:: {");
				println("\t\t\td_step {");
				println("\t\t\t\t" + deviceType + "Index++;");
				println("\t\t\t\t/* Validate counter */");
				println("\t\t\t\tif");
				println("\t\t\t\t:: " + deviceType + "Index >= _g_" + deviceType + "Arr.length -> " + deviceType + "Index = 0;");
				println("\t\t\t\t:: else -> skip;");
				println("\t\t\t\tfi");
				println("\t\t\t}");
				println();
				
				println("\t\t\tif");
				println("\t\t\t:: generatedEvent.EvtType = close; generatedEvent.value = CLOSED;");
				println("\t\t\t:: generatedEvent.EvtType = open; generatedEvent.value = OPEN;");
				println("\t\t\tfi");
				println("\t\t\tgeneratedEvent.name = CONTACT;");
				println("\t\t\tHandle" + deviceType + "Evt(" + deviceType + "Index, generatedEvent);");
				println("\t\t}");
				isEvtGenerated = true;
			}
			else if(deviceType.equals("STPowerMeter"))
			{
				println("\t\t/* Generate events for power meter */");
				println("\t\t:: {");
				println("\t\t\td_step {");
				
//				println("\t\t\t\t/* Validate power meter */");
//				println("\t\t\t\tif");
//				println("\t\t\t\t:: powerMeter > MAX_POWER_METER -> powerMeter = 1;");
//				println("\t\t\t\t:: else -> powerMeter++;");
//				println("\t\t\t\tfi");
//				println();
				
				println("\t\t\t\tgeneratedEvent.EvtType = power_meter;");
//				println("\t\t\t\tgeneratedEvent.value = powerMeter;");
				println("\t\t\t\tgeneratedEvent.name = POWER;");
				
				println("\t\t\t}");
				
				println("\t\t\tif");
				println("\t\t\t:: generatedEvent.value = 1;");
				println("\t\t\t:: generatedEvent.value = 2;");
				println("\t\t\t:: generatedEvent.value = 3;");
				println("\t\t\tfi");
				println("\t\t\tHandle" + deviceType + "Evt(0, generatedEvent);");
				println("\t\t}");
				isEvtGenerated = true;
			}
			else if(deviceType.equals("STThermostat"))
			{
				println("\t\t/* Generate temperature events for a thermostat */");
				println("\t\t:: {");
				println("\t\t\td_step {");
				println("\t\t\t\t" + deviceType + "Index++;");
				println("\t\t\t\t/* Validate counter */");
				println("\t\t\t\tif");
				println("\t\t\t\t:: " + deviceType + "Index >= _g_" + deviceType + "Arr.length -> " + deviceType + "Index = 0;");
				println("\t\t\t\t:: else -> skip;");
				println("\t\t\t\tfi");
				println("\t\t\t\tgeneratedEvent.EvtType = temperature;");
				println("\t\t\t\tgeneratedEvent.value = STCurrentTemperature;");
				println("\t\t\t\tgeneratedEvent.name = TEMPERATURE;");
				println("\t\t\t}");
				
				println("\t\t\tHandle" + deviceType + "Evt(" + deviceType + "Index, generatedEvent);");
				println("\t\t}");
				isEvtGenerated = true;
			}
			else if(deviceType.equals("STIlMeas"))
			{
				println("\t\t/* Generate events for illuminance measurement */");
				println("\t\t:: {");
				println("\t\t\td_step {");
				println("\t\t\t\t" + deviceType + "Index++;");
				println("\t\t\t\t/* Validate counter */");
				println("\t\t\t\tif");
				println("\t\t\t\t:: " + deviceType + "Index >= _g_" + deviceType + "Arr.length -> " + deviceType + "Index = 0;");
				println("\t\t\t\t:: else -> skip;");
				println("\t\t\t\tfi");
				println();
				println("\t\t\t\tgeneratedEvent.EvtType = illuminance;");
				println("\t\t\t\tgeneratedEvent.name = ILLUMINANCE;");
				println("\t\t\t}");
				
				println("\t\t\tif");
				println("\t\t\t:: generatedEvent.value = 5;");
				println("\t\t\t:: generatedEvent.value = 35;");
				println("\t\t\t:: generatedEvent.value = 65;");
				println("\t\t\tfi");
				
				println("\t\t\tHandle" + deviceType + "Evt(" + deviceType + "Index, generatedEvent);");
				println("\t\t}");
				isEvtGenerated = true;
			}
			else if(deviceType.equals("STWaterSensor"))
			{
				println("\t\t/* Generate events for water sensor */");
				println("\t\t:: {");
				println("\t\t\td_step {");
				println("\t\t\t\t" + deviceType + "Index++;");
				println("\t\t\t\t/* Validate counter */");
				println("\t\t\t\tif");
				println("\t\t\t\t:: " + deviceType + "Index >= _g_" + deviceType + "Arr.length -> " + deviceType + "Index = 0;");
				println("\t\t\t\t:: else -> skip;");
				println("\t\t\t\tfi");
				println();
				println("\t\t\t\tgeneratedEvent.name = WATER;");
				println("\t\t\t}");
				
				println("\t\t\tif");
				println("\t\t\t:: generatedEvent.EvtType = wet; generatedEvent.EvtType = WET;");
				println("\t\t\t:: generatedEvent.EvtType = dry; generatedEvent.EvtType = DRY;");
				println("\t\t\tfi");
				
				println("\t\t\tHandle" + deviceType + "Evt(" + deviceType + "Index, generatedEvent);");
				println("\t\t}");
				isEvtGenerated = true;
			}
			else if(deviceType.equals("STAccSensor"))
			{
				println("\t\t/* Generate events for acceleration sensors */");
				println("\t\t:: {");
				println("\t\t\td_step {");
				println("\t\t\t\t" + deviceType + "Index++;");
				println("\t\t\t\t/* Validate counter */");
				println("\t\t\t\tif");
				println("\t\t\t\t:: " + deviceType + "Index >= _g_" + deviceType + "Arr.length -> " + deviceType + "Index = 0;");
				println("\t\t\t\t:: else -> skip;");
				println("\t\t\t\tfi");
				println("\t\t\t}");
				println();
				
				println("\t\t\tif");
				println("\t\t\t:: generatedEvent.EvtType = inactive; generatedEvent.value = INACTIVE;");
				println("\t\t\t:: generatedEvent.EvtType = _active; generatedEvent.value = ACTIVE;");
				println("\t\t\tfi");
				println("\t\t\tgeneratedEvent.name = ACCELERATION;");
				println("\t\t\tHandle" + deviceType + "Evt(" + deviceType + "Index, generatedEvent);");
				println("\t\t}");
				isEvtGenerated = true;
			}
			else if(deviceType.equals("STCarMoDetector"))
			{
				println("\t\t/* Generate events for carbon monoxide detector */");
				println("\t\t:: {");
				println("\t\t\td_step {");
				println("\t\t\t\t" + deviceType + "Index++;");
				println("\t\t\t\t/* Validate counter */");
				println("\t\t\t\tif");
				println("\t\t\t\t:: " + deviceType + "Index >= _g_" + deviceType + "Arr.length -> " + deviceType + "Index = 0;");
				println("\t\t\t\t:: else -> skip;");
				println("\t\t\t\tfi");
				println("\t\t\t}");
				println();
				
				println("\t\t\tif");
				println("\t\t\t:: generatedEvent.EvtType = clear; generatedEvent.value = CLEAR;");
				println("\t\t\t:: generatedEvent.EvtType = detected; generatedEvent.value = DETECTED;");
				println("\t\t\t:: generatedEvent.EvtType = tested; generatedEvent.value = TESTED;");
				println("\t\t\tfi");
				println("\t\t\tgeneratedEvent.name = ALARM;");
				println("\t\t\tHandle" + deviceType + "Evt(" + deviceType + "Index, generatedEvent);");
				println("\t\t}");
				isEvtGenerated = true;
			}
			else if(deviceType.equals("STSmokeDetector"))
			{
				println("\t\t/* Generate events for smoke detector */");
				println("\t\t:: {");
				println("\t\t\td_step {");
				println("\t\t\t\t" + deviceType + "Index++;");
				println("\t\t\t\t/* Validate counter */");
				println("\t\t\t\tif");
				println("\t\t\t\t:: " + deviceType + "Index >= _g_" + deviceType + "Arr.length -> " + deviceType + "Index = 0;");
				println("\t\t\t\t:: else -> skip;");
				println("\t\t\t\tfi");
				println("\t\t\t}");
				println();
				
				println("\t\t\tif");
				println("\t\t\t:: generatedEvent.EvtType = clear; generatedEvent.value = CLEAR;");
				println("\t\t\t:: generatedEvent.EvtType = detected; generatedEvent.value = DETECTED;");
				println("\t\t\t:: generatedEvent.EvtType = tested; generatedEvent.value = TESTED;");
				println("\t\t\tfi");
				println("\t\t\tgeneratedEvent.name = SMOKE;");
				println("\t\t\tHandle" + deviceType + "Evt(" + deviceType + "Index, generatedEvent);");
				println("\t\t}");
				isEvtGenerated = true;
			}
			else if(deviceType.equals("STAeonKeyFob"))
			{
				println("\t\t/* Generate events for AeonKeyFob button device */");
				println("\t\t:: {");
				println("\t\t\td_step {");
				println("\t\t\t\t" + deviceType + "Index++;");
				println("\t\t\t\t/* Validate counter */");
				println("\t\t\t\tif");
				println("\t\t\t\t:: " + deviceType + "Index >= _g_" + deviceType + "Arr.length -> " + deviceType + "Index = 0;");
				println("\t\t\t\t:: else -> skip;");
				println("\t\t\t\tfi");
				println("\t\t\t}");
				println();
				
				println("\t\t\tif");
				println("\t\t\t:: generatedEvent.EvtType = pushed; generatedEvent.value = PUSHED;");
				println("\t\t\t:: generatedEvent.EvtType = held; generatedEvent.value = HELD;");
				println("\t\t\tfi");
				println("\t\t\tgeneratedEvent.name = BUTTON;");
				println("\t\t\tHandle" + deviceType + "Evt(" + deviceType + "Index, generatedEvent);");
				println("\t\t}");
				isEvtGenerated = true;
			}
			else if(deviceType.equals("STRelHumMeas"))
			{
				println("\t\t/* Generate events for relative humidity measurement */");
				println("\t\t:: {");
				println("\t\t\td_step {");
				println("\t\t\t\t" + deviceType + "Index++;");
				println("\t\t\t\t/* Validate counter */");
				println("\t\t\t\tif");
				println("\t\t\t\t:: " + deviceType + "Index >= _g_" + deviceType + "Arr.length -> " + deviceType + "Index = 0;");
				println("\t\t\t\t:: else -> skip;");
				println("\t\t\t\tfi");
				println();
				println("\t\t\t\tgeneratedEvent.EvtType = humidity;");
				println("\t\t\t\tgeneratedEvent.name = HUMIDITY;");
				println("\t\t\t}");
				
				println("\t\t\tif");
				println("\t\t\t:: generatedEvent.value = 10;");
				println("\t\t\t:: generatedEvent.value = 30;");
				println("\t\t\t:: generatedEvent.value = 50;");
				println("\t\t\t:: generatedEvent.value = 70;");
				println("\t\t\t:: generatedEvent.value = 100;");
				println("\t\t\tfi");
				
				println("\t\t\tHandle" + deviceType + "Evt(" + deviceType + "Index, generatedEvent);");
				println("\t\t}");
				isEvtGenerated = true;
			}
		}
//		{
//			Map<String, String> deviceList = GConfigInfoManager.getDeviceListFromConfigInfo();
//			
//			if(deviceList.size() > 0)
//			{
//				for(Map.Entry<String, String> entry : deviceList.entrySet())
//				{
//					if(entry.getValue().equals("STSwitch"))
//					{
//						println("\t/* Generate events for a switch */");
//						println("\t:: {");
//						println("\t\tif");
//						println("\t\t:: generatedEvent.EvtType = off; generatedEvent.value = OFF;");
//						println("\t\t:: generatedEvent.EvtType = on; generatedEvent.value = ON;");
//						println("\t\tfi");
//						println("\t\td_step {");
//						println("\t\t\tgeneratedEvent.name = SWITCH;");
//						println("\t\t\tgeneratedEvent.date = STCurrentSystemTime*3600000;");
//						println("\t\t}");
//						println("\t\tHandleSTSwitchEvt(" + entry.getKey() + ", generatedEvent);");
//						println("\t}");
//						isEvtGenerated = true;
//					}
//					else if(entry.getValue().equals("STLock"))
//					{
//						println("\t/* Generate events for a lock */");
//						println("\t:: atomic {");
//						println("\t\tif");
//						println("\t\t:: generatedEvent.EvtType = lock; generatedEvent.value = LOCKED;");
//						println("\t\t:: generatedEvent.EvtType = unlock; generatedEvent.value = UNLOCKED;");
//						println("\t\tfi");
//						println("\t\td_step {");
//						println("\t\t\tgeneratedEvent.name = LOCK;");
//						println("\t\t\tgeneratedEvent.date = STCurrentSystemTime*3600000;");
//						println("\t\t}");
//						println("\t\tHandleSTLockEvt(" + entry.getKey() + ", generatedEvent);");
//						println("\t}");
//						isEvtGenerated = true;
//					}
//				}
//			}
//		}
		/* Generate app events */
//		for(String smartAppName : GPotentialRiskScreener.getCurrentSmartAppNames())
//		{
//			if(GConfigInfoManager.isSubscribedToApp(smartAppName))
//			{
//				println("\t\t/* Generate app events for " + smartAppName + " */");
//				println("\t\t:: {");
//				println("\t\t\tgeneratedEvent.EvtType = appTouch;");
//				println("\t\t\t" + smartAppName + "_AppChan!generatedEvent;");
//				println("\t\t}");
//			}
//		}
		if(!isEvtGenerated)
		{
			println("\t\t:: skip;");
		}
		println("\t\tfi");
		println();
		
		println("loc_StartOfEvtHandlers:");
		println("\t\tallEvtsHandled = true;");
		println("\t\tsystemTimeIncrementNeeded = false;");
		println();
		{
			Set<String> evtHandlers = GPotentialRiskScreener.getCurrentEvtHandlers();
			String evtHandler;
			
			for(String smartAppName : smartApps)
			{
				java.util.List<GProcessedSubscriptionInfo> subscriptionInfoList = GConfigInfoManager.getSubscriptionInfo(smartAppName);
				
				println("\t\t/* Handlers of " + smartAppName + " */");
				if(subscriptionInfoList != null)
				{
					for(GProcessedSubscriptionInfo subscriptionInfo : subscriptionInfoList)
					{
						if(subscriptionInfo.inputName.equals("location"))
						{
							boolean skipNeeded = true;
							evtHandler = smartAppName + "_" + subscriptionInfo.subscriptionInfoList.get(0).evtHandler;
							
							println("\t\tif");
							println("\t\t:: location.BroadcastChans[" + smartAppName + "_location] == true -> atomic {");
							println("\t\t\tallEvtsHandled = false;");
//							println("\t\t\tsystemTimeIncrementNeeded = true;");
							println("\t\t\t/* Reset broadcast event indicator */");
							println("\t\t\tlocation.BroadcastChans[" + smartAppName + "_location] = false;");
							println();
							
							println("\t\t\t/* Fetch current data of global variables */");
							println("\t\t\td_step {");
							if(GConfigInfoManager.getDeviceConfigInfo(smartAppName) != null)
							{
								for(GDeviceConfigInfo dci : GConfigInfoManager.getDeviceConfigInfo(smartAppName))
								{
									if(dci.isMultiple)
									{
										int index = 0;
										
										for(String deviceName : dci.configDevices)
										{
											/* frontDoorSensor_STMotionSensor */
											String name = SpinUtil.getGlobalArrRef(deviceName, dci.deviceType);
											
											println("\t\t\t\tassign_" + dci.deviceType + 
													"_rec(" + dci.inputName + ".element[" + index + "], " + name + ");");
											index++;
											skipNeeded = false;
										}
									}
									else
									{
										String name = SpinUtil.getGlobalArrRef(dci.configDevices.get(0), dci.deviceType);
										
										println("\t\t\t\tassign_" + dci.deviceType + 
												"_rec(" + dci.inputName + ", " + name + ");");
										skipNeeded = false;
									}
								}
							}
							{
								GStateMapEnum stateMapType = GConfigInfoManager.getStateMapUsed(smartAppName);
								
								if(stateMapType != GStateMapEnum.unknown)
								{
									if(stateMapType == GStateMapEnum.Int2IntMap)
									{
										println("\t\t\t\tassign_CInt2IntMap_rec(" + smartAppName 
												+ "_state, _g_CInt2IntMapArr.element[" + smartAppName 
												+ "_state.gArrIndex]);");
										skipNeeded = false;
									}
									else if(stateMapType == GStateMapEnum.Int2IIMMap)
									{
										println("\t\t\t\tassign_CInt2IIMMap_rec(" + smartAppName 
												+ "_state, _g_CInt2IIMMapArr.element[" + smartAppName 
												+ "_state.gArrIndex]);");
										skipNeeded = false;
									}
									else if(stateMapType == GStateMapEnum.Int2IIIMMap)
									{
										println("\t\t\t\tassign_CInt2IIIMMap_rec(" + smartAppName 
												+ "_state, _g_CInt2IIIMMapArr.element[" + smartAppName 
												+ "_state.gArrIndex]);");
										skipNeeded = false;
									}
								}
							}
							if(skipNeeded)
							{
								println("\t\t\t\tskip;");
							}
							println("\t\t\t}");
							println();
							
//							if(evtHandlers.contains(evtHandler))
//							{
//								println("\t\t\t" + evtHandler + "(location.latestEvt);");
//							}
//							else
//							{
//								println("\t\t\tskip;");
//							}
							
							/*****************/
							println("\t\t\tif");
							for(GSubscriptionInfo subEvtInfo : subscriptionInfo.subscriptionInfoList)
							{
								String evtAttr = subEvtInfo.subscribedAttribute;
								String printStr;
								
								evtHandler = smartAppName + "_" + subEvtInfo.evtHandler;

								if(evtAttr.equals(""))
								{
									/* Print out event handler */
									if(evtHandlers.contains(evtHandler))
									{
										printStr = "\t\t\t:: true -> " + evtHandler + "(location.latestEvt);";
									}
									else
									{
										printStr = "\t\t\t:: true -> skip;";
									}
									println(printStr);
								}
								else if(evtAttr.equals("mode") || evtAttr.equals("location"))
								{
									/* Print out event attribute check and event handler */
									if(evtHandlers.contains(evtHandler))
									{
										printStr = "\t\t\t:: (location.latestEvt.EvtType == Home) || (location.latestEvt.EvtType == Away) || (location.latestEvt.EvtType == Night) -> " 
													+ evtHandler + "(location.latestEvt);";
									}
									else
									{
										printStr = "\t\t\t:: (location.latestEvt.EvtType == Home) || (location.latestEvt.EvtType == Away) || (location.latestEvt.EvtType == Night) -> skip;";
									}
									println(printStr);
								}
								else
								{
									/* Print out event attribute check and event handler */
									if(evtHandlers.contains(evtHandler))
									{
										printStr = "\t\t\t:: location.latestEvt.EvtType == " + evtAttr + " -> " + evtHandler + "(location.latestEvt);";
									}
									else
									{
										printStr = "\t\t\t:: location.latestEvt.EvtType == " + evtAttr + " -> skip;";
									}
									println(printStr);
								}
							}
							println("\t\t\t:: else -> skip;");
							println("\t\t\tfi");
							/*****************/
							
							println("\t\t}");
							println("\t\t:: else -> skip;");
							println("\t\tfi");
						}
						else if(subscriptionInfo.inputName.equals("app"))
						{
//							evtHandler = smartAppName + "_" + subscriptionInfo.subscriptionInfoList.get(0).evtHandler;
//							
//							println("\t\t:: " + smartAppName + "_AppChan?generatedEvent -> atomic {");
//							println("\t\t\t/* Fetch current data of global variables */");
//							println("\t\t\td_step {");
//							for(GDeviceConfigInfo dci : GConfigInfoManager.getDeviceConfigInfo(smartAppName))
//							{
//								if(dci.isMultiple)
//								{
//									int index = 0;
//									
//									for(String deviceName : dci.configDevices)
//									{
//										/* frontDoorSensor_STMotionSensor */
//										String name = SpinUtil.getGlobalArrRef(deviceName, dci.deviceType);
//										
//										println("\t\t\t\tassign_" + dci.deviceType + 
//												"_rec(" + dci.inputName + ".element[" + index + "], " + name + ");");
//										index++;
//									}
//								}
//								else
//								{
//									String name = SpinUtil.getGlobalArrRef(dci.configDevices.get(0), dci.deviceType);
//									
//									println("\t\t\t\tassign_" + dci.deviceType + 
//											"_rec(" + dci.inputName + ", " + name + ");");
//								}
//							}
//							println("\t\t\t}");
//							println();
//							
//							if(evtHandlers.contains(evtHandler))
//							{
//								println("\t\t\t" + evtHandler + "(generatedEvent);");
//							}
//							else
//							{
//								println("\t\t\tskip;");
//							}
//							println("\t\t}");
						}
						else if(subscriptionInfo.subscriptionInfoList.get(0).subscribedAttribute.equals("time"))
						{
							evtHandler = smartAppName + "_" + subscriptionInfo.subscriptionInfoList.get(0).evtHandler;
							
							if(evtHandlers.contains(evtHandler))
							{
								println("\t\tif");
								println("\t\t:: STCurrentSystemTime == " + subscriptionInfo.inputName + " -> {");
								println("\t\t\tallEvtsHandled = false;");
								println("\t\t\tsystemTimeIncrementNeeded = true;");
								println("\t\t\t" + evtHandler + "(generatedEvent);");
								println("\t\t}");
								println("\t\t:: else -> skip;");
								println("\t\tfi");
							}
						}
						else
						{
							int numConfigDevices = GConfigInfoManager.getNumConfigDevices(smartAppName, subscriptionInfo.inputName);
							String deviceType = GConfigInfoManager.getDeviceType(smartAppName, subscriptionInfo.inputName);
							
							if(!GConfigInfoManager.isInputDeviceMultiple(smartAppName, subscriptionInfo.inputName))
							{
								String arrIndex = subscriptionInfo.inputName + ".gArrIndex";
								String broadcastRef = "_g_" + deviceType + "Arr.element[" + arrIndex
										+ "].BroadcastChans[" + subscriptionInfo.inputName + ".BroadcastChanIndex]";
								
								println("\t\tif");
								println("\t\t:: " + broadcastRef + " == true -> atomic {");
								{
									println("\t\t\tallEvtsHandled = false;");
//									println("\t\t\tsystemTimeIncrementNeeded = true;");
									println("\t\t\t/* Reset broadcast event indicator */");
									println("\t\t\t " + broadcastRef + " = false");
									println();
									
									println("\t\t\t/* Get latest event */");
									{
										println("\t\t\tgetPrevStoredEvtIndex(_g_" + deviceType + "Arr.element[" + arrIndex 
												+ "].currEvtIndex, " + latestEvtIndex + ");");
										println("\t\t\tassign_STEvent_rec(generatedEvent, " + "_g_" + deviceType + "Arr.element[" + arrIndex 
										        + "].events.element[" + latestEvtIndex + "]);");
									}
									println();
									
									println("\t\t\t/* Fetch current data of global variables */");
									boolean skipNeeded = true;
									println("\t\t\td_step {");
									if(GConfigInfoManager.getDeviceConfigInfo(smartAppName) != null)
									{
										for(GDeviceConfigInfo dci : GConfigInfoManager.getDeviceConfigInfo(smartAppName))
										{
											if(dci.isMultiple)
											{
												int index = 0;
												
												for(String deviceName : dci.configDevices)
												{
													/* frontDoorSensor_STMotionSensor */
													String name = SpinUtil.getGlobalArrRef(deviceName, dci.deviceType);
													
													println("\t\t\t\tassign_" + dci.deviceType + 
															"_rec(" + dci.inputName + ".element[" + index + "], " + name + ");");
													index++;
													skipNeeded = false;
												}
											}
											else
											{
												String name = SpinUtil.getGlobalArrRef(dci.configDevices.get(0), dci.deviceType);
												
												println("\t\t\t\tassign_" + dci.deviceType + 
														"_rec(" + dci.inputName + ", " + name + ");");
												skipNeeded = false;
											}
										}
									}
									{
										GStateMapEnum stateMapType = GConfigInfoManager.getStateMapUsed(smartAppName);
										
										if(stateMapType != GStateMapEnum.unknown)
										{
											if(stateMapType == GStateMapEnum.Int2IntMap)
											{
												println("\t\t\t\tassign_CInt2IntMap_rec(" + smartAppName 
														+ "_state, _g_CInt2IntMapArr.element[" + smartAppName 
														+ "_state.gArrIndex]);");
												skipNeeded = false;
											}
											else if(stateMapType == GStateMapEnum.Int2IIMMap)
											{
												println("\t\t\t\tassign_CInt2IIMMap_rec(" + smartAppName 
														+ "_state, _g_CInt2IIMMapArr.element[" + smartAppName 
														+ "_state.gArrIndex]);");
												skipNeeded = false;
											}
											else if(stateMapType == GStateMapEnum.Int2IIIMMap)
											{
												println("\t\t\t\tassign_CInt2IIIMMap_rec(" + smartAppName 
														+ "_state, _g_CInt2IIIMMapArr.element[" + smartAppName 
														+ "_state.gArrIndex]);");
												skipNeeded = false;
											}
										}
									}
									if(skipNeeded)
									{
										println("\t\t\t\tskip;");
									}
									println("\t\t\t}");
									println();
									
									/*****************/
									println("\t\t\tif");
									for(GSubscriptionInfo subEvtInfo : subscriptionInfo.subscriptionInfoList)
									{
										String evtType = subEvtInfo.subscribedEvtType;
										String printStr;
										
										evtHandler = smartAppName + "_" + subEvtInfo.evtHandler;
	
										if(evtType.equals(""))
										{
											/* Print out event handler */
											if(evtHandlers.contains(evtHandler))
											{
												printStr = "\t\t\t:: true -> " + evtHandler + "(generatedEvent);";
											}
											else
											{
												printStr = "\t\t\t:: true -> skip;";
											}
											println(printStr);
										}
										else
										{
											/* Print out event type check and event handler */
											if(evtHandlers.contains(evtHandler))
											{
												printStr = "\t\t\t:: generatedEvent.EvtType == " + evtType + " -> " + evtHandler + "(generatedEvent);";
											}
											else
											{
												printStr = "\t\t\t:: generatedEvent.EvtType == " + evtType + " -> skip;";
											}
											println(printStr);
										}
									}
									println("\t\t\t:: else -> skip;");
									println("\t\t\tfi");
									/*****************/
								}
								println("\t\t}");
								println("\t\t:: else -> skip;");
								println("\t\tfi");
							}
							else
							{
								for(int i = 0; i < numConfigDevices; i++)
								{
									String arrIndex = subscriptionInfo.inputName + ".element[" + i + "]" + ".gArrIndex";
									String broadcastRef = "_g_" + deviceType + "Arr.element[" + arrIndex
											+ "].BroadcastChans[" + subscriptionInfo.inputName + ".element[" + i + "]" 
											+ ".BroadcastChanIndex]";
									
									println("\t\tif");
									println("\t\t:: " + broadcastRef + " == true -> atomic {");
									{
										println("\t\t\tallEvtsHandled = false;");
//										println("\t\t\tsystemTimeIncrementNeeded = true;");
										println("\t\t\t/* Reset broadcast event indicator */");
										println("\t\t\t " + broadcastRef + " = false");
										println();
										
										println("\t\t\t/* Get latest event */");
										{
											println("\t\t\tgetPrevStoredEvtIndex(_g_" + deviceType + "Arr.element[" + arrIndex 
													+ "].currEvtIndex, " + latestEvtIndex + ");");
											println("\t\t\tassign_STEvent_rec(generatedEvent, " + "_g_" + deviceType + "Arr.element[" + arrIndex 
											        + "].events.element[" + latestEvtIndex + "]);");
										}
										println();
										
										println("\t\t\t/* Fetch current data of global variables */");
										boolean skipNeeded = true;
										println("\t\t\td_step {");
										for(GDeviceConfigInfo dci : GConfigInfoManager.getDeviceConfigInfo(smartAppName))
										{
											if(dci.isMultiple)
											{
												int index = 0;
												
												for(String deviceName : dci.configDevices)
												{
													/* frontDoorSensor_STMotionSensor */
													String name = SpinUtil.getGlobalArrRef(deviceName, dci.deviceType);
													
													println("\t\t\t\tassign_" + dci.deviceType + 
															"_rec(" + dci.inputName + ".element[" + index + "], " + name + ");");
													index++;
													skipNeeded = false;
												}
											}
											else
											{
												String name = SpinUtil.getGlobalArrRef(dci.configDevices.get(0), dci.deviceType);
												
												println("\t\t\t\tassign_" + dci.deviceType + 
														"_rec(" + dci.inputName + ", " + name + ");");
												skipNeeded = false;
											}
										}
										if(skipNeeded)
										{
											println("\t\t\t\tskip;");
										}
										println("\t\t\t}");
										println();
										
										/*****************/
										println("\t\t\tif");
										for(GSubscriptionInfo subEvtInfo : subscriptionInfo.subscriptionInfoList)
										{
											String evtType = subEvtInfo.subscribedEvtType;
											String printStr;
											
											evtHandler = smartAppName + "_" + subEvtInfo.evtHandler;
		
											if(evtType.equals(""))
											{
												/* Print out event handler */
												if(evtHandlers.contains(evtHandler))
												{
													printStr = "\t\t\t:: true -> " + evtHandler + "(generatedEvent);";
												}
												else
												{
													printStr = "\t\t\t:: true -> skip;";
												}
												println(printStr);
											}
											else
											{
												/* Print out event type check and event handler */
												if(evtHandlers.contains(evtHandler))
												{
													printStr = "\t\t\t:: generatedEvent.EvtType == " + evtType + " -> " + evtHandler + "(generatedEvent);";
												}
												else
												{
													printStr = "\t\t\t:: generatedEvent.EvtType == " + evtType + " -> skip;";
												}
												println(printStr);
											}
										}
										println("\t\t\t:: else -> skip;");
										println("\t\t\tfi");
										/*****************/
									}
									println("\t\t}");
									println("\t\t:: else -> skip;");
									println("\t\tfi");
								}
							}
						}
					}
				}
				println();
			}
		}
		
		println("\t\tif");
		println("\t\t:: allEvtsHandled == false -> {");
		println("\t\t\tif");
		println("\t\t\t:: STCurrentSystemTime >= MAX_SYSTEM_TIME -> assert(0);");
		println("\t\t\t:: else -> skip;");
		println("\t\t\tfi");
		println();
		
		println("\t\t\tif");
		println("\t\t\t:: systemTimeIncrementNeeded == true -> STCurrentSystemTime++;");
		println("\t\t\t:: else -> skip;");
		println("\t\t\tfi");
		println("\t\t\tgoto loc_StartOfEvtHandlers;");
		println("\t\t}");
		println("\t\t:: !(allEvtsHandled == false) -> eventProcessed = true;");
		println("\t\tfi");
		
		println("\t}");
//		println("\tod");
		println("}");
		
		println("/*************** End of events generator *********************/");
		println();
		
		indexValue++;
	}
	/* [Thomas, May 16, 2017]
	 * */
	private void declareDevices()
	{
		Map<String, String> deviceList = GConfigInfoManager.getDeviceListFromConfigInfo();
		
		println("/*************** Start of devices declaration ****************/");
		
		for(Map.Entry<String, String> entry : deviceList.entrySet())
		{
			println("hidden byte " + entry.getKey() + ";");
		}
		println();
		
		for(String deviceType : GConfigInfoManager.getDeviceTypeListFromConfigInfo())
		{
			println(deviceType + "_arr _g_" + deviceType + "Arr;");
		}
		if(GConfigInfoManager.isStateMapUsed())
		{
			println("hidden CInt2IntMap_arr _g_CInt2IntMapArr;");
			println("hidden CInt2IIMMap_arr _g_CInt2IIMMapArr;");
			println("hidden CInt2IIIMMap_arr _g_CInt2IIIMMapArr;");
		}
		println();
		println("byte STCurrentSystemTime;");
		println("byte STCurrentTemperature;");
		println("STNetworkManager_rec _STNetworkManager;");
		println("bool httpPostAllowed = false;");
		println("STLocation_rec location;");
//		println("byte numTranFailure = 0;");
		
		/* Declare location's broadcast channel index parameter */
		{
			for(String smartAppName : GPotentialRiskScreener.getCurrentSmartAppNames())
			{
				if(GConfigInfoManager.isSubscribedToLocation(smartAppName))
				{
					println("hidden byte " + smartAppName + "_location; /* location's broadcast channel index */");
				}
			}
		}
		/* Declare app channel */
//		{
//			for(String smartAppName : GPotentialRiskScreener.getCurrentSmartAppNames())
//			{
//				if(GConfigInfoManager.isSubscribedToApp(smartAppName))
//				{
//					println("chan " + smartAppName + "_AppChan =  [MAX_CHAN_SIZE] of {STEvent_rec};");
//				}
//			}
//		}
		
		println("/*************** End of devices declaration ******************/");
		println();
	}
	/* [Thomas, May 19, 2017]
	 * */
	private void declareLocationEvtHandler()
	{
		println("/*************** Start of location manager *******************/");
		println("hidden byte _index" + indexValue + ";");
		println("inline HandleLocationEvt(Evt)");
		println("{");
		println("\tshort HandleLocationEvt_mode;");
		println();

//		println("\tif");//
//		println("\t:: (Evt.EvtType != location.LatestCommandType) || (location.latestEvt.id == 0) -> {");//
//		println("\t\td_step {");//
		
		println("\td_step {");
		println("\t\tif");
		
		/* Location mode change */
		
		println("\t\t:: (Evt.EvtType == Home) || (Evt.EvtType == Night) || (Evt.EvtType == Away) -> {");
		println("\t\t\t/* Check conflict commands */");
		println("\t\t\tif");
		println("\t\t\t:: (location.latestEvt.id == Evt.id) && (location.latestEvt.date == STCurrentSystemTime*3600000) -> assert(Evt.EvtType == location.latestEvt.EvtType);");
		println("\t\t\t:: else -> skip;");
		println("\t\t\tfi");
		println();
		
		println("\t\t\t/* Increase number of received commands per event */");
		println("\t\t\tif");
		println("\t\t\t:: (Evt.id == location.latestEvt.id) || (location.latestEvt.id == 0) -> location.NumReceivedCommands++;");
		println("\t\t\t:: else -> skip;");
		println("\t\t\tfi");
		println("\t\t\t/* Check DOS attack or infinite loop of commands */");
		println("\t\t\tif");
		println("\t\t\t:: (location.NumReceivedCommands >= MAX_COMMAND_REPITIONS) -> assert(0);");
		println("\t\t\t:: else -> skip;");
		println("\t\t\tfi");
		println();
		
		println("\t\t\t/* Update current mode */");
		println("\t\t\tgetIntValueFromMtype(Evt.EvtType, HandleLocationEvt_mode);");
		println("\t\t\tlocation.mode = HandleLocationEvt_mode;");
		println("\t\t\tEvt.name = MODE;");
		println("\t\t\tEvt.value = HandleLocationEvt_mode;");
		println("\t\t\tEvt.date = STCurrentSystemTime*3600000;");
		println("\t\t\tEvt.isAlive = 1;");
		println("\t\t}");
		println("\t\t:: else -> skip;");
		println("\t\tfi");
		println("\t\t/* Store the current event */");
		println("\t\tassign_STEvent_rec(location.latestEvt, Evt);");
		println();
		
//		println("\t\tif");
//		println("\t\t:: (location.latestEvt.id == 0) || (Evt.EvtType != location.latestEvt.EvtType) -> {");
//		println("\t\t\t/* Broadcast the mode change event to subscribers */");
//		println("\t\t\tfor(_index" + indexValue + " : 0 .. location.NumSubscribers-1) {");
//		println("\t\t\t\tlocation.BroadcastChans[_index" + indexValue + "] = true;");
//		println("\t\t\t}");
//		println("\t\t}");
//		println("\t\t:: else -> skip;");
//		println("\t\tfi");
		
		println("\t\t/* Broadcast the mode change event to subscribers */");
		println("\t\t\tfor(_index" + indexValue + " : 0 .. location.NumSubscribers-1) {");
		println("\t\t\t\tlocation.BroadcastChans[_index" + indexValue + "] = true;");
		println("\t\t\t}");
		
		println("\t}");
		println("}");
		
		println("inline ChangeLocationMode(Evt)");
		println("{");
		println("\td_step {");
		println("\t\tif");
		println("\t\t:: Evt.EvtType == present -> location.mode = HOME;");
		println("\t\t:: else -> skip;");
		println("\t\tfi");
		println("\t}");
		println("}");
		
		println("inline InitializeLocation()");
		println("{");
		println("\tlocation.NumSubscribers = 0;");
//		println("\t/* Randomly select initial location mode */");
//		println("\tif");
//		println("\t:: location.mode = AWAY;");
//		println("\t:: location.mode = HOME;");
//		println("\t:: location.mode = NIGHT;");
//		println("\tfi");
		println("\tlocation.mode = HOME;");
		println("\tlocation.modes.length = 3;");
		println("\tlocation.modes.element[0].name = HOME;");
		println("\tlocation.modes.element[0].isAlive = 1;");
		println("\tlocation.modes.element[1].name = AWAY;");
		println("\tlocation.modes.element[1].isAlive = 1;");
		println("\tlocation.modes.element[2].name = NIGHT;");
		println("\tlocation.modes.element[2].isAlive = 1;");
		println("\tlocation.sunriseSunset.sunrise = ST_SUNRISE_TIME;");
		println("\tlocation.sunriseSunset.sunset = ST_SUNSET_TIME;");
		println("\tlocation.sunriseSunset.isAlive = 1;");
		println("}");

		println("/*************** End of location manager *********************/");
		println();
		
		indexValue++;
	}
	
	private void declareDeviceManager()
	{
		println("/*************** Start of device manager *********************/");
		
		println("proctype DeviceManager()");
		println("{");
		println("\tSTEvent_rec Evt;");
		println();
		println("end: do");
		
		println("\t:: location.ToDeviceChannel?Evt -> atomic {HandleLocationEvt(Evt);}");
		
		/* Print out device managers */
		{
			Map<String, String> deviceList = GConfigInfoManager.getDeviceListFromConfigInfo();
			
			for(Map.Entry<String, String> entry : deviceList.entrySet())
			{
				String deviceType = entry.getValue();
				
				/* Check if deviceType is an actuator device */
				if(SpinUtil.isADevice(deviceType + "_rec") && !SpinUtil.isASensorDevice(deviceType)
						&& !deviceType.equals("location") && !deviceType.equals("app"))
				{
					println("\t:: _g_" + deviceType + "Arr.element[" + entry.getKey() 
						+ "].ToDeviceChannel?Evt -> atomic {Handle" + deviceType + "Evt(" + 
						entry.getKey() + ", Evt);}");
				}
			}
		}
		
		println("\tod");
		println("}");
		println("/*************** End of device manager ***********************/");
		println();
	}
	
	private void declareDeviceManagers()
	{
		println("/*************** Start of device managers *********************/");
		
		/* LocationManager */
		{
			println("proctype LocationManager()");
			println("{");
			println("\tSTEvent_rec Evt;");
			println();
			println("end: do");
			println("\t:: location.ToDeviceChannel?Evt -> atomic {HandleLocationEvt(Evt);}");
			println("\tod");
			println("}");
		}
		
		/* Other actuator devices */
		{
			for(String deviceType : GConfigInfoManager.getDeviceTypeListFromConfigInfo())
			{
				/* Check if deviceType is an actuator device */
				if(SpinUtil.isADevice(deviceType + "_rec") && !SpinUtil.isASensorDevice(deviceType)
						&& !deviceType.equals("location") && !deviceType.equals("app"))
				{
					println("proctype " + deviceType + "Manager(byte deviceIndex)");
					println("{");
					println("\tSTEvent_rec Evt;");
					println();
					println("end: do");
					
					println("\t:: _g_" + deviceType + "Arr.element[deviceIndex].ToDeviceChannel?Evt -> atomic {Handle" + 
							deviceType + "Evt(deviceIndex, Evt);}");
					
					println("\tod");
					println("}");
				}
			}
		}
		
		println("/*************** End of device manager ***********************/");
		println();
	}
	
	/* [Thomas, May 19, 2017]
	 * */
	private void declareNetworkEvtHandler()
	{
		println("/*************** Start of network manager ********************/");
		println("inline HandleNetworkEvt(_ST_Command)");
		println("{");
		println("\td_step {");
		println("\t\t/* Verify the recipient for sendSms */");
		println("\t\tif");
		
		println("\t\t:: (_ST_Command.EvtType == sendSms) -> {");
		println("\t\t\tif");
		println("\t\t\t:: (_STNetworkManager.configuredPhoneNumber == 0) -> assert(0);");
		println("\t\t\t:: (_STNetworkManager.configuredPhoneNumber != _STNetworkManager.receivedPhoneNumber) -> assert(0);");
		println("\t\t\t:: else -> skip;");
		println("\t\t\tfi");
		println("\t\t}");
		
		println("\t\t:: (_ST_Command.EvtType == httpPost) -> {");
		println("\t\t\tif");
		println("\t\t\t:: (httpPostAllowed == false) -> assert(0);");
		println("\t\t\t:: !((httpPostAllowed == false)) -> skip;");
		println("\t\t\tfi");
		println("\t\t}");
		
		println("\t\t:: (_ST_Command.EvtType == unsubscribe) -> assert(0);");
		
		println("\t\t:: else -> skip;");
		println("\t\tfi");
		
		println("\t}");
		println("}");

		println("/*************** End of network manager **********************/");
		println();
	}
	
	/* [Thomas, May 21, 2017]
	 * */
	private void printConfiguredPhoneNumberAssignment()
	{
		String configuredPhoneNumb = GConfigInfoManager.getConfiguredPhoneNumber(this.sootClassName);
		
		if(configuredPhoneNumb != null)
		{
			println("/* Configured phone number of recipient */");
			println("_STNetworkManager.configuredPhoneNumber = " + configuredPhoneNumb + ";");
		}
		else
		{
			println("/* Phone number of recipient is not configured by user */");
			println("_STNetworkManager.configuredPhoneNumber = 0;");
		}
		
		println();
	}
	
	/* [Thomas, May 25, 2017]
	 * */
	private void declareSystemTimeManager()
	{	
		println("/*************** Start of system time manager ****************/");
		
		println("proctype SystemTimeManager()");
		println("{");
		println("\tSTCurrentSystemTime = 1;");
		println("end: do");
		println("\t:: atomic {");
		println("\t\tif");
		println("\t\t:: STCurrentSystemTime > MAX_SYSTEM_TIME -> break;");
		println("\t\t:: else -> STCurrentSystemTime++;");
		println("\t\tfi");
		println("\t}");
		println("\tod");
		println("}");
		
		println("/*************** End of system time manager ******************/");
		println();
	}
	
	/* [Thomas, May 16, 2017]
	 * */
	private void declareWatchdog()
	{
		Map<String, String> deviceList = GConfigInfoManager.getDeviceListFromConfigInfo();
		boolean motionDevice, switchDevice;
		
		println("/*************** Start of Watchdog ***************************/");
		
		println("proctype Watchdog()");
		println("{");
		println("\tSTEvent_rec Evt;");
		println("\tint i, j;");
		println();
		println("\tEvt.EvtType = exit;");
		
		println("\ttimeout -> {");
		
		motionDevice = false;
		switchDevice = false;
		for(Map.Entry<String, String> entry : deviceList.entrySet())
		{
			if(entry.getValue().equals("STMotionSensor") && !motionDevice)
			{
				motionDevice = true;
				
				println("\t\t/* Kill all motion sensor devices and their subsribers */");
				println("\t\tfor(i : 0 .. _g_MotionSensorArr.length-1) {");
				
				println("\t\t\t_g_MotionSensorArr.element[i].ToDeviceChannel!Evt;");
				println();
				println("\t\t\tfor(j : 0 .. _g_MotionSensorArr.element[i].NumSubscribers-1) {");
				println("\t\t\t\t_g_MotionSensorArr.element[i].BroadcastChans[j]!Evt;");
				println("\t\t\t}");
				
				println("\t\t}");
			}
			else if(entry.getValue().equals("STSwitch") && !switchDevice)
			{
				switchDevice = true;
				
				println("\t\t/* Kill all Switch devices and their subsribers */");
				println("\t\tfor(i : 0 .. _g_SwitchArr.length-1) {");
				
				println("\t\t\t_g_SwitchArr.element[i].ToDeviceChannel!Evt;");
				println();
				println("\t\t\tfor(j : 0 .. _g_SwitchArr.element[i].NumSubscribers-1) {");
				println("\t\t\t\t_g_SwitchArr.element[i].BroadcastChans[j]!Evt;");
				println("\t\t\t}");
				
				println("\t\t}");
			}
		}
		println();
		
		println("\t\t/* Kill all system processes */");
		println("\t\tlocation.ToDeviceChannel!Evt;");
		println("\t\t_STNetworkManager.ToDeviceChannel!Evt;");
		println();
		
		println("\t\t/* Kill all subscribers of location */");
		println("\t\tfor(i : 0 .. location.NumSubscribers-1) {");
		println("\t\t\tlocation.BroadcastChans[i]!Evt;");
		println("\t\t}");
		println();
		
		println("\t}");
		println("}");
		
		println("/*************** End of Watchdog *****************************/");
		println();
	}

	void subtypeDecl() {
		SubtypeRelation relation = new SubtypeRelation(system.getAggregateCount());
		packedSubtype = relation.build(system.getSubtypes());
		int targetSize = system.refAnyType().getTargets().size();
		if (targetSize > 0) {
			println("/* Subtype relation and accessors */");
			println("hidden int _subtype[" + packedSubtype.getLength() + "];");
			println("hidden int _typeid[" + targetSize + "];");
			println("#define _extends(x, y)\t(_subtype[(x*" +
					packedSubtype.getSize() + "+y)/8] & " + 
					"(1 << (7-((x*" + packedSubtype.getSize() + "+y)%8))))");
			println("");
		}
	}


	void subtypeInit() {
		//		int targetSize = system.refAnyType().getTargets().size();
		//		if (targetSize > 0) {
		//		    println("/* Subtype relation initialization */");
		//		    for (int i = 0; i < packedSubtype.getLength(); i ++)
		//			println("_subtype[" + i + "] = " + packedSubtype.elementAt(i) + ";");
		//	
		//		    for (int i = 0; i < targetSize; i ++) {
		//			Type type = system.refAnyType().getTargets().elementAt(i).getType();
		//			print("_typeid[" + i + "] = ");
		//			if (type instanceof Collection) {
		//			    Type base = ((Collection) type).getBaseType();
		//		  
		//			    if (base instanceof Array)
		//				base = arrayBaseType(base);
		//		  
		//			    if (base instanceof Record)
		//				println(((Record) base).getUnique()+";");
		//			    else
		//				println("-1;");
		//			}
		//		    }
		//	
		//		    println("");
		//		}
	}

	/**
	 * Retrieve simple result from translator, flag error if not simple.
	 * <p>
	 * A simple result is a case tree with only a single expression
	 * node under the NORMAL case.
	 */
	String getSimpleResult() {
		TreeNode result = ((CaseNode) getResult()).getCase(normal);
		if (result instanceof ExprNode)
			return ((ExprNode) result).expr1;
		else
			throw new RuntimeException("Result not simple: " + result);
	}  


	public SpinOptions getSpinOptions() {
		return options;
	}  


	/**
	 * Test if guard is present.
	 * <p>
	 * If the guard is TRUE (BoolLit) or the HAS_LOCK test (which we
	 * don't use), claim there is no guard.
	 */
	boolean guardPresent(Transformation trans) {
		Expr guard = trans.getGuard();
		if (guard == null)
			return false;
		if (guard instanceof BoolLit)
			return ! ((BoolLit) guard).getValue();
		if ((guard instanceof LockTest) &&
				((LockTest) guard).getOperation() == HAS_LOCK)
			return false;
		return true;
	}  


	/**
	 * Generate the header for all PROMELA files
	 */
	void header() {
		ThreadVector threadVector = system.getThreads();

		println("/*  Promela for transition system: " + system.getName() + " */");
		println();

		// Lock types (R = reentrant, W = waiting)
		//println("/* Lock operations */");
		/*println("#define LOCK 0");
	println("typedef lock_ {\n  chan lock = [1] of { bit };\n");
	println("  byte owner;\n};");
	println("typedef lock_RW {\n  chan lock = [1] of { bit };\n");
	println("  byte owner;\n  int waiting;\n  byte count;\n};\n");
	println("typedef lock_W {\n  chan lock = [1] of { bit };\n");
	println("  byte owner;\n  int waiting;\n};");
	println("typedef lock_R {\n  chan lock = [1] of { bit };\n");
	println("  byte owner;\n  byte count;\n};");

	// Monitor operations - most have separate reentrant version (_R)
	println("#define _lock(sync,locNum,transNum,actionNum) \\");
	println("  sync.lock?LOCK; sync.owner = _pid ");
	println("#define _lock_R(sync,locNum,transNum,actionNum) \\");
	println("  if \\");
	println("  :: sync.owner == _pid -> sync.count++; \\");
	println("  :: else -> sync.lock?LOCK; sync.owner = _pid; \\");
	println("  fi ");

	println("#define _unlock(sync,locNum,transNum,actionNum) \\");
	println("  sync.owner = 0; sync.lock!LOCK ");
	println("#define _unlock_R(sync,locNum,transNum,actionNum) \\");
	println("  if \\");
	println("  :: sync.count > 0 -> sync.count--; \\");
	println("  :: else -> sync.owner = 0; sync.lock!LOCK; \\");
	println("  fi ");

	println("#define _lockAvailable(sync) \\");
	println("  nempty(sync.lock)");
	println("#define _lockAvailable_R(sync) \\");
	println("  (nempty(sync.lock) || sync.owner == _pid) ");

	println("#define _unwait(sync,locNum,transNum,actionNum) \\");
	println("  sync.lock?LOCK; sync.owner = _pid ");
	println("#define _unwait_R(sync,locNum,transNum,actionNum) \\");
	println("  sync.lock?LOCK; sync.owner = _pid; \\");
	println("  sync.count = _temp_; _temp_ = 0 ");

	println("#define _notifyAll(sync,locNum,transNum,actionNum) \\");
	println("  if \\");
	println("  :: sync.owner == _pid -> sync.waiting = 0; \\");
	println("  :: else -> printf(\"BIR: %d locNum transNum actionNum IllegalMonitorStateException\\n\", _pid); assert(0); \\");
	println("  fi ");

	println("#define _notify(sync,locNum,transNum,actionNum) \\");
	println("  if \\");
	for (int i = 0; i < threadVector.size(); i++) {
	    int thread = (1 << i);
	    println("  :: (sync.owner == _pid) && (sync.waiting & " +
		    thread + ") -> sync.waiting = sync.waiting & " +
		    ~ thread + " ; printf(\"BIR? %d " + i + "\\n\",actionNum); \\");
	}
	println("  :: (sync.owner == _pid) && (sync.waiting == 0) -> printf(\"BIR? %d " + threadVector.size() + "\\n\",actionNum); \\");
	println("  :: else -> printf(\"BIR: %d locNum transNum actionNum IllegalMonitorStateException\\n\", _pid); assert(0); \\");
	println("  fi  ");

	for (int i = 0; i < threadVector.size(); i++) {
	    int thread = (1 << i);

	    println("#define _wait_" + i + "(sync,locNum,transNum,actionNum) \\");
	    println("  if \\");
	    println(
		    "  :: sync.owner == _pid -> sync.waiting = sync.waiting | " +
		    thread + ";  \\");
	    println("       sync.owner = 0; sync.lock!LOCK; \\");
	    println("  :: else -> printf(\"BIR: %d locNum transNum actionNum IllegalMonitorStateException\\n\", _pid); assert(0); \\");
	    println("  fi ");
	    println("#define _wait_" + i + "_R(sync,locNum,transNum,actionNum) \\");
	    println("  if \\");
	    println(
		    "  :: sync.owner == _pid -> sync.waiting = sync.waiting | " +
		    thread + ";  \\");
	    println("       _temp_ = sync.count; sync.count = 0; sync.owner = 0; sync.lock!LOCK; \\");
	    println("  :: else -> printf(\"BIR: %d locNum transNum actionNum IllegalMonitorStateException\\n\", _pid); assert(0); \\");
	    println("  fi ");

	    println("#define _wasNotified_" + i + "(sync) \\");
	    println("  !(sync.waiting & " + thread + ") ");

	    //    Thread operation
	    //    if (! threadVector.elementAt(i).isMain()) {
	    //  	println("#define _startThread_" + i + " \\");
	    //  	println("  _threadActive_" + i +
	    //  		" = true; _threadStart_" + i + "!LOCK");
	    //  	println("#define _beginThread_" + i + " \\");
	    //  	println("  _threadStart_" + i + "?LOCK");
	    //  	println("#define _joinThread_" + i + " \\");
	    //  	println("  skip");
	    //  	println("#define _exitThread_" + i + " \\");
	    //  	println("  _threadActive_" + i + " = 0; goto endtop_" + i);
	    //  	println("#define _threadTerminated_" + i + " \\");
	    //  	println("  (_threadActive_" + i + " == 0)");
	    //        }

	}*/

		// Reference macros
		println();
		println("/* Reference operations */");
		println("#define _collect(r) (r >> 8)");
		println("#define _index(r) (r & 255)");
		println("#define _ref(c,i) ((c << 8) | i)");

		// Allocator macro
		//   _i_ is always zero outside of this macro
		println("#define _allocate(collection,refindex,maxsize,locNum,transNum,actionNum) \\");
		println("  do \\");
		println("  :: collection.inuse[_i_] -> \\");
		println("     _i_ = _i_ + 1; \\");
		println("     if  \\");
		println("     :: _i_ == maxsize -> printf(\"BIR: %d locNum transNum actionNum  CollectionSizeLimitException\\n\", _pid); _i_ = 0; goto done; \\");
		println("     :: else \\");
		println("     fi; \\");
		println("  :: else -> \\");
		println("     collection.inuse[_i_] = 1; \\");
		println("     _temp_ = _ref(refindex,_i_);  \\");
		println("     _i_ = 0;  \\");
		println("     break;  \\");
		println("  od ");
		println();
		println("#define TRAP 1");
		println();
	}  


	/**
	 * Generate init {} block to initialize variables and start processes.
	 */
	void initBlock() {
		Set<String> smartAppNames = GPotentialRiskScreener.getCurrentSmartAppNames();
		
		indentLevel = 0;
		println("init {");
		println("\tint gDeviceId;");
		println();
		
		println("\tatomic {");
		
		/* Initialize global variables */
		{
			/* Initialize devices' index */
			{
				Map<String, String> deviceList = GConfigInfoManager.getDeviceListFromConfigInfo();
				if(GConfigInfoManager.isStateMapUsed())
				{
					println("\t\tfor(i : 0 .. MAX_INT_ARRAY_SIZE-1) {\n" +
								"\t\t\t_g_CInt2IntMapArr.element[i].isAlive = 1;\n" +
							"\t\t}\n" +
							"\t\t_g_CInt2IIMMapArr.length = 1;\n" +
							"\t\tfor(i : 1 .. MAX_INT_ARRAY_SIZE-1) {\n" +
								"\t\t\t_g_CInt2IIMMapArr.element[i].isAlive = 1;\n" +
								"\t\t\tfor(i1 : 1 .. MAX_INT_ARRAY_SIZE-1) {\n" +
									"\t\t\t\t_g_CInt2IIMMapArr.element[i].valueArr.element[i1].isAlive = 1;\n" +
								"\t\t\t}\n" +
							"\t\t}\n" +
							"\t\t_g_CInt2IIIMMapArr.length = 1;\n" +
							"\t\tfor(i : 1 .. MAX_INT_ARRAY_SIZE-1) {\n" +
								"\t\t\t_g_CInt2IIIMMapArr.element[i].isAlive = 1;\n" +
								"\t\t\tfor(i1 : 1 .. MAX_INT_ARRAY_SIZE-1) {\n" +
									"\t\t\t\t_g_CInt2IIIMMapArr.element[i].valueArr.element[i1].isAlive = 1;\n" +
									"\t\t\t\tfor(i2 : 1 .. MAX_INT_ARRAY_SIZE-1) {\n" +
										"\t\t\t\t\t_g_CInt2IIIMMapArr.element[i].valueArr.element[i1].valueArr.element[i2].isAlive = 1;\n" +
									"\t\t\t\t}\n" +
								"\t\t\t}\n" +
							"\t\t}");
					println("\t\t_g_CInt2IIIMMapArr.length = 1;");
				}
				println("\t\tInitializeLocation();");
				println();
				
				for(Map.Entry<String, String> entry : deviceList.entrySet())
				{
					String deviceType = entry.getValue();
					String deviceName = entry.getKey();
					
					/* Common behaviors */
					println("\t\t" + deviceName + " = _g_" + deviceType + "Arr.length;");
					println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].id = gDeviceId;");
					println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].gArrIndex = " + deviceName + ";");
					println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].NumSubscribers = 0;");
					println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].events.length = MAX_STORED_EVENTS;");
					println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].states.length = MAX_STORED_EVENTS;");
					println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].isAlive = 1;");
					println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].isOnline = 1;");
					
					/* Individual specific behaviors */
					if(entry.getValue().equals("STMotionSensor"))
					{
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].currentMotion = INACTIVE;");
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].motionState.value = INACTIVE;");
					}
					else if(entry.getValue().equals("STSwitch"))
					{
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].currentSwitch = OFF;");
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].switchState.value = OFF;");
					}
					else if(entry.getValue().equals("STTempMeas"))
					{
					}
					else if(entry.getValue().equals("STPresSensor"))
					{
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].currentPresence = PRESENT;");
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].presenceState.value = PRESENT;");
					}
					else if(entry.getValue().equals("STLock"))
					{
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].currentLock = LOCKED;");
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].lockState.value = LOCKED;");
					}
					else if(entry.getValue().equals("STContactSensor"))
					{
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].currentContact = CLOSED;");
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].contactState.value = CLOSED;");
					}
					else if(entry.getValue().equals("STThermostat"))
					{
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].currentThermostatFanMode = ON;");
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].thermostatFanModeState.value = ON;");
						
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].currentThermostatMode = AUTO;");
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].thermostatModeState.value = AUTO;");
					}
					else if(entry.getValue().equals("STPowerMeter"))
					{
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].currentPower = 1;");
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].powerState.value = 1;");
					}
					else if(entry.getValue().equals("STWaterSensor"))
					{
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].currentWater = DRY;");
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].waterState.value = DRY;");
					}
					else if(entry.getValue().equals("STValve"))
					{
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].currentValve = OPEN;");
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].valveState.value = OPEN;");
					}
					else if(entry.getValue().equals("STAccSensor"))
					{
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].currentAcceleration = INACTIVE;");
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].accelerationState.value = INACTIVE;");
					}
					else if(entry.getValue().equals("STAlarm"))
					{
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].currentAlarm = OFF;");
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].alarmState.value = OFF;");
					}
					else if(entry.getValue().equals("STSmokeDetector"))
					{
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].currentSmoke = CLEAR;");
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].smokeState.value = CLEAR;");
					}
					else if(entry.getValue().equals("STCarMoDetector"))
					{
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].currentCarbonMonoxide = CLEAR;");
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].carbonMonoxideState.value = CLEAR;");
					}
					else if(entry.getValue().equals("STAeonKeyFob"))
					{
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].currentButton = CLEAR;");
						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].buttonState.value = CLEAR;");
					}
//					else if(entry.getValue().equals("STSwitchLevel"))
//					{
//						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].currentLevel = 1;");
//						println("\t\t_g_" + deviceType + "Arr.element[" + deviceName + "].levelState.value = 1;");
//					}
					
					/* Common behaviors */
					println("\t\t_g_" + deviceType + "Arr.length++;");
					println("\t\tgDeviceId++;");
				}
				println();
			}
			/* Initialize device configuration */
			for(Map.Entry<String, GSmartAppConfigInfo> smartAppConfig : GConfigInfoManager.getSmartAppConfigInfoMap().entrySet())
			{
				String smartAppName = smartAppConfig.getKey();
				
				if(smartAppNames.contains(smartAppName))
				{
					java.util.List<String> subscribedDevices = GConfigInfoManager.getSubscribedDevices(smartAppName);
							
					println("\t\t/* Initialization for " + smartAppName + " */");
					if(smartAppConfig.getValue().deviceConfigInfoList != null)
					{
						for(GDeviceConfigInfo dci : smartAppConfig.getValue().deviceConfigInfoList)
						{
							if(dci.isMultiple)
							{
								int index = 0;
								
								/* Initialize lengths of array variables */
								println("\t\t" + dci.inputName + ".length = " + dci.configDevices.size() + ";");
								
								for(String deviceName : dci.configDevices)
								{
									/* frontDoorSensor_STMotionSensor */
									String name = SpinUtil.getGlobalArrRef(deviceName, dci.deviceType);
	//								String broadcastChanRef = SpinUtil.getGlobalArrRefBroadcastChans(deviceName, dci.deviceType);
									String numbSubscriber = SpinUtil.getGlobalArrRefNumSubscribers(deviceName, dci.deviceType);
									
									println("\t\tassign_" + dci.deviceType + 
											"_rec(" + dci.inputName + ".element[" + index + "], " + name + ");");
									if(subscribedDevices.contains(dci.inputName))
									{
	//									println("\t\t" + dci.inputName + ".element[" + index + "].BroadcastChanIndex = " 
	//											+ broadcastChanRef + ";");
										println("\t\t" + dci.inputName + ".element[" + index + "].BroadcastChanIndex = " 
												+ numbSubscriber + ";");
										println("\t\t" + numbSubscriber + "++;");
									}
									index++;
								}
							}
							else
							{
								String name = SpinUtil.getGlobalArrRef(dci.configDevices.get(0), dci.deviceType);
	//							String broadcastChanRef = SpinUtil.getGlobalArrRefBroadcastChans(dci.configDevices.get(0), dci.deviceType);
								String numbSubscriber = SpinUtil.getGlobalArrRefNumSubscribers(dci.configDevices.get(0), dci.deviceType);
								
								println("\t\tassign_" + dci.deviceType + 
										"_rec(" + dci.inputName + ", " + name + ");");
								if(subscribedDevices.contains(dci.inputName))
								{
	//								println("\t\t" + dci.inputName + ".BroadcastChanIndex = " 
	//										+ broadcastChanRef + ";");
									println("\t\t" + dci.inputName + ".BroadcastChanIndex = " 
											+ numbSubscriber + ";");
									println("\t\t" + numbSubscriber + "++;");
								}
							}
						}
					}
					
					/* Print out phone number assignment */
	//				{
	//					GPhoneConfigInfo phoneInfo = GConfigInfoManager.getPhoneConfigInfo(smartAppName);
	//					
	//					if(phoneInfo != null)
	//					{
	//						println("\t\t/* Configured phone number assignment */");
	//						println("\t\t" + phoneInfo.inputName + ".length = " + phoneInfo.configuredPhoneNumber.size() + ";");
	//						for(int i = 0; i < phoneInfo.configuredPhoneNumber.size(); i++)
	//						{
	//							println("\t\t" + phoneInfo.inputName + ".element[" + i + "] = " + 
	//										phoneInfo.configuredPhoneNumber.get(i) + ";");
	//						}
	//					}
	//				}
					
					if(GConfigInfoManager.isSubscribedToLocation(smartAppName))
					{
						println("\t\t/* Broadcast channel index of location assignment */");
						println("\t\t" + smartAppName + "_location = " + "location.NumSubscribers;");
						println("\t\tlocation.NumSubscribers++;");
					}
					
					/* Print out other configuration info assignment */
					println("\t\t/* Other input info assignment */");
					if(GConfigInfoManager.getOtherConfigInfoList(smartAppName) != null)
					{
						for(GOtherConfigInfo otherInfo : GConfigInfoManager.getOtherConfigInfoList(smartAppName))
						{
							if(otherInfo.isMultiple) 
							{
								println("\t\t" + otherInfo.inputName + ".length = " + otherInfo.values.size() + ";");
								for(int i = 0; i < otherInfo.values.size(); i++)
								{
									println("\t\t" + otherInfo.inputName + ".element[" + i + "] = " + otherInfo.values.get(i) + ";");
								}
							}
							else 
							{
								println("\t\t" + otherInfo.inputName + " = " + otherInfo.values.get(0) + ";");
							}
						}
					}
					{
						GStateMapEnum stateMapType = GConfigInfoManager.getStateMapUsed(smartAppName);
						
						if(stateMapType != GStateMapEnum.unknown)
						{
							if(stateMapType == GStateMapEnum.Int2IntMap)
							{
								println("\t\tallocateCInt2IntMapArrIndex(" + smartAppName + "_state);");
							}
							else if(stateMapType == GStateMapEnum.Int2IIMMap)
							{
								println("\t\tallocateCInt2IIMMapArrIndex(" + smartAppName + "_state);");
							}
							else if(stateMapType == GStateMapEnum.Int2IIIMMap)
							{
								println("\t\tallocateCInt2IIIMMapArrIndex(" + smartAppName + "_state);");
							}
						}
					}
					/* Initialization for httpGet variables */
					{
						Map<String, List<String>> httpGet2VarRangeMap = GConfigInfoManager.getHttpGet2ValueRangeMap(smartAppName);
						
						if(httpGet2VarRangeMap != null)
						{
							if(httpGet2VarRangeMap.size() > 0)
							{
								List<String> additonalRangeList = GConfigInfoManager.getAdditionalIntHttpGetVarRangeList(smartAppName);
								
								println("\t\t/*Initialization for httpGet variables */");
								
								for(Map.Entry<String, List<String>> entry : httpGet2VarRangeMap.entrySet())
								{
									List<String> valRangeList = entry.getValue();
									String varName = entry.getKey();
									
									println("\t\tif");
									if(valRangeList.size() == 0)
									{
										for(String addVal : additonalRangeList)
										{
											if(!valRangeList.contains(addVal))
											{
												println("\t\t:: " + varName + " = " + addVal + ";");
											}
										}
										println("\t\t:: " + varName + " = 0;");
										println("\t\t:: " + varName + " = 1;");
									}
									else
									{
										for(String value : valRangeList)
										{
											println("\t\t:: " + varName + " = " + value + ";");
										}
										if(!valRangeList.contains("true") && !valRangeList.contains("false"))
										{
											for(String addVal : additonalRangeList)
											{
												if(!valRangeList.contains(addVal))
												{
													println("\t\t:: " + varName + " = " + addVal + ";");
												}
											}
										}
									}
									println("\t\tfi");
								}
							}
						}
					}
					println();
				}
			}
		}
		
		/* Start all of the processes */
		{
			println("\t\t/* Start all of the processes */");
			/* Print out the system's processes */
			println("\t\trun SmartThings();");
//			println("\t\trun DeviceManager();");
//			println("\t\trun SmartAppManager();");
//			println("\t\trun LocationManager();");
			
			/* Start device managers */
//			{
//				Map<String, String> deviceList = GConfigInfoManager.getDeviceListFromConfigInfo();
//				
//				for(Map.Entry<String, String> entry : deviceList.entrySet())
//				{
//					String deviceType = entry.getValue();
//					
//					/* Check if deviceType is an actuator device */
//					if(SpinUtil.isADevice(deviceType + "_rec") && !SpinUtil.isASensorDevice(deviceType)
//							&& !deviceType.equals("location") && !deviceType.equals("app"))
//					{
//						println("\t\trun " + deviceType + "Manager(" + entry.getKey() + ");");
//					}
//				}
//			}
//			println("\t\trun NetworkManager();");
//			println("\t\trun Watchdog();");
//			println();
			
//			/* Print out processes of smart apps */
//			for(String smartAppName : smartApps)
//			{
//				println("\t\trun " + smartAppName + "();");
//			}
		}
		
		println("\t}");
		println("}");
		indentLevel = 0;
	}  


	/**
	 * Test if the out transitions of a location model an 'if' branch,
	 * i.e., there are exactly two transitions, and one guard is
	 * the negation of the other.
	 */
	boolean isIfBranch(TransVector outTrans) {
		if (outTrans.size() != 2)
			return false;
		Expr guard1 = outTrans.elementAt(0).getGuard();
		Expr guard2 = outTrans.elementAt(1).getGuard();
		if (guard1 == null || guard2 == null)
			return false;
		if ((guard1 instanceof NotExpr) &&
				((NotExpr) guard1).getOp().equals(guard2))
			return true;
		if ((guard2 instanceof NotExpr) &&
				((NotExpr) guard2).getOp().equals(guard1))
			return true;
		return false;
	}  
	public boolean isVerbose() { return isVerbose; }
	/**
	 * Get PROMELA label for BIR location.
	 * <p>
	 * If location has no out transitions (i.e., is end location of thread),
	 * make the label an end-label (does not count for deadlock).
	 */
	static String locLabel(Location loc) {
		//      if (loc.getOutTrans().size() == 0)
		//        return "endloc_" + loc.getId();
		//      else
		return "loc_" + loc.getId();
	}  


	/**
	 * Get PROMELA label of location encountered during coarsening.
	 * <p>
	 * Since we may encounter the same location along several
	 * collapsed paths, we must have a unique label for each.
	 */
	static String locLabel(Location loc1, int branch, Location loc2) {
		return "loc_" + loc1.getId() + "_" + branch + "_" + loc2.getId();
	}  


	/**
	 * Test if an expression is statically nonnegative (either a constant
	 * or in a range type that is nonnegative).
	 */
	boolean nonNegative(Expr expr) {
		if (expr instanceof ConstExpr)
			return ((ConstExpr) expr).getValue() >= 0;
			if (expr.getType() instanceof Range) {
				Range type = (Range) expr.getType();
				return (type.getFromVal().getValue() >= 0);
			}
			return false;
	}  


	static int parseInt(String s) {
		try {
			int result = Integer.parseInt(s);
			return result;
		} catch (NumberFormatException e) {
			throw new RuntimeException("Integer didn't parse: " + s);
		}
	}  

	/**
	 * Parse the output of 'pan' and interpret it as either a violation
	 * (sequence of transformations) or as a error-free analysis.
	 * <p>
	 * @return a vector of transformations (if there was an error found), or null (otherwise)
	 */
	public BirTrace parseOutput() {
		return new BirTrace(system, getCounterExample());
	}          

	/**
	 * Generate the predicate definitions.
	 * <p>
	 * Note that these are at the bottom so they can refer to
	 * any variable or code location.
	 */
	void predicates() {
		evaluatingPredicate = true;
		println("/* Predicates */");
		Vector preds = system.getPredicates();
		for (int i = 0; i < preds.size(); i++) {
			Expr pred = (Expr) preds.elementAt(i);
			String name = system.predicateName(pred);
			inDefine = true;
			println("#define " + name + " ");
			indentLevel = 1;
			// Print the PROMELA expression
			printGuard(applyTo(pred).getCase(normal));
			inDefine = false;
			println("");
			indentLevel = 0;
		}
		println("");
		evaluatingPredicate = false;
	}  


	/**
	 * Support for printing things nicely indented.
	 */
	void print(String s) {
		if (newLine) { // at start of line---indent
			for (int i = 0; i < indentLevel; i++)
				out.print("   ");
			newLine = false;
		}
		out.print(s);
	}  


	/**
	 * Print a case tree as an expression to represent a guard.
	 * <p>
	 * We take advantage of the short-curcuit evaluation in PROMELA
	 * to express a case tree as one large boolean expression.
	 * A case node:
	 * <pre>
	 * (cond1 => expr1, cond2 => expr2, ...)
	 * </pre>
	 * is translated as
	 * <pre>
	 * ((cond1 && expr1) || (cond2 && expr2) || ... )
	 * </pre>
	 * We consider traps as always enabled, so they evaluate to
	 * the constant TRAP (#defined to be 1).
	 */
	void printGuard(TreeNode tree) {
		if (tree instanceof ExprNode) {
			ExprNode node = (ExprNode) tree;
			print(node.expr1);
		} else if (tree instanceof TrapNode) {
			TrapNode node = (TrapNode) tree;
			//		    print("TRAP");Thomas
		} else {
			CaseNode node = (CaseNode) tree;
			print("(  ");
			for (int i = 0; i < node.size(); i++) {
				Case c = node.elementAt(i);
				indentLevel++;
				println("(  " + c.cond);
				print("&& ");
				indentLevel++;
				printGuard(c.node);
				indentLevel--;
				print(" )");
				indentLevel--;
				if (i < node.size() - 1) {
					println();
					print("|| ");
				}
			}
			println(" )");
		}
	}  


	void println() {
		if (inDefine)
			out.print("\\"); // must use line continuation in #define
		out.println();
		newLine = true;
	}  


	void println(String s) {
		print(s);
		println();
	}  


	/**
	 * Print a case tree as a statement.
	 * <p>
	 * A case node:
	 * <pre>
	 * (cond1 => expr1, cond2 => expr2, ..., condN => exprN)
	 * </pre>
	 * is translated as
	 * <pre>
	 * if
	 * :: cond1 -> expr1
	 * :: cond2 -> expr2
	 * ...
	 * else -> exprN
	 * fi
	 * </pre>
	 */
	void printStatement(TreeNode tree) {
		if (tree instanceof ExprNode) {
			ExprNode node = (ExprNode) tree;
			println(node.expr1);
		} else if (tree instanceof TrapNode) {
			TrapNode node = (TrapNode) tree;
			/*print("   printf(\"BIR: %d " + actionId() + " " + node.desc +
		  "\\n\", _pid); ");*/
			// If fatal trap, flag error, else just branch to end label
			if (node.fatal)
				println("assert(0);");
			else
				println("goto done;");
		} else {
			CaseNode node = (CaseNode) tree;
			println("if ");
			for (int i = 0; i < node.size(); i++) {
				Case c = node.elementAt(i);
				// Use "else" for last alternative
				String cond = (i < node.size() - 1) ? c.cond : "else";
				println(":: " + cond + " -> ");
				indentLevel++;
				printStatement(c.node);
				indentLevel--;
			}
			println("fi; ");
		}
	}  


	/**
	 * Generate the #include for the never claim
	 */
	void property() {
		if (options.getApplyNeverClaim())
			println("#include \"" + system.getName() + ".nvr\"");
	}  


	/**
	 * Return the refIndex of a state variable (it's index in the
	 * universal reference type refAny).
	 */
	int refIndex(StateVar target) {
		return 1 + system.refAnyType().getTargets().indexOf(target);
	}  


	/**
	 * Reset any variables that have become dead between locations.
	 * <p>
	 * In particular, we reset a variable if it was live at fromLoc and
	 * is not live at toLoc.
	 */
	void resetDeadVariables(Location fromLoc, Location toLoc) {
		StateVarVector liveBefore = fromLoc.getLiveVars();
		StateVarVector liveAfter = toLoc.getLiveVars();
		if (liveBefore != null && liveAfter != null)
		{
			for (int i = 0; i < liveBefore.size(); i++)
			{
				if (liveAfter.indexOf(liveBefore.elementAt(i)) == -1) {
					/* [Thomas, May 2nd, 2017]
					 * We will not reset ref type
					 * */
					// var was live, now is not---reset it
					StateVar var = liveBefore.elementAt(i);
					if(!var.getType().isKind(RECORD | ARRAY | COLLECTION | REF))
					{
						var.apply(this);
						print(getSimpleResult() + " = ");
						var.getInitVal().apply(this);
						println(getSimpleResult() + ";");
					}
				}
			}
		}
	}  


	/**
	 * Run translator: generate header, definitions, state variables,
	 * transitions (processes), predicates, and the LTL property.
	 */
	SpinTrans run() {
		if(GConfigInfoManager.isStateMapUsed())
		{
			GUtil.setMaxIntArraySize(10);
		}
//		GConfigInfoManager.createTestData();

		declareMacros();
		//		header();
		definitions();
		//		subtypeDecl();
		declareDevices();
		stateVariables();
		declareAssignMethods();
//		declareSystemTimeManager();
		transitions();
		declareSTUtilities();
		declareLocationEvtHandler();
		declareNetworkEvtHandler();
		declareActuatorDeviceEvtHandlers();
		declareSensorDeviceEvtHandlers();
//		declareDeviceManager();
//		declareDeviceManagers();
//		declareSmartAppManager();
		declareSmartThings();
		
		//		predicates();
		//		property();
//		declareWatchdog();
		initBlock();
		return this;
	}  

	public String check() {
		String systemName = system.getName();
		String promFilename = systemName + ".prom";
		String trailFilename = promFilename + ".trail";
		String ln = System.getProperty("line.separator");

		if (options.getApplyNeverClaim()) {
			// Translate LTL into never claim
			String ltlFilename = systemName + ".ltl";
			File ltlFile = new File(ltlFilename);
			if (!ltlFile.exists()) {
				throw new RuntimeException("Cannot find LTL file: " + ltlFile.getAbsolutePath());
			}

			try {
				NeverClaimTranslator nct = NeverClaimTranslatorFactory.createNeverClaimTranslator();
				nct.translate(ltlFile, new File(systemName + ".nvr"));
			}
			catch(IOException ioe) {
				log.error("An exception occured while producing the never claim.", ioe);
				throw new RuntimeException("An exception occured while producing the never claim.");
			}

			/*
	    try {
		boolean haveJPF = false;
		try {
		    String jpfHomeDir = new File(Class.forName("gov.nasa.arc.ase.jpf.jvm.Main").getResource("Main.class").getFile()).getParentFile().getParentFile().getParentFile().getParentFile().getParentFile().getParentFile().getParent();
		    if (jpfHomeDir != null) haveJPF = true;
		}
		catch (Exception exx) {
		    log.error("An Exception occured while checking to see if JPF exists.  This probably means it does not.", exx);
		}

		if (haveJPF) {
			gov.nasa.arc.ase.util.graph.Graph ba = gov.nasa.arc.ase.ltl.LTL2Buchi.translate(new File(ltlFilename));
			ba.save(systemName+".nvr", gov.nasa.arc.ase.util.graph.Graph.SPIN_FORMAT);
		}
		else {
		    // Alright, so we don't have JPF, so we can't use JPF's
		    // LTL2Buchi translator
		    log.warn("Using spin -f to generate the never claim." +
			     "However, spin -f is not reliable to do so since there are known bugs (as of version 3.4.16)!");
		    String neverClaim = BanderaUtil.exec(Preferences.getSpinAlias() + " -F " + ltlFilename, true);
		    PrintWriter pw = new PrintWriter(new FileWriter(systemName + ".nvr"));
		    pw.print(neverClaim);
		    pw.flush();
		    pw.close();
		}
	    }
	    catch (Exception e) {
		log.error("An Exception occured while producing the never claim. file = " + systemName + ".nvr", e);
		throw new RuntimeException("An Exception occured while producing the never claim. file = " + systemName + ".nvr", e);
	    }
			 */
		}

		File sourceFile = new File(promFilename);
		if (!sourceFile.exists()) {
			throw new RuntimeException("Cannot find SPIN source file: " + sourceFile.getAbsolutePath());
		}
		new File(trailFilename).delete(); // Remove previous trail filename if it exists
		new File("pan.exe").delete(); // Remove pan.exe if it exists

		/* // Execute cpp
	   execAndWait("cpp -P " + promFilename + " tmp", true);
	   File promFile = new File(promFilename);
	   if (!promFile.delete() || !new File("tmp").renameTo(promFile))
	   throw new RuntimeException("Could not rename tmp to " + promFilename);
		 */

		StringBuffer output = new StringBuffer();

		try {
			output.append(BanderaUtil.exec(Preferences.getSpinAlias() + " -a " + promFilename, isVerbose));
			log.info("Using  command <" +Preferences.getSpinAlias() + "> to run spin.");
			File panFile = new File("pan.c");
			if (!panFile.exists()) {
				throw new RuntimeException("The pan.c @ "+panFile.getAbsolutePath() +" file was not created." );
			}
		}
		catch (Exception e) {
			cleanMess();
			throw new RuntimeException("An error occured while creating the pan.c file using spin -a.", e);
		}

		try {
			/* IMPORTANT: You have to turn OFF warning in gcc (with -w switch)
			 * Because if gcc ever spits off warnings, it will lock up Bandera.
			 * The only indicator that the compilation succeed or not is through
			 * the existence of pan.exe  (robbyjo)
			 */
			output.append(BanderaUtil.exec(Preferences.getCCAlias() + " "+ Preferences.getCCWarningsFlag() +" "+ options.getCompilerCommandLineOptions() + " "+ Preferences.getCCOutputFileFlag() +" pan.exe pan.c", isVerbose));
			log.info("Using  command <" +Preferences.getCCAlias() + "> for c compiler.");
			if (!(new File("pan.exe")).exists()) {
				throw new RuntimeException("The pan.c file could not be compiled into pan.exe.");
			}
		} catch (Exception e) {
			cleanMess();
			throw new RuntimeException("An error occured while compiling pan.c.", e);
		}

		String panOutput;
		try {

			// if this is a unix-like os (has chmod), change the persmissions of pan.exe
			if(PlatformInformation.isUnixOSFamily()) {
				try {
					// check to see if it is executable.
					String t = BanderaUtil.exec("ls -l pan.exe", isVerbose);
					log.debug("Output of ls -l pan.exe: " + t);
					String permissions = t.substring(0, 10);
					log.debug("permissions = " + permissions);
					if(permissions.charAt(3) != 'x') {
						log.debug("pan.exe is not executable by the user.");
						// change the permissions so we can run it.
						String trash = BanderaUtil.exec("chmod u+x pan.exe", isVerbose);
						log.debug("Output of chmod command: " + trash);
					}
					else {
						log.debug("pan.exe is executable by the user.");
					}
				}
				catch(Exception e) {
					log.warn("An exception occured while attempting to change the file permissions on pan.exe.", e);
				}
			}


			// Return output of 'pan'
			String panExecutable = "pan.exe";
			if(PlatformInformation.isUnixOSFamily()) {
				// this is a hack just in case the default environment PATH doesn't include the current dir (.).
				panExecutable = "./pan.exe";
			}
			panOutput = BanderaUtil.exec(panExecutable + " " + options.getPanCommandLineOptions(), isVerbose);
			output.append(panOutput);
		}
		catch (Exception e) {
			try {
				cleanMess();
			}
			catch (Exception exx){
				// ignored exception
			}
			throw new RuntimeException("An error occured while running pan.exe.", e);
		}

		// we will need parts of the pan output when we generate the
		// counter example.  therefore, we will grab some lines of the output
		// if necessary.  those will be stored into the panOutputList and then
		// used in getCounterExample().
		if(panOutputList == null) {
			panOutputList = new ArrayList(3);
		}
		else {
			panOutputList.clear();
		}

		if (panOutput.indexOf("error: max search depth too small") >= 0) {
			throw new RuntimeException("Verifier search depth exceeded!");
		} else if (panOutput.indexOf("pan: out of memory") >= 0) {
			throw new RuntimeException("Verifier ran out of memory!");
		} else if (panOutput.indexOf("error: VECTORSZ is too small") >= 0) {
			throw new RuntimeException("Verifier vector exceeded!");
		} else if (panOutput.indexOf("Search not completed") >= 0 && panOutput.indexOf("pan: wrote") < 0) {
			// Search not complete, but it doesn't write any counter example trail
			throw new RuntimeException("Verifier cannot complete its run: " + System.getProperty("line.separator") + panOutput);
		} else if (panOutput.indexOf("pan: acceptance cycle") >= 0) {
			log.debug("This run has failed due to a liveness property violation.");
			panOutputList.add("pan: acceptance cycle");
		} else if (panOutput.indexOf("pan: claim violated") >= 0) {
			log.debug("This run has failed due to a property violation.");
			panOutputList.add("pan: claim violated");
		} else if (panOutput.indexOf("pan: assertion violated") >= 0) {
			log.debug("This run has failed due to an assertion violation.");
			panOutputList.add("pan: assertion violated");
		} else if (panOutput.indexOf("pan: invalid endstate") >= 0) {
			log.debug("This run has failed due to a deadlock.");
			panOutputList.add("pan: invalid endstate");
		}

		/*
	  try {
	  BanderaUtil.moveFile(trailFilename, outputDir);
	  } catch (Exception e)
	  {
	  throw new RuntimeException("Cannot move the counter example trail into the output directory!");
	  }
		 */
		return output.toString();
	}

	public void cleanMess() {
		// Sweep
		String systemName = system.getName();
		String ltlFilename = systemName + ".ltl";
		String nvrFilename = systemName + ".nvr";
		String promFilename = systemName + ".prom";
		String birFilename = systemName + ".bir";
		String trailFilename = promFilename + ".trail";

		if (!".".equals(outputDir) && !("."+File.separator).equals(outputDir))
		{
			new File(outputDir+birFilename).delete();
			new File(outputDir+ltlFilename).delete();
			new File(outputDir+nvrFilename).delete();
			new File(outputDir+promFilename).delete();
			new File(outputDir+trailFilename).delete();
			new File(outputDir+"pan.exe").delete();
			new File(outputDir+"pan.b").delete();
			new File(outputDir+"pan.c").delete();
			new File(outputDir+"pan.h").delete();
			new File(outputDir+"pan.m").delete();
			new File(outputDir+"pan.t").delete();
		}
		BanderaUtil.moveFile(birFilename, outputDir);
		BanderaUtil.moveFile(ltlFilename, outputDir);
		BanderaUtil.moveFile(nvrFilename, outputDir);
		BanderaUtil.moveFile(promFilename, outputDir);
		BanderaUtil.moveFile(trailFilename, outputDir);
		BanderaUtil.moveFile("pan.exe", outputDir);
		BanderaUtil.moveFile("pan.b", outputDir);
		BanderaUtil.moveFile("pan.c", outputDir);
		BanderaUtil.moveFile("pan.h", outputDir);
		BanderaUtil.moveFile("pan.m", outputDir);
		BanderaUtil.moveFile("pan.t", outputDir);
	}

	/**
	 * getCounterExample method comment.
	 */
	public List getCounterExample() {
		String systemName = system.getName();
		String baseName = systemName;
		String promFilename = baseName + ".prom";
		String trailFilename = promFilename + ".trail";
		File trail = new File(trailFilename);

		/*
		 * HACK: This is a hack put in to work around the new Spin 4.0.1 file name
		 * system.  For some reason, spin now wants to generate it's tail file into
		 * <name>.prom1.trail instead of <name>.prom.trail.  So, we will just rename
		 * it here.  This bug should be reported to the Spin "folks" and see if this
		 * is on purpose and if it is configurable. -tcw
		 */
		String alternateTrailFilename = baseName + ".prom1.trail";
		File alternateTrailFile = new File(alternateTrailFilename);
		if(alternateTrailFile.exists()) {
			log.debug("Found the alternate trail file: " + alternateTrailFilename);

			// move it to the other file name
			boolean success = alternateTrailFile.renameTo(trail);
			if(success) {
				log.debug("Renamed the alternate trail file.");
			}
			else {
				log.debug("Could not rename the alternate trail file.");
			}
		}
		// END HACK

		if (!trail.exists()) {
			cleanMess();
			return null;
			/*	     trail = new File(outputDir+"."+systemName+".prom.trail");
		     if (!trail.exists()) return null;
		     trail.renameTo(new File(trailFilename));
			 */
		}

		String output = BanderaUtil.exec(Preferences.getSpinAlias() + " -t " + promFilename, true);
		log.info("Using command <" +Preferences.getSpinAlias() + "> to run spin.");
		BufferedReader reader = new BufferedReader(new StringReader(output));
		//LinkedList ll = new LinkedList();
		LinkedList ll = new LinkedList(panOutputList); // start with the pan output
		String ln = System.getProperty("line.separator");
		try {
			String line;
			do {
				line = reader.readLine();
				if(line != null) {
					line = line.trim();
					if((line.startsWith("BIR")) || (line.startsWith("pan"))) {
						ll.add(line);
					}
				}
			} while (line != null);
		}
		catch (Exception e) {
			log.error("Exception while reading the spin trail output.", e);
		}

		cleanMess();
		return ll;
	}

	/**
	 * Run SPIN to generate an analyzer (pan), and then run the analyzer.
	 * <p>
	 * The translator must have been executed for the transition system
	 * (the file NAME.prom must exist and contain the translated PROMELA).
	 * @return the output of 'pan' (as a String)
	 */
	public String runSpin() {
		return(check());
	}  

	public void setVerbose(boolean v) {
		isVerbose = v;
	}

	/**
	 * Build a trivial case tree with one ExprNode holding the string.
	 */
	TreeNode specialize(String expr) {
		return new CaseNode(new Case(normal, new ExprNode(expr)));
	}  

	/**
	 * Create a spin declaration for this variable.
	 *
	 * @param StateVar var The variable whose declaration should be generated.
	 * @return String The String that holds a Spin declaration for this variable.
	 */
	private String createVarDeclaration(StateVar var) {

		if(var == null) {
			log.error("The StateVar given is null.  We cannot create a declaration for a null StateVar.");
			return("");
		}

		var.getType().apply(namer, null);
		String refIndex = "   ";
		if(var.getType().isKind(RECORD | ARRAY | COLLECTION)) {
			refIndex += "/*  ref index = " + refIndex(var) + " */";
		}
		String varDeclaration = namer.getResult() + " " +
				var.getName() + ";" + refIndex;

		return(varDeclaration);
	}

	/**
	 * Create a spin declaration for this variable such that it is a
	 * global array instead of a local variable.  This will also make use of
	 * the BIR bounds for thread instances to bound the array.
	 *
	 * @param StateVar var The variable whose declaration should be generated.
	 * @return String The String that holds a Spin declaraion for this variable.
	 */
	private String createGlobalLocalVarDeclaration(StateVar var) {

		if(var == null) {
			log.error("The StateVar given is null.  We cannot create a global local declaration for a null StateVar.");
			return("");
		}

		var.getType().apply(namer, null);
		BirThread birThread = var.getThread();
		if(birThread == null) {
			log.error("The BirThread associated with this var (" +
					var.toString() + ") is null.  This should never be true!");
			return("");
		}
		String threadName = birThread.getName();
		int arraySize = getMaxThreadInstances(threadName);
		String varDeclaration = namer.getResult() + " " + threadName + "_" + var.getName() + "[" + arraySize + "];";
		return(varDeclaration);

	}

	private int getMaxThreadInstances(String threadName) {
		int threadInstances = 1;

		try {
			SessionManager sm = SessionManager.getInstance();
			Session activeSession = sm.getActiveSession();
			BIROptions bo = activeSession.getBIROptions();
			ResourceBounds rb = bo.getResourceBounds();
			threadInstances = rb.getThreadMax(threadName) + 1; // add one for the pid of the init process.
		}
		catch(Exception e) {
			log.error("An exception occured while retrieving the max thread instances for thread " + threadName, e);
		}

		return(threadInstances);
	}

	/**
	 * Declare the global locals that are associated with this thread.  A global local
	 * is a variable that is accessed outside it's scope (within the proc).  This is
	 * most likely when referenced in a predicate.
	 *
	 * @param BirThread birThread The thread show global locals should be printed.
	 */
	private void declareGlobalLocals(BirThread birThread) {

		if(birThread == null) {
			log.error("The BirThread given is null.  We cannot print the global locals for a null thread.");
			return;
		}

		StateVarVector vars = birThread.getLocals();
		if((vars != null) && (vars.size() > 0)) {
			for(int i = 0; i < vars.size(); i++) {
				StateVar var = vars.elementAt(i);
				if((var != null) && (var.isGlobalLocal())) {
					String varDeclaration = createGlobalLocalVarDeclaration(var);
					println(varDeclaration);
				}
			}
		}
	}

	/**
	 * Declare the locals associated with the given BirThread.
	 *
	 * @param BirThread birThread The thread whose locals should be printed.
	 */
	private void declareVars(BirThread birThread, boolean printIntType) {
		if(birThread == null) {
			log.error("The BirThread given is null.  We cannot print the locals for a null thread.");
			return;
		}

		StateVarVector vars = birThread.getLocals();
		if((vars != null) && (vars.size() > 0)) {
			if(printIntType)
			{
				indentLevel = 1;
				for(int i = 0; i < vars.size(); i++) {
					StateVar var = vars.elementAt(i);
					if((var != null) && (var.getIsLocal())) {
						String varDeclaration = createVarDeclaration(var);
						
						if(varDeclaration.startsWith(intTypeName) ||
								varDeclaration.startsWith(boolTypeName))
						{
							println(varDeclaration);
						}
					}
				}
			}
			else
			{
				indentLevel = 0;
				for(int i = 0; i < vars.size(); i++) {
					StateVar var = vars.elementAt(i);
					if((var != null) && (var.getIsLocal())) {
						String varDeclaration = createVarDeclaration(var);
						
						if(!varDeclaration.startsWith(intTypeName) &&
								!varDeclaration.startsWith(boolTypeName))
						{
							println("hidden " + varDeclaration);
						}
					}
				}
			}
		}
		else {
			/* report no locals! */
			log.debug("There are no locals for this thread (" + birThread.getName() + ").");
		}
	}
	
	/* [Thomas, June 29, 2017]
	 * */
	private void declareVarsInitialization(BirThread birThread) {
		if(birThread == null) {
			log.error("The BirThread given is null.  We cannot print the locals for a null thread.");
			return;
		}

		StateVarVector vars = birThread.getLocals();
		if((vars != null) && (vars.size() > 0)) {
			indentLevel = 1;
			
			println("STEvent_rec _ST_Command;");
			println();
			
			println("/* Default initialization */");
			println("d_step {");
			indentLevel++;
			for(int i = 0; i < vars.size(); i++) {
				StateVar var = vars.elementAt(i);
				if((var != null) && 
						(var.getIsLocal() || 
						var.getName().startsWith("_STNetworkManager.receivedPhoneNumber")
						|| var.getName().startsWith("_STNetworkManager.sentMessage"))) {
					if (var.getType().isKind(REF))
					{
						if(((Ref)var.getType()).getTargetType().isKind(ARRAY))
						{
							println(var.getName() + ".length = 0;");
						}
						else if(((Ref)var.getType()).getTargetType().isKind(RECORD))
						{
							println(var.getName() + ".isAlive = 0;");
						}
					}
				}
			}
			
			println("_ST_Command.id = " + GConfigInfoManager.getEvtVarName(birThread.getSootClassName(), birThread.getName()) + ".id;");
			println("_ST_Command.physical = 0;");
			indentLevel--;
			println("}");	
			indentLevel = 0;
		}
		else
		{
			indentLevel = 1;
			println("skip;");
			println();
			indentLevel = 0;
		}
	}

	/**
	 * Declare the globals.
	 */
	private void declareGlobalVars() {
		StateVarVector vars = system.getStateVars();
		
		if((vars != null) && (vars.size() > 0)) {
			println("/*************** Start of global variables of smart apps *****/");
			for (int i = 0; i < vars.size(); i++) {
				StateVar var = vars.elementAt(i);
				if (var.getIsGlobal()) {
					String varDeclaration = createVarDeclaration(var);

					/* [Thomas, April 29, 2017]
					 * Remove all definitions of global variable of type array,
					 * record, or collection
					 * */
					if(!var.getType().isKind(RECORD | ARRAY | COLLECTION) && 
							!varDeclaration.startsWith("STEvent_rec") && /* Remove additional STEvent variables */
							!varDeclaration.contains("STInitializer_record_") && /* Remove ST initializer variables */
							!varDeclaration.contains("STInitializer_arr_") && /* Remove ST initializer variables */
							!varDeclaration.contains("STCurrentSystemTime") &&
							!varDeclaration.contains("location") &&
							!varDeclaration.contains("_STNetworkManager"))
					{
						if(!varDeclaration.contains(boolTypeName))
						{
							println("hidden " + varDeclaration);
						}
						else
						{
							println(varDeclaration);
						}
					}
				}
			}
			println("/*************** End of global variables of smart apps *******/");
		}
		else {
			log.debug("There are no globals for this system.");
		}
	}


	/**
	 * Declare the state variables.
	 */
	void stateVariables() {
		//		 limit_exeption is set on a BIR limit exception
		//		 _i_ is used as a temporary in the _allocate macro
		//		println("bool limit_exception = 0;");
		//		println("byte _i_ = 0;");
		//	
		//		 For each (non-main) thread, declare channel to block on for starting
		//		 the thread and a boolean indicating whether the thread is active
		//		 ThreadVector threads = system.getThreads();
		//		 for (int i = 0; i < threads.size(); i++) {
		//		  if (! threads.elementAt(i).isMain()) {
		//			println("chan _threadStart_" + i + " = [1] of { bit };");
		//			println("bit _threadActive_" + i + ";");
		//		  }
		//		 }
		//		 println();
		//	
		//		 For the moment we keep the thread status flag globally
		//		/*println("#define THREAD_ACTIVE\t" + THREAD_ACTIVE);
		//		println("#define THREAD_EXITED\t" + THREAD_EXITED);
		//		println("byte _threadActive[" + ((Tid)Type.tidType).maxTid() + "];");*/
		//	
		//		 Global variables
		//		declareVars(null);
		declareGlobalVars();
		println();
	}  


	/**
	 * Generate the threads.
	 * <p>
	 * Each thread is a PROMELA proctype with a label for each
	 * (visible) location and an if-case for each transformation
	 * out of that location.
	 */
	void transitions() {
		ThreadVector threadVector = system.getThreads();
		Set<String> evtHandlers = GPotentialRiskScreener.getCurrentEvtHandlers();
				
		println("/*************** Start of event handler inline methods *******/");
		for (int i = 0; i < threadVector.size(); i++) {
			BirThread thread = threadVector.elementAt(i);
			String evtHandlerName = thread.getName();
			String installedEvtHandlerName;

			// global locals
			declareGlobalLocals(thread);
			
			this.sootClassName = thread.getSootClassName();
			installedEvtHandlerName = this.sootClassName + "_installedEvtHandler";

//			if(evtHandlers.contains(evtHandlerName) || evtHandlerName.equals(installedEvtHandlerName)
//					|| evtHandlerName.equals("STInitializer_STInitializerEvtHandler"))
			if(evtHandlers.contains(evtHandlerName) || evtHandlerName.equals(installedEvtHandlerName))
			{
				int j, size;
				LocVector threadLocVector;
				
				// Local variables
				declareVars(thread, false);
				
				/* Thomas: replace "proctype" with "inline" */
//				if(evtHandlerName.equals(installedEvtHandlerName) || evtHandlerName.equals("STInitializer_STInitializerEvtHandler"))
				if(evtHandlerName.equals(installedEvtHandlerName))
				{
					println("inline " + installedEvtHandlerName + "(evt) {");
				}
				else
				{
					print("inline " + evtHandlerName + "(" + GConfigInfoManager.getEvtVarName(thread.getSootClassName(), evtHandlerName));
					println(") {");
				}
				
//				if(!evtHandlerName.equals("STInitializer_STInitializerEvtHandler"))
//				{
//					declareVarsInitialization(thread);
//				}
				// Local variables with int type
				declareVars(thread, true);
				declareVarsInitialization(thread);

				// Generate label/trans for each location
				threadLocVector = thread.getLocations();
				size = threadLocVector.size();
				if(size == 2)
				{
					println("\tskip;");
					println("}");
				}
				else
				{
					j = 1;
					while (j < size)
					{
						String label;
						TransVector outTrans;
						
						/* [Thomas, May 23, 2017]
						 * */
						if (!threadLocVector.elementAt(j).isVisible()) continue;
						
//						threadLocVector.elementAt(j).setPrinted();
	
						label = locLabel(threadLocVector.elementAt(j));
						println(label + ":");
						indentLevel++;
						outTrans = threadLocVector.elementAt(j).getOutTrans();
						
						if (outTrans.size() == 1)
						{
							Location startLoc;
							int k;
							TransVector prevOutTrans;
							
							println("atomic { ");
							indentLevel ++;
							println("skip;");
							println("d_step {");
							indentLevel ++;
							d_stepOpen = true;
							d_stepSize = 0;
							
							k = j;
//							prevOutTrans = outTrans;
							do
							{
								prevOutTrans = outTrans;
								startLoc = outTrans.elementAt(0).getFromLoc();
								translateSeq(new LocVector(startLoc), outTrans.elementAt(0), 0, false, false);
								threadLocVector.elementAt(k).setPrinted(); /* [August 31, 2017] */
								
								k++;
								if(k < size)
								{
//									prevOutTrans = outTrans;
									outTrans = threadLocVector.elementAt(k).getOutTrans();
								}
								else
								{
									break;
								}
							}
							while((outTrans.size() == 1) && 
									(prevOutTrans.elementAt(0).getToLoc().getId() == threadLocVector.elementAt(k).getId())
									&& !SpinUtil.isLocReferred(threadLocVector, threadLocVector.elementAt(k).getId()));
							if(d_stepOpen)
							{
								if(d_stepSize == 0)
								{
									println("skip;");
								}
								
								d_stepOpen = false;
								indentLevel --;
								println("}");
								println("skip;");
							}
							println("goto " + locLabel(prevOutTrans.elementAt(0).getToLoc()) + ";");
								
							indentLevel --;
							println("}");
							
							if((k == size) && !threadLocVector.elementAt(k-1).isPrinted())
							{
								j = k-1;
							}
							else
							{
								j = k;
							}
						}
						else if (outTrans.size() == 0) {
							println("goto done;");
							j++;
						} else {
							println("if");
							for (int k = 0; k < outTrans.size(); k++) {
								print(":: ");
								indentLevel++;
								{
									Location startLoc = outTrans.elementAt(k).getFromLoc();
									println("atomic { ");
									indentLevel ++;
									translateSeq(new LocVector(startLoc), outTrans.elementAt(k), k, false, true);
									indentLevel --;
									println("}");
								}
								indentLevel--;
							}
							println("fi;");
							j++;
						}
						indentLevel--;
					}
	
					// Special end location (accepting)
					println("done: skip;");
					println("}");
					if(i < threadVector.size()-1)
					{
						println();
					}
				}
			}
		}
		println("/*************** End of event handler inline methods *********/");
		println();
		//		initBlock();
	}  


	/**
	 * Generate PROMELA source representing a transition system.
	 * <p>
	 * As above, but with default options.
	 * @param system the transition system
	 * @return the SpinTrans control object
	 */
	public static SpinTrans translate(TransSystem system) {
		return translate(system, null);
	}  


	/**
	 * Generate PROMELA source representing a transition system.
	 * <p>
	 * The PROMELA is written to a file NAME.prom where NAME
	 * is the name of the transition system.  A set of
	 * options can be provided in a SpinOptions object.
	 * @param system the transition system
	 * @param options the SPIN verifier options
	 * @return the SpinTrans control object
	 */

	public static SpinTrans translate(TransSystem system, SpinOptions options) {
		return translate(system, options, ".");
	}  


	/**
	 * Generate PROMELA source representing a transition system.
	 * <p>
	 * As above, but the PROMELA is written to the PrintWriter provided.
	 * @param system the transition system
	 * @param out the PrintWriter to write the PROMELA to
	 * @return the SpinTrans control object
	 */
	public static SpinTrans translate(TransSystem system,
			SpinOptions options, PrintWriter out) {
		return (new SpinTrans(system, options, out)).run();
	}  

	public static SpinTrans translate(TransSystem system, SpinOptions options, String outputDir) {
		try {
			if (outputDir == null || "".equals(outputDir)) outputDir = "."+File.separator; 
			else if (!outputDir.endsWith(File.separator)) outputDir = outputDir + File.separator;

			// File sourceFile = new File(isWindows ? system.getName() : system.getName() + ".prom");
			File sourceFile = new File(system.getName() + ".prom");
			FileOutputStream streamOut = new FileOutputStream(sourceFile);
			PrintWriter writerOut = new PrintWriter(streamOut);
			SpinTrans result;
			try {
				result = translate(system, options, writerOut);
				result.outputDir = outputDir;
			} catch (RuntimeException e) {
				writerOut.flush();
				writerOut.close();
				throw(e);
			}
			writerOut.close();
			return result;
		} catch (IOException e) {
			throw new RuntimeException("Could not produce SPIN file: " + e); 
		}
	}  


	/**
	 * Translate binary op
	 */
	void translateBinaryOp(Expr e1, Expr e2, String op) {
		boolean isE1ArrayType = false;
		boolean isE2ArrayType = false;
		boolean isE1RecordType = false;
		boolean isE2RecordType = false;
		TreeNode e1Tree = applyTo(e1);
		TreeNode e2Tree = applyTo(e2);
		// Compose argument trees and then update each leaf
		TreeNode result = e1Tree.compose(e2Tree, null);
		Vector leaves = result.getLeaves(new Vector());

		/* [Thomas, May 9, 2017]
		 * */
		if(e1 instanceof StateVar)
		{
			if (e1.getType().isKind(REF))
			{
				if(((Ref)e1.getType()).getTargetType().isKind(ARRAY))
				{
					isE1ArrayType = true;
				}
				else if(((Ref)e1.getType()).getTargetType().isKind(RECORD))
				{
					isE1RecordType = true;
				}
			}
		}
		if(e2 instanceof StateVar)
		{
			if (e2.getType().isKind(REF))
			{
				if(((Ref)e2.getType()).getTargetType().isKind(ARRAY))
				{
					isE2ArrayType = true;
				}
				else if(((Ref)e2.getType()).getTargetType().isKind(RECORD))
				{
					isE2RecordType = true;
				}
			}
		}
		for (int i = 0; i < leaves.size(); i++) {
			ExprNode leaf = (ExprNode) leaves.elementAt(i);
			
//			if(leaf.expr1.equals("allQuiet_findResult1"))
//			{
//				System.out.println("hello");
//			}
			
			if(isE1ArrayType && isE2ArrayType)
			{
				leaf.update("(" + leaf.expr1 + ".element[0]" + op + leaf.expr2 + ".element[0]" + ")");
			}
			else if(isE1ArrayType && (e2 instanceof IntLit)) /* && !((Ref)e1.getType()).getTargetType().getName().equals("int_arr")  */
			{
				leaf.update("(" + leaf.expr1 + ".length" + op + leaf.expr2 + ")");
			}
			else if(isE1ArrayType)
			{
				leaf.update("(" + leaf.expr1 + ".element[0]" + op + leaf.expr2 + ")");
			}
			else if(isE1RecordType && (e2 instanceof IntLit))
			{
				leaf.update("(" + leaf.expr1 + ".isAlive" + op + leaf.expr2 + ")");
			}
			else if(isE2ArrayType && (e1 instanceof IntLit)) /* && !((Ref)e2.getType()).getTargetType().getName().equals("int_arr")  */
			{
				leaf.update("(" + leaf.expr1 + op + leaf.expr2 + ".length" + ")");
			}
			else if(isE2ArrayType)
			{
				leaf.update("(" + leaf.expr1 + op + leaf.expr2 + ".element[0]" + ")");
			}
			else if(isE2RecordType && (e1 instanceof IntLit))
			{
				leaf.update("(" + leaf.expr1 + op + leaf.expr2 + ".isAlive" + ")");
			}
			else
			{
				leaf.update("(" + leaf.expr1 + op + leaf.expr2 + ")");
			}
		}
		setResult(result);
	}  


	/**
	 * Generate PROMELA for visible BIR location.
	 */
	public void translateLocation(Location loc) {

		if (!loc.isVisible()) return;

		String label = locLabel(loc);
		println(label + ":");
		indentLevel++;
		TransVector outTrans = loc.getOutTrans();
		
		if (outTrans.size() == 1)
		{
			/* [Thomas, May 23, 2017]
			 * */
//			translateSequence(outTrans.elementAt(0), 0);
			Location startLoc = outTrans.elementAt(0).getFromLoc();
			println("atomic { ");
			indentLevel ++;
			translateSeq(new LocVector(startLoc), outTrans.elementAt(0), 0, false, true);
			indentLevel --;
			println("}");
		}
		else if (outTrans.size() == 0) {
			println("goto done;");
		} else {
			println("if");
			for (int i = 0; i < outTrans.size(); i++) {
				print(":: ");
				indentLevel++;
//				translateSequence(outTrans.elementAt(i), i);
				{
					Location startLoc = outTrans.elementAt(i).getFromLoc();
					println("atomic { ");
					indentLevel ++;
					translateSeq(new LocVector(startLoc), outTrans.elementAt(i), i, false, true);
					indentLevel --;
					println("}");
				}
				indentLevel--;
			}
			println("fi;");
		}
		indentLevel--;
	}  


	/**
	 * Translate a sequence of transformations starting with the given one,
	 * continuing until we hit a visible transformation.
	 * <p>
	 * The locSet param keeps track of the locations we've encountered
	 * in this branch (i.e., this atomic block).  If we transition to
	 * an invisible location in this set, we just generate a goto to the local
	 * label for that location, otherwise we recursively call translateSeq
	 * to continue following the sequence.
	 * <p>
	 * If we transition to a visible location, we always generate a goto
	 * to the top-level location label for that location.
	 * <p>
	 * The guard of the transformation may be supressed by setting
	 * param supressGuard.  This is used when an 'if' branch pattern
	 * is detected (two transitions, one on expression E, the other
	 * on expression !E).
	 */
	void translateSeq(LocVector locSet, 
			Transformation trans,
			int branch, 
			boolean supressGuard,
			boolean gotoNeeded) {
		// Generate code for one transformation (possibly without the guard)
		translateTrans(trans, supressGuard);

		// If the target location is visible, reset the dead variables
		// and branch to the code for that location
		if (trans.getToLoc().isVisible()) {
			/* [Thomas, May 23, 2017]
			 * Remove reset
			 * */
//			resetDeadVariables(locSet.firstElement(), trans.getToLoc());
			if(gotoNeeded)
			{
				println("goto " + locLabel(trans.getToLoc()) + ";");
			}
		}

		// If the target location is invisible, but we've already
		// generated code for it in this atomic block, branch to
		// the local label for that location.
		else if (locSet.contains(trans.getToLoc()))
			if(gotoNeeded)
			{
				println("goto " + locLabel(locSet.firstElement(), branch,
						trans.getToLoc()) + ";");
			}

		// Otherwise, continue following the sequence
		else {
			locSet.addElement(trans.getToLoc());

			// If there's more than one way into this location, we
			// may branch back to it from within the atomic block, so
			// generate a local label for the location.
			if (trans.getToLoc().getInTrans().size() > 1)
				println( locLabel(locSet.firstElement(), branch,
						trans.getToLoc()) + ":");
			TransVector successors = trans.getToLoc().getOutTrans();
			// For one successor, don't bother with an 'if'
			if (successors.size() == 1)
				translateSeq(locSet, successors.elementAt(0), branch, false, true);
			// For 'if' branches, use a PROMELA 'else' for efficiency
			else if (isIfBranch(successors)) {
				println("if");
				print(":: ");
				indentLevel++;
				translateSeq(locSet, successors.elementAt(0), branch, false, true);
				indentLevel--;
				print(":: else -> ");
				indentLevel++;
				translateSeq(locSet, successors.elementAt(1), branch, true, true);
				indentLevel--;
				println("fi;");
			} else { // general case
				println("if");
				for (int i = 0; i < successors.size(); i++) {
					print(":: ");
					indentLevel++;
					translateSeq(locSet, successors.elementAt(i), branch, false, true);
					indentLevel--;
				}
				println("fi;");
			}
		}
	}  


	/**
	 * Translate a sequence of transformations, continuing until
	 * we hit a visible transformation.  Make the sequence
	 * atomic.
	 * <p>
	 * The 'branch' parameter is used to generate location
	 * labels that are local to this atomic block.
	 */
	void translateSequence(Transformation trans, int branch) {
		Location startLoc = trans.getFromLoc();
		println("atomic { ");
		indentLevel ++;
		translateSeq(new LocVector(startLoc), trans, branch, false, true);
		indentLevel --;
		println("}");
	}  


	/**
	 * Generate code for a single transformation (possibly suppressing
	 * the code for the guard expression).
	 */
	void translateTrans(Transformation trans, 
			boolean supressGuard) {
		// Set this trans as the context (for action ID generation)
		currentTrans = trans;
		actionNum = 0;
		// Translate and print the guard if present and not supressed
		if (! supressGuard && guardPresent(trans)) {
			printGuard(applyTo(trans.getGuard()).getCase(normal));
			println("-> ");
		}

		ActionVector actions = trans.getActions();
		// Translate each action
		for (int i = 0; i < actions.size(); i++) {
			actionNum = i + 1;
			//println("printf(\"BIR: %d " + actionId() + " OK\\n\", _pid); ");
			actions.elementAt(i).apply(this);
			d_stepSize++;
		}
		// If we used _temp_, reset it
		if (resetTemp) {
			/* [Thomas, May 9, 2017]
			 * Disable printing _temp_
			 * */
			//		    println("_temp_ = 0;");
			resetTemp = false;
		}
	}  


	/**
	 * Translate unary op
	 */
	void translateUnaryOp(Expr e, String op) {
		TreeNode result = applyTo(e);
		Vector leaves = result.getLeaves(new Vector());
		// Translate argument and then apply to each leaf
		for (int i = 0; i < leaves.size(); i++) {
			ExprNode leaf = (ExprNode) leaves.elementAt(i);
			leaf.update("(" + op + leaf.expr1 + ")");
		}
		setResult(result);
	}  
}
