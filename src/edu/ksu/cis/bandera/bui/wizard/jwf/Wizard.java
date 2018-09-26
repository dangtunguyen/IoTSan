package edu.ksu.cis.bandera.bui.wizard.jwf;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.Frame;
import java.awt.Dialog;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.CardLayout;
import java.awt.Insets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

import javax.swing.border.LineBorder;
import javax.swing.border.BevelBorder;

import java.net.URL;

/**
 * This class controls a wizard.<p>
 *  Add it to a frame or any other container then call start with your initial
 *  wizard panel.<p>
 * Listeners can also be added to trap when the wizard finishes and when
 *  the wizard is cancelled.
 *
 * Note: I have added some stuff that is supposed to do an animation while performing
 * the call to next.  It currently doesn't do anything and needs to be fixed.  It _does not_
 * harm anything!
 *
 * @author Christopher Brind
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 */
public class Wizard extends javax.swing.JPanel implements java.awt.event.ActionListener {
    private JButton backButton;
    private JButton nextButton;
    private JButton finishButton;
    private JButton cancelButton;
    private JButton helpButton;
    private final HashMap listeners = new HashMap();
    private Stack previous = null;
    private WizardPanel current = null;
    private WizardContext ctx = null;
    private java.util.ArrayList stageList;
    private int currentStageIndex;
    private javax.swing.JPanel stagesPanel;
    private final static java.awt.Color INACTIVE_FOREGROUND_COLOR = Color.black;
    private final static java.awt.Color INACTIVE_BACKGROUND_COLOR = Color.gray;
    private final static java.awt.Color ACTIVE_FOREGROUND_COLOR = Color.yellow;
    private final static java.awt.Color ACTIVE_BACKGROUND_COLOR = new Color(150, 50, 202); // purple
    private java.util.Map stageMap;
    private final static java.lang.String BACK_BUTTON_ACTION_COMMAND = "BACK";
    private final static java.lang.String NEXT_BUTTON_ACTION_COMMAND = "NEXT";
    private final static java.lang.String HELP_BUTTON_ACTION_COMMAND = "HELP";
    private final static java.lang.String FINISH_BUTTON_ACTION_COMMAND = "FINISH";
    private final static java.lang.String CANCEL_BUTTON_ACTION_COMMAND = "CANCEL";
    private javax.swing.JLabel logoLabel;
    private javax.swing.JLabel animatedLogoLabel;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JPanel logoPanel;
    private CardLayout logoPanelCardLayout;
    private GridBagConstraints stagesPanelConstraints;
    private GridBagConstraints titlePanelConstraints;
    private GridBagConstraints mainPanelConstraints;
    private GridBagConstraints buttonPanelConstraints;
    private JLabel titleLabel;
    private JPanel titlePanel;
    private static final String DEFAULT_TITLE = "Bandera WizardPanel Title";

