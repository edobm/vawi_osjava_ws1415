package osjava.tl3.logic.planning.strategies;

import osjava.tl3.model.Schedule;
import osjava.tl3.model.controller.DataController;

/**
 *
 * @author Meikel Bode
 */
public abstract class Strategy {
    
    public enum Type {
        COST_OPTIMIZED
    }
    
    public abstract Schedule schedule(DataController dataController);
}
