package osjava.tl3.logic.planning.strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import osjava.tl3.logic.planning.strategies.helpers.RoomAvailableEquipmentComparator;
import osjava.tl3.logic.planning.strategies.helpers.SortOrder;
import osjava.tl3.model.Course;
import osjava.tl3.model.Room;
import osjava.tl3.model.RoomType;
import osjava.tl3.model.controller.DataController;
import osjava.tl3.model.schedule.ScheduleNew;

/**
 * Diese abstrakte Klasse stellt die Basis für alle Planungstrategien zur
 * Erstellung eines Gesamtplans dar.
 *
 * @author Meikel Bode
 */
public abstract class StrategyNew {
    
    /**
     * Der Name der Planungsstrategie
     */
    private final String name;

    /**
     * Der durch die Strategie erstellte Gesamtplan
     */
    protected ScheduleNew schedule;

    /**
     * Der Data Controller
     */
    protected DataController dataController;

    /**
     * Speichert potentielle Laufzeitparameter für die Planungsstrategie
     */
    protected HashMap<String, Object> parameters;

    /**
     * Eine neue Instanz erzeugen
     *
     * @param name Der Name der Planungsstrategie
     */
    public StrategyNew(String name) {
        this.name = name;
        this.schedule = new ScheduleNew();
    }

    /**
     * Liefert den Namen der Planungsstrategie
     *
     * @return Der Name der Planungsstrategie
     */
    public final String getName() {
        return name;
    }

    /**
     * Implementiert den Algorithmus zur Erstellung des Gesamtplans. Muss von
     * einer konkreten Klasse implementiert werden
     *
     * @param dataController Der Controller der das Modell hält
     * @param parameters Mögliche Parameter für die Planungsstrategie
     * @return
     */
    public abstract ScheduleNew execute(DataController dataController, HashMap<String, Object> parameters);

    /**
     * Ermittele alle für den gegeben Kurs potentiell geeigneten Räume des
     * gegebenen Raumtyps. Ein Raum ist geeignet wenn das geforderte Equipment
     * vorhanden ist und die Anzahl der Sitzeplätze kleine oder gleich der
     * Anzahl der Kursteilnehmer ist.
     *
     * @param course Der Kurs für den passende Räume gesucht werden
     * @param roomType Der Raumtyp
     * @return Die Liste der geeigneten Räume
     */
    public final List<Room> getMatchingRooms(Course course, RoomType roomType) {

        List<Room> matchingRooms = new ArrayList<>();

        for (Room room : dataController.getRooms()) {

            /**
             * Passt der Raumtyp?
             */
            if (room.getType() == roomType) {

                /**
                 * Sind Equipment und Anzahl der Sitzeplätze ausreichen?
                 */
                if (room.getAvailableEquipments().containsAll(course.getRequiredEquipments())
                        && room.getSeats() >= course.getStudents()) {

                    /**
                     * Der Raum passt grundsätzlich, daher für die Rückgabe
                     * berücksichtigen
                     */
                    matchingRooms.add(room);
                }
            }
        }

        /**
         * Sortiere die passenden Räume anhand der Anzahl verfügbarer
         * Ausstattung in aufsteigender Reihenfolge. Damit ist der Raum mit den
         * wenigsten passenden Ausstattungsgegenständen vorne in der Liste.
         * Damit werden zunächst die Räume für Kurse verplant, die am ehesten an
         * das geforderte Mindestmaß herranreichen.
         */
        Collections.sort(matchingRooms, new RoomAvailableEquipmentComparator(SortOrder.ASCENDING));

        /**
         * Die Liste zurück geben
         */
        return matchingRooms;
    }

}