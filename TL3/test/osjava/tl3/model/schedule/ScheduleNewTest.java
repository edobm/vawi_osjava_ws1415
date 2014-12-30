package osjava.tl3.model.schedule;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import osjava.tl3.logic.io.input.CourseReader;
import osjava.tl3.logic.io.input.RoomReader;
import osjava.tl3.logic.io.input.StudyProgramReader;
import osjava.tl3.model.Academic;
import osjava.tl3.model.Course;
import osjava.tl3.model.Day;
import osjava.tl3.model.Room;
import osjava.tl3.model.RoomType;
import osjava.tl3.model.Semester;
import osjava.tl3.model.StudyProgram;
import osjava.tl3.model.TimeSlot;
import osjava.tl3.model.controller.DataController;

/**
 *
 * @author meikelbode
 */
public class ScheduleNewTest {

    private DataController dataController;

    public ScheduleNewTest() {
    }

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        dataController = new DataController();

        RoomReader roomReader = new RoomReader();
        roomReader.readRooms("/Users/meikelbode/Desktop/inputfiles/raeume.csv", dataController);

        CourseReader courseReader = new CourseReader();
        courseReader.readCourses("/Users/meikelbode/Desktop/inputfiles/lehrveranstaltungen.csv", dataController);

        StudyProgramReader studyProgramReader = new StudyProgramReader();
        studyProgramReader.readStudyPrograms("/Users/meikelbode/Desktop/inputfiles/studiengang_bwlba.csv", dataController);
        studyProgramReader.readStudyPrograms("/Users/meikelbode/Desktop/inputfiles/studiengang_drachenba.csv", dataController);
        studyProgramReader.readStudyPrograms("/Users/meikelbode/Desktop/inputfiles/studiengang_mumama.csv", dataController);
        studyProgramReader.readStudyPrograms("/Users/meikelbode/Desktop/inputfiles/studiengang_physikba.csv", dataController);
        studyProgramReader.readStudyPrograms("/Users/meikelbode/Desktop/inputfiles/studiengang_seba.csv", dataController);
        studyProgramReader.readStudyPrograms("/Users/meikelbode/Desktop/inputfiles/studiengang_wiba.csv", dataController);
        studyProgramReader.readStudyPrograms("/Users/meikelbode/Desktop/inputfiles/studiengang_wiingba.csv", dataController);
        studyProgramReader.readStudyPrograms("/Users/meikelbode/Desktop/inputfiles/studiengang_wiwila.csv", dataController);

    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCreation() {
        System.out.println("new Instance");
        ScheduleNew instance = new ScheduleNew();
        
        assertEquals(25, instance.getScheduleElements().size());
    }

    /**
     * Test of getScheduleElement method, of class ScheduleNew.
     */
    @Test
    public void testGetScheduleElement() {
        System.out.println("getScheduleElement");

        ScheduleNew instance = new ScheduleNew();
        ScheduleCoordinate coordinate = new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800);
        ScheduleElementNew expResult = new ScheduleElementNew(coordinate);
        ScheduleElementNew result = instance.getScheduleElement(coordinate);

