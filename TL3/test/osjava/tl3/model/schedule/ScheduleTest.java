package osjava.tl3.model.schedule;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import osjava.tl3.model.Academic;
import osjava.tl3.model.Course;
import osjava.tl3.model.Room;
import osjava.tl3.model.RoomType;
import osjava.tl3.model.Semester;
import osjava.tl3.model.StudyProgram;

/**
 *
 * @author meikelbode
 */
public class ScheduleTest extends ScheduleBasicTest {

    public ScheduleTest() {
    }

    @Test
    public void testCreation() {
        System.out.println("new Instance");
        Schedule instance = new Schedule();

        assertEquals(25, instance.getScheduleElements().size());
    }

    /**
     * Test of getScheduleElement method, of class Schedule.
     */
    @Test
    public void testGetScheduleElement() {
        System.out.println("getScheduleElement");

        Schedule instance = new Schedule();
        ScheduleCoordinate coordinate = new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800);
        ScheduleElementImpl expResult = new ScheduleElementImpl(coordinate);
        ScheduleElementImpl result = instance.getScheduleElement(coordinate);

        assertEquals(expResult.getCoordiate(), result.getCoordiate());

    }

    /**
     * Test of getScheduleElements method, of class Schedule.
     */
    @Test
    public void testGetScheduleElements() {
        System.out.println("getScheduleElements");
        Schedule instance = new Schedule();

        List<ScheduleElementImpl> result = instance.getScheduleElements();
        assertEquals(25, result.size());

    }

    /**
     * Test of getFreeCoordiates method, of class Schedule.
     */
    @Test
    public void testGetFreeCoordiates_List_Course() {
        System.out.println("getFreeCoordiates");

        List<StudyProgram> studyPrograms = new ArrayList<>();
        studyPrograms.add(dataController.getStudyProgramByName("BWL Bachelor"));
        Course course = dataController.getCourseByID("1"); // Mathematik 1 bei Frey
        Room room = dataController.getRoomByName("Audimax");
        ScheduleCoordinate coordinate = new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800);

        Schedule instance = new Schedule();

        List<ScheduleCoordinate> result = instance.getFreeCoordiates(studyPrograms, course);
        assertEquals(25, result.size());

            instance.createAppointment(coordinate, room, course);
            result = instance.getFreeCoordiates(studyPrograms, course);
            assertEquals(24, result.size());

      
    }

    /**
     * Test of getFreeCoordiates method, of class Schedule.
     */
    @Test
    public void testGetFreeCoordiates_Academic() {
        System.out.println("getFreeCoordiates");

        Academic academic = dataController.getAcademicByName("Frey");
        Course course = dataController.getCourseByID("1"); // Mathematik 1 bei Frey
        Room room = dataController.getRoomByName("Audimax");
        ScheduleCoordinate coordinate = new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800);

        Schedule instance = new Schedule();

        List<ScheduleCoordinate> result = instance.getFreeCoordiates(academic);
        assertEquals(25, result.size());

      
            instance.createAppointment(coordinate, room, course);
            result = instance.getFreeCoordiates(academic);
            assertEquals(24, result.size());

      
    }

    /**
     * Test of getFreeCoordiates method, of class Schedule.
     */
    @Test
    public void testGetFreeCoordiates_Room() {
        System.out.println("getFreeCoordiates");

        Course course = dataController.getCourseByID("1"); // Mathematik 1 bei Frey
        Room room = dataController.getRoomByName("Audimax");
        ScheduleCoordinate coordinate = new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800);

        Schedule instance = new Schedule();

        List<ScheduleCoordinate> result = instance.getFreeCoordiates(room);
        assertEquals(25, result.size());

      
            instance.createAppointment(coordinate, room, course);
            result = instance.getFreeCoordiates(room);
            assertEquals(24, result.size());

     
    }

    /**
     * Test of createAppointment method, of class Schedule.
     */
    @Test
    public void testCreateAppointment() throws Exception {
        System.out.println("createAppointment");

        Schedule instance = new Schedule();

        Course course = dataController.getCourseByID("1"); // Mathematik 1 bei Frey
        Room room = dataController.getRoomByName("Audimax");
        ScheduleCoordinate coordinate = new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800);

        ScheduleElementImpl element = instance.getScheduleElement(coordinate);
        assertNotNull(element);
        assertTrue(element.isEmpty());

       
            instance.createAppointment(coordinate, room, course);
            element = instance.getScheduleElement(coordinate);
            assertFalse(element.isEmpty());

    }

    /**
     * Test of getAppointmentCount method, of class Schedule.
     */
    @Test
    public void testGetAppointmentCount() throws Exception {
        System.out.println("getAppointmentCount");

        Schedule instance = new Schedule();

        Course course = dataController.getCourseByID("1"); // Mathematik 1 bei Frey
        Room room = dataController.getRoomByName("Audimax");
        ScheduleCoordinate coordinate = new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800);

        assertEquals(0, instance.getAppointmentCount());

        instance.createAppointment(coordinate, room, course);
        assertEquals(1, instance.getAppointmentCount());

    }

    /**
     * Test of getStudentsCount method, of class Schedule.
     */
    @Test
    public void testGetStudentsCount() {
        System.out.println("getStudentsCount");

        Schedule instance = new Schedule();

        Course course1 = dataController.getCourseByID("1"); // Mathematik 1 bei Frey
        Course course2 = dataController.getCourseByID("2"); // Mathematik 1 bei Schneider
        Room room = dataController.getRoomByName("Audimax");
        Room externalRoom = dataController.createExternalRoom();

        ScheduleCoordinate coordinate = new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800);

        ScheduleElementImpl element = instance.getScheduleElement(coordinate);

        assertEquals(0, instance.getStudentsCount(RoomType.INTERNAL));
        assertEquals(0, instance.getStudentsCount(RoomType.EXTERNAL));

       
            instance.createAppointment(coordinate, room, course1);
            instance.createAppointment(coordinate, externalRoom, course2);

            assertEquals(800, instance.getStudentsCount(RoomType.INTERNAL));
            assertEquals(100, instance.getStudentsCount(RoomType.EXTERNAL));

     
    }

    /**
     * Test of getRoomViews method, of class Schedule.
     */
    @Test
    public void testGetRoomViews() {
        System.out.println("getRoomViews");
        List<Room> rooms = dataController.getRooms();

        Schedule instance = new Schedule();

        List<ScheduleView> result = instance.getRoomViews(rooms);

        assertEquals(rooms.size(), result.size());

    }

    /**
     * Test of getAcademicScheduleViews method, of class Schedule.
     */
    @Test
    public void testGetAcademicScheduleViews() {
        System.out.println("getAcademicScheduleVies");

        List<Academic> academics = dataController.getAcademics();

        Schedule instance = new Schedule();

        List<ScheduleView> result = instance.getAcademicScheduleViews(academics);

        assertEquals(academics.size(), result.size());
    }

    /**
     * Test of getStudyProgramScheduleViews method, of class Schedule.
     */
    @Test
    public void testGetStudyProgramScheduleViews() {
        System.out.println("getStudyProgramScheduleViews");

        List<StudyProgram> studyPrograms = dataController.getStudyPrograms();

        Schedule instance = new Schedule();

        List<ScheduleView> result = instance.getStudyProgramScheduleViews(studyPrograms);

        assertEquals(studyPrograms.size(), result.size());
    }

    /**
     * Test of getSemesterScheduleViews method, of class Schedule.
     */
    @Test
    public void testGetSemesterScheduleViews() {
        System.out.println("getSemesterScheduleViews");
        List<Semester> semesters = dataController.getStudyProgramByName("BWL Bachelor").getSemesters();

        Schedule instance = new Schedule();

        List<ScheduleView> result = instance.getSemesterScheduleViews(semesters);

        assertEquals(semesters.size(), result.size());
    }

    /**
     * Test of getAllScheduleViews method, of class Schedule.
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

        Schedule instance = new Schedule();

        List<ScheduleView> result = instance.getAllScheduleViews(rooms, academics, studyPrograms);
        long expResult = rooms.size() + academics.size() + studyPrograms.size() + semesterCount;
        assertEquals(expResult, result.size());
    }

}
