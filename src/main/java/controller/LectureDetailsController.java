package controller;

import view.*;
import SQLiteManager.*;

import java.sql.SQLException;

public class LectureDetailsController extends Controller {

    private LectureDetailsView view;
    private SQLiteManager sqLiteManager;
    private MenuController menuController;
    private LecturesTableController tableController;
    private String studentID;
    private String lectureID;

    public LectureDetailsController(
            SQLiteManager sqLiteManager, MenuController menuController, LecturesTableController tableController) {
        this.view = new LectureDetailsView();
        this.sqLiteManager = sqLiteManager;
        this.menuController = menuController;
        this.tableController = tableController;
    }

    //<editor-fold desc="Get/Set Section">
    @Override
    public View getView() {
        return view;
    }
    //</editor-fold>

    //<editor-fold desc="Query-building Section">
    private QueryBuilder addLectureQuery(String studentID, String lectureID) {
        QueryBuilder addBuilder = new QueryBuilder(QueryType.INSERT);
        addBuilder.addInsertTab("ATTENDS");
        addBuilder.addInsertCols(new String[]{"STUDENT_ID","LECTURE_ID"});
        addBuilder.addInsertVals(new String[]{studentID, lectureID});
        return addBuilder;
    }

    private QueryBuilder deleteLectureQuery(String studentID, String lectureID) {
        QueryBuilder deleteBuilder = new QueryBuilder(QueryType.DELETE);
        deleteBuilder.addDeleteTab("ATTENDS");
        deleteBuilder.addDeleteWhere(new String[]{
                "STUDENT_ID=" + studentID,"LECTURE_ID=" + lectureID
        });
        return deleteBuilder;
    }
    //</editor-fold>

    //<editor-fold desc="Action Section">
    public void addLecture() {
        try {
            sqLiteManager.executeQuery(addLectureQuery(studentID,lectureID));
        } catch (SQLException e) {
            System.out.println("Error executing adding lecture: " + e.toString());
        }
    }
    public void deleteLecture() {
        try {
            sqLiteManager.executeQuery(deleteLectureQuery(studentID,lectureID));
        } catch (SQLException e) {
            System.out.println("Error executing deleting lecture: " + e.toString());
        }
    }
    //</editor-fold>

    //<editor-fold desc="Rest Section">
    @Override
    void addListeners() {

    }

    @Override
    public void update() {
        // get updated state
        studentID = menuController.getActiveStudentName();
        lectureID = tableController.getSelectedLectureId();
    }
    //</editor-fold>

}
