package osjava.tl3.model.schedule;

import osjava.tl3.model.Room;

/**
 * Der Gesamtplan aus Sicht eines Raumes
 *
 * @author Meikel Bode
 */
public class ScheduleViewRoom extends ScheduleView {

    /**
     * Der Raum auf dem diese Sicht operiert
     */
    private final Room room;

    /**
     * Erzeugt eine neue Instanz der Fachsemestersicht
     *
     * @param room Der Raum auf dem diese Sicht operiert
     * @param schedule Der Plan auf dem diese Sicht operiert
     */
    public ScheduleViewRoom(Room room, Schedule schedule) {
        super(schedule);
        this.room = room;
    }

    /**
     * Liefert die Termine des Planelements auf der gegebenen Koordinate 
     * anhand des Raum. 
     * Nur die Termine für das Filterkrieterium werden zurückgeliefert
     * @param coordinate Die Plankoordinate
     * @return Das gefilterte Planelement 
     */
    @Override
    public ScheduleElement getScheduleElement(ScheduleCoordinate coordinate) {
        
        ScheduleElementImpl elementFiltered = new ScheduleElementImpl(coordinate);
        
        ScheduleAppointment appointment = schedule.getScheduleElement(coordinate).getAppointment(getRoom());
        
        if (appointment != null) {
            elementFiltered.addAppointment(appointment);
        }
        
        return new ScheduleElementViewWrapper(this, elementFiltered);

    }

    /**
     * Liefert den Raum auf der diese Sicht operiert
     * @return Der Raum
     */
    public Room getRoom() {
        return room;
    }
    
}