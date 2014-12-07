package osjava.tl3.logic.io;

import java.util.ArrayList;
import java.util.List;
import osjava.tl3.model.Course;



/**
 * Diese Klasse behandelt das spezifische Zeilenformat der Eingabedatei für Kurse
 * und erzeugt Instanzen der Modellklassen Course.
 * 
 * @author Fabian Simon
 * @version 1.0
 */
public class CourseReader extends InputFileReader{
    
    /**
     * Die Methode liest die Daten aus der Eingabedatei mit den Kursen ein,
     * verarbeitet diese und erzeugt für jeden Kurs ein entsprechendes Kurs-Objekt.
     * Diese Kurs-Obejekte werden in einer Liste gespeichert, welche von der Methode
     * zurückgegeben wird.
     *
     * @return Die Liste mit Objekten der Kurse.
     */
    public List<Course> readCourses(){
        ArrayList<String> courseData = super.readFile("lehrveranstaltungen.csv");
        
        List<Course> courses = new ArrayList<>();
        
        return courses;
    }
    
    /**
     * Hilfsmethode für das erzeugen von Kurs-Objekten
     * 
     * @param courseDataRecord
     * @return Kurs-Objekt
     */
    private Course getCourse (String courseDataRecord) {
        
        Course c = new Course();
        
        return c;
    }
  
}