	public final static java.awt.Color DEFAULT_PANEL_BACKGROUND_COLOR = new Color(204, 204, 255);
    /** Creates a new wizard. */
    public Wizard() {

	    setBackground(DEFAULT_PANEL_BACKGROUND_COLOR);
	setLayout(new GridBagLayout());

	// set up stages panel grid bag constraints
	stagesPanelConstraints = new GridBagConstraints();
	stagesPanelConstraints.gridx = 0;
	stagesPanelConstraints.gridy = 0;
	stagesPanelConstraints.weightx = 1.0;
	stagesPanelConstraints.weighty = 0.0;
	stagesPanelConstraints.anchor = GridBagConstraints.NORTH;
	stagesPanelConstraints.fill = GridBagConstraints.HORIZONTAL;

	// set up title panel grid bag constraints
	titlePanelConstraints = new GridBagConstraints();
	titlePanelConstraints.gridx = 0;
	titlePanelConstraints.gridy = 1;
	titlePanelConstraints.weightx = 0.0;
	titlePanelConstraints.weighty = 0.0;
	titlePanelConstraints.ipadx = 30;
	titlePanelConstraints.ipady = 10;
	titlePanelConstraints.insets = new Insets(10, 2, 10, 2);
	titlePanelConstraints.anchor = GridBagConstraints.NORTH;
	//titlePanelConstraints.fill = GridBagConstraints.HORIZONTAL;

	// set up main panel grid bag constraints
	mainPanelConstraints = new GridBagConstraints();
	mainPanelConstraints.gridx = 0;
	mainPanelConstraints.gridy = 2;
	mainPanelConstraints.weightx = 1.0;
	mainPanelConstraints.weighty = 1.0;
	mainPanelConstraints.anchor = GridBagConstraints.CENTER;
	mainPanelConstraints.fill = GridBagConstraints.BOTH;

	// set up button panel grid bag constraints
	buttonPanelConstraints = new GridBagConstraints();
	buttonPanelConstraints.gridx = 0;
	buttonPanelConstraints.gridy = 3;
	buttonPanelConstraints.weightx = 1.0;
	buttonPanelConstraints.weighty = 0.0;
	buttonPanelConstraints.anchor = GridBagConstraints.SOUTH;
	buttonPanelConstraints.fill = GridBagConstraints.HORIZONTAL;

	// set up the Next button
	try {
	    String nextButtonIconFilename =
		"edu/ksu/cis/bandera/bui/wizard/jwf/images/nextButton2.gif";
	    URL nextButtonIconURL =
		ClassLoader.getSystemClassLoader().getResource(nextButtonIconFilename);
	    ImageIcon nextButtonIcon = new ImageIcon(nextButtonIconURL);
	    //nextButton = new JButton(nextButtonIcon);
	    nextButton = new JButton("Next", nextButtonIcon);
	    nextButton.setBackground(DEFAULT_PANEL_BACKGROUND_COLOR);
	}
	catch (Exception e) {
	    nextButton = new JButton("Next");
	}
	nextButton.addActionListener(this);
	nextButton.setActionCommand(NEXT_BUTTON_ACTION_COMMAND);
	nextButton.setEnabled(false);

	// set up the Back button
	try {
	    String backButtonIconFilename =
		"edu/ksu/cis/bandera/bui/wizard/jwf/images/backButton2.gif";
	    URL backButtonIconURL =
		ClassLoader.getSystemClassLoader().getResource(backButtonIconFilename);
	    ImageIcon backButtonIcon = new ImageIcon(backButtonIconURL);
	    //backButton = new JButton(backButtonIcon);
	    backButton = new JButton("Back", backButtonIcon);
	    backButton.setBackground(DEFAULT_PANEL_BACKGROUND_COLOR);
	}
	catch (Exception e) {
	    backButton = new JButton("Back");
	}
	backButton.addActionListener(this);
	backButton.setActionCommand(BACK_BUTTON_ACTION_COMMAND);
	backButton.setEnabled(false);

	// set up the Finish button
	try {
	    String finishButtonIconFilename =
		"edu/ksu/cis/bandera/bui/wizard/jwf/images/finishButton2.gif";
	    URL finishButtonIconURL =
		ClassLoader.getSystemClassLoader().getResource(finishButtonIconFilename);
	    ImageIcon finishButtonIcon = new ImageIcon(finishButtonIconURL);
	    //finishButton = new JButton(finishButtonIcon);
	    finishButton = new JButton("Finish", finishButtonIcon);
	    finishButton.setBackground(DEFAULT_PANEL_BACKGROUND_COLOR);
	}
	catch (Exception e) {
	    finishButton = new JButton("Finish");
	}
	finishButton.addActionListener(this);
	finishButton.setActionCommand(FINISH_BUTTON_ACTION_COMMAND);
	finishButton.setEnabled(false);

	// set up the Cancel button
	try {
	    String cancelButtonIconFilename =
		"edu/ksu/cis/bandera/bui/wizard/jwf/images/cancelButton2.gif";
	    URL cancelButtonIconURL =
		ClassLoader.getSystemClassLoader().getResource(cancelButtonIconFilename);
	    ImageIcon cancelButtonIcon = new ImageIcon(cancelButtonIconURL);
	    //cancelButton = new JButton(cancelButtonIcon);
	    cancelButton = new JButton("Cancel", cancelButtonIcon);
	    cancelButton.setBackground(DEFAULT_PANEL_BACKGROUND_COLOR);
	}
	catch (Exception e) {
	    cancelButton = new JButton("Cancel");
	}
	cancelButton.addActionListener(this);
	cancelButton.setActionCommand(CANCEL_BUTTON_ACTION_COMMAND);
	cancelButton.setEnabled(false);

	// set up the Help button
	try {
	    String helpButtonIconFilename =
		"edu/ksu/cis/bandera/bui/wizard/jwf/images/helpButton2.gif";
	    URL helpButtonIconURL =
		ClassLoader.getSystemClassLoader().getResource(helpButtonIconFilename);
	    ImageIcon helpButtonIcon = new ImageIcon(helpButtonIconURL);
	    //helpButton = new JButton(helpButtonIcon);
	    helpButton = new JButton("Help", helpButtonIcon);
	    helpButton.setBackground(DEFAULT_PANEL_BACKGROUND_COLOR);
	}
	catch (Exception e) {
	    helpButton = new JButton("Help");
	}
	helpButton.addActionListener(this);
	helpButton.setActionCommand(HELP_BUTTON_ACTION_COMMAND);
	helpButton.setEnabled(false);

	logoLabel = null;
	try {
	    String logoIconFilename =
		"edu/ksu/cis/bandera/bui/wizard/jwf/images/banderaFlagLogoSmall.gif";
	    URL logoIconURL =
		ClassLoader.getSystemClassLoader().getResource(logoIconFilename);
	    ImageIcon logoIcon = new ImageIcon(logoIconURL);
	    logoLabel = new JLabel(logoIcon);
	    logoLabel.setVisible(true);
	}
	catch (Exception e) {
	    System.err.println("Exception caught while loading the logoLabel: " + e.toString());
	    logoLabel = new JLabel("Bandera");
	}

	animatedLogoLabel = null;
	try {
	    String animatedLogoIconFilename =
		"edu/ksu/cis/bandera/bui/wizard/jwf/images/animatedBanderaFlagLogoSmall3.gif";
	    URL animatedLogoIconURL =
		ClassLoader.getSystemClassLoader().getResource(animatedLogoIconFilename);
	    ImageIcon animatedLogoIcon = new ImageIcon(animatedLogoIconURL);
	    animatedLogoLabel = new JLabel(animatedLogoIcon);
	    animatedLogoLabel.setVisible(false);
	}
	catch (Exception e) {
	    animatedLogoLabel = new JLabel("Bandera");
	}
	logoPanel = new JPanel();
	logoPanel.setBackground(new Color(204, 204, 255));
	logoPanelCardLayout = new CardLayout();
	logoPanel.setLayout(logoPanelCardLayout);
	logoPanel.add(logoLabel, "logoLabel");
	logoPanel.add(animatedLogoLabel, "animatedLogoLabel");
	logoPanelCardLayout.first(logoPanel);
    
	JPanel navButtons = new JPanel();
	navButtons.setBackground(new Color(204, 204, 255));
	navButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));
	navButtons.add(backButton);
	navButtons.add(nextButton);
	navButtons.add(finishButton);

	JPanel cancelButtons = new JPanel();
	cancelButtons.setBackground(new Color(204, 204, 255));
	cancelButtons.setLayout(new FlowLayout(FlowLayout.LEFT));
	cancelButtons.add(cancelButton);
	cancelButtons.add(helpButton);

	buttonPanel = new JPanel();
	buttonPanel.setLayout(new BorderLayout());
	buttonPanel.setBackground(new Color(204, 204, 255));
	buttonPanel.add(navButtons, BorderLayout.EAST);
	buttonPanel.add(cancelButtons, BorderLayout.WEST);
	buttonPanel.add(logoPanel, BorderLayout.CENTER);

	add(buttonPanel, buttonPanelConstraints);

	titleLabel = new JLabel();
	titleLabel.setText(DEFAULT_TITLE);
	titleLabel.setForeground(ACTIVE_FOREGROUND_COLOR);
	titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
	titleLabel.setVerticalAlignment(SwingConstants.CENTER);
	titlePanel = new JPanel();
	titlePanel.setLayout(new BorderLayout());
	titlePanel.add(titleLabel, BorderLayout.CENTER);
	titlePanel.setBorder(new LineBorder(Color.black, 2));
	titlePanel.setBackground(ACTIVE_BACKGROUND_COLOR);
	add(titlePanel, titlePanelConstraints);

	//Dimension size = new Dimension(450, 200);
	//setMinimumSize(size);
	//setPreferredSize(size);

    }
    /** Handle's button presses.
     * param ae an ActionEvent object
     */
    public void actionPerformed(ActionEvent ae) {

        String ac = ae.getActionCommand();
        if (BACK_BUTTON_ACTION_COMMAND.equals(ac)) {
            back();
        } else if (NEXT_BUTTON_ACTION_COMMAND.equals(ac)) {
            next();
        } else if (FINISH_BUTTON_ACTION_COMMAND.equals(ac)) {
            finish();
        } else if (CANCEL_BUTTON_ACTION_COMMAND.equals(ac)) {
            cancel();
        } else if (HELP_BUTTON_ACTION_COMMAND.equals(ac)) {
            help();
        }

    }
    /** Add a listener to this wizard.
     * @param listener a WizardListener object
     */
    public void addWizardListener(WizardListener listener) {
        listeners.put(listener, listener);
    }
    private void back() {

	WizardPanel wp = (WizardPanel) previous.pop();
	setPanel(wp);
	updateButtons();

	Stage currentStage = (Stage) stageList.get(currentStageIndex);
	if (!(currentStage.contains(wp))) {
	    currentStageIndex--;

	    // change the color of the previous stage to be inactive
	    Stage previousStage = currentStage;
	    JPanel previousStagePanel = (JPanel) stageMap.get(previousStage.getName());
	    previousStagePanel.setBackground(INACTIVE_BACKGROUND_COLOR);
	    Component[] previousComponents = previousStagePanel.getComponents();
	    for(int i = 0; i < previousComponents.length; i++) {
	        if(previousComponents[i] instanceof JLabel) {
		    JLabel previousStageLabel = (JLabel)previousComponents[i];
		    previousStageLabel.setForeground(INACTIVE_FOREGROUND_COLOR);
		    break;
	        }
	    }

	    // change the color of the current stage to be active
	    currentStage = (Stage) stageList.get(currentStageIndex);
	    JPanel currentStagePanel = (JPanel) stageMap.get(currentStage.getName());
	    currentStagePanel.setBackground(ACTIVE_BACKGROUND_COLOR);
	    Component[] currentComponents = currentStagePanel.getComponents();
	    for(int i = 0; i < currentComponents.length; i++) {
	        if(currentComponents[i] instanceof JLabel) {
		    JLabel currentStageLabel = (JLabel)currentComponents[i];
		    currentStageLabel.setForeground(ACTIVE_FOREGROUND_COLOR);
		    break;
	        }
	    }

	}

    }
    private void cancel() {

        Iterator iter = listeners.values().iterator();
        while(iter.hasNext()) {
            WizardListener listener = (WizardListener)iter.next();
            listener.wizardCancelled(this);
        }
    }
    private void finish() {

        ArrayList list = new ArrayList();
        if (current.validateFinish(list)) {
            current.finish();
            Iterator iter = listeners.values().iterator();
            while(iter.hasNext()) {
                WizardListener listener = (WizardListener)iter.next();
                listener.wizardFinished(this);
            }
        } else {
            showErrorMessages(list);
        }
    }
    private void help() {
        current.help();
    }
    private void next() {
	
	// start animation now.
	//System.out.println("Starting animation ...");
	//logoPanelCardLayout.show(logoPanel, "animatedLogoLabel");
	
	ArrayList list = new ArrayList();
	if (current.validateNext(list)) {
	    previous.push(current);
	    WizardPanel wp = current.next();
	    setPanel(wp);

	    if (null != wp) {
		wp.setWizardContext(ctx);
	    }

	    updateButtons();

	    // update the stage that is current
	    /* check to see if a new stage should be set as active. */
	    Stage currentStage = (Stage) stageList.get(currentStageIndex);
	    if (!(currentStage.contains(wp))) {
		currentStageIndex++;

		// change the color of the previous stage to be inactive
		Stage previousStage = currentStage;
		JPanel previousStagePanel = (JPanel) stageMap.get(previousStage.getName());
		previousStagePanel.setBackground(INACTIVE_BACKGROUND_COLOR);
		Component[] previousComponents = previousStagePanel.getComponents();
		for (int i = 0; i < previousComponents.length; i++) {
		    if (previousComponents[i] instanceof JLabel) {
			JLabel previousStageLabel = (JLabel) previousComponents[i];
			previousStageLabel.setForeground(INACTIVE_FOREGROUND_COLOR);
			break;
		    }
		}

		// change the color of the current stage to be active
		currentStage = (Stage) stageList.get(currentStageIndex);
		JPanel currentStagePanel = (JPanel) stageMap.get(currentStage.getName());
		currentStagePanel.setBackground(ACTIVE_BACKGROUND_COLOR);
		Component[] currentComponents = currentStagePanel.getComponents();
		for (int i = 0; i < currentComponents.length; i++) {
		    if (currentComponents[i] instanceof JLabel) {
			JLabel currentStageLabel = (JLabel) currentComponents[i];
			currentStageLabel.setForeground(ACTIVE_FOREGROUND_COLOR);
			break;
		    }
		}
	    }

	}
	else {
	    showErrorMessages(list);
	}
	
	// stop animation now.
	//logoPanelCardLayout.show(logoPanel, "logoLabel");
	//System.out.println("stopped animation.");
	
    }
    /**
     * A stage is a set of WizardPanel's that are grouped together to form a single unit
     * of the wizard.  Each stage will have a separate title box in the progress bar at the 
     * top of the wizard.  While the user walks back and forth through the wizard, the progress
     * bar will keep track of the active stage.
     *
     * @param stage edu.ksu.cis.bandera.bui.wizard.jwf.Stage
     */
    public void registerStage(Stage stage) {
	System.out.println("starting to register stage ...");
	
	if (stageList == null) {

	    stageList = new ArrayList();
	    stageMap = new HashMap();
	    currentStageIndex = 0;
	        
	    stagesPanel = new JPanel();
	    stagesPanel.setLayout(new GridBagLayout());
	    stagesPanel.setBackground(new Color(204, 204, 255));
	    JScrollPane stagesScrollPane = new JScrollPane(stagesPanel);
	    stagesScrollPane.setHorizontalScrollBarPolicy(stagesScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    stagesScrollPane.setVerticalScrollBarPolicy(stagesScrollPane.VERTICAL_SCROLLBAR_NEVER);

	    //add(stagesScrollPane, BorderLayout.NORTH);
	    add(stagesScrollPane, stagesPanelConstraints);
        
	}
	else {
	    try {
	    	String arrowIconFilename = "edu/ksu/cis/bandera/bui/wizard/jwf/images/arrow.gif";
	    	URL arrowIconURL = ClassLoader.getSystemClassLoader().getResource(arrowIconFilename);
	    	ImageIcon arrowImageIcon = new ImageIcon(arrowIconURL);
	    	JLabel arrowLabel = new JLabel(arrowImageIcon);
	    	stagesPanel.add(arrowLabel);
	    }
	    catch(Exception e) {
		JLabel arrowLabel = new JLabel("  >  ");
		stagesPanel.add(arrowLabel);
	    }
	}
    
	stageList.add(stage);

	// create a new panel and add it to the stages panel
	JLabel currentStageLabel = new JLabel(stage.getName());
	currentStageLabel.setForeground(INACTIVE_FOREGROUND_COLOR);
	currentStageLabel.setBackground(INACTIVE_BACKGROUND_COLOR);
	JPanel currentStagePanel = new JPanel();
	currentStagePanel.setBorder(new LineBorder(Color.black, 2));
	currentStagePanel.setForeground(INACTIVE_FOREGROUND_COLOR);
	currentStagePanel.setBackground(INACTIVE_BACKGROUND_COLOR);
	currentStagePanel.setLayout(new GridBagLayout());
	GridBagConstraints constraints = new GridBagConstraints();
	constraints.insets = new Insets(2, 4, 2, 4);
	currentStagePanel.add(currentStageLabel, constraints);
	stagesPanel.add(currentStagePanel);

	stageMap.put(stage.getName(), currentStagePanel);

	System.out.println("finished registering stage " + stage.getName() + ".");
    }
    /** Remove a listener from this wizard.
     * @param listener a WizardListener object
     */
    public void removeWizardListener(WizardListener listener) {
        listeners.remove(listener);
    }
    private void setPanel(WizardPanel wp) {
        if (null != current) {
            remove(current);
        }

        current = wp;
        if (null == current) {
            current = new NullWizardPanel();
        }
        //add(current, BorderLayout.CENTER);
	add(current, mainPanelConstraints);

	if(titleLabel != null) {
	    titleLabel.setText(wp.getTitle());
	}

        Iterator iter = listeners.values().iterator();
        while(iter.hasNext()) {
            WizardListener listener = (WizardListener)iter.next();
            listener.wizardPanelChanged(this);
        }
        setVisible(true);
        revalidate();
        updateUI();
        current.display();
    }
    private void showErrorMessages(ArrayList list) {
        Window w = SwingUtilities.windowForComponent(this);
        JFrame frame = null;
        ErrorMessageBox errorMsgBox = null;

        if (w instanceof Frame) {
            errorMsgBox = new ErrorMessageBox((Frame)w);
        } else if (w instanceof Dialog) {
            errorMsgBox = new ErrorMessageBox((Dialog)w);
        } else {
            errorMsgBox = new ErrorMessageBox();
        }

        errorMsgBox.showErrorMessages(list);
    }
    /** Start this wizard with this panel. */
    public void start(WizardPanel wp) {
        previous = new Stack();
        ctx = new WizardContext();
        wp.setWizardContext(ctx);
        setPanel(wp);
        updateButtons();
        currentStageIndex = 0;

        // make sure to make the first stage active
        if((stageList != null) && (stageList.size() > 0)) {
	    Stage firstStage = (Stage)stageList.get(0);
	    JPanel firstStagePanel = (JPanel)stageMap.get(firstStage.getName());
	    firstStagePanel.setBackground(ACTIVE_BACKGROUND_COLOR);
            Component[] firstComponents = firstStagePanel.getComponents();
            for (int i = 0; i < firstComponents.length; i++) {
                if (firstComponents[i] instanceof JLabel) {
                    JLabel firstStageLabel = (JLabel) firstComponents[i];
                    firstStageLabel.setForeground(ACTIVE_FOREGROUND_COLOR);
                    break;
                }
            }
        }
        
    }
    private void updateButtons() {
        cancelButton.setEnabled(true);
        helpButton.setEnabled(current.hasHelp());
        backButton.setEnabled(previous.size() > 0);
        nextButton.setEnabled(current.hasNext());
        finishButton.setEnabled(current.canFinish());
    }
}
