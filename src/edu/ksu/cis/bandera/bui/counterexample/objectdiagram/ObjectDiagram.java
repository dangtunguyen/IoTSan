package edu.ksu.cis.bandera.bui.counterexample.objectdiagram;

import edu.ksu.cis.bandera.ui.common.layout.LayoutEngine;
import edu.ksu.cis.bandera.ui.common.layout.LayoutAdvisor;
import edu.ksu.cis.bandera.ui.common.layout.LayoutCalculator;
import edu.ksu.cis.bandera.ui.common.layout.LayoutManager;

/**
 * The ObjectDiagram interface provides a contract for working with
 * implementations of Object Diagrams.  An ObjectDiagram can be hidden
 * and shown, closed, cleared of all objects, and allows objects and references
 * to be added.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.2 $ - $Date: 2003/04/30 19:33:18 $
 */
public interface ObjectDiagram {

    /**
     * Add a new object to the diagram.
     *
     * @param objectDataNode edu.ksu.cis.bandera.bui.counterexample.ObjectData
     */
    void addNode(ObjectData objectDataNode);

    /**
     * Add a new reference between two objects in the diagram.
     * 
     * @param fromObjectDataNode edu.ksu.cis.bandera.bui.counterexample.ObjectData
     * @param fieldNumber int
     * @param toObjectDataNode edu.ksu.cis.bandera.bui.counterexample.ObjectData
     */
    void addReference(ObjectData fromObjectDataNode, int fieldNumber, ObjectData toObjectDataNode);

    /**
     * Clear all objects and references from this diagram.
     */
    void clear();

    /**
     * Close this object diagram.
     */
    void close();

    /**
     * Hide this object diagram.
     */
    void hide();

    /**
     * Show this object diagram.
     */
    void show();

    /**
     * Show or hide this diagram based upon the flag given.  If true,
     * show the object diagram, otherwise, hide it.
     *
     * @param showIt boolean
     */
    void show(boolean showIt);

    /**
     * Set the LayoutEngine to use with this ObjectDiagram.
     *
     * @param LayoutEngine An instance of LayoutEngine to use for the
     *        layout of objects in the diagram.
     */
    void setLayoutEngine(LayoutEngine layoutEngine);

    /**
     * Set the LayoutEngine to use with this ObjectDiagram.
     *
     * @param LayoutEngine layoutEngine An instance of LayoutEngine to use for the
     *        layout of objects in the diagram.
     * @param boolean useLayoutEngine A flag to tell the ObjectDiagram to use
     *        the given LayoutEngine instead of a LayoutCalculator or
     *        LayoutAdvisor.
     */
    void setLayoutEngine(LayoutEngine layoutEngine, boolean useLayoutEngine);
    void setUseLayoutEngine(boolean useLayoutEngine);
    boolean usingLayoutEngine();
    LayoutEngine getLayoutEngine();

    /**
     * Set the LayoutAdvisor to use with this ObjectDiagram.
     *
     * @param LayoutAdvisor An instance of LayoutAdvisor to use for the
     *        layout of objects in the diagram.
     */
    void setLayoutAdvisor(LayoutAdvisor layoutAdvisor);

    /**
     * Set the LayoutAdvisor to use with this ObjectDiagram.
     *
     * @param LayoutAdvisor layoutAdvisor An instance of LayoutAdvisor to use for the
     *        layout of objects in the diagram.
     * @param boolean useLayoutAdvisor A flag to tell the ObjectDiagram to use
     *        the given LayoutAdvisor instead of a LayoutCalculator or
     *        LayoutEngine.
     */
    void setLayoutAdvisor(LayoutAdvisor layoutAdvisor, boolean useLayoutAdvisor);
    void setUseLayoutAdvisor(boolean useLayoutAdvisor);
    boolean usingLayoutAdvisor();
    LayoutAdvisor getLayoutAdvisor();

    /**
     * Set the LayoutCalculator to use with this ObjectDiagram.
     *
     * @param LayoutCalculator An instance of LayoutCalculator to use for the
     *        layout of objects in the diagram.
     */
    void setLayoutCalculator(LayoutCalculator LayoutCalculator);

    /**
     * Set the LayoutCalculator to use with this ObjectDiagram.
     *
     * @param LayoutCalculator layoutCalculator An instance of Layoutcalculator to use for the
     *        layout of objects in the diagram.
     * @param boolean useLayoutCalculator A flag to tell the ObjectDiagram to use
     *        the given LayoutCalculator instead of a LayoutEngine or
     *        LayoutAdvisor.
     */
    void setLayoutCalculator(LayoutCalculator layoutCalculator, boolean useLayoutCalculator);
    void setUseLayoutCalculator(boolean useLayoutCalculator);
    boolean usingLayoutCalculator();
    LayoutCalculator getLayoutCalculator();

    void setLayoutManager(LayoutManager layoutManager);
    void setLayoutManager(LayoutManager layoutManager, boolean useLayoutManager);
    LayoutManager getLayoutManager();
    boolean usingLayoutManager();
    void setUseLayoutManager(boolean useLayoutManager);

}
