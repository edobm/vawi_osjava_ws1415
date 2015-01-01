package osjava.tl3.model.schedule;

import osjava.tl3.model.Course;

/**
 * Der Gesamtplan aus Sicht eines Kurses
 *
 * @author Meikel Bode
 */
public class ScheduleViewCourse extends ScheduleView {

    /**
     * Der Raum auf dem diese Sicht operiert
     */
    private final Course course;

    /**
     * Erzeugt eine neue Instanz der Fachsemestersicht
     *
     * @param course Das Kurs auf dem diese Sicht operiert
     * @param schedule Der Plan auf dem diese Sicht operiert
     */
    public ScheduleViewCourse(Course course, Schedule schedule) {
        super(schedule);
        this.course = course;
    }

    /**
     * Liefert die Termine des Planelements auf der gegebenen Koordinate 
     * anhand des Kurses. 
     * Nur die Termine für das Filterkrieterium werden zurückgeliefert
     * @param coordinate Die Plankoordinate
     * @return Das gefilterte Planelement 
     */
    @Override
    public ScheduleElement getScheduleElement(ScheduleCoordinate coordinate) {
        
        ScheduleElementImpl elementFiltered = new ScheduleElementImpl(coordinate);
        
        ScheduleAppointment appointment = schedule.getScheduleElement(coordinate).getAppointment(course);
        
        if (appointment != null) {
            elementFiltered.addAppointment(appointment);
        }
        
        return new ScheduleElementViewWrapper(this, elementFiltered);

    }

    /**
     * Liefert den Kurs auf der diese Sicht operiert
     * @return Der Raum
     */
    public Course getCourse() {
        return course;
    }
    
}