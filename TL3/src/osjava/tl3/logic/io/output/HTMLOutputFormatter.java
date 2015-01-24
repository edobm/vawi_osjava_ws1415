package osjava.tl3.logic.io.output;

import osjava.tl3.model.RoomType;
import osjava.tl3.model.schedule.Day;
import osjava.tl3.model.schedule.ScheduleAppointment;
import osjava.tl3.model.schedule.ScheduleCoordinate;
import osjava.tl3.model.schedule.ScheduleElement;
import osjava.tl3.model.schedule.ScheduleView;
import osjava.tl3.model.schedule.ScheduleViewAcademic;
import osjava.tl3.model.schedule.ScheduleViewRoom;
import osjava.tl3.model.schedule.ScheduleViewSemester;
import osjava.tl3.model.schedule.ScheduleViewStudyProgram;
import osjava.tl3.model.schedule.TimeSlot;

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
     * @param scheduleView Der auszugebende Plan
     * @param title Der Titel/Beschriftung des Plans
     * @return Die Repräsentation des Plans im Ausgabeformat
     */
    @Override
    public StringBuilder format(ScheduleView scheduleView, String title) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sbTitle = new StringBuilder();
        
        ScheduleViewStudyProgram studyProgramView = null;
       
        if (scheduleView instanceof ScheduleViewAcademic) {
            sbTitle.append("Dozentenplan: ").append(title);
        }
        if (scheduleView instanceof ScheduleViewRoom) {
            ScheduleViewRoom viewRoom = (ScheduleViewRoom) scheduleView;
            sbTitle.append("Raumplan: ").append(title).append(viewRoom.getRoom().getType() == RoomType.INTERNAL ? " (intern)" : " (extern)");
        }
        if (scheduleView instanceof ScheduleViewSemester) {
            sbTitle.append("Fachsemesterplan: ");
        }
        if (scheduleView instanceof ScheduleViewStudyProgram) {
            sbTitle.append("Studiengangsplan: ").append(title);
            studyProgramView = (ScheduleViewStudyProgram)scheduleView;
        }

        sb.append("<!DOCTYPE html>");
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/>");
        sb.append("<style>");
        sb.append("table, th, td { border: 1px solid black; border-collapse: collapse; } ");
        sb.append("th, td { padding: 5px; text-align: left; }");
        sb.append("td.appointment { vertical-align: top; }");
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
                scheduleElement = scheduleView.getScheduleElement(scheduleCoordinate);

                sb.append("<td class=\"appointment\">");

                for (ScheduleAppointment appointment : scheduleElement.getAppointments()) {
                    sb.append("<div style=\" margin: 3px; padding: 3px; word-wrap: break-word; overflow-x: auto; vertical-align: top;");
                    sb.append("border: 2px solid #cccccc; ");

                    if (appointment.getCourse().getType().getName().equals("Uebung")) {
                        sb.append("background-color: #99eeaa; ");
                    } else {
                        sb.append("background-color: #99ddff; ");
                    }
                    sb.append("\">");
                    sb.append("Kurs ");
                    sb.append(appointment.getCourse().getNumber()).append(" (")
                            .append(appointment.getCourse().getType().getName().equals("Uebung") ? "Übung" : "Vorlesung").append("):<br/>");
                    sb.append("<b>").append(appointment.getCourse().getName()).append("</b><br/>");
                    sb.append("Raum: ").append(appointment.getRoom().getName()).append("<br/>");
                    sb.append("Dozent: ").append(appointment.getCourse().getAcademic().getName()).append("<br/>");
                    sb.append("Teilnehmer: ").append(appointment.getCourse().getStudents());
                    /**
                     * Semester ausgeben
                     */
                    if (studyProgramView != null) {
                         sb.append("<br/>").append(studyProgramView.getStudyProgramm().getSemesterByCourse(appointment.getCourse()));
                    }
                    
                    sb.append("</div>");
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