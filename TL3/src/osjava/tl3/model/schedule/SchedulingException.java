package osjava.tl3.model.schedule;

/**
 * Ausnahme die auftritt falls ein Termin nicht erstellt werden konnte
 * 
 * @author Meikel Bode
 */
public class SchedulingException extends Exception {

    public SchedulingException(String message) {
        super(message);
    }
    
}