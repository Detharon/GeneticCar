package com.dth.geneticcar.desktop;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import com.dth.geneticcar.desktop.utils.ChromosomeValidator;
import com.dth.geneticcar.desktop.utils.HtmlReader;
import com.dth.geneticcar.desktop.utils.SpecCreator;
import com.dth.geneticcar.datatype.CarChromosome;
import com.dth.geneticcar.datatype.FixtureSpecs;
import com.dth.geneticcar.datatype.AlgorithmSettings;
import com.dth.geneticcar.datatype.IndividualConstraints;
import com.dth.geneticcar.entities.CarChromosomeGenerator;

import net.miginfocom.swing.MigLayout;

public class NewAlgorithmDialog extends JDialog {
    private static final long serialVersionUID = -3997620731223391377L;
    public static final int OK = 1;
    private static final Color ERRORCOLOR = new Color(255, 166, 166);
    private static Color ORIGINALCOLOR;

    JButton okButton, cancelButton, helpButton, wczytajButton, defaultSpecButton, loadSpecButton, saveSpecButton;
    JButton generate, loadPopButton, savePopButton, removeAllButton;
    JTextField minCarDensity, maxCarDensity, minCarFriction, maxCarFriction, minCarRestitution, maxCarRestitution, minSegmentLength, maxSegmentLength, segments;
    ;
    JTextField minWheelDensity, maxWheelDensity, minWheelFriction, maxWheelFriction, minWheelRestitution, maxWheelRestitution, minWheelRadius, maxWheelRadius, wheels, genNumber;
    JSpinner crossoverProbabilitySpinner, mutationProbabilitySpinner, gravitySpinner, tournamentSizeSpinner;
    JComboBox<String> crossoverMethodComboBox, selectionMethodComboBox, fitnessMethodComboBox;
    JCheckBox eliteSelectionCheckBox;
    JLabel selectionLabel1, selectionLabel2;

    JTable table;
    DefaultTableModel tableModel;

    IndividualConstraints specs;

    JCheckBox fillSpecs;

    private AlgorithmSettings settings;
    private int value = 0;

    private void defaultSpecButtonActionPerformed() {
	minCarDensity.setText("2.0");
	maxCarDensity.setText("2.0");

	minCarFriction.setText("0.4");
	maxCarFriction.setText("0.4");

	minCarRestitution.setText("0.4");
	maxCarRestitution.setText("0.4");

	minSegmentLength.setText("0.2");
	maxSegmentLength.setText("1");

	segments.setText("6");

	minWheelDensity.setText("2.0");
	maxWheelDensity.setText("2.0");

	minWheelFriction.setText("0.4");
	maxWheelFriction.setText("0.4");

	minWheelRestitution.setText("0.4");
	maxWheelRestitution.setText("0.4");

	minWheelRadius.setText("0.1");
	maxWheelRadius.setText("0.25");

	wheels.setText("2");
    }

