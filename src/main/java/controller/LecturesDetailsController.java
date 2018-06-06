package controller;

import view.*;
import SQLiteManager.*;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

public class LecturesDetailsController extends Controller {

    private LecturesDetailsView lecturesDetailsView;
    private CommentController commentController;
    private SQLiteManager sqLiteManager;
    private MenuController menuController;
    private LecturesTableController lecturesTableController;
    private String studentName;
    private String lectureID;

    public LecturesDetailsController(
            SQLiteManager sqLiteManager, MenuController menuController, LecturesTableController tableController) {
        this.lecturesDetailsView = new LecturesDetailsView();
        this.sqLiteManager = sqLiteManager;
        this.menuController = menuController;
        this.lecturesTableController = tableController;
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
    private QueryBuilder queryLocationDetails(String lectureID) {
        QueryBuilder query = new QueryBuilder(QueryType.SELECT);

        query.addSelect("ROOMNUMBER", "LECTURES");
        query.addSelect("BUILDINGNUMBER", "LECTURES");

        query.addFrom("LECTURES");

        query.addWhere("L.ID = " + lectureID);

        return query;
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
    public void showMap() {
        String[][] queryResult = new String[0][];
        try {
            queryResult = sqLiteManager.executeQuery(queryLocationDetails(lectureID));
        } catch (SQLException e) {
            System.out.println("Error executing showing map: " + e.toString());
        }

        String roomnumber = queryResult[0][0];
        String buildingnumber = queryResult[0][1];

        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI("http://portal.mytum.de/displayRoomMap?" + roomnumber + "@" + buildingnumber));
            } catch (IOException | URISyntaxException e1) {
                e1.printStackTrace();
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="Rest Section">
    @Override
    void addListeners() {
        lecturesDetailsView.setRowListener(new ButtonListener(this));
        lecturesDetailsView.setMapListener(new ButtonListener(this));

    }

    @Override
    public void update() {
        // get updated state
        lecturesDetailsView.getJoinDropLectureButton().setText(
                lecturesTableController.getSelectedRight()?"Drop Lecture":"Join Lecture");
        studentName = menuController.getActiveStudentName();
        lectureID = lecturesTableController.getSelectedLectureId();
    }
    //</editor-fold>

}
