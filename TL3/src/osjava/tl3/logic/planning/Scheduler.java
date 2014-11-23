package osjava.tl3.logic.planning;

import osjava.tl3.logic.planning.strategies.CostOptimizedStrategy;
import osjava.tl3.logic.planning.strategies.Strategy;
import osjava.tl3.model.Schedule;
import osjava.tl3.model.controller.DataController;

/**
 * Diese Klasse erzeugt auf Basis der gegebenen Kurse und verfügbaren Räume den
 * Gesamtplan.
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
     * Strategie für die Erstellung des Gesamtplans
     */
    private Strategy.Type strategyType = Strategy.Type.COST_OPTIMIZED;
    
    /**
     * Den Gesamtplan auf Basis der gewählten Strategie erstellen.
     */
    public void executeStrategy() {
        
        Strategy strategy;
        
        switch(strategyType) {
            
            case COST_OPTIMIZED:
            default:
                strategy = new CostOptimizedStrategy();
                
           
        }
        
        schedule = strategy.schedule(dataController);
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
    public void setStrategyType(Strategy.Type strategyType) {
        this.strategyType = strategyType;
    }
    
}
