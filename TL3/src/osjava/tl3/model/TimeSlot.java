package osjava.tl3.model;


/**
 * Enum Typ der die verfügbaren Startzeiten repräsentiert.
 * Die Reihenfolge der Slots wird über numerische Wertzuweisung erreicht.
 * Es ergeben sich 5 Slots pro Tag mit einer jeweiligen Dauer von 2 Stunden.
 * 
 * @author Christoph Lurz
 * @version 1.0
 */
public enum TimeSlot
{
   SLOT_0800(0), SLOT_1000(1), SLOT_1200(2), SLOT_1400(3), SLOT_1600(4); 
   
   /**
     * The enums value
     */
   public final int value;
 
   private TimeSlot(int value)
    {
        this.value = value;
    }
}
