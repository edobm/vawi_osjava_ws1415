package osjava.tl3.logic.planning.strategies.helpers;

import java.util.Comparator;
import osjava.tl3.model.Room;

/**
 *
 * @author Meikel Bode
 */
public class RoomAvailableEquipmentComparator implements Comparator<Room> {

    public enum SortOrder {

        ASCENDING,
        DESCENDING
    }

    private SortOrder sortOrder = SortOrder.ASCENDING;

    /**
     * Erzeugt eine Instanz mit aufsteigender Sortierung nach Anzahl Studenten
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

    @Override
    public int compare(Room o1, Room o2) {
        if (o1.getAvailableEquipments().size() < o2.getAvailableEquipments().size()) {
            return -1;
        } else if (o1.getAvailableEquipments().size() > o2.getAvailableEquipments().size()) {
            return 1;
        } else {
            return 0;
        }
    }

    public static RoomAvailableEquipmentComparator getInstance(SortOrder sortOrder) {
        return new RoomAvailableEquipmentComparator(sortOrder);
    }

}
