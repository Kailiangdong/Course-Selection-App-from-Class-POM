package backend;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class BackendAdapter {

    public static final String STUDENTS_FILE = "data/students.csv";
    public static final String LECTURES_FILE = "data/lectures.csv";
    public static final String ATTENDS_FILE = "data/attends.csv";
    public static final String CHAIRS_FILE = "data/chairs.csv";
    public static final String COMMENTS_FILE = "data/comments.csv";
    public static final String FRIENDS_WITH_FILE = "data/friendswith.csv";
    public static final String LIKES_FILE = "data/likes.csv";
    public static final String REQUEST_FRIENDS_FILE = "data/requestfriends.csv";

    /**
     * Fills the table 'STUDENTS' with the master data. Make sure to truncate table before calling
     * this method, otherwise the table may be corrupted afterwards.
     * <p>
     * The given insertStmt is executed once for each available row in the 'STUDENTS' table.
     *
     * @param insertStmt SQL statement object for "insert into STUDENTS values(?, ?, ?, ?)" (4
     *                   parameters)
     * @author Robert zur Bonsen
     */
    public static void fillStudentsTable(PreparedStatement insertStmt) throws SQLException {
        try {
            ArrayList<String[]> table = readCSVFile(STUDENTS_FILE);

            for (String[] row : table) {

                int id = Integer.parseInt(row[0]);
                String name = row[1];
                String password = row[2];
                String major = row[3];
                String minor = row[4];

                insertStmt.clearParameters();
                insertStmt.setInt(1, id);
                insertStmt.setString(2, name);
                insertStmt.setString(3, password);
                insertStmt.setString(4, major);
                insertStmt.setString(5, minor);
                insertStmt.executeUpdate();

            }
        } catch (FileNotFoundException e) {
            System.out.println("File with 'STUDENTS' data was not found: " + STUDENTS_FILE);
        } catch (RuntimeException e) {
            System.out.println("Error while inserting 'STUDENTS' data.");
        }
    }

    /**
     * Fills the table 'LECTURES' with the master data. Make sure to truncate table before callin this
     * method, otherwise the table may be corrupted afterwards.
     * <p>
     * The given insertStmt is executed once for each available row in the 'LECTURES' table.
     *
     * @param insertStmt SQL statement object for "insert into LECTURES values(?, ?, ?, ?, ?, ?, ?, ?,
     *                   ?)" (9 parameters)
     * @author Robert zur Bonsen
     */
    public static void fillLecturesTable(PreparedStatement insertStmt) throws SQLException {
        try {
            ArrayList<String[]> table = readCSVFile(LECTURES_FILE);

            for (String[] row : table) {

                int id = Integer.parseInt(row[0]);
                String title = row[1];
                String chair = row[2];
                int ects = Integer.parseInt(row[3]);
                String semester = row[4];
                String time = row[5];
                String roomnumber = row[6];
                String buildingnumber = row[7];
                String lecturer = row[8];
                float gradeFactor = Float.parseFloat(row[9].replace(',', '.'));

                insertStmt.clearParameters();
                insertStmt.setInt(1, id);
                insertStmt.setString(2, title);
                insertStmt.setString(3, chair);
                insertStmt.setInt(4, ects);
                insertStmt.setString(5, semester);
                insertStmt.setString(6, time);
                insertStmt.setString(7, roomnumber);
                insertStmt.setString(8, buildingnumber);
                insertStmt.setString(9, lecturer);
                insertStmt.setFloat(10, gradeFactor);
                insertStmt.executeUpdate();

            }
        } catch (FileNotFoundException e) {
            System.out.println("File with 'LECTURES' data was not found: " + LECTURES_FILE);
        } catch (RuntimeException e) {
            System.out.println("Error while inserting 'LECTURES' data.");
        }
    }

    /**
     * Fills the table 'ATTENDS' with the master data. Make sure to truncate table before calling this
     * method, otherwise the table may be corrupted afterwards.
     * <p>
     * The given insertStmt is executed once for each available row in the 'ATTENDS' table.
     *
     * @param insertStmt SQL statement object for "insert into STUDENTS values(?, ?)" (2 parameters)
     * @author Robert zur Bonsen
     */
    public static void fillAttendsTable(PreparedStatement insertStmt) throws SQLException {
        try {
            ArrayList<String[]> table = readCSVFile(ATTENDS_FILE);

            for (String[] row : table) {

                int studentID = Integer.parseInt(row[0]);
                int lectureID = Integer.parseInt(row[1]);

                insertStmt.clearParameters();
                insertStmt.setInt(1, studentID);
                insertStmt.setInt(2, lectureID);
                insertStmt.executeUpdate();
            }
        } catch (FileNotFoundException e) {
            System.out.println("File with 'ATTENDS' data was not found: " + ATTENDS_FILE);
        } catch (RuntimeException e) {
            System.out.println("Error while inserting 'ATTENDS' data.");
        }
    }

    public static void fillFriendsWithTable(PreparedStatement insertStmt) throws SQLException {
        try {
            ArrayList<String[]> table = readCSVFile(FRIENDS_WITH_FILE);

            for (String[] row : table) {

                int studentID1 = Integer.parseInt(row[0]);
                int studentID2 = Integer.parseInt(row[1]);

                insertStmt.clearParameters();
                insertStmt.setInt(1, studentID1);
                insertStmt.setInt(2, studentID2);
                insertStmt.executeUpdate();
            }
        } catch (FileNotFoundException e) {
            System.out.println("File with 'FRIENDSWITH' data was not found: " + FRIENDS_WITH_FILE);
        } catch (RuntimeException e) {
            System.out.println("Error while inserting 'FRIENDSWITH' data.");
        }
    }

    public static void fillLikesTable(PreparedStatement insertStmt) throws SQLException {
        try {
            ArrayList<String[]> table = readCSVFile(LIKES_FILE);

            for (String[] row : table) {

                int studentID = Integer.parseInt(row[0]);
                int commentID = Integer.parseInt(row[1]);

                insertStmt.clearParameters();
                insertStmt.setInt(1, studentID);
                insertStmt.setInt(2, commentID);
                insertStmt.executeUpdate();
            }
        } catch (FileNotFoundException e) {
            System.out.println("File with 'LIKES' data was not found: " + LIKES_FILE);
        } catch (RuntimeException e) {
            System.out.println("Error while inserting 'LIKES' data.");
        }
    }

    public static void fillRequestFriendsTable(PreparedStatement insertStmt) throws SQLException {
        try {
            ArrayList<String[]> table = readCSVFile(REQUEST_FRIENDS_FILE);

            for (String[] row : table) {

                int requestTo = Integer.parseInt(row[0]);
                int requestFrom = Integer.parseInt(row[1]);
                String time = row[2];
                String date = row[3];

                insertStmt.clearParameters();
                insertStmt.setInt(1, requestTo);
                insertStmt.setInt(2, requestFrom);
                insertStmt.setString(3, time);
                insertStmt.setString(4, date);
                insertStmt.executeUpdate();
            }
        } catch (FileNotFoundException e) {
            System.out.println("File with 'REQUESTFRIENDS' data was not found: " + REQUEST_FRIENDS_FILE);
        } catch (RuntimeException e) {
            System.out.println("Error while inserting 'REQUESTFRIENDS' data.");
        }
    }

    /**
     * Fills the table 'CHAIRS' with the master data. Make sure to truncate table before calling this
     * method, otherwise the table may be corrupted afterwards.
     * <p>
     * The given insertStmt is executed once for each available row in the 'CHAIRS' table.
     *
     * @param insertStmt SQL statement object for "insert into STUDENTS values(?, ?, ?)" (3
     *                   parameters)
     * @author Robert zur Bonsen
     */
    public static void fillChairsTable(PreparedStatement insertStmt) throws SQLException {
        try {
            ArrayList<String[]> table = readCSVFile(CHAIRS_FILE);

            for (String[] row : table) {

                String chair = row[0];
                String lecturer = row[1];
                String subject = row[2];

                insertStmt.clearParameters();
                insertStmt.setString(1, chair);
                insertStmt.setString(2, lecturer);
                insertStmt.setString(3, subject);
                insertStmt.executeUpdate();
            }
        } catch (FileNotFoundException e) {
            System.out.println("File with 'CHAIRS' data was not found: " + CHAIRS_FILE);
        } catch (RuntimeException e) {
            System.out.println("Error while inserting 'CHAIRS' data.");
        }
    }

    public static void fillCommentsTable(PreparedStatement insertStmt) throws SQLException {
        try {
            ArrayList<String[]> table = readCSVFile(COMMENTS_FILE);

            for (String[] row : table) {

                String ID = row[0];
                String studentID = row[1];
                String lectureID = row[2];
                String time = row[3];
                String date = row[4];
                String content = row[5];

                insertStmt.clearParameters();
                insertStmt.setString(1, ID);
                insertStmt.setString(2, studentID);
                insertStmt.setString(3, lectureID);
                insertStmt.setString(4, time);
                insertStmt.setString(5, date);
                insertStmt.setString(6, content);
                insertStmt.executeUpdate();
            }
        } catch (FileNotFoundException e) {
            System.out.println("File with 'COMMENTS' data was not found: " + COMMENTS_FILE + e.getLocalizedMessage());
        } catch (RuntimeException e) {
            System.out.println("Error while inserting 'COMMENTS' data.");
        }
    }

    /**
     * Reads data from a ';'-delimited .csv file
     *
     * @author Robert zur Bonsen
     */
    public static ArrayList<String[]> readCSVFile(String fileName) throws FileNotFoundException {

        ArrayList<String[]> table = new ArrayList();
        String delim = ";";

        try (Scanner scanner = new Scanner(new File(fileName))) {
            String[] headerLine = scanner.nextLine().split(delim);
            while (scanner.hasNext()) {
                String[] line = scanner.nextLine().split(delim);
                if (line.length != headerLine.length) {
                    System.out.println("Error parsing row: " + Arrays.toString(line));
                    throw new RuntimeException();
                }
                table.add(line);
            }
        }
        return table;
    }


}
