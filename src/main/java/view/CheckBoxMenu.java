package view;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CheckBoxMenu extends JMenu {

    private String[] dynamicLabels;
    private String[] staticLabels;
    private JCheckBoxMenuItem[] items;

    public CheckBoxMenu(String title, String[] dynamicLabels, String[] staticLabels) {
        super(title);
        this.dynamicLabels = dynamicLabels;
        this.staticLabels = staticLabels;
        this.buildMenu();
    }

    private void buildMenu() {
        items = new JCheckBoxMenuItem[staticLabels.length + dynamicLabels.length];
        JCheckBoxMenuItem item;
        for(int i = 0; i < staticLabels.length; i++) {
            item =  new JCheckBoxMenuItem(staticLabels[i]);
            item.setState(true);
            items[i] = item;
        }
        for(int i = 0; i < dynamicLabels.length; i++) {
            item = new JCheckBoxMenuItem(dynamicLabels[i]);
            item.setState(true);
            items[staticLabels.length + i] = item;
            this.add(item);
        }
    }

    public List<String> getActiveLabels() {
        List<String> activeLabels = new ArrayList<>();
        for(JCheckBoxMenuItem item : items) {
            if(item.getState()) {
                activeLabels.add(item.getText());
            }
        }
        return activeLabels;
    }

    public void addLabelListener(ActionListener l) {
        for (JMenuItem item : items) {
            item.addActionListener(l);
        }
    }
}