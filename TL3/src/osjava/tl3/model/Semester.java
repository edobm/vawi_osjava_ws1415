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
}
