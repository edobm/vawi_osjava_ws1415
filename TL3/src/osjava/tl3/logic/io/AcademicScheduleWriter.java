package osjava.tl3.logic.io;

import osjava.tl3.model.Schedule;
import osjava.tl3.model.ScheduleElement;
import osjava.tl3.model.ScheduleType;

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
     * @param schedule
     * @param outputFormat
     * @param outputPath
     */
    @Override
    public void writeSchedule(Schedule schedule, OutputFormat outputFormat, String outputPath){
        
        /**
         * Prüfung der Eingabe
         */
        if (schedule == null) {
             throw new IllegalArgumentException("Given instance of Schedule must not be null!");
        }
        if (schedule.getType() != ScheduleType.ACADAMIC) {
            throw new IllegalArgumentException("Given instance of Schedule invalid. Type is: " + schedule.getType() + ", expected: " + ScheduleType.ACADAMIC);
        }
        
        /**
         * Die Beschriftung des Dozentenplans ermitteln
         */
        String title = getPrimaryNameElement(schedule);
        
        /**
         * Ausgabe des Schedules an an Vaterklasse delegieren und spezifische Beschriftung übergeben
         */
        writeSchedule(schedule, outputFormat, outputPath, title);
        
    }

    /**
     * Den Dateinamen anhand des Dozenten ermitteln
     * @param schedule Der Plan für den ein Name erzeugt werden soll
     * @return Der Name des Plans
     */
    @Override
    public String getPrimaryNameElement(Schedule schedule) {
        
        String primaryNameElement = null;
        
        for (ScheduleElement scheduleElement : schedule.getScheduleElements()) {
            if (scheduleElement.isBlocked()) {
                primaryNameElement = scheduleElement.getCourse().getAcademic().getName();
                break;
            }
        }
        
        return primaryNameElement == null ? "Unbekannter_Dozent" : primaryNameElement;

    }
   
}