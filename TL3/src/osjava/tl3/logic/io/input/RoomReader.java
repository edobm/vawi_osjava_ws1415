package osjava.tl3.logic.io.input;

import java.util.ArrayList;
import java.util.List;
import osjava.tl3.logging.Protocol;
import osjava.tl3.model.Equipment;
import osjava.tl3.model.Room;
import static osjava.tl3.model.RoomType.INTERNAL;
import osjava.tl3.model.controller.DataController;

/**
 * Diese Klasse behandelt das spezifische Zeilenformat für Raumbeschreibungen
 * und erzeugt Instanzen der Modellklasse "Room".
 *
 * @author Fabian Simon
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
        String roomDataRecord;
        for (int i = 0; i < roomData.size(); i++) {

            roomDataRecord = roomData.get(i);

            if (roomDataRecord == null || roomDataRecord.length() == 0) {

                Protocol.log("Fehler in Raumdatei: " + fileName + " auf Zeile " + (i + 1) + ": Zeile wird ignoriert");
            }
            
            // Zeilenweise die Datei verarbeiten
            try {
                dataController.getRooms().add(getRoom(i + 1, roomDataRecord, dataController));
            } catch (InputFileReaderException e) {
                Protocol.log("Fehler in Raumdatei: " + fileName + " auf Zeile " + (i + 1) + ":" + e.getMessage() + ": Zeile wird ignoriert");
            }
        }
    }

    /**
     * Hilfsmethode für das Erzeugen von Raum-Objekten.
     *
     * @param roomDataRecord
     * @return erzeugtes Raum-Objekt
     */
    private Room getRoom(int rowNumber, String roomDataRecord, DataController dataController) throws InputFileReaderException {

        //Erzeugen des Raum-Objektes
        Room room = new Room();

        //Zerlegen des Eingabe-String
        String[] roomData = validateRecord(rowNumber, roomDataRecord);

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
            Protocol.log("In Raum " + room.getName() + " kein Equipment vorhanden!");
        }

        //Typ setzen
        room.setType(INTERNAL);

        //Ausgabe des Raum-Objektes
        return room;
    }

    /**
     * Validiert Datensätze aus Raumdateien
     *
     * @param rowNumber Die aktuelle Zeilenzahl
     * @param recordLine Die aktuelle Zeile aus der Datei
     * @return Die Spalten der Zeile
     * @throws InputFileReaderException Wenn ein Validierungsfehler aufgetreten
     * sein sollte
     * 
     * @see InputFileReader#validateRecord(int, java.lang.String) 
     */
    @Override
    protected String[] validateRecord(int rowNumber, String recordLine) throws InputFileReaderException {

        /**
         * Spaltentrenner
         */
        final String delimiter = ";";

        /**
         * Zeile zerlegen
         */
        String[] record = recordLine.split(delimiter);

        /**
         * Beispielzeile "NAT-Labor";50;"Mechanikbaukasten,
         * Elektrotechnikbaukasten, Tafel, Dunstabzugshaube,
         * Chemikaliensortiment, Periodensystem, Uranbrennstaebe, Bleiwesten"
         */
        /**
         * Es werden 2 oder 3 Spalten pro Raumdatensatz erwartet:
         *
         * 2 Spalten: Name und Anzahl Sitzplätze aber kein Equipment 3 Spalte:
         * Wie 2 und zusätzlich Equipment
         *
         */
        if (record.length > 3) {
            throw new InputFileReaderException("Ungültige Spaltenanzahl für Datensatz. Erwartet: 3, Ist: " + record.length, null, recordLine, rowNumber);
        }

        /**
         * Prüfung Spalte Raumname (Index 0): Leerer Wert
         */
        InputValidator.validateEmpty("Name", record[0], recordLine, rowNumber);
        InputValidator.validateStardEndsWithBraces("Name", record[0], recordLine, rowNumber);

        /**
         * Prünfung Spalte Sitzplanzahl (Index 1)
         */
        InputValidator.validateEmpty("Sitzplätze", record[1], recordLine, rowNumber);
        InputValidator.validateInteger("Sitzplätze", record[1], recordLine, rowNumber);

        /**
         * Prüfung Spalte Ausstattung (Index 2)
         */
        if (record.length == 3) {
            if (record[2] != null && record[2].length() > 2) {
                InputValidator.validateEquipment("Austattung", record[2], recordLine, rowNumber);
            }

        }

        /**
         * Datensatz zurückgeben
         */
        return record;
    }

}
