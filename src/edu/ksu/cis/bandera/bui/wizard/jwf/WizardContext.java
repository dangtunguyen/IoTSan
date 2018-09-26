package edu.ksu.cis.bandera.bui.wizard.jwf;

import java.util.HashMap;

/** An instance of this class is maintained by the Wizard.  WizardPanels
 * can store information here for use in other WizardPanels, or for the
 * overal purpose of the wizard.
 *
 * @author Christopher Brind
 */
public class WizardContext {

    private final HashMap attributes = new HashMap();

    /** Gets an attribute.
     * @param key an Object used to retrieve information from this context
     * @return an Object
     */
    public Object getAttribute(Object key) {
        return attributes.get(key);
    }
    /** Sets an attribute.
     * @param key an Object that is the key for this attribute
     * @param value an Object this is the value of this attribute
     */
    public void setAttribute(Object key, Object value) {
        attributes.put(key, value);
    }
}
