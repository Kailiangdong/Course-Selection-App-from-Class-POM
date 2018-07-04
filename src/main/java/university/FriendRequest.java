package university;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class FriendRequest {

    private Student requestTo;
    private Student requestFrom;
    private String date;
    private String time;

    public FriendRequest(Student requestTo, Student requestFrom) {
        this.requestTo = requestTo;
        this.requestFrom = requestFrom;
        ZonedDateTime timestamp = ZonedDateTime.now();
        this.date = DateTimeFormatter.ofPattern("dd-MM-yy").format(timestamp);
        this.time = DateTimeFormatter.ofPattern("hh:mm").format(timestamp);
    }

    public FriendRequest(Student requestTo, Student requestFrom, String date, String time) {
        this.requestTo = requestTo;
        this.requestFrom = requestFrom;
        this.date = date;
        this.time = time;
    }

    //<editor-fold desc="Getters">
    public Student getStudent1() {
        return requestTo;
    }

    public Student getStudent2() {
        return requestFrom;
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
        String s = "" + requestFrom.getName() +
                "                            "
                + date + "  -  " + time;

        return s;
    }

}
