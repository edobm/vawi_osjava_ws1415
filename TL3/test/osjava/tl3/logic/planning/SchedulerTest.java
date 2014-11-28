package osjava.tl3.logic.planning;

import java.util.HashMap;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import osjava.tl3.logic.planning.strategies.StrategyType;
import osjava.tl3.model.Academic;
import osjava.tl3.model.Course;
import osjava.tl3.model.Room;
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
        generateRooms();
        generateCourses();
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

    private void generateRooms() {
        final String delimiter = ";";

        List<String> records = ParsingHelpers.getInputFile("raeume.csv");

        Room room = null;
        for (String s : records) {
            room = new Room();
            String[] columns = s.split(delimiter);

            // Namen setzen
            room.setName(ParsingHelpers.removeQuotationMarks(columns[0]));

            // Anzahl Plätze setzen
            room.setSeats(Integer.parseInt(columns[1]));

            // Verfügbares Equipment setzen
            room.setAvailableEquipments(ParsingHelpers.parseEquipments(columns[2]));

            // Raum im DataController speichern
            dataController.getRooms().add(room);

        }
    }

    private void generateCourses() {

        final String delimiter = ";";

        List<String> records = ParsingHelpers.getInputFile("lehrveranstaltungen.csv");

        Course course = null;
        for (String s : records) {
            course = new Course();

            // 1;"Mathematik 1";"Vorlesung";"Frey";800;"Tafel, Mikrofonanlage";
            String[] columns = s.split(delimiter);

            course.setNumber(columns[0]);
            course.setName(ParsingHelpers.removeQuotationMarks(columns[1]));
            course.setType(ParsingHelpers.mapCourseType(columns[2]));
            Academic academic = new Academic();
            academic.setName(ParsingHelpers.removeQuotationMarks(columns[3]));
            course.setAcademic(academic);
            course.setStudents(Integer.parseInt(columns[4]));
            try {
                if (columns.length == 6) {
                    course.setRequiredEquipments(ParsingHelpers.parseEquipments(columns[5]));
                }
            } catch (Exception e) {
                e.printStackTrace();
                
                System.err.println("ERROR: " + s);
            }
            dataController.getCourses().add(course);

        }
    }

}
