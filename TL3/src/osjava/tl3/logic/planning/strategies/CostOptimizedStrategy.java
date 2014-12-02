package osjava.tl3.logic.planning.strategies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.PriorityQueue;
import java.util.Queue;
import osjava.tl3.logic.planning.strategies.helpers.CourseStudentsComparator;
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
 *
 * - Erstelle für jeden Raum eine Liste der Kurse die passen würden. - Suche die
 * Kurse mit den meisten Teilnehmern K_MAX - Verteile K_MAX so auf
 *
 * @author Meikel Bode
 *
 */
public class CostOptimizedStrategy extends Strategy {

    /**
     * Hält alle für einen Raum potentiell möglichen Kurse
     */
    private HashMap<Room, List<Course>> roomsFittingCourse;

    /**
     * Hält alle Kurse, für die kein interner Raum verfügbar ist
     */
    private List<Course> coursesNotFitting;

    /**
     * Erzeugt eine Instanz der Planungsstrategie CostOptimizedStrategy
     */
    public CostOptimizedStrategy() {
        super();
    }

    @Override
    public MasterSchedule execute(DataController dataController, HashMap<String, Object> parameters) {
        super.dataController = dataController;
        super.parameters = parameters;
        super.masterSchedule.initFromDataController(dataController);

        // Auf Basis der vorbereiteten Hilfstabellen den Gesamtplan aufbauen
        createSchedule();
        
        masterSchedule.printCoreStats();
        
        // masterSchedule.printStatistics();
        return masterSchedule;
    }

    private void createSchedule() {

        Queue<Course> courseQueue = new PriorityQueue<>(new CourseStudentsComparator(CourseStudentsComparator.SortOrder.DESCENDING));

        courseQueue.addAll(dataController.getCourses());
        System.out.println("Kurse zu planen: " + courseQueue.size());

        Course course = null;
        List<Room> matchingRooms = null;
        List<ScheduleCoordinate> freeCoordinatesAcademic = null;
        List<ScheduleCoordinate> freeCoordinatesRoom = null;
        List<ScheduleCoordinate> freeCoordinatesStudyPrograms = null;

        List<ScheduleCoordinate> freeIntersection = null;

        List<Course> notPlanned = new ArrayList<Course>();

        while ((course = courseQueue.poll()) != null) {

            System.out.println("Plane Kurs: " + course);

            freeCoordinatesAcademic = masterSchedule.getFreeCoordiates(course.getAcademic());
            if (freeCoordinatesAcademic.isEmpty()) {
                System.out.println("\tFehler: Kurs nicht einplanbar. Dozent hat keine Slots mehr frei: " + course);
                notPlanned.add(course);
                continue;
            }

            freeCoordinatesStudyPrograms = masterSchedule.getFreeCoordiates(course);
            if (freeCoordinatesAcademic.isEmpty()) {
                System.out.println("\tFehler: Kurs nicht einplanbar. Fachsemester haben haben keine Slots mehr frei: " + course);
                notPlanned.add(course);
                continue;
            }

            boolean coursePlanned = false;
            matchingRooms = getMatchingRooms(course, RoomType.INTERNAL);

            for (Room room : matchingRooms) {

                freeCoordinatesRoom = masterSchedule.getFreeCoordiates(room);
                freeIntersection = new ArrayList<>(freeCoordinatesRoom);
                freeIntersection.retainAll(freeCoordinatesStudyPrograms);
                freeIntersection.retainAll(freeCoordinatesAcademic);

                if (freeIntersection.isEmpty()) {

                    System.out.println("\tKeine Koordinate frei: " + room + " [" + room.getRoomId() + "]");
                    continue;
                }

                ScheduleCoordinate scheduleCoordinate = freeIntersection.get(0);

                masterSchedule.blockCoordinate(scheduleCoordinate, room, course);
                System.out.println("\tEingeplant (interner Raum): " + course.getAcademic().getName() + "; " + scheduleCoordinate + ";" + room + " [" + room.getRoomId() + "]");
                coursePlanned = true;
                break;
            }

            if (!coursePlanned) {
                
                matchingRooms = getMatchingRooms(course, RoomType.EXTERNAL);
                
                for (Room room : matchingRooms) {

                    freeCoordinatesRoom = masterSchedule.getFreeCoordiates(room);
                    freeIntersection = new ArrayList<>(freeCoordinatesRoom);
                    freeIntersection.retainAll(freeCoordinatesStudyPrograms);
                    freeIntersection.retainAll(freeCoordinatesAcademic);

                    if (freeIntersection.isEmpty()) {

                        System.out.println("\tKeine Koordiate frei: " + room + " [" + room.getRoomId() + "]");
                        continue;
                    }

                    ScheduleCoordinate scheduleCoordinate = freeIntersection.get(0);

                    masterSchedule.blockCoordinate(scheduleCoordinate, room, course);
                    System.out.println("\tExtern Eingeplant (bestehender Raum): " + course.getAcademic().getName() + "; " + scheduleCoordinate + ";" + room + " [" + room.getRoomId() + "]");
                    coursePlanned = true;
                    break;
                }
            }

            if (!coursePlanned) {

                // Wurde kein passender Raum gefunden, erzeugen wir einen externen
                Room externalRoom = masterSchedule.createExternalRoom(course, dataController.getEquipments());
                dataController.getRooms().add(externalRoom);

                masterSchedule.blockCoordinate(freeCoordinatesAcademic.get(0), externalRoom, course);
                System.out.println("\tExtern Eingeplant (neuer Raum): " + course.getAcademic().getName() + "; " + freeCoordinatesAcademic.get(0) + ";" + externalRoom + " [" + externalRoom.getRoomId() + "]");

            }
        }

        System.out.println("Nicht einplanbare Kurse: " + notPlanned);
    }

    /**
     * Ermittele alle für den gegeben Kurs potentiell geeigneten Räume. Ein Raum
     * ist geeignet wenn das geforderte Equipment vorhanden ist und die Anzahl
     * der Sitzeplätze <= der Anzahl der Kursteilnehmer ist @param course Der
     * Kurs für den passende Räume gesucht werden @return Die Liste der
     * geeigneten Räume
     */
    private List<Room> getMatchingRooms(Course course, RoomType roomType) {

        List<Room> matchingRooms = new ArrayList<>();

        for (Room room : masterSchedule.getRooms()) {
            if (room.getType() == roomType) {
                if (room.getAvailableEquipments().containsAll(course.getRequiredEquipments())
                        && room.getSeats() >= course.getStudents()) {

                    //  if (!masterSchedule.getFreeCoordiates(room).isEmpty()){
                    matchingRooms.add(room);
                    //  }
                }
            }
        }

        return matchingRooms;
    }

}
