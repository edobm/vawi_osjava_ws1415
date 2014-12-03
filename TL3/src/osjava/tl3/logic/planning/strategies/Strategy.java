package osjava.tl3.logic.planning.strategies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import osjava.tl3.logic.planning.strategies.helpers.StrategyProtocol;
import osjava.tl3.model.Course;
import osjava.tl3.model.MasterSchedule;
import osjava.tl3.model.Room;
import osjava.tl3.model.RoomType;
import osjava.tl3.model.controller.DataController;

/**
 * Diese abstrakte Klasse stellt die Basis für alle Planungstrategien zur Erstellung
 * eines Gesamtplans dar.
 * 
 * @author Meikel Bode
 */
public abstract class Strategy {
    
    /**
     * Der durch die Strategie erstellte Gesamtplan
     */
    protected MasterSchedule masterSchedule;
   
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
     */
    public Strategy() {
       masterSchedule = new MasterSchedule();
    }
   
    /**
     * Implementiert den Algorithmus zur Erstellung des Gesamtplans
     * @param dataController Der Controller der das Modell hält
     * @param parameters Mögliche Parameter für die Planungsstrategie
     * @return 
     */
    public abstract MasterSchedule execute(DataController dataController, HashMap<String, Object> parameters);
    
    /**
     * Ermittele alle für den gegeben Kurs potentiell geeigneten Räume des gegebenen Raumtyps.
     * Ein Raum ist geeignet wenn das geforderte Equipment vorhanden ist und die Anzahl
     * der Sitzeplätze kleine oder gleich der Anzahl der Kursteilnehmer ist.
     * 
     * @param course Der Kurs für den passende Räume gesucht werden 
     * @param roomType Der Raumtyp
     * @return Die Liste der geeigneten Räume
     */
    protected List<Room> getMatchingRooms(Course course, RoomType roomType) {
        
        List<Room> matchingRooms = new ArrayList<>();

        for (Room room : masterSchedule.getRooms()) {

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
                     * Der Raum passt grundsätzlich, daher für die Rückgabe berücksichtigen
                     */
                    matchingRooms.add(room);
                }
            }
        }

        return matchingRooms;
    }

}