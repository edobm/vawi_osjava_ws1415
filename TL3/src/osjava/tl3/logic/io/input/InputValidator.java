package osjava.tl3.logic.io.input;

import java.util.List;

/**
 * Validate Eingabedaten aus Dateien
 *
 * @author Meikel Bode
 */
public class InputValidator {

    /**
     * Prüft den gegebenen Wert auf das Vorhandenseins eines Wertes
     * @param colName Der Name der Spalte
     * @param value Der zu prüfende Wert
     * @param recordLine Die aktuelle Dateizeile
     * @param rowNumber Die aktuelle Zeilenummer
     * @throws InputFileReaderException Wenn ein Validierungsfehler auftritt
     */
    public static void validateEmpty(String colName, String value, String recordLine, int rowNumber) throws InputFileReaderException {
        if (value.isEmpty() || value.equals("\"\"")) {
            throw new InputFileReaderException("Ungültiger Wert für Spalte '" + colName + "'. Die Spalte darf nicht leer sein", null, recordLine, rowNumber);
        }
    }

    /**
     * Prüft den gegebenen Wert auf das Vorhandensein von einschließenden, 
     * doppelten Anführungsszeichen
     * @param colName Der Name der Spalte
     * @param value Der zu prüfende Wert
     * @param recordLine Die aktuelle Dateizeile
     * @param rowNumber Die aktuelle Zeilenummer
     * @throws InputFileReaderException Wenn ein Validierungsfehler auftritt
     */
    public static void validateStardEndsWithBraces(String colName, String value, String recordLine, int rowNumber) throws InputFileReaderException {
        if (!(value.startsWith("\"") && value.endsWith("\"")) ){
            throw new InputFileReaderException("Ungültiger Wert für Spalte '" + colName + "'. Der Spaltenwert muss von Anführungszeichen (\") eingefasst sein", null, recordLine, rowNumber);
        }
    }

    /**
     * Prüft ob der angegebene Wert in eine Ganzzahl konvertieren lässt (Integer)
     * @param colName Der Name der Spalte
     * @param value Der zu prüfende Wert
     * @param recordLine Die aktuelle Dateizeile
     * @param rowNumber Die aktuelle Zeilenummer
     * @throws InputFileReaderException Wenn ein Validierungsfehler auftritt
     */
    public static void validateInteger(String colName, String value, String recordLine, int rowNumber) throws InputFileReaderException {
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new InputFileReaderException("Ungültiger Wert für Spalte '" + colName + "'. Die Spaltenwert ist keine Ganzzahl", e, recordLine, rowNumber);

        }
    }

    /**
     * Prüft den angegebenen Wert auf Verwendbarkeit als Equipment
     * @param colName Der Name der Spalte
     * @param value Der zu prüfende Wert
     * @param recordLine Die aktuelle Dateizeile
     * @param rowNumber Die aktuelle Zeilenummer
     * @throws InputFileReaderException Wenn ein Validierungsfehler auftritt
     */
    public static void validateEquipment(String colName, String value, String recordLine, int rowNumber) throws InputFileReaderException {
        final String delimiter = ",";

        validateStardEndsWithBraces(colName, value, recordLine, rowNumber);

        String[] columns = value.split(delimiter);

        for (String column : columns) {
           validateEmpty(colName, column, recordLine, rowNumber);
        }
    }

    /**
     * Prüft ob die Datei Daten enthalten hat
     * @param fileContents Die eingelesenen Zeilen
     * @throws InputFileReaderException InputFileReaderException Wenn ein Validierungsfehler auftritt
     */
    public static void validateLineCount(List<String> fileContents) throws InputFileReaderException {
        if (fileContents == null || fileContents.isEmpty()) {
             throw new InputFileReaderException("Die Dateigabedatei ist leer", null, "", 0);
        }
    }
}