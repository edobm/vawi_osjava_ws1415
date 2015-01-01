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
 * Klassensystem dar. Als Parameter erwartet die Main-Methode die Parameter:
 *
 * -in="<directory>" - Verzeichnis das die Eingabedatein enthält
 * -out="<directory>" - Verzeichnis in das die Ausgabedatein geschrieben werden
 * sollen -format="<plaintext, html>" - Das gewünschte Ausgabeformat
 * -in="c:\tmp\int" -out="blablub" -format="plaintext"
 *
 * @author Christian Müller
 * @version 1.0
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
    private static final String[] parameterKeys = new String[]{"help", "mode", "roomfiles", "coursefiles", "studyprogramfiles",
        "out", "format", "strategy"};

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
        Protocol.log(" -mode=<gui>|console - Laufmodus des Programms GUI oder Konsole");
        Protocol.log("");
        Protocol.log("GUI-Modus erfordert keine weiteren Parameter!");
        Protocol.log("");
        Protocol.log("Konsolenmodus erfordert folgende Parameter:");
        Protocol.log(" -roomfiles=Datei1;Datei2;... - Die Raumdateien");
        Protocol.log(" -coursefiles=Datei1;Datei2;... - Die Kursdateien");
        Protocol.log(" -studyprogramfiles=Datei1;Datei2;... - Die Studiengangsdateien");
        Protocol.log(" -out=Ausgabeverzeichnis - Das Verzeichnis in das die Ausgabedateien geschrieben werden sollen");
        Protocol.log(" -format=<csv_text>|html - Das Format in dem die Ausgabedateien erzeugt werden sollen");
        Protocol.log(" -strategy=<CostOptimizedStrategy> - Die Planungsstrategie");
        Protocol.log("");
        Protocol.log("Pfade und Dateinamen dürfen keine Leerzeichen enthalten.");
        Protocol.log("<Wert> ist der Standard falls der Parameter nicht angegeben wird.");

    }

    /**
     * Verarbeitet den Argumentenvektor und speichert die Parameter in der
     * Parameterhashmap
     *
     * @param argv die Kommandozeilenparameter des Programms
     */
    public void parseArgumentVector(String[] argv) {

        try {
            for (String parameter : argv) {
                if (!parameter.startsWith("-") && !parameter.contains("=")) {
                    printExecutionHint();
                    System.exit(2);
                } else {
                    // Aktuelles Argument zerlegen
                    parameter = parameter.replace("-", "");
                    String[] parts = parameter.split("=");

                    // Das Argument in Schlüssel und Wert aufteilen 
                    // und in der Parameterliste speichern
                    parameters.put(parts[0], parts[1]);
                }
            }
        } catch (Exception e) {
            Protocol.log("Bei der Verarbeitung der Laufzeitparameter ist ein Fehler ausfgetreten: " + e.getMessage());
            printExecutionHint();
            System.exit(2);
        }

        if (parameters.containsKey("help")) {
            printExecutionHint();
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

                        File file = new File(parameters.get(key));
                        if (!file.exists()) {
                            Protocol.log("Ausgabeverzeichnis exitiert nicht: " + parameters.get(key));
                            System.exit(101);
                        }
                    }

                    /**
                     * Ausgabeformat validieren
                     */
                    if (key.equals("format")) {
                        if (!parameters.containsKey("format")) {
                            Protocol.log("Setze Default-Format: CSV_TEXT");
                            parameters.put("format", "CSV_TEXT");
                            System.exit(200);
                        }
                        if (!parameters.get(key).equalsIgnoreCase("csv_text") && !parameters.get(key).equalsIgnoreCase("html")) {
                            Protocol.log("Ausgabeformat unbekannt: " + parameters.get(key));
                            System.exit(201);
                        }
                    }

                    /**
                     * Planungsstrategie validieren
                     */
                    if (key.equals("strategy")) {
                        if (!parameters.containsKey("strategy")) {
                            parameters.put("strategy", "CostOptimizedStrategy");
                            Protocol.log("Setze Default-Planungstrategie: CostOptimizedStrategy");
                        }
                    }

                    /**
                     * Raumdateien validieren
                     */
                    if (key.equals("roomfiles")) {
                        if (!parameters.containsKey("roomfiles")) {
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
                    if (key.equals("studyprogramfiles")) {
                        if (!parameters.containsKey("studyprogramfiles")) {
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
                        if (!parameters.containsKey("studyprogramfiles")) {
                            Protocol.log("Studiengangsdateien wurden nicht übergeben!");
                            System.exit(600);
                        }

                        Protocol.log("Prüfe Studiengangsdateien: " + parameters.get(key));
                        if (!validateFiles(parameters.get(key))) {
                            Protocol.log("Dateien fehlerhaft");
                            System.exit(601);
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

        if (fileNames == null || fileNames.isEmpty()) {
            Protocol.log("Es wurden keine Dateinamen übergeben");
            return false;
        }

        String[] records = fileNames.split(";");

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
             * SchedulePlanner als Observer deregistrieren
             */
            Protocol.getInstance().deleteObserver(SchedulePlanner.this);
            executeConsole();
        } else {
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
     * Startet den GUI Modus
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
     * @param o Das observierbare Objekt
     * @param arg Die letzte Änderung
     */
    @Override
    public void update(Observable o, Object arg) {
        System.out.println(arg);
    }

}
