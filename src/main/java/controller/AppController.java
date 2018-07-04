package controller;

import controller.account.AccountController;
import controller.lectures.LectureController;
import controller.login.LoginController;
import controller.students.StudentsController;
import view.AppFrame;
import view.View;
import SQLiteManager.SQLiteManager;

import java.awt.*;

public class AppController extends Controller {

    private AppFrame view;
    private MenuController menuController;
    private LoginController loginController;
    private SQLiteManager sqLiteManager;

    public AppController() {
        view = new AppFrame();
        sqLiteManager = new SQLiteManager();

        // Add Controllers
        loginController = new LoginController(sqLiteManager);
        menuController = new MenuController(sqLiteManager, loginController);
        LectureController lectureController = new LectureController(sqLiteManager, loginController, menuController);
        StudentsController studentsController = new StudentsController(sqLiteManager, loginController);
        AccountController accountController = new AccountController(sqLiteManager,loginController);

        // Add Views
        view.setMenuPane(menuController.getView());
        view.setLogInPane(loginController.getView());
        view.setLecturesPane(lectureController.getView());
        view.setStudentsPane(studentsController.getView());
        view.setAccountPane(accountController.getView());
        view.disableApp();

        // Logic Observers
        menuController.attach(this); // check if user closed the app
        loginController.attach(this); // check if user logged in

    }

    //<editor-fold desc="Getters">
    @Override
    public View getView() {
        return view;
    }
    //</editor-fold>

    //<editor-fold desc="Action Section">
    public void start() {
        view.open();
    }

    public void end() {
        view.close();
    }

    @Override
    public void update() {
        if(!menuController.isAppActive()) {
            end();
        }
        if(loginController.isLoginActive()) {
            view.enableApp();
        } else {
            view.disableApp();
        }
    }
    //</editor-fold>

    //<editor-fold desc="Listeners">
    @Override
    public void addListeners() {
        // does not react to user input
    }
    //</editor-fold>

    //<editor-fold desc="Other">
    public void icon() {
        view.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.png")));
    }
    //</editor-fold>

    //<editor-fold desc="Main Method">
    public static void main(String[] args) {
        AppController app = new AppController();
        app.start();

        // show icon
        app.icon();
    }
    //</editor-fold>

}
