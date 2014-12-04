package osjava.tl3.logic.planning.strategies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import osjava.tl3.logic.planning.strategies.helpers.CourseStudentsComparator;
import osjava.tl3.logic.planning.strategies.helpers.SortOrder;
import osjava.tl3.logic.planning.strategies.helpers.StrategyProtocol;
import osjava.tl3.model.Course;
import osjava.tl3.model.MasterSchedule;
import osjava.tl3.model.Room;
import osjava.tl3.model.RoomType;
import osjava.tl3.model.ScheduleCoordinate;
import osjava.tl3.model.controller.DataController;
import osjava.tl3.model.helpers.MasterScheduleConsoleWriter;

/**
 * Eine konkrete Implementierung einer Planungstrategie mit dem Ziel optimierter
 * Kosten.
 *
 * @author Meikel Bode
 *
 */
public class CostOptimizedStrategy extends Strategy {

    /**
     * Erzeugt eine Instanz der Planungsstrategie CostOptimizedStrategy
     */
    public CostOptimizedStrategy() {
        super();
        StrategyProtocol.log("Strategie: kostenoptimiert");
    }

    /**
     * Erzeugt einen Auf Basis der Eingabedaten einen kostenoptimierten
     * Gesamtplan.
     *
     * @see Strategy
     * @return Der erzeugte Gesamplan
     */
    @Override
    public MasterSchedule execute(DataController dataController, HashMap<String, Object> parameters) {
        super.dataController = dataController;
        super.parameters = parameters;
        super.masterSchedule.initFromDataController(dataController);

        /**
         * Auf Basis der vorbereiteten Hilfstabellen den Gesamtplan aufbauen
         */
        createSchedule();

        // Statistiken zum erzeugten Plan auf der Konsole ausgeben
        MasterScheduleConsoleWriter consoleWriter = new MasterScheduleConsoleWriter(masterSchedule);
        consoleWriter.printCoreStats();

        /**
         * Den erzeugten Gesamtplan zurück geben
         */
        return masterSchedule;
    }

