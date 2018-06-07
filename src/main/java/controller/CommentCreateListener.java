package controller;

import view.CommentView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.ZonedDateTime;

class CommentCreateListener implements ActionListener {

    private CommentController commentController;
    private CommentView commentView;

    public CommentCreateListener(CommentController commentController) {
        this.commentController = commentController;
        commentView = (CommentView) commentController.getView();
    }

    //<editor-fold desc="Action Section">
    @Override
    public void actionPerformed(ActionEvent e) {
        String input = commentView.getInputText();
        if (input != null && !input.isEmpty()) {
            commentController.getComments().add(new LectureComment(
                    commentController.getStudentName(), input, ZonedDateTime.now(), false));
            commentView.clearInputField();
            commentController.update();
        }
    }
    //</editor-fold>

}
