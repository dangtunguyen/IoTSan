package edu.ksu.cis.bandera.bui.wizard.jwf;

import java.awt.Frame;
import java.awt.Dialog;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;

/** Displays a list of error messages and blocks until ok is pressed.
 * @author Christopher Brind
 */
public class ErrorMessageBox extends JDialog implements ActionListener {

    private final JTextPane textPane = new JTextPane();

    /** Construct a dialog with no parent. */
    public ErrorMessageBox() {
    }
    /** Construct a dialog with a dialog parent. */
    public ErrorMessageBox(Dialog dialog) {
        super(dialog, "messages", true);
        init();
        center(dialog);
    }
    /** Construct a dialog with a frame parent. */
    public ErrorMessageBox(Frame frame) {
        super(frame, "messages", true);
        init();
        center(frame);
    }
    /** Handles the ok press. */
    public void actionPerformed(ActionEvent ae) {
        if ("ok".equals(ae.getActionCommand())) {
            setVisible(false);
        }
    }
    private void center(Window window) {

        int x = 0;
        int y = 0;

        x = window.getLocation().x +
            (window.getSize().width / 2) -
            (this.getSize().width / 2);
        y = window.getLocation().y +
            (window.getSize().height / 2) -
            (this.getSize().height / 2);

        setLocation(x, y);
    }
    private void init() {
        Container c = getContentPane();
        c.setLayout(new BorderLayout());
        c.add(new JScrollPane(textPane), BorderLayout.CENTER);
        JButton button = new JButton("ok");
        button.addActionListener(this);
        c.add(button, BorderLayout.SOUTH);
        textPane.setEditable(false);
        setSize(200, 200);
    }
    /** Show a list of messages and block until ok is pressed.
     * @param list a List of String objects.
     */
    public void showErrorMessages(List list) {
        String theText = "";
        Iterator iter = list.iterator();
        while(iter.hasNext()) {
            String s = (String)iter.next();
            theText = theText + s + (iter.hasNext() ? "\n" : "");
        }
        textPane.setText(theText);
        setVisible(true);
    }
}
