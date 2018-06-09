package university;

import java.util.Objects;

public class Lecture {

    private int id;
    private String name;
    private String chair;
    private int credits;

    public Lecture(int id, String name, String chair, int credits) {
        this.id = id;
        this.name = name;
        this.chair = chair;
        this.credits = credits;
    }

    //<editor-fold desc="Getters">
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getChair() {
        return chair;
    }

    public int getCredits() {
        return credits;
    }
    //</editor-fold>

    //<editor-fold desc="Standard Methods">
    @Override
    public String toString() {
        return "Lecture{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", chair='" + chair + '\'' +
                ", credits=" + credits +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lecture lecture = (Lecture) o;
        return getId() == lecture.getId();
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }
    //</editor-fold>

}
