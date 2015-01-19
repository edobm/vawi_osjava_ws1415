package osjava.tl3.logging;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;

/**
 * Sammelt Daten über die Strategieausführung und den erzeugten Gesamtplan.
 * Intern hält die Klasse eine Instanz von sich selbst. Über statische Methoden
 * können Log-Nachrichten an die intern gehaltene Instanz übergeben werden.
 * 
 * Die Klasse implementiert das Observer-Pattern und erweitert aus diesem 
 * Grund die Klasse Observable. Damit können sich das Interface Observer 
 * implementierende Klasse bei dieser Klasse registrieren, um die neuesten 
 * Protokolleinträge zugewiesen zu bekommen.
 * 
 * @see Observable
 * 
 * @author Meikel Bode
 * 
 */
public class Protocol extends Observable {
    
    /**
     * Formatiert den Zeitpunkt eines Protkolleintrages
     */
    private static final SimpleDateFormat timeFormat;

    /**
     * Singleton des Protokolls
     */
    private static final Protocol instance;

    /**
     * Statischer Konstruktor
     */
    static {
        instance = new Protocol();
        timeFormat = new SimpleDateFormat("HH:mm:ss:SSS");
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
     * Fügt einen Logeintrag hinzu und übergibt ihn zugleich an registrierte
     * Oberserver
     *
     * @param message Der neue Logeintrag
     */
    private void addMessage(String message) {

        protocol.add("[" + timeFormat.format(new Date()) + "] " + message);

        setChanged();
        notifyObservers(protocol.get(protocol.size() - 1));

    }

}