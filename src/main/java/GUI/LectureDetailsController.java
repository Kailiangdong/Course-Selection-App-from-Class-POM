package main.java.GUI;

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
