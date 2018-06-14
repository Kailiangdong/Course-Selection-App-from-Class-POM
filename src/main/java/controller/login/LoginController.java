package controller.login;

import SQLiteManager.SQLiteManager;
import controller.Controller;
import university.Student;
import view.View;
import view.login.LoginView;

public class LoginController extends Controller {

    LoginView loginView;
    SQLiteManager sqLiteManager;

    Student loggedInStudent;

    public LoginController(SQLiteManager sqLiteManager) {
        this.loginView = new LoginView();
        this.sqLiteManager = new SQLiteManager();

        loggedInStudent = new Student(3953, "Robert", "Management", "Computer Science");

        addListeners();
    }

    //<editor-fold desc="Actions">
    @Override
    public void update() {

    }
    //</editor-fold>

    //<editor-fold desc="Getters & Setters">
    public Student getLoggedInStudent() {
        return loggedInStudent;
    }

    @Override
    public View getView() {
        return loginView;
    }
    //</editor-fold>

    //<editor-fold desc="Listeners">
    @Override
    public void addListeners() {

    }
    //</editor-fold>
}
