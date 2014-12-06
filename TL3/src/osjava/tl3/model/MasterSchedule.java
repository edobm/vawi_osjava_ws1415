package osjava.tl3.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import osjava.tl3.logic.planning.strategies.helpers.StrategyProtocol;
import osjava.tl3.model.controller.DataController;

/**
 * Diese Klasse repräsentiert den Gesamtplan. Er basiert auf allen Raumplänen,
 * Dozentenplänen sowie Studiengangsplänen.
 *
 * Der Gesamtplan stellt verschiedene Methoden bereit um einfach auf
 * Plankoordinaten der Unterpläne zugreifen zu können.
 *
 * @author Meikel Bode
 */
public class MasterSchedule {

    /**
     * Die Raumpläne
     */
    private final HashMap<Room, Schedule> roomSchedules = new HashMap<>();

    /**
     * Die Dozentenpläne
     */
    private final HashMap<Academic, Schedule> acadademicSchedules = new HashMap<>();

    /**
     * Die Studienganspläne pro Fachsemester
     */
    private final HashMap<StudyProgram, HashMap<Semester, Schedule>> studyProgramSchedules = new HashMap<>();

    /**
     * Auf Basis der Informationen des DataControllers den Gesamtplan
     * initialisieren
     *
     * @param dataControler Der DataController
     */
    public void initFromDataController(DataController dataControler) {

        /**
         * Raumpläne intialisieren
         */
        for (Room room : dataControler.getRooms()) {
            getRoomSchedules().put(room, new Schedule(ScheduleType.ROOM_INTERNAL));
        }
        StrategyProtocol.log("Raumpläne initialisiert: " + getRoomSchedules().size());

        /**
         * Dozentenpläne initialisieren
         */
        for (Course course : dataControler.getCourses()) {
            if (!acadademicSchedules.containsKey(course.getAcademic())) {
                getAcadademicSchedules().put(course.getAcademic(), new Schedule(ScheduleType.ACADAMIC));
            }
        }
        StrategyProtocol.log("Dozentenpläne initialisiert: " + getAcadademicSchedules().size());

        /**
         * Studiengangspläne initialisieren
         */
        int studyProgrammScheduleCount = 0;
        for (StudyProgram studyProgram : dataControler.getStudyPrograms()) {

            HashMap<Semester, Schedule> semesterPlans = new HashMap<>(studyProgram.getSemesters().size());
            for (Semester semester : studyProgram.getSemesters()) {
                semesterPlans.put(semester, new Schedule(ScheduleType.STUDY_PROGRAM));
            }
            getStudyProgramSchedules().put(studyProgram, semesterPlans);
            studyProgrammScheduleCount += semesterPlans.size();
        }

        StrategyProtocol.log("Fachsemesterpläne initialisiert: " + studyProgrammScheduleCount);
        StrategyProtocol.log("Gesamtplan erfolgreich initialisiert");

    }

    /**
     * Ermittelt alle freien Koordinaten im gegebenen Plan
     *
     * @param schedule Der zu durchsuchende Plan
     * @return Die freien Koordinaten des Plans
     */
    public List<ScheduleCoordinate> getFreeCoordinates(Schedule schedule) {

        List<ScheduleCoordinate> freeCordinates = new ArrayList<>();

        for (ScheduleElement scheduleElement : schedule.getScheduleElements()) {
            if (!scheduleElement.isBlocked()) {
                freeCordinates.add(scheduleElement.getCoordiate());
            }
        }

        return freeCordinates;
    }

    /**
     * Convenience Methode zur Ermittlung der freien Koordinaten eines Raumplans
     *
     * @param room Der Raum dessen Plan durchsucht werden soll
     * @return Die freien Koordinaten des Raumplanes
     */
    public List<ScheduleCoordinate> getFreeCoordiates(Room room) {
        return getFreeCoordinates(getRoomSchedules().get(room));
    }

