package osjava.tl3.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse repräsentiert die Entität Studiengang.
 *
 * Ein Studiengang hat eine Bezeichnung und besteht aus Fachsemestern. In jedem
 * Fachsemester werden bestimmte Lehrveranstaltungen angeboten.
 *
 * @author Christian Müller
 * @version 1.0
 */
public class StudyProgram {

    private String name;
    private List<Semester> semesters = new ArrayList<>();

    /**
     * Prüft ob der gegebene Kurs Teil des Curriculums des Studienganges ist
     * @param course Der zur prüfende Kurs
     * @return Ja oder Nein
     */
    public boolean containsCourse(Course course) {
        for (Semester semester : semesters) {
            if (semester.getCourses().contains(course)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Liefert das Semester des Studiengangs, in dem der Kurs belegt werden soll.
     * @param course Der zu prüfende Kurs
     * @return Das Semester oder null, wenn der Kurs nicht Teil des Studienganges ist
     */
    public Semester getSemesterByCourse(Course course) {
        for (Semester semester : semesters) {
            if (semester.getCourses().contains(course)) {
                return semester;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return name;
    }

    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the semesters
     */
    public List<Semester> getSemesters() {
        return semesters;
    }

    /**
     * @param semesters the semesters to set
     */
    public void setSemesters(List<Semester> semesters) {
        this.semesters = semesters;
    }
}
