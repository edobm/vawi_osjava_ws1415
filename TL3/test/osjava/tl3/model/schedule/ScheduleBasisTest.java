package osjava.tl3.model.schedule;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author meikelbode
 */
public class ScheduleBasisTest {
    
    public ScheduleBasisTest() {
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
     * Test of getPossibleScheduleCoordinates method, of class ScheduleBasis.
     */
    @Test
    public void testGetPossibleScheduleCoordinates() {
        System.out.println("getPossibleScheduleCoordinates");
        
        String expResult = "[[MONDAY;08:00-10:00], [MONDAY;10:00-12:00], [MONDAY;12:00-14:00], [MONDAY;14:00-16:00], [MONDAY;16:00-18:00], [TUESDAY;08:00-10:00], [TUESDAY;10:00-12:00], [TUESDAY;12:00-14:00], [TUESDAY;14:00-16:00], [TUESDAY;16:00-18:00], [WEDNESDAY;08:00-10:00], [WEDNESDAY;10:00-12:00], [WEDNESDAY;12:00-14:00], [WEDNESDAY;14:00-16:00], [WEDNESDAY;16:00-18:00], [THURSDAY;08:00-10:00], [THURSDAY;10:00-12:00], [THURSDAY;12:00-14:00], [THURSDAY;14:00-16:00], [THURSDAY;16:00-18:00], [FRIDAY;08:00-10:00], [FRIDAY;10:00-12:00], [FRIDAY;12:00-14:00], [FRIDAY;14:00-16:00], [FRIDAY;16:00-18:00]]";
        List<ScheduleCoordinate> coordinate = ScheduleBasis.getPossibleScheduleCoordinates();
        
        assertEquals(expResult, coordinate.toString());
        
        assertEquals(25, coordinate.size());
      
        
    }
    
}
