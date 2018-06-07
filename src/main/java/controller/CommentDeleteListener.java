package controller;

import view.CommentView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class CommentDeleteListener implements ActionListener {

    private CommentController commentController;
    private CommentView commentView;

    public CommentDeleteListener(CommentController commentController) {
        this.commentController = commentController;
        commentView = (CommentView) commentController.getView();
    }

    //<editor-fold desc="Action-Section">
    @Override
    public void actionPerformed(ActionEvent e) {
        commentController.getComments().remove(commentController.getSelectedComment());
        commentController.update();
    }
    //</editor-fold>

}
