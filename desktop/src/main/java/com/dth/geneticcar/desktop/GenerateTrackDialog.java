package com.dth.geneticcar.desktop;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.dth.geneticcar.desktop.utils.HtmlReader;

import net.miginfocom.swing.MigLayout;

public class GenerateTrackDialog extends JDialog {
    private static final long serialVersionUID = 6817645505581437387L;
    private static final Color ERRORCOLOR = new Color(255, 166, 166);
    private static Color ORIGINALCOLOR;

    JTextField filenameTextField, numberOfSegmentsTextField, segmentLengthTextField, maximumDiffTextField, deltaTextField;
    JButton okButton, cancelButton, helpButton;

    private int numberOfSegments;
    private float segmentLength, maximumDiff, delta;

    boolean value = false;

    private void okButtonActionPerformed() {
	value = true;

	segmentLengthTextField.setText(segmentLengthTextField.getText().replace(",", "."));
	maximumDiffTextField.setText(maximumDiffTextField.getText().replace(",", "."));
	deltaTextField.setText(deltaTextField.getText().replace(",", "."));

	if (filenameTextField.getText().equals("")) {
	    filenameTextField.setBackground(ERRORCOLOR);
	    value = false;
	} else {
	    filenameTextField.setBackground(ORIGINALCOLOR);
	}

	try {
	    numberOfSegments = Integer.parseInt(numberOfSegmentsTextField.getText());
	    numberOfSegmentsTextField.setBackground(ORIGINALCOLOR);
	} catch (NumberFormatException ex) {
	    numberOfSegmentsTextField.setBackground(ERRORCOLOR);
	    value = false;
	}

	try {
	    segmentLength = Float.parseFloat(segmentLengthTextField.getText());
	    segmentLengthTextField.setBackground(ORIGINALCOLOR);
	} catch (NumberFormatException ex) {
	    segmentLengthTextField.setBackground(ERRORCOLOR);
	    value = false;
	}

	try {
	    maximumDiff = Float.parseFloat(maximumDiffTextField.getText());
	    maximumDiffTextField.setBackground(ORIGINALCOLOR);
	} catch (NumberFormatException ex) {
	    maximumDiffTextField.setBackground(ERRORCOLOR);
	    value = false;
	}

	try {
	    delta = Float.parseFloat(deltaTextField.getText());
	    deltaTextField.setBackground(ORIGINALCOLOR);
	} catch (NumberFormatException ex) {
	    deltaTextField.setBackground(ERRORCOLOR);
	    value = false;
	}

	if (!value) {
	    JOptionPane.showMessageDialog(this, "W zaznaczonych polach brakuje wartości lub są one nieprawidłowe.", "Błąd", JOptionPane.WARNING_MESSAGE);
	}

	/*
	 * Additional checks.
	 */

	else if (numberOfSegments < 10) {
	    numberOfSegmentsTextField.setBackground(ERRORCOLOR);
	    JOptionPane.showMessageDialog(this, "Podano zbyt mało segmentów trasy.\nNależy wybrać liczbę większą lub równą 10.", "Błąd", JOptionPane.WARNING_MESSAGE);
	    value = false;
	} else if (segmentLength < 0.05f || segmentLength > 10.0f) {
	    segmentLengthTextField.setBackground(ERRORCOLOR);
	    JOptionPane.showMessageDialog(this, "Podano niew�a�ciw� d�ugo�� segmentu.\nDopuszczalne warto�ci zawieraj� si� pomi�dzy 0.05 a 10.", "Błąd", JOptionPane.WARNING_MESSAGE);
	    value = false;
	} else if (maximumDiff < 0 || maximumDiff > 10.0f) {
	    maximumDiffTextField.setBackground(ERRORCOLOR);
	    JOptionPane.showMessageDialog(this, "Podano niew�a�ciw� maksymaln� r�nic� wysoko�ci.\nNale�y poda� warto�� wi�ksz� lub r�wn� 0, ale mniejsz� od 10.", "Błąd", JOptionPane.WARNING_MESSAGE);
	    value = false;
	} else if (delta < 0 || delta > 0.5f) {
	    deltaTextField.setBackground(ERRORCOLOR);
	    JOptionPane.showMessageDialog(this, "Podano niew�a�ciw� warto�� delty.\nDopuszczalne warto�ci zawieraj� si� pomi�dzy 0 a 0.5.", "Błąd", JOptionPane.WARNING_MESSAGE);
	    value = false;
	}

	if (value) {
	    setVisible(false);
	}
    }

    private void cancelButtonActionPerformed() {
	value = false;
	setVisible(false);
    }

    private void helpButtonActionPerformed() {
	HelpWindow help = new HelpWindow(this,
	    HtmlReader.readHtml("res/generator_tras.htm")
	    , true);
	help.setVisible(true);
    }

    // --------------------------------------------------
    // Public methods
    // --------------------------------------------------

    public String getFilename() {
	return filenameTextField.getText();
    }

    public int getNumberOfSegments() {
	return numberOfSegments;
    }

    public float getSegmentLength() {
	return segmentLength;
    }

    public float getMaximumDiff() {
	return maximumDiff;
    }

    public float getDelta() {
	return delta;
    }

    // --------------------------------------------------
    // UI code
    // --------------------------------------------------

    public GenerateTrackDialog(JFrame parent) {
	setTitle("Generator tras");

	JPanel panel = new JPanel(new MigLayout());

	panel.add(new JLabel("Nazwa pliku:"));
	filenameTextField = new JTextField("trasa.txt", 10);
	panel.add(filenameTextField, "wrap");

	panel.add(new JLabel("Liczba segmentów:"));
	numberOfSegmentsTextField = new JTextField(10);
	panel.add(numberOfSegmentsTextField, "wrap");

	panel.add(new JLabel("Długość segmentu:"));
	segmentLengthTextField = new JTextField(10);
	panel.add(segmentLengthTextField, "wrap");

	panel.add(new JLabel("Maksymalna różnica:"));
	maximumDiffTextField = new JTextField(10);
	panel.add(maximumDiffTextField, "wrap");

	panel.add(new JLabel("Delta:"));
	deltaTextField = new JTextField(10);
	panel.add(deltaTextField, "wrap");

	okButton = new JButton("Ok");
	okButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
		okButtonActionPerformed();
	    }
	});
	panel.add(okButton, "span, split, align right");

	cancelButton = new JButton("Anuluj");
	cancelButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
		cancelButtonActionPerformed();
	    }
	});
	panel.add(cancelButton);

	helpButton = new JButton("Pomoc");
	helpButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
		helpButtonActionPerformed();
	    }
	});
	panel.add(helpButton);

	add(panel);
	pack();
	setLocationRelativeTo(parent);

	ORIGINALCOLOR = filenameTextField.getBackground();
    }
}
