package university;

import university.*;
import org.junit.*;
import static org.junit.Assert.*;

public class StudentTest {

    private Student firstStudent;
    private Student secondStudent;
    private Student thirdStudent;

    @Before
    public void setUp() throws Exception {
        firstStudent = new Student(1,"Anna","password", "Computer Science","Electro Engineering");
        secondStudent = new Student(1,"Anna","password", "Computer Science","Electro Engineering");
        thirdStudent = new Student(2,"Bernd","1234", "Electro Engineering","Economics");
        assertNotNull("First student could not be created",firstStudent);
        assertNotNull("Second student could not be created",secondStudent);
        assertNotNull("Third student could not be created",thirdStudent);
    }

    @Test
    public void testEquality() {
        assertEquals("First student and second student are not equal, but should be", firstStudent, secondStudent);
        assertEquals("Second student and first student are not equal, but should be", secondStudent, firstStudent);
    }

    @Test
    public void testNonEquality() {
        assertNotEquals("First student and third student are equal, but should not be", firstStudent, thirdStudent);
        assertNotEquals("Third student and first student are equal, but should not be", thirdStudent, firstStudent);
        assertNotEquals("Second student and third student are equal, but should not be", secondStudent, thirdStudent);
        assertNotEquals("Third student and second student are equal, but should not be", thirdStudent, secondStudent);
    }

    @Test
    public void testHash() {
        assertEquals("First student hash code not correct, but should be",32,firstStudent.hashCode());
        assertEquals("Second student hash code not correct, but should be",32,secondStudent.hashCode());
        assertEquals("Third student hash code not correct, but should be",33,thirdStudent.hashCode());
    }

    @Test
    public void testString() {
        assertEquals(
                "First student string not correct, but should be",
                "Student{id=1, name='Anna\', major='Computer Science\', minor='Electro Engineering\'}",
                firstStudent.toString());
        assertEquals(
                "Second student string not correct, but should be",
                "Student{id=1, name='Anna\', major='Computer Science\', minor='Electro Engineering\'}",
                secondStudent.toString());
        assertEquals(
                "Third student string not correct, but should be",
                "Student{id=2, name='Bernd\', major='Electro Engineering\', minor='Economics\'}",
                thirdStudent.toString());
    }

    @After
    public void tearDown() throws Exception {
    }

}
