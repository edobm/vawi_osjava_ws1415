package osjava.tl3.logic.io;

import java.util.ArrayList;
import java.util.List;


/**
 * Abstract class BasicFileReader
 * 
 * Diese Klasse realisiert die Funktionalität Dateien einlesen zu können.
 * 
 * @author Fabian Simon
 * @version 1.0
 */
public abstract class FileReader
{  
    public List<String> readFile(String fileName) {
        return new ArrayList<String>();
    }
    
}
