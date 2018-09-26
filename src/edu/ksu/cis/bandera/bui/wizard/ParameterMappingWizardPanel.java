package edu.ksu.cis.bandera.bui.wizard;

import edu.ksu.cis.bandera.bui.wizard.jwf.*;
import java.util.*;
import javax.swing.*;
import java.awt.FlowLayout;
import edu.ksu.cis.bandera.specification.pattern.datastructure.Pattern;
import edu.ksu.cis.bandera.specification.predicate.datastructure.Predicate;
import org.apache.log4j.*;


/**
 * The ParameterMappingWizardPanel provides a JPanel that queries the user for variable names for parameters
 * of the predicates that they have selected already.  This will provide the quantification information necessary to
 * complete the temporal property specification.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 * @version $Revision: 1.3 $ - $Date: 2003/04/30 19:33:20 $
 */
public final class ParameterMappingWizardPanel extends BanderaAbstractWizardPanel {
	private JTextArea ivjMessageTextArea = null;
	private JPanel ivjParameterMappingPanel = null;
	private final static java.lang.String message = "Now that you have created expressions to fill in the holes in the selected pattern, you will need to name the parameters to each predicate in each expression.  By doing this, will be create a quantification for the property.  Be sure that you use the same variable name for each associated parameter.  Variable names must start with a character (a-zA-Z) and can contain letters, numbers, and some symbols (underscore and hyphen).";
	private JLabel ivjPatternLabel = null;
	private BoxLayout ivjParameterMappingPanelBoxLayout = null;
	private java.util.Map predicateMap;
	private final static Category log = Category.getInstance(ParameterMappingWizardPanel.class);
	private JScrollPane ivjParameterMappingScrollPane = null;
/**
 * ParameterMappingWizardPanel constructor comment.
 */
public ParameterMappingWizardPanel() {
	super();
	initialize();
}
/**
 * Return the MessageTextArea property value.
 * @return javax.swing.JTextArea
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextArea getMessageTextArea() {
	if (ivjMessageTextArea == null) {
		try {
			ivjMessageTextArea = new javax.swing.JTextArea();
			ivjMessageTextArea.setName("MessageTextArea");
			ivjMessageTextArea.setLineWrap(true);
			ivjMessageTextArea.setBorder(new javax.swing.border.EtchedBorder());
			ivjMessageTextArea.setWrapStyleWord(true);
			ivjMessageTextArea.setBackground(java.awt.Color.white);
			// user code begin {1}
			ivjMessageTextArea.setText(message);
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjMessageTextArea;
}
/**
 * Return the ParameterMappingPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getParameterMappingPanel() {
	if (ivjParameterMappingPanel == null) {
		try {
			ivjParameterMappingPanel = new javax.swing.JPanel();
			ivjParameterMappingPanel.setName("ParameterMappingPanel");
			ivjParameterMappingPanel.setLayout(getParameterMappingPanelBoxLayout());
			ivjParameterMappingPanel.setBounds(0, 0, 529, 215);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjParameterMappingPanel;
}
/**
 * Return the ParameterMappingPanelBoxLayout property value.
 * @return javax.swing.BoxLayout
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.BoxLayout getParameterMappingPanelBoxLayout() {
	javax.swing.BoxLayout ivjParameterMappingPanelBoxLayout = null;
	try {
		/* Create part */
		ivjParameterMappingPanelBoxLayout = new javax.swing.BoxLayout(getParameterMappingPanel(), javax.swing.BoxLayout.Y_AXIS);
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	};
	return ivjParameterMappingPanelBoxLayout;
}
/**
 * Return the ParameterMappingScrollPane property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getParameterMappingScrollPane() {
	if (ivjParameterMappingScrollPane == null) {
		try {
			ivjParameterMappingScrollPane = new javax.swing.JScrollPane();
			ivjParameterMappingScrollPane.setName("ParameterMappingScrollPane");
			getParameterMappingScrollPane().setViewportView(getParameterMappingPanel());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjParameterMappingScrollPane;
}
/**
 * Return the PatternLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getPatternLabel() {
	if (ivjPatternLabel == null) {
		try {
			ivjPatternLabel = new javax.swing.JLabel();
			ivjPatternLabel.setName("PatternLabel");
			ivjPatternLabel.setText("<Pattern>");
			ivjPatternLabel.setForeground(java.awt.Color.black);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjPatternLabel;
}
/**
 * Retrieve the selections that the user made for mapping parameter's onto quantified variable
 * names.  The Map that is returned will have the pattern parameter name as the key and the value
 * will be a List.  That List will contain Predicate objects as well as Strings.  The Predicate should
 * be followed by n Strings where n is the number of parameters the Predicate has.  Those Strings
 * are the names of the variables to use in the predicate.  These can be used, along with the type
 * information from the Predicate, to build a quantification.
 * 
 * @return java.util.Map
 */
