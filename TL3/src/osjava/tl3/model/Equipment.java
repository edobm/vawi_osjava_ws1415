package osjava.tl3.model;

import java.util.Objects;

/**
 * Diese Klasse repräsentiert Ausstattungsgegenstände
 *
 * @author Meikel Bode
 */
public class Equipment {

    private String name;

    public Equipment(String name) {
        this.name = name;
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Equipment) {
            return this.name.equals(((Equipment) obj).getName());
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

}
