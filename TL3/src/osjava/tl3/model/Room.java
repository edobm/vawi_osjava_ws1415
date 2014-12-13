package osjava.tl3.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Diese Klasse repräsentiert die Entität Raum.
 *
 * Ein Raum hat eine Bezeichnung, ist von einem bestimmten Typ (intern oder
 * extern) und zeichnet sich durch verfügbares Equipment und eine maximale
 * Anzahl von Sitzplätzen aus.
 *
 * Anhand des Raumtyps kann ermittelt werden, ob der Raum angemietet werden muss
 * und welche Kosten dadurch entstehen.
 *
 * @author Christoph Lurz
 * @version 1.0
 */
public class Room implements Comparable<Room> {

    /**
     * Die eindeutige Raumnummer. Wird mit jeder weiteren Instanz inkrementiert
     */
    private static int counter = 1;

    /**
     * Die ID des Raumes (wird berechnet)
     */
    private final String roomId;

    /**
     * Der Typ des Raumes
     */
    private RoomType type;

    /**
     * Der Name des Raumes
     */
    private String name;

    /**
     * Das im Raum verfügbare Equipment
     */
    private List<Equipment> availableEquipments = new ArrayList<>();

    /**
     * Die Anzahl der Sitzplätze
     */
    private int seats;

    /**
     * Erzeugt eine neue Instanz von Raum und inkrementiert dabei den statischen
     * Zähler für die Erzeugung einer eindeutigen Raum ID.
     */
    public Room() {

        /**
         * Jede neue Instanz erzeugt eine neue eindeutige Raumnummer
         */
        roomId = String.valueOf(counter++);
    }

    /**
     * Vergleicht zwei Instanzen von Raum
     *
     * @param other Die andere Instanz
     * @return Ob diese Instanz vor, gleich oder hinter diesem in der
     * Reihenfolge steht bezogen auf die Raum ID
     */
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other instanceof Room) {
            return this.roomId.equals(((Room) other).roomId);
        } else {
            return false;
        }

    }

    /**
     * Berechnet den HashCode
     *
     * @return Der HashCode
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.roomId);
        return hash;
    }

    /**
     * Die String-Darstellung
     *
     * @return Die String-Darstellung
     */
    @Override
    public String toString() {
        return name + " (" + seats + ")";
    }

    /**
     * Liefert den Raumtyp
     *
     * @return Der Raumtyp
     */
    public RoomType getType() {
        return type;
    }

    /**
     * Setzt den Raumtyp
     *
     * @param type Der Raumtyp
     */
    public void setType(RoomType type) {
        this.type = type;
    }

    /**
     * Liefert den Namen
     *
     * @return Der Name
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen
     *
     * @param name Der Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Liefert das verfügbare Equipment
     *
     * @return Das verfügbare Equipment
     */
    public List<Equipment> getAvailableEquipments() {
        return availableEquipments;
    }

    /**
     * Setzt das verfügbare Equipment
     *
     * @param availableEquipments Das verfügbare Equipment
     */
    public void setAvailableEquipments(List<Equipment> availableEquipments) {
        this.availableEquipments = availableEquipments;
    }

    /**
     * Liefert die Anzahl der Sitzplätze
     *
     * @return Die Anzahl der Sitzplätze
     */
    public int getSeats() {
        return seats;
    }

    /**
     * Setzt die Anzahl der Sitzplätze
     *
     * @param seats Die Anzahl der Sitzplätze
     */
    public void setSeats(int seats) {
        this.seats = seats;
    }

    /**
     * Liefert die Raum ID
     *
     * @return the roomId
     */
    public String getRoomId() {
        return roomId;
    }

    /**
     * Überschreibt die Methode compareTo für die Room Course auf Basis des
     * Attributs Name
     *
     * @param other Das andere Objekte
     * @return Ob dieses Objekt vor, gleich oder hinter diesem Objekt in der
     * Folge steht
     */
    @Override
    public int compareTo(Room other) {
        return name.compareToIgnoreCase(other.name);
    }

}