public Map getVariableMap() {

	if(predicateMap == null) {
		log.warn("predicateMap is null.  Therefore, we will return since there are no variables.");
		return(null);
	}
	
	Map variableMap = new TreeMap();
	Iterator iterator = predicateMap.keySet().iterator();
	while(iterator.hasNext()) {
		Object parameterNameObject = iterator.next();
		if(!(parameterNameObject instanceof String)) {
			log.warn("current key (parameterNameObject) is not a String as expected, skipping it.");
			continue;
		}
		String parameterName = (String)parameterNameObject;

		Object predicateListObject = predicateMap.get(parameterName);
		if(!(predicateListObject instanceof List)) {
			log.warn("current value (predicateListObject) is not a List as expected, skipping it.");
			continue;
		}
		List predicateList = (List)predicateListObject;

		if((predicateList == null) || (predicateList.size() < 1)) {
			log.warn("current list (predicateList) is invalid.  It is either null or has a size less than 1.");
			continue;
		}
		ArrayList variableList = new ArrayList(predicateList.size());

		for(int i = 0; i < predicateList.size(); i++) {
			Object o = predicateList.get(i);
			if(o == null) {
				continue;
			}
			
			if(o instanceof Predicate) {
				variableList.add(o);
				continue;
			}
			
			if(o instanceof JTextField) {
				JTextField tf = (JTextField)o;
				variableList.add(tf.getText());
				continue;
			}

			if(o instanceof String) {
			    variableList.add((String)o);
			    continue;
			}

			// if all else fails, just print the type to the log so we can fix it later! -tcw
			log.debug("found an object in the list we don't know what to do with: " + o.getClass().getName());
		}

		log.debug("mapping the parameter name, " + parameterName + ", to a list of size = " + variableList.size());
		variableMap.put(parameterName, variableList);
	}

	return(variableMap);
}
/**
 * Called whenever the part throws an exception.
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {

	/* Uncomment the following lines to print uncaught exceptions to stdout */
	// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
	// exception.printStackTrace(System.out);
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("ParameterMappingWizardPanel");
		setLayout(new java.awt.GridBagLayout());
		setBackground(new java.awt.Color(204,204,255));
		setSize(537, 488);

		java.awt.GridBagConstraints constraintsMessageTextArea = new java.awt.GridBagConstraints();
		constraintsMessageTextArea.gridx = 0; constraintsMessageTextArea.gridy = 0;
		constraintsMessageTextArea.fill = java.awt.GridBagConstraints.BOTH;
		constraintsMessageTextArea.weightx = 1.0;
		constraintsMessageTextArea.weighty = 1.0;
		constraintsMessageTextArea.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getMessageTextArea(), constraintsMessageTextArea);

		java.awt.GridBagConstraints constraintsPatternLabel = new java.awt.GridBagConstraints();
		constraintsPatternLabel.gridx = 0; constraintsPatternLabel.gridy = 1;
		constraintsPatternLabel.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getPatternLabel(), constraintsPatternLabel);

		java.awt.GridBagConstraints constraintsParameterMappingScrollPane = new java.awt.GridBagConstraints();
		constraintsParameterMappingScrollPane.gridx = 0; constraintsParameterMappingScrollPane.gridy = 2;
		constraintsParameterMappingScrollPane.fill = java.awt.GridBagConstraints.BOTH;
		constraintsParameterMappingScrollPane.weightx = 1.0;
		constraintsParameterMappingScrollPane.weighty = 1.0;
		constraintsParameterMappingScrollPane.insets = new java.awt.Insets(4, 4, 4, 4);
		add(getParameterMappingScrollPane(), constraintsParameterMappingScrollPane);
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	setTitle("Name the Variables");
	// user code end
}
/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		JFrame frame = new javax.swing.JFrame();
		ParameterMappingWizardPanel aParameterMappingWizardPanel;
		aParameterMappingWizardPanel = new ParameterMappingWizardPanel();
		frame.setContentPane(aParameterMappingWizardPanel);
		frame.setSize(aParameterMappingWizardPanel.getSize());
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		frame.show();
		java.awt.Insets insets = frame.getInsets();
		frame.setSize(frame.getWidth() + insets.left + insets.right, frame.getHeight() + insets.top + insets.bottom);
		frame.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of edu.ksu.cis.bandera.bui.wizard.BanderaAbstractWizardPanel");
		exception.printStackTrace(System.out);
	}
}
/**
 * This will set the current parameter mapping using a Map.  The Map is expected
 * to contain keys that represent the parameters to the pattern and the value
 * will be the predicate selected for the pattern parameter.
 *
 * @param parameterMap java.util.Map
 */
