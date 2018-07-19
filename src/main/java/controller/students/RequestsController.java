package controller.students;

import controller.Controller;
import controller.login.LoginController;
import university.FriendRequest;
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
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
    //</editor-fold desc="Getters">

    //<editor-fold desc="Setters">
    public void setSelectedRequest(FriendRequest selectedRequest) {
        this.selectedRequest = selectedRequest;
    }
    //</editor-fold desc="Setters">

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

    private QueryBuilder createFriendRequestQuery(int studentID) {
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

    private boolean getIfFriendRequestExists(int studentID) {
        QueryBuilder query = new QueryBuilder(QueryType.SELECT);
        query.addSelect("REQUEST_TO", "REQUESTS");
        query.addFrom("REQUESTFRIENDS");
        query.addWhere("R.REQUEST_TO = " + studentID);
        query.addWhere("R.REQUEST_FROM = " + loginController.getLoggedInStudent().getId());
        String[][] likeMatrix = null;
        try {
            likeMatrix = sqLiteManager.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Error querying existing friend request: " + e.toString());
        }
        if(likeMatrix == null || likeMatrix.length==0) {
            return false;
        }
        else {
            return true;
        }
    }

    private void addFriend(int studentID) {
        QueryBuilder addBuilderLeft = new QueryBuilder(QueryType.INSERT);
        addBuilderLeft.addInsertTab("FRIENDSWITH");
        addBuilderLeft.addInsertCols(new String[]{"STUDENT_ID1", "STUDENT_ID2"});
        addBuilderLeft.addInsertVals(new String[]{"" + loginController.getLoggedInStudent().getId(), Integer.toString(studentID)});

        QueryBuilder addBuilderRight = new QueryBuilder(QueryType.INSERT);
        addBuilderRight.addInsertTab("FRIENDSWITH");
        addBuilderRight.addInsertCols(new String[]{"STUDENT_ID1", "STUDENT_ID2"});
        addBuilderRight.addInsertVals(new String[]{Integer.toString(studentID), "" + loginController.getLoggedInStudent().getId()});
        try {
            sqLiteManager.executeStatement(addBuilderLeft);
            sqLiteManager.executeStatement(addBuilderRight);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void makeRequest() {
        try {
            sqLiteManager.executeQuery(createFriendRequestQuery(tableController.getSelectedStudent().getId()));

        } catch (SQLException e) {
            System.out.println("Error executing make request: " + e.toString());
        }
        update();
    }

    private QueryBuilder removeRequestQuery(int toStudentID, int fromStudentID) {
        QueryBuilder deleteBuilder = new QueryBuilder(QueryType.DELETE);
        deleteBuilder.addDeleteTab("REQUESTFRIENDS");
        deleteBuilder.addDeleteWhere(new String[]{
                "REQUEST_TO = " + toStudentID,
                "REQUEST_FROM = " + fromStudentID
        });
        return deleteBuilder;
    }

    private void queryRequests() {
        String[][] queryResult = new String[0][];
        requests = new ArrayList<>();
        try {
            queryResult = sqLiteManager.executeQuery(listAllReceivedRequests("" + loginController.getLoggedInStudent().getId()));
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

    private QueryBuilder queryDeleteFriend(int studentID) {
        QueryBuilder deleteBuilder = new QueryBuilder(QueryType.DELETE);
        deleteBuilder.addDeleteTab("FRIENDSWITH");
        deleteBuilder.addDeleteWhere(new String[]{
                "STUDENT_ID1 = " + loginController.getLoggedInStudent().getId(),
                "STUDENT_ID2 = " + studentID
                + " OR STUDENT_ID1 = " + studentID,
                "STUDENT_ID2 = " + loginController.getLoggedInStudent().getId()
        });
        return deleteBuilder;
    }

    public void removeFriend() {
        try {
            sqLiteManager.executeQuery(queryDeleteFriend(tableController.getSelectedStudent().getId()));
            //sqLiteManager.executeQuery(queryDeleteFriend(loginController.getLoggedInStudent().getId()));
        } catch (SQLException e) {
            System.out.println("Error executing remove student: " + e.toString());
        }
    }

    @Override
    public View getView() {
        return view;
    }

    public ActionListener getAddRemoveListener() {
        return new AddRemoveListener();
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
        view.setAcceptListener(new AcceptListener());
        view.setDeletionListener(new RejectListener());
    }


    class AddRemoveListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (tableController.getSelectedRight()) {
                    removeFriend();
                } else {
                    if(getIfFriendRequestExists(tableController.getSelectedStudent().getId())) {
                        sqLiteManager.executeStatement(removeRequestQuery(tableController.getSelectedStudent().getId(),
                                loginController.getLoggedInStudent().getId()));
                    }
                    sqLiteManager.executeStatement(createFriendRequestQuery(tableController.getSelectedStudent().getId()));
                }
            } catch (SQLException ex) {
                System.out.println("Error executing like query " + ex.toString());
            }
            RequestsController.this.update();
            RequestsController.this.notifyAllObservers();
        }
    }

    class AcceptListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                addFriend(selectedRequest.getStudent2().getId());
                sqLiteManager.executeStatement(removeRequestQuery(selectedRequest.getStudent1().getId(), selectedRequest.getStudent2().getId()));
            } catch (SQLException e1) {
                System.out.println("Error adding friend: " + e.toString());
            }
            RequestsController.this.update();
            RequestsController.this.notifyAllObservers();
        }
    }

    class RejectListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                sqLiteManager.executeStatement(removeRequestQuery(selectedRequest.getStudent1().getId(), selectedRequest.getStudent2().getId()));
            } catch (SQLException e1) {
                System.out.println("Error removing friend request: " + e.toString());
            }
            RequestsController.this.update();
            RequestsController.this.notifyAllObservers();
        }
    }
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