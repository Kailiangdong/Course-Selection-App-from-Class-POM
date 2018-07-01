package university;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class FriendRequest {

    private Student student1;
    private Student student2;
    private String date;
    private String time;

    public FriendRequest(Student student1, Student student2) {
        this.student1 = student1;
        this.student2 = student2;
        ZonedDateTime timestamp = ZonedDateTime.now();
        this.date = DateTimeFormatter.ofPattern("dd-MM-yy").format(timestamp);
        this.time = DateTimeFormatter.ofPattern("hh:mm").format(timestamp);
    }

    public FriendRequest(Student student1, Student student2, String date, String time) {
        this.student1 = student1;
        this.student2 = student2;
        this.date = date;
        this.time = time;
    }

    //<editor-fold desc="Getters">
    public Student getStudent1() {
        return student1;
    }

    public Student getStudent2() {
        return student2;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public boolean getRequest () {
        return false;
    }

    @Override
    public String toString() {
        String s = "" + student1.getName() +
                "       " +
                "" + date + "  -  " + time;

        return s;
    }

    public String toString(FriendRequest[] array) {
        String s = "";
        for(int i = 0; i < array.length; i++) {
            s += array[i].toString();
            s += "\n";
        }
        return s;
    }
}
