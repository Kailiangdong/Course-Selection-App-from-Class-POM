package main.java.viewcontroller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JMenuItem;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import main.java.SQLiteManager.SQLiteManager;

public class LectureController {

  private SQLiteManager sqlManager;
  private TableView tableView;

  // Controller Parameters

  private String[] colNames = {"ID", "TITLE", "CHAIR", "ECTS"};
  private String[] chairNames;

  private ArrayList<String> chairNamesActive;
  private ArrayList<String> colNamesActive;

  private String studentName = "Stefan";
  private String colToSort = "ID";
  private String order = "ASC";

  public LectureController() {

    this.sqlManager = new SQLiteManager();
    this.tableView = new TableView();

    colNamesActive = new ArrayList<>();
    for (String colName : colNames) {
      colNamesActive.add(colName);
    }
    tableView.setColProjectionMenu(colNames);

    this.chairNames = new String[]{};
    this.chairNamesActive = new ArrayList<>();

    QueryBuilder query = queryChairNames();
    String[][] queryResult = executeQuery(query);
    chairNames = new String[queryResult.length];
    chairNamesActive = new ArrayList<>();
    for (int i = 0; i < queryResult.length; i++) {
      String chairName = queryResult[i][0];
      chairNames[i] = chairName;
      chairNamesActive.add(chairName);
    }
    tableView.setSelectionMenu(chairNames);

    updateData();
    addListeners();
    tableView.setVisible(true);
  }

  private void addListeners() {
    tableView.setTableModelListener(new LectureTableListener());
    tableView.setSelectionMenuItemsListener(new SelectionMenuItemListener());
    tableView.setProjectionMenuItemsListener(new ProjectionMenuItemListener());
    tableView.setColumnHeaderListener(new TableColumnHeaderListener());
    tableView.setExitListener(new ExitListener());
  }

  // Database Access

  private QueryBuilder queryLectureDetails(String lectureID) {
    if (lectureID == null) {
      return null;
    }

    QueryBuilder query = new QueryBuilder();

    query.addSelect("TIME", "LECTURES");
    query.addSelect("PLACE", "LECTURES");

    query.addFrom("LECTURES");

    query.addWhere("L.ID = " + lectureID);

    return query;
  }

  private QueryBuilder queryChairNames() {
    QueryBuilder query = new QueryBuilder();

    query.addSelect("CHAIR", "CHAIRS");
    query.addFrom("CHAIRS");
    query.addFrom("STUDENTS");
    query.addWhere("S.NAME = " + "'" + studentName + "'");
    query.addWhere("(C.SUBJECT = S.MINOR or C.SUBJECT = S.MAJOR)");
    query.addGroupBy("CHAIR", "CHAIRS");

    return query;
  }

  private QueryBuilder queryLectures() {

    QueryBuilder query = new QueryBuilder();

    for (String colName : colNames) {
      query.addSelect(colName, "LECTURES");
    }

    query.addFrom("LECTURES");

    StringBuilder where = new StringBuilder("L.CHAIR in (");
    for (int i = 0; i < chairNamesActive.size(); i++) {
      where.append("'");
      where.append(chairNamesActive.get(i));
      where.append("'");
      if (i != chairNamesActive.size() - 1) {
        where.append(",");
      }
    }
    where.append(")");
    query.addWhere(where.toString());

    if (colToSort != null) {
      query.addOrderBy(colToSort, "LECTURES", order);
    }

    return query;
  }

  private QueryBuilder queryJoinableLectures() {
    QueryBuilder query = queryLectures();

    query.addFrom("CHAIRS");
    query.addFrom("STUDENTS");

    query.addWhere("S.NAME = " + "'" + studentName + "'");
    query.addWhere("L.CHAIR = C.CHAIR");
    query.addWhere("(C.SUBJECT = S.MINOR or C.SUBJECT = S.MAJOR)");

    return query;
  }

  private QueryBuilder queryJoinedLectures() {
    QueryBuilder query = queryJoinableLectures();
    query.addFrom("ATTENDS");
    query.addWhere("S.ID = A.STUDENT_ID");
    query.addWhere("L.ID = A.LECTURE_ID");
    return query;
  }
  private String[][] executeQuery(QueryBuilder query) {
    try {
      return sqlManager.executeQuery(
          query.getSelect(), query.getFrom(), query.getWhere(), query.getGroupBy(),
          query.getHaving(),
          query.getOrderBy());
    } catch (SQLException e) {
      e.printStackTrace();
      return new String[][]{{}};
    }
  }

  private void updateData() {
    QueryBuilder query = queryJoinedLectures();
    String[][] rowData = executeQuery(query);
    String[] colNames = colNamesActive.toArray(new String[]{});
    tableView.setTableModel(new DefaultTableModel(rowData, colNames));
  }

  // Listener Classes

  class LectureTableListener implements ListSelectionListener {
    @Override
    public void valueChanged(ListSelectionEvent e) {
      int selectedRow = tableView.getJoinableLecturesTable().getSelectedRow();
      if (selectedRow == -1) {
        // no selection when event is triggered due to data update
        return;
      }
      String lectureID = (String) tableView.getJoinableLecturesTable().getValueAt(selectedRow, 0);
      QueryBuilder query = queryLectureDetails(lectureID);
      String[][] queryResult = executeQuery(query);
      String details = Arrays.toString(queryResult[0]);
      tableView.setDetailsAreaText("Lecture Details:" + details);
    }
  }

  class SelectionMenuItemListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      int index = Arrays.asList(tableView.getSelectionMenuItems()).indexOf(e.getSource());
      String itemName = chairNames[index];
      if (chairNamesActive.contains(itemName)) {
        chairNamesActive.remove(itemName);
        ((JMenuItem) e.getSource()).setText(itemName);
      } else {
        chairNamesActive.add(itemName);
        ((JMenuItem) e.getSource()).setText("√ " + itemName);
      }
      updateData();

    }
  }

  class ProjectionMenuItemListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      int index = Arrays.asList(tableView.getProjectionMenuItems()).indexOf(e.getSource());
      if (index == 0) {
        // never touch ID column
        return;
      }
      String itemName = colNames[index];
      if (colNamesActive.contains(itemName)) {
        colNamesActive.remove(itemName);
        ((JMenuItem) e.getSource()).setText(itemName);
      } else {
        colNamesActive.add(itemName);
        ((JMenuItem) e.getSource()).setText("√ " + itemName);
      }
      updateData();

    }
  }

  class TableColumnHeaderListener extends MouseAdapter {

    @Override
    public void mouseClicked(MouseEvent e) {
      int col = tableView.getJoinableLecturesTable().columnAtPoint(e.getPoint());
      String colName = tableView.getJoinableLecturesTable().getColumnName(col);
      colToSort = colName;
      if (order.equals("ASC")) {
        order = "DESC";
      } else {
        order = "ASC";
      }
      updateData();

    }
  }

  class ExitListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      tableView.setVisible(false);
      System.exit(0);
    }
  }

  public static void main(String[] args) {
    LectureController controller = new LectureController();
  }

}
