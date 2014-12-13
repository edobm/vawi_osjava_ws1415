package osjava.tl3.model;

import java.util.Hashtable;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import osjava.tl3.logic.io.InputFileHelper;
import osjava.tl3.logic.planning.Scheduler;
import osjava.tl3.logic.planning.strategies.Strategy;
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

        assertEquals(10, instance.getRoomCount(RoomType.INTERNAL, false));

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
     * Test of getCosts method, of class MasterSchedule.
     */
    @Test
    public void testGetExternalScheduledSeats() {
        System.out.println("getExternalScheduledSeats");
       
        ScheduleCoordinate coordinate = new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800);
        Course course = dataController.getCourseByID("1");
        Room room = instance.createExternalRoom(dataController.getEquipments());
        
        assertEquals(0, instance.getExternallyScheduledSeats());
        instance.blockCoordinate(coordinate, room, course);
        assertEquals(course.getStudents(), instance.getExternallyScheduledSeats());
    }

    /**
     * Test of getTotalRoomBlocks method, of class MasterSchedule.
     */
    @Test
    public void testGetTotalBlocks() {
        System.out.println("getTotalBlocks");
        
        ScheduleCoordinate coordinate = new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800);
        Room room1 = dataController.getRoomByName("Audimax");
        Room room2 = instance.createExternalRoom(dataController.getEquipments());
        
        Course course1 = dataController.getCourseByID("1");
        Course course2 = dataController.getCourseByID("114");

        assertEquals(0, instance.getTotalRoomBlocks(RoomType.INTERNAL));
        assertEquals(0, instance.getTotalRoomBlocks(RoomType.EXTERNAL));
         
        instance.blockCoordinate(coordinate, room1, course1);
        instance.blockCoordinate(coordinate, room2, course2);
        
        assertEquals(1, instance.getTotalRoomBlocks(RoomType.INTERNAL));
        assertEquals(1, instance.getTotalRoomBlocks(RoomType.EXTERNAL));
       
    }
    
    @Test
    public void testPlanValidity() {
         System.out.println("testPlanValidity");
         
         Scheduler scheduler = new Scheduler();
         scheduler.setDataController(dataController);
         scheduler.setStrategy(Strategy.getStrategyInstanceByClassName("CostOptimizedStrategy"));
         scheduler.executeStrategy(null);
         
         instance = scheduler.getMasterSchedule();
         
         for (Course course : dataController.getCourses()) {
             assertEquals(1, countScheduleCordinatesOfCourse(course));
         }
    }
    
        @Test
    public void testAssignedToAllScheduleTypes() {
         System.out.println("testAssignedToAllScheduleTypes");
         
         Scheduler scheduler = new Scheduler();
         scheduler.setDataController(dataController);
         scheduler.setStrategy(Strategy.getStrategyInstanceByClassName("CostOptimizedStrategy"));
         scheduler.executeStrategy(null);
         
         instance = scheduler.getMasterSchedule();
         
         for (Course course : dataController.getCourses()) {
             assertTrue("Course not assigned to all schedule types: " + course,isAssignedToAllScheduleTypes(course));
         }
    }

    private int countScheduleCordinatesOfCourse(Course course) {
        Hashtable<String, String> hmCoordinates = new Hashtable<>();
        
        List<Schedule> schedules = instance.getSchedules(course);
        System.out.println("Course: [" + course.getNumber() + "] '" + course.getName() + "' is assined to Schedules: " + schedules.size());
        for (Schedule schedule : schedules) {
            for (ScheduleElement scheduleElement : schedule.getScheduleElements()) {
                if(scheduleElement.getCourse() != null && scheduleElement.getCourse().equals(course)) {
                    hmCoordinates.put(scheduleElement.getCoordiate().toString(), "");
                    System.out.println(" ScheduleType: " + schedule.getType().name() + ", Room: '" + scheduleElement.getRoom().getName() + "', Coordinate: " + scheduleElement.getCoordiate());
                }
            }
        }
        
        return hmCoordinates.size();
    }
    
    private boolean isAssignedToAllScheduleTypes(Course course) {
        List<Schedule> schedules = instance.getSchedules(course);
        boolean assignedToRoomSchedule = false;
        boolean assignedToAcademicSchedule = false;
        boolean assignedToStudyProgramSchedule = false;
        
        for (Schedule schedule : schedules) {
            if (schedule.getType() == ScheduleType.ACADAMIC) {
                assignedToAcademicSchedule = true;
            }
            if (schedule.getType() == ScheduleType.ROOM_EXTERNAL || schedule.getType() == ScheduleType.ROOM_INTERNAL) {
                assignedToRoomSchedule = true;
            }
             if (schedule.getType() == ScheduleType.STUDY_PROGRAM) {
                assignedToStudyProgramSchedule = true;
            }
        }
        
        System.out.println("Course: [" + course.getNumber() + "] '" + course.getName() + "' is assined to room, academic and study programms schedules: " + (assignedToAcademicSchedule && assignedToRoomSchedule && assignedToStudyProgramSchedule));
      
        return assignedToAcademicSchedule && assignedToRoomSchedule && assignedToStudyProgramSchedule;
    }
}
