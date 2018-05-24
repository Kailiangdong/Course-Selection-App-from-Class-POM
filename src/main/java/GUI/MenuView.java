package main.java.GUI;

import javax.swing.*;
import javax.swing.event.MenuListener;
import java.awt.event.ActionListener;

public class MenuView implements View {

    private JPanel mainPane;
    private JMenuBar menuBar;

    // Menus
    private JMenu mainMenu;
    private JMenu projectionMenu;
    private JMenu selectionMenu;
    private JMenu tableMenu;

    // Menu contents
    private JMenuItem[] selectionMenuItems;
    private JMenuItem[] projectionMenuItems;
    private JMenuItem[] tableMenuItems;
    private JMenuItem quitProgram;
    private JMenuItem updateState;

    public MenuView() {
        menuBar = new JMenuBar(); // top menu bar

        mainMenu = new JMenu("☰"); // program control
        quitProgram = new JMenuItem("Quit");
        updateState = new JMenuItem("Refresh");
        mainMenu.add(updateState);
        mainMenu.add(quitProgram);

        tableMenu = new JMenu("T");
        projectionMenu = new JMenu("P"); // column projection menu
        selectionMenu = new JMenu("S"); // row selection menu

        // order of JMenuBar.add() calls determines displayed order
        menuBar.add(mainMenu);
        menuBar.add(tableMenu);
        menuBar.add(projectionMenu);
        menuBar.add(selectionMenu);

        mainPane.add(menuBar);
    }

    public void setUpdateStateListener(ActionListener l) {
        updateState.addActionListener(l);
    }

    @Override
    public JPanel getMainPane() {
        return mainPane;
    }
}
