package Backend;

import backend.BackendAdapter;
import org.junit.*;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class BackendAdapterTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testReadCSVStudentFile() throws Exception{
        ArrayList<String[]> studentList = BackendAdapter.readCSVFile(BackendAdapter.STUDENTS_FILE);
        assertArrayEquals(
                "Loaded student list false, but should not be",
                new Object[] {"3953","Robert","robert123","Management","Computer Science"},
                studentList.get(0));
    }

    @Test
    public void testReadCSVLectureFile() throws Exception{
        ArrayList<String[]> lectureList = BackendAdapter.readCSVFile(BackendAdapter.LECTURES_FILE);
        assertArrayEquals(
                "Loaded lecture list false, but should not be",
                new Object[] {"1","Algorithmen und Datenstrukturen","Chair CS 1","5","1","\"Mo., 11:30-13:00\"","102","5620","Prof. Mustermann","1"},
                lectureList.get(0));
    }

    @After
    public void tearDown() throws Exception {
    }
}
