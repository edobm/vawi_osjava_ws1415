package osjava.tl3.model.schedule;

import osjava.tl3.model.Academic;

/**
 * Der Gesamtplan aus Sicht eines Dozenten
 *
 * @author Meikel Bode
 */
public class ScheduleViewAcademic extends ScheduleView {

    /**
     * Falls dieser Plan ein Dozentenplan sein sollte, ist dieses Attribut
     * gefüllt
     */
    private final Academic academic;

    /**
     * Erzeugt eine neue Instanz einer Dozentensicht
     * @param academic Der Dozent
     * @param schedule Der Plan auf dem diese Sicht operiert
     */
    public ScheduleViewAcademic(Academic academic, Schedule schedule) {
        super(schedule);
        this.academic = academic;
    }

    /**
     * Liefert die Termine des Planelements auf der gegebenen Koordinate 
     * anhand des Dozenten. 
     * Nur die Termine für das Filterkrieterium werden zurückgeliefert
     * @param coordinate Die Plankoordinate
     * @return Das gefilterte Planelement 
     */
    @Override
    public ScheduleElement getScheduleElement(ScheduleCoordinate coordinate) {

        ScheduleElement elementFiltered = new ScheduleElement(coordinate);

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