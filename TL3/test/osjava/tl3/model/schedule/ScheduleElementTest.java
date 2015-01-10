package osjava.tl3.model.schedule;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import osjava.tl3.model.Academic;
import osjava.tl3.model.Course;
import osjava.tl3.model.Room;

/**
 *
 * @author meikelbode
 */
public class ScheduleElementTest extends ScheduleBasicTest {

    public ScheduleElementTest() {
    }

    /**
     * Test of createAppointment method, of class ScheduleElementImpl.
     */
    @Test
    public void testCreateAppointment() throws Exception {
        System.out.println("createAppointment");

        Room room = dataController.getRoomByName("Audimax");
        Course course = dataController.getCourseByID("1");

        ScheduleElementImpl instance = new ScheduleElementImpl(new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800));
        assertTrue(instance.isEmpty());

        instance.createAppointment(room, course);
        assertFalse(instance.isEmpty());

    }

    /**
     * Test of getAppointment method, of class ScheduleElementImpl.
     */
    @Test
    public void testGetAppointment_Academic() throws Exception {
        System.out.println("getAppointment");
        Academic academic = dataController.getAcademicByName("Frey");

        Room room = dataController.getRoomByName("Audimax");
        Course course = dataController.getCourseByID("1");

        ScheduleElementImpl instance = new ScheduleElementImpl(new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800));
        assertTrue(instance.isEmpty());

        instance.createAppointment(room, course);

        ScheduleAppointment expResult = new ScheduleAppointment(room, course);
        ScheduleAppointment result = instance.getAppointment(academic);

        assertEquals(expResult, result);

    }

    /**
     * Test of getAppointment method, of class ScheduleElementImpl.
     */
    @Test
    public void testGetAppointment_Room() throws Exception {
        System.out.println("getAppointment");

        Room room = dataController.getRoomByName("Audimax");
        Course course = dataController.getCourseByID("1");

        ScheduleElementImpl instance = new ScheduleElementImpl(new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800));
        assertTrue(instance.isEmpty());

        instance.createAppointment(room, course);

        ScheduleAppointment expResult = new ScheduleAppointment(room, course);
        ScheduleAppointment result = instance.getAppointment(room);

        assertEquals(expResult, result);
    }

    /**
     * Test of getAppointment method, of class ScheduleElementImpl.
     */
    @Test
    public void testGetAppointment_Course() throws Exception {
        System.out.println("getAppointment");
        Room room = dataController.getRoomByName("Audimax");
        Course course = dataController.getCourseByID("1");

        ScheduleElementImpl instance = new ScheduleElementImpl(new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800));
        assertTrue(instance.isEmpty());

        instance.createAppointment(room, course);

        ScheduleAppointment expResult = new ScheduleAppointment(room, course);
        ScheduleAppointment result = instance.getAppointment(course);

        assertEquals(expResult, result);
    }

    /**
     * Test of isEmpty method, of class ScheduleElementImpl.
     */
    @Test
    public void testIsEmpty() {
        System.out.println("isEmpty");
        
        ScheduleElementImpl instance = new ScheduleElementImpl(new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800));
        Room room = dataController.getRoomByName("Audimax");
        Course course = dataController.getCourseByID("1");
        
        assertTrue(instance.isEmpty());
        
        ScheduleAppointment appointment = new ScheduleAppointment(room, course);
        instance.addAppointment(appointment);
        
        assertFalse(instance.isEmpty());

    }

    /**
     * Test of getCoordiate method, of class ScheduleElementImpl.
     */
    @Test
    public void testGetCoordiate() {
        System.out.println("getCoordiate");
        
        ScheduleElementImpl instance = new ScheduleElementImpl(new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800));
        
        ScheduleCoordinate expResult = new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800);
        ScheduleCoordinate result = instance.getCoordiate();
        
        assertEquals(expResult, result);
        
    }

    /**
     * Test of getAppointments method, of class ScheduleElementImpl.
     */
    @Test
    public void testGetAppointments() {
        System.out.println("getAppointments");
        ScheduleElementImpl instance = new ScheduleElementImpl(new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800));
         Room room = dataController.getRoomByName("Audimax");
        Course course = dataController.getCourseByID("1");
        
        List<ScheduleAppointment> result = instance.getAppointments();
        assertEquals(0, result.size());
        
        ScheduleAppointment appointment = new ScheduleAppointment(room, course);
        instance.addAppointment(appointment);
        
        result = instance.getAppointments();
        assertEquals(1, result.size());
       
    }

}