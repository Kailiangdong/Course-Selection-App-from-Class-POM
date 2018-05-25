package main.java.view;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;

public class LecturesTableView implements View {

    private JPanel mainPane;
    private JScrollPane leftPane;
    private JScrollPane rightPane;
    private JTable leftTable;
    private JTable rightTable;
    private JTextArea infoArea;


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

    public JTable getLeftTable() {
        return leftTable;
    }

    public JTable getRightTable() {
        return rightTable;
    }

}