    private void saveSpecButtonActionPerformed() {
	if (!validateSpecs()) return;
	createSpecs();

	JFileChooser fc = new JFileChooser("./Specs");
	int returnVal = fc.showSaveDialog(this);
	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    ObjectOutputStream os = null;

	    try {
		os = new ObjectOutputStream(new FileOutputStream(fc.getSelectedFile()));
		os.writeObject(specs);
	    } catch (Exception e) {
		e.printStackTrace();
	    } finally {
		if (os != null)
		    try {
			os.close();
		    } catch (IOException e) {
			e.printStackTrace();
		    }
	    }
	}
    }

    private void loadSpecButtonActionPerformed() {
	JFileChooser fc = new JFileChooser("./Specs");
	int returnVal = fc.showOpenDialog(this);
	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    ObjectInputStream os = null;

	    try {
		os = new ObjectInputStream(new FileInputStream(fc.getSelectedFile()));
		specs = (IndividualConstraints) os.readObject();
	    } catch (Exception e) {
		e.printStackTrace();
	    } finally {
		if (os != null)
		    try {
			os.close();
		    } catch (IOException e) {
			e.printStackTrace();
		    }
	    }
	    specsToFields(specs);
	}
    }

    private void okButtonActionPerformed() {
	boolean valid = validateSpecs();
	if (!valid) return;
	valid = isPopulationValid();
	if (!valid) return;
	valid = validateSettings();
	if (!valid) return;

	createSpecs();
	setSettings();
	value = OK;
	dispose();
    }

    private void cancelButtonActionPerformed() {
	dispose();
    }

    private void helpButtonActionPerformed() {
	HelpWindow help = new HelpWindow(this,
	    HtmlReader.readHtml("nowy_algorytm.html")
	    , true);
	help.setVisible(true);
    }

    private void generateButtonActionPerformed() {
	boolean valid = validateGenerateButton();
	if (!valid) return;
	valid = validateSpecs();
	if (!valid) return;

	createSpecs();

	CarChromosomeGenerator gen = new CarChromosomeGenerator(specs);

	for (int i = 0; i < Integer.parseInt(genNumber.getText()); i++) {
	    tableModel.addRow(
		new Object[]{
		    tableModel.getRowCount(),
		    gen.generateCarChromosome().toString()
		}
	    );
	}
    }

    private void loadPopButtonActionPerformed() {
	JFileChooser fc = new JFileChooser("./Populations");
	int returnVal = fc.showOpenDialog(this);

	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    BufferedReader br = null;

	    try {
		br = new BufferedReader(new FileReader(fc.getSelectedFile()));

		ArrayList<String> cars = new ArrayList<>();
		String s;
		while ((s = br.readLine()) != null) {
		    s = s.trim();
		    cars.add(s);
		}

		for (String car : cars) {
		    tableModel.addRow(
			new Object[]{
			    tableModel.getRowCount(),
			    car
			}
		    );
		}

	    } catch (NumberFormatException ex) {
		ex.printStackTrace();
		JOptionPane.showMessageDialog(this, "Plik zawiera niew�a�ciwe warto�ci.", "B��d", JOptionPane.WARNING_MESSAGE);
	    } catch (Exception ex) {
		ex.printStackTrace();
	    } finally {
		if (br != null) {
		    try {
			br.close();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		}
	    }
	}

	if (isPopulationValid() && fillSpecs.isSelected()) fillSpecs();
    }

    private void savePopButtonActionPerformed() {
	JFileChooser fc = new JFileChooser("./Populations");
	int returnVal = fc.showSaveDialog(this);

	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    BufferedWriter br = null;

	    try {
		br = new BufferedWriter(new FileWriter(fc.getSelectedFile()));

		for (int i = 0; i < tableModel.getRowCount(); i++) {
		    br.write((String) tableModel.getValueAt(i, 1));
		    br.write("\r\n");
		}
	    } catch (Exception e) {
		e.printStackTrace();
	    } finally {
		if (br != null)
		    try {
			br.close();
		    } catch (IOException e) {
			e.printStackTrace();
		    }
	    }

	}
    }

    private void savePopupActionPerformed() {
	JFileChooser fc = new JFileChooser("./Populations");
	int returnVal = fc.showSaveDialog(this);

	if (returnVal == JFileChooser.APPROVE_OPTION) {

	    int l = table.getSelectedRowCount();
	    int[] rows = table.getSelectedRows();

	    BufferedWriter br = null;

	    try {
		br = new BufferedWriter(new FileWriter(fc.getSelectedFile()));

		for (int i = 0; i < l; i++) {
		    br.write((String) tableModel.getValueAt(rows[i], 1));
		    br.write("\r\n");
		}
	    } catch (Exception e) {
		e.printStackTrace();
	    } finally {
		if (br != null)
		    try {
			br.close();
		    } catch (IOException e) {
			e.printStackTrace();
		    }
	    }

	}
    }

    private void removeAllButtonActionPerformed() {
	int l = table.getRowCount();

	for (int i = l - 1; i >= 0; i--) {
	    ((DefaultTableModel) table.getModel()).removeRow(i);
	}
    }

    private void removePopupActionPerformed() {
	int l = table.getSelectedRowCount();
	int[] rows = table.getSelectedRows();

	for (int i = l - 1; i >= 0; i--) {
	    ((DefaultTableModel) table.getModel()).removeRow(rows[i]);
	}
	fixTableNumbers(table);
    }

    private void fillSpecs() {
	SpecCreator sc = new SpecCreator();
	for (int i = 0; i < table.getRowCount(); i++) {
	    sc.getData(new CarChromosome((String) table.getModel().getValueAt(i, 1)));
	}

	minCarDensity.setText(String.valueOf(sc.getMinCar().getDensity()));
	maxCarDensity.setText(String.valueOf(sc.getMaxCar().getDensity()));

	minCarFriction.setText(String.valueOf(sc.getMinCar().getFriction()));
	maxCarFriction.setText(String.valueOf(sc.getMaxCar().getFriction()));

	minCarRestitution.setText(String.valueOf(sc.getMinCar().getRestitution()));
	maxCarRestitution.setText(String.valueOf(sc.getMaxCar().getRestitution()));

	minWheelDensity.setText(String.valueOf(sc.getMinWheel().getDensity()));
	maxWheelDensity.setText(String.valueOf(sc.getMaxWheel().getDensity()));

	minWheelFriction.setText(String.valueOf(sc.getMinWheel().getFriction()));
	maxWheelFriction.setText(String.valueOf(sc.getMaxWheel().getFriction()));

	minWheelRestitution.setText(String.valueOf(sc.getMinWheel().getRestitution()));
	maxWheelRestitution.setText(String.valueOf(sc.getMaxWheel().getRestitution()));

	minSegmentLength.setText(String.valueOf(sc.getMinSegmentLength()));
	maxSegmentLength.setText(String.valueOf(sc.getMaxSegmentLength()));

	minWheelRadius.setText(String.valueOf(sc.getMinWheelRadius()));
	maxWheelRadius.setText(String.valueOf(sc.getMaxWheelRadius()));

	segments.setText(String.valueOf(sc.getSegments()));
	wheels.setText(String.valueOf(sc.getWheels()));
    }

    // --------------------------------------------------
    // Helpers
    // --------------------------------------------------

    private void specsToFields(IndividualConstraints specs) {
	FixtureSpecs minCar = specs.getMinFixtureChassisSpecs();
	FixtureSpecs maxCar = specs.getMaxFixtureChassisSpecs();

	minCarDensity.setText(String.valueOf(minCar.getDensity()));
	maxCarDensity.setText(String.valueOf(maxCar.getDensity()));
	minCarFriction.setText(String.valueOf(minCar.getFriction()));
	maxCarFriction.setText(String.valueOf(maxCar.getFriction()));
	minCarRestitution.setText(String.valueOf(minCar.getRestitution()));
	maxCarRestitution.setText(String.valueOf(maxCar.getRestitution()));

	minSegmentLength.setText(String.valueOf(specs.getMinChassisLength()));
	maxSegmentLength.setText(String.valueOf(specs.getMaxChassisLength()));

	segments.setText(String.valueOf(specs.getChassisVertex()));

	FixtureSpecs minWheel = specs.getMinFixtureWheelSpecs();
	FixtureSpecs maxWheel = specs.getMaxFixtureWheelSpecs();

	minWheelDensity.setText(String.valueOf(minWheel.getDensity()));
	maxWheelDensity.setText(String.valueOf(maxWheel.getDensity()));
	minWheelFriction.setText(String.valueOf(minWheel.getFriction()));
	maxWheelFriction.setText(String.valueOf(maxWheel.getFriction()));
	minWheelRestitution.setText(String.valueOf(minWheel.getRestitution()));
	maxWheelRestitution.setText(String.valueOf(maxWheel.getRestitution()));

	minWheelRadius.setText(String.valueOf(specs.getMinWheelRadius()));
	maxWheelRadius.setText(String.valueOf(specs.getMaxWheelRadius()));

	wheels.setText(String.valueOf(specs.getWheelVertex()));
    }

    private void createSpecs() {
	FixtureSpecs minCarSpecs = new FixtureSpecs(
	    Float.parseFloat(minCarDensity.getText()),
	    Float.parseFloat(minCarFriction.getText()),
	    Float.parseFloat(minCarRestitution.getText())
	);

	FixtureSpecs maxCarSpecs = new FixtureSpecs(
	    Float.parseFloat(maxCarDensity.getText()),
	    Float.parseFloat(maxCarFriction.getText()),
	    Float.parseFloat(maxCarRestitution.getText())
	);

	FixtureSpecs minWheelSpecs = new FixtureSpecs(
	    Float.parseFloat(minWheelDensity.getText()),
	    Float.parseFloat(minWheelFriction.getText()),
	    Float.parseFloat(minWheelRestitution.getText())
	);

	FixtureSpecs maxWheelSpecs = new FixtureSpecs(
	    Float.parseFloat(maxWheelDensity.getText()),
	    Float.parseFloat(maxWheelFriction.getText()),
	    Float.parseFloat(maxWheelRestitution.getText())
	);

	specs = new IndividualConstraints(
	    Integer.parseInt(segments.getText()),
	    Integer.parseInt(wheels.getText()),
	    Float.parseFloat(minSegmentLength.getText()), Float.parseFloat(maxSegmentLength.getText()),
	    Float.parseFloat(minWheelRadius.getText()), Float.parseFloat(maxWheelRadius.getText()),
	    minCarSpecs, maxCarSpecs,
	    minWheelSpecs, maxWheelSpecs
	);
    }

    private void selectionMethodComboBoxActionPerformed() {
	switch (selectionMethodComboBox.getSelectedIndex()) {
	    case 1:
		selectionLabel2.setVisible(true);
		tournamentSizeSpinner.setVisible(true);

		selectionLabel1.setVisible(false);
		fitnessMethodComboBox.setVisible(false);
		break;
	    default:
		selectionLabel2.setVisible(false);
		tournamentSizeSpinner.setVisible(false);

		selectionLabel1.setVisible(true);
		fitnessMethodComboBox.setVisible(true);
		break;
	}
    }


    private void setSettings() {
	settings = new AlgorithmSettings();
	settings.setCrossoverMethod(crossoverMethodComboBox.getSelectedIndex());
	settings.setCrossoverProbability(((Double) crossoverProbabilitySpinner.getValue()).floatValue() / 100);

	settings.setMutationProbability(((Double) mutationProbabilitySpinner.getValue()).floatValue() / 100);

	settings.setSelectionMethod(selectionMethodComboBox.getSelectedIndex());

	settings.setFitnessMethod(fitnessMethodComboBox.getSelectedIndex());

	settings.setGravity(((Double) gravitySpinner.getValue()).floatValue());

	settings.setElite(eliteSelectionCheckBox.isSelected());

	settings.setTournamentSize(((Integer) tournamentSizeSpinner.getValue()).intValue());
    }

    // --------------------------------------------------
    // Public API
    // --------------------------------------------------

    public int getValue() {
	return value;
    }

    public AlgorithmSettings getSettings() {
	return settings;
    }

    public void setSettings(AlgorithmSettings settings) {
	this.settings = settings;
    }

    public IndividualConstraints getSpecs() {
	return specs;
    }


    public String[] getCars() {
	String[] cars = new String[tableModel.getRowCount()];

	for (int i = 0; i < tableModel.getRowCount(); i++) {
	    cars[i] = tableModel.getValueAt(i, 1).toString();
	}

	return cars;
    }

    // --------------------------------------------------
    // Validations
    // --------------------------------------------------

    private boolean validateSpecs() {
	boolean valid = true;
	JTextField[] fields = new JTextField[]{minCarDensity, maxCarDensity, minCarFriction, maxCarFriction, minCarRestitution, maxCarRestitution, minSegmentLength, maxSegmentLength,
	    minWheelDensity, maxWheelDensity, minWheelFriction, maxWheelFriction, minWheelRestitution, maxWheelRestitution, minWheelRadius, maxWheelRadius
	};

	for (int i = 0; i < fields.length; i++) {
	    try {
		fields[i].setText(fields[i].getText().replace(",", "."));
		float f = Float.parseFloat(fields[i].getText());
		if (f < 0) throw new NumberFormatException();
		fields[i].setBackground(ORIGINALCOLOR);
	    } catch (NumberFormatException ex) {
		fields[i].setBackground(ERRORCOLOR);
		valid = false;
	    }
	}

	fields = new JTextField[]{segments, wheels};

	for (int i = 0; i < fields.length; i++) {
	    try {
		int f = Integer.parseInt(fields[i].getText());
		if (f < 0) throw new NumberFormatException();
		fields[i].setBackground(ORIGINALCOLOR);
	    } catch (NumberFormatException ex) {
		fields[i].setBackground(ERRORCOLOR);
		valid = false;
	    }
	}

	if (!valid) {
	    JOptionPane.showMessageDialog(this,
		"Podano niepoprawną wartość",
		"Błąd",
		JOptionPane.ERROR_MESSAGE
	    );
	    return valid;
	}

	//Now all values are valid, but they may not be in range
	//wheels and segments

	int s = Integer.parseInt(segments.getText());
	int w = Integer.parseInt(wheels.getText());

	if (s < 3 || s > 20) {
	    JOptionPane.showMessageDialog(this,
		"Maksymalna dopuszczalna liczba segmentów to 20",
		"Błądd",
		JOptionPane.ERROR_MESSAGE
	    );
	    segments.setBackground(ERRORCOLOR);
	    return false;
	}
	segments.setBackground(ORIGINALCOLOR);

	if (s < w) {
	    JOptionPane.showMessageDialog(this,
		"Liczba k� musi by� mniejsza lub r�wna liczbie segment�w.",
		"B��d",
		JOptionPane.ERROR_MESSAGE
	    );
	    wheels.setBackground(ERRORCOLOR);
	    return false;
	}
	wheels.setBackground(ORIGINALCOLOR);

	//minmax
	fields = new JTextField[]{minCarDensity, maxCarDensity, minCarFriction, maxCarFriction, minCarRestitution, maxCarRestitution, minSegmentLength, maxSegmentLength,
	    minWheelDensity, maxWheelDensity, minWheelFriction, maxWheelFriction, minWheelRestitution, maxWheelRestitution, minWheelRadius, maxWheelRadius
	};

	for (int i = 0; i < fields.length - 1; i += 2) {
	    fields[i].setBackground(ORIGINALCOLOR);
	    fields[i + 1].setBackground(ORIGINALCOLOR);
	    if (Float.parseFloat(fields[i].getText()) > Float.parseFloat(fields[i + 1].getText())) {
		JOptionPane.showMessageDialog(this,
		    "Warto�� maksymalna musi by� wi�ksza lub r�wna ni� warto�� minimalna.",
		    "B��d",
		    JOptionPane.ERROR_MESSAGE
		);
		fields[i].setBackground(ERRORCOLOR);
		fields[i + 1].setBackground(ERRORCOLOR);
		return false;
	    }
	}

	fields = new JTextField[]{minCarDensity, maxCarDensity, minWheelDensity, maxWheelDensity};
	//Density: 0.1 - 10
	for (int i = 0; i < fields.length; i++) {
	    fields[i].setBackground(ORIGINALCOLOR);
	    if (Float.parseFloat(fields[i].getText()) < 0.1 || Float.parseFloat(fields[i].getText()) > 10) {
		JOptionPane.showMessageDialog(this,
		    "G�sto�� musi by� wi�ksza lub r�wna 0.1 i mniejsza lub r�wna 10.",
		    "B��d",
		    JOptionPane.ERROR_MESSAGE
		);
		fields[i].setBackground(ERRORCOLOR);
		return false;
	    }
	}

	fields = new JTextField[]{minCarFriction, maxCarFriction, minWheelFriction, maxWheelFriction};
	//Friction: 0 - 1
	for (int i = 0; i < fields.length; i++) {
	    fields[i].setBackground(ORIGINALCOLOR);
	    if (Float.parseFloat(fields[i].getText()) > 1) {
		JOptionPane.showMessageDialog(this,
		    "Tarcie musi by� mniejsze od 1 i wi�ksze od 0.",
		    "B��d",
		    JOptionPane.ERROR_MESSAGE
		);
		fields[i].setBackground(ERRORCOLOR);
		return false;
	    }
	}

	fields = new JTextField[]{minCarRestitution, maxCarRestitution, minWheelRestitution, maxWheelRestitution};
	//Restitution: 0 - 1
	for (int i = 0; i < fields.length; i++) {
	    fields[i].setBackground(ORIGINALCOLOR);
	    if (Float.parseFloat(fields[i].getText()) > 1) {
		JOptionPane.showMessageDialog(this,
		    "Elastyczno�� musi by� mniejsza od 1 i wi�ksza od 0.",
		    "B��d",
		    JOptionPane.ERROR_MESSAGE
		);
		fields[i].setBackground(ERRORCOLOR);
		return false;
	    }
	}

	fields = new JTextField[]{minSegmentLength, maxSegmentLength};
	//Segment length 0.1 - 2
	for (int i = 0; i < fields.length; i++) {
	    fields[i].setBackground(ORIGINALCOLOR);
	    if (Float.parseFloat(fields[i].getText()) < 0.1 && Float.parseFloat(fields[i].getText()) > 2) {
		JOptionPane.showMessageDialog(this,
		    "D�ugo�� segmentu musi by� mniejsza od 2 i wi�ksza od 0.1.",
		    "B��d",
		    JOptionPane.ERROR_MESSAGE
		);
		fields[i].setBackground(ERRORCOLOR);
		return false;
	    }
	}

	fields = new JTextField[]{minWheelRadius, maxWheelRadius};
	//Wheel radius 0.1 - 0.5
	for (int i = 0; i < fields.length; i++) {
	    fields[i].setBackground(ORIGINALCOLOR);
	    if (Float.parseFloat(fields[i].getText()) < 0.1 && Float.parseFloat(fields[i].getText()) > 0.5) {
		JOptionPane.showMessageDialog(this,
		    "Promie� ko�a musi by� mniejszy od 0.5 i wi�kszy od 0.1.",
		    "B��d",
		    JOptionPane.ERROR_MESSAGE
		);
		fields[i].setBackground(ERRORCOLOR);
		return false;
	    }
	}

	return valid;
    }

    private boolean validateSettings() {
	boolean valid = true;

	float val = ((Double) crossoverProbabilitySpinner.getValue()).floatValue();

	if (val < 0 || val > 100) {
	    JOptionPane.showMessageDialog(this,
		"Nieprawid�owa warto�� prawdopodobie�stwa krzy�owania.",
		"B��d",
		JOptionPane.WARNING_MESSAGE
	    );
	    crossoverProbabilitySpinner.setBackground(ERRORCOLOR);
	    return false;
	}
	crossoverProbabilitySpinner.setBackground(ORIGINALCOLOR);

	val = ((Double) mutationProbabilitySpinner.getValue()).floatValue();

	if (val < 0 || val > 100) {
	    JOptionPane.showMessageDialog(this,
		"Nieprawid�owa warto�� prawdopodobie�stwa mutacji.",
		"B��d",
		JOptionPane.WARNING_MESSAGE
	    );
	    mutationProbabilitySpinner.setBackground(ERRORCOLOR);
	    return false;
	}
	mutationProbabilitySpinner.setBackground(ORIGINALCOLOR);

	return valid;
    }

    private boolean validateGenerateButton() {
	if (genNumber.getText().equals("")) {
	    JOptionPane.showMessageDialog(this,
		"Nie podano liczby osobnik�w do wygenerowania.",
		"B��d",
		JOptionPane.WARNING_MESSAGE
	    );
	    genNumber.setBackground(ERRORCOLOR);
	    return false;
	}

	try {
	    int a = Integer.parseInt(genNumber.getText());
	    if (a < 0) throw new NumberFormatException();
	} catch (NumberFormatException ex) {
	    JOptionPane.showMessageDialog(this,
		"Nieprawid�owa liczba osobnik�w do wygenerowania.",
		"B��d",
		JOptionPane.WARNING_MESSAGE
	    );
	    genNumber.setBackground(ERRORCOLOR);
	    return false;
	}
	genNumber.setBackground(ORIGINALCOLOR);
	return true;
    }

    private boolean isPopulationValid() {
	if (table.getRowCount() == 0) {
	    JOptionPane.showMessageDialog(this,
		"Populacja jest pusta.",
		"B��d",
		JOptionPane.ERROR_MESSAGE);
	    return false;
	}

	ChromosomeValidator vl = new ChromosomeValidator();
	for (int i = 0; i < table.getRowCount(); i++) {
	    if (!vl.isValid((String) table.getModel().getValueAt(i, 1))) {
		JOptionPane.showMessageDialog(this,
		    "Wyst�pi� b��d przy sprawdzaniu poprawno�ci populacji, przy osobniku " + i + ".\n" +
			vl.getError(),
		    "B��d",
		    JOptionPane.ERROR_MESSAGE);
		return false;
	    }
	}

	return true;
    }

    // --------------------------------------------------
    // UI code
    // --------------------------------------------------

    public NewAlgorithmDialog(JFrame parent) {
	setTitle("Nowy algorytm");

	JPanel populationPanel = new JPanel(new MigLayout());

	JPanel specPanel = createSpecPanel();
	JPanel tablePanel = createTablePanel();

	populationPanel.add(specPanel, "growy");
	populationPanel.add(tablePanel, "wrap");

	okButton = new JButton("Ok");
	okButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
		okButtonActionPerformed();
	    }
	});
	populationPanel.add(okButton, "span, split, align right");

	cancelButton = new JButton("Anuluj");
	cancelButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
		cancelButtonActionPerformed();
	    }
	});
	populationPanel.add(cancelButton);

	helpButton = new JButton("Pomoc");
	helpButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
		helpButtonActionPerformed();
	    }
	});
	populationPanel.add(helpButton);

	//---

	JPanel settingsPanel = createSettingsPanel();

	JTabbedPane tabbedPane = new JTabbedPane();
	tabbedPane.addTab("Populacja początkowa", populationPanel);
	tabbedPane.addTab("Ustawienia", settingsPanel);

	add(tabbedPane);
	pack();
	setLocationRelativeTo(parent);

	ORIGINALCOLOR = minCarDensity.getBackground();
    }

    private JPanel createSpecPanel() {
	JPanel specPanel = new JPanel(new MigLayout());

	specPanel.add(new JLabel("<html><b>Specyfikacje karoserii</b></html>"), "wrap");
	specPanel.add(new JSeparator(), "growx, wrap, span 5");

	//Car density
	specPanel.add(new JLabel("Gęstość:"));

	specPanel.add(new JLabel("Min:"));
	minCarDensity = new JTextField(6);
	specPanel.add(minCarDensity);

	specPanel.add(new JLabel("Max:"));
	maxCarDensity = new JTextField(6);
	specPanel.add(maxCarDensity, "wrap");

	//Car friction
	specPanel.add(new JLabel("Tarcie:"));

	specPanel.add(new JLabel("Min:"));
	minCarFriction = new JTextField(6);
	specPanel.add(minCarFriction);

	specPanel.add(new JLabel("Max:"));
	maxCarFriction = new JTextField(6);
	specPanel.add(maxCarFriction, "wrap");

	//Car restitution
	specPanel.add(new JLabel("Elastyczność:"));

	specPanel.add(new JLabel("Min:"));
	minCarRestitution = new JTextField(6);
	specPanel.add(minCarRestitution);

	specPanel.add(new JLabel("Max:"));
	maxCarRestitution = new JTextField(6);
	specPanel.add(maxCarRestitution, "wrap");

	//Segment length
	specPanel.add(new JLabel("Długość segmentu:"));

	specPanel.add(new JLabel("Min:"));
	minSegmentLength = new JTextField(6);
	specPanel.add(minSegmentLength);

	specPanel.add(new JLabel("Max:"));
	maxSegmentLength = new JTextField(6);
	specPanel.add(maxSegmentLength, "wrap");

	//Number of segments
	specPanel.add(new JLabel("Liczba segmentów:"));
	segments = new JTextField(6);
	specPanel.add(segments, "skip, growx, wrap");

	//Wheel --------------------------------------------------

	specPanel.add(new JLabel("<html><b>Specyfikacje kół</b></html>"), "wrap");
	specPanel.add(new JSeparator(), "growx, wrap, span 5");

	//Wheel density
	specPanel.add(new JLabel("Gęstość:"));

	specPanel.add(new JLabel("Min:"));
	minWheelDensity = new JTextField(6);
	specPanel.add(minWheelDensity);

	specPanel.add(new JLabel("Max:"));
	maxWheelDensity = new JTextField(6);
	specPanel.add(maxWheelDensity, "wrap");
	//Wheel friction
	specPanel.add(new JLabel("Tarcie:"));

	specPanel.add(new JLabel("Min:"));
	minWheelFriction = new JTextField(6);
	specPanel.add(minWheelFriction);

	specPanel.add(new JLabel("Max:"));
	maxWheelFriction = new JTextField(6);
	specPanel.add(maxWheelFriction, "wrap");
	//Wheel restitution
	specPanel.add(new JLabel("Elastyczność:"));

	specPanel.add(new JLabel("Min:"));
	minWheelRestitution = new JTextField(6);
	specPanel.add(minWheelRestitution);

	specPanel.add(new JLabel("Max:"));
	maxWheelRestitution = new JTextField(6);
	specPanel.add(maxWheelRestitution, "wrap");

	//Wheel radius
	specPanel.add(new JLabel("Promień koła:"));

	specPanel.add(new JLabel("Min:"));
	minWheelRadius = new JTextField(6);
	specPanel.add(minWheelRadius);

	specPanel.add(new JLabel("Max:"));
	maxWheelRadius = new JTextField(6);
	specPanel.add(maxWheelRadius, "wrap");

	//Number of wheels
	specPanel.add(new JLabel("Liczba kół:"));
	wheels = new JTextField(6);
	specPanel.add(wheels, "skip, wrap push");

	defaultSpecButton = new JButton("Domyślne");
	defaultSpecButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
		defaultSpecButtonActionPerformed();
	    }
	});
	specPanel.add(defaultSpecButton, "span, split, align left");

	saveSpecButton = new JButton("Zapisz");
	saveSpecButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
		saveSpecButtonActionPerformed();
	    }
	});
	specPanel.add(saveSpecButton);

	loadSpecButton = new JButton("Wczytaj");
	loadSpecButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
		loadSpecButtonActionPerformed();
	    }
	});
	specPanel.add(loadSpecButton);

	return specPanel;
    }

    @SuppressWarnings("serial")
    private JPanel createTablePanel() {
	JPanel tablePanel = new JPanel(new MigLayout());
	tableModel = new DefaultTableModel(
	    new Object[]{"Numer", "Chromosom"},
	    0
	) {

	    @Override
	    public boolean isCellEditable(int row, int column) {
		return false;
	    }
	};
	table = new JTable(tableModel);
	table.getColumn("Numer").setPreferredWidth(50);
	table.getColumn("Chromosom").setPreferredWidth(400);
	createTablePopup(table);

	JScrollPane tablePane = new JScrollPane(table);
	tablePanel.add(tablePane, "wrap, growx");

	genNumber = new JTextField(3);
	tablePanel.add(genNumber, "span, split, align left");

	generate = new JButton("Generuj");
	generate.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
		generateButtonActionPerformed();
	    }
	});
	tablePanel.add(generate);

	savePopButton = new JButton("Zapisz");
	savePopButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
		savePopButtonActionPerformed();
	    }
	});
	tablePanel.add(savePopButton);

	loadPopButton = new JButton("Wczytaj");
	loadPopButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
		loadPopButtonActionPerformed();
	    }
	});
	tablePanel.add(loadPopButton);

	removeAllButton = new JButton("Usu� wszystkie");
	removeAllButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
		removeAllButtonActionPerformed();
	    }
	});
	tablePanel.add(removeAllButton);

	fillSpecs = new JCheckBox("Wypełnij specyfikacje");
	fillSpecs.setSelected(true);
	tablePanel.add(fillSpecs);

	return tablePanel;
    }

    private JPanel createSettingsPanel() {
	JPanel settingsPanel = new JPanel(new MigLayout());

	settingsPanel.add(new JLabel("<html><b>Ustawienia krzyżowania</b></html>"), "wrap");
	settingsPanel.add(new JSeparator(), "growx, wrap, span 2");

	settingsPanel.add(new JLabel("Prawdopodobieństwo(%):"));
	crossoverProbabilitySpinner = new JSpinner(new SpinnerNumberModel(75, 0, 100, 1.0f));
	settingsPanel.add(crossoverProbabilitySpinner, "wrap");

	settingsPanel.add(new JLabel("Metoda:"));
	crossoverMethodComboBox = new JComboBox<String>(AlgorithmSettings.CROSSOVER_METHODS);
	settingsPanel.add(crossoverMethodComboBox, "growx, wrap");

	settingsPanel.add(new JLabel("<html><b>Ustawienia selekcji</b></html>"), "wrap");
	settingsPanel.add(new JSeparator(), "growx, wrap, span 2");

	settingsPanel.add(new JLabel("Metoda selekcji:"));
	selectionMethodComboBox = new JComboBox<String>(AlgorithmSettings.SELECTION_METHODS);
	selectionMethodComboBox.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
		selectionMethodComboBoxActionPerformed();
	    }
	});
	settingsPanel.add(selectionMethodComboBox, "growx, wrap");

	selectionLabel1 = new JLabel("Funkcja przystosowania:");
	settingsPanel.add(selectionLabel1, "hidemode 3");
	fitnessMethodComboBox = new JComboBox<String>(AlgorithmSettings.FITNESS_METHODS);
	settingsPanel.add(fitnessMethodComboBox, "growx, wrap, hidemode 3");

	selectionLabel2 = new JLabel("Wielkość turnieju:");
	selectionLabel2.setVisible(false);
	settingsPanel.add(selectionLabel2, "hidemode 3");
	tournamentSizeSpinner = new JSpinner(new SpinnerNumberModel(3, 3, 100, 1));
	tournamentSizeSpinner.setVisible(false);
	settingsPanel.add(tournamentSizeSpinner, "wrap, hidemode 3");

	eliteSelectionCheckBox = new JCheckBox("Sukcesja elitarna");
	eliteSelectionCheckBox.setSelected(true);
	settingsPanel.add(eliteSelectionCheckBox, "growx, wrap");

	settingsPanel.add(new JLabel("<html><b>Ustawienia mutacji</b></html>"), "wrap");
	settingsPanel.add(new JSeparator(), "growx, wrap, span 2");

	settingsPanel.add(new JLabel("Prawdopodobieństwo(%):"));
	mutationProbabilitySpinner = new JSpinner(new SpinnerNumberModel(5, 0, 100, 1.0f));
	settingsPanel.add(mutationProbabilitySpinner, "wrap");

	//------

	JPanel worldPanel = new JPanel(new MigLayout());

	worldPanel.add(new JLabel("<html><b>Ustawienia świata</b></html>"), "wrap");
	worldPanel.add(new JSeparator(), "growx, wrap, span 2");

	worldPanel.add(new JLabel("Grawitacja:"));
	gravitySpinner = new JSpinner(new SpinnerNumberModel(9.81f, 0, 100, 1.0f));
	worldPanel.add(gravitySpinner, "wrap");

	JPanel mainSettingsPanel = new JPanel(new MigLayout("", "", "[top]"));
	mainSettingsPanel.add(settingsPanel);
	mainSettingsPanel.add(worldPanel);

	return mainSettingsPanel;
    }

    private void createTablePopup(JTable table) {
	JPopupMenu menu = new JPopupMenu();

	JMenuItem save = new JMenuItem("Zapisz");
	save.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
		savePopupActionPerformed();
	    }
	});
	menu.add(save);

	JMenuItem remove = new JMenuItem("Usuń");
	remove.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
		removePopupActionPerformed();
	    }
	});
	menu.add(remove);

	table.setComponentPopupMenu(menu);
    }

    private void fixTableNumbers(JTable table) {
	DefaultTableModel model = (DefaultTableModel) table.getModel();

	for (int i = 0; i < model.getRowCount(); i++) {
	    model.setValueAt(i, i, 0);
	}
    }
}
