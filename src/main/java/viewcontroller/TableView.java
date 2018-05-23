package main.java.viewcontroller;

import java.util.*;
import main.java.SQLiteManager.SQLiteManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class TableView implements ActionListener{

    private SQLiteManager manager;

    private JFrame frame;
    private JMenuBar menuBar;
    private JMenu mainMenu;
    private JMenu projectionMenu;
    private JMenu selectionMenu;
    private JMenuItem update;
    private JMenuItem exit;
    private JMenuItem modulID;
    private JMenuItem title;
    private JMenuItem chair;
    private JMenuItem csOne;
    private JMenuItem csTwo;
    private JMenuItem csThree;
    private JPanel tablePanel;
    private Object rowData[][];
    private Object columnNames[];
    private JTable joinableLecturesTable;
    private JScrollPane joinableLecturesPanel;
    private JLabel details;

    private ArrayList<String> base;
    private ArrayList<String> projection;
    private ArrayList<String> selection;

    public TableView() {
        manager = new SQLiteManager();

        frame = new JFrame("University app");
        frame.setSize(500, 300);

        menuBar = new JMenuBar();
        mainMenu = new JMenu("☰");
        update = new JMenuItem("Update Table");
        exit = new JMenuItem("EXIT");
        update.addActionListener(this);
        exit.addActionListener(this);
        mainMenu.add(update);
        mainMenu.add(exit);
        menuBar.add(mainMenu);

        projectionMenu = new JMenu("P");
        modulID = new JMenuItem("✓ ModulID");
        title = new JMenuItem("✓ Title");
        chair = new JMenuItem("Chair");
        modulID.addActionListener(this);
        title.addActionListener(this);
        chair.addActionListener(this);
        projectionMenu.add(modulID);
        projectionMenu.add(title);
        projectionMenu.add(chair);
        menuBar.add(projectionMenu);

        selectionMenu = new JMenu("S");
        csOne = new JMenuItem("✓ Chair CS 1");
        csTwo = new JMenuItem("✓ Chair CS 1");
        csThree = new JMenuItem("Chair CS 3");
        csOne.addActionListener(this);
        csTwo.addActionListener(this);
        csThree.addActionListener(this);
        projectionMenu.add(csOne);
        projectionMenu.add(csTwo);
        projectionMenu.add(csThree);
        menuBar.add(selectionMenu);

        tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());

        details = new JLabel("Details", SwingConstants.CENTER);

        joinableLecturesPanel = new JScrollPane(joinableLecturesTableUpdate());

        tablePanel.add(joinableLecturesPanel, BorderLayout.CENTER);
        tablePanel.add(menuBar,BorderLayout.NORTH);
        frame.add(tablePanel);

        base = new ArrayList();
        projection = new ArrayList();
        projection.add("ModulID");
        projection.add("Title");
        selection = new ArrayList();
        selection.add("Chair CS 1");
        selection.add("Chair CS 2");
        selection.add("Chair CS 2");

        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent object) {
        if (object.getSource() == exit){
            frame.setVisible(false);
            frame.dispose();
        }
        if (object.getSource() == modulID){
            if (modulID.getText().equals("✓ ModulID")){
                modulID.setText("ModulID");
                projection.remove("ModulID");

            }
            else{
                modulID.setText("✓ ModulID");
                projection.add("ModulID");
            }
        }
        if (object.getSource() == title){
            if (title.getText().equals("✓ Title")){
                title.setText("Title");
                projection.remove("Title");
            }
            else{
                title.setText("✓ Title");
                projection.add("Title");
            }
        }
        if (object.getSource() == chair) {
            if (chair.getText().equals("✓ Chair")){
                chair.setText("Chair");
                projection.remove("Chair");
            }
            else{
                chair.setText("✓ Chair");
                projection.add("Chair");
            }
        }
        if (object.getSource() == csOne){
            if (csOne.getText().equals("✓ Chair CS 1")){
                csOne.setText("Chair CS 1");
                selection.remove("Chair CS 1");

            }
            else{
                csOne.setText("✓ Chair CS 1");
                selection.add("Chair CS 1");
            }
        }
        if (object.getSource() == csTwo){
            if (csTwo.getText().equals("✓ Chair CS 2")){
                csTwo.setText("Chair CS 2");
                selection.remove("Chair CS 2");
            }
            else{
                csTwo.setText("✓ Chair CS 2");
                selection.add("Chair CS 2");
            }
        }
        if (object.getSource() == csThree) {
            if (csThree.getText().equals("✓ Chair CS 3")){
                csThree.setText("Chair CS 3");
                selection.remove("Chair CS 3");
            }
            else{
                csThree.setText("✓ Chair CS 3");
                selection.add("Chair CS 3");
            }
        }
    }

    private JTable joinableLecturesTableUpdate(){
        columnNames = new Object[2];
        columnNames[0] = "Module ID";
        columnNames[1] = "Title";
        try {
            rowData = manager.queryJoinableLectures("Stefan");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return joinableLecturesTable = new JTable(rowData, columnNames);
    }

    public static void main(String[] args) {
        TableView table = new TableView();
    }

}

