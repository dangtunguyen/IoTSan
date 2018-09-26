package edu.ksu.cis.bandera.bui.counterexample.objectdiagram.gef;

import org.tigris.gef.presentation.*;

import java.util.StringTokenizer;
import java.awt.Color;
import java.awt.Rectangle;
import java.lang.Math;
import java.util.ArrayList;
import javax.swing.table.TableModel;

import org.apache.log4j.Category;

/*
 * To do list:
 * x) Add config to turn table header on/off
 *
 * x) Add config to change font size, color, and family
 *
 * x) Can I set the background and outline color for the FigText.  If so, make that
 * configurable.
 *
 * x) javadoc comment all objects in this package before adding it to VAJ.
 *
 * x) review algorithm and design for possible improvements - space/time/readable/reusable
 *
 * x) should the type be displayed w/o package?  or maybe configurable?
 *
 */


public class FigObjectNode extends FigNode {

    private static final String HEADER_TEXT_FONT_FAMILY_STRING = "TimesRoman";
    private static final Color HEADER_TEXT_COLOR = Color.blue;
    private static final int HEADER_TEXT_FONT_SIZE = 15;

    private static final String FIELD_TEXT_FONT_FAMILY_STRING = "TimesRoman";
    private static final int FIELD_TEXT_FONT_SIZE = 12;
    private static final Color FIELD_TEXT_COLOR = Color.blue;

    private static final Color RECT_LINE_COLOR = Color.black;
    private static final Color RECT_FILL_COLOR = Color.white;

    private static Category log = Category.getInstance(FigObjectNode.class);

