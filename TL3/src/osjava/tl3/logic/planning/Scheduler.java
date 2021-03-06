package osjava.tl3.logic.planning;

import osjava.tl3.logging.Protocol;
import osjava.tl3.logic.planning.strategies.Strategy;
import osjava.tl3.model.controller.DataController;
import osjava.tl3.model.schedule.Schedule;

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
     * Instanz der Strategie für die Planung
     */
    private Strategy strategy;

    /**
     * Den Gesamtplan auf Basis der gewählten Strategie erstellen.
     *
     */
    public void executeStrategy() {

        /**
         * Führt die Strategie aus und erzeugt dabei den Gesamtplan
         */
        long startTime = System.currentTimeMillis();
        schedule = strategy.execute(dataController);
        Protocol.log("Plan erzeugt in: " + (System.currentTimeMillis() - startTime) + "ms");
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
    public Schedule getSchedule() {
        return schedule;
    }

    /**
     * Liefert die Strategie
     *
     * @return Die Strategie
     */
    public Strategy getStrategy() {
        return strategy;
    }

    /**
     * Setzt die Strategie
     *
     * @param strategy Die Strategie
     */
    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }
    
}
