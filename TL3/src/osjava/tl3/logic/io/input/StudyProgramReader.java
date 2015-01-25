package osjava.tl3.logic.io.input;

import java.util.ArrayList;
import osjava.tl3.logging.Protocol;
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
        try {
            StudyProgram studyProgram = new StudyProgram();
            String record;
            for (int i = 0; i < studyProgramDataRecords.size(); i++) {
                record = studyProgramDataRecords.get(i);

                String[] columns = record.split(";");

                if (firstRecord) {
                    InputValidator.validateEmpty("Studiengangsname", columns[0], record, i);
                    InputValidator.validateStardEndsWithBraces("Studiengangsname", columns[0], record, i);

                    firstRecord = false;
                    studyProgram.setName(removeQuotationMarks(columns[0]));
                } else {
                    
                    if (columns.length < 2) {
                        InputValidator.validateInteger("Dem Fachsemester sind keine Kurse zugeordnet", columns[0], record, i);
                    }
                    InputValidator.validateInteger("Fachsemester", columns[0], record, i);

                    Semester semester = new Semester();
                    semester.setStudyProgram(studyProgram);
                    semester.setName("Semester " + columns[0]);
                    String courseName;
                    for (int y = 1; y < columns.length; y++) {
                        courseName = removeQuotationMarks(columns[y]).trim();
                        boolean added = false;
                        for (Course course : dataController.getCourses()) {
                            if (course.getName().equals(courseName)) {
                                semester.getCourses().add(course);
                                added = true;
                            }
                        }
                        if (!added) {
                            throw new InputFileReaderException("Der Studiengang " + studyProgram.getName()
                                    + " bezieht sich in " + semester.getName() + " auf den unbekannten Kurs " + courseName, null, record, i);
                        }
                    }

                    studyProgram.getSemesters().add(semester);

                }

            }

            if (dataController.getStudyProgramByName(studyProgram.getName()) != null) {
                  Protocol.log("Fehler in Studiengangsdatei: " + fileName + ": Der Studiengang mit der Bezeichnung '" + studyProgram.getName() + "' wurde bereits eingelesen. Datei ignoriert.");
            } else {
                dataController.getStudyPrograms().add(studyProgram);
            }

        } catch (InputFileReaderException ex) {
            Protocol.log("Fehler in Studiengangsdatei: " + fileName + " auf Zeile " + ex.getRow() + ": " + ex.getMessage() + ": Datei wird ignoriert");
        }
    }

    /**
     * Nicht verwendet
     *
     * @return
     * @throws osjava.tl3.logic.io.input.InputFileReaderException
     * @see InputFileReader#validateRecord(int, java.lang.String)
     */
    @Override
    protected String[] validateRecord(int rowNumber, String recordLine) throws InputFileReaderException {
        /**
         * Die Methode wird nicht verwendet!! Die Validierung findet aufgrund
         * des Dateaufbaus innerhalb der Methode readStudyProgramm statt
         */

        return null;
    }

}
