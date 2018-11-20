package edu.ksu.cis.bandera.checker;

import edu.ksu.cis.bandera.checker.Options;
import edu.ksu.cis.bandera.checker.OptionsView;
import edu.ksu.cis.bandera.checker.CompletionListener;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

/**
 * The AbstractOptionsView class provides a base for extension for
 * all OptionsView implementations.  This provides the common functionality
 * including the handling of CompletionListeners and setting/getting Options.
 * To use this as the base, just extend it, over-ride the init method,
 * and implement the setVisible method.  Remember to call fireCompletion() upon completion
 * of the configuration.  This should be done right after you have hidden the
 * view from the user (probably by calling setVisible(false)).
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:21 $
 */
public abstract class AbstractOptionsView implements OptionsView {

    /**
     * The Options model that this view will use.
     */
    private Options options;

    /**
     * A Set of CompletionListener objects that will notified when the view
     * has completed configuration.
     */
    private Set completionListenerSet;

    /**
     * Initialize the view using the given options as the model.
     *
     * @param Options options The Options to use as the model for our view.
     */
    public void init(Options options) {
	setOptions(options);
    }

    /**
     * Set the visibility of this component.
     *
     * @param boolean visibile True if the component should be visible, False otherwise.
     */
    public abstract void setVisible(boolean visible);

    /**
     * Set the Options model that this view should work on.
     *
     * @param Options options The Options model that this view will configure.
     */
    public void setOptions(Options options) {
	this.options = options;
    }

    /**
     * Get the Options model that this view is working on.
     *
     * @return Options The Options model that this view is configuring.
     */
    public Options getOptions() {
	return(options);
    }

    /**
     * Register a CompletionListener that is requesting notification
     * of the completion of the Options configuration.
     *
     * @param CompletionListener completionListener The object to signal when configuration
     *        is complete.
     *
     * @post The given completionListener will in the completionListenerSet.
     */
    public void registerCompletionListener(CompletionListener completionListener) {
	if(completionListener == null) {
	    return;
	}
	if(completionListenerSet == null) {
	    completionListenerSet = new HashSet();
	}
	completionListenerSet.add(completionListener);
    }

    /**
     * Remove all listeners that have been registered.
     *
     * @post The completionListenerSet is empty (or null).
     */
    public void clearCompletionListeners() {
	if(completionListenerSet != null) {
	    completionListenerSet.clear();
	}
    }

    /**
     * Remove the given listener that has been registered.
     *
     * @param CompletionListener completionListener The object that should be removed from
     *        the set of listeners.
     * @post The given CompletionListener is not in the completionListenerSet.
     */
    public void removeCompletionListener(CompletionListener completionListener) {
	if(completionListenerSet != null) {
	    completionListenerSet.remove(completionListener);
	}
    }

    /**
     * When the OptionsView has completed it's tasks, it will call the fireCompletion
     * method which will signal all CompletionListeners that the configuration has
     * completed.
     *
     * @post All registered CompletionListeners will be notified of completion.
     */
    protected void fireCompletion() {
	if(completionListenerSet != null) {
	    Iterator i = completionListenerSet.iterator();
	    while(i.hasNext()) {
		CompletionListener cl = (CompletionListener)i.next();
		cl.complete(this);
	    }
	}
    }
}
