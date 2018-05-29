package controller;

import view.LecturesTableView;
import view.View;
import SQLiteManager.*;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

public class LecturesTableController extends Controller {

    private LecturesTableView view;
    private MenuController menuController;

    private SQLiteManager sqLiteManager;

    private String colToSort = "ID";
    private String sortOrder = "ASC";
    private String lectureId = "";

    public LecturesTableController(SQLiteManager sqLiteManager, MenuController menuController) {
        this.view = new LecturesTableView();
        this.sqLiteManager = sqLiteManager;
        this.menuController = menuController;
        addListeners();
    }

    private QueryBuilder prebuildLecturesQuery() {
        List<String> colNames = menuController.getActiveColNames();
        List<String> chairNames = menuController.getActiveChairNames();
        String studentName = menuController.getActiveStudentName();

        QueryBuilder query = new QueryBuilder(QueryBuilder.Type.SELECT);

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
        query.addWhere(String.format("s.NAME = '%s'", studentName));

        query.addOrderBy(colToSort, "LECTURES", sortOrder);

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

    @Override
    void addListeners() {
        view.setRowListener(new RowListener());
        view.setHeaderListener(new HeaderListener());
    }

    @Override
    public void update() {
        String[] colNames = menuController.getActiveColNames().toArray(new String[]{});
        String[][] leftData = getNonJoinedLectures();
        String[][] rightData = getJoinedLectures();

        AbstractTableModel leftTableModel = new LectureTableModel(leftData, colNames);
        AbstractTableModel rightTableModel = new LectureTableModel(rightData, colNames);
        view.getLeftTable().setModel(leftTableModel);
        view.getRightTable().setModel(rightTableModel);
    }

    /**
     * Returns the ID of the lecture the user currently selected in the data tables.
     * @return lectureId
     */
    public String getSelectedLectureID() {
        return lectureId;
    }

    public View getView() {
        return view;
    }

    class HeaderListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            int leftCol = view.getLeftTable().columnAtPoint(e.getPoint());
            int rightCol = view.getRightTable().columnAtPoint(e.getPoint());
            String colName = view.getLeftTable().getColumnName(leftCol);
            colToSort = colName;
            if (sortOrder.equals("ASC")) {
                sortOrder = "DESC";
            } else {
                sortOrder = "ASC";
            }
            update();
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
            if(e.getSource() == view.getLeftTable().getSelectionModel()) {
                // User selected row in left table
                view.getRightTable().getSelectionModel().clearSelection();
                selectedRow = view.getLeftTable().getSelectedRow();
                if(selectedRow != -1) {
                    // no actual user input
                    lectureId = (String) view.getLeftTable().getValueAt(selectedRow, 0);
                }
            } else if(e.getSource() == view.getRightTable().getSelectionModel()) {
                view.getLeftTable().getSelectionModel().clearSelection();
                selectedRow = view.getRightTable().getSelectedRow();
                if(selectedRow != -1) {
                    // no actual user input
                    lectureId = (String) view.getLeftTable().getValueAt(selectedRow, 0);
                }
            } else {
                throw new RuntimeException();
            }
            notifyAllObservers();
        }
    }

    /**
     * Custom TableModel to represent lecture data in a JTable.
     */
    class LectureTableModel extends DefaultTableModel {

        public LectureTableModel(String[][] rowData, String[] colNames) {
            super(rowData, colNames);
        }

        /**
         * The user should not be able to edit the table data.
         *
         * @param row
         * @param column
         * @return
         */
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

}
