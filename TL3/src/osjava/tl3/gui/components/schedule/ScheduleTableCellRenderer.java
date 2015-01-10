package osjava.tl3.gui.components.schedule;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import osjava.tl3.model.StudyProgram;
import osjava.tl3.model.schedule.TimeSlot;
import osjava.tl3.model.schedule.ScheduleAppointment;
import osjava.tl3.model.schedule.ScheduleElementViewWrapper;
import osjava.tl3.model.schedule.ScheduleViewStudyProgram;

/**
 * Dieser Renderer kann Instanzen der Klassen ScheduleElementImpl und TimeSlot
 * ausgeben. Die Klasse erweitert die Klasse DefaultTableCellRenderer
 *
 * @author Meikel Bode
 */
public class ScheduleTableCellRenderer extends DefaultTableCellRenderer {

    private final Color colorFree = Color.WHITE;
    private final Color colorTimeSlot = new Color(212, 212, 212);

    /**
     * Erzeugt eine neue Instanz des Renderers
     */
    public ScheduleTableCellRenderer() {
        super();
        setBorder(new LineBorder(Color.DARK_GRAY, 1));
        setFont(new Font("Helvetica", Font.PLAIN, 12));
    }

    /**
     * Erzeugt eine Instanz von JLabel, die als darstellende Komponente der
     * zugewiesenen Instanz von JTable an der Koordinate (row, column) verwendet
     * werden soll. Dieser Renderer kann Instanzen von ScheduleElementImpl und
     * TimeSlot darstellen.
     *
     * @param table Die JTable auf der der Renderer operiert
     * @param value Der Wert des TableModels, der durch diesen Renderer
     * dargestellt werden soll
     * @param isSelected Ob die aktuelle Zelle der Tabelle aktuell selektiert
     * ist
     * @param hasFocus Ob die aktuelle Zelle der Tabelle aktuell den Fokus hält
     * @param row Der Index der Tabellenzeile
     * @param column Der Index der Tabellenspalte
     * @return Die Renderer Komponente (JLabel)
     */
    @Override
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row,
            int column) {

        if (value instanceof ScheduleElementViewWrapper) {
            ScheduleElementViewWrapper scheduleElement = (ScheduleElementViewWrapper) value;

            StudyProgram studyProgram = null;
            if (scheduleElement.getScheduleView() instanceof ScheduleViewStudyProgram) {
                studyProgram = ((ScheduleViewStudyProgram) scheduleElement.getScheduleView()).getStudyProgramm();
            }

            if (scheduleElement.isEmpty()) {
                setText("");
                setToolTipText("Nicht belegt.");
                setBackground(colorFree);
            } else {
                setVerticalAlignment(SwingConstants.TOP);
                setBackground(colorFree);
                StringBuilder sb = new StringBuilder();
                sb.append("<html>");
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
                    if (studyProgram != null) {
                        sb.append("<br/>").append(studyProgram.getSemesterByCourse(appointment.getCourse()));
                    }

                    sb.append("</div>");

                }
                sb.append("</html>");
                /**
                 * Tabellenwert zuweisen
                 */
                setText(sb.toString());

            }
        } else if (value instanceof TimeSlot) {
            setVerticalAlignment(SwingConstants.CENTER);
            setText(value.toString());
            setToolTipText("");
            setBackground(colorTimeSlot);

        } else {
            setToolTipText("Nicht belegt.");
            setText("");
            setBackground(colorFree);
        }
        return this;
    }

}
