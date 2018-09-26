package edu.ksu.cis.bandera.sessions.ui.gui;

import java.io.File;

import java.util.List;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Component;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.UIManager;

import javax.swing.border.EmptyBorder;

/**
 * The SessionConversionListCellRenderer provides a way to render session conversion
 * information in a list.  This renderer expects that the session conversion information
 * is in a java.util.List with a size of 4:
 * <ol>
 * <li>Source file (java.io.File)</li>
 * <li>Target file (java.io.File)</li>
 * <li>Version (java.lang.Integer)</li>
 * <li>Status (java.lang.Integer)</li>
 * </ol>
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:42 $
 */
public final class SessionConversionListCellRenderer extends JPanel implements ListCellRenderer {

    private final static Color SOURCE_FILE_TEXT_COLOR =      new Color(120, 137, 186);
    private final static Color SOURCE_FILE_BG_COLOR =        Color.white;
    private final static Color TARGET_FILE_TEXT_COLOR =      new Color(120, 137, 186);
    private final static Color TARGET_FILE_BG_COLOR =        Color.white;
    private final static Color ARROW_TEXT_COLOR =            Color.black;
    private final static Color ARROW_BG_COLOR =              Color.white;
    private final static Color ERROR_STATUS_TEXT_COLOR =     Color.red;
    private final static Color ERROR_STATUS_BG_COLOR =       Color.white;
    private final static Color COMPLETE_STATUS_TEXT_COLOR =  Color.green;
    private final static Color COMPLETE_STATUS_BG_COLOR =    Color.white;
    private final static Color WORKING_STATUS_TEXT_COLOR =   Color.orange;
    private final static Color WORKING_STATUS_BG_COLOR =     Color.white;

    private GridBagConstraints statusGBC;
    private GridBagConstraints sourceGBC;
    private GridBagConstraints arrowGBC;
    private GridBagConstraints targetGBC;
    
    /**
     * Create a new SessionConversionListCellRenderer.
     */
    public SessionConversionListCellRenderer() {
	super();
	setLayout(new java.awt.GridBagLayout());
	Insets insets = new Insets(2, 5, 2, 5);
	statusGBC = new GridBagConstraints();
	statusGBC.gridx = 0;
	statusGBC.gridy = 0;
	statusGBC.gridheight = 1;
	statusGBC.gridwidth = 1;
	statusGBC.weightx = 0.0;
	statusGBC.weighty = 0.0;
	statusGBC.anchor = GridBagConstraints.WEST;
	statusGBC.fill = GridBagConstraints.NONE;
	statusGBC.insets = insets;

	sourceGBC = new GridBagConstraints();
	sourceGBC.gridx = 1;
	sourceGBC.gridy = 0;
	sourceGBC.gridheight = 1;
	sourceGBC.gridwidth = 1;
	sourceGBC.weightx = 0.0;
	sourceGBC.weighty = 0.0;
	sourceGBC.anchor = GridBagConstraints.WEST;
	sourceGBC.fill = GridBagConstraints.NONE;
	sourceGBC.insets = insets;

	arrowGBC = new GridBagConstraints();
	arrowGBC.gridx = 2;
	arrowGBC.gridy = 0;
	arrowGBC.gridheight = 1;
	arrowGBC.gridwidth = 1;
	arrowGBC.weightx = 0.0;
	arrowGBC.weighty = 0.0;
	arrowGBC.anchor = GridBagConstraints.WEST;
	arrowGBC.fill = GridBagConstraints.NONE;
	sourceGBC.insets = insets;

	targetGBC = new GridBagConstraints();
	targetGBC.gridx = 3;
	targetGBC.gridy = 0;
	targetGBC.gridheight = 1;
	targetGBC.gridwidth = 1;
	targetGBC.weightx = 1.0;
	targetGBC.weighty = 0.0;
	targetGBC.anchor = GridBagConstraints.WEST;
	targetGBC.fill = GridBagConstraints.HORIZONTAL;
	targetGBC.insets = insets;

    }

    /**
     * Return a component that has been configured to display the specified
     * value. That component's <code>paint</code> method is then called to
     * "render" the cell.  If it is necessary to compute the dimensions
     * of a list because the list cells do not have a fixed size, this method
     * is called to generate a component on which <code>getPreferredSize</code>
     * can be invoked.
     *
     * @param list The JList we're painting.
     * @param value The value returned by list.getModel().getElementAt(index).
     * @param index The cells index.
     * @param isSelected True if the specified cell was selected.
     * @param cellHasFocus True if the specified cell has the focus.
     * @return A component whose paint() method will render the specified value.
     *
     * @see JList
     * @see ListSelectionModel
     * @see ListModel
     */
    public Component getListCellRendererComponent(
        JList list,
        Object value,
        int index,
        boolean isSelected,
        boolean cellHasFocus) {

	    removeAll();

        if (value instanceof List) {
            List l = (List) value;
            if ((l != null) && (l.size() == 4)) {
                File sourceFile = (File) l.get(0);
                File targetFile = (File) l.get(1);
                Integer version = (Integer) l.get(2);
	            Integer status = (Integer) l.get(3);

                JLabel sourceLabel = new JLabel(sourceFile.toString());
                sourceLabel.setForeground(SOURCE_FILE_TEXT_COLOR);
                JLabel targetLabel =
                    new JLabel(targetFile.toString() + "(" + version.toString() + ")");
                targetLabel.setForeground(TARGET_FILE_TEXT_COLOR);
                JLabel arrowLabel = new JLabel("->");
                arrowLabel.setForeground(ARROW_TEXT_COLOR);
                String statusText = "";
                Color statusTextColor = null;
                switch(status.intValue()) {
	                case SessionFileConverterView.DEFAULT_STATUS:
	                	statusText = "";
	                	statusTextColor = Color.gray;
	                	break;
	                case SessionFileConverterView.WORKING_STATUS:
	                	statusText = "Working";
	                	statusTextColor = WORKING_STATUS_TEXT_COLOR;
	                	break;
	                case SessionFileConverterView.COMPLETE_STATUS:
	                	statusText = "Done";
	                	statusTextColor = COMPLETE_STATUS_TEXT_COLOR;
	                	break;
	                case SessionFileConverterView.ERROR_STATUS:
	                	statusText = "Error";
	                	statusTextColor = ERROR_STATUS_TEXT_COLOR;
	                	break;
	                default:
	                	statusText = "";
	                	statusTextColor = Color.gray;
                }
                JLabel statusLabel = new JLabel(statusText);
                statusLabel.setForeground(statusTextColor);

                add(statusLabel, statusGBC);
                add(sourceLabel, sourceGBC);
                add(arrowLabel, arrowGBC);
                add(targetLabel, targetGBC);
            }
            else {
	            System.out.println("List is invalid (either null or not a size of 3).  No renderering.");
            }
        }
        else {
	        System.out.println("Cell value is invalid (not a List).  No rendering.");
        }

        setBackground(
            isSelected ? list.getSelectionBackground() : list.getBackground());
        setForeground(
            isSelected ? list.getSelectionForeground() : list.getForeground());
        setFont(list.getFont());
        setBorder(
            cellHasFocus
                ? UIManager.getBorder("List.focusCellHighlightBorder")
                : new EmptyBorder(1, 1, 1, 1));

        return (this);
    }
}
