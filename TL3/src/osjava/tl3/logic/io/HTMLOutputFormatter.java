package osjava.tl3.logic.io;

import osjava.tl3.model.Schedule;
import osjava.tl3.model.ScheduleElement;
import osjava.tl3.model.ScheduleType;

/**
 * Ein Ausgabeformatierer für Planinstanzen für das HTML-Format
 *
 * @author Fabian Simon
 */
public class HTMLOutputFormatter extends OutputFormatter {

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

        sb.append("<html>");
        sb.append("<head>");

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

        int elementCount = 0;
        for (int i = 0; i < schedule.getScheduleElements().size(); i++) {

            if (elementCount == 4) {
                sb.append("</tr>");
                elementCount = 0;
                continue;
            }

            if (elementCount == 0) {
                sb.append("<tr>");
            }

            ScheduleElement scheduleElement = schedule.getScheduleElements().get(i);

            sb.append("<td>");
            sb.append("Kurs ").append(scheduleElement.getCourse().getNumber()).append(" (")
                    .append(scheduleElement.getCourse().getType().getName().equals("Uebung") ? "Übung" : "Vorlesung").append("):<br>");
            sb.append("<b>").append(scheduleElement.getCourse().getName()).append("</b><br>");
            sb.append("Raum: ").append(scheduleElement.getRoom().getName()).append("<br>");
            sb.append("Dozent: ").append(scheduleElement.getCourse().getAcademic().getName()).append("<br>");
            sb.append("Teilnehmer: ").append(scheduleElement.getCourse().getStudents());

            sb.append("</td>");

            elementCount++;
        }

        sb.append("</table>");

        sb.append("</body>");

        sb.append("</html>");

        return sb;
    }

    /**
     * Liefert das Dateinamensuffix für HTML-Dateien
     * @return Das Suffix (HTML)
     */
    @Override
    public String getFileNameSuffix() {
        final String suffix = "html";
        return suffix;
    }

}
