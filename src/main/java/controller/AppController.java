package controller;

import view.AppFrame;
import view.View;
import SQLiteManager.SQLiteManager;

public class AppController extends Controller {

    private AppFrame view;

    private MenuController menuController;

    private SQLiteManager sqLiteManager;

    public AppController() {
        view = new AppFrame();
        sqLiteManager = new SQLiteManager();

        // add menu bar
        menuController = new MenuController(sqLiteManager);
        view.setMenuPane(menuController.getView().getMainPane());
    }

    public void start() {
        view.open();
    }

    public void showLectureScreen() {

        // Set up controllers
        LecturesTableController tableController = new LecturesTableController(sqLiteManager, menuController);
        LectureDetailsController detailsController = new LectureDetailsController(sqLiteManager, tableController);

        // Show views in AppFrame
        view.setMiddlePane(tableController.getView().getMainPane());
        view.setBottomPane(detailsController.getView().getMainPane());

        // Set observers
        menuController.attach(this); // app reacts to quit button in menu
        menuController.attach(tableController); // lecture tables react to menu settings
        menuController.attach(detailsController); // lecture details react to changing student name
        tableController.attach(detailsController); // lecture details react to lecture selection
    }

    @Override
    void addListeners() {
        // does not react to user input
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public void update() {
        if(!menuController.isAppActive()) {
            view.setVisible(false);
            view.dispose();
        }
    }

    public static void main(String[] args) {
        // initialize main frame where the application lives in
        AppController app = new AppController();

        // display lectures table and details for selected lecture
        app.start();
        app.showLectureScreen();
    }

}
