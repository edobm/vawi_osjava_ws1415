package osjava.tl3.console;

import java.util.ArrayList;
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
import osjava.tl3.model.Room;
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
     * Konstruktor Erzeugt eine neue Instanz des Konsolen-Modus und übergibt die
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
        final String delimiter = ",";

        /**
         * Raumdateien einlesen
         */
        RoomReader roomReader = new RoomReader();
        String[] fileNames = parameters.get("roomfiles").split(delimiter);
        for (String fileName : fileNames) {
            roomReader.readRooms(fileName, dataController);
        }

        /**
         * Kursdateien einlesen
         */
        CourseReader courseReader = new CourseReader();
        fileNames = parameters.get("coursefiles").split(delimiter);
        for (String fileName : fileNames) {
            courseReader.readCourses(fileName, dataController);
        }

        /**
         * Studiengangsdateien einlesen
         */
        StudyProgramReader studyProgramReader = new StudyProgramReader();
        fileNames = parameters.get("studyprogramfiles").split(delimiter);
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
        scheduler.executeStrategy();

        /**
         * Gesamtplan zurück geben
         */
        return scheduler.getSchedule();

    }

    /**
     * Erzeugen des Outputs im gewünschten Format
     */
    private void writeOutput(DataController dataController, Schedule schedule) {

        /**
         * Das Ausgabeformat aus der Parameterliste lesen
         */
        OutputFormat outputFormat = mapOutputFormat(parameters.get("format"));
        Protocol.log("Ausgabeformat: " + outputFormat);

        /**
         * Das Ausgabeverzeichnis aus der Parameterliste lesen
         */
        String outputDirectory = parameters.get("out");
        Protocol.log("Ausgabeverzeichnis: " + outputDirectory);

        /**
         * Einen OutputController erzeugen
         */
        OutputController outputController = new OutputController();

        /**
         * Die Ausgabedateien erzeugen für Dozenten, Räume, Studiengänge
         * und Fachsemester
         */
        List<ScheduleView> scheduleViews = schedule.getAllScheduleViews(dataController.getRooms(), dataController.getAcademics(), dataController.getStudyPrograms());
        outputController.outputSchedules(scheduleViews, outputFormat, outputDirectory);

    }

    /**
     * Setzt den Eingabewert für das Ausgabeformat in seine Enum-Entsprechung
     * um
     * @param outputFormatText Das Ausgabeformat als Textwert
     * @return Das Ausgabeformat als Enum-Entsprechung
     */
    private OutputFormat mapOutputFormat(String outputFormatText) {

        switch (outputFormatText.toLowerCase()) {
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

        /**
         * Ermitteln welche Räume nicht belegt wurden
         */
        List<Room> unusedRooms = new ArrayList<>(dataController.getRooms());
        unusedRooms.removeAll(schedule.getPlannedRooms());

        Protocol.log("Räume insgesamt: " + dataController.getRooms().size());
        Protocol.log("Räume intern: " + dataController.getRooms(RoomType.INTERNAL).size());
        Protocol.log("Räume extern: " + dataController.getRooms(RoomType.EXTERNAL).size());
        Protocol.log("Räume ohne Termin: " + unusedRooms);
        Protocol.log("Anzahl Termine: " + schedule.getAppointmentCount());
        Protocol.log("Sitzplätze benötigt: " + (int) (schedule.getStudentsCount(RoomType.INTERNAL) + schedule.getStudentsCount(RoomType.EXTERNAL)));
        Protocol.log("Sitzplätze intern besetzt: " + schedule.getStudentsCount(RoomType.INTERNAL));
        Protocol.log("Sitzplätze extern besetzt: " + schedule.getStudentsCount(RoomType.EXTERNAL));
        Protocol.log("Gesamtkosten: " + (int) (schedule.getStudentsCount(RoomType.EXTERNAL) * 10) + " EUR");

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