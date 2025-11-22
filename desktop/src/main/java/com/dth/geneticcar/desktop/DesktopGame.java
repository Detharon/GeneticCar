package com.dth.geneticcar.desktop;

import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;

import javax.swing.*;
import javax.swing.table.TableColumn;

import com.dth.geneticcar.Core;
import com.dth.geneticcar.desktop.utils.HtmlReader;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DeviationRenderer;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import com.dth.geneticcar.datatype.CarIndividual;
import com.dth.geneticcar.datatype.AlgorithmSettings;
import com.dth.geneticcar.utils.CarEvent;
import com.dth.geneticcar.utils.CarListener;
import com.dth.geneticcar.utils.TrackGenerator;

import net.miginfocom.swing.MigLayout;

import com.badlogic.gdx.backends.lwjgl.LwjglAWTCanvas;

public class DesktopGame extends JFrame {
    JMenuBar menuBar;
    JMenu algorithm, track, help;

    JMenuItem run, quickRun, pause, resume, end, exit;
    JCheckBoxMenuItem follow;

    JMenuItem loadTrack, generateTrack;
    JMenuItem about;

    JTable table;
    CarTableModel tableModel;

    XYSeries series;

    GenerateTrackDialog dialog = new GenerateTrackDialog(this);

    private static final long serialVersionUID = 1L;
    private Core game;

    public DesktopGame() {
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	JPopupMenu.setDefaultLightWeightPopupEnabled(false); //due to heavyweight / lightweight mixing issues
	setTitle("Genetyczny samochód");

	File file = new File("icon.png");
	Image img = new ImageIcon(file.getAbsolutePath()).getImage();
	setIconImage(img);
	initializeGUI();

	JPanel canvasPanel = new JPanel(new MigLayout());
	game = new Core();
	game.addCarListener(new CarListener() {
	    public void carsReceived(CarEvent e) {
		CarIndividual[] cars = e.getCars();
		addCarsToTable(cars, e.getTrackLength());
		addCarsToGraph(cars);
	    }
	});
	LwjglAWTCanvas canvas = new LwjglAWTCanvas(game);
	canvas.getCanvas().setSize(720, 480);
	canvasPanel.add(canvas.getCanvas(), "shrink 0");

	tableModel = new CarTableModel();
	table = new JTable(tableModel);
	table.setAutoCreateRowSorter(true);
	table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	table.getColumnModel().getColumn(0).setPreferredWidth(40);
	table.getColumnModel().getColumn(1).setPreferredWidth(80);
	table.getColumnModel().getColumn(2).setPreferredWidth(100);
	table.getColumnModel().getColumn(3).setPreferredWidth(500);

	TableColumn tm = table.getColumnModel().getColumn(2);
	tm.setCellRenderer(new CarTableRenderer());

	initializeTablePopup();

	JScrollPane tablePane = new JScrollPane(table);

	series = new XYSeries("");
	XYSeriesCollection xySeriesCollection = new XYSeriesCollection(series);
	IntervalXYDataset data = xySeriesCollection;

	Container container = getContentPane();
	container.setLayout(new MigLayout("", "[][grow,fill,:280:]", "[240][240]"));
	container.add(canvasPanel, "span 2 2");
	container.add(tablePane, "wrap");
	container.add(createChartPanel(data));

	pack();
	setLocationRelativeTo(null);
	setResizable(false);
	setVisible(true);
    }

    private ChartPanel createChartPanel(IntervalXYDataset dataset) {
	final JFreeChart chart = ChartFactory.createXYLineChart(
	    "",
	    "Populacja",
	    "Średnie przystosowanie",
	    dataset
	);
	XYPlot plot = chart.getXYPlot();
	Font def = UIManager.getDefaults().getFont("TextField.font");

	NumberFormat formatter = NumberFormat.getInstance();
	formatter.setMinimumFractionDigits(0);
	formatter.setMaximumFractionDigits(0);

	NumberAxis xAxis = new NumberAxis();
	xAxis.setLabel("Populacja");
	xAxis.setNumberFormatOverride(formatter);
	xAxis.setAutoRange(true);
	xAxis.setTickLabelFont(def);
	xAxis.setLabelFont(def);
	plot.setDomainAxis(xAxis);


	formatter = NumberFormat.getInstance();
	formatter.setMinimumFractionDigits(0);
	formatter.setMaximumFractionDigits(1);

	NumberAxis yAxis = new NumberAxis();
	yAxis.setLabel("Przystosowanie");
	yAxis.setNumberFormatOverride(formatter);
	yAxis.setAutoRange(true);
	yAxis.setTickLabelFont(def);
	yAxis.setLabelFont(def);

	plot.setRangeAxis(yAxis);

	chart.removeLegend();
	chart.setBackgroundPaint(UIManager.getColor("Panel.background"));

	DeviationRenderer deviationrenderer = new DeviationRenderer(true, false);
	plot.setRenderer(deviationrenderer);

	ChartPanel cpanel = new ChartPanel(chart);
	cpanel.setMinimumDrawWidth(0);
	cpanel.setMinimumDrawHeight(0);
	cpanel.setMaximumDrawWidth(1920);
	cpanel.setMaximumDrawHeight(1200);
	return cpanel;
    }

