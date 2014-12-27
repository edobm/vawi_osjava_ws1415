package osjava.tl3.logic.io;

import osjava.tl3.model.Schedule;
import osjava.tl3.model.ScheduleElement;
import osjava.tl3.model.ScheduleType;

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
     * @param schedule
     * @param outputFormat
     * @param outputPath
     */
    @Override
    public void writeSchedule(Schedule schedule, OutputFormat outputFormat, String outputPath) {
        
        /**
         * Prefix für Ausgabedateien
         */
        final String fileNamePrefix = "Raumplan_";
        
        /**
         * Prüfung der Eingabe
         */
        if (schedule == null) {
            throw new IllegalArgumentException("Given instance of Schedule must not be null!");
        }
        if (schedule.getType() != ScheduleType.ROOM_INTERNAL
                && schedule.getType() != ScheduleType.ROOM_EXTERNAL) {
            throw new IllegalArgumentException("Given instance of Schedule invalid. Type is: " + schedule.getType()
                    + ", expected: " + ScheduleType.ROOM_INTERNAL + " or " + ScheduleType.ROOM_EXTERNAL);
        }

        /**
         * Die Beschriftung des Raumplans ermitteln
         */
        String title = getPrimaryNameElement(schedule);

        /**
         * Ausgabe des Schedules an an Vaterklasse delegieren und spezifische
         * Beschriftung übergeben
         */
        writeSchedule(schedule, outputFormat, outputPath, fileNamePrefix, title);

    }

    @Override
    public String getPrimaryNameElement(Schedule schedule) {
        return schedule.getRoom().getName();
    }
}