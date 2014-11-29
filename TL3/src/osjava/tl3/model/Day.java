package osjava.tl3.model;

/**
 * Dieser Enum Typ repräsentiert die Wochentage. Die Sortierbarkeit wird über
 * die Parametrisierung der Auspräungen mittels numerischer Wertzuweisung
 * möglich.
 *
 * @author Christoph Lurz
 * @version 1.0
 */
public enum Day {

    MONDAY(0), TUESDAY(1), WEDNESDAY(2), THURSDAY(3), FRIDAY(4), SATURDAY(5), SUNDAY(6);

    /**
     * The enums value
     */
    public final int value;

    private Day(int value) {
        this.value = value;
    }

    /**
     * Konvertiert den gegebene Integerwert in den entsprechend Enum Wert
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
