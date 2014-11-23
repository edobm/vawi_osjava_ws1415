package osjava.tl3.model;
  
import java.util.List;

/**
 * Diese Klasse repräsentiert die Entität Raum.
 * 
 * Ein Raum hat eine Bezeichnung, ist von einem bestimmten Typ (intern oder extern) 
 * und zeichnet sich durch verfügbares Equipment und eine maximale Anzahl von Sitzplätzen aus.
 * 
 * Anhand des Raumtyps kann ermittelt werden, ob der Raum angemietet werden muss und welche
 * Kosten dadurch entstehen.
 * 
 * Unklar ist zu diesem Zeitpunkt, ob externe Räume über die Eingabedateien geliefert werden
 * oder ob externe Räume "unbegrenzt" zur Verfügung stehen. Zudem ist unklar, ob die Kosten eines
 * externen Raumes von der tatsächlichen Anzahl der Teilnehmer oder von der verfügbaren
 * Anzahl der Plätze abhängnt (letztes ist die Annahme).
 * 
 * @author Christoph Lurz
 * @version 1.0
 */
public class Room
{
   public static final int COST_PER_SEAT = 10; 
   
   private RoomType type;
   private String name;
   private List<Equipment> availableEquipments;
   private int seats;
   
   
   /**
    * Berechnet die Kosten eines Raumes, wenn dieser Extern angemietet werden muss
    * unter der Annahme, dass die Kosten von der Anzahl verfügbarer Sitzeplätze abhängt.
    */
   public int getCosts() {
       return type == RoomType.EXTERNAL ? seats * Room.COST_PER_SEAT : 0;
    }
   
}
