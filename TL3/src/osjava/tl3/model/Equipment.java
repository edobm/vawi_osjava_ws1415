package osjava.tl3.model;

import java.util.Objects;

/**
 * Diese Klasse repräsentiert Ausstattungsgegenstände
 *
 * @author Christoph Lurz
 */
public class Equipment {

    /**
     * Der Name des Equipments
     */
    private String name;

    public Equipment(String name) {
        this.name = name;
    }

    /**
     * Liefert den Namen des Equipments
     *
     * @return Der Name
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen des Equipments
     *
     * @param name Der Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Vergleicht zwei Instanzen von Equipment
     *
     * @param obj Die andere Instanz
     * @return Ob diese Instanz vor, gleich oder hinter diesem in der
     * Reihenfolge steht bezogen auf den Namen
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Equipment) {
            return this.name.equals(((Equipment) obj).getName());
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