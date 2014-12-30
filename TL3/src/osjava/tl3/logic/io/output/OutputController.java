package osjava.tl3.logic.io.output;

import java.util.List;
import osjava.tl3.model.schedule.ScheduleView;

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
     * @param scheduleView Die Plansicht
     * @param outputFormat Das Format
     * @param outputPath Das Ausgabeverzeichnis
     */
    public void outputSchedule(ScheduleView scheduleView, OutputFormat outputFormat, String outputPath) {

        /**
         * Anhand des Plantyps den passenden FileWriter erzeugen
         */
        fileWriter = OutputFileWriterFactory.getInstance(scheduleView);

        /**
         * Mittels des FileWriters den Plan im gewünschten Ausgabeformat im gegebenen Pfad
         * ausgeben
         */
        fileWriter.writeSchedule(scheduleView, outputFormat, outputPath);
    }

    /**
     * Gibt eine Liste von Plänen aus
     *
     * @param scheduleViews Die auszugebenen Plansichten
     * @param outputFormat Das Ausgabeformat
     * @param outputPath der Ausgabepfad
     */
    public void outputSchedules(List<ScheduleView> scheduleViews, OutputFormat outputFormat, String outputPath) {
        for (ScheduleView schedule : scheduleViews) {
            outputSchedule(schedule, outputFormat, outputPath);
        }
    }
}