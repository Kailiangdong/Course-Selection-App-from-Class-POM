package controller.lectures;

import SQLiteManager.SQLiteManager;
import controller.AppController;
import controller.Controller;
import controller.MenuController;
import controller.login.LoginController;
import view.Section;
import view.View;
import view.lectures.LectureView;

public class LectureController extends Controller {

    private LectureView lectureView;

    private SQLiteManager sqLiteManager;
    private LoginController loginController;
    private MenuController menuController;

    public LectureController(SQLiteManager sqLiteManager, LoginController loginController, MenuController menuController) {
        lectureView = new LectureView();
        this.sqLiteManager = sqLiteManager;
        this.loginController = loginController;
        this.menuController = menuController;

        // Add Sub-Controller
        TableController tableController = new TableController(sqLiteManager, loginController, menuController);
        DetailsController detailsController = new DetailsController(sqLiteManager, loginController, tableController);

        // Add Sub-Views
        lectureView.setView(tableController.getView(), Section.Upper);
        lectureView.setView(detailsController.getView(), Section.Lower);

        // User Observer
        loginController.attach(tableController);
        loginController.attach(detailsController);

        // Logic Observer
        menuController.attach(tableController); // get filters for chairs / columns
        tableController.attach(detailsController); // get selected lecture
        detailsController.attach(tableController); // update when lectures are joined or dropped

        addListeners();
    }

    //<editor-fold desc="Action">
    @Override
    public void update() {

    }
    //</editor-fold>

    //<editor-fold desc="Getters & Setters">
    @Override
    public View getView() {
        return lectureView;
    }
    //</editor-fold>

    //<editor-fold desc="Listeners">
    @Override
    public void addListeners() {

    }
    //</editor-fold>

}
