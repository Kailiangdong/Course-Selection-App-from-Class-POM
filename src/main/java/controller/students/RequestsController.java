package controller.students;

import controller.Controller;
import controller.lectures.DetailsController;
import controller.login.LoginController;
import university.FriendRequest;
import university.Lecture;
import university.LectureComment;
import university.Student;
import view.View;
import SQLiteManager.*;
import view.students.FriendRequestsView;

import java.util.ArrayList;

public class RequestsController extends Controller {

    FriendRequestsView view;
    private SQLiteManager sqLiteManager;
    private StudentsDetailsController detailsController;
    private LoginController loginController;

    private Student student;
    private Lecture lecture;

    private ArrayList<FriendRequest> requests = new ArrayList<>();
    private FriendRequest selectedRequest;

    public RequestsController(SQLiteManager sqLiteManager, StudentsDetailsController detailsController, LoginController loginController) {
        this.view = new FriendRequestsView();
        this.detailsController = detailsController;
        this.sqLiteManager = sqLiteManager;
        this.loginController = loginController;

        addListeners();
        update();
    }

    @Override
    public void addListeners() {

    }
    @Override
    public View getView() {
        return view;
    }

    @Override
    public void update() {


        requests.add(new FriendRequest(sqLiteManager.getStudent(3953), loginController.getLoggedInStudent(), "6-1-2018", "12:30"));
        view.setList(requests.toArray(new FriendRequest[]{}));


    }
}
