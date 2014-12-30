package osjava.tl3.model.schedule;

import java.util.ArrayList;
import java.util.List;
import osjava.tl3.model.Day;
import osjava.tl3.model.TimeSlot;

/**
 *
 * @author meikelbode
 */
public class ScheduleBasis {

    protected static final List<ScheduleCoordinate> possibleScheduleCoordinates = new ArrayList<>(25);

    static {
        for (int day = 0; day < 5; day++) {
            for (int timeSlot = 0; timeSlot < 5; timeSlot++) {
                possibleScheduleCoordinates.add(new ScheduleCoordinate(Day.valueOf(day), TimeSlot.valueOf(timeSlot)));
            }
        }
    }

    public static final List<ScheduleCoordinate> getPossibleScheduleCoordinates() {
        return possibleScheduleCoordinates;
    }
}
