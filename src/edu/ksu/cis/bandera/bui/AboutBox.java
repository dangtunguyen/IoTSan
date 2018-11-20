package edu.ksu.cis.bandera.bui;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Bandera, a Java(TM) analysis and transformation toolkit           *
 * Copyright (C) 1998-2001 SAnToS Laboratories (santos@cis.ksu.edu)  *

 * All rights reserved.                                              *
 *                                                                   *
 * This work was done as a project in the SAnToS Laboratory,         *
 * Department of Computing and Information Sciences, Kansas State    *
 * University, USA (http://www.cis.ksu.edu/santos).                  *
 * It is understood that any modification not identified as such is  *
 * not covered by the preceding statement.                           *
 *                                                                   *
 * This work is free software; you can redistribute it and/or        *
 * modify it under the terms of the GNU Library General Public       *
 * License as published by the Free Software Foundation; either      *
 * version 2 of the License, or (at your option) any later version.  *
 *                                                                   *
 * This work is distributed in the hope that it will be useful,      *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of    *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU *
 * Library General Public License for more details.                  *
 *                                                                   *
 * You should have received a copy of the GNU Library General Public *
 * License along with this toolkit; if not, write to the             *
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,      *
 * Boston, MA  02111-1307, USA.                                      *
 *                                                                   *
 * Java is a trademark of Sun Microsystems, Inc.                     *
 *                                                                   *
 * To submit a bug report, send a comment, or get the latest news on *
 * this project and other SAnToS projects, please visit the web-site *
 *                http://www.cis.ksu.edu/santos                      *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JLabel;

import java.net.URL;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

import edu.ksu.cis.bandera.BanderaInfo;

/**
  * The AboutBox class provides a little window that shows a simple message,
  * the Bandera logo, and the URL to the Bandera project at the SAnToS
  * Laboratory at Kansas State University.
  *
  * @author Original Author?
  * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
  */
public class AboutBox extends javax.swing.JDialog {
	private javax.swing.JPanel ivjJDialogContentPane = null;
	private javax.swing.JPanel ivjJPanel1 = null;
	private javax.swing.JButton ivjOKButton = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	private javax.swing.JLabel ivjJLabel1 = null;
	private javax.swing.JTextArea ivjBanderaMsg = null;
	private javax.swing.JScrollPane ivjJScrollPane1 = null;
	private javax.swing.JLabel ivjBuildInfoLabel = null;
	private javax.swing.JPanel ivjBuildInfoPanel = null;

class IvjEventHandler implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == AboutBox.this.getOKButton()) 
				connEtoC1();
		};
	};
    /**
     * Create a new AboutBox using default values.
     */
    public AboutBox() {
        super();
        initialize();
    }
    /**
     * AboutBox constructor comment.
     * @param owner java.awt.Dialog
     */
    private AboutBox(java.awt.Dialog owner) {
        super(owner);
    }
    /**
     * AboutBox constructor comment.
     * @param owner java.awt.Dialog
     * @param title java.lang.String
     */
    private AboutBox(java.awt.Dialog owner, String title) {
        super(owner, title);
    }
    /**
     * AboutBox constructor comment.
     * @param owner java.awt.Dialog
     * @param title java.lang.String
     * @param modal boolean
     */
    private AboutBox(java.awt.Dialog owner, String title, boolean modal) {
        super(owner, title, modal);
    }
    /**
     * AboutBox constructor comment.
     * @param owner java.awt.Dialog
     * @param modal boolean
     */
    private AboutBox(java.awt.Dialog owner, boolean modal) {
        super(owner, modal);
    }
    /**
     * AboutBox constructor comment.
     * @param owner java.awt.Frame
     */
    private AboutBox(java.awt.Frame owner) {
        super(owner);
    }
    /**
     * AboutBox constructor comment.
     * @param owner java.awt.Frame
     * @param title java.lang.String
     */
    private AboutBox(java.awt.Frame owner, String title) {
        super(owner, title);
    }
    /**
     * AboutBox constructor comment.
     * @param owner java.awt.Frame
     * @param title java.lang.String
     * @param modal boolean
     */
    private AboutBox(java.awt.Frame owner, String title, boolean modal) {
        super(owner, title, modal);
    }
    /**
     * AboutBox constructor comment.
     * @param owner java.awt.Frame
     * @param modal boolean
     */
    private AboutBox(java.awt.Frame owner, boolean modal) {
        super(owner, modal);
    }
    /**
     * connEtoC1:  (OKButton.action. --> AboutBox.oKButton()V)
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void connEtoC1() {
        try {
            // user code begin {1}
            // user code end
            this.oKButton();
            // user code begin {2}
            // user code end
        }
        catch (java.lang.Throwable ivjExc) {
            // user code begin {3}
            // user code end
            handleException(ivjExc);
        }
    }
    /**
     * Return the JTextArea1 property value.
     * @return javax.swing.JTextArea
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JTextArea getBanderaMsg() {
	if (ivjBanderaMsg == null) {
		try {
			ivjBanderaMsg = new javax.swing.JTextArea();
			ivjBanderaMsg.setName("BanderaMsg");
			ivjBanderaMsg.setLineWrap(true);
			ivjBanderaMsg.setWrapStyleWord(true);
			ivjBanderaMsg.setText("");
			ivjBanderaMsg.setBackground(new java.awt.Color(204,204,204));
			ivjBanderaMsg.setRows(0);
			ivjBanderaMsg.setBounds(0, 0, 158, 228);
			ivjBanderaMsg.setNextFocusableComponent(getOKButton());
			ivjBanderaMsg.setEditable(false);
			ivjBanderaMsg.setRequestFocusEnabled(true);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBanderaMsg;
}
/**
 * Return the BuildInfoLabel property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getBuildInfoLabel() {
	if (ivjBuildInfoLabel == null) {
		try {
			ivjBuildInfoLabel = new javax.swing.JLabel();
			ivjBuildInfoLabel.setName("BuildInfoLabel");
			ivjBuildInfoLabel.setText("v0.2 17 March 2002");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBuildInfoLabel;
}
/**
 * Return the BuildInfoPanel property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getBuildInfoPanel() {
	if (ivjBuildInfoPanel == null) {
		try {
			ivjBuildInfoPanel = new javax.swing.JPanel();
			ivjBuildInfoPanel.setName("BuildInfoPanel");
			ivjBuildInfoPanel.setLayout(new java.awt.FlowLayout());
			ivjBuildInfoPanel.setBackground(new java.awt.Color(204,204,255));
			getBuildInfoPanel().add(getBuildInfoLabel(), getBuildInfoLabel().getName());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjBuildInfoPanel;
}
    /**
     * Return the JDialogContentPane property value.
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getJDialogContentPane() {
	if (ivjJDialogContentPane == null) {
		try {
			ivjJDialogContentPane = new javax.swing.JPanel();
			ivjJDialogContentPane.setName("JDialogContentPane");
			ivjJDialogContentPane.setLayout(new java.awt.GridBagLayout());
			ivjJDialogContentPane.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsJPanel1 = new java.awt.GridBagConstraints();
			constraintsJPanel1.gridx = 0; constraintsJPanel1.gridy = 2;
			constraintsJPanel1.fill = java.awt.GridBagConstraints.BOTH;
			constraintsJPanel1.weightx = 1.0;
			constraintsJPanel1.weighty = 1.0;
			constraintsJPanel1.insets = new java.awt.Insets(4, 4, 4, 4);
			getJDialogContentPane().add(getJPanel1(), constraintsJPanel1);

			java.awt.GridBagConstraints constraintsOKButton = new java.awt.GridBagConstraints();
			constraintsOKButton.gridx = 0; constraintsOKButton.gridy = 3;
			constraintsOKButton.insets = new java.awt.Insets(4, 4, 4, 4);
			getJDialogContentPane().add(getOKButton(), constraintsOKButton);

			java.awt.GridBagConstraints constraintsJLabel1 = new java.awt.GridBagConstraints();
			constraintsJLabel1.gridx = 0; constraintsJLabel1.gridy = 0;
			constraintsJLabel1.insets = new java.awt.Insets(4, 4, 4, 4);
			getJDialogContentPane().add(getJLabel1(), constraintsJLabel1);

			java.awt.GridBagConstraints constraintsBuildInfoPanel = new java.awt.GridBagConstraints();
			constraintsBuildInfoPanel.gridx = 0; constraintsBuildInfoPanel.gridy = 1;
			constraintsBuildInfoPanel.fill = java.awt.GridBagConstraints.BOTH;
			constraintsBuildInfoPanel.weightx = 1.0;
			constraintsBuildInfoPanel.weighty = 0.1;
			constraintsBuildInfoPanel.insets = new java.awt.Insets(4, 4, 4, 4);
			getJDialogContentPane().add(getBuildInfoPanel(), constraintsBuildInfoPanel);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJDialogContentPane;
}
    /**
     * Return the JLabel1 property value.
     * @return javax.swing.JLabel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JLabel getJLabel1() {
	if (ivjJLabel1 == null) {
		try {
			ivjJLabel1 = new javax.swing.JLabel();
			ivjJLabel1.setName("JLabel1");
			ivjJLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edu/ksu/cis/bandera/bui/images/santos-logo.gif")));
			ivjJLabel1.setText("");
			ivjJLabel1.setRequestFocusEnabled(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJLabel1;
}
    /**
     * Return the JPanel1 property value.
     * @return javax.swing.JPanel
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JPanel getJPanel1() {
	if (ivjJPanel1 == null) {
		try {
			ivjJPanel1 = new javax.swing.JPanel();
			ivjJPanel1.setName("JPanel1");
			ivjJPanel1.setLayout(new java.awt.GridBagLayout());
			ivjJPanel1.setBackground(new java.awt.Color(204,204,255));

			java.awt.GridBagConstraints constraintsJScrollPane1 = new java.awt.GridBagConstraints();
			constraintsJScrollPane1.gridx = 0; constraintsJScrollPane1.gridy = 0;
			constraintsJScrollPane1.fill = java.awt.GridBagConstraints.BOTH;
			constraintsJScrollPane1.weightx = 1.0;
			constraintsJScrollPane1.weighty = 1.0;
			constraintsJScrollPane1.insets = new java.awt.Insets(4, 4, 4, 4);
			getJPanel1().add(getJScrollPane1(), constraintsJScrollPane1);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJPanel1;
}
    /**
     * Return the JScrollPane1 property value.
     * @return javax.swing.JScrollPane
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JScrollPane getJScrollPane1() {
        if (ivjJScrollPane1 == null) {
            try {
                ivjJScrollPane1 = new javax.swing.JScrollPane();
                ivjJScrollPane1.setName("JScrollPane1");
                getJScrollPane1().setViewportView(getBanderaMsg());
                // user code begin {1}
                // user code end
            }
            catch (java.lang.Throwable ivjExc) {
                // user code begin {2}
                // user code end
                handleException(ivjExc);
            }
        }
        return ivjJScrollPane1;
    }
    /**
     * Return the OKButton property value.
     * @return javax.swing.JButton
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private javax.swing.JButton getOKButton() {
	if (ivjOKButton == null) {
		try {
			ivjOKButton = new javax.swing.JButton();
			ivjOKButton.setName("OKButton");
			ivjOKButton.setText("     O K     ");
			ivjOKButton.setBackground(new java.awt.Color(204,204,255));
			ivjOKButton.setNextFocusableComponent(getBanderaMsg());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjOKButton;
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
     * Initializes connections
     * @exception java.lang.Exception The exception description.
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void initConnections() throws java.lang.Exception {
        // user code begin {1}
        // user code end
        getOKButton().addActionListener(ivjEventHandler);
    }
    /**
     * Initialize the class.
     */
    /* WARNING: THIS METHOD WILL BE REGENERATED. */
    private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("AboutBox");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setSize(532, 670);
		setTitle("About");
		setContentPane(getJDialogContentPane());
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
        initializeBanderaMessage();
        initializeBuildInfoLabel();
	// user code end
}
    /**
     * Initialize the message that is seen in the about box.  First it will
     * attempt to load this from an external source.  If that fails (file isn't
     * found, improper format, etc.) it will use a hard-coded message.
     *
     * Creation date: (3/12/2002 9:11:43 PM)
     */
    private void initializeBanderaMessage() {

        StringBuffer message = new StringBuffer();

        try {
            // first try to find the external source to use.
            String messageFilename = "edu/ksu/cis/bandera/bui/aboutBoxMessage.txt";
            URL messageURL =
                ClassLoader.getSystemClassLoader().getResource(messageFilename);
            if (messageURL != null) {
                // read the file into the message string buffer
                File messageFile = new File(messageURL.getFile());

                // make sure that we are using a valid file: not null, file (not dir), and readable
                if ((messageFile != null)
                    && (messageFile.isFile())
                    && (messageFile.canRead())) {
                    FileReader fr = new FileReader(messageFile);
                    BufferedReader br = new BufferedReader(fr);
                    while (br.ready()) {
                        String temp = br.readLine();
                        message.append(temp);
                        message.append("\n");
                    }
                }
                else {
                    // setting message to null will cause the default message to be used.
                    message = null;
                }
            }
            else {
                message = null;
            }
        }
        catch (Exception e) {
            message = null;
        }

        // if the message is not set already, use the default, hard-coded message
        if (message == null) {
            message = new StringBuffer();
            message.append(
                "Bandera is a open-source tool kit for model-checking concurrent Java software.");
            message.append(
                "Bandera is developed and maintained primarily by the researchers at ");
            message.append(
                "the SAnToS Laboratory at Kansas State University and supported by ");
            message.append(
                "signficant collaboration with researchers from the Automated Software ");
            message.append(
                "Engineering Group at NASA Ames and the LASER Group at the University ");
            message.append("of Massachussetts at Amherst.");
            message.append("\n");
            message.append("\n");
            message.append("Bandera is distributed under a General Public License (GPL).");
            message.append("\n");
            message.append("\n");
            message.append(
                "For current information about the Bandera project, please consult the project URL above.");
            message.append("\n");
            message.append("\n");

            message.append(
                "Research and development of Bandera has been supported by the following grants and contracts:");
            message.append("\n");
            message.append("- Army Research Office CIP/SW URI award DAAD190110564. ");
            message.append("\n");
            message.append(
                "- Cooperative Agreement, NCC-1-399, sponsored by Honeywell Technology Center and NASA Langley Research Center.");
            message.append("\n");
            message.append("- DARPA/AFRL project AFRL-F33615-00-C-3044.");
            message.append("\n");
            message.append("- NASA Ames Laboratory award NAG-02-1209.");
            message.append("\n");
            message.append("- NSF-EIA Experimental Software Systems (ESS) award 9708184.");
            message.append("\n");
            message.append("- NSF-CCR CAREER award 9703094.");
            message.append("\n");
            message.append("- NSF-CCR CAREER award 9896354.");
            message.append("\n");
            message.append("- NSF-CCR Postdoctoral Associate award 9901605.");
            message.append("\n");
            message.append(
                "- NSF-CCR/DARPA Evolutionary Design of Complex Systems (EDCS) award 9633388.");
            message.append("\n");
            message.append("- Rockwell-Collins Advanced Technology Center.");
            message.append("\n");
            message.append(
                "- Sun Microsystems Academic Equipment Grant EDUD-7824-000130-US.");

        }

        // assume: message is not null and contains a meaningful message.

        getBanderaMsg().setText(message.toString());
	getBanderaMsg().setCaretPosition(0);

    }
