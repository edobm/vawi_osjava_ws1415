package osjava.tl3.model.schedule;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import osjava.tl3.model.Academic;
import osjava.tl3.model.Course;
import osjava.tl3.model.Room;

/**
 *
 * @author meikelbode
 */
public class ScheduleViewAcademicTest extends ScheduleBasicTest {

    public ScheduleViewAcademicTest() {
    }

    /**
     * Test of getScheduleElement method, of class ScheduleViewAcademic.
     */
    @Test
    public void testGetScheduleElement() {
        System.out.println("getScheduleElement");
        Academic academic = dataController.getAcademicByName("Frey");
        Schedule schedule = new Schedule();
        ScheduleCoordinate coordinate = new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800);
        ScheduleViewAcademic instance = new ScheduleViewAcademic(academic, schedule);

        ScheduleElement result = instance.getScheduleElement(coordinate);
        assertEquals(0, result.getAppointments().size());

    }

    /**
     * Test of getAcademic method, of class ScheduleViewAcademic.
     */
    @Test
    public void testGetAcademic() {
        System.out.println("getAcademic");
        Academic academic = dataController.getAcademicByName("Frey");
        Schedule schedule = new Schedule();
        ScheduleCoordinate coordinate = new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800);
        ScheduleViewAcademic instance = new ScheduleViewAcademic(academic, schedule);

        assertEquals(academic, instance.getAcademic());
    }

    @Test
    public void testGetBlockedCoodinates(){
        System.out.println("getBlockedCoodinates");
        Academic academic = dataController.getAcademicByName("Frey");
        Schedule schedule = new Schedule();
        ScheduleCoordinate coordinate = new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800);
        ScheduleViewAcademic instance = new ScheduleViewAcademic(academic, schedule);

        Room room = dataController.getRoomByName("Audimax");
        Course course = dataController.getCourseByID("1");

        assertEquals(0, instance.getBlockedCoordinates().size());

        schedule.createAppointment(coordinate, room, course);

        assertEquals(1, instance.getBlockedCoordinates().size());

    }

    @Test
    public void testGetFreeCoodinates() {
        System.out.println("getFreeCoodinates");
        Academic academic = dataController.getAcademicByName("Frey");
        Schedule schedule = new Schedule();
        ScheduleCoordinate coordinate = new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800);
        ScheduleViewAcademic instance = new ScheduleViewAcademic(academic, schedule);

        Room room = dataController.getRoomByName("Audimax");
        Course course = dataController.getCourseByID("1");

        assertEquals(25, instance.getFreeCoordinates().size());

        schedule.createAppointment(coordinate, room, course);

        assertEquals(24, instance.getFreeCoordinates().size());

    }

//    @Test
//    public void testGetCoodinates_boolean() throws SchedulingException {
//        System.out.println("getCoodinates_boolean");
//        Academic academic = dataController.getAcademicByName("Frey");
//        Schedule schedule = new Schedule();
//        ScheduleCoordinate coordinate = new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800);
//        ScheduleViewAcademic instance = new ScheduleViewAcademic(academic, schedule);
//
//        Room room = dataController.getRoomByName("Audimax");
//        Course course = dataController.getCourseByID("1");
//
//        assertEquals(0, instance.getCoordinates(true).size());
//        assertEquals(25, instance.getCoordinates(false).size());
//        
//        schedule.createAppointment(coordinate, room, course);
//
//        assertEquals(1, instance.getCoordinates(true).size());
//        assertEquals(24, instance.getCoordinates(false).size());
//
//    }

}
