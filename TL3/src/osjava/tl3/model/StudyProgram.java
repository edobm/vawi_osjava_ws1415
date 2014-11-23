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
}
