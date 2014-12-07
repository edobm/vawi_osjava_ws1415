package osjava.tl3.logic.io;

import java.util.ArrayList;
import java.util.List;
import osjava.tl3.model.Room;
import static osjava.tl3.model.RoomType.INTERNAL;

/**
 * Diese Klasse behandelt das spezifische Zeilenformat für Raumbeschreibungen
 * und erzeugt Instanzen der Modellklasse "Room".
 * 
 * @author Fabian Simon
 * @version 1.0
 */
public class RoomReader extends InputFileReader
{

    /**
     * Diese Methode liest die Daten aus der Eingabedatei mit den Raumdaten ein,
     * verarbeitet diese und erzeugt für jeden Raum ein entsprechendes Raum-Objekt.
     * Diese Raum-Objekte werden in einer Liste gespeichert, welche von der Methode 
     * zurückgegeben wird.
     *
     * @return  Die Liste mit Objekten der verfügbaren internen Räume.
     */
    public List<Room> readRooms() {
        ArrayList<String> roomData = super.readFile("raeume.csv");
        
        
        // Raumname;Platzanzahl;Beamer,PC,Speaker;
        List<Room> rooms = new ArrayList<>();
        for (String roomDataRecord : roomData) {
            rooms.add(getRoom(roomDataRecord));
        }
        
        
        return rooms;
    }
    
    /**
     * Hilfsmethode für das Erzeugen von Raum-Objekten.
     * 
     * @param roomDataRecord
     * @return Raum-Objekt
     */
    private Room getRoom(String roomDataRecord) {
        
        Room room = new Room();
        String [] roomData = roomDataRecord.split(";");
        
        //Name setzen
        room.setName(roomData[1].replaceAll("\"", ""));
        
        //Platzanzahl setzen
        room.setSeats(Integer.parseInt(roomData[2]));
        
        //Equipment setzen
        room.setAvailableEquipments(null);
        
        //Typ setzen
        room.setType(INTERNAL);
        
        return room;
    }
}
