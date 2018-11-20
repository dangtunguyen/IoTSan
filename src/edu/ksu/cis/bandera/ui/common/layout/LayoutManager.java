package edu.ksu.cis.bandera.ui.common.layout;

/**
 * The LayoutManager provides a layout mechanism that will
 * perform the layout actions on the graph directly.  Therefore,
 * each LayoutManager will make calls on a particular graph API.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:45 $
 */
public interface LayoutManager {

    /**
     * Perform the layout according the implemented algorithm.
     */
    public void layout();

}
