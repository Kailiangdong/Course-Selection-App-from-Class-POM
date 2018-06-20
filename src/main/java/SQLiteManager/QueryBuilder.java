package SQLiteManager;

import java.sql.SQLException;
import java.util.*;

public class QueryBuilder {

    private QueryType type;
    private List<String> select;
    private List<String> from;
    private List<String> join;
    private List<String> where;
    private List<String> groupBy;
    private List<String> having;
    private List<String> orderBy;

    public QueryBuilder(QueryType type) {
        this.type = type;
        select = new ArrayList<>();
        from = new ArrayList<>();
        join = new ArrayList<>();
        where = new ArrayList<>();
        groupBy = new ArrayList<>();
        having = new ArrayList<>();
        orderBy = new ArrayList<>();
    }

    //<editor-fold desc="Helper Section">
    private String returnFirstChar(String string){
        return Character.toString(string.charAt(0)).toLowerCase();
    }

    private void listAddingToBuilder(
            StringBuilder builder, ListIterator<String> listIterator, String append, Boolean space) {
        while (listIterator.hasNext()) {
            if (space) {
                builder.append(" " + listIterator.next());
            }
            else {
                builder.append(listIterator.next());
            }
            if (listIterator.hasNext()) {
                builder.append(append);
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="Select Section">
    public void addSelect(String colName, String tabName) { select.add(returnFirstChar(tabName) + "." + colName); }

    public void addSelect(String[] colNames, String[] tabNames) {
        if (colNames.length <= tabNames.length) {
            for (int colNamesLength = 0; colNamesLength < colNames.length; colNamesLength++) {
                addSelect(colNames[colNamesLength], tabNames[colNamesLength]);
            }
        }
    }

    public void addFrom(String tabName) { from.add(tabName + " " + returnFirstChar(tabName)); }

    public void addFrom(String[] tabNames) {
        for (String tabName : tabNames) {
            addFrom(tabName);
        }
    }

    public void addJoin(String joinStmt) {
        join.add(joinStmt);
    }

    public void addJoin(String[] joinStmts) { Collections.addAll(join, joinStmts); }

    public void addWhere(String condition) {
        where.add(condition);
    }

    public void addWhere(String[] conditions) { Collections.addAll(where, conditions); }

    public void addGroupBy(String colName, String tabName) {
        groupBy.add(returnFirstChar(tabName) + "." + colName);
    }

    public void addGroupBy(String[] colNames, String[] tabNames) {
        if (colNames.length <= tabNames.length) {
            for (int colNamesLength = 0; colNamesLength < colNames.length; colNamesLength++) {
                addGroupBy(colNames[colNamesLength], tabNames[colNamesLength]);
            }
        }
    }

    public void addHaving(String condition) {
        having.add(condition);
    }

    public void addHaving(String[] conditions) { Collections.addAll(having, conditions); }

    public void addOrderBy(String colName, String tabName, String direction) {
        orderBy.add(returnFirstChar(tabName) + "." + colName + " " + ((direction != null) ? direction : "asc"));
    }

    public void addOrderBy(String[] colNames, String[] tabNames, String[] directions) {
        if (colNames.length <= tabNames.length && colNames.length <= directions.length) {
            for (int colNamesLength = 0; colNamesLength < colNames.length; colNamesLength++) {
                addOrderBy(colNames[colNamesLength], tabNames[colNamesLength], directions[colNamesLength]);
            }
        }
    }

    private String getSelectStmt() {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT");
        listAddingToBuilder(builder, select.listIterator(), ",", true);
        builder.append("\n" + "FROM");
        listAddingToBuilder(builder, from.listIterator(), ",", true);
        if (join != null && join.size() > 0) {
            builder.append("\n");
            listAddingToBuilder(builder, join.listIterator(), "\n", false);
        }
        if (where != null && where.size() > 0) {
            builder.append("\n" + "WHERE");
            listAddingToBuilder(builder, where.listIterator(), " AND", true);
        }
        if (groupBy != null && groupBy.size() > 0) {
            builder.append("\n" + "GROUP BY");
            listAddingToBuilder(builder, groupBy.listIterator(), ",", true);
        }
        if (having != null && having.size() > 0) {
            builder.append("\n" + "HAVING");
            listAddingToBuilder(builder, having.listIterator(), ",", true);
        }
        if (orderBy != null && orderBy.size() > 0) {
            builder.append("\n" + "ORDER BY");
            listAddingToBuilder(builder, orderBy.listIterator(), ",", true);
        }
        System.out.println(builder.toString()+"\n");
        return builder.toString();
    }
    //</editor-fold>

    //<editor-fold desc="Insert Section">
    public void addInsertTab(String tabName) { from.add(tabName); }

    public void addInsertCols(String colName) { select.add(colName); }

    public void addInsertCols(String[] colNames) { Collections.addAll(select, colNames); }

    public void addInsertVals(String value) { where.add(value); }

    public void addInsertVals(String[] values) { Collections.addAll(where, values); }

    private String getInsertStmt() {
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO " + from.get(0) + " (");
        listAddingToBuilder(builder, select.listIterator(), ", ", false);
        builder.append(")\n" + "VALUES (");
        listAddingToBuilder(builder, where.listIterator(), ", ", false);
        builder.append(")");
        System.out.println(builder.toString()+"\n");
        return builder.toString();
    }
    //</editor-fold>

    //<editor-fold desc="Delete Section">
    public void addDeleteTab(String tabName) { from.add(tabName); }

    public void addDeleteWhere(String condition) {
        where.add(condition);
    }

    public void addDeleteWhere(String[] conditions) { Collections.addAll(where,conditions);}

    private String getDeleteStmt() {
        StringBuilder builder = new StringBuilder();
        builder.append("DELETE FROM " + from.get(0));
        if (where != null && where.size() > 0) {
            builder.append("\n" + "WHERE");
            listAddingToBuilder(builder, where.listIterator(), " AND", true);
        }
        System.out.println(builder.toString()+"\n");
        return builder.toString();
    }
    //</editor-fold>

    //<editor-fold desc="Main Section">
    public String toString() {
        switch (type) {
            case SELECT:
                return getSelectStmt();
            case INSERT:
                return getInsertStmt();
            case DELETE:
                return getDeleteStmt();
        }
        return "";
    }
    //</editor-fold>

}