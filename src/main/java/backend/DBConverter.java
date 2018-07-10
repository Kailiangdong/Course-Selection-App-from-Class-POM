package backend;

import org.json.JSONObject;
import org.json.JSONArray;

import SQLiteManager.*;

import java.sql.SQLException;

public class DBConverter {

    private static final String TABLE_KEY = "table";
    private static final String COLUMN_KEY = "columns";
    private static final String ENTRY_KEY = "entries";

    private SQLiteManager sqLiteManager;

    public DBConverter(SQLiteManager sqLiteManager) {
        this.sqLiteManager = sqLiteManager;
    }

    public JSONObject serializeDatabase() {
        QueryBuilder tableNameQuery = QueryBuilder.getTableNames();
        String[][] tableNameMatrix;
        try {
            tableNameMatrix = sqLiteManager.executeQuery(tableNameQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        JSONArray tableArray = new JSONArray();
        for(int i = 0; i < tableNameMatrix.length; i++) {
            String tableName = tableNameMatrix[i][0];
            JSONObject tableObject = serializeTable(tableName);
            tableArray.put(tableObject);
        }
        JSONObject databaseObject = new JSONObject();
        databaseObject.put("tables", tableArray);
        return databaseObject;
    }

    public void deserializeDatabase() {

    }

    private JSONObject serializeTable(String tableName) {
        String[] colNames = new String[]{"Name", "ID"};
        String[][] data = new String[][]{{"Robert", "1"}, {"Stefan", "2"}};

        JSONObject object = new JSONObject();
        JSONArray entries = new JSONArray();
        JSONArray schema = new JSONArray();
        for (int row = 0; row < data.length; row++) {
            JSONObject entry = new JSONObject();
            for (int col = 0; col < data[row].length; col++) {
                // store column names
                if (row == 0) {
                    schema.put(colNames[col]);
                }
                entry.put(colNames[col], data[row][col]);
            }
            entries.put(entry);
        }
        object.put(TABLE_KEY, tableName);
        object.put(COLUMN_KEY, schema);
        object.put(ENTRY_KEY, entries);
        System.out.println(object.toString(2));
        return object;
    }

    private String[][] deserializeTable(JSONObject object) {
        String tableName = object.getString(TABLE_KEY);
        JSONArray colJSONArray = object.getJSONArray(COLUMN_KEY);
        String[] colNames = new String[colJSONArray.length()];
        for (int i = 0; i < colJSONArray.length(); i++) {
            colNames[i] = colJSONArray.getString(i);
        }
        JSONArray entriesJSONArray = object.getJSONArray(ENTRY_KEY);
        String[][] entries = new String[entriesJSONArray.length()][colNames.length];

        for (int row = 0; row < entries.length; row++) {
            for (int col = 0; col < colNames.length; col++) {
                entries[row][col] = entriesJSONArray.getJSONObject(row).getString(colNames[col]);
            }
        }
        return null;
    }
}
