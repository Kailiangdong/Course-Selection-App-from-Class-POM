package view.account;

import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import view.View;
import javax.swing.*;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionListener;

public class AccountView implements View {
    private JPanel mainPane;
    private JTextField userNameField;
    private JPasswordField oldPasswordField;
    private JPasswordField passwordRepeatField;
    private JButton userNameButton;
    private JButton passwordButton;
    private JButton majorButton;
    private JButton minorButton;
    private JComboBox majorBox;
    private JComboBox minorBox;
    private JPasswordField passwordField;
    private JLabel userNameValue;
    private JLabel majorValue;
    private JLabel minorValue;
    private JLabel userNameCheck;
    private JLabel passwordCheck;
    private JLabel majorCheck;
    private JLabel minorCheck;

    //<editor-fold desc="Getters & Setters">
    @Override
    public JPanel getMainPane() {
        return mainPane;
    }

    public String getUserNameInput() {
        return userNameField.getText();
    }

    public String getPasswordInput() {
        return passwordField.getText();
    }

    public String getPasswordRepeatInput() {
        return passwordRepeatField.getText();
    }

    public String getOldPasswordInput() {
        return oldPasswordField.getText();
    }

    public String getMajorInput() {
        return (String) majorBox.getSelectedItem();
    }

    public String getMinorInput() {
        return (String) minorBox.getSelectedItem();
    }

    public void setUserNameValue(String value) {
        userNameValue.setText(value);
    }

    public void setMajorValue(String value) {
        majorValue.setText(value);
    }

    public void setMinorValue(String value) {
        minorValue.setText(value);
    }

    public void setUserNameCheck(String value) {
        userNameCheck.setText(value);
    }

    public void setPasswordCheck(String value) {
        passwordCheck.setText(value);
    }

    public void setMajorCheck(String value) {
        majorCheck.setText(value);
    }

    public void setMinorCheck(String value) {
        minorCheck.setText(value);
    }

    public void setMajorBoxChoices(String[] majors) {
        for (String major : majors) {
            majorBox.addItem(major);
        }
    }

    public void setMinorBoxChoices(String[] minors) {
        for (String minor : minors) {
            minorBox.addItem(minor);
        }
    }
    //</editor-fold>


    //<editor-fold desc="Listeners">
    public void addUserNameActionListener(ActionListener actionListener) {
        userNameButton.addActionListener(actionListener);
    }

    public void addPasswordActionListener(ActionListener actionListener) {
        passwordButton.addActionListener(actionListener);
    }

    public void addMajorActionListener(ActionListener actionListener) {
        majorButton.addActionListener(actionListener);
    }

    public void addMinorActionListener(ActionListener actionListener) {
        minorButton.addActionListener(actionListener);
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
        mainPane.setLayout(new FormLayout("fill:d:grow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:grow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:max(d;4px):noGrow,left:4dlu:noGrow,fill:d:grow", "center:d:grow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:d:grow,top:3dlu:noGrow,center:d:grow"));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new FormLayout("", ""));
        CellConstraints cc = new CellConstraints();
        mainPane.add(panel1, cc.xy(3, 15));
        final Spacer spacer1 = new Spacer();
        mainPane.add(spacer1, cc.xywh(1, 3, 1, 13, CellConstraints.FILL, CellConstraints.DEFAULT));
        final Spacer spacer2 = new Spacer();
        mainPane.add(spacer2, cc.xywh(13, 3, 1, 13, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label1 = new JLabel();
        label1.setText("User Name:");
        mainPane.add(label1, cc.xy(3, 3));
        userNameValue = new JLabel();
        userNameValue.setText("");
        mainPane.add(userNameValue, cc.xy(5, 3));
        userNameField = new JTextField();
        mainPane.add(userNameField, cc.xy(7, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label2 = new JLabel();
        label2.setText("New Password");
        mainPane.add(label2, cc.xy(3, 5));
        final JLabel label3 = new JLabel();
        label3.setText("Repeat New Password");
        mainPane.add(label3, cc.xy(3, 7));
        final JLabel label4 = new JLabel();
        label4.setText("Old Password");
        mainPane.add(label4, cc.xy(3, 9));
        oldPasswordField = new JPasswordField();
        mainPane.add(oldPasswordField, cc.xy(7, 9, CellConstraints.FILL, CellConstraints.DEFAULT));
        passwordRepeatField = new JPasswordField();
        mainPane.add(passwordRepeatField, cc.xy(7, 7, CellConstraints.FILL, CellConstraints.DEFAULT));
        passwordField = new JPasswordField();
        mainPane.add(passwordField, cc.xy(7, 5, CellConstraints.FILL, CellConstraints.DEFAULT));
        userNameButton = new JButton();
        userNameButton.setText("Button");
        mainPane.add(userNameButton, cc.xy(9, 3));
        passwordButton = new JButton();
        passwordButton.setText("Button");
        mainPane.add(passwordButton, cc.xy(9, 9));
        final JLabel label5 = new JLabel();
        label5.setText("Major");
        mainPane.add(label5, cc.xy(3, 11));
        majorValue = new JLabel();
        majorValue.setText("");
        mainPane.add(majorValue, cc.xy(5, 11));
        final JLabel label6 = new JLabel();
        label6.setText("Minor");
        mainPane.add(label6, cc.xy(3, 13));
        minorValue = new JLabel();
        minorValue.setText("");
        mainPane.add(minorValue, cc.xy(5, 13));
        majorButton = new JButton();
        majorButton.setText("Button");
        mainPane.add(majorButton, cc.xy(9, 11));
        minorButton = new JButton();
        minorButton.setText("Button");
        mainPane.add(minorButton, cc.xy(9, 13));
        majorBox = new JComboBox();
        mainPane.add(majorBox, cc.xy(7, 11));
        minorBox = new JComboBox();
        mainPane.add(minorBox, cc.xy(7, 13));
        final Spacer spacer3 = new Spacer();
        mainPane.add(spacer3, cc.xy(1, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
        final Spacer spacer4 = new Spacer();
        mainPane.add(spacer4, cc.xy(1, 17, CellConstraints.DEFAULT, CellConstraints.FILL));
        userNameCheck = new JLabel();
        userNameCheck.setText("");
        mainPane.add(userNameCheck, cc.xy(11, 3));
        passwordCheck = new JLabel();
        passwordCheck.setText("");
        mainPane.add(passwordCheck, cc.xy(11, 9));
        majorCheck = new JLabel();
        majorCheck.setText("asdfgg");
        mainPane.add(majorCheck, cc.xy(11, 11));
        minorCheck = new JLabel();
        minorCheck.setText("Label");
        mainPane.add(minorCheck, cc.xy(11, 13));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPane;
    }
    //</editor-fold>
}
