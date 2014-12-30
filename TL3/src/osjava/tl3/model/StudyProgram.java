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
public class StudyProgram implements Comparable<StudyProgram> {

    /**
     * Der Name des Studiengangs
     */
    private String name;

    /**
     * Die Fachsemester des Studiengangs
     */
    private List<Semester> semesters = new ArrayList<>();

    /**
     * Prüft ob der gegebene Kurs Teil des Curriculums des Studienganges ist
     *
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
     * Liefert das Semester des Studiengangs, in dem der Kurs belegt werden
     * soll.
     *
     * @param course Der zu prüfende Kurs
     * @return Das Semester oder null, wenn der Kurs nicht Teil des
     * Studienganges ist
     */
    public Semester getSemesterByCourse(Course course) {
        for (Semester semester : semesters) {
            if (semester.getCourses().contains(course)) {
                return semester;
            }
        }
        return null;
    }
    
    /**
     * Liefert alle Kurse dieses Studiegangs
     * @return Die Liste der Kurse
     */
    public List<Course> getCourses() {
        List<Course> courses = new ArrayList<>();
        for (Semester semester : semesters) {
            courses.addAll(semester.getCourses());
        }
        return courses;
    }

    /**
     * Liefert die String-Darstellung
     *
     * @return Die String-Darstellung
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Liefert den Namen
     *
     * @return Der Name
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen
     *
     * @param name Der Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Liefert die Fachsemester
     *
     * @return Die Fachsemester
     */
    public List<Semester> getSemesters() {
        return semesters;
    }

    /**
     * Setzt die Fachsemester
     *
     * @param semesters Die Fachsemester
     */
    public void setSemesters(List<Semester> semesters) {
        this.semesters = semesters;
    }
    
    /**
     * Fügt ein Semester zur Liste der Semester hinzu
     * @param semester Semester das hinzugefügt werden soll
     */
    public void addSemester (Semester semester){
        semesters.add(semester);
    }

    /**
     * Überschreibt die Methode compareTo für die Klasse StudyProgram auf Basis des
     * Attributs Name
     *
     * @param other Das andere Objekte
     * @return Ob dieses Objekt vor, gleich oder hinter diesem Objekt in der
     * Folge steht
     */
    @Override
    public int compareTo(StudyProgram other) {
        return name.compareToIgnoreCase(other.name);
    }
}