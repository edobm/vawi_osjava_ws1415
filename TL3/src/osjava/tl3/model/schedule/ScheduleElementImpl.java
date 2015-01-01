package osjava.tl3.model.schedule;

import java.util.ArrayList;
import java.util.List;
import osjava.tl3.model.Academic;
import osjava.tl3.model.Course;
import osjava.tl3.model.Room;

/**
 * Diese Klasse repräsentiert ein Element des Terminplans. 
 * Sie implementiert das Interface ScheduleELementInterface. 
 * Dokumentation siehe Interface.
 * 
 * Jedes Element ist einer bestimmten Plankoordinate zugeordnet. Jedes Planelement 
 * kann beliebig viele Termine (ScheduleAppointments) in Form einer Kombination
 * von Raum und Kurs beinhalten.
 *
 * @author Meikel Bode
 */
public class ScheduleElementImpl implements ScheduleElement {

    /**
     * Die Koordinate dieses Planelements
     */
    private final ScheduleCoordinate coordiate;

    /**
     * Die Liste von Terminen die diesem ScheduleElementImpl zugewiesen sind
     */
    private final List<ScheduleAppointment> appointments = new ArrayList<>();
    
    /**
     * Erzeugt eine neue Instanz für die gegebene
     *
     * @param coordinate
     */
    public ScheduleElementImpl(ScheduleCoordinate coordinate) {
        this.coordiate = coordinate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createAppointment(Room room, Course course) {
        appointments.add(new ScheduleAppointment(room, course));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScheduleAppointment getAppointment(Academic academic) {
        for (ScheduleAppointment scheduleAppointment : getAppointments()) {
            if (scheduleAppointment.getCourse().getAcademic().equals(academic)) {
                return scheduleAppointment;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScheduleAppointment getAppointment(Room room) {
        for (ScheduleAppointment scheduleAppointment : getAppointments()) {
            if (scheduleAppointment.getRoom().equals(room)) {
                return scheduleAppointment;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScheduleAppointment getAppointment(Course course) {
        for (ScheduleAppointment scheduleAppointment : getAppointments()) {
            if (scheduleAppointment.getCourse().equals(course)) {
                return scheduleAppointment;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addAppointment(ScheduleAppointment appointment) {
        getAppointments().add(appointment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return getAppointments().isEmpty();
    }

   /**
     * {@inheritDoc}
     */
    @Override
    public ScheduleCoordinate getCoordiate() {
        return coordiate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ScheduleAppointment> getAppointments() {
        return appointments;
    }

}