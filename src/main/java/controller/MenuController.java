package controller;

import controller.login.LoginController;
import view.InputDialog;
import view.MenuView;
import view.View;
import SQLiteManager.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuController extends Controller {


    private MenuView view;
    private SQLiteManager sqLiteManager;
    private LoginController loginController;
    private InputDialog inputDialog;

    private boolean appActive = true;

    private String[] chairNames;
    private List<String> activeChairNames;

    private String[] colNames = new String[]{"TITLE", "CHAIR", "ECTS"};
    private String[] staticColNames = new String[]{"ID"};
    private List<String> activeColNames;

    // Initialization

    public MenuController(SQLiteManager sqLiteManager, LoginController loginController) {
        view = new MenuView();
        this.sqLiteManager = sqLiteManager;
        this.loginController = loginController;

        initColumnMenu();
        initChairMenu();
        addListeners();
    }

    //<editor-fold desc="Actions">
    @Override
    public void update() {
        initChairMenu();
    }

    private void initColumnMenu() {
        activeColNames = new ArrayList<>();
        activeColNames.addAll(Arrays.asList(staticColNames));
        activeColNames.addAll(Arrays.asList(colNames));
        view.setColumnMenu(colNames, staticColNames);
        view.setColumnMenuListener(new ColumnLabelListener());
    }

    private void initChairMenu() {
        chairNames = getChairNames();
        activeChairNames = new ArrayList<>();
        activeChairNames.addAll(Arrays.asList(chairNames));
        view.setChairMenu(chairNames);
        view.setChairMenuListener(new ChairLabelListener());
    }
    //</editor-fold>

    //<editor-fold desc="Getters & Setters">
    public List<String> getActiveChairNames() {
        return activeChairNames;
    }

    public List<String> getActiveColNames() {
        return activeColNames;
    }

    public boolean isAppActive() {
        return appActive;
    }

    @Override
    public View getView() {
        return view;
    }
    //</editor-fold>

    //<editor-fold desc="Queries">
    private String[] getChairNames() {
        QueryBuilder query = new QueryBuilder(QueryType.SELECT);
        query.addSelect("CHAIR", "CHAIRS");
        query.addFrom("CHAIRS");
        query.addFrom("STUDENTS");
        query.addWhere("s.NAME = " + "'" + loginController.getLoggedInStudent().getName() + "'");
        query.addWhere("(c.SUBJECT = s.MINOR or c.SUBJECT = s.MAJOR)");
        query.addGroupBy("CHAIR", "CHAIRS");
        try {
            String[][] resultMatrix = sqLiteManager.executeQuery(query);
            String[] resultVector = new String[resultMatrix.length];
            for (int i = 0; i < resultVector.length; i++) {
                resultVector[i] = resultMatrix[i][0];
            }
            return resultVector;
        } catch (SQLException e) {
            System.out.println("Error querying chair names: " + e.toString());
            return new String[]{};
        }
    }

    //</editor-fold>

    //<editor-fold desc="Listeners">
    @Override
    public void addListeners() {
        view.setQuitButtonListener(new QuitButtonListener());
        view.setRefreshButtonListener(new RefreshButtonListener());
        view.setLogoutButtonListener(new LogoutButtonListener());
        view.setFeedbackButtonListener(new FeedbackButtonListener());
        view.setPushButtonListener(new PushButtonListener());
        view.setPullButtonListener(new PullButtonListener());
    }

    // Empty Listener for Push
    class PushButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SyncController syncController = new SyncController(sqLiteManager, loginController, SyncController.Type.PUSH);
            syncController.setupConnection();
        }
    }

    // Empty Listener for Pull
    class PullButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SyncController syncController = new SyncController(sqLiteManager, loginController, SyncController.Type.PULL);
            syncController.setupConnection();
        }
    }

    class LogoutButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            loginController.logout();
        }
    }

    class FeedbackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            FeedbackController feedbackController = new FeedbackController();
        }
    }

    class QuitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            appActive = false;
            notifyAllObservers();
        }
    }

    class RefreshButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            notifyAllObservers();
        }
    }

    class ChairLabelListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            activeChairNames = view.getChairMenu().getActiveLabels();
            notifyAllObservers();
        }
    }

    class ColumnLabelListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            activeColNames = view.getColumnChoiceMenu().getActiveLabels();
            notifyAllObservers();
        }
    }
    //</editor-fold>
}
