package osjava.tl3.model;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import osjava.tl3.logic.planning.InputFileHelper;
import osjava.tl3.model.controller.DataController;

/**
 *
 * @author meikelbode
 */
public class MasterScheduleTest {

    DataController dataController;
    MasterSchedule instance;

    public MasterScheduleTest() {
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
        InputFileHelper.loadRooms(dataController);
        InputFileHelper.loadCourses(dataController);
        InputFileHelper.loadStudyPrograms(dataController);

        instance = new MasterSchedule();
        instance.initFromDataController(dataController);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of initFromDataController method, of class MasterSchedule.
     */
    @Test
    public void testInitFromDataController() {
        System.out.println("initFromDataController");

        assertEquals(10, instance.getRoomCount(RoomType.INTERNAL));

    }

    /**
     * Test of getFreeCoordinates method, of class MasterSchedule.
     */
    @Test
    public void testGetFreeCoordinates() {
        System.out.println("getFreeCoordinates");

        Schedule schedule = new Schedule(ScheduleType.ROOM_EXTERNAL);

        List<ScheduleCoordinate> expResult = instance.generateMaximumCoordinates();
        List<ScheduleCoordinate> result = instance.getFreeCoordinates(schedule);

        assertTrue(result.containsAll(expResult));
        assertTrue(result.size() == 25);
    }

    /**
     * Test of getFreeCoordiates method, of class MasterSchedule.
     */
    @Test
    public void testGetFreeCoordiates_Room() {
        System.out.println("getFreeCoordiates");

        Room room = dataController.getRoomByName("Audimax");
        List<ScheduleCoordinate> expResult = instance.generateMaximumCoordinates();
        List<ScheduleCoordinate> result = instance.getFreeCoordiates(room);
        assertTrue(result.containsAll(expResult));
        assertTrue(result.size() == 25);

        room = dataController.getRoomByName("NAT-Labor");
        result = instance.getFreeCoordiates(room);
        assertTrue(result.containsAll(expResult));
        assertTrue(result.size() == 25);
    }

    /**
     * Test of getFreeCoordiates method, of class MasterSchedule.
     */
    @Test
    public void testGetFreeCoordiates_Academic() {
        System.out.println("getFreeCoordiates");
        Academic academic = dataController.getAcademicByName("Hamann");

        List<ScheduleCoordinate> expResult = instance.generateMaximumCoordinates();
        List<ScheduleCoordinate> result = instance.getFreeCoordiates(academic);

        assertTrue(result.containsAll(expResult));
        assertTrue(result.size() == 25);
    }

    /**
     * Test of getFreeCoordiates method, of class MasterSchedule.
     */
    @Test
    public void testGetFreeCoordiates_Course() {
        System.out.println("getFreeCoordiates");
        Course course = dataController.getCourseByID("1");

        List<ScheduleCoordinate> expResult = instance.generateMaximumCoordinates();
        List<ScheduleCoordinate> result = instance.getFreeCoordiates(course);

        assertTrue(result.containsAll(expResult));
        assertTrue(result.size() == 25);
    }

    /**
     * Test of blockCoordinate method, of class MasterSchedule.
     */
    @Test
    public void testBlockCoordinate() {
        System.out.println("blockCoordinate");

        ScheduleCoordinate coordinate = new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800);
        Room room = dataController.getRoomByName("Audimax");
        Course course = dataController.getCourseByID("1");

        instance.blockCoordinate(coordinate, room, course);

        // Prüfe Raumplan
        Schedule schedule = instance.getSchedule(room);
        ScheduleElement scheduleElement = schedule.getScheduleElement(coordinate);

        assertTrue(scheduleElement.isBlocked());
        assertTrue(scheduleElement.getCourse().equals(course));
        assertTrue(scheduleElement.getRoom().equals(room));
        assertTrue(scheduleElement.getCoordiate().equals(coordinate));

        // Prüfe Dozentenplan
        Academic academic = dataController.getAcademicByName("Frey");
        schedule = instance.getSchedule(academic);
        scheduleElement = schedule.getScheduleElement(coordinate);

        assertTrue(scheduleElement.isBlocked());
        assertTrue(scheduleElement.getCourse().equals(course));
        assertTrue(scheduleElement.getRoom().equals(room));
        assertTrue(scheduleElement.getCoordiate().equals(coordinate));

        // Prüfe alle relevanten Fachsemester
        List<StudyProgram> studyPrograms = dataController.getStudyProgramsByCourse(course);
        for (StudyProgram studyProgram : studyPrograms) {
            schedule = instance.getSchedule(studyProgram, studyProgram.getSemesterByCourse(course));
            scheduleElement = schedule.getScheduleElement(coordinate);

            assertTrue(scheduleElement.isBlocked());
            assertTrue(scheduleElement.getCourse().equals(course));
            assertTrue(scheduleElement.getRoom().equals(room));
            assertTrue(scheduleElement.getCoordiate().equals(coordinate));
        }

    }

    /**
     * Test of scheduleExternal method, of class MasterSchedule.
     */
    //@Test
    public void testScheduleExternal() {
        System.out.println("scheduleExternal");
        ScheduleCoordinate coordinate = new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800);
        Course course = dataController.getCourseByID("1");

        instance.scheduleExternal(coordinate, course);

        assertEquals(1, instance.getRoomCount(RoomType.EXTERNAL));
        
    
        Room room = dataController.getRoomByName("Externer Hörsaal");
        
        Schedule schedule = instance.getSchedule(room);
        ScheduleElement scheduleElement = schedule.getScheduleElement(coordinate);

        assertTrue(scheduleElement.isBlocked());
        assertTrue(scheduleElement.getCourse().equals(course));
        assertTrue(scheduleElement.getRoom().equals(room));
        assertTrue(scheduleElement.getCoordiate().equals(coordinate));
        assertTrue(scheduleElement.getRoom().getType() == RoomType.EXTERNAL);
    }

    /**
     * Test of getCosts method, of class MasterSchedule.
     */
    @Test
    public void testGetCosts() {
        System.out.println("getCosts");
       
        ScheduleCoordinate coordinate = new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800);
        Course course = dataController.getCourseByID("1");

        assertEquals(0, instance.getCosts());
        instance.scheduleExternal(coordinate, course);
        assertEquals(10 * course.getStudents(), instance.getCosts());
    }

    /**
     * Test of getTotalBlocks method, of class MasterSchedule.
     */
    @Test
    public void testGetTotalBlocks() {
        System.out.println("getTotalBlocks");
        
        ScheduleCoordinate coordinate = new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800);
        Room room1 = dataController.getRoomByName("Audimax");
        Course course1 = dataController.getCourseByID("1");
        Course course2 = dataController.getCourseByID("17");

        assertEquals(0, instance.getTotalBlocks(RoomType.INTERNAL));
        assertEquals(0, instance.getTotalBlocks(RoomType.EXTERNAL));
         
        instance.blockCoordinate(coordinate, room1, course1);
        instance.scheduleExternal(coordinate, course2);
        
        assertEquals(1, instance.getTotalBlocks(RoomType.INTERNAL));
        assertEquals(1, instance.getTotalBlocks(RoomType.EXTERNAL));
       
    }

}
