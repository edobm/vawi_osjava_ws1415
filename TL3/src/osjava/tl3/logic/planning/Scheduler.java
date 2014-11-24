package osjava.tl3.logic.planning;

import java.util.HashMap;
import osjava.tl3.logic.planning.strategies.CostOptimizedStrategy;
import osjava.tl3.logic.planning.strategies.Strategy;
import osjava.tl3.logic.planning.strategies.StrategyType;
import osjava.tl3.model.Schedule;
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
    private Schedule schedule;
    
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
        
        // Die Strategie wählen
        switch(strategyType) {
            
            // Kostenoptimierte Strategie (zugleich der Default)
            case COST_OPTIMIZED:
            default:
                strategy = new CostOptimizedStrategy();
          
        }
        
        // Führt die Strategie aus und erzeugt dabei den Gesamtplan
        schedule = strategy.execute(dataController, parameters);
    }
    
    /**
     * @param dataController the dataController to set
     */
    public void setDataController(DataController dataController) {
        this.dataController = dataController;
    }

    /**
     * @return the schedule
     */
    public Schedule getSchedule() {
        return schedule;
    }

    /**
     * @param strategyType the strategyType to set
     */
    public void setStrategyType(StrategyType strategyType) {
        this.strategyType = strategyType;
    }
    
}
