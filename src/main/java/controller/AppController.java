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
        menuController = new MenuController(sqLiteManager);
        view.setMenuPane(menuController.getView().getMainPane());
    }

    //<editor-fold desc="Get Section">
    @Override
    public View getView() {
        return view;
    }
    //</editor-fold>

    //<editor-fold desc="Action Section">
    public void start() {
        view.open();
    }

    public void showLectureScreen() {

        // Set up controllers
        LecturesTableController tableController = new LecturesTableController(sqLiteManager, menuController);
        LecturesDetailsController detailsController = new LecturesDetailsController(sqLiteManager, menuController, tableController);

        // Show views in AppFrame
        view.setMiddlePane(tableController.getView().getMainPane());
        view.setBottomPane(detailsController.getView().getMainPane());

        // Set observers
        menuController.attach(this); // app reacts to quit button in menu
        menuController.attach(tableController); // lecture tables react to menu settings
        menuController.attach(detailsController); // lecture details react to changing student name
        tableController.attach(detailsController); // lecture details react to lecture selection
        detailsController.attach(tableController); // lecture tables react to joining/dropping lectures
    }
    //</editor-fold>

    //<editor-fold desc="Rest Section">
    @Override
    void addListeners() {
        // does not react to user input
    }

    @Override
    public void update() {
        if(!menuController.isAppActive()) {
            view.setVisible(false);
            view.dispose();
        }
    }
    //</editor-fold>

    //<editor-fold desc="Main Section">
    public static void main(String[] args) {
        // initialize main frame where the application lives in
        AppController app = new AppController();

        // display lectures table and details for selected lecture
        app.start();
        app.showLectureScreen();
    }
    //</editor-fold>

}
