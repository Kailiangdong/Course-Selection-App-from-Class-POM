package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MenuView implements View {

    public static final char CHECK_SYMBOL = '*';

    private JPanel mainPane;
    private JMenuBar menuBar;

    // Menus
    private JMenu mainMenu;
    private CheckBoxMenu columnMenu;
    private CheckBoxMenu chairMenu;

    // Menu contents
    private JMenuItem quitItem;
    private JMenuItem refreshItem;
    private JMenuItem logoutItem;
    private JMenuItem feedbackItem;
    private JMenuItem pushItem;
    private JMenuItem pullItem;

    public MenuView() {
        menuBar = new JMenuBar(); // top menu bar
        mainPane.add(menuBar);
        buildMainMenu();
    }

    // Main Menu

    public void setFeedbackButtonListener(ActionListener actionListener) {
        feedbackItem.addActionListener(actionListener);
    }

    public void setLogoutButtonListener(ActionListener actionListener) {
        logoutItem.addActionListener(actionListener);
    }

    public void setRefreshButtonListener(ActionListener l) {
        refreshItem.addActionListener(l);
    }

    public void setQuitButtonListener(ActionListener l) {
        quitItem.addActionListener(l);
    }

    public void setPullButtonListener(ActionListener l) {
        pullItem.addActionListener(l);
    }

    public void setPushButtonListener(ActionListener l) {
        pushItem.addActionListener(l);
    }

    private void buildMainMenu() {
        mainMenu = new JMenu("☰"); // program control
        quitItem = new JMenuItem("Quit");
        refreshItem = new JMenuItem("Refresh");
        logoutItem = new JMenuItem("Logout");
        feedbackItem = new JMenuItem("Feedback");
        pushItem = new JMenuItem("Push");
        pullItem = new JMenuItem("Pull");

        // Add items
        mainMenu.add(refreshItem);
        mainMenu.add(feedbackItem);
        mainMenu.add(logoutItem);
        mainMenu.add(quitItem);
        mainMenu.add(pushItem);
        mainMenu.add(pullItem);

        // Add menu
        menuBar.add(mainMenu);
    }

    // Hide / Show Columns

    public void setColumnMenu(String[] dynamicLabels, String[] staticLabels) {
        if (columnMenu != null) {
            menuBar.remove(columnMenu);
        }
        columnMenu = new CheckBoxMenu("Columns", dynamicLabels, staticLabels);
        menuBar.add(columnMenu);
    }

    public void setColumnMenuListener(ActionListener l) {
        columnMenu.addLabelListener(l);
    }

    public CheckBoxMenu getColumnChoiceMenu() {
        return columnMenu;
    }

    // Hide / Show Chairs

    public void setChairMenu(String[] chairNames) {
        if (chairMenu != null) {
            menuBar.remove(chairMenu);
        }
        chairMenu = new CheckBoxMenu("Chairs", chairNames, new String[]{});
        menuBar.add(chairMenu);
        menuBar.updateUI();
    }

    public void setChairMenuListener(ActionListener l) {
        chairMenu.addLabelListener(l);
    }

    public CheckBoxMenu getChairMenu() {
        return chairMenu;
    }

    @Override
    public JPanel getMainPane() {
        return mainPane;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPane = new JPanel();
        mainPane.setLayout(new BorderLayout(0, 0));
        mainPane.setPreferredSize(new Dimension(0, 25));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPane;
    }
}

