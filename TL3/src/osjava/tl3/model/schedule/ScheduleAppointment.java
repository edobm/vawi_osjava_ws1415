package osjava.tl3.model.schedule;

import java.util.Objects;
import osjava.tl3.model.Course;
import osjava.tl3.model.Room;

/**
 *
 * @author meikelbode
 */
public class ScheduleAppointment {
    
    private final Room room;
    private final Course course;

    public ScheduleAppointment(Room room, Course course) {
        this.room = room;
        this.course = course;
    }

    /**
     * @return the room
     */
    public Room getRoom() {
        return room;
    }

    /**
     * @return the course
     */
    public Course getCourse() {
        return course;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ScheduleAppointment) {
            ScheduleAppointment otherScheduleAppointment = (ScheduleAppointment)other;
            return this.course.equals(otherScheduleAppointment.course) && this.room.equals(otherScheduleAppointment.room);
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.room);
        hash = 79 * hash + Objects.hashCode(this.course);
        return hash;
    }
    
}