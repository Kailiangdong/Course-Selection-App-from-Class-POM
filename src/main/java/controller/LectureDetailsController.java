package main.java.controller;

import main.java.view.LectureDetailsView;
import main.java.view.View;
import main.java.SQLiteManager.SQLiteManager;

public class LectureDetailsController extends Controller {

    private LectureDetailsView view;
    private LecturesTableController tableController;

    private SQLiteManager sqLiteManager;

    public LectureDetailsController(SQLiteManager sqLiteManager, LecturesTableController tableController) {
        this.view = new LectureDetailsView();
        this.sqLiteManager = sqLiteManager;
        this.tableController = tableController;
    }
    @Override
    void addListeners() {

    }

    @Override
    public void update() {
        // get updated state
        tableController.getSelectedLectureID();
    }

    @Override
    public View getView() {
        return view;
    }

}
