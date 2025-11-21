package com.dth.geneticcar.desktop;

import java.awt.Component;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class HelpWindow extends JDialog {
    private static final long serialVersionUID = -7898465796326087757L;

    // --------------------------------------------------
    // UI code
    // --------------------------------------------------

    public HelpWindow(Component parent, String text, boolean modal) {
	setTitle("Pomoc");

	setModal(modal);

	JTextPane pane = new JTextPane();
	pane.setContentType("text/html");
	pane.setEditable(false);
	pane.setText(text);

	JScrollPane panel = new JScrollPane(pane);

	add(panel);

	pack();
	setSize(400, 400);
	setLocationRelativeTo(parent);
    }
}
