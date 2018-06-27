package controller.lectures;

import SQLiteManager.QueryBuilder;
import SQLiteManager.QueryType;
import SQLiteManager.SQLiteManager;
import controller.Controller;
import controller.login.LoginController;
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
import java.util.ArrayList;

public class CommentController extends Controller {

    private CommentView view;
    private SQLiteManager sqLiteManager;
    private DetailsController detailsController;
    private LoginController loginController;

    private Student student;
    private Lecture lecture;

    private ArrayList<LectureComment> comments = new ArrayList<>();
    private LectureComment selectedComment;
    private boolean commentLiked;

    public CommentController(SQLiteManager sqLiteManager, DetailsController detailsController, LoginController loginController) {
        this.view = new CommentView();
        this.detailsController = detailsController;
        this.sqLiteManager = sqLiteManager;
        this.loginController = loginController;

        addListeners();
        update();
    }

    //<editor-fold desc="Actions">
    @Override
    public void update() {
        student = loginController.getLoggedInStudent();
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
        view.clearInputField();
        LectureComment comment = new LectureComment(student, text);
        try {
            sqLiteManager.executeQuery(addCommentQuery(student.getId(),lecture.getId(),comment.getTime(), comment.getDate(),comment.getText()));

        } catch (SQLException e) {
            System.out.println("Error executing add comment: " + e.toString());
        }
        update();
    }

    private void deleteComment() {
        LectureComment comment = getSelectedComment();
        try {
            sqLiteManager.executeQuery(deleteCommentQuery(comment.getId()));

        } catch (SQLException e) {
            System.out.println("Error executing delete comment: " + e.toString());
        }
        update();
    }

    private void queryComments() {
        String[][] queryResult = new String[0][];
        comments = new ArrayList<>();
        view.hideDeleteOption();
        view.hideLikeDislikeOption();
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

            LectureComment comment = new LectureComment(student, content, date, time, commentID, getLikeNumber(commentID));
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
    private QueryBuilder addCommentQuery(int studentID, int lectureID, String time, String date, String content) {
        content = "'" + content + "'";
        time = "'" + time + "'";
        date = "'" + date + "'";
        QueryBuilder addBuilder = new QueryBuilder(QueryType.INSERT);
        addBuilder.addInsertTab("COMMENTS");
        addBuilder.addInsertCols(new String[]{"ID","STUDENT_ID", "LECTURE_ID","TIME", "DATE","CONTENT"});
        addBuilder.addInsertVals(new String[]{null,Integer.toString(studentID), Integer.toString(lectureID), time, date, content});
        return addBuilder;
    }

    private QueryBuilder deleteCommentQuery(int commentID) {
        QueryBuilder deleteBuilder = new QueryBuilder(QueryType.DELETE);
        deleteBuilder.addDeleteTab("COMMENTS");
        deleteBuilder.addDeleteWhere(new String[]{
                "ID = " + commentID,
        });
        return deleteBuilder;
    }

    private QueryBuilder addLikeQuery(int studentID, int commentID) {
        QueryBuilder addBuilder = new QueryBuilder(QueryType.INSERT);
        addBuilder.addInsertTab("LIKES");
        addBuilder.addInsertCols(new String[]{"STUDENT_ID","COMMENT_ID"});
        addBuilder.addInsertVals(new String[]{Integer.toString(studentID),Integer.toString(commentID)});
        return addBuilder;
    }

    private QueryBuilder removeLikeQuery(int studentID, int commentID) {
        QueryBuilder deleteBuilder = new QueryBuilder(QueryType.DELETE);
        deleteBuilder.addDeleteTab("LIKES");
        deleteBuilder.addDeleteWhere(new String[]{
                "STUDENT_ID = " + studentID,
                "COMMENT_ID = " + commentID,
        });
        return deleteBuilder;
    }

    private int getLikeNumber(int commentID) {
        QueryBuilder query = new QueryBuilder(QueryType.SELECT);
        query.addSelect("STUDENT_ID", "LIKES");
        query.addFrom("LIKES");
        query.addWhere("L.COMMENT_ID = " + commentID);
        String[][] likeMatrix = null;
        try {
            likeMatrix = sqLiteManager.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Error querying like numbers: " + e.toString());
        }
        return likeMatrix.length;
    }

    private boolean getIfLiked(int commentID) {
        QueryBuilder query = new QueryBuilder(QueryType.SELECT);
        query.addSelect("STUDENT_ID", "LIKES");
        query.addFrom("LIKES");
        query.addWhere("L.COMMENT_ID = " + commentID);
        query.addWhere("L.STUDENT_ID = " + student.getId());
        String[][] likeMatrix = null;
        try {
            likeMatrix = sqLiteManager.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Error querying like numbers: " + e.toString());
        }
        if(likeMatrix == null || likeMatrix.length==0) {
            return false;
        }
        else {
            return true;
        }
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
    //</editor-fold>

    //<editor-fold desc="Listeners">
    @Override
    public void addListeners() {
        view.setCreationListener(new CreateListener());
        view.setSelectionListener(new SelectListener());
        view.setDeletionListener(new DeleteListener());
        view.setLikeDislikeListener(new LikeDislikeListener());
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

    class LikeDislikeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (commentLiked) {
                    sqLiteManager.executeStatement(removeLikeQuery(student.getId(), selectedComment.getId()));
                } else {
                    sqLiteManager.executeStatement(addLikeQuery(student.getId(), selectedComment.getId()));
                }
            } catch (SQLException ex) {
                System.out.println("Error executing like query " + ex.toString());
            }
            CommentController.this.update();
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
                view.showDeleteOption();
            } else {
                view.hideDeleteOption();
            }

            if (getSelectedComment() == null) {
                view.hideLikeDislikeOption();
            } else {
                commentLiked = getIfLiked(selectedComment.getId());
                if(commentLiked) {
                    view.setLikeDislikeButtonText("Unlike");
                } else {
                    view.setLikeDislikeButtonText("Like");
                }
                view.showLikeDislikeOption();
            }
        }
    }
    //</editor-fold>
}
