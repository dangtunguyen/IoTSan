package edu.ksu.cis.bandera.bui.wizard;

import edu.ksu.cis.bandera.bui.wizard.jwf.WizardPanel;

/**
 * The WizardController is an interface that will provide a means to put the
 * control of the Wizard steps outside the WizardPanels.  To use implement this,
 * you will need to implement the getNext(Object object): WizardPanel method.  This should
 * determine the next WizardPanel based upon the current state of the system.  This
 * information should be made based upon the object passed in.  In most cases, this will
 * be the current WizardPanel.  Using the type of WizardPanel and the WizardPanel's data,
 * the current state of the system should be able to be discovered.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2002/06/14 17:37:59 $
 */
public interface WizardController {
/**
 * Get the next WizardPanel in the Wizard based upon the current WizardPanel's
 * state.
 *
 * @return jwf.WizardPanel
 * @param currentWizardPanel jwf.WizardPanel
 */
WizardPanel getNext(WizardPanel currentWizardPanel);
}
