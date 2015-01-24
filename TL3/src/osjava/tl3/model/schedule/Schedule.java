package osjava.tl3.model.schedule;

import java.util.ArrayList;
import java.util.List;
import osjava.tl3.model.Academic;
import osjava.tl3.model.Course;
import osjava.tl3.model.Room;
import osjava.tl3.model.RoomType;
import osjava.tl3.model.Semester;
import osjava.tl3.model.StudyProgram;

/**
 * Der Gesamtplan für alle Räume, Kurse, Dozenten und Fachsemester der
 * Studiengengänge
 * 
 * @author Meikel Bode
 */
public class Schedule extends ScheduleBasis {

    /**
     * Die Elemente des Plans
     */
    private final List<ScheduleElementImpl> scheduleElements;

    /**
     * Erzeugt einen neuen Plan vom gegebenen Typ
     *
     */
    public Schedule() {
        this.scheduleElements = new ArrayList<>(25);
        initSchedule();
    }

    /**
     * Initialisiert den Plan mit 5 x 5 Koordinaten
     */
    private void initSchedule() {

        for (ScheduleCoordinate coordinate : ScheduleBasis.getPossibleScheduleCoordinates()) {
            scheduleElements.add(new ScheduleElementImpl(coordinate));
        }
    }

    /**
     * Liefert das Planelement der gegebenen Koordinate
     *
     * @param coordinate Die Koordinate des zu liefernden Planelements
     * @return Das zur Koordinate passende Planelement oder null, wenn zur
     * gegebenen Kordinate kein Element gefunden wurde
     */
    public ScheduleElementImpl getScheduleElement(ScheduleCoordinate coordinate) {
        for (ScheduleElementImpl scheduleElement : scheduleElements) {
            if (scheduleElement.getCoordiate().equals(coordinate)) {
                return scheduleElement;
            }
        }
        return null;
    }

    /**
     * Liefert die Planelemente
     *
     * @return Die Planelemente
     */
    public List<ScheduleElementImpl> getScheduleElements() {
        return scheduleElements;
    }

    /**
     * Liefert alle freien Koordinaten aus Fachsemestersicht der gegebenen
     * Studiengängen in denen der gegebene Kurs Bestandteils des Currikulums ist
     *
     * @param studyPrograms Die Studiengänge in denen der Kurs Teil des
     * Curriculums ist
     * @param course Der zu prüfende Kurs
     * @return Die freien Koordinaten über alle Sichten der relevannten
     * Studiengänge
     */
    public List<ScheduleCoordinate> getFreeCoordiates(List<StudyProgram> studyPrograms, Course course) {

        /**
         * Maximalkoodinatensystem erzeugen
         */
        List<ScheduleCoordinate> freeCordinates = ScheduleBasis.getPossibleScheduleCoordinates();

        /**
         * Für alle Studiengänge in denen der Kurs im Curriculum ist die bereits
         * geblockten Koordinaten ermitteln und aus dem Maximalkoodinatensystem
         * entfernen
         */
        for (StudyProgram studyProgram : studyPrograms) {
            if (studyProgram.containsCourse(course)) {
                Semester semester = studyProgram.getSemesterByCourse(course);
                ScheduleViewSemester semesterView = new ScheduleViewSemester(semester, this);
                freeCordinates.removeAll(semesterView.getBlockedCoordinates());
            }
        }

        /**
         * Die über alle Fachsemester der relenvanten Studiengänge ermittelten
         * freien Koordinaten
         */
        return freeCordinates;

    }

    /**
     * Convenience Methode zur Ermittlung der freien Koordinaten aus Sicht eines
     * Dozenten
     *
     * @param academic Der Dozent dessen Plan durchsucht werden soll
     * @return Die freien Koordinaten des Dozentenplanes
     */
    public List<ScheduleCoordinate> getFreeCoordiates(Academic academic) {

        return new ScheduleViewAcademic(academic, this).getFreeCoordinates();

    }

    /**
     * Convenience Methode zur Ermittlung der freien Koordinaten aus Sicht eines
     * Raumes
     *
     * @param room Der Raum dessen Plan durchsucht werden soll
     * @return Die freien Koordinaten des Raumplanes
     */
    public List<ScheduleCoordinate> getFreeCoordiates(Room room) {
        return new ScheduleViewRoom(room, this).getFreeCoordinates();
    }

