package osjava.tl3.logic.planning.strategies.helpers;

import java.util.Comparator;
import osjava.tl3.model.Course;

/**
 * Eine Hilfsklasse für die Sortierung von Kursen anhand der Anzahl der von
 * teilnehmenden Studenten
 *
 * @author Meikel Bode
 */
public class CourseStudentsComparator implements Comparator<Course> {

    /**
     * Die Sortierungreihenfolge dieser Instanz
     */
    private final SortOrder sortOrder;

    /**
     * Erzeugt eine Instanz mit aufsteigender Sortierung nach Anzahl Studenten
     */
    public CourseStudentsComparator() {
        this.sortOrder = SortOrder.ASCENDING;
    }

    /**
     * Erzeugt eine Instanz mit gegebener Sortierreihenfolge
     *
     * @param sortOrder Die Reihenfolge nach der sortiert wird
     */
    public CourseStudentsComparator(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * Vergleicht Kurs1 und Kurs2 anhand der Anzahl der Studenten in
     * Abhängigkeit der konfigurierten Sortierreihenfolge
     *
     * @param course1 Kurs1
     * @param course2 Kurs2
     * @return Das Ergebnis des Vergleichs
     */
    @Override
    public int compare(Course course1, Course course2) {

        if (sortOrder == SortOrder.ASCENDING) {
            if (course1.getStudents() < course2.getStudents()) {
                return -1;
            } else if (course1.getStudents() == course2.getStudents()) {
                return 0;
            } else {
                return 1;
            }
        } else {
            if (course1.getStudents() < course2.getStudents()) {
                return 1;
            } else if (course1.getStudents() == course2.getStudents()) {
                return 0;
            } else {
                return -1;
            }
        }

    }

}