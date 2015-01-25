package osjava.tl3;

import java.io.File;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import osjava.tl3.console.SchedulerConsole;
import osjava.tl3.gui.SchedulerUI;
import osjava.tl3.logging.Protocol;

/**
 * Diese Klasse stellt mit der Main-Methode den Einstiegspunkt in das
 * Klassensystem dar. 
 * 
 * Es werden zwei Laufzeitmodi unterschieden: "gui" und "console".
 * Wird das Programm ohne parameter gestartet, wird der graphische Modus gestartet.
 * 
 * Die Klasse implementiert das Observer/Oberservable Pattern. Sie registriert sich 
 * bei der Klasse Protocol als Observer und erhält damit alle während der 
 * Laufzeit erzeugten Programmausgaben und gibt diese auf der Konsole aus.
 * Wird der Konsolenmodus gestartet deregistriert sich diese Klasse als Observer,
 * da diese Aufgabe durch den Konsolenmodus übernommen wird.
 * 
 * @author Christian Müller
 */
public final class SchedulePlanner implements Observer {

    /**
     * Die Willkommensnachricht
     */
    private static final String[] welcomeMessage = new String[]{
        "Schedule Planner: VAWi WS14/15 OSJAVA TL3 (Gruppe 1: Christoph Lurz, Christian Müller, Fabian Simon, Meikel Bode)",
        "Parameter -help für Hilfe!"};

    /**
     * Erlaubte Laufzeitparameter
     */
    private static final String[] parameterKeys = new String[]{
        "help", "mode", "roomfiles", "coursefiles", "studyprogramfiles",
        "out", "format", "strategy", "seatcosts"};

    /**
     * Die vorbereiteten Laufzeitparameter
     */
    private final HashMap<String, String> parameters = new HashMap<>();

    
    /**
     * Die Main-Methode und zugleich der Einstiegspunkt in das Klassensystem
     *
     * @param argumentVector Der Argumentenvektor mit Laufzeitparametern
     */
    public static void main(String[] argumentVector) {

        /**
         * Instanz von SchedulePlanner erzeugen und ausführen
         */
        SchedulePlanner schedulePlanner = new SchedulePlanner(argumentVector);
        schedulePlanner.execute();

    }

    /**
     * Erzeugt eine neue Instanz von SchedulePlanner
     *
     * @param argumentVector Die Laufzeitparameter
     */
    public SchedulePlanner(String[] argumentVector) {

        /**
         * Als Observer registrieren
         */
        Protocol.getInstance().addObserver(SchedulePlanner.this);

        /**
         * Willkommensnachricht ausgeben
         */
        for (String message : welcomeMessage) {
            Protocol.log(message);
        }

        /* 
         * Argumenten Vektor prozessieren, validieren und
         * Instanz des SchedulePlanners erzeugen
         */
        parseArgumentVector(argumentVector);

    }

    /**
     * Gibt Eine Hilfe zur Bedienung des Programms auf der Konsole aus
     */
    private static void printExecutionHint() {

        Protocol.log("Parameter:");
        Protocol.log(" -mode=<gui>|console\t\t\t- Laufmodus des Programms GUI oder Konsole");
        Protocol.log("");
        Protocol.log("GUI-Modus erfordert keine weiteren Parameter!");
        Protocol.log("");
        Protocol.log("Konsolenmodus erfordert folgende Parameter:");
        Protocol.log(" -roomfiles=Datei1,Datei2, ...\t\t- Die Raumdateien");
        Protocol.log(" -coursefiles=Datei1,Datei2, ...\t\t- Die Kursdateien");
        Protocol.log(" -studyprogramfiles=Datei1,Datei2, ...\t- Die Studiengangsdateien");
        Protocol.log(" -out=Ausgabeverzeichnis\t\t\t- Das Verzeichnis in das die Ausgabedateien geschrieben werden sollen");
        Protocol.log(" -format=<csv_text>|html\t\t\t- Das Format in dem die Ausgabedateien erzeugt werden sollen");
        Protocol.log(" -strategy=<CostOptimizedStrategy>\t- Die Planungsstrategie");
        Protocol.log(" -seatcosts=<10>\t\t\t\t- Die Kosten für einen externen Sitzplatz (ganzzahlig)");
        Protocol.log("");
        Protocol.log("Pfade und Dateinamen dürfen keine Leerzeichen enthalten.");
        Protocol.log("<Wert> ist der Standard falls der Parameter nicht angegeben wird.");

    }

