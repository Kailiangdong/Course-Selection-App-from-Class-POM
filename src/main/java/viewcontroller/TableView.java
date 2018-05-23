package main.java.viewcontroller;

import main.java.SQLiteManager.SQLiteManager;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class TableView {

    private JFrame frame = new JFrame("University app");
    private JPanel tablePanel = new JPanel();
    private Object rowData[][];
    private Object columnNames[] = { "Module ID", "Title" };
    private JTable joinableLecturesTable;
    private JScrollPane joinableLecturesPanel;
    private JMenuBar menuBar = new JMenuBar();
    private JMenu menu = new JMenu("☰");
    private JLabel details = new JLabel("Details", SwingConstants.CENTER);

    private JTable initTable() {
        SQLiteManager manager = new SQLiteManager();
        try {
            rowData = manager.queryJoinableLectures("Stefan");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return joinableLecturesTable = new JTable(rowData, columnNames);
    }

    public void display() {
        joinableLecturesPanel = new JScrollPane(initTable());
        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(joinableLecturesPanel, BorderLayout.CENTER);
        menu.add("EXIT");
        menu.add("✓ Modul ID");
        menu.add("✓ Title");
        menu.add("Chair");
        menuBar.add(menu);
        tablePanel.add(menuBar,BorderLayout.NORTH);
        //tablePanel.add(details, BorderLayout.SOUTH);
        frame.add(tablePanel);
        frame.setSize(500, 300);
        frame.setVisible(true);

    }

    public static void main(String[] args) {
        TableView table = new TableView();
        table.display();
    }

}

