package osjava.tl3.logic.io;

import java.util.ArrayList;
import java.util.List;
import osjava.tl3.model.Equipment;
import osjava.tl3.model.Room;
import static osjava.tl3.model.RoomType.INTERNAL;
import osjava.tl3.model.controller.DataController;

/**
 * Diese Klasse behandelt das spezifische Zeilenformat für Raumbeschreibungen
 * und erzeugt Instanzen der Modellklasse "Room".
 *
 * @author Fabian Simon
 * @version 1.0
 */
public class RoomReader extends InputFileReader {

    /**
     * Diese Methode liest die Daten aus der Eingabedatei mit den Raumdaten ein,
     * verarbeitet diese und erzeugt für jeden Raum ein entsprechendes
     * Raum-Objekt. Diese Raum-Objekte werden in einer Liste gespeichert, welche
     * von der Methode zurückgegeben wird.
     *
     * @param fileName Name der Eingabedatei
     * @param dataController DataController-Instanz zur Ablage der Daten
     */
    public void readRooms(String fileName, DataController dataController) {
        ArrayList<String> roomData = readFile(fileName);

        // Raumname;Platzanzahl;Beamer,PC,Speaker;
        for (String roomDataRecord : roomData) {
            dataController.getRooms().add(getRoom(roomDataRecord, dataController));
        }

    }

    /**
     * Hilfsmethode für das Erzeugen von Raum-Objekten.
     *
     * @param roomDataRecord
     * @return erzeugtes Raum-Objekt
     */
    private Room getRoom(String roomDataRecord, DataController dataController) {

        //Erzeugen des Raum-Objektes
        Room room = new Room();

        //Zerlegen des Eingabe-String
        String[] roomData = roomDataRecord.split(";");

        //Name setzen
        room.setName(removeQuotationMarks(roomData[0]));

        //Platzanzahl setzen
        room.setSeats(Integer.parseInt(roomData[1]));

        //Equipment setzen falls vorhanden
        if (roomData.length == 3) {
            List<Equipment> equipments = parseEquipments(roomData[2]);
            room.setAvailableEquipments(equipments);
            for (Equipment e : equipments) {
                if (!dataController.getEquipments().contains(e)) {
                    dataController.getEquipments().add(e);
                }
            }
        } else {
            System.out.println("In Raum " + removeQuotationMarks(roomData[0]) + " kein Equipment vorhanden!");
        }

        //Typ setzen
        room.setType(INTERNAL);

        //Ausgabe des Raum-Objektes
        return room;
    }
}
