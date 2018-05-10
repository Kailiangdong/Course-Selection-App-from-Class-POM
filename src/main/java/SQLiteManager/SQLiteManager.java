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
     *  TODO: If the cache is corrupted it wipes it
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
        if (Files.notExists(path)) {
            initializeCache();
        }
    }

    /** Initializes the SQLite database with:
     *      Lectures table - contains all lectures
     *      Students table - contains all students
     *      Attends table - contains all (student, lecture) pairs where a the student attends the lecture
     */
    private static void initializeCache() {
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

    public void executeQuery(String query) {
        try(Connection c = DriverManager.getConnection(DB_URL); Statement stmt = c.createStatement()) {
            stmt.execute(query);
        } catch (SQLException e) {
            System.out.println("Invalid query " + e.toString());
        }
    }

    public static void main(String[] args) {
        SQLiteManager manager = new SQLiteManager();
    }//end main
}//end JDBCExample
