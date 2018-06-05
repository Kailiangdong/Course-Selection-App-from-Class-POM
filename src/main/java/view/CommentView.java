package view;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;

import controller.LectureComment;


public class CommentView implements View {

    private JPanel mainPane;
    private JPanel createPane;
    private JPanel listPane;
    private JPanel actionPane;
    private JSeparator topSep;
    private JSeparator bottomSep;
    private JList dataList;
    private JTextField inputField;
    private JButton addButton;
    private JButton deleteButton;

    public JPanel getMainPane() {
        return mainPane;
    }

    public CommentView() {
        DefaultListModel listModel = new DefaultListModel();
        dataList.setCellRenderer(new CommentCellRenderer(200));
    }

    public String getInputText() {
        return inputField.getText();
    }

    public void clearInputField() {
        inputField.setText("");
    }

    public void setList(LectureComment[] commentTable) {
        CommentListModel listModel = new CommentListModel();
        for (LectureComment comment : commentTable) {
            listModel.addElement(comment);
        }
        dataList.setModel(listModel);
    }

    public void addDeletionListener(ActionListener l) {
        this.deleteButton.addActionListener(l);
    }

    public void addCreationListener(ActionListener l) {
        this.addButton.addActionListener(l);
    }

    public void addSelectionListener(ListSelectionListener l) {
        this.dataList.getSelectionModel().addListSelectionListener(l);
    }

    public void showOptions() {
        deleteButton.setVisible(true);
    }

    public void hideOptions() {
        deleteButton.setVisible(false);
    }

    public void hide() {
        mainPane.setVisible(false);
    }

    public void show() {
        mainPane.setVisible(true);
    }

    public Object getObject(int index) {
        return dataList.getModel().getElementAt(index);
    }

    class CommentListModel extends DefaultListModel {

    }

    class CommentCellRenderer extends DefaultListCellRenderer {

        public static final String HTML_REG = "<html>\n" +
                "<body style='width: %dpx'>\n" +
                "<p><strong>%s</strong> - %s</p>\n" +
                "<p style=\"text-align: right;\"><span style=\"color: #999999;\"><em>%s</em></span></p>\n" +
                "</html>";

        public static final String HTML_ANSWER = "<html>\n" +
                "<body style='width: %dpx'>\n" +
                "<p style=\"padding-left: 30px;\"><strong>%s</strong> - %s</p>\n" +
                "<p style=\"text-align: right;\"><span style=\"color: #999999;\"><em>%s</em></span></p>\n" +
                "</html>";

        private int width;

        public CommentCellRenderer(int width) {
            this.width = width;
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            LectureComment comment = (LectureComment) value;

            String template = comment.isAnswer() ? HTML_ANSWER : HTML_REG;
            String text = String.format(template, width, comment.getAuthor(), comment.getText(), comment.getTime().toString());
            return super.getListCellRendererComponent(list, text, index, isSelected,
                    cellHasFocus);
        }

    }

}
