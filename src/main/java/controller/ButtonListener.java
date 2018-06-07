package controller;

import view.LecturesDetailsView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonListener implements ActionListener {

    private LecturesDetailsController lecturesDetailsController;
    private LecturesDetailsView lecturesDetailsView;

    public ButtonListener(LecturesDetailsController lecturesDetailsController) {
        this.lecturesDetailsController = lecturesDetailsController;
        lecturesDetailsView = (LecturesDetailsView) lecturesDetailsController.getView();
    }

    //<editor-fold desc="Action Section">
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == lecturesDetailsView.getJoinDropLectureButton()) {
            if(lecturesDetailsController.getStudentName() != null && lecturesDetailsController.getLectureID() != null) {
                if(lecturesDetailsController.getTableController().getSelectedRight()) {
                    lecturesDetailsController.deleteLecture();
                }
                else {
                    lecturesDetailsController.addLecture();
                }
            }
        } else {
            throw new RuntimeException();
        }
        lecturesDetailsController.notifyAllObservers();
    }
    //</editor-fold>

}
