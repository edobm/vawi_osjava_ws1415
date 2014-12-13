package osjava.tl3.model;

import java.util.Objects;

/**
 * Diese Klasse dient der Differenzierung der Veranstaltungstypen
 *
 * @author Christoph Lurs
 */
public class CourseType {

    /**
     * Der Name des Kurstyps
     */
    private String name;

    /**
     * Erzeugt eine neue Instanz eines Kurstyps mit der angegebenen Bezeichnung
     * @param name Der Name des Kurstyps
     */
    public CourseType(String name) {
        this.name = name;
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
     * @param name Der Name
     */
    public void setName(String name) {
        this.name = name;
    }

     /**
     * Vergleicht zwei Instanzen von CourseType
     * @param obj Der andere Kurstyp
     * @return Ob dieser Kurstyp vor, gleich oder hinter diesem in der Reihefolge steht bezogen auf dem Namen
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CourseType) {
            return this.name.equals(((CourseType) obj).getName());
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
        hash = 59 * hash + Objects.hashCode(this.name);
        return hash;
    }

    /**
     * Die String-Darstellung
     * @return Die String-Darstellung
     */
    @Override
    public String toString() {
        return name;
    }

}
