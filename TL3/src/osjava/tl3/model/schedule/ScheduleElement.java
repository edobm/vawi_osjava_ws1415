package osjava.tl3.model.schedule;

import java.util.List;
import osjava.tl3.model.Academic;
import osjava.tl3.model.Course;
import osjava.tl3.model.Room;

/**
 * Stellt Methoden für den Zugriff auf Planelementdaten bereit
 *
 * @author Meikel Bode
 */
public interface ScheduleElement {

    /**
     * Erzeugt einen neuen Termin in diesem Planelement für den angegebenen Kurs
     * im angegebenen Raum
     *
     * @param room Der Raum
     * @param course Der Kurs
     */
    void createAppointment(Room room, Course course);

    /**
     * Liefert einen Termin auf diesem Planelement für den gegebenen Dozenten
     *
     * @param academic Der Dozent
     * @return Der Termin der null wenn keiner existiert
     */
    ScheduleAppointment getAppointment(Academic academic);

    /**
     * Liefert einen Termin auf diesem Planelement für den gegebenen Raum
     *
     * @param room Der Raum
     * @return Der Termin der null wenn keiner existiert
     */
    ScheduleAppointment getAppointment(Room room);

    /**
     * Liefert einen Termin auf diesem Planelement für den gegebenen Kurs
     *
     * @param course Der Dozent
     * @return Der Termin der null wenn keiner existiert
     */
    ScheduleAppointment getAppointment(Course course);

    /**
     * Fügt einen Termin hinzu
     *
     * @param appointment Der Termin
     */
    void addAppointment(ScheduleAppointment appointment);

    /**
     * Prüft ob diesem Planelement Termine zugewiesen sind
     *
     * @return Ja oder Nein
     */
    boolean isEmpty();

    /**
     * Setzt die Plankoordinate
     *
     * @return Die Plankoordinate
     */
    ScheduleCoordinate getCoordiate();

    /**
     * Liefert die Termine dieses Elements
     *
     * @return Die Termine
     */
    List<ScheduleAppointment> getAppointments();

}