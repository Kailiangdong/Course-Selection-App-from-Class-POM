package controller;

import SQLiteManager.QueryBuilder;
import SQLiteManager.QueryType;
import SQLiteManager.SQLiteManager;
import org.sqlite.util.StringUtils;
import view.CommentView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CommentController extends Controller {

    /*
     * TODO:
     * - make sure that only one item can be selected in list
     */

    CommentView view;
    LecturesTableController tableController;
    MenuController menuController;
    private SQLiteManager sqLiteManager;

    String lectureId;
    String studentName;

    ArrayList<LectureComment> comments = new ArrayList<>();
    LectureComment selectedComment;

    public CommentController(LecturesTableController tableController, MenuController menuController) {
        this.view = new CommentView();
        this.tableController = tableController;
        this.menuController = menuController;
        this.sqLiteManager = sqLiteManager;
        addListeners();
        addDummyComments();
        update();
    }

    private QueryBuilder commentQuery(String lectureID) {
        QueryBuilder query = new QueryBuilder(QueryType.SELECT);

        query.addSelect("CONTENT", "COMMENTS");
        query.addSelect("STUDENT_ID", "COMMENTS");
        query.addSelect("TIME", "COMMENTS");
        query.addSelect("DATE", "COMMENTS");


        query.addFrom("COMMENTS");

        query.addWhere("L.ID = " + lectureID);

        return query;
    }

    public static ZonedDateTime changeStringToUTC(String DateTimeStr) {
        DateTimeFormatter beijingFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Asia/Shanghai"));
        if (DateTimeStr == "") {
            return null;
        }
        ZonedDateTime beijingDateTime = ZonedDateTime.parse(DateTimeStr, beijingFormatter);
        return beijingDateTime.withZoneSameInstant(ZoneId.of("UTC"));
    }

    private void addDummyComments() {

        String[][] queryResult = new String[0][];
        try {
            queryResult = sqLiteManager.executeQuery(commentQuery(lectureId));

        } catch (SQLException e) {
            System.out.println("Error executing show comment: " + e.toString());
        }
        for (int i = 0; i < queryResult.length; i++) {
            comments.add(new LectureComment(queryResult[i][0], queryResult[i][1], changeStringToUTC(queryResult[i][2] + "," + queryResult[i][3]),
                    false));
        }
/*        comments.add(new LectureComment("Stefan", "Lorem ipsum dolor sit amet, consectetur adipisici " +
                "elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua.", ZonedDateTime.now().minusHours(1), false));
        comments.add(new LectureComment("Markus", "Ut enim ad minim veniam, quis nostrud exercitation " +
                "ullamco laboris nisi ut aliquid ex ea commodi consequat.", ZonedDateTime.now().minusHours(2), false));
        comments.add(new LectureComment("Robert", "Quis aute iure reprehenderit in voluptate velit esse " +
                "cillum dolore eu fugiat nulla pariatur. ", ZonedDateTime.now().minusHours(1), false));
        comments.add(new LectureComment("Joana", "Excepteur sint obcaecat cupiditat non proident, sunt in " +
                "culpa qui officia deserunt mollit anim id est laborum.", ZonedDateTime.now().minusHours(1), true));*/
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
