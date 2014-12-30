package osjava.tl3.logic.planning;

import java.util.HashMap;
import osjava.tl3.logic.planning.strategies.Strategy;
import osjava.tl3.logic.planning.strategies.StrategyNew;
import osjava.tl3.model.schedule.MasterSchedule;
import osjava.tl3.model.controller.DataController;
import osjava.tl3.model.schedule.ScheduleNew;

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
    private ScheduleNew schedule;

    /**
     * Instanz der Strategie für die Planung
     */
    private StrategyNew strategy;

    /**
     * Den Gesamtplan auf Basis der gewählten Strategie erstellen.
     *
     * @param parameters Laufzeitparameter für die Planungsstrategie
     */
    public void executeStrategy(HashMap<String, Object> parameters) {
        
        /**
         * Führt die Strategie aus und erzeugt dabei den Gesamtplan
         */
        schedule = strategy.execute(dataController, parameters);
    }

    /**
     * Setzt den Data Controller der die Eingabedaten hält
     *
     * @param dataController the dataController to set
     */
    public void setDataController(DataController dataController) {
        this.dataController = dataController;
    }

    /**
     * Liefert den Gesamtplan zurück
     *
     * @return Der Gesamtplan
     */
    public ScheduleNew getSchedule() {
        return schedule;
    }

    /**
     * Liefert die Strategie
     * @return Die Strategie
     */
    public StrategyNew getStrategy() {
        return strategy;
    }

    /**
     * Setzt die Strategie
     * @param strategy Die Strategie
     */
    public void setStrategy(StrategyNew strategy) {
        this.strategy = strategy;
    }

} 