/**
 * Insert the method's description here.
 * Creation date: (3/17/2002 8:28:59 PM)
 */
private void initializeBuildInfoLabel() {

	String version = "0.2";
	String buildDate = "01 January 2002";

	try {
		version = BanderaInfo.buildVersion;
		buildDate = BanderaInfo.buildDate;
	}
	catch(Exception e) {
	}

	JLabel l = getBuildInfoLabel();
	l.setText("v" + version + " " + buildDate);
	
}
    /**
     * This is a simple test mechanism for the AboutBox.  Running the main method
     * will create a new AboutBox and set it to be visible to the user.  It will also
     * make it so that the default close operation calls System.exit(0) so that all the
     * Java Swing threads will be stopped.
     *
     * @param args java.lang.String[]
     */
    public static void main(java.lang.String[] args) {
        try {
            AboutBox aAboutBox;
            aAboutBox = new AboutBox();
            aAboutBox.setModal(true);
            aAboutBox.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                };
            });
            aAboutBox.setVisible(true);
        }
        catch (Throwable exception) {
            System.err.println("Exception occurred in main() of javax.swing.JDialog");
            exception.printStackTrace(System.out);
        }
    }
    /**
     * This method is the event handler for the ok button in the AboutBox.  It will
     * cause the window to be disposed of.
     */
    public void oKButton() {
        this.dispose();
    }
}
