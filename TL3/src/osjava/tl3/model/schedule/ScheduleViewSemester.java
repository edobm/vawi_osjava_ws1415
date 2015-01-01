package osjava.tl3.model.schedule;

import osjava.tl3.model.Course;
import osjava.tl3.model.Semester;

/**
 * Der Gesamtplan aus Sicht eines Fachsemesters
 *
 * @author Meikel Bode
 */
public class ScheduleViewSemester extends ScheduleView {

    /**
     * Der Studiengang auf dem diese Sicht operiert
     */
    private final Semester semester;

    /**
     * Erzeugt eine neue Instanz der Fachsemestersicht
     *
     * @param semester Das Fachsemester auf dem diese Sicht operiert
     * @param schedule Der Plan auf dem diese Sicht operiert
     */
    public ScheduleViewSemester(Semester semester, Schedule schedule) {
        super(schedule);
        this.semester = semester;
    }

    /**
     * Liefert die Termine des Planelements auf der gegebenen Koordinate 
     * anhand des Fachsemesters. 
     * Nur die Termine für das Filterkrieterium werden zurückgeliefert
     * @param coordinate Die Plankoordinate
     * @return Das gefilterte Planelement 
     */
    @Override
    public ScheduleElement getScheduleElement(ScheduleCoordinate coordinate) {

        ScheduleElementImpl elementFiltered = new ScheduleElementImpl(coordinate);

        ScheduleAppointment appointment;
        for (Course course : getSemester().getCourses()) {
            appointment = schedule.getScheduleElement(coordinate).getAppointment(course);
            if (appointment != null) {
                elementFiltered.addAppointment(appointment);
            }
        }

        return new ScheduleElementViewWrapper(this, elementFiltered);

    }

    /**
     * Liefert das Fachsemester auf dem diese Sicht operiert
     * @return Das Fachsemester
     */
    public Semester getSemester() {
        return semester;
    }

}