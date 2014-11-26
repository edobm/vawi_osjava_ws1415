package osjava.tl3.logic.planning.strategies.helpers;

import java.util.Comparator;
import osjava.tl3.model.Course;

/**
 *
 * @author Meikel Bode
 */
public class CourseStudentsComparator implements Comparator<Course>{

    private static CourseStudentsComparator singleton;
    
    @Override
    public int compare(Course o1, Course o2) {
        
        if (o1.getStudents() < o2.getStudents()) {
            return -1;
        }
        else if (o1.getStudents() == o2.getStudents()) {
            return 0;
        }
        else{
            return 1;
        }
        
    }
    public static CourseStudentsComparator getInstance() {
        if (singleton == null) {
            singleton = new CourseStudentsComparator();
        }
        return singleton;
    }
    
}
