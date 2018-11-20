package edu.ksu.cis.bandera.jext;

/**
 * The IntChooseExpr provides a choose expression that
 * contains all int values from 0 to the number given in
 * the constructor.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:24 $
 */
public class IntChooseExpr extends IntRangeChooseExpr {

    public IntChooseExpr(int max) {
	super(0, max);
    }

}
