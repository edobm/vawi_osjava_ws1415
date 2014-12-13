package osjava.tl3.logic.planning;

import java.util.HashMap;
import osjava.tl3.logic.planning.strategies.Strategy;
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
     * Instanz der Strategie für die Planung
     */
    private Strategy strategy;

    /**
     * Den Gesamtplan auf Basis der gewählten Strategie erstellen.
     *
     * @param parameters Laufzeitparameter für die Planungsstrategie
     */
    public void executeStrategy(HashMap<String, Object> parameters) {
        
        /**
         * Führt die Strategie aus und erzeugt dabei den Gesamtplan
         */
        masterSchedule = strategy.execute(dataController, parameters);
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
    public MasterSchedule getMasterSchedule() {
        return masterSchedule;
    }

    /**
     * Liefert die Strategie
     * @return Die Strategie
     */
    public Strategy getStrategy() {
        return strategy;
    }

    /**
     * Setzt die Strategie
     * @param strategy Die Strategie
     */
    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

} 