    /**
     * Planungsstrategie ausführen
     */
    private void createSchedule() {

        /**
         * Eine nach Anzahl der Teilnehmer absteigend sortierte Queue über alle
         * Kurse erzeugen
         */
        Queue<Course> courseQueue
                = new PriorityQueue<>(new CourseStudentsComparator(SortOrder.DESCENDING));
        courseQueue.addAll(dataController.getCourses());

        StrategyProtocol.log("Kurse zu planen: " + courseQueue.size());

        /**
         * Hält Kurse, die nicht eingeplant werden konnten
         */
        List<Course> coursesNotPlanned = new ArrayList<>();

        /**
         * Hilfsvariablen für die Planungslogik
         */
        List<Room> matchingRooms;
        List<ScheduleCoordinate> freeCoordinatesAcademic;
        List<ScheduleCoordinate> freeCoordinatesRoom;
        List<ScheduleCoordinate> freeCoordinatesStudyPrograms;
        List<ScheduleCoordinate> freeIntersection;
        Course course;
        boolean coursePlanned = false;

        /**
         * Alle Kurse in der Queue verarbeiten
         */
        while ((course = courseQueue.poll()) != null) {
            coursePlanned = false;

            StrategyProtocol.log("Plane Kurs: " + course);

            /**
             * Prüfen ob der Dozent noch freie Termine hat. Wenn nicht den Kurs
             * in die Liste der nicht einplanbaren Kurse eintragen
             */
            freeCoordinatesAcademic = masterSchedule.getFreeCoordiates(course.getAcademic());
            if (freeCoordinatesAcademic.isEmpty()) {
                StrategyProtocol.log("\tFehler: Kurs nicht einplanbar. Dozent hat keine Slots mehr frei: " + course);
                coursesNotPlanned.add(course);
                continue;
            }

            /**
             * Prüfen ob die Studiengangs-/Fachsemesterpläne noch freie Termine
             * haben. Wenn nicht den Kurs in die Liste der nicht einplanbaren
             * Kurse eintragen
             */
            freeCoordinatesStudyPrograms = masterSchedule.getFreeCoordiates(course);
            if (freeCoordinatesAcademic.isEmpty()) {
                StrategyProtocol.log("\tFehler: Kurs nicht einplanbar. Fachsemester haben haben keine Slots mehr frei: " + course);
                coursesNotPlanned.add(course);
                continue;
            }

            /**
             * Schritt 1: Einen passenden, internen Raum finden, um den Kurs
             * dort einzuplnen.
             */
            matchingRooms = getMatchingRooms(course, RoomType.INTERNAL);

            /**
             * Alle passenden, internen Räumen prüfen
             */
            for (Room room : matchingRooms) {

                /**
                 * Schnittmenge über alle freien Koordinaten des Raumplans, des
                 * Dozentenplans und alle Fachsemesterpläne erzeugen
                 */
                freeCoordinatesRoom = masterSchedule.getFreeCoordiates(room);
                freeIntersection = new ArrayList<>(freeCoordinatesRoom);
                freeIntersection.retainAll(freeCoordinatesStudyPrograms);
                freeIntersection.retainAll(freeCoordinatesAcademic);

                /**
                 * Prüfen ob der aktuelle Raum einen freie Koordinate hat. Wenn
                 * nicht, zum nächsten Raum gehen
                 */
                if (freeIntersection.isEmpty()) {
                    StrategyProtocol.log("\tKeine Koordinate frei: " + room + " [" + room.getRoomId() + "]");
                    continue;
                }

                /**
                 * Die erste freie Koordinate für die Planung verwenden
                 */
                ScheduleCoordinate scheduleCoordinate = freeIntersection.get(0);
                masterSchedule.blockCoordinate(scheduleCoordinate, room, course);

                /**
                 * Vermerken, dass der aktuelle Kurs intern eingeplant wurde und
                 * damit eine spätere Überprüfung externer Räume entfallen kann
                 */
                coursePlanned = true;

                StrategyProtocol.log("\tEingeplant (interner Raum): " + course.getAcademic().getName() + "; " + scheduleCoordinate + ";" + room + " [" + room.getRoomId() + "]");

                /**
                 * Die Schleife kann beendet werden, da der Kurs erfolgreich
                 * eingeplant wurde
                 */
                break;
            }

            /**
             * Schritt 2: Den Raum versuchen extern einzuplanen auf Basis
             * bereits existierender, externer Räume.
             *
             * Wurde der Kurs bisher nicht eingeplant?
             */
            if (!coursePlanned) {

                /**
                 * Passende externe Räume ermitteln
                 */
                matchingRooms = getMatchingRooms(course, RoomType.EXTERNAL);

                /**
                 * Alle passenden, externen Räume prüfen
                 */
                for (Room room : matchingRooms) {

                    /**
                     * Schnittmenge über alle freien Koordinaten des Raumplans,
                     * des Dozentenplans und alle Fachsemesterpläne erzeugen
                     */
                    freeCoordinatesRoom = masterSchedule.getFreeCoordiates(room);
                    freeIntersection = new ArrayList<>(freeCoordinatesRoom);
                    freeIntersection.retainAll(freeCoordinatesStudyPrograms);
                    freeIntersection.retainAll(freeCoordinatesAcademic);

                    /**
                     * Prüfen ob der aktuelle Raum einen freie Koordinate hat.
                     * Wenn nicht, zum nächsten Raum gehen.
                     */
                    if (freeIntersection.isEmpty()) {
                        StrategyProtocol.log("\tKeine Koordiate frei: " + room + " [" + room.getRoomId() + "]");
                        continue;
                    }

                    /**
                     * Die erste freie Koordinate für die Planung verwenden
                     */
                    ScheduleCoordinate scheduleCoordinate = freeIntersection.get(0);
                    masterSchedule.blockCoordinate(scheduleCoordinate, room, course);

                    /**
                     * Vermerken, dass der aktuelle Kurs extern eingeplant wurde
                     * und damit kein weiterer externer Raum erzeugt werden
                     * muss.
                     */
                    coursePlanned = true;

                    StrategyProtocol.log("\tExtern Eingeplant (bestehender Raum): " + course.getAcademic().getName() + "; " + scheduleCoordinate + ";" + room + " [" + room.getRoomId() + "]");

                    /**
                     * Die Schleife kann beendet werden, da der Kurs erfolgreich
                     * eingeplant wurde
                     */
                    break;
                }
            }

            /**
             * Schritt 3: Der Kurs konnte bisher werder intern noch in einen
             * bereits erzeugten externen Raum eingeplant werden. Daher einen
             * weiteren externen Raum erzeugen um den Kurs dort einzuplanen.
             */
            if (!coursePlanned) {

                /**
                 * Wurde kein passender Raum gefunden, erzeugen wir einen
                 * externen
                 */
                Room externalRoom = masterSchedule.createExternalRoom(dataController.getEquipments());
                dataController.getRooms().add(externalRoom);

                /**
                 * Schnittmenge über alle freien Koordinaten des Raumplans, des
                 * Dozentenplans und alle Fachsemesterpläne erzeugen
                 */
                freeCoordinatesRoom = masterSchedule.getFreeCoordiates(externalRoom);
                freeIntersection = new ArrayList<>(freeCoordinatesRoom);
                freeIntersection.retainAll(freeCoordinatesStudyPrograms);
                freeIntersection.retainAll(freeCoordinatesAcademic);
                
                /**
                 * Erste freie Koordinate für die Planung verwenden
                 */
                masterSchedule.blockCoordinate(freeIntersection.get(0), externalRoom, course);

                StrategyProtocol.log("\tExtern Eingeplant (neuer Raum): " + course.getAcademic().getName() + "; " + freeIntersection.get(0) + ";" + externalRoom + " [" + externalRoom.getRoomId() + "]");

            }
        }

        StrategyProtocol.log("Nicht einplanbare Kurse: " + coursesNotPlanned);
    }

}
