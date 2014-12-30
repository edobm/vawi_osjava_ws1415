package osjava.tl3.model.schedule;

import java.util.ArrayList;
import java.util.List;
import osjava.tl3.model.Academic;
import osjava.tl3.model.Course;
import osjava.tl3.model.Room;

/**
 * Diese Klasse repräsentiert ein Element des Terminplans. Jedes Element ist
 * einem Zeitpunkt an einem bestimmten Wochentag in einem Semeter zugeordnet.
 * Zudem wird diesem Element ein bestimmter Kurs einem Raum zugeordnet. Implizit
 * wird damit auch festgelegt welcher Dozent wann welche Lehrveranstaltung hält.
 *
 * @author Meikel Bode
 */
public class ScheduleElementNew {

    /**
     * Die Koordinate dieses Planelements
     */
    private final ScheduleCoordinate coordiate;

    /**
     * Die Liste von Terminen die diesem ScheduleElement zugewiesen sind
     */
    private final List<ScheduleAppointment> appointments = new ArrayList<>();


    /**
     * Erzeugt eine neue Instanz für die gegebene 
     * @param coordinate 
     */
    public ScheduleElementNew(ScheduleCoordinate coordinate) {
        this.coordiate = coordinate;
    }
    
    public void createAppointment(Room room, Course course) throws SchedulingException {

        ScheduleAppointment newAppointment = new ScheduleAppointment(room, course);

        ScheduleAppointment existingAppointment = null;
        if ((existingAppointment = getAppointment(newAppointment)) != null) {
            throw new SchedulingException("An Plankoordinate " + coordiate + " existiert für die gegebene Kombination von Raum und Kurs bereits ein Termin: " + existingAppointment);
        }
        if ((existingAppointment = getAppointment(room)) != null) {
            throw new SchedulingException("An Plankoordinate " + coordiate + " existiert für Raum bereits ein anderer Termin: " + existingAppointment);
        }

        if ((existingAppointment = getAppointment(course)) != null) {
            throw new SchedulingException("An Plankoordinate " + coordiate + " existiert für Kurs bereits ein anderer Termin: " + existingAppointment);
        }

        getAppointments().add(newAppointment);
    }

    public ScheduleAppointment getAppointment(Academic academic) {
        for (ScheduleAppointment scheduleAppointment : getAppointments()) {
            if (scheduleAppointment.getCourse().getAcademic().equals(academic)) {
                return scheduleAppointment;
            }
        }
        return null;
    }
    
    public ScheduleAppointment getAppointment(Room room) {
        for (ScheduleAppointment scheduleAppointment : getAppointments()) {
            if (scheduleAppointment.getRoom().equals(room)) {
                return scheduleAppointment;
            }
        }
        return null;
    }

    public ScheduleAppointment getAppointment(Course course) {
        for (ScheduleAppointment scheduleAppointment : getAppointments()) {
            if (scheduleAppointment.getCourse().equals(course)) {
                return scheduleAppointment;
            }
        }
        return null;
    }

    private ScheduleAppointment getAppointment(ScheduleAppointment appointment) {
        for (ScheduleAppointment scheduleAppointment : getAppointments()) {
            if (scheduleAppointment.equals(appointment)) {
                return scheduleAppointment;
            }
        }
        return null;
    }

    public void addAppointment(ScheduleAppointment appointment) {
        getAppointments().add(appointment);
    }
    
    /**
     * Prüft ob diesem Planelement Termine zugewiesen sind
     * @return Ja oder Nein
     */
    public boolean isEmpty() {
        return getAppointments().isEmpty();
    }
    
    /**
     * Setzt die Plankoordinate
     *
     * @return Die Plankoordinate
     */
    public ScheduleCoordinate getCoordiate() {
        return coordiate;
    }

    /**
     * Liefert die Termine dieses Elements
     * @return Die Termine
     */
    public List<ScheduleAppointment> getAppointments() {
        return appointments;
    }
    
}