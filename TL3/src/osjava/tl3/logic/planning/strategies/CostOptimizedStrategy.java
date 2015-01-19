package osjava.tl3.logic.planning.strategies;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import osjava.tl3.logic.planning.strategies.helpers.CourseStudentsComparator;
import osjava.tl3.logic.planning.strategies.helpers.SortOrder;
import osjava.tl3.logging.Protocol;
import osjava.tl3.model.Course;
import osjava.tl3.model.Room;
import osjava.tl3.model.RoomType;
import osjava.tl3.model.schedule.ScheduleCoordinate;
import osjava.tl3.model.controller.DataController;
import osjava.tl3.model.schedule.Schedule;

/**
 * Eine konkrete Implementierung einer Planungstrategie mit dem Ziel optimierter
 * Kosten. Die Strategie versucht dabei priorisiert Kurse mit vielen Teilnehmern
 * auf interne Räume zu verteilen, falls benötigtes und vorhandenes Equipment
 * passen. Kann passender interner Raum gefunden werden, wird zunächst geprüft,
 * ob ggf. bereits ein externer Raum bekannt ist, in den dieser Kurs eingeplant
 * werden kann. Ist der externe Raum bereits voll oder kann passt der Raum nicht
 * zum Plan des Dozenten, wird einer weiterer externer Raum angemietet (erzeugt)
 * und der Kurs dort eingeplant, passend zum Dozetenplan. Kann der Kurs nicht
 * eingeplant werden, da der Dozent keinen freien Termin mehr hat oder die
 * keines der Fachsemester der Studiengänge, in denen der Kurs angeboten wird
 * mehr einen freien Termin hat, wird der Kurs in die Liste der nicht
 * einplanbaren Kurse eingetragen.
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
    public Schedule execute(DataController dataController) {
        super.dataController = dataController;

        /**
         * Auf Basis der vorbereiteten Hilfstabellen den Gesamtplan aufbauen
         */
        createSchedule();

        /**
         * Den erzeugten Gesamtplan zurück geben
         */
        return schedule;
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
            Protocol.log("Plane: [KursID='" + course.getNumber() + "', Kurs='" + course.getName() + "', Dozent='" + course.getAcademic().getName() + "', Teilnehmer='" + course.getStudents() + "']");

            /**
             * Prüfen ob der Dozent noch freie Termine hat un wenn nicht, den
             * Kurs in die Liste der nicht einplanbaren Kurse eintragen
             */
            freeCoordinatesAcademic = schedule.getFreeCoordiates(course.getAcademic());
            if (freeCoordinatesAcademic.isEmpty()) {
                Protocol.log("\tFehler: Kurs nicht einplanbar. Dozent hat keine Plankoordinaten mehr frei: [KursID='" + course.getNumber() + "', Kurs='" + course.getName() + "', Dozent='" + course.getAcademic().getName() + "']");
                coursesNotPlanned.add(course);
                continue;
            }

            /**
             * Prüfen ob die Studiengangs-/Fachsemesterpläne noch freie Termine
             * haben und wenn nicht, den Kurs in die Liste der nicht
             * einplanbaren Kurse eintragen
             */
            freeCoordinatesStudyPrograms = schedule.getFreeCoordiates(dataController.getStudyProgramsByCourse(course), course);
            if (freeCoordinatesStudyPrograms.isEmpty()) {
                Protocol.log("\tFehler: Kurs nicht einplanbar. Fachsemester haben haben keine Plankoordinaten mehr frei: " + course);
                coursesNotPlanned.add(course);
                continue;
            }

            /**
             * Schritt 1: Einen passenden, internen Raum finden, um den Kurs
             * dort einzuplanen.
             */
            matchingRooms = getMatchingRooms(course, RoomType.INTERNAL);
            Protocol.log("\tPassende Räume (intern): " + matchingRooms);

            /**
             * Alle passenden, internen Räumen prüfen
             */
            for (Room room : matchingRooms) {

                /**
                 * Schnittmenge über alle freien Plankoordinaten des Raums, des
                 * Dozenten und aller Fachsemester erzeugen
                 */
                freeCoordinatesRoom = schedule.getFreeCoordiates(room);
                freeIntersection = new ArrayList<>(freeCoordinatesRoom);
                freeIntersection.retainAll(freeCoordinatesStudyPrograms);
                freeIntersection.retainAll(freeCoordinatesAcademic);

                /**
                 * Prüfen ob der aktuelle Raum einen freie Koordinate hat. Wenn
                 * nicht, zum nächsten Raum gehen
                 */
                if (freeIntersection.isEmpty()) {
                    Protocol.log("\t\tKeine Plankoordinate frei: [RaumID='" + room.getRoomId() + "', Raum='" + room.getName() + "', Plätze='" + room.getSeats() + "']");
                    continue;
                }

                /**
                 * Die erste freie Koordinate für die Planung verwenden
                 */
                ScheduleCoordinate scheduleCoordinate = freeIntersection.get(0);

                /**
                 * Termin erstellen
                 */
                schedule.createAppointment(scheduleCoordinate, room, course);

                /**
                 * Vermerken, dass der aktuelle Kurs intern eingeplant wurde und
                 * damit eine spätere Überprüfung externer Räume entfallen kann
                 */
                coursePlanned = true;

                Protocol.log("\t\tIntern eingeplant: [Plankoordinate='" + scheduleCoordinate + "', RaumID='" + room.getRoomId() + "', Raum='" + room.getName() + "' Plätze='" + room.getSeats() + "']");

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
             * Dieser Schritt optimiert daher die Anzahl der anzumietenden,
             * externen Räume indem so viele Kurse eingeplant werden, wie freie
             * Plan Koordinaten existieren, also maximal 25 Kurse pro externem
             * Raum.
             */
            if (!coursePlanned) {

                /**
                 * Passende externe Räume ermitteln
                 */
                matchingRooms = getMatchingRooms(course, RoomType.EXTERNAL);
                Protocol.log("\tPassende Räume (extern): " + matchingRooms);
                
                /**
                 * Alle passenden, externen Räume prüfen
                 */
                for (Room room : matchingRooms) {

                    /**
                     * Schnittmenge über alle freien Plankoordinaten des Raums,
                     * des Dozenten und aller Fachsemester erzeugen
                     */
                    freeCoordinatesRoom = schedule.getFreeCoordiates(room);
                    freeIntersection = new ArrayList<>(freeCoordinatesRoom);
                    freeIntersection.retainAll(freeCoordinatesStudyPrograms);
                    freeIntersection.retainAll(freeCoordinatesAcademic);

                    /**
                     * Prüfen ob der aktuelle Raum einen freie Koordinate hat.
                     * Wenn nicht, zum nächsten Raum gehen.
                     */
                    if (freeIntersection.isEmpty()) {
                        Protocol.log("\t\tKeine Plankoordinate frei: [RaumID='" + room.getRoomId() + "', Raum='" + room.getName() + "', Plätze='" + room.getSeats() + "']");
                        continue;
                    }

                    /**
                     * Die erste freie Koordinate für die Planung verwenden
                     */
                    ScheduleCoordinate scheduleCoordinate = freeIntersection.get(0);

                    /**
                     * Termin erstellen
                     */
                    schedule.createAppointment(scheduleCoordinate, room, course);

                    /**
                     * Vermerken, dass der aktuelle Kurs intern eingeplant wurde
                     * und damit eine spätere Überprüfung externer Räume
                     * entfallen kann
                     */
                    coursePlanned = true;

                    Protocol.log("\t\tExtern eingeplant (bestehender Raum): [Plankoordinate='" + scheduleCoordinate + "', RaumID='" + room.getRoomId() + "', Raum='" + room.getName() + "', Dozent='" + course.getAcademic().getName() + "']");

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
                 * Einen externen Raum erzeugen
                 */
                Room room = dataController.createExternalRoom();
                Protocol.log("\tErzeuge neuen Raum (extern): " + room);
                
                /**
                 * Schnittmenge über alle freien Plankoordinaten des Raums, des
                 * Dozenten und aller Fachsemester erzeugen
                 */
                freeCoordinatesRoom = schedule.getFreeCoordiates(room);
                freeIntersection = new ArrayList<>(freeCoordinatesRoom);
                freeIntersection.retainAll(freeCoordinatesStudyPrograms);
                freeIntersection.retainAll(freeCoordinatesAcademic);

                /**
                 * Prüfen ob eine freie Plan Koordinaten gefunden werden konnte.
                 * Wenn nicht, dann ist der Kurs endgültig nicht einplanbar!
                 */
                if (freeIntersection.isEmpty()) {
                    Protocol.log("\t\tKeine Plankoordinate frei: Kurs konnte nicht eingeplant werden!");
                    coursesNotPlanned.add(course);
                    continue;
                }

                /**
                 * Die erste freie Koordinate für die Planung verwenden
                 */
                ScheduleCoordinate scheduleCoordinate = freeIntersection.get(0);

                /**
                 * Termin erstellen
                 */
                schedule.createAppointment(scheduleCoordinate, room, course);

                /**
                 * Protokoll fortschreiben
                 */
                Protocol.log("\t\tExtern eingeplant (neuer Raum): [Plankoordinate='" + scheduleCoordinate + "', RaumID='" + room.getRoomId() + "', Raum='" + room.getName() + "']");

            }
        }

        /**
         * Protokoll fortschreiben
         */
        Protocol.log("Nicht einplanbare Kurse: " + coursesNotPlanned);
    }

}
