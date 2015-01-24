package osjava.tl3.logic.io.output;

import osjava.tl3.model.schedule.ScheduleView;
import osjava.tl3.model.schedule.ScheduleViewStudyProgram;

/**
 * Diese Klasse erzeugt die Pläne für einen Studiengang
 *
 * @author Fabian Simon
 * @version 1.0
 */
public class StudyProgramScheduleWriter extends OutputFileWriter {

    /**
     * Erzeugt eine Studiengangsplandatei auf Basis der übergebenen Plansicht im
     * angegebenen Format im angegebenen Pfad.
     *
     * @param scheduleView Die Plansicht
     * @param outputFormat Das Ausgabeformat
     * @param outputPath Der Ausgabepfad
     */
    @Override
    public void writeSchedule(ScheduleView scheduleView, OutputFormat outputFormat, String outputPath) {
        
        /**
         * Prefix für Ausgabedateien
         */
        final String fileNamePrefix = "Studiengangsplan_";
        
        /**
         * Prüfung der Eingabe
         */
        if (scheduleView == null) {
            throw new IllegalArgumentException("Given instance of ScheduleView must not be null!");
        }
      
        /**
         * Die Beschriftung des Studiengangplans ermitteln
         */
        String title = getPrimaryNameElement(scheduleView);

        /**
         * Ausgabe des Schedules an an Vaterklasse delegieren und spezifische
         * Beschriftung übergeben
         */
        writeSchedule(scheduleView, outputFormat, outputPath, fileNamePrefix, title);

    }

    @Override
    public String getPrimaryNameElement(ScheduleView scheduleView) {
        return ((ScheduleViewStudyProgram)scheduleView).getStudyProgramm().getName();
    }

    
}
