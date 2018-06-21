package university;

import java.util.Objects;

public class Student {

    private int id;
    private String name;
    private String password;
    private String major;
    private String minor;

    public Student(int id, String name, String major, String minor) {
        this(id, name, "", major, minor);
    }

    public Student(int id, String name, String password, String major, String minor) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.major = major;
        this.minor = minor;
    }

    //<editor-fold desc="Getters">
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {return password; }

    public String getMajor() {
        return major;
    }

    public String getMinor() {
        return minor;
    }
    //</editor-fold>

    //<editor-fold desc="Standard Methods">
    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", major='" + major + '\'' +
                ", minor='" + minor + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return getId() == student.getId();
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }
    //</editor-fold>
}
