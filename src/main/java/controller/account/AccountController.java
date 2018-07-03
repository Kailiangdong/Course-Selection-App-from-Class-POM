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
    private Student activeStudent;
    private String[] subjects;
    private List<Student> students;

    public AccountController(SQLiteManager sqLiteManager) {
        this.accountView = new AccountView();
        this.sqLiteManager = new SQLiteManager();
        addListeners();
        update();
    }

    //<editor-fold desc="Getter & Setter">
    @Override
    public View getView() {
        return accountView;
    }
    //</editor-fold>

    //<editor-fold desc="Querie Builder">
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
            List<Student> output = new ArrayList<Student>();
            return output;
        }
    }
    private Student createActiveStudent() {


    }
    //</editor-fold>

    //<editor-fold desc="Listener Actions">
    private boolean checkUserName(String userNameInput){
        return userNameInput != null &&
                !userNameInput.equals("") &&
                students
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
                students
                        .stream()
                        .map(student -> student.getName())
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

    }
    private void changePassword(String passwordInput){

    }
    private void changeMajor(String majorInput){

    }
    private void changeMinor(String minorInput){

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
