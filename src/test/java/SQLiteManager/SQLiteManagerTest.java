package SQLiteManager;

import university.*;
import SQLiteManager.*;
import org.junit.*;
import static org.junit.Assert.*;


public class SQLiteManagerTest {

    SQLiteManager sqLiteManager;

    @Before
    public void setUp() throws Exception {
        sqLiteManager = new SQLiteManager();
    }

    @Test
    public void testStudent() {
        Student student = sqLiteManager.getStudent(971);
        assertEquals("Returned Student not correct, but should be",971,student.getId());
    }

    @Test
    public void testLecture() {
        Lecture lecture = sqLiteManager.getLecture(10);
        assertEquals("Returned Lecture not correct, but should be",10,lecture.getId());
    }

    @After
    public void tearDown() throws Exception {
    }
}
