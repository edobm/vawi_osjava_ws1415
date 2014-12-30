package osjava.tl3.logic.io.output;

import static osjava.tl3.model.schedule.ScheduleType.ACADAMIC;
import static osjava.tl3.model.schedule.ScheduleType.ROOM_EXTERNAL;
import static osjava.tl3.model.schedule.ScheduleType.ROOM_INTERNAL;
import static osjava.tl3.model.schedule.ScheduleType.STUDY_PROGRAM;
import osjava.tl3.model.schedule.ScheduleView;
import osjava.tl3.model.schedule.ScheduleViewAcademic;
import osjava.tl3.model.schedule.ScheduleViewRoom;
import osjava.tl3.model.schedule.ScheduleViewSemester;
import osjava.tl3.model.schedule.ScheduleViewStudyProgram;

/**
 * Erzeugt OutputFileWriter
 * 
 * @author Fabian Simon
 */
public class OutputFileWriterFactory {
    
    /**
     * Liefert eine Instanz des angeforderderten FileWriters
     * @param scheduleView Die Plansicht für die ein Writer erzeugt werden soll
     * @return Der passende Writer
     */
    public static OutputFileWriter getInstance(ScheduleView scheduleView) {
        
        /**
         * In Abhängigkeit des gewünschten Ausgabeformats eine Instanz des 
         * passenden Formatierers erzeugen und zurückgeben
         */
        if (scheduleView instanceof ScheduleViewAcademic) {
            return new AcademicScheduleWriter();
        }
         else if (scheduleView instanceof ScheduleViewRoom) {
            return new RoomScheduleWriter();
        }
        else if (scheduleView instanceof ScheduleViewStudyProgram) {
            return new StudyProgramScheduleWriter();
        }
        else if (scheduleView instanceof ScheduleViewSemester) {
            return new SemesterScheduleWriter();
        }
        else {
            throw new IllegalArgumentException("Unbekannter Plansichttyp: " + scheduleView.getClass().getName());
        }
        
    }
}