    /**
     * Verarbeitet den Argumentenvektor und speichert die Parameter in der
     * Parameterhashmap
     *
     * @param argv Die Kommandozeilenparameter des Programms
     */
    private void parseArgumentVector(String[] argv) {

        try {
            for (String parameter : argv) {
                if (parameter.equalsIgnoreCase("-help")) {
                   printExecutionHint();
                   System.exit(0);
                }
                else if (!parameter.startsWith("-") && !parameter.contains("=")) {
                    printExecutionHint();
                    System.exit(2);
                } else {
                    // Aktuelles Argument zerlegen
                    parameter = parameter.replace("-", ""); // -mode=gui -> mode=gui
                    String[] parts = parameter.split("="); 

                    // Das Argument in Schlüssel und Wert aufteilen 
                    // und in der Parameterliste speichern
                    parameters.put(parts[0], parts[1]); // Index 0 -> mode, Index 1 -> gui
                }
            }
        } catch (Exception e) {
            Protocol.log("Bei der Verarbeitung der Laufzeitparameter ist ein Fehler ausfgetreten: " + e.getMessage());
            printExecutionHint();
            System.exit(2);
        }

        /**
         * Soll die Hilfe ausgegeben werden?
         */
        if (parameters.containsKey("help")) {
            printExecutionHint();
            System.exit(0);
        }

        /**
         * GUI Modus als Default setzen, falls keine Vorgabe vorhanden
         */
        if (!parameters.containsKey("mode")) {
            parameters.put("mode", "gui");
            Protocol.log("Setze GUI-Modus als Default");
        }

        /**
         * Laufzeitparameter für Konsolen-Modus prüfen
         */
        switch (parameters.get("mode")) {
            case "console":
                Protocol.log("Validiere Laufzeitparameter...");

                /**
                 * Die Liste der Parameter überprüfen, ob alle definierten
                 * Parameterschlüssel tatsächlich vorhanden sind
                 */
                for (String key : parameterKeys) {

                    /**
                     * Die Parameter mode und help wurden bereits verarbeitet
                     * und können an dieser Stelle ignoriert werden
                     */
                    if (key.equals("mode") || key.equals("help")) {
                        continue;
                    }

                    /**
                     * Ausgabeverzeichnis validieren
                     */
                    if (key.equals("out")) {
                        if (!parameters.containsKey(key)) {
                            Protocol.log("Ausgabeverzeichnis muss übegeben werden!");
                            System.exit(100);
                        }

                        Protocol.log("Prüfe Ausgabeverzeichnis: " + parameters.get(key));
                        // Existiert das angegebenen Verzeichnis?
                        File file = new File(parameters.get(key));
                        if (!file.exists()) {
                            Protocol.log("Ausgabeverzeichnis exitiert nicht: " + parameters.get(key));
                            System.exit(101);
                        }
                        
                        // Ist das angegebene Verzeichnis wirklich ein Verzeichnis?
                        if (!file.isDirectory()) {
                            Protocol.log("Ausgabeverzeichnis ist kein Verzeichnis: " + parameters.get(key));
                            System.exit(101);
                        }
                       
                    }

                    /**
                     * Ausgabeformat validieren
                     */
                    if (key.equals("format")) {
                        if (!parameters.containsKey(key)) {
                            Protocol.log("Kein Ausgabeformat angegeben. Setze Default-Format: csv_text");
                            parameters.put("format", "CSV_TEXT");
                        }
                        if (!parameters.get(key).equalsIgnoreCase("csv_text") && !parameters.get(key).equalsIgnoreCase("html")) {
                            Protocol.log("Ausgabeformat unbekannt: " + parameters.get(key));
                            System.exit(200);
                        }
                    }

                    /**
                     * Planungsstrategie validieren
                     */
                    if (key.equals("strategy")) {
                        if (!parameters.containsKey(key)) {
                            parameters.put(key, "CostOptimizedStrategy");
                            Protocol.log("Keine Planungsstrategie übergeben. Setze Default-Planungstrategie: CostOptimizedStrategy");
                        }
                    }

                    /**
                     * Raumdateien validieren
                     */
                    if (key.equals("roomfiles")) {
                        if (!parameters.containsKey(key)) {
                            Protocol.log("Raumdateien wurden nicht übergeben!");
                            System.exit(400);
                        }

                        Protocol.log("Prüfe Raumdateien: " + parameters.get(key));
                        if (!validateFiles(parameters.get(key))) {
                            Protocol.log("Dateien fehlerhaft");
                            System.exit(401);
                        }

                    }

                    /**
                     * Kursdateien validieren
                     */
                    if (key.equals("coursefiles")) {
                        if (!parameters.containsKey(key)) {
                            Protocol.log("Kursdateien wurden nicht übergeben!");
                            System.exit(500);
                        }

                        Protocol.log("Prüfe Kursdateien: " + parameters.get(key));
                        if (!validateFiles(parameters.get(key))) {
                            Protocol.log("Dateien fehlerhaft");
                            System.exit(501);
                        }
                    }

                    /**
                     * Studiegangsdateien validieren
                     */
                    if (key.equals("studyprogramfiles")) {
                        if (!parameters.containsKey(key)) {
                            Protocol.log("Studiengangsdateien wurden nicht übergeben!");
                            System.exit(600);
                        }

                        Protocol.log("Prüfe Studiengangsdateien: " + parameters.get(key));
                        if (!validateFiles(parameters.get(key))) {
                            Protocol.log("Dateien fehlerhaft");
                            System.exit(601);
                        }
                    }

                    /**
                     * Sitzplatzkosten validieren
                     */
                    if (key.equals("seatcosts")) {
                        if (!parameters.containsKey(key)) {
                            parameters.put(key, "10");
                            Protocol.log("Keine Sitzplatzkosten übergeben. Setze Default-Sitzplatzkosten: 10 EUR");
                        } else {
                            try {
                                Integer.parseInt((String) parameters.get(key));
                                // Alles ok, Wert ist ein Integer
                            } catch (Exception e) {
                                Protocol.log("Wert für Sitzplatzkosten ist keine Ganzzahl: " + parameters.get(key));
                                System.exit(701);
                            }
                        }
                    }

                }

                break;
            case "gui":
                break;
            default:
                printExecutionHint();
                System.exit(2);
        }
    }

