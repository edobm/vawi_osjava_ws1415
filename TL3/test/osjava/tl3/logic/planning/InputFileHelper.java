package osjava.tl3.logic.planning;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 * Liest Testdateien aus einen Package ein und erzeugt die korrespondierenden
 * Modellobjektinstanzen
 * @author Meikel Bode
 */
public class InputFileHelper {

    public static void loadRooms(DataController dataController) {
        final String delimiter = ";";

        List<String> records = InputFileHelper.getInputFile("raeume.csv");

        Room room = null;
        for (String s : records) {
            room = new Room();
            String[] columns = s.split(delimiter);

            // Namen setzen
            room.setName(InputFileHelper.removeQuotationMarks(columns[0]));

            // Anzahl Plätze setzen
            room.setSeats(Integer.parseInt(columns[1]));

            // Verfügbares Equipment setzen
            if (columns.length == 3) {
                room.setAvailableEquipments(InputFileHelper.parseEquipments(columns[2]));
            }

            // Typ setzen
            room.setType(RoomType.INTERNAL);

            // Raum im DataController speichern
            dataController.getRooms().add(room);

        }
    }

    public static void loadCourses(DataController dataController) {

        final String delimiter = ";";

        List<String> records = InputFileHelper.getInputFile("lehrveranstaltungen.csv");

        Course course = null;
        for (String s : records) {
            course = new Course();

            // 1;"Mathematik 1";"Vorlesung";"Frey";800;"Tafel, Mikrofonanlage";
            String[] columns = s.split(delimiter);

            course.setNumber(columns[0]);
            course.setName(InputFileHelper.removeQuotationMarks(columns[1]));
            course.setType(new CourseType(InputFileHelper.removeQuotationMarks(columns[2])));
            
            Academic academic = new Academic();
            academic.setName(InputFileHelper.removeQuotationMarks(columns[3]));
            course.setAcademic(academic);
            course.setStudents(Integer.parseInt(columns[4]));
            try {
                if (columns.length == 6) {
                    course.setRequiredEquipments(InputFileHelper.parseEquipments(columns[5]));
                }
            } catch (Exception e) {
                e.printStackTrace();

                System.err.println("ERROR: " + s);
            }

            dataController.getCourses().add(course);

        }
    }

    public static void loadStudyPrograms(DataController dataController) {

        final String delimiter = ";";

        final String[] fileNames = new String[]{
           // "studiengang_bwlba.csv", 
           // "studiengang_drachenba.csv",
           // "studiengang_mumama.csv", 
           // "studiengang_physikba.csv",
            "studiengang_seba.csv", 
            "studiengang_wiba.csv",
            //"studiengang_wiingba.csv", 
            //"studiengang_wiwila.csv"
        };

        // "BWL Bachelor";;;;
        // 1;"Mathematik 1";"Recht 1";"Einführung in BWL und VWL";
        for (String fileName : fileNames) {
            boolean firstRecord = true;
            List<String> records = InputFileHelper.getInputFile(fileName);
            StudyProgram studyProgram = new StudyProgram();

            for (String s : records) {

                String[] columns = s.split(delimiter);

                if (firstRecord) {
                    firstRecord = false;
                    studyProgram.setName(InputFileHelper.removeQuotationMarks(columns[0]));
                } else {

                    Semester semester = new Semester();
                    semester.setStudyProgram(studyProgram);
                    semester.setName("Semester " + columns[0]);

                    for (int i = 1; i < columns.length; i++) {
                         for (Course course : dataController.getCourses()) {
                            if (course.getName().equals(removeQuotationMarks(columns[i]).trim())) {
                                semester.getCourses().add(course);
                            }
                        }                       
                    }

                    studyProgram.getSemesters().add(semester);

                }

            }

            dataController.getStudyPrograms().add(studyProgram);
        }

    }

    public static String removeQuotationMarks(String inString) {
        if (inString != null) {

            return inString.trim().replaceAll("\"", "");
        } else {
            return "";
        }
    }

    public static List<Equipment> parseEquipments(String record) {
        final String delimiter = ",";

        record = removeQuotationMarks(record);
        record = record.trim();
        
        List<Equipment> equipments = new ArrayList<>();

        if (record != null) {
            
            String[] columns = record.split(delimiter);
            for (String equipmentExternal : columns) {
                
                equipments.add(new Equipment(equipmentExternal.trim()));
            }

        }

        return equipments;
    }

    public static List<String> getInputFile(String fileName) {

        final String path = "/osjava/tl3/inputfiles/";

        List<String> records = new ArrayList<>();

        BufferedReader br = null;
        InputStreamReader isr = null;

        try {
            isr = new InputStreamReader(Class.class.getResourceAsStream(path + fileName));
            br = new BufferedReader(isr);

            while (br.ready()) {
                String value = br.readLine().trim();
                if (value.length() > 0) {
                    records.add(value);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(SchedulerTest.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (Exception e) {

            }
            try {
                isr.close();
            } catch (Exception e) {

            }
        }

        return records;

    }
}
