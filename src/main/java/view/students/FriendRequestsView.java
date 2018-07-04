package view.students;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import university.FriendRequest;
import university.LectureComment;
import view.View;
import view.lectures.CommentView;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;

public class FriendRequestsView implements View {
    private JPanel mainPane;
    private JPanel actionPane;
    private JPanel rightPane;
    private JPanel leftPane;
    private JButton rejectRequestButton;
    private JButton acceptRequestButton;
    private JPanel listPane;
    private JScrollPane scrollPane;
    private JList requestsList;

    public FriendRequestsView() {
        DefaultListModel listModel = new DefaultListModel();
    }

    public void showOptions() {
        rejectRequestButton.setVisible(true);
        acceptRequestButton.setVisible(true);
    }

    public void hideOptions() {
        rejectRequestButton.setVisible(false);
        acceptRequestButton.setVisible(false);
    }

    public JPanel getMainPane() {
        return mainPane;
    }

    public Object getObject(int index) {
        return requestsList.getModel().getElementAt(index);
    }

    //<editor-fold desc="Listener Section">
    public void setAcceptListener(ActionListener l) {
        this.acceptRequestButton.addActionListener(l);
    }


    public void setDeletionListener(ActionListener l) {
        this.rejectRequestButton.addActionListener(l);
    }

    public void setSelectionListener(ListSelectionListener l) {
        this.requestsList.getSelectionModel().addListSelectionListener(l);
    }
    //</editor-fold desc="Listener Section">

    public void setList(FriendRequest[] reqestTable) {
        RequestListModel listModel = new RequestListModel();
        for (FriendRequest request : reqestTable) {
            listModel.addElement(request);
        }
        //dataList.setListData(commentTable);
        //dataList.removeAll();
        requestsList.setModel(listModel);
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
        mainPane.setPreferredSize(new Dimension(500, 300));
        actionPane = new JPanel();
        actionPane.setLayout(new BorderLayout(0, 0));
        actionPane.setPreferredSize(new Dimension(500, 50));
        actionPane.setRequestFocusEnabled(true);
        mainPane.add(actionPane, BorderLayout.SOUTH);
        leftPane = new JPanel();
        leftPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        actionPane.add(leftPane, BorderLayout.WEST);
        acceptRequestButton = new JButton();
        acceptRequestButton.setText("Accept");
        leftPane.add(acceptRequestButton);
        rightPane = new JPanel();
        rightPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        actionPane.add(rightPane, BorderLayout.EAST);
        rejectRequestButton = new JButton();
        rejectRequestButton.setText("Reject");
        rightPane.add(rejectRequestButton);
        listPane = new JPanel();
        listPane.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPane.add(listPane, BorderLayout.CENTER);
        scrollPane = new JScrollPane();
        listPane.add(scrollPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        requestsList = new JList();
        scrollPane.setViewportView(requestsList);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPane;
    }


    class RequestListModel extends DefaultListModel {

    }
}
