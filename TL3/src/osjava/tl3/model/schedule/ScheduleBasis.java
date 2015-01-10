package osjava.tl3.model.schedule;

import java.util.ArrayList;
import java.util.List;

/**
 * Stellt grundlengende Plandaten und Methoden bereit
 * 
 * @author Meikel Bode
 */
public abstract class ScheduleBasis {
    
    /**
     * Anzahl von Tagen und Zeitfenstern
     */
    private static final int days = 5;
    private static final int timeSlots = 5;

    /**
     * Die Liste der Plankoordinaten
     */
    private static final List<ScheduleCoordinate> possibleScheduleCoordinates = new ArrayList<>();

    /**
     * Statischer Konstruktor: Baut die Liste der Plankoordinaten auf
     */
    static {
        for (int day = 0; day < days; day++) {
            for (int timeSlot = 0; timeSlot < timeSlots; timeSlot++) {
                possibleScheduleCoordinates.add(new ScheduleCoordinate(Day.valueOf(day), TimeSlot.valueOf(timeSlot)));
            }
        }
    }

    /**
     * Liefert eine neue Liste aller Plankoordinaten
     * @return Die Liste aller Plankoordinaten
     */
    public static final List<ScheduleCoordinate> getPossibleScheduleCoordinates() {
        return new ArrayList<>(possibleScheduleCoordinates);
    }
    
}