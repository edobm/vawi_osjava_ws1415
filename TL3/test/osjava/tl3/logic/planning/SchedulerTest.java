package osjava.tl3.logic.planning;

import java.util.HashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import osjava.tl3.logic.planning.strategies.StrategyType;
import osjava.tl3.model.controller.DataController;

/**
 * s
 *
 * @author Meikel Bode
 */
public class SchedulerTest {

    private DataController dataController;

    public SchedulerTest() {
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
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of executeStrategy method, of class Scheduler.
     */
    @Test
    public void testExecuteStrategy() {
        System.out.println("executeStrategy");
        HashMap<String, Object> parameters = null;
        
        Scheduler instance = new Scheduler();
        instance.setDataController(dataController);
        instance.setStrategyType(StrategyType.COST_OPTIMIZED);

        instance.executeStrategy(parameters);

    }

}