    /**
     * Liefert die Räume die dem Gesamtplan bekannt sind vom gegebenen Typen.
     *
     * @param roomType Der Typ
     * @return Die Liste der Räume
     */
    public List<Room> getRooms(RoomType roomType) {
        List<Room> roomList = new ArrayList<>();

        Iterator<Room> rooms = getRoomSchedules().keySet().iterator();
        while (rooms.hasNext()) {
            Room r = rooms.next();
            if (r.getType() == roomType) {
                roomList.add(r);
            }
        }

        Collections.sort(roomList);

        return roomList;
    }

    /**
     * Liefert die Räume die dem Gesamtplan bekannt sind.
     *
     * @return Die Liste der Räume
     */
    public List<Room> getRooms() {
        List<Room> roomList = new ArrayList<>();

        Iterator<Room> rooms = getRoomSchedules().keySet().iterator();
        while (rooms.hasNext()) {
            roomList.add(rooms.next());
        }

        Collections.sort(roomList);

        return roomList;
    }

    /**
     * Convenience Methode zur Ermittlung der freien Koordinaten eines
     * Dozentenplanes
     *
     * @param academic Der Dozent dessen Plan durchsucht werden soll
     * @return Die freien Koordinaten des Dozentenplanes
     */
    public List<ScheduleCoordinate> getFreeCoordiates(Academic academic) {
        return getFreeCoordinates(getAcadademicSchedules().get(academic));
    }

    /**
     * Liefert alle freien Koordinaten aller Studiengangspläne in denen der
     * gegebene Kurs Bestandteils des Currikulums ist
     *
     * @param course Der zu prüfende Kurs
     * @return Die freien Koordinaten über alle Pläne der relevannten
     * Studiengänge
     */
    public List<ScheduleCoordinate> getFreeCoordiates(Course course) {

        /**
         * Maximalplan erzeugen
         */
        List<ScheduleCoordinate> freeCordinates = generateMaximumCoordinates();

        /**
         * Für alle Studiengänge in denen der Kurs im Curriculum ist die bereits
         * geblockten Koordinaten ermitteln und aus dem Maximalplan entfernen
         */
        Iterator<StudyProgram> studyPrograms = getStudyProgramSchedules().keySet().iterator();
        while (studyPrograms.hasNext()) {
            StudyProgram studyProgram = studyPrograms.next();

            if (studyProgram.containsCourse(course)) {

                Semester semester = studyProgram.getSemesterByCourse(course);
                Schedule semesterSchedule = getStudyProgramSchedules().get(studyProgram).get(semester);

                freeCordinates.removeAll(semesterSchedule.getBlockedCoordinates());
            }
        }

        /**
         * Die über alle Fachsemester der relenvanten Studiengänge ermittelten
         * freien Koordinaten
         */
        return freeCordinates;

    }

    /**
     * Erzeugt einen maximal ausgeprägten Plan mit allen Koordinaten
     *
     * @return Der Maximalplan
     */
    public List<ScheduleCoordinate> generateMaximumCoordinates() {
        List<ScheduleCoordinate> coordinates = new ArrayList<>(25);

        for (int i = 0; i < 5; i++) {
            for (int y = 0; y < 5; y++) {
                coordinates.add(new ScheduleCoordinate(Day.valueOf(i), TimeSlot.valueOf(y)));
            }
        }

        return coordinates;
    }

