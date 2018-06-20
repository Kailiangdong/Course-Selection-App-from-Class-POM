package controller.students;

import SQLiteManager.SQLiteManager;
import controller.Controller;
import controller.TableModel;
import controller.lectures.TableController;
import controller.login.LoginController;
import university.Student;
import view.View;
import view.students.TableViewStudents;
import SQLiteManager.*;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Array;
import java.sql.SQLException;
import java.util.Arrays;

public class StudentsTableController extends Controller {

    private SQLiteManager sqLiteManager;
    private LoginController loginController;
    private TableViewStudents tableViewStudents;

    private Student selectedStudent;
    private String[][] leftTableContent;
    private String[][] rightTableContent;

    private Boolean selectedRight;
    private String selectedCol;
    private String sortOrder;
    private String selectedStudentId;

    public StudentsTableController(SQLiteManager sqLiteManager, LoginController loginController) {
        tableViewStudents = new TableViewStudents();
        this.sqLiteManager = sqLiteManager;
        this.loginController = loginController;

        // Default values
        selectedRight = true;
        selectedCol = "ID";
        sortOrder = "ASC";
        selectedStudentId = "";
        update();

        addListeners();
    }

    private void friends() {
        QueryBuilder builder = new QueryBuilder(QueryType.SELECT);
        try {
            String[][] redundantFriends = sqLiteManager.executeQuery(builder.queryFriends("" + loginController.getLoggedInStudent().getId()));
            rightTableContent = new String[redundantFriends.length][redundantFriends[0].length];
            for(int i = 0; i < redundantFriends.length; i++) {
                if(redundantFriends[i][0].equals("" + loginController.getLoggedInStudent().getId())) {
                    int friendID = Integer.parseInt(redundantFriends[i][1]);
                    rightTableContent[i][0] = "" + sqLiteManager.getStudent(friendID).getId();
                    rightTableContent[i][1] = "" + sqLiteManager.getStudent(friendID).getName();
                } else {
                    int friendID = Integer.parseInt(redundantFriends[i][0]);
                    rightTableContent[i][0] = "" + sqLiteManager.getStudent(friendID).getId();
                    rightTableContent[i][1] = "" + sqLiteManager.getStudent(friendID).getName();
                }
            }
            System.out.println(Arrays.deepToString(rightTableContent));
        } catch (SQLException e) {
            System.out.println("Error loading friends");
        }
    }

    private void notFriends() {

    }

    @Override
    public void update() {
        // TODO: Query functions

        friends();

        leftTableContent = new String[][]{new String[]{"7355", "Stefan"}};
        //rightTableContent = new String[][]{new String[]{"971", "Markus"}};

        tableViewStudents.getRightTable().setModel(new TableModel(rightTableContent, new String[]{"ID", "Friends"}));
        tableViewStudents.getLeftTable().setModel(new TableModel(leftTableContent, new String[]{"ID", "Students"}));
    }

    @Override
    public View getView() {
        return tableViewStudents;
    }

    @Override
    public void addListeners() {
        tableViewStudents.setRowListener(new StudentsTableController.RowListener());
        tableViewStudents.setHeaderListener(new StudentsTableController.HeaderListener());
    }

    public Boolean isJoinedSelected() {
        return selectedRight;
    }

    public void setJoinedSelected(Boolean selectedRight) {
        this.selectedRight = selectedRight;
    }

    public Student getSelectedStudent() {
        return selectedStudent;
    }

    public void setSelectedStudentId(String selectedStudentId) {
        this.selectedStudentId = selectedStudentId;
    }

    public void changeOrderForSelectedCol(String selectedCol) {
        this.selectedCol = selectedCol;
        if (this.sortOrder.equals("ASC")) {
            this.sortOrder = "DESC";
        } else {
            this.sortOrder = "ASC";
        }
        update();
    }

    class HeaderListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int colId = tableViewStudents.getLeftTable().columnAtPoint(e.getPoint());
            String selectedCol = tableViewStudents.getLeftTable().getColumnName(colId);
            changeOrderForSelectedCol(selectedCol);
        }
    }

    /**
     * Listens to the TableSelectionModel and determines
     * which row was selected by the user.
     */
    class RowListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            int selectedRow;
            if (e.getSource() == tableViewStudents.getLeftTable().getSelectionModel()) {
                // User selected row in left table
                setJoinedSelected(false);
                tableViewStudents.getRightTable().getSelectionModel().clearSelection();
                selectedRow = tableViewStudents.getLeftTable().getSelectedRow();
                if (selectedRow != -1) {
                    setSelectedStudentId(
                            (String) tableViewStudents.getLeftTable().getValueAt(selectedRow, 0));
                    selectedStudent = sqLiteManager.getStudent(Integer.parseInt(selectedStudentId));
                }
            } else if (e.getSource() == tableViewStudents.getRightTable().getSelectionModel()) {
                setJoinedSelected(true);
                tableViewStudents.getLeftTable().getSelectionModel().clearSelection();
                selectedRow = tableViewStudents.getRightTable().getSelectedRow();
                if (selectedRow != -1) {
                    setSelectedStudentId(
                            (String) tableViewStudents.getRightTable().getValueAt(selectedRow, 0));
                    selectedStudent = sqLiteManager.getStudent(Integer.parseInt(selectedStudentId));
                }
            } else {
                throw new RuntimeException();
            }
            notifyAllObservers();
        }
    }

}
