package osjava.tl3.logic.io;

import osjava.tl3.model.Schedule;
import osjava.tl3.model.ScheduleElement;

/**
 * Ein Ausgabeformatierer für Planinstanzen für das CSV-Format
 *
 * @author Fabian Simon
 */
public class CSVOutputFormatter extends OutputFormatter {

    /**
     * Liefert das Dateinamensuffix für CSV-Dateien
     *
     * @return Das Suffix (csv)
     */
    @Override
    public String getFileNameSuffix() {
        final String suffix = "csv";
        return suffix;
    }

    /**
     * Erzeugt für den eigegebenen Plan die CSV Ausgabe
     *
     * @param schedule Der auszugebende Plan
     * @param title Der Titel/Beschriftung des Plans
     * @return Die Repräsentation des Plans im Ausgabeformat
     */
    @Override
    public StringBuilder format(Schedule schedule, String title) {
        final String SEMICOLON = ";";
        final String NEW_LINE = "\n";
     
        StringBuilder sb = new StringBuilder();
        sb.append(title).append(NEW_LINE);
        
        switch (schedule.getType()){
            case ACADAMIC:
                final String DESCRIPTION_LINE_ACADEMIC = "Tag;Zeit;Kurs;Raum";
                
                // Kopfzeile ausgeben
                sb.append(DESCRIPTION_LINE_ACADEMIC).append(NEW_LINE);
                
                // Zeilen ausgeben
                String column1, column2, column3, column4;
                for (ScheduleElement scheduleElement : schedule.getScheduleElements()) {
           
                    column1 = scheduleElement.getCoordiate().getDay().toString();
                    column2 = scheduleElement.getCoordiate().getTimeSlot().toString();

                    if (scheduleElement.getCourse().getName() != null) {
                        column3 = scheduleElement.getCourse().getName();
                    } else {
                        column3 = "Keine Veranstaltung";
                    }

                    if (scheduleElement.getRoom().getName() != null) {
                        column4 = scheduleElement.getRoom().getName();
                    } else {
                        column4 = "Kein Raum";
                    }

                    sb.append(column1).append(SEMICOLON).append(column2).append(SEMICOLON).append(column3)
                            .append(SEMICOLON).append(column4).append(NEW_LINE);
                }
                
                break;
                
            case ROOM_EXTERNAL:
            case ROOM_INTERNAL:
                final String DESCRIPTION_LINE_ROOM = "Tag;Zeit;Kurs;Dozent";
                
                // Kopfzeile ausgeben
                sb.append(DESCRIPTION_LINE_ROOM).append(NEW_LINE);
                
                // Zeilen ausgeben
                String column5, column6, column7, column8;
                for (ScheduleElement scheduleElement : schedule.getScheduleElements()) {
           
                    column5 = scheduleElement.getCoordiate().getDay().toString();
                    column6 = scheduleElement.getCoordiate().getTimeSlot().toString();

                    if (scheduleElement.getCourse().getName() != null) {
                        column7 = scheduleElement.getCourse().getName();
                    } else {
                        column7 = "Keine Veranstaltung";
                    }

                    if (scheduleElement.getCourse().getAcademic().getName() != null) {
                        column8 = scheduleElement.getCourse().getAcademic().getName();
                    } else {
                        column8 = "Kein Dozent";
                    }

                    sb.append(column5).append(SEMICOLON).append(column6).append(SEMICOLON).append(column7)
                            .append(SEMICOLON).append(column8).append(NEW_LINE);
                }
                
                break;
                
            case STUDY_PROGRAM:
                final String DESCRIPTION_LINE_STUDYPROGRAM = "Tag;Zeit;Kurs;Raum;Dozent";
                
                // Kopfzeile ausgeben
                sb.append(DESCRIPTION_LINE_STUDYPROGRAM).append(NEW_LINE);
                
                // Zeilen ausgeben
                String column9, column10, column11, column12, column13;
                for (ScheduleElement scheduleElement : schedule.getScheduleElements()) {
           
                    column9 = scheduleElement.getCoordiate().getDay().toString();
                    column10 = scheduleElement.getCoordiate().getTimeSlot().toString();

                    if (scheduleElement.getCourse().getName() != null) {
                        column11 = scheduleElement.getCourse().getName();
                    } else {
                        column11 = "Keine Veranstaltung";
                    }
                    
                    if (scheduleElement.getRoom().getName() != null) {
                        column12 = scheduleElement.getRoom().getName();
                    } else {
                        column12 = "Kein Raum";
                    }

                    if (scheduleElement.getCourse().getAcademic().getName() != null) {
                        column13 = scheduleElement.getCourse().getAcademic().getName();
                    } else {
                        column13 = "Kein Dozent";
                    }

                    sb.append(column9).append(SEMICOLON).append(column10).append(SEMICOLON).append(column11)
                            .append(SEMICOLON).append(column12).append(SEMICOLON).append(column13).append(NEW_LINE);
                }
                
                break;
                
            default:
                break;
                
        }
        
        return sb;
    }

}