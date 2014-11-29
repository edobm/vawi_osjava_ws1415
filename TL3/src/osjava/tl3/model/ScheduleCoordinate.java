package osjava.tl3.model;

/**
 * Diese Klasse stellt eine eindeutig bestimmte Koordinate in einem 
 * Wochenplan dar, wie beispielsweise (MO;0800) oder (MI;1400)
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
     * @param day Die Tag-Komponente der Koordinate
     * @param timeSlot Die Zeitraum-Komponente der Koordinate
     */
    public ScheduleCoordinate(Day day, TimeSlot timeSlot) {
        this.day = day;
        this.timeSlot = timeSlot;
    }
    
    /**
     * @return Die Tag-Komponente der Koordinate
     */
    public Day getDay() {
        return day;
    }

    /**
     * @return Die Zeitraum-Komponente der Koordinate
     */
    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    /**
     * Prüft auf Gleichheit. 
     * Diese liegt vor, wenn Tag- und Zeitraum-Komponente identisch sind
     * @param obj Die andere Plankoordinate
     * @return Ja oder Nein
     */
    @Override
    public boolean equals(Object obj) {
        ScheduleCoordinate other = (ScheduleCoordinate)obj;
        
        return this.day == other.day && this.timeSlot == other.timeSlot;
    }

    @Override
    public String toString() {
        return "[" + day + ";" + timeSlot + "]";
    }
    
    
}
