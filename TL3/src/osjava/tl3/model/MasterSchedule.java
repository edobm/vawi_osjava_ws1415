package osjava.tl3.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
     * Default Construtor
     */
    public MasterSchedule() {
    }

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
            roomSchedules.put(room, new Schedule(ScheduleType.ROOM_INTERNAL));
        }
        System.out.println("Raumpläne erzeugt: " + roomSchedules.size());

        /**
         * Dozentenpläne initialisieren
         */
        for (Course course : dataControler.getCourses()) {
            if (!acadademicSchedules.containsKey(course.getAcademic())) {
                acadademicSchedules.put(course.getAcademic(), new Schedule(ScheduleType.ACADAMIC));
            }
        }
        System.out.println("Dozentenpläne erzeugt: " + acadademicSchedules.size());

        /**
         * Studiengangspläne initialisieren
         */
        int studyProgrammScheduleCount = 0;
        for (StudyProgram studyProgram : dataControler.getStudyPrograms()) {

            HashMap<Semester, Schedule> semesterPlans = new HashMap<>(studyProgram.getSemesters().size());
            for (Semester semester : studyProgram.getSemesters()) {
                semesterPlans.put(semester, new Schedule(ScheduleType.STUDY_PROGRAM));
            }
            studyProgramSchedules.put(studyProgram, semesterPlans);
            studyProgrammScheduleCount += semesterPlans.size();
        }
        System.out.println("Fachsemesterpläne erzeugt: " + studyProgrammScheduleCount);

        System.out.println("Gesamtplan initialisiert");
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
        return getFreeCoordinates(roomSchedules.get(room));
    }
    
    /**
     * Liefert die Räume die dem Gesamtplan bekannt sind vom gegebenen Typen.
     * @param roomType Der Typ 
     * @return Die Liste der Räume
     */
    public List<Room> getRooms(RoomType roomType) {
        List<Room> roomList = new ArrayList<>();
        
        Iterator<Room> rooms = roomSchedules.keySet().iterator();
        while (rooms.hasNext()) {
            Room r = rooms.next();
            if (r.getType() == roomType) {
                roomList.add(r);
            }
        }
        
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
        return getFreeCoordinates(acadademicSchedules.get(academic));
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
        Iterator<StudyProgram> studyPrograms = studyProgramSchedules.keySet().iterator();
        while (studyPrograms.hasNext()) {
            StudyProgram studyProgram = studyPrograms.next();

            if (studyProgram.containsCourse(course)) {

                Semester semester = studyProgram.getSemesterByCourse(course);
                Schedule semesterSchedule = studyProgramSchedules.get(studyProgram).get(semester);

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
        roomSchedules.get(room).getScheduleElement(coordinate).assignRoom(room).assignCourse(course);

        /**
         * Koodinate des Dozentenplanes belegen
         */
        acadademicSchedules.get(course.getAcademic()).getScheduleElement(coordinate).assignRoom(room).assignCourse(course);

        /**
         * Koordinate aller Pläne der relevanten Studiengänge belegen
         */
        Iterator<StudyProgram> studyPrograms = studyProgramSchedules.keySet().iterator();
        while (studyPrograms.hasNext()) {
            StudyProgram studyProgram = studyPrograms.next();

            if (studyProgram.containsCourse(course)) {

                Semester semester = studyProgram.getSemesterByCourse(course);
                Schedule semesterSchedule = studyProgramSchedules.get(studyProgram).get(semester);

                semesterSchedule.getScheduleElement(coordinate).assignRoom(room).assignCourse(course);
            }
        }
    }

    /**
     * Erzeugt für einen Kurs eine externe Raumbelegung
     *
     * @param coordinate Die zu belegende Koordinate
     * @param course Der einzuplanende Kurs
     */
    public void scheduleExternal(ScheduleCoordinate coordinate, Course course) {

        /**
         * Einen Raum erzeugen, der genau auf den Kurs passt
         */
        Room room = new Room();
        room.setType(RoomType.EXTERNAL);
        if (course.getType().getName().equals("Uebung")) {
            room.setName("Externer Seminarraum");
        } else {
            room.setName("Externer Hörsaal");
        }
        room.setSeats(course.getStudents());
        room.setAvailableEquipments(course.getRequiredEquipments());
        
        /**
         * Einen Plan für den externen Raum erzeugen und Raum und Kurs zuweisen
         */
        Schedule schedule = new Schedule(ScheduleType.ROOM_EXTERNAL);

        /**
         * Den externen Raum und seinen Plan zur liste der Raumpläne hinzufügen
         */
        roomSchedules.put(room, schedule);

        /**
         * Die Koordinate blocken
         */
        blockCoordinate(coordinate, room, course);

    }
    
    public Room createExternalRoom(Course course) {
        
        /**
         * Einen Raum erzeugen, der genau auf den Kurs passt
         */
        Room room = new Room();
        room.setType(RoomType.EXTERNAL);
        if (course.getType().getName().equals("Uebung")) {
            room.setName("Externer Seminarraum");
        } else {
            room.setName("Externer Hörsaal");
        }
        room.setSeats(course.getStudents());
        room.setAvailableEquipments(course.getRequiredEquipments());
        
          /**
         * Einen Plan für den externen Raum erzeugen und Raum und Kurs zuweisen
         */
        Schedule schedule = new Schedule(ScheduleType.ROOM_EXTERNAL);

        /**
         * Den externen Raum und seinen Plan zur liste der Raumpläne hinzufügen
         */
        roomSchedules.put(room, schedule);
        
        return room;
    }

    /**
     * Liefert die Gesamtkosten für die Anmietung externer Räume
     *
     * @return Die anfallenden Kosten
     */
    public int getCosts() {
        int costs = 0;

        Iterator<Room> rooms = roomSchedules.keySet().iterator();
        while (rooms.hasNext()) {
            costs += rooms.next().getCosts();
        }

        return costs;
    }

    /**
     * Ermittelt die Anzahl der Räume des gegebenen Raumtyps
     *
     * @param roomType
     * @return Die Anzahl der Räume des gegebenen Typs
     */
    public int getRoomCount(RoomType roomType) {
        int count = 0;

        Iterator<Room> rooms = roomSchedules.keySet().iterator();
        while (rooms.hasNext()) {
            if (rooms.next().getType() == roomType) {
                count++;
            }
        }

        return count;
    }

    /**
     * Die Gesamtzahl aller Termine
     *
     * @param roomType
     * @return
     */
    public int getTotalBlocks(RoomType roomType) {

        int blocks = 0;
        Iterator<Room> rooms = roomSchedules.keySet().iterator();
        while (rooms.hasNext()) {
            Room r = rooms.next();
            if (r.getType() == roomType) {
                blocks += roomSchedules.get(r).getBlockedCoordinates().size();
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
        return roomSchedules.get(room);
    }

    public List<Schedule> getSchedules(Course course) {
        List<Schedule> schedules = new ArrayList<>();

        Iterator<Room> rooms = roomSchedules.keySet().iterator();
        while (rooms.hasNext()) {
            Room r = rooms.next();
            Schedule schedule = roomSchedules.get(r);
            for (ScheduleElement scheduleElement : schedule.getScheduleElements()) {
                if (scheduleElement.getCourse() != null && scheduleElement.getCourse().equals(course)) {
                    schedules.add(schedule);
                }
            }
        }

        Iterator<Academic> academics = acadademicSchedules.keySet().iterator();
        while (academics.hasNext()) {
            Academic a = academics.next();
            Schedule schedule = acadademicSchedules.get(a);
            for (ScheduleElement scheduleElement : schedule.getScheduleElements()) {
                if (scheduleElement.getCourse() != null && scheduleElement.getCourse().equals(course)) {
                    schedules.add(schedule);
                }
            }
        }

        Iterator<StudyProgram> studyPrograms = studyProgramSchedules.keySet().iterator();
        while (studyPrograms.hasNext()) {
            StudyProgram studyProgram = studyPrograms.next();
         //  if (studyProgram.containsCourse(course)) {
                for (Semester semester : studyProgram.getSemesters()) {
                    Schedule schedule = studyProgramSchedules.get(studyProgram).get(semester);
                    for (ScheduleElement scheduleElement : schedule.getScheduleElements()) {
                        if (scheduleElement.getCourse() != null && scheduleElement.getCourse().equals(course)) {
                            schedules.add(schedule);
                        }
                    }
                }
         //   }
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
        return acadademicSchedules.get(academic);
    }

    /**
     * Liefert den Plan für das Fachsemesters des gegebenen Studienganges
     *
     * @param studyProgram Der Studiengang
     * @param semester Das Fachsemesters des Studienganges
     * @return Der Semesterplan
     */
    public Schedule getSchedule(StudyProgram studyProgram, Semester semester) {

        return studyProgramSchedules.get(studyProgram).get(semester);
    }

    public void printRoomScheduleOverview(RoomType roomType) {
        final String formatPattern = "|%-22s|%-10s|%6s|%6s|%7s|%s%n";
        System.out.printf(formatPattern, "Raum", "Typ", "Plätze", "Kosten", "Termine", "Ausstattung");
        printSeparator(true);
        Iterator<Room> rooms = roomSchedules.keySet().iterator();
        while (rooms.hasNext()) {
            Room r = rooms.next();
            if (r.getType() == roomType) {

                Schedule schedule = roomSchedules.get(r);
                System.out.printf(formatPattern, r.getName(), r.getType(), r.getSeats(), r.getCosts(), schedule.getBlockedCoordinates().size() + "/" + generateMaximumCoordinates().size(), r.getAvailableEquipments());

            }
        }
    }

    public void printStatistics() {
        printHeader();
        printCoreStats();
        printSeparator(true);
        printRoomScheduleOverview(RoomType.INTERNAL);
        printSeparator(false);
        printRoomScheduleOverview(RoomType.EXTERNAL);
        printSeparator(false);
        printRoomSchedules(RoomType.INTERNAL);
        printSeparator(false);
        printRoomSchedules(RoomType.EXTERNAL);
        printSeparator(false);
        printAcademicSchedules();

    }

    private void printHeader() {
        printSeparator(false);
        System.out.println(" GESAMTPLAN ");
        printSeparator(false);
    }

    private void printSeparator(boolean single) {

        if (single) {
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        } else {
            System.out.println("===============================================================================================================================================================================================================================================================================================================================================================================");
        }

    }

    private void printCoreStats() {
        System.out.println("Räume (intern) : " + getRoomCount(RoomType.INTERNAL));
        System.out.println("Räume (extern) : " + getRoomCount(RoomType.EXTERNAL));
        System.out.println("Gesamtkosten   : " + getCosts());
        System.out.println("Anzahl Termine : " + getTotalBlocks(RoomType.INTERNAL) + getTotalBlocks(RoomType.EXTERNAL));
    }

    public void printRoomSchedules(RoomType roomType) {

        Iterator<Room> rooms = roomSchedules.keySet().iterator();
        while (rooms.hasNext()) {
            Room r = rooms.next();
            if (r.getType() == roomType) {
                printSeparator(false);
                System.out.println("Raumplan: " + r + "; Termine: " + roomSchedules.get(r).getBlockedCoordinates().size());

                printSchedule(roomSchedules.get(r), false);

            }
        }
    }

    public void printAcademicSchedules() {

        Iterator<Academic> academics = acadademicSchedules.keySet().iterator();
        while (academics.hasNext()) {
            Academic r = academics.next();
            printSeparator(false);
            System.out.println("Dozentenplan: " + r.getName() + "; Termine: " + acadademicSchedules.get(r).getBlockedCoordinates().size());

            printSchedule(acadademicSchedules.get(r), true);

        }
    }

    public void printSchedule(Schedule schedule, boolean withRoom) {
        final String formatPattern = "%-11s|%-70s|%-70s|%-70s|%-70s|%-70s|%n";

        System.out.printf(formatPattern, "Zeitrahmen", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag");
        printSeparator(true);

        String[] line = new String[6];

        for (int i = 0; i < 5; i++) {
            for (int y = 0; y < 5; y++) {
                ScheduleElement scheduleElement = schedule.getScheduleElement(new ScheduleCoordinate(Day.valueOf(y), TimeSlot.valueOf(i)));
                if (scheduleElement.isBlocked()) {
                    line[y + 1] = scheduleElement.getCourse().getName() + " (" + scheduleElement.getCourse().getNumber() + "; " + scheduleElement.getCourse().getAcademic().getName() + "; " + scheduleElement.getCourse().getStudents();
                    if (withRoom) {
                        line[y + 1] += "; " + scheduleElement.getRoom().getName();
                    }
                    line[y + 1] += ")";
                } else {
                    line[y + 1] = "";
                }

                line[0] = TimeSlot.valueOf(i).toString();

                if (y == 4) {

                    System.out.printf(formatPattern, line[0], line[1], line[2], line[3], line[4], line[5]);
                }
            }
        }

    }

}
