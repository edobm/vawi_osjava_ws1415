package osjava.tl3.model.schedule;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import osjava.tl3.logic.io.input.CourseReader;
import osjava.tl3.logic.io.input.RoomReader;
import osjava.tl3.logic.io.input.StudyProgramReader;
import osjava.tl3.model.controller.DataController;

/**
 *
 * @author meikelbode
 */
public class ScheduleBasicTest {

    protected DataController dataController;

    public ScheduleBasicTest() {
    }
    

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
    }

   
    @After
    public void tearDown() {
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

}
