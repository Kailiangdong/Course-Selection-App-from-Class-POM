package main.java.SQLiteManager;

import main.java.backend.BackendAdapter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class SQLiteManager {

    private static final String JDBC_DRIVER = "org.sqlite.JDBC";
    private static final String DB_URL = "jdbc:sqlite:cache.db";
    private static final String DB_NAME = "cache.db";

    /** Creates an SQLiteManager object
     *  If the cache doesn't exist it creates a new one
     *  If the cache is corrupted it wipes it
     * @author Stefan Vladov
     */
    public SQLiteManager() {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Failed loading JDBC_DRIVER " + e.toString());
            System.exit(-1);
        }
        Path path = Paths.get(DB_NAME);
        if (Files.notExists(path) || !checkHealth()) {
            wipeDatabase();
            initializeCache();
            populateDatabase();
        }
    }

    /** Initializes the SQLite database with:
     *      Lectures table - contains all lectures
     *      Students table - contains all students
     *      Attends table - contains all (student, lecture) pairs where a the student attends the lecture
     */
    private void initializeCache() {
        try(Connection c = DriverManager.getConnection(DB_URL); Statement stmt = c.createStatement()) {
            String sql = "CREATE TABLE LECTURES " +
                    "(ID INT PRIMARY KEY     NOT NULL," +
                    " TITLE          TEXT    NOT NULL, " +
                    " CHAIR          TEXT, " +
                    " ECTS           INT, " +
                    " SEMESTER       TEXT, " +
                    " TIME           TEXT, " +
                    " PLACE          TEXT, " +
                    " LECTURER       TEXT, " +
                    " GRADE_FACTOR   FLOAT " +
                    ");";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE STUDENTS " +
                    "(ID   INT PRIMARY KEY NOT NULL," +
                    " NAME TEXT NOT NULL," +
                    " MAJOR TEXT NOT NULL, " +
                    " MINOR TEXT" +
                    ");";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE ATTENDS " +
                    "(STUDENT_ID INTEGER REFERENCES STUDENTS ON DELETE CASCADE," +
                    " LECTURE_ID INTEGER REFERENCES LECTURES ON DELETE CASCADE," +
                    " PRIMARY KEY (STUDENT_ID, LECTURE_ID));";
            stmt.executeUpdate(sql);

            sql = "CREATE TABLE CHAIRS " +
                    "(NAME TEXT PRIMARY KEY NOT NULL," +
                    " MAJOR TEXT NOT NULL, " +
                    " MINOR TEXT" +
                    ");";
            stmt.executeUpdate(sql);

        } catch (SQLException e) {
            System.out.println("Error executing query " + e.toString());
            System.exit(-1);
        }
        System.out.println("Database created successfully");
    }

    private void populateDatabase() {
        try(Connection c = DriverManager.getConnection(DB_URL);
            PreparedStatement stmtLectures = c.prepareStatement("insert into Lectures values(?, ?, ?, ?, ?, ?, ?, ?, ?)");
            PreparedStatement stmtStudents = c.prepareStatement("insert into Students values(?, ?, ?, ?)");
            PreparedStatement stmtAttends = c.prepareStatement("insert into Attends values(?, ?)");
            PreparedStatement stmtChairs = c.prepareStatement("insert into Chairs values(?, ?, ?)")) {
            BackendAdapter.fillLecturesTable(stmtLectures);
            BackendAdapter.fillStudentsTable(stmtStudents);
            BackendAdapter.fillAttendsTable(stmtAttends);
            BackendAdapter.fillChairsTable(stmtChairs);
        } catch (SQLException e) {
            System.out.println("Error importing records " + e.toString());
        }
    }

    /** Checks whether the local database is corrupted by checking
     *  whether all the tables exist
     *  @return true or false
     */
    private boolean checkHealth() {
        try {
            executeStatement("SELECT * FROM LECTURES");
            executeStatement("SELECT * FROM STUDENTS");
            executeStatement("SELECT * FROM ATTENDS");
            executeStatement("SELECT * FROM CHAIRS");
        } catch (SQLException e) {
            System.out.println("Database corrupted: " + e.toString());
            return false;
        }
        return true;
    }

    /** Removes all data from the database.
     *  Use with caution!
     */
    private void wipeDatabase() {
        System.out.println("Cleaning database");
        try {
            executeStatement("SELECT * FROM STUDENTS");
            executeStatement("DROP TABLE STUDENTS");
            System.out.println("STUDENTS table is deleted");
        } catch(SQLException e) {
            System.out.println("STUDENTS table is missing");
        }
        try {
            executeStatement("SELECT * FROM LECTURES");
            executeStatement("DROP TABLE LECTURES");
            System.out.println("LECTURES table is deleted");
        } catch(SQLException e) {
            System.out.println("LECTURES table is missing");
        }
        try {
            executeStatement("SELECT * FROM ATTENDS");
            executeStatement("DROP TABLE ATTENDS");
            System.out.println("ATTENDS table is deleted");
        } catch(SQLException e) {
            System.out.println("ATTENDS table is missing");
        }
        try {
            executeStatement("SELECT * FROM CHAIRS");
            executeStatement("DROP TABLE CHAIRS");
            System.out.println("CHAIRS table is deleted");
        } catch(SQLException e) {
            System.out.println("CHAIRS table is missing");
        }
    }


    /** Executes a query split into three parts on the local database:
     * @param select - Select part of a query0
     * @param from - From part of a query
     * @param where - Where part of a query
     * @return 2D Array containing all the information
     * @throws SQLException
     */
    public String[][] executeQuery(String[] select, String[] from, String[] where) throws SQLException {
        try(Connection c = DriverManager.getConnection(DB_URL); Statement stmt = c.createStatement()) {
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT");
            for(int i = 0; i <= select.length - 1; i++) {
                builder.append(" " + select[i]);
                if(i < select.length - 1) {
                    builder.append(",");
                }
            }
            builder.append("\n");
            builder.append("FROM");
            for(int i = 0; i <= from.length - 1; i++) {
                builder.append(" " + from[i]);
                if(i < from.length - 1) {
                    builder.append(",");
                }
            }
            if(where.length > 0) {
                builder.append("\n");
                builder.append("WHERE");
                for (int i = 0; i <= where.length - 1; i++) {
                    builder.append(" " + where[i]);
                    if (i < where.length - 1) {
                        builder.append("and");
                    }
                }
            }
            System.out.println(builder.toString());
            ResultSet resultSet = stmt.executeQuery(builder.toString());
            int columnCount = resultSet.getMetaData().getColumnCount();
            ArrayList<String[]> resultList = new ArrayList<>();
            while(resultSet.next()) {
                String[] row = new String[columnCount];
                for(int i = 0; i < columnCount; i++) {
                    row[i] = resultSet.getString(i + 1);
                }
                resultList.add(row);
            }
            String[][] resultArray = new String[resultList.size()][];
            resultList.toArray(resultArray);
            return resultArray;
        }
    }

    private void executeStatement(String query) throws SQLException{
        try(Connection c = DriverManager.getConnection(DB_URL); Statement stmt = c.createStatement()) {
            stmt.execute(query);
        }
    }

    public static void main(String[] args) {
        SQLiteManager manager = new SQLiteManager();
        try {
            String[][] resultset = manager.executeQuery(new String[]{"*"}, new String[]{"STUDENTS"}, new String[]{"ID > 1000"});
            for(String[] s: resultset) {
                System.out.println(Arrays.toString(s));
            }
        } catch(SQLException e) {
            System.out.println("Error " + e.toString());
        }
    }
}
