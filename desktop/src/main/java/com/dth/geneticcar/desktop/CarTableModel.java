package com.dth.geneticcar.desktop;

import javax.swing.table.AbstractTableModel;

public class CarTableModel extends AbstractTableModel {
    private static final long serialVersionUID = -276976699325507691L;

    private String[] tableHeaders = new String[]{"Nr", "Dystans", "Trasa", "Chromosom"};
    private Object[][] data = {};

    public final int PROGRESS = 2;

    @Override
    public String getColumnName(int columnIndex) {
	return tableHeaders[columnIndex];
    }

    @Override
    public int getColumnCount() {
	return tableHeaders.length;
    }

    @Override
    public int getRowCount() {
	return data.length;
    }

    @Override
    public Object getValueAt(int row, int col) {
	return data[row][col];
    }

    public void setProgress(int row, int value) {
	data[row][PROGRESS] = value;
	fireTableCellUpdated(row, PROGRESS);
    }

    public void setData(Object[][] data) {
	this.data = data;
	fireTableDataChanged();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
	switch (columnIndex) {
	    case 0:
		return Integer.class;
	    case 1:
		return Float.class;
	    case 2:
		return Object.class;
	    case 3:
		return String.class;
	    default:
		return Object.class;
	}
    }

}
