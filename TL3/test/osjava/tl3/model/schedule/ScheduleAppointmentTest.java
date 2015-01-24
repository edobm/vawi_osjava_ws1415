/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package osjava.tl3.model.schedule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import osjava.tl3.model.Course;
import osjava.tl3.model.Room;

/**
 *
 * @author meikelbode
 */
public class ScheduleAppointmentTest  extends ScheduleBasicTest {
    
    public ScheduleAppointmentTest() {
    }

    /**
     * Test of getRoom method, of class ScheduleAppointment.
     */
    @Test
    public void testGetRoom() {
        System.out.println("getRoom");
        
        Room room = dataController.getRoomByName("Audimax");
        Course course = dataController.getCourseByID("1");

        ScheduleAppointment instance = new ScheduleAppointment(room, course);
        
        Room result = instance.getRoom();
        assertEquals(room, result);
        
    }

    /**
     * Test of getCourse method, of class ScheduleAppointment.
     */
    @Test
    public void testGetCourse() {
        System.out.println("getCourse");
         
        Room room = dataController.getRoomByName("Audimax");
        Course course = dataController.getCourseByID("1");

        ScheduleAppointment instance = new ScheduleAppointment(room, course);
        
        Course result = instance.getCourse();
        assertEquals(course, result);
    }

    /**
     * Test of equals method, of class ScheduleAppointment.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
         
        Room room = dataController.getRoomByName("Audimax");
        Course course = dataController.getCourseByID("1");

        ScheduleAppointment instance = new ScheduleAppointment(room, course);
       
        assertTrue(instance.equals(instance));
        
        assertFalse(instance.equals(course));
        
    }
    
}
