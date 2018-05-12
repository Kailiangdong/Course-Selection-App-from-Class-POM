package main.java.SQLiteManager;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

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

            // TODO A table which determines to which chair a major belongs to
        } catch (SQLException e) {
            System.out.println("Error executing query " + e.toString());
            System.exit(-1);
        }
        System.out.println("Database created successfully");
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
    }

    public ResultSet executeQuery(String query) throws SQLException{
        try(Connection c = DriverManager.getConnection(DB_URL); Statement stmt = c.createStatement()) {
            return stmt.executeQuery(query);
        }
    }

    private void executeStatement(String query) throws SQLException{
        try(Connection c = DriverManager.getConnection(DB_URL); Statement stmt = c.createStatement()) {
            stmt.execute(query);
        }
    }

    public static void main(String[] args) {
        SQLiteManager manager = new SQLiteManager();
    }//end main
}//end JDBCExample
