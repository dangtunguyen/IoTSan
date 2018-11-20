package edu.ksu.cis.bandera.bui.counterexample.objectdiagram.gef;

import org.tigris.gef.base.*;
import org.tigris.gef.presentation.*;
import org.tigris.gef.graph.presentation.*;
import java.awt.Color;

public class ObjectEdge extends NetEdge {

    private org.tigris.gef.presentation.FigEdge figEdge;

    public ObjectEdge() {
        super();
    }

    /**
     * Insert the method's description here.
     * Creation date: (1/23/02 4:54:47 PM)
     * @return org.tigris.gef.presentation.FigEdge
     */
    public org.tigris.gef.presentation.FigEdge getFigEdge() {

        if (figEdge == null) {
            // generic line from port to port
            //figEdge = new FigEdgeRectiline();

            // arrowhead line from port to port
            figEdge = new FigEdgeLine();
            figEdge.setSourceArrowHead(new ArrowHeadTriangle());

        }

        return figEdge;
    }

    public String getId() {
        return ("" + this.hashCode());
    }

    public FigEdge makePresentation(Layer lay) {
        return (getFigEdge());
    }

    /**
     * Insert the method's description here.
     * Creation date: (1/23/02 4:54:47 PM)
     * @param newFigEdge org.tigris.gef.presentation.FigEdge
     */
    private void setFigEdge(org.tigris.gef.presentation.FigEdge newFigEdge) {
        figEdge = newFigEdge;
    }
}