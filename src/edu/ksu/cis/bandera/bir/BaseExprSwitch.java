package edu.ksu.cis.bandera.bir;

/**
 * The BaseExprSwitch provides an easy way to create an expression switch
 * that will handle all the cases for you except for those that are
 * of interest to your visitor pattern.  To use this, just extend it,
 * implement those cases that are necessary and you are set to go.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:32:50 $
 */
public class BaseExprSwitch implements ExprSwitch {

    public void caseAddExpr(AddExpr expr) {
	caseBinaryExpr(expr);
    }
    public void caseAndExpr(AndExpr expr) {
	caseBinaryExpr(expr);
    }
    public void caseArrayExpr(ArrayExpr expr) {
	// nothing? -tcw
    }
    public void caseAssertAction(AssertAction assertAction) {
	Expr condition = assertAction.getCondition();
	condition.apply(this);
    }
    public void caseAssignAction(AssignAction assign) {
	Expr rhs = assign.getRhs();
	rhs.apply(this);
	Expr lhs = assign.getLhs();
	lhs.apply(this);
    }
    public void caseBoolLit(BoolLit expr) {
	// nothing? -tcw
    }
    public void caseInternChooseExpr(InternChooseExpr expr) {
	// nothing? -tcw
    }
    public void caseExternChooseExpr(ExternChooseExpr expr) {
	// nothing? -tcw
    }
    public void caseForallExpr(ForallExpr expr) {
	// nothing? -tcw
    }
    public void caseConstant(Constant expr) {
	// nothing? -tcw
    }
    public void caseDerefExpr(DerefExpr expr) {
	Expr target = expr.getTarget();
	target.apply(this);
    }
    public void caseDivExpr(DivExpr expr) {
	caseBinaryExpr(expr);
    }
    public void caseEqExpr(EqExpr expr) {
	caseBinaryExpr(expr);
    }
    public void caseInstanceOfExpr(InstanceOfExpr expr) {
	Expr refExpr = expr.getRefExpr();
	refExpr.apply(this);
    }
    public void caseIntLit(IntLit expr) {
	// nothing? -tcw
    }
    public void caseLeExpr(LeExpr expr) {
	caseBinaryExpr(expr);
    }
    public void caseLengthExpr(LengthExpr expr) {
	Expr array = expr.getArray();
	array.apply(this);
    }
    public void caseLockAction(LockAction lockAction) {
	Expr lockExpr = lockAction.getLockExpr();
	lockExpr.apply(this);
    }
    public void caseLockLit(LockLit expr) {
	// nothing? -tcw
    }
    public void caseLockTest(LockTest lockTest) {
	Expr lockExpr = lockTest.getLockExpr();
	lockExpr.apply(this);
    }
    public void caseLtExpr(LtExpr expr) {
	caseBinaryExpr(expr);
    }
    public void caseMulExpr(MulExpr expr) {
	caseBinaryExpr(expr);
    }
    public void caseNeExpr(NeExpr expr) {
	caseBinaryExpr(expr);
    }
    public void caseNewArrayExpr(NewArrayExpr expr) {
	Expr length = expr.getLength();
	length.apply(this);
    }
    public void caseNewExpr(NewExpr expr) {
	// nothing? -tcw
    }
    public void caseNotExpr(NotExpr expr) {
	Expr op1 = expr.getOp();
	op1.apply(this);
    }
    public void caseNullExpr(NullExpr expr) {
	// nothing? -tcw
    }
    public void caseOrExpr(OrExpr expr) {
	caseBinaryExpr(expr);
    }
    public void casePrintAction(PrintAction printAction) {
	// nothing? -tcw
    }
    public void caseRecordExpr(RecordExpr expr) {
	Expr record = expr.getRecord();
	record.apply(this);
    }
    public void caseRefExpr(RefExpr expr) {
	// nothing? -tcw
    }
    public void caseRefLit(RefLit expr) {
	// nothing? -tcw
    }
    public void caseRemExpr(RemExpr expr) {
	caseBinaryExpr(expr);
    }
    public void caseStateVar(StateVar expr) {
	// nothing? -tcw
    }
    public void caseSubExpr(SubExpr expr) {
	caseBinaryExpr(expr);
    }
    public void caseThreadAction(ThreadAction threadAction) {
	Expr lhs = threadAction.getLhs();
	lhs.apply(this);
    }
    public void caseThreadLocTest(ThreadLocTest threadLocTest) {
	Expr lhs = threadLocTest.getLhs();
	lhs.apply(this);
    }
    public void caseRemoteRef(RemoteRef remoteRef) {
	Expr lhs = remoteRef.getLhs();
	lhs.apply(this);
	StateVar var = remoteRef.getVar();
	var.apply(this);
    }
    public void caseThreadTest(ThreadTest threadTest) {
	Expr lhs = threadTest.getLhs();
	lhs.apply(this);
    }
    public void defaultCase(Object obj) {
	// nothing? -tcw
    }

    protected void caseBinaryExpr(BinaryExpr binaryExpr) {
	Expr op1 = binaryExpr.getOp1();
	op1.apply(this);
	Expr op2 = binaryExpr.getOp2();
	op2.apply(this);
    }
}
