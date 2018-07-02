package controller.students;

import SQLiteManager.SQLiteManager;
import SQLiteManager.QueryBuilder;
import SQLiteManager.QueryType;
import controller.Controller;
import controller.login.LoginController;
import university.Student;
import view.View;
import view.students.DetailsStudents;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Arrays;


public class StudentsDetailsController extends Controller {

    private DetailsStudents detailsView;
    private SQLiteManager sqLiteManager;
    private LoginController loginController;
    private StudentsTableController tableController;
    private RequestsController requestsController;

    private Student student;

    public StudentsDetailsController(SQLiteManager sqLiteManager,
                                     LoginController loginController,
                                     StudentsTableController tableController) {
        this.sqLiteManager = sqLiteManager;
        this.detailsView = new DetailsStudents();
        this.loginController = loginController;
        this.tableController = tableController;

        // Add Sub-Controllers
        this.requestsController = new RequestsController(sqLiteManager, this, loginController);

        // Add Sub-Views
        this.detailsView.setRightPane(requestsController.getView().getMainPane());

        // Logic Observers
        tableController.attach(requestsController);

        update();
        addListeners();
        //detailsView.getAddRemoveFriendButton().setText("Add");
    }

    private void updateTextPane() {
        if(student == null) {
            detailsView.setTextPane("Select a student to view his details");
            return;
        }
        try {
            String[][] table = sqLiteManager.executeQuery(queryStudentDetails(student.getId()));
            StringBuilder sb = new StringBuilder();
            sb.append("ID: ");
            sb.append(table[0][0]);
            sb.append("\n");

            sb.append("Name: ");
            sb.append(table[0][1]);
            sb.append("\n");

            sb.append("Major: ");
            sb.append(table[0][2]);
            sb.append("\n");

            sb.append("Minor: ");
            sb.append(table[0][3]);
            sb.append("\n");
            detailsView.setTextPane(sb.toString());
        } catch (SQLException e) {
            System.out.println("Error executing query " + e.toString());
        }
    }



    //<editor-fold desc="Queries">
    private QueryBuilder queryStudentDetails(int studentID) {
        QueryBuilder query = new QueryBuilder(QueryType.SELECT);

        query.addSelect("ID", "STUDENTS");
        query.addSelect("NAME", "STUDENTS");
        query.addSelect("MAJOR", "STUDENTS");
        query.addSelect("MINOR", "STUDENTS");

        query.addFrom("STUDENTS");

        query.addWhere("S.ID = " + studentID);

        return query;
    }

    private QueryBuilder listAllReceivedRequests(String studentID) {
        QueryBuilder query = new QueryBuilder(QueryType.SELECT);

        query.addSelect("REQUEST_FROM", "REQUESTFRIENDS");
        query.addSelect("TIME", "REQUESTFRIENDS");
        query.addSelect("DATE", "REQUESTFRIENDS");
        query.addFrom("REQUESTFRIENDS");
        query.addOrderBy("DATE", "REQUESTFRIENDS", "DESC");
        query.addOrderBy("TIME", "REQUESTFRIENDS", "DESC");
        query.addWhere("r.REQUEST_TO = " + studentID);

        return query;
    }
    private String[][] queryReceivedRequests() {
        String[][] queryResult = new String[0][];
        try {
            queryResult = sqLiteManager.executeQuery(listAllReceivedRequests(Integer.toString(loginController.getLoggedInStudent().getId())));
        } catch (SQLException e) {
            System.out.println("Error executing list received request: " + e.toString());
        }
        return queryResult;
    }

    private QueryBuilder makeRequestQuery(int studentID) {
        ZonedDateTime timestamp = ZonedDateTime.now();
        String date = DateTimeFormatter.ofPattern("dd-MM-yy").format(timestamp);
        String time = DateTimeFormatter.ofPattern("hh:mm").format(timestamp);
        time = "'" + time + "'";
        date = "'" + date + "'";
        QueryBuilder addBuilder = new QueryBuilder(QueryType.INSERT);
        addBuilder.addInsertTab("REQUESTFRIENDS");
        addBuilder.addInsertCols(new String[]{"REQUEST_TO","REQUEST_FROM","TIME", "DATE"});
        addBuilder.addInsertVals(new String[]{Integer.toString(studentID),"" + loginController.getLoggedInStudent().getId(),time, date});
        return addBuilder;
    }

    private void makeRequest() {
        try {
            sqLiteManager.executeQuery(makeRequestQuery(tableController.getSelectedStudent().getId()));

        } catch (SQLException e) {
            System.out.println("Error executing make request: " + e.toString());
        }
        update();
    }

    private QueryBuilder removeRequestQuery(int studentID) {
        QueryBuilder deleteBuilder = new QueryBuilder(QueryType.DELETE);
        deleteBuilder.addDeleteTab("REQUESTFRIENDS");
        deleteBuilder.addDeleteWhere(new String[]{
                "REQUEST_TO = " + loginController.getLoggedInStudent().getId(),
                "REQUEST_FROM = " + studentID
        });
        return deleteBuilder;
    }

    private void removeRequest() {
        try {
            sqLiteManager.executeQuery(removeRequestQuery(tableController.getSelectedStudent().getId()));

        } catch (SQLException e) {
            System.out.println("Error executing remove request: " + e.toString());
        }
        update();
    }
    private QueryBuilder queryAddFriend(int studentID) {
        QueryBuilder addBuilder = new QueryBuilder(QueryType.INSERT);
        addBuilder.addInsertTab("FRIENDSWITH");
        addBuilder.addInsertCols(new String[]{"STUDENT_ID1", "STUDENT_ID2"});
        addBuilder.addInsertVals(new String[]{"" + loginController.getLoggedInStudent().getId(), Integer.toString(studentID)});
        return addBuilder;
    }

    public void addFriend() {
        try {
            sqLiteManager.executeQuery(queryAddFriend(tableController.getSelectedStudent().getId()));
        } catch (SQLException e) {
            System.out.println("Error executing add student: " + e.toString());
        }
    }

    private QueryBuilder queryDeleteLecture(int studentID) {
        QueryBuilder deleteBuilder = new QueryBuilder(QueryType.DELETE);
        deleteBuilder.addDeleteTab("FRIENDSWITH");
        deleteBuilder.addDeleteWhere(new String[]{
                "STUDENT_ID1 = " + loginController.getLoggedInStudent().getId(),
                "STUDENT_ID2 = " + studentID
        });
        return deleteBuilder;
    }

    public void removeFriend() {
        try {
            sqLiteManager.executeQuery(queryDeleteLecture(tableController.getSelectedStudent().getId()));
        } catch (SQLException e) {
            System.out.println("Error executing remove student: " + e.toString());
        }
    }
    //</editor-fold>

    @Override
    public View getView() {
        return detailsView;
    }

    @Override
    public void update() {
        student = tableController.getSelectedStudent();
        detailsView.getAddRemoveFriendButton().setText(tableController.isJoinedSelected() ? "Remove" : "Add");
        updateTextPane();
    }

    @Override
    public void addListeners() {
        detailsView.setRowListener(new ButtonListener());
    }

    class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == detailsView.getAddRemoveFriendButton()) {
                if (student != null) {
                    if (tableController.isJoinedSelected()) {
                        removeFriend();
                    } else {
                        addFriend();
                    }
                }
            }
            notifyAllObservers();
        }
    }
}
