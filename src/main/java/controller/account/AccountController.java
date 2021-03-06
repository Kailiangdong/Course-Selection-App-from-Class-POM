package controller.account;

import SQLiteManager.*;
import controller.Controller;
import controller.login.LoginController;
import university.Student;
import view.View;
import view.account.AccountView;

import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AccountController extends Controller {

    private AccountView accountView;
    private SQLiteManager sqLiteManager;
    private LoginController loginController;
    private Student activeStudent;
    private String[] subjects;
    private List<Student> students;

    public AccountController(SQLiteManager sqLiteManager, LoginController loginController) {
        this.accountView = new AccountView();
        this.sqLiteManager = sqLiteManager;
        this.loginController = loginController;
        this.loginController.attach(this);
        addListeners();
        update();
    }

    //<editor-fold desc="Getter & Setter">
    @Override
    public View getView() {
        return accountView;
    }
    //</editor-fold>

    //<editor-fold desc="Query Builder">
    private QueryBuilder createSubjectsQuery() {
        QueryBuilder queryBuilder = new QueryBuilder(QueryType.SELECT);
        queryBuilder.addSelect("SUBJECT", "CHAIRS");
        queryBuilder.addFrom("CHAIRS");
        queryBuilder.addGroupBy("SUBJECT", "CHAIRS");
        return queryBuilder;
    }
    private QueryBuilder createStudentsQuery() {
        QueryBuilder queryBuilder = new QueryBuilder(QueryType.SELECT);
        queryBuilder.addSelect("ID", "STUDENTS");
        queryBuilder.addSelect("NAME", "STUDENTS");
        queryBuilder.addSelect("PASSWORD", "STUDENTS");
        queryBuilder.addSelect("MAJOR", "STUDENTS");
        queryBuilder.addSelect("MINOR", "STUDENTS");
        queryBuilder.addFrom("STUDENTS");
        return queryBuilder;
    }
    //</editor-fold>

    //<editor-fold desc="Actions">
    @Override
    public void update() {
        activeStudent = createActiveStudent();
        subjects = createSubjects();
        students = createStudents();
        accountView.setUserNameValue(activeStudent.getName());
        accountView.setMajorValue(activeStudent.getMajor());
        accountView.setMinorValue(activeStudent.getMinor());
        accountView.setMajorBoxChoices(subjects);
        accountView.setMinorBoxChoices(subjects);
        accountView.setUserNameValue(activeStudent.getName());
        accountView.setMajorValue(activeStudent.getMajor());
        accountView.setMinorValue(activeStudent.getMinor());

    }
    private String[] createSubjects() {
        try {
            return Arrays
                    .stream(sqLiteManager.executeQuery(createSubjectsQuery()))
                    .map(subjectsArray -> subjectsArray[0])
                    .toArray(String[]::new);
        } catch (SQLException e) {
            System.out.println("Error querying student names: " + e.toString());
            return new String[]{};
        }
    }
    private List<Student> createStudents() {
        try {
            return Arrays
                    .stream(sqLiteManager.executeQuery(createStudentsQuery()))
                    .map(studentArray ->
                            new Student(Integer.parseInt(studentArray[0]),
                                    studentArray[1],
                                    studentArray[2],
                                    studentArray[3],
                                    studentArray[4]))
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            System.out.println("Error querying student names: " + e.toString());
            return new ArrayList<Student>();
        }
    }
    private Student createActiveStudent() {
        return loginController.getLoggedInStudent();
    }
    //</editor-fold>

    //<editor-fold desc="Listener Actions">
    private boolean checkUserName(String userNameInput){
        return userNameInput != null &&
                !userNameInput.equals("") &&
                !students
                        .stream()
                        .map(student -> student.getName())
                        .collect(Collectors.toList())
                        .contains(userNameInput);

    }
    private boolean checkPassword(String passwordInput, String passwordRepeatInput, String oldPasswordInput){
        return passwordInput != null &&
                passwordRepeatInput != null &&
                oldPasswordInput != null &&
                !passwordInput.equals("") &&
                passwordRepeatInput.equals(passwordInput) &&
                activeStudent.getPassword().equals(oldPasswordInput) &&
                !students
                        .stream()
                        .map(student -> student.getPassword())
                        .collect(Collectors.toList())
                        .contains(passwordInput);

    }
    private boolean checkMajor(String majorInput, String minorInput){
        return majorInput !=null &&
                minorInput != null &&
                !majorInput.equals(minorInput);

    }
    private boolean checkMinor(String minorInput, String majorInput){
        return minorInput !=null &&
                majorInput != null &&
                !minorInput.equals(majorInput);

    }
    private void changeUserName(String userNameInput){
        QueryBuilder qb = new QueryBuilder(QueryType.UPDATE);
        qb.addFrom("STUDENTS", "");
        qb.addSetTab("NAME=" + "'" + userNameInput + "'");
        qb.addWhere("ID=" + createActiveStudent().getId());
        System.out.println(qb.toString());
        try {
            sqLiteManager.executeStatement(qb);
            activeStudent.setName(userNameInput);
        } catch(Exception e) {
            System.out.println("Error changing username " + e.toString());
        }
    }
    private void changePassword(String passwordInput){
        QueryBuilder qb = new QueryBuilder(QueryType.UPDATE);
        qb.addFrom("STUDENTS", "");
        qb.addSetTab("PASSWORD=" + "'" + passwordInput + "'");
        qb.addWhere("ID=" + createActiveStudent().getId());
        System.out.println(qb.toString());
        try {
            sqLiteManager.executeStatement(qb);
            activeStudent.setPassword(passwordInput);
        } catch(Exception e) {
            System.out.println("Error changing password " + e.toString());
        }
    }
    private void changeMajor(String majorInput){
        QueryBuilder qb = new QueryBuilder(QueryType.UPDATE);
        qb.addFrom("STUDENTS", "");
        qb.addSetTab("MAJOR=" + "'" + majorInput + "'");
        qb.addWhere("ID=" + createActiveStudent().getId());
        System.out.println(qb.toString());
        try {
            sqLiteManager.executeStatement(qb);
            activeStudent.setMajor(majorInput);
        } catch(Exception e) {
            System.out.println("Error changing major " + e.toString());
        }
    }
    private void changeMinor(String minorInput){
        QueryBuilder qb = new QueryBuilder(QueryType.UPDATE);
        qb.addFrom("STUDENTS", "");
        qb.addSetTab("MINOR=" + "'" + minorInput + "'");
        qb.addWhere("ID=" + createActiveStudent().getId());
        System.out.println(qb.toString());
        try {
            sqLiteManager.executeStatement(qb);
            activeStudent.setMinor(minorInput);
        } catch(Exception e) {
            System.out.println("Error changing minor " + e.toString());
        }
    }
    //</editor-fold>

    //<editor-fold desc="Listeners">
    @Override
    public void addListeners() {
        accountView.addUserNameActionListener(new UserNameListener());
        accountView.addPasswordActionListener(new PasswordListener());
        accountView.addMajorActionListener(new MajorListener());
        accountView.addMinorActionListener(new MinorListener());
    }

    class UserNameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String userNameInput = accountView.getUserNameInput();
            if (checkUserName(userNameInput)) {
                accountView.setUserNameCheck("✓");
                changeUserName(userNameInput);
                update();
            } else {
                accountView.setUserNameCheck("✗");
            }
        }
    }
    class PasswordListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String passwordInput = accountView.getPasswordInput();
            String passwordRepeatInput = accountView.getPasswordRepeatInput();
            String oldPasswordInput = accountView.getOldPasswordInput();
            if (checkPassword(passwordInput,passwordRepeatInput,oldPasswordInput)) {
                accountView.setPasswordCheck("✓");
                changePassword(passwordInput);
                update();
            } else {
                accountView.setPasswordCheck("✗");
            }
        }
    }
    class MajorListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String majorInput = accountView.getMajorInput();
            String minorInput = accountView.getMinorInput();
            if (checkMajor(majorInput,minorInput)) {
                accountView.setMajorCheck("✓");
                changeMajor(majorInput);
                update();
            } else {
                accountView.setMajorCheck("✗");
            }
        }
    }
    class MinorListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String minorInput = accountView.getMinorInput();
            String majorInput = accountView.getMajorInput();
            if (checkMinor(minorInput,majorInput)) {
                accountView.setMinorCheck("✓");
                changeMinor(minorInput);
                update();

            } else {
                accountView.setMinorCheck("✗");
            }
        }
    }
    //</editor-fold>
}
