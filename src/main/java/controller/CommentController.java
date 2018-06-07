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

    private CommentView commentView;
    private LecturesDetailsController lecturesDetailsController;
    private LecturesTableController tableController;
    private SQLiteManager sqLiteManager;
    private MenuController menuController;
    private String lectureId;
    private String studentName;
    private ArrayList<LectureComment> comments = new ArrayList<>();
    private LectureComment selectedComment;

    public CommentController(
            LecturesDetailsController lecturesDetailsController,
            MenuController menuController,
            SQLiteManager sqLiteManager,
            LecturesTableController lecturesTableController) {
        this.commentView = new CommentView();
        this.lecturesDetailsController = lecturesDetailsController;
        this.menuController = menuController;
        this.sqLiteManager = sqLiteManager;
        this.tableController = lecturesTableController;
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

    //<editor-fold desc="Get/Set Section">
    @Override
    public CommentView getView() {
        return commentView;
    }

    public String getStudentName() {
        return studentName;
    }

    public ArrayList<LectureComment> getComments() {
        return comments;
    }

    public LectureComment getSelectedComment() {
        return selectedComment;
    }

    public void setSelectedComment(LectureComment selectedComment) {
        this.selectedComment = selectedComment;
    }
    //</editor-fold>

    //<editor-fold desc="Action Section">
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
        System.out.println(studentName);
        lectureId = lecturesDetailsController.getLectureID();
        comments = getComments();
        if (!lectureId.equals("")) {
            commentView.setList(comments.toArray(new LectureComment[]{}));
            commentView.getMainPane().setVisible(true);
        } else {
            commentView.getMainPane().setVisible(false);
        }
    }
    //</editor-fold>

}
