package university;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class LectureComment {

    private int id;
    private Student author;
    private String text;
    private String date;
    private String time;
    private int likes;

    /**
     * For locally created objects (not retrieved from DB)
     * @param author
     * @param text
     */
    public LectureComment(Student author, String text) {
        this.author = author;
        this.text = text;
        ZonedDateTime timestamp = ZonedDateTime.now();
        this.date = DateTimeFormatter.ofPattern("dd-MM-yy").format(timestamp);
        this.time = DateTimeFormatter.ofPattern("hh:mm").format(timestamp);
        this.id = -1;
        this.likes = -1;
    }

    public LectureComment(Student author, String text, String date, String time, int id, int likes) {
        this.author = author;
        this.text = text;
        this.date = date;
        this.time = time;
        this.id = id;
        this.likes = likes;
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

    public int getLikes() { return likes; }

    /**
     * For future use
     * @return
     */
    public boolean getAnswer() {
        return false;
    }
    //</editor-fold>

}