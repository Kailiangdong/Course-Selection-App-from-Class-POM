package controller.login;

import SQLiteManager.*;
import controller.Controller;
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

    Student loggedInStudent;

    String[] studentNames;
    String[] studentIds;

    public LoginController(SQLiteManager sqLiteManager) {
        this.loginView = new LoginView();
        this.sqLiteManager = new SQLiteManager();

        loginView.setMajorBoxChoices(getSubjects());
        loginView.setMinorBoxChoices(getSubjects());

        loggedInStudent = new Student(3953, "Robert", "robert123","Management", "Computer Science");

        addListeners();
    }

    //<editor-fold desc="Actions">
    @Override
    public void update() {

    }

    private void registerStudent() {
        // TODO: get inputs and create new Student + login
    }

    private void loginStudent() {
        // TODO: login student --> set loggedInStudent & notify (password needs to be added to table first)

        // loginView.showErrorMessage("You entered wrong credentials."); // activate if wrong password / username was entered
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
        if(loginView.isLoginSelected()) {
            loginView.setUserNameState(true);
            return;
        }
        String userName = loginView.getUserNameInput();
        String[] existingUserNames = getStudentNames();
        for(String existingUserName : existingUserNames) {
            if(existingUserName.equals(userName)) {
                loginView.setUserNameState(false);
                return;
            }
        }
        loginView.setUserNameState(true);
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
    private  QueryBuilder insertStudentQuery(Student student) {
        QueryBuilder addBuilder = new QueryBuilder(QueryType.INSERT);
        addBuilder.addInsertTab("STUDENTS");
        addBuilder.addInsertCols(new String[]{"ID","NAME", "PASSWORD","MAJOR", "MINOR"});
        addBuilder.addInsertVals(new String[]{Integer.toString(student.getId()),student.getName(), student.getPassword(), student.getMajor(), student.getMinor()});
        return addBuilder;
    }


    private Student getStudent(String name) {
        QueryBuilder query = new QueryBuilder(QueryType.SELECT);
        query.addSelect("ID", "STUDENTS");
        query.addSelect("NAME", "STUDENTS");
        query.addSelect("PASSWORD", "STUDENTS");
        query.addSelect("MAJOR", "STUDENTS");
        query.addSelect("MINOR", "STUDENTS");
        query.addFrom("STUDENTS");
        query.addWhere("S.NAME = " + name);
        String[][] queryresult = new String[0][];
        try {
            queryresult = sqLiteManager.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Error querying student: " + e.toString());
        }
        String id = queryresult[0][0];
        String password = queryresult[0][2];
        String major = queryresult[0][3];
        String minor = queryresult[0][4];
        return new Student(Integer.parseInt(id), name, password, major, minor);
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
    }

    class ConfirmListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(loginView.isLoginSelected()) {
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
