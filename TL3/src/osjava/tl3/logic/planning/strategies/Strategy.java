package osjava.tl3.logic.planning.strategies;

import java.util.HashMap;
import osjava.tl3.model.MasterSchedule;
import osjava.tl3.model.Schedule;
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
    
}
