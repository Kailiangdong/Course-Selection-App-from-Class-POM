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
        this.requestsController = new RequestsController(sqLiteManager, this, loginController, tableController);

        // Add Sub-Views
        this.detailsView.setRightPane(requestsController.getView().getMainPane());

        // Logic Observers
        tableController.attach(requestsController);
        requestsController.attach(tableController);
        loginController.attach(requestsController);

        update();
        addListeners();
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
        detailsView.setRowListener(requestsController.getAddRemoveListener());
    }
}
