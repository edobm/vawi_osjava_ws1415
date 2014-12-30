package osjava.tl3.model.schedule;

import osjava.tl3.model.Course;
import osjava.tl3.model.StudyProgram;

/**
 *
 * @author meikelbode
 */
public class ScheduleViewStudyProgram extends ScheduleView {

    /**
     * Der Studiengang auf dem diese Sicht operiert
     */
    private final StudyProgram studyProgramm;

    /**
     * Erzeugt eine neue Instanz der Studiengangssicht
     *
     * @param studyProgramm Der Studiengang auf dem diese Sicht operiert
     * @param schedule Der Plan auf dem diese Sicht operiert
     */
    public ScheduleViewStudyProgram(StudyProgram studyProgramm, ScheduleNew schedule) {
        super(schedule);
        this.studyProgramm = studyProgramm;
    }

    @Override
    public ScheduleElementNew getScheduleElement(ScheduleCoordinate coordinate) {

        ScheduleElementNew elementFiltered = new ScheduleElementNew(coordinate);

        ScheduleAppointment appointment;
        for (Course course : getStudyProgramm().getCourses()) {
            appointment = schedule.getScheduleElement(coordinate).getAppointment(course);
            if (appointment != null) {
                elementFiltered.addAppointment(appointment);
            }
        }

        return elementFiltered;

    }

    /**
     * Liefert den Studiengang auf dem diese Sicht operiert
     * @return Der Studiengang
     */
    public StudyProgram getStudyProgramm() {
        return studyProgramm;
    }
  
}