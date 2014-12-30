package osjava.tl3.model.schedule;

import osjava.tl3.model.Room;

/**
 *
 * @author meikelbode
 */
public class ScheduleViewRoom extends ScheduleView {

    /**
     * Der Raum auf dem diese Sicht operiert
     */
    private final Room room;

    public ScheduleViewRoom(Room room, ScheduleNew schedule) {
        super(schedule);
        this.room = room;
    }

    @Override
    public ScheduleElementNew getScheduleElement(ScheduleCoordinate coordinate) {
        
        ScheduleElementNew elementFiltered = new ScheduleElementNew(coordinate);
        
        ScheduleAppointment appointment = schedule.getScheduleElement(coordinate).getAppointment(getRoom());
        
        if (appointment != null) {
            elementFiltered.addAppointment(appointment);
        }
        
        return elementFiltered;

    }

    /**
     * Liefert den Raum auf der diese Sicht operiert
     * @return Der Raum
     */
    public Room getRoom() {
        return room;
    }
    
}