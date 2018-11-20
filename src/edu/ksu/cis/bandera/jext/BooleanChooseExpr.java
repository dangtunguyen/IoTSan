package edu.ksu.cis.bandera.jext;

/**
 * The BooleanChooseExpr provides a choose expression that
 * contains true and false values (0 and 1).
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:23 $
 */
public class BooleanChooseExpr extends IntRangeChooseExpr {

    public BooleanChooseExpr() {
	super(0, 1);
    }

}
