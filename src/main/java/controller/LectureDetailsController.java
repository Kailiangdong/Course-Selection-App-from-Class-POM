package main.java.controller;

import main.java.SQLiteManager.QueryBuilder;
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

    public QueryBuilder addLectureQuery(int studentID, int lectureID) {
        QueryBuilder addBuilder = new QueryBuilder();
        addBuilder.addSelect(false, "STUDENT_ID", "");
        addBuilder.addSelect(false, "LECTURE_ID", "");
        addBuilder.addFrom(false, "ATTENDS");
        addBuilder.addWhere(Integer.toString(studentID));
        addBuilder.addWhere(Integer.toString(lectureID));
        return addBuilder;
    }

    public QueryBuilder deleteLectureQuery(int studentID, int lectureID) {
        QueryBuilder deleteBuilder = new QueryBuilder();
        deleteBuilder.addFrom(false, "ATTENDS");
        deleteBuilder.addWhere("STUDENT_ID=" + Integer.toString(studentID));
        deleteBuilder.addWhere("LECTURE_ID=" + Integer.toString(lectureID));
        return deleteBuilder;
    }

}
