package osjava.tl3.model.schedule;

import osjava.tl3.model.Course;
import osjava.tl3.model.StudyProgram;

/**
 * Der Gesamtplan aus Sicht eines Studiengangs
 *
 * @author Meikel Bode
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
    public ScheduleViewStudyProgram(StudyProgram studyProgramm, Schedule schedule) {
        super(schedule);
        this.studyProgramm = studyProgramm;
    }

    /**
     * Liefert die Termine des Planelements auf der gegebenen Koordinate 
     * anhand des Studiengangs. 
     * Nur die Termine für das Filterkrieterium werden zurückgeliefert
     * @param coordinate Die Plankoordinate
     * @return Das gefilterte Planelement 
     */
    @Override
    public ScheduleElement getScheduleElement(ScheduleCoordinate coordinate) {

        ScheduleElement elementFiltered = new ScheduleElement(coordinate);

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