    private void initializeGUI() {
	menuBar = new JMenuBar();

	initializeAlgorithmMenu();
	initializeTrackMenu();
	initializeHelpMenu();

	this.setJMenuBar(menuBar);
    }

    private void initializeAlgorithmMenu() {
	algorithm = new JMenu("Algorytm");
	algorithm.setMnemonic(KeyEvent.VK_A);

	run = new JMenuItem("Start...", KeyEvent.VK_S);
	run.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
	run.setEnabled(false);
	run.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
		runActionPerformed();
	    }
	});
	algorithm.add(run);

	quickRun = new JMenuItem("Szybki start", KeyEvent.VK_B);
	quickRun.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
	quickRun.setEnabled(false);
	quickRun.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
		quickRunActionPerformed();
	    }
	});
	algorithm.add(quickRun);

	algorithm.add(new JSeparator());
	// ----------

	pause = new JMenuItem("Pauza", KeyEvent.VK_P);
	pause.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
	pause.setEnabled(false);
	pause.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
		pauseActionPerformed();
	    }
	});
	algorithm.add(pause);

	resume = new JMenuItem("Wznów");
	resume.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
	resume.setEnabled(false);
	resume.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
		resumeActionPerformed();
	    }
	});
	algorithm.add(resume);

	end = new JMenuItem("Zakończ", KeyEvent.VK_W);
	end.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
	end.setEnabled(false);
	end.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
		endActionPerformed();
	    }
	});
	algorithm.add(end);

	follow = new JCheckBoxMenuItem("Śledź najlepszego");
	follow.setSelected(true);
	follow.addItemListener(new ItemListener() {
	    public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
		    fixCameraActionPerformed(true);
		} else {
		    fixCameraActionPerformed(false);
		}
	    }
	});
	algorithm.add(follow);

	algorithm.add(new JSeparator());
	// ----------

	exit = new JMenuItem("Wyjście", KeyEvent.VK_W);
	algorithm.add(exit);

	menuBar.add(algorithm);
    }

    private void initializeTrackMenu() {
	track = new JMenu("Trasa");
	track.setMnemonic(KeyEvent.VK_T);

	loadTrack = new JMenuItem("Wczytaj...", KeyEvent.VK_W);
	loadTrack.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
	loadTrack.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
		loadTrackActionPerformed();
	    }
	});
	track.add(loadTrack);

	generateTrack = new JMenuItem("Generuj...", KeyEvent.VK_G);
	generateTrack.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
	generateTrack.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
		generateTrackActionPerformed();
	    }
	});
	track.add(generateTrack);

	menuBar.add(track);
    }

    private void initializeHelpMenu() {
	help = new JMenu("Pomoc");
	help.setMnemonic(KeyEvent.VK_P);

	about = new JMenuItem("O programie");
	about.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
		aboutActionPerformed();
	    }
	});
	help.add(about);

	menuBar.add(help);
    }

    private void initializeTablePopup() {
	JPopupMenu menu = new JPopupMenu();

	JMenuItem save = new JMenuItem("Zapisz");
	save.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
		savePopupActionPerformed();
	    }
	});
	menu.add(save);

	table.setComponentPopupMenu(menu);
    }

    private void runActionPerformed() {
	NewAlgorithmDialog dialog = new NewAlgorithmDialog(this);
	dialog.setModal(true);
	dialog.setVisible(true);

	if (dialog.getValue() == NewAlgorithmDialog.OK) {
	    clearData();

	    game.setSettings(dialog.getSettings());
	    game.setConstraints(dialog.getSpecs());
	    String cars[] = dialog.getCars();
	    game.createCar(game.getWorld(), cars);

	    loadTrack.setEnabled(false);
	    generateTrack.setEnabled(false);
	    pause.setEnabled(true);
	    run.setEnabled(false);
	    quickRun.setEnabled(false);
	    end.setEnabled(true);
	}
    }

    private void quickRunActionPerformed() {
	clearData();
	game.setSettings(new AlgorithmSettings());
	game.createRandomCars(game.getWorld(), 30);

	loadTrack.setEnabled(false);
	generateTrack.setEnabled(false);
	pause.setEnabled(true);
	run.setEnabled(false);
	quickRun.setEnabled(false);
	end.setEnabled(true);
    }

    private void pauseActionPerformed() {
	pause.setEnabled(false);
	resume.setEnabled(true);
	game.pause();
    }

    private void resumeActionPerformed() {
	pause.setEnabled(true);
	resume.setEnabled(false);
	game.resume();
    }

    private void endActionPerformed() {
	game.end();

	run.setEnabled(true);
	quickRun.setEnabled(true);
	loadTrack.setEnabled(true);
	generateTrack.setEnabled(true);

	pause.setEnabled(false);
	resume.setEnabled(false);
	end.setEnabled(false);
	if (game.getGamestate() == game.STATUS_PAUSED) {
	    game.resume();
	}
    }

    private void fixCameraActionPerformed(boolean fix) {
	if (fix) {
	    game.fixCamera();
	} else {
	    game.unfixCamera();
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
		    br.write((String) tableModel.getValueAt(table.convertRowIndexToModel(rows[i]), 3));
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

    private void loadTrackActionPerformed() {
	JFileChooser fc = new JFileChooser("Tracks"); //look for a file in a current directory
	int returnVal = fc.showOpenDialog(this);

	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    loadTrack(fc.getSelectedFile(), true);
	}
    }

    private void loadTrack(File file, boolean showMessage) {
	BufferedReader br = null;
	int i = 0;

	try {
	    br = new BufferedReader(new FileReader(file));

	    String s;
	    while ((s = br.readLine()) != null) {
		i++; //checking how big the file is
	    }

	    float[] points = new float[i];
	    i = 0;

	    br.close(); //re-reading the file
	    br = new BufferedReader(new FileReader(file));

	    while ((s = br.readLine()) != null) {
		points[i] = Float.parseFloat(s.trim());
		i++;
	    }

	    game.createGround(points);

	    if (showMessage) {
		JOptionPane.showMessageDialog(this, "Trasa pomyślnie wczytana.\nZawiera " + (i - 1) + " punktów o długości " + points[0] + "."
		    , "Success", JOptionPane.INFORMATION_MESSAGE);
	    }

	    run.setEnabled(true);
	    quickRun.setEnabled(true);
	} catch (NumberFormatException ex) {
	    JOptionPane.showMessageDialog(this, "Plik zawiera niewłaściwe wartości.", "Błąd", JOptionPane.WARNING_MESSAGE);
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

    private void generateTrackActionPerformed() {
	dialog.setModal(true);
	dialog.setVisible(true);
	if (dialog.value == true) {
	    TrackGenerator.generateTrack("assets/Tracks/" + dialog.getFilename(), dialog.getSegmentLength(),
		dialog.getNumberOfSegments(), dialog.getMaximumDiff(), dialog.getDelta());
	    JOptionPane.showMessageDialog(this, "Trasa \"" + dialog.getFilename() + "\" pomyślnie wygenerowana.", "Success", JOptionPane.INFORMATION_MESSAGE);
	    loadTrack(new File("assets/Tracks" + "/" + dialog.getFilename()), false);
	}
    }

    private void aboutActionPerformed() {
	HelpWindow help = new HelpWindow(this, HtmlReader.readHtml("o_programie.html"), false);
	help.setVisible(true);
    }

    private void addCarsToTable(CarIndividual[] cars, float trackLength) {
	Object[][] data = new Object[cars.length][];
	CarIndividual c;
	for (int i = 0; i < cars.length; i++) {
	    c = cars[i];

	    data[i] = new Object[4];

	    data[i][0] = i;
	    data[i][1] = c.getFitness();
	    data[i][2] = Math.round(c.getFitness() / trackLength * 100);
	    data[i][3] = c.getCarChromosome().toString();
	}
	tableModel.setData(data);
    }

    private void addCarsToGraph(CarIndividual[] cars) {
	float sum = 0;
	for (CarIndividual car : cars) {
	    sum += car.getFitness();
	}
	float avg = sum / cars.length;
	series.add(series.getItemCount(), avg);
    }

    private void clearData() {
	series.delete(0, series.getItemCount() - 1);

	Object[][] data = {};
	((CarTableModel) table.getModel()).setData(data);
    }

    public static void main(String[] args) {
	try {
	    for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
		if ("Nimbus".equals(info.getName())) {
		    javax.swing.UIManager.setLookAndFeel(info.getClassName());
		    break;
		}
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	}

	SwingUtilities.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		new DesktopGame();
	    }
	});
    }
}
