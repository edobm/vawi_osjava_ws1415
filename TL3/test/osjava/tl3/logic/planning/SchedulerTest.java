package osjava.tl3.logic.planning;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import osjava.tl3.logic.planning.strategies.StrategyType;
import osjava.tl3.model.Academic;
import osjava.tl3.model.Course;
import osjava.tl3.model.CourseType;
import osjava.tl3.model.Equipment;
import osjava.tl3.model.Room;
import osjava.tl3.model.RoomType;
import osjava.tl3.model.Semester;
import osjava.tl3.model.StudyProgram;
import osjava.tl3.model.controller.DataController;

/**
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

        // Hörsäle
        Room r = new Room();
        r.setType(RoomType.INTERNAL);
        r.setName("Hörsaal 1");
        r.setSeats(80);
        r.getAvailableEquipments().add(Equipment.BEAMER);
        r.getAvailableEquipments().add(Equipment.ACADEMIC_COMPUTER);
        r.getAvailableEquipments().add(Equipment.SPEAKER);
        dataController.getRooms().add(r);

        r = new Room();
        r.setType(RoomType.INTERNAL);
        r.setName("Hörsal 2");
        r.setSeats(40);
        r.getAvailableEquipments().add(Equipment.BEAMER);
        r.getAvailableEquipments().add(Equipment.SPEAKER);

        r = new Room();
        r.setType(RoomType.INTERNAL);
        r.setName("Hörsaal 3");
        r.setSeats(120);
        r.getAvailableEquipments().add(Equipment.BEAMER);
        r.getAvailableEquipments().add(Equipment.ACADEMIC_COMPUTER);
        r.getAvailableEquipments().add(Equipment.SPEAKER);
        dataController.getRooms().add(r);

        r = new Room();
        r.setType(RoomType.INTERNAL);
        r.setName("Hörsaal 4");
        r.setSeats(120);
        r.getAvailableEquipments().add(Equipment.BEAMER);
        r.getAvailableEquipments().add(Equipment.ACADEMIC_COMPUTER);
        r.getAvailableEquipments().add(Equipment.SPEAKER);
        dataController.getRooms().add(r);

        r = new Room();
        r.setType(RoomType.INTERNAL);
        r.setName("Hörsaal 5");
        r.setSeats(100);
        r.getAvailableEquipments().add(Equipment.BEAMER);
        dataController.getRooms().add(r);

        r = new Room();
        r.setType(RoomType.INTERNAL);
        r.setName("Hörsaal 6");
        r.setSeats(80);
        r.getAvailableEquipments().add(Equipment.BEAMER);
        dataController.getRooms().add(r);

        // Seminarräume
        r = new Room();
        r.setType(RoomType.INTERNAL);
        r.setName("Seminarraum 1");
        r.setSeats(30);
        r.getAvailableEquipments().add(Equipment.BEAMER);
        r.getAvailableEquipments().add(Equipment.ACADEMIC_COMPUTER);
        r.getAvailableEquipments().add(Equipment.SPEAKER);
        r.getAvailableEquipments().add(Equipment.STUDENT_COMPUTER);
        dataController.getRooms().add(r);

        r = new Room();
        r.setType(RoomType.INTERNAL);
        r.setName("Seminarraum 2");
        r.setSeats(25);
        r.getAvailableEquipments().add(Equipment.BEAMER);
        r.getAvailableEquipments().add(Equipment.SPEAKER);
        r.getAvailableEquipments().add(Equipment.STUDENT_COMPUTER);

        r = new Room();
        r.setType(RoomType.INTERNAL);
        r.setName("Seminarraum 3");
        r.setSeats(35);
        r.getAvailableEquipments().add(Equipment.BEAMER);
        r.getAvailableEquipments().add(Equipment.ACADEMIC_COMPUTER);
        r.getAvailableEquipments().add(Equipment.STUDENT_COMPUTER);
        dataController.getRooms().add(r);

        r = new Room();
        r.setType(RoomType.INTERNAL);
        r.setName("Seminarraum 4");
        r.setSeats(25);
        r.getAvailableEquipments().add(Equipment.BEAMER);
        r.getAvailableEquipments().add(Equipment.ACADEMIC_COMPUTER);
        r.getAvailableEquipments().add(Equipment.SPEAKER);
        r.getAvailableEquipments().add(Equipment.STUDENT_COMPUTER);
        dataController.getRooms().add(r);

        r = new Room();
        r.setType(RoomType.INTERNAL);
        r.setName("Seminarraum 5");
        r.setSeats(40);
        dataController.getRooms().add(r);

        r = new Room();
        r.setType(RoomType.INTERNAL);
        r.setName("Seminarraum 6");
        r.setSeats(30);
        dataController.getRooms().add(r);

    }

    private void generateCourses() {

        Academic ac1 = new Academic();
        ac1.setName("Prof. Müller");

        Academic ac2 = new Academic();
        ac2.setName("Prof. Meier");

        Academic ac3 = new Academic();
        ac3.setName("Herr Wagner");

        Academic ac4 = new Academic();
        ac4.setName("Frau Tetzold");

        Academic ac5 = new Academic();
        ac5.setName("Dr. Hermann");

        Academic ac6 = new Academic();
        ac6.setName("Prof. Gerhardt");

        Academic ac7 = new Academic();
        ac7.setName("Prof. Bode");

        Academic ac8 = new Academic();
        ac8.setName("Prof. Simon");

        Academic ac9 = new Academic();
        ac9.setName("Prof. Lurz");

        Academic ac10 = new Academic();
        ac10.setName("Frau Buck");

        Academic ac11 = new Academic();
        ac11.setName("Dr. Oberwittler");

        Academic ac12 = new Academic();
        ac12.setName("Prof. Neumann");

        // Erzeuge Kurse
        Course c_bwl01 = new Course();
        c_bwl01.setName("BWL01");
        c_bwl01.setType(CourseType.READING);
        c_bwl01.setAcademic(ac1);
        c_bwl01.setStudents(120);
        c_bwl01.getRequiredEquipments().add(Equipment.BEAMER);
        c_bwl01.getRequiredEquipments().add(Equipment.ACADEMIC_COMPUTER);
        c_bwl01.getRequiredEquipments().add(Equipment.SPEAKER);

        Course c_bwl02 = new Course();
        c_bwl01.setName("BWL02");
        c_bwl01.setType(CourseType.READING);
        c_bwl01.setAcademic(ac1);
        c_bwl01.setStudents(120);
        c_bwl01.getRequiredEquipments().add(Equipment.BEAMER);
        c_bwl01.getRequiredEquipments().add(Equipment.ACADEMIC_COMPUTER);
        c_bwl01.getRequiredEquipments().add(Equipment.SPEAKER);

        // Erzeuge Kurse
        Course c_bwl01_tut = new Course();
        c_bwl01_tut.setName("BWL01 Tutorium");
        c_bwl01_tut.setType(CourseType.TUTORIAL);
        c_bwl01_tut.setAcademic(ac1);
        c_bwl01_tut.setStudents(30);
        c_bwl01_tut.getRequiredEquipments().add(Equipment.BEAMER);

        Course c_bwl02_tut = new Course();
        c_bwl01.setName("BWL02 Tutorium");
        c_bwl01.setType(CourseType.TUTORIAL);
        c_bwl01.setAcademic(ac1);
        c_bwl01.setStudents(25);
        c_bwl01.getRequiredEquipments().add(Equipment.BEAMER);

        // Erzeuge Kurse
        Course c_math01 = new Course();
        c_math01.setName("Mathe 1");
        c_math01.setType(CourseType.READING);
        c_math01.setAcademic(ac1);
        c_math01.setStudents(120);
        c_math01.getRequiredEquipments().add(Equipment.BEAMER);
        c_math01.getRequiredEquipments().add(Equipment.ACADEMIC_COMPUTER);
        c_math01.getRequiredEquipments().add(Equipment.SPEAKER);

        Course c_math02 = new Course();
        c_math02.setName("Mathe 2");
        c_math02.setType(CourseType.READING);
        c_math02.setAcademic(ac1);
        c_math02.setStudents(80);
        c_math02.getRequiredEquipments().add(Equipment.BEAMER);
        c_math02.getRequiredEquipments().add(Equipment.ACADEMIC_COMPUTER);
        c_math02.getRequiredEquipments().add(Equipment.SPEAKER);

        // Erzeuge Kurse
        Course c_math01_tut = new Course();
        c_math01_tut.setName("Mathe 1 Tutorium");
        c_math01_tut.setType(CourseType.TUTORIAL);
        c_math01_tut.setAcademic(ac1);
        c_math01_tut.setStudents(30);
        c_math01_tut.getRequiredEquipments().add(Equipment.BEAMER);

        Course c_math02_tut = new Course();
        c_math02_tut.setName("Mathe 2 Tutorium");
        c_math02_tut.setType(CourseType.TUTORIAL);
        c_math02_tut.setAcademic(ac1);
        c_math02_tut.setStudents(25);
        c_math02_tut.getRequiredEquipments().add(Equipment.BEAMER);

        StudyProgram sp_wi_ba = new StudyProgram();
        sp_wi_ba.setName("WI B.Sc.");
        sp_wi_ba.setSemesters(new ArrayList<>());

        Semester wi_ba_s1 = new Semester();
        wi_ba_s1.setCourses(new ArrayList<>());
        wi_ba_s1.getCourses().add(c_bwl01);
        wi_ba_s1.getCourses().add(c_bwl01_tut);
        wi_ba_s1.getCourses().add(c_math01);
        wi_ba_s1.getCourses().add(c_math01_tut);

        sp_wi_ba.getSemesters().add(wi_ba_s1);

        dataController.setCourses(new ArrayList<>());
        dataController.getCourses().add(c_bwl01);
        dataController.getCourses().add(c_bwl02);
        dataController.getCourses().add(c_bwl01_tut);
        dataController.getCourses().add(c_bwl02_tut);

        dataController.getCourses().add(c_math01);
        dataController.getCourses().add(c_math02);
        dataController.getCourses().add(c_math01_tut);
        dataController.getCourses().add(c_math02_tut);

    }

}
