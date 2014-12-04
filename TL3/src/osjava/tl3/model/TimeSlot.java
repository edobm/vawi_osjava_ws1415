package osjava.tl3.model;

/**
 * Enum Typ der die verfügbaren Startzeiten repräsentiert. Die Reihenfolge der
 * Slots wird über numerische Wertzuweisung erreicht. Es ergeben sich 5 Slots
 * pro Tag mit einer jeweiligen Dauer von 2 Stunden.
 *
 * @author Christoph Lurz
 * @version 1.0
 */
public enum TimeSlot {

    SLOT_0800(0), SLOT_1000(1), SLOT_1200(2), SLOT_1400(3), SLOT_1600(4);

    /**
     * The enums value
     */
    public final int value;

    private TimeSlot(int value) {
        this.value = value;
    }

    /**
     * Konvertiert den gegebene Integerwert in den entsprechend Enum Wert
     *
     * @param i Der Integer, der konvertiert werden soll
     * @return Die Wert
     */
    public static TimeSlot valueOf(int i) {
        if (i == SLOT_0800.value) {
            return SLOT_0800;
        } else if (i == SLOT_1000.value) {
            return SLOT_1000;
        } else if (i == SLOT_1200.value) {
            return SLOT_1200;
        } else if (i == SLOT_1400.value) {
            return SLOT_1400;
        } else if (i == SLOT_1600.value) {
            return SLOT_1600;
        } else {
            throw new IllegalArgumentException("Unbekannter TimeSlot");
        }
    }

    /**
     * Konvertiert den Zeitrahmen in eine natürliche Ausgabe
     *
     * @return Der konvertierte Wert
     */
    @Override
    public String toString() {
        if (value == SLOT_0800.value) {
            return "08:00-10:00";
        } else if (value == SLOT_1000.value) {
            return "10:00-12:00";
        } else if (value == SLOT_1200.value) {
            return "12:00-14:00";
        } else if (value == SLOT_1400.value) {
            return "14:00-16:00";
        } else {
            return "16:00-18:00";
        }
    }

}
