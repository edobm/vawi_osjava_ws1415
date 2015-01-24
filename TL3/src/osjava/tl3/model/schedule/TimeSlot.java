package osjava.tl3.model.schedule;

/**
 * Enum Typ der die verfügbaren Startzeiten repräsentiert. Die Reihenfolge der
 * Slots wird über numerische Wertzuweisung erreicht. Es ergeben sich 5 Slots
 * pro Tag mit einer jeweiligen Dauer von 2 Stunden.
 *
 * @author Meikel Bode
 */
public enum TimeSlot {

    /**
     * Repräsentiert den Zeitraum 08:00 bis 10:00 Uhr
     */
    SLOT_0800(0),
    /**
     * Repräsentiert den Zeitraum 10:00 bis 12:00 Uhr
     */
    SLOT_1000(1),
    /**
     * Repräsentiert den Zeitraum 12:00 bis 14:00 Uhr
     */
    SLOT_1200(2),
    /**
     * Repräsentiert den Zeitraum 14:00 bis 16:00 Uhr
     */
    SLOT_1400(3),
    /**
     * Repräsentiert den Zeitraum 16:00 bis 18:00 Uhr
     */
    SLOT_1600(4);

    /**
     * Der Integerwert des Enum Wertes
     */
    public final int value;

    /**
     * Privater Konstruktur zur Erzeugung einer neuen Instanz
     *
     * @param value Der Integerwert des Enum Wertes
     */
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