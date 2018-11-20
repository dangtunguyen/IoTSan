package edu.ksu.cis.bandera.bui.wizard.jwf;

/** Implement this interface to be able to receive wizard related events.
 *
 * @author Christopher Brind
 */
public interface WizardListener {

    /** Called when the wizard is cancelled.
     * @param wizard the wizard that was cancelled.
     */
    public void wizardCancelled(Wizard wizard);
    /** Called when the wizard finishes.
     * @param wizard the wizard that finished.
     */
    public void wizardFinished(Wizard wizard);
    /** Called when a new panel has been displayed in the wizard.
     * @param wizard the wizard that was updated
     */
    public void wizardPanelChanged(Wizard wizard);
}
