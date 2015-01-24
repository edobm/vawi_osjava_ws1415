package osjava.tl3.logic.io.input;

import java.util.ArrayList;
import java.util.List;
import osjava.tl3.logging.Protocol;
import osjava.tl3.model.Academic;
import osjava.tl3.model.Course;
import osjava.tl3.model.CourseType;
import osjava.tl3.model.Equipment;
import osjava.tl3.model.controller.DataController;

/**
 * Diese Klasse behandelt das spezifische Zeilenformat der Eingabedatei für
 * Kurse und erzeugt Instanzen der Modellklassen Course.
 *
 * @author Fabian Simon
 */
public class CourseReader extends InputFileReader {

    /**
     * Die Methode liest die Daten aus der Eingabedatei mit den Kursen ein,
     * verarbeitet diese und erzeugt für jeden Kurs ein entsprechendes
     * Kurs-Objekt. Diese Kurs-Obejekte werden in einer Liste gespeichert,
     * welche von der Methode zurückgegeben wird.
     *
     * @param fileName Name der Eingabedatei
     * @param dataController DataController-Instanz zur Ablage der Daten
     */
    public void readCourses(String fileName, DataController dataController) {
        ArrayList<String> courseRecords = readFile(fileName);

        String courseRecord;
        for (int i = 0; i < courseRecords.size(); i++) {
            courseRecord = courseRecords.get(i);

            if (courseRecord == null || courseRecord.length() == 0) {

                Protocol.log("Fehler in Kursdatei: " + fileName + " auf Zeile " + (i + 1) + ": Zeile wird ignoriert");
            }

            // Zeilenweise die Kurse erzeugen
            try {
                dataController.getCourses().add(getCourse(i + 1, courseRecord, dataController));
            } catch (InputFileReaderException e) {
                Protocol.log("Fehler in Kursdatei: " + fileName + " auf Zeile " + (i + 1) + ":" + e.getMessage() + ": Zeile wird ignoriert");
            }
        }

    }

    /**
     * Hilfsmethode für die Erzeugung von Kurs-Objekten
     *
     * @param courseDataRecord
     * @return Kurs-Objekt
     */
    private Course getCourse(int rowNumber, String courseDataRecord, DataController dataController) throws InputFileReaderException {

        Course course = new Course();

        // 1;"Mathematik 1";"Vorlesung";"Frey";800;"Tafel, Mikrofonanlage";
        String[] courseData = validateRecord(rowNumber, courseDataRecord);

        //Setzen von Kursnummer und Kursname 
        course.setNumber(courseData[0]);
        course.setName(removeQuotationMarks(courseData[1]));

        //Erzeugen und setzen des Kurstyp
        CourseType courseType = new CourseType(removeQuotationMarks(courseData[2]));
        course.setType(courseType);

        //Erzeugen und setzen des Dozenten
        Academic academic = new Academic();
        academic.setName(removeQuotationMarks(courseData[3]));
        course.setAcademic(academic);

        //Setzen der Anzahl der Studenten
        int numberOfStudents = 0;
        try {
            numberOfStudents = Integer.parseInt(courseData[4]);
        } catch (Exception e) {
            Protocol.log("Eingabewert für die Anzahl der Studenten ist kein Integer!");
        }
        course.setStudents(numberOfStudents);

        //Equipments setzen falls vorhanden
        if (courseData.length == 6) {
            List<Equipment> requiredEquipments = parseEquipments(courseData[5]);
            course.setRequiredEquipments(requiredEquipments);
            for (Equipment e : requiredEquipments) {
                if (!dataController.getEquipments().contains(e)) {
                    dataController.getEquipments().add(e);
                }
            }
        } else {
            Protocol.log("Die Veranstaltung [KursID='" + course.getNumber()+ "', Kurs='" + course.getName() + "'] benötigt kein Equipment!");
        }

        return course;
    }

    /**
     * Validiert die Zeile aus der Importdatei auf dem gegebenen Zeilenindex
     * @param rowNumber Der Zeilenindex
     * @param recordLine Die Zeile aus der Importdatei
     * @return Die in Spalten zerlegte Zeile
     * @throws InputFileReaderException 
     * 
     * @see InputFileReader#validateRecord(int, java.lang.String) 
     */
    @Override
    protected String[] validateRecord(int rowNumber, String recordLine) throws InputFileReaderException {

        /**
         * Spaltentrenner
         */
        final String delimiter = ";";

        /**
         * Zeile zerlegen
         */
        String[] record = recordLine.split(delimiter);

        /**
         * Beispielzeilen 78;"Projektilphysik";"Vorlesung";"Voss";50;"Beamer,
         * DozentenPC"; 79;"Diplomatie";"Uebung";"Westerwelle";60;;
         */
        /**
         * Es werden 2 oder 3 Spalten pro Raumdatensatz erwartet:
         *
         * 2 Spalten: Name und Anzahl Sitzplätze aber kein Equipment 3 Spalte:
         * Wie 2 und zusätzlich Equipment
         *
         */
        if (record.length > 7) {
            throw new InputFileReaderException("Ungültige Spaltenanzahl für Datensatz. Erwartet: 7, Ist: " + record.length, null, recordLine, rowNumber);
        }

        /**
         * Prüfung Spalte KursID (Index 0):
         */
        InputValidator.validateInteger("KursID", record[0], recordLine, rowNumber);

        /**
         * Prüfung Spalte Kurname (Index 1)
         */
        InputValidator.validateEmpty("Kursname", record[1], recordLine, rowNumber);
        InputValidator.validateStardEndsWithBraces("Kursname", record[1], recordLine, rowNumber);

        /**
         * Prüfung Spalte Kurstyp (Index 2)
         */
        InputValidator.validateEmpty("Kurstyp", record[2], recordLine, rowNumber);
        InputValidator.validateStardEndsWithBraces("Kurstyp", record[2], recordLine, rowNumber);

        /**
         * Prüfung Spalte Dozent (Index 3)
         */
        InputValidator.validateEmpty("Dozent", record[3], recordLine, rowNumber);
        InputValidator.validateStardEndsWithBraces("Dozent", record[3], recordLine, rowNumber);

        /**
         * Prüfung Spalte Teilnehmerzahl (Index 4)
         */
        InputValidator.validateEmpty("Teilnehmerzahl", record[4], recordLine, rowNumber);
        InputValidator.validateInteger("Teilnehmerzahl", record[4], recordLine, rowNumber);

        /**
         * Prüfung Spalte Ausstattung (Index 6)
         */
        if (record.length == 6) {
            if (record[5] != null && record[5].length() > 2) {
                InputValidator.validateEquipment("Austattung", record[5], recordLine, rowNumber);
            }
        }

        /**
         * Prüfung Spalte Frequenz (Index 7)
         */
        if (record.length == 7) {
            InputValidator.validateEmpty("Frequenz", record[6], recordLine, rowNumber);
            InputValidator.validateStardEndsWithBraces("Frequenz", record[6], recordLine, rowNumber);
        }

        /**
         * Datensatz zurückgeben
         */
        return record;
    }

}