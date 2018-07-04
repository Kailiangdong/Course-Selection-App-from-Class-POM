package controller.students;

import controller.Controller;
import controller.lectures.CommentController;
import controller.lectures.DetailsController;
import controller.login.LoginController;
import university.FriendRequest;
import university.Lecture;
import university.LectureComment;
import university.Student;
import view.View;
import SQLiteManager.*;
import view.students.FriendRequestsView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class RequestsController extends Controller {

    FriendRequestsView view;
    private SQLiteManager sqLiteManager;
    private StudentsDetailsController detailsController;
    private LoginController loginController;
    private FriendRequest selectedRequest;
    private StudentsTableController tableController;

    private ArrayList<FriendRequest> requests = new ArrayList<>();


    public RequestsController(SQLiteManager sqLiteManager, StudentsDetailsController detailsController, LoginController loginController,
                              StudentsTableController tableController) {
        this.view = new FriendRequestsView();
        this.detailsController = detailsController;
        this.sqLiteManager = sqLiteManager;
        this.loginController = loginController;
        this.tableController = tableController;

        attach(loginController);
        //requests.add(new FriendRequest(sqLiteManager.getStudent(3953), loginController.getLoggedInStudent(), "12-06-2018", "12:30"));
        addListeners();
        update();
    }

    //<editor-fold desc="Getters">
    public FriendRequest getSelectedRequest() {
        return selectedRequest;
    }

    //<editor-fold desc="Setters">
    public void setSelectedRequest(FriendRequest selectedRequest) {
        this.selectedRequest = selectedRequest;
    }

    private QueryBuilder listAllReceivedRequests(String studentID) {
        QueryBuilder query = new QueryBuilder(QueryType.SELECT);
        query.addSelect("REQUEST_TO", "REQUESTFRIENDS");
        query.addSelect("REQUEST_FROM", "REQUESTFRIENDS");
        query.addSelect("TIME", "REQUESTFRIENDS");
        query.addSelect("DATE", "REQUESTFRIENDS");
        query.addFrom("REQUESTFRIENDS");
        query.addOrderBy("DATE", "REQUESTFRIENDS", "DESC");
        query.addOrderBy("TIME", "REQUESTFRIENDS", "DESC");
        query.addWhere("r.REQUEST_TO = " + studentID);

        return query;
    }

    private QueryBuilder makeRequestQuery(int studentID) {
        ZonedDateTime timestamp = ZonedDateTime.now();
        String date = DateTimeFormatter.ofPattern("dd-MM-yy").format(timestamp);
        String time = DateTimeFormatter.ofPattern("hh:mm").format(timestamp);
        time = "'" + time + "'";
        date = "'" + date + "'";
        QueryBuilder addBuilder = new QueryBuilder(QueryType.INSERT);
        addBuilder.addInsertTab("REQUESTFRIENDS");
        addBuilder.addInsertCols(new String[]{"REQUEST_TO","REQUEST_FROM","TIME", "DATE"});
        addBuilder.addInsertVals(new String[]{Integer.toString(studentID),"" + loginController.getLoggedInStudent().getId(),time, date});
        return addBuilder;
    }

    private void makeRequest() {
        try {
            sqLiteManager.executeQuery(makeRequestQuery(tableController.getSelectedStudent().getId()));

        } catch (SQLException e) {
            System.out.println("Error executing make request: " + e.toString());
        }
        update();
    }

    private String[][] queryReceivedRequests() {
        String[][] queryResult = new String[0][];
        try {
            queryResult = sqLiteManager.executeQuery(listAllReceivedRequests(Integer.toString(loginController.getLoggedInStudent().getId())));
        } catch (SQLException e) {
            System.out.println("Error executing list received request: " + e.toString());
        }
        return queryResult;
    }

    private QueryBuilder removeRequestQuery(int studentID) {
        QueryBuilder deleteBuilder = new QueryBuilder(QueryType.DELETE);
        deleteBuilder.addDeleteTab("REQUESTFRIENDS");
        deleteBuilder.addDeleteWhere(new String[]{
                "REQUEST_TO = " + loginController.getLoggedInStudent().getId(),
                "REQUEST_FROM = " + studentID
        });
        return deleteBuilder;
    }

    private void removeRequest() {
        try {
            sqLiteManager.executeQuery(removeRequestQuery(tableController.getSelectedStudent().getId()));

        } catch (SQLException e) {
            System.out.println("Error executing remove request: " + e.toString());
        }
        update();
    }

    private void queryRequests() {
        String[][] queryResult = new String[0][];
        requests = new ArrayList<>();
        try {
            queryResult = sqLiteManager.executeQuery(listAllReceivedRequests("" + loginController.getLoggedInStudent().getId()));
            System.out.println("" + loginController.getLoggedInStudent().getName());
        } catch (SQLException e) {
            System.out.println("Error executing show comment: " + e.toString());
        }
        for (int i = 0; i < queryResult.length; i++) {
            Student requestTo = sqLiteManager.getStudent(Integer.parseInt(queryResult[i][0]));
            Student requestFrom = sqLiteManager.getStudent(Integer.parseInt(queryResult[i][1]));
            String time = queryResult[i][2];
            String date = queryResult[i][3];

            FriendRequest request = new FriendRequest(requestTo, requestFrom, date, time);
            requests.add(request);
        }
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public void update() {
        queryRequests();
        view.setList(requests.toArray(new FriendRequest[]{}));
    }

    //<editor-fold desc="Listeners">
    @Override
    public void addListeners() {
        view.setSelectionListener(new SelectListener());
        //view.setAddListener(new AddListner());
    }



/*class AddListener implements ActionListener {
   @Override
   public void actionPerformed(ActionEvent e) {
       try {
           /*if (tableController.getSelectedRight()) {
               return;
           } else {

           }
       } catch (SQLException ex) {
           System.out.println("Error executing like query " + ex.toString());
       }
       CommentController.this.update();
   }*/
        class SelectListener implements ListSelectionListener {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                int index = lsm.getMinSelectionIndex();
                if (index < 0) {
                    return;
                }

                setSelectedRequest((FriendRequest) view.getObject(index));

                RequestsController.this.update();
            }
        }
    //</editor-fold>

}