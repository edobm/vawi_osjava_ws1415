package osjava.tl3.logic.io.input;

import osjava.tl3.logic.io.output.OutputFormatter;
import osjava.tl3.model.Day;
import osjava.tl3.model.Schedule;
import osjava.tl3.model.ScheduleCoordinate;
import osjava.tl3.model.ScheduleElement;
import osjava.tl3.model.ScheduleType;
import osjava.tl3.model.TimeSlot;

/**
 * Ein Ausgabeformatierer für Planinstanzen für das HTML-Format
 *
 * @author Fabian Simon
 */
public class HTMLOutputFormatter extends OutputFormatter {

    /**
     * Liefert das Dateinamensuffix für HTML-Dateien
     *
     * @return Das Suffix (HTML)
     */
    @Override
    public String getFileNameSuffix() {
        final String suffix = "html";
        return suffix;
    }

    /**
     * Erzeugt für den eigegebenen Plan die HTML Ausgabe
     *
     * @param schedule Der auszugebende Plan
     * @param title Der Titel/Beschriftung des Plans
     * @return Die Repräsentation des Plans im Ausgabeformat
     */
    @Override
    public StringBuilder format(Schedule schedule, String title) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sbTitle = new StringBuilder();

        if (schedule.getType() == ScheduleType.ACADAMIC) {
            sbTitle.append("Dozentenplan: ").append(title);
        }
        if (schedule.getType() == ScheduleType.ROOM_INTERNAL) {
            sbTitle.append("Raumplan: ").append(title).append(" (intern)");
        }
        if (schedule.getType() == ScheduleType.ROOM_EXTERNAL) {
            sbTitle.append("Raumplan: ").append(title).append(" (extern)");
        }
        if (schedule.getType() == ScheduleType.STUDY_PROGRAM) {
            sbTitle.append("Studiengangsplan: ").append(title);
        }
        
        sb.append("<!DOCTYPE html>");
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>");
        sb.append("<style>");
        sb.append("table, th, td { border: 1px solid black; border-collapse: collapse; } ");
        sb.append("th, td { padding: 5px; text-align: left; }");
        sb.append("</style>");

        sb.append("<title>");
        sb.append(sbTitle);
        sb.append("</title>");
        sb.append("</head>");

        sb.append("<body>");

        sb.append("<table style=\"width:100%\">");
        sb.append("<caption>").append(sbTitle).append("</caption>");
        sb.append("<tr><th>Zeit</th><th>Montag</th><th>Dienstag</th><th>Mittwoch</th><th>Donnerstag</th><th>Freitag</th></tr>");

        for (int timeslot = 0; timeslot < 5; timeslot++) {
            sb.append("<tr>");

            sb.append("<td>");
            sb.append(TimeSlot.valueOf(timeslot));
            sb.append("</td>");

            ScheduleCoordinate scheduleCoordinate;
            ScheduleElement scheduleElement;
            for (int day = 0; day < 5; day++) {
                scheduleCoordinate = new ScheduleCoordinate(Day.valueOf(day), TimeSlot.valueOf(timeslot));
                scheduleElement = schedule.getScheduleElement(scheduleCoordinate);

                sb.append("<td>");
                if (scheduleElement.isBlocked()) {
                    sb.append("Kurs ");
                    sb.append(scheduleElement.getCourse().getNumber()).append(" (")
                            .append(scheduleElement.getCourse().getType().getName().equals("Uebung") ? "Übung" : "Vorlesung").append("):<br/>");
                    sb.append("<b>").append(scheduleElement.getCourse().getName()).append("</b><br/>");
                    sb.append("Raum: ").append(scheduleElement.getRoom().getName()).append("<br/>");
                    sb.append("Dozent: ").append(scheduleElement.getCourse().getAcademic().getName()).append("<br/>");
                    sb.append("Teilnehmer: ").append(scheduleElement.getCourse().getStudents());
                }
                sb.append("</td>");
            }

            sb.append("</tr>");
        }

        sb.append("</table>");

        sb.append("</body>");

        sb.append("</html>");

        return sb;
    }

}