package edu.ksu.cis.bandera.ui.common.layout;

/**
 * The RootedLayoutManager extends upon the LayoutMangager
 * interface to provide a rooting mechanism.  Therefore, a
 * LayoutManager can work based upon a single root node
 * in the graph.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:45 $
 */
public interface RootedLayoutManager extends LayoutManager {

    /**
     * Set the root object to use in this layout manager.
     */
    public void setRoot(Object root);

    /**
     * Get the root object used in this layout manager.
     */
    public Object getRoot();

}
