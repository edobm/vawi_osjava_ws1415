package osjava.tl3.model;
  
/**
 * Diese Klasse repräsentiert ein Element des Terminplans.
 * Jedes Element ist einem Zeitpunkt an einem bestimmten Wochentag in einem Semeter zugeordnet.
 * Zudem wird diesem Element ein bestimmter Kurs einem Raum zugeordnet. 
 * Implizit wird damit auch festgelegt welcher Dozent wann welche Lehrveranstaltung hält.
 * 
 * @author Christoph Lurz
 * @version 1.0
 */
public class ScheduleElement
{
    private ScheduleCoordinate coordiate = null;
    private Room room = null;
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
        this.course = course;
        return this;
    }
    
    /**
     * Setzt den gegebenen Raum und liefert das aktuelle ScheduleElement zurück
     * @param room Der zusetzende Raum
     * @return Die aktuelle Instanz des Planelements auf dem gearbeitet wird.
     */
    public ScheduleElement assignRoom(Room room) {
        this.room = room;
        return this;
    }

    /**
     * @return the course
     */
    public Course getCourse() {
        return course;
    }

    /**
     * @param course the course to set
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * @return the room
     */
    public Room getRoom() {
        return room;
    }

    /**
     * @param room the room to set
     */
    public void setRoom(Room room) {
        this.room = room;
    }

    /**
     * @return the coordiate
     */
    public ScheduleCoordinate getCoordiate() {
        return coordiate;
    }

    /**
     * @param coordiate the coordiate to set
     */
    public void setCoordiate(ScheduleCoordinate coordiate) {
        this.coordiate = coordiate;
    }
}
