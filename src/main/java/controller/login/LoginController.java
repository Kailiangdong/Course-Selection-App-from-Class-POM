package controller.login;

import SQLiteManager.*;
import controller.Controller;
import controller.MenuController;
import university.Student;
import view.View;
import view.login.LoginView;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginController extends Controller {

    LoginView loginView;
    SQLiteManager sqLiteManager;
    MenuController menuController;

    Student loggedInStudent;
    boolean loginActive;

    public LoginController(SQLiteManager sqLiteManager) {
        this.loginView = new LoginView();
        this.sqLiteManager = new SQLiteManager();

        loginView.setMajorBoxChoices(getSubjects());
        loginView.setMinorBoxChoices(getSubjects());

        loginActive = false;
        // other subscribers can't handle null objects
        loggedInStudent = new Student(9999, "Dummy", "Management", "Computer Science");

        addListeners();
    }

    //<editor-fold desc="Actions">
    @Override
    public void update() {
        if(!menuController.isLoginActive()) {
            loginActive = false;
            notifyAllObservers();
        }
    }

    private void registerStudent() {
        // TODO: get inputs and create new Student + login
    }

    private void loginStudent() {
        String userName = loginView.getUserNameInput();
        String enteredPwd = loginView.getPasswordInput();
        String userPwd = "";

        if(!enteredPwd.equals(userPwd)) {
            loginView.showErrorMessage("You've entered a wrong password. Please try again.");
            return;
        }

        loginActive = true;
        loggedInStudent = sqLiteManager.getStudent(userName);
        notifyAllObservers();
    }

    private void checkAll() {
        String regex = "[\\w?\\s?]*|";
        boolean result = loginView.getUserNameInput().matches(regex);
        result = result && loginView.getPasswordInput().matches(regex);
        result = result && loginView.getConfPasswordInput().matches(regex);
        result = result && loginView.getStudentIdInput().matches(regex);

        if(!result) {
            loginView.showErrorMessage("Please use only use characters matching" + regex + ".");
        }
    }

    private void checkInput() {
        checkAll();
        checkPassword();
        checkUserName();
        checkStudentId();
        checkSubject();
    }

    private void checkPassword() {
        if(loginView.isLoginSelected()) {
            loginView.setPasswordState(true);
            return;
        }
        String pwd = loginView.getPasswordInput();
        String confPwd = loginView.getConfPasswordInput();
        if (pwd.equals(confPwd)) {
            loginView.setPasswordState(true);
        } else {
            loginView.setPasswordState(false);
        }
    }

    private void checkUserName() {
        String userName = loginView.getUserNameInput();
        String[] existingUserNames = getStudentNames();
        boolean existingName = false;
        for(String existingUserName : existingUserNames) {
            if(existingUserName.equals(userName)) {
                existingName = true;
            }
        }
        if(loginView.isLoginSelected()) {
            if(existingName) {
                loginView.setUserNameState(true);
            } else {
                loginView.setUserNameState(false);
            }
        } else {
            if(existingName) {
                loginView.setUserNameState(false);
            } else {
                loginView.setUserNameState(true);
            }
        }
    }

    private void checkStudentId() {
        if(loginView.isLoginSelected()) {
            loginView.setStudentIdState(true);
            return;
        }
        String studentId = loginView.getStudentIdInput();
        try {
            Integer.parseInt(studentId);
        } catch (NumberFormatException e) {
            loginView.setStudentIdState(false);
            return;
        }
        String[] currStudentIds = getStudentIds();
        for(String currStudentId : currStudentIds) {
            if(currStudentId.equals(studentId)) {
                loginView.setStudentIdState(false);
                return;
            }
        }
        loginView.setStudentIdState(true);
    }

    private void checkSubject() {
        if(loginView.isLoginSelected()) {
            loginView.setSubjectState(true);
            return;
        }
        String major = loginView.getMajorInput();
        String minor = loginView.getMinorInput();
        if(major.equals(minor)) {
            loginView.setSubjectState(false);
        } else {
            loginView.setSubjectState(true);
        }
    }
    //</editor-fold>

    //<editor-fold desc="Queries">
    private void insertStudent(Student student) {
        // TODO: add new student - include password either in student object or as separate string ?!
    }

    private Student getStudent(String name) {
        // TODO: query student by name
        return null;
    }

    private String[] getSubjects() {
        QueryBuilder query = new QueryBuilder(QueryType.SELECT);
        query.addSelect("SUBJECT", "CHAIRS");
        query.addFrom("CHAIRS");
        query.addGroupBy("SUBJECT", "CHAIRS");
        try {
            String[][] resultMatrix = sqLiteManager.executeQuery(query);
            String[] resultVector = new String[resultMatrix.length];
            for (int i = 0; i < resultVector.length; i++) {
                resultVector[i] = resultMatrix[i][0];
            }
            return resultVector;
        } catch (SQLException e) {
            System.out.println("Error querying student names: " + e.toString());
            return new String[]{};
        }
    }

    private String[] getStudentNames() {
        QueryBuilder query = new QueryBuilder(QueryType.SELECT);
        query.addSelect("NAME", "STUDENTS");
        query.addFrom("STUDENTS");
        query.addGroupBy("NAME", "STUDENTS");
        try {
            String[][] resultMatrix = sqLiteManager.executeQuery(query);
            String[] resultVector = new String[resultMatrix.length];
            for (int i = 0; i < resultVector.length; i++) {
                resultVector[i] = resultMatrix[i][0];
            }
            return resultVector;
        } catch (SQLException e) {
            System.out.println("Error querying student names: " + e.toString());
            return new String[]{};
        }
    }

    private String[] getStudentIds() {
        QueryBuilder query = new QueryBuilder(QueryType.SELECT);
        query.addSelect("ID", "STUDENTS");
        query.addFrom("STUDENTS");
        query.addGroupBy("ID", "STUDENTS");
        try {
            String[][] resultMatrix = sqLiteManager.executeQuery(query);
            String[] resultVector = new String[resultMatrix.length];
            for (int i = 0; i < resultVector.length; i++) {
                resultVector[i] = resultMatrix[i][0];
            }
            return resultVector;
        } catch (SQLException e) {
            System.out.println("Error querying student ids: " + e.toString());
            return new String[]{};
        }
    }
    //</editor-fold>

    //<editor-fold desc="Getters & Setters">
    public boolean isLoginActive() {
        return loginActive;
    }

    public void setMenuController(MenuController menuController) {
        this.menuController = menuController;
    }

    public Student getLoggedInStudent() {
        return loggedInStudent;
    }

    @Override
    public View getView() {
        return loginView;
    }
    //</editor-fold>

    //<editor-fold desc="Listeners">
    @Override
    public void addListeners() {
        loginView.addUserDocumentListener(new UserDocumentListener());
        loginView.addSelectionActionListener(new SelectionActionListener());
        loginView.addConfirmActionListener(new ConfirmListener());
    }

    class ConfirmListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(!loginView.isLoginSelected()) {
                registerStudent();
            } else {
                loginStudent();
            }
        }
    }

    class SelectionActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(loginView.isLoginSelected()) {
                loginView.hideRegisterPane();
            } else {
                loginView.showRegisterPane();
            }
            checkInput();
        }
    }

    class UserDocumentListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            checkInput();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            checkInput();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            checkInput();
        }
    }
    //</editor-fold>
}
