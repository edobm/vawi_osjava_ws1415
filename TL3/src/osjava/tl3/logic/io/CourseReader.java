package osjava.tl3.logic.io;

import java.util.ArrayList;
import java.util.List;
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
 * @version 1.0
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
        ArrayList<String> courseData = readFile(fileName);

        for (String courseDataRecord : courseData) {
            dataController.getCourses().add(getCourse(courseDataRecord, dataController));
        }

    }

    /**
     * Hilfsmethode für das erzeugen von Kurs-Objekten
     *
     * @param courseDataRecord
     * @return Kurs-Objekt
     */
    private Course getCourse(String courseDataRecord, DataController dataController) {

        Course course = new Course();

        // 1;"Mathematik 1";"Vorlesung";"Frey";800;"Tafel, Mikrofonanlage";
        String[] courseData = courseDataRecord.split(";");

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
            System.out.println("Eingabewert für die Anzahl der Studenten ist kein Integer!");
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
            System.out.println("Die Veranstaltung " + removeQuotationMarks(courseData[1]) + " benötigt kein Equipment!");
        }

        return course;
    }

}
