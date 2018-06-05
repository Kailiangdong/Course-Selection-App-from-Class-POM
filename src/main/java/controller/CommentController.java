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

    CommentView view;
    LecturesTableController tableController;
    MenuController menuController;

    String lectureId;
    String studentName;

    ArrayList<LectureComment> comments = new ArrayList<>();
    LectureComment selectedComment;

    public CommentController(LecturesTableController tableController, MenuController menuController) {
        this.view = new CommentView();
        this.tableController = tableController;
        this.menuController = menuController;
        addListeners();
        addDummyComments();
        update();
    }

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

    @Override
    void addListeners() {
        view.addCreationListener(new CommentCreateListener());
        view.addSelectionListener(new CommentSelectListener());
        view.addDeletionListener(new CommentDeleteListener());
    }

    public CommentView getView() {
        return view;
    }

    public void update() {
        studentName = menuController.getActiveStudentName();
        lectureId = tableController.getSelectedLectureId();
        if (!lectureId.equals("")) {
            view.setList(comments.toArray(new LectureComment[]{}));
            view.show();
        } else {
            view.hide();
        }
    }

    class CommentCreateListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String input = view.getInputText();
            if (input != null && !input.isEmpty()) {
                comments.add(new LectureComment(studentName, input, ZonedDateTime.now(), false));
                view.clearInputField();
                update();
            }
        }
    }

    class CommentSelectListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            ListSelectionModel lsm = (ListSelectionModel) e.getSource();
            int index = lsm.getMinSelectionIndex();
            if (index < 0) {
                return;
            }
            selectedComment = (LectureComment) view.getObject(index);

            if (selectedComment.getAuthor().equals(studentName)) {
                view.showOptions();
            } else {
                view.hideOptions();
            }
        }
    }

    class CommentDeleteListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            comments.remove(selectedComment);
            update();
        }
    }

}
