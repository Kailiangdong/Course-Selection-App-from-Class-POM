package controller;

import view.*;
import SQLiteManager.*;
import java.sql.SQLException;
import java.util.List;

public class LecturesTableController extends Controller {

    private LecturesTableView lecturesTableView;
    private MenuController menuController;
    private SQLiteManager sqLiteManager;
    private String[] colNames;
    private String[][] leftTableContent;
    private String[][] rightTableContent;
    private Boolean selectedRight;
    private String selectedCol;
    private String sortOrder;
    private String selectedLectureId;

    public LecturesTableController(SQLiteManager sqLiteManager, MenuController menuController) {
        lecturesTableView = new LecturesTableView();
        this.sqLiteManager = sqLiteManager;
        this.menuController = menuController;
        selectedRight = true;
        selectedCol = "ID";
        sortOrder = "ASC";
        selectedLectureId = "";
        update();
        addListeners();
    }

    //<editor-fold desc="Get/Set Section">
    @Override
    public View getView() { return lecturesTableView; }

    public void setView(LecturesTableView lecturesTableView) { this.lecturesTableView = lecturesTableView; }

    public Boolean getSelectedRight() { return selectedRight; }

    public void setSelectedRight(Boolean selectedRight) { this.selectedRight = selectedRight; }

    public String getSelectedCol() { return selectedCol; }

    public void setSelectedCol(String selectedCol) { this.selectedCol = selectedCol; }

    public String getSortOrder() { return sortOrder; }

    public void setSortOrder(String sortOrder) { this.sortOrder = sortOrder; }

    public String getSelectedLectureId() {
        return selectedLectureId;
    }

    public void setSelectedLectureId(String selectedLectureId) { this.selectedLectureId = selectedLectureId; }
    //</editor-fold>

    //<editor-fold desc="Query-building Section">
    private QueryBuilder prebuildLecturesQuery() {
        List<String> colNames = menuController.getActiveColNames();
        List<String> chairNames = menuController.getActiveChairNames();

        QueryBuilder query = new QueryBuilder(QueryType.SELECT);

        for(String colName : colNames) {
            query.addSelect(colName, "LECTURES");
        }

        query.addFrom(new String[]{"LECTURES", "STUDENTS"});
        query.addJoin("INNER JOIN CHAIRS c ON c.CHAIR = l.CHAIR");

        StringBuilder whereStmt = new StringBuilder("l.CHAIR in (");
        for(int i = 0; i < chairNames.size(); i++) {
            whereStmt.append(String.format("'%s'", chairNames.get(i)));
            if (i < chairNames.size() - 1) {
                whereStmt.append(",");
            }
        }
        whereStmt.append(")");
        query.addWhere(whereStmt.toString());
        query.addWhere(String.format("s.NAME = '%s'", menuController.getActiveStudentName()));

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

    //<editor-fold desc="Action Section">
    public void changeSelectedCol(String selectedCol) {
        this.selectedCol = selectedCol;
        if (this.sortOrder.equals("ASC")) {
            this.sortOrder = "DESC";
        } else {
            this.sortOrder = "ASC";
        }
        update();
    }
    //</editor-fold>

    //<editor-fold desc="Rest Section">
    @Override
    void addListeners() {
        lecturesTableView.setRowListener(new RowListener(this));
        lecturesTableView.setHeaderListener(new HeaderListener(this));
    }

    @Override
    public void update() {
        colNames = menuController.getActiveColNames().toArray(new String[]{});
        leftTableContent = getNonJoinedLectures();
        rightTableContent = getJoinedLectures();
        lecturesTableView.getLeftTable().setModel(new LectureTableModel(leftTableContent, colNames));
        lecturesTableView.getRightTable().setModel(new LectureTableModel(rightTableContent, colNames));
    }
    //</editor-fold>

}
