package osjava.tl3.logic.io.output;

import osjava.tl3.model.schedule.ScheduleView;
import osjava.tl3.model.schedule.ScheduleViewSemester;

/**
 * Diese Klasse erzeugt die Pläne für ein Fachsemester
 *
 * @author Fabian Simon
 */
public class SemesterScheduleWriter extends OutputFileWriter {

   /**
     * Erzeugt eine Semesterplandatei auf Basis der übergebenen Plansicht im
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
        final String fileNamePrefix = "Fachsemesterplan_";
        
        /**
         * Prüfung der Eingabe
         */
        if (scheduleView == null) {
            throw new IllegalArgumentException("Die übergebene Plansicht darf nicht null sein!");
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
    
    /**
     * Liefert das primäre Namenselement für die Plansicht
     * @param scheduleView Die Plansicht
     * @return Das primäre Namenslement
     */
    @Override
    public String getPrimaryNameElement(ScheduleView scheduleView) {
        
        ScheduleViewSemester viewSemester = (ScheduleViewSemester)scheduleView;
        
        StringBuilder sb = new StringBuilder();
        
        sb.append(viewSemester.getSemester().getStudyProgram().getName());
        sb.append("_");
        sb.append(viewSemester.getSemester().getName());
        
        return sb.toString();
    }

    
}
