package controller;

import javax.swing.table.DefaultTableModel;

/**
 * Custom TableModel to represent lecture data in a JTable.
 */
public class LectureTableModel extends DefaultTableModel {

    public LectureTableModel(String[][] rowData, String[] colNames) {
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
