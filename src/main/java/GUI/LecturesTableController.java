package main.java.GUI;

import main.java.SQLiteManager.SQLiteManager;
import main.java.viewcontroller.QueryBuilder;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.util.Arrays;

public class LecturesTableController extends Controller {

    private LecturesTableView view;
    private MenuController menuController;

    private SQLiteManager sqLiteManager;

    private String[][] mockRowData = new String[][]{{"Row 1", "Row 1"}, {"Row 2", "Row 2"}};
    private String[] mockColHeaders = new String[]{"Head 1", "Head 2"};

    public LecturesTableController(SQLiteManager sqLiteManager, MenuController menuController) {
        this.view = new LecturesTableView();
        this.sqLiteManager = sqLiteManager;
        this.menuController = menuController;
    }

    @Override
    void addListeners() {
        view.setTableModelListener(new LectureTableListener());
    }

    @Override
    public void update() {
        // get updated state
        menuController.getActiveChairNames();
        menuController.getActiveColNames();
        menuController.getActiveStudentName();

        AbstractTableModel tableModel = new LectureTableDatModel(mockRowData, mockColHeaders);
        view.getLeftTable().setModel(tableModel);
        view.getRightTable().setModel(tableModel);
    }

    public int getSelectedLectureID() {
        // TODO: replace mockup value
        return 5;
    }

    public View getView() {
        return view;
    }

    class LectureTableListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if(e.getSource() == view.getLeftTable()) {
                view.getRightTable().getSelectionModel().clearSelection();
            } else if(e.getSource() == view.getRightTable()) {
                view.getLeftTable().getSelectionModel().clearSelection();
            }
        }
    }

    class LectureTableDatModel extends DefaultTableModel {

        public LectureTableDatModel(String[][] rowData, String[] colNames) {
            super(rowData, colNames);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

}
