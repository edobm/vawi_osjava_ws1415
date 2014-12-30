package osjava.tl3.logic.io.output;

import osjava.tl3.model.schedule.ScheduleView;
import osjava.tl3.model.schedule.ScheduleViewRoom;

/**
 * Diese Klasse erzeugt die Ausgabe für die Raumpläne
 *
 * @author Fabian Simon
 * @version 1.0
 */
public class RoomScheduleWriter extends OutputFileWriter {

    /**
     * Umwandeln des gegebenen Schedules in eine Liste aus Strings und Ausgabe
     * eines Raumplanes im angegebenen Format in den angegebenen Pfad.
     *
     * @param scheduleView
     * @param outputFormat
     * @param outputPath
     */
    @Override
    public void writeSchedule(ScheduleView scheduleView, OutputFormat outputFormat, String outputPath) {
        
        /**
         * Prefix für Ausgabedateien
         */
        final String fileNamePrefix = "Raumplan_";
        
        /**
         * Prüfung der Eingabe
         */
        if (scheduleView == null) {
            throw new IllegalArgumentException("Given instance of Schedule must not be null!");
        }
        

        /**
         * Die Beschriftung des Raumplans ermitteln
         */
        String title = getPrimaryNameElement(scheduleView);

        /**
         * Ausgabe des Schedules an an Vaterklasse delegieren und spezifische
         * Beschriftung übergeben
         */
        writeSchedule(scheduleView, outputFormat, outputPath, fileNamePrefix, title);

    }

    /**
     * Liefert das primäre Namenselement für eine Raumsicht
     * @param scheduleView Die Plansicht
     * @return Das primäre Namenselement
     */
    @Override
    public String getPrimaryNameElement(ScheduleView scheduleView) {
        return ((ScheduleViewRoom)scheduleView).getRoom().getName();
    }
    
}