package osjava.tl3.logic.io.output;

import osjava.tl3.model.schedule.ScheduleView;
import osjava.tl3.model.schedule.ScheduleViewSemester;
import osjava.tl3.model.schedule.ScheduleViewStudyProgram;

/**
 * Diese Klasse erzeugt die Pläne für ein Fachsemester
 *
 * @author Fabian Simon
 * @version 1.0
 */
public class SemesterScheduleWriter extends OutputFileWriter {

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
