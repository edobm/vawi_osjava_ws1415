package osjava.tl3.logic.io.input;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import osjava.tl3.model.Equipment;

/**
 * Diese Klasse realisiert die Funktionalität Dateien einlesen zu können.
 *
 * @author Fabian Simon
 */
public abstract class InputFileReader {

    /**
     * Diese Methode liest die Daten aus der eingegebenen Datei zeilenweise aus.
     * Pro Zeile werden die Daten als String in einer Liste abgelegt. Diese
     * Liste wird von der Methode zurückgegeben.
     *
     * @param fileName Der Dateiname
     * @return Die Liste mit Strings der eingelesenen Daten.
     */
    public ArrayList<String> readFile(String fileName) {

        ArrayList data = new ArrayList<>();

        /**
         * Try with resources zum automatischen schließen
         */
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {

            while (bufferedReader.ready()) {
                String value = bufferedReader.readLine().trim();
                if (value.length() != 0) {

                    data.add(value);
                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(InputFileReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(InputFileReader.class.getName()).log(Level.SEVERE, null, ex);

        }

        return data;
    }

    /**
     * Verarbeitet einen String von Ausstattungselementen und liefert 
     * eine Liste von Equipement Instanzen
     * @param data Der String mit den Ausstattungsgegenständen
     * @return Die Liste der erzeugten Equipments
     */
    public List<Equipment> parseEquipments(String data) {
        List<Equipment> equipments = new ArrayList<>();

        data = removeQuotationMarks(data);
        data = data.trim();

        if (data != null) {
            String[] columns = data.split(",");

            for (String equipmentString : columns) {
                Equipment equipment = new Equipment(equipmentString.trim());
                equipments.add(equipment);
            }
        }

        return equipments;
    }

    /**
     * Entfernt die Anführungszeichen eines Strings
     *
     * @param inString Eingabestring mit Anführungszeichen
     * @return Eingabestring ohne Anführungszeichen
     */
    public String removeQuotationMarks(String inString) {
        if (inString != null) {
            return inString.trim().replaceAll("\"", "");
        } else {
            return "";
        }
    }

    /**
     * Validatiert einen Datensatz des jeweiligen Implemntierungstyps
     *
     * @param rowNumber Die aktuelle Zeilenzahl
     * @param recordLine Die aktuelle Zeile aus der Datei
     * @return Der Datensatz zerlegt in Spalten
     * @throws InputFileReaderException Wenn ein Validierungsfehler auftritt
     */
    protected abstract String[] validateRecord(int rowNumber, String recordLine) throws InputFileReaderException;

}
