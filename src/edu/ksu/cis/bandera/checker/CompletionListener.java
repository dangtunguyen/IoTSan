package edu.ksu.cis.bandera.checker;

/**
 * The CompletionListener interface provides a way to signal
 * the completion of a task to those that wish to hear it.  An
 * example is when the the OptionsView is complete (the user has
 * completed the configuration of the Options), the SessionManagerView
 * will need to perform actions based upon those final, completed
 * Options.  So the SessionManagerView will implement the CompletionListener
 * interface and register itself with the OptionsView.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.1 $ - $Date: 2003/04/30 19:33:21 $
 */
public interface CompletionListener {

    /**
     * This method will be called upon completion of the activity and
     * the source will be the Object that completes that activity.
     *
     * @param Object source The Object that completes the task.
     */
    public void complete(Object source);
}
