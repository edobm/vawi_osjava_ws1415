package osjava.tl3;

import java.io.File;
import java.util.HashMap;
import osjava.tl3.logic.io.InputFileHelper;
import osjava.tl3.logic.io.OutputController;
import osjava.tl3.logic.io.OutputFormat;
import static osjava.tl3.logic.io.OutputFormat.CSV_TEXT;
import static osjava.tl3.logic.io.OutputFormat.HTML;
import osjava.tl3.logic.planning.Scheduler;
import osjava.tl3.logic.planning.strategies.Strategy;
import osjava.tl3.logic.planning.strategies.StrategyFactory;
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

    private static final String[] parameterKeys = new String[]{"mode", "roomfiles","coursefiles","studyprogramfiles", 
        "out", "format", "strategy"};
    
    private static HashMap<String, String> parameters = new HashMap<>();

    /**
     * Die Main-Methode und zugleich der Einstiegspunkt in das Klassensystem
     *
     * @param argv Der Argumentenvektor
     */
    public static void main(String[] argv) {

        /* 
         * Argumenten Vektor prozessieren, validieren und
         * Instanz des SchedulePlanners erzeugen
         */
        SchedulePlanner.parseArgumentVector(argv);

        /**
         * Instanz von SchedulePlanner erzeugen und ausführen
         */
        SchedulePlanner schedulePlanner = new SchedulePlanner();
        schedulePlanner.execute();
    }

    /**
     * Gibt Eine Hilfe zur Bedienung des Programms auf der Konsole aus
     */
    private static void printExecutionHint() {
        StringBuilder sb = new StringBuilder();
        sb.append("SchedulePlanner (VAWi, Modul OS_JAVA, TL3, Gruppe 1)\n");
        sb.append("Autoren: Christoph Lurz, Christian Müller, Fabian Simon, Meikel Bode\n");
        sb.append("Parameter:\n");
        sb.append(" -mode=gui|console - Laufmodus des Programms GUI oder Konsole\n\n");
        sb.append("GUI-Modus erfordert keine weiteren Parameter!\n\n");
        sb.append("Konsolenmodus erfordert folgende Parameter:\n");
        sb.append(" -roomfiles=Datei1;Datei2...,Datein - Das Verzeichnis in dem die Raumdateien liegen\n");
        sb.append(" -coursefiles=Datei1;Datei2...,Datein - Das Verzeichnis in dem die Raumdateien liegen\n");
        sb.append(" -studyprogramfiles=Datei1;Datei2...,Datein - Das Verzeichnis in dem die Raumdateien liegen\n");
        sb.append(" -out=\"<Ausgabeverzeichnis>\" - Das Verzeichnis in das die Ausgabedateien geschrieben werden sollen\n");
        sb.append(" -format=csv|html - Das Format in dem die Ausgabedateien erzeugt werden sollen\n");
        sb.append(" -strategy=CostOptimizedStrategy - Die Planungsstrategie\n\n");
        sb.append("Pfade dürfen keine Leerzeichen enthalten!");

        System.out.println(sb.toString());
    }

    /**
     * Verarbeitet den Argumentenvektor und speichert die Parameter in der
     * Parameterhashmap
     *
     * @param argv die Kommandozeilenparameter des Programms
     * @return Die HashMap mit den aufbereiteten parametern
     */
    public static void parseArgumentVector(String[] argv) {

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
        
        /**
         * GUI Modus als Default setzen, falls keine Vorgabe vorhanden
         */
        if (!parameters.containsKey("mode")) {
            parameters.put("mode", "gui");
            System.out.println("Setze GUI-Modus als Default");
        }

        if (parameters.get("mode").equals("console")) {
            // Die Liste der Parameter überprüfen, ob alle definierten 
            // Parameterschlüssel tatsächlich vorhanden sind
            for (String key : parameterKeys) {
                if (key.equals("mode")) {
                    continue;
                }

                if (key.equals("out")) {
                    File file = new File(parameters.get(key));
                    if (!file.exists()) {
                        System.out.println("Ausgabeverzeichnis exitiert nicht!");
                        printExecutionHint();
                        System.exit(100);
                    }
                }

                if (key.equals("format")) {
                    if(!parameters.containsKey("format")) {
                        System.out.println("Setze Default-Format: CSV-Text");
                        parameters.put("format", "csv");
                    }
                    if (!parameters.get(key).equals("csv") && !parameters.get(key).equals("html")) {
                        System.out.println("Ausgabeformat unbekannt!");
                        System.exit(200);
                    }
                }

                if (key.equals("strategy")) {
                    if (!parameters.containsKey("strategy")) {
                        parameters.put("strategy", "CostOptimizedStrategy");
                    }
                    if (!parameters.get(key).equals("CostOptimized")) {
                        System.out.println("Planungsstrategy unbekannt!");
                    }
                }
            }
        } else if (parameters.get("mode").equals("gui")) {
            // Überprüfung der Parameter nicht notwendig,
            // da die Parametrisierung der Anwedung über das GUI erfolgt
        } else {
            printExecutionHint();
            System.exit(2);
        }
    }

    /**
     * Startet die eigentliche Programmausführung
     */
    public void execute() {
        String mode = parameters.get("mode");

        if (mode.equals("console")) {
            executeConsole();
        } else {
            executeGUI();
        }
    }

    /**
     * Startet den GUI Modus
     */
    private void executeGUI() {
        System.out.println("Starte GUI-Modus");

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SchedulerUI().setVisible(true);
            }
        });
    }

    /**
     * Startet die Kommandozeilenausführung
     */
    private void executeConsole() {
        System.out.println("Starte Konsolen-Modus");
        DataController dataController = new DataController();
        MasterSchedule masterSchedule;

        // Eingabedaten lesen
        loadInputData(dataController);

        // Plan erzeugen
        masterSchedule = createSchedule(dataController);

        // Ausgabedateien erzeugen
        writeOutput(masterSchedule);
    }

    /**
     * Laden der Eingabedaten
     * @param dataController Der DataController der die Eingabedaten puffert
     */
    public void loadInputData(DataController dataController) {

         // Daten über Inputhelper lesen
        InputFileHelper.loadRooms(dataController);
        InputFileHelper.loadCourses(dataController);
        InputFileHelper.loadStudyPrograms(dataController);
        
        // -roomfiles=Datei1,Datei2
//        String[] roomfiles = parameters.get("roomfiles").split(";");
        
        // TODO richtige Reader Logik einbauen
//        RoomReader roomReader = new RoomReader();
//        roomReader.readRooms(roomfiles[0], dataController);
//      
//        CourseReader courseReader = new CourseReader();
//        courseReader.readCourses(null, dataController);
//        
//        StudyProgramReader studyProgramReader = new StudyProgramReader();
//        studyProgramReader.readStudyPrograms(null, dataController);
       
    }

    /**
     * Auf Basis der Parametriesierung die Zeitplanung durchführen
     */
    private MasterSchedule createSchedule(DataController dataController) {

        /**
         * Strategie erzeugen auf Basis Eingabedateien
         */
        Strategy strategy = StrategyFactory.getInstanceByClassName(parameters.get("strategy"));
        if (strategy == null) {
            System.out.println("Unbekannte Planungsstrategie: " + parameters.get("strategy"));
            System.exit(2);
        }

        /**
         * Scheduler erzeugen
         */
        Scheduler scheduler = new Scheduler();
        
        /**
         * DataController setzen
         */
        scheduler.setDataController(dataController);
        
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
        // TODO Ausgabelogik integrieren
        // 
        OutputFormat outputFormat = getOutputFormat(parameters.get("format"));
        String outputDirectory = parameters.get("out");
        OutputController oc = new OutputController();
        
        oc.outputSchedules(masterSchedule.getAllSchedules(), outputFormat, outputDirectory);
        
    }
    
    
    public OutputFormat getOutputFormat(String parameterName) {
        if (parameterName.equalsIgnoreCase("html")) {
            return HTML;
        }
        else {
            return CSV_TEXT;
        }
    }
}