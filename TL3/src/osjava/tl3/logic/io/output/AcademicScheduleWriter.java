package osjava.tl3.logic.io.output;

import osjava.tl3.model.schedule.ScheduleView;
import osjava.tl3.model.schedule.ScheduleViewAcademic;


/**
 * Diese Klasse erzeugt den Stundenplan für Dozenten
 * 
 * @author Fabian Simon
 * @version 1.0
 */
public class AcademicScheduleWriter extends OutputFileWriter
{

    /**
     * Umwandeln des gegebenen Schedules in eine Liste aus Strings und Ausgabe 
     * eines Dozentenplanes im angegebenen Format in den angegebenen Pfad.
     *
     * @param scheduleView
     * @param outputFormat
     * @param outputPath
     */
    @Override
    public void writeSchedule(ScheduleView scheduleView, OutputFormat outputFormat, String outputPath){
        /**
         * Prefix für Ausgabedateien
         */
        final String fileNamePrefix = "Dozentenplan_";
        
        /**
         * Prüfung der Eingabe
         */
        if (scheduleView == null) {
             throw new IllegalArgumentException("Given instance of Schedule must not be null!");
        }
        
        /**
         * Die Beschriftung des Dozentenplans ermitteln
         */
        String title = getPrimaryNameElement(scheduleView);
        
        /**
         * Ausgabe des Schedules an an Vaterklasse delegieren und spezifische Beschriftung übergeben
         */
        writeSchedule(scheduleView, outputFormat, outputPath, fileNamePrefix, title);
        
    }

    /**
     * Den Dateinamen anhand des Dozenten ermitteln
     * @param scheduleView Die Plansicht für den ein Name erzeugt werden soll
     * @return Der Name des Plans
     */
    @Override
    public String getPrimaryNameElement(ScheduleView scheduleView) {
       return ((ScheduleViewAcademic)scheduleView).getAcademic().getName();
    }
   
}