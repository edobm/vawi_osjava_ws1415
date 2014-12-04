package osjava.tl3.model;
  
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
public class Course implements Comparable<Course>
{
    private CourseType type;
    private String number;
    private String name;
    private Academic academic;
    private List<Equipment> requiredEquipments = new ArrayList<>();;
    private int students;

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Course) {
            Course other = (Course)obj;
            return this.getNumber().equals(other.getNumber());
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.type);
        hash = 59 * hash + Objects.hashCode(this.number);
        return hash;
    }
    
    @Override
    public String toString() {
         return name + "(" + number + ";" + academic.getName() + ";"+ students + ")";
    }
    
    
    public CourseType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(CourseType type) {
        this.type = type;
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
     * @return the academic
     */
    public Academic getAcademic() {
        return academic;
    }

    /**
     * @param academic the academic to set
     */
    public void setAcademic(Academic academic) {
        this.academic = academic;
    }

    /**
     * @return the requiredEquipments
     */
    public List<Equipment> getRequiredEquipments() {
        return requiredEquipments;
    }

    /**
     * @param requiredEquipments the requiredEquipments to set
     */
    public void setRequiredEquipments(List<Equipment> requiredEquipments) {
        this.requiredEquipments = requiredEquipments;
    }

    /**
     * @return the students
     */
    public int getStudents() {
        return students;
    }

    /**
     * @param students the students to set
     */
    public void setStudents(int students) {
        this.students = students;
    }

    /**
     * @return the number
     */
    public String getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public int compareTo(Course other) {
        return name.compareToIgnoreCase(other.name);
    }
}
