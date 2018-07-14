package backend;

import org.json.JSONObject;
import org.json.JSONArray;

import SQLiteManager.*;

import java.sql.SQLException;
import java.util.*;

public class DBConverter {

    private static final String TABLE_LIST_KEY = "tables";
    private static final String TABLE_NAME_KEY = "tableName";
    private static final String COLUMN_LIST_KEY = "columns";
    private static final String ENTRY_LIST_KEY = "entries";

    private SQLiteManager sqLiteManager;
    private List<String> tableList;
    private Map<String, ArrayList<String>> tableColsMap;

    public DBConverter(SQLiteManager sqLiteManager) {
        this.sqLiteManager = sqLiteManager;

        // Read data base structure
        // table names
        this.tableList = new ArrayList<>();
        try {
            QueryBuilder tableNameQuery = QueryBuilder.tableNamesQuery();
            String[][] tableNameMatrix = sqLiteManager.executeQuery(tableNameQuery);
            Arrays.stream(tableNameMatrix).map(s -> s[0]).forEach(tableList::add);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // column names
        this.tableColsMap = new HashMap<>();
        for(String tableName : tableList) {
            ArrayList<String> list = new ArrayList<>();
            tableColsMap.put(tableName, list);
            try {
                QueryBuilder colNameQuery = QueryBuilder.columnNamesQuery(tableName);
                String[][] colNameMatrix = sqLiteManager.executeQuery(colNameQuery);
                Arrays.stream(colNameMatrix).map(s -> s[1]).forEach(list::add);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private JSONObject serializeDatabase() {
        JSONArray tableArray = new JSONArray();
        for(String tableName : tableList) {
            JSONObject tableObject = serializeTable(tableName);
            tableArray.put(tableObject);
        }

        JSONObject databaseObject = new JSONObject();
        databaseObject.put(TABLE_LIST_KEY, tableArray);
        return databaseObject;
    }

    private void deserializeDatabase(JSONObject databaseObject) {
        if(!validateSchema(databaseObject)) {
            System.out.println("Database schema does not match");
            return;
        }

        JSONArray tableArray = databaseObject.getJSONArray(TABLE_LIST_KEY);

        for(int i = 0; i < tableArray.length(); i++) {
            JSONObject tableObject = tableArray.getJSONObject(i);
            String table = tableObject.getString(TABLE_NAME_KEY);
            String[][] entries = deserializeTable(tableObject);
            String[] columns = tableColsMap.get(table).toArray(new String[0]);

            QueryBuilder truncateQuery = new QueryBuilder(QueryType.DELETE);
            truncateQuery.addDeleteTab(table);
            try {
                sqLiteManager.executeStatement(truncateQuery);
                sqLiteManager.insertEntries(table, columns, entries);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private JSONObject serializeTable(String tableName) {
        QueryBuilder tableQuery = QueryBuilder.tableDataQuery(tableName);
        JSONObject object = new JSONObject();
        try {
            String[][] data = sqLiteManager.executeQuery(tableQuery);

            JSONArray entries = new JSONArray();
            JSONArray schema = new JSONArray();
            tableColsMap.get(tableName).forEach(schema::put);

            for (int row = 0; row < data.length; row++) {
                JSONObject entry = new JSONObject();
                for (int col = 0; col < data[row].length; col++) {
                    entry.put(tableColsMap.get(tableName).get(col), data[row][col]);
                }
                entries.put(entry);
            }
            object.put(TABLE_NAME_KEY, tableName);
            object.put(COLUMN_LIST_KEY, schema);
            object.put(ENTRY_LIST_KEY, entries);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return object;
    }

    private String[][] deserializeTable(JSONObject object) {
        String tableName = object.getString(TABLE_NAME_KEY);
        List<String> colList = tableColsMap.get(tableName);

        JSONArray entriesJSONArray = object.getJSONArray(ENTRY_LIST_KEY);
        String[][] entries = new String[entriesJSONArray.length()][colList.size()];

        for (int rowIdx = 0; rowIdx < entries.length; rowIdx++) {
            for (int colIdx = 0; colIdx < colList.size(); colIdx++) {
                entries[rowIdx][colIdx] = entriesJSONArray.getJSONObject(rowIdx).getString(colList.get(colIdx));
            }
        }
        return entries;
    }

    private boolean validateSchema(JSONObject database) {
        JSONArray tableArray = database.getJSONArray(TABLE_LIST_KEY);
        if (tableArray.length() != tableList.size()) {
            return false;
        }

        for(int i = 0; i < tableArray.length(); i++) {
            JSONObject tableObject = tableArray.getJSONObject(i);
            String tableName = tableObject.getString(TABLE_NAME_KEY);
            if(!tableList.contains(tableName)) {
                // table names not matching
                return false;
            }
            JSONArray colArray = tableObject.getJSONArray(COLUMN_LIST_KEY);
            if(colArray.length() != tableColsMap.get(tableName).size()) {
                return false;
            }
            for(int j = 0; j < colArray.length(); j++) {
                String colName = colArray.getString(j);
                if(!tableColsMap.get(tableName).contains(colName)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        SQLiteManager sqLiteManager = new SQLiteManager();
        DBConverter dbConverter = new DBConverter(sqLiteManager);

        JSONObject database = dbConverter.serializeDatabase();
        dbConverter.deserializeDatabase(database);
    }
}
