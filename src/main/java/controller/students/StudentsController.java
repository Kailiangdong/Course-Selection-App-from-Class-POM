package controller.students;

import SQLiteManager.SQLiteManager;
import controller.Controller;
import controller.login.LoginController;
import view.Section;
import view.View;
import view.students.StudentsView;

public class StudentsController extends Controller {

    private StudentsView studentsView;
    private SQLiteManager sqLiteManager;
    private LoginController loginController;

    //private TableViewStudents tableView;

    public StudentsController(SQLiteManager sqLiteManager, LoginController loginController) {
        // Setting up references
        studentsView = new StudentsView();
        this.sqLiteManager = sqLiteManager;
        this.loginController = loginController;

        // Add sub-controllers
        StudentsTableController studentsTableController = new StudentsTableController(sqLiteManager, loginController);
        // TODO: StudentDetailsController studentDetailsController = new StudentDetailsController(sqLiteManager, loginController);

        // Add sub-views
        studentsView.setView(studentsTableController.getView(), Section.Upper);
        // TODO: studentsView.setView(studentsDetailsController.getView(), Section.Lower);

        // User-observer
        loginController.attach(studentsTableController);
        // TODO: loginController.attach(studentsDetailsController);

        // Logic observer
        // TODO: studentsDetailsController.attach(studentsTableController);
        // TODO: studentsTableController.attach(studentsDetailsController);

        addListeners();
    }

    //<editor-fold desc="Actions">
    @Override
    public void update() {

    }
    //</editor-fold>

    //<editor-fold desc="Getters & Setters">
    @Override
    public View getView() {
        return studentsView;
    }
    //</editor-fold>

    //<editor-fold desc="Listeners">
    @Override
    public void addListeners() {

    }
    //</editor-fold>
}