    /**
     * Belegt die gegebene Koordinate im Plan des gegebenen Raumes mit dem
     * gegebenen Kurs. Weiterhin wird der Plan des Dozenten sowie die Pläne der
     * Studiengänge aktualisiert, da sich implizit eine Belegung ihrer Pläne
     * ergibt.
     *
     * @param coordinate Die Plankoordinate
     * @param room Der Raum des Plan aktualisiert werden muss
     * @param course Der Kurs der den Raum belegen soll
     */
    public void blockCoordinate(ScheduleCoordinate coordinate, Room room, Course course) {

        /**
         * Koordinate des Raumplanes belegen
         */
        getRoomSchedules().get(room).getScheduleElement(coordinate).assignRoom(room).assignCourse(course);

        /**
         * Koodinate des Dozentenplanes belegen
         */
        getAcadademicSchedules().get(course.getAcademic()).getScheduleElement(coordinate).assignRoom(room).assignCourse(course);

        /**
         * Koordinate aller Pläne der relevanten Studiengänge belegen
         */
        Iterator<StudyProgram> studyPrograms = getStudyProgramSchedules().keySet().iterator();
        while (studyPrograms.hasNext()) {
            StudyProgram studyProgram = studyPrograms.next();

            if (studyProgram.containsCourse(course)) {

                Semester semester = studyProgram.getSemesterByCourse(course);
                Schedule semesterSchedule = getStudyProgramSchedules().get(studyProgram).get(semester);

                semesterSchedule.getScheduleElement(coordinate).assignRoom(room).assignCourse(course);
            }
        }
    }

    /**
     * Erzeugt einen externen Raum und einen korrespondierenden Plan. Weiterhin
     * werden der Raum und der Plan der Liste der Raumpläne hinzufügt.
     *
     * @param allEquipments Die Obermenge alle Equipments
     * @return Der neue, externe Raum
     */
    public Room createExternalRoom(List<Equipment> allEquipments) {

        /**
         * Einen Raum erzeugen
         */
        Room room = new Room();
        room.setType(RoomType.EXTERNAL);
        room.setName("Externer Raum " + (getRoomCount(RoomType.EXTERNAL, false) + 1));
        room.setSeats(10000);
        room.setAvailableEquipments(allEquipments);

        /**
         * Einen Plan für den externen Raum erzeugen und Raum und Kurs zuweisen
         */
        Schedule schedule = new Schedule(ScheduleType.ROOM_EXTERNAL);

        /**
         * Den externen Raum und seinen Plan zur liste der Raumpläne hinzufügen
         */
        getRoomSchedules().put(room, schedule);

        return room;
    }

    /**
     * Liefert die Gesamtzahl extern eingeplanter Sitzplätze
     *
     * @return Die Anzahl der extern eingeplanten Plätze
     */
    public int getExternallyScheduledSeats() {
        int seats = 0;

        for (Room room : getRooms(RoomType.EXTERNAL)) {
            Schedule schedule = getSchedule(room);
            for (ScheduleElement scheduleElement : schedule.getScheduleElements()) {
                if (scheduleElement.isBlocked()) {
                    seats += scheduleElement.getCourse().getStudents();
                }
            }
        }

        return seats;
    }

    /**
     * Liefert die Gesamtzahl intern eingeplanter Sitzplätze
     *
     * @return Die Anzahl der intern eingeplanten Plätze
     */
    public int getInternallyScheduledSeats() {
        int seats = 0;

        for (Room room : getRooms(RoomType.INTERNAL)) {
            Schedule schedule = getSchedule(room);
            for (ScheduleElement scheduleElement : schedule.getScheduleElements()) {
                if (scheduleElement.isBlocked()) {
                    seats += scheduleElement.getCourse().getStudents();
                }
            }
        }

        return seats;
    }

