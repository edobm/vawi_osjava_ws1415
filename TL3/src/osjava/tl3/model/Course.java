package osjava.tl3.model;
  
import java.util.List;

/**
 * Diese Klasse repräsentiert die Entität Lehrveranstaltung.
 * 
 * Eine Lehrveranstaltung hat eine Bezeichnung und ist von einem bestimmten Typ (Vorlesung, Tutorium) 
 * und wird von einem Dozenten geleitet.
 * 
 * Weiterhin stellt eine Lehrveranstaltung bestimmte Anforderungen an die Austattung 
 * (Beamer, Lautsprecher...) eines Raumes.
 * 
 * @author Christian Müller
 * @version 1.0
 */
public class Course
{
    private CourseType type;
    private String name;
    private Academic academic;
    private List<Equipment> requiredEquipments;
    
    public CourseType getType() {
        return type;
    }
}
