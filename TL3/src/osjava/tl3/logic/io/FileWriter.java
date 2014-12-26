package osjava.tl3.logic.io;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import osjava.tl3.model.Schedule;



/**
 * Diese Klasse stellt die Dateischreiboperationen bereit.
 * 
 * @author Fabian Simon
 * @version 1.0
 */
public abstract class FileWriter
{
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd-HHmmss");
    
    /**
     * Diese Methode erwartet als Eingabewert eine Liste von Strings.
     * Diese wird abgearbeitet und pro String wird eine Zeile in die Zieldatei
     * für die Ausgabe der Daten geschrieben.
     * 
     * @param output Der String der in die Datei geschrieben werden soll
     * @param outputPath Ausgabepfad
     */
    public void writeFile(String output, String outputPath){
        
    }
    
    
    
}
