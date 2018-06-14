package controller.students;

import SQLiteManager.SQLiteManager;
import controller.Controller;
import controller.login.LoginController;
import view.View;
import view.students.StudentsView;

public class StudentsController extends Controller {

    StudentsView studentsView;
    SQLiteManager sqLiteManager;
    LoginController loginController;

    public StudentsController(SQLiteManager sqLiteManager, LoginController loginController) {
        this.studentsView = new StudentsView();
        this.sqLiteManager = sqLiteManager;
        this.loginController = loginController;

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
