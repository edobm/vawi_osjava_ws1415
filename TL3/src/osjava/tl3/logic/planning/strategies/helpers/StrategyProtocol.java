package osjava.tl3.logic.planning.strategies.helpers;

/**
 * Sammelt Daten über die Strategieausführung und den erzeugten Gesamtplan
 * 
 * @author Meikel Bode
 */
public class StrategyProtocol {
    
    /**
     * Stringbuffer zur Aufnahme des Protokolls
     */
    private static final StringBuffer protocol = new StringBuffer();

    /**
     * Statischer Konstruktor
     */
    static {
        log("Protokoll erzeugt.");
    }

    /**
     * Fügt einen Logeintrag hinzu
     * @param message Der neue Logeintrag
     */
    public static void log(String message) {
        protocol.append("[").append(System.nanoTime()).append("] ").append(message).append("\n");
    }
    
    /**
     * Liefert das Protokoll
     * @return Das Protokoll
     */
    public static String getProtocol() {
        return protocol.toString();
    }
    
    /**
     * Setzt das Protokoll zurück
     */
    public static void reset() {
        protocol.setLength(0);
    }
}