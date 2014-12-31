package osjava.tl3.model.schedule;

import java.util.ArrayList;
import java.util.List;
import osjava.tl3.model.Academic;
import osjava.tl3.model.Course;
import osjava.tl3.model.Room;

/**
 * Diese Klasse repräsentiert ein Element des Terminplans. Jedes Element ist
 * einer bestimmten Plankoordinate zugeordnet. Jedes Planelement kann beliebig
 * viele Termine (ScheduleAppointments) in Form einer Kombination von Raum und
 * Kurs beinhalten.
 *
 * @author Meikel Bode
 */
public class ScheduleElement {

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
     *
     * @param coordinate
     */
    public ScheduleElement(ScheduleCoordinate coordinate) {
        this.coordiate = coordinate;
    }

    /**
     * Erzeugt einen neuen Termin in diesem Planelement für den angegebenen Kurs
     * im angegebenen Raum
     *
     * @param room Der Raum
     * @param course Der Kurs
     */
    public void createAppointment(Room room, Course course) {
        appointments.add(new ScheduleAppointment(room, course));
    }

    /**
     * Liefert einen Termin auf diesem Planelement für den gegebenen Dozenten
     *
     * @param academic Der Dozent
     * @return Der Termin der null wenn keiner existiert
     */
    public ScheduleAppointment getAppointment(Academic academic) {
        for (ScheduleAppointment scheduleAppointment : getAppointments()) {
            if (scheduleAppointment.getCourse().getAcademic().equals(academic)) {
                return scheduleAppointment;
            }
        }
        return null;
    }

    /**
     * Liefert einen Termin auf diesem Planelement für den gegebenen Raum
     *
     * @param room Der Raum
     * @return Der Termin der null wenn keiner existiert
     */
    public ScheduleAppointment getAppointment(Room room) {
        for (ScheduleAppointment scheduleAppointment : getAppointments()) {
            if (scheduleAppointment.getRoom().equals(room)) {
                return scheduleAppointment;
            }
        }
        return null;
    }

    /**
     * Liefert einen Termin auf diesem Planelement für den gegebenen Kurs
     *
     * @param course Der Dozent
     * @return Der Termin der null wenn keiner existiert
     */
    public ScheduleAppointment getAppointment(Course course) {
        for (ScheduleAppointment scheduleAppointment : getAppointments()) {
            if (scheduleAppointment.getCourse().equals(course)) {
                return scheduleAppointment;
            }
        }
        return null;
    }

    /**
     * Fügt einen Termin hinzu
     *
     * @param appointment Der Termin
     */
    public void addAppointment(ScheduleAppointment appointment) {
        getAppointments().add(appointment);
    }

    /**
     * Prüft ob diesem Planelement Termine zugewiesen sind
     *
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
     *
     * @return Die Termine
     */
    public List<ScheduleAppointment> getAppointments() {
        return appointments;
    }

}
