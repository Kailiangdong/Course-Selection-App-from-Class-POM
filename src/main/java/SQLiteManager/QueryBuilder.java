package SQLiteManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QueryBuilder {

    public enum Type {SELECT, INSERT, DELETE}

    ;

    private Type type;
    private List<String> select;
    private List<String> from;
    private List<String> join;
    private List<String> where;
    private List<String> groupBy;
    private List<String> having;
    private List<String> orderBy;

    public QueryBuilder(Type type) {
        this.type = type;
        select = new ArrayList<>();
        from = new ArrayList<>();
        join = new ArrayList<>();
        where = new ArrayList<>();
        groupBy = new ArrayList<>();
        having = new ArrayList<>();
        orderBy = new ArrayList<>();
    }

    /**
     * Call only once
     *
     * @param tabName
     */
    public void addInsertTab(String tabName) {
        this.select = new ArrayList<>();
        select.add(tabName);
    }

    /**
     * Call as often as you like, but make sure to add enough values!
     *
     * @param colNames
     */
    public void addInsertCols(String[] colNames) {
        Collections.addAll(select, colNames);
    }

    public void addInsertVals(String[] values) {
        Collections.addAll(from, values);
    }

    public void addDelete(String tabName) {
        this.select = new ArrayList<>();
        select.add(tabName);
    }

    public void addSelect(String colName, String tabName) {
        String stmt = Character.toString(tabName.charAt(0)).toLowerCase() + "." + colName;
        select.add(stmt);
    }

    public void addFrom(String tabName) {
        String stmt = tabName + " " + Character.toString(tabName.charAt(0)).toLowerCase();
        from.add(stmt);
    }

    public void addFrom(String[] tabNames) {
        for (String tabName : tabNames) {
            addFrom(tabName);
        }
    }

    public void addJoin(String joinStmt) {
        join.add(joinStmt);
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
        if (direction != null) {
            stmt += " " + direction;
        }
        orderBy.add(stmt);
    }

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

    private String getSelectStmt() {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT");
        for (int i = 0; i <= select.size() - 1; i++) {
            builder.append(" " + select.get(i));
            if (i < select.size() - 1) {
                builder.append(",");
            }
        }
        builder.append("\n");
        builder.append("FROM");
        for (int i = 0; i <= from.size() - 1; i++) {
            builder.append(" " + from.get(i));
            if (i < from.size() - 1) {
                builder.append(",");
            }
        }
        if (join != null && join.size() > 0) {
            builder.append("\n");
            for (int i = 0; i < join.size(); i++) {
                builder.append(join.get(i));
                if (i < join.size() - 1) {
                    builder.append("\n");
                }
            }
        }
        if (where != null && where.size() > 0) {
            builder.append("\n");
            builder.append("WHERE");
            for (int i = 0; i <= where.size() - 1; i++) {
                builder.append(" " + where.get(i));
                if (i < where.size() - 1) {
                    builder.append(" and ");
                }
            }
        }
        if (groupBy != null && groupBy.size() > 0) {
            builder.append("\n");
            builder.append("GROUP BY");
            for (int i = 0; i <= groupBy.size() - 1; i++) {
                builder.append(" " + groupBy.get(i));
                if (i < groupBy.size() - 1) {
                    builder.append(", ");
                }
            }
        }
        if (having != null && having.size() > 0) {
            builder.append("\n");
            builder.append("HAVING");
            for (int i = 0; i <= having.size() - 1; i++) {
                builder.append(" " + having.get(i));
                if (i < having.size() - 1) {
                    builder.append(", ");
                }
            }
        }
        if (orderBy != null && orderBy.size() > 0) {
            builder.append("\n");
            builder.append("ORDER BY");
            for (int i = 0; i <= orderBy.size() - 1; i++) {
                builder.append(" " + orderBy.get(i));
                if (i < orderBy.size() - 1) {
                    builder.append(", ");
                }
            }
        }
        System.out.println(builder.toString());
        return builder.toString();
    }

    private String getInsertStmt() {
        StringBuilder builder = new StringBuilder();

        // Build INSERT INTO table_name (column1, column2, column3, ...)
        builder.append("INSERT INTO ");
        builder.append(select.get(0));
        builder.append(" (");
        for (int i = 1; i < select.size(); i++) {
            builder.append(select.get(i));
            if (i < select.size() - 1) {
                builder.append(", ");
            }
        }
        builder.append(")\n");

        // Build VALUES (value1, value2, value3, ...)
        builder.append("VALUES (");
        for (int i = 0; i < from.size(); i++) {
            builder.append(from.get(i));
            if (i < from.size() - 1) {
                builder.append(", ");
            }
        }
        builder.append(")");
        return builder.toString();
    }

    private String getDeleteStmt() {
        StringBuilder builder = new StringBuilder();

        // Build DELETE FROM table_name
        builder.append("DELETE FROM ");
        builder.append(select.get(0));

        // Build WHERE condition;
        if (where != null && where.size() > 0) {
            builder.append("\nWHERE");
            for (int i = 0; i <= where.size() - 1; i++) {
                builder.append(" " + where.get(i));
                if (i < where.size() - 1) {
                    builder.append(" and ");
                }
            }
        }

        return builder.toString();
    }

}