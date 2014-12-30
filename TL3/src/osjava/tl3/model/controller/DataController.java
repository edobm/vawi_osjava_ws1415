package osjava.tl3.model.controller;

import java.util.ArrayList;
import java.util.List;
import osjava.tl3.logic.io.input.CourseReader;
import osjava.tl3.logic.io.input.RoomReader;
import osjava.tl3.logic.io.input.StudyProgramReader;
import osjava.tl3.model.Academic;
import osjava.tl3.model.Course;
import osjava.tl3.model.Equipment;
import osjava.tl3.model.Room;
import osjava.tl3.model.RoomType;
import osjava.tl3.model.StudyProgram;

/**
 * Diese Klasse lädt und hält die verschiedenen Eingabedaten mittels der
 * verschiedenen Reader Ausprägungen. Zudem hält sie die eingelesenen Daten für
 * die spätere Nutzung der Klasse Scheduler.
 *
 * @author Christoph Lurz
 * @version 1.0
 */
public class DataController {

    /**
     * Die Liste der Räume
     */
    private List<Room> rooms = new ArrayList<>();

    /**
     * Die Liste der Studiengänge
     */
    private List<StudyProgram> studyPrograms = new ArrayList<>();

    /**
     * Die Liste der Kurse
     */
    private List<Course> courses = new ArrayList<>();

    /**
     * Die Liste der Austattungsgegenstände
     */
    private List<Equipment> equipments = new ArrayList<>();

    /**
     * Readerinstanz für Räume
     */
    private RoomReader roomReader;

    /**
     * Readerinstanz für Studiengänge
     */
    private StudyProgramReader studyProgrammReader;

    /**
     * Readerinstanz für Kurse
     */
    private CourseReader courseReader;

    /**
     * Die Eingabedaten mittels der Reader laden
     */
    public void load() {
    }

    /**
     * Liefert den Raum mit der gegebenen Bezeichnung
     *
     * @param name Der Name des Raumes
     * @return Der Raum oder null, wenn unbekannt
     */
    public Room getRoomByName(String name) {
        for (Room room : getRooms()) {
            if (room.getName().equals(name)) {
                return room;
            }
        }
        return null;
    }

    /**
     * Liefert den Kurs mit der gegebenen Bezeichnung
     *
     * @param name Der Name des Kurses
     * @return Der Kurs oder null, wenn unbekannt
     */
    public Course getCourseByName(String name) {
        for (Course course : courses) {
            if (course.getName().equals(name)) {
                return course;
            }
        }
        return null;
    }

    /**
     * Liefert den Kurs mit der gegebenen ID
     *
     * @param id Die ID des Kurses
     * @return Der Kurs oder null, wenn unbekannt
     */
    public Course getCourseByID(String id) {
        for (Course course : courses) {
            if (course.getNumber().equals(id)) {
                return course;
            }
        }
        return null;
    }

    /**
     * Liefert den Dozenten mit dem gegebenen Namen
     *
     * @param name Der Name des Dozenten
     * @return Der Dozent oder null, wenn unbekannt
     */
    public Academic getAcademicByName(String name) {
        for (Course course : courses) {
            if (course.getAcademic().getName().equals(name)) {
                return course.getAcademic();
            }
        }
        return null;
    }

    /**
     * Liefert alle Dozenten
     *
     * @return Die Liste aller bekannten Dozenten
     */
    public List<Academic> getAcademics() {
        List<Academic> academics = new ArrayList<>();

        for (Course course : courses) {
            if (!academics.contains(course.getAcademic())) {
                academics.add(course.getAcademic());
            }
        }

        return academics;
    }

    /**
     * Liefert den Studiengang mit der gegebenen Bezeichnung
     *
     * @param name Der Name des Studienganges
     * @return Der Studiengang oder null, wenn unbekannt
     */
    public StudyProgram getStudyProgramByName(String name) {
        for (StudyProgram studyProgram : studyPrograms) {
            if (studyProgram.getName().equals(name)) {
                return studyProgram;
            }
        }
        return null;
    }

