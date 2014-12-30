package osjava.tl3.model.schedule;

import osjava.tl3.model.Course;
import osjava.tl3.model.Room;

/**
 *
 * @author meikelbode
 */
public class ScheduleViewCourse extends ScheduleView {

    /**
     * Der Raum auf dem diese Sicht operiert
     */
    private final Course course;

    public ScheduleViewCourse(Course course, ScheduleNew schedule) {
        super(schedule);
        this.course = course;
    }

    @Override
    public ScheduleElementNew getScheduleElement(ScheduleCoordinate coordinate) {
        
        ScheduleElementNew elementFiltered = new ScheduleElementNew(coordinate);
        
        ScheduleAppointment appointment = schedule.getScheduleElement(coordinate).getAppointment(course);
        
        if (appointment != null) {
            elementFiltered.addAppointment(appointment);
        }
        
        return elementFiltered;

    }

    /**
     * Liefert den Kurs auf der diese Sicht operiert
     * @return Der Raum
     */
    public Course getCourse() {
        return course;
    }
    
}