    /**
     * Erzeugt einen neuen Termin an der gegebenen Koordinate für den gegebenen
     * Raum und Kurs
     *
     * @param scheduleCoordinate Die Plankoordinate
     * @param room Der Raum
     * @param course Der Kurs
     */
    public void createAppointment(ScheduleCoordinate scheduleCoordinate, Room room, Course course){
        getScheduleElement(scheduleCoordinate).createAppointment(room, course);
    }

    /**
     * Liefet die Anzahl der Termine im Plan
     *
     * @return Die Anzahl der Termine
     */
    public int getAppointmentCount() {
        int count = 0;

        for (ScheduleElementImpl element : scheduleElements) {
            count += element.getAppointments().size();
        }

        return count;
    }

    /**
     * Liefert die Anzahl der Teilnehmer Terminen in Räumen des gegebenen Typs
     *
     * @param roomType Der Raumtyp
     * @return Die Anzahl der Teilnehmer
     */
    public int getStudentsCount(RoomType roomType) {
        int count = 0;

        for (ScheduleElementImpl element : scheduleElements) {
            for (ScheduleAppointment appointment : element.getAppointments()) {
                if (appointment.getRoom().getType() == roomType) {
                    count += appointment.getCourse().getStudents();
                }
            }
        }

        return count;
    }

    /**
     * Liefert Plansichten für alle gegebenen Räume
     *
     * @param rooms Die Liste der Räume
     * @return Die Liste der Plansichten
     */
    public List<ScheduleView> getRoomViews(List<Room> rooms) {
        List<ScheduleView> views = new ArrayList<>();

        for (Room room : rooms) {
            views.add(new ScheduleViewRoom(room, this));
        }

        return views;
    }

    /**
     * Liefert Plansichten für alle gegebenen Dozenten
     *
     * @param academics Die Liste der Dozenten
     * @return Die Liste der Plansichten
     */
    public List<ScheduleView> getAcademicScheduleViews(List<Academic> academics) {
        List<ScheduleView> views = new ArrayList<>();

        for (Academic academic : academics) {
            views.add(new ScheduleViewAcademic(academic, this));
        }

        return views;
    }

    /**
     * Liefert Plansichten für alle gegebenen Studiengänge
     *
     * @param studyPrograms Die Liste der Studiengänge
     * @return Die Liste der Plansichten
     */
    public List<ScheduleView> getStudyProgramScheduleViews(List<StudyProgram> studyPrograms) {
        List<ScheduleView> views = new ArrayList<>();

        for (StudyProgram studyProgram : studyPrograms) {
            views.add(new ScheduleViewStudyProgram(studyProgram, this));
        }

        return views;
    }

    /**
     * Liefert Plansichten für alle gegebenen Fachsemester
     *
     * @param semesters Die Liste der Fachsemester
     * @return Die Liste der Plansichten
     */
    public List<ScheduleView> getSemesterScheduleViews(List<Semester> semesters) {
        List<ScheduleView> views = new ArrayList<>();

        for (Semester semester : semesters) {
            views.add(new ScheduleViewSemester(semester, this));
        }

        return views;
    }

    /**
     * Liefert alle Plansichten zu allen Räumen, Dozenten, Studiengängen und
     * Fachsemestern
     *
     * @param rooms Die Räume
     * @param academics Die Dozenten
     * @param studyPrograms Die Studiengänge
     * @return Die Plansichten
     */
    public List<ScheduleView> getAllScheduleViews(List<Room> rooms, List<Academic> academics, List<StudyProgram> studyPrograms) {
        List<ScheduleView> views = new ArrayList<>();

        views.addAll(getRoomViews(rooms));
        views.addAll(getAcademicScheduleViews(academics));
        views.addAll(getStudyProgramScheduleViews(studyPrograms));

        for (StudyProgram studyProgram : studyPrograms) {
            views.addAll(getSemesterScheduleViews(studyProgram.getSemesters()));
        }

        return views;
    }
    
    /**
     * Liefert die Stringrepräsentation des Plans
     * @return Der Name des Plans
     */
    @Override
    public String toString() {
        return "Gesamtplan";
    }

}