    /**
     * Ermittelt die Anzahl der Räume des gegebenen Raumtyps und kann dabei
     * auch nur die jeweilige Anzahl unbenutzter Räume liefern.
     *
     * @param roomType Der Raumtyp
     * @param unusedOnly Zählt nur die nicht genutzten Räume
     * @return Die Anzahl der Räume des gegebenen Typs
     */
    public int getRoomCount(RoomType roomType, boolean unusedOnly) {
        int count = 0;

        Room room;
        Iterator<Room> rooms = getRoomSchedules().keySet().iterator();
        while (rooms.hasNext()) {
            room = rooms.next();
            if (room.getType() == roomType) {
                
                if (!unusedOnly) {
                    count++;
                }
                else {
                    if(getSchedule(room).getFreeCoordinates().size() == 25) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    /**
     * Die Gesamtzahl aller Raumtermine
     *
     * @param roomType
     * @return
     */
    public int getTotalBlocks(RoomType roomType) {

        int blocks = 0;
        Iterator<Room> rooms = getRoomSchedules().keySet().iterator();
        while (rooms.hasNext()) {
            Room r = rooms.next();
            if (r.getType() == roomType) {
                blocks += getRoomSchedules().get(r).getBlockedCoordinates().size();
            }
        }

        return blocks;
    }

    /**
     * Liefert den Plan für den gegebenen Raum
     *
     * @param room Der Raum
     * @return Der Raumplan
     */
    public Schedule getSchedule(Room room) {
        return getRoomSchedules().get(room);
    }

    /**
     * Liefert alle Pläne in denen der gegebene Kurs einplant wurde
     *
     * @param course Der Kurs dessen Einplanungen gesucht sind
     * @return Die Liste der Pläne
     */
    public List<Schedule> getSchedules(Course course) {
        List<Schedule> schedules = new ArrayList<>();

        /**
         * Alle Raumpläne finden
         */
        Iterator<Room> rooms = getRoomSchedules().keySet().iterator();
        while (rooms.hasNext()) {
            Room r = rooms.next();
            Schedule schedule = getRoomSchedules().get(r);
            for (ScheduleElement scheduleElement : schedule.getScheduleElements()) {
                if (scheduleElement.getCourse() != null && scheduleElement.getCourse().equals(course)) {
                    schedules.add(schedule);
                }
            }
        }

        /**
         * Alle Dozentenpläne finden
         */
        Iterator<Academic> academics = getAcadademicSchedules().keySet().iterator();
        while (academics.hasNext()) {
            Academic a = academics.next();
            Schedule schedule = getAcadademicSchedules().get(a);
            for (ScheduleElement scheduleElement : schedule.getScheduleElements()) {
                if (scheduleElement.getCourse() != null && scheduleElement.getCourse().equals(course)) {
                    schedules.add(schedule);
                }
            }
        }

        /**
         * Alle Fachsemesterpläne finden
         */
        Iterator<StudyProgram> studyPrograms = getStudyProgramSchedules().keySet().iterator();
        while (studyPrograms.hasNext()) {
            StudyProgram studyProgram = studyPrograms.next();
            for (Semester semester : studyProgram.getSemesters()) {
                Schedule schedule = getStudyProgramSchedules().get(studyProgram).get(semester);
                for (ScheduleElement scheduleElement : schedule.getScheduleElements()) {
                    if (scheduleElement.getCourse() != null && scheduleElement.getCourse().equals(course)) {
                        schedules.add(schedule);
                    }
                }
            }
        }

        return schedules;
    }

    /**
     * Liefert den Plan für den gegebenen Dozeten
     *
     * @param academic Der Dozent
     * @return Der Dozentenplan
     */
    public Schedule getSchedule(Academic academic) {
        return getAcadademicSchedules().get(academic);
    }

    /**
     * Liefert den Plan für das Fachsemesters des gegebenen Studienganges
     *
     * @param studyProgram Der Studiengang
     * @param semester Das Fachsemesters des Studienganges
     * @return Der Semesterplan
     */
    public Schedule getSchedule(StudyProgram studyProgram, Semester semester) {
        return getStudyProgramSchedules().get(studyProgram).get(semester);
    }

    /**
     * Liefert die Map der Raumpläne
     *
     * @return Die Raumpläne
     */
    public HashMap<Room, Schedule> getRoomSchedules() {
        return roomSchedules;
    }

    /**
     * Liert die Map der Dozentenpläne
     *
     * @return Die Dozentenpläne
     */
    public HashMap<Academic, Schedule> getAcadademicSchedules() {
        return acadademicSchedules;
    }

    /**
     * Liefert die Studigangs-/Fachsemesterpläne
     *
     * @return Die Studiengs-/Fachsemesterpläne
     */
    public HashMap<StudyProgram, HashMap<Semester, Schedule>> getStudyProgramSchedules() {
        return studyProgramSchedules;
    }
}
