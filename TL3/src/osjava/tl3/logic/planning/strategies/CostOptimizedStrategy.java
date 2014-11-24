package osjava.tl3.logic.planning.strategies;

import java.util.HashMap;
import osjava.tl3.model.Schedule;
import osjava.tl3.model.controller.DataController;

/**
 * Eine konkrete Implementierung einer Planungstrategie mit dem Ziel optimierter
 * Kosten.
 *
 * 
 * - Erstelle für jeden Raum eine Liste der Kurse die passen würden.
 * - Suche die Kurse mit den meisten Teilnehmern K_MAX
 * - Verteile K_MAX so auf
 * 
 * @author Meikel Bode
 *
 */
public class CostOptimizedStrategy extends Strategy {

    @Override
    public Schedule execute(DataController dataController, HashMap<String, Object> parameters) {
        super.parameters = parameters;
        return new Schedule();
    }

}
