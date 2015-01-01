package osjava.tl3.model.schedule;

/**
 * Stellt eine unspezifische Plansicht dar. Wird verwendet im UI zur
 * initialisierung des ScheduleViewTableModels
 *
 * @author Meikel Bode
 */
public class ScheduleViewUnspecific extends ScheduleView {

    /**
     * Erzeugt eine unspezifische Plansicht dar
     */
    public ScheduleViewUnspecific() {
        super(new Schedule());
    }

    /**
     * Liefert das Planelement an der gegebenen Plankoordinate
     *
     * @param coordinate Die Plankoordinate
     * @return Das Plementelement
     */
    @Override
    public ScheduleElement getScheduleElement(ScheduleCoordinate coordinate) {
        return new ScheduleElementViewWrapper(this, schedule.getScheduleElement(coordinate));
    }

}