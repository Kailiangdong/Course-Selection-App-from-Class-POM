package main.java.GUI;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class LecturesTableView implements View {

    private JPanel mainPane;
    private JScrollPane leftPane;
    private JScrollPane rightPane;
    private JTable leftTable;
    private JTable rightTable;


    public LecturesTableView() {
        leftTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        rightTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public void setTableModelListener(ListSelectionListener l) {
        leftTable.getSelectionModel().addListSelectionListener(l);
        rightTable.getSelectionModel().addListSelectionListener(l);
    }

    public JPanel getMainPane() {
        return mainPane;
    }

    protected JTable getLeftTable() {
        return leftTable;
    }

    protected JTable getRightTable() {
        return rightTable;
    }

}
