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
 * @author Christian Müller
 * @version 1.0
 */
public class Semester
{
    private StudyProgram studyProgram;
    private String name;
    private List<Course> courses = new ArrayList<>();

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
       if (obj instanceof Semester) {
           Semester other = (Semester)obj;
           
           return this.getName().equals(other.getName()) && this.getStudyProgram().getName().equals(other.getStudyProgram().getName());
       }
       else {
           return false;
       }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.studyProgram);
        hash = 61 * hash + Objects.hashCode(this.name);
        return hash;
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
     * @return the studyProgram
     */
    public StudyProgram getStudyProgram() {
        return studyProgram;
    }

    /**
     * @param studyProgram the studyProgram to set
     */
    public void setStudyProgram(StudyProgram studyProgram) {
        this.studyProgram = studyProgram;
    }
}
