package osjava.tl3.logic.planning.strategies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import osjava.tl3.logic.planning.strategies.helpers.CourseStudentsComparator;
import osjava.tl3.model.Course;
import osjava.tl3.model.MasterSchedule;
import osjava.tl3.model.Room;
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
        
        while ((course = courseQueue.poll()) != null) {
            
            System.out.println("Plane Kurs: " + course);
            
            matchingRooms = getMatchingRooms(course);
            
            if (matchingRooms.isEmpty()) {
                
                freeCoordinatesAcademic = masterSchedule.getFreeCoordiates(course.getAcademic());
                masterSchedule.scheduleExternal(freeCoordinatesAcademic.get(0), course);
                System.out.println("\tExtern eingeplant: " + course.getAcademic().getName() + "; " + freeCoordinatesAcademic.get(0));
                
            } else {
                
                for (int i = 0; i < matchingRooms.size(); i++) {
                    Room room = matchingRooms.get(i);
                    
                    freeCoordinatesRoom = masterSchedule.getFreeCoordiates(room);
                    freeCoordinatesAcademic = masterSchedule.getFreeCoordiates(course.getAcademic());

                    /**
                     * Wenn der aktuelle Raum keinen weitere freie Koordinate
                     * hat zum nächsten Raum wechseln
                     */
                    if (freeCoordinatesRoom.isEmpty()) {
                        System.out.println("\tRaum voll: " + room);
                        if (i < matchingRooms.size() - 1) {
                            continue;
                        } else {
                            masterSchedule.scheduleExternal(freeCoordinatesAcademic.get(0), course);
                            System.out.println("\tExtern eingeplant: " + course.getAcademic().getName() + "; " + freeCoordinatesAcademic.get(0));
                            
                            continue;
                        }
                    }
                    
                    freeCoordinatesStudyPrograms = masterSchedule.getFreeCoordiates(course);
                    
                    freeIntersection = new ArrayList<>(freeCoordinatesRoom);
                    freeIntersection.retainAll(freeCoordinatesStudyPrograms);
                    freeIntersection.retainAll(freeCoordinatesAcademic);
                    
                    ScheduleCoordinate scheduleCoordinate = freeIntersection.get(0);
                    
                    masterSchedule.blockCoordinate(scheduleCoordinate, room, course);
                    System.out.println("\tIntern eingeplant: " + course.getAcademic().getName() + "; " + scheduleCoordinate + ";" + room);
                    break;
                    
                }
            }
        }
    }

    /**
     * Ermittele alle für den gegeben Kurs potentiell geeigneten Räume. Ein Raum
     * ist geeignet wenn das geforderte Equipment vorhanden ist und die Anzahl
     * der Sitzeplätze <= der Anzahl der Kursteilnehmer ist @param course Der
     * Kurs für den passende Räume gesucht werden @return Die Liste der geeigneten Räume
     */
    private List<Room> getMatchingRooms(Course course) {
        
        List<Room> matchingRooms = new ArrayList<>();

        for (Room room : dataController.getRooms()) {
            if (room.getAvailableEquipments().containsAll(course.getRequiredEquipments()) 
                    && room.getSeats() >= course.getStudents()) {
                
                matchingRooms.add(room);
            }
        }
        
        return matchingRooms;
    }
    
}
