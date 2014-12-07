package osjava.tl3.logic.io;

import java.util.List;
import osjava.tl3.model.Schedule;
import osjava.tl3.model.ScheduleType;

/**
 * Steuert die Erzeugung von Plandaten als Dateien.
 *
 * @author
 */
public class OutputController {

    private FileWriter fileWriter;

    /**
     * Gigt einen Schedule im gegebenen Ausgabeformat aus
     *
     * @param schedule Der Plan
     * @param outputFormat Das Format
     * @param outputPath Das Ausgabeverzeichnis
     */
    public void outputSchedule(Schedule schedule, OutputFormat outputFormat, String outputPath) {

        if(schedule.getType() == ScheduleType.ACADAMIC){}
        
    }

    /**
     * Gibt eine Liste von Plänen aus
     * @param schedules Die Pläne
     * @param outputFormat 
     * @param outputPath 
     */
    public void outputSchedules(List<Schedule> schedules, OutputFormat outputFormat, String outputPath) {
        for(Schedule schedule : schedules) {
            outputSchedule(schedule, outputFormat, outputPath);
        }
    }
}
