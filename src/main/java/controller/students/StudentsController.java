package controller.students;

import SQLiteManager.SQLiteManager;
import controller.Controller;
import controller.TableModel;
import controller.login.LoginController;
import view.View;
import view.lectures.TableView;
import view.students.StudentsView;
import view.students.TableViewStudents;

public class StudentsController extends Controller {

    private StudentsView studentsView;
    private SQLiteManager sqLiteManager;
    private LoginController loginController;
    private TableViewStudents tableView;

    private String[] colName;
    private String[][] leftTableContent;
    private String[][] rightTableContent;

    public StudentsController(SQLiteManager sqLiteManager, LoginController loginController) {
        this.studentsView = new StudentsView();
        this.tableView = new TableViewStudents();
        this.sqLiteManager = sqLiteManager;
        this.loginController = loginController;

        addListeners();
    }

    //<editor-fold desc="Actions">
    @Override
    public void update() {
        colName = new String[]{"Friends"};
        leftTableContent = new String[] []{{"Stefan"}};
        tableView.getLeftStudentsTable().setModel(new TableModel(leftTableContent, new String[]{"Firends"}));
        rightTableContent = new  String[] []{{"Joana"}};
        tableView.getRightStudentsTable().setModel(new TableModel(leftTableContent, new String[]{"Firends"}));
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
