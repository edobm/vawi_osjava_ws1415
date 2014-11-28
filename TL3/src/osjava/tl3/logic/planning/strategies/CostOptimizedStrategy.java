package osjava.tl3.logic.planning.strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import osjava.tl3.logic.planning.strategies.helpers.CourseStudentsComparator;
import osjava.tl3.model.Course;
import osjava.tl3.model.Equipment;
import osjava.tl3.model.Room;
import osjava.tl3.model.Schedule;
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
    public Schedule execute(DataController dataController, HashMap<String, Object> parameters) {
        super.dataController = dataController;
        super.parameters = parameters;

        // Initialisierung der Basis Lookup Tables
        initHelperTables();

        // Potententielle Raum-Kurs-Kombinationen ermitteln
        findPotentialCourses();
        
        // Auf Basis der vorbereiteten Hilfstabellen den Gesamtplan aufbauen
        createSchedule();

        return schedule;
    }

    /**
     * Erzeugt die Hilfstabellen für die Raumplanung und legt für jeden Raum
     * eine Liste mit potentiell passenden Kursen an
     */
    private void initHelperTables() {

        // Erzeuge Listen für Planerstellung
        roomsFittingCourse = new HashMap<>();
        coursesNotFitting = new ArrayList<>();

        // Erzeuge einen Eintrag für jeden Raum
        for (Room room : dataController.getRooms()) {
            roomsFittingCourse.put(room, new ArrayList<>());
        }

    }

    private void findPotentialCourses() {
        boolean courseFits;

        for (Room room : dataController.getRooms()) {
            List<Course> fittingCourses;

            // Eintrag für den Raum in Hilfstabelle bereits vorhanden?
            // Wenn nicht erzeugen
            if (!roomsFittingCourse.containsKey(room)) {
                fittingCourses = new ArrayList<>();
                roomsFittingCourse.put(room, fittingCourses);
            } else {
                fittingCourses = roomsFittingCourse.get(room);
            }

            // Jeden Kurs prüfen ob er für einen Raum grundsätzlich geeignet wäre
            // Passt der Kurs für keinen Raum, dann aussortieren.
            for (Course course : dataController.getCourses()) {
                courseFits = false;

                // Ein Kurs kann in einem Raum grundsätzlich stattfinden,
                // wenn der Raum das erforderliche Equipment bereitstellt und die
                // die Anzahl der Sitzplätze <= der Anzahl der Teilnehmer des Kurses ist
                if (room.getAvailableEquipments().containsAll(course.getRequiredEquipments())
                        && room.getSeats() <= course.getStudents()) {

                    courseFits = true;
                    fittingCourses.add(course);
                }

                // Passt der Kurs zu keinem Raum sortieren wir ihn aus, da er 
                // nur in einem externen Raum abgehalten werden kann.
                if (!courseFits) {
                    coursesNotFitting.add(course);
                }
            }
        }

        // Kurse pro Raum sortieren nach Anzahl Studenten pro Kurs.
        // Kurse mit vielen Teilnehmern vorne/oben.
        for (Room room : dataController.getRooms()) {
            Collections.sort(roomsFittingCourse.get(room), CourseStudentsComparator.getInstance());
        }
    }
    
    private void printFittings() {
        
        for (Room r : dataController.getRooms()) {
            
            System.out.print("Raum [" + r.getName() + "]: ");
            
            for (Equipment c : r.getAvailableEquipments()) {
                System.out.print(c + ", ");
            }
            
            for (Course c : roomsFittingCourse.get(r)) {
                System.out.print(c.getName() + "; ");
            }
            System.out.println();
        }
    }
    
    private void createSchedule() {
    
        printFittings();
        
    }


}
