package osjava.tl3.console;

import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import osjava.tl3.logging.Protocol;
import osjava.tl3.logic.io.input.CourseReader;
import osjava.tl3.logic.io.input.RoomReader;
import osjava.tl3.logic.io.input.StudyProgramReader;
import osjava.tl3.logic.io.output.OutputController;
import osjava.tl3.logic.io.output.OutputFormat;
import static osjava.tl3.logic.io.output.OutputFormat.CSV_TEXT;
import static osjava.tl3.logic.io.output.OutputFormat.HTML;
import osjava.tl3.logic.planning.Scheduler;
import osjava.tl3.logic.planning.strategies.Strategy;
import osjava.tl3.logic.planning.strategies.StrategyFactory;
import osjava.tl3.model.RoomType;
import osjava.tl3.model.controller.DataController;
import osjava.tl3.model.schedule.Schedule;
import osjava.tl3.model.schedule.ScheduleView;

/**
 * Ezeugt den Gesamtplan als Kommandozeilenprogramm
 *
 * @author Christian Müller
 */
public class SchedulerConsole implements Observer {

    /**
     * Die Laufzeitparameter
     */
    private final HashMap<String, String> parameters;

    /**
     * Konstruktor
     * Erzeugt eine neue Instanz des Konsolen-Modus und übergibt die
     * Laufzeitparameter
     *
     * @param parameters Die Laufzeitparameter
     */
    public SchedulerConsole(HashMap<String, String> parameters) {
        this.parameters = parameters;

        /**
         * Als Observer registrieren
         */
        Protocol.getInstance().addObserver(SchedulerConsole.this);

        Protocol.log("Konsolen-Modus initialisiert");
    }

    /**
     * Startet die Kommandozeilenausführung
     */
    public void execute() {
        Protocol.log("Ausführung gestartet");
        DataController dataController = new DataController();

        // Eingabedaten lesen
        loadInputData(dataController);

        // Plan erzeugen
        Schedule schedule = createSchedule(dataController);

        // Statistiken zum Gesamtplan ausgeben
        printCoreStats(dataController, schedule);

        // Ausgabedateien erzeugen
        writeOutput(dataController, schedule);

        Protocol.log("Ausführung beendet");
    }

    /**
     * Laden der Eingabedaten
     *
     * @param dataController Der DataController der die Eingabedaten puffert
     */
    private void loadInputData(DataController dataController) {

        /**
         * Raumdateien einlesen
         */
        RoomReader roomReader = new RoomReader();
        String[] fileNames = parameters.get("roomfiles").split(";");
        for (String fileName : fileNames) {
            roomReader.readRooms(fileName, dataController);
        }

        /**
         * Kursdateien einlesen
         */
        CourseReader courseReader = new CourseReader();
        fileNames = parameters.get("coursefiles").split(";");
        for (String fileName : fileNames) {
            courseReader.readCourses(fileName, dataController);
        }

        /**
         * Studiengangsdateien einlesen
         */
        StudyProgramReader studyProgramReader = new StudyProgramReader();
        fileNames = parameters.get("studyprogramfiles").split(";");
        for (String fileName : fileNames) {
            studyProgramReader.readStudyPrograms(fileName, dataController);
        }

    }

    /**
     * Auf Basis der Parametriesierung die Zeitplanung durchführen
     */
    private Schedule createSchedule(DataController dataController) {

        /**
         * Strategie erzeugen auf Basis Eingabedateien
         */
        Strategy strategy = StrategyFactory.getInstanceByClassName(parameters.get("strategy"));
        if (strategy == null) {
            Protocol.log("Planungsstrategie unbekannt: " + parameters.get("strategy"));
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
        return scheduler.getSchedule();

    }

    /**
     * Erzeugen des Outputs im gewünschten Format
     */
    private void writeOutput(DataController dataController, Schedule schedule) {

        OutputFormat outputFormat = mapOutputFormat(parameters.get("format"));
        Protocol.log("Ausgabeformat: " + outputFormat);

        String outputDirectory = parameters.get("out");
        Protocol.log("Ausgabeverzeichnis: " + outputDirectory);

        OutputController outputController = new OutputController();

        List<ScheduleView> scheduleViews = schedule.getAllScheduleViews(dataController.getRooms(), dataController.getAcademics(), dataController.getStudyPrograms());
        outputController.outputSchedules(scheduleViews, outputFormat, outputDirectory);

    }

    /**
     *
     * @param parameterName
     * @return
     */
    private OutputFormat mapOutputFormat(String parameterName) {

        switch (parameterName.toLowerCase()) {
            case "html":
                return HTML;
            case "csv_text":
            default:
                return CSV_TEXT;
        }

    }

    /**
     * Gibt zentrale Statistiken aus
     *
     * @param dataController Der DataController
     * @param schedule Der Gesamtplan für den Statistiken ausgegeben werden
     * sollen
     */
    private void printCoreStats(DataController dataController, Schedule schedule) {

        Protocol.log("Räume insgesamt: " + dataController.getRooms().size());
        Protocol.log("Räume intern: " + dataController.getRooms(RoomType.INTERNAL).size());
        Protocol.log("Räume extern: " + dataController.getRooms(RoomType.EXTERNAL).size());
        Protocol.log("Anzahl Termine: " + schedule.getAppointmentCount());
        Protocol.log("Sitzplätze benötigt: " + (int) (schedule.getStudentsCount(RoomType.INTERNAL) + schedule.getStudentsCount(RoomType.EXTERNAL)));
        Protocol.log("Sitzplätze intern besetzt: " + schedule.getStudentsCount(RoomType.INTERNAL));
        Protocol.log("Sitzplätze extnern besetzt: " + schedule.getStudentsCount(RoomType.EXTERNAL));
        Protocol.log("Gesamtkosten: " + (int) (schedule.getStudentsCount(RoomType.EXTERNAL) * 10) + " EUR");

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
