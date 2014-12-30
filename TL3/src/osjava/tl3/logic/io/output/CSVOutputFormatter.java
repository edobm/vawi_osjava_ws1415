package osjava.tl3.logic.io.output;

import osjava.tl3.model.schedule.ScheduleAppointment;
import osjava.tl3.model.schedule.ScheduleElement;
import osjava.tl3.model.schedule.ScheduleElementNew;
import osjava.tl3.model.schedule.ScheduleView;
import osjava.tl3.model.schedule.ScheduleViewAcademic;
import osjava.tl3.model.schedule.ScheduleViewRoom;
import osjava.tl3.model.schedule.ScheduleViewSemester;
import osjava.tl3.model.schedule.ScheduleViewStudyProgram;

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
     * @param scheduleView Die auszugebende Plansicht
     * @param title Der Titel/Beschriftung des Plans
     * @return Die Repräsentation des Plans im Ausgabeformat
     */
    @Override
    public StringBuilder format(ScheduleView scheduleView, String title) {
        final String SEMICOLON = ";";
        final String DESCRIPTION_LINE_ACADEMIC = "Tag;Zeit;Kurs;Raum";
        final String DESCRIPTION_LINE_ROOM = "Tag;Zeit;Kurs;Dozent";
        final String DESCRIPTION_LINE_STUDYPROGRAM = "Tag;Zeit;Kurs;Raum;Dozent";
        final String NEW_LINE = "\n";

        StringBuilder sb = new StringBuilder();
        sb.append(title).append(NEW_LINE);

        if (scheduleView instanceof ScheduleViewAcademic) {

            // Typkonvertierung
            ScheduleViewAcademic view = (ScheduleViewAcademic) scheduleView;

            // Kopfzeile ausgeben
            sb.append(DESCRIPTION_LINE_ACADEMIC).append(NEW_LINE);

            // Zeilen ausgeben
            String column1, column2, column3, column4;
            for (ScheduleElementNew scheduleElement : scheduleView.getScheduleElements()) {

                column1 = scheduleElement.getCoordiate().getDay().toString();
                column2 = scheduleElement.getCoordiate().getTimeSlot().toString();

                if (scheduleElement.isEmpty()) {
                    column3 = "Keine Veranstaltung";
                    column4 = "Kein Raum";
                } else {
                    column3 = scheduleElement.getAppointments().get(0).getCourse().getName();
                    column4 = scheduleElement.getAppointments().get(0).getRoom().getName();
                }

                sb.append(column1).append(SEMICOLON).append(column2).append(SEMICOLON).append(column3)
                        .append(SEMICOLON).append(column4).append(NEW_LINE);
            }

        }

        if (scheduleView instanceof ScheduleViewRoom) {

            // Typkonvertierung
            ScheduleViewRoom view = (ScheduleViewRoom) scheduleView;

            // Kopfzeile ausgeben
            sb.append(DESCRIPTION_LINE_ROOM).append(NEW_LINE);

            // Zeilen ausgeben
            String column1, column2, column3, column4;
            for (ScheduleElementNew scheduleElement : scheduleView.getScheduleElements()) {

                column1 = scheduleElement.getCoordiate().getDay().toString();
                column2 = scheduleElement.getCoordiate().getTimeSlot().toString();

                if (scheduleElement.isEmpty()) {
                    column3 = "Keine Veranstaltung";
                    column4 = "Kein Dozent";
                } else {
                    column3 = scheduleElement.getAppointments().get(0).getCourse().getName();
                    column4 = scheduleElement.getAppointments().get(0).getCourse().getAcademic().getName();
                }

                sb.append(column1).append(SEMICOLON).append(column2).append(SEMICOLON).append(column3)
                        .append(SEMICOLON).append(column4).append(NEW_LINE);
            }

        }

        if (scheduleView instanceof ScheduleViewStudyProgram 
                || scheduleView instanceof ScheduleViewSemester) {

            // Typkonvertierung
            ScheduleViewSemester view = (ScheduleViewSemester) scheduleView;

            // Kopfzeile ausgeben
            sb.append(DESCRIPTION_LINE_STUDYPROGRAM).append(NEW_LINE);

            // Zeilen ausgeben
            String column1, column2, column3, column4, column5;
            for (ScheduleElementNew scheduleElement : scheduleView.getScheduleElements()) {

                column1 = scheduleElement.getCoordiate().getDay().toString();
                column2 = scheduleElement.getCoordiate().getTimeSlot().toString();

                if (scheduleElement.isEmpty()) {
                    column3 = "Keine Veranstaltung";
                    column4 = "Kein Raum";
                    column5 = "Kein Dozent";
                    
                    sb.append(column1).append(SEMICOLON).append(column2).append(SEMICOLON).append(column3)
                            .append(SEMICOLON).append(column4).append(SEMICOLON).append(column5).append(NEW_LINE);
                } else {
                    for (ScheduleAppointment appointment : scheduleElement.getAppointments()) {
                        column3 = appointment.getCourse().getName();
                        column4 = appointment.getRoom().getName();
                        column5 = appointment.getCourse().getAcademic().getName();
                        sb.append(column1).append(SEMICOLON).append(column2).append(SEMICOLON).append(column3)
                                .append(SEMICOLON).append(column4).append(SEMICOLON).append(column5).append(NEW_LINE);
                    }
                }

            }

        }

        return sb;
    }

}