package edu.ksu.cis.bandera.jext;

public interface BanderaSpecExprSwitch {

    /**
     * 
     * @param expr edu.ksu.cis.bandera.jext.AllThreadsExpr
     */
    void caseAllThreadsExpr(AllThreadsExpr expr);

    /**
     * 
     * @param expr edu.ksu.cis.bandera.jext.ExistsThreadExpr
     */
    void caseExistsThreadExpr(ExistsThreadExpr expr);
}