    public FigObjectNode(Object owner) {
        super();
        setOwner(owner);
    }
    private void initialize(Object owner) {

        if((owner != null) && (owner instanceof ObjectNode)) {

            ObjectNode node = (ObjectNode) owner;

            // create the headerText
            String name = node.getName();
            String type = node.getType();
            FigText headerText = new FigText(0, 0, 1, 1, HEADER_TEXT_COLOR, HEADER_TEXT_FONT_FAMILY_STRING,
                                             HEADER_TEXT_FONT_SIZE, true);
            headerText.setText(name + " : " + type);
            headerText.calcBounds();

            ObjectPort objectPort = (ObjectPort)node.getPort(0);
            if(objectPort == null) {
	            log.warn("objectPort is null.  This shouldn't happen????");
            }
            else {
            	bindPort(objectPort, headerText);
            }
            addFig(headerText);

            TableModel tableModel = node.getTableModel();
            if(tableModel == null) {
	            // show that this object has no fields
	            Rectangle headerTextBounds = headerText.getBounds();
	            int noFieldsTextX = headerTextBounds.x;
	            int noFieldsTextY = headerTextBounds.y + headerTextBounds.height;
	            
	            FigText noFieldsText = new FigText(noFieldsTextX, noFieldsTextY, 1, 1, HEADER_TEXT_COLOR, HEADER_TEXT_FONT_FAMILY_STRING,
		            HEADER_TEXT_FONT_SIZE, true);
	            noFieldsText.setText("No fields.");
	            noFieldsText.calcBounds();
	            addFig(noFieldsText);

	            Rectangle noFieldsTextBounds = noFieldsText.getBounds();
	            if(noFieldsTextBounds.width > headerTextBounds.width) {
		            // expand the header text
		            headerTextBounds.x = noFieldsTextBounds.x;
		            headerTextBounds.width = noFieldsTextBounds.width;
		            headerText.setBounds(headerTextBounds);
	            }
	            else {
		            if(noFieldsTextBounds.width < headerTextBounds.width) {
			            // expand the noFields text
		            	noFieldsTextBounds.x = headerTextBounds.x;
			            noFieldsTextBounds.width = headerTextBounds.width;
			            noFieldsText.setBounds(noFieldsTextBounds);
		            }
		            else {
			            // do nothing, as chance would have it, they are the same width
		            }
	            }

	            
	            calcBounds();
                return;
            }

            int rows = tableModel.getRowCount();
            int columns = tableModel.getColumnCount();
            if((rows <= 0) || (columns <= 0)) {
	            log.error("No data to display.  Quitting.");
	            return;
            }

            Fig[][] figArray = new Fig[columns][rows];
            int[] columnWidthArray = new int[columns];  // assume: initialized to all 0
            int[] columnXArray = new int[columns];  // assume: initialized to all 0
            int[] rowHeightArray = new int[rows];  // assume: initialized to all 0
            int[] rowYArray = new int[rows];  // assume: initialized to all 0

            for(int i = 0; i < columns; i++) {
                for(int j = 0; j < rows; j++) {
                    Rectangle currentBounds = null;
                    Object currentValue = tableModel.getValueAt(j, i);
                    if(currentValue == null) {
	                    continue;
                    }

                    if(currentValue instanceof ObjectPort) {
                        FigRect currentRect = new FigRect(0, 0, 1, 1, RECT_LINE_COLOR, RECT_FILL_COLOR);
                        currentRect.calcBounds();

                        figArray[i][j] = currentRect;

                        currentBounds = currentRect.getBounds();

                        ObjectPort currentObjectPort = (ObjectPort)currentValue;
                        bindPort(currentObjectPort, currentRect);

                    }
                    else {
                        FigText currentText = new FigText(0, 0, 1, 1, FIELD_TEXT_COLOR, FIELD_TEXT_FONT_FAMILY_STRING,
                                                          FIELD_TEXT_FONT_SIZE, true);
                        currentText.setText(currentValue.toString());
                        currentText.calcBounds();

                        figArray[i][j] = currentText;

                        currentBounds = currentText.getBounds();
                    }

                    columnWidthArray[i] = Math.max(columnWidthArray[i], currentBounds.width);
                    rowHeightArray[j] = Math.max(rowHeightArray[j], currentBounds.height);

                }
            }

            columnXArray[0] = headerText.getBounds().x;
            for(int i = 1; i < columns; i++) {
                columnXArray[i] = columnXArray[i - 1] + columnWidthArray[i - 1];
            }

            rowYArray[0] = headerText.getBounds().y + headerText.getBounds().height;
            for(int i = 1; i < rows; i++) {
                rowYArray[i] = rowYArray[i - 1] + rowHeightArray[i - 1];
            }

            for(int i = 0; i < columns; i++) {
                for(int j = 0; j < rows; j++) {
                    Rectangle currentBounds = figArray[i][j].getBounds();

                    currentBounds.x = columnXArray[i];
                    currentBounds.y = rowYArray[j];
                    currentBounds.height = rowHeightArray[j];
                    currentBounds.width = columnWidthArray[i];

                    figArray[i][j].setBounds(currentBounds);
                    addFig(figArray[i][j]);
                }
            }

            // make sure that the table width and the header width match (to make a perfect rectangle)
            int tableWidth = 0;
            for(int i = 0; i < columns; i++) {
                tableWidth += columnWidthArray[i];
            }
            int headerWidth = headerText.getBounds().width;
            if(headerWidth > tableWidth) {
                // expand the table to match the header width
                /* to expand the table, take the difference of the header and table and add
                 * that padding to the last column of the table ... the value section.
                 */
                int padding = headerWidth - tableWidth;
                for(int i = 0; i < rows; i++) {
                    Rectangle currentBounds = figArray[columns - 1][i].getBounds();

                    currentBounds.width += padding;

                    figArray[columns - 1][i].setBounds(currentBounds);
                }
            }
            else {
                if(headerWidth < tableWidth) {
                    // expand the header to match the table width
                    Rectangle headerBounds = headerText.getBounds();
                    headerBounds.width = tableWidth;
                    headerText.setBounds(headerBounds);
                }
                else {
                    // do nothing, as luck would have it, they are the same width :) -tcw
                }
            }

            calcBounds();

        }
        else {
            log.error("owner is not an ObjectNode or is null.");
        }

    }
    public boolean isResizable() {
        return(false);
    }
    public void setOwner(Object owner) {
        super.setOwner(owner);
        initialize(owner);
    }
}
