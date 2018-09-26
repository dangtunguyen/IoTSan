package edu.ksu.cis.bandera.bui;

import javax.swing.JLabel;
import javax.swing.JWindow;
import java.awt.Frame;
import java.awt.Dimension;
import javax.swing.ImageIcon;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.Color;
import java.net.URL;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.JTextArea;

import edu.ksu.cis.bandera.BanderaInfo;

/**
 * A simple splash screen that pops up in the center of the screen.  It depends on
 * a URL to provide the picture that it will use.
 *
 * @author Todd Wallentine &lt;tcw@cis.ksu.edu&gt;
 */
public class SplashScreen extends JWindow {

    /**
     * Create a new SplashScreen window given a URL for an image to use.
     */
    public SplashScreen(URL imageIconURL) {
        this(imageIconURL, null);
    }
    
    /**
     * Create a new SplashScreen window given a parent frame and a URL for
     * an image to use.
     */
    public SplashScreen(URL imageIconURL, Frame frame) {
        super(frame);

	// assume: URL is valid (not null and readable)

	// create the image based upon the given URL
        ImageIcon imageIcon = new ImageIcon(imageIconURL);
        JLabel l = new JLabel(imageIcon);
        getContentPane().add(l, BorderLayout.CENTER);

	// add a little text message below the image
	/* HACK: The message is assumed to be no more than the width of
	 * the picture.  If this isn't the case, the SplashScreen will look
	 * rather funny.  This needs to be fixed.
	 */
	String message = getMessage();
	JTextArea text = new JTextArea(message);
        getContentPane().add(text, BorderLayout.SOUTH);

	// add a black border around this window pane
        getRootPane().setBorder(new LineBorder(Color.black, 5));

        pack();

	// center the splash screen on the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension labelSize = l.getPreferredSize();
        setLocation(
            screenSize.width / 2 - (labelSize.width / 2),
            screenSize.height / 2 - (labelSize.height / 2));
        screenSize = null;
        labelSize = null;
    }

    /**
     * Get the message that will be displayed below the splash screen picture.
     */
    private static String getMessage() {
	String buildDate = "01 January 2003";
	String buildVersion = "0.3";

	try {
	    buildDate = BanderaInfo.buildDate;
	    buildVersion = BanderaInfo.buildVersion;
	}
	catch(Exception e) {
	    System.err.println("exception while getting the build info.");
	}

	String message = "Bandera (v" + buildVersion + ") built on " + buildDate + ".\n" +
	    "Copyright 1999-2003 by SAnToS Laboratory, Kansas State University";
	return(message);
    }

    /**
     * The main method provides a very simple test of the splash screen by loading
     * up a URL of a GIF image that should be found in the Bandera system:
     * <i>edu/ksu/cis/bandera/bui/images/santos-logo.gif</i>
     */
    public static void main(String[] args) {
       // simple test of the SplashScreen
       System.out.println("Starting the SplashScreen test ...");

	String imageFilename = "edu/ksu/cis/bandera/bui/images/santos-logo.gif";
        URL url = ClassLoader.getSystemClassLoader().getResource(imageFilename);
        
        if(url != null) {
	        SplashScreen ss = new SplashScreen(url);
	        ss.setVisible(true);
        }
        else {
	        System.out.println("Cannot load the url.");
        }

        System.out.println("Finished the SplashScreen test.");
	    
    }
}
