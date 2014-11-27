package osjava.tl3.model;

import java.util.List;

/**
 * Diese Klasse repräsentiert die Entität Fachsemester.
 * 
 * Einfachsemester hat eine Bezeichnung und im Verlauf des Semesters werden 
 * Lehrveranstaltungen angeboten.
 * 
 * @author Christian Müller
 * @version 1.0
 */
public class Semester
{
    private String name;
    private List<Course> courses;

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
}
