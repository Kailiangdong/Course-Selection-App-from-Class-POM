package controller;

import view.*;
import SQLiteManager.*;

import javax.swing.*;
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

    private void updateTextPane() {
        if(lectureID.equals("")) {
            lecturesDetailsView.setTextPane("Select a lecture to view its details");
            return;
        }
        try {
            String[][] table = sqLiteManager.executeQuery(queryLectureDetails(lectureID));
            StringBuilder sb = new StringBuilder();
            sb.append("ID: ");
            sb.append(table[0][0]);
            sb.append(" ");

            sb.append("Title: ");
            sb.append(table[0][1]);
            sb.append("\n");

            sb.append("Chair: ");
            sb.append(table[0][2]);
            sb.append(" ");

            sb.append("ECTS: ");
            sb.append(table[0][3]);
            sb.append(" ");

            sb.append("SEMESTER: ");
            sb.append(table[0][4]);
            sb.append("\n");

            sb.append("Lecturer: ");
            sb.append(table[0][5]);
            sb.append("\n");

            sb.append("Time: ");
            sb.append(table[0][6]);
            sb.append("\n");

            sb.append("Room number: ");
            sb.append(table[0][7]);
            sb.append(" ");

            sb.append("Building number: ");
            sb.append(table[0][8]);
            sb.append("\n");

            sb.append("Grade factor: ");
            sb.append(table[0][9]);
            sb.append(" ");

            lecturesDetailsView.setTextPane(sb.toString());
        } catch (SQLException e) {
            System.out.println("Error executing query " + e.toString());
        }
    }

    private QueryBuilder queryLectureDetails(String lectureId) {
        QueryBuilder query = new QueryBuilder(QueryType.SELECT);

        query.addSelect("ID", "LECTURES");
        query.addSelect("TITLE", "LECTURES");
        query.addSelect("CHAIR", "LECTURES");
        query.addSelect("ECTS", "LECTURES");
        query.addSelect("SEMESTER", "LECTURES");
        query.addSelect("LECTURER", "LECTURES");
        query.addSelect("TIME", "LECTURES");
        query.addSelect("ROOMNUMBER", "LECTURES");
        query.addSelect("BUILDINGNUMBER", "LECTURES");
        query.addSelect("GRADE_FACTOR", "LECTURES");

        query.addFrom("LECTURES");

        query.addWhere("L.ID = " + lectureId);
        return query;
    }

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

    public void showParticipants() {
        // Query part
        QueryBuilder query = new QueryBuilder(QueryType.SELECT);
        query.addSelect("NAME", "STUDENTS");
        query.addFrom("STUDENTS");
        query.addFrom("LECTURES");
        query.addFrom("ATTENDS");
        query.addWhere("L.ID = " + lectureID);
        query.addWhere("L.ID = A.LECTURE_ID");
        query.addWhere("S.ID = A.STUDENT_ID");

        try {
            String[][] res = sqLiteManager.executeQuery(query);
            // Create new window and fill it with content
            JFrame participantsView = new JFrame("Participants");
            String[] title = {"Participants"};
            JTable participantsTable = new JTable(res, title);
            JScrollPane pane = new JScrollPane(participantsTable);
            JPanel participantsPanel = new JPanel();

            participantsPanel.add(pane);
            participantsView.add(participantsPanel);
            participantsView.setSize(400, 500);
            participantsView.setVisible(true);
        } catch (SQLException e) {
            System.out.println("Error in participants query " + e.toString());
        }

    }
    //</editor-fold>

    //<editor-fold desc="Rest Section">
    @Override
    void addListeners() {
        lecturesDetailsView.setRowListener(new ButtonListener(this));
        lecturesDetailsView.setMapListener(new ButtonListener(this));
        lecturesDetailsView.setParticipantsButton(new ButtonListener(this));
    }

    @Override
    public void update() {
        // get updated state
        lecturesDetailsView.getJoinDropLectureButton().setText(
                lecturesTableController.getSelectedRight()?"Drop Lecture":"Join Lecture");
        studentName = menuController.getActiveStudentName();
        lectureID = lecturesTableController.getSelectedLectureId();

        // update TextPane
        updateTextPane();
    }
    //</editor-fold>

}
