package controller;

import view.LecturesDetailsView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonListener implements ActionListener {

    private LecturesDetailsController lecturesDetailsController;
    private LecturesDetailsView lecturesDetailsView;

    public ButtonListener(LecturesDetailsController lecturesDetailsController) {
        this.lecturesDetailsController = lecturesDetailsController;
        lecturesDetailsView = (LecturesDetailsView) lecturesDetailsController.getView();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == lecturesDetailsView.getJoinLectureButton()) {
            if(lecturesDetailsController.getStudentName() != null && lecturesDetailsController.getLectureID() != null) {
                lecturesDetailsController.addLecture();
            }
        } else if(e.getSource() == lecturesDetailsView.getDropLectureButton()) {
            if(lecturesDetailsController.getStudentName() != null && lecturesDetailsController.getLectureID() != null) {
                lecturesDetailsController.deleteLecture();
            }
        } else {
            throw new RuntimeException();
        }
        lecturesDetailsController.notifyAllObservers();
    }

}
