package osjava.tl3.model;

import java.util.List;

/**
 * Diese Klasse repräsentiert die Entität Studiengang.
 * 
 * Ein Studiengang hat eine Bezeichnung und besteht aus Fachsemestern.
 * In jedem Fachsemester werden bestimmte Lehrveranstaltungen angeboten.
 * 
 * @author Christian Müller
 * @version 1.0
 */
public class StudyProgram
{
    
    private String name;
    private List<Semester> semesters;

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
