package controller.lectures;

import controller.Controller;
import controller.MenuController;
import controller.TableModel;
import controller.login.LoginController;
import university.Lecture;
import view.*;
import SQLiteManager.*;
import view.lectures.TableView;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

public class TableController extends Controller {

    private TableView lecturesTableView;
    private SQLiteManager sqLiteManager;
    private LoginController loginController;
    private MenuController menuController;

    private Lecture selectedLecture;

    private String[] colNames;
    private String[][] leftTableContent;
    private String[][] rightTableContent;

    private Boolean selectedRight;
    private String selectedCol;
    private String sortOrder;
    private String selectedLectureId;

    public TableController(SQLiteManager sqLiteManager, LoginController loginController, MenuController menuController) {
        this.lecturesTableView = new TableView();
        this.sqLiteManager = sqLiteManager;
        this.loginController = loginController;
        this.menuController = menuController;

        // Default values
        selectedRight = true;
        selectedCol = "ID";
        sortOrder = "ASC";
        selectedLectureId = "";
        update();

        addListeners();
    }

    //<editor-fold desc="Actions">
    @Override
    public void update() {
        colNames = menuController.getActiveColNames().toArray(new String[]{});
        leftTableContent = getNonJoinedLectures();
        rightTableContent = getJoinedLectures();
        lecturesTableView.getLeftTable().setModel(new TableModel(leftTableContent, colNames));
        lecturesTableView.getRightTable().setModel(new TableModel(rightTableContent, colNames));
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
    //</editor-fold>

    //<editor-fold desc="Queries">
    private QueryBuilder prebuildLecturesQuery() {
        List<String> colNames = menuController.getActiveColNames();
        List<String> chairNames = menuController.getActiveChairNames();

        QueryBuilder query = new QueryBuilder(QueryType.SELECT);

        for (String colName : colNames) {
            query.addSelect(colName, "LECTURES");
        }

        query.addFrom(new String[]{"LECTURES", "STUDENTS"});
        query.addJoin("INNER JOIN CHAIRS c ON c.CHAIR = l.CHAIR");

        StringBuilder whereStmt = new StringBuilder("l.CHAIR in (");
        for (int i = 0; i < chairNames.size(); i++) {
            whereStmt.append(String.format("'%s'", chairNames.get(i)));
            if (i < chairNames.size() - 1) {
                whereStmt.append(",");
            }
        }
        whereStmt.append(")");
        query.addWhere(whereStmt.toString());
        query.addWhere(String.format("s.ID = '%s'", loginController.getLoggedInStudent().getId()));

        query.addOrderBy(selectedCol, "LECTURES", sortOrder);

        return query;
    }

    private String[][] getJoinedLectures() {
        QueryBuilder query = prebuildLecturesQuery();

        query.addJoin("INNER JOIN ATTENDS a ON a.STUDENT_ID = s.ID AND a.LECTURE_ID = l.ID");

        try {
            return sqLiteManager.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Error querying joined lectures: " + e.toString());
            return new String[][]{{}};
        }
    }

    private String[][] getNonJoinedLectures() {
        QueryBuilder query = prebuildLecturesQuery();

        query.addJoin("LEFT JOIN ATTENDS a ON a.STUDENT_ID = s.ID AND a.LECTURE_ID = l.ID");
        query.addWhere("a.STUDENT_ID IS NULL");

        try {
            return sqLiteManager.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Error querying non-joined lectures: " + e.toString());
            return new String[][]{{}};
        }
    }
    //</editor-fold>

    //<editor-fold desc="Getters & Setters">
    @Override
    public View getView() {
        return lecturesTableView;
    }

    public void setView(TableView lecturesTableView) {
        this.lecturesTableView = lecturesTableView;
    }

    public Boolean isJoinedSelected() {
        return selectedRight;
    }

    public void setJoinedSelected(Boolean selectedRight) {
        this.selectedRight = selectedRight;
    }

    public String getSelectedCol() {
        return selectedCol;
    }

    public void setSelectedCol(String selectedCol) {
        this.selectedCol = selectedCol;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getSelectedLectureId() {
        return selectedLectureId;
    }

    public Lecture getLecture() {
        return selectedLecture;
    }

    public void setSelectedLectureId(String selectedLectureId) {
        this.selectedLectureId = selectedLectureId;
    }
    //</editor-fold>

    //<editor-fold desc="Listeners">
    @Override
    public void addListeners() {
        lecturesTableView.setRowListener(new RowListener());
        lecturesTableView.setHeaderListener(new HeaderListener());
    }

    class HeaderListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int colId = lecturesTableView.getLeftTable().columnAtPoint(e.getPoint());
            String selectedCol = lecturesTableView.getLeftTable().getColumnName(colId);
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
            if (e.getSource() == lecturesTableView.getLeftTable().getSelectionModel()) {
                // User selected row in left table
                setJoinedSelected(false);
                lecturesTableView.getRightTable().getSelectionModel().clearSelection();
                selectedRow = lecturesTableView.getLeftTable().getSelectedRow();
                if (selectedRow != -1) {
                    setSelectedLectureId(
                            (String) lecturesTableView.getLeftTable().getValueAt(selectedRow, 0));
                    selectedLecture = sqLiteManager.getLecture(Integer.parseInt(selectedLectureId));
                }
            } else if (e.getSource() == lecturesTableView.getRightTable().getSelectionModel()) {
                setJoinedSelected(true);
                lecturesTableView.getLeftTable().getSelectionModel().clearSelection();
                selectedRow = lecturesTableView.getRightTable().getSelectedRow();
                if (selectedRow != -1) {
                    setSelectedLectureId(
                            (String) lecturesTableView.getRightTable().getValueAt(selectedRow, 0));
                    selectedLecture = sqLiteManager.getLecture(Integer.parseInt(selectedLectureId));
                }
            } else {
                throw new RuntimeException();
            }
            notifyAllObservers();
        }
    }
    //</editor-fold>
}
