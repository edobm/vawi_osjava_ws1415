package osjava.tl3.model.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import osjava.tl3.model.Course;
import osjava.tl3.model.Room;
import osjava.tl3.model.Semester;

/**
 * Diese Klasse Repräsentiert einen Termin dar der einer Plankoordinate 
 * zugewiesen wird.
 * Ein Termin besteht immer aus einem Raum und einer Kurs.
 * 
 * @author Meikel Bode
 */
public class ScheduleAppointment {
    
    /**
     * Der Raum dieses Termins
     */
    private final Room room;
    
    /**
     * Der Kurs dieses Termins
     */
    private final Course course;

//    /**
//     * Die Fachsemester, die diesen Kurs tasächlich anbieten werden.
//     * 
//     */
//    private final List<Semester> assignedSemesters = new ArrayList<>();
    
    /**
     * Erzeugt eine neue Termininstanz für den gegebenen Raum und Kurs
     * @param room Der Raum
     * @param course Der Kurs
     */
    public ScheduleAppointment(Room room, Course course) {
        this.room = room;
        this.course = course;
    }

    /**
     * Liefert den Raum
     * @return Der Raum
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Liefert den Kurs
     * @return Der Kurs
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Vergleich zwei Termine auf Gleichheit anhand von Raum und Kurs
     * @param other Der andere Termin
     * @return Ob beide Termine gleich sind
     */
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

    /**
     * Liefert den HashCode
     * @return Der HashCode
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.room);
        hash = 79 * hash + Objects.hashCode(this.course);
        return hash;
    }
    
}