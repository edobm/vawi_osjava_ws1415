package osjava.tl3.logic.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Diese Klasse realisiert die Funktionalität Dateien einlesen zu können.
 * 
 * @author Fabian Simon
 * @version 1.0
 */
public abstract class InputFileReader
{  
    
    /**
     * Diese Methode liest die Daten aus der eingegebenen Datei zeilenweise aus.
     * Pro Zeile werden die Daten als String in einer Liste abgelegt.
     * Diese Liste wird von der Methode zurückgegeben.
     *
     * @param fileName
     * @return Die Liste mit Strings der eingelesenen Daten.
     */
    public ArrayList<String> readFile(String fileName) { 
        
        ArrayList data = new ArrayList<>();
        
        InputStreamReader inputReader = null;
        BufferedReader bufferedReader = null;
        
        try {
            inputReader = new InputStreamReader(getClass().getResourceAsStream("/osjava/tl3/inputfiles/" + fileName));
            bufferedReader = new BufferedReader(inputReader);
            
            while (bufferedReader.ready()){
                String value = bufferedReader.readLine().trim();
                if (value.length() != 0){
                    data.add(value); 
                }
            }
            
            } catch (FileNotFoundException ex) {
            Logger.getLogger(InputFileReader.class.getName()).log(Level.SEVERE, null, ex);
        }   catch (IOException ex) {
            Logger.getLogger(InputFileReader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bufferedReader.close();
            } catch (Exception e) {
            } 
            try {
                inputReader.close();
            } catch (Exception e) {
            }
        } 
                 
        return data;
    }
}
