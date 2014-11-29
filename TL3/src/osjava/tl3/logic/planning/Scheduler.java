package osjava.tl3.logic.planning;

import java.util.HashMap;
import osjava.tl3.logic.planning.strategies.CostOptimizedStrategy;
import osjava.tl3.logic.planning.strategies.Strategy;
import osjava.tl3.logic.planning.strategies.StrategyType;
import osjava.tl3.model.MasterSchedule;
import osjava.tl3.model.controller.DataController;

/**
 * Diese Klasse erzeugt auf Basis der gegebenen Kurse und verfügbaren Räume den
 * Gesamtplan mithilfe einer Planungsstrategie.
 *
 * Der Raumplan und die Stundenpläne für Studiengang+Fachsemester und Dozent
 * werden dabei lediglich als verschiedene Sichten auf den Gesamtplan
 * interpretiert und sind das Ergebnis einer spezifischen Abfrage des
 * Gesamtplans anhand verschiedener Kriterien, wie Raum, Dozent oder
 * Studiengang.
 *
 * @author Meikel Bode
 * @version 1.0
 */
public class Scheduler {

    /**
     * Instanz des dataControllers
     */
    private DataController dataController;

    /**
     * Instanz des Gesamtplans
     */
    private MasterSchedule masterSchedule;
    
    /**
     * Typ der Strategie für die Erstellung des Gesamtplans
     */
    private StrategyType strategyType = StrategyType.COST_OPTIMIZED;
    
    /**
     * Instanz der Strategie für die Planung
     */
    private Strategy strategy;
    
    /**
     * Den Gesamtplan auf Basis der gewählten Strategie erstellen.
     * 
     * @param parameters Laufzeitparameter für die Planungsstrategie
     */
    public void executeStrategy(HashMap<String, Object> parameters) {
     
        // Eine Instanz der Planungsstrategie erzeugen
        initStrategy();
        
        // Führt die Strategie aus und erzeugt dabei den Gesamtplan
        masterSchedule = strategy.execute(dataController, parameters);
    }
    
    /**
     * Erzeugt eine Instanz der vorgegebenen Planungsstrategie
     */
    private void initStrategy() {
           
        // Die Strategie wählen
        switch(strategyType) {
            
            // Kostenoptimierte Strategie (zugleich der Default)
            case COST_OPTIMIZED:
            default:
                strategy = new CostOptimizedStrategy();
          
        }
    }
    
    /**
     * Setzt den Data Controller der die Eingabedaten hält
     * @param dataController the dataController to set
     */
    public void setDataController(DataController dataController) {
        this.dataController = dataController;
    }

    /**
     * Liefert den Gesamtplan zurück
     * @return Der Gesamtplan
     */
    public MasterSchedule getMasterSchedule() {
        return masterSchedule;
    }

    /**
     * Setzt die Planungsstrategie
     * @param strategyType the strategyType to set
     */
    public void setStrategyType(StrategyType strategyType) {
        this.strategyType = strategyType;
    }
    
}
