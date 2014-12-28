package osjava.tl3.logic.planning.strategies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import osjava.tl3.logic.planning.strategies.helpers.CourseStudentsComparator;
import osjava.tl3.logic.planning.strategies.helpers.SortOrder;
import osjava.tl3.Protocol;
import osjava.tl3.model.Course;
import osjava.tl3.model.MasterSchedule;
import osjava.tl3.model.Room;
import osjava.tl3.model.RoomType;
import osjava.tl3.model.ScheduleCoordinate;
import osjava.tl3.model.controller.DataController;

/**
 * Eine konkrete Implementierung einer Planungstrategie mit dem Ziel optimierter
 * Kosten.
 *
 * @author Meikel Bode
 */
public class CostOptimizedStrategy extends Strategy {
    
    /**
     * Erzeugt eine Instanz der Planungsstrategie CostOptimizedStrategy
     */
    public CostOptimizedStrategy() {
        super("Kostenoptimiert");
        Protocol.log("Strategie: " + getName());
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

        /**
         * Alle Kurse in die Queue eintragen
         */
        courseQueue.addAll(dataController.getCourses());

        /**
         * Protokoll fortschreiben
         */
        Protocol.log("Kurse zu planen: " + courseQueue.size());

        /**
         * Liste für Kurse, die nicht eingeplant werden konnten Gründe dafür
         * sind:
         *
         * a) Dem Dozenten wurden mehr 25 als Kurse zugeordnet und zu diesem
         * Zeitpunkt wurden bereits alle Plan Koordinaten mit anderen
         * Veranstaltungen belegt.
         *
         * b) Einem der Fachsemester wurden bereits mehr als 25 Veranstaltungen
         * zugeordnet und alle Plan Koordinaten sind damit bereits belegt
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
        boolean coursePlanned;

        /**
         * Alle Kurse in der Queue verarbeiten
         */
        while ((course = courseQueue.poll()) != null) {

            /**
             * Vermerken, dass der aktuelle Kurs bisher nicht eingeplant wurde
             */
            coursePlanned = false;

            /**
             * Protokoll fortschreiben
             */
            Protocol.log("Plane Kurs: " + course);

            /**
             * Prüfen ob der Dozent noch freie Termine hat un wenn nicht, den
             * Kurs in die Liste der nicht einplanbaren Kurse eintragen
             */
            freeCoordinatesAcademic = masterSchedule.getFreeCoordiates(course.getAcademic());
            if (freeCoordinatesAcademic.isEmpty()) {
                Protocol.log("\tFehler: Kurs nicht einplanbar. Dozent hat keine Slots mehr frei: " + course);
                coursesNotPlanned.add(course);
                continue;
            }

            /**
             * Prüfen ob die Studiengangs-/Fachsemesterpläne noch freie Termine
             * haben und wenn nicht, den Kurs in die Liste der nicht
             * einplanbaren Kurse eintragen
             */
            freeCoordinatesStudyPrograms = masterSchedule.getFreeCoordiates(course);
            if (freeCoordinatesStudyPrograms.isEmpty()) {
                Protocol.log("\tFehler: Kurs nicht einplanbar. Fachsemester haben haben keine Slots mehr frei: " + course);
                coursesNotPlanned.add(course);
                continue;
            }

            /**
             * Schritt 1: Einen passenden, internen Raum finden, um den Kurs
             * dort einzuplanen.
             */
            matchingRooms = getMatchingRooms(course, RoomType.INTERNAL);

            /**
             * Alle passenden, internen Räumen prüfen
             */
            for (Room room : matchingRooms) {

                /**
                 * Schnittmenge über alle freien Koordinaten des Raumplans, des
                 * Dozentenplans und aller Fachsemesterpläne erzeugen
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
                    Protocol.log("\tKeine Koordinate frei: " + room + " [" + room.getRoomId() + "]");
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

                Protocol.log("\tIntern eingeplant: " + course.getAcademic().getName() + "; " + scheduleCoordinate + ";" + room + " [" + room.getRoomId() + "]");

                /**
                 * Die Schleife kann beendet werden, da der Kurs erfolgreich
                 * eingeplant wurde
                 */
                break;
            }

            /**
             * Schritt 2: Konnte der Raum nicht intern eingeplant werden, jetzt
             * versuchen ihn extern einzuplanen. Dieser Schritt versucht
             * zunächst, einen bereits erzeugten, externen Raum zu finden, der
             * noch eine passende, freie Plan Koordinate hat.
             *
             * Dieser Schritt optimiert daher die Anzahl der anzumietenden, externen
             * Räume indem so viele Kurse eingeplant werden, wie freie Plan
             * Koordinaten existieren, also maximal 25 Kurse pro externem Raum.
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
                     * des Dozentenplans und aller Fachsemesterpläne erzeugen
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
                        Protocol.log("\tKeine Koordinate frei: " + room + " [" + room.getRoomId() + "]");
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

                    /**
                     * Protokoll fortschreiben
                     */
                    Protocol.log("\tExtern eingeplant (bestehender Raum): " 
                            + course.getAcademic().getName() + "; " + scheduleCoordinate + ";" + room + " [" + room.getRoomId() + "]");

                    /**
                     * Die Schleife kann beendet werden, da der Kurs erfolgreich
                     * eingeplant wurde
                     */
                    break;
                }
            }

            /**
             * Schritt 3: Der Kurs konnte bisher weder intern noch in einen
             * bereits erzeugten externen Raum eingeplant werden. Daher einen
             * weiteren externen Raum erzeugen, um den Kurs dort einzuplanen.
             */
            if (!coursePlanned) {

                /**
                 * Einen weiteren externen Raum mithilfe des Gesamtplans
                 * erzeugen und dem Raum alle möglichen bekannten
                 * Ausstatungsgegenstände zuweisen
                 */
                Room externalRoom = masterSchedule.createExternalRoom(dataController.getEquipments());

                /**
                 * Den Raum auch im DataController ablegen
                 */
                dataController.getRooms().add(externalRoom);

                /**
                 * Schnittmenge über alle freien Koordinaten des Raumplans, des
                 * Dozentenplans und aller Fachsemesterpläne erzeugen
                 */
                freeCoordinatesRoom = masterSchedule.getFreeCoordiates(externalRoom);
                freeIntersection = new ArrayList<>(freeCoordinatesRoom);
                freeIntersection.retainAll(freeCoordinatesStudyPrograms);
                freeIntersection.retainAll(freeCoordinatesAcademic);

                /**
                 * Prüfen ob eine freie Plan Koordinaten gefunden werden konnte.
                 * Wenn nicht, dann ist der Kurs endgültig nicht einplanbar!
                 */
                if (freeIntersection.isEmpty()) {
                    Protocol.log("\tKeine Koordinate frei: Kurs konnte nicht eingeplant werden!");
                    coursesNotPlanned.add(course);
                    continue;
                }

                /**
                 * Erste freie Koordinate für die Planung verwenden
                 */
                masterSchedule.blockCoordinate(freeIntersection.get(0), externalRoom, course);

                /**
                 * Protokoll fortschreiben
                 */
                Protocol.log("\tExtern eingeplant (neuer Raum): " + course.getAcademic().getName() 
                        + "; " + freeIntersection.get(0) + ";" + externalRoom + " [" + externalRoom.getRoomId() + "]");

            }
        }

        /**
         * Protokoll fortschreiben
         */
        Protocol.log("Nicht einplanbare Kurse: " + coursesNotPlanned);
    }

}