package view;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import controller.ButtonListener;

import javax.swing.*;
import java.awt.*;

public class LecturesDetailsView implements View {

    private JPanel mainPane;
    private JButton joinDropLectureButton;
    private JPanel buttonPane;
    private JPanel commentPane;
    private JPanel detailsPane;
    private JButton mapLocationButton;
    private JButton participantsButton;
    private JTextPane textPane;

    //<editor-fold desc="Get/Set Section">
    private void setPane(JPanel pane, JPanel paneContent) {
        pane.removeAll();
        pane.add(paneContent, BorderLayout.CENTER);
        pane.setPreferredSize(paneContent.getPreferredSize());
    }

    @Override
    public JPanel getMainPane() {
        return mainPane;
    }

    public JButton getJoinDropLectureButton() {
        return joinDropLectureButton;
    }

    public JButton getMapLocationButton() {
        return mapLocationButton;
    }

    public JButton getParticipantsButton() {
        return participantsButton;
    }

    public JPanel getCommentPane() {
        return commentPane;
    }

    public void setCommentPane(JPanel commentPane) {
        setPane(this.commentPane, commentPane);
    }

    public JPanel getDetailsPane() {
        return detailsPane;
    }
    //</editor-fold>

    //<editor-fold desc="Listener Section">
    public void setRowListener(ButtonListener l) {
        joinDropLectureButton.addActionListener(l);
    }

    public void setMapListener(ButtonListener l) {
        mapLocationButton.addActionListener(l);
    }

    public void setParticipantsButton(ButtonListener l) {
        participantsButton.addActionListener(l);
    }

    public void setTextPane(String text) {
        textPane.setText(text);
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
        mainPane.setPreferredSize(new Dimension(800, 300));
        buttonPane = new JPanel();
        buttonPane.setLayout(new BorderLayout(0, 0));
        buttonPane.setPreferredSize(new Dimension(150, 300));
        buttonPane.setVerifyInputWhenFocusTarget(true);
        mainPane.add(buttonPane, BorderLayout.WEST);
        joinDropLectureButton = new JButton();
        joinDropLectureButton.setText("Join Lecture");
        joinDropLectureButton.setToolTipText("Click here to join the selected lecture");
        buttonPane.add(joinDropLectureButton, BorderLayout.NORTH);
        mapLocationButton = new JButton();
        mapLocationButton.setText("Map");
        buttonPane.add(mapLocationButton, BorderLayout.CENTER);
        participantsButton = new JButton();
        participantsButton.setText("Participants");
        buttonPane.add(participantsButton, BorderLayout.SOUTH);
        commentPane = new JPanel();
        commentPane.setLayout(new BorderLayout(0, 0));
        commentPane.setPreferredSize(new Dimension(400, 300));
        mainPane.add(commentPane, BorderLayout.EAST);
        detailsPane = new JPanel();
        detailsPane.setLayout(new BorderLayout(0, 0));
        detailsPane.setPreferredSize(new Dimension(250, 300));
        mainPane.add(detailsPane, BorderLayout.CENTER);
        textPane = new JTextPane();
        detailsPane.add(textPane, BorderLayout.CENTER);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPane;
    }

    //</editor-fold>


}
