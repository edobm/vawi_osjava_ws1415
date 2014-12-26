package osjava.tl3.logic.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import osjava.tl3.model.Schedule;
import osjava.tl3.model.ScheduleCoordinate;
import osjava.tl3.model.ScheduleElement;



/**
 * Diese Klasse stellt die Dateischreiboperationen bereit.
 * 
 * @author Fabian Simon
 * @version 1.0
 */
public abstract class OutputFileWriter
{
    /**
     * Diese Methode erwartet als Eingabewert eine Liste von Strings.
     * Diese wird abgearbeitet und pro String wird eine Zeile in die Zieldatei
     * für die Ausgabe der Daten geschrieben. Dabei entsteht je nach Format der
     * Strings eine CSV- bzw. HTML-Datei
     * 
     * @param output  Die Liste aus Strings, welche in die Datei geschrieben werden sollen
     * @param outputPath Der Ausgabepfad
     */
    public void writeFile(ArrayList<String> output, String outputPath){
        
        FileWriter outputStream = null;
        BufferedWriter outputWriter = null;
        
        try {
            outputStream = new FileWriter(outputPath);
            outputWriter = new BufferedWriter(outputStream);
            
            for (String line : output){
                outputWriter.write(line);
                outputWriter.newLine();
            }
            
        } catch (IOException ex) {
            Logger.getLogger(OutputFileWriter.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                outputWriter.close();
            } catch (Exception e) {
            }
            try {
                outputStream.close();
            } catch (Exception e) {
            }
 
        }
    }
    
    /**
     * Liefert das erste belegte ScheduleElement
     * @param schedule
     * @return Das erste belegte ScheduleElement
     */
    public ScheduleElement getFirstScheduleElement(Schedule schedule){
        ScheduleCoordinate firstBlockedCoodinate = schedule.getBlockedCoordinates().get(0);
        ScheduleElement firstElement = schedule.getScheduleElement(firstBlockedCoodinate);
        return firstElement;
    }
    
}
