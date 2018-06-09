package controller;

import javax.swing.table.DefaultTableModel;

public class TableModel extends DefaultTableModel {

    public TableModel(String[][] rowData, String[] colNames) {
        super(rowData, colNames);
    }

    /**
     * The user should not be able to edit the table data.
     *
     * @param row
     * @param column
     * @return
     */
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
