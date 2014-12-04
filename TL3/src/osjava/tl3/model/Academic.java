package osjava.tl3.model;

import java.util.Objects;

/**
 * Diese Klasse repräsentiert die Entität Dozent.
 *
 * Zu diesem Zeitpunkt ist unklar, ob diese Klasse tatsächlich benötigt wird.
 * Wenn zu Dozenten keine weiteren Daten über die Eingabedateien geliefert
 * werden sollten, könnte diese Klasse obsolet werden.
 *
 * @author Christian Müller
 * @version 1.0
 */
public class Academic implements Comparable<Academic> {

    private String name;

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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Academic) {
            return this.name.equals(((Academic) obj).getName());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Academic other) {
        return name.compareToIgnoreCase(other.name);
    }

}
