package university;

import university.*;
import org.junit.*;
import static org.junit.Assert.*;

public class LectureTest {

    private Lecture firstLecture;
    private Lecture secondLecture;
    private Lecture thirdLecture;

    @Before
    public void setUp() throws Exception {
        firstLecture = new Lecture(1,"EIDI1","Computer Science",5);
        secondLecture = new Lecture(1,"EIDI1","Computer Science",5);
        thirdLecture = new Lecture(2,"SD","Electro Engineering",5);
        assertNotNull("First lecture could not be created",firstLecture);
        assertNotNull("Second lecture could not be created",secondLecture);
        assertNotNull("Third lecture could not be created",thirdLecture);
    }

    @Test
    public void testEquality() {
        assertEquals("First lecture and second lecture are not equal, but should be", firstLecture, secondLecture);
        assertEquals("Second lecture and first lecture are not equal, but should be", secondLecture, firstLecture);
    }

    @Test
    public void testNonEquality() {
        assertNotEquals("First lecture and third lecture are equal, but should not be", firstLecture, thirdLecture);
        assertNotEquals("Third lecture and first lecture are equal, but should not be", thirdLecture, firstLecture);
        assertNotEquals("Second lecture and third lecture are equal, but should not be", secondLecture, thirdLecture);
        assertNotEquals("Third lecture and second lecture are equal, but should not be", thirdLecture, secondLecture);
    }

    @Test
    public void testHash() {
        assertEquals("First lecture hash code not correct, but should be",32,firstLecture.hashCode());
        assertEquals("Second lecture hash code not correct, but should be",32,secondLecture.hashCode());
        assertEquals("Third lecture hash code not correct, but should be",33,thirdLecture.hashCode());
    }

    @Test
    public void testString() {
        assertEquals(
                "First lecture string not correct, but should be",
                "Lecture{id=1, name='EIDI1\', chair='Computer Science\', credits=5}",
                firstLecture.toString());
        assertEquals(
                "Second lecture string not correct, but should be",
                "Lecture{id=1, name='EIDI1\', chair='Computer Science\', credits=5}",
                secondLecture.toString());
        assertEquals(
                "Third lecture string not correct, but should be",
                "Lecture{id=2, name='SD\', chair='Electro Engineering\', credits=5}",
                thirdLecture.toString());
    }

    @After
    public void tearDown() throws Exception {
    }
}
