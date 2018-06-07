package controller;

import view.CommentView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.ZonedDateTime;
import java.util.ArrayList;

public class CommentController extends Controller {

    /*
     * TODO:
     * - make sure that only one item can be selected in list
     */

    private CommentView commentView;
    private LecturesDetailsController lecturesDetailsController;
    private MenuController menuController;
    private String lectureId;
    private String studentName;
    private ArrayList<LectureComment> comments = new ArrayList<>();
    private LectureComment selectedComment;

    public CommentController(
            LecturesDetailsController lecturesDetailsController,
            MenuController menuController) {
        this.commentView = new CommentView();
        this.lecturesDetailsController = lecturesDetailsController;
        this.menuController = menuController;
        addListeners();
        addDummyComments();
        update();
    }

    //<editor-fold desc="Get/Set Section">
    @Override
    public CommentView getView() {
        return commentView;
    }

    public String getStudentName() {return studentName;}

    public ArrayList<LectureComment> getComments() {return comments;}

    public LectureComment getSelectedComment() {return selectedComment;}

    public void setSelectedComment(LectureComment selectedComment) {this.selectedComment = selectedComment;}
    //</editor-fold>

    //<editor-fold desc="Action Section">
    private void addDummyComments() {
        comments.add(new LectureComment("Stefan", "Lorem ipsum dolor sit amet, consectetur adipisici " +
                "elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua.", ZonedDateTime.now().minusHours(1), false));
        comments.add(new LectureComment("Markus", "Ut enim ad minim veniam, quis nostrud exercitation " +
                "ullamco laboris nisi ut aliquid ex ea commodi consequat.", ZonedDateTime.now().minusHours(2), false));
        comments.add(new LectureComment("Robert", "Quis aute iure reprehenderit in voluptate velit esse " +
                "cillum dolore eu fugiat nulla pariatur. ", ZonedDateTime.now().minusHours(1), false));
        comments.add(new LectureComment("Joana", "Excepteur sint obcaecat cupiditat non proident, sunt in " +
                "culpa qui officia deserunt mollit anim id est laborum.", ZonedDateTime.now().minusHours(1), true));
    }
    //</editor-fold>

    //<editor-fold desc="Rest Section">
    @Override
    void addListeners() {
        commentView.setCreationListener(new CommentCreateListener(this));
        commentView.setSelectionListener(new CommentSelectListener(this));
        commentView.setDeletionListener(new CommentDeleteListener(this));
    }

    @Override
    public void update() {
        studentName = menuController.getActiveStudentName();
        lectureId = lecturesDetailsController.getLectureID();
        if (!lectureId.equals("")) {
            commentView.setList(comments.toArray(new LectureComment[]{}));
            commentView.getMainPane().setVisible(true);
        } else {
            commentView.getMainPane().setVisible(false);
        }
    }
    //</editor-fold>

}
