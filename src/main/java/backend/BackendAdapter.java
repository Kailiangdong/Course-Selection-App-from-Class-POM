package main.java.backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class BackendAdapter {

  public static final String STUDENTS_FILE = "data/students.csv";
  public static final String LECTURES_FILE = "data/lectures.csv";
  public static final String ATTENDS_FILE = "data/attends.csv";

  /**
   * Fills the table 'STUDENTS' with the master data. Make sure to truncate table before calling
   * this method, otherwise the table may be corrupted afterwards.
   *
   * The given insertStmt is executed once for each available row in the 'STUDENTS' table.
   *
   * @param insertStmt SQL statement object for "insert into STUDENTS values(?, ?, ?, ?)" (4
   * parameters)
   * @author Robert zur Bonsen
   */
  public static void fillStudentsTable(PreparedStatement insertStmt) throws SQLException {
    try {
      ArrayList<String[]> table = readCSVFile(STUDENTS_FILE);

      for (String[] row : table) {

        int id = Integer.parseInt(row[0]);
        String name = row[1];
        String major = row[2];
        String minor = row[3];

        insertStmt.clearParameters();
        insertStmt.setInt(1, id);
        insertStmt.setString(2, name);
        insertStmt.setString(3, major);
        insertStmt.setString(4, minor);
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
   *
   * The given insertStmt is executed once for each available row in the 'LECTURES' table.
   *
   * @param insertStmt SQL statement object for "insert into LECTURES values(?, ?, ?, ?, ?, ?, ?, ?,
   * ?)" (9 parameters)
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
        String place = row[6];
        String lecturer = row[7];
        float gradeFactor = Float.parseFloat(row[8].replace(',', '.'));

        insertStmt.clearParameters();
        insertStmt.setInt(1, id);
        insertStmt.setString(2, title);
        insertStmt.setString(3, chair);
        insertStmt.setInt(4, ects);
        insertStmt.setString(5, semester);
        insertStmt.setString(6, time);
        insertStmt.setString(7, place);
        insertStmt.setString(8, lecturer);
        insertStmt.setFloat(9, gradeFactor);
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
   *
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

  /**
   * Reads data from a ';'-delimited .csv file
   *
   * @author Robert zur Bonsen
   */
  private static ArrayList<String[]> readCSVFile(String fileName) throws FileNotFoundException {

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
