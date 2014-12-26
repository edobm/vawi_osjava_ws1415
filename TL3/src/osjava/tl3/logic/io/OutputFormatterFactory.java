package osjava.tl3.logic.io;

/**
 * Erzeugt eine Instanz des angerforderten Ausgabeformatierers.
 * @author Fabian Simon
 */
public class OutputFormatterFactory {

    /**
     * Liefert eine Instanz des Default-Formatierer
     * @return Instanz des Formatierers
     */
    public static OutputFormatter getDefaultInstance() {
        
        /**
         * Einen CSV Formatierer (default) liefern
         */
        return getInstance(OutputFormat.CSV_TEXT);
    }
    
    /**
     * Liefert eine Instanz des angeforderderten Formatierers
     * @param outputFormat Das Ausgabeformat für das ein Formatierer geliefert werden soll
     * @return Die Instanz des Formatierers
     */
    public static OutputFormatter getInstance(OutputFormat outputFormat) {
        
        /**
         * In Abhängigkeit des gewünschten Ausgabeformats eine Instanz des 
         * passenden Formatierers erzeugen und zurückgeben
         */
        switch (outputFormat) {
            case HTML:
                return new HTMLOutputFormatter();
            case CSV_TEXT:
            default:
                return new CSVOutputFormatter();
        }
    }
}