public void setParameterMap(Map parameterMap) {

    /*
     * Can I re-use some of the labels: comma, open paren, close paren? -tcw
     */

    if (parameterMap == null) {
        return;
    }

    // lazy initialization ... if it is null, create a new TreeMap (so that it will be sorted!).
    //  otherwise, just clear it out.
    if (predicateMap == null) {
        predicateMap = new TreeMap();
    }
    else {
        predicateMap.clear();
    }

    Set keySet = parameterMap.keySet();
    Iterator iterator = keySet.iterator();
    while (iterator.hasNext()) {
        Object key = iterator.next();
        String currentParameterName = key.toString();

        JPanel currentPanel = new JPanel();
        currentPanel.setLayout(new FlowLayout());
        currentPanel.add(Box.createVerticalStrut(10));

        JLabel parameterNameLabel = new JLabel(currentParameterName);
        currentPanel.add(parameterNameLabel);
        currentPanel.add(Box.createVerticalStrut(5));
        ArrayList predicateAndTextFields = new ArrayList();
        List expressionList = (List) parameterMap.get(key);
        if ((expressionList != null) && (expressionList.size() > 0)) {
            StringBuffer expressionString = new StringBuffer();
            Iterator eli = expressionList.iterator();
            while (eli.hasNext()) {
                Object o = eli.next();
                if (o == null) {
                    continue;
                }

                if (o instanceof String) {
                    expressionString.append((String) o);
		    predicateAndTextFields.add((String)o);
		    continue;
                }

                if (o instanceof Predicate) {
                    Predicate currentPredicate = (Predicate) o;
                    expressionString.append(currentPredicate.getName().toString());
                    expressionString.append("(");
                    JLabel l = new JLabel(expressionString.toString());
                    currentPanel.add(l);

                    // generate the text fields for each predicate parameter
                    predicateAndTextFields.add(currentPredicate);

                    int parameterCount = currentPredicate.getNumOfParams();
                    log.debug("parameterCount = " + parameterCount);

                    if (!(currentPredicate.isStatic())) {
                        log.debug("currentPredicate is not static so the first parameter is 'this'.");

                        JTextField currentTextField = new JTextField(5);
                        currentPanel.add(currentTextField);
                        currentPanel.add(Box.createVerticalStrut(2));

                        predicateAndTextFields.add(currentTextField);

                        String currentParameterType = currentPredicate.getType().toString();
                        log.debug("currentParameterType = " + currentParameterType);
                        JLabel currentTypeLabel = new JLabel(" : " + currentParameterType);
                        currentPanel.add(currentTypeLabel);

                        // if this isn't the only one, put in a comma label
                        if (parameterCount > 0) {
                            JLabel commaLabel = new JLabel(",");
                            currentPanel.add(commaLabel);
                            currentPanel.add(Box.createVerticalStrut(1));
                        }
                    }

                    for (int i = 0; i < parameterCount; i++) {

                        JTextField currentTextField = new JTextField(5);
                        currentPanel.add(currentTextField);
                        currentPanel.add(Box.createVerticalStrut(2));

                        predicateAndTextFields.add(currentTextField);

                        String currentParameterType = currentPredicate.getParamType(i).toString();
                        log.debug("currentParameterType = " + currentParameterType);
                        JLabel currentTypeLabel = new JLabel(" : " + currentParameterType);
                        currentPanel.add(currentTypeLabel);

                        // if this isn't the last one, put in a comma label
                        if (i >= parameterCount) {
                            JLabel commaLabel = new JLabel(",");
                            currentPanel.add(commaLabel);
                            currentPanel.add(Box.createVerticalStrut(1));
                        }
                    }

                    expressionString = new StringBuffer();
                    expressionString.append(")");
                }
            }

            if ((expressionString != null) && (expressionString.length() > 0)) {
                // create and add the last label to the current panel
                JLabel lastLabel = new JLabel(expressionString.toString());
                currentPanel.add(lastLabel);
                currentPanel.add(Box.createVerticalStrut(10));
            }

        }
        else {
            // create a Label that says no parameters
            JLabel l = new JLabel("There is nothing in the expression for this parameter.");
            currentPanel.add(l);
        }

        // add this predicate and it's text fields to the collection to
        //  be queried later when we build the variable map! -tcw
        predicateMap.put(currentParameterName, predicateAndTextFields);

        // now add this current panel to the parameter map panel
        getParameterMappingPanel().add(currentPanel);
    }
}
/**
 * Set the current pattern being used in this wizard so that it can be
 * displayed for the user.
 * 
 * @param pattern java.lang.String
 */
public void setPattern(Pattern pattern) {
	getPatternLabel().setText(pattern.getFormat());
}
}
