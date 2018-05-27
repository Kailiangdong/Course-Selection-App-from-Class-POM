package controller;

import view.AppFrame;
import view.View;
import SQLiteManager.SQLiteManager;

public class AppController extends Controller {

    private AppFrame view;

    private Controller menuController;
    private Controller middleController;
    private Controller bottomController;

    private SQLiteManager sqLiteManager;

    public AppController() {
        view = new AppFrame();

        // add menu bar
        menuController = new MenuController(sqLiteManager);
        view.setMenuPane(menuController.getView().getMainPane());
    }

    public void start() {
        view.open();
    }

    public void showLectureScreen() {
        middleController = new LecturesTableController(sqLiteManager, (MenuController) menuController);
        view.setMiddlePane(middleController.getView().getMainPane());

        bottomController = new LectureDetailsController(sqLiteManager, (LecturesTableController) middleController);
        view.setBottomPane(bottomController.getView().getMainPane());

        menuController.attach(middleController); // lecture tables react to menu settings
        middleController.attach(bottomController); // lecture details react to lecture selection
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
        // nothing to update, will never subscribe to anything
    }

    public static void main(String[] args) {
        // initialize main frame where the application lives in
        AppController app = new AppController();

        // display lectures table and details for selected lecture
        app.start();
        app.showLectureScreen();
    }

}
