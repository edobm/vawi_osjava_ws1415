package osjava.tl3.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
 * Unklar ist zu diesem Zeitpunkt, ob externe Räume über die Eingabedateien
 * geliefert werden oder ob externe Räume "unbegrenzt" zur Verfügung stehen.
 * Zudem ist unklar, ob die Kosten eines externen Raumes von der tatsächlichen
 * Anzahl der Teilnehmer oder von der verfügbaren Anzahl der Plätze abhängnt
 * (letztes ist die Annahme).
 *
 * @author Christoph Lurz
 * @version 1.0
 */
public class Room {

    private static int COST_PER_SEAT = 10;

    
    /**
     * @return the COST_PER_SEAT
     */
    public static int getCOST_PER_SEAT() {
        return COST_PER_SEAT;
    }

    /**
     * @param aCOST_PER_SEAT the COST_PER_SEAT to set
     */
    public static void setCOST_PER_SEAT(int aCOST_PER_SEAT) {
        COST_PER_SEAT = aCOST_PER_SEAT;
    }
    
    private String roomId;

    private RoomType type;
    private String name;
    private List<Equipment> availableEquipments = new ArrayList<>();
    private int seats;

    public Room() {
        roomId = UUID.randomUUID().toString().toUpperCase();
    }

    @Override
    public boolean equals(Object other) {
       if (other == null) {
           return false;
       }
       if (other instanceof Room) {
           return this.roomId.equals(((Room)other).roomId);
       }
       else {
           return false;
       }
        
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.roomId);
        return hash;
    }

    
    
    @Override
    public String toString() {
        return name + " (" + seats + ")";
    }

    /**
     * Berechnet die Kosten eines Raumes, wenn dieser Extern angemietet werden
     * muss unter der Annahme, dass die Kosten von der Anzahl verfügbarer
     * Sitzeplätze abhängt.
     *
     * @return Die Kosten eines externen Raumes
     */
    public int getCosts() {
        return type == RoomType.EXTERNAL ? seats * Room.COST_PER_SEAT : 0;
    }

    /**
     * @return the type
     */
    public RoomType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(RoomType type) {
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
     * @return the availableEquipments
     */
    public List<Equipment> getAvailableEquipments() {
        return availableEquipments;
    }

    /**
     * @param availableEquipments the availableEquipments to set
     */
    public void setAvailableEquipments(List<Equipment> availableEquipments) {
        this.availableEquipments = availableEquipments;
    }

    /**
     * @return the seats
     */
    public int getSeats() {
        return seats;
    }

    /**
     * @param seats the seats to set
     */
    public void setSeats(int seats) {
        this.seats = seats;
    }

    /**
     * @return the roomId
     */
    public String getRoomId() {
        return roomId;
    }

}
