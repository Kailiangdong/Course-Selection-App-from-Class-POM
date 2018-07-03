package view.account;

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
    public JPanel getMainPane() { return mainPane; }
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
    public void setUserNameValue(String value) {userNameValue.setText(value);}
    public void setMajorValue(String value) {majorValue.setText(value);}
    public void setMinorValue(String value) {minorValue.setText(value);}
    public void setUserNameCheck(String value) {userNameCheck.setText(value);}
    public void setPasswordCheck(String value) {passwordCheck.setText(value);}
    public void setMajorCheck(String value) {majorCheck.setText(value);}
    public void setMinorCheck(String value) {minorCheck.setText(value);}
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
    public void addUserNameActionListener(ActionListener actionListener) { userNameButton.addActionListener(actionListener); }
    public void addPasswordActionListener(ActionListener actionListener) { passwordButton.addActionListener(actionListener); }
    public void addMajorActionListener(ActionListener actionListener) {
        majorButton.addActionListener(actionListener);
    }
    public void addMinorActionListener(ActionListener actionListener) {
        minorButton.addActionListener(actionListener);
    }
    //</editor-fold>
}
