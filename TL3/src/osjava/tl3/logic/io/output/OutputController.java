package osjava.tl3.logic.io.output;

import java.util.List;
import osjava.tl3.model.Schedule;

/**
 * Steuert die Erzeugung von Plandaten als Dateien.
 *
 * @author Fabian Simon
 */
public class OutputController {

    private OutputFileWriter fileWriter;

    /**
     * Gibt einen Schedule im gegebenen Ausgabeformat in den gegebenen Pfad aus
     *
     * @param schedule Der Plan
     * @param outputFormat Das Format
     * @param outputPath Das Ausgabeverzeichnis
     */
    public void outputSchedule(Schedule schedule, OutputFormat outputFormat, String outputPath) {

        /**
         * Anhand des Plantyps den passenden FileWriter erzeugen
         */
        fileWriter = OutputFileWriterFactory.getInstance(schedule.getType());

        /**
         * Mittels des FileWriters den Plan im gewünschten Ausgabeformat im gegebenen Pfad
         * ausgeben
         */
        fileWriter.writeSchedule(schedule, outputFormat, outputPath);
    }

    /**
     * Gibt eine Liste von Plänen aus
     *
     * @param schedules Die Pläne
     * @param outputFormat
     * @param outputPath
     */
    public void outputSchedules(List<Schedule> schedules, OutputFormat outputFormat, String outputPath) {
        for (Schedule schedule : schedules) {
            outputSchedule(schedule, outputFormat, outputPath);
        }
    }
}