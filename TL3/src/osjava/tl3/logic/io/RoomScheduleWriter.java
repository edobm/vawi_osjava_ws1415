package osjava.tl3.logic.io;

import osjava.tl3.model.Schedule;



/**
 * Diese Klasse erzeugt die Ausgabe für die Raumpläne
 * 
 * @author Fabian Simon
 * @version 1.0
 */
public class RoomScheduleWriter extends FileWriter
{
    @Override
    public String getPrimaryNameElement(Schedule schedule) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

     
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}