        assertEquals(expResult.getCoordiate(), result.getCoordiate());

    }

    /**
     * Test of getScheduleElements method, of class ScheduleNew.
     */
    @Test
    public void testGetScheduleElements() {
        System.out.println("getScheduleElements");
        ScheduleNew instance = new ScheduleNew();

        List<ScheduleElementNew> result = instance.getScheduleElements();
        assertEquals(25, result.size());

    }

    /**
     * Test of getFreeCoordiates method, of class ScheduleNew.
     */
    @Test
    public void testGetFreeCoordiates_List_Course() {
        System.out.println("getFreeCoordiates");

        List<StudyProgram> studyPrograms = new ArrayList<>();
        studyPrograms.add(dataController.getStudyProgramByName("BWL Bachelor"));
        Course course = dataController.getCourseByID("1"); // Mathematik 1 bei Frey
        Room room = dataController.getRoomByName("Audimax");
        ScheduleCoordinate coordinate = new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800);

        ScheduleNew instance = new ScheduleNew();

        List<ScheduleCoordinate> result = instance.getFreeCoordiates(studyPrograms, course);
        assertEquals(25, result.size());

        try {
            instance.createAppointment(coordinate, room, course);
            result = instance.getFreeCoordiates(studyPrograms, course);
            assertEquals(24, result.size());

        } catch (SchedulingException ex) {
            fail("Exception aufgetreten: " + ex);
        }

    }

    /**
     * Test of getFreeCoordiates method, of class ScheduleNew.
     */
    @Test
    public void testGetFreeCoordiates_Academic() {
        System.out.println("getFreeCoordiates");

        Academic academic = dataController.getAcademicByName("Frey");
        Course course = dataController.getCourseByID("1"); // Mathematik 1 bei Frey
        Room room = dataController.getRoomByName("Audimax");
        ScheduleCoordinate coordinate = new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800);

        ScheduleNew instance = new ScheduleNew();

        List<ScheduleCoordinate> result = instance.getFreeCoordiates(academic);
        assertEquals(25, result.size());

        try {
            instance.createAppointment(coordinate, room, course);
            result = instance.getFreeCoordiates(academic);
            assertEquals(24, result.size());

        } catch (SchedulingException ex) {
            fail("Exception aufgetreten: " + ex);
        }
    }

    /**
     * Test of getFreeCoordiates method, of class ScheduleNew.
     */
    @Test
    public void testGetFreeCoordiates_Room() {
        System.out.println("getFreeCoordiates");

        Course course = dataController.getCourseByID("1"); // Mathematik 1 bei Frey
        Room room = dataController.getRoomByName("Audimax");
        ScheduleCoordinate coordinate = new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800);

        ScheduleNew instance = new ScheduleNew();

        List<ScheduleCoordinate> result = instance.getFreeCoordiates(room);
        assertEquals(25, result.size());

        try {
            instance.createAppointment(coordinate, room, course);
            result = instance.getFreeCoordiates(room);
            assertEquals(24, result.size());

        } catch (SchedulingException ex) {
            fail("Exception aufgetreten: " + ex);
        }
    }

    /**
     * Test of createAppointment method, of class ScheduleNew.
     */
    @Test
    public void testCreateAppointment() throws Exception {
        System.out.println("createAppointment");

        ScheduleNew instance = new ScheduleNew();

        Course course = dataController.getCourseByID("1"); // Mathematik 1 bei Frey
        Room room = dataController.getRoomByName("Audimax");
        ScheduleCoordinate coordinate = new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800);

        ScheduleElementNew element = instance.getScheduleElement(coordinate);
        assertNotNull(element);
        assertTrue(element.isEmpty());

        try {
            instance.createAppointment(coordinate, room, course);
            element = instance.getScheduleElement(coordinate);
            assertFalse(element.isEmpty());

        } catch (SchedulingException ex) {
            fail("Exception aufgetreten: " + ex);
        }
    }

    /**
     * Test of getAppointmentCount method, of class ScheduleNew.
     */
    @Test
    public void testGetAppointmentCount() {
        System.out.println("getAppointmentCount");

        ScheduleNew instance = new ScheduleNew();

        Course course = dataController.getCourseByID("1"); // Mathematik 1 bei Frey
        Room room = dataController.getRoomByName("Audimax");
        ScheduleCoordinate coordinate = new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800);

        ScheduleElementNew element = instance.getScheduleElement(coordinate);

        assertEquals(0, instance.getAppointmentCount());

        try {
            instance.createAppointment(coordinate, room, course);
            assertEquals(1, instance.getAppointmentCount());

        } catch (SchedulingException ex) {
            fail("Exception aufgetreten: " + ex);
        }
    }

    /**
     * Test of getStudentsCount method, of class ScheduleNew.
     */
    @Test
    public void testGetStudentsCount() {
        System.out.println("getStudentsCount");

        ScheduleNew instance = new ScheduleNew();

        Course course1 = dataController.getCourseByID("1"); // Mathematik 1 bei Frey
        Course course2 = dataController.getCourseByID("2"); // Mathematik 1 bei Schneider
        Room room = dataController.getRoomByName("Audimax");
        Room externalRoom = dataController.createExternalRoom();

        ScheduleCoordinate coordinate = new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800);

        ScheduleElementNew element = instance.getScheduleElement(coordinate);

        assertEquals(0, instance.getStudentsCount(RoomType.INTERNAL));
        assertEquals(0, instance.getStudentsCount(RoomType.EXTERNAL));

        try {
            instance.createAppointment(coordinate, room, course1);
            instance.createAppointment(coordinate, externalRoom, course2);

            assertEquals(800, instance.getStudentsCount(RoomType.INTERNAL));
            assertEquals(100, instance.getStudentsCount(RoomType.EXTERNAL));

        } catch (SchedulingException ex) {
            fail("Exception aufgetreten: " + ex);
        }
    }

    /**
     * Test of getRoomViews method, of class ScheduleNew.
     */
    @Test
    public void testGetRoomViews() {
        System.out.println("getRoomViews");
        List<Room> rooms = dataController.getRooms();

        ScheduleNew instance = new ScheduleNew();

        List<ScheduleView> result = instance.getRoomViews(rooms);

        assertEquals(rooms.size(), result.size());

    }

    /**
     * Test of getAcademicScheduleViews method, of class ScheduleNew.
     */
    @Test
    public void testGetAcademicScheduleVies() {
        System.out.println("getAcademicScheduleVies");

        List<Academic> academics = dataController.getAcademics();

        ScheduleNew instance = new ScheduleNew();

        List<ScheduleView> result = instance.getAcademicScheduleViews(academics);

        assertEquals(academics.size(), result.size());
    }

    /**
     * Test of getStudyProgramScheduleViews method, of class ScheduleNew.
     */
    @Test
    public void testGetStudyProgramScheduleViews() {
        System.out.println("getStudyProgramScheduleViews");

        List<StudyProgram> studyPrograms = dataController.getStudyPrograms();

        ScheduleNew instance = new ScheduleNew();

        List<ScheduleView> result = instance.getStudyProgramScheduleViews(studyPrograms);

        assertEquals(studyPrograms.size(), result.size());
    }

    /**
     * Test of getSemesterScheduleViews method, of class ScheduleNew.
     */
    @Test
    public void testGetSemesterScheduleViews() {
        System.out.println("getSemesterScheduleViews");
        List<Semester> semesters = dataController.getStudyProgramByName("BWL Bachelor").getSemesters();

        ScheduleNew instance = new ScheduleNew();

        List<ScheduleView> result = instance.getSemesterScheduleViews(semesters);

        assertEquals(semesters.size(), result.size());
    }

    /**
     * Test of getAllScheduleViews method, of class ScheduleNew.
     */
    @Test
    public void testGetAllScheduleViews() {
        System.out.println("getAllScheduleViews");
        List<Room> rooms = dataController.getRooms();
        List<Academic> academics = dataController.getAcademics();
        List<StudyProgram> studyPrograms = dataController.getStudyPrograms();
        int semesterCount = 0;

        for (StudyProgram studyProgram : studyPrograms) {
            semesterCount += studyProgram.getSemesters().size();
        }

        ScheduleNew instance = new ScheduleNew();

        List<ScheduleView> result = instance.getAllScheduleViews(rooms, academics, studyPrograms);
        long expResult = rooms.size() + academics.size() + studyPrograms.size() + semesterCount;
        assertEquals(expResult, result.size());
    }

}
