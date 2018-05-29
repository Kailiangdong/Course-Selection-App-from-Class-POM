package main.java.SQLiteManager;

import java.util.ArrayList;
import java.util.List;

public class QueryBuilder {

  private List<String> select;
  private List<String> from;
  private List<String> where;
  private List<String> groupBy;
  private List<String> having;
  private List<String> orderBy;

  public QueryBuilder() {
    select = new ArrayList<>();
    from = new ArrayList<>();
    where = new ArrayList<>();
    groupBy = new ArrayList<>();
    having = new ArrayList<>();
    orderBy = new ArrayList<>();
  }

  public void addSelect(Boolean tabNameSelected,String colName, String tabName) {
    if (tabNameSelected) {
      String stmt = tabName.charAt(0) + "." + colName;
      select.add(stmt);
    }
    else {
      select.add(colName);
    }

  }

  public void addFrom(Boolean tabNameSelected, String tabName) {
    if (tabNameSelected) {
      String stmt = tabName + " " + tabName.charAt(0);
      from.add(stmt);
    }
    else{
      from.add(tabName);
    }
  }

  public void addWhere(String condition) {
    where.add(condition);
  }

  public void addGroupBy(String colName, String tabName) {
    String stmt = tabName.charAt(0) + "." + colName;
    groupBy.add(stmt);
  }

  public void addHaving(String condition) {
    having.add(condition);
  }

  public void addOrderBy(String colName, String tabName, String direction) {
    String stmt = tabName.charAt(0) + "." + colName;
    if(direction != null) {
      stmt += " " + direction;
    }
    orderBy.add(stmt);
  }

  public String[] getSelect() {
    return select.toArray(new String[]{});
  }

  public String[] getFrom() {
    return from.toArray(new String[]{});
  }

  public String[] getWhere() {
    return where.toArray(new String[]{});
  }

  public String[] getGroupBy() {
    return groupBy.toArray(new String[]{});
  }

  public String[] getHaving() {
    return having.toArray(new String[]{});
  }

  public String[] getOrderBy() {
    return orderBy.toArray(new String[]{});
  }

}