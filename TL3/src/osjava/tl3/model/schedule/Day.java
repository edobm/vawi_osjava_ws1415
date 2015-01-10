package osjava.tl3.model.schedule;

/**
 * Dieser Enum Typ repräsentiert die Wochentage. Die Sortierbarkeit wird über
 * die Parametrisierung der Ausprägungen mittels numerischer Wertzuweisung
 * möglich.
 *
 * @author Meikel Bode
 * @version 1.0
 */
public enum Day {

    /**
     * Montag
     */
    MONDAY(0), 
    
    /**
     * Dienstag
     */
    TUESDAY(1), 
    
    /**
     * Mittwoch
     */
    WEDNESDAY(2), 
    
    /**
     * Donnerstag
     */
    THURSDAY(3), 
    
    /**
     * Freitag
     */
    FRIDAY(4);

    /**
     * Der Integer Wert des jeweiligen Enum Wertes
     */
    public final int value;

    /**
     * Privater Konstruktor zur Zuweisung des Integer Wertes
     * @param value 
     */
    private Day(int value) {
        this.value = value;
    }

    /**
     * Konvertiert den gegebenen Integerwert in den entsprechend Enum Wert
     *
     * @param i Der Integer, der konvertiert werden soll
     * @return Die Wert
     */
    public static Day valueOf(int i) {
        if (i == MONDAY.value) {
            return MONDAY;
        } else if (i == TUESDAY.value) {
            return TUESDAY;
        } else if (i == WEDNESDAY.value) {
            return WEDNESDAY;
        } else if (i == THURSDAY.value) {
            return THURSDAY;
        } else if (i == FRIDAY.value) {
            return FRIDAY;
        } else {
            throw new IllegalArgumentException("Unbekannter Tag");
        }
    }
}
