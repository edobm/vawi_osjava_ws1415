package osjava.tl3.model.schedule;

import osjava.tl3.model.Academic;

/**
 *
 * @author meikelbode
 */
public class ScheduleViewAcademic extends ScheduleView {

    /**
     * Falls dieser Plan ein Dozentenplan sein sollte, ist dieses Attribut
     * gefüllt
     */
    private final Academic academic;

    public ScheduleViewAcademic(Academic academic, ScheduleNew schedule) {
        super(schedule);
        this.academic = academic;
    }

    @Override
    public ScheduleElementNew getScheduleElement(ScheduleCoordinate coordinate) {

        ScheduleElementNew elementFiltered = new ScheduleElementNew(coordinate);

        ScheduleAppointment appointment = schedule.getScheduleElement(coordinate).getAppointment(academic);

        if (appointment != null) {
            elementFiltered.addAppointment(appointment);
        }

        return elementFiltered;

    }

    /**
     * Liefert den Dozenten auf dem diese Sicht operiert
     *
     * @return Der Dozent
     */
    public Academic getAcademic() {
        return academic;
    }

}
