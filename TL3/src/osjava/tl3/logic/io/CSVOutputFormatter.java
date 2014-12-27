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
     * Erzeugt für den eigegebenen Plan die CSV Ausgabe
     *
     * @param schedule Der auszugebende Plan
     * @param title Der Titel/Beschriftung des Plans
     * @return Die Repräsentation des Plans im Ausgabeformat
     */
    @Override
    public StringBuilder format(Schedule schedule, String title) {
        final String SEMICOLON = ";";
        final String DESCRIPTION_LINE = "Tag;Zeit;Kurs;Raum";
        final String NEW_LINE = "\n";
     
        StringBuilder sb = new StringBuilder();
        
        // Kopfzeile ausgeben
        sb.append(DESCRIPTION_LINE).append(NEW_LINE);
        
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
                column4 = "Kein Dozent";
            }

            sb.append(column1).append(SEMICOLON).append(column2).append(SEMICOLON).append(column3)
                    .append(SEMICOLON).append(column4).append(NEW_LINE);
        }
        
        return sb;
    }

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

}
