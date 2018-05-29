package view;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseListener;

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

    public void setRowListener(ListSelectionListener l) {
        leftTable.getSelectionModel().addListSelectionListener(l);
        rightTable.getSelectionModel().addListSelectionListener(l);
    }

    public void setHeaderListener(MouseListener l) {
        leftTable.getTableHeader().addMouseListener(l);
        rightTable.getTableHeader().addMouseListener(l);
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
        mainPane.setPreferredSize(new Dimension(908, 435));
        leftPane = new JScrollPane();
        mainPane.add(leftPane, BorderLayout.WEST);
        leftTable = new JTable();
        leftPane.setViewportView(leftTable);
        rightPane = new JScrollPane();
        mainPane.add(rightPane, BorderLayout.EAST);
        rightTable = new JTable();
        rightPane.setViewportView(rightTable);
        infoArea = new JTextArea();
        infoArea.setText("Joined lectures / Joinable lectures");
        mainPane.add(infoArea, BorderLayout.NORTH);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPane;
    }
}
