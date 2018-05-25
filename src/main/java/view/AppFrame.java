package main.java.view;

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

    public void open() {
        this.pack();
        this.setVisible(true);
    }

    public void close() {
        this.setVisible(false);
        this.dispose();
    }

    public void setMenuPane(JPanel menuPane) {
        this.menuPane.removeAll();
        this.menuPane.add(menuPane, BorderLayout.CENTER);
        this.menuPane.setPreferredSize(menuPane.getPreferredSize());
        this.pack();
    }

    public void setMiddlePane(JPanel middlePane) {
        this.middlePane.removeAll();
        this.middlePane.add(middlePane, BorderLayout.CENTER);
        this.middlePane.setPreferredSize(middlePane.getPreferredSize());
        this.pack();
    }

    public void setBottomPane(JPanel bottomPane) {
        this.bottomPane.removeAll();
        this.bottomPane.add(bottomPane, BorderLayout.CENTER);
        this.bottomPane.setPreferredSize(bottomPane.getPreferredSize());
        this.pack();
    }

    public JPanel getMainPane() {
        return mainPane;
    }

}
