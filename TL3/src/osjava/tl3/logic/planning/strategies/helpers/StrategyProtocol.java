package osjava.tl3.logic.planning.strategies.helpers;

/**
 * Sammelt Daten über die Strategieausführung und den erzeugten Gesamtplan
 * 
 * @author Meikel Bode
 */
public class StrategyProtocol {
    
    private static StringBuffer protocol = new StringBuffer();

    static {
        log("Protokoll erzeugt.");
    }

    public static void log(String message) {
        protocol.append("[").append(System.nanoTime()).append("] ").append(message).append("\n");
    }
    
    public static String getProtocol() {
        return protocol.toString();
    }
}
