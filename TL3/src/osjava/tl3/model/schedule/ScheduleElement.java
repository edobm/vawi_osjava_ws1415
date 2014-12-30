package osjava.tl3.model.schedule;
  
import osjava.tl3.model.Course;
import osjava.tl3.model.Room;

/**
 * Diese Klasse repräsentiert ein Element des Terminplans.
 * Jedes Element ist einem Zeitpunkt an einem bestimmten Wochentag in einem Semeter zugeordnet.
 * Zudem wird diesem Element ein bestimmter Kurs einem Raum zugeordnet. 
 * Implizit wird damit auch festgelegt welcher Dozent wann welche Lehrveranstaltung hält.
 * 
 * @author Meikel Bode
 * @version 1.0
 */
public class ScheduleElement
{
    /**
     * Die Koordinate dieses Planelements
     */
    private ScheduleCoordinate coordiate = null;
    
    /**
     * Der diesem Planelement zugewiesene Raum
     */
    private Room room = null;
    
    /**
     * Der diesem Planelement zugewiesene Kurs
     */
    private Course course = null;
    
    /**
     * Ob dieses Planelement bereits belegt ist.
     * @return Belegt ja oder nein
     */
    public boolean isBlocked() {
        return course != null && room != null;
    }
    
    /**
     * Setzt den gegebenen Kurs und liefert das aktuelle ScheduleElement zurück
     * @param course Der zusetzende Kurs
     * @return Die aktuelle Instanz des Planelements auf dem gearbeitet wird.
     */
    public ScheduleElement assignCourse(Course course) {
        if (this.course != null) {
            throw new IllegalStateException("Course already assinged!");
        }
        this.course = course;
        return this;
    }
    
    /**
     * Setzt den gegebenen Raum und liefert das aktuelle ScheduleElement zurück
     * @param room Der zusetzende Raum
     * @return Die aktuelle Instanz des Planelements auf dem gearbeitet wird.
     */
    public ScheduleElement assignRoom(Room room) {
        if (this.room != null) {
            throw new IllegalStateException("Room already assinged!");
        }
        this.room = room;
        return this;
    }

    /**
     * Liefert den Kurs 
     * @return Der Kurs
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Setzt den Kurs
     * @param course Der Kurs
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * Liefert den Raum
     * @return Der Raum
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Setzt den Raum
     * @param room Der Raum
     */
    public void setRoom(Room room) {
        this.room = room;
    }

    /**
     * Setzt die Plankoordinate
     * @return Die Plankoordinate
     */
    public ScheduleCoordinate getCoordiate() {
        return coordiate;
    }

    /**
     * Setzt die Plankoordinate
     * @param coordiate Plankoordinate
     */
    public void setCoordiate(ScheduleCoordinate coordiate) {
        this.coordiate = coordiate;
    }
}