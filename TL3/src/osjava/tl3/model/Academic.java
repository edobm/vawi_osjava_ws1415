package osjava.tl3.model;

import java.util.Objects;

/**
 * Diese Klasse repräsentiert die Entität Dozent.
 *
 * @author Christoph Lurz
 */
public class Academic implements Comparable<Academic> {

    /**
     * Der Name des Dozenten
     */
    private String name;

    /**
     * Liefert den Namen des Dozenten
     *
     * @return Der Name
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen des Dozenten
     *
     * @param name Der Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Überschreibt die Methode Equals
     *
     * @param obj Das Objekt, das mit dieser Instanz verglichen werden soll
     * @return Ob die Objekte gleich sind
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Academic) {
            return this.name.equals(((Academic) obj).getName());
        } else {
            return false;
        }
    }

    /**
     * Überschreibt die Methode hashCode. Relevant für die Nutzung dieser Klasse
     * in HashTables/HashMaps
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.name);
        return hash;
    }

    /**
     * Liefert die String-Darstellung
     *
     * @return Die String-Darstellung
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Überschreibt die Methode compareTo für die Klasse Academic auf Basis des
     * Attributs Name
     *
     * @param other Das andere Objekte
     * @return Ob dieses Objekt vor, gleich oder hinter diesem Objekt in der
     * Folge steht
     */
    @Override
    public int compareTo(Academic other) {
        return name.compareToIgnoreCase(other.name);
    }

}
