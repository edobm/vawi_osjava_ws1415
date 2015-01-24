package osjava.tl3.model.schedule;

import java.util.List;
import osjava.tl3.model.Academic;
import osjava.tl3.model.Course;
import osjava.tl3.model.Room;

/**
 * Stellt ein Element einer Plansicht dar ergänzt um eine Referenz auf die
 * zugehörige Plansicht.
 * Instanzen dieser Klasse werden durch im GUI Modus in der Tabellenansicht
 * und durch den Outputmanager zur Erzeugung der Ausgabedateien verwendet
 * 
 * @author Meikel Bode
 */
public class ScheduleElementViewWrapper implements ScheduleElement {
    
    /**
     * Die Plansicht auf der dieser Wrapper operiert
     */
    private final ScheduleView scheduleView;
    
    /**
     * Das Planelement auf dem dieser Wrapper operiert
     */
    private final ScheduleElement scheduleElement;
    
    /**
     * Erzeugt eine neue Instanz eines Planelementwrappers
     * @param scheduleView Die Plansicht
     * @param scheduleElement  Das Planelement
     */
    public ScheduleElementViewWrapper(ScheduleView scheduleView, ScheduleElement scheduleElement) {
        this.scheduleView = scheduleView;
        this.scheduleElement = scheduleElement;
    }
    
    /**
     * Liefert die Plansicht dieses Elements
     * @return Die Plansicht
     */
    public ScheduleView getScheduleView() {
        return scheduleView;
    }

    /**
     * {@inheritDoc}
     * Wrapper
     */
    @Override
    public void createAppointment(Room room, Course course) {
        scheduleElement.createAppointment(room, course);
    }

    /**
     * {@inheritDoc}
     * Wrapper
     */
    @Override
    public ScheduleAppointment getAppointment(Academic academic) {
        return scheduleElement.getAppointment(academic);
    }

    /**
     * {@inheritDoc}
     * Wrapper
     */
    @Override
    public ScheduleAppointment getAppointment(Room room) {
        return scheduleElement.getAppointment(room);
    }

    /**
     * {@inheritDoc}
     * Wrapper
     */
    @Override
    public ScheduleAppointment getAppointment(Course course) {
        return scheduleElement.getAppointment(course);
    }

    /**
     * {@inheritDoc}
     * Wrapper
     */
    @Override
    public void addAppointment(ScheduleAppointment appointment) {
        scheduleElement.addAppointment(appointment);
    }

    /**
     * {@inheritDoc}
     * Wrapper
     */
    @Override
    public boolean isEmpty() {
        return scheduleElement.isEmpty();
    }

    /**
     * {@inheritDoc}
     * Wrapper
     */
    @Override
    public ScheduleCoordinate getCoordiate() {
        return scheduleElement.getCoordiate();
    }

    /**
     * {@inheritDoc}
     * Wrapper
     */
    @Override
    public List<ScheduleAppointment> getAppointments() {
        return scheduleElement.getAppointments();
    }
    
}