    /**
     * Liefert die Studiengänge in denen der gegebene Kurs Bestandteil des
     * Curriculums ist
     *
     * @param course Der Kurs für den Studiengänge gesucht werden
     * @return Die Liste der passenden Studiengänge
     */
    public List<StudyProgram> getStudyProgramsByCourse(Course course) {

        List<StudyProgram> studyProgrammsWithCourse = new ArrayList<>();

        for (StudyProgram studyProgramm : studyPrograms) {
            if (studyProgramm.containsCourse(course)) {
                studyProgrammsWithCourse.add(studyProgramm);
            }
        }

        return studyProgrammsWithCourse;
    }

    /**
     * Erzeugt einen externen Raum und einen korrespondierenden Plan. Weiterhin
     * werden der Raum und der Plan der Liste der Raumpläne hinzufügt.
     *
     * @return Der neue, externe Raum
     */
    public Room createExternalRoom() {

        /**
         * Einen Raum erzeugen
         */
        Room room = new Room();
        room.setType(RoomType.EXTERNAL);
        room.setName("Externer Raum " + (getRooms(RoomType.EXTERNAL).size() + 1));
        room.setSeats(10000);
        room.setAvailableEquipments(equipments);

        /**
         * Den externen Raum hinzufügen
         */
        rooms.add(room);

        return room;
    }

    /**
     * Liefert die Räume
     *
     * @return Die Räume
     */
    public List<Room> getRooms() {
        return rooms;
    }

    /**
     * Liefert die Räume des angegebenen Typs
     *
     * @param roomType Der Raumtyp
     * @return Die Räume
     */
    public List<Room> getRooms(RoomType roomType) {
        List<Room> list = new ArrayList<>();

        for (Room room : rooms) {
            if (room.getType() == roomType) {
                list.add(room);
            }
        }
        return list;
    }

    /**
     * Setzt die Räume
     *
     * @param rooms Die Räume
     */
    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    /**
     * Liefert die Studiengänge
     *
     * @return Die Studiengänge
     */
    public List<StudyProgram> getStudyPrograms() {
        return studyPrograms;
    }

    /**
     * Setzt die Studiengänge
     *
     * @param studyPrograms Die Studiengänge
     */
    public void setStudyPrograms(List<StudyProgram> studyPrograms) {
        this.studyPrograms = studyPrograms;
    }

    /**
     * Fügt einen Studiengang zur Liste der Studiengänge hinzu
     *
     * @param studyProgram Studiengang der hinzugefügt werden soll
     */
    public void addStudyProgram(StudyProgram studyProgram) {
        studyPrograms.add(studyProgram);
    }

    /**
     * Liefert die Kurse
     *
     * @return Die Kurse
     */
    public List<Course> getCourses() {
        return courses;
    }

    /**
     * Setzt die Kurse
     *
     * @param courses Die Kurse
     */
    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    /**
     * Fügt einen Kurs zur Liste der Kurse hinzu
     *
     * @param course Kurs der hinzugefügt werden soll
     */
    public void addCourse(Course course) {
        courses.add(course);
    }

    /**
     * Liefert die Ausstattungsgegenstände
     *
     * @return Die Ausstattungsgegenstände
     */
    public List<Equipment> getEquipments() {
        return equipments;
    }

    /**
     * Setzt die Ausstattungsgegenstände
     *
     * @param equipments Die Ausstattungsgegenstände
     */
    public void setEquipments(List<Equipment> equipments) {
        this.equipments = equipments;
    }

    /**
     * Fügt einen Ausstattungsgegenstand zur Liste der Ausstattungsgegenstände
     * hinzu
     *
     * @param equipment Ausstattungsgegenstand der hinzugefügt werden soll
     */
    public void addEquipment(Equipment equipment) {
        equipments.add(equipment);
    }

}
