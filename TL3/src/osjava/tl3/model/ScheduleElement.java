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
    private Day day; 
    private TimeSlot timeSlot;
    private Semester semester;
    private Course course;
    private Room room;
}
