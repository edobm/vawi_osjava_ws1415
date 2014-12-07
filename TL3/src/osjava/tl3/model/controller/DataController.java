package osjava.tl3.model.controller;

import java.util.ArrayList;
import java.util.List;
import osjava.tl3.logic.io.CourseReader;
import osjava.tl3.logic.io.RoomReader;
import osjava.tl3.logic.io.StudyProgramReader;
import osjava.tl3.model.Academic;
import osjava.tl3.model.Course;
import osjava.tl3.model.Equipment;
import osjava.tl3.model.Room;
import osjava.tl3.model.StudyProgram;

/**
 * Diese Klasse lädt und hält die verschiedenen Eingabedaten mittels der verschiedenen
 * Reader Ausprägungen. Zudem hält sie die eingelesenen Daten für die spätere
 * Nutzung der Klasse Scheduler.
 *
 * @author Christoph Lurz
 * @version 1.0
 */
public class DataController {

    private List<Room> rooms = new ArrayList<>();
    private List<StudyProgram> studyPrograms = new ArrayList<>();
    private List<Course> courses = new ArrayList<>();
    private List<Equipment> equipments = new ArrayList<>();

    private RoomReader roomReader;
    private StudyProgramReader studyProgrammReader;
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
     * @return the rooms
     */
    public List<Room> getRooms() {
        return rooms;
    }

    /**
     * @param rooms the rooms to set
     */
    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    /**
     * @return the studyPrograms
     */
    public List<StudyProgram> getStudyPrograms() {
        return studyPrograms;
    }

    /**
     * @param studyPrograms the studyPrograms to set
     */
    public void setStudyPrograms(List<StudyProgram> studyPrograms) {
        this.studyPrograms = studyPrograms;
    }

    /**
     * @return the courses
     */
    public List<Course> getCourses() {
        return courses;
    }

    /**
     * @param courses the courses to set
     */
    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    /**
     * @return the equipments
     */
    public List<Equipment> getEquipments() {
        return equipments;
    }

    /**
     * @param equipments the equipments to set
     */
    public void setEquipments(List<Equipment> equipments) {
        this.equipments = equipments;
    }

}
