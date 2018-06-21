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
import java.util.List;

public class LoginController extends Controller {

    private LoginView loginView;
    private SQLiteManager sqLiteManager;

    private List<Student> students;
    private String[] subjects;

    private Student loggedInStudent;
    private Student dummyStudent = new Student(3953, "Robert","Management", "Computer Science" );
    private boolean loginActive;

    public LoginController(SQLiteManager sqLiteManager) {
        this.loginView = new LoginView();
        this.sqLiteManager = new SQLiteManager();
        subjects = getSubjects();


        loginActive = false;
        // other subscribers can't handle null objects
        loggedInStudent = dummyStudent;

        students = sqLiteManager.getStudents();
        subjects = getSubjects();

        loginView.setMinorBoxChoices(subjects);
        loginView.setMajorBoxChoices(subjects);
        addListeners();
    }

    //<editor-fold desc="Actions">
    @Override
    public void update() {
        students = sqLiteManager.getStudents();
        subjects = getSubjects();
    }

    public void logout() {
        update();
        loggedInStudent = dummyStudent;
        loginActive = false;
        notifyAllObservers();
    }

    private void registerStudent() {
        String name = loginView.getUserNameInput();
        String password = loginView.getPasswordInput();

        if(name.equals("") || password.equals("")) {
            loginView.showErrorMessage("You have not filled out all fields. Please try again.");
            return;
        }

        String major = loginView.getMajorInput();
        String minor = loginView.getMinorInput();
        int id = Integer.parseInt(loginView.getStudentIdInput());
        Student student = new Student(id, name, password, major, minor);

        try {
            sqLiteManager.executeStatement(insertStudent(student));
        } catch (SQLException e) {
            System.out.println("Error registering student: " + student.toString());
            return;
        }

        loginActive = true;
        loggedInStudent = student;
        notifyAllObservers();
    }

    private void loginStudent() {
        String userName = loginView.getUserNameInput();
        Student account = null;
        for(Student existingStudent : students) {
            if(existingStudent.getName().equals(userName)) {
                account = existingStudent;
            }
        }
        if(account == null) {
            loginView.showErrorMessage("You've entered a wrong username. Please try again.");
            return;
        }

        String enteredPwd = loginView.getPasswordInput();

        if (!enteredPwd.equals(account.getPassword())) {
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

        if (!result) {
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
        if (loginView.isLoginSelected()) {
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
        boolean existingName = false;
        for(Student existingStudent: students) {
            if (existingStudent.getName().toLowerCase().equals(userName.toLowerCase())) {
                existingName = true;
            }
        }
        if (!loginView.isLoginSelected()) {
            if (existingName) {
                loginView.setUserNameState(false);
            } else {
                loginView.setUserNameState(true);
            }
        }
    }

    private void checkStudentId() {
        if (loginView.isLoginSelected()) {
            loginView.setStudentIdState(true);
            return;
        }

        // Is an ID > 0 given?
        int studentId;
        try {
            studentId = Integer.parseInt(loginView.getStudentIdInput());
            if(studentId <= 0) {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            loginView.setStudentIdState(false);
            return;
        }

        // is studentID already taken?
        for(Student existingStudent : students) {
            if (existingStudent.getId() ==  studentId) {
                loginView.setStudentIdState(false);
                return;
            }
        }

        loginView.setStudentIdState(true);
    }

    private void checkSubject() {
        if (loginView.isLoginSelected()) {
            loginView.setSubjectState(true);
            return;
        }
        String major = loginView.getMajorInput();
        String minor = loginView.getMinorInput();
        if (major.equals(minor)) {
            loginView.setSubjectState(false);
        } else {
            loginView.setSubjectState(true);
        }
    }
    //</editor-fold>

    //<editor-fold desc="Queries">
    private QueryBuilder insertStudent(Student student) {
        QueryBuilder addBuilder = new QueryBuilder(QueryType.INSERT);
        addBuilder.addInsertTab("STUDENTS");
        addBuilder.addInsertCols(new String[]{"ID", "NAME", "PASSWORD", "MAJOR", "MINOR"});
        String id = "'" + student.getId() + "'";
        String name = "'" + student.getName() + "'";
        String pwd = "'" + student.getPassword() + "'";
        String major = "'" + student.getMajor() + "'";
        String minor = "'" + student.getMinor() + "'";
        addBuilder.addInsertVals(new String[]{id, name, pwd, major, minor});
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

//    private String[] getStudentNames() {
//        QueryBuilder query = new QueryBuilder(QueryType.SELECT);
//        query.addSelect("NAME", "STUDENTS");
//        query.addFrom("STUDENTS");
//        query.addGroupBy("NAME", "STUDENTS");
//        try {
//            String[][] resultMatrix = sqLiteManager.executeQuery(query);
//            String[] resultVector = new String[resultMatrix.length];
//            for (int i = 0; i < resultVector.length; i++) {
//                resultVector[i] = resultMatrix[i][0];
//            }
//            return resultVector;
//        } catch (SQLException e) {
//            System.out.println("Error querying student names: " + e.toString());
//            return new String[]{};
//        }
//    }
//
//    private String[] getStudentIds() {
//        QueryBuilder query = new QueryBuilder(QueryType.SELECT);
//        query.addSelect("ID", "STUDENTS");
//        query.addFrom("STUDENTS");
//        query.addGroupBy("ID", "STUDENTS");
//        try {
//            String[][] resultMatrix = sqLiteManager.executeQuery(query);
//            String[] resultVector = new String[resultMatrix.length];
//            for (int i = 0; i < resultVector.length; i++) {
//                resultVector[i] = resultMatrix[i][0];
//            }
//            return resultVector;
//        } catch (SQLException e) {
//            System.out.println("Error querying student ids: " + e.toString());
//            return new String[]{};
//        }
//    }
    //</editor-fold>

    //<editor-fold desc="Getters & Setters">
    public boolean isLoginActive() {
        return loginActive;
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
            if (!loginView.isLoginSelected()) {
                registerStudent();
            } else {
                loginStudent();
            }
            loginView.clearInputs();
        }
    }

    class SelectionActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (loginView.isLoginSelected()) {
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
