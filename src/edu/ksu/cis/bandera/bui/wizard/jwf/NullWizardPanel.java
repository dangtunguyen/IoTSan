package edu.ksu.cis.bandera.bui.wizard.jwf;

import java.util.List;
import javax.swing.border.TitledBorder;
import edu.ksu.cis.bandera.bui.wizard.jwf.WizardPanel;

/** An implementation of the base class used for implementing a panel that is
 * displayed in a Wizard.  Used if a null panel is set.
 *
 * @author Christopher Brind
 */
public class NullWizardPanel extends WizardPanel {

    /** A default constructor. */
    public NullWizardPanel() {

        setBorder(new TitledBorder("null wizard panel"));
    }
    /** Can this panel finish the wizard?
     * @return true if this panel can finish the wizard.
     */
    public boolean canFinish() {
        return false;
    }
    /** Called when the panel is set. */
    public void display() {
    }
    /** Handle finishing the wizard. */
    public void finish() {
    }
    /** Is there be a next panel?
     * @return true if there is a panel to move to next
     */
    public boolean hasNext() {
        return false;
    }
    /** Get the next panel to go to. */
    public WizardPanel next() {
        return null;
    }
    /** Called to validate the panel before finishing the wizard. Should
     * return false if canFinish returns false.
     * @param list a List of error messages to be displayed.
     * @return true if it is valid for this wizard to finish.
     */
    public boolean validateFinish(List list) {
        return false;
    }
    /** Called to validate the panel before moving to next panel.
     * @param list a List of error messages to be displayed.
     * @return true if the panel is valid,
     */
    public boolean validateNext(List list) {
        return false;
    }
}
