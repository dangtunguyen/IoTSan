package edu.ksu.cis.bandera.jext;

/**
 * The ExternalIntChooseExpr provides a choose expression that
 * contains all int values from 0 to the number given in
 * the constructor.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:23 $
 */
public class ExternalIntChooseExpr extends ExternalIntRangeChooseExpr {

    public ExternalIntChooseExpr(int max) {
	super(0, max);
    }

}
