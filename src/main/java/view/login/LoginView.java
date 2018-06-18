package view.login;

import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import view.View;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView implements View {

    private boolean passwordState = true;
    private boolean studentIdState = true;
    private boolean userNameState = true;
    private boolean subjectState = true;

    private JPanel mainPane;
    private JPanel selectionPanel;
    private JPanel loginPanel;
    private JPanel registerPanel;
    private JPanel ActionPanel;
    private JRadioButton loginButton;
    private JRadioButton registerButton;
    private JTextField userNameField;
    private JButton confirmButton;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox majorBox;
    private JComboBox minorBox;
    private JLabel warningLabelPassword;
    private JTextField studentIDField;
    private JLabel warningLabelID;
    private JLabel warningLabelUserName;
    private JLabel warningLabelSubject;

    public LoginView() {
        if (loginButton.isSelected()) {
            hideRegisterPane();
        } else {
            showRegisterPane();
        }
    }

    public void addSelectionActionListener(ActionListener actionListener) {
        loginButton.addActionListener(actionListener);
        registerButton.addActionListener(actionListener);
        majorBox.addActionListener(actionListener);
        minorBox.addActionListener(actionListener);
    }

    public void addUserDocumentListener(DocumentListener documentListener) {
        userNameField.getDocument().addDocumentListener(documentListener);
        passwordField.getDocument().addDocumentListener(documentListener);
        confirmPasswordField.getDocument().addDocumentListener(documentListener);
        studentIDField.getDocument().addDocumentListener(documentListener);
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

    public void setSubjectState(boolean state) {
        subjectState = state;
        updateWarnings();
    }

    public void setUserNameState(boolean state) {
        userNameState = state;
        updateWarnings();
    }

    public void setPasswordState(boolean state) {
        passwordState = state;
        updateWarnings();
    }

    public void setStudentIdState(boolean state) {
        studentIdState = state;
        updateWarnings();
    }

    public boolean isLoginSelected() {
        return loginButton.isSelected();
    }

    public String getMajorInput() {
        return (String) majorBox.getSelectedItem();
    }

    public String getMinorInput() {
        return (String) minorBox.getSelectedItem();
    }

    public String getPasswordInput() {
        return passwordField.getText();
    }

    public String getConfPasswordInput() {
        return confirmPasswordField.getText();
    }

    public String getStudentIdInput() {
        return studentIDField.getText();
    }

    public String getUserNameInput() {
        return userNameField.getText();
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(new JFrame(),
                message,
                "Inane error",
                JOptionPane.ERROR_MESSAGE);
    }

    public void showRegisterPane() {
        for (Component component : registerPanel.getComponents()) {
            component.setVisible(true);
        }
    }

    public void hideRegisterPane() {
        for (Component component : registerPanel.getComponents()) {
            component.setVisible(false);
            if (component instanceof JTextField) {
                ((JTextField) component).setText("");
            }
        }
    }

    private void updateWarnings() {
        if (!(studentIdState && passwordState && userNameState && subjectState)) {
            confirmButton.setVisible(false);
        } else {
            confirmButton.setVisible(true);
        }
        if (studentIdState) {
            warningLabelID.setVisible(false);
        } else {
            warningLabelID.setVisible(true);
        }
        if (passwordState) {
            warningLabelPassword.setVisible(false);
        } else {
            warningLabelPassword.setVisible(true);
        }
        if (userNameState) {
            warningLabelUserName.setVisible(false);
        } else {
            warningLabelUserName.setVisible(true);
        }
        if (subjectState) {
            warningLabelSubject.setVisible(false);
        } else {
            warningLabelSubject.setVisible(true);
        }
    }

    //<editor-fold desc="Getters & Setters">
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
        mainPane.setLayout(new FormLayout("fill:250px:noGrow,left:4dlu:noGrow,fill:500px:noGrow,left:4dlu:noGrow,fill:250px:noGrow", "center:d:noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow"));
        mainPane.setToolTipText("");
        mainPane.setVisible(true);
        selectionPanel = new JPanel();
        selectionPanel.setLayout(new FormLayout("fill:200px:noGrow,left:4dlu:noGrow,fill:300px:noGrow", "center:d:grow,top:3dlu:noGrow,center:30px:noGrow,top:3dlu:noGrow,center:30px:noGrow"));
        CellConstraints cc = new CellConstraints();
        mainPane.add(selectionPanel, cc.xy(3, 1));
        loginButton = new JRadioButton();
        loginButton.setSelected(true);
        loginButton.setText("Login");
        selectionPanel.add(loginButton, cc.xy(3, 3));
        registerButton = new JRadioButton();
        registerButton.setText("Register");
        selectionPanel.add(registerButton, cc.xy(3, 5));
        final Spacer spacer1 = new Spacer();
        selectionPanel.add(spacer1, cc.xy(1, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        loginPanel = new JPanel();
        loginPanel.setLayout(new FormLayout("right:200px:noGrow,left:4dlu:noGrow,fill:260px:grow,left:4dlu:noGrow,fill:20px:noGrow", "fill:d:noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow"));
        mainPane.add(loginPanel, cc.xy(3, 3));
        final JLabel label1 = new JLabel();
        label1.setText("User Name");
        loginPanel.add(label1, cc.xy(1, 1));
        userNameField = new JTextField();
        loginPanel.add(userNameField, cc.xy(3, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label2 = new JLabel();
        label2.setText("Password");
        loginPanel.add(label2, cc.xy(1, 3));
        passwordField = new JPasswordField();
        loginPanel.add(passwordField, cc.xy(3, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        warningLabelUserName = new JLabel();
        warningLabelUserName.setForeground(new Color(-2013634));
        warningLabelUserName.setText("⚠");
        warningLabelUserName.setToolTipText("Please enter available user name.");
        warningLabelUserName.setVisible(false);
        loginPanel.add(warningLabelUserName, cc.xy(5, 1));
        registerPanel = new JPanel();
        registerPanel.setLayout(new FormLayout("right:200px:noGrow,left:4dlu:noGrow,fill:260px:grow,left:4dlu:noGrow,fill:20px:noGrow", "center:d:noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow,top:3dlu:noGrow,center:max(d;4px):noGrow"));
        mainPane.add(registerPanel, cc.xy(3, 5));
        final JLabel label3 = new JLabel();
        label3.setText("Confirm Password");
        registerPanel.add(label3, cc.xy(1, 1));
        confirmPasswordField = new JPasswordField();
        registerPanel.add(confirmPasswordField, cc.xy(3, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        final JLabel label4 = new JLabel();
        label4.setText("Major");
        registerPanel.add(label4, cc.xy(1, 5));
        final JLabel label5 = new JLabel();
        label5.setText("Minor");
        registerPanel.add(label5, cc.xy(1, 7));
        majorBox = new JComboBox();
        registerPanel.add(majorBox, cc.xy(3, 5));
        minorBox = new JComboBox();
        registerPanel.add(minorBox, cc.xy(3, 7));
        warningLabelPassword = new JLabel();
        Font warningLabelPasswordFont = this.$$$getFont$$$(null, Font.BOLD, -1, warningLabelPassword.getFont());
        if (warningLabelPasswordFont != null) warningLabelPassword.setFont(warningLabelPasswordFont);
        warningLabelPassword.setForeground(new Color(-2013634));
        warningLabelPassword.setPreferredSize(new Dimension(20, 20));
        warningLabelPassword.setText("⚠");
        warningLabelPassword.setToolTipText("Please enter matching passwords.");
        warningLabelPassword.setVisible(false);
        registerPanel.add(warningLabelPassword, cc.xy(5, 1));
        final JLabel label6 = new JLabel();
        label6.setText("Student ID");
        registerPanel.add(label6, cc.xy(1, 3));
        studentIDField = new JTextField();
        studentIDField.setText("");
        studentIDField.setToolTipText("");
        registerPanel.add(studentIDField, cc.xy(3, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        warningLabelID = new JLabel();
        Font warningLabelIDFont = this.$$$getFont$$$(null, Font.BOLD, -1, warningLabelID.getFont());
        if (warningLabelIDFont != null) warningLabelID.setFont(warningLabelIDFont);
        warningLabelID.setForeground(new Color(-2013634));
        warningLabelID.setText("⚠");
        warningLabelID.setToolTipText("Please enter numeric value / available student id.");
        warningLabelID.setVisible(false);
        registerPanel.add(warningLabelID, cc.xy(5, 3));
        warningLabelSubject = new JLabel();
        warningLabelSubject.setForeground(new Color(-2013634));
        warningLabelSubject.setText("⚠");
        warningLabelSubject.setToolTipText("Please select different subjects.");
        warningLabelSubject.setVisible(false);
        registerPanel.add(warningLabelSubject, cc.xy(5, 5));
        ActionPanel = new JPanel();
        ActionPanel.setLayout(new FormLayout("fill:200px:noGrow,left:4dlu:noGrow,fill:300px:noGrow", "center:d:grow,top:3dlu:noGrow,center:max(d;4px):noGrow"));
        mainPane.add(ActionPanel, cc.xy(3, 7));
        confirmButton = new JButton();
        confirmButton.setText("Confirm");
        ActionPanel.add(confirmButton, cc.xy(3, 3));
        final Spacer spacer2 = new Spacer();
        ActionPanel.add(spacer2, cc.xy(1, 3, CellConstraints.FILL, CellConstraints.DEFAULT));
        final Spacer spacer3 = new Spacer();
        mainPane.add(spacer3, cc.xy(1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        final Spacer spacer4 = new Spacer();
        mainPane.add(spacer4, cc.xy(5, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
        label1.setLabelFor(userNameField);
        label2.setLabelFor(passwordField);
        warningLabelUserName.setLabelFor(userNameField);
        label3.setLabelFor(confirmPasswordField);
        label4.setLabelFor(minorBox);
        label5.setLabelFor(majorBox);
        warningLabelPassword.setLabelFor(confirmPasswordField);
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(loginButton);
        buttonGroup.add(registerButton);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPane;
    }

    //</editor-fold>

    //<editor-fold desc="---">
    // Dummy
    //</editor-fold>


}
