package controller;

import view.LecturesTableView;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Listens to the TableSelectionModel and determines
 * which row was selected by the user.
 */
public class RowListener implements ListSelectionListener {

    private LecturesTableController lecturesTableController;
    private LecturesTableView lecturesTableView;

    public RowListener(LecturesTableController lecturesTableController) {
        this.lecturesTableController = lecturesTableController;
        lecturesTableView = (LecturesTableView) lecturesTableController.getView();
    }

    //<editor-fold desc="Action Section">
    @Override
    public void valueChanged(ListSelectionEvent e) {
        int selectedRow;
        if(e.getSource() == lecturesTableView.getLeftTable().getSelectionModel()) {
            // User selected row in left table
            lecturesTableView.getRightTable().getSelectionModel().clearSelection();
            selectedRow = lecturesTableView.getLeftTable().getSelectedRow();
            if(selectedRow != -1) {
                // no actual user input
                lecturesTableController.setSelectedLectureId(
                        (String) lecturesTableView.getLeftTable().getValueAt(selectedRow, 0));
            }
        } else if(e.getSource() == lecturesTableView.getRightTable().getSelectionModel()) {
            lecturesTableView.getLeftTable().getSelectionModel().clearSelection();
            selectedRow = lecturesTableView.getRightTable().getSelectedRow();
            if(selectedRow != -1) {
                // no actual user input
                lecturesTableController.setSelectedLectureId(
                        (String) lecturesTableView.getRightTable().getValueAt(selectedRow, 0));
            }
        } else {
            throw new RuntimeException();
        }
        lecturesTableController.notifyAllObservers();
    }
    //</editor-fold>

}