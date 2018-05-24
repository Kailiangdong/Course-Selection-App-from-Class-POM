package main.java.viewcontroller;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import main.java.SQLiteManager.SQLiteManager;

public class TableView extends JFrame {

    // Menu
    private JMenuBar menuBar;
    private JMenu mainMenu;
    private JMenu projectionMenu;
    private JMenu selectionMenu;
    private JMenuItem[] selectionMenuItems;
    private JMenuItem[] projectionMenuItems;
    private JMenuItem quitProgram;

    // Lectures Table
    private JPanel tablePanel;
    private JTable joinableLecturesTable;
    private JScrollPane joinableLecturesPanel;
    private JTextArea detailsArea;

    public TableView() {

        // Init components

        this.setTitle("University app");
        this.setSize(500, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Top menu
        menuBar = new JMenuBar(); // top menu bar

        mainMenu = new JMenu("☰"); // program control
        quitProgram = new JMenuItem("Quit");
        mainMenu.add(quitProgram);

        projectionMenu = new JMenu("P"); // column projection menu
        selectionMenu = new JMenu("S"); // row selection menu

        menuBar.add(mainMenu);
        menuBar.add(projectionMenu);
        menuBar.add(selectionMenu);

        // Build panel

        tablePanel.setLayout(new BorderLayout());
        tablePanel.add(joinableLecturesPanel, BorderLayout.CENTER);
        tablePanel.add(menuBar,BorderLayout.NORTH);
        tablePanel.add(detailsArea, BorderLayout.SOUTH);

        this.add(tablePanel);
    }

    // Table Data

    public JTable getJoinableLecturesTable() {
        return joinableLecturesTable;
    }

    public void setTableModel(TableModel tableModel) {
        joinableLecturesTable.setModel(tableModel);
    }

    // Main Menu

    public void setExitListener(ActionListener l) {
        quitProgram.addActionListener(l);
    }

    // Chair Selection Menu

    public JMenuItem[] getSelectionMenuItems() {
        return selectionMenuItems;
    }

    public void setSelectionMenu(String[] chairNames) {
        selectionMenuItems = new JMenuItem[chairNames.length];
        for(int i = 0; i < chairNames.length; i++) {
            selectionMenuItems[i] = new JMenuItem("√ " + chairNames[i]);
            selectionMenu.add(selectionMenuItems[i]);
        }
    }

    public void setSelectionMenuItemsListener(ActionListener l) {
        for(JMenuItem selectionMenuItem : selectionMenuItems) {
            selectionMenuItem.addActionListener(l);
        }
    }

    // Column Selection Menu

    public JMenuItem[] getProjectionMenuItems() {
        return projectionMenuItems;
    }

    public void setColProjectionMenu(String[] colNames) {
        projectionMenuItems = new JMenuItem[colNames.length];
        for(int i = 0; i < colNames.length; i++) {
            projectionMenuItems[i] = new JMenuItem("√ " + colNames[i]);
            projectionMenu.add(projectionMenuItems[i]);
        }
    }

    public void setProjectionMenuItemsListener(ActionListener l) {
        for(JMenuItem projectionMenuItem : projectionMenuItems) {
            projectionMenuItem.addActionListener(l);
        }
    }

    // Details Area

    public JTextArea getDetailsArea() {
        return detailsArea;
    }

    public void setDetailsAreaText(String text) {
        detailsArea.setText(text);
    }

    public void setTableModelListener(ListSelectionListener l) {
        joinableLecturesTable.getSelectionModel().addListSelectionListener(l);
    }

    // Column Headers

    public void setColumnHeaderListener(MouseListener l) {
        joinableLecturesTable.getTableHeader().addMouseListener(l);
    }

}

