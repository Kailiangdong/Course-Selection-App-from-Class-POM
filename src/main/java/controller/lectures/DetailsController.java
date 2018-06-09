package controller.lectures;

import controller.Controller;
import controller.login.LoginController;
import university.Lecture;
import university.Student;
import view.*;
import SQLiteManager.*;
import view.lectures.DetailsView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

public class DetailsController extends Controller {

    public static final int NO_SELECTION = -1;

    private DetailsView detailsView;
    private SQLiteManager sqLiteManager;
    private LoginController loginController;
    private TableController tableController;
    private CommentController commentController;

    private Lecture lecture;
    private Student student;

    public DetailsController(
            SQLiteManager sqLiteManager,
            LoginController loginController,
            TableController lecturesTableController) {
        this.detailsView = new DetailsView();
        this.sqLiteManager = sqLiteManager;
        this.loginController = loginController;
        this.tableController = lecturesTableController;

        // Add Sub-Controllers
        this.commentController = new CommentController(sqLiteManager, this);

        // Add Sub-Views
        this.detailsView.setRightPane(commentController.getView().getMainPane());

        // Logic Observers
        lecturesTableController.attach(commentController);

        update();
        addListeners();
    }

    //<editor-fold desc="Actions">
    @Override
    public void update() {
        lecture = tableController.getLecture();
        student = loginController.getLoggedInStudent();
        detailsView.getJoinDropLectureButton().setText(
                tableController.isJoinedSelected() ? "Drop" : "Join");
        updateTextPane();
    }

    private void updateTextPane() {
        if (lecture == null) {
            detailsView.setTextPane("Select a lecture to view its details");
            return;
        }
        try {
            String[][] table = sqLiteManager.executeQuery(queryLectureDetails(lecture.getId()));
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

            detailsView.setTextPane(sb.toString());
        } catch (SQLException e) {
            System.out.println("Error executing query " + e.toString());
        }
    }

    public void addLecture() {
        try {
            sqLiteManager.executeQuery(addLectureQuery(student.getId(), lecture.getId()));
        } catch (SQLException e) {
            System.out.println("Error executing adding lecture: " + e.toString());
        }
    }

    public void deleteLecture() {
        try {
            sqLiteManager.executeQuery(deleteLectureQuery(student.getId(), lecture.getId()));
        } catch (SQLException e) {
            System.out.println("Error executing deleting lecture: " + e.toString());
        }
    }

    public void showMap() {
        String[][] queryResult = new String[0][];
        try {
            queryResult = sqLiteManager.executeQuery(queryLocationDetails(lecture.getId()));
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
        query.addWhere("L.ID = " + lecture.getId());
        query.addWhere("L.ID = A.LECTURE_ID");
        query.addWhere("S.ID = A.STUDENT_ID");

        try {
            String[][] res = sqLiteManager.executeQuery(query);
            // Create new window and fill it with content
            JFrame participantsView = new JFrame("Participants");
            String[] title = {"Participants"};
            JTable participantsTable = new JTable(res, title);
            JScrollPane pane = new JScrollPane(participantsTable);
            JPanel participantsPanel = new JPanel(new BorderLayout());

            participantsPanel.add(pane, BorderLayout.CENTER);
            participantsView.add(participantsPanel);
            participantsView.setSize(300, 500);
            participantsView.setVisible(true);
        } catch (SQLException e) {
            System.out.println("Error in participants query " + e.toString());
        }

    }
    //</editor-fold>

    //<editor-fold desc="Queries">
    private QueryBuilder addLectureQuery(int studentID, int lectureID) {
        QueryBuilder addBuilder = new QueryBuilder(QueryType.INSERT);
        addBuilder.addInsertTab("ATTENDS");
        addBuilder.addInsertCols(new String[]{"STUDENT_ID", "LECTURE_ID"});
        addBuilder.addInsertVals(new String[]{Integer.toString(studentID), Integer.toString(lectureID)});
        return addBuilder;
    }

    private QueryBuilder deleteLectureQuery(int studentID, int lectureID) {
        QueryBuilder deleteBuilder = new QueryBuilder(QueryType.DELETE);
        deleteBuilder.addDeleteTab("ATTENDS");
        deleteBuilder.addDeleteWhere(new String[]{
                "STUDENT_ID = " + studentID,
                "LECTURE_ID = " + lectureID
        });
        return deleteBuilder;
    }

    private QueryBuilder queryLocationDetails(int lectureID) {
        QueryBuilder query = new QueryBuilder(QueryType.SELECT);

        query.addSelect("ROOMNUMBER", "LECTURES");
        query.addSelect("BUILDINGNUMBER", "LECTURES");

        query.addFrom("LECTURES");

        query.addWhere("L.ID = " + lectureID);

        return query;
    }

    private QueryBuilder queryLectureDetails(int lectureId) {
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
    //</editor-fold>

    //<editor-fold desc="Getters & Setters">
    public Lecture getLecture() {
        return lecture;
    }

    public Student getStudent() {
        return student;
    }

    @Override
    public View getView() {
        return detailsView;
    }

    public TableController getTableController() {
        return tableController;
    }
    //</editor-fold>

    //<editor-fold desc="Listeners">
    @Override
    public void addListeners() {
        detailsView.setRowListener(new ButtonListener());
        detailsView.setMapListener(new ButtonListener());
        detailsView.setParticipantsButton(new ButtonListener());
    }

    class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == detailsView.getJoinDropLectureButton()) {
                if (student != null && lecture != null) {
                    if (getTableController().isJoinedSelected()) {
                        deleteLecture();
                    } else {
                        addLecture();
                    }
                }
            } else if (e.getSource() == detailsView.getMapLocationButton()) {
                if (lecture != null) {
                    showMap();
                }
            } else if (e.getSource() == detailsView.getParticipantsButton()) {
                if (lecture != null) {
                    showParticipants();
                }
            } else {
                throw new RuntimeException();
            }
            notifyAllObservers();
        }
    }
    //</editor-fold>
}
