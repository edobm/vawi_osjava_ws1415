package osjava.tl3.logic.io.output;

import osjava.tl3.logic.io.input.AcademicScheduleWriter;
import osjava.tl3.model.ScheduleType;

/**
 * Erzeugt OutputFileWriter
 * 
 * @author Fabian Simon
 */
public class OutputFileWriterFactory {
    
    /**
     * Liefert eine Instanz des angeforderderten FileWriters
     * @param scheduleType Der Typ des Writers
     * @return Der passende Writer
     */
    public static OutputFileWriter getInstance(ScheduleType scheduleType) {
        
        /**
         * In Abhängigkeit des gewünschten Ausgabeformats eine Instanz des 
         * passenden Formatierers erzeugen und zurückgeben
         */
        switch (scheduleType) {
            case ACADAMIC:
                return new AcademicScheduleWriter();
            case STUDY_PROGRAM:
                return new StudyProgramScheduleWriter();
            case ROOM_INTERNAL:
            case ROOM_EXTERNAL:
                return new RoomScheduleWriter();
            default:
                return null;
        }
    }
}