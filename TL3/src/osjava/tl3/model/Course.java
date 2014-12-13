package osjava.tl3.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Diese Klasse repräsentiert die Entität Lehrveranstaltung.
 *
 * Eine Lehrveranstaltung hat eine Bezeichnung und ist von einem bestimmten Typ
 * (Vorlesung, Tutorium) und wird von einem Dozenten geleitet.
 *
 * Weiterhin stellt eine Lehrveranstaltung bestimmte Anforderungen an die
 * Austattung (Beamer, Lautsprecher...) eines Raumes.
 *
 * @author Christoph Lurz
 * @version 1.0
 */
public class Course implements Comparable<Course> {

    /**
     * Der Kurstyp
     */
    private CourseType type;

    /**
     * Die Kursnummer
     */
    private String number;

    /**
     * Der Kursname
     */
    private String name;

    /**
     * Der Dozent
     */
    private Academic academic;

    /**
     * Das für den Kurs benötigte Equipment
     */
    private List<Equipment> requiredEquipments = new ArrayList<>();

    /**
     * Die Anzahl der Teilnehmer
     */
    private int students;

    /**
     * Liefert den Typ des Kurses
     *
     * @return Der Typ
     */
    public CourseType getType() {
        return type;
    }

    /**
     * Setzt den Typ des Kurses
     *
     * @param type Der Typ des Kurses
     */
    public void setType(CourseType type) {
        this.type = type;
    }

    /**
     * Liefert den Namen des Kurses
     *
     * @return Der Name
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen des Kurses
     *
     * @param name Der Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Liefert den Dozenten
     *
     * @return Der Dozent
     */
    public Academic getAcademic() {
        return academic;
    }

    /**
     * Setzt den Dozenten
     *
     * @param academic Der Dozent
     */
    public void setAcademic(Academic academic) {
        this.academic = academic;
    }

    /**
     * Liefert das für den Kurs erforderliche Equipment
     *
     * @return Das Equipment
     */
    public List<Equipment> getRequiredEquipments() {
        return requiredEquipments;
    }

    /**
     * Setzt das erforderliche Equipment
     *
     * @param requiredEquipments the requiredEquipments to set
     */
    public void setRequiredEquipments(List<Equipment> requiredEquipments) {
        this.requiredEquipments = requiredEquipments;
    }

    /**
     * Liefert die Anzahl der Teilnehmer
     *
     * @return Die Anzahl
     */
    public int getStudents() {
        return students;
    }

    /**
     * Setzt die Anzahl der Studenten
     *
     * @param students Die Anzahl
     */
    public void setStudents(int students) {
        this.students = students;
    }

    /**
     * Liefert die Kursnummer
     *
     * @return Die Nummer
     */
    public String getNumber() {
        return number;
    }

    /**
     * Setzt die Kursnummer
     *
     * @param number Die Nummer
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * Überschreibt die Methode compareTo für die Klasse Course auf Basis des
     * Attributs Name
     *
     * @param other Das andere Objekte
     * @return Ob dieses Objekt vor, gleich oder hinter diesem Objekt in der
     * Folge steht
     */
    @Override
    public int compareTo(Course other) {
        return name.compareToIgnoreCase(other.name);
    }

    /**
     * Vergleicht zwei Instanzen von Course
     *
     * @param obj Der andere Kurs
     * @return Ob diese Instanz gleich der anderen ist
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Course) {
            Course other = (Course) obj;
            return this.getNumber().equals(other.getNumber());
        } else {
            return false;
        }
    }

    /**
     * Berecht den HashCode diser Instanz
     *
     * @return Der HashCode
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.type);
        hash = 59 * hash + Objects.hashCode(this.number);
        return hash;
    }

    /**
     * Die String-Darstellung dieses Kurses
     *
     * @return Die String-Darstellung
     */
    @Override
    public String toString() {
        return name + "(" + number + ";" + academic.getName() + ";" + students + ")";
    }

}
