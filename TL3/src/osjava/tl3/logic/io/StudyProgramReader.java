package osjava.tl3.logic.io;

import java.util.ArrayList;
import java.util.List;
import osjava.tl3.model.Course;
import osjava.tl3.model.Semester;
import osjava.tl3.model.StudyProgram;
import osjava.tl3.model.controller.DataController;

/**
 * Diese Klasse behandelt das spezifische Zeilenformat für Studiengänge,
 * Fachsemester und Kursnamen und erzeugt Instanzen der Modellklassen Course und
 * setzt das Fachsemester pro Instanz. Die Details eines jeden Kurses werden
 * durch die Klasse CourseReader ergänzt.
 *
 * @author Fabian Simon
 * @version 1.0
 */
public class StudyProgramReader extends InputFileReader {

    /**
     * Die Methode liest die Daten der Studiengänge aus den einzelnen
     * Eingabedateien der Studiuengänge ein, verarbeitet diese und erzeugt für
     * jeden Studiengang ein Studiengang-Objekt. Diese Studiengang-Objekte
     * werden in einer Liste gespeichert, welche von der Methode zurückgegeben
     * wird.
     *
     * @param fileName Namen der einzulesenden Datei
     * @param dataController DataController-Instanz zur Ablage der Daten
     */
    public void readStudyPrograms(String fileName, DataController dataController) {
        
        boolean firstRecord = true;
        ArrayList<String> studyProgramDataRecords = super.readFile(fileName);
        
        StudyProgram studyProgram = new StudyProgram();

        for (String s : studyProgramDataRecords) {

            String[] columns = s.split(";");

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
