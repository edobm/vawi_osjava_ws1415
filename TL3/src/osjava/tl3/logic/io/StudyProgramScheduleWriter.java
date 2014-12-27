package osjava.tl3.logic.io;

import osjava.tl3.model.Schedule;
import osjava.tl3.model.ScheduleType;

/**
 * Diese Klasse erzeugt die Pläne für Studiengang und Fachsemester
 *
 * @author Fabian Simon
 * @version 1.0
 */
public class StudyProgramScheduleWriter extends OutputFileWriter {

    @Override
    public void writeSchedule(Schedule schedule, OutputFormat outputFormat, String outputPath) {
        
        /**
         * Prefix für Ausgabedateien
         */
        final String fileNamePrefix = "Studiengangsplan_";
        
        /**
         * Prüfung der Eingabe
         */
        if (schedule == null) {
            throw new IllegalArgumentException("Given instance of Schedule must not be null!");
        }
        if (schedule.getType() != ScheduleType.STUDY_PROGRAM) {
            throw new IllegalArgumentException("Given instance of Schedule invalid. Type is: "
                    + schedule.getType() + ", expected: " + ScheduleType.STUDY_PROGRAM);
        }

        /**
         * Die Beschriftung des Studiengangplans ermitteln
         */
        String title = getPrimaryNameElement(schedule);

        /**
         * Ausgabe des Schedules an an Vaterklasse delegieren und spezifische
         * Beschriftung übergeben
         */
        writeSchedule(schedule, outputFormat, outputPath, fileNamePrefix, title);

    }

    @Override
    public String getPrimaryNameElement(Schedule schedule) {
        StringBuilder sb = new  StringBuilder();
        sb.append(schedule.getSemester().getStudyProgram().getName());
        sb.append(" ");
        sb.append(schedule.getSemester().getName());

        return sb.toString();
    }
}
