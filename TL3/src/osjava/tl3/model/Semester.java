package osjava.tl3.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Diese Klasse repräsentiert die Entität Fachsemester.
 *
 * Einfachsemester hat eine Bezeichnung und im Verlauf des Semesters werden
 * Lehrveranstaltungen angeboten.
 *
 * @author Christoph Lurz
 */
public class Semester {

    /**
     * Der Studiengang zu dem dieses Semester gehört
     */
    private StudyProgram studyProgram;

    /**
     * Die Bezeichnung dieses Fachsemsters
     */
    private String name;

    /**
     * Die Kurse dieses Fachsemesters
     */
    private List<Course> courses = new ArrayList<>();

     /**
     * Vergleicht zwei Instanzen von Semester mithilfe von Name und Studiengangsbezeichnung
     *
     * @param obj Das andere Semester
     * @return Ob diese Instanz gleich der anderen ist
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Semester) {
            Semester other = (Semester) obj;

            return this.getName().equals(other.getName()) && this.getStudyProgram().getName().equals(other.getStudyProgram().getName());
        } else {
            return false;
        }
    }

    /**
     * Berechnet den HashCode
     * @return Der HashCode
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.studyProgram);
        hash = 61 * hash + Objects.hashCode(this.name);
        return hash;
    }

    /**
     * Liefert die String-Darstellung
     * @return Die String-Darstellung
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Liefert den Namen
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
     * Liefert die Kurse
     * @return Die Kurse
     */
    public List<Course> getCourses() {
        return courses;
    }

    /**
     * Setzt die Kurse
     * @param courses Die Kurse
     */
    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    /**
     * Liefert den Studiengang
     * @return Der Studiengang
     */
    public StudyProgram getStudyProgram() {
        return studyProgram;
    }

    /**
     * Setzt den Studiengang
     * @param studyProgram Der Studiengang
     */
    public void setStudyProgram(StudyProgram studyProgram) {
        this.studyProgram = studyProgram;
    }
}