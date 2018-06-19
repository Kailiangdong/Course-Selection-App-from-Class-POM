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



    public StudentsController(SQLiteManager sqLiteManager, LoginController loginController) {
        // Setting up references
        studentsView = new StudentsView();
        this.sqLiteManager = sqLiteManager;
        this.loginController = loginController;

        // Add sub-controllers
        StudentsTableController studentsTableController = new StudentsTableController(sqLiteManager, loginController);
        StudentsDetailsController studentsDetailsController = new StudentsDetailsController(sqLiteManager, loginController, studentsTableController);
        // Add sub-views
        studentsView.setView(studentsTableController.getView(), Section.Upper);
        studentsView.setView(studentsDetailsController.getView(), Section.Lower);

        // User-observer
        loginController.attach(studentsTableController);
        loginController.attach(studentsDetailsController);

        // Logic observer
        studentsDetailsController.attach(studentsTableController);
        studentsTableController.attach(studentsDetailsController);

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
