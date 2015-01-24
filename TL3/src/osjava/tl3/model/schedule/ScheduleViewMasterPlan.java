package osjava.tl3.model.schedule;

/**
 * Stellt eine Plansicht auf den Gesamtplan dar. 
 *
 * @author Meikel Bode
 */
public class ScheduleViewMasterPlan extends ScheduleView {

    /**
     * Erzeugt eine Plansicht auf den Gesamtplan
     * @param schedule Der Plan
     */
    public ScheduleViewMasterPlan(Schedule schedule) {
        super(schedule);
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