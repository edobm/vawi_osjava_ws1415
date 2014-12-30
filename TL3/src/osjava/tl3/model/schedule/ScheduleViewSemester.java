package osjava.tl3.model.schedule;

import java.util.ArrayList;
import java.util.List;
import osjava.tl3.model.Course;
import osjava.tl3.model.Semester;

/**
 *
 * @author meikelbode
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
    public ScheduleViewSemester(Semester semester, ScheduleNew schedule) {
        super(schedule);
        this.semester = semester;
    }

    @Override
    public ScheduleElementNew getScheduleElement(ScheduleCoordinate coordinate) {

        ScheduleElementNew elementFiltered = new ScheduleElementNew(coordinate);

        ScheduleAppointment appointment;
        for (Course course : getSemester().getCourses()) {
            appointment = schedule.getScheduleElement(coordinate).getAppointment(course);
            if (appointment != null) {
                elementFiltered.addAppointment(appointment);
            }
        }

        return elementFiltered;

    }

    /**
     * Liefert das Fachsemester auf dem diese Sicht operiert
     * @return Das Fachsemester
     */
    public Semester getSemester() {
        return semester;
    }

}