package osjava.tl3.logic.io.input;

/**
 * Wird ausgelöst, wenn ein Reader nicht beim Einlesen einer Datei 
 * Fehler feststellt
 * 
 * @author Meikel Bode
 */
public class InputFileReaderException extends Exception {
    
    /**
     * Der fehlerhafte Wert
     */
    private final String value;
    private final int row;
    
    /**
     * Erzeugt eine neue Exception
     * @param message Die Fehlermeldung
     * @param cause Ein möglicherweise aufgetretener Fehler
     * @param value Der fehlerhafte Wert
     * @param row Der Zeilenindex
     */
    public InputFileReaderException(String message, Throwable cause, String value, int row) {
        super(message, cause);
        this.value = value;
        this.row = row;
    }

    /**
     * Liefert den fehlerhaften Wert
     * @return Der fehlerhafte Wert
     */
    public String getValue() {
        return value;
    }

    /**
     * Liefert die Fehlerhafte Zeile oder -1 wenn unbekannt
     * @return die Fehlerhafte Zeile
     */
    public int getRow() {
        return row;
    }
    
}