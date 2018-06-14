package university;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class LectureComment {

    private int id;
    private Student author;
    private String text;
    String date;
    String time;

    /**
     * For locally created objects (not retrieved from DB)
     * @param author
     * @param text
     */
    public LectureComment(Student author, String text) {
        this.author = author;
        this.text = text;
        ZonedDateTime timestamp = ZonedDateTime.now();
        this.date = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(timestamp);
        this.time = DateTimeFormatter.ofPattern("hh:mm").format(timestamp);
        this.id = -1;
    }

    public LectureComment(Student author, String text, String date, String time, int id) {
        this.author = author;
        this.text = text;
        this.date = date;
        this.time = time;
        this.id = id;
    }

    //<editor-fold desc="Getters">
    public int getId() {
        return id;
    }

    public Student getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    /**
     * For future use
     * @return
     */
    public boolean getAnswer() {
        return false;
    }
    //</editor-fold>

}