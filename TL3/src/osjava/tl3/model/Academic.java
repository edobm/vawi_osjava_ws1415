package osjava.tl3.model;
  
/**
 * Diese Klasse repräsentiert die Entität Dozent.
 * 
 * Zu diesem Zeitpunkt ist unklar, ob diese Klasse tatsächlich benötigt wird.
 * Wenn zu Dozenten keine weiteren Daten über die Eingabedateien geliefert werden sollten,
 * könnte diese Klasse obsolet werden.
 * 
 * @author Christian Müller
 * @version 1.0
 */
public class Academic
{
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
}
