package controller.students;

import SQLiteManager.SQLiteManager;
import controller.Controller;
import controller.lectures.DetailsController;
import controller.lectures.TableController;
import controller.login.LoginController;
import university.Lecture;
import university.Student;
import view.View;
import view.students.DetailsStudents;

public class StudentsDetailsController extends Controller {

    private DetailsStudents detailsView;
    private SQLiteManager sqLiteManager;
    private LoginController loginController;
    private StudentsTableController tableController;

    private Student student;
    private Lecture lecture;

    public StudentsDetailsController(SQLiteManager sqLiteManager,
                                     LoginController loginController,
                                     StudentsTableController tableController) {
        this.detailsView = new DetailsStudents();
        this.loginController = loginController;
        this.tableController = tableController;

        detailsView.getTextPane().setText("Select a student to view its details");
        //detailsView.getAddRemoveFriendButton().setText("Add");
    }

    @Override
    public void addListeners() {
        //detailsView.setRowListener(new ButtonListener());
    }

    @Override
    public View getView() {
        return detailsView;
    }

    @Override
    public void update() {
        student = loginController.getLoggedInStudent();
        detailsView.getAddRemoveFriendButton().setText(
                tableController.isJoinedSelected() ? "Remove" : "Add");
    }
}
