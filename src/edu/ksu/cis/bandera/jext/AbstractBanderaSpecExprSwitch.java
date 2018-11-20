package edu.ksu.cis.bandera.jext;

public abstract class AbstractBanderaSpecExprSwitch extends AbstractBanderaExprSwitch implements BanderaSpecExprSwitch
{
    public void caseAllThreadsExpr(AllThreadsExpr expr) {
        defaultCase(expr);
    }
    public void caseExistsThreadExpr(ExistsThreadExpr expr) {
        defaultCase(expr);
    }

}
