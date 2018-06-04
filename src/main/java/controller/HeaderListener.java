package controller;

import view.LecturesTableView;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HeaderListener extends MouseAdapter {

    private LecturesTableController lecturesTableController;
    private LecturesTableView lecturesTableView;

    public HeaderListener(LecturesTableController lecturesTableController) {
        this.lecturesTableController = lecturesTableController;
        lecturesTableView = (LecturesTableView) lecturesTableController.getView();
    }

    //<editor-fold desc="Action Section">
    @Override
    public void mouseClicked(MouseEvent e) {
        int colID = lecturesTableView.getLeftTable().columnAtPoint(e.getPoint());
        String selectedCol = lecturesTableView.getLeftTable().getColumnName(colID);
        lecturesTableController.changeSelectedCol(selectedCol);
    }
    //</editor-fold>

}
