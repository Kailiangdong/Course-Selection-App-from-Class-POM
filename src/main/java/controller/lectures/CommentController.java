package controller.lectures;

import SQLiteManager.QueryBuilder;
import SQLiteManager.QueryType;
import SQLiteManager.SQLiteManager;
import controller.Controller;
import university.Lecture;
import university.LectureComment;
import university.Student;
import view.lectures.CommentView;

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

    private CommentView view;
    private SQLiteManager sqLiteManager;
    private DetailsController detailsController;

    private Student student;
    private Lecture lecture;

    private ArrayList<LectureComment> comments = new ArrayList<>();
    private LectureComment selectedComment;

    public CommentController(SQLiteManager sqLiteManager, DetailsController detailsController) {
        this.view = new CommentView();
        this.detailsController = detailsController;
        this.sqLiteManager = sqLiteManager;

        addListeners();
        update();
    }

    //<editor-fold desc="Actions">
    @Override
    public void update() {
        student = detailsController.getStudent();
        lecture = detailsController.getLecture();

        if (lecture != null) {
            queryComments();
            view.setList(comments.toArray(new LectureComment[]{}));
            view.getMainPane().setVisible(true);
        } else {
            view.getMainPane().setVisible(false);
        }
    }

    private void addComment(String text) {
        // TODO: call insert query
        view.clearInputField();
        LectureComment comment = new LectureComment(student, text);
        update();
    }

    private void deleteComment() {
        LectureComment comment = getSelectedComment();
        // TODO: call delete query

        update();
    }

    private void queryComments() {
        String[][] queryResult = new String[0][];
        comments = new ArrayList<>();
        view.hideOptions();
        try {
            queryResult = sqLiteManager.executeQuery(selectAllComments(Integer.toString(lecture.getId())));
        } catch (SQLException e) {
            System.out.println("Error executing show comment: " + e.toString());
        }
        for (int i = 0; i < queryResult.length; i++) {
            int studentID = Integer.parseInt(queryResult[i][1]);
            Student student = sqLiteManager.getStudent(studentID);
            String content = queryResult[i][0];
            String time = queryResult[i][2];
            String date = queryResult[i][3];
            int commentID = Integer.parseInt(queryResult[i][4]);

            LectureComment comment = new LectureComment(student, content, date, time, commentID);
            comments.add(comment);
        }
    }
    //</editor-fold>

    //<editor-fold desc="Queries">
    private QueryBuilder selectAllComments(String lectureID) {
        QueryBuilder query = new QueryBuilder(QueryType.SELECT);

        query.addSelect("CONTENT", "COMMENTS");
        query.addSelect("STUDENT_ID", "COMMENTS");
        query.addSelect("TIME", "COMMENTS");
        query.addSelect("DATE", "COMMENTS");
        query.addSelect("ID", "COMMENTS");

        query.addFrom("COMMENTS");

        query.addWhere("C.LECTURE_ID = " + lectureID);

        query.addOrderBy("DATE", "COMMENTS", "DESC");
        query.addOrderBy("TIME", "COMMENTS", "DESC");

        return query;
    }
    //</editor-fold>

    //<editor-fold desc="Getters">
    @Override
    public CommentView getView() {
        return view;
    }

    public ArrayList<LectureComment> getComments() {
        return comments;
    }

    public LectureComment getSelectedComment() {
        return selectedComment;
    }
    //</editor-fold>

    //<editor-fold desc="Setters">
    public void setSelectedComment(LectureComment selectedComment) {
        this.selectedComment = selectedComment;
    }
    //</editor-fold>

    //<editor-fold desc="Other">
    public static ZonedDateTime changeStringToUTC(String DateTimeStr) {
        DateTimeFormatter beijingFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Asia/Shanghai"));
        if (DateTimeStr == "") {
            return null;
        }
        ZonedDateTime beijingDateTime = ZonedDateTime.parse(DateTimeStr, beijingFormatter);
        return beijingDateTime.withZoneSameInstant(ZoneId.of("UTC"));
    }
    //</editor-fold>

    //<editor-fold desc="Listeners">
    @Override
    public void addListeners() {
        view.setCreationListener(new CreateListener());
        view.setSelectionListener(new SelectListener());
        view.setDeletionListener(new DeleteListener());
    }

    class CreateListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String input = view.getInputText();
            addComment(input);
        }

    }

    class DeleteListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            deleteComment();
        }
    }

    class SelectListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {

            ListSelectionModel lsm = (ListSelectionModel) e.getSource();
            int index = lsm.getMinSelectionIndex();
            if (index < 0) {
                return;
            }

            setSelectedComment((LectureComment) view.getObject(index));

            if (student == null) {
                return;
            }

            if (getSelectedComment().getAuthor().equals(student)) {
                view.showOptions();
            } else {
                view.hideOptions();
            }
        }
    }
    //</editor-fold>
}
