package com.dth.geneticcar.desktop;

import java.awt.Component;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class CarTableRenderer extends JProgressBar implements TableCellRenderer {
    private static final long serialVersionUID = 1203689731234980884L;

    public CarTableRenderer() {
	super(0, 100);
	setValue(0);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
						   boolean isSelected, boolean hasFocus, int row, int column) {
	int progress = (int) value;
	setValue((int) progress);
	return this;
    }
}
