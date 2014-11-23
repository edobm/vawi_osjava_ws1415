package osjava.tl3.logic.io;

import java.util.ArrayList;
import java.util.List;
import osjava.tl3.model.Room;

/**
 * Diese Klasse behandelt das spezifische Zeilenformat für Raumbeschreibungen
 * und erzeugt Instanzen der Modellklasse "Room".
 * 
 * @author Fabian Simon
 * @version 1.0
 */
public class RoomReader extends FileReader
{
    public List<Room> readRooms() {
        List<String> roomRecords = super.readFile("room.txt");
        
        // Raumname;Platzanzahl;Beamer,PC,Speaker;
        List<Room> rooms = new ArrayList<Room>();
        for (String roomRecord : roomRecords) {
            rooms.add(getRoom(roomRecord));
        }
        
        
        return rooms;
    }
    
    private Room getRoom(String roomRecord) {
        
        Room r = new Room();
      
       
       return r;
    }
}
