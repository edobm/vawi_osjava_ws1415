package osjava.tl3.model.controller;

import java.util.List;
import osjava.tl3.logic.io.CourseReader;
import osjava.tl3.logic.io.RoomReader;
import osjava.tl3.logic.io.StudyProgramReader;
import osjava.tl3.model.Course;
import osjava.tl3.model.Room;
import osjava.tl3.model.StudyProgram;

/**
 * Diese Klasse lädt die verschiedenen Eingabedaten mittels der verschiedenen Reader Ausprägungen.
 * Zudem hält sie die eingelesenen Daten für die spätere Nutzung der Klasse Scheduler.
 * 
 * @author Christoph Lurz
 * @version 1.0
 */
public class DataController
{
    private List<Room> rooms;
    private List<StudyProgram> studyPrograms;
    private List<Course> courses;
    
    private RoomReader roomReader;
    private StudyProgramReader studyProgrammReader;
    private CourseReader courseReader;
    
    /**
     * Die Eingabedaten mittels der Reader laden
     */
    public void load() {
    }
}
