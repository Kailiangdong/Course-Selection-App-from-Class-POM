package view;

import javax.swing.*;
import java.awt.*;

public class AppFrame extends JFrame implements View {

    private JPanel mainPane;
    private JPanel middlePane;
    private JPanel menuPane;
    private JPanel bottomPane;

    public AppFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("University App feat. Team 13");
        this.add(mainPane);
    }

    //<editor-fold desc="Open/Close Section">
    public void open() {
        this.pack();
        this.setVisible(true);
    }

    public void close() {
        this.setVisible(false);
        this.dispose();
    }
    //</editor-fold>

    //<editor-fold desc="Get/Set Section">
    private void setPane(JPanel pane, JPanel paneContent) {
        pane.removeAll();
        pane.add(paneContent, BorderLayout.CENTER);
        pane.setPreferredSize(paneContent.getPreferredSize());
        this.pack();
    }

    public JPanel getMenuPane() {
        return menuPane;
    }

    public void setMenuPane(JPanel menuPane) { setPane(this.menuPane, menuPane); }

    public JPanel getMiddlePane() {
        return middlePane;
    }

    public void setMiddlePane(JPanel middlePane) {
        setPane(this.middlePane, middlePane);
    }

    public JPanel getBottomPane() {
        return bottomPane;
    }

    public void setBottomPane(JPanel bottomPane) {
        setPane(this.bottomPane, bottomPane);
    }

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
        mainPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-13602637)), null));
        middlePane = new JPanel();
        middlePane.setLayout(new BorderLayout(0, 0));
        mainPane.add(middlePane, BorderLayout.CENTER);
        menuPane = new JPanel();
        menuPane.setLayout(new BorderLayout(0, 0));
        mainPane.add(menuPane, BorderLayout.NORTH);
        bottomPane = new JPanel();
        bottomPane.setLayout(new BorderLayout(0, 0));
        mainPane.add(bottomPane, BorderLayout.SOUTH);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPane;
    }

    //</editor-fold>

}
