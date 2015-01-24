package osjava.tl3.logic.planning;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import osjava.tl3.logic.io.input.CourseReader;
import osjava.tl3.logic.io.input.RoomReader;
import osjava.tl3.logic.io.input.StudyProgramReader;
import osjava.tl3.logic.planning.strategies.StrategyFactory;
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

    /**
     * Test of executeStrategy method, of class Scheduler.
     */
    @Test
    public void testExecuteStrategy() {
        System.out.println("executeStrategy");
        
        Scheduler instance = new Scheduler();
        instance.setDataController(dataController);
        instance.setStrategy(StrategyFactory.getInstanceByClassName("CostOptimizedStrategy"));

        instance.executeStrategy();

    }

}
