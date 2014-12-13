package osjava.tl3.logic.io;

import java.util.List;
import osjava.tl3.model.Schedule;
import osjava.tl3.model.ScheduleType;

/**
 * Steuert die Erzeugung von Plandaten als Dateien.
 *
 * @author Fabian Simon
 */
public class OutputController {

    private FileWriter fileWriter;

    /**
     * Gibt einen Schedule im gegebenen Ausgabeformat in den gegebenen Pfad aus
     *
     * @param schedule Der Plan
     * @param outputFormat Das Format
     * @param outputPath Das Ausgabeverzeichnis
     */
    public void outputSchedule(Schedule schedule, OutputFormat outputFormat, String outputPath) {

        if(schedule.getType() == ScheduleType.ACADAMIC){
            AcademicScheduleWriter.writeAcademicSchedule(schedule, outputFormat, outputPath);
        } else if (schedule.getType() == ScheduleType.STUDY_PROGRAM){
            StudyProgramScheduleWriter.writeStudyProgramSchedule(schedule, outputFormat, outputPath);
        } else if (schedule.getType() == ScheduleType.ROOM_INTERNAL){
            RoomScheduleWriter.writeRoomSchedule(schedule, outputFormat, outputPath);
        } else if (schedule.getType() == ScheduleType.ROOM_EXTERNAL){
            RoomScheduleWriter.writeRoomSchedule(schedule, outputFormat, outputPath);
        } else {
            // @TODO: Ausgabe Fehler
        }
        
    }

    /**
     * Gibt eine Liste von Plänen aus
     * 
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
