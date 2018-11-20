package edu.ksu.cis.bandera.checker;

import edu.ksu.cis.bandera.checker.Options;
import edu.ksu.cis.bandera.checker.CompletionListener;

/**
 * The OptionsView interface provides a view for the Options model.
 *
 * There are two suggested patterns to implementing an OptionsView.  The first
 * is to implement OptionsView interface while extending some GUI component (probably
 * a JFrame, JDialog, or something like it).  This means that you will have
 * to re-implement the set/getOptions methods as well as the register/clear/removeCompletionListener
 * methods.
 * The second is to extend AbstractOptionsView.  This obviously means you cannot
 * extend a GUI compenent.  Therefore you can create the GUI component as another
 * object (i.e. CheckerOptionsDialog extending JDialog or CheckerOptionsFrame extending
 * JFrame) and then have an instance of that be a field in CheckerOptionsView.  You will
 * then just over-ride setOptions to set that model for the GUI component and have
 * the GUI component call fireCompletion.  You can then implement setVisible as a simple
 * pass-through method to call the components setVisible method.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:22 $
 */
public interface OptionsView {

    /**
     * Initialize the view using the given options as the model.
     *
     * @param Options options The Options to use as the model for our view.
     */
    public void init(Options options);

    /**
     * Set the Options model that this view should work on.
     *
     * @param Options options The Options model that this view will configure.
     */
    public void setOptions(Options options);

    /**
     * Get the Options model that this view is working on.
     *
     * @return Options The Options model that this view is configuring.
     */
    public Options getOptions();

    /**
     * Set the visibility of this component.
     *
     * @param boolean visibile True if the component should be visible, False otherwise.
     */
    public void setVisible(boolean visible);

    /**
     * Register a CompletionListener that is requesting notification
     * of the completion of the Options configuration.
     *
     * @param CompletionListener completionListener The object to signal when configuration
     *        is complete.
     */
    public void registerCompletionListener(CompletionListener completionListener);

    /**
     * Remove all listeners that have been registered.
     */
    public void clearCompletionListeners();

    /**
     * Remove the given listener that has been registered.
     *
     * @param CompletionListener completionListener The object that should be removed from
     *        the set of listeners.
     */
    public void removeCompletionListener(CompletionListener completionListener);

}
