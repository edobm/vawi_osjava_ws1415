package osjava.tl3.logic.planning.strategies.helpers;

import java.util.Comparator;
import osjava.tl3.model.Course;

/**
 *
 * @author Meikel Bode
 */
public class CourseStudentsComparator implements Comparator<Course> {

    public enum SortOrder {

        ASCENDING,
        DESCENDING
    }

    private SortOrder sortOrder = SortOrder.ASCENDING;

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
     *
     * @param o1
     * @param o2
     * @return
     */
    @Override
    public int compare(Course o1, Course o2) {

        if (sortOrder == SortOrder.ASCENDING) {
            if (o1.getStudents() < o2.getStudents()) {
                return -1;
            } else if (o1.getStudents() == o2.getStudents()) {
                return 0;
            } else {
                return 1;
            }
        } else {
            if (o1.getStudents() < o2.getStudents()) {
                return 1;
            } else if (o1.getStudents() == o2.getStudents()) {
                return 0;
            } else {
                return -1;
            }
        }

    }

    public static CourseStudentsComparator getInstance(SortOrder sortOrder) {
        return new CourseStudentsComparator(sortOrder);
    }

}
