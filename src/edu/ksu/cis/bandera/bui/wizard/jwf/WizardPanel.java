package edu.ksu.cis.bandera.bui.wizard.jwf;

import java.util.List;
import javax.swing.JPanel;

/** The base class used for implementing a panel that is displayed in
 * a Wizard.
 *
 * @author Christopher Brind
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 */
public abstract class WizardPanel extends JPanel {

    /** The context of the wizard process. */
    protected WizardContext wizardContext;

    protected String title;

    /** A default constructor. */
    public WizardPanel() {
	title = "Wizard";
    }
    /** Can this panel finish the wizard?
     * @return true if this panel can finish the wizard.
     */
    public abstract boolean canFinish();
    /** Called when the panel is set. */
    public abstract void display();
    /** Handle finishing the wizard. */
    public abstract void finish();
    /** Get the wizard context.
     *@return a WizardContext object
     */
    public final WizardContext getWizardContext() {
        return wizardContext;
    }
    /** Has this panel got help?  Defaults to false, override to change.
     * @return false if there is no help for this panel.
     */
    public boolean hasHelp() {
        return false;
    }
    /** Is there be a next panel?
     * @return true if there is a panel to move to next
     */
    public abstract boolean hasNext();
    /** Override this method to provide help. */
    public void help() {
    }
    /** Get the next panel to go to. */
    public abstract WizardPanel next();
    /** Sets the context this wizard should use. */
    protected final void setWizardContext(WizardContext wizardContext) {
        this.wizardContext = wizardContext;
    }
    /** Called to validate the panel before finishing the wizard. Should
     * return false if canFinish returns false.
     * @param list a List of error messages to be displayed.
     * @return true if it is valid for this wizard to finish.
     */
    public abstract boolean validateFinish(List list);
    /** Called to validate the panel before moving to next panel.
     * @param list a List of error messages to be displayed.
     * @return true if the panel is valid,
     */
    public abstract boolean validateNext(List list);

    public String getTitle() {
	return(title);
    }

    public void setTitle(String title) {
	this.title = title;
    }
}
