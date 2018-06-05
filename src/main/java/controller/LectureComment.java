package controller;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class LectureComment {
    String author;
    String text;
    ZonedDateTime time;
    boolean answer;

    public LectureComment(String author, String text, ZonedDateTime time, boolean answer) {
        this.author = author;
        this.text = text;
        this.time = time;
        this.answer = answer;
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public String getTime() {
        return DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mm").format(time);
    }

    public boolean isAnswer() {
        return this.answer;
    }

}