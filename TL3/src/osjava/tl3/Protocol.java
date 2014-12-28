package osjava.tl3;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Sammelt Daten über die Strategieausführung und den erzeugten Gesamtplan
 * Die Klasse ist als Singleton implementiert.
 * 
 * @author Meikel Bode
 */
public class Protocol extends Observable {

    /**
     * Singleton des Protokolls
     */
    private static final Protocol instance;

    /**
     * Statischer Konstruktor
     */
    static {
        instance = new Protocol();
    }

    /**
     * Liefert die Instanz des Singletons
     * @return Die Instanz
     */
    public static Protocol getInstance() {
        return instance;
    }

    /**
     * Fügt einen Logeintrag hinzu
     *
     * @param message Der neue Logeintrag
     */
    public static void log(String message) {

        instance.addMessage(message);

    }
    
    /**
     * List<String> zur Aufnahme des Protokolls
     */
    private final List<String> protocol = new ArrayList<>();

    /**
     * Fügt einen Logeintrag hinzu
     *
     * @param message Der neue Logeintrag
     */
    private void addMessage(String message) {

        protocol.add("[" + System.nanoTime() + "] " + message);

        setChanged();
        notifyObservers(protocol.get(protocol.size() - 1));

    }

}