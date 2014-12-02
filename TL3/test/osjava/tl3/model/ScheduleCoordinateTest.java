package osjava.tl3.model;

import java.util.HashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author BODE28
 */
public class ScheduleCoordinateTest {
    
    public ScheduleCoordinateTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getDay method, of class ScheduleCoordinate.
     */
    @Test
    public void testGetDay() {
        System.out.println("getDay");
        
        ScheduleCoordinate instance = new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800);
        
        Day expResult = Day.MONDAY;
        Day result = instance.getDay();
        assertEquals(expResult, result);
   
    }

    /**
     * Test of getTimeSlot method, of class ScheduleCoordinate.
     */
    @Test
    public void testGetTimeSlot() {
        System.out.println("getTimeSlot");
        
        ScheduleCoordinate instance = new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800);
        
        TimeSlot expResult = TimeSlot.SLOT_0800;
        TimeSlot result = instance.getTimeSlot();
        assertEquals(expResult, result);
    
    }

    /**
     * Test of equals method, of class ScheduleCoordinate.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object obj = null;
        ScheduleCoordinate instance1 = new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800);
        ScheduleCoordinate instance2 = new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800);
        
        boolean expResult = true;
        boolean result = instance1.equals(instance2);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of equals method, of class ScheduleCoordinate.
     */
    @Test
    public void testNotEquals() {
        System.out.println("notEquals");
       
        ScheduleCoordinate instance1 = new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800);
        ScheduleCoordinate instance2 = new ScheduleCoordinate(Day.WEDNESDAY, TimeSlot.SLOT_1400);
        
        boolean expResult = false;
        boolean result = instance1.equals(instance2);
        assertEquals(expResult, result);
        
    }
    
    @Test
    public void testIdentityInHashMap() {
        System.out.println("identityInHashMap");
       
        ScheduleCoordinate instance1 = new ScheduleCoordinate(Day.MONDAY, TimeSlot.SLOT_0800);
        ScheduleCoordinate instance2 = new ScheduleCoordinate(Day.WEDNESDAY, TimeSlot.SLOT_1400);
        
        HashMap<ScheduleCoordinate, String> hashMap = new HashMap<>();
        
        hashMap.put(instance1, "First");
        assertEquals(1, hashMap.size());
        
        hashMap.put(instance2, "Second");
        assertEquals(2, hashMap.size());
        
        hashMap.put(instance1, "Third");
        assertEquals(2, hashMap.size());
        
        String result = hashMap.get(instance1);
        assertTrue(result.equals("Third"));
        
    }
}
