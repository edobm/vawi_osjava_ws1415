package osjava.tl3.model.schedule;

import java.util.Objects;
import osjava.tl3.model.Day;
import osjava.tl3.model.TimeSlot;

/**
 * Diese Klasse stellt eine eindeutig bestimmte Koordinate in einem Wochenplan
 * dar, wie beispielsweise (MONDAY;0800) oder (WEDNESDAY;1400)
 *
 * @author Meikel Bode
 */
public class ScheduleCoordinate {

    /**
     * Die Tag-Komponente der Koordinate
     */
    private final Day day;

    /**
     * Die Zeitraum-Komponente der Koordinate
     */
    private final TimeSlot timeSlot;

    /**
     * Erzeugt eine Plankoordinate
     *
     * @param day Die Tag-Komponente der Koordinate
     * @param timeSlot Die Zeitraum-Komponente der Koordinate
     */
    public ScheduleCoordinate(Day day, TimeSlot timeSlot) {
        this.day = day;
        this.timeSlot = timeSlot;
    }

    /**
     * Liefert die Tag-Komponente der Koordinate
     *
     * @return Die Tag-Komponente
     */
    public Day getDay() {
        return day;
    }

    /**
     * Liefert die Zeitraum-Komponente der Koordinate
     *
     * @return Die Zeitraum-Komponente der Koordinate
     */
    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    /**
     * Prüft auf Gleichheit. Diese liegt vor, wenn Tag- und Zeitraum-Komponente
     * identisch sind
     *
     * @param obj Die andere Plankoordinate
     * @return Ja oder Nein
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ScheduleCoordinate) {
            ScheduleCoordinate other = (ScheduleCoordinate) obj;
            return this.day == other.day && this.timeSlot == other.timeSlot;
        } else {
            return false;
        }
    }

    /**
     * Berechnet den HashCode
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + Objects.hashCode(this.day);
        hash = 73 * hash + Objects.hashCode(this.timeSlot);
        return hash;
    }

    /**
     * Die String-Darstellung
     *
     * @return Die String-Darstellung
     */
    @Override
    public String toString() {
        return "[" + day + ";" + timeSlot + "]";
    }

}
