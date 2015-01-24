package osjava.tl3.logic.io.output;

import osjava.tl3.model.schedule.ScheduleView;
import osjava.tl3.model.schedule.ScheduleViewRoom;

/**
 * Diese Klasse erzeugt die Ausgabe für die Raumpläne
 *
 * @author Fabian Simon
 */
public class RoomScheduleWriter extends OutputFileWriter {

    /**
     * Erzeugt eine Raumplandatei auf Basis der übergebenen Plansicht im
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
        final String fileNamePrefix = "Raumplan_";
        
        /**
         * Prüfung der Eingabe
         */
        if (scheduleView == null) {
            throw new IllegalArgumentException("Die übergebene Plansicht darf nicht null sein!");
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