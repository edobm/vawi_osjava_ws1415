package osjava.tl3.logic.planning.strategies.helpers;

import java.util.Comparator;
import osjava.tl3.model.Room;

/**
 * Eine Hilfsklasse für die Sortierung von Räumen anhand der Anzahl der der
 * enthaltenen Equipments
 *
 * @author Meikel Bode
 */
public class RoomAvailableEquipmentComparator implements Comparator<Room> {

    /**
     * Die Sortierungreihenfolge dieser Instanz
     */
    private SortOrder sortOrder = SortOrder.ASCENDING;

    /**
     * Erzeugt eine Instanz mit aufsteigender Sortierung nach Anzahl verfügbarer
     * Equipments
     */
    public RoomAvailableEquipmentComparator() {
        this.sortOrder = SortOrder.ASCENDING;
    }

    /**
     * Erzeugt eine Instanz mit gegebener Sortierreihenfolge
     *
     * @param sortOrder Die Reihenfolge nach der sortiert wird
     */
    public RoomAvailableEquipmentComparator(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * Vergleicht Raum1 und Raum2 anhand der Anzahl verfügbaren Equipments
     * Abhängigkeit der konfigurierten Sortierreihenfolge
     *
     * @param room1 Raum1
     * @param room2 Raum2
     * @return Das Ergebnis des Vergleichs
     */
    @Override
    public int compare(Room room1, Room room2) {

        if (sortOrder == SortOrder.ASCENDING) {
            if (room1.getAvailableEquipments().size() < room2.getAvailableEquipments().size()) {
                return -1;
            } else if (room1.getAvailableEquipments().size() > room2.getAvailableEquipments().size()) {
                return 1;
            } else {
                return 0;
            }
        } else {
            if (room1.getAvailableEquipments().size() > room2.getAvailableEquipments().size()) {
                return -1;
            } else if (room1.getAvailableEquipments().size() < room2.getAvailableEquipments().size()) {
                return 1;
            } else {
                return 0;
            }
        }
    }

}