    /**
     * Prüft ob die angegebenen Dateien existieren
     *
     * @param fileNames Die Dateinamen
     * @return Alle Dateien existieren oder nicht
     */
    private static boolean validateFiles(String fileNames) {
        final String delimiter = ",";
        
        if (fileNames == null || fileNames.isEmpty()) {
            Protocol.log("Es wurden keine Dateinamen übergeben");
            return false;
        }

        String[] records = fileNames.split(delimiter);

        File file;
        for (String fileName : records) {
            file = new File(fileName);

            if (!file.exists()) {
                Protocol.log("Datei exitiert nicht: " + fileName);
                return false;
            }
        }

        return true;
    }

    /**
     * Startet die eigentliche Programmausführung
     */
    public void execute() {

        /**
         * Den gewünschten Modus starten
         */
        String mode = parameters.get("mode");
        if (mode.equals("console")) {
            /**
             * SchedulePlanner als Observer deregistrieren. Weitere Ausgaben
             * erfolgen über Konsolenmodus direkt.
             */
            Protocol.getInstance().deleteObserver(SchedulePlanner.this);
            executeConsole();
        } else {
            /**
             * Weiterhin als Observer agieren. Die GUI gibt ebenfalls Meldungen 
             * aus jedoch in einer spezifischen Komponente.
             */
            executeGUI();
        }
    }

    /**
     * Start den Konsolen-Modus
     */
    private void executeConsole() {

        /**
         * Instanz des Konsolen-Modus erzeugen
         */
        SchedulerConsole scheduleConsole = new SchedulerConsole(parameters);

        /**
         * Konsolen-Modus ausführen
         */
        scheduleConsole.execute();

    }

    /**
     * Startet den GUI Modus im eigenen Thread
     */
    private void executeGUI() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SchedulerUI().setVisible(true);
            }
        });
    }

    /**
     * Protokollausgaben auf der Konsole ausgeben
     *
     * @param observable Das observierbare Objekt
     * @param lastChange Die letzte Änderung
     */
    @Override
    public void update(Observable observable, Object lastChange) {
        System.out.println(lastChange);
    }

}
