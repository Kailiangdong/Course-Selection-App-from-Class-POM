package controller;

import view.*;
import SQLiteManager.*;

import java.sql.SQLException;

public class LecturesDetailsController extends Controller {

    private LecturesDetailsView lecturesDetailsView;
    private SQLiteManager sqLiteManager;
    private MenuController menuController;
    private LecturesTableController lecturesTableController;
    private CommentController commentController;
    private String studentName;
    private String lectureID;

    public LecturesDetailsController(
            SQLiteManager sqLiteManager,
            MenuController menuController,
            LecturesTableController lecturesTableController) {
        this.lecturesDetailsView = new LecturesDetailsView();
        this.sqLiteManager = sqLiteManager;
        this.menuController = menuController;
        this.lecturesTableController = lecturesTableController;
        this.lectureID = "";
        this.commentController = new CommentController(this, this.menuController);
        this.lecturesDetailsView.setCommentPane(commentController.getView().getMainPane());
        this.attach(commentController);

        update();
        addListeners();
    }

    //<editor-fold desc="Get/Set Section">
    @Override
    public View getView() {
        return lecturesDetailsView;
    }

    public LecturesTableController getTableController() {return lecturesTableController;}

    public String getStudentName() {
        return studentName;
    }

    public String getLectureID() {
        return lectureID;
    }
    //</editor-fold>

    //<editor-fold desc="Query-building Section">
    private QueryBuilder addLectureQuery(String studentID, String lectureID) {
        QueryBuilder addBuilder = new QueryBuilder(QueryType.INSERT);
        addBuilder.addInsertTab("ATTENDS");
        addBuilder.addInsertCols(new String[]{"STUDENT_ID","LECTURE_ID"});
        addBuilder.addInsertVals(new String[]{"(SELECT s.ID FROM STUDENTS s WHERE s.NAME = '" + studentName + "')", lectureID});
        return addBuilder;
    }

    private QueryBuilder deleteLectureQuery(String studentName, String lectureID) {
        QueryBuilder deleteBuilder = new QueryBuilder(QueryType.DELETE);
        deleteBuilder.addDeleteTab("ATTENDS");
        deleteBuilder.addDeleteWhere(new String[]{
                "STUDENT_ID = (SELECT s.ID FROM STUDENTS s WHERE s.NAME = '" + studentName + "')",
                "LECTURE_ID = " + lectureID
        });
        return deleteBuilder;
    }
    //</editor-fold>

    //<editor-fold desc="Action Section">
    public void addLecture() {
        try {
            sqLiteManager.executeQuery(addLectureQuery(studentName,lectureID));
        } catch (SQLException e) {
            System.out.println("Error executing adding lecture: " + e.toString());
        }
    }
    public void deleteLecture() {
        try {
            sqLiteManager.executeQuery(deleteLectureQuery(studentName,lectureID));
        } catch (SQLException e) {
            System.out.println("Error executing deleting lecture: " + e.toString());
        }
    }
    //</editor-fold>

    //<editor-fold desc="Rest Section">
    @Override
    void addListeners() {
        lecturesDetailsView.setRowListener(new ButtonListener(this));


    }

    @Override
    public void update() {
        // get updated state
        lecturesDetailsView.getJoinDropLectureButton().setText(
                lecturesTableController.getSelectedRight()?"Drop Lecture":"Join Lecture");
        studentName = menuController.getActiveStudentName();
        lectureID = lecturesTableController.getSelectedLectureId();
        notifyAllObservers();
    }
    //</editor-fold>

}
