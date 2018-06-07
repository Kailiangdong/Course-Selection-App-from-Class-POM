package controller;

import view.CommentView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

class CommentSelectListener implements ListSelectionListener {

    private CommentController commentController;
    private CommentView commentView;

    public CommentSelectListener(CommentController commentController) {
        this.commentController = commentController;
        commentView = (CommentView) commentController.getView();
    }

    //<editor-fold desc="Action Section">
    @Override
    public void valueChanged(ListSelectionEvent e) {
        ListSelectionModel lsm = (ListSelectionModel) e.getSource();
        int index = lsm.getMinSelectionIndex();
        if (index < 0) {
            return;
        }
        commentController.setSelectedComment((LectureComment) commentView.getObject(index));

        if (commentController.getSelectedComment().getAuthor().equals(commentController.getStudentName())) {
            commentView.showOptions();
        } else {
            commentView.hideOptions();
        }
    }
    //</editor-fold>

}
