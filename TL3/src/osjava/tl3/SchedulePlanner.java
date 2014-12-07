package osjava.tl3;

import java.io.File;
import java.util.HashMap;
import osjava.tl3.logic.planning.Scheduler;
import osjava.tl3.logic.planning.strategies.Strategy;
import osjava.tl3.model.MasterSchedule;
import osjava.tl3.model.controller.DataController;
import osjava.tl3.ui.SchedulerUI;

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
public class SchedulePlanner {

    private static final String[] parameterKeys = new String[]{"mode", "in", "out", "format", "strategy"};
    private static HashMap<String, String> parameters;

    public static void main(String[] argv) {

        /* 
         * Argumenten Vektor prozessieren, validieren und
         * Instanz des SchedulePlanners erzeugen
         */
        parameters = SchedulePlanner.parseArgumentVector(argv);

        /**
         * Instanz von SchedulePlanner erzeugen und ausführen
         */
        SchedulePlanner schedulePlanner = new SchedulePlanner();
        schedulePlanner.execute();
    }

    private static void printExecutionHint() {
        StringBuilder sb = new StringBuilder();
        sb.append("SchedulePlanner (VAWi, Modul OS_JAVA, TL3, Gruppe 1)\n");
        sb.append("Autoren: Christoph Lurz, Christian Müller, Fabian Simon, Meikel Bode\n");
        sb.append("Parameter:\n");
        sb.append(" -mode=gui|console - Laufmodus des Programms GUI oder Konsole\n\n");
        sb.append("GUI-Modus erfordert keine weiteren Parameter!\n\n");
        sb.append("Konsolenmodus erfordert folgende Parameter:\n");
        sb.append(" -in=\"<Eingabeverzeichnis>\" - Das Verzeichnis in dem die Eingabedateien liegen\n");
        sb.append(" -out=\"<Ausgabeverzeichnis>\" - Das Verzeichnis in das die Ausgabedateien geschrieben werden sollen\n");
        sb.append(" -format=plaintext|html - Das Format in dem die Ausgabedateien erzeugt werden sollen\n");
        sb.append(" -strategy=CostOptimizedStrategy - Die Planungsstrategie\n\n");
        sb.append("Pfade dürfen keine Leerzeichen enthalten!");

        System.out.println(sb.toString());
    }

    public static HashMap<String, String> parseArgumentVector(String[] argv) {
        HashMap<String, String> parameters = new HashMap<>();

        try {
            for (String parameter : argv) {
                if (!parameter.startsWith("-") && !parameter.contains("=")) {
                    printExecutionHint();
                    System.exit(2);
                } else {
                    // Aktuelles Argument zerlegen
                    parameter = parameter.replace("-", "");
                    String[] parts = parameter.split("=");

                    // Das Argument in Schlüssel und Wert aufteilen und in der Parameterliste speichern
                    parameters.put(parts[0], parts[1]);
                }
            }
        } catch (Exception e) {
            printExecutionHint();
            System.exit(2);
        }

//        for (String key : parameterKeys) {
//            if (!parameters.containsKey(key)) {
//                printExecutionHint();
//                System.exit(2);
//            }
//        }
//
//        if (parameters.get("mode").equals("console")) {
//            // Die Liste der Parameter überprüfen, ob alle definierten 
//            // Parameterschlüssel tatsächlich vorhanden sind
//            for (String key : parameterKeys) {
//                if (key.equals("mode")) {
//                    continue;
//                }
//
//                if (key.equals("in")) {
//                    File file = new File(parameters.get(key));
//                    if (!file.exists()) {
//                        System.out.println("Eingabeverzeichnis exitiert nicht!");
//                    }
//                }
//
//                if (key.equals("out")) {
//                    File file = new File(parameters.get(key));
//                    if (!file.exists()) {
//                        System.out.println("Ausgabeverzeichnis exitiert nicht!");
//                    }
//                }
//
//                if (key.equals("format")) {
//                    if (!parameters.get(key).equals("plaintext") && !parameters.get(key).equals("html")) {
//                        System.out.println("Ausgabeformat unbekannt!");
//                    }
//                }
//
//                if (key.equals("strategy")) {
//                    if (!parameters.get(key).equals("costoptimized")) {
//                        System.out.println("Planungsstrategy unbekannt!");
//                    }
//                }
//            }
//        } else if (parameters.get("mode").equals("gui")) {
//            // Überprüfung der Parameter nicht notwendig,
//            // da die Parametrisierung der Anwedung über das GUI erfolgt
//        } else {
//            printExecutionHint();
//            System.exit(2);
//        }

        // Eingabeparameter sind in Ordnung.
        // Parametertabelle zurückliefern.
        return parameters;

    }

    /**
     * Initialisierung
     */
    public SchedulePlanner() {
    }

    public void execute() {
        String mode = parameters.get("mode");
        
        if (mode.equals("console")) {
            executeConsole();
        } else {
            executeGUI();
        }
    }

    private void executeGUI() {
        System.out.println("Starte GUI-Modus");
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SchedulerUI().setVisible(true);
            }
        });
    }

    private void executeConsole() {
        System.out.println("Starte Konsolen-Modus");
        DataController dataController = new DataController();
        MasterSchedule masterSchedule;

        // Eingabedaten lesen
        loadInputData(dataController);

        // Plan erzeugen
        masterSchedule = createSchedule();

        // Ausgabedateien erzeugen
        writeOutput(masterSchedule);
    }

    /**
     * Laden der Eingabedaten
     */
    public void loadInputData(DataController dataController) {

        //
        // Logik zum Laden der Eingabedateien einbauen
        // 
        String inputDirectory = parameters.get("in");
        dataController.load();
    }

    /**
     * Erzeugen der Zeitplanung
     */
    private MasterSchedule createSchedule() {

        /**
         * Strategie erzeugen auf Basis Eingabedateien
         */
        Strategy strategy = Strategy.getStrategyInstanceByClassName(parameters.get("strategy"));
        if (strategy == null) {
            System.out.println("Unbekannte Planungsstrategie: " + parameters.get("strategy"));
            System.exit(2);
        }

        /**
         * Scheduler erzeugen
         */
        Scheduler scheduler = new Scheduler();

        /**
         * Planungsstrategie zuweisen
         */
        scheduler.setStrategy(strategy);

        /**
         * Planungsstrategie ausführen
         */
        scheduler.executeStrategy(null);

        /**
         * Gesamtplan zurück geben
         */
        return scheduler.getMasterSchedule();

    }

    /**
     * Erzeugen des Outputs im gewünschten Format
     */
    private void writeOutput(MasterSchedule masterSchedule) {

        //
        // Ausgabelogik integrieren
        // 
    }
}
