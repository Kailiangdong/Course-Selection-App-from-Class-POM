package view;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ChoiceMenu extends JMenu {

    String[] labels;
    JRadioButtonMenuItem[] items;
    ButtonGroup group;

    public ChoiceMenu(String title, String[] labels) {
        super(title);
        this.labels = labels;
        buildMenu();
    }

    private void buildMenu() {
        items = new JRadioButtonMenuItem[labels.length];
        group = new ButtonGroup();
        JRadioButtonMenuItem item;
        for(int i = 0; i < labels.length; i++) {
            item = new JRadioButtonMenuItem(labels[i]);
            items[i] = item;
            item.setActionCommand(labels[i]);
            group.add(item);
            this.add(item);
        }
    }

    public String getActiveLabel() {
        return group.getSelection().getActionCommand();
    }

    public void addLabelListener(ActionListener l) {
        for (JMenuItem item : items) {
            item.addActionListener(l